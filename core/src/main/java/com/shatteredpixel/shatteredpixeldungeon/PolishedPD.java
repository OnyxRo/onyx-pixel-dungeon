/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WaterOfAwareness;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WaterOfChange;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WaterOfHealth;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WellWater;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Brittle;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Electrified;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BlastingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisintegrationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FlashingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrimTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrippingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GuardianTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.PoisonDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WarpingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WornDartTrap;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Polished PD (GPL-3.0). Hooks for the parts of the mod that change Shattered's own things. They only act
 * with the Polished PD changes option on (Challenges.POLISHED_CHANGES), so with it off a run plays as before.
 * Each hook is one line in the shared class that calls it.
 */
public class PolishedPD {

	public static boolean active() {
		return Dungeon.isChallenged(Challenges.POLISHED_CHANGES);
	}

	//Char.attack, physical damage a brittle attacker deals
	public static float brittleAttack( Char attacker, float dmg ) {
		return attacker.buff(Brittle.class) != null ? dmg * 0.75f : dmg;
	}

	//Char.attack, physical damage a brittle defender takes after armor
	public static int brittleTaken( Char enemy, int dmg ) {
		return enemy.buff(Brittle.class) != null ? Math.round(dmg * 1.25f) : dmg;
	}

	//Char.damage, damage breaks the Electrified stun
	public static void onDamage( Char ch, int dmg, Object src ) {
		Electrified e = ch.buff(Electrified.class);
		if (e != null) e.processDamage(dmg, src);
	}

	//Electricity, stuns with Electrified under the option, otherwise Shattered's paralysis
	public static void shock( Char ch, float duration ) {
		if (active()) {
			if (ch.buff(Electrified.class) == null) Buff.prolong(ch, Electrified.class, Electrified.DURATION);
		} else if (ch.buff(Paralysis.class) == null) {
			Buff.prolong(ch, Paralysis.class, duration);
		}
	}

	//WandOfCorruption.debuffEnemy, Brittle joins the minor debuffs
	public static void addBrittle( HashMap<Class<? extends Buff>, Float> debuffs, boolean minor ) {
		if (minor && active() && !debuffs.containsKey(Brittle.class)) debuffs.put(Brittle.class, 2f);
	}

	@SuppressWarnings("unchecked")
	private static final Class<? extends Trap>[][] TRAPS_ROOM = new Class[][]{
			//sewers
			{WornDartTrap.class, GrippingTrap.class, WarpingTrap.class},
			//prison
			{PoisonDartTrap.class, GrippingTrap.class, BlastingTrap.class},
			//caves
			{DisintegrationTrap.class, GuardianTrap.class, WarpingTrap.class},
			//city
			{GuardianTrap.class, FlashingTrap.class, BlastingTrap.class},
			//halls
			{GrimTrap.class}
	};

	//TrapsRoom, Polished's trap table with its Blasting traps
	public static Class<? extends Trap>[][] trapsRoom( Class<? extends Trap>[][] normal ) {
		return active() ? TRAPS_ROOM : normal;
	}

	//MagicWellRoom, the Well of Change joins the other two
	@SuppressWarnings("unchecked")
	public static Class<? extends WellWater> wellWater( Class<? extends WellWater> rolled ) {
		if (!active()) return rolled;
		return (Class<? extends WellWater>) Random.oneOf(WaterOfAwareness.class, WaterOfHealth.class, WaterOfChange.class);
	}

	/*
	 * Metamorphosis helpers for the Well of Change. ScrollOfMetamorphosis does the same work inside its windows.
	 */

	//how many talent tiers the hero can have points in
	public static int tiersUnlocked( Hero hero ) {
		int tiers = 1;
		while (tiers < Talent.MAX_TALENT_TIERS && hero.lvl + 1 >= Talent.tierLevelThresholds[tiers + 1]) {
			tiers++;
		}
		if (tiers > 2 && hero.subClass == HeroSubClass.NONE) tiers = 2;
		else if (tiers > 3 && hero.armorAbility == null) tiers = 3;
		return tiers;
	}

