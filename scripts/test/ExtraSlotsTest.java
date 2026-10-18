import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.LeatherArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.*;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.*;
import com.watabou.utils.Bundle;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Headless check for Ryuse's extra equipment slots. Saving and loading (and an old save with no extra keys),
 * rings and artifacts using the free extra slots, extra armor adding to armor and being taken off again.
 */
public class ExtraSlotsTest {

	static int failures = 0;
	static void fail( String what ) { failures++; System.out.println( "FAIL: " + what ); }

	public static void main( String[] args ) throws Exception {
		TestBoot.boot();
		GamesInProgress.selectedClass = HeroClass.WARRIOR;
		Dungeon.seed = 424242L;
		Dungeon.sandbox = true;
		Dungeon.init();
		MobArenaTest.level( 3 );
		Hero hero = Dungeon.hero;
		Belongings b = hero.belongings;

		//rings, the ring slot, the misc slot, then the extra slots
		Ring[] rings = { new RingOfMight(), new RingOfHaste(), new RingOfEvasion(), new RingOfFuror(), new RingOfWealth() };
		for (Ring r : rings) { r.identify(); r.collect(); }
		for (Ring r : rings) { if (!r.doEquip( hero )) fail( r.name() + " did not equip" ); }
		int worn = (b.ring != null ? 1 : 0) + (b.misc != null ? 1 : 0);
		for (Ring r : b.extraRings) if (r != null) worn++;
		if (worn != 5) fail( "expected 5 rings worn, got " + worn );
		if (!rings[4].isEquipped( hero )) fail( "fifth ring should count as equipped" );

		//the extra ring unequips and clears its slot
		Ring extra = b.extraRings[0];
		if (extra == null) fail( "no ring in the first extra slot" );
		else {
			extra.doUnequip( hero, true, false );
			if (b.extraRings[0] != null) fail( "extra ring slot not cleared" );
			if (extra.isEquipped( hero )) fail( "unequipped ring still equipped" );
		}

		//artifacts, two slots then extras. Same kind twice is refused
		Artifact a1 = new CloakOfShadows(), a2 = new HornOfPlenty(), a3 = new TalismanOfForesight();
		for (Artifact a : new Artifact[]{a1, a2, a3}) { a.identify(); a.collect(); }
		b.artifact = null; b.misc = null; b.ring = null;
		a1.doEquip( hero ); a2.doEquip( hero ); a3.doEquip( hero );
		if (!a3.isEquipped( hero )) fail( "third artifact not equipped" );
		if (b.extraArtifacts[0] != a3) fail( "third artifact should sit in the first extra artifact slot" );

		//extra armor adds to armor and evasion hooks run, take it off again
		LeatherArmor main = new LeatherArmor(); main.identify(); b.armor = main;
		PlateArmor plate = new PlateArmor(); plate.identify(); plate.collect();
		hero.STR = 30;
		long base = 0, with = 0;
		for (int i = 0; i < 400; i++) base += hero.drRoll();
		b.extraArmors[0] = plate;
		if (!plate.isEquipped( hero )) fail( "extra armor not equipped" );
		for (int i = 0; i < 400; i++) with += hero.drRoll();
		if (with <= base) fail( "extra armor did not add to armor (" + base + " vs " + with + ")" );
		hero.defenseProc( null, 5 );

		//save and load, then an old save with no extra keys
		Bundle bundle = new Bundle();
		b.storeInBundle( bundle );
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Bundle.write( bundle, out );
		Bundle again = Bundle.read( new ByteArrayInputStream( out.toByteArray() ) );
		Belongings loaded = new Belongings( hero );
		loaded.restoreFromBundle( again );
		if (loaded.extraArmors[0] == null || !(loaded.extraArmors[0] instanceof PlateArmor)) fail( "extra armor lost on reload" );
		if (loaded.extraArtifacts[0] == null) fail( "extra artifact lost on reload" );
		int n = 0;
		for (Item i : loaded) n++;
		if (n < 5) fail( "iterator misses equipped items (" + n + ")" );

		Bundle old = new Bundle();
		Belongings fresh = new Belongings( hero );
		fresh.armor = new LeatherArmor();
		fresh.storeInBundle( old );
		Belongings oldLoad = new Belongings( hero );
		oldLoad.extraArmors[2] = new PlateArmor();
		oldLoad.restoreFromBundle( old );
		if (oldLoad.extraArmors[2] != null) fail( "old save should leave extra slots empty" );
		if (oldLoad.armor == null) fail( "old save lost main armor" );

		//armor unequip from an extra slot leaves main armor alone
		plate.doUnequip( hero, true, false );
		if (b.armor != main) fail( "unequipping extra armor removed the main armor" );
		if (b.extraArmors[0] != null) fail( "extra armor slot not cleared" );

		System.out.println( "SUMMARY extraslots failures=" + failures );
		if (failures > 0) System.exit( 1 );
	}
}
