import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.journal.Document;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.BarricadeRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

/**
 * Summoning PD Reincarnated leftovers, headless. Food healing, the Barricade room, the two badges, the guide pages,
 * and (added as they are ported) the ring artifacts, cloak glyphs, Soul of Yendor, talents, the Adventurer,
 * conducts, game modes and the tier challenge.
 */
public class SummoningLeftoversTest {

	static int failures = 0;
	static void fail( String what ) { failures++; System.out.println( "FAIL: " + what ); }
	static void check( boolean ok, String what ) { if (!ok) fail( what ); }

	static Hero startHero( HeroClass cls ) {
		GamesInProgress.selectedClass = cls;
		Dungeon.seed = 515151L;
		Dungeon.sandbox = true;
		Dungeon.init();
		Hero hero = Dungeon.hero;
		MobArenaTest.levelDepth = -1;
		MobArenaTest.level( 3 );
		Actor.remove( Dungeon.hero );
		int start = Dungeon.hero.pos;
		Dungeon.hero = hero;
		hero.live();
		hero.sprite = TestBoot.sprite();
		hero.sprite.ch = hero;
		hero.pos = start;
		Actor.add( hero );
		return hero;
	}

	public static void main( String[] args ) throws Exception {
		TestBoot.boot();
		Hero hero = startHero( HeroClass.CONJURER );

		//food heals the Conjurer
		hero.HP = 1;
		com.shatteredpixel.shatteredpixeldungeon.actors.hero.SummoningHooks.onFoodEaten( hero );
		check( hero.HP == 6, "Conjurer should heal 5 from food, HP " + hero.HP );

		//the Barricade room
		BarricadeRoom room = new BarricadeRoom();
		room.set( 2, 2, 12, 12 );
		room.connected.put( new BarricadeRoom(), new com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room.Door( 2, 7 ) );
		room.paint( Dungeon.level );
		int bars = 0;
		for (int x = 3; x < 12; x++) for (int y = 3; y < 12; y++) {
			if (Dungeon.level.map[x + y * Dungeon.level.width()] == Terrain.BARRICADE) bars++;
		}
		check( bars > 0 && bars < 81, "Barricade room should hold some barricades, has " + bars );

		//badges and guide pages have their text
		for (Badges.Badge b : new Badges.Badge[]{Badges.Badge.NIGHTMARE_SLAIN, Badges.Badge.SPIRIT_SLAIN}) {
			check( !b.title().contains( "!!!" ) && !b.desc().contains( "!!!" ), "badge text missing for " + b );
		}
		for (String page : new String[]{Document.GUIDE_SUMMONING, Document.GUIDE_TIERING}) {
			check( Document.ADVENTURERS_GUIDE.pageNames().contains( page ), "guide page missing " + page );
			check( !Document.ADVENTURERS_GUIDE.pageTitle( page ).contains( "!!!" ), "guide page title missing " + page );
			check( !Document.ADVENTURERS_GUIDE.pageBody( page ).contains( "!!!" ), "guide page body missing " + page );
		}

		finish();
	}

	static void finish() {
		System.out.println( "SUMMARY summoning leftovers failures=" + failures );
		System.exit( failures > 0 ? 1 : 0 );
	}
}
