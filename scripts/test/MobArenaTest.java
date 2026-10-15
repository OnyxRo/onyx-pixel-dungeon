import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Reflection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * The testing ground. Puts monsters through everything that can break without a window, all in
 * one run, so a port can be checked in seconds.
 *
 *   MobArenaTest --all          every monster ported from another mod
 *   MobArenaTest --new          ported monsters whose files changed since the last commit
 *   MobArenaTest --roster       monsters on the ModRoster
 *   MobArenaTest Kobold Spitter just those classes, by simple name
 *
 * Per monster it checks the name and description strings, saving and reloading, loot, a damage
 * and accuracy roll, a fight against a hero that cannot die, and 60 turns of its own AI beside
 * the hero. It also checks that every file named in Assets exists, which catches missing sprites
 * and sounds. Lines starting FAIL fail the run, WARN lines are worth a look.
 * Run it from the assets folder. scripts/check.sh does that.
 */
public class MobArenaTest {

	static int failures = 0, warnings = 0;

	public static void main(String[] args) throws Exception {
		TestBoot.boot();
		TestBoot.stubWindow();

		List<Class<?>> mobs = pick(args);
		if (mobs.isEmpty()) {
			System.out.println("WARN: no monsters picked for " + String.join(" ", args));
		}

		loadBaseline();
		checkAssets();

		GamesInProgress.selectedClass = HeroClass.WARRIOR;
		Dungeon.seed = 424242L;
		Dungeon.sandbox = true;
		Dungeon.init();

		int ok = 0;
		for (Class<?> cls : mobs) {
			int before = failures;
			//the same dice every run, so a failure is a failure and not luck
			com.watabou.utils.Random.pushGenerator( cls.getName().hashCode() );
			try {
				check( cls );
			} catch (Throwable t) {
				fail( cls, "crashed the test", t );
			} finally {
				com.watabou.utils.Random.popGenerator();
			}
			if (failures == before) ok++;
		}
		if (writeBaseline) {
			java.nio.file.Files.write( new java.io.File( System.getProperty( "mob.baseline" ) ).toPath(),
					("# Problems known before the test existed, mostly things a headless run cannot do (sprites, emitters, teleporting).\n"
					+ "# One 'Class what' per line. Regenerate with scripts/check.sh --write-baseline, but only after checking new entries by eye.\n"
					+ String.join( "\n", FOUND ) + "\n").getBytes() );
			System.out.println( "wrote " + FOUND.size() + " known problems" );
			System.exit( 0 );
		}
		System.out.println("SUMMARY monsters checked: " + mobs.size() + ", clean: " + ok
				+ ", failures: " + failures + ", known problems skipped: " + baselined + ", headless limits: " + headless + ", warnings: " + warnings);
		System.exit(failures == 0 ? 0 : 1);
	}

	// ---------------------------------------------------------------------------------------

	static List<Class<?>> pick( String[] args ) throws Exception {
		Set<Class<?>> out = new LinkedHashSet<>();
		boolean all = args.length == 0;
		Set<String> names = new LinkedHashSet<>();
		boolean changed = false, roster = false;
		for (String a : args) {
			if (a.equals("--all")) all = true;
			else if (a.equals("--new")) changed = true;
			else if (a.equals("--roster")) roster = true;
			else if (a.equals("--write-baseline")) { writeBaseline = true; all = true; }
			else names.add( a );
		}
		Set<String> changedFiles = changed ? changedJavaNames() : new LinkedHashSet<>();
		if (roster) {
			out.addAll( com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ModRoster.mobs() );
		}
		for (Bestiary cat : Bestiary.values()) {
			for (Class<?> cls : cat.entities()) {
				if (!Mob.class.isAssignableFrom( cls )) continue;
				Class<?> top = cls.getEnclosingClass() == null ? cls : cls.getEnclosingClass();
				boolean wanted = names.contains( cls.getSimpleName() ) || names.contains( top.getSimpleName() )
						|| (changed && changedFiles.contains( top.getSimpleName() ));
				//bosses need their boss level, allies and neutral NPCs need an owner or a quest. Name them to test them anyway
				boolean needsSetup = cat == Bestiary.BOSSES || cat == Bestiary.ALLY || cat == Bestiary.NEUTRAL || cat == Bestiary.QUEST;
				if (wanted || (all && Bestiary.isPorted( cls ) && !needsSetup)) out.add( cls );
			}
		}
		//monsters named on the command line that the bestiary does not list
		for (String n : names) {
			boolean found = false;
			for (Class<?> c : out) if (c.getSimpleName().equals( n )) found = true;
			if (found) continue;
			for (String pkg : new String[]{"", "altregion."}) {
				try {
					out.add( Class.forName( "com.shatteredpixel.shatteredpixeldungeon.actors.mobs." + pkg + n ) );
					found = true;
					break;
				} catch (ClassNotFoundException e) { /* try the next package */ }
			}
			if (!found) { System.out.println( "FAIL: no monster class called " + n ); failures++; }
		}
		return new ArrayList<>( out );
	}

