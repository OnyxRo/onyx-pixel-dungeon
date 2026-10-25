/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Xun's Pixel Dungeon, by X-U-N-123, ported into Onyx's Pixel Dungeon
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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.watabou.utils.Random;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Pulse;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.engineer.Detonator;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.engineer.ForceField;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.engineer.SummoningBeacon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.explorer.OpticalCamou;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.explorer.Sandstorm;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.explorer.Underpass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.wraith.EvilUnfold;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.wraith.GhostWander;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.wraith.Transfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.BoneSpike;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MultiTool;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.XunShovel;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.BoneFragment;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Clay;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;

//xun, Xun's PD's Explorer, Wraith and Engineer. Starting kits and armor abilities, as in the mod.
public class XunHeroes {

	public static void init( HeroClass cls, Hero hero ){
		switch (cls){
			case EXPLORER: {
				(hero.belongings.weapon = new XunShovel()).identify();
				hero.belongings.weapon.activate(hero);
				Clay clay = new Clay();
				clay.quantity(5).collect();
				Dungeon.quickslot.setSlot(0, hero.belongings.weapon);
				Dungeon.quickslot.setSlot(1, clay);
				new PotionOfFrost().identify();
				new ScrollOfTeleportation().identify();
				break;
			}
			case WRAITH: {
				(hero.belongings.weapon = new BoneSpike()).identify();
				hero.belongings.weapon.activate(hero);
				BoneFragment fragment = new BoneFragment();
				fragment.quantity(3).collect();
				Dungeon.quickslot.setSlot(0, hero.belongings.weapon);
				Dungeon.quickslot.setSlot(1, fragment);
				new PotionOfToxicGas().identify();
				new ScrollOfRetribution().identify();
				break;
			}
			case ENGINEER: {
				(hero.belongings.weapon = new MultiTool()).identify();
				hero.belongings.weapon.activate(hero);
				Dungeon.quickslot.setSlot(0, hero.belongings.weapon);
				ActionIndicator.setAction(Buff.affect(hero, Pulse.class));
				new PotionOfParalyticGas().identify();
				new ScrollOfRecharging().identify();
				break;
			}
			default:
		}
	}

	//the action buffs that run the subclasses, and the Engineer's pulse
	public static void ensureBuffs( Hero hero ){
		if (hero.heroClass == HeroClass.ENGINEER && hero.buff(Pulse.class) == null)
			ActionIndicator.setAction(Buff.affect(hero, Pulse.class));
		if (hero.hasTalent(Talent.ACID_RAIN) && hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AcidRain.class) == null)
			ActionIndicator.setAction(Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AcidRain.class));
		if (hero.pointsInTalent(Talent.HOMEMADE_DRUG) >= 2 && hero.buff(XunTalents.HomemadeDrugTracker.class) == null)
			Buff.affect(hero, XunTalents.HomemadeDrugTracker.class);
		switch (hero.subClass){
			case WAVECHASER:
				if (hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WhirlpoolBuff.class) == null)
					ActionIndicator.setAction(Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WhirlpoolBuff.class));
				break;
			case TRAPPER:
				if (hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.TrapChoose.class) == null)
					ActionIndicator.setAction(Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.TrapChoose.class));
				break;
			case ROCKSY:
				Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RockFallBuff.class);
				break;
			case INCUBUS:
				if (hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hypnosis.class) == null)
					ActionIndicator.setAction(Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hypnosis.class));
				break;
			default:
		}
	}

	//class passives. The Explorer sees a tile further and gains from the terrain under him, the Wraith can weaken
	public static int viewBonus( Hero hero ){
		return hero.heroClass == HeroClass.EXPLORER ? 1 : 0;
	}

	private static int terrain( Hero hero ){
		return Dungeon.level == null ? -1 : Dungeon.level.map[hero.pos];
	}

	public static float speedFactor( Hero hero ){
		float s = XunTalents.speed(hero);
		if (hero.heroClass != HeroClass.EXPLORER || hero.flying) return s;
		//the Wavechaser swims through Char.spend instead, which speeds up everything he does in water
		return terrain(hero) == Terrain.WATER && hero.subClass != HeroSubClass.WAVECHASER ? 1.15f * s : s;
	}

	public static float accuracyFactor( Hero hero ){
		if (hero.heroClass != HeroClass.EXPLORER) return 1f;
		int t = terrain(hero);
		return t == Terrain.EMPTY || t == Terrain.EMPTY_DECO ? 1.15f : 1f;
	}

	public static float evasionFactor( Hero hero ){
		return accuracyFactor( hero );
	}

	public static int attackProc( Hero hero, Char enemy, int damage ){
		if (hero.heroClass == HeroClass.EXPLORER){
			int t = terrain(hero);
			if (t == Terrain.EMPTY_SP || t == Terrain.PEDESTAL) damage = Math.round(damage * 1.15f);
		}
		if (hero.heroClass == HeroClass.WRAITH && enemy.isAlive() && Random.Int(4) == 0){
			Buff.prolong(enemy, Weakness.class, 3f);
		}
		return XunTalents.attackProc(hero, enemy, damage);
	}

	public static ArmorAbility[] armorAbilities( HeroClass cls ){
		switch (cls){
			case EXPLORER: default:
				return new ArmorAbility[]{new OpticalCamou(), new Sandstorm(), new Underpass()};
			case WRAITH:
				return new ArmorAbility[]{new Transfusion(), new EvilUnfold(), new GhostWander()};
			case ENGINEER:
				return new ArmorAbility[]{new ForceField(), new SummoningBeacon(), new Detonator()};
		}
	}
}
