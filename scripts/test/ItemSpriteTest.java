import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.sprites.EXItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Reflection;

/**
 * Every item in the journal catalog must point at a picture that exists on the item sheet. One that does not
 * crashes the game as soon as the journal's item list is opened (it happened on 2026-10-23).
 */
public class ItemSpriteTest {

	//Transformation PD's second sheet (EXItemSpriteSheet) marks its numbers with a flag, ItemSprite draws those from ex_items.png
	static boolean hasPicture( int image ) {
		//ex_items.png is 256 by 512, 16 columns and 32 rows
		if (EXItemSpriteSheet.isEX( image )) return (image & 0xFFFF) < 16 * 32;
		return ItemSpriteSheet.film.get( image ) != null;
	}

	public static void main( String[] args ) throws Exception {
		TestBoot.boot();
		int failures = 0, checks = 0;
		//the bestiary loads every monster class on the hero select screen, before any hero exists (crash eeca67f4)
		com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero = null;
		for (com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary b : com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary.values()) {
			for (Class<?> cls : b.entities()) {
				try {
					Class.forName( cls.getName(), true, cls.getClassLoader() );
				} catch (Throwable t) {
					failures++;
					System.out.println( "FAIL: " + cls.getName() + " cannot load with no hero, " + (t.getCause() != null ? t.getCause() : t) );
				}
			}
		}
		for (Catalog cat : Catalog.values()) {
			for (Class<?> cls : cat.items()) {
				if (!Item.class.isAssignableFrom( cls )) continue;
				Item item;
				try {
					item = (Item) Reflection.newInstance( cls );
				} catch (Throwable t) {
					failures++;
					System.out.println( "FAIL: " + cls.getName() + " could not be made, " + t );
					continue;
				}
				if (item == null) {
					failures++;
					System.out.println( "FAIL: " + cls.getName() + " could not be made" );
					continue;
				}
				checks++;
				if (!hasPicture( item.image )) {
					failures++;
					System.out.println( "FAIL: " + cls.getName() + " image " + item.image + " is not on the item sheet" );
				}
				//the journal shows item text from the title screen, before any hero exists
				com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero = null;
				try {
					com.shatteredpixel.shatteredpixeldungeon.windows.WndJournal.catalogText( item );
				} catch (Throwable t) {
					failures++;
					System.out.println( "FAIL: " + cls.getName() + " journal text with no hero, " + t );
				}
				if (item instanceof Ring) ((Ring) item).anonymize();
				else if (item instanceof Potion) ((Potion) item).anonymize();
				else if (item instanceof Scroll) ((Scroll) item).anonymize();
				if (!hasPicture( item.image )) {
					failures++;
					System.out.println( "FAIL: " + cls.getName() + " unidentified image " + item.image + " is not on the item sheet" );
				}
			}
		}
		System.out.println( "SUMMARY item sprites " + checks + " items, " + failures + " failures" );
		System.exit( failures == 0 ? 0 : 1 );
	}
}