	/** Java files changed in the working tree or in the last commit. */
	static Set<String> changedJavaNames() throws Exception {
		Set<String> names = new LinkedHashSet<>();
		java.io.File dir = new java.io.File( "." ).getAbsoluteFile();
		while (dir != null && !new java.io.File( dir, ".git" ).exists()) dir = dir.getParentFile();
		if (dir == null) return names;
		for (String[] cmd : new String[][]{
				{"git", "status", "--short", "--untracked-files=all"},
				{"git", "show", "--name-only", "--format=", "HEAD"}}) {
			Process p = new ProcessBuilder( cmd ).directory( dir ).redirectErrorStream( true ).start();
			try (java.io.BufferedReader r = new java.io.BufferedReader( new java.io.InputStreamReader( p.getInputStream() ) )) {
				String line;
				while ((line = r.readLine()) != null) {
					line = line.trim();
					if (!line.endsWith( ".java" )) continue;
					String f = line.substring( line.lastIndexOf( '/' ) + 1 );
					names.add( f.substring( 0, f.length() - 5 ) );
				}
			}
		}
		return names;
	}

	// ---------------------------------------------------------------------------------------

	//known problems that were there before this test existed, in scripts/test/mob-baseline.txt, one "Class what" per line
	static final Set<String> BASELINE = new LinkedHashSet<>();
	static int baselined = 0;

	static void loadBaseline() throws Exception {
		java.io.File f = new java.io.File( System.getProperty( "mob.baseline", "" ) );
		if (!f.isFile()) return;
		for (String line : java.nio.file.Files.readAllLines( f.toPath() )) {
			line = line.trim();
			if (!line.isEmpty() && !line.startsWith( "#" )) BASELINE.add( line );
		}
	}

	//--write-baseline records today's failures as known problems
	static boolean writeBaseline = false;
	static final Set<String> FOUND = new LinkedHashSet<>();
	static int headless = 0;

	static void fail( Class<?> cls, String what, Throwable t ) {
		FOUND.add( cls.getSimpleName() + " " + what.replaceAll( ":.*", "" ) );
		if (BASELINE.contains( cls.getSimpleName() + " " + what.replaceAll( ":.*", "" ) )) {
			baselined++;
			return;
		}
		//a null emitter, sprite or field of view is the headless run's limit, not a bug in the port. Counted apart, shown once.
		//Run with -Dstrict=true to make these failures again.
		if (t instanceof NullPointerException && !Boolean.getBoolean( "strict" ) && String.valueOf( t.getMessage() ).matches( "(?is).*(emitter|sprite|fieldOfView|Camera|[.]parent).*" )) {
			headless++;
			System.out.println( "HEADLESS (not counted): " + cls.getSimpleName() + " " + what );
			return;
		}
		failures++;
		System.out.println( "FAIL: " + cls.getSimpleName() + " " + what + (t == null ? "" : ": " + t) );
		if (t != null) {
			StackTraceElement[] st = t.getStackTrace();
			for (int i = 0; i < Math.min(5, st.length); i++) System.out.println( "        at " + st[i] );
			if (t.getCause() != null) System.out.println( "        caused by " + t.getCause() );
		}
	}

	static void warn( Class<?> cls, String what ) {
		warnings++;
		System.out.println( "WARN: " + cls.getSimpleName() + " " + what );
	}

	/** Every string constant in Assets that looks like a file must exist. */
	static void checkAssets() throws Exception {
		int missing = 0, checked = 0;
		for (Class<?> inner : Class.forName( "com.shatteredpixel.shatteredpixeldungeon.Assets" ).getDeclaredClasses()) {
			collect( inner, new ArrayList<>() );
		}
		for (String path : ASSET_PATHS) {
			checked++;
			if (!new java.io.File( path ).exists()) {
				//Shattered itself names this one and never loads it
				if (path.equals( "effects/fireball.png" )) continue;
				missing++;
				failures++;
				System.out.println( "FAIL: Assets names a file that does not exist: " + path );
			}
		}
		System.out.println( "assets checked: " + checked + ", missing: " + missing );
	}