	//the hero's class talents at a tier, as they stand after earlier metamorphoses
	public static ArrayList<Talent> classTalents( Hero hero, int tier ) {
		ArrayList<LinkedHashMap<Talent, Integer>> lists = new ArrayList<>();
		Talent.initClassTalents(hero.heroClass, lists, hero.metamorphedTalents);
		ArrayList<Talent> result = new ArrayList<>();
		if (tier - 1 >= lists.size() || tier - 1 >= hero.talents.size()) return result;
		for (Talent t : lists.get(tier - 1).keySet()) {
			if (hero.talents.get(tier - 1).containsKey(t)) result.add(t);
		}
		return result;
	}

	//one talent of each other class at this tier that could replace the given one
	public static ArrayList<Talent> options( Hero hero, int tier, Talent replacing ) {
		ArrayList<Talent> options = new ArrayList<>();
		Set<Talent> current = hero.talents.get(tier - 1).keySet();
		for (HeroClass cls : HeroClass.values()) {
			ArrayList<LinkedHashMap<Talent, Integer>> clsTalents = new ArrayList<>();
			Talent.initClassTalents(cls, clsTalents);
			if (tier - 1 >= clsTalents.size()) continue;
			ArrayList<Talent> pool = new ArrayList<>();
			boolean replacingIsInSet = false;
			for (Talent t : clsTalents.get(tier - 1).keySet()) {
				if (t == replacing) { replacingIsInSet = true; break; }
				if (!current.contains(t)) pool.add(t);
			}
			if (!replacingIsInSet && !pool.isEmpty()) options.add(Random.element(pool));
		}
		return options;
	}

	//swaps a talent for another, keeping its points, and records it the way the scroll does
	public static void replaceTalent( Hero hero, Talent replacing, Talent talent ) {
		for (int i = 0; i < hero.talents.size(); i++) {
			LinkedHashMap<Talent, Integer> tier = hero.talents.get(i);
			if (!tier.containsKey(replacing)) continue;
			LinkedHashMap<Talent, Integer> newTier = new LinkedHashMap<>();
			for (Talent t : tier.keySet()) {
				if (t == replacing) {
					newTier.put(talent, tier.get(replacing));
					if (!hero.metamorphedTalents.containsValue(replacing)) {
						hero.metamorphedTalents.put(replacing, talent);
					} else if (hero.metamorphedTalents.get(talent) == replacing) {
						hero.metamorphedTalents.remove(talent);
					} else {
						for (Talent t2 : hero.metamorphedTalents.keySet()) {
							if (hero.metamorphedTalents.get(t2) == replacing) {
								hero.metamorphedTalents.put(t2, talent);
							}
						}
					}
				} else {
					newTier.put(t, tier.get(t));
				}
			}
			hero.talents.set(i, newTier);
			break;
		}
		if (hero.hasTalent(talent)) {
			Talent.onTalentUpgraded(hero, talent);
		}
	}

	//metamorphs one random class talent, avoiding the tier of the talent given. Returns {old, new} or null
	public static Talent[] randomMetamorph( Hero hero, Talent avoid ) {
		int avoidTier = -1;
		if (avoid != null) {
			for (int i = 0; i < hero.talents.size(); i++) {
				if (hero.talents.get(i).containsKey(avoid)) avoidTier = i + 1;
			}
		}
		ArrayList<Integer> tiers = new ArrayList<>();
		ArrayList<Integer> fallback = new ArrayList<>();
		for (int t = 1; t <= tiersUnlocked(hero); t++) {
			ArrayList<Talent> pool = classTalents(hero, t);
			pool.remove(avoid);
			if (pool.isEmpty()) continue;
			if (t == avoidTier) fallback.add(t);
			else tiers.add(t);
		}
		if (tiers.isEmpty()) tiers = fallback;
		Random.shuffle(tiers);
		for (int tier : tiers) {
			ArrayList<Talent> pool = classTalents(hero, tier);
			pool.remove(avoid);
			Random.shuffle(pool);
			for (Talent replacing : pool) {
				ArrayList<Talent> options = options(hero, tier, replacing);
				if (options.isEmpty()) continue;
				Talent talent = Random.element(options);
				replaceTalent(hero, replacing, talent);
				return new Talent[]{replacing, talent};
			}
		}
		return null;
	}
}
