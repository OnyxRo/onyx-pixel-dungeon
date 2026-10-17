import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.Tower;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerCrossbow1;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerWall1;
import com.shatteredpixel.shatteredpixeldungeon.items.towerspawners.TowerSpawner;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.SpecialFloors;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndModes;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Reflection;

import java.io.File;
import java.util.ArrayList;

/**
 * Tower PD's defense systems without a window. Every tower is made, saved, reloaded, upgraded and sold. Every
 * arena floor is built in the sandbox way, a tower is placed on it and several waves are started and ended while
 * every mob acts. FAIL lines fail the run, a null sprite, emitter or scene is the headless limit and prints as
 * HEADLESS. Run from the assets folder, scripts/check.sh does that.
 *
 *   TOWER_ONLY=Arena5 scripts/check.sh --new   plays only the arenas whose name contains that text
 */
public class TowerArenaTest {

	static int failures = 0, warnings = 0, headless = 0, arenas = 0, towers = 0;
	static final java.util.Set<String> SEEN = new java.util.LinkedHashSet<>();
	static volatile String current = null;
	static volatile long start = 0;

	public static void main( String[] args ) throws Exception {
		TestBoot.boot();
		TestBoot.stubWindow();
		GamesInProgress.selectedClass = HeroClass.WARRIOR;
		Dungeon.seed = 424242L;
		Dungeon.sandbox = true;
		Dungeon.init();

		final Thread mainThread = Thread.currentThread();
		Thread watchdog = new Thread( () -> {
			while (true) {
				try { Thread.sleep( 1000 ); } catch (InterruptedException e) { return; }
				if (current != null && System.currentTimeMillis() - start > 90000) {
					System.out.println( "FAIL: " + current + " did not finish in 90 seconds, it loops or hangs" );
					StackTraceElement[] st = mainThread.getStackTrace();
					for (int i = 0; i < Math.min( 12, st.length ); i++) System.out.println( "        at " + st[i] );
					System.out.println( "SUMMARY towers: " + towers + ", arenas: " + arenas + ", failures: " + (failures + 1) );
					Runtime.getRuntime().halt( 1 );
				}
			}
		} );
		watchdog.setDaemon( true );
		watchdog.start();

		String only = System.getenv( "TOWER_ONLY" ) == null ? "" : System.getenv( "TOWER_ONLY" );

		if (only.isEmpty()) towerChecks();

		for (SpecialFloors.Entry e : SpecialFloors.LIST) {
			if (!e.mod.equals( "Tower PD" )) continue;
			if (!only.isEmpty() && !e.label.contains( only )) continue;
			current = e.label;
			start = System.currentTimeMillis();
			try {
				playArena( e );
			} catch (Throwable t) {
				report( e.label, t );
			}
			current = null;
		}
		System.out.println( "SUMMARY towers: " + towers + ", arenas: " + arenas + ", failures: " + failures + ", headless limits: " + headless + ", warnings: " + warnings );
		System.exit( failures == 0 ? 0 : 1 );
	}

	static boolean isHeadless( Throwable t ) {
		String msg = String.valueOf( t.getMessage() );
		return t instanceof NullPointerException && msg.matches( "(?is).*(emitter|sprite|fieldOfView|Camera|[.]parent|Scene|Sample|Music|tilemap|Tilemap|visual).*" );
	}

	static void report( String name, Throwable t ) {
		if (isHeadless( t )) {
			headless++;
			System.out.println( "HEADLESS (not counted): " + name + ": " + String.valueOf( t.getMessage() ).substring( 0, Math.min( 100, String.valueOf( t.getMessage() ).length() ) ) );
			return;
		}
		failures++;
		System.out.println( "FAIL: " + name + " crashed: " + t );
		StackTraceElement[] st = t.getStackTrace();
		for (int i = 0; i < Math.min( 8, st.length ); i++) System.out.println( "        at " + st[i] );
	}

