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
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.GuildLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.SnowfieldLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.KoboldCavesLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.ChristmasCavesLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.AbyssLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.LabsLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.ArrangedTempleLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.ArenaLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.BeachLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.SurfaceLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.CursedTempleLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.DolyaHavenLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.PollutedSewersLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.UesLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.LushSurfaceLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.GoldenCityLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.WildsLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.ColdLandsLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.DiedLandsLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.GhostManorLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.ColdChestLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.LingGardenLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.TenguHideoutLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.TrueVoidLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.AbyssalSewersLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.AbyssalPrisonLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.AbyssalCavesLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.AbyssalCityLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.AbyssalHallsLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.RatKingSewersLevel;
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
			SkyAlchemyGreenhouseLevel.class, TowerCoreGearworksLevel.class, ChineseHallLevel.class,
			//second wave, worlds from more of the Shattered family of mods
			//Shattered Pixel Dungeon Matt Edition
			GuildLevel.class, SnowfieldLevel.class,
			//Pixel Dungeon Reforged
			KoboldCavesLevel.class,
			//Shittened Pixel Dungeon
			ChristmasCavesLevel.class,
			//Summoning Pixel Dungeon Reincarnated
			AbyssLevel.class,
			//Re-ARranged Pixel Dungeon
			LabsLevel.class, ArrangedTempleLevel.class,
			//Experienced Pixel Dungeon
			ArenaLevel.class,
			//Cursed Pixel Dungeon
			BeachLevel.class, SurfaceLevel.class, CursedTempleLevel.class, DolyaHavenLevel.class,
			//Ryuse's Pixel Dungeon
			PollutedSewersLevel.class,
			//Transformation Pixel Dungeon
			UesLevel.class, LushSurfaceLevel.class,
			//Magic Ling Pixel Dungeon
			GoldenCityLevel.class, WildsLevel.class, ColdLandsLevel.class, DiedLandsLevel.class, GhostManorLevel.class, ColdChestLevel.class, LingGardenLevel.class, TenguHideoutLevel.class,
			//Devoted Pixel Dungeon
			TrueVoidLevel.class,
			//Abyssalnite Pixel Dungeon
			AbyssalSewersLevel.class, AbyssalPrisonLevel.class, AbyssalCavesLevel.class, AbyssalCityLevel.class, AbyssalHallsLevel.class,
			//Rat King Pixel Dungeon 2
			RatKingSewersLevel.class
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
			ANY_REGION, ANY_REGION, ANY_REGION, ANY_REGION, ANY_REGION, ANY_REGION,
			//second wave. These regions were picked from each tile set's look and name, not checked against the mods
			//Shattered Pixel Dungeon Matt Edition
			PRISON, CAVES,
			//Pixel Dungeon Reforged
			CAVES,
			//Shittened Pixel Dungeon
			CAVES,
			//Summoning Pixel Dungeon Reincarnated
			HALLS,
			//Re-ARranged Pixel Dungeon
			HALLS, CITY,
			//Experienced Pixel Dungeon
			PRISON,
			//Cursed Pixel Dungeon
			SEWERS, SEWERS, CAVES, CITY,
			//Ryuse's Pixel Dungeon
			SEWERS,
			//Transformation Pixel Dungeon
			ANY_REGION, ANY_REGION,
			//Magic Ling Pixel Dungeon
			CITY, CAVES, PRISON, HALLS, PRISON, CAVES, SEWERS, PRISON,
			//Devoted Pixel Dungeon
			HALLS,
			//Abyssalnite Pixel Dungeon
			SEWERS, PRISON, CAVES, CITY, HALLS,
			//Rat King Pixel Dungeon 2
			SEWERS
	};

	//the source mod's app icon for each world, in the same order as WORLDS. Null for Shattered's own
	private static final String COMBUSTED      = "interfaces/mods/combusted.png";
	private static final String DEVOTED        = "interfaces/mods/devoted.png";
	private static final String MAGIC_LING     = "interfaces/mods/magicling.png";
	private static final String TRANSFORMATION = "interfaces/mods/transformation.png";
	private static final String MATT = "interfaces/mods/matt.png";
	private static final String REFORGED = "interfaces/mods/reforged.png";
	private static final String SHITTENED = "interfaces/mods/shittened.png";
	private static final String SUMMONING = "interfaces/mods/summoning.png";
	private static final String REARRANGED = "interfaces/mods/rearranged.png";
	private static final String EXPERIENCED = "interfaces/mods/experienced.png";
	private static final String RYUSE = "interfaces/mods/ryuse.png";
	private static final String ABYSSALNITE = "interfaces/mods/abyssalnite.png";
	private static final String RKPD2 = "interfaces/mods/rkpd2.png";
	private static final String CURSED = "interfaces/mods/cursed.png";
	private static final String[] LOGOS = {
			null, null, null, null, null,
			COMBUSTED, COMBUSTED, COMBUSTED,
			DEVOTED, DEVOTED, DEVOTED, DEVOTED, DEVOTED,
			MAGIC_LING, MAGIC_LING, MAGIC_LING, MAGIC_LING, MAGIC_LING,
			TRANSFORMATION, TRANSFORMATION, TRANSFORMATION, TRANSFORMATION, TRANSFORMATION, TRANSFORMATION,
			//Shattered Pixel Dungeon Matt Edition
			MATT, MATT,
			//Pixel Dungeon Reforged
			REFORGED,
			//Shittened Pixel Dungeon
			SHITTENED,
			//Summoning Pixel Dungeon Reincarnated
			SUMMONING,
			//Re-ARranged Pixel Dungeon
			REARRANGED, REARRANGED,
			//Experienced Pixel Dungeon
			EXPERIENCED,
			//Cursed Pixel Dungeon
			CURSED, CURSED, CURSED, CURSED,
			//Ryuse's Pixel Dungeon
			RYUSE,
			//Transformation Pixel Dungeon
			TRANSFORMATION, TRANSFORMATION,
			//Magic Ling Pixel Dungeon
			MAGIC_LING, MAGIC_LING, MAGIC_LING, MAGIC_LING, MAGIC_LING, MAGIC_LING, MAGIC_LING, MAGIC_LING,
			//Devoted Pixel Dungeon
			DEVOTED,
			//Abyssalnite Pixel Dungeon
			ABYSSALNITE, ABYSSALNITE, ABYSSALNITE, ABYSSALNITE, ABYSSALNITE,
			//Rat King Pixel Dungeon 2
			RKPD2
	};

	/** The asset path of the icon of the mod a floor's world comes from, or null for Shattered's own worlds. */
	public static String modLogo( Level level ){
		int world = worldOf(level);
		return world >= 0 && world < LOGOS.length ? LOGOS[world] : null;
	}

	/** The mod a ported world comes from, without the "Pixel Dungeon" part, like "Devoted". */
	public static String modShortName( Level level ){
		return Messages.get(level.getClass(), "mod").replace(" Pixel Dungeon", "");
	}

	/**
	 * The monsters a floor spawns. A floor built in one of Devoted's worlds uses Devoted's own
	 * monsters for that depth. Every other floor uses Shattered's.
	 */
	public static java.util.ArrayList<Class<? extends com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob>> mobRotation( Level level, int depth ){
		if (Dungeon.branch == 0 && DEVOTED.equals(modLogo(level))) {
			return com.shatteredpixel.shatteredpixeldungeon.actors.mobs.altregion.DevotedSpawner.getMobRotation(depth);
		}
		if (Dungeon.branch == 0 && MATT.equals(modLogo(level))) {
			return com.shatteredpixel.shatteredpixeldungeon.actors.mobs.MattSpawner.getMobRotation(level);
		}
		return com.shatteredpixel.shatteredpixeldungeon.actors.mobs.MobSpawner.getMobRotation(depth);
	}

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

	/**
	 * The boss floor that belongs with a world, for mods whose bosses are ported, or null.
	 * Devoted has one boss for each of its five worlds, on the same floors as Shattered's.
	 */
	public static Level modBossLevel( int world, int depth ){
		if (world < 0 || world >= LOGOS.length || !DEVOTED.equals(LOGOS[world])) return null;
		switch (depth) {
			case 5:  return new GardenBossLevel();
			case 10: return new ColdhouseBossLevel();
			case 15: return new ForgeBossLevel();
			case 20: return new CitadelBossLevel();
			case 25: return new VoidBossLevel();
			default: return null;
		}
	}

	/**
	 * The world whose mod supplies this boss floor's boss, or -1 for Shattered's own boss.
	 * If any shuffled floor of the region leading up to the boss was built in a world from a
	 * mod with ported bosses, that mod's boss guards the region.
	 */
	public static int bossWorldFor( int depth ){
		if (!Dungeon.bossLevel(depth)) return -1;
		for (int d = depth - 4; d < depth; d++) {
			if (!shuffles(d)) continue;
			int world = worldFor(d);
			if (modBossLevel(world, depth) != null) return world;
		}
		return -1;
	}

	/**
	 * Shattered's bosses hand out the items a run needs to progress, the mask on floor 10 that
	 * picks a subclass and the crown on floor 20 that picks an armor ability. Mods' bosses drop
	 * their own things instead, so their boss floors call this when the boss falls.
	 */
	public static void dropProgressionItem( Level level ){
		com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero hero = Dungeon.hero;
		if (hero == null || Dungeon.branch != 0) return;

		com.shatteredpixel.shatteredpixeldungeon.items.Item item = null;
		if (Dungeon.depth == 10 && hero.subClass == com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass.NONE) {
			item = new com.shatteredpixel.shatteredpixeldungeon.items.TengusMask();
		} else if (Dungeon.depth == 20 && hero.armorAbility == null) {
			item = new com.shatteredpixel.shatteredpixeldungeon.items.KingsCrown();
		}
		if (item == null) return;

		//only ever one, however many times the floor is unsealed
		if (hero.belongings.getItem(item.getClass()) != null) return;
		for (com.shatteredpixel.shatteredpixeldungeon.items.Heap heap : level.heaps.valueList()) {
			for (com.shatteredpixel.shatteredpixeldungeon.items.Item i : heap.items) {
				if (i.getClass() == item.getClass()) return;
			}
		}
		level.drop( item, hero.pos ).sprite.drop();
	}

	/** Whether a sandbox run can force a world onto this floor. Boss floors and the last two floors keep their own levels. */
	public static boolean canForce( int depth ){
		return depth >= 1 && depth < 25 && depth % 5 != 0;
	}

	/** A world's name for menus, like "Astral Library". */
	public static String menuName( int world ){
		if (world < 0 || world >= WORLDS.length) return "";
		String name = world <= HALLS
				? Messages.get(WorldShuffle.class, "home_" + world)
				: Messages.get(WORLDS[world], "name");
		if (name.startsWith("the ")) name = name.substring(4);
		return name;
	}

	/** The mod a world comes from, for menus. */
	public static String modName( int world ){
		if (world < 0 || world >= WORLDS.length) return "";
		return world <= HALLS ? "Shattered Pixel Dungeon" : Messages.get(WORLDS[world], "mod");
	}

	/** The mods that worlds have been ported from, in the order they were added. */
	public static java.util.ArrayList<String> mods(){
		java.util.ArrayList<String> mods = new java.util.ArrayList<>();
		for (int w = HALLS + 1; w < WORLDS.length; w++){
			if (!mods.contains(modName(w))) mods.add(modName(w));
		}
		return mods;
	}

	/** The icon of the mod a world comes from, or null for Shattered's own. */
	public static String modLogo( int world ){
		return world >= 0 && world < LOGOS.length ? LOGOS[world] : null;
	}

	/** Whether a world's mod is left out of the roll, by Single mod mode or by the sandbox mod toggles. */
	private static boolean modOff( int w ){
		if (!Dungeon.singleMod.isEmpty()) return !Dungeon.singleMod.equals(modName(w));
		return Dungeon.sandboxOffMods.contains(modName(w));
	}

	public static int worldFor( int depth ){
		if (!shuffles(depth)) return homeWorld(depth);

		int home = homeWorld(depth);

		//worlds made for this region, except the home world itself. Worlds that fit any
		//region share one slot, so they don't crowd out the region's own worlds
		java.util.ArrayList<Integer> regional = new java.util.ArrayList<>();
		java.util.ArrayList<Integer> anywhere = new java.util.ArrayList<>();
		//a sandbox run can switch mods off, which leaves their worlds out of the roll
		boolean single = !Dungeon.singleMod.isEmpty();
		boolean filtered = single || (Dungeon.sandbox && !Dungeon.sandboxOffMods.isEmpty());
		for (int w = 0; w < WORLDS.length; w++){
			if (w == home) continue;
			if (filtered && (w <= HALLS || modOff(w))) continue;
			if (REGIONS[w] == home) regional.add(w);
			else if (REGIONS[w] == ANY_REGION) anywhere.add(w);
		}
		//if the mods left on have nothing made for this stage, use any of their worlds
		if (filtered && regional.isEmpty() && anywhere.isEmpty()) {
			for (int w = HALLS + 1; w < WORLDS.length; w++){
				if (!modOff(w)) regional.add(w);
			}
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
