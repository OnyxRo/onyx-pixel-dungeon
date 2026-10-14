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
 * Most regular floors are built in a world other than the one their depth normally uses,
 * picked from the worlds that were made for that stage of the dungeon. Each ported world
 * keeps the region its own mod built it for, so a sewer-stage floor gets a sewer-stage
 * world (like Devoted's Gardens), a prison-stage floor gets a prison-stage world (like
 * Combusted's Necropolis), and so on. Transformation's tower styles weren't tied to a
 * depth in their mod, so they can appear in any region, sharing one slot between them.
 *
 * The world decides the look of the floor, meaning its tileset, layout style, terrain
 * decoration, ambient effects and music. Monsters, loot and trap danger follow the
 * actual depth.
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
 * To add a world, write a level class for it, add the class to WORLDS and its region
 * to REGIONS.
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

	/** A world that fits any region, used for Transformation's tower styles. */
	public static final int ANY_REGION = -1;

	//the region each world was made for, in the same order as WORLDS
	private static final int[] REGIONS = {
			SEWERS, PRISON, CAVES, CITY, HALLS,
			//Combusted: replaced the Caves, Prison and City in their mod
			CAVES, PRISON, CITY,
			//Devoted: replaced all five regions in their mod
			SEWERS, PRISON, CAVES, CITY, HALLS,
			//Magic Ling: Ancient City was a side area at floors 17 and 18, Dragon Cave one off
			//floor 5, Pale Palace (their Church) replaced the City, Lost Tomb the Caves, and
			//the Hollow was a deep holiday area past floor 26
			CITY, SEWERS, CITY, CAVES, HALLS,
			//Transformation: tower styles rotated freely in their mod
			ANY_REGION, ANY_REGION, ANY_REGION, ANY_REGION, ANY_REGION, ANY_REGION
	};

	public static int regionOf( int world ){
		return world >= 0 && world < REGIONS.length ? REGIONS[world] : ANY_REGION;
	}

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

		//worlds made for this region, except the home world itself. Worlds that fit any
		//region share one slot, so they don't crowd out the region's own worlds
		java.util.ArrayList<Integer> regional = new java.util.ArrayList<>();
		java.util.ArrayList<Integer> anywhere = new java.util.ArrayList<>();
		for (int w = 0; w < WORLDS.length; w++){
			if (w == home) continue;
			if (REGIONS[w] == home) regional.add(w);
			else if (REGIONS[w] == ANY_REGION) anywhere.add(w);
		}

		Random.pushGenerator( Dungeon.seedForDepth(depth, 0) ^ SALT );
		int slots = regional.size() + (anywhere.isEmpty() ? 0 : 1);
		int world;
		if (slots == 0) {
			world = home;
		} else {
			int slot = Random.Int(slots);
			world = slot < regional.size() ? regional.get(slot) : anywhere.get(Random.Int(anywhere.size()));
		}
		Random.popGenerator();
		return world;
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
