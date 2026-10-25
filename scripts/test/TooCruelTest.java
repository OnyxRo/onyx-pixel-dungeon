import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GameModes;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.TooCruel;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

//toocruel, every Too Cruel PD mode on its own: build floors, spawn monsters, trade blows, kill one, save and reload
public class TooCruelTest {

	static int failures = 0, checks = 0;

	static void check( boolean ok, String what ) {
		checks++;
		if (!ok) {
			failures++;
			System.out.println( "FAIL: " + what );
		}
	}

	static int headless = 0;
	static volatile long stepStart = System.currentTimeMillis();
	static volatile String step = "start";

	static void step( String s ) {
		step = s;
		stepStart = System.currentTimeMillis();
	}

	//a crash from a missing item sprite or particle emitter only happens headless, as MobArenaTest counts them
	static boolean isHeadless( Throwable t ) {
		String m = String.valueOf( t.getMessage() );
		if (t instanceof ClassCastException && m.contains( "Sprite" )) return true; //a monster's own sprite type, the test gives every monster a plain one
		return t instanceof NullPointerException && (m.contains( "ItemSprite" ) || m.contains( "Emitter" ) || m.contains( "sprite" ) || m.contains( "GameScene.scene" ));
	}

	static void fail( String id, String stage, Throwable t ) {
		if (isHeadless( t )) {
			headless++;
			System.out.println( "HEADLESS (not counted) " + id + " " + stage + " " + t );
			return;
		}
		failures++;
		System.out.println( "FAIL: " + id + " " + stage + " threw " + t );
		StackTraceElement[] st = t.getStackTrace();
		for (int i = 0; i < Math.min( 8, st.length ); i++) System.out.println( "        at " + st[i] );
	}

	static boolean hasText( String s ) {
		return s != null && !s.contains( "NO TEXT" ) && !s.startsWith( "!!!" );
	}

	static void newRun( long seed, String mode ) {
		GameModes.active.clear();
		if (mode != null) GameModes.set( mode, 1 );
		GamesInProgress.selectedClass = HeroClass.WARRIOR;
		Dungeon.seed = seed;
		Dungeon.sandbox = false;
		com.shatteredpixel.shatteredpixeldungeon.SPDSettings.challenges( 0 );
		Dungeon.init();
		Dungeon.hero.sprite = TestBoot.sprite();
		Dungeon.hero.sprite.ch = Dungeon.hero;
		//a hit interrupts the hero, which touches the game screen's key handling, missing headless
		Dungeon.hero.damageInterrupt = false;
	}

	static Level build( int depth ) {
		Dungeon.depth = depth;
		Level l = Dungeon.newLevel();
		Dungeon.switchLevel( l, l.entrance() );
		Dungeon.hero.pos = l.entrance();
		Actor.add( Dungeon.hero );
		return l;
	}

	static int freeCellNextTo( Level level, int pos ) {
		for (int o : PathFinder.NEIGHBOURS8) {
			int c = pos + o;
			if (c >= 0 && c < level.length() && level.passable[c] && Actor.findChar( c ) == null) return c;
		}
		return -1;
	}

	static void act( Actor a ) throws Throwable {
		try {
			java.lang.reflect.Method m = Actor.class.getDeclaredMethod( "act" );
			m.setAccessible( true );
			m.invoke( a );
		} catch (java.lang.reflect.InvocationTargetException e) {
			throw e.getCause();
		}
	}

	//dropping loot needs item sprites, which a headless run does not have
	static void noLoot( Mob m ) throws Exception {
		java.lang.reflect.Field f = Mob.class.getDeclaredField( "lootChance" );
		f.setAccessible( true );
		f.set( m, 0f );
	}

