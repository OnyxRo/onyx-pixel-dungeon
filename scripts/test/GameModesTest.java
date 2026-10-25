import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GameModes;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.Playstyles;
import com.shatteredpixel.shatteredpixeldungeon.RadarSearch;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CapeOfThorns;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.ScrollHolder;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingStone;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;

//the game modes from Half PD, Even Shorter PD, Radar and Custom PD's starting kits
public class GameModesTest {

	static int failures = 0, checks = 0;

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

	static void newRun( long seed, String... modes ) {
		GameModes.active.clear();
		for (String m : modes) GameModes.set( m, 1 );
		Playstyles.refresh();
		GamesInProgress.selectedClass = HeroClass.WARRIOR;
		Dungeon.seed = seed;
		Dungeon.sandbox = true;
		com.shatteredpixel.shatteredpixeldungeon.SPDSettings.challenges( 0 );
		Dungeon.init();
	}

	static Level build( int depth ) {
		Dungeon.depth = depth;
		Level l = Dungeon.newLevel();
		Dungeon.switchLevel( l, l.entrance() );
		return l;
	}

	static int exitDepth( Level l, LevelTransition.Type type ) {
		for (LevelTransition t : l.transitions) if (t.type == type) return t.destDepth;
		return -99;
	}

	static boolean has( Class<?> cls ) {
		for (Item i : Dungeon.hero.belongings) if (cls.isInstance( i )) return true;
		return false;
	}

