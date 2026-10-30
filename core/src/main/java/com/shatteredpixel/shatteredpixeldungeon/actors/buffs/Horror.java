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
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

//Music Implant's horror. It grows while the hero explores in the dark, a light source slows or
//reverses it, and at high levels it costs accuracy and surprise attacks.
public class Horror extends Buff {

	{
		type = buffType.NEUTRAL;
		revivePersists = false;
	}

	private static final float STEP = 1f;

	public static final float FRIGHTENED = 25f;
	public static final float HORRIFIED  = 50f;
	public static final float TREMBLING  = 75f;
	public static final float MAX        = 99.99f;
	private static final float UNEASY_growth     = 0.1f;
	private static final float FRIGHTENED_growth = 0.07f;
	private static final float HORRIFIED_growth  = 0.05f;
	private static final float TREMBLING_growth  = 0.03f;

	private float level = 0;

	private static final String LEVEL = "level";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put(LEVEL, level);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		level = bundle.getFloat(LEVEL);
	}

	public float level(){
		return level;
	}

	public void set( float value ){
		level = Math.max(0, Math.min(MAX, value));
	}

	public void mod( float value ){
		set(level + value);
	}

	public static float level( Hero hero ){
		Horror h = hero == null ? null : hero.buff(Horror.class);
		return h == null ? 0 : h.level;
	}

	//the floor's region boss is already beaten
	public static boolean regionSecure(){
		if (Dungeon.depth <= 0 || Dungeon.depth > 25) return false;
		int bossFloor = ((Dungeon.depth - 1) / 5 + 1) * 5;
		return Statistics.deepestFloor > bossFloor;
	}

	@Override
	public boolean act() {
		if (!(target instanceof Hero) || !target.isAlive()){
			spend(STEP);
			return true;
		}
		Hero hero = (Hero) target;
		int light = LightsourceLight.intensity(hero);

		float step = STEP;
		if (level <= FRIGHTENED){
			switch (light){
				case 1: step *= 0.5f * UNEASY_growth; break;
				case 2: step *= 0; break;
				case 3: step *= -0.07f; break;
				default: step *= UNEASY_growth; break;
			}
		} else if (level <= HORRIFIED){
			switch (light){
				case 1: step *= 0; break;
				case 2: step *= -0.02f; break;
				case 3: step *= -0.08f; break;
				default: step *= FRIGHTENED_growth; break;
			}
		} else if (level <= TREMBLING){
			switch (light){
				case 1: step *= 0; break;
				case 2: step *= -0.05f; break;
				case 3: step *= -0.1f; break;
				default: step *= HORRIFIED_growth; break;
			}
		} else {
			switch (light){
				case 1: step *= -0.05f; break;
				case 2: step *= -0.07f; break;
				case 3: step *= -0.15f; break;
				default: step *= TREMBLING_growth; break;
			}
		}

		//a recent kill halts the growth
		if (step > 0 && hero.buff(FellEnemy.class) != null) step = 0;
		//a region whose boss is dead keeps horror below horrified
		boolean secure = regionSecure();
		if (secure && level > HORRIFIED - 0.5f) step = Math.min(step, 0);
		//a satiated hero does not grow afraid, a full one calms down
		if (Satiation.isSatiated(hero)) step = Math.min(step, 0);
		if (Satiation.isFull(hero)) step = Math.min(step, 0) - 0.05f;

		if (step > 0){
			if (MusicImplant.perk("nictophobia")) step *= 2;
			else if (MusicImplant.perk("confident") && hero.HP / (float)hero.HT > 0.90f) step *= 0.5f;
			else if (MusicImplant.perk("brave") && hero.HP / (float)hero.HT < 0.25f) step = 0;
			if (Satiation.isSatisfied(hero)) step *= 0.5f;
		}

		int before = (int)(level / 25);
		set(level + step);
		if (before != (int)(level / 25)) BuffIndicator.refreshHero();

		//the region and empty hand markers from the mod's status bar
		if (secure && hero.buff(RegionSecure.class) == null) Buff.affect(hero, RegionSecure.class);
		if (!secure && hero.buff(RegionSecure.class) != null) hero.buff(RegionSecure.class).detach();
		boolean empty = MusicImplant.emptyHand(hero);
		if (empty && hero.buff(Emptyoffhand.class) == null) Buff.affect(hero, Emptyoffhand.class);
		if (!empty && hero.buff(Emptyoffhand.class) != null) hero.buff(Emptyoffhand.class).detach();

		spend(STEP);
		return true;
	}

	public static float accPenalty( Hero hero ){
		float l = level(hero);
		return l > FRIGHTENED ? Math.min(l * 0.4f, 30) : 0;
	}

	public static float movBonus( Hero hero ){
		if (!MusicImplant.perk("adrenaline")) return 0;
		float l = level(hero);
		return l > FRIGHTENED ? Math.min(l * 0.15f, 15) : 0;
	}

	public static float evaBonus( Hero hero ){
		if (!MusicImplant.perk("adrenaline")) return 0;
		float l = level(hero);
		return l > HORRIFIED ? Math.min(l * 0.25f, 25) : 0;
	}

	public static float atkspeedBonus( Hero hero ){
		if (!MusicImplant.perk("adrenaline")) return 0;
		float l = level(hero);
		return l > TREMBLING ? Math.min((l - 70) * 0.5f, 15) : 0;
	}

	private String stage(){
		if (level <= FRIGHTENED) return "uneasy";
		if (level <= HORRIFIED) return "frightened";
		if (level <= TREMBLING) return "horrified";
		return "trembling";
	}

	@Override
	public int icon() {
		return BuffIndicator.TERROR;
	}

	@Override
	public void tintIcon( Image icon ) {
		if (level <= FRIGHTENED) icon.hardlight(0.6f, 0.6f, 0.6f);
		else if (level <= HORRIFIED) icon.hardlight(1f, 1f, 0f);
		else if (level <= TREMBLING) icon.hardlight(1f, 0.5f, 0f);
		else icon.hardlight(1f, 0f, 0f);
	}

	@Override
	public float iconFadePercent() {
		return Math.max(0, 1f - (level % 25f) / 25f);
	}

	@Override
	public String iconTextDisplay() {
		return Integer.toString((int)level);
	}

	@Override
	public String name() {
		return Messages.get(this, "title_" + stage());
	}

	@Override
	public String desc() {
		Hero hero = target instanceof Hero ? (Hero) target : Dungeon.hero;
		String s = stage();
		String desc = Messages.get(this, "desc_" + s);
		desc += "\n\n" + Messages.get(this, "level", (int)level);
		if (MusicImplant.perk("adrenaline") && !s.equals("uneasy")){
			desc += "\n\n" + Messages.get(this, "desc_" + s + "_positive",
					(int)movBonus(hero), (int)evaBonus(hero), (int)atkspeedBonus(hero));
		}
		desc += "\n\n" + Messages.get(this, "desc_" + s + "_generic", (int)accPenalty(hero));
		desc += "\n\n" + Messages.get(this, "desc_horror");
		return desc;
	}
}