	static byte[] bytes( Bundle b ) throws java.io.IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Bundle.write( b, out );
		return out.toByteArray();
	}

	static void exercise( String id, long seed ) {
		String stage = "starting a run";
		try {
			newRun( seed, id );
			check( GameModes.on( id ), id + " turns on" );
			for (String d : TooCruel.dependencies( id )) check( GameModes.on( d ), id + " turns on " + d );

			for (int depth : new int[]{ 1, 4, 12 }) {
				stage = "building floor " + depth;
				Level l = build( depth );
				Hero hero = Dungeon.hero;
				hero.HT = hero.HP = 5000;
				for (com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShieldBuff s : hero.buffs( com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShieldBuff.class )) s.incShield( 5000 );

				stage = "spawning monsters on floor " + depth;
				ArrayList<Mob> mobs = new ArrayList<>();
				for (int i = 0; i < 3; i++) {
					int cell = freeCellNextTo( l, hero.pos );
					if (cell < 0) break;
					//Shattered's own monsters for the depth, the ones from ported worlds need the game screen to act
					java.util.ArrayList<Class<? extends Mob>> rot = com.shatteredpixel.shatteredpixeldungeon.actors.mobs.MobSpawner.getMobRotation( TooCruel.mobDepth( depth ) );
					Mob m = rot.isEmpty() ? null : com.watabou.utils.Reflection.newInstance( rot.get( i % rot.size() ) );
					if (m == null) break;
					m.pos = cell;
					m.state = m.HUNTING;
					m.sprite = TestBoot.sprite();
					m.sprite.ch = m;
					noLoot( m );
					l.mobs.add( m );
					Actor.add( m );
					mobs.add( m );
				}
				for (Mob m : new ArrayList<>( l.mobs )) {
					if (m.sprite == null) { m.sprite = TestBoot.sprite(); m.sprite.ch = m; }
				}

				try {
					stage = "trading blows on floor " + depth;
					for (int r = 0; r < 6; r++) {
						for (Mob m : mobs) {
							if (!m.isAlive() || !hero.isAlive()) continue;
							m.attack( hero );
							hero.attack( m );
							m.damage( 2, hero );
						}
						if (hero.isAlive()) hero.damage( 3, mobs.isEmpty() ? hero : mobs.get( 0 ) );
						if (!hero.isAlive()) { hero.HP = hero.HT; }
					}
				} catch (Throwable t) { fail( id, stage, t ); }

				try {
					stage = "monster turns on floor " + depth;
					for (int r = 0; r < 5; r++) {
						for (Mob m : mobs) if (m.isAlive() && Actor.all().contains( m )) act( m );
					}
				} catch (Throwable t) { fail( id, stage, t ); }

				try {
					stage = "hero buffs acting on floor " + depth;
					for (Buff b : hero.buffs().toArray( new Buff[0] )) {
						if (b.getClass().getName().contains( "toocruel" )) act( b );
					}
					hero.spendConstant( 1f );
				} catch (Throwable t) { fail( id, stage, t ); }

				try {
					stage = "a monster dying on floor " + depth;
					for (Mob m : mobs) {
						if (m.isAlive()) { m.die( hero ); break; }
					}
				} catch (Throwable t) { fail( id, stage, t ); }
				if (!hero.isAlive()) hero.HP = hero.HT;
			}

			stage = "saving and reloading";
			Bundle b = new Bundle();
			b.put( "level", Dungeon.level );
			b.put( "hero", Dungeon.hero );
			GameModes.store( b );
			Bundle back = Bundle.read( new ByteArrayInputStream( bytes( b ) ) );
			Level saved = Dungeon.level;
			Level l2 = (Level) back.get( "level" );
			check( l2 != null && l2.getClass() == saved.getClass(), id + " floor comes back after a save" );
			Hero h2 = (Hero) back.get( "hero" );
			check( h2 != null, id + " hero comes back after a save" );
			GameModes.active.clear();
			GameModes.restore( back );
			check( GameModes.on( id ), id + " mode comes back after a save" );
		} catch (Throwable t) {
			fail( id, stage, t );
		}
	}

	public static void main( String[] args ) throws Exception {
		TestBoot.boot();
		TestBoot.stubWindow();
		//a floor that never finishes building hangs the whole check, so stop with a stack trace after two minutes on one step
		final Thread main = Thread.currentThread();
		Thread dog = new Thread( () -> {
			while (true) {
				try { Thread.sleep( 5000 ); } catch (InterruptedException e) { return; }
				if (System.currentTimeMillis() - stepStart > 120000) {
					System.out.println( "FAIL: hung on " + step );
					for (StackTraceElement e : main.getStackTrace()) System.out.println( "        at " + e );
					System.exit( 1 );
				}
			}
		} );
		dog.setDaemon( true );
		dog.start();

		ArrayList<String> ids = new ArrayList<>();
		for (GameModes.Mode m : GameModes.MODES) {
			if (!m.mod.equals( GameModes.MOD_TOOCRUEL )) continue;
			ids.add( m.id );
			check( hasText( m.title() ) && hasText( m.desc() ), "text for " + m.id );
		}
		check( ids.size() >= 80, "Too Cruel modes registered, got " + ids.size() );
		for (String w : new String[]{ "trials", "trials_desc", "trials_normal", "trials_hard", "trials_extreme", "trial_yes", "trial_no" }) {
			check( hasText( Messages.get( com.shatteredpixel.shatteredpixeldungeon.windows.WndGameModes.class, w ) ), "window text " + w );
		}

		//dependencies and conflicts
		GameModes.active.clear();
		GameModes.set( TooCruel.TIER_UP, 1 );
		check( GameModes.on( TooCruel.UNTIERED ) && GameModes.on( TooCruel.RETIERED ), "Tier Up turns on Untiered and Retiered" );
		GameModes.set( TooCruel.RETIERED, 0 );
		check( !GameModes.on( TooCruel.UNTIERED ) && !GameModes.on( TooCruel.TIER_UP ), "turning off Retiered turns off what needs it" );
		GameModes.set( TooCruel.HORDE, 1 );
		GameModes.set( TooCruel.ABANDONED_SHIP, 1 );
		check( !GameModes.on( TooCruel.HORDE ) && GameModes.on( TooCruel.SKELETON_CREW ), "Abandoned Ship turns off Horde" );
		GameModes.set( TooCruel.REPOPULATION, 1 );
		GameModes.set( TooCruel.HYDRA, 1 );
		check( GameModes.on( TooCruel.INFERTILITY ) && !GameModes.on( TooCruel.REPOPULATION ), "Hydra brings Infertility, which turns off Repopulation" );

		//trials
		for (TooCruel.Trial t : TooCruel.TRIALS) {
			GameModes.active.clear();
			TooCruel.applyTrial( t );
			check( hasText( TooCruel.trialSummary( t ) ), "trial summary for " + t.name );
		}
		com.shatteredpixel.shatteredpixeldungeon.SPDSettings.challenges( 0 );

		//each mode on its own
		long seed = 101;
		for (String id : ids) {
			int before = failures;
			step( id );
			exercise( id, seed++ );
			if (failures > before) System.out.println( "INFO mode with failures " + id );
		}

		//a few together, as the trials use them
		for (TooCruel.Trial t : TooCruel.TRIALS) {
			if (!t.group.equals( "hard" ) && !t.group.equals( "extreme" )) continue;
			GameModes.active.clear();
			TooCruel.applyTrial( t );
			com.shatteredpixel.shatteredpixeldungeon.SPDSettings.challenges( 0 );
			String first = null;
			for (String k : GameModes.active.keySet()) { first = k; break; }
			if (first == null) continue;
			java.util.LinkedHashMap<String, Integer> keep = new java.util.LinkedHashMap<>( GameModes.active );
			step( "trial " + t.name );
			try {
				GamesInProgress.selectedClass = HeroClass.WARRIOR;
				Dungeon.seed = seed++;
				Dungeon.sandbox = false;
				Dungeon.init();
				Dungeon.hero.sprite = TestBoot.sprite();
				Dungeon.hero.sprite.ch = Dungeon.hero;
				for (int depth : new int[]{ 1, 3, 6 }) build( depth );
			} catch (Throwable e) {
				fail( "trial " + t.name, "building floors", e );
			}
			GameModes.active.clear();
			GameModes.active.putAll( keep );
		}

		GameModes.active.clear();
		TooCruel.refresh();
		Random.crookedDie = false;
		System.out.println( "SUMMARY toocruel: " + ids.size() + " modes, " + checks + " checks, headless: " + headless + ", failures: " + failures );
		System.exit( failures == 0 ? 0 : 1 );
	}
}
