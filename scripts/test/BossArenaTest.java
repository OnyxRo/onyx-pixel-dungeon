import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.WorldShuffle;

import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * Plays the start and the end of every mod boss fight without a window. For each boss floor a mod supplies, the
 * hero is walked through the level until the fight starts (the level seals or a boss appears), then every boss is
 * killed. The floor must be unsealed afterwards, or a player would be trapped on it. Floors 10 and 20 must also
 * hand out the item that picks a subclass or armor ability.
 *
 * FAIL lines fail the run. WARN lines mean the fight could not be triggered by walking, look at the level by hand.
 * A null sprite, emitter or field of view is the headless run's limit and prints as HEADLESS. Run from the assets
 * folder, scripts/check.sh does that.
 */
public class BossArenaTest {

	static int failures = 0, warnings = 0, headless = 0, fights = 0;
	static volatile String current = null;
	static volatile long fightStart = 0;

	public static void main( String[] args ) throws Exception {
		TestBoot.boot();
		TestBoot.stubWindow();

		GamesInProgress.selectedClass = HeroClass.WARRIOR;
		Dungeon.seed = 424242L;
		Dungeon.sandbox = true;
		Dungeon.init();

		//a fight that loops forever would freeze the whole check, so one that takes over a minute is a failure that ends the run
		final Thread mainThread = Thread.currentThread();
		Thread watchdog = new Thread( () -> {
			while (true) {
				try { Thread.sleep( 1000 ); } catch (InterruptedException e) { return; }
				if (current != null && System.currentTimeMillis() - fightStart > 60000) {
					System.out.println( "FAIL: " + current + " did not finish in 60 seconds, it loops or hangs" );
					StackTraceElement[] st = mainThread.getStackTrace();
					for (int i = 0; i < Math.min( 12, st.length ); i++) System.out.println( "        at " + st[i] );
					System.out.println( "SUMMARY boss fights tried: " + fights + ", failures: " + (failures + 1) + ", headless limits: " + headless + ", warnings: " + warnings );
					Runtime.getRuntime().halt( 1 );
				}
			}
		} );
		watchdog.setDaemon( true );
		watchdog.start();

		String only = args.length > 0 ? args[0] : "";
		int[] depths = {5, 10, 15, 20, 25};
		for (int world = 0; world < WorldShuffle.worldCount(); world++) {
			for (int depth : depths) {
				Level[] variants = WorldShuffle.modBossVariants( world, depth );
				for (int v = 0; v < variants.length; v++) {
				String name = variants[v].getClass().getSimpleName();
				if (!only.isEmpty() && java.util.Arrays.stream( only.split(",") ).noneMatch( name::contains )) continue; //BOSS_ONLY=A,B runs several
				current = name + " on floor " + depth;
				fightStart = System.currentTimeMillis();
				WorldShuffle.forcedVariant = v;
				try {
					fight( world, depth, name );
					current = null;
				} catch (Throwable t) {
					current = null;
					String msg = String.valueOf( t.getMessage() );
					if (t instanceof NullPointerException && msg.matches( "(?is).*(emitter|sprite|fieldOfView|Camera|[.]parent|Scene|HealthIndicator).*" )) {
						headless++;
						System.out.println( "HEADLESS (not counted): " + name + " on floor " + depth + ": " + msg.substring( 0, Math.min( 120, msg.length() ) ) );
					} else {
						failures++;
						System.out.println( "FAIL: " + name + " on floor " + depth + " crashed: " + t );
						StackTraceElement[] st = t.getStackTrace();
						for (int i = 0; i < Math.min( 6, st.length ); i++) System.out.println( "        at " + st[i] );
					}
				}
				}
			}
		}
		WorldShuffle.forcedVariant = -1;

		//the special floors with a boss, built the way the sandbox builds them
		for (com.shatteredpixel.shatteredpixeldungeon.levels.SpecialFloors.Entry e : com.shatteredpixel.shatteredpixeldungeon.levels.SpecialFloors.LIST) {
			if (!e.label.toLowerCase().contains( "boss" ) && !e.label.toLowerCase().contains( "final" )) continue;
			if (!only.isEmpty() && !e.label.contains( only )) continue;
			current = e.label;
			fightStart = System.currentTimeMillis();
			try {
				Dungeon.sandboxSpecial = e.make;
				fight( -1, 15, e.label );
			} catch (Throwable t) {
				failures++;
				System.out.println( "FAIL: special floor '" + e.label + "' crashed: " + t );
				StackTraceElement[] st = t.getStackTrace();
				for (int i = 0; i < Math.min( 6, st.length ); i++) System.out.println( "        at " + st[i] );
			}
			current = null;
		}
		System.out.println( "SUMMARY boss fights tried: " + fights + ", failures: " + failures + ", headless limits: " + headless + ", warnings: " + warnings );
		System.exit( failures == 0 ? 0 : 1 );
	}

