/*
 * Music Implant SPD, ported into Onyx's Pixel Dungeon (key musicimplant)
 * https://github.com/CatAzreal/Jinkeloid-MISPD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.MusicImplant;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

//Music Implant's satiation, which takes the place of hunger while its game mode is on.
//Seven stages from full to starving. A well fed hero is stronger and heals faster, a hungry one
//hits softer, and a starving one loses health faster the longer it lasts.
public class Satiation extends Buff implements com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero.Doom {

	{
		type = buffType.NEUTRAL;
		revivePersists = false;
	}

	private static final float STEP = 1f;

	public static final float SATIATED  = 600f;
	public static final float SATISFIED = 500f;
	public static final float STABLE    = 400f;
	public static final float PECKISH   = 200f;
	public static final float HUNGRY    = 100f;
	public static final float STARVING  = 0f;
	private static final float FULL_decay      = 5f;
	private static final float SATIATED_decay  = 3f;
	private static final float SATISFIED_decay = 1.5f;
	private static final float PECKISH_decay   = 0.75f;
	private static final float HUNGRY_decay    = 0.4f;

	private float level = 300f;
	private float partialDamage;

	private static final String LEVEL         = "level";
	private static final String PARTIALDAMAGE = "partialDamage";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put(LEVEL, level);
		bundle.put(PARTIALDAMAGE, partialDamage);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		level = bundle.getFloat(LEVEL);
		partialDamage = bundle.getFloat(PARTIALDAMAGE);
	}

	public int satiation(){
		return (int)Math.ceil(level);
	}

	private int stageIndex(){
		if (level <= STARVING) return 0;
		if (level <= HUNGRY) return 1;
		if (level <= PECKISH) return 2;
		if (level <= STABLE) return 3;
		if (level <= SATISFIED) return 4;
		if (level <= SATIATED) return 5;
		return 6;
	}

	private static final String[] STAGES = {"starving", "hungry", "peckish", "stable", "satisfied", "satiated", "full"};

	@Override
	public boolean act() {
		if (!(target instanceof Hero) || !target.isAlive()){
			diactivate();
			return true;
		}
		Hero hero = (Hero) target;
		int before = stageIndex();

		if (Dungeon.level != null && Dungeon.level.locked || Dungeon.godMode()
				|| target.buff(WellFed.class) != null){
			spend(STEP);
			return true;
		}

		boolean abstinence = MusicImplant.perk("abstinence");
		if (level <= STARVING){
			level -= STEP / 2;
			//every point under the line adds 0.1% of max health a turn, half that with abstinence
			partialDamage += (1 - level / 10) * target.HT / (abstinence ? 2000f : 1000f);
			if (partialDamage > 1){
				target.damage((int)partialDamage, this);
				partialDamage -= (int)partialDamage;
			}
		} else {
			float step = STEP;
			switch (before){
				case 6: step *= FULL_decay; break;
				case 5: step *= SATIATED_decay; break;
				case 4: step *= SATISFIED_decay; break;
				case 3: break;
				case 2: step *= PECKISH_decay; break;
				default: step *= HUNGRY_decay; break;
			}
			step *= MusicImplant.hungerRate();
			float newLevel = level - step;
			if (newLevel <= HUNGRY && level > HUNGRY){
				GLog.n(Messages.get(this, "onhungry"));
			} else if (newLevel <= STARVING){
				GLog.w(Messages.get(this, "onstarving"));
				hero.resting = false;
				hero.interrupt();
			}
			level = newLevel;
		}
		if (stageIndex() != before){
			hero.updateHT(false);
			BuffIndicator.refreshHero();
		}
		spend(target.buff(Shadows.class) == null ? STEP : STEP * 1.5f);
		return true;
	}

	public void affect( float energy ){
		HornOfPlenty.hornRecharge horn = target.buff(HornOfPlenty.hornRecharge.class);
		if (energy > 0 && horn != null && horn.isCursed()){
			energy *= 0.67f;
			GLog.n(Messages.get(this, "cursedhorn"));
		}
		if (energy < 0 && target.buff(WellFed.class) != null){
			target.buff(WellFed.class).left += energy;
			BuffIndicator.refreshHero();
			return;
		}
		int before = stageIndex();
		//a starving hero who eats anything is back to plain hunger
		if (level < 0 && energy > 0) level = energy;
		else level += energy;
		if (stageIndex() != before && target instanceof Hero) ((Hero) target).updateHT(false);
		BuffIndicator.refreshHero();
	}

	private static Satiation of( Hero hero ){
		if (hero == null || !MusicImplant.satiationOn()) return null;
		return hero.buff(Satiation.class);
	}

	private static int stage( Hero hero ){
		Satiation s = of(hero);
		return s == null ? 3 : s.stageIndex();
	}

	public static boolean isStarving( Hero hero ){ return stage(hero) == 0; }
	public static boolean isHungry( Hero hero ){ return stage(hero) == 1; }
	public static boolean isPeckish( Hero hero ){ return stage(hero) == 2; }
	public static boolean isSatisfied( Hero hero ){ return stage(hero) == 4; }
	public static boolean isSatiated( Hero hero ){ return stage(hero) == 5; }
	public static boolean isFull( Hero hero ){ return stage(hero) == 6; }

	public static int strBonus( Hero hero ){
		switch (stage(hero)){
			case 6: return 2;
			case 5: case 4: return 1;
			case 0: return MusicImplant.perk("abstinence") ? 0 : -1;
			default: return 0;
		}
	}

	public static float speedFactor( Hero hero ){
		switch (stage(hero)){
			case 0: return MusicImplant.perk("abstinence") ? 1f : 0.85f;
			case 6: return 0.9f;
			default: return 1f;
		}
	}

	public static float hpFactor( Hero hero ){
		switch (stage(hero)){
			case 1: return MusicImplant.perk("abstinence") ? 1f : 0.9f;
			case 0: return MusicImplant.perk("abstinence") ? 1f : 0.8f;
			default: return 1f;
		}
	}

	public static float damageFactor( Hero hero ){
		boolean ab = MusicImplant.perk("abstinence");
		switch (stage(hero)){
			case 0: return ab ? 0.85f : 0.75f;
			case 1: return ab ? 0.90f : 0.85f;
			case 2: return ab ? 0.95f : 0.9f;
			default: return 1f;
		}
	}

	//a factor on the turns between natural heals
	public static float regenFactor( Hero hero ){
		switch (stage(hero)){
			case 6: return 0.1f;
			case 5: return 0.2f;
			case 4: return 0.5f;
			case 1: return 2f;
			default: return 1f;
		}
	}

	private int turnsToNext(){
		float t;
		switch (stageIndex()){
			case 6: t = (level - SATIATED) / FULL_decay; break;
			case 5: t = (level - SATISFIED) / SATIATED_decay; break;
			case 4: t = (level - STABLE) / SATISFIED_decay; break;
			case 3: t = (level - PECKISH); break;
			case 2: t = (level - HUNGRY) / PECKISH_decay; break;
			case 1: t = level / HUNGRY_decay; break;
			default: return 0;
		}
		t /= MusicImplant.hungerRate();
		return (int)Math.ceil(t);
	}

	@Override
	public int icon() {
		switch (stageIndex()){
			case 6: return BuffIndicator.OVERSAT;
			case 5: return BuffIndicator.FULLYSAT;
			case 4: return BuffIndicator.MEDIUMSAT;
			case 3: return BuffIndicator.MILDYSAT;
			case 2: case 1: return BuffIndicator.HUNGER;
			default: return BuffIndicator.STARVATION;
		}
	}

	@Override
	public float iconFadePercent() {
		switch (stageIndex()){
			case 5: return 1 - (level - SATISFIED) / (SATIATED - SATISFIED);
			case 4: return 1 - (level - STABLE) / (SATISFIED - STABLE);
			case 3: return 1 - (level - PECKISH) / (STABLE - PECKISH);
			case 2: return 1 - (level - HUNGRY) / (PECKISH - HUNGRY);
			case 1: return 1 - level / HUNGRY;
			default: return 0;
		}
	}

	@Override
	public String name() {
		return Messages.get(this, STAGES[stageIndex()]);
	}

	@Override
	public String desc() {
		String s = STAGES[stageIndex()];
		boolean ab = MusicImplant.perk("abstinence") && stageIndex() <= 2;
		String result = Messages.get(this, "desc_intro_" + s + (ab ? "_ab" : ""));
		if (stageIndex() > 0) result += "\n\n" + Messages.get(this, "desc_turncount_" + s, turnsToNext());
		result += "\n\n" + Messages.get(this, "desc");
		return result;
	}

	@Override
	public void onDeath() {
		Dungeon.fail(this);
		GLog.n(Messages.get(this, "ondeath"));
	}
}
