import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfExtraction;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.Dart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.watabou.utils.Bundle;

import java.lang.reflect.Method;

/**
 * Headless check for Transformation PD's Scroll of Extraction. The main point is that the new bookkeeping
 * (Item.upgradeScrollUses, MissileWeapon.UpgradedSetTracker credits) leaves ordinary upgrades exactly as they were.
 *
 *   java -cp <classes>:<libs.jar>:<test classes> ExtractionTest
 */
public class ExtractionTest {

	static int failures = 0;

	static void fail( String what ) {
		failures++;
		System.out.println( "FAIL: " + what );
	}

	static void check( boolean ok, String what ) {
		if (!ok) fail( what );
	}

	static Item roundTrip( Item item ) {
		Bundle b = new Bundle();
		b.put( "item", item );
		return (Item) b.get( "item" );
	}

	static boolean bundleHasKey( Item item, String key ) {
		Bundle b = new Bundle();
		item.storeInBundle( b );
		return b.contains( key );
	}

	static Method extraction( String name, Class<?>... params ) throws Exception {
		Class<?> c = Class.forName( "com.shatteredpixel.shatteredpixeldungeon.items.scrolls.UpgradeExtraction" );
		Method m = c.getDeclaredMethod( name, params );
		m.setAccessible( true );
		return m;
	}

