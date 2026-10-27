package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.levels.CavesBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.CityBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.HallsBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.LastLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.PrisonBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.SewerBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.WorldShuffle;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Boss crawl, a run that is only boss floors (an ordinary run, not a sandbox one). It goes through five segments,
 * the floor 5 bosses first and the floor 25 bosses last, Shattered's own boss first in each. Before each segment
 * the hero stops in Magic Ling's town, gets SEGMENT_GOLD to spend at the merchant (CrawlMerchant, every item,
 * never runs out) and can rest in the glowing pool. The town's north stairs lead on to the segment's first boss.
 * The last stop is the Amulet floor, taking the Amulet there ends the run and ranks it like a normal win.
 * Started from the Boss crawl button on the title screen, see Dungeon.bossCrawl.
 */
public class BossCrawl {

	/** Which stop the run is on, saved with the run. */
	public static int step = 0;

	/** Gold handed out on arriving in the town before each segment, the first one included. */
	public static final int SEGMENT_GOLD = 1500;

	//each stop is { world, depth, variant }, world -1 is Shattered's own boss, world -2 the Amulet floor, world -3 the town
	private static ArrayList<int[]> stops;
	//a readable name for each stop, in the same order
	private static ArrayList<String> names;

	public static ArrayList<int[]> stops(){
		if (stops == null) {
			stops = new ArrayList<>();
			names = new ArrayList<>();
			HashSet<String> seen = new HashSet<>();
			String[] shattered = { "Sewer boss", "Prison boss", "Caves boss", "City boss", "Halls boss" };
			for (int depth = 5; depth <= 25; depth += 5) {
				stops.add( new int[]{ -3, depth, 0 } );
				names.add( "Town, before the floor " + depth + " bosses" );
				stops.add( new int[]{ -1, depth, 0 } );
				names.add( "Shattered " + shattered[depth/5 - 1] );
				for (int w = 0; w < WorldShuffle.worldCount(); w++) {
					Level[] vs = WorldShuffle.modBossVariants( w, depth );
					for (int v = 0; v < vs.length; v++) {
						//several worlds of one mod share the same boss, keep it once
						if (seen.add( depth + ":" + vs[v].getClass().getName() )) {
							stops.add( new int[]{ w, depth, v } );
							names.add( WorldShuffle.modName( w ).replace( " Pixel Dungeon", "" ) + ", " + readable( vs[v].getClass().getSimpleName() ) );
						}
					}
				}
			}
			stops.add( new int[]{ -2, 26, 0 } );
			names.add( "The Amulet of Yendor" );
		}
		return stops;
	}

	/** The readable name of stop i, the mod and the boss floor's class name spelled out. */
	public static String name( int i ){
		stops();
		return i >= 0 && i < names.size() ? names.get( i ) : "";
	}

	//ShopBossLevel becomes "Shop boss"
	private static String readable( String cls ){
		String n = cls.endsWith( "Level" ) ? cls.substring( 0, cls.length() - 5 ) : cls;
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < n.length(); i++) {
			char c = n.charAt( i );
			if (i > 0 && Character.isUpperCase( c ) && !Character.isUpperCase( n.charAt( i - 1 ) )) b.append( ' ' );
			b.append( i > 0 ? Character.toLowerCase( c ) : c );
		}
		return b.toString();
	}

	/** Moves the run to stop i, its boss floor built fresh. */
	public static void jumpTo( int i ){
		if (i < 0 || i >= stops().size()) return;
		step = i;
		Level.beforeTransition();
		com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene.mode = com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene.Mode.SANDBOX;
		com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene.returnDepth = stops().get( i )[1];
		com.watabou.noosa.Game.switchScene( com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene.class );
	}

	/** True when the run is in the town between segments. */
	public static boolean inTown(){
		int[] s = current();
		return s != null && s[0] == -3;
	}

	/** The segment's pay, given on arriving in the town. */
	public static void payForSegment(){
		Dungeon.gold += SEGMENT_GOLD;
		com.shatteredpixel.shatteredpixeldungeon.utils.GLog.p( com.shatteredpixel.shatteredpixeldungeon.messages.Messages.get( BossCrawl.class, "gold", SEGMENT_GOLD ) );
	}

	/** The stop the run is on, or null once every boss is done. */
	public static int[] current(){
		return step >= 0 && step < stops().size() ? stops().get( step ) : null;
	}

	/** Builds the boss floor of the current stop, or null when this depth is not the stop's floor. */
	public static Level build( int depth ){
		int[] s = current();
		if (s == null || s[1] != depth) return null;
		Dungeon.sandboxWorld = s[0] < 0 ? -1 : s[0];
		if (s[0] == -2) return new LastLevel();
		if (s[0] == -3) return new com.shatteredpixel.shatteredpixeldungeon.levels.NormalZeroFiveLevel();
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
