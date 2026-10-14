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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
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

import java.util.ArrayList;

/**
 * A self-running screenshot tour, for checking every world without clicking through the game.
 *
 * Start the game with -Donyx.gallery=<folder>. It begins a sandbox run on its own, visits
 * every ported world in turn, saves a picture of each into the folder, then spawns and pictures
 * every ported monster, then pictures the sandbox windows and quits. Does nothing at all when the property is not set.
 */
public class Gallery {

	private static final String DIR = System.getProperty("onyx.gallery");

	//a shuffled floor from each region, in region order
	private static final int[] DEPTHS = { 2, 7, 12, 17, 22 };

	private static boolean started = false;
	//-Donyx.gallery.only=mobs skips the worlds and goes straight to the monsters
	private static int world = System.getProperty("onyx.gallery.only") != null ? Integer.MAX_VALUE : WorldShuffle.HALLS + 1;
	private static float time = 0;
	private static boolean shot = false;

	private static final ArrayList<Class<?>> MOBS = new ArrayList<>();
	private static int mob = 0;
	private static Mob mobSpawned = null;

	private static final int[] BOSS_DEPTHS = { 5, 10, 15, 20, 25 };
	private static int boss = 0;

	//the first world that has a mod boss floor to go with it
	private static int bossWorld(){
		for (int w = 0; w < WorldShuffle.worldCount(); w++) {
			if (WorldShuffle.modBossLevel(w, BOSS_DEPTHS[0]) != null) return w;
		}
		return -1;
	}

	private static int uiStep = 0;
	private static Window uiWindow = null;

	//only runs once the launcher has confirmed the tour has a save folder of its own.
	//On 2026-10-13 a tour started from a jar used the player's real folder and wiped save slot 1
	public static boolean active(){
		return DIR != null && "true".equals(System.getProperty("onyx.gallery.isolated"));
	}

	/** Called when the title screen opens. Starts the tour's sandbox run. */
	public static void onTitle(){
		if (!active() || started) return;
		started = true;

		SPDSettings.intro(false);
		Dungeon.deleteGame(1, true);

		//every monster ported from another mod
		for (Bestiary category : Bestiary.values()) {
			for (Class<?> cls : category.entities()) {
				if (Bestiary.isPorted(cls) && WndSandboxMobs.safeToSpawn(cls)) MOBS.add(cls);
			}
		}

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

		//-Donyx.gallery.only=bosses skips the monsters too
		if ("bosses".equals(System.getProperty("onyx.gallery.only"))) mob = MOBS.size();

		//then spawn each ported monster beside the hero, let it act for a moment, picture it and kill it
		if (mob < MOBS.size()) {
			if (mobSpawned == null) {
				if (time < 0.3f) return;
				int before = Dungeon.level.mobs.size();
				WndSandboxMobs.spawn( MOBS.get(mob) );
				for (Mob m : Dungeon.level.mobs) {
					if (m.getClass() == MOBS.get(mob) && Dungeon.level.adjacent(m.pos, Dungeon.hero.pos)) mobSpawned = m;
				}
				if (mobSpawned == null) {
					System.out.println("[GALLERY] could not spawn " + MOBS.get(mob).getSimpleName() + " (" + before + " mobs on floor)");
					mob++;
				}
				time = 0;
				return;
			}
			//the hero has to spend turns for anything else to act
			if (Dungeon.hero.ready && time < 1.6f) Dungeon.hero.rest( false );
			if (time > 1.8f) {
				capture( String.format("mob_%02d_%s", mob, MOBS.get(mob).getSimpleName()) );
				if (mobSpawned.isAlive()) mobSpawned.die( null );
				mobSpawned = null;
				mob++;
				time = 0;
			}
			return;
		}

		//then each boss floor of a mod whose bosses are ported
		if (boss < BOSS_DEPTHS.length) {
			boolean here = Dungeon.depth == BOSS_DEPTHS[boss] && !WorldShuffle.canForce(Dungeon.depth)
					&& Dungeon.level.getClass() == WorldShuffle.modBossLevel(bossWorld(), BOSS_DEPTHS[boss]).getClass();
			if (!here) {
				Dungeon.sandboxWorld = bossWorld();
				InterlevelScene.mode = InterlevelScene.Mode.SANDBOX;
				InterlevelScene.returnDepth = BOSS_DEPTHS[boss];
				time = 0;
				Game.switchScene( InterlevelScene.class );
				return;
			}
			if (Dungeon.hero.ready && time < 2.2f) Dungeon.hero.rest( false );
			if (time > 2.5f) {
				capture( String.format("boss_%02d_%s", BOSS_DEPTHS[boss], Dungeon.level.getClass().getSimpleName()) );
				boss++;
				time = 0;
			}
			return;
		}

		//after the bosses, picture each sandbox window
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
