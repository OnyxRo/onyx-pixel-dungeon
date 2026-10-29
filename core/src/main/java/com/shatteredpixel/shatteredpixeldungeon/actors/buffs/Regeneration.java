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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.SpiritForm;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ChaliceOfBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.ChaoticCenser;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.SaltCube;
import com.shatteredpixel.shatteredpixeldungeon.levels.VaultLevel;
import com.watabou.utils.Bundle;

public class Regeneration extends Buff {

	//summoning, Summoning PD's instant heal with a number shown over the character
	public static void regenerate(com.shatteredpixel.shatteredpixeldungeon.actors.Char ch, int amount){
		regenerate(ch, amount, true, false);
	}

	public static void regenerate(com.shatteredpixel.shatteredpixeldungeon.actors.Char ch, int amount, boolean visible){
		regenerate(ch, amount, visible, false);
	}

	public static void regenerate(com.shatteredpixel.shatteredpixeldungeon.actors.Char ch, int amount, boolean visible, boolean showHPString){
		int healAmt = Math.min( amount, ch.HT - ch.HP );
		if (healAmt > 0 && ch.isAlive()) {
			ch.HP += healAmt;
			if (visible && ch.sprite != null){
				if (showHPString) ch.sprite.showStatus(com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite.POSITIVE, "+%dHP", healAmt);
				else ch.sprite.showStatus(com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite.POSITIVE, Integer.toString(healAmt));
			}
		}
	}
	
	{
		//unlike other buffs, this one acts after the hero and takes priority against other effects
		//healing is much more useful if you get some of it off before taking damage
		actPriority = HERO_PRIO - 1;
	}

	private float partialRegen = 0f;

	private static final float REGENERATION_DELAY = 10; //1HP every 10 turns
	
	@Override
	public boolean act() {
		if (com.shatteredpixel.shatteredpixeldungeon.TooCruel.steelBody()) { diactivate(); return true; } //toocruel, Certainty of Steel
		if (target.isAlive()) {

			//if other trinkets ever get buffs like this should probably make the buff attaching
			// behaviour more like wands/rings/artifacts
			if (ChaoticCenser.averageTurnsUntilGas() != -1){
				Buff.affect(Dungeon.hero, ChaoticCenser.CenserGasTracker.class);
			}

			if (regenOn() && target.HP < regencap() && com.shatteredpixel.shatteredpixeldungeon.LoreGoo.canRegen((Hero)target)) { //lore, the Goo heals while not arid
				boolean chaliceCursed = false;
				int chaliceLevel = -1;
				if (target.buff(MagicImmune.class) == null) {
					if (Dungeon.hero.buff(ChaliceOfBlood.chaliceRegen.class) != null) {
						chaliceCursed = Dungeon.hero.buff(ChaliceOfBlood.chaliceRegen.class).isCursed();
						chaliceLevel = Dungeon.hero.buff(ChaliceOfBlood.chaliceRegen.class).itemLevel();
					} else if (Dungeon.hero.buff(SpiritForm.SpiritFormBuff.class) != null
							&& Dungeon.hero.buff(SpiritForm.SpiritFormBuff.class).artifact() instanceof ChaliceOfBlood) {
						chaliceLevel = SpiritForm.artifactLevel();
					}
				}

				float delay = REGENERATION_DELAY;
				//Pixel Dungeon Reforged, one health every 7.5 turns on easy and 12.5 on impossible
				if (Dungeon.difficulty == 1) delay = 7.5f;
				else if (Dungeon.difficulty == 4) delay = 12.5f;
				if (chaliceLevel != -1 && target.buff(MagicImmune.class) == null) {
					if (chaliceCursed) {
						delay *= 1.5f;
					} else {
						//15% boost at +0, scaling to a 500% boost at +10
						delay -= 1.33f + chaliceLevel*0.667f;
						delay /= RingOfEnergy.artifactChargeMultiplier(target);
					}
				}
				delay = com.shatteredpixel.shatteredpixeldungeon.actors.hero.RearrangedRework.regenDelay(target, delay); //rearranged

				delay = com.shatteredpixel.shatteredpixeldungeon.actors.hero.SummoningHooks.soulRegenDelay(target, delay); //summoning
				//Ryuse's PD helmets
				delay *= com.shatteredpixel.shatteredpixeldungeon.items.helmets.Helmet.regenDelay(target);
				//transformation, Strengthen Chalice and Weaken Chalice
				if (chaliceLevel >= 0 && target instanceof com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero) delay *= com.shatteredpixel.shatteredpixeldungeon.actors.hero.TransformationTalents.chaliceFactor((com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero) target);

				//rearranged, the Medic's Strong Healpower
				if (target instanceof com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero
						&& ((com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero)target).hasTalent(com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent.STRONG_HEALPOWER)) {
					delay /= 1f+0.1f*((com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero)target).pointsInTalent(com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent.STRONG_HEALPOWER);
				}

				//salt cube is turned off while regen is disabled.
				if (target.buff(LockedFloor.class) == null) {
					delay /= SaltCube.healthRegenMultiplier();
				}

				if (Dungeon.hero != null && Dungeon.hero.subClass.is(com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass.WATERSLIME)){
					delay /= Math.max(target.HT * 0.01 * (1 + Dungeon.hero.pointsInTalent(com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent.WATER_REGENERATION)/6f), 1);
				}
				delay *= com.shatteredpixel.shatteredpixeldungeon.actors.hero.BAHero.regenDelay(target); //bluearchive
				partialRegen += 1f / delay;

				if (partialRegen >= 1) {
					target.HP += (int)partialRegen;
					partialRegen -= (int)partialRegen;
					if (target.HP >= regencap()) {
						target.HP = regencap();
						((Hero) target).resting = false;
					}
				}

			}

			spend( TICK );
			
		} else {
			
			diactivate();
			
		}
		
		return true;
	}
	
	public int regencap(){
		return target.HT;
	}

	public static boolean regenOn(){
		//summoning, the Occultist from Summoning PD does not regenerate
		if (Dungeon.hero != null && Dungeon.hero.subClass == com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass.OCCULTIST) return false;
		LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
		if (lock != null && !lock.regenOn()){
			return false;
		}
		return true;
	}

	public static final String PARTIAL_REGEN = "partial_regen";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(PARTIAL_REGEN, partialRegen);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		partialRegen = bundle.getFloat(PARTIAL_REGEN);
	}
}