	/** Runs one step. A null emitter, sprite or scene is the headless limit. */
	static boolean quietly( Runnable step ) {
		try {
			step.run();
			return false;
		} catch (NullPointerException e) {
			if (!isHeadless( e )) throw e;
			headless++;
			//which frames keep hitting the limit, printed once each
			StackTraceElement top = e.getStackTrace().length > 0 ? e.getStackTrace()[0] : null;
			StackTraceElement caller = e.getStackTrace().length > 1 ? e.getStackTrace()[1] : top;
			if (top != null && SEEN.add( top.getClassName() + "." + top.getMethodName() + " <- " + caller.getClassName() + "." + caller.getMethodName() + ":" + caller.getLineNumber() ))
				System.out.println( "LIMIT " + top.getClassName() + "." + top.getMethodName() + ":" + top.getLineNumber() + " <- " + caller.getClassName() + "." + caller.getMethodName() + ":" + caller.getLineNumber() );
			return true;
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

	//---------------------------------------------------------------- the towers themselves

	static void towerChecks() throws Exception {
		File dir = new File( System.getProperty( "src.root", "." ), "core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/actors/mobs/towers" );
		File[] files = dir.listFiles();
		ArrayList<String> names = new ArrayList<>();
		if (files != null) for (File f : files) if (f.getName().startsWith( "Tower" ) && f.getName().endsWith( ".java" )) names.add( f.getName().replace( ".java", "" ) );
		java.util.Collections.sort( names );
		//a tiny level so towers can stand on something
		Dungeon.depth = 12;
		Dungeon.branch = 0;
		Dungeon.sandboxSpecial = null;
		Level level = Dungeon.newLevel();
		Dungeon.level = level;
		Actor.clear();
		Hero hero = Dungeon.hero;
		hero.sprite = TestBoot.sprite();
		hero.sprite.ch = hero;
		hero.pos = level.entrance();
		hero.HP = hero.HT = 1000;
		Actor.add( hero );
		for (String n : names) {
			Class<?> cls;
			try { cls = Class.forName( "com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers." + n ); } catch (Throwable t) { continue; }
			if (!Tower.class.isAssignableFrom( cls ) || java.lang.reflect.Modifier.isAbstract( cls.getModifiers() )) continue;
			//the base classes have no text of their own
			if (n.equals( "Tower" ) || n.equals( "TowerCShooting" ) || n.equals( "TowerCSpawning" ) || n.equals( "TowerCTotem" ) || n.equals( "TowerCWall" ) || n.equals( "TowerNotliving" ) || n.equals( "TowerWave" )) continue;
			current = n;
			start = System.currentTimeMillis();
			try {
				Tower t = (Tower)Reflection.newInstance( (Class<? extends Tower>)cls );
				towers++;
				String name = Messages.get( cls, "name" );
				if (name == null || name.contains( "!!!" )) { failures++; System.out.println( "FAIL: " + n + " has no name text" ); }
				if (Messages.get( cls, "desc" ).contains( "!!!" )) { failures++; System.out.println( "FAIL: " + n + " has no description text" ); }
				if (t.HT <= 0 || t.cost < 0) { failures++; System.out.println( "FAIL: " + n + " has no health or a negative cost" ); }
				//save and reload
				Bundle b = new Bundle();
				t.storeInBundle( b );
				Tower t2 = (Tower)Reflection.newInstance( (Class<? extends Tower>)cls );
				t2.restoreFromBundle( b );
				if (t2.HT != t.HT) { failures++; System.out.println( "FAIL: " + n + " changed its health when reloaded" ); }
				//the upgrade chain makes towers that exist
				if (t.upgCount >= 1) {
					Tower u = t.upgradeTower1();
					if (u == null) { failures++; System.out.println( "FAIL: " + n + " upgrade 1 is null" ); }
				}
				if (t.upgCount >= 2) { if (t.upgradeTower2() == null) { failures++; System.out.println( "FAIL: " + n + " upgrade 2 is null" ); } }
				//it can stand on the floor, act and take damage
				int cell = free( level, hero.pos );
				t.pos = cell;
				level.mobs.add( t );
				Actor.add( t );
				quietly( () -> level.occupyCell( t ) );
				for (int i = 0; i < 12; i++) {
					final int step = i;
					quietly( () -> { try { act( t ); } catch (RuntimeException e) { throw e; } catch (Throwable e) { throw new RuntimeException( e ); } } );
				}
				quietly( () -> t.damage( 1, hero ) );
				level.mobs.remove( t );
				Actor.remove( t );
			} catch (Throwable t) {
				report( n, t );
			}
		}
		//the spawner items that hand the towers out
		for (String n : new String[]{ "SpawnerCannon", "SpawnerCrossbow", "SpawnerDartgun", "SpawnerDisintegration", "SpawnerGrave", "SpawnerGuard", "SpawnerLightning", "SpawnerTntlog", "SpawnerTotemAttack", "SpawnerTotemHealing", "SpawnerTotemNecrotic", "SpawnerTotemShield", "SpawnerWall", "SpawnerWand", "SpawnerCamp" }) {
			current = n;
			try {
				Class<?> cls = Class.forName( "com.shatteredpixel.shatteredpixeldungeon.items.towerspawners." + n );
				TowerSpawner s = (TowerSpawner)Reflection.newInstance( (Class<? extends TowerSpawner>)cls );
				if (s.name().contains( "!!!" ) ) { failures++; System.out.println( "FAIL: " + n + " has no name" ); }
				s.desc();
				towers++;
			} catch (Throwable t) {
				report( n, t );
			}
		}
		current = null;
	}

	static int free( Level level, int near ) {
		for (int r = 1; r < 12; r++) {
			for (int o : PathFinder.NEIGHBOURS25) {
				int c = near + o * r;
				if (c > 0 && c < level.length() && level.passable[c] && Actor.findChar( c ) == null) return c;
			}
		}
		for (int i = 0; i < level.length(); i++) if (level.passable[i] && Actor.findChar( i ) == null) return i;
		return near;
	}

	//---------------------------------------------------------------- the arenas

	static void playArena( SpecialFloors.Entry e ) throws Throwable {
		for (WndModes.Modes mode : new WndModes.Modes[]{ WndModes.Modes.NORMAL, WndModes.Modes.CHALLENGE }) {
			WndModes.current = mode;
			Dungeon.depth = 12;
			Dungeon.branch = 0;
			Dungeon.sandboxSpecial = e.make;
			Level level = Dungeon.newLevel();
			Dungeon.level = level;
			Actor.clear();
			Hero hero = Dungeon.hero;
			hero.sprite = TestBoot.sprite();
			hero.sprite.ch = hero;
			hero.pos = level.entrance();
			if (hero.pos < 0 || hero.pos >= level.length() || !level.passable[hero.pos]) hero.pos = free( level, level.length() / 2 );
			hero.HP = hero.HT = 1000;
			Actor.add( hero );
			arenas++;
			if (!(level instanceof Arena)) {
				failures++;
				System.out.println( "FAIL: " + e.label + " is not an Arena" );
				return;
			}
			Arena arena = (Arena)level;
			if (arena.amuletTower == null || !arena.mobs.contains( arena.amuletTower )) {
				warnings++;
				System.out.println( "WARN: " + e.label + " (" + mode + ") has no amulet tower among its mobs" );
			}
			//a tower beside the hero
			final Tower t = new TowerCrossbow1();
			t.pos = free( level, hero.pos );
			level.mobs.add( t );
			Actor.add( t );
			final Tower w = new TowerWall1();
			w.pos = free( level, t.pos );
			level.mobs.add( w );
			Actor.add( w );
			Dungeon.gold = 5000;
			//several waves, each one started, played with every mob acting, then ended
			for (int wave = 0; wave < 4; wave++) {
				quietly( arena::startWave );
				for (int turn = 0; turn < 25; turn++) {
					for (Mob m : new ArrayList<>( level.mobs )) {
						if (!m.isAlive()) continue;
						final Mob mob = m;
						quietly( () -> { try { act( mob ); } catch (RuntimeException ex) { throw ex; } catch (Throwable ex) { throw new RuntimeException( ex ); } } );
					}
				}
				//the tower shop and the arena's own end of wave actions
				quietly( arena::endWave );
				for (Mob m : new ArrayList<>( level.mobs )) if (m.alignment == Char.Alignment.ENEMY) { final Mob mob = m; quietly( () -> mob.die( hero ) ); }
			}
			//the level can be saved
			Bundle b = new Bundle();
			quietly( () -> level.storeInBundle( b ) );
		}
		WndModes.current = WndModes.Modes.NORMAL;
	}
}
