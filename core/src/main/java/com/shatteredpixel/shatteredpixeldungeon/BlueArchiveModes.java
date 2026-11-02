/*
 * Onyx's Pixel Dungeon
 *
 * The two Blue Archive mods (Hoto-Mocha's Blue Archive PD and Re Blue Archive PD) took Shattered's
 * heroes out of hero select and left only their students. Here that is a game mode for each mod,
 * so normal runs keep every hero. While a mode is on, the shopkeeper is Sora and the blacksmith is
 * Cherino as in both mods, and with Re Blue Archive's mode the Imp is Akira. Those NPCs also look
 * that way on floors built in one of the two mods' worlds. Each hook in Shattered's classes is a one
 * line call into this class.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.BAHeroes;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.RBAHero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.RBASandbag;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.WorldShuffle;

public class BlueArchiveModes {

	public static final String MOD_BA   = "Blue Archive Pixel Dungeon";
	public static final String MOD_RBA  = "Re Blue Archive Pixel Dungeon";

	public static final String BA_STUDENTS  = "ba_students";
	public static final String RBA_STUDENTS = "rba_students";

	//only one roster can be on at a time
	private static final String GROUP = "student_roster";

	public static void register(){
		GameModes.register(BA_STUDENTS, MOD_BA, 1, GROUP);
		GameModes.register(RBA_STUDENTS, MOD_RBA, 1, GROUP);
	}

	public static boolean baOn(){
		return !ModsUnlock.locked() && GameModes.on(BA_STUDENTS);
	}

	public static boolean rbaOn(){
		return !ModsUnlock.locked() && GameModes.on(RBA_STUDENTS);
	}

	/*****************************************************************
	 * Hero select, only the mod's students while its mode is on
	 *****************************************************************/

	private static String shownRoster = "";

	private static String roster(){
		if (baOn()) return BA_STUDENTS;
		if (rbaOn()) return RBA_STUDENTS;
		return "";
	}

	//HeroClass.isUnlocked and the hero buttons of HeroSelectScene
	public static boolean hides( HeroClass cls ){
		shownRoster = roster();
		if (baOn()) return !BAHeroes.isBA(cls);
		if (rbaOn()) return !RBAHero.isStudent(cls);
		return false;
	}

	//HeroSelectScene, after the game modes window closes. True when the hero buttons must be built again
	public static boolean rosterChanged(){
		return !roster().equals(shownRoster);
	}

	/*****************************************************************
	 * The mods' pictures for Shattered's NPCs
	 *****************************************************************/

	private static boolean inWorld( String key ){
		Level level = Dungeon.level;
		if (level == null || ModsUnlock.locked()) return false;
		return key.equals(WorldShuffle.modKey(level));
	}

	//Sora at the shop and Cherino at the forge, both mods drew them the same way
	public static boolean studentNpcs(){
		return baOn() || rbaOn() || inWorld("bluearchive") || inWorld("rebluearchive");
	}

	//Akira as the Imp, only Re Blue Archive has her
	public static boolean akiraImp(){
		return rbaOn() || inWorld("rebluearchive");
	}

	/*****************************************************************
	 * Re Blue Archive's punching bag beside the Amulet
	 *****************************************************************/

	//LastLevel.createMobs, as in the mod, two tiles above the Amulet
	public static void lastLevelMobs( Level level, int amuletPos ){
		if (ModsUnlock.locked()) return;
		RBASandbag sandbag = new RBASandbag();
		sandbag.pos = amuletPos - 2*level.width();
		level.mobs.add( sandbag );
	}
}
