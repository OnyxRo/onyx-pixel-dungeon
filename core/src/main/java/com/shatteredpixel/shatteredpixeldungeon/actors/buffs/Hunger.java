/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfChallenge;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.SaltCube;
import com.shatteredpixel.shatteredpixeldungeon.journal.Document;
import com.shatteredpixel.shatteredpixeldungeon.levels.VaultLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

public class Hunger extends Buff implements Hero.Doom {

	public static final float HUNGRY	= 300f;
	public static final float STARVING	= 450f;

	//Matt Edition, the Sin of Gluttony makes hunger arrive sooner
	public float hungryThreshold() {
		return Math.max(HUNGRY / com.shatteredpixel.shatteredpixeldungeon.SinEffects.gluttonyHungerFactor(), 100f);
	}

	private float level;
	private float partialDamage;

	private static final String LEVEL			= "level";
	private static final String PARTIALDAMAGE 	= "partialDamage";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put( LEVEL, level );
		bundle.put( PARTIALDAMAGE, partialDamage );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		level = bundle.getFloat( LEVEL );
		partialDamage = bundle.getFloat(PARTIALDAMAGE);
	}

	@Override
	public boolean act() {
		if (com.shatteredpixel.shatteredpixeldungeon.TooCruel.steelBody()) { diactivate(); return true; } //toocruel, Certainty of Steel
		if (com.shatteredpixel.shatteredpixeldungeon.MusicImplant.satiationOn() && target instanceof Hero) { level = 0; spend(TICK); return true; } //musicimplant, satiation takes over
		if (com.shatteredpixel.shatteredpixeldungeon.LoreGoo.noHunger(target)) { spend(TICK); return true; } //lore, the Goo never hungers

		//the glowing lake in Transformation's surface town (the boss crawl's village) heals 10 a turn and keeps hunger away
		if (target instanceof Hero && target.isAlive()
				&& com.shatteredpixel.shatteredpixeldungeon.levels.SurfaceTownLevel.inHealingPool( target.pos )) {
			int heal = Math.min( 10, target.HT - target.HP );
			if (heal > 0) {
				target.HP += heal;
				if (target.sprite != null) target.sprite.showStatusWithIcon( com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite.POSITIVE,
						Integer.toString( heal ), com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText.HEALING );
			}
			spend(TICK);
			return true;
		}

		if (Dungeon.level.locked
				|| target.buff(WellFed.class) != null
				|| SPDSettings.intro()
				|| Dungeon.godMode()
				|| target.buff(ScrollOfChallenge.ChallengeArena.class) != null
				//rearranged, the Specialist does not get hungry while hidden with Stealth Master
				|| (Dungeon.hero != null && Dungeon.hero.pointsInTalent(com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent.STEALTH_MASTER) > 2 && Dungeon.hero.buff(Cloaking.class) != null)){
			spend(TICK);
			return true;
		}

		if (target.isAlive() && target instanceof Hero) {

			Hero hero = (Hero)target;

			if (isStarving()) {

				partialDamage += target.HT/1000f;

				if (partialDamage > 1){
					//transformation, Chocolate Coins, Unbear Hunger and the hunger curses
					if (!com.shatteredpixel.shatteredpixeldungeon.actors.hero.TransformationTalents.onStarveDamage(hero)) {
						target.damage( com.shatteredpixel.shatteredpixeldungeon.actors.hero.TransformationTalents.starveDamage(hero, (int)partialDamage), this);
					}
					partialDamage -= (int)partialDamage;
				}
				
			} else {

				float hungerDelay = 1f;
				if (target.buff(Shadows.class) != null){
					hungerDelay *= 1.5f;
				}
				hungerDelay /= SaltCube.hungerGainMultiplier();
				if (target instanceof Hero && ((Hero) target).hasTalent(com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent.NATURAL_AFFINITY)){
					hungerDelay *= 1 + ((Hero) target).pointsInTalent(com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent.NATURAL_AFFINITY) * 0.25f;
				}
				hungerDelay *= com.shatteredpixel.shatteredpixeldungeon.items.helmets.Helmet.hungerDelay(target);

				hungerDelay /= com.shatteredpixel.shatteredpixeldungeon.MusicImplant.hungerRate(); //musicimplant, on diet and big stomach
				float newLevel = level + (1f/hungerDelay) * com.shatteredpixel.shatteredpixeldungeon.actors.hero.TransformationTalents.hungerRate(hero); //transformation, Eater
				//Shittened PD's Acceleration and Lavage make the stomach run
				if (target.buff(Acceleration.class) != null) newLevel += 4f / hungerDelay;
				if (target.buff(Lavage.class) != null) newLevel += 20f / hungerDelay;
				if (newLevel >= STARVING) {

					GLog.n( Messages.get(this, "onstarving") );
					if (!com.shatteredpixel.shatteredpixeldungeon.actors.hero.TransformationTalents.onStarveDamage(hero)) hero.damage( com.shatteredpixel.shatteredpixeldungeon.actors.hero.TransformationTalents.starveDamage(hero, 1), this );
					if (hero.heroClass == com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass.FRIAR) Reason.loseReason(target, 2);

					hero.interrupt();
					newLevel = STARVING;

				} else if (newLevel >= hungryThreshold() && level < hungryThreshold()) {

					GLog.w( Messages.get(this, "onhungry") );

					if (!Document.ADVENTURERS_GUIDE.isPageRead(Document.GUIDE_FOOD)){
						GameScene.flashForDocument(Document.ADVENTURERS_GUIDE, Document.GUIDE_FOOD);
					}

				}
				level = newLevel;

			}
			
			spend( TICK );

		} else {

			diactivate();

		}

		return true;
	}

	//Magic Ling PD, the Crumb eats at the hero's food
	public void damgeExtraHungry( int amount ) {
		affectHunger( -amount );
	}

	public void satisfy( float energy ) {
		affectHunger( energy, false );
	}

	//Transformation PD's Hunger Knight drains satiety, the result is the share that came out of the red (starving) part
	public float drainForHungerKnight(float amount) {
		if (amount <= 0) return 0f;
		if (target.buff(WellFed.class) != null) {
			affectHunger(-amount);
			return 0f;
		}
		float oldLevel = level;
		affectHunger(-amount, true);
		return Math.max(0f, Math.min(1f, (oldLevel + amount - STARVING) / amount));
	}

	//summoning, Summoning PD changes the hero's hunger from anywhere, positive energy feeds
	public static void adjustHunger(float energy){
		if (Dungeon.hero == null) return;
		Buff.affect(Dungeon.hero, Hunger.class).affectHunger(energy);
	}

	public void affectHunger(float energy ){
		affectHunger( energy, false );
	}

	public void affectHunger(float energy, boolean overrideLimits ) {
		if (com.shatteredpixel.shatteredpixeldungeon.TooCruel.steelBody()) return; //toocruel
		if (com.shatteredpixel.shatteredpixeldungeon.MusicImplant.feedSatiation(target, energy)) return; //musicimplant

		if (energy < 0 && target.buff(WellFed.class) != null){
			target.buff(WellFed.class).left += energy;
			BuffIndicator.refreshHero();
			return;
		}

		float oldLevel = level;

		energy = com.shatteredpixel.shatteredpixeldungeon.actors.hero.XunTalents.hungerEnergy(energy); //xun, Eat Little and Often
		level -= energy;
		if (level < 0 && !overrideLimits) {
			level = com.shatteredpixel.shatteredpixeldungeon.actors.hero.XunTalents.hungerFloor(level); //xun
		} else if (level > STARVING) {
			float excess = level - STARVING;
			level = STARVING;
			partialDamage += excess * (target.HT/1000f);
			if (partialDamage > 1f){
				target.damage( (int)partialDamage, this );
				partialDamage -= (int)partialDamage;
			}
		}

		if (oldLevel < hungryThreshold() && level >= hungryThreshold()){
			GLog.w( Messages.get(this, "onhungry") );
		} else if (oldLevel < STARVING && level >= STARVING){
			GLog.n( Messages.get(this, "onstarving") );
			target.damage( 1, this );
			if (target instanceof Hero && ((Hero) target).heroClass == com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass.FRIAR) Reason.loseReason(target, 2);
		}

		BuffIndicator.refreshHero();
	}

	public boolean isHungry() {
		return level >= hungryThreshold();
	}

	public boolean isStarving() {
		return level >= STARVING;
	}

	public int hunger() {
		return (int)Math.ceil(level);
	}

	@Override
	public int icon() {
		if (level < hungryThreshold()) {
			return BuffIndicator.NONE;
		} else if (level < STARVING) {
			return BuffIndicator.HUNGER;
		} else {
			return BuffIndicator.STARVATION;
		}
	}

	@Override
	public String name() {
		if (level < STARVING) {
			return Messages.get(this, "hungry");
		} else {
			return Messages.get(this, "starving");
		}
	}

	@Override
	public String desc() {
		String result;
		if (level < STARVING) {
			result = Messages.get(this, "desc_intro_hungry");
		} else {
			result = Messages.get(this, "desc_intro_starving");
		}

		result += Messages.get(this, "desc");

		return result;
	}

	@Override
	public void onDeath() {

		Badges.validateDeathFromHunger();

		Dungeon.fail( this );
		GLog.n( Messages.get(this, "ondeath") );
	}
}
