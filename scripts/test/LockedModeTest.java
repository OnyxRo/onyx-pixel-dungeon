import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GameModes;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.ModsUnlock;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.WorldShuffle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

import java.util.TreeSet;

//a copy from GitHub plays plain Shattered until Yog is beaten, see ModsUnlock
public class LockedModeTest {

	static int failures = 0, checks = 0;
	static TreeSet<String> strays = new TreeSet<>();

	static void check( boolean ok, String what ) {
		checks++;
		if (!ok) {
			failures++;
			System.out.println( "FAIL: " + what );
		}
	}

	static boolean hasText( String s ) {
		return s != null && !s.contains( "NO_TEXT" ) && !s.startsWith( "!!!" );
	}

	static void stray( Class<?> c, String where ) {
		if (!ModsUnlock.isOriginal( c )) strays.add( c.getName().replace( "com.shatteredpixel.shatteredpixeldungeon.", "" ) + " (" + where + ")" );
	}

	public static void main( String[] args ) throws Exception {
		TestBoot.boot();
		int seeds = args.length > 0 ? Integer.parseInt( args[0] ) : 20;

		//a copy from GitHub, never unlocked
		ModsUnlock.dev = false;
		SPDSettings.put( "onyx_mods_unlocked", false );
		check( ModsUnlock.locked(), "locked in a fresh copy" );
		check( ModsUnlock.isOriginal( com.shatteredpixel.shatteredpixeldungeon.items.food.StewedMeat.twoMeat.class ), "Shattered's nested classes count" );
		check( !ModsUnlock.isOriginal( com.shatteredpixel.shatteredpixeldungeon.ModsUnlock.class ), "new classes do not count" );

		for (HeroClass cl : HeroClass.values()) {
			check( cl.isUnlocked() == ModsUnlock.isShatteredHero( cl ), "hero " + cl + " only open if Shattered's" );
		}
		SPDSettings.put( "onyx_game_modes", GameModes.HALF + "=1" );
		GameModes.loadSettings();
		check( GameModes.count() == 0, "no game modes" );

		for (String k : new String[]{ "title", "body", "open_title", "open_body" }) {
			check( hasText( Messages.get( com.shatteredpixel.shatteredpixeldungeon.windows.WndModsLocked.class, k ) ), "welcome text " + k );
		}
		for (String k : new String[]{ "title", "body_on", "body_off", "turn_on", "turn_off", "start_over" }) {
			check( hasText( Messages.get( com.shatteredpixel.shatteredpixeldungeon.windows.WndFriendView.class, k ) ), "friend view text " + k );
		}
		check( hasText( Messages.get( ModsUnlock.class, "unlocked_log" ) ), "unlock log text" );

		for (int s = 1; s <= seeds; s++) {
			GamesInProgress.selectedClass = HeroClass.values()[s % 6];
			Dungeon.seed = 1000 + s;
			Dungeon.sandbox = false;
			SPDSettings.challenges( Challenges.MAX_VALUE );
			SPDSettings.difficulty( 4 );
			SPDSettings.sins( 1 );
			Dungeon.init();
			check( (Dungeon.challenges & ~Challenges.SHATTERED_MASK) == 0, "only Shattered challenges, seed " + s );
			check( Dungeon.difficulty == 2 && Dungeon.sins == 0, "no difficulty or sins, seed " + s );
			for (Item i : Dungeon.hero.belongings) stray( i.getClass(), "start kit" );

			for (int depth = 1; depth <= 26; depth++) {
				check( !WorldShuffle.shuffles( depth ), "no shuffle on floor " + depth );
				Dungeon.depth = depth;
				Level l = Dungeon.newLevel();
				Dungeon.switchLevel( l, l.entrance() );
				stray( l.getClass(), "level " + depth );
				for (Mob m : l.mobs) stray( m.getClass(), "mob floor " + depth );
				for (Heap h : l.heaps.valueList()) for (Item i : h.items) stray( i.getClass(), "item floor " + depth );
				for (int n = 0; n < 15; n++) {
					Item i = Generator.random();
					if (i != null) stray( i.getClass(), "Generator floor " + depth );
				}
			}
		}
		SPDSettings.challenges( 0 );
		SPDSettings.difficulty( 2 );
		SPDSettings.sins( 0 );

		for (String st : strays) System.out.println( "STRAY: " + st );
		check( strays.isEmpty(), "nothing from the mods in " + seeds + " plain runs" );

		//beating Yog opens everything
		ModsUnlock.unlock();
		check( !ModsUnlock.locked() && ModsUnlock.newsDue(), "Yog opens the mods" );
		check( HeroClass.values().length > 6 && HeroClass.values()[6].isUnlocked(), "mod heroes open after" );
		ModsUnlock.newsShown();
		SPDSettings.put( "onyx_mods_unlocked", false );
		ModsUnlock.dev = true;
		check( !ModsUnlock.locked(), "Onyx's copy is never locked" );

		System.out.println( "LockedModeTest " + checks + " checks, " + failures + " failures" );
		System.exit( failures == 0 ? 0 : 1 );
	}
}
