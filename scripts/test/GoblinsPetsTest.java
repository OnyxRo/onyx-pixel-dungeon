import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.Bunny;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.Fairy;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.FireKlik;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.FrostKlik;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.GoblinsPets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.MetalKlik;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.PET;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.PetOwner;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.PetVelocirooster;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.PhaseKlik;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.PoisonKlik;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.SpiderKlik;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.SugarplumFairy;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.pets.KlikSoulorb;
import com.shatteredpixel.shatteredpixeldungeon.items.pets.PetEgg;
import com.shatteredpixel.shatteredpixeldungeon.items.pets.PetEggItem;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;

//Goblins PD pet eggs and the pet owner numbers, headless.
//scripts/test-recompile.sh GoblinsPetsTest
public class GoblinsPetsTest {

	static int failures = 0, checks = 0;

	static void check( boolean ok, String what ) {
		checks++;
		if (!ok) {
			failures++;
			System.out.println( "FAIL: " + what );
		}
	}

	static boolean hasText( String s ) {
		return s != null && !s.isEmpty() && !s.contains( "NO_TEXT" ) && !s.startsWith( "!!!" );
	}

	static void newRun( long seed, int depth ) {
		GamesInProgress.selectedClass = HeroClass.WARRIOR;
		Dungeon.seed = seed;
		Dungeon.sandbox = true;
		Dungeon.init();
		Dungeon.depth = depth;
		Level l = Dungeon.newLevel();
		Dungeon.switchLevel( l, l.entrance() );
	}

	static PET hatchFrom( PetEggItem egg, int moves, int burns, int freezes, int poisons, int lits, int summons ) {
		egg.moves = moves; egg.burns = burns; egg.freezes = freezes; egg.poisons = poisons; egg.lits = lits; egg.summons = summons;
		return egg.hatchling();
	}

