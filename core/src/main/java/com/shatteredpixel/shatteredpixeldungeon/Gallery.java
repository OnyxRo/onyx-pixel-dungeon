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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Perks;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndHero;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndPerks;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndSettings;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndCompendium;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndModInfo;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndSandbox;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndAddToPlaylist;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndNowPlaying;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndPlaylist;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndPlaylists;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndSongs;
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

	//floor and branch pairs for -Donyx.gallery.only=branches
	private static final int[][] BRANCH_FLOORS = { {14, 2}, {15, 2}, {16, 2}, {17, 2}, {18, 2}, {8, 2}, {9, 2} };
	private static int branchStep = 0;
	private static boolean stayDone = false;

	private static boolean started = false;
	//-Donyx.gallery.only=mobs skips the worlds and goes straight to the monsters
	//-Donyx.gallery.only=ui visits one world, then pictures the windows and tries Ctrl+N
	//-Donyx.gallery.only=arena -Donyx.gallery.mobs=Kobold,Spitter puts those monsters on one floor
	//together, pictures them once and quits (scripts/arena.sh). Without .mobs it uses every ported monster
	//-Donyx.gallery.fromworld=N pictures worlds N onward and then quits
	private static final String FROM_WORLD = System.getProperty("onyx.gallery.fromworld");
	private static int world = FROM_WORLD != null ? Integer.parseInt(FROM_WORLD)
			: System.getProperty("onyx.gallery.only") == null ? WorldShuffle.HALLS + 1
			: "ui".equals(System.getProperty("onyx.gallery.only")) ? WorldShuffle.worldCount() - 1 : Integer.MAX_VALUE;
	private static final boolean ARENA = "arena".equals(System.getProperty("onyx.gallery.only"));
	private static final java.util.List<String> ARENA_NAMES = System.getProperty("onyx.gallery.mobs") == null
			? new ArrayList<String>() : java.util.Arrays.asList(System.getProperty("onyx.gallery.mobs").split(","));
	private static float time = 0;
	private static boolean shot = false;

	private static final ArrayList<Class<?>> MOBS = new ArrayList<>();
	private static int mob = 0;
	private static boolean arenaSpawned = false;
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

		//no sound in the automatic tours (only this run, the saved setting is not touched). The final arena mode
		//is for playing, so it keeps the player's own setting
		if (!"final".equals(System.getProperty("onyx.gallery.only"))) {
			com.watabou.noosa.audio.Music.INSTANCE.enable( false );
			com.watabou.noosa.audio.Sample.INSTANCE.enable( false );
		}

		//every monster ported from another mod
		for (Bestiary category : Bestiary.values()) {
			for (Class<?> cls : category.entities()) {
				if (ARENA && !ARENA_NAMES.isEmpty()) {
					if (ARENA_NAMES.contains(cls.getSimpleName()) && !MOBS.contains(cls)) MOBS.add(cls);
				} else if (Bestiary.isPorted(cls) && WndSandboxMobs.safeToSpawn(cls)) MOBS.add(cls);
			}
		}

		//-Donyx.gallery.only=heroselect opens the hero select screen and picks every hero class in turn
		if ("heroselect".equals(System.getProperty("onyx.gallery.only"))) {
			Dungeon.sandbox = true;
			Game.switchScene( com.shatteredpixel.shatteredpixeldungeon.scenes.HeroSelectScene.class );
			return;
		}

		//the full run bot plays a normal run, not a sandbox one
		Dungeon.sandbox = !"fullrun".equals(System.getProperty("onyx.gallery.only"));
		//-Donyx.gallery.class=ZEALOT plays the tour as another hero class
		GamesInProgress.selectedClass = System.getProperty("onyx.gallery.class") != null
				? HeroClass.valueOf(System.getProperty("onyx.gallery.class")) : HeroClass.WARRIOR;
		GamesInProgress.curSlot = 1;
		Dungeon.hero = null;
		Dungeon.daily = Dungeon.dailyReplay = false;
		Dungeon.initSeed();
		if (System.getProperty("onyx.gallery.seed") != null) {
			Dungeon.seed = Long.parseLong( System.getProperty("onyx.gallery.seed") );
		}
		InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
		Game.switchScene( InterlevelScene.class );
	}

	/** Called once per frame while in the dungeon. */
	public static void onGameFrame(){
		if (!active() || !started) return;

		time += Game.elapsed;

		//-Donyx.gallery.only=fullrun -Donyx.gallery.seed=12345 plays a normal run with an unkillable hero. It goes down
		//to floor 26 one floor at a time, killing everything on each floor the way a win would, picks up the Amulet,
		//then climbs back to floor 1. [FULL] lines show where it got stuck, exceptions show in the log.
		if ("fullrun".equals(System.getProperty("onyx.gallery.only"))) {
			fullRun();
			return;
		}

		//-Donyx.gallery.only=allbosses visits every boss floor of every world and every special floor, lets the fight
		//run for a few seconds with an unkillable hero, pictures it, and prints [ALLB] lines so the log shows which
		//floor an exception came from. Needs the real window, the headless boss test cannot see sprite or effect crashes.
		if ("allbosses".equals(System.getProperty("onyx.gallery.only"))) {
			allBosses();
			return;
		}

		//-Donyx.gallery.only=diamond walks the Diamond Knight fight (Magic Ling) through its phases by hand and
		//pictures each one, [DIAM] lines give the boss's HP, phase, sprite and alpha
		if ("diamond".equals(System.getProperty("onyx.gallery.only"))) {
			diamond();
			return;
		}

		//-Donyx.gallery.only=dragon walks the Dragon fight (Dragonic) through its phases by hand and pictures each one,
		//[DRAG] lines give the state, the dragon's HP and cell and how many dragon sprites are on screen
		if ("dragon".equals(System.getProperty("onyx.gallery.only"))) {
			dragon();
			return;
		}

		//-Donyx.gallery.only=final opens Abyssalnite's final arena (Cthulhu) once and leaves the window open to play
		if ("final".equals(System.getProperty("onyx.gallery.only"))) {
			if (!stayDone && time > 1.5f && Dungeon.hero != null && Dungeon.hero.ready) {
				stayDone = true;
				com.shatteredpixel.shatteredpixeldungeon.levels.SpecialFloors.travel(
						com.shatteredpixel.shatteredpixeldungeon.levels.FinalLevel::new );
			}
			return;
		}

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
				else if (FROM_WORLD != null) Gdx.app.exit();
				else time = 0;
			}
			return;
		}

		//-Donyx.gallery.only=bosses skips the monsters too
		if ("bosses".equals(System.getProperty("onyx.gallery.only"))) mob = MOBS.size();
		if ("ui".equals(System.getProperty("onyx.gallery.only"))) { mob = MOBS.size(); boss = BOSS_DEPTHS.length; }

		//-Donyx.gallery.only=branches pictures Matt Edition's branch floors (Ice Caves 14 to 18, Thieves Guild 8 and 9)
		if ("branches".equals(System.getProperty("onyx.gallery.only"))) {
			mob = MOBS.size();
			boss = BOSS_DEPTHS.length;
			if (branchStep >= BRANCH_FLOORS.length) {
				Gdx.app.exit();
				return;
			}
			int d = BRANCH_FLOORS[branchStep][0], b = BRANCH_FLOORS[branchStep][1];
			if (Dungeon.depth != d || Dungeon.branch != b) {
				if (time < 0.8f) return;
				InterlevelScene.curTransition = new LevelTransition(
						Dungeon.level, Dungeon.hero.pos, LevelTransition.Type.BRANCH_EXIT, d, b, null );
				InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
				time = 0;
				Game.switchScene( InterlevelScene.class );
				return;
			}
			if (Dungeon.hero.ready && time < 2.0f) Dungeon.hero.rest( false );
			if (time > 2.4f) {
				capture( String.format( "branch_%02d_%d_%s", d, b, Dungeon.level.getClass().getSimpleName() ) );
				branchStep++;
				time = 0;
			}
			return;
		}

		//the arena puts every chosen monster beside the hero at once, lets them act, takes one picture and quits
		if (ARENA) {
			if (!arenaSpawned) {
				if (time < 0.3f) return;
				for (Class<?> cls : MOBS) WndSandboxMobs.spawnNear( cls, 3 );
				System.out.println("[GALLERY] arena has " + Dungeon.level.mobs.size() + " monsters");
				arenaSpawned = true;
				Camera.main.zoom( 3 );
				time = 0;
				return;
			}
			if (Dungeon.hero.ready && time < 3.5f) Dungeon.hero.rest( false );
			if (time > 4f) {
				capture( "arena" );
				Gdx.app.exit();
			}
			return;
		}

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
		//Cursed PD's mana bar only shows once some mana is spent, so spend some for the pictures
		if (uiStep == 0 && uiWindow == null && Dungeon.hero.MAX_MP > 4) Dungeon.hero.MP = 4;
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
			case 5: uiWindow = new WndSongs(); break;
			case 6: uiWindow = new WndModInfo( WorldShuffle.create( WorldShuffle.worldCount() - 1 ) ); break;
			case 7: uiWindow = new WndHero(); break;
			case 8:
				//a few perks so the list has something in it
				Dungeon.hero.perks.clear();
				Dungeon.hero.perks.add( Perks.Perk.IRON_WILL );
				Dungeon.hero.perks.add( Perks.Perk.HOLD_FAST );
				Dungeon.hero.perks.add( Perks.Perk.FRIENDLY_BEES );
				uiWindow = new WndPerks( Dungeon.hero.perks, false );
				break;
			case 9: uiWindow = new WndSettings(); break;
			case 10:
				SandboxHotkeys.randomLevel();
				uiStep++;
				time = 0;
				return;
			case 11:
				if (time < 4f) return;
				capture( "hotkey_random_" + Dungeon.level.getClass().getSimpleName() );
				uiStep++;
				return;
			case 12:
				//a sample playlist, in the gallery's own settings
				SongPlayer.createPlaylist( "Upbeat" );
				if (SongPlayer.playlist( "Upbeat" ).isEmpty()) {
					int n = 0;
					for (SongPlayer.Song s : SongPlayer.modSongs()) {
						if (n++ >= 4) break;
						SongPlayer.togglePlaylist( "Upbeat", s.path );
					}
				}
				uiWindow = new WndPlaylists();
				break;
			case 13: uiWindow = new WndPlaylist( "Upbeat" ); break;
			case 14:
				WndSongs.addingTo = "Upbeat";
				uiWindow = new WndSongs();
				WndSongs.addingTo = null;
				break;
			case 15:
				//a song picked by hand, so the name shows at the top left, then its mid game window
				SongPlayer.play( SongPlayer.modSongs().get( 40 ).path );
				uiWindow = new WndNowPlaying( SongPlayer.nowPlaying() );
				break;
			case 16: uiWindow = new WndAddToPlaylist( WndSongs.byPath().get( SongPlayer.nowPlaying() ) ); break;
			case 17: uiWindow = new WndSongs(); break;
			default:
				Gdx.app.exit();
				return;
		}
		GameScene.show( uiWindow );
		time = 0;
	}

	//one entry per floor to visit. kind 0 is a boss floor (world, depth, variant), kind 1 a special floor, kind 2 a plain depth
	private static final ArrayList<int[]> TARGETS = new ArrayList<>();
	private static int target = -1;
	private static int targetStage = 0;
	private static int killRound = 0;
	private static boolean earlyShot = false;
	private static int targetOffset = 0;
	private static boolean exitTried = false;
	private static com.shatteredpixel.shatteredpixeldungeon.levels.Level levelBefore = null;

	private static int fullDepth = -1;
	private static int fullBranch = -1;
	private static float fullTime = 0;
	private static int fullKills = 0;
	private static boolean fullClimbing = false;
	private static boolean fullSentExit = false;
	private static int fullStuckCount = 0;

	private static boolean subclassSet = false;

	private static void fullRun(){
		if (Dungeon.hero == null) return;
		//-Donyx.gallery.subclass=CONSTRUCTOR gives the hero a subclass from the start
		if (!subclassSet && System.getProperty("onyx.gallery.subclass") != null) {
			subclassSet = true;
			Dungeon.hero.subClass = com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass.valueOf(System.getProperty("onyx.gallery.subclass"));
			com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent.initSubclassTalents(Dungeon.hero);
		}
		Dungeon.hero.HT = Math.max( Dungeon.hero.HT, 9999 );
		Dungeon.hero.HP = Dungeon.hero.HT;

		//a new floor arrived
		if (Dungeon.depth != fullDepth || Dungeon.branch != fullBranch) {
			fullDepth = Dungeon.depth;
			fullBranch = Dungeon.branch;
			fullTime = 0;
			fullKills = 0;
			fullSentExit = false;
			fullStuckCount = 0;
			System.out.println( "[FULL] arrived floor " + Dungeon.depth + " branch " + Dungeon.branch + " "
					+ Dungeon.level.getClass().getSimpleName() + (fullClimbing ? " (climbing)" : "") );
			return;
		}
		fullTime += Game.elapsed;
		if (!Dungeon.hero.ready) return;
		if (fullTime < 0.7f) return;

		//the first time on the way up, pick up the Amulet on floor 26
		if (!fullClimbing && Dungeon.depth == 26 && fullKills >= 3) {
			try {
				com.shatteredpixel.shatteredpixeldungeon.items.Amulet amulet = new com.shatteredpixel.shatteredpixeldungeon.items.Amulet();
				//the real pickup opens the Amulet scene, which this bot cannot click. This is what choosing to stay does.
				amulet.collect( Dungeon.hero.belongings.backpack );
				Statistics.amuletObtained = true;
				System.out.println( "[FULL] picked up the Amulet, amuletObtained " + Statistics.amuletObtained );
			} catch (Throwable e) {
				System.out.println( "[FULL] FAIL picking up the Amulet " + e );
				e.printStackTrace( System.out );
			}
			fullClimbing = true;
			fullDepth = -1; //so the next frame treats it as a fresh floor for the climb
			return;
		}

		//kill everything in rounds, later fight phases and summoned monsters die too
		if (fullKills < 3 && fullTime > 0.7f + fullKills * 1.4f) {
			fullKills++;
			for (com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob m : new ArrayList<>( Dungeon.level.mobs )) {
				if (m.isAlive() && m.alignment != com.shatteredpixel.shatteredpixeldungeon.actors.Char.Alignment.ALLY) {
					try {
						m.die( Dungeon.hero );
					} catch (Throwable e) {
						System.out.println( "[FULL] FAIL killing " + m.getClass().getSimpleName() + " on floor " + Dungeon.depth + " " + e );
						e.printStackTrace( System.out );
					}
				}
			}
			return;
		}
		if (fullKills < 3) return;

		if (Dungeon.level.locked) {
			if (fullTime > 8f) {
				System.out.println( "[FULL] STUCK floor " + Dungeon.depth + " is still sealed after the kills, unsealing it" );
				try { Dungeon.level.unseal(); } catch (Throwable e) { System.out.println( "[FULL] unseal threw " + e ); }
			}
			return;
		}

		if (!fullSentExit) {
			LevelTransition pick = null;
			LevelTransition.Type want = fullClimbing ? LevelTransition.Type.REGULAR_ENTRANCE : LevelTransition.Type.REGULAR_EXIT;
			for (LevelTransition t : Dungeon.level.transitions) {
				if (t.type != want) continue;
				if (pick == null) pick = t;
				boolean main = t.destBranch == 0 && t.destDepth == (fullClimbing ? Dungeon.depth - 1 : Dungeon.depth + 1);
				if (main) { pick = t; break; }
			}
			if (pick == null && !fullClimbing) {
				System.out.println( "[FULL] STUCK floor " + Dungeon.depth + " has no way down" );
				Gdx.app.exit();
				return;
			}
			if (pick == null) {
				System.out.println( "[FULL] no way up from floor " + Dungeon.depth + " branch " + Dungeon.branch );
				Gdx.app.exit();
				return;
			}
			if (fullClimbing && Dungeon.depth == 1 && Dungeon.branch == 0) {
				System.out.println( "[FULL] climbed all the way to floor 1, run complete" );
				Gdx.app.exit();
				return;
			}
			//on the way up, the floor below the amulet floor asks if you want to ascend. Choosing yes starts the ascension challenge.
			if (fullClimbing && Dungeon.hero.buff( com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge.class ) == null
					&& Dungeon.depth <= 25 && Dungeon.depth >= 24) {
				com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.affect( Dungeon.hero,
						com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge.class );
				System.out.println( "[FULL] chose to ascend on floor " + Dungeon.depth );
			}
			fullSentExit = true;
			fullTime = 0;
			try {
				boolean went = Dungeon.level.activateTransition( Dungeon.hero, pick );
				System.out.println( "[FULL] leaving floor " + Dungeon.depth + " went " + went );
			} catch (Throwable e) {
				System.out.println( "[FULL] FAIL leaving floor " + Dungeon.depth + " " + e );
				e.printStackTrace( System.out );
				Gdx.app.exit();
			}
			return;
		}

		//sent the exit but the floor did not change
		if (fullTime > 20f) {
			System.out.println( "[FULL] STUCK on floor " + Dungeon.depth + ", the exit did not move us in 20 seconds" );
			Gdx.app.exit();
		}
	}

	private static void allBosses(){
		if (TARGETS.isEmpty()) {
			int[] depths = { 5, 10, 15, 20, 25 };
			for (int w = 0; w < WorldShuffle.worldCount(); w++) {
				for (int d : depths) {
					int n = WorldShuffle.modBossVariants( w, d ).length;
					for (int v = 0; v < n; v++) TARGETS.add( new int[]{ 0, w, d, v } );
				}
			}
			for (int i = 0; i < com.shatteredpixel.shatteredpixeldungeon.levels.SpecialFloors.LIST.size(); i++) TARGETS.add( new int[]{ 1, i, 0, 0 } );
			for (int d : new int[]{ 5, 10, 15, 20, 25, 26 }) TARGETS.add( new int[]{ 2, 0, d, 0 } );
			//-Donyx.gallery.label=Town keeps only the special floors whose label contains that text
			String label = System.getProperty( "onyx.gallery.label" );
			if (label != null) {
				ArrayList<int[]> keep = new ArrayList<>();
				for (int[] t : TARGETS) {
					if (t[0] == 1 && com.shatteredpixel.shatteredpixeldungeon.levels.SpecialFloors.LIST.get( t[1] ).label.contains( label )) keep.add( t );
				}
				TARGETS.clear();
				TARGETS.addAll( keep );
			}
			int from = Integer.getInteger( "onyx.gallery.from", 0 ), count = Integer.getInteger( "onyx.gallery.count", Integer.MAX_VALUE );
			ArrayList<int[]> part = new ArrayList<>( TARGETS.subList( Math.min( from, TARGETS.size() ), (int)Math.min( (long)TARGETS.size(), (long)from + count ) ) );
			TARGETS.clear();
			TARGETS.addAll( part );
			targetOffset = from;
			System.out.println( "[ALLB] " + TARGETS.size() + " floors to visit, starting at " + from );
		}
		//an unkillable hero, so the fight can run
		if (Dungeon.hero != null) {
			Dungeon.hero.HT = Math.max( Dungeon.hero.HT, 9999 );
			Dungeon.hero.HP = Dungeon.hero.HT;
		}
		if (target >= TARGETS.size()) {
			System.out.println( "[ALLB] all done" );
			Gdx.app.exit();
			return;
		}
		if (target < 0) {
			if (time < 1.5f || Dungeon.hero == null || !Dungeon.hero.ready) return;
			target = 0;
			targetStage = 0;
		}
		int[] t = TARGETS.get( target );
		if (targetStage == 0) {
			String name;
			if (t[0] == 0) {
				name = "boss world " + t[1] + " depth " + t[2] + " variant " + t[3];
				WorldShuffle.forcedVariant = t[3];
				Dungeon.sandboxWorld = t[1];
				Dungeon.sandboxSpecial = null;
				InterlevelScene.mode = InterlevelScene.Mode.SANDBOX;
				InterlevelScene.returnDepth = t[2];
			} else if (t[0] == 1) {
				name = "special " + com.shatteredpixel.shatteredpixeldungeon.levels.SpecialFloors.LIST.get( t[1] ).label;
				WorldShuffle.forcedVariant = -1;
				Dungeon.sandboxSpecial = com.shatteredpixel.shatteredpixeldungeon.levels.SpecialFloors.LIST.get( t[1] ).make;
				InterlevelScene.mode = InterlevelScene.Mode.SANDBOX;
				InterlevelScene.returnDepth = 12;
			} else {
				name = "plain depth " + t[2];
				WorldShuffle.forcedVariant = -1;
				Dungeon.sandboxWorld = -1;
				Dungeon.sandboxSpecial = null;
				InterlevelScene.mode = InterlevelScene.Mode.SANDBOX;
				InterlevelScene.returnDepth = t[2];
			}
			System.out.println( "[ALLB] start " + (target + targetOffset) + " " + name );
			levelBefore = Dungeon.level;
			com.shatteredpixel.shatteredpixeldungeon.levels.Level.beforeTransition();
			time = 0;
			targetStage = 1;
			Game.switchScene( InterlevelScene.class );
			return;
		}
		if (Dungeon.level == levelBefore || Dungeon.hero == null) return;
		if (targetStage == 1) {
			System.out.println( "[ALLB] arrived " + target + " " + Dungeon.level.getClass().getSimpleName() + " on floor " + Dungeon.depth );
			targetStage = 2;
			time = 0;
		}
		if (Dungeon.hero.ready && time < 5f) Dungeon.hero.rest( false );
		//a picture of the floor itself, before the kills and the exit
		if (time > 2.5f && !earlyShot) {
			earlyShot = true;
			capture( String.format( "early_%03d_%s", target + targetOffset, Dungeon.level.getClass().getSimpleName() ) );
		}
		//after the fight has run, kill every monster the way a win would, three rounds so later phases are killed too,
		//then try to leave by the exit
		if (time > 5f && killRound < 3 && time > 5f + killRound * 1.5f) {
			killRound++;
			System.out.println( "[ALLB] killing round " + killRound );
			for (com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob m : new ArrayList<>( Dungeon.level.mobs )) {
				if (m.isAlive()) { try { if (killRound < 3 && m.properties().contains( com.shatteredpixel.shatteredpixeldungeon.actors.Char.Property.BOSS )) m.damage( Math.max( 1, m.HT * 4 / 10 ), Dungeon.hero ); else m.die( Dungeon.hero ); } catch (Throwable e) { System.out.println( "[ALLB] FAIL killing " + m.getClass().getSimpleName() + " on " + Dungeon.level.getClass().getSimpleName() + " " + e ); e.printStackTrace( System.out ); } }
			}
		}
		if (time > 10f && !exitTried) {
			exitTried = true;
			try {
				LevelTransition ex = Dungeon.level.getTransition( LevelTransition.Type.REGULAR_EXIT );
				if (ex != null) {
					boolean went = Dungeon.level.activateTransition( Dungeon.hero, ex );
					System.out.println( "[ALLB] used the exit, went " + went );
				}
			} catch (Throwable e) {
				System.out.println( "[ALLB] exit lookup failed " + e );
			}
		}
		if (time > 14f) {
			capture( String.format( "allb_%03d_%s", target, Dungeon.level.getClass().getSimpleName() ) );
			killRound = 0;
			exitTried = false;
			earlyShot = false;
			System.out.println( "[ALLB] ok " + target );
			target++;
			targetStage = 0;
			time = 0;
		}
	}

	private static int diamondStep = 0;

	private static com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.DiamondKnight diamondBoss(){
		for (com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob m : Dungeon.level.mobs) {
			if (m instanceof com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.DiamondKnight) {
				return (com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.DiamondKnight) m;
			}
		}
		return null;
	}

	private static boolean diamondInScene( com.watabou.noosa.Gizmo g ){
		while (g.parent != null) g = g.parent;
		return g == Game.scene();
	}

	private static void diamondLog( String what ){
		com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.DiamondKnight b = diamondBoss();
		com.shatteredpixel.shatteredpixeldungeon.levels.ColdChestBossLevel l = (com.shatteredpixel.shatteredpixeldungeon.levels.ColdChestBossLevel) Dungeon.level;
		System.out.println( "[DIAM] " + what + " state " + l.pro + (b == null ? " no boss" : " HP " + b.HP + "/" + b.HT + " phase " + b.phase + " pos " + b.pos
				+ " sprite " + (b.sprite == null ? "null" : b.sprite.getClass().getSimpleName() + " alpha " + b.sprite.alpha() + " visible " + b.sprite.visible
				+ " parent " + (b.sprite.parent != null) + " xy " + b.sprite.x + "," + b.sprite.y
				+ " frame " + b.sprite.frame().left + "," + b.sprite.frame().top + "," + b.sprite.frame().right + "," + b.sprite.frame().bottom + " cls2 " + b.sprite.getClass().getName() + " ox " + b.sprite.origin.x + " wh " + b.sprite.width + "x" + b.sprite.height + " scale " + b.sprite.scale
				+ " mul " + b.sprite.rm + "," + b.sprite.gm + "," + b.sprite.bm + "," + b.sprite.am + " add " + b.sprite.ra + "," + b.sprite.ga + "," + b.sprite.ba + "," + b.sprite.aa
				+ " tex " + (b.sprite.texture == null ? "null" : b.sprite.texture.width + "x" + b.sprite.texture.height)
				+ " tier " + Dungeon.hero.tier() + " cls " + Dungeon.hero.heroClass
				+ " inScene " + diamondInScene( b.sprite )) ) );
	}

	private static void diamond(){
		if (Dungeon.hero != null) Dungeon.hero.HP = Dungeon.hero.HT;
		if (diamondStep == 0) {
			if (time < 1.5f || Dungeon.hero == null || !Dungeon.hero.ready) return;
			diamondStep = 1;
			com.shatteredpixel.shatteredpixeldungeon.levels.SpecialFloors.travel(
					com.shatteredpixel.shatteredpixeldungeon.levels.ColdChestBossLevel::new );
			time = 0;
			return;
		}
		if (!(Dungeon.level instanceof com.shatteredpixel.shatteredpixeldungeon.levels.ColdChestBossLevel)) { time = 0; return; }
		com.shatteredpixel.shatteredpixeldungeon.levels.ColdChestBossLevel l = (com.shatteredpixel.shatteredpixeldungeon.levels.ColdChestBossLevel) Dungeon.level;
		com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.DiamondKnight b = diamondBoss();
		//turns only pass while the hero acts
		if (diamondStep >= 2 && Dungeon.hero.ready) Dungeon.hero.rest( false );
		try {
			if (diamondStep == 1 && time > 2f) {
				diamondStep = 2;
				l.progress();
				diamondLog( "fight started" );
			} else if (diamondStep == 2 && time > 4f) {
				diamondStep = 3;
				diamondLog( "before maze" );
				capture( "diam_1_start" );
				if (b != null) b.damage( b.HP - 355, Dungeon.hero );
			} else if (diamondStep == 3 && time > 6f) {
				diamondStep = 4;
				diamondLog( "maze opened" );
				capture( "diam_2_maze" );
				com.shatteredpixel.shatteredpixeldungeon.MagicLingStats.TPDoorDieds = true;
				com.shatteredpixel.shatteredpixeldungeon.MagicLingStats.KillMazeMimic = 5;
				l.occupyCell( Dungeon.hero );
			} else if (diamondStep == 4 && time > 8f) {
				diamondStep = 5;
				diamondLog( "after maze" );
				capture( "diam_3_aftermaze" );
				if (b != null) b.damage( b.HP - 238, Dungeon.hero );
			} else if (diamondStep == 5 && time > 10f) {
				diamondStep = 6;
				diamondLog( "at 240" );
				capture( "diam_4_240" );
				if (b != null) b.damage( b.HP - 195, Dungeon.hero );
			} else if (diamondStep == 6 && time > 13f) {
				diamondStep = 7;
				diamondLog( "copy phase" );
				capture( "diam_5_copy" );
			} else if (diamondStep == 7 && time > 14f) {
				System.out.println( "[DIAM] done" );
				Gdx.app.exit();
			}
		} catch (Throwable e) {
			System.out.println( "[DIAM] FAIL at step " + diamondStep + " " + e );
			e.printStackTrace( System.out );
			diamondStep++;
		}
	}

	private static int dragonStep = 0;

	private static int dragonSprites( com.watabou.noosa.Group g ){
		int n = 0;
		java.util.List<com.watabou.noosa.Gizmo> members;
		try {
			java.lang.reflect.Field f = com.watabou.noosa.Group.class.getDeclaredField( "members" );
			f.setAccessible( true );
			members = new java.util.ArrayList<>( (java.util.List<com.watabou.noosa.Gizmo>) f.get( g ) );
		} catch (Exception e) {
			return -1;
		}
		for (com.watabou.noosa.Gizmo m : members) {
			if (m instanceof com.shatteredpixel.shatteredpixeldungeon.sprites.DragonSprite && m.alive && m.visible) n++;
			else if (m instanceof com.watabou.noosa.Group) n += dragonSprites( (com.watabou.noosa.Group) m );
		}
		return n;
	}

	private static void dragonLog( String what ){
		com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Dragon d = com.shatteredpixel.shatteredpixeldungeon.levels.DragonBossLevel.dragon;
		System.out.println( "[DRAG] " + what + " state " + com.shatteredpixel.shatteredpixeldungeon.levels.DragonBossLevel.state
				+ (d == null ? " no dragon" : " HP " + d.HP + "/" + d.HT + " pos " + d.pos + " inLevel " + Dungeon.level.mobs.contains( d ))
				+ " hero " + Dungeon.hero.pos + " sprites " + dragonSprites( Game.scene() ) + " exit " + Dungeon.level.exit() + " sealed " + Dungeon.level.locked );
	}

	private static void dragonMove( int cell ){
		Dungeon.hero.pos = cell;
		Dungeon.hero.sprite.place( cell );
		Dungeon.level.occupyCell( Dungeon.hero );
		Dungeon.observe();
	}

	private static void dragonHurt( int toHP ){
		com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Dragon d = com.shatteredpixel.shatteredpixeldungeon.levels.DragonBossLevel.dragon;
		if (d != null && d.HP > toHP) d.damage( d.HP - toHP, Dungeon.hero );
	}

	private static void dragon(){
		if (Dungeon.hero != null) Dungeon.hero.HP = Dungeon.hero.HT;
		if (dragonStep == 0) {
			if (time < 1.5f || Dungeon.hero == null || !Dungeon.hero.ready) return;
			dragonStep = 1;
			com.shatteredpixel.shatteredpixeldungeon.levels.SpecialFloors.travel(
					com.shatteredpixel.shatteredpixeldungeon.levels.DragonBossLevel::new );
			time = 0;
			return;
		}
		if (dragonStep == 10 && !(Dungeon.level instanceof com.shatteredpixel.shatteredpixeldungeon.levels.DragonBossLevel) && time > 3f) {
			System.out.println( "[DRAG] went down the exit to depth " + Dungeon.depth + ", " + Dungeon.level.getClass().getSimpleName() );
			capture( "drag_9_below" );
			Gdx.app.exit();
			return;
		}
		if (!(Dungeon.level instanceof com.shatteredpixel.shatteredpixeldungeon.levels.DragonBossLevel)) { if (dragonStep != 10) time = 0; return; }
		com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Dragon d = com.shatteredpixel.shatteredpixeldungeon.levels.DragonBossLevel.dragon;
		int w = Dungeon.level.width();
		//turns only pass while the hero acts
		if (dragonStep >= 2 && Dungeon.hero.ready) Dungeon.hero.rest( false );
		try {
			if (dragonStep == 1 && time > 2f) {
				dragonStep = 2;
				capture( "drag_0_arrive" );
				dragonMove( 20 * w + 10 );
				dragonLog( "walked into the hall" );
			} else if (dragonStep == 2 && time > 5f) {
				dragonStep = 3;
				dragonLog( "fire rings" );
				capture( "drag_1_rings" );
				//the rings have to finish before the dragon can pass three quarters, as in play
				java.lang.reflect.Field f = d.getClass().getDeclaredField( "fireComplete" );
				f.setAccessible( true );
				f.setBoolean( d, true );
				dragonHurt( 3 * (d.HT / 4) + 1 );
				dragonHurt( 3 * (d.HT / 4) - 2 );
				dragonLog( "jumped" );
			} else if (dragonStep == 3 && time > 7f) {
				dragonStep = 4;
				dragonLog( "fire wave" );
				capture( "drag_2_jump" );
			} else if (dragonStep == 4 && time > 10f) {
				dragonStep = 5;
				dragonLog( "fire wave later" );
				capture( "drag_3_wave" );
				if ("half".equals( System.getProperty( "onyx.gallery.dragon" ) )) {
					//the other way out of stage two, beating the dragon to half before reaching the trap
					dragonHurt( d.HT / 2 - 1 );
					dragonLog( "hurt to half" );
				} else {
					dragonMove( 24 * w + 19 );
					dragonLog( "stepped on the trap" );
				}
			} else if (dragonStep == 5 && time > 13f) {
				dragonStep = 6;
				dragonLog( "maze" );
				capture( "drag_4_maze" );
				if (com.shatteredpixel.shatteredpixeldungeon.levels.DragonBossLevel.state == com.shatteredpixel.shatteredpixeldungeon.levels.DragonBossLevel.State.MAZE) {
					dragonMove( 3 * w + 21 );
					dragonLog( "left the maze" );
				}
			} else if (dragonStep == 6 && time > 16f) {
				dragonStep = 7;
				dragonLog( "arena" );
				capture( "drag_5_arena" );
				dragonHurt( 0 );
				dragonLog( "dragon beaten" );
			} else if (dragonStep == 7 && time > 19f) {
				dragonStep = 8;
				dragonLog( "end" );
				capture( "drag_6_end" );
				//stand next to the way down so the picture shows it
				int exit = Dungeon.level.exit();
				dragonMove( exit - w );
				System.out.println( "[DRAG] exit cell " + exit + " terrain " + Dungeon.level.map[exit] + " transition " + Dungeon.level.getTransition( exit )
						+ " passable " + Dungeon.level.passable[exit] + " mapped " + Dungeon.level.mapped[exit] + " visited " + Dungeon.level.visited[exit] );
			} else if (dragonStep == 8 && time > 21f) {
				dragonStep = 9;
				capture( "drag_7_exit" );
				com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene.resetMap();
			} else if (dragonStep == 9 && time > 21.6f) {
				dragonStep = 10;
				capture( "drag_8_exit_redrawn" );
				int ex = Dungeon.level.exit();
				System.out.println( "[DRAG] hero " + Dungeon.hero.pos + " fov " + Dungeon.level.heroFOV[ex] + " visited " + Dungeon.level.visited[ex]
						+ " losBlocking " + Dungeon.level.losBlocking[ex] + " below " + Dungeon.level.map[ex + w] + " depth " + Dungeon.depth );
				Dungeon.hero.handle( ex );
			} else if (dragonStep == 10 && time > 24f) {
				System.out.println( "[DRAG] after walking onto the exit, depth " + Dungeon.depth + " level " + Dungeon.level.getClass().getSimpleName() + " scene " + Game.scene().getClass().getSimpleName() );
				System.out.println( "[DRAG] done" );
				Gdx.app.exit();
			}
		} catch (Throwable e) {
			System.out.println( "[DRAG] FAIL at step " + dragonStep + " " + e );
			e.printStackTrace( System.out );
			dragonStep++;
		}
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

	private static int heroStep = -1;
	private static float heroTime = 0;

	/** Called once per frame on the hero select screen. Picks each hero class a few frames apart, [HERO] lines show the last one picked. */
	public static void onHeroSelect( com.shatteredpixel.shatteredpixeldungeon.scenes.HeroSelectScene scene ){
		if (!active() || !"heroselect".equals(System.getProperty("onyx.gallery.only"))) return;
		heroTime += Game.elapsed;
		if (heroTime < 0.4f) return;
		heroTime = 0;
		HeroClass[] all = HeroClass.values();
		if (heroStep >= 0 && heroStep < all.length) {
			System.out.println("[HERO] drawn " + all[heroStep].name());
			if (heroStep == 0 || all[heroStep].name().startsWith("RBA_ARIS") || all[heroStep] == HeroClass.RAT_KING) {
				capture("hero_" + all[heroStep].name());
			}
		}
		heroStep++;
		if (heroStep >= all.length) {
			System.out.println("[HERO] done");
			Gdx.app.exit();
			return;
		}
		System.out.println("[HERO] picking " + all[heroStep].name());
		scene.gallerySelect(all[heroStep]);
		System.out.println("[HERO] picked " + all[heroStep].name());
	}

	private static void capture( String name ){
		Pixmap pixmap = Pixmap.createFromFrameBuffer( 0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight() );
		PixmapIO.writePNG( Gdx.files.absolute( DIR + "/" + name + ".png" ), pixmap, 6, true );
		pixmap.dispose();
	}

}
