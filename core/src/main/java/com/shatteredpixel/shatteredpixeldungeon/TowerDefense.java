/*
 * Onyx's Pixel Dungeon
 * Copyright (C) 2026 Onyx Rothman
 *
 * Pixel Towers / Tower Pixel Dungeon
 * Copyright (C) 2024-2025 FixAkaTheFix
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

import com.shatteredpixel.shatteredpixeldungeon.levels.Arena;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena1;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena10;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena11;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena12;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena13;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena14;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena15;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena16;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena17;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena18;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena19;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena2;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena20;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena21;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena22;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena23;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena24;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena25;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena3;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena4;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena5;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena6;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena7;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena8;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena9;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.endlessarenas.EndlessArena1;
import com.shatteredpixel.shatteredpixeldungeon.levels.endlessarenas.EndlessArena2;
import com.shatteredpixel.shatteredpixeldungeon.levels.endlessarenas.EndlessArena3;
import com.shatteredpixel.shatteredpixeldungeon.levels.endlessarenas.EndlessArena4;
import com.shatteredpixel.shatteredpixeldungeon.levels.endlessarenas.EndlessArena5;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.towerlist.TowerInfo;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndModes;
import com.watabou.utils.Bundle;
import com.watabou.utils.Reflection;

import java.util.Arrays;
import java.util.List;

/**
 * Tower PD's tower defense game as a mode of its own, started from the Tower defense button on the title screen.
 * The flow is the mod's. Pick a hero, then an arena on the level select screen (TowerLevelSelectScene), with its
 * difficulty mode and the four towers to take (TowerSelectScene). Story mode has the 25 arenas in order, each
 * one unlocked by winning the one before it in the same mode. Endless mode has five arenas that open as the story
 * goes on. Each arena is a short run of its own. Winning one shows WndTowerVictory, which starts the next arena with
 * a fresh hero, returns to the title, or lets the hero stay. Progress, chosen towers and lifetime numbers are kept in
 * the settings, never in the run, like the mod. Tower defense runs are not ranked.
 */
public class TowerDefense {

	/** True while a tower defense run is being set up or played. Set by the title screen, saved with the run. */
	public static boolean active = false;
	/** Endless mode instead of story mode. */
	public static boolean endless = false;
	/** Which arena of the current list, counted from 1 like the mod's level select. */
	public static int arena = 1;
	/** Set once the current arena is won. Taking the exit under the portal then leads to the next arena. */
	public static boolean cleared = false;

	public static final List<Class<? extends Arena>> STORY = Arrays.asList(
			Arena1.class, Arena2.class, Arena3.class, Arena4.class, Arena5.class,
			Arena6.class, Arena7.class, Arena8.class, Arena9.class, Arena10.class,
			Arena11.class, Arena12.class, Arena13.class, Arena14.class, Arena15.class,
			Arena16.class, Arena17.class, Arena18.class, Arena19.class, Arena20.class,
			Arena21.class, Arena22.class, Arena23.class, Arena24.class, Arena25.class );

	public static final List<Class<? extends Arena>> ENDLESS = Arrays.asList(
			EndlessArena1.class, EndlessArena2.class, EndlessArena3.class, EndlessArena4.class, EndlessArena5.class );

	public static List<Class<? extends Arena>> list(){
		return endless ? ENDLESS : STORY;
	}

	public static Class<? extends Arena> chosen(){
		arena = Math.max( 1, Math.min( arena, list().size() ) );
		return list().get( arena - 1 );
	}

	//---------------------------------------------------------------- progress, kept in the settings

	private static String unlockKey( WndModes.Modes mode ){
		return "td_unlocked_" + mode.name().toLowerCase( java.util.Locale.ROOT );
	}

	/** The highest story arena open in this mode. Hard and challenge never go past normal, as in the mod. */
	public static int unlocked( WndModes.Modes mode ){
		int normal = SPDSettings.getInt( unlockKey( WndModes.Modes.NORMAL ), 1 );
		if (mode == WndModes.Modes.NORMAL) return normal;
		return Math.min( normal, SPDSettings.getInt( unlockKey( mode ), 1 ) );
	}

	/** Whether an arena of the current list may be played. */
	public static boolean isUnlocked( int index ){
		int normal = unlocked( WndModes.Modes.NORMAL );
		if (endless) {
			//the mod opened endless arenas as the story reached each region
			if (index <= 2) return true;
			if (index == 3) return normal > 10;
			return normal > 15;
		}
		return index <= unlocked( WndModes.current );
	}

	/** Tower PD's towers unlock by story progress, see TowerInfo.getTowerUnlockLevel. */
	public static int towerProgress(){
		return unlocked( WndModes.Modes.NORMAL );
	}

