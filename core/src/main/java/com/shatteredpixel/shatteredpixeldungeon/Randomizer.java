/*
 * Onyx's Pixel Dungeon
 * Based on Shattered Pixel Dungeon, Copyright (C) 2014-2025 Evan Debenham
 * The Randomizer challenge ported from Pixel Dungeon Reforged by nathanlink169
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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Brute;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Crab;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM100;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM200;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Elemental;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Eye;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Ghoul;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Gnoll;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Golem;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Guard;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Monk;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Necromancer;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RipperDemon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Scorpio;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Shaman;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Skeleton;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Slime;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Snake;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Spinner;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Succubus;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Swarm;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Thief;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Warlock;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndRandomizerDisplay;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//Pixel Dungeon Reforged. With the Randomizer challenge every region has one creature that gains an extra ability
//(a buff, 1 to 3) and another that loses something (a nerf, 1 to 3). The picks are stored in Dungeon.randomizer.
public class Randomizer {
	// Randomizer is bitwise. Each region is 12 bits
	// 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000
	//|______________|
	// First Region
	//
	// Leftmost 4 bits is the buffed creature ID
	// Second 4 bits is the nerfed creature ID
	// Next 2 bits is the buff ID
	// Last 2 bits is the nerf ID
	//
	// For example: 0001 0101 1011   1253
	// This means creature ID 1 is buffed with buff ID 2.
	//			  creature ID 5 is nerfed with nerf ID 3.
	//
	// Last four bits represent which is the highest region we've seen the window for.

	public static void initialize() {
		Dungeon.randomizer = 0;

		if (!Dungeon.isChallenged(Challenges.RANDOMIZER)) return;

		//the picks come from the dungeon seed, so a seed always gives the same randomizer
		Random.pushGenerator( Dungeon.seed + 777777L );
		for (int region = 1; region <= 5; region++) {
			setRandomCreaturesForRegion(region);
		}
		Random.popGenerator();
	}

	public static void showRandomizerWindowIfRequired() {
		if (!Dungeon.isChallenged(Challenges.RANDOMIZER)) return;

		int region = 1;
		if (Dungeon.depth >= 6) region = 2;
		if (Dungeon.depth >= 11) region = 3;
		if (Dungeon.depth >= 16) region = 4;
		if (Dungeon.depth >= 21) region = 5;

		int highestRegion = getBits(0, 4);

		if (highestRegion < region) {
			ShatteredPixelDungeon.scene().add(new WndRandomizerDisplay(region));
			setBits(0, 4, region);
			Notes.add(Notes.Landmark.RANDOMIZER);
		}
	}

	//Only for the headless tests, 1 to 3 makes every creature use that buff or nerf, so each one can be run
	public static int testBuff = 0, testNerf = 0;

	public static int getCreatureBuff(Class<? extends Char> creature) {
		if (!Dungeon.isChallenged(Challenges.RANDOMIZER)) return 0;

		CreatureData data = m_creatureData.get(creature);
		if (data == null) return 0;
		if (testBuff > 0) return testBuff;
		int bitForRegion = (-12 * data.region) + 72;
		int buffedCreatureId = getBits(bitForRegion, 4);
		if (buffedCreatureId == data.id) {
			return getBits(bitForRegion - 6, 2);
		}
		return 0;
	}

	public static int getCreatureNerf(Class<? extends Char> creature) {
		if (!Dungeon.isChallenged(Challenges.RANDOMIZER)) return 0;

		CreatureData data = m_creatureData.get(creature);
		if (data == null) return 0;
		if (testNerf > 0) return testNerf;
		int bitForRegion = (-12 * data.region) + 72;
		int nerfedCreatureId = getBits(bitForRegion - 4, 4);
		if (nerfedCreatureId == data.id) {
			return getBits(bitForRegion - 8, 2);
		}
		return 0;
	}

	public static Class<? extends Char> getBuffedCreature(int region) {
		if (!Dungeon.isChallenged(Challenges.RANDOMIZER)) return null;

		int bitForRegion = (-12 * region) + 72;
		int buffedCreatureId = getBits(bitForRegion, 4);

		for (Map.Entry<Class<? extends Char>, CreatureData> entry : m_creatureData.entrySet()) {
			if (entry.getValue().region == region && entry.getValue().id == buffedCreatureId) {
				return entry.getKey();
			}
		}
		return null;
	}

	public static Class<? extends Char> getNerfedCreature(int region) {
		if (!Dungeon.isChallenged(Challenges.RANDOMIZER)) return null;

		int bitForRegion = (-12 * region) + 72;
		int nerfedCreatureId = getBits(bitForRegion - 4, 4);

		for (Map.Entry<Class<? extends Char>, CreatureData> entry : m_creatureData.entrySet()) {
			if (entry.getValue().region == region && entry.getValue().id == nerfedCreatureId) {
				return entry.getKey();
			}
		}
		return null;
	}

	private static void setRandomCreaturesForRegion(int region) {
		//sorted by id so that the same seed always picks the same creatures
		ArrayList<CreatureData> regionCreatures = new ArrayList<>();
		for (CreatureData data : m_creatureData.values()) {
			if (data.region == region) regionCreatures.add(data);
		}
		regionCreatures.sort((a, b) -> Integer.compare(a.id, b.id));

		if (regionCreatures.size() >= 2) {
			CreatureData randomCreature1 = regionCreatures.get(Random.Int(regionCreatures.size()));
			regionCreatures.remove(randomCreature1);
			CreatureData randomCreature2 = regionCreatures.get(Random.Int(regionCreatures.size()));
			int bitForRegion = (-12 * region) + 72;
			setBits(bitForRegion, 4, randomCreature1.id);
			setBits(bitForRegion - 4, 4, randomCreature2.id);

			int rand1 = Random.Int(3) + 1;
			int rand2 = Random.Int(3) + 1;
			setBits(bitForRegion - 6, 2, rand1);
			setBits(bitForRegion - 8, 2, rand2);
		} else {
			throw new Error("Randomizer could not find creatures!");
		}
	}

	public static void setBits(int startBit, int numBits, int newValue) {
		// Create a mask with the specified number of bits set to 1
		long mask = (1L << numBits) - 1;

		// Clear the target bits in the original value
		long cleared = Dungeon.randomizer & ~(mask << startBit);

		// Set the new bits
		Dungeon.randomizer = cleared | ((long)newValue << startBit);
	}

	private static int getBits(int startBit, int numBits) {
		// Create a mask with the specified number of bits set to 1
		long mask = (1L << numBits) - 1;
		long value = Dungeon.randomizer;
		// Shift the value right to move target bits to the rightmost position
		// Then mask to keep only the bits we want
		return (int)((value >> startBit) & mask);
	}

	private static HashMap<Class<? extends Char>, CreatureData> m_creatureData;
	static {
		m_creatureData = new HashMap<>();
		m_creatureData.put(Rat.class, new CreatureData(1, 1));
		m_creatureData.put(Snake.class, new CreatureData(1, 2));
		m_creatureData.put(Gnoll.class, new CreatureData(1, 3));
		m_creatureData.put(Crab.class, new CreatureData(1, 4));
		// 5 is Reforged's Spitter, which keeps its own traits
		m_creatureData.put(Swarm.class, new CreatureData(1, 6));
		m_creatureData.put(Slime.class, new CreatureData(1, 7));

		m_creatureData.put(Skeleton.class, new CreatureData(2, 1));
		m_creatureData.put(Thief.class, new CreatureData(2, 2));
		m_creatureData.put(DM100.class, new CreatureData(2, 3));
		// 4 is Reforged's Unholy Priest
		m_creatureData.put(Guard.class, new CreatureData(2, 5));
		m_creatureData.put(Necromancer.class, new CreatureData(2, 6));

		m_creatureData.put(Bat.class, new CreatureData(3, 1));
		m_creatureData.put(Brute.class, new CreatureData(3, 2));
		m_creatureData.put(Shaman.class, new CreatureData(3, 3));
		m_creatureData.put(Spinner.class, new CreatureData(3, 4));
		m_creatureData.put(DM200.class, new CreatureData(3, 5));
		// 6 is Reforged's Ballista

		m_creatureData.put(Ghoul.class, new CreatureData(4, 1));
		m_creatureData.put(Elemental.class, new CreatureData(4, 2));
		m_creatureData.put(Warlock.class, new CreatureData(4, 3));
		m_creatureData.put(Monk.class, new CreatureData(4, 4));
		m_creatureData.put(Golem.class, new CreatureData(4, 5));

		m_creatureData.put(RipperDemon.class, new CreatureData(5, 1));
		m_creatureData.put(Succubus.class, new CreatureData(5, 2));
		m_creatureData.put(Eye.class, new CreatureData(5, 3));
		m_creatureData.put(Scorpio.class, new CreatureData(5, 4));
		// 5 is Reforged's Demon Goo
	}

	private static class CreatureData {
		public int region;
		public int id;
		private CreatureData( int region, int id) {
			this.region = region;
			this.id = id;
		}
	}


	public static boolean buff(Class<? extends Char> c, int n) {
		return getCreatureBuff(c) == n;
	}

	public static boolean nerf(Class<? extends Char> c, int n) {
		return getCreatureNerf(c) == n;
	}

	//This game does not tag damage with types, so the sort of damage a hit does is worked out from its source.
	//A hit by the hero counts as the kind of weapon the hero was attacking with.
	private static String weaponName(Object src) {
		if (src == Dungeon.hero && Dungeon.hero != null) {
			com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon w = Dungeon.hero.belongings.attackingWeapon();
			return w == null ? "" : w.getClass().getSimpleName();
		}
		if (src instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon) {
			return src.getClass().getSimpleName();
		}
		return "";
	}

	public static boolean isSlashing(Object src) {
		String n = weaponName(src);
		for (String k : new String[]{"Sword", "Blade", "Axe", "Scimitar", "Katana", "Sickle", "Scythe", "Whip", "Tomahawk",
				"Glaive", "Claw", "Saw", "Cleaver", "Machete"}) {
			if (n.contains(k)) return true;
		}
		return false;
	}

	public static boolean isPiercing(Object src) {
		String n = weaponName(src);
		for (String k : new String[]{"Dagger", "Dirk", "Spear", "Glaive", "Katana", "Rapier", "Sai", "Sword", "Dart",
				"Javelin", "Kunai", "Shuriken", "Knife", "Spike", "Trident", "Bow", "Gun", "Crossbow", "Pike", "Lance", "Needle"}) {
			if (n.contains(k)) return true;
		}
		return false;
	}

	//magic, fire, poison and the like, anything that is not a blow, a bleed or hunger
	public static boolean isEnergy(Object src) {
		return src != null
				&& !(src instanceof Char)
				&& !(src instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon)
				&& !(src instanceof com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding)
				&& !(src instanceof com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger)
				&& !(src instanceof Class);
	}
}
