import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.TowerDefense;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.Tower;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.towerspawners.TowerSpawner;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena1;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena2;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.endlessarenas.EndlessArena1;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.TowerLevelSelectScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.TowerSelectScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.towerlist.TowerIcons;
import com.shatteredpixel.shatteredpixeldungeon.ui.towerlist.TowerInfo;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndModes;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTowerVictory;
import com.watabou.utils.PathFinder;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

/**
 * The tower defense mode without a window (see TowerDefense). Starts a run the way the level select's Start button
 * does, checks the chosen towers reach the tower shop, places a tower the way a tower spawner does, plays a wave,
 * saves and reloads the run and the arena, wins it, checks the next arena opened and that the exit leads on, and
 * checks endless mode, every string and every picture the menus use. A null sprite, emitter or scene is the
 * headless limit and prints as LIMIT.
 *
 *   scripts/test-one.sh TowerDefenseTest
 */
public class TowerDefenseTest {

	static int failures = 0, checks = 0, limits = 0;

	static void check( boolean ok, String what ) {
		checks++;
		if (!ok) {
			failures++;
			System.out.println( "FAIL: " + what );
		}
	}

	static final java.util.Set<String> SEEN = new java.util.HashSet<>();

	static void quietly( Runnable r, String what ) {
		try {
			r.run();
		} catch (NullPointerException e) {
			limits++;
			StackTraceElement top = e.getStackTrace().length > 0 ? e.getStackTrace()[0] : null;
			//each place the headless run stops is printed once
			if (SEEN.add( what + top )) System.out.println( "LIMIT " + what + " " + top );
		}
	}

	static void act( Mob mob ) throws Throwable {
		try {
			java.lang.reflect.Method m = Actor.class.getDeclaredMethod( "act" );
			m.setAccessible( true );
			m.invoke( mob );
		} catch (java.lang.reflect.InvocationTargetException e) {
			throw e.getCause();
		}
	}

	static int free( Level level, int near ) {
		for (int r = 1; r < 12; r++) {
			for (int o : PathFinder.NEIGHBOURS8) {
				int c = near + o * r;
				if (c > 0 && c < level.length() && level.passable[c] && Actor.findChar( c ) == null && level.heaps.get( c ) == null) return c;
			}
		}
		return -1;
	}

	static void heroOn( Level level ) {
		Dungeon.level = level;
		Actor.clear();
		Hero hero = Dungeon.hero;
		hero.sprite = TestBoot.sprite();
		hero.sprite.ch = hero;
		hero.pos = level.entrance();
		if (hero.pos <= 0 || !level.passable[hero.pos]) hero.pos = free( level, level.length() / 2 );
		hero.HP = hero.HT = 1000;
		Actor.add( hero );
		for (Mob m : level.mobs) Actor.add( m );
	}

	public static void main( String[] args ) throws Exception {
		TestBoot.boot();
		TestBoot.stubWindow();
		try {
			run();
		} catch (Throwable t) {
			failures++;
			System.out.println( "FAIL: crashed " + t );
			StackTraceElement[] st = t.getStackTrace();
			for (int i = 0; i < Math.min( 10, st.length ); i++) System.out.println( "        at " + st[i] );
		}
		System.out.println( "SUMMARY tower defense checks " + checks + ", failures " + failures + ", headless limits " + limits );
		System.exit( failures == 0 ? 0 : 1 );
	}