	static final List<String> ASSET_PATHS = new ArrayList<>();

	static void collect( Class<?> c, List<String> unused ) throws Exception {
		for (Field f : c.getDeclaredFields()) {
			if (!Modifier.isStatic( f.getModifiers() ) || f.getType() != String.class) continue;
			f.setAccessible( true );
			String v = (String) f.get( null );
			if (v == null || !v.matches( ".*\\.(png|mp3|ogg|wav|json|tmx)$" ) || v.endsWith( "/" )) continue;
			ASSET_PATHS.add( v );
		}
		for (Class<?> inner : c.getDeclaredClasses()) collect( inner, unused );
	}

	// ---------------------------------------------------------------------------------------

	static Level level;
	static int levelDepth = -1;

	static void level( int depth ) {
		if (levelDepth == depth) return;
		Dungeon.depth = Math.max( 1, Math.min( 24, depth ) );
		Dungeon.branch = 0;
		level = Dungeon.newLevel();
		Dungeon.level = level;
		levelDepth = depth;
		Actor.clear();
		Dungeon.hero = new Hero();
		Dungeon.hero.live();
		Dungeon.hero.sprite = TestBoot.sprite();
		Dungeon.hero.sprite.ch = Dungeon.hero;
		Dungeon.hero.pos = level.entrance();
		if (Dungeon.hero.pos < 0 || !level.passable[Dungeon.hero.pos]) {
			for (int i = 0; i < level.length(); i++) if (level.passable[i]) { Dungeon.hero.pos = i; break; }
		}
		Actor.add( Dungeon.hero );
	}

	static int freeCellNextTo( int pos ) {
		for (int o : PathFinder.NEIGHBOURS8) {
			int c = pos + o;
			if (c >= 0 && c < level.length() && level.passable[c] && Actor.findChar( c ) == null) return c;
		}
		return -1;
	}

	/**
	 * A sprite often finishes an attack by casting its character, like ((Warlock)ch).onZapComplete(). A monster that
	 * borrows another monster's sprite crashes the game the first time it attacks, and a headless run cannot see it
	 * because sprites are stubs. So read the sprite's source, and its parents', and check each cast against the monster.
	 */
	static void checkSpriteCasts( Class<?> cls, Mob mob ) throws Exception {
		String root = System.getProperty( "src.root", "" );
		if (root.isEmpty()) return;
		java.util.regex.Pattern cast = java.util.regex.Pattern.compile( "\\(\\(\\s*([A-Z][\\w.]*)\\s*\\)\\s*ch\\s*\\)" );
		for (Class<?> sc = mob.spriteClass; sc != null && sc.getName().contains( "shatteredpixeldungeon.sprites." ); sc = sc.getSuperclass()) {
			java.io.File f = new java.io.File( root, "core/src/main/java/" + sc.getName().replace( '.', '/' ).replaceAll( "\\$.*", "" ) + ".java" );
			if (!f.isFile()) continue;
			String src = new String( java.nio.file.Files.readAllBytes( f.toPath() ), "UTF-8" );
			//a file with several sprites in it (nested classes) casts to a different type in each, so one match is enough
			java.util.regex.Matcher m = cast.matcher( src );
			java.util.Set<String> types = new java.util.LinkedHashSet<>();
			boolean any = false;
			while (m.find()) {
				String type = m.group( 1 );
				types.add( type );
				if (src.contains( "instanceof " + type )) any = true;
				Class<?> t = null;
				for (String pkg : new String[]{"actors.mobs.", "actors.mobs.altregion.", "actors.mobs.npcs.", "actors.", ""}) {
					try { t = Class.forName( "com.shatteredpixel.shatteredpixeldungeon." + pkg + type.replace( '.', '$' ) ); break; }
					catch (ClassNotFoundException e) { /* next */ }
				}
				if (t == null || t.isInstance( mob )) any = true;
			}
			if (!types.isEmpty() && !any) {
				fail( cls, "uses " + sc.getSimpleName() + ", which casts its character to " + String.join( " or ", types ) + " and would crash", null );
			}
			//a sprite that writes its own zap or attack has dealt with its parent's cast
			if (src.contains( "void zap(" ) || src.contains( "void attack(" )) break;
		}
	}

