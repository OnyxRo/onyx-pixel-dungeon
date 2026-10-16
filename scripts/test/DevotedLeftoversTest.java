import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.InfusionCD;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.HalfRipper;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Gun;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.VoidTrapLevel;

import java.lang.reflect.Field;

/**
 * Headless check for the pieces ported after Devoted and Reforged were otherwise finished. The Void trap floor,
 * the bounty hunter's item infusion, the Half Ripper escort quest across floors 24 to 20, and the Gun.
 *
 *   java -cp <classes>:<libs.jar>:<test classes> DevotedLeftoversTest
 */
public class DevotedLeftoversTest {

	static int failures = 0;
	static int checks = 0;

	static void check( boolean ok, String what ) {
		checks++;
		if (!ok) {
			failures++;
			System.out.println( "FAIL: " + what );
		}
	}

	static void set( String field, Object value ) throws Exception {
		Field f = HalfRipper.Quest.class.getDeclaredField( field );
		f.setAccessible( true );
		f.set( null, value );
	}

	static int countRippers( Level l ) {
		int n = 0;
		for (Mob m : l.mobs) if (m instanceof HalfRipper) n++;
		return n;
	}

	public static void main( String[] args ) throws Exception {
		TestBoot.boot();
		GamesInProgress.selectedClass = HeroClass.WARRIOR;
		Dungeon.seed = 777001L;
		Dungeon.init();

		//the Void trap, one orb for each hero level and one exit portal
		try {
			Dungeon.depth = 12;
			Dungeon.branch = 0;
			Dungeon.hero.lvl = 4;
			Level v = new VoidTrapLevel();
			v.create();
			int orbs = 0, portals = 0;
			for (Mob m : v.mobs) {
				if (m instanceof VoidTrapLevel.VoidOrb) orbs++;
				if (m instanceof VoidTrapLevel.ExitPortal) portals++;
			}
			check( orbs == 4, "Void trap orbs " + orbs + " for hero level 4" );
			check( portals == 1, "Void trap exit portals " + portals );
			check( Dungeon.hero.lvl == 1, "Void trap drops the hero to level 1" );
		} catch (Throwable t) {
			failures++;
			System.out.println( "FAIL: Void trap threw " + t );
			t.printStackTrace( System.out );
		}

		//infusion, an infused item that leaves the pack is owed back, and the flag survives saving
		try {
			Dungeon.hero.lvl = 10;
			Item scroll = new ScrollOfIdentify();
			scroll.isInfused = true;
			Dungeon.hero.belongings.backpack.items.add( scroll );
			scroll.detach( Dungeon.hero.belongings.backpack );
			InfusionCD cd = Dungeon.hero.buff( InfusionCD.class );
			check( cd != null, "infused item leaving the pack starts the cooldown" );
			check( cd != null && cd.ToGiveBack == scroll, "cooldown remembers the item" );
			check( scroll.duplicate().isInfused, "infusion survives a save" );
			check( !new ScrollOfIdentify().isInfused, "plain items are not infused" );
		} catch (Throwable t) {
			failures++;
			System.out.println( "FAIL: infusion threw " + t );
			t.printStackTrace( System.out );
		}

		//the Gun, loaded by default, unloaded after the flag is cleared, saved and loaded
		try {
			Gun g = new Gun();
			check( g.isLoaded(), "Gun starts loaded" );
			check( "1/1".equals( g.status() ), "Gun status when loaded" );
			g.setLoaded( false );
			check( "0/1".equals( g.status() ), "Gun status when empty" );
			check( !((Gun) g.duplicate()).isLoaded(), "Gun empty state survives a save" );
			check( g.max() >= g.min() && g.min() > 0, "Gun damage range" );
		} catch (Throwable t) {
			failures++;
			System.out.println( "FAIL: Gun threw " + t );
			t.printStackTrace( System.out );
		}

		//the Half Ripper, floor 24 spawns it once, the quest follows the hero up and pays out on floor 20
		try {
			HalfRipper.Quest.reset();
			Dungeon.branch = 0;

			Dungeon.depth = 24;
			Level l24 = Dungeon.newLevel();
			HalfRipper.Quest.spawn( l24 );
			check( countRippers( l24 ) == 1, "floor 24 has one Half Ripper, found " + countRippers( l24 ) );
			HalfRipper.Quest.spawn( l24 );
			check( countRippers( l24 ) == 1, "arriving again does not add a second Half Ripper" );

			//save and load of the quest state
			com.watabou.utils.Bundle b = new com.watabou.utils.Bundle();
			set( "given", true );
			set( "started", true );
			set( "depth", 23 );
			HalfRipper.Quest.storeInBundle( b );
			HalfRipper.Quest.reset();
			check( !HalfRipper.Quest.started(), "reset clears the quest" );
			HalfRipper.Quest.restoreFromBundle( b );
			check( HalfRipper.Quest.started() && HalfRipper.Quest.depth() == 23, "quest state survives a save" );

			Dungeon.depth = 23;
			Level l23 = Dungeon.newLevel();
			HalfRipper.Quest.spawn( l23 );
			check( countRippers( l23 ) == 1, "floor 23 gets the Half Ripper at the exit" );

			//finished, the reward ripper waits on floor 20
			HalfRipper.Quest.complete();
			check( HalfRipper.Quest.completed() && HalfRipper.Quest.depth() == 20, "complete() sends the quest to floor 20" );
			Dungeon.depth = 20;
			Level l20 = Dungeon.newLevel();
			HalfRipper.Quest.spawn( l20 );
			check( countRippers( l20 ) == 1, "floor 20 has the reward Half Ripper" );

			//failing it brings back an enemy ripper where the quest ended
			HalfRipper.Quest.reset();
			set( "spawned", true );
			set( "given", true );
			set( "started", true );
			set( "depth", 22 );
			HalfRipper.Quest.abandon();
			check( HalfRipper.Quest.failed(), "abandon() fails the quest" );
			Dungeon.depth = 22;
			Level l22 = Dungeon.newLevel();
			HalfRipper.Quest.spawn( l22 );
			boolean ripperDemon = false;
			for (Mob m : l22.mobs) if (m instanceof com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RipperDemon) ripperDemon = true;
			check( ripperDemon, "the abandoned ripper returns as an enemy" );
			HalfRipper.Quest.reset();
		} catch (Throwable t) {
			failures++;
			System.out.println( "FAIL: Half Ripper threw " + t );
			t.printStackTrace( System.out );
		}

		System.out.println( "SUMMARY devoted leftovers checks: " + checks + ", failures: " + failures );
		if (failures > 0) System.exit( 1 );
	}
}
