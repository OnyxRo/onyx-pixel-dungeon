import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.Skin;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.SkinPaint;
import com.shatteredpixel.shatteredpixeldungeon.items.Torch;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.LeatherArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Berry;
import com.shatteredpixel.shatteredpixeldungeon.items.food.CakeBase;
import com.shatteredpixel.shatteredpixeldungeon.items.food.CoffeeBean;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Milk;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.UnstableBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Glaive;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WarHammer;
import com.shatteredpixel.shatteredpixeldungeon.journal.SkinCatalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Firebloom;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SkinSprites;
import com.watabou.utils.Bundle;
import com.watabou.utils.Reflection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Headless check for Shittened PD's item skins.
 * Every skin, its picture, its strings, its sparkle, saving and reloading the painted item.
 * The paint rules (ingredients and the mod's special steps), the journal record, a painted item in the
 * hero's bag surviving a save, and an old save with no skin key keeping the usual look.
 */
public class SkinPaintTest {

	static int failures = 0;
	static void fail( String what ) { failures++; System.out.println( "FAIL: " + what ); }

	static Bundle roundTrip( Bundle b ) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Bundle.write( b, out );
		return Bundle.read( new ByteArrayInputStream( out.toByteArray() ) );
	}

	static Item reload( Item item ) throws Exception {
		Bundle b = new Bundle();
		b.put( "item", item );
		return (Item) roundTrip( b ).get( "item" );
	}

	public static void main( String[] args ) throws Exception {
		TestBoot.boot();
		TestBoot.stubWindow();
		GamesInProgress.selectedClass = HeroClass.WARRIOR;
		Dungeon.seed = 424242L;
		Dungeon.sandbox = true;
		Dungeon.init();
		MobArenaTest.level( 3 );
		Hero hero = Dungeon.hero;

		//every skin
		int skins = 0, pictures = 0;
		for (Skin skin : Skin.values()) {
			skins++;
			Item item = (Item) Reflection.newInstance( skin.skinFor() );
			if (item == null) { fail( skin + " item cannot be made" ); continue; }
			SkinPaint.change( item, skin );
			if (item.image != skin.sprite()) fail( skin + " picture not set" );
			if (SkinSprites.is( item.image )) {
				pictures++;
				if (SkinSprites.frame( item.image ) == null) fail( skin + " has no frame on the skin sheet" );
				if (SkinSprites.width( item.image ) < 1 || SkinSprites.height( item.image ) < 1) fail( skin + " frame has no size" );
			}
			String info = SkinPaint.info( item );
			if (info.contains( Messages.NO_TEXT_FOUND )) fail( skin + " info string missing: " + info );
			if (skin.rarityName().contains( Messages.NO_TEXT_FOUND )) fail( skin + " rarity name missing" );
			String name = item.name();
			if (name == null || name.contains( Messages.NO_TEXT_FOUND )) fail( skin + " name missing" );
			if (skin.legendName() != null && !name.contains( skin.legendName() )) fail( skin + " legendary name not used, got " + name );
			if (skin.fullSkinName().isEmpty()) fail( skin + " full name empty" );
			item.emitter(); //builds the sparkle, must not throw
			if (Skin.current( item ) != skin) fail( skin + " current skin wrong" );

			Item back = reload( item );
			if (back.skin != skin) fail( skin + " lost on reload, got " + back.skin );
			if (back.image != skin.sprite()) fail( skin + " picture lost on reload" );

			ArrayList<String> actions = item.actions( hero );
			item.identify();
			actions = item.actions( hero );
			if (!actions.contains( SkinPaint.AC_PAINT )) fail( skin.skinFor().getSimpleName() + " has no paint action" );
			if (Messages.get( item, "ac_paint" ).contains( Messages.NO_TEXT_FOUND )) fail( skin + " paint label missing" );
			if (Messages.get( item, "applyskin" ).contains( Messages.NO_TEXT_FOUND )) fail( skin + " applyskin missing" );
		}
		System.out.println( "info skins " + skins + ", on the skin sheet " + pictures );
		if (skins < 500) fail( "only " + skins + " skins" );

		//an item never painted saves no skin, and an old save keeps the usual look
		LeatherArmor plain = new LeatherArmor();
		Bundle pb = new Bundle();
		plain.storeInBundle( pb );
		if (pb.contains( "skin" )) fail( "unpainted armor saved a skin" );
		Item plainBack = reload( plain );
		if (plainBack.skin != null || plainBack.image != new LeatherArmor().image) fail( "unpainted armor changed on reload" );
		if (SkinPaint.info( plainBack ).length() != 0) fail( "unpainted armor shows a skin line" );
		//a skin name this version does not know
		Bundle unknown = new Bundle();
		new LeatherArmor().storeInBundle( unknown );
		unknown.put( "skin", "NOT_A_SKIN" );
		LeatherArmor la = new LeatherArmor();
		la.restoreFromBundle( unknown );
		if (la.skin != null) fail( "unknown skin name was not ignored" );
		//only exact classes are paintable, the plain item class is not
		if (SkinPaint.paintable( new Torch() )) fail( "a torch should not be paintable" );

		//ingredients
		Item seed = new Firebloom.Seed();
		seed.identify();
		if (SkinPaint.pick( new LeatherArmor(), seed ) != Skin.ORANGELEATHER) fail( "firebloom seed should dye leather orange" );
		Item ring = new RingOfHaste();
		ring.identify();
		if (SkinPaint.pick( new BrokenSeal(), ring ) != Skin.RINGSEAL) fail( "any ring should give the ring seal" );

		//the glaive, rainbow then extra rainbow
		Glaive glaive = new Glaive();
		Item brew = new UnstableBrew();
		Skin s = SkinPaint.pick( glaive, brew );
		if (s != Skin.RAINBOWGLAIVE) fail( "first brew should give the rainbow glaive, got " + s );
		SkinPaint.change( glaive, s );
		s = SkinPaint.pick( glaive, brew );
		if (s != Skin.FULLRAINBOWGLAIVE) fail( "second brew should give the extra rainbow glaive, got " + s );
		if (SkinPaint.pick( new Glaive(), new Torch() ) != null) fail( "a torch on a plain glaive should do nothing" );

		//the seal and the cloak catch fire only when wooden
		BrokenSeal seal = new BrokenSeal();
		if (SkinPaint.pick( seal, new Torch() ) != null) fail( "a torch on a plain seal should do nothing" );
		SkinPaint.change( seal, Skin.WOODSEAL );
		if (SkinPaint.pick( seal, new Torch() ) != Skin.FIERYSEAL) fail( "a torch on a wooden seal should make it fiery" );
		CloakOfShadows cloak = new CloakOfShadows();
		SkinPaint.change( cloak, Skin.WOODENCLOAK );
		if (SkinPaint.pick( cloak, new Torch() ) != Skin.FIERYCLOAK) fail( "a torch on a wooden cloak should make it fiery" );

		//the cake hammer, in order, then eaten
		WarHammer hammer = new WarHammer();
		if (SkinPaint.pick( hammer, new Milk() ) != null) fail( "milk first should be refused" );
		SkinPaint.change( hammer, SkinPaint.pick( hammer, new CakeBase() ) );
		SkinPaint.change( hammer, SkinPaint.pick( hammer, new CoffeeBean() ) );
		SkinPaint.change( hammer, SkinPaint.pick( hammer, new Milk() ) );
		Berry berry = new Berry();
		berry.collect();
		Skin cake = SkinPaint.pick( hammer, berry );
		if (cake != Skin.CAKEHAMMER) fail( "the cake steps ended at " + cake );
		SkinPaint.change( hammer, cake );
		if (!hammer.actions( hero ).contains( SkinPaint.AC_EAT )) fail( "the cake hammer cannot be eaten" );
		if (!hammer.name().contains( "Cake" )) fail( "the cake hammer name is " + hammer.name() );

		//the spirit bow's arrows
		SpiritBow bow = new SpiritBow();
		SkinPaint.change( bow, Skin.FLAMINGBOW );
		Item arrow = bow.knockArrow();
		if (arrow.image != SkinSprites.FLAMING_ARROW) fail( "the flaming bow shoots a plain arrow" );
		if (arrow.name().contains( "Bow" )) fail( "the arrow took the bow's name" );

		//the journal records a painted skin, and saves it
		Dungeon.sandbox = false;
		SkinCatalog.reset();
		SkinPaint.apply( new LeatherArmor(), Skin.BLUELEATHER );
		Dungeon.sandbox = true;
		if (!SkinCatalog.isSeen( Skin.BLUELEATHER )) fail( "the journal did not record the blue leather" );
		if (SkinCatalog.applyCount( Skin.BLUELEATHER ) != 1) fail( "apply count is " + SkinCatalog.applyCount( Skin.BLUELEATHER ) );
		Bundle jb = new Bundle();
		SkinCatalog.store( jb );
		SkinCatalog.reset();
		SkinCatalog.restore( roundTrip( jb ) );
		if (!SkinCatalog.isSeen( Skin.BLUELEATHER )) fail( "the journal lost the blue leather on reload" );
		if (SkinCatalog.isSeen( Skin.REDCLOTH )) fail( "the journal shows a skin never applied" );
		if (SkinCatalog.totalAll() < 450) fail( "the journal lists only " + SkinCatalog.totalAll() + " skins" );
		SkinCatalog.reset();
		//the sandbox never writes to the journal
		SkinPaint.apply( new LeatherArmor(), Skin.REDCLOTH );
		if (SkinCatalog.isSeen( Skin.REDCLOTH )) fail( "a sandbox run wrote to the skin journal" );

		//painted items in the hero's bag and on the hero survive a save
		LeatherArmor worn = new LeatherArmor();
		worn.identify();
		SkinPaint.apply( worn, Skin.PURPLELEATHER );
		hero.belongings.armor = worn;
		SpiritBow carried = new SpiritBow();
		SkinPaint.apply( carried, Skin.GOLDENBOW );
		carried.collect();
		Bundle hb = new Bundle();
		hero.belongings.storeInBundle( hb );
		Belongings loaded = new Belongings( hero );
		loaded.restoreFromBundle( roundTrip( hb ) );
		if (loaded.armor == null || loaded.armor.skin != Skin.PURPLELEATHER) fail( "worn armor lost its skin on reload" );
		else if (loaded.armor.image != Skin.PURPLELEATHER.sprite()) fail( "worn armor lost its picture on reload" );
		SpiritBow loadedBow = loaded.getItem( SpiritBow.class );
		if (loadedBow == null || loadedBow.skin != Skin.GOLDENBOW) fail( "carried bow lost its skin on reload" );

		System.out.println( "SUMMARY skins checked: " + skins + ", failures: " + failures );
		System.exit( failures == 0 ? 0 : 1 );
	}
}