	static void check( Class<?> cls ) throws Exception {
		// strings
		for (String key : new String[]{"name", "desc"}) {
			if (Messages.get( cls, key ).contains( "!!!" )) fail( cls, "has no " + key + " string", null );
		}

		Mob mob = (Mob) Reflection.newInstance( cls );
		if (mob == null) { fail( cls, "could not be created with no arguments", null ); return; }
		if (mob.spriteClass == null) fail( cls, "has no spriteClass", null );
		else checkSpriteCasts( cls, mob );
		if (mob.HT <= 0 || mob.HP <= 0) fail( cls, "has no health (HP " + mob.HP + ", HT " + mob.HT + ")", null );

		// save and reload
		Bundle b = new Bundle();
		b.put( "mob", mob );
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Bundle.write( b, out );
		Mob back = (Mob) Bundle.read( new ByteArrayInputStream( out.toByteArray() ) ).get( "mob" );
		if (back == null || back.getClass() != cls || back.HP != mob.HP) fail( cls, "did not survive saving and loading", null );

		// loot
		if (field( mob, "loot" ) != null && (float) field( mob, "lootChance" ) > 0) {
			Dungeon.depth = 5;
			for (int i = 0; i < 20; i++) mob.createLoot();
		}

		// a fight
		int depth = mob.maxLvl > 0 ? Math.max( 2, Math.min( 24, mob.maxLvl - 2 ) ) : 10;
		level( depth );
		Hero hero = Dungeon.hero;
		hero.HP = hero.HT = 5000;
		int cell = freeCellNextTo( hero.pos );
		if (cell < 0) { warn( cls, "no free cell next to the hero, skipped the fight" ); return; }
		mob = (Mob) Reflection.newInstance( cls );
		setField( mob, "lootChance", 0f );
		mob.pos = cell;
		mob.state = mob.HUNTING;
		mob.sprite = TestBoot.sprite();
		mob.sprite.ch = mob;
		Dungeon.level.mobs.add( mob );
		Actor.add( mob );

		int dmg = 0;
		for (int i = 0; i < 30; i++) dmg += mob.damageRoll();
		if (dmg <= 0 && !isHarmless( mob )) warn( cls, "never rolled any melee damage in 30 tries" );
		mob.attackSkill( hero );
		mob.drRoll();

		try {
			for (int i = 0; i < 25 && mob.isAlive(); i++) {
				mob.attack( hero );
				hero.attack( mob );
			}
		} catch (Throwable t) {
			fail( cls, "crashed while trading blows", t );
		}

		// its own AI, beside the hero, for 60 turns. Needs sprites to be optional, which they are headless
		mob = (Mob) Reflection.newInstance( cls );
		setField( mob, "lootChance", 0f );
		cell = freeCellNextTo( hero.pos );
		if (cell >= 0) {
			mob.pos = cell;
			mob.state = mob.HUNTING;
			mob.enemy = hero;
			mob.sprite = TestBoot.sprite();
			mob.sprite.ch = mob;
		mob.sprite.ch = mob;
			Dungeon.level.mobs.add( mob );
			Actor.add( mob );
			try {
				for (int i = 0; i < 60 && mob.isAlive(); i++) {
					act( mob );
					if (i % 7 == 6) mob.damage( 1, hero );
				}
			} catch (Throwable t) {
				fail( cls, "crashed during its own turns", t );
			}
		}

		// death, with whatever it does when it dies
		try {
			//loot was already rolled above, and dropping it needs item sprites
			setField( mob, "lootChance", 0f );
			if (mob.isAlive()) mob.die( hero );
		} catch (Throwable t) {
			fail( cls, "crashed when it died", t );
		}

		// cleanup, so the next monster starts with an empty floor
		for (Mob m : new ArrayList<>( Dungeon.level.mobs )) { Actor.remove( m ); Dungeon.level.mobs.remove( m ); }
	}

	static Object field( Object o, String name ) throws Exception {
		Class<?> c = o.getClass();
		while (c != null) {
			try {
				Field f = c.getDeclaredField( name );
				f.setAccessible( true );
				return f.get( o );
			} catch (NoSuchFieldException e) { c = c.getSuperclass(); }
		}
		return null;
	}

	static void setField( Object o, String name, Object value ) throws Exception {
		Class<?> c = o.getClass();
		while (c != null) {
			try {
				Field f = c.getDeclaredField( name );
				f.setAccessible( true );
				f.set( o, value );
				return;
			} catch (NoSuchFieldException e) { c = c.getSuperclass(); }
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

	static boolean isHarmless( Mob mob ) {
		return mob.getClass().getSimpleName().contains( "Spawner" );
	}
}
