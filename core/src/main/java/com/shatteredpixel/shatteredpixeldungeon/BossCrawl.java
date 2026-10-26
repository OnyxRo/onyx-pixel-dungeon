package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.levels.CavesBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.CityBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.HallsBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.PrisonBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.SewerBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.WorldShuffle;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Boss crawl, a sandbox run that is only boss floors. It starts on the easiest boss and every exit leads
 * to the next one, floor 5 bosses first and floor 25 bosses last, Shattered's own boss first on each floor.
 * Started from the Boss crawl button on the title screen, see Dungeon.bossCrawl.
 */
public class BossCrawl {

	/** Which stop the run is on, saved with the run. */
	public static int step = 0;

	//each stop is { world, depth, variant }, world -1 is Shattered's own boss
	private static ArrayList<int[]> stops;

	public static ArrayList<int[]> stops(){
		if (stops == null) {
			stops = new ArrayList<>();
			HashSet<String> seen = new HashSet<>();
			for (int depth = 5; depth <= 25; depth += 5) {
				stops.add( new int[]{ -1, depth, 0 } );
				for (int w = 0; w < WorldShuffle.worldCount(); w++) {
					Level[] vs = WorldShuffle.modBossVariants( w, depth );
					for (int v = 0; v < vs.length; v++) {
						//several worlds of one mod share the same boss, keep it once
						if (seen.add( depth + ":" + vs[v].getClass().getName() )) stops.add( new int[]{ w, depth, v } );
					}
				}
			}
		}
		return stops;
	}

	/** The stop the run is on, or null once every boss is done. */
	public static int[] current(){
		return step >= 0 && step < stops().size() ? stops().get( step ) : null;
	}

	/** Builds the boss floor of the current stop, or null when this depth is not the stop's floor. */
	public static Level build( int depth ){
		int[] s = current();
		if (s == null || s[1] != depth) return null;
		Dungeon.sandboxWorld = s[0];
		if (s[0] == -1) {
			switch (depth) {
				case 5:  return new SewerBossLevel();
				case 10: return new PrisonBossLevel();
				case 15: return new CavesBossLevel();
				case 20: return new CityBossLevel();
				default: return new HallsBossLevel();
			}
		}
		WorldShuffle.forcedVariant = s[2];
		try {
			return WorldShuffle.modBossLevel( s[0], depth );
		} finally {
			WorldShuffle.forcedVariant = -1;
		}
	}
}
