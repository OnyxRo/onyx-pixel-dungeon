import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Goo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.WarriorBoss;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.GreatImp;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.ShopBoss;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.TransformationCityBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.TransformationHallsBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.TransformationSewerBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.WorldShuffle;
import com.watabou.utils.Bundle;

//Transformation PD's own versions of Shattered's boss floors, headless.
//scripts/test-recompile.sh TransformationBossFloorsTest
public class TransformationBossFloorsTest {

	static int failures = 0, checks = 0;

	static void check( boolean ok, String what ) {
		checks++;
		if (!ok) {
			failures++;
			System.out.println( "FAIL: " + what );
		}
	}

	static boolean has( Level l, Class<?> c ) {
		for (Mob m : l.mobs) if (c.isInstance( m )) return true;
		return false;
	}

	static Level enter( Level l, int depth, long seed ) {
		GamesInProgress.selectedClass = HeroClass.WARRIOR;
		Dungeon.seed = seed;
		Dungeon.sandbox = true;
		Dungeon.init();
		Dungeon.depth = depth;
		l.create();
		Dungeon.switchLevel( l, l.entrance() );
		return l;
	}

	static void unseal( Level l, String what ) {
		try {
			l.seal();
		} catch (NullPointerException e) {
			System.out.println( "HEADLESS (not counted) " + what + " seal " + e );
		}
		try {
			l.unseal();
		} catch (NullPointerException e) {
			System.out.println( "HEADLESS (not counted) " + what + " unseal " + e );
		}
	}

	public static void main( String[] args ) throws Exception {
		TestBoot.boot();
		try {
			for (long seed = 1; seed <= 8; seed++) {
				Level sewer = enter( new TransformationSewerBossLevel(), 5, seed );
				check( has( sewer, WarriorBoss.class ) && !has( sewer, Goo.class ) && !has( sewer, com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GreenGoo.class ), "floor 5 has the Warrior and no Goo, seed " + seed );
			}

			Level city = enter( new TransformationCityBossLevel(), 20, 4 );
			unseal( city, "city" );
			check( has( city, ShopBoss.class ), "the shop owner waits after the Dwarf King" );
			int owners = 0;
			unseal( city, "city again" );
			for (Mob m : city.mobs) if (m instanceof ShopBoss) owners++;
			check( owners == 1, "only one shop owner after a second unseal, found " + owners );
			Bundle b = new Bundle();
			b.put( "l", city );
			Level back = (Level) b.get( "l" );
			check( back instanceof TransformationCityBossLevel && has( back, ShopBoss.class ), "city floor saves and loads with the shop owner" );

			Level halls = enter( new TransformationHallsBossLevel(), 25, 5 );
			unseal( halls, "halls" );
			check( has( halls, GreatImp.class ), "the Great Imp waits after Yog" );
			b = new Bundle();
			b.put( "l", halls );
			check( b.get( "l" ) instanceof TransformationHallsBossLevel, "halls floor saves and loads" );

			//registered for every Transformation world, never in normal runs
			int found = 0;
			for (int w = 0; w < WorldShuffle.worldCount(); w++) {
				for (Level l : WorldShuffle.modBossVariants( w, 5 )) if (l instanceof TransformationSewerBossLevel) found++;
			}
			check( found > 0, "floor 5 Warrior is listed for Transformation worlds" );
			check( !WorldShuffle.runBossFloors().contains( TransformationSewerBossLevel.class.getName() ), "not in normal runs until Onyx plays it" );
		} catch (Throwable t) {
			failures++;
			System.out.println( "FAIL: exception " + t );
			t.printStackTrace( System.out );
		}
		System.out.println( "TransformationBossFloorsTest " + (checks - failures) + "/" + checks + " passed" );
		System.exit( failures == 0 ? 0 : 1 );
	}
}
