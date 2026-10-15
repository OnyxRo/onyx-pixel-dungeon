/*
 * Onyx's Pixel Dungeon
 * Copyright (C) 2026 Onyx Rothman
 *
 * Based on Shattered Pixel Dungeon, Copyright (C) 2014-2026 Evan Debenham
 * and Pixel Dungeon, Copyright (C) 2012-2015 Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollingListPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Reflection;

/**
 * The item part of the sandbox panel. Lists every item in the journal's catalog
 * and gives the hero whichever one is clicked, already identified.
 */
public class WndSandboxItems extends Window {

	private static final int WIDTH       = 126;
	private static final int GAP         = 2;
	private static final int BTN_SIZE    = 16;
	private static final int MAX_UPGRADE = 15;
	//how many of a stackable item one click gives
	private static final int STACK       = 5;

	//remembered between openings, so a set of gear can be handed out at one level
	private static int upgrade = 0;

	private RenderedTextBlock upgradeText;

	private static String query = "";

	public WndSandboxItems() {

		super();

		int height = (int)Math.min( 200, PixelScene.uiCamera.height - 40 );

		RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(WndSandbox.class, "items_title"), 12 );
		title.hardlight( TITLE_COLOR );
		title.setPos( (WIDTH - title.width()) / 2, 0 );
		PixelScene.align(title);
		add( title );

		float top = title.bottom() + GAP;

		RedButton less = new RedButton( "-" ) {
			@Override
			protected void onClick() {
				setUpgrade( upgrade - 1 );
			}
		};
		less.setRect( 0, top, BTN_SIZE, BTN_SIZE );
		add( less );

		RedButton more = new RedButton( "+" ) {
			@Override
			protected void onClick() {
				setUpgrade( upgrade + 1 );
			}
		};
		more.setRect( WIDTH - BTN_SIZE, top, BTN_SIZE, BTN_SIZE );
		add( more );

		upgradeText = PixelScene.renderTextBlock( 8 );
		add( upgradeText );
		setUpgrade( upgrade );
		upgradeText.setPos( (WIDTH - upgradeText.width()) / 2, top + (BTN_SIZE - upgradeText.height()) / 2 );
		PixelScene.align(upgradeText);

		top += BTN_SIZE + GAP;

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
							hide();
							GameScene.show( new WndSandboxItems() );
						}
					}
				} );
			}
		};
		search.setRect( 0, top, WIDTH, 16 );
		add( search );
		top += 16 + GAP;

		//the window has to be at its final size before the list is placed, or the list draws off centre
		resize( WIDTH, height );

		ScrollingListPane list = new ScrollingListPane();
		add( list );
		list.setRect( 0, top, WIDTH, height - top );

		for (Catalog catalog : Catalog.values()) {
			boolean titled = false;
			for (final Class<?> cls : catalog.items()) {
				//enchantments and glyphs are in the catalog too, but aren't items
				if (!Item.class.isAssignableFrom(cls)) continue;
				Item sample = (Item) Reflection.newInstance(cls);
				if (sample == null) continue;

				String name = Messages.get(cls, "name");
				if (name.equals(Messages.NO_TEXT_FOUND)) name = sample.name();
				if (!query.isEmpty() && !name.toLowerCase().contains( query.toLowerCase() )) continue;

				if (!titled) {
					list.addTitle( Messages.titleCase(catalog.title()) );
					titled = true;
				}

				list.addItem( new ScrollingListPane.ListItem( new ItemSprite(sample), Messages.titleCase(name) ) {
					@Override
					public boolean onClick( float x, float y ) {
						if (inside( x, y )) {
							give( cls );
							return true;
						}
						return false;
					}
				} );
			}
		}

		list.setRect( 0, top, WIDTH, height - top );
	}

	private void setUpgrade( int value ) {
		upgrade = Math.max( 0, Math.min( MAX_UPGRADE, value ) );
		upgradeText.text( Messages.get(WndSandbox.class, "upgrade", upgrade) );
	}

	private static void give( Class<?> cls ) {
		Item item = (Item) Reflection.newInstance(cls);
		if (item == null) return;

		if (item.isUpgradable() && upgrade > 0) item.upgrade( upgrade );
		if (item.stackable) item.quantity( STACK );
		item.identify();

		if (item.collect( Dungeon.hero.belongings.backpack )) {
			GLog.p( Messages.get(WndSandbox.class, "given", item.name()) );
		} else {
			Dungeon.level.drop( item, Dungeon.hero.pos ).sprite.drop();
			GLog.w( Messages.get(WndSandbox.class, "dropped", item.name()) );
		}
	}

}
