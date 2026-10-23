/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
 *
 * Onyx's Pixel Dungeon, the seven extra challenges and the unlocking of them come from Pixel Dungeon Reforged
 * by nathanlink169
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

import com.shatteredpixel.shatteredpixeldungeon.items.Dewdrop;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;
import com.watabou.utils.Random;

import java.io.IOException;
import java.util.ArrayList;

public class Challenges {

	//Some of these internal IDs are outdated and don't represent what these challenges do
	public static final int NO_FOOD				= 1;
	public static final int NO_ARMOR			= 2;
	public static final int NO_HEALING			= 4;
	public static final int NO_HERBALISM		= 8;
	public static final int SWARM_INTELLIGENCE	= 16;
	public static final int DARKNESS			= 32;
	public static final int NO_SCROLLS		    = 64;
	public static final int CHAMPION_ENEMIES	= 128;
	public static final int STRONGER_BOSSES 	= 256;

	//Pixel Dungeon Reforged calls challenges modifiers, these seven are its own. They start locked, a Challenge Cup
	//from a trial room unlocks them one at a time (see ChallengeCup and the rooms in levels/rooms/modifierunlocks)
	public static final int HORDE				= 1 << 9;
	public static final int MONSTER_UNKNOWN		= 1 << 10;
	public static final int TRINKET_MADNESS		= 1 << 11;
	public static final int RANDOMIZER			= 1 << 12;
	public static final int ADAPTIVE			= 1 << 13;
	public static final int CUBE				= 1 << 14;
	public static final int BARRIER_BREAKER		= 1 << 15;
	//Experienced PD, opt in to its cycles after the Amulet
	public static final int ENDLESS_CYCLES		= 1 << 16;
	//transformation, Transformation PD's negative talents
	public static final int CURSED_TALENTS		= 1 << 17;
	//rearranged, Re-ARranged PD's five challenges and its rework of Shattered's six heroes
	public static final int SUPERMAN			= 1 << 18;
	public static final int PYRO				= 1 << 19;
	public static final int CURSED_DUNGEON		= 1 << 20;
	public static final int FATIGUE				= 1 << 21;
	public static final int MUTATION			= 1 << 22;
	public static final int REARRANGED_HEROES	= 1 << 23;
	//polished, an option that turns on Polished PD's changes to Shattered's own things
	public static final int POLISHED_CHANGES	= 1 << 24;

	public static final int MAX_VALUE           = (1 << 25) - 1;
	public static final int MAX_CHALS           = 25;

	public static final String[] NAME_IDS = {
			"champion_enemies",
			"stronger_bosses",
			"no_food",
			"no_armor",
			"no_healing",
			"no_herbalism",
			"swarm_intelligence",
			"darkness",
			"no_scrolls",
			"horde",
			"monster_unknown",
			"trinket_madness",
			"randomizer",
			"adaptive",
			"cube",
			"barrier_breaker",
			"endless_cycles",
			//transformation
			"cursed_talents",
			//rearranged
			"superman", "pyro", "cursed_dungeon", "fatigue", "mutation", "rearranged_heroes",
			//polished
			"polished_changes"
	};

	public static final int[] MASKS = {
			CHAMPION_ENEMIES, STRONGER_BOSSES, NO_FOOD, NO_ARMOR, NO_HEALING, NO_HERBALISM, SWARM_INTELLIGENCE, DARKNESS, NO_SCROLLS,
			HORDE, MONSTER_UNKNOWN, TRINKET_MADNESS, RANDOMIZER, ADAPTIVE, CUBE, BARRIER_BREAKER, ENDLESS_CYCLES,
			//transformation
			CURSED_TALENTS,
			//rearranged
			SUPERMAN, PYRO, CURSED_DUNGEON, FATIGUE, MUTATION, REARRANGED_HEROES,
			//polished
			POLISHED_CHANGES
	};

	//the nine challenges Shattered always has and Experienced's cycles, the rest have to be unlocked
	private static final int ALWAYS_UNLOCKED = NO_FOOD | NO_ARMOR | NO_HEALING | NO_HERBALISM | SWARM_INTELLIGENCE
			| DARKNESS | NO_SCROLLS | CHAMPION_ENEMIES | STRONGER_BOSSES | ENDLESS_CYCLES
			| CURSED_TALENTS //transformation
			| SUPERMAN | PYRO | CURSED_DUNGEON | FATIGUE | MUTATION | REARRANGED_HEROES //rearranged
			| POLISHED_CHANGES; //polished

	public static int activeChallenges(){
		return activeChallenges(Dungeon.challenges);
	}

	public static int activeChallenges(int mask){
		int chCount = 0;
		for (int ch : Challenges.MASKS){
			if ((mask & ch) != 0) chCount++;
		}
		return chCount;
	}

	public static boolean isItemBlocked( Item item ){

		if (Dungeon.isChallenged(NO_HERBALISM) && item instanceof Dewdrop){
			return true;
		}

		return false;

	}

	/*****************************************************************
	 * Unlocking, kept in challenges.dat next to the other save files.
	 * Sandbox runs have everything unlocked.
	 *****************************************************************/

	private static int m_UnlockedChallenges = ALWAYS_UNLOCKED;

	private static int allMasks() {
		int all = 0;
		for (int ch : MASKS) all |= ch;
		return all;
	}

	public static boolean HasAnyChallengesToUnlock() {
		if (Dungeon.sandbox) return false;
		load();
		return (allMasks() & ~m_UnlockedChallenges) != 0;
	}

	//unlocks one random locked challenge and returns its index in NAME_IDS, or -1 if nothing was locked
	public static int UnlockRandomChallenge() {
		load();
		ArrayList<Integer> locked = new ArrayList<>();
		for (int i = 0; i < MASKS.length; i++) {
			if ((m_UnlockedChallenges & MASKS[i]) == 0) locked.add(i);
		}
		if (locked.isEmpty()) return -1;
		int index = Random.element(locked);
		m_UnlockedChallenges |= MASKS[index];
		Save();
		return index;
	}

	// Debug
	public static void UnlockAllChallenges() {
		load();
		m_UnlockedChallenges |= allMasks();
		Save();
	}

	public static boolean IsChallengeUnlocked(int mask) {
		if (Dungeon.sandbox) return true;
		load();
		return (m_UnlockedChallenges & mask) == mask;
	}

	public static int LockedCount() {
		int n = 0;
		for (int ch : MASKS) if (!IsChallengeUnlocked(ch)) n++;
		return n;
	}

	public static final String UNLOCKED_CHALLENGES_KEY = "unlocked_challenges_key";
	public static final String CHALLENGES_FILE	= "challenges.dat";

	public static void Save() {
		Bundle bundle = new Bundle();
		store( bundle );

		try {
			FileUtils.bundleToFile(CHALLENGES_FILE, bundle);
		} catch (IOException e) {
			ShatteredPixelDungeon.reportException(e);
		}
	}

	public static void store( Bundle bundle ) {
		bundle.put( UNLOCKED_CHALLENGES_KEY, m_UnlockedChallenges );
	}

	public static void load() {
		try {
			Bundle bundle = FileUtils.bundleFromFile( CHALLENGES_FILE );
			m_UnlockedChallenges = bundle.getInt(UNLOCKED_CHALLENGES_KEY) | ALWAYS_UNLOCKED;
		} catch (IOException e) {
			m_UnlockedChallenges = ALWAYS_UNLOCKED;
		}
	}
}
