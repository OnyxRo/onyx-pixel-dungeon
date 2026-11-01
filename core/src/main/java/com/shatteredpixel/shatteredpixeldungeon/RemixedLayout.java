/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Re-Remixed Dungeon, by QuasiStellar, ported into Onyx's Pixel Dungeon
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

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.items.FakeAmulet;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.TownLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.VeryLastLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;

//reremixed, Re-Remixed Dungeon's run layout as a game mode. The run starts in the town on floor 0, whose
//stairs lead down to floor 1 (and floor 1's stairs lead back up to it). The amulet on floor 26 is a mimic,
//and its pedestal is a way down to floor 27 where the true Amulet lies. The plain depth + 1 and depth - 1
//order already fits these floors, so GameModes.nextDepth and prevDepth need no change for it.
public class RemixedLayout {

	public static final String LAYOUT = "reremixed_layout";
	public static final String MOD = "Re-Remixed Dungeon";

	public static void register(){
		GameModes.register(LAYOUT, MOD, 1, "layout");
	}

	public static boolean on(){
		return GameModes.on(LAYOUT);
	}

	//Dungeon.init, the run starts above floor 1
	public static void onStart( Hero hero ){
		if (on() && !Dungeon.bossCrawl) Dungeon.depth = 0;
	}

	//Dungeon.newLevel, the town and the floor below the false amulet
	public static boolean builds( int depth ){
		return on() && (depth == 0 || depth == 27);
	}

	public static Level level( int depth ){
		return depth == 0 ? new TownLevel() : new VeryLastLevel();
	}

	//Level.create, the town and floor 27 must not take the strength potions and upgrade scrolls meant for real floors
	public static boolean skipsDrops(){
		return on() && Dungeon.branch == 0 && (Dungeon.depth == 0 || Dungeon.depth == 27);
	}

	//LevelTransition, floor 1's way up leads to the town instead of the surface
	public static LevelTransition.Type transitionType( LevelTransition.Type type ){
		if (on() && type == LevelTransition.Type.SURFACE && Dungeon.depth == 1 && Dungeon.branch == 0) return LevelTransition.Type.REGULAR_ENTRANCE;
		return type;
	}

	//LastLevel, the false amulet and the way down beneath it
	public static Item lastFloorAmulet(){
		return on() ? new FakeAmulet() : new Amulet();
	}

	public static void addLastExit( Level level, int cell ){
		if (on()) level.transitions.add(new LevelTransition(level, cell, LevelTransition.Type.REGULAR_EXIT));
	}
}
