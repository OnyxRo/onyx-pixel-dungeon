import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;
import com.watabou.utils.Reflection;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * The item testing ground, the counterpart of MobArenaTest for weapons, armor, rings, wands and the rest.
 *
 *   ItemArenaTest --new          catalog items whose files changed since the last commit
 *   ItemArenaTest --all          every item in the journal catalog
 *   ItemArenaTest SwordMidas ... just those classes, by simple name
 *
 * Per item it checks the name and description strings, that it has a picture, saving and reloading,
 * and for weapons and armor a damage roll at +0 and +3, a hit on a rat, and every enchantment or glyph proc.
 * Lines starting FAIL fail the run. Run from the assets folder, scripts/check.sh does that.
 */
public class ItemArenaTest {

	public static void main(String[] args) throws Exception {
		TestBoot.boot();
		TestBoot.stubWindow();
		MobArenaTest.loadBaseline();

		List<Class<?>> items = pick( args );
		if (items.isEmpty()) {
			System.out.println( "no items to check for " + String.join( " ", args ) );
			System.out.println( "SUMMARY items checked: 0, clean: 0, failures: 0" );
			System.exit( 0 );
		}

		GamesInProgress.selectedClass = HeroClass.WARRIOR;
		Dungeon.seed = 424242L;
		Dungeon.sandbox = true;
		Dungeon.init();

		int ok = 0;
		for (Class<?> cls : items) {
			int before = MobArenaTest.failures;
			com.watabou.utils.Random.pushGenerator( cls.getName().hashCode() );
			try {
				check( cls );
			} catch (Throwable t) {
				MobArenaTest.fail( cls, "crashed the test", t );
			} finally {
				com.watabou.utils.Random.popGenerator();
			}
			if (MobArenaTest.failures == before) ok++;
		}
		System.out.println( "SUMMARY items checked: " + items.size() + ", clean: " + ok + ", failures: " + MobArenaTest.failures );
		System.exit( MobArenaTest.failures == 0 ? 0 : 1 );
	}

	static List<Class<?>> pick( String[] args ) throws Exception {
		boolean all = false, changed = args.length == 0;
		Set<String> names = new LinkedHashSet<>();
		for (String a : args) {
			if (a.equals( "--all" )) all = true;
			else if (a.equals( "--new" )) changed = true;
			else if (!a.startsWith( "--" )) names.add( a );
		}
		Set<String> changedFiles = changed ? MobArenaTest.changedJavaNames() : new LinkedHashSet<>();
		Set<Class<?>> out = new LinkedHashSet<>();
		for (Catalog cat : Catalog.values()) {
			for (Class<?> cls : cat.items()) {
				Class<?> top = cls.getEnclosingClass() == null ? cls : cls.getEnclosingClass();
				if (all || names.contains( cls.getSimpleName() ) || changedFiles.contains( top.getSimpleName() )) out.add( cls );
			}
		}
		return new ArrayList<>( out );
	}

	static boolean missing( String s ) {
		return s == null || s.isEmpty() || s.contains( "NO TEXT FOUND" );
	}

