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
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.AncientCityLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.AstralLibraryLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.ChineseHallLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.CitadelLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.ColdhouseLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.DragonCaveLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.ForgeLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.FrostArchiveLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.GardensLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.GothicCastleLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.HollowLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.IceCavesLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.LostTombLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.ModWorld;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.NecropolisLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.PalePalaceLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.SkyAlchemyGreenhouseLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.SpiderNestLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.TowerCoreGearworksLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.VoidLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

/**
 * "Every floor a new world."
 *
 * Most regular floors are built in a randomly chosen world instead of the one their depth
 * normally uses. That can be one of Shattered's five (Sewers, Prison, Caves, Dwarven City,
 * Demon Halls) or a world ported from another Pixel Dungeon mod (see the levels.worlds
 * package). The world decides the look of the floor, meaning its tileset, layout style,
 * terrain decoration, ambient effects and music. Monsters, loot and trap danger still
 * follow the actual depth, so a Demon Halls floor at depth 2 looks like the Halls but plays
 * like depth 2.
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
 * To add a world, write a level class for it and add the class to WORLDS.
 */
public class WorldShuffle {

	public static final int SEWERS = 0;
	public static final int PRISON = 1;
	public static final int CAVES  = 2;
	public static final int CITY   = 3;
	public static final int HALLS  = 4;

	//the first five are Shattered's own worlds, in depth order. Ported worlds follow.
	//Appending is safe. Reordering changes which world a given seed rolls.
	private static final Class<?>[] WORLDS = {
			SewerLevel.class, PrisonLevel.class, CavesLevel.class, CityLevel.class, HallsLevel.class,
			//Combusted Pixel Dungeon
			IceCavesLevel.class, NecropolisLevel.class, SpiderNestLevel.class,
			//Devoted Pixel Dungeon
			GardensLevel.class, ColdhouseLevel.class, ForgeLevel.class, CitadelLevel.class, VoidLevel.class,
			//Magic Ling Pixel Dungeon
			AncientCityLevel.class, DragonCaveLevel.class, PalePalaceLevel.class, LostTombLevel.class, HollowLevel.class,
			//Transformation Pixel Dungeon
			AstralLibraryLevel.class, FrostArchiveLevel.class, GothicCastleLevel.class,
			SkyAlchemyGreenhouseLevel.class, TowerCoreGearworksLevel.class, ChineseHallLevel.class
	};

	//mixed into the depth seed so world rolls don't correlate with other per-depth rolls
	private static final long SALT = 0x0A1B2C3D4E5F6071L;

	public static int worldCount(){
		return WORLDS.length;
	}

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
		int roll = Random.Int(WORLDS.length - 1);
		Random.popGenerator();
		return roll >= home ? roll + 1 : roll;
	}

	public static RegularLevel create( int world ){
		if (world < 0 || world >= WORLDS.length) world = SEWERS;
		return (RegularLevel) Reflection.newInstance(WORLDS[world]);
	}

	/** The world index of a level, or -1 if it isn't one of the shuffle's worlds. */
	public static int worldOf( Level level ){
		for (int i = 0; i < WORLDS.length; i++){
			if (WORLDS[i] == level.getClass()) return i;
		}
		return -1;
	}

	public static boolean isPortedWorld( Level level ){
		return level instanceof ModWorld;
	}

	//traps never get more dangerous than the actual depth allows. A deeper Shattered world
	//rolled on a shallow floor, or any ported world, uses the home world's trap list.
	private static RegularLevel trapSource( RegularLevel level ){
		if (Dungeon.branch != 0) return level;
		int home = homeWorld(Dungeon.depth);
		int world = worldOf(level);
		if (isPortedWorld(level) || world > home){
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

	/** The floor's world name, like "The Astral Library", or null for Shattered's own worlds. */
	public static String worldName( Level level ){
		return isPortedWorld(level) ? Messages.get(level.getClass(), "name") : null;
	}

	/** Logs which world the hero has arrived in, when the floor is a ported world. */
	public static void announce( Level level ){
		if (Dungeon.branch != 0 || !isPortedWorld(level)) return;
		GLog.p(Messages.get(WorldShuffle.class, "arrive",
				Messages.get(level.getClass(), "name"), Messages.get(level.getClass(), "mod")));
	}

}
