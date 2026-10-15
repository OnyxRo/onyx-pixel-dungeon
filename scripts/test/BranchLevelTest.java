import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.FriendlyThief;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;

import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * Headless check for the side branches that come from Matt Edition. It builds whole dungeons, and every time
 * a floor has a branch transition it builds the floor on the other side and checks that the way back exists,
 * including the named transitions (transitionId and targetTransitionId). It also saves and reloads a floor
 * that holds a named transition. Fails if a floor throws, a transition leads nowhere, or an id is lost.
 *
 *   java -cp <classes>:<libs.jar>:<test classes> BranchLevelTest [seeds]
 */
public class BranchLevelTest {

	static int failures = 0;
	static int branchFloors = 0;
	static int named = 0;
	static int thiefRooms = 0;
	static int iceEntrances = 0;
	static int reloaded = 0;

	static byte[] bytes( com.watabou.utils.Bundle b ) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		com.watabou.utils.Bundle.write( b, out );
		return out.toByteArray();
	}

	static void fail( String what, Throwable t ) {
		failures++;
		System.out.println( "FAIL: " + what + (t != null ? " " + t : "") );
		if (t != null) t.printStackTrace( System.out );
	}

	static java.util.Map<String, Level> built = new java.util.HashMap<>();

	//builds the floor on the far side of every branch transition of l, then walks on from there
	static void follow( Level l, int depth, int branch, Set<String> seen, int guard ) {
		if (guard > 12) return;
		for (LevelTransition t : new java.util.ArrayList<>( l.transitions )) {
			boolean branchType = t.type == LevelTransition.Type.BRANCH_EXIT || t.type == LevelTransition.Type.BRANCH_ENTRANCE;
			if (!branchType && t.destBranch == branch) continue;
			if (t.destBranch == 0 && t.destDepth == 0) continue;
			if (t.transitionId != null || t.targetTransitionId != null) named++;
			String key = depth + ":" + branch + ">" + t.destDepth + ":" + t.destBranch + ":" + t.targetTransitionId;
			if (!seen.add( key )) continue;
			int savedDepth = Dungeon.depth, savedBranch = Dungeon.branch;
			try {
				Dungeon.depth = t.destDepth;
				Dungeon.branch = t.destBranch;
				String destKey = t.destDepth + ":" + t.destBranch;
				Level dest = built.get( destKey );
				if (dest == null) {
					dest = Dungeon.newLevel();
					built.put( destKey, dest );
				}
				branchFloors++;
				LevelTransition back = dest.getTransition( t.destType, t.targetTransitionId );
				if (back == null) {
					fail( "floor " + depth + " branch " + branch + " leads to floor " + t.destDepth + " branch " + t.destBranch
							+ " which has no transition of type " + t.destType + " id " + t.targetTransitionId, null );
				} else if (back.cell() <= 0 || back.cell() >= dest.length()) {
					fail( "arrival cell " + back.cell() + " is off the map on floor " + t.destDepth, null );
				}
				if (dest.entrance() < 0 && dest.exit() < 0 && dest.transitions.isEmpty()) {
					fail( "floor " + t.destDepth + " branch " + t.destBranch + " has no transitions at all", null );
				}
				if (t.transitionId != null && reloaded < 3) {
					com.watabou.utils.Bundle b = new com.watabou.utils.Bundle();
					Dungeon.level = l;
					b.put( "level", l );
					Dungeon.level = null;
					com.watabou.utils.Bundle reread = com.watabou.utils.Bundle.read(
							new java.io.ByteArrayInputStream( bytes( b ) ) );
					Level again = (Level) reread.get( "level" );
					boolean found = false;
					for (LevelTransition r : again.transitions) {
						if (t.transitionId.equals( r.transitionId )
								&& (t.targetTransitionId == null || t.targetTransitionId.equals( r.targetTransitionId ))) found = true;
					}
					if (!found) fail( "save and reload lost the transition id " + t.transitionId + " on floor " + depth, null );
					reloaded++;
				}
				follow( dest, t.destDepth, t.destBranch, seen, guard + 1 );
			} catch (Throwable e) {
				fail( "building floor " + t.destDepth + " branch " + t.destBranch + " from floor " + depth, e );
			} finally {
				Dungeon.depth = savedDepth;
				Dungeon.branch = savedBranch;
			}
		}
	}

	public static void main( String[] args ) throws Exception {
		int seeds = args.length > 0 ? Integer.parseInt( args[0] ) : 12;
		TestBoot.boot();
		HeroClass[] classes = {HeroClass.WARRIOR, HeroClass.MAGE, HeroClass.ROGUE, HeroClass.HUNTRESS, HeroClass.DUELIST, HeroClass.CLERIC};
		int floors = 0;

		for (int s = 0; s < seeds; s++) {
			long seed = 31000003L * (s + 1);
			GamesInProgress.selectedClass = classes[s % classes.length];
			Dungeon.seed = seed;
			Dungeon.init();
			Set<String> seen = new HashSet<>();
			built.clear();
			boolean thiefHere = false;
			for (int d = 1; d <= 20; d++) {
				Dungeon.depth = d;
				Dungeon.branch = 0;
				try {
					Level l = Dungeon.newLevel();
					built.put( d + ":0", l );
					floors++;
					if (d == 8 || d == 9) {
						if (FriendlyThief.Quest.spawned() && !thiefHere) {
							thiefHere = true;
							thiefRooms++;
							//the thief opens the entrance when asked, this adds the same transition FriendlyThief.openEntrance does
							int cell = FriendlyThief.Quest.getEntranceCell();
							if (cell <= 0 || cell >= l.length()) {
								fail( "seed " + seed + " floor " + d + " has the Friendly Thief room but no entrance cell", null );
							} else {
								l.transitions.add( new LevelTransition( l, cell, LevelTransition.Type.BRANCH_EXIT, d, 2,
										LevelTransition.Type.BRANCH_ENTRANCE, "guild_branch_entrance", "guild_entrance" ) );
							}
						}
					}
					if (d == 14) {
						boolean hasBranch = false;
						for (LevelTransition t : l.transitions) {
							if (t.destBranch == 2) hasBranch = true;
						}
						if (hasBranch) iceEntrances++;
						else fail( "seed " + seed + " floor 14 has no Ice Caves branch exit", null );
					}
					follow( l, d, 0, seen, 0 );
				} catch (Throwable t) {
					fail( "seed " + seed + " depth " + d, t );
				}
			}
		}
		System.out.println( "SUMMARY branch floors built: " + branchFloors + ", main floors: " + floors
				+ ", named transitions: " + named + ", thief rooms: " + thiefRooms + ", ice entrances: " + iceEntrances
				+ ", reloaded: " + reloaded + ", failures: " + failures );
		if (thiefRooms == 0) { System.out.println( "FAIL: no seed produced a Friendly Thief room" ); failures++; }
		if (failures > 0) System.exit( 1 );
	}
}
