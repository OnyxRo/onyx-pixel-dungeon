import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Perks;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.treasurebags.GambleBag;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * Headless check for Experienced PD's perks. Covers the parts that do not need a window. Strings, the order in
 * which perks are earned, saving and loading, Steel Skin, Protein Infusion and More Bag. The effects that need
 * the screen (the blast of Iron Reflection, Restored Nature, Ratforcements) are played in the sandbox.
 *
 *   java -cp <classes>:<libs.jar>:<test classes> PerksTest
 */
public class PerksTest {

	static int failures = 0;

	static void fail( String what ) {
		failures++;
		System.out.println( "FAIL: " + what );
	}

	static boolean missing( String s ) {
		return s == null || s.isEmpty() || s.contains( "NO TEXT FOUND" ) || s.contains( "!!!" );
	}

	static int bagItems( GambleBag bag ) throws Exception {
		java.lang.reflect.Method m = GambleBag.class.getDeclaredMethod( "items" );
		m.setAccessible( true );
		return ((java.util.List<?>) m.invoke( bag )).size();
	}

	public static void main( String[] args ) throws Exception {
		TestBoot.boot();
		GamesInProgress.selectedClass = HeroClass.WARRIOR;
		Dungeon.seed = 515151L;
		Dungeon.sandbox = true;
		Dungeon.init();
		MobArenaTest.level( 3 );
		Hero hero = Dungeon.hero;

		//every perk has a name and a description
		for (Perks.Perk p : Perks.Perk.values()) {
			if (missing( p.toString() )) fail( p.name() + " has no name text" );
			if (missing( p.desc() )) fail( p.name() + " has no description text" );
		}
		if (missing( Messages.get( Perks.PerkHoldFast.class, "name" ) )) fail( "Hold Fast buff has no name" );
		if (missing( Messages.get( Perks.PerkHoldFast.class, "desc", 1, 2 ) )) fail( "Hold Fast buff has no description" );
		if (missing( Messages.get( Perks.DirectiveMovingTracker.class, "name" ) )) fail( "Instant Steps buff has no name" );
		if (missing( Messages.get( Perks.class, "perk_obtain", "x" ) )) fail( "perk_obtain text is missing" );

		//perks arrive at growing level gaps and never repeat
		hero.perks.clear();
		Set<Perks.Perk> seen = new HashSet<>();
		int lastLevel = 0;
		for (int i = 0; i < Perks.Perk.values().length; i++) {
			hero.lvl = Perks.nextPerkLevel();
			if (hero.lvl <= lastLevel) fail( "next perk level did not grow, " + hero.lvl );
			lastLevel = hero.lvl;
			int before = hero.perks.size();
			Perks.earnPerk( hero );
			if (hero.perks.size() != before + 1) fail( "no perk was earned at level " + hero.lvl );
			seen.addAll( hero.perks );
		}
		if (seen.size() != Perks.Perk.values().length) fail( "perks repeated, only " + seen.size() + " of " + Perks.Perk.values().length );
		hero.lvl = Perks.nextPerkLevel();
		int full = hero.perks.size();
		Perks.earnPerk( hero );
		if (hero.perks.size() != full) fail( "a perk was earned with none left" );

		//save and load
		Bundle b = new Bundle();
		Perks.storeInBundle( b, hero.perks );
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Bundle.write( b, out );
		Bundle again = Bundle.read( new ByteArrayInputStream( out.toByteArray() ) );
		java.util.ArrayList<Perks.Perk> loaded = new java.util.ArrayList<>();
		Perks.restoreFromBundle( again, loaded );
		if (!loaded.equals( hero.perks )) fail( "perks changed in a save and load" );

		//steel skin halves a hit that would kill from full health
		hero.perks.clear();
		hero.perks.add( Perks.Perk.NO_ONESHOTS );
		hero.HP = hero.HT;
		Rat rat = new Rat();
		rat.pos = MobArenaTest.freeCellNextTo( hero.pos );
		hero.damage( hero.HT + 2, rat );
		if (hero.HP <= 0) fail( "Steel Skin did not stop a lethal hit from full health" );

		//protein infusion raises evasion when well fed
		hero.perks.clear();
		hero.HP = hero.HT;
		int plain = hero.defenseSkill( rat );
		hero.perks.add( Perks.Perk.PROTEIN_INFUSION );
		int fed = hero.defenseSkill( rat );
		if (fed <= plain) fail( "Protein Infusion did not raise evasion, " + plain + " then " + fed );

		//bigger lootbags
		hero.perks.clear();
		double without = 0, with = 0;
		int n = 400;
		Random.pushGenerator( 99 );
		for (int i = 0; i < n; i++) without += bagItems( new GambleBag() );
		hero.perks.add( Perks.Perk.MORE_BAG );
		for (int i = 0; i < n; i++) with += bagItems( new GambleBag() );
		Random.popGenerator();
		if (with <= without * 1.2) fail( "More Bag did not give clearly more items, " + without / n + " then " + with / n );

		System.out.println( "SUMMARY perks checked: " + Perks.Perk.values().length + ", failures: " + failures );
		System.exit( failures == 0 ? 0 : 1 );
	}
}
