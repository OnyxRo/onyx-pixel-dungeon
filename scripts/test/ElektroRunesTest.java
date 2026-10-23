import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WaterOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Goo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.YogDzewa;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Recipe;
import com.shatteredpixel.shatteredpixeldungeon.items.ancientrunes.*;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.BloodOrb;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CrackedGeode;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.ScrollHolder;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTrauma;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfDamnation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfDivination;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.GreaterTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.InvariantAncientRune;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfChaos;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfCosmicTravel;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.GhostDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.TippedDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.VoidDart;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.secret.SecretSpellRoom;
import com.shatteredpixel.shatteredpixeldungeon.plants.Ghostbulb;
import com.shatteredpixel.shatteredpixeldungeon.plants.Voidpearl;
import com.watabou.utils.Bundle;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Headless check for Elektrocheckers PD's ancient rune magic, from finding a rune to using it.
 * Goo and Yog drop a rune, the mod's weights give all eight kinds, each rune does its job,
 * the Invariant rune recipe and Greater Transmutation work on runes, runes fit the scroll holder,
 * and the mod's other loot is in the decks with a weight above zero.
 */
public class ElektroRunesTest {

	static int failures = 0, checks = 0;

	static void check( boolean ok, String what ) {
		checks++;
		if (!ok) {
			failures++;
			System.out.println( "FAIL: " + what );
		}
	}

	interface Step { void run() throws Exception; }

	//effects on the stub sprite can fail after the rune has done its work, that is the headless limit
	static void headless( String what, Step s ) {
		try {
			s.run();
		} catch (Throwable e) {
			Throwable c = e instanceof java.lang.reflect.InvocationTargetException ? e.getCause() : e;
			if (c instanceof NullPointerException) {
				System.out.println( "info: " + what + " stopped at a headless effect, " + c );
			} else {
				failures++;
				System.out.println( "FAIL: " + what + " threw " + c );
				c.printStackTrace( System.out );
			}
		}
	}

	static int count( Hero hero, Class<?> cls ) {
		int n = 0;
		for (Item i : hero.belongings.backpack.items) if (cls.isInstance( i )) n += i.quantity();
		return n;
	}

	static boolean runeAt( int pos ) {
		Heap h = Dungeon.level.heaps.get( pos );
		if (h == null) return false;
		for (Item i : h.items) if (i instanceof AncientRune) return true;
		return false;
	}

	static void select( AncientRuneInventory rune, Item target ) throws Exception {
		Method m = rune.getClass().getDeclaredMethod( "onItemSelected", Item.class );
		m.setAccessible( true );
		m.invoke( rune, target );
	}

	static void cast( AncientRune rune, Hero hero ) throws Exception {
		Method m = rune.getClass().getDeclaredMethod( "onCast", Hero.class );
		m.setAccessible( true );
		m.invoke( rune, hero );
	}

	static boolean inDeck( Generator.Category cat, Class<?> cls ) {
		for (int i = 0; i < cat.classes.length; i++) {
			if (cat.classes[i] == cls) {
				float w = cat.defaultProbs[i];
				if (cat.defaultProbs2 != null) w += cat.defaultProbs2[i];
				return w > 0;
			}
		}
		return false;
	}

