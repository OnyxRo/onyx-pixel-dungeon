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

package com.shatteredpixel.shatteredpixeldungeon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.WorldShuffle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.watabou.noosa.Game;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Keyboard shortcuts for sandbox runs.
 *
 *   Ctrl+R builds the current floor again. The floor gets a new layout, and if no world is
 *          forced it also rolls its world again, so it may come from a different mod.
 *   Ctrl+N jumps to a random floor in a random world, from the mods that are switched on.
 *          It does not change a forced world, only the floor it lands on.
 */
public class SandboxHotkeys {

	//the floors that shuffle, by stage
	private static final int[][] STAGE_FLOORS = {
			{ 2, 3 }, { 6, 7, 8 }, { 11, 12, 13 }, { 16, 17, 18 }, { 21, 22, 23, 24 }
	};

	/** Called once per frame while in the dungeon. */
	public static void poll(){
		if (!Dungeon.sandbox || Dungeon.hero == null || !Dungeon.hero.isAlive() || Dungeon.branch != 0) return;

		boolean ctrl = Gdx.input.isKeyPressed( Input.Keys.CONTROL_LEFT ) || Gdx.input.isKeyPressed( Input.Keys.CONTROL_RIGHT );
		if (!ctrl) return;

		if (Gdx.input.isKeyJustPressed( Input.Keys.R )) {
			regenerate();
		} else if (Gdx.input.isKeyJustPressed( Input.Keys.N )) {
			randomLevel();
		}
	}

	public static void regenerate(){
		Dungeon.sandboxSalt++;
		jump( Dungeon.depth );
	}

	public static void randomLevel(){
		ArrayList<Integer> pool = new ArrayList<>();
		for (int world = WorldShuffle.HALLS + 1; world < WorldShuffle.worldCount(); world++) {
			if (!Dungeon.sandboxOffMods.contains( WorldShuffle.modName(world) )) pool.add( world );
		}
		//with every mod switched off there is nothing to pick from, so use them all
		if (pool.isEmpty()) {
			for (int world = WorldShuffle.HALLS + 1; world < WorldShuffle.worldCount(); world++) pool.add( world );
		}

		ThreadLocalRandom random = ThreadLocalRandom.current();
		int world = pool.get( random.nextInt( pool.size() ) );

		int region = WorldShuffle.regionOf( world );
		if (region == WorldShuffle.ANY_REGION) region = random.nextInt( STAGE_FLOORS.length );
		int[] floors = STAGE_FLOORS[region];

		Dungeon.sandboxSalt++;
		Dungeon.sandboxOnceWorld = world;
		jump( floors[ random.nextInt( floors.length ) ] );
	}

	private static void jump( int depth ){
		Level.beforeTransition();
		InterlevelScene.mode = InterlevelScene.Mode.SANDBOX;
		InterlevelScene.returnDepth = depth;
		Game.switchScene( InterlevelScene.class );
	}

}
