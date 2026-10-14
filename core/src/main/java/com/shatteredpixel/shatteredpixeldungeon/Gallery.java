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
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.levels.WorldShuffle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndCompendium;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndSandbox;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndSandboxFloor;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndSandboxItems;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndSandboxMobs;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;

/**
 * A self-running screenshot tour, for checking every world without clicking through the game.
 *
 * Start the game with -Donyx.gallery=<folder>. It begins a sandbox run on its own, visits
 * every ported world in turn, saves a picture of each into the folder, then pictures the
 * sandbox windows and quits. Does nothing at all when the property is not set.
 */
public class Gallery {

	private static final String DIR = System.getProperty("onyx.gallery");

	//a shuffled floor from each region, in region order
	private static final int[] DEPTHS = { 2, 7, 12, 17, 22 };

	private static boolean started = false;
	private static int world = WorldShuffle.HALLS + 1;
	private static float time = 0;
	private static boolean shot = false;

	private static int uiStep = 0;
	private static Window uiWindow = null;

	public static boolean active(){
		return DIR != null;
	}

	/** Called when the title screen opens. Starts the tour's sandbox run. */
	public static void onTitle(){
		if (!active() || started) return;
		started = true;

		SPDSettings.intro(false);
		Dungeon.deleteGame(1, true);

		Dungeon.sandbox = true;
		GamesInProgress.selectedClass = HeroClass.WARRIOR;
		GamesInProgress.curSlot = 1;
		Dungeon.hero = null;
		Dungeon.daily = Dungeon.dailyReplay = false;
		Dungeon.initSeed();
		InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
		Game.switchScene( InterlevelScene.class );
	}

	/** Called once per frame while in the dungeon. */
	public static void onGameFrame(){
		if (!active() || !started) return;

		time += Game.elapsed;

		if (world < WorldShuffle.worldCount()) {
			//the first floor of the run is only a starting point, go straight to the first world
			boolean here = WorldShuffle.worldOf(Dungeon.level) == world;
			if (here && !shot && time > 1.5f) {
				Camera.main.zoom( 2 );
			}
			if (here && !shot && time > 1.8f) {
				capture( String.format("world_%02d_%s", world, Dungeon.level.getClass().getSimpleName()) );
				shot = true;
			}
			if (!here || (shot && time > 2.0f)) {
				if (here) world++;
				if (world < WorldShuffle.worldCount()) jump();
				else time = 0;
			}
			return;
		}

		//after the worlds, picture each sandbox window
		if (time < 0.6f) return;
		if (uiWindow != null) {
			capture( "window_" + uiStep + "_" + uiWindow.getClass().getSimpleName() );
			uiWindow.hide();
			uiWindow = null;
			uiStep++;
			time = 0;
			return;
		}
		switch (uiStep) {
			case 0: uiWindow = new WndSandbox(); break;
			case 1: uiWindow = new WndSandboxFloor(); break;
			case 2: uiWindow = new WndSandboxItems(); break;
			case 3: uiWindow = new WndSandboxMobs(); break;
			case 4: uiWindow = new WndCompendium(); break;
			default:
				Gdx.app.exit();
				return;
		}
		GameScene.show( uiWindow );
		time = 0;
	}

	private static void jump(){
		int region = WorldShuffle.regionOf(world);
		Dungeon.sandboxWorld = world;
		InterlevelScene.mode = InterlevelScene.Mode.SANDBOX;
		InterlevelScene.returnDepth = DEPTHS[region == WorldShuffle.ANY_REGION ? 0 : region];
		time = 0;
		shot = false;
		Game.switchScene( InterlevelScene.class );
	}

	private static void capture( String name ){
		Pixmap pixmap = Pixmap.createFromFrameBuffer( 0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight() );
		PixmapIO.writePNG( Gdx.files.absolute( DIR + "/" + name + ".png" ), pixmap, 6, true );
		pixmap.dispose();
	}

}