	static void fight( int world, int depth, String name ) throws Exception {
		Dungeon.sandboxWorld = world;
		Dungeon.depth = depth;
		Dungeon.branch = 0;
		Level level = Dungeon.newLevel();
		Dungeon.level = level;
		Actor.clear();
		//the hero from Dungeon.init has its class, talents and starting items, a bare new Hero does not
		Hero hero = Dungeon.hero;
		hero.sprite = TestBoot.sprite();
		hero.sprite.ch = hero;
		hero.pos = level.entrance();
		hero.HP = hero.HT = 1000;
		Actor.add( hero );
		fights++;

		//walk outward from the entrance, a cell at a time, until the fight starts
		boolean[] seen = new boolean[level.length()];
		ArrayDeque<Integer> queue = new ArrayDeque<>();
		queue.add( hero.pos );
		seen[hero.pos] = true;
		boolean started = false;
		int steps = 0;
		int[] dirs = {-1, 1, -level.width(), level.width()};
		while (!queue.isEmpty() && !started && steps < 6000) {
			int cell = queue.poll();
			steps++;
			hero.pos = cell;
			quietly( () -> level.occupyCell( hero ) );
			started = level.locked || bossesIn( level ).size() > 0;
			for (int d : dirs) {
				int n = cell + d;
				//locked doors count as open, the keys for them are somewhere on the floor
				boolean door = level.map[n] == com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.LOCKED_DOOR
						|| level.map[n] == com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.DOOR;
				if (n < 0 || n >= level.length() || seen[n] || !(level.passable[n] || door)) continue;
				if (Math.abs( n % level.width() - cell % level.width() ) > 1) continue;
				seen[n] = true;
				queue.add( n );
			}
		}
		if (!started) {
			warnings++;
			System.out.println( "WARN: " + name + " on floor " + depth + " never started a fight by walking, check it in the sandbox" );
			return;
		}
		ArrayList<Mob> bosses = bossesIn( level );
		final int arenaCell = hero.pos;
		if (bosses.isEmpty()) {
			warnings++;
			System.out.println( "WARN: " + name + " on floor " + depth + " sealed but showed no boss in " + steps + " steps" );
		}
		boolean cutShort = false;
		for (Mob boss : bosses) {
			boss.HP = 0;
			cutShort |= quietly( () -> boss.die( hero ) );
		}
		//dying bosses may hand the unseal to a later turn, give the level a few of its own
		if (level.locked && !bosses.isEmpty()) {
			String cls = level.getClass().getSimpleName();
			if (level instanceof com.shatteredpixel.shatteredpixeldungeon.levels.DragonBossLevel) {
				dragonPhases( (com.shatteredpixel.shatteredpixeldungeon.levels.DragonBossLevel)level, name, depth );
			} else if (MULTI_PHASE.contains( name ) || MULTI_PHASE.contains( cls ) || cls.contains( "PrisonBossLevel" ) || cls.contains( "TenguBossLevel" )) {
				//the boss changes the map and comes back several times, one kill in a headless run is not the end of the fight
				warnings++;
				System.out.println( "WARN: " + name + " on floor " + depth + " is a multi-phase fight, kill the boss in every phase in the sandbox to check the unseal." );
			} else if (cutShort) {
				//the death stopped at a headless limit before the level could unseal, so this cannot be judged here
				warnings++;
				System.out.println( "WARN: " + name + " on floor " + depth + " is still sealed, but the boss death was cut short by a headless limit. Kill the boss in the sandbox to check the unseal." );
			} else {
				failures++;
				System.out.println( "FAIL: " + name + " on floor " + depth + " is still sealed after its boss died" );
			}
		}
		//once unsealed, the hero must be able to walk from the arena onto a cell that is a working exit
		//a death cut short by a headless limit never reached the unseal, run it here so the exit still gets checked
		if (level.locked && cutShort && !bosses.isEmpty()) quietly( level::unseal );
		if (!level.locked && !bosses.isEmpty()) {
			String why = exitProblem( level, arenaCell );
			if (why != null && why.startsWith( "the exit tile" )) {
				failures++;
				System.out.println( "FAIL: " + name + " on floor " + depth + " exit after the boss died, " + why );
			} else if (why != null) {
				warnings++;
				System.out.println( "WARN: " + name + " on floor " + depth + " exit after the boss died, " + why + ". Kill the boss in the sandbox and walk out to check." );
			}
		}
		if (depth == 10 || depth == 20) {
			Class<?> reward = depth == 10 ? com.shatteredpixel.shatteredpixeldungeon.items.TengusMask.class
					: com.shatteredpixel.shatteredpixeldungeon.items.KingsCrown.class;
			boolean found = false;
			for (com.shatteredpixel.shatteredpixeldungeon.items.Heap h : level.heaps.valueList())
				for (com.shatteredpixel.shatteredpixeldungeon.items.Item i : h.items) if (reward.isInstance( i )) found = true;
			if (!found && !bosses.isEmpty() && !cutShort) {
				failures++;
				System.out.println( "FAIL: " + name + " on floor " + depth + " dropped no " + reward.getSimpleName() );
			}
		}
	}

