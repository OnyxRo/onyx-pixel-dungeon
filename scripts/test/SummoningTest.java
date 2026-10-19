import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.minions.Minion;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.ConjurerBook;
import com.shatteredpixel.shatteredpixeldungeon.items.magic.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.items.magic.ConjurerSpell;
import com.shatteredpixel.shatteredpixeldungeon.items.magic.Heal;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfAttunement;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfSoulEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Knife;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.staffs.FroggitStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.staffs.Staff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;

/**
 * Summoning PD Reincarnated, headless. The Conjurer's start, staff summoning and attunement, minion save and load,
 * mana from minion kills, level up spells, the subclass spells, staffs in the loot and the new scroll pair.
 */
public class SummoningTest {

	static int failures = 0;
	static void fail( String what ) { failures++; System.out.println( "FAIL: " + what ); }

	public static void main( String[] args ) throws Exception {
		TestBoot.boot();
		GamesInProgress.selectedClass = HeroClass.CONJURER;
		Dungeon.seed = 515151L;
		Dungeon.sandbox = true;
		Dungeon.init();
		Hero hero = Dungeon.hero;
		//the test level makes a fresh hero, put the Conjurer back on it
		MobArenaTest.level( 3 );
		com.shatteredpixel.shatteredpixeldungeon.actors.Actor.remove( Dungeon.hero );
		int start = Dungeon.hero.pos;
		Dungeon.hero = hero;
		hero.live();
		hero.sprite = TestBoot.sprite();
		hero.sprite.ch = hero;
		hero.pos = start;
		com.shatteredpixel.shatteredpixeldungeon.actors.Actor.add( hero );

		//the start
		if (hero.heroClass != HeroClass.CONJURER) fail( "hero is not the Conjurer" );
		if (hero.maxMana != 20) fail( "Conjurer should start with 20 mana, has " + hero.maxMana );
		if (hero.attunement != 1) fail( "Conjurer should start with 1 attunement, has " + hero.attunement );
		if (hero.HT >= 20) fail( "Conjurer should be frail, HT " + hero.HT );
		if (!(hero.belongings.weapon instanceof Knife)) fail( "Conjurer should hold the toy knife" );
		if (hero.belongings.getItem( ConjurerBook.class ) == null) fail( "no spellbook" );
		if (hero.belongings.getItem( Heal.class ) == null) fail( "no Heal spell" );
		FroggitStaff staff = hero.belongings.getItem( FroggitStaff.class );
		if (staff == null) { fail( "no froggit staff" ); finish(); return; }
		for (String key : new String[]{"conjurer", "conjurer_desc", "conjurer_desc_short"}) {
			if (Messages.get( HeroClass.class, key ).contains( "!!!" )) fail( "missing hero text " + key );
		}
		for (HeroSubClass sub : new HeroSubClass[]{HeroSubClass.SOUL_REAVER, HeroSubClass.OCCULTIST}) {
			if (sub.title().contains( "!!!" ) || sub.desc().contains( "!!!" )) fail( "missing subclass text for " + sub );
		}
		if (HeroClass.CONJURER.armorAbilities().length != 3) fail( "Conjurer should have three armor abilities" );

		//summoning, attunement accounting, a second minion does not fit
		int before = Dungeon.level.mobs.size();
		staff.curCharges = 1;
		staff.summon( hero );
		Minion minion = null;
		for (Mob m : Dungeon.level.mobs) if (m instanceof Minion) minion = (Minion) m;
		if (minion == null || Dungeon.level.mobs.size() != before + 1) fail( "the staff summoned nothing" );
		else {
			if (minion.HT <= 0 || minion.maxDamage <= 0) fail( "minion has no stats from the staff" );
			if (Math.abs( hero.usedAttunement() - staff.requiredAttunement() ) > 0.001f) fail( "used attunement " + hero.usedAttunement() + " should be " + staff.requiredAttunement() );
			staff.curCharges = 1;
			staff.summon( hero );
			int count = 0;
			for (Mob m : Dungeon.level.mobs) if (m instanceof Minion) count++;
			if (count != 1) fail( "a second minion should not fit in 1 attunement, have " + count );

			//save and load keeps the staff's numbers
			Bundle b = new Bundle();
			minion.storeInBundle( b );
			Minion copy = minion.getClass().newInstance();
			copy.restoreFromBundle( b );
			if (copy.maxDamage != minion.maxDamage || copy.HT != minion.HT) fail( "minion lost its stats on reload" );

			//a kill by the minion gives mana
			hero.mana = 0;
			Rat rat = new Rat();
			rat.pos = minion.pos;
			Dungeon.level.mobs.add( rat );
			Actor.add( rat );
			rat.die( minion );
			if (hero.mana <= 0) fail( "a minion kill gave no mana" );
		}

		//hero save and load keeps attunement and mana
		hero.mana = 7;
		Bundle hb = new Bundle();
		hero.storeInBundle( hb );
		if (hb.getInt( "summ_mana" ) != 7 || hb.getFloat( "summ_attunement" ) != hero.attunement) fail( "hero save is missing mana or attunement" );

		//level 5 brings Barrier and more mana
		int mana = hero.maxMana;
		while (hero.lvl < 5) hero.earnExp( hero.maxExp(), null );
		if (hero.belongings.getItem( Barrier.class ) == null) fail( "level 5 should bring the Barrier spell" );
		if (hero.maxMana <= mana) fail( "levels should raise max mana" );

		//the subclass spells
		hero.subClass = HeroSubClass.SOUL_REAVER;
		ConjurerSpell.giveSubclassSpells( hero );
		if (hero.belongings.getItem( com.shatteredpixel.shatteredpixeldungeon.items.magic.soulreaver.Support.class ) == null) fail( "Soul Wielder spells missing" );
		hero.subClass = HeroSubClass.OCCULTIST;
		ConjurerSpell.giveSubclassSpells( hero );
		if (hero.belongings.getItem( com.shatteredpixel.shatteredpixeldungeon.items.magic.knight.Punch.class ) == null) fail( "Knight spells missing" );

		//staffs in the loot, by floor
		for (int depth = 1; depth <= 24; depth += 5) {
			Dungeon.depth = depth;
			Item item = Generator.random( Generator.Category.STAFF );
			if (!(item instanceof Staff)) fail( "no staff from the loot on floor " + depth );
		}

		//the new scroll and its exotic version
		if (ExoticScroll.regToExo.get( ScrollOfAttunement.class ) != ScrollOfSoulEnergy.class) fail( "Scroll of Attunement has no exotic version" );
		new ScrollOfSoulEnergy();
		new ScrollOfAttunement().image();

		finish();
	}

	static void finish() {
		System.out.println( "SUMMARY summoning failures=" + failures );
		System.exit( failures > 0 ? 1 : 0 );
	}
}
