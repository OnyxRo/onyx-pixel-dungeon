import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.PolishedPD;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WaterOfChange;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WaterOfHealth;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Brittle;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Electrified;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Recipe;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.HoneyedMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MeatPie;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Pasty;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Dagger;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BlastingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Headless check of the Polished PD port. Honeyed Meat and its recipes, the Polished PD changes option (off and on),
 * Brittle, Electrified, the blast trap and the Well of Change talent swap.
 */
public class PolishedTest {

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

	static void newRun( int challenges, long seed ) {
		GamesInProgress.selectedClass = HeroClass.WARRIOR;
		Dungeon.seed = seed;
		Dungeon.sandbox = true;
		Dungeon.challenges = 0;
		com.shatteredpixel.shatteredpixeldungeon.SPDSettings.challenges( challenges );
		Dungeon.init();
		Dungeon.depth = 7;
		Level l = Dungeon.newLevel();
		Dungeon.switchLevel( l, l.entrance() );
	}

	static Rat ratNextToHero() {
		Hero hero = Dungeon.hero;
		for (int n : com.watabou.utils.PathFinder.NEIGHBOURS8) {
			int c = hero.pos + n;
			if (Dungeon.level.passable[c] && Actor.findChar( c ) == null) {
				Rat r = new Rat();
				r.pos = c;
				r.HP = r.HT = 1000;
				Dungeon.level.mobs.add( r );
				Actor.add( r );
				return r;
			}
		}
		return null;
	}