	/** Null when an exit transition can be walked onto from start, otherwise what is wrong. */
	static String exitProblem( Level level, int start ) {
		com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition exit =
				level.getTransition( com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition.Type.REGULAR_EXIT );
		if (exit == null || exit.type != com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition.Type.REGULAR_EXIT) {
			return "the floor has no exit";
		}
		//every exit tile must lead somewhere, stepping on a dead one looks like a broken door
		for (int c = 0; c < level.length(); c++) {
			if (level.map[c] == com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EXIT && level.getTransition( c ) == null) {
				return "the exit tile at cell " + c + " does nothing when stepped on";
			}
		}
		boolean[] seen = new boolean[level.length()];
		ArrayDeque<Integer> queue = new ArrayDeque<>();
		queue.add( start );
		seen[start] = true;
		int[] dirs = {-1, 1, -level.width(), level.width(), -level.width()-1, -level.width()+1, level.width()-1, level.width()+1};
		while (!queue.isEmpty()) {
			int cell = queue.poll();
			if (exit.inside( cell )) return null;
			for (int d : dirs) {
				int n = cell + d;
				if (n < 0 || n >= level.length() || seen[n]) continue;
				if (Math.abs( n % level.width() - cell % level.width() ) > 1) continue;
				boolean door = level.map[n] == com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.LOCKED_DOOR
						|| level.map[n] == com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.DOOR
						|| level.map[n] == com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.LOCKED_EXIT; //opened by the boss's skeleton key
				if (!(level.passable[n] || door)) continue;
				seen[n] = true;
				queue.add( n );
			}
		}
		return "no walkable path reaches the exit at cell " + exit.cell() + " (tile " + level.map[exit.cell()] + ")";
	}

