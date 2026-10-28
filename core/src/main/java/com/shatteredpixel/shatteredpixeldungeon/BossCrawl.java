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
 * the floor 5 bosses first and the floor 25 bosses last, each floor's easiest boss first (see RANKS and difficulty). Before each segment
 * the hero stops in Transformation's surface town (with the Dungeon Doctor), gets SEGMENT_GOLD to spend at the merchant (CrawlMerchant, every item,
 * never runs out) and can rest in the glowing lake. The manor stair leads on to the segment's first boss.
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

	/**
	 * How hard the run is, 1 easy, 2 medium, 3 hard, 4 insane. Each floor's bosses are sorted from easiest
	 * to hardest by RANKS and split into four groups of about the same size. Easy fights the easiest group,
	 * medium the two easiest, hard three and insane every boss. Saved with the run.
	 */
	public static int difficulty = 4;

	//each floor's bosses from easiest to hardest, by the simple class name of the boss floor.
	//Ranked from their health, damage and tricks (2026-10-28). A boss missing here goes last, so only insane gets it
	private static final String[][] RANKS = {
		//floor 5
		{ "SewerBossUsurperLevel", "GardenBossLevel", "SewerBossLevel",
		  "SewerBossGooLevel", "Forest", "ForestBossLevel",
		  "CrabKingBossLevel", "ForestPoisonBossLevel", "SLMKingLevel",
		  "SkyGooBossLevel", "ForestHardBossLevel" },
		//floor 10
		{ "OldPrisonBossLevel", "PrisonBossLevel", "CursedTenguBossLevel",
		  "NewPrisonBossLevel", "DeepShadowLevel", "ColdhouseBossLevel",
		  "Gnoll", "CursedPrisonBossLevel", "GreenStlingBossLevel",
		  "ColdChestBossLevel", "TransformationRunBossLevel" },
		//floor 15
		{ "OldCavesBossLevel", "CavesBossLevel", "CaveTwoBossLevel",
		  "ForgeBossLevel", "CavesGirlDeadLevel", "RogerBossLevel",
		  "NewCavesBossLevel", "AncientMysteryCityBossLevel", "ShopBossLevel",
		  "CursedCavesBossLevel", "TransformationRunBossLevel" },
		//floor 20
		{ "OldCityBossLevel", "CityBossLevel", "DwarfMasterBossLevel",
		  "DwarfGeneralBossLevel", "CitadelBossLevel", "CursedCityBossLevel",
		  "TransformationRunBossLevel" },
		//floor 25
		{ "OldHallsBossLevel", "DragonBossLevel", "HallsBossLevel",
		  "YogGodHardBossLevel", "LabsBossLevel", "TransformationRunBossLevel",
		  "CursedHallsBossLevel", "VoidBossLevel", "FinalLevel" },
	};

	//boss floors that are an exact copy of another one already in the list
	private static final HashSet<String> COPIES = new HashSet<>( java.util.Arrays.asList(
		"CursedSewerBossLevel",	//Reforged's Goo floor line for line
		"NewCityBossLevel",		//Shattered's Dwarf King in another arena
		"NewHallsBossLevel"		//Shattered's Yog-Dzewa in another arena
	) );

	private static final String[] SHATTERED_LEVELS = { "SewerBossLevel", "PrisonBossLevel", "CavesBossLevel", "CityBossLevel", "HallsBossLevel" };

	//the difficulty and banned mods the list was built for, it is built again when either changes
	private static String builtFor = null;

	public static ArrayList<int[]> stops(){
		String key = difficulty + ":" + bannedKey();
		if (stops != null && !key.equals( builtFor )) stops = null;
		builtFor = key;
		if (stops == null) {
			stops = new ArrayList<>();
			names = new ArrayList<>();
			HashSet<String> seen = new HashSet<>();
			String[] shattered = { "Sewer boss", "Prison boss", "Caves boss", "City boss", "Halls boss" };
			for (int depth = 5; depth <= 25; depth += 5) {
				stops.add( new int[]{ -3, depth, 0 } );
				names.add( "Town, before the floor " + depth + " bosses" );

				ArrayList<int[]> segStops = new ArrayList<>();
				ArrayList<String> segNames = new ArrayList<>();
				final ArrayList<String> segKeys = new ArrayList<>();
				segStops.add( new int[]{ -1, depth, 0 } );
				segNames.add( "Shattered " + shattered[depth/5 - 1] );
				segKeys.add( SHATTERED_LEVELS[depth/5 - 1] );
				for (int w = 0; w < WorldShuffle.worldCount(); w++) {
					//a mod banned in the title screen's mods list sends none of its bosses
					if (WorldShuffle.isBanned( WorldShuffle.modName( w ) )) continue;
					Level[] vs = WorldShuffle.modBossVariants( w, depth );
					for (int v = 0; v < vs.length; v++) {
						String cls = vs[v].getClass().getSimpleName();
						if (COPIES.contains( cls )) continue;
						//several worlds of one mod share the same boss, keep it once
						if (seen.add( depth + ":" + vs[v].getClass().getName() )) {
							segStops.add( new int[]{ w, depth, v } );
							segNames.add( WorldShuffle.modName( w ).replace( " Pixel Dungeon", "" ) + ", " + readable( cls ) );
							segKeys.add( cls );
						}
					}
				}

				//easiest first, then keep the groups this difficulty fights
				final String[] rank = RANKS[depth/5 - 1];
				ArrayList<Integer> order = new ArrayList<>();
				for (int i = 0; i < segStops.size(); i++) order.add( i );
				java.util.Collections.sort( order, new java.util.Comparator<Integer>() {
					@Override
					public int compare( Integer a, Integer b ) {
						return Integer.compare( rankOf( rank, segKeys.get( a ) ), rankOf( rank, segKeys.get( b ) ) );
					}
				} );
				int n = order.size();
				int keep = Math.max( 1, (int)Math.ceil( n * Math.max( 1, Math.min( 4, difficulty ) ) / 4f ) );
				for (int i = 0; i < Math.min( keep, n ); i++) {
					stops.add( segStops.get( order.get( i ) ) );
					names.add( segNames.get( order.get( i ) ) );
				}
			}
			stops.add( new int[]{ -2, 26, 0 } );
			names.add( "The Amulet of Yendor" );
		}
		return stops;
	}

	private static int rankOf( String[] rank, String cls ){
		for (int i = 0; i < rank.length; i++) if (rank[i].equals( cls )) return i;
		return rank.length;
	}

	private static String bannedKey(){
		StringBuilder b = new StringBuilder();
		for (String mod : WorldShuffle.mods()) if (WorldShuffle.isBanned( mod )) b.append( mod ).append( '|' );
		return b.toString();
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
		if (s[0] == -3) return new com.shatteredpixel.shatteredpixeldungeon.levels.SurfaceTownLevel();
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