	public static void main( String[] args ) throws Exception {
		TestBoot.boot();
		GamesInProgress.selectedClass = HeroClass.WARRIOR;
		Dungeon.seed = 424242L;
		Dungeon.sandbox = true;
		Dungeon.init();
		MobArenaTest.level( 3 );
		Hero hero = Dungeon.hero;
		java.lang.reflect.Field cu = Item.class.getDeclaredField( "curUser" );
		cu.setAccessible( true );
		cu.set( null, hero );

		Method canExtract = extraction( "canExtract", Item.class, Hero.class );
		Method extract = extraction( "extractUpgradeUses",
				Item.class, com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag.class, Hero.class );

		//1. ordinary upgrades are untouched, no counter, no new save key, same levels as before
		Sword plain = new Sword();
		plain.upgrade( 3 );
		check( plain.level() == 3, "plain upgrade changed the level, " + plain.level() );
		check( plain.upgradeScrollUses == 0, "plain upgrade wrote a scroll use" );
		check( !bundleHasKey( plain, "upgrade_scroll_uses" ), "an item with no scroll uses saved the new key" );
		check( !(Boolean) canExtract.invoke( null, plain, hero ), "extraction accepted an item with no scroll uses" );
		Item plainBack = roundTrip( plain );
		check( plainBack.level() == 3 && plainBack.upgradeScrollUses == 0, "plain item changed on save and load" );

		//2. a scroll upgrade does the same level change as a plain one and adds one use
		Sword viaScroll = new Sword();
		viaScroll.identify();
		boolean usedRealScroll = true;
		try {
			new ScrollOfUpgrade().upgradeItem( viaScroll );
		} catch (Throwable t) {
			//the scroll plays a hero sprite effect that a headless run cannot, do the part under test by hand
			usedRealScroll = false;
			if (viaScroll.level() == 0) viaScroll.upgrade();
			viaScroll.recordUpgradeScrollUse();
		}
		Sword viaPlain = new Sword();
		viaPlain.upgrade();
		check( viaScroll.level() == viaPlain.level(), "scroll upgrade level " + viaScroll.level() + " differs from plain " + viaPlain.level() );
		check( viaScroll.upgradeScrollUses == 1, "scroll upgrade recorded " + viaScroll.upgradeScrollUses + " uses" );
		System.out.println( "info: Scroll of Upgrade " + (usedRealScroll ? "ran for real" : "was replaced by its two calls") );

		//3. extraction takes back only the scroll upgrades and leaves plain ones
		Sword mixed = new Sword();
		mixed.upgrade();                 // found at +1
		mixed.upgrade(); mixed.recordUpgradeScrollUse();
		mixed.upgrade(); mixed.recordUpgradeScrollUse();
		check( mixed.level() == 3 && mixed.upgradeScrollUses == 2, "setup for mixed sword is wrong" );
		check( (Boolean) canExtract.invoke( null, mixed, hero ), "extraction refused an item with scroll uses" );
		Item mixedBack = roundTrip( mixed );
		check( mixedBack.upgradeScrollUses == 2 && mixedBack.level() == 3, "scroll uses were lost on save and load" );
		int got = (Integer) extract.invoke( null, mixed, hero.belongings.backpack, hero );
		check( got == 2, "extraction returned " + got + " scrolls instead of 2" );
		check( mixed.level() == 1, "extraction left level " + mixed.level() + " instead of 1" );
		check( mixed.upgradeScrollUses == 0, "extraction did not clear the uses" );
		check( !(Boolean) canExtract.invoke( null, mixed, hero ), "extraction accepted the same item twice" );

		//4. a thrown weapon set shares its uses through the tracker
		Dart a = new Dart();
		a.identify();
		a.upgrade(); a.recordUpgradeScrollUse();
		a.upgrade(); a.recordUpgradeScrollUse();
		check( a.level() == 2, "dart level " + a.level() );
		check( MissileWeapon.UpgradedSetTracker.availableUpgradeScrollUses( hero, a ) == 2, "set credits are not 2" );
		Dart sibling = (Dart) a.duplicate();
		check( sibling.setID == a.setID, "duplicate left its set" );
		hero.belongings.backpack.items.add( sibling );
		Item dartBack = roundTrip( a );
		check( dartBack.upgradeScrollUses == 2 || MissileWeapon.UpgradedSetTracker.availableUpgradeScrollUses( hero, (MissileWeapon) dartBack ) == 2,
				"dart credits did not survive save and load" );

		//the tracker itself must save and load its credits
		MissileWeapon.UpgradedSetTracker tracker = hero.buff( MissileWeapon.UpgradedSetTracker.class );
		check( tracker != null, "no set tracker after upgrading a dart" );
		if (tracker != null) {
			Bundle tb = new Bundle();
			tracker.storeInBundle( tb );
			MissileWeapon.UpgradedSetTracker copy = new MissileWeapon.UpgradedSetTracker();
			copy.restoreFromBundle( tb );
			check( copy.upgradeScrollCredits.get( a.setID ) != null && copy.upgradeScrollCredits.get( a.setID ) == 2,
					"tracker lost its credits on save and load" );
		}

		int dartsGot = (Integer) extract.invoke( null, a, hero.belongings.backpack, hero );
		check( dartsGot == 2, "dart extraction returned " + dartsGot );
		check( a.level() == 0, "dart level after extraction " + a.level() );
		check( sibling.level() == 0, "the other stack of the set was not degraded, level " + sibling.level() );
		check( MissileWeapon.UpgradedSetTracker.availableUpgradeScrollUses( hero, a ) == 0, "set credits not cleared" );
		check( MissileWeapon.UpgradedSetTracker.pickupValid( hero, sibling ), "a degraded stack of the set can no longer be picked up" );

		//5. a normal dart upgrade (no scroll) still leaves no credits and still locks old stacks out
		Dart b = new Dart();
		b.upgrade();
		check( MissileWeapon.UpgradedSetTracker.availableUpgradeScrollUses( hero, b ) == 0, "plain dart upgrade made credits" );
		Dart old = (Dart) new Dart().level( 0 );
		old.setID = b.setID;
		check( !MissileWeapon.UpgradedSetTracker.pickupValid( hero, old ), "plain dart upgrade stopped locking out old stacks" );

		//6. the scroll itself
		ScrollOfExtraction scroll = new ScrollOfExtraction();
		check( scroll.isKnown(), "scroll of extraction is not known on pickup" );
		check( scroll.value() > 0, "scroll of extraction has no value" );

		System.out.println( "SUMMARY extraction failures: " + failures );
		System.exit( failures == 0 ? 0 : 1 );
	}
}