	/** The tower in one of the four slots the tower shop sells from. The mod's own defaults. */
	public static TowerInfo.AllTowers slot( int n ){
		int def = n == 1 ? 302 : n == 2 ? 303 : 306;
		TowerInfo.AllTowers t = TowerInfo.getTowerByIndex( SPDSettings.getInt( "td_slot" + n, def ) );
		return t == null ? TowerInfo.AllTowers.WALL : t;
	}

	/** For Level's slot fields. A tower defense run uses the chosen towers, anything else keeps its default. */
	public static TowerInfo.AllTowers slot( int n, TowerInfo.AllTowers fallback ){
		return active ? slot( n ) : fallback;
	}

	/** Equips a tower the way the mod did. It goes in the first slot and the others move down one. */
	public static void equip( TowerInfo.AllTowers tower ){
		for (int n = 1; n <= 4; n++) if (slot( n ) == tower) return;
		for (int n = 4; n > 1; n--) SPDSettings.put( "td_slot" + n, TowerInfo.getTowerIndex( slot( n - 1 ) ) );
		SPDSettings.put( "td_slot1", TowerInfo.getTowerIndex( tower ) );
	}

	public static int wins(){
		return SPDSettings.getInt( "td_wins", 0 );
	}

	//---------------------------------------------------------------- the run

	/** Builds the arena for a tower defense run, called from Dungeon.newLevel. */
	public static Level build(){
		//the exit under a held portal leads on to the next arena with the same hero
		if (Dungeon.hero != null && cleared && arena < list().size()) arena++;
		cleared = false;
		//endless arenas are always normal, as in the mod
		if (endless) WndModes.current = WndModes.Modes.NORMAL;
		Arena a = Reflection.newInstance( chosen() );
		if (a.arenaDepth > 0) Dungeon.depth = a.arenaDepth;
		return a;
	}

	/** Called by Arena.completeStage. Opens the next arena and shows the victory window. */
	public static void onStageComplete(){
		if (!active || cleared) return;
		cleared = true;
		SPDSettings.put( "td_wins", wins() + 1 );
		if (!endless && Dungeon.level instanceof Arena) {
			WndModes.Modes mode = Dungeon.level.mode;
			int next = arena + 1;
			if (SPDSettings.getInt( unlockKey( mode ), 1 ) < next) SPDSettings.put( unlockKey( mode ), next );
		}
		if (ShatteredPixelDungeon.scene() instanceof GameScene) {
			GameScene.show( new com.shatteredpixel.shatteredpixeldungeon.windows.WndTowerVictory() );
		}
	}

	/** Whether the next arena exists and is open. */
	public static boolean hasNext(){
		if (arena >= list().size()) return false;
		arena++;
		boolean open = isUnlocked( arena );
		arena--;
		return open;
	}

	/** Starts the next arena as a fresh run with the same hero class, the way the mod went back to its level select. */
	public static void startNext(){
		GamesInProgress.selectedClass = Dungeon.hero.heroClass;
		Dungeon.deleteGame( GamesInProgress.curSlot, true );
		arena++;
		cleared = false;
		start();
	}

	/** Starts a fresh run in the chosen arena. */
	public static void start(){
		active = true;
		cleared = false;
		Dungeon.hero = null;
		Dungeon.daily = Dungeon.dailyReplay = false;
		Dungeon.sandbox = false;
		Dungeon.bossCrawl = false;
		Dungeon.initSeed();
		com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator.clearAction();
		com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene.mode = com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene.Mode.DESCEND;
		com.watabou.noosa.Game.switchScene( com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene.class );
	}

	/** Leaves the run and goes back to the title screen, the run is deleted as in the mod. */
	public static void quit(){
		Dungeon.deleteGame( GamesInProgress.curSlot, true );
		active = false;
		ShatteredPixelDungeon.switchScene( com.shatteredpixel.shatteredpixeldungeon.scenes.TitleScene.class );
	}

	/** Tower defense runs are kept out of the rankings. */
	public static boolean unranked(){
		return active;
	}

	private static final String ACTIVE = "tower_defense";

	public static void store( Bundle bundle ){
		if (!active) return;
		bundle.put( ACTIVE, true );
		bundle.put( "td_endless", endless );
		bundle.put( "td_arena", arena );
		bundle.put( "td_cleared", cleared );
		bundle.put( "td_mode", WndModes.current.name() );
	}

	public static void restore( Bundle bundle ){
		active = bundle.getBoolean( ACTIVE );
		if (!active) return;
		endless = bundle.getBoolean( "td_endless" );
		arena = bundle.getInt( "td_arena" );
		cleared = bundle.getBoolean( "td_cleared" );
		try {
			WndModes.current = WndModes.Modes.valueOf( bundle.getString( "td_mode" ) );
		} catch (Exception e) {
			WndModes.current = WndModes.Modes.NORMAL;
		}
	}
}