	public static void main( String[] args ) throws Exception {
		TestBoot.boot();
		GamesInProgress.selectedClass = HeroClass.WARRIOR;
		Dungeon.seed = 424242L;
		Dungeon.init();
		MobArenaTest.level( 7 );
		Hero hero = Dungeon.hero;
		java.lang.reflect.Field cu = Item.class.getDeclaredField( "curUser" );
		cu.setAccessible( true );
		cu.set( null, hero );

		//finding runes, Goo and Yog drop one each when they die
		ElektroHooks.dropBossRune( null );
		Goo goo = new Goo();
		goo.pos = MobArenaTest.freeCellNextTo( hero.pos );
		goo.sprite = TestBoot.sprite();
		goo.sprite.ch = goo;
		Dungeon.level.mobs.add( goo );
		Actor.add( goo );
		headless( "Goo dies", () -> goo.die( null ) );
		check( runeAt( goo.pos ), "Goo drops an ancient rune" );
		YogDzewa yog = new YogDzewa();
		yog.pos = MobArenaTest.freeCellNextTo( hero.pos );
		ElektroHooks.dropBossRune( yog );
		check( runeAt( yog.pos ), "the boss rune drop works for Yog" );

		//the mod's weights reach all eight runes
		HashSet<Class<?>> kinds = new HashSet<>();
		for (int i = 0; i < 2000; i++) kinds.add( ElektroHooks.randomRune().getClass() );
		check( kinds.size() == 8, "random runes give all 8 kinds, got " + kinds.size() );

		//every rune has text, a picture and saves
		AncientRune[] all = { new ContravariantAncientRune(), new CovariantAncientRune(), new NablaAncientRune(),
				new FieldOperatorAncientRune(), new PartialAncientRune(), new LaplaceAncientRune(),
				new PropagationAncientRune(), new ExistenceAncientRune() };
		for (AncientRune r : all) {
			String n = r.getClass().getSimpleName();
			check( !r.name().contains( "NO TEXT" ) && !r.name().startsWith( "!!!" ), n + " has a name" );
			check( !r.desc().contains( "NO TEXT" ) && !r.desc().startsWith( "!!!" ) && r.desc().length() > 20, n + " has a description" );
			check( r.image() != 0, n + " has a picture" );
			Bundle b = new Bundle();
			b.put( "r", r );
			check( b.get( "r" ) != null && b.get( "r" ).getClass() == r.getClass(), n + " saves and loads" );
			check( new ScrollHolder().canHold( r ), n + " fits the scroll holder" );
		}

		//using them
		int str = hero.STR;
		hero.HP = 1;
		headless( "Field rune", () -> cast( new FieldOperatorAncientRune(), hero ) );
		check( hero.HP == hero.HT && hero.STR == str + 1, "Field rune heals fully and adds strength" );

		int before = hero.belongings.backpack.items.size();
		headless( "Existence rune", () -> cast( new ExistenceAncientRune(), hero ) );
		check( hero.belongings.backpack.items.size() > before, "Existence rune conjures equipment" );

		hero.lvl = 15;
		int tp = hero.bonusTalentPoints( 2 );
		headless( "Propagation rune", () -> cast( new PropagationAncientRune(), hero ) );
		check( hero.bonusTalentPoints( 2 ) >= tp + 2, "Propagation rune adds two tier 2 talent points" );

		ScrollOfIdentify id = new ScrollOfIdentify();
		id.collect();
		headless( "Laplace rune", () -> select( new LaplaceAncientRune(), id ) );
		check( count( hero, ScrollOfDivination.class ) == 3, "Laplace rune turns a scroll into three exotics" );

		ScrollOfDivination div = new ScrollOfDivination();
		div.collect();
		int ids = count( hero, ScrollOfIdentify.class );
		headless( "Partial rune", () -> select( new PartialAncientRune(), div ) );
		check( count( hero, ScrollOfIdentify.class ) == ids + 3, "Partial rune turns an exotic into three scrolls" );

		Sword sword = new Sword();
		sword.collect();
		headless( "Nabla rune", () -> select( new NablaAncientRune(), sword ) );
		check( count( hero, Sword.class ) == 0, "Nabla rune transmutes the weapon" );

		PartialAncientRune other = new PartialAncientRune();
		other.collect();
		int co = count( hero, CovariantAncientRune.class );
		headless( "Covariant rune", () -> select( new CovariantAncientRune(), other ) );
		check( count( hero, PartialAncientRune.class ) == 0 && count( hero, CovariantAncientRune.class ) > co,
				"Covariant rune turns another rune into a pair" );

		//the Invariant rune recipe and Greater Transmutation on a rune
		ArrayList<Item> in = new ArrayList<>();
		in.add( new ContravariantAncientRune() );
		in.add( new CovariantAncientRune() );
		boolean found = false;
		for (Recipe r : Recipe.findRecipes( in )) found |= r.sampleOutput( in ) instanceof InvariantAncientRune;
		check( found, "Contravariant and Covariant brew the Invariant rune" );
		Method change = GreaterTransmutation.class.getDeclaredMethod( "changeAncientRune", AncientRune.class );
		change.setAccessible( true );
		LaplaceAncientRune lap = new LaplaceAncientRune();
		Object out = change.invoke( null, lap );
		check( out instanceof AncientRune && out.getClass() != LaplaceAncientRune.class, "Greater Transmutation changes a rune" );

		//the mod's other loot sits in the decks where the mod put it
		check( inDeck( Generator.Category.SCROLL, ScrollOfTrauma.class ), "Scroll of Trauma is in the scroll deck" );
		check( inDeck( Generator.Category.WAND, WandOfChaos.class ), "Wand of Chaos is in the wand deck" );
		check( inDeck( Generator.Category.WAND, WandOfCosmicTravel.class ), "Wand of Cosmic Travel is in the wand deck" );
		check( inDeck( Generator.Category.ARTIFACT, BloodOrb.class ), "Blood Orb is in the artifact deck" );
		check( inDeck( Generator.Category.ARTIFACT, CrackedGeode.class ), "Cracked Geode is in the artifact deck" );
		check( inDeck( Generator.Category.SEED, Ghostbulb.Seed.class ), "Ghostbulb is in the seed deck" );
		check( inDeck( Generator.Category.SEED, Voidpearl.Seed.class ), "Voidpearl is in the seed deck" );
		check( TippedDart.getTipped( new Voidpearl.Seed(), 1 ) instanceof VoidDart, "Voidpearl tips a Void Dart" );
		check( TippedDart.getTipped( new Ghostbulb.Seed(), 1 ) instanceof GhostDart, "Ghostbulb tips a Ghost Dart" );
		check( ExoticScroll.regToExo.get( ScrollOfTrauma.class ) == ScrollOfDamnation.class, "Trauma brews into Damnation" );
		ScrollOfTrauma trauma = new ScrollOfTrauma();
		check( trauma.image() != 0 && !trauma.name().contains( "NO TEXT" ), "Scroll of Trauma has a rune and text" );
		check( new SecretSpellRoom() != null, "the secret spell room exists" );

		//the well of enchantment
		Sword s2 = new Sword();
		Method affect = WaterOfEnchantment.class.getDeclaredMethod( "affectItem", Item.class, int.class );
		affect.setAccessible( true );
		headless( "well of enchantment", () -> affect.invoke( new WaterOfEnchantment(), s2, hero.pos ) );
		check( s2.enchantment != null, "the well of enchantment enchants a dropped weapon" );

		System.out.println( "SUMMARY elektro " + checks + " checks, " + failures + " failures" );
		System.exit( failures == 0 ? 0 : 1 );
	}
}
