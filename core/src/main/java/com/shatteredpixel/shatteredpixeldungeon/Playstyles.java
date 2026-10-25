/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Playstyles from Shattered Pixel Dungeon Radar, ported into Onyx's Pixel Dungeon
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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;

//Radar's playstyles. Each one has a level from 0 to 3 (a game mode with that level) and moves the hero's
//stats up and down by its row of points, the multipliers are worked out once whenever the picks change.
public class Playstyles {

	public static final String PREFIX = "radar_";

	public static final String[] NAME_IDS = {
			"glass_cannon", "walking_tank", "flailing_maniac", "fencer", "heavy_hitter", "adventurer",
			"faerie_blood", "sniper", "physically_fit", "fast_feet", "steelskin", "sage"
	};

	public static final int MELEE    = 0;
	public static final int RANGED   = 1;
	public static final int MAGIC    = 2;
	public static final int MOVE     = 3;
	public static final int ATK_SPD  = 4;
	public static final int ACCURACY = 5;
	public static final int EVASION  = 6;
	public static final int HP       = 7;
	public static final int ARMOR    = 8;
	public static final int LUCK     = 9;
	public static final int VISION   = 10;
	public static final int XRAY     = 11;

	public static final int STAT_COUNT = 12;

	public static final String[] STAT_IDS = {
			"melee", "ranged", "magic", "move", "atk_spd", "accuracy", "evasion", "hp", "armor", "luck", "vision", "xray"
	};

	public static final int[][] POINTS = {
		//                        mel  rng  mag  spd  asp  acc  eva   hp  arm  lck  vis  xry
		/* GLASS_CANNON    */ {   0,   1,   1,   0,   0,   0,   0,  -1,  -1,   0,   0,   0 },
		/* WALKING_TANK    */ {   0,   0,   0,  -1,   0,   0,  -1,   1,   1,   0,   0,   0 },
		/* FLAILING_MANIAC */ {   1,   0,   0,   0,   1,  -1,  -1,   0,   0,   0,   0,   0 },
		/* FENCER          */ {   0,  -1,  -1,   0,   1,   1,   0,   0,  -1,   0,   0,   0 },
		/* HEAVY_HITTER    */ {   1,   1,   0,   0,  -1,   0,  -1,   0,   0,   0,   0,   0 },
		/* ADVENTURER      */ {  -1,   0,  -1,   0,   0,   0,   1,   0,   0,   1,   0,   1 },
		/* FAERIE_BLOOD    */ {  -1,   0,   1,   0,   0,   0,   0,  -1,   0,   1,   0,   0 },
		/* SNIPER          */ {  -1,   1,   0,   0,  -1,   0,   0,   0,   0,   0,   1,   0 },
		/* PHYSICALLY_FIT  */ {   1,   0,   0,   0,   0,   0,  -1,   1,  -1,   0,   0,   0 },
		/* FAST_FEET       */ {   0,  -1,  -1,   1,   0,   0,   0,  -1,   0,   0,   0,   0 },
		/* STEELSKIN       */ {   1,   0,   0,  -1,   0,   0,   0,   0,   1,   0,   0,   0 },
		/* SAGE            */ {  -1,  -1,  -1,   0,   0,   0,   1,   0,   0,   1,   1,   1 },
	};

	private static final int[] points = new int[STAT_COUNT];
	private static final float[] values = new float[STAT_COUNT];
	private static boolean any = false;

	static {
		refresh();
	}

	public static void refresh(){
		java.util.Arrays.fill(points, 0);
		any = false;
		for (int i = 0; i < NAME_IDS.length; i++){
			int lvl = GameModes.level(PREFIX + NAME_IDS[i]);
			if (lvl == 0) continue;
			any = true;
			for (int s = 0; s < STAT_COUNT; s++) points[s] += lvl * POINTS[i][s];
		}
		values[MELEE]    = (float) Math.pow(1.24, points[MELEE]);
		values[RANGED]   = (float) Math.pow(1.24, points[RANGED]);
		values[MAGIC]    = (float) Math.pow(1.24, points[MAGIC]);
		values[MOVE]     = (float) Math.pow(1.075, points[MOVE]);
		values[ATK_SPD]  = (float) Math.pow(1.15, points[ATK_SPD]);
		values[ACCURACY] = (float) Math.pow(1.3, points[ACCURACY]);
		values[EVASION]  = (float) Math.pow(1.125, points[EVASION]);
		values[HP]       = (float) Math.pow(1.33, points[HP]);
		values[ARMOR]    = (float) Math.pow(1.25, points[ARMOR]);
		values[LUCK]     = (float) Math.pow(1.20, 0.5 * points[LUCK]);
		values[VISION]   = points[VISION];
		values[XRAY]     = points[XRAY];
	}

	public static boolean any(){
		return any;
	}

	public static int points( int stat ){
		return points[stat];
	}

	public static float value( int stat ){
		return values[stat];
	}

	private static boolean isHero( Char ch ){
		return any && ch instanceof Hero;
	}

	//one call per hook in Shattered's code, each returns the number unchanged when no playstyle is on

	public static int meleeDamage( Char attacker, int dmg ){
		return isHero(attacker) ? Math.round(dmg * values[MELEE]) : dmg;
	}

	public static int rangedDamage( Char attacker, int dmg ){
		return isHero(attacker) ? Math.round(dmg * values[RANGED]) : dmg;
	}

	public static int magicDamage( int dmg ){
		return any && Dungeon.hero != null ? Math.round(dmg * values[MAGIC]) : dmg;
	}

	public static float accuracy( Char attacker, float acc ){
		return isHero(attacker) ? acc * values[ACCURACY] : acc;
	}

	public static float evasion( Char defender, float def ){
		return isHero(defender) ? def * values[EVASION] : def;
	}

	public static int maxHP( int ht ){
		return any ? Math.max(1, Math.round(ht * values[HP])) : ht;
	}

	public static int armor( int dr ){
		return any ? Math.round(dr * values[ARMOR]) : dr;
	}

	public static float moveSpeed( float speed ){
		return any ? speed * values[MOVE] : speed;
	}

	public static float attackDelay( float delay ){
		return any ? delay / values[ATK_SPD] : delay;
	}

	public static float lootChance( float chance ){
		return any ? chance * values[LUCK] : chance;
	}

	public static int vision( int dist ){
		return any ? Math.max(1, dist + points[VISION]) : dist;
	}

	public static int xray( int range ){
		return any ? Math.max(0, range + points[XRAY]) : range;
	}
}