	static void check( Class<?> cls ) throws Exception {
		if (!Item.class.isAssignableFrom( cls ) && !(com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon.Enchantment.class.isAssignableFrom( cls ))
				&& !(com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor.Glyph.class.isAssignableFrom( cls ))) return;
		if (Modifier.isAbstract( cls.getModifiers() )) return;
		MobArenaTest.level( 3 );
		if (Dungeon.hero.fieldOfView == null) Dungeon.hero.fieldOfView = new boolean[MobArenaTest.level.length()];

		//enchantments and glyphs are not items, they are checked through a weapon or armor below
		if (com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon.Enchantment.class.isAssignableFrom( cls )) {
			checkEnchant( cls );
			return;
		}
		if (com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor.Glyph.class.isAssignableFrom( cls )) {
			checkGlyph( cls );
			return;
		}

		Item item = (Item) Reflection.newInstance( cls );
		if (item == null) { MobArenaTest.fail( cls, "has no public constructor without arguments", null ); return; }

		if (missing( item.name() ) || item.name().startsWith( "items." )) MobArenaTest.fail( cls, "has no name string", null );
		if (missing( item.desc() ) || item.desc().startsWith( "items." )) MobArenaTest.fail( cls, "has no description string", null );
		if (item.image() == 0 && !cls.getSimpleName().equals( "Gold" )) MobArenaTest.warn( cls, "uses picture 0, the placeholder" );

		//save and reload
		Bundle b = new Bundle();
		b.put( "item", item );
		Item back = (Item) b.get( "item" );
		if (back == null || back.getClass() != cls) MobArenaTest.fail( cls, "did not come back from a save as the same class", null );

		if (item instanceof Weapon && !(item instanceof MissileWeapon && ((MissileWeapon) item).tier == 0 && false)) {
			Weapon w = (Weapon) item;
			for (int lvl : new int[]{0, 3}) {
				w.level( lvl );
				int min = w.min(), max = w.max();
				if (min < 0 || max < min) MobArenaTest.fail( cls, "damage range is wrong at +" + lvl + ": " + min + " to " + max, null );
				int roll = w.damageRoll( Dungeon.hero );
				if (roll < 0) MobArenaTest.fail( cls, "damage roll is negative at +" + lvl, null );
			}
			w.level( 0 );
			if (w.STRReq() <= 0 || w.STRReq() > 30) MobArenaTest.fail( cls, "strength requirement is " + w.STRReq(), null );
			Rat rat = new Rat();
			rat.pos = MobArenaTest.freeCellNextTo( Dungeon.hero.pos );
			rat.HP = rat.HT = 1000;
			if (rat.pos >= 0) {
				Actor.add( rat );
				rat.sprite = TestBoot.sprite();
				rat.sprite.ch = rat;
				try {
					w.proc( Dungeon.hero, rat, 5 );
				} catch (Throwable t) {
					MobArenaTest.fail( cls, "crashed when it hit a rat", t );
				}
				Actor.remove( rat );
			}
			Bundle wb = new Bundle();
			w.enchant( com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon.Enchantment.random() );
			wb.put( "w", w );
			if (wb.get( "w" ) == null) MobArenaTest.fail( cls, "did not reload with an enchantment", null );
		}
		if (item instanceof Armor) {
			Armor a = (Armor) item;
			a.level( 3 );
			if (a.DRMax() < a.DRMin()) MobArenaTest.fail( cls, "armor range is wrong", null );
		}
	}

	static void checkEnchant( Class<?> cls ) {
		com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon.Enchantment e =
				(com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon.Enchantment) Reflection.newInstance( cls );
		if (missing( e.name( "x" ) ) ) MobArenaTest.fail( cls, "has no name string", null );
		if (missing( e.desc() )) MobArenaTest.fail( cls, "has no description string", null );
		Rat rat = new Rat();
		rat.pos = MobArenaTest.freeCellNextTo( Dungeon.hero.pos );
		if (rat.pos < 0) return;
		rat.HP = rat.HT = 1000;
		Actor.add( rat );
		rat.sprite = TestBoot.sprite();
		rat.sprite.ch = rat;
		com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sword sword = new com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sword();
		sword.enchant( e );
		try {
			for (int i = 0; i < 20; i++) e.proc( sword, Dungeon.hero, rat, 5 );
		} catch (Throwable t) {
			MobArenaTest.fail( cls, "crashed when it hit a rat", t );
		}
		Actor.remove( rat );
		Bundle b = new Bundle();
		b.put( "e", e );
		if (b.get( "e" ) == null) MobArenaTest.fail( cls, "did not come back from a save", null );
	}

	static void checkGlyph( Class<?> cls ) {
		com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor.Glyph g =
				(com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor.Glyph) Reflection.newInstance( cls );
		if (missing( g.name( "x" ) )) MobArenaTest.fail( cls, "has no name string", null );
		if (missing( g.desc() )) MobArenaTest.fail( cls, "has no description string", null );
		Rat rat = new Rat();
		rat.pos = MobArenaTest.freeCellNextTo( Dungeon.hero.pos );
		if (rat.pos < 0) return;
		Actor.add( rat );
		rat.sprite = TestBoot.sprite();
		rat.sprite.ch = rat;
		Armor armor = new com.shatteredpixel.shatteredpixeldungeon.items.armor.LeatherArmor();
		armor.inscribe( g );
		try {
			for (int i = 0; i < 20; i++) g.proc( armor, rat, Dungeon.hero, 5 );
		} catch (Throwable t) {
			MobArenaTest.fail( cls, "crashed when it was hit by a rat", t );
		}
		Actor.remove( rat );
	}
}
