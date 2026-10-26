import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.minilevels.MiniChestMazeLevel;
import com.watabou.utils.PathFinder;

/**
 * Headless check for the Diamond Knight's portal (Magic Ling). The portal sends the hero to floor 10 branch 4,
 * which must be the mimic maze, with an arrival cell, and enough mimics the hero can walk to for the five kills
 * that open the way back. Before 2026-10-26 branch 4 built an empty dead end room.
 *
 *   java -cp <classes>:<libs.jar>:<test classes> MimicMazeTest [seeds]
 */
public class MimicMazeTest {

	public static void main( String[] args ) throws Exception {
		int seeds = args.length > 0 ? Integer.parseInt( args[0] ) : 8;
		TestBoot.boot();
		int failures = 0;
		for (int s = 0; s < seeds; s++) {
			com.shatteredpixel.shatteredpixeldungeon.GamesInProgress.selectedClass = com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass.WARRIOR;
			Dungeon.seed = 7700001L * (s + 1);
			Dungeon.init();
			Dungeon.depth = 10;
			Dungeon.branch = 4;
			try {
				Level l = Dungeon.newLevel();
				if (!(l instanceof MiniChestMazeLevel)) {
					System.out.println( "FAIL: floor 10 branch 4 is " + l.getClass().getSimpleName() );
					failures++;
					continue;
				}
				LevelTransition arrive = l.getTransition( LevelTransition.Type.REGULAR_EXIT );
				if (arrive == null || arrive.cell() <= 0 || arrive.cell() >= l.length()) {
					System.out.println( "FAIL: seed " + Dungeon.seed + " maze has no arrival cell" );
					failures++;
					continue;
				}
				boolean[] pass = new boolean[l.length()];
				for (int i = 0; i < pass.length; i++) pass[i] = l.passable[i] || l.avoid[i];
				PathFinder.setMapSize( l.width(), l.height() );
				PathFinder.buildDistanceMap( arrive.cell(), pass );
				int mimics = 0, reachable = 0;
				for (Mob m : l.mobs) {
					if (!(m instanceof Mimic)) continue;
					mimics++;
					boolean near = false;
					for (int o : PathFinder.NEIGHBOURS9) {
						int c = m.pos + o;
						if (c >= 0 && c < l.length() && PathFinder.distance[c] != Integer.MAX_VALUE) near = true;
					}
					if (near) reachable++;
				}
				System.out.println( "seed " + Dungeon.seed + " mimics " + mimics + " reachable " + reachable );
				if (reachable < 5) {
					System.out.println( "FAIL: seed " + Dungeon.seed + " only " + reachable + " mimics can be reached, five kills are needed" );
					failures++;
				}
			} catch (Throwable t) {
				System.out.println( "FAIL: seed " + Dungeon.seed + " " + t );
				t.printStackTrace( System.out );
				failures++;
			}
		}
		System.out.println( "SUMMARY maze seeds " + seeds + ", failures " + failures );
		System.exit( failures == 0 ? 0 : 1 );
	}
}