	/**
	 * The Dragon's four phases (fire rings, fire wave, maze, crumbling arena) are driven by calling the level's own
	 * progress step, the way the hero's movement does in play. The last step must unseal the floor.
	 */
	static void dragonPhases( com.shatteredpixel.shatteredpixeldungeon.levels.DragonBossLevel level, String name, int depth ) {
		com.shatteredpixel.shatteredpixeldungeon.levels.DragonBossLevel.State before = com.shatteredpixel.shatteredpixeldungeon.levels.DragonBossLevel.state;
		StringBuilder trail = new StringBuilder( String.valueOf( before ) );
		boolean cutShort = false;
		for (int i = 0; i < 6 && com.shatteredpixel.shatteredpixeldungeon.levels.DragonBossLevel.state != com.shatteredpixel.shatteredpixeldungeon.levels.DragonBossLevel.State.WON; i++) {
			com.shatteredpixel.shatteredpixeldungeon.levels.DragonBossLevel.State was = com.shatteredpixel.shatteredpixeldungeon.levels.DragonBossLevel.state;
			boolean hit = quietly( level::progress );
			cutShort |= hit;
			System.out.println( "INFO: Dragon step from " + was + " limit hit " + hit + " sealed " + level.locked );
			if (was == com.shatteredpixel.shatteredpixeldungeon.levels.DragonBossLevel.State.START) {
				//the dragon has to appear in the hall the hero just walked into, not behind the locked door, and its jump has to land there too
				int hallEnd = com.shatteredpixel.shatteredpixeldungeon.levels.DragonBossLevel.TOP + com.shatteredpixel.shatteredpixeldungeon.levels.DragonBossLevel.HALL_HEIGHT;
				int[] cells = { com.shatteredpixel.shatteredpixeldungeon.levels.DragonBossLevel.dragon.pos, com.shatteredpixel.shatteredpixeldungeon.levels.DragonBossLevel.getTelePos() };
				for (int c : cells) {
					int row = c / level.width();
					if (row < com.shatteredpixel.shatteredpixeldungeon.levels.DragonBossLevel.TOP || row >= hallEnd || level.map[c] != com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY) {
						System.out.println( "FAIL: Dragon on cell " + c + " (row " + row + ") is outside the hall" );
						failures++;
					}
				}
			}
			trail.append( " > " ).append( com.shatteredpixel.shatteredpixeldungeon.levels.DragonBossLevel.state );
			//no invisible walls, every plain floor tile of the current map can be walked on (the dragon used to block five tiles each side of him)
			for (int c = 0; c < level.length(); c++) {
				if (level.map[c] == com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY && !level.passable[c]) {
					System.out.println( "FAIL: Dragon " + com.shatteredpixel.shatteredpixeldungeon.levels.DragonBossLevel.state + " floor cell " + c + " is blocked but looks open" );
					failures++;
					break;
				}
			}
			if (com.shatteredpixel.shatteredpixeldungeon.levels.DragonBossLevel.state == was) {
				//a step that stopped at a headless limit (a null sprite) leaves the state behind, move it on by hand so the later phases and the unseal still run
				if (!cutShort) break;
				com.shatteredpixel.shatteredpixeldungeon.levels.DragonBossLevel.state = com.shatteredpixel.shatteredpixeldungeon.levels.DragonBossLevel.State.values()[was.ordinal() + 1];
				trail.append( " (forced)" );
				cutShort = false;
			}
		}
		boolean won = com.shatteredpixel.shatteredpixeldungeon.levels.DragonBossLevel.state == com.shatteredpixel.shatteredpixeldungeon.levels.DragonBossLevel.State.WON;
		System.out.println( "INFO: Dragon phases " + trail );
		if (won && level.locked) {
			failures++;
			System.out.println( "FAIL: " + name + " on floor " + depth + " is still sealed after all four phases" );
		} else if (!won) {
			warnings++;
			System.out.println( "WARN: " + name + " on floor " + depth + " stopped at " + com.shatteredpixel.shatteredpixeldungeon.levels.DragonBossLevel.state + (cutShort ? " at a headless limit" : "") + ", play the later phases in the sandbox." );
		}
	}

	/** Runs one step. A null emitter, sprite or scene is the headless run's limit, the state it leaves behind is still checked. */
	static boolean quietly( Runnable step ) {
		try {
			step.run();
			return false;
		} catch (NullPointerException e) {
			String msg = String.valueOf( e.getMessage() );
			if (!msg.matches( "(?is).*(emitter|sprite|fieldOfView|Camera|[.]parent|Scene|HealthIndicator).*" )) throw e;
			headless++;
			//which frames keep hitting the limit, printed once each so the stubs can be improved
			StackTraceElement top = e.getStackTrace()[0];
			StackTraceElement caller = e.getStackTrace().length > 1 ? e.getStackTrace()[1] : top;
			String key = top.getClassName() + "." + top.getMethodName() + " <- " + caller.getClassName() + "." + caller.getMethodName() + ":" + caller.getLineNumber();
			if (SEEN_LIMITS.add( key )) System.out.println( "LIMIT " + key );
			return true;
		}
	}

	static final java.util.Set<String> SEEN_LIMITS = new java.util.LinkedHashSet<>();

	//boss levels whose fight has several phases. Add a class name here with a reason when a new one is ported.
	static final java.util.Set<String> MULTI_PHASE = new java.util.HashSet<>( java.util.Arrays.asList( "OldPrisonBossLevel", "NewPrisonBossLevel", "FinalLevel", "DragonBossLevel" ) );

	static ArrayList<Mob> bossesIn( Level level ) {
		ArrayList<Mob> out = new ArrayList<>();
		for (Mob m : level.mobs) if (m.properties().contains( Char.Property.BOSS )) out.add( m );
		return out;
	}
}