	public static void main( String[] args ) throws Exception {
		TestBoot.boot();
		try {
			//text
			for (PetEggItem egg : new PetEggItem[]{ new KlikSoulorb(), new PetEgg() }) {
				String n = egg.getClass().getSimpleName();
				check( hasText( egg.name() ) && hasText( egg.desc() ), n + " name and description" );
				check( hasText( egg.actionName( PetEggItem.AC_BREAK, null ) ) && hasText( egg.actionName( PetEggItem.AC_SHAKE, null ) ), n + " action names" );
				for (String k : new String[]{ "yolk", "kicks", "likes_pack", "only_one", "not_ready", "hatch", "slosh" }) {
					check( hasText( Messages.get( egg, k ) ), n + " text " + k );
				}
				check( Catalog.MISC_CONSUMABLES.items().contains( egg.getClass() ), n + " is in the catalog" );
			}
			check( hasText( Messages.get( KlikSoulorb.class, "slithers" ) ) && hasText( Messages.get( PetEgg.class, "scratch" ) ), "shake text" );

			//the mod's hatching rules
			KlikSoulorb orb = new KlikSoulorb();
			check( hatchFrom( orb, 0, 0, 0, 0, 0, 0 ) == null, "an untouched soulorb is only goop" );
			check( hatchFrom( orb, 199, 0, 0, 0, 0, 0 ) == null, "199 turns is not enough" );
			check( hatchFrom( orb, 200, 0, 0, 0, 0, 0 ) instanceof SpiderKlik, "200 turns make a Spider Klik" );
			check( hatchFrom( orb, 1000, 0, 0, 0, 0, 0 ) instanceof MetalKlik, "1000 turns make a Metal Klik" );
			check( hatchFrom( orb, 1000, 15, 0, 0, 0, 0 ) instanceof FireKlik, "15 fires make a Fire Klik" );
			check( hatchFrom( orb, 0, 15, 0, 0, 5, 0 ) instanceof PhaseKlik, "5 lightning strikes make a Phase Klik" );
			check( hatchFrom( orb, 0, 15, 0, 5, 5, 0 ) instanceof PoisonKlik, "5 poisonings make a Poison Klik" );
			check( hatchFrom( orb, 0, 15, 5, 5, 5, 0 ) instanceof FrostKlik, "5 frosts make a Frost Klik" );
			PetEgg egg = new PetEgg();
			check( hatchFrom( egg, 9, 0, 0, 0, 0, 0 ) == null, "9 turns is not enough for the egg" );
			check( hatchFrom( egg, 10, 0, 0, 0, 0, 0 ) instanceof Bunny, "10 turns make a Bunny" );
			check( hatchFrom( egg, 10, 1, 0, 0, 0, 0 ) instanceof PetVelocirooster, "a fire makes a Velocirooster" );
			PET fairy = hatchFrom( egg, 10, 1, 0, 0, 0, 1 );
			check( fairy instanceof Fairy || fairy instanceof SugarplumFairy, "a summoning trap makes a Fairy" );

			//saving
			orb.moves = 123; orb.burns = 4;
			Bundle b = new Bundle();
			b.put( "e", orb );
			KlikSoulorb back = (KlikSoulorb) b.get( "e" );
			check( back.moves == 123 && back.burns == 4, "soulorb counts save and load" );

			//in a run
			newRun( 11, 3 );
			Hero hero = Dungeon.hero;
			KlikSoulorb carried = new KlikSoulorb();
			check( carried.collect( hero.belongings.backpack ), "the hero picks up a soulorb" );
			PetOwner owner = hero.buff( PetOwner.class );
			check( owner != null, "carrying an egg makes the hero a pet owner" );
			for (int i = 0; i < 200 && owner != null; i++) owner.act();
			check( carried.moves == 200, "each turn in the pack counts, " + carried.moves );
			check( carried.hatchling() instanceof SpiderKlik, "after 200 turns it would hatch a Spider Klik" );

			boolean hatched = false;
			try {
				hatched = carried.hatch( hero, carried.hatchling() );
			} catch (NullPointerException e) {
				System.out.println( "HEADLESS (not counted) hatch " + e );
				hatched = PET.currentPet() != null;
			}
			check( hatched && PET.currentPet() instanceof SpiderKlik, "the Spider Klik hatches next to the hero" );
			check( hero.belongings.getItem( KlikSoulorb.class ) == null, "the orb is used up" );
			check( owner.haspet && owner.petType == 1 && owner.petLevel == 1, "the owner remembers the pet, type " + owner.petType + " level " + owner.petLevel );

			KlikSoulorb second = new KlikSoulorb();
			second.collect( hero.belongings.backpack );
			second.moves = 1000;
			boolean twice = true;
			try {
				twice = second.hatch( hero, second.hatchling() );
			} catch (NullPointerException e) {
				System.out.println( "HEADLESS (not counted) second hatch " + e );
			}
			check( !twice && hero.belongings.getItem( KlikSoulorb.class ) == second, "a second pet waits while the first lives" );

			try {
				PET.currentPet().die( null );
			} catch (NullPointerException e) {
				System.out.println( "HEADLESS (not counted) pet death " + e );
			}
			check( !owner.haspet && owner.petCount == 1, "the owner is free again after the pet dies" );

			b = new Bundle();
			b.put( "o", owner );
			PetOwner ob = (PetOwner) b.get( "o" );
			check( ob.petCount == 1 && ob.petType == 1, "pet owner numbers save and load" );

			//eggs lying on the floor
			Heap heap = Dungeon.level.drop( new PetEgg(), hero.pos );
			PetEgg lying = (PetEgg) heap.peek();
			try { heap.burn(); } catch (NullPointerException e) { System.out.println( "HEADLESS (not counted) burn " + e ); }
			GoblinsPets.heapAt( hero.pos, GoblinsPets.SUMMON );
			GoblinsPets.heapAt( hero.pos, GoblinsPets.LIT );
			GoblinsPets.heapAt( hero.pos, GoblinsPets.POISON );
			try { heap.freeze(); } catch (NullPointerException e) { System.out.println( "HEADLESS (not counted) freeze " + e ); }
			check( lying.burns == 1 && lying.summons == 1 && lying.lits == 1 && lying.poisons == 1 && lying.freezes == 1, "fire, frost, poison, lightning and summoning count on a lying egg" );

			//how often a soulorb turns up
			int orbs = 0, tries = 0;
			for (long seed = 1; seed <= 40; seed++) {
				Dungeon.seed = seed;
				for (int d = 2; d <= 24; d++) {
					if (d % 5 == 0) continue;
					Dungeon.depth = d;
					tries++;
					if (GoblinsPets.rollSoulorb( Dungeon.level )) orbs++;
				}
			}
			System.out.println( "soulorbs on " + orbs + " of " + tries + " floors" );
			check( orbs > 0 && orbs < tries / 8, "soulorbs are rare but do turn up" );
			Dungeon.chosenMods.add( "Devoted PD" );
			check( !GoblinsPets.rollSoulorb( Dungeon.level ), "no soulorbs when only some mods are on" );
			Dungeon.chosenMods.clear();

			//floors still build with the hooks in
			for (long seed = 1; seed <= 4; seed++) {
				for (int d = 2; d <= 12; d += 3) {
					newRun( seed, d );
					check( Dungeon.level != null, "floor " + d + " seed " + seed + " builds" );
				}
			}
		} catch (Throwable t) {
			failures++;
			System.out.println( "FAIL: exception " + t );
			t.printStackTrace( System.out );
		}
		System.out.println( "GoblinsPetsTest " + (checks - failures) + "/" + checks + " passed" );
		System.exit( failures == 0 ? 0 : 1 );
	}
}
