import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.Randomizer;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.items.ChallengeCup;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfDecay;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfGrowth;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ForceCube;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.modifierunlocks.ModifierUnlockRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GatewayTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.PressurePlateTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SummoningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndRandomizerDisplay;

import java.lang.reflect.Field;

/**
 * Headless check of what was ported last from Pixel Dungeon Reforged. The seven new challenges for every hero,
 * the trial rooms and their Challenge Cup, the difficulty setting, the Randomizer picks and text, and the
 * Scroll of Decay.
 *
 *   scripts/check.sh runs it, or  java -cp ... ReforgedChallengeTest
 */
public class ReforgedChallengeTest {

	static int failures = 0;
	static int checks = 0;

	static void check( boolean ok, String what ) {
		checks++;
		if (!ok) {
			failures++;
			System.out.println( "FAIL: " + what );
		}
	}

	static void newRun( int challenges, HeroClass cls, long seed ) {
		GamesInProgress.selectedClass = cls;
		Dungeon.seed = seed;
		Dungeon.sandbox = true;
		Dungeon.challenges = 0;
		com.shatteredpixel.shatteredpixeldungeon.SPDSettings.challenges( challenges );
		Dungeon.init();
	}

	public static void main( String[] args ) throws Exception {
		TestBoot.boot();

		int[] masks = { Challenges.HORDE, Challenges.MONSTER_UNKNOWN, Challenges.TRINKET_MADNESS, Challenges.RANDOMIZER,
				Challenges.ADAPTIVE, Challenges.CUBE, Challenges.BARRIER_BREAKER };

		//every new challenge, every hero, the start of a run, and Adaptive's swap done twice
		for (int mask : masks) {
			for (HeroClass cls : HeroClass.values()) {
				try {
					newRun( mask, cls, 91000 + mask );
					check( Dungeon.hero != null, "hero for " + cls + " with challenge " + mask );
					if (mask == Challenges.CUBE) {
						check( Dungeon.hero.belongings.weapon == null, cls + " has no weapon in the Cube challenge" );
						check( Dungeon.hero.belongings.getItem( ForceCube.class ) != null, cls + " has a Force Cube" );
					}
					if (mask == Challenges.TRINKET_MADNESS) {
						int trinkets = Dungeon.hero.belongings.getAllItems( com.shatteredpixel.shatteredpixeldungeon.items.trinkets.Trinket.class ).size();
						check( trinkets == 3, cls + " starts with " + trinkets + " trinkets, not 3" );
					}
					if (mask == Challenges.ADAPTIVE) {
						int before = Dungeon.hero.belongings.backpack.items.size();
						Dungeon.depth = 3;
						Dungeon.hero.belongings.TriggerAdaptive();
						Dungeon.hero.belongings.TriggerAdaptive();
						check( Dungeon.hero.belongings.backpack.items.size() > 0, cls + " keeps an inventory after Adaptive (" + before + " items before)" );
						Dungeon.depth = 1;
					}
				} catch (Throwable t) {
					check( false, "challenge " + mask + " for " + cls + " crashed " + t );
					t.printStackTrace( System.out );
				}
			}
		}

		//locked and unlocked
		Dungeon.sandbox = false;
		check( Challenges.IsChallengeUnlocked( Challenges.NO_FOOD ), "the original challenges are always open" );
		Dungeon.sandbox = true;
		check( Challenges.IsChallengeUnlocked( Challenges.CUBE ), "sandbox opens every challenge" );
		check( !Challenges.HasAnyChallengesToUnlock(), "sandbox has nothing to unlock" );

		//the trial rooms, found by building floor 3 for many seeds with the room forced on
		int cups = 0, plates = 0, summons = 0, gateways = 0, floors = 0;
		Field depthField = ModifierUnlockRoom.class.getDeclaredField( "m_DepthToSpawn" );
		depthField.setAccessible( true );
		for (long seed = 5000; seed < 5000 + Integer.getInteger("trial.seeds", 60); seed++) {
			try {
				newRun( 0, HeroClass.WARRIOR, seed );
				Dungeon.depth = 3;
				Dungeon.branch = 0;
				depthField.setInt( null, 3 );
				Level l = Dungeon.newLevel();
				floors++;
				boolean cup = false;
				for (Heap h : l.heaps.valueList()) {
					for (Item i : h.items) if (i instanceof ChallengeCup) cup = true;
				}
				if (cup) cups++;
				boolean plate = false, summon = false, gate = false;
				for (Trap t : l.traps.valueList()) {
					if (t instanceof PressurePlateTrap) plate = true;
					if (t instanceof SummoningTrap) summon = true;
					if (t instanceof GatewayTrap) gate = true;
				}
				if (plate) plates++;
				if (summon) summons++;
				if (gate) gateways++;
			} catch (Throwable t) {
				check( false, "floor 3 with a trial room for seed " + seed + " crashed " + t );
				t.printStackTrace( System.out );
			}
		}
		System.out.println( "info: " + floors + " floors, " + cups + " cups, " + plates + " plate rooms, " + summons + " summoning rooms, " + gateways + " levitation rooms" );
		check( cups >= floors * 0.8, "trial rooms with a cup in only " + cups + " of " + floors + " floors" );
		check( plates > 0 && summons > 0 && gateways > 0, "all of the plate, summoning and levitation rooms turn up" );
		depthField.setInt( null, -1 );

		//difficulty, health and gold
		Dungeon.depth = 5;
		int[] hp = new int[5];
		for (int d = 1; d <= 4; d++) {
			newRun( 0, HeroClass.WARRIOR, 123 );
			Dungeon.difficulty = d;
			Dungeon.hero.updateHT( false );
			hp[d] = Dungeon.hero.HT;
		}
		check( hp[1] > hp[2] && hp[2] > hp[3] && hp[3] > hp[4], "health by difficulty " + hp[1] + " " + hp[2] + " " + hp[3] + " " + hp[4] );
		check( hp[2] == 20, "medium keeps 20 health, got " + hp[2] );
		long[] gold = new long[5];
		for (int d = 1; d <= 4; d++) {
			Dungeon.difficulty = d;
			long total = 0;
			for (int i = 0; i < 400; i++) total += ((Gold) new Gold().random()).quantity();
			gold[d] = total;
		}
		check( gold[1] > gold[2] && gold[2] > gold[3] && gold[3] > gold[4], "gold by difficulty " + gold[1] + " " + gold[2] + " " + gold[3] + " " + gold[4] );
		Dungeon.difficulty = 2;

		//the Randomizer, picks and text for many seeds
		for (long seed = 1; seed <= 40; seed++) {
			newRun( Challenges.RANDOMIZER, HeroClass.WARRIOR, seed );
			for (int region = 1; region <= 5; region++) {
				Class<?> buffed = Randomizer.getBuffedCreature( region );
				Class<?> nerfed = Randomizer.getNerfedCreature( region );
				check( buffed != null && nerfed != null && buffed != nerfed, "region " + region + " picks for seed " + seed );
				String text = WndRandomizerDisplay.getRandomizerDescriptionForRegion( region );
				check( !text.isEmpty() && !text.toLowerCase().contains( "no text found" ), "randomizer text for region " + region + ": " + text );
			}
		}

		//the Scroll of Decay and its exotic version
		check( ExoticScroll.regToExo.get( ScrollOfDecay.class ) == ScrollOfGrowth.class, "decay turns into growth" );
		check( new ScrollOfDecay().name().length() > 0 && new ScrollOfGrowth().desc().length() > 0, "decay and growth have text" );
		check( com.shatteredpixel.shatteredpixeldungeon.items.Generator.Category.SCROLL.classes.length
				== com.shatteredpixel.shatteredpixeldungeon.items.Generator.Category.SCROLL.defaultProbs.length, "scroll deck has a weight for every scroll" );

		System.out.println( "SUMMARY reforged challenges: " + checks + " checks, " + failures + " failures" );
		System.exit( failures == 0 ? 0 : 1 );
	}
}