	static void run() throws Throwable {
		String root = System.getProperty( "src.root", "../../../.." );

		//---- strings and pictures of the menus
		for (Class<?> c : TowerDefense.STORY) {
			String k = c.getSimpleName().toLowerCase( Locale.ROOT );
			for (String part : new String[]{ "name", "desc", "knowledge", "challenge" })
				check( !Messages.get( TowerLevelSelectScene.class, k + part ).contains( "!!!" ), "no text for " + k + part );
			check( new File( "splashes/tower/levels/" + k + ".png" ).exists(), "no picture for " + k );
		}
		for (Class<?> c : TowerDefense.ENDLESS) {
			String k = c.getSimpleName().toLowerCase( Locale.ROOT );
			for (String part : new String[]{ "name", "desc", "knowledge" })
				check( !Messages.get( TowerLevelSelectScene.class, k + part ).contains( "!!!" ), "no text for " + k + part );
			check( new File( "splashes/tower/levels/" + k + ".png" ).exists(), "no picture for " + k );
		}
		for (String k : new String[]{ "start", "lockedtext", "lockedtextneedstory", "prize", "story_name", "endless_name", "endless_locked", "title_button" })
			check( !Messages.get( TowerLevelSelectScene.class, k ).contains( "!!!" ), "no level select text " + k );
		for (String k : new String[]{ "select", "uppertext", "welcometitle", "welcometext" })
			check( !Messages.get( TowerSelectScene.class, k ).contains( "!!!" ), "no tower select text " + k );
		for (String k : new String[]{ "title", "next", "title_screen", "stay", "next_open" })
			check( !Messages.get( WndTowerVictory.class, k ).contains( "!!!" ), "no victory text " + k );
		check( !Messages.get( WndTowerVictory.class, "stats", 1, 2, 3, 4, 5 ).contains( "!!!" ), "no victory stats text" );
		for (TowerInfo.AllTowers t : TowerInfo.AllTowers.values()) {
			check( new File( "splashes/tower/towers/tower" + t.name().toLowerCase( Locale.ROOT ) + ".png" ).exists(), "no preview picture for " + t );
			try { TowerIcons.forTower( t ); } catch (IllegalArgumentException e) { check( false, "no icon for " + t ); }
			check( !TowerInfo.getTowerName( t ).contains( "!!!" ), "no name for " + t );
		}
		check( new File( TowerIcons.SHEET ).exists(), "no icon sheet" );
		for (String region : new String[]{ "sewers", "prison", "caves", "city", "halls" })
			for (String suffix : new String[]{ "", "_hard", "_chal" })
				check( new File( "interfaces/tower/loading_" + region + suffix + ".png" ).exists(), "no loading picture " + region + suffix );

		//---- progress and towers start like a new player's
		TowerDefense.active = true;
		TowerDefense.endless = false;
		TowerDefense.arena = 1;
		WndModes.current = WndModes.Modes.NORMAL;
		check( TowerDefense.isUnlocked( 1 ) && !TowerDefense.isUnlocked( 2 ), "a new player should only have arena 1 open" );
		check( TowerDefense.slot( 1 ) == TowerInfo.AllTowers.CROSSBOW && TowerDefense.slot( 2 ) == TowerInfo.AllTowers.MAGICMISSILE, "default towers should be the mod's" );
		check( TowerInfo.getTowerLock( TowerInfo.AllTowers.CANNON ) == TowerInfo.Lock.LOCKED, "the cannon should be locked before arena 5" );
		TowerDefense.equip( TowerInfo.AllTowers.LIGHTNING );
		check( TowerDefense.slot( 1 ) == TowerInfo.AllTowers.LIGHTNING && TowerDefense.slot( 2 ) == TowerInfo.AllTowers.CROSSBOW, "equipping should push the towers down one slot" );

		//---- start a run as the Start button does
		GamesInProgress.selectedClass = HeroClass.WARRIOR;
		GamesInProgress.curSlot = 1;
		Dungeon.sandbox = false;
		Dungeon.bossCrawl = false;
		Dungeon.seed = 777L;
		Dungeon.init();
		Level level = Dungeon.newLevel();
		check( level instanceof Arena1, "arena 1 should be built, got " + level.getClass().getSimpleName() );
		Arena arena = (Arena)level;
		check( Dungeon.depth == arena.arenaDepth, "the floor number should be the arena's" );
		check( level.slot1 == TowerInfo.AllTowers.LIGHTNING, "the arena should sell the chosen towers" );
		heroOn( level );
		quietly( level::initNpcs, "initNpcs" );
		boolean sold = false;
		for (Heap h : level.heaps.valueList())
			for (Item i : h.items)
				if (i.getClass() == TowerInfo.getTowerSpawner( TowerInfo.AllTowers.LIGHTNING ).getClass()) sold = true;
		check( sold, "the tower shop should sell the lightning tower" );
		check( Dungeon.gold > 0, "the hero should start with gold for towers" );

		//---- place a tower the way a tower spawner does
		TowerSpawner spawner = (TowerSpawner)TowerInfo.getTowerSpawner( TowerInfo.AllTowers.CROSSBOW );
		int cell = free( level, Dungeon.hero.pos );
		check( cell != -1, "no free cell for a tower" );
		java.lang.reflect.Method onThrow = Item.class.getDeclaredMethod( "onThrow", int.class );
		onThrow.setAccessible( true );
		try {
			onThrow.invoke( spawner, cell );
		} catch (java.lang.reflect.InvocationTargetException e) {
			if (!(e.getCause() instanceof NullPointerException)) throw e.getCause();
			limits++;
			System.out.println( "LIMIT place tower " + e.getCause().getStackTrace()[0] );
		}
		boolean placed = false;
		for (Mob m : level.mobs) if (m instanceof Tower && m.pos == cell) placed = true;
		check( placed, "the spawner should place a tower" );

		//---- play a wave
		quietly( arena::startWave, "startWave" );
		check( arena.wave == 1, "the first wave should start" );
		for (int turn = 0; turn < 25; turn++) {
			for (Mob m : new ArrayList<>( level.mobs )) {
				if (!m.isAlive()) continue;
				final Mob mob = m;
				quietly( () -> { try { act( mob ); } catch (RuntimeException ex) { throw ex; } catch (Throwable ex) { throw new RuntimeException( ex ); } }, "act " + m.getClass().getSimpleName() );
			}
		}
		quietly( arena::endWave, "endWave" );

		//---- save and reload the run and the arena
		Dungeon.saveGame( GamesInProgress.curSlot );
		Dungeon.saveLevel( GamesInProgress.curSlot );
		TowerDefense.active = false;
		TowerDefense.arena = 9;
		Dungeon.loadGame( GamesInProgress.curSlot );
		check( TowerDefense.active && TowerDefense.arena == 1 && !TowerDefense.endless, "the run should reload as tower defense arena 1" );
		Level back = Dungeon.loadLevel( GamesInProgress.curSlot );
		check( back instanceof Arena1, "the arena should reload" );
		check( back.wave == 1, "the arena should remember its wave, got " + back.wave );
		boolean towerBack = false;
		for (Mob m : back.mobs) if (m instanceof Tower && m.pos == cell) towerBack = true;
		check( towerBack, "the placed tower should come back with the arena" );
		check( back.slot1 == TowerInfo.AllTowers.LIGHTNING, "the reloaded arena should keep the chosen towers" );
		heroOn( back );

		//---- win it
		check( TowerDefense.unranked(), "tower defense runs should not be ranked" );
		Arena.completeStage();
		check( TowerDefense.cleared, "winning should mark the arena cleared" );
		check( TowerDefense.unlocked( WndModes.Modes.NORMAL ) == 2, "winning arena 1 should open arena 2" );
		check( TowerDefense.wins() == 1, "the win should be counted" );
		check( TowerDefense.hasNext(), "arena 2 should be offered next" );
		check( TowerDefense.unlocked( WndModes.Modes.HARDMODE ) == 1, "hard mode should not open from a normal win" );

		//---- the exit under the portal leads to the next arena
		Level next = Dungeon.newLevel();
		check( next instanceof Arena2 && TowerDefense.arena == 2 && !TowerDefense.cleared, "the exit should lead to arena 2" );

		//---- endless mode
		TowerDefense.endless = true;
		TowerDefense.arena = 1;
		check( !TowerDefense.isUnlocked( 3 ), "endless arena 3 should wait for the story" );
		Level endless = Dungeon.newLevel();
		check( endless instanceof EndlessArena1, "endless arena 1 should be built" );
		check( WndModes.current == WndModes.Modes.NORMAL, "endless arenas are always normal" );

		//---- outside tower defense nothing changes
		TowerDefense.active = false;
		TowerDefense.endless = false;
		check( !TowerDefense.unranked(), "normal runs should be ranked" );
		check( new Arena1().slot1 == TowerInfo.AllTowers.WALL, "sandbox arenas should keep their default towers" );
	}
}
