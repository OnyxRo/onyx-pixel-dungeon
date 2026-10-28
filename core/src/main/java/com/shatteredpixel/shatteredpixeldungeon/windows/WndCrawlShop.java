package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollingListPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.utils.Reflection;

/**
 * The boss crawl merchant's goods. Every item in the journal catalog, as many times as the hero can pay for,
 * priced like a Shattered shop on the first floors, listed from most to least expensive. Clicking an item shows
 * what it does and offers to buy it. The Sell button sells from the backpack the way Shattered's shops do.
 */
public class WndCrawlShop extends Window {

	private static final int WIDTH = 126;
	private static final int GAP   = 2;
	//the cheapest anything sells for, items with no shop value (quest items and the like) cost this
	private static final int MIN_PRICE = 50;

	private static String query = "";

	private RenderedTextBlock goldText;

	public WndCrawlShop() {

		super();

		int height = (int)Math.min( 200, PixelScene.uiCamera.height - 40 );

		RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(this, "title"), 12 );
		title.hardlight( TITLE_COLOR );
		title.setPos( (WIDTH - title.width()) / 2, 0 );
		PixelScene.align(title);
		add( title );

		float top = title.bottom() + GAP;

		goldText = PixelScene.renderTextBlock( 8 );
		add( goldText );
		updateGold();
		goldText.setPos( (WIDTH - goldText.width()) / 2, top );
		PixelScene.align(goldText);
		top = goldText.bottom() + GAP;

		RedButton search = new RedButton( query.isEmpty() ? Messages.get(WndSandbox.class, "search")
				: Messages.get(WndSandbox.class, "search_for", query), 7 ) {
			@Override
			protected void onClick() {
				GameScene.show( new WndTextInput( Messages.get(WndSandbox.class, "search"), null, query, 30, false,
						Messages.get(WndSandbox.class, "search_go"), Messages.get(WndSandbox.class, "cancel") ) {
					@Override
					public void onSelect( boolean positive, String text ) {
						if (positive) {
							query = text.trim();
							WndCrawlShop.this.hide();
							GameScene.show( new WndCrawlShop() );
						}
					}
				} );
			}
		};
		search.setRect( 0, top, (WIDTH - GAP) / 2f, 16 );
		add( search );

		//selling goes through Shattered's shop, an item sells for a fifth of what the merchant asks for it
		RedButton sell = new RedButton( Messages.get(this, "sell"), 7 ) {
			@Override
			protected void onClick() {
				WndCrawlShop.this.hide();
				com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Shopkeeper.sell();
			}
		};
		sell.setRect( search.right() + GAP, top, WIDTH - search.right() - GAP, 16 );
		add( sell );
		top += 16 + GAP;

		//the window has to be at its final size before the list is placed, or the list draws off centre
		resize( WIDTH, height );

		ScrollingListPane list = new ScrollingListPane();
		add( list );
		list.setRect( 0, top, WIDTH, height - top );
		list.holdLayout = true;

		//every item in one list, the most expensive first
		java.util.ArrayList<Object[]> goods = new java.util.ArrayList<>();
		java.util.HashSet<Class<?>> listed = new java.util.HashSet<>();
		for (Catalog catalog : Catalog.values()) {
			for (final Class<?> cls : catalog.items()) {
				//enchantments and glyphs are in the catalog too, but aren't items
				if (!Item.class.isAssignableFrom(cls)) continue;
				final Item sample = make( cls );
				if (sample == null) continue;
				final int price = price( sample );
				if (price < 0) continue;

				String name = Messages.get(cls, "name");
				if (name.equals(Messages.NO_TEXT_FOUND)) name = sample.name();
				if (!query.isEmpty() && !name.toLowerCase().contains( query.toLowerCase() )) continue;
				if (!listed.add( cls )) continue;

				goods.add( new Object[]{ cls, sample, Messages.titleCase(name), price } );
			}
		}
		java.util.Collections.sort( goods, new java.util.Comparator<Object[]>() {
			@Override
			public int compare( Object[] a, Object[] b ) {
				int byPrice = Integer.compare( (Integer) b[3], (Integer) a[3] );
				return byPrice != 0 ? byPrice : ((String) a[2]).compareTo( (String) b[2] );
			}
		} );

		for (Object[] g : goods) {
			final Class<?> cls = (Class<?>) g[0];
			final String itemName = (String) g[2];
			final int price = (Integer) g[3];
			list.addItem( new ScrollingListPane.ListItem( new ItemSprite( (Item) g[1] ), Messages.get(this, "price", itemName, price) ) {
				@Override
				public boolean onClick( float x, float y ) {
					if (inside( x, y )) {
						offer( cls, itemName, price );
						return true;
					}
					return false;
				}
			} );
		}

		list.holdLayout = false;
		list.setRect( 0, top, WIDTH, height - top );
	}

	private void updateGold() {
		goldText.text( Messages.get(this, "gold", Dungeon.gold) );
	}

	//a fresh, identified copy of an item, or null if the item cannot be made outside the place it belongs
	private static Item make( Class<?> cls ) {
		try {
			Item item = (Item) Reflection.newInstance(cls);
			if (item != null) item.identify();
			return item;
		} catch (Throwable t) {
			return null;
		}
	}

	//what the merchant asks, the same as a Shattered shop on floors 1 to 4, or -1 when the item has no price
	public static int price( Item item ) {
		try {
			return Math.max( MIN_PRICE, item.value() * 5 );
		} catch (Throwable t) {
			return -1;
		}
	}

	private void offer( final Class<?> cls, final String label, final int price ) {
		final Item sample = make( cls );
		if (sample == null) return;
		String info;
		try {
			info = sample.info();
		} catch (Throwable t) {
			info = "";
		}
		GameScene.show( new WndOptions( new ItemSprite(sample), label, info,
				Messages.get(this, "buy", price), Messages.get(WndSandbox.class, "cancel") ) {
			@Override
			protected void onSelect( int index ) {
				if (index == 0) buy( cls, price );
			}
		} );
	}

	private void buy( Class<?> cls, int price ) {
		if (Dungeon.gold < price) {
			GLog.w( Messages.get(this, "poor", price) );
			return;
		}
		Item item = make( cls );
		if (item == null) return;
		Dungeon.gold -= price;
		Sample.INSTANCE.play( Assets.Sounds.GOLD );
		if (item.collect( Dungeon.hero.belongings.backpack )) {
			GLog.p( Messages.get(this, "bought", item.name()) );
		} else {
			Dungeon.level.drop( item, Dungeon.hero.pos ).sprite.drop();
			GLog.w( Messages.get(WndSandbox.class, "dropped", item.name()) );
		}
		updateGold();
	}
}