	public static void main( String[] args ) throws Exception {
		TestBoot.boot();

		for (GameModes.Mode m : GameModes.MODES) {
			check( hasText( m.title() ) && hasText( m.desc() ), "text for " + m.id );
		}
		for (String w : new String[]{ "title", "desc", "clear", "button_none", "radar_items", "stat_melee", "stat_xray" }) {
			check( hasText( Messages.get( com.shatteredpixel.shatteredpixeldungeon.windows.WndGameModes.class, w ) ), "window text " + w );
		}

		try {
			//groups, the two layouts cannot both be on
			GameModes.active.clear();
			GameModes.set( GameModes.HALF, 1 );
			GameModes.set( GameModes.EVEN_SHORTER, 1 );
			check( !GameModes.on( GameModes.HALF ) && GameModes.on( GameModes.EVEN_SHORTER ), "layouts replace each other" );

			//Half PD
			newRun( 11, GameModes.HALF );
			int floors = 0;
			for (int d = 1; d < 26; d = GameModes.nextDepth( d )) floors++;
			check( floors == 15, "Half has 15 floors before the amulet, got " + floors );
			Level l = build( 1 );
			check( exitDepth( l, LevelTransition.Type.REGULAR_EXIT ) == 4, "Half floor 1 leads to floor 4" );
			l = build( 4 );
			check( exitDepth( l, LevelTransition.Type.REGULAR_ENTRANCE ) == 1, "Half floor 4 leads up to floor 1" );
			check( Dungeon.LimitedDrops.STRENGTH_POTIONS.count == 2, "Half sewers give 2 strength potions, got " + Dungeon.LimitedDrops.STRENGTH_POTIONS.count );
			check( Dungeon.LimitedDrops.UPGRADE_SCROLLS.count == 3, "Half sewers give 3 upgrade scrolls, got " + Dungeon.LimitedDrops.UPGRADE_SCROLLS.count );
			check( Dungeon.LimitedDrops.ARCANE_STYLI.count == 1, "Half sewers give a stylus" );
			for (int d : new int[]{ 5, 6, 9 }) build( d );
			check( Dungeon.LimitedDrops.UPGRADE_SCROLLS.count == 6, "Half prison gives 3 more upgrade scrolls, got " + Dungeon.LimitedDrops.UPGRADE_SCROLLS.count );
			check( Hero.maxExp( 1 ) == 6, "Half levels up faster" );
			l = build( 26 );
			check( exitDepth( l, LevelTransition.Type.REGULAR_ENTRANCE ) == 25, "Half amulet floor leads up to 25" );

			//saved with the run
			Bundle b = new Bundle();
			GameModes.store( b );
			GameModes.active.clear();
			GameModes.restore( b );
			check( GameModes.on( GameModes.HALF ), "modes come back after a save" );

			//Even Shorter PD
			newRun( 12, GameModes.EVEN_SHORTER );
			check( GameModes.nextDepth( 10 ) == 26 && GameModes.prevDepth( 26 ) == 10, "Even Shorter goes from Tengu to the amulet" );
			l = build( 10 );
			check( exitDepth( l, LevelTransition.Type.REGULAR_EXIT ) == 26, "Tengu's floor leads to the amulet floor" );
			l = build( 26 );
			check( exitDepth( l, LevelTransition.Type.REGULAR_ENTRANCE ) == 10, "amulet floor leads up to Tengu" );
			check( GameModes.maxLevel() == 12, "Even Shorter caps the level at 12" );
			for (int i = 0; i < 40; i++) {
				Item w = com.shatteredpixel.shatteredpixeldungeon.items.Generator.randomWeapon( 4 );
				check( ((com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon) w).tier <= 3, "Even Shorter weapons stay at tier 3 or lower" );
			}
			check( Hero.maxExp( 1 ) == 10, "Even Shorter keeps the normal level curve" );

			//no mode, the normal dungeon
			newRun( 13 );
			check( GameModes.nextDepth( 1 ) == 2 && GameModes.prevDepth( 26 ) == 25 && GameModes.maxLevel() == Hero.MAX_LEVEL, "no mode keeps every floor" );

			//Radar playstyles
			newRun( 14 );
			int ht = Dungeon.hero.HT;
			GameModes.set( Playstyles.PREFIX + "glass_cannon", 3 );
			Playstyles.refresh();
			Dungeon.hero.updateHT( false );
			check( Dungeon.hero.HT < ht, "Glass Cannon lowers health" );
			check( Playstyles.value( Playstyles.RANGED ) > 1.5f && Playstyles.value( Playstyles.MAGIC ) > 1.5f, "Glass Cannon raises ranged and magic damage" );
			GameModes.set( Playstyles.PREFIX + "glass_cannon", 0 );
			Playstyles.refresh();
			check( !Playstyles.any() && Playstyles.value( Playstyles.HP ) == 1f, "no playstyle changes nothing" );

			//Custom PD kits
			newRun( 15, GameModes.KIT_CAPE, GameModes.KIT_MORSTOR, GameModes.KIT_ROXX );
			check( Dungeon.hero.belongings.artifact instanceof CapeOfThorns || Dungeon.hero.belongings.misc instanceof CapeOfThorns, "Cape of Thorns is worn" );
			check( has( ScrollHolder.class ), "MorStor gives a scroll holder" );
			boolean roxx = false;
			for (Item i : Dungeon.hero.belongings) if (i instanceof ThrowingStone && i.level() == 20 && i.quantity() == 20) roxx = true;
			check( roxx, "Roxx gives twenty +20 throwing stones" );
			for (String kit : new String[]{ GameModes.KIT_CURSED, GameModes.KIT_FACTS, GameModes.KIT_MODERN, GameModes.KIT_ROCC, GameModes.KIT_STARTER, GameModes.KIT_KNOWLEDGE }) {
				int before = 0;
				newRun( 16 );
				for (Item i : Dungeon.hero.belongings) before++;
				newRun( 16, kit );
				int after = 0;
				for (Item i : Dungeon.hero.belongings) after++;
				check( after > before || kit.equals( GameModes.KIT_KNOWLEDGE ), kit + " adds items" );
			}
			check( new com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHaste().isKnown(), "Superior Knowledge identifies potions" );

			//Radar seed search, a ration of food is on floor 1 of most seeds
			String old = RadarSearch.items( 0 );
			RadarSearch.items( 0, "ration" );
			newRun( 17 );
			RadarSearch search = new RadarSearch();
			long seed = search.search();
			check( seed >= 0, "Radar finds a seed with a ration on floor 1" );
			check( search.failed == 0, "Radar seeds build without errors" );
			System.out.println( "INFO radar checked " + search.checked + " seeds" );
			RadarSearch.items( 0, old );

			//timing of a deep search, only when asked with -Dradar.timing=true
			if (Boolean.getBoolean( "radar.timing" )) {
				String old3 = RadarSearch.items( 3 );
				RadarSearch.items( 3, "no item has this name" );
				final RadarSearch slow = new RadarSearch();
				new Thread( () -> { try { Thread.sleep( 30000 ); } catch (InterruptedException ignored) {} slow.cancelled = true; } ).start();
				slow.search();
				System.out.println( "INFO radar timing, " + slow.checked + " seeds to floor 10 in 30 seconds, " + slow.failed + " failed" );
				if (slow.lastError != null) slow.lastError.printStackTrace( System.out );
				RadarSearch.items( 3, old3 );
			}
		} catch (Throwable t) {
			failures++;
			System.out.println( "FAIL: threw " + t );
			StackTraceElement[] st = t.getStackTrace();
			for (int i = 0; i < Math.min( 12, st.length ); i++) System.out.println( "        at " + st[i] );
		}
		GameModes.active.clear();
		System.out.println( "SUMMARY gamemodes: " + checks + " checks, failures: " + failures );
		System.exit( failures == 0 ? 0 : 1 );
	}
}