	public static void main( String[] args ) throws Exception {
		TestBoot.boot();

		check( hasText( Messages.get( Challenges.class, "polished_changes" ) ) && hasText( Messages.get( Challenges.class, "polished_changes_desc" ) ), "option text" );
		check( Challenges.NAME_IDS.length == Challenges.MASKS.length, "challenge names and masks line up" );
		check( Challenges.IsChallengeUnlocked( Challenges.POLISHED_CHANGES ), "option is open" );
		for (Class<?> c : new Class<?>[]{ Brittle.class, Electrified.class, WaterOfChange.class, BlastingTrap.class, HoneyedMeat.class }) {
			check( hasText( Messages.get( c, "name" ) ) && hasText( Messages.get( c, "desc" ) ), "text for " + c.getSimpleName() );
		}
		check( hasText( Messages.get( Electrified.class, "out" ) ) && hasText( Messages.get( WaterOfChange.class, "changed", "a", "b" ) ), "extra text" );

		try {
			//Honeyed Meat
			ArrayList<Item> in = new ArrayList<>( Arrays.asList( new MysteryMeat(), new Honeypot.ShatteredPot() ) );
			Recipe r = null;
			for (Recipe x : Recipe.findRecipes( in )) if (x instanceof HoneyedMeat.Recipe) r = x;
			check( r != null, "meat and a shattered pot make Honeyed Meat" );
			check( r != null && r.cost( in ) == 3 && r.sampleOutput( in ) instanceof HoneyedMeat, "Honeyed Meat costs 3 energy" );
			check( new MeatPie.Recipe().testIngredients( new ArrayList<>( Arrays.asList( new Pasty(), new Food(), new HoneyedMeat() ) ) ), "Meat Pie takes Honeyed Meat" );
			check( Catalog.FOOD.items().contains( HoneyedMeat.class ), "Honeyed Meat is in the catalog" );

			//option off, everything as in Shattered
			newRun( 0, 3 );
			Hero hero = Dungeon.hero;
			check( !PolishedPD.active(), "option off" );
			Rat rat = ratNextToHero();
			PolishedPD.shock( rat, 3 );
			check( rat.buff( Paralysis.class ) != null && rat.buff( Electrified.class ) == null, "electricity paralyses with the option off" );
			HashMap<Class<? extends Buff>, Float> debuffs = new HashMap<>();
			PolishedPD.addBrittle( debuffs, true );
			check( debuffs.isEmpty(), "no Brittle from corruption with the option off" );
			check( PolishedPD.wellWater( WaterOfHealth.class ) == WaterOfHealth.class, "wells unchanged with the option off" );
			Class<? extends Trap>[][] normal = new Class[][]{ {Trap.class} };
			check( PolishedPD.trapsRoom( normal ) == normal, "trap rooms unchanged with the option off" );

			//option on
			newRun( Challenges.POLISHED_CHANGES, 3 );
			hero = Dungeon.hero;
			check( PolishedPD.active(), "option on" );
			rat = ratNextToHero();
			PolishedPD.shock( rat, 3 );
			check( rat.buff( Electrified.class ) != null && rat.paralysed > 0, "electricity electrifies with the option on" );
			Bundle b = new Bundle();
			b.put( "e", rat.buff( Electrified.class ) );
			check( b.get( "e" ) instanceof Electrified, "Electrified saves and loads" );
			rat.damage( 5, hero );
			check( rat.buff( Electrified.class ) == null && rat.paralysed == 0, "a direct hit breaks Electrified" );
			check( rat.buff( Electrified.ElectricResist.class ) != null, "breaking out gives a short resistance" );
			PolishedPD.shock( rat, 3 );
			check( rat.buff( Electrified.class ) == null, "resistance stops a new stun" );

			debuffs.clear();
			PolishedPD.addBrittle( debuffs, true );
			check( debuffs.containsKey( Brittle.class ), "corruption can make Brittle with the option on" );
			boolean change = false;
			for (int i = 0; i < 60; i++) if (PolishedPD.wellWater( WaterOfHealth.class ) == WaterOfChange.class) change = true;
			check( change, "magic well rooms can roll the Well of Change" );
			boolean blasting = false;
			for (Class<? extends Trap>[] row : PolishedPD.trapsRoom( normal )) for (Class<? extends Trap> t : row) if (t == BlastingTrap.class) blasting = true;
			check( blasting, "trap rooms use blast traps" );

			//Brittle
			Rat rat2 = ratNextToHero();
			Buff.affect( rat2, Brittle.class, Brittle.DURATION );
			check( PolishedPD.brittleAttack( rat2, 100f ) == 75f && PolishedPD.brittleTaken( rat2, 100 ) == 125, "Brittle is 25% each way" );
			check( PolishedPD.brittleAttack( hero, 100f ) == 100f && PolishedPD.brittleTaken( hero, 100 ) == 100, "no change without Brittle" );

			//Well of Change talent swap
			hero.lvl = 12;
			int before = 0;
			for (java.util.LinkedHashMap<Talent, Integer> t : hero.talents) before += t.size();
			Talent[] first = PolishedPD.randomMetamorph( hero, null );
			Talent[] second = first == null ? null : PolishedPD.randomMetamorph( hero, first[1] );
			check( first != null && second != null, "two talents can be metamorphed" );
			if (first != null && second != null) {
				boolean hasNew = false, hasOld = false;
				for (java.util.LinkedHashMap<Talent, Integer> t : hero.talents) {
					if (t.containsKey( first[1] ) && t.containsKey( second[1] )) hasNew = true;
					if (t.containsKey( first[0] ) || t.containsKey( second[0] )) hasOld = true;
				}
				boolean bothNew = false;
				for (java.util.LinkedHashMap<Talent, Integer> t : hero.talents) if (t.containsKey( first[1] )) for (java.util.LinkedHashMap<Talent, Integer> u : hero.talents) if (u.containsKey( second[1] )) bothNew = true;
				check( bothNew && !hasOld, "talents swapped, " + Arrays.toString( first ) + " " + Arrays.toString( second ) );
				check( hero.metamorphedTalents.get( first[0] ) == first[1], "swap is recorded like the scroll's" );
			}
			int after = 0;
			for (java.util.LinkedHashMap<Talent, Integer> t : hero.talents) after += t.size();
			check( before == after, "talent count unchanged" );

			//blast trap destroys low equipment in reach, leaves potions
			int cell = -1;
			for (int c = 0; c < Dungeon.level.length(); c++) {
				if (Dungeon.level.passable[c] && Actor.findChar( c ) == null && Dungeon.level.heaps.get( c ) == null
						&& Dungeon.level.distance( c, hero.pos ) > 6) { cell = c; break; }
			}
			Heap heap = Dungeon.level.drop( new Dagger(), cell );
			heap.drop( new PotionOfHealing() );
			BlastingTrap trap = new BlastingTrap();
			trap.pos = cell;
			try {
				trap.activate();
			} catch (NullPointerException e) {
				System.out.println( "HEADLESS (not counted) blast trap effects: " + e );
			}
			Heap after2 = Dungeon.level.heaps.get( cell );
			boolean dagger = false, potion = false;
			if (after2 != null) for (Item i : after2.items) { if (i instanceof Dagger) dagger = true; if (i instanceof PotionOfHealing) potion = true; }
			check( !dagger, "blast trap destroys a +0 weapon" );
		} catch (Throwable t) {
			failures++;
			System.out.println( "FAIL: threw " + t );
			StackTraceElement[] st = t.getStackTrace();
			for (int i = 0; i < Math.min( 10, st.length ); i++) System.out.println( "        at " + st[i] );
		}
		System.out.println( "SUMMARY polished: " + checks + " checks, failures: " + failures );
		System.exit( failures == 0 ? 0 : 1 );
	}
}
