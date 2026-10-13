/*
 * Onyx's Pixel Dungeon
 * Copyright (C) 2026 Onyx Rothman
 *
 * Based on Shattered Pixel Dungeon, Copyright (C) 2014-2026 Evan Debenham
 * and Pixel Dungeon, Copyright (C) 2012-2015 Oleg Dolya
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

package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.watabou.utils.Random;

/**
 * "Every floor a new world."
 *
 * Most regular floors are built in a randomly chosen world (Sewers, Prison, Caves,
 * Dwarven City or Demon Halls) instead of the one their depth normally uses. The world
 * decides the look of the floor, meaning its tileset, layout style, terrain decoration,
 * ambient effects and music. Monsters, loot and trap danger still follow the actual depth, so a
 * Demon Halls floor at depth 2 looks like the Halls but plays like depth 2.
 *
 * Floors that stay in their home world:
 *  - depth 1 (the start, and the exit to the surface when you win)
 *  - every boss floor (5, 10, 15, 20, 25) and the last floor (26)
 *  - the last regular floor of each region (4, 9, 14, 19), because the Ghost,
 *    Wandmaker, Blacksmith and Imp quests are guaranteed to appear by then
 *
 * The roll is seeded from the dungeon seed, so a given seed always produces the
 * same sequence of worlds.
 *
 * This is also the hook for porting content from other Pixel Dungeon forks. Add a
 * world to WORLDS and a case to create(), and it joins the rotation.
 */
public class WorldShuffle {

	public static final int SEWERS = 0;
	public static final int PRISON = 1;
	public static final int CAVES  = 2;
	public static final int CITY   = 3;
	public static final int HALLS  = 4;

	public static final int WORLDS = 5;

	//mixed into the depth seed so world rolls don't correlate with other per-depth rolls
	private static final long SALT = 0x0A1B2C3D4E5F6071L;

	public static int homeWorld( int depth ){
		return Math.max(0, Math.min(HALLS, (depth - 1) / 5));
	}

	public static boolean shuffles( int depth ){
		if (depth <= 1 || depth >= 25) return false;
		if (depth % 5 == 0) return false;      //boss floors
		//quest floors, since each region's quest is guaranteed to spawn by its 4th floor
		if (depth == 4 || depth == 9 || depth == 14 || depth == 19) return false;
		return true;
	}

	public static int worldFor( int depth ){
		if (!shuffles(depth)) return homeWorld(depth);

		int home = homeWorld(depth);
		Random.pushGenerator( Dungeon.seedForDepth(depth, 0) ^ SALT );
		//pick any world except the home one, so a shuffled floor always feels different
		int roll = Random.Int(WORLDS - 1);
		Random.popGenerator();
		return roll >= home ? roll + 1 : roll;
	}

	public static RegularLevel create( int world ){
		switch (world){
			case SEWERS: default: return new SewerLevel();
			case PRISON:          return new PrisonLevel();
			case CAVES:           return new CavesLevel();
			case CITY:            return new CityLevel();
			case HALLS:           return new HallsLevel();
		}
	}

	public static int worldOf( Level level ){
		if (level instanceof HallsLevel)  return HALLS;
		if (level instanceof CityLevel)   return CITY;
		if (level instanceof CavesLevel)  return CAVES;
		if (level instanceof PrisonLevel) return PRISON;
		return SEWERS;
	}

	//traps never get more dangerous than the actual depth allows:
	//a deeper world rolled on a shallow floor uses the home world's trap list
	private static RegularLevel trapSource( RegularLevel level ){
		int home = homeWorld(Dungeon.depth);
		if (Dungeon.branch == 0 && worldOf(level) > home){
			return create(home);
		}
		return level;
	}

	public static Class<?>[] trapClasses( RegularLevel level ){
		return trapSource(level).trapClasses();
	}

	public static float[] trapChances( RegularLevel level ){
		return trapSource(level).trapChances();
	}

}
