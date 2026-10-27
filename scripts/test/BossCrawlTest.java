import com.shatteredpixel.shatteredpixeldungeon.BossCrawl;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;

/**
 * Headless check for the boss crawl. Builds every stop the way the Pick a boss list does and checks the floor
 * built is the boss the list names. Before 2026-10-26 picking Magic Ling's shop boss built a different boss.
 *
 *   java -cp <classes>:<libs.jar>:<test classes> BossCrawlTest
 */
public class BossCrawlTest {

	public static void main( String[] args ) throws Exception {
		TestBoot.boot();
		GamesInProgress.selectedClass = HeroClass.WARRIOR;
		Dungeon.seed = 4242;
		Dungeon.init();
		Dungeon.sandbox = true;
		Dungeon.bossCrawl = true;
		int failures = 0;
		for (int i = 0; i < BossCrawl.stops().size(); i++) {
			BossCrawl.step = i;
			int[] s = BossCrawl.current();
			Dungeon.depth = s[1];
			Dungeon.branch = 0;
			try {
				Level l = Dungeon.newLevel();
				String built = l.getClass().getSimpleName();
				String want = BossCrawl.name( i );
				String wantCls = want.substring( want.lastIndexOf( ", " ) + 2 ).replace( " ", "" ).toLowerCase();
				boolean ok = s[0] == -1 || built.toLowerCase().replace( "level", "" ).equals( wantCls );
				System.out.println( (ok ? "ok " : "FAIL: ") + i + " " + want + " built " + built );
				if (!ok) failures++;
			} catch (Throwable t) {
				System.out.println( "FAIL: " + i + " " + BossCrawl.name( i ) + " threw " + t );
				failures++;
			}
		}
		System.out.println( "SUMMARY boss crawl stops " + BossCrawl.stops().size() + ", failures " + failures );
		System.exit( failures == 0 ? 0 : 1 );
	}
}
