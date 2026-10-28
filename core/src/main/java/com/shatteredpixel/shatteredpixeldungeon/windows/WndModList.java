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

import com.shatteredpixel.shatteredpixeldungeon.levels.WorldShuffle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollingListPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

import java.util.Locale;

/**
 * Every mod in the game with its summary, opened from the Mods button on the title screen
 * (where Shattered had its News button). The cross on the right of a mod bans it, so none
 * of its worlds or bosses come up in the shuffle or the boss crawl. Bans are kept in the settings.
 */
public class WndModList extends Window {

	private static final int WIDTH = 160;
	private static final int GAP   = 2;
	private static final int MARK_W = 15;

	public WndModList() {
		super();

		int height = (int) Math.min( 230, PixelScene.uiCamera.height - 20 );

		RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get( this, "title" ), 12 );
		title.hardlight( TITLE_COLOR );
		title.setPos( (WIDTH - title.width()) / 2, 0 );
		PixelScene.align( title );
		add( title );

		RenderedTextBlock desc = PixelScene.renderTextBlock( Messages.get( this, "desc" ), 6 );
		desc.maxWidth( WIDTH );
		desc.setPos( 0, title.bottom() + GAP );
		add( desc );

		float top = desc.bottom() + 2 * GAP;

		//the window has to be at its final size before the list is placed, or the list draws off centre
		resize( WIDTH, height );

		ScrollingListPane list = new ScrollingListPane();
		add( list );
		for (String mod : WorldShuffle.mods()) list.addItem( new ModItem( mod ) );
		list.setRect( 0, top, WIDTH, height - top );
	}

	/** The mod's summary from windows.wndmodinfo, keyed by its name with only letters and digits kept. */
	public static String summary( String mod ){
		String desc = Messages.get( WndModInfo.class, mod.toLowerCase( Locale.ROOT ).replaceAll( "[^a-z0-9]", "" ) );
		return desc.equals( Messages.NO_TEXT_FOUND ) ? Messages.get( WndModInfo.class, "unknown" ) : desc;
	}

	private static Image logo( String mod ){
		for (int w = 0; w < WorldShuffle.worldCount(); w++) {
			if (WorldShuffle.modName( w ).equals( mod ) && WorldShuffle.modLogo( w ) != null) {
				return new Image( WorldShuffle.modLogo( w ) );
			}
		}
		return Icons.get( Icons.NEWS );
	}

	private static class ModItem extends ScrollingListPane.ListItem {

		private final String mod;
		private Image ban;

		ModItem( String mod ) {
			super( logo( mod ), "_" + mod + "_\n" + summary( mod ) );
			this.mod = mod;
			if (icon.height > 0) icon.scale.set( 14f / icon.height );
			ban = Icons.get( Icons.CLOSE );
			add( ban );
			refresh();
		}

		private void refresh(){
			boolean off = WorldShuffle.isBanned( mod );
			ban.alpha( off ? 1f : 0.3f );
			label.alpha( off ? 0.4f : 1f );
			icon.alpha( off ? 0.4f : 1f );
		}

		@Override
		protected void layout() {
			super.layout();
			if (ban == null) return;
			label.maxWidth( (int)(width - 16 - 1 - MARK_W) );
			label.setPos( x + 17, y + (height() - label.height()) / 2f );
			PixelScene.align( label );
			icon.y = y + 2;
			PixelScene.align( icon );
			ban.x = x + width - (MARK_W + ban.width()) / 2f;
			ban.y = y + 1 + (height() - 1 - ban.height()) / 2f;
			PixelScene.align( ban );
		}

		@Override
		public boolean onClick( float x, float y ) {
			if (!inside( x, y )) return false;
			if (this.x + width - x < MARK_W) {
				WorldShuffle.toggleBanned( mod );
				refresh();
			}
			return true;
		}
	}
}
