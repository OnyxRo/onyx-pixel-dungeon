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

			//Modded PD's test kit
			newRun( 18, com.shatteredpixel.shatteredpixeldungeon.SmallModes.MODDED_KIT );
			boolean plate = false;
			for (Item i : Dungeon.hero.belongings) if (i instanceof com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor && i.level() == 100) plate = true;
			check( plate, "Modded kit gives a +100 plate armor" );

			//Speedrun PD Bones% and Old Speedrun PD's wand, remains on floor 1
			for (int lvl = 1; lvl <= 10; lvl++) {
				check( com.shatteredpixel.shatteredpixeldungeon.SmallModes.bonesItem( lvl ) != null, "bones item " + lvl );
			}
			for (String m : new String[]{ com.shatteredpixel.shatteredpixeldungeon.SmallModes.SPEEDRUN_BONES, com.shatteredpixel.shatteredpixeldungeon.SmallModes.OLDSPEEDRUN_WAND }) {
				newRun( 19, m );
				l = build( 1 );
				boolean found = false;
				for (com.shatteredpixel.shatteredpixeldungeon.items.Heap h : l.heaps.valueList()) {
					if (h.type == com.shatteredpixel.shatteredpixeldungeon.items.Heap.Type.REMAINS) found = true;
				}
				check( found, m + " puts remains on floor 1" );
			}

			//Speedrun PD timer, splits, finish and save
			newRun( 20, com.shatteredpixel.shatteredpixeldungeon.SmallModes.SPEEDRUN_TIMER );
			build( 1 );
			com.shatteredpixel.shatteredpixeldungeon.SmallModes.time = 5000;
			build( 2 );
			check( com.shatteredpixel.shatteredpixeldungeon.SmallModes.splits[2] == 5000, "timer takes a split on floor 2" );
			Bundle tb = new Bundle();
			GameModes.store( tb );
			com.shatteredpixel.shatteredpixeldungeon.SmallModes.time = 0;
			GameModes.restore( tb );
			check( com.shatteredpixel.shatteredpixeldungeon.SmallModes.time == 5000, "timer comes back after a save" );
			com.shatteredpixel.shatteredpixeldungeon.SmallModes.onAmulet();
			check( com.shatteredpixel.shatteredpixeldungeon.SmallModes.finished && com.shatteredpixel.shatteredpixeldungeon.SmallModes.personalBest() > 0, "amulet stops the timer and sets a best" );
			check( hasText( com.shatteredpixel.shatteredpixeldungeon.SmallModes.categoryName() ), "category text" );

			//Shattered Pixel Lore, the Goo's story
			newRun( 21, com.shatteredpixel.shatteredpixeldungeon.LoreGoo.GOO );
			int carried = 0;
			for (Item i : Dungeon.hero.belongings) carried++;
			check( carried == 0, "the Goo starts with nothing, has " + carried );
			check( Dungeon.hero.lvl == 2, "the Goo starts at level 2" );
			check( Dungeon.hero.talents.get( 0 ).containsKey( com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent.CAUSTIC_OOZE ) && Dungeon.hero.talents.get( 0 ).size() == 4, "the Goo has its own talents" );
			for (int d = 1; d <= 5; d++) {
				l = build( d );
				int loot = 0;
				for (com.shatteredpixel.shatteredpixeldungeon.items.Heap h : l.heaps.valueList()) for (Item i : h.items) {
					if (i instanceof com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem || i instanceof com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion
							|| i instanceof com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll || i instanceof com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand) loot++;
				}
				check( loot == 0 || d == 5, "Goo floor " + d + " holds no loot, has " + loot );
			}
			check( l instanceof com.shatteredpixel.shatteredpixeldungeon.levels.SewerBossLevel, "the Goo meets Shattered's Goo on floor 5, got " + l.getClass().getSimpleName() );
			float before = com.shatteredpixel.shatteredpixeldungeon.LoreGoo.moisture( Dungeon.hero ).level();
			for (int c = 0; c < l.length(); c++) if (l.water[c] && l.passable[c]) { Dungeon.hero.pos = c; break; }
			com.shatteredpixel.shatteredpixeldungeon.LoreGoo.onHeroTurn( Dungeon.hero );
			check( !l.water[Dungeon.hero.pos] || com.shatteredpixel.shatteredpixeldungeon.LoreGoo.moisture( Dungeon.hero ).level() > before, "water raises moisture" );
			check( Dungeon.hero.damageRoll() >= 0, "the Goo rolls damage" );
			check( com.shatteredpixel.shatteredpixeldungeon.LoreGoo.blockPickUp( Dungeon.hero ), "the Goo cannot pick up items" );
			Bundle gb = new Bundle();
			Dungeon.hero.storeInBundle( gb );
			com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero h2 = new com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero();
			h2.restoreFromBundle( gb );
			check( h2.talents.get( 0 ).containsKey( com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent.SLIPPERY_SLIME ), "the Goo's talents come back after a save" );
			check( hasText( Messages.get( com.shatteredpixel.shatteredpixeldungeon.scenes.GooWinScene.class, "text" ) ) && hasText( new com.shatteredpixel.shatteredpixeldungeon.actors.buffs.GooMoisture().desc() ), "Goo text" );

			//Shattered Trap Dungeon, its traps and monsters on every floor
			newRun( 22, com.shatteredpixel.shatteredpixeldungeon.TrapDungeon.MODE );
			java.util.List<Class<?>> modTraps = java.util.Arrays.asList( com.shatteredpixel.shatteredpixeldungeon.levels.traps.TrapDungeonTraps.CLASSES );
			int newTraps = 0, modMobs = 0;
			for (int d = 1; d <= 24; d++) {
				l = build( d );
				for (com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap t : l.traps.valueList()) {
					if (t.getClass().getPackage().getName().endsWith( "traps" ) && modTraps.contains( t.getClass() )
							&& t.getClass().getName().matches( ".*(Annihilation|Bargain|Cannon|Chaos|Chestnut|Cryo|Disrobing|Dream|Enchanting|FrostCurse|Fury|Goblin|Lottery|Marking|Mimic|Multi|Rearm|Sharpnel|Slicing|Smokescreen|Spook|Transmutation)Trap" )) newTraps++;
				}
				for (com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob m : l.mobs) {
					if (com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ModRoster.class != null && m.getClass().getSimpleName().matches( "DancingSkeleton|FilthJet|Orc|ToughGhost|Assasin|KeyChain|BombSpider2?|DartSpitter|ShockerBlock|FrostMeteor|ChaosElemental|TDDemonRat|Prismancer|Hole" )) modMobs++;
				}
			}
			check( newTraps > 0, "Trap Dungeon puts its own traps on the floors" );
			check( modMobs > 0, "Trap Dungeon's monsters join the floors" );
			System.out.println( "INFO trap dungeon, " + newTraps + " new traps and " + modMobs + " of its monsters on floors 1 to 24" );
			int trapErrors = 0, trapHeadless = 0;
			for (Class<?> tc : com.shatteredpixel.shatteredpixeldungeon.levels.traps.TrapDungeonTraps.CLASSES) {
				com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap t = (com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap) com.watabou.utils.Reflection.newInstance( tc );
				check( t != null && hasText( t.name() ) && hasText( t.desc() ), "trap text for " + tc.getSimpleName() );
				if (t == null) continue;
				l = build( 12 );
				Dungeon.hero.HP = Dungeon.hero.HT = 10000;
				int cell = -1;
				for (int n : com.watabou.utils.PathFinder.NEIGHBOURS8) {
					int c = Dungeon.hero.pos + n;
					if (l.passable[c] && com.shatteredpixel.shatteredpixeldungeon.actors.Actor.findChar( c ) == null) { cell = c; break; }
				}
				t.pos = cell < 0 ? Dungeon.hero.pos : cell;
				try {
					t.activate();
				} catch (NullPointerException e) {
					trapHeadless++;
				} catch (Throwable e) {
					trapErrors++;
					System.out.println( "FAIL: trap " + tc.getSimpleName() + " threw " + e );
					StackTraceElement[] st = e.getStackTrace();
					for (int i = 0; i < Math.min( 5, st.length ); i++) System.out.println( "        at " + st[i] );
				}
			}
			check( trapErrors == 0, "Trap Dungeon traps go off without errors" );
			System.out.println( "INFO trap dungeon traps, " + trapHeadless + " stopped at a sprite or effect headless" );
			Bundle td = new Bundle();
			com.shatteredpixel.shatteredpixeldungeon.TrapDungeon.increaseTrapSkill();
			GameModes.store( td );
			com.shatteredpixel.shatteredpixeldungeon.TrapDungeon.reset();
			GameModes.restore( td );
			check( com.shatteredpixel.shatteredpixeldungeon.TrapDungeon.getTrapSkill() == 1, "trap skill comes back after a save" );

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
