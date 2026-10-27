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

import com.shatteredpixel.shatteredpixeldungeon.SongPlayer;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollingListPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

/**
 * Every playlist the player has made, plus a New playlist button.
 * Clicking a playlist opens it in WndPlaylist.
 */
public class WndPlaylists extends Window {

	private static final int WIDTH = 150;
	private static final int GAP   = 2;
	private static final int BTN_H = 16;

	public WndPlaylists() {

		super();

		int height = (int)Math.min( 200, PixelScene.uiCamera.height - 40 );

		RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(this, "title"), 12 );
		title.hardlight( TITLE_COLOR );
		title.setPos( (WIDTH - title.width()) / 2, 0 );
		PixelScene.align(title);
		add( title );

		float top = title.bottom() + GAP;
		float half = WIDTH / 2f - 1;

		RedButton back = new RedButton( Messages.get(this, "back"), 7 ) {
			@Override
			protected void onClick() {
				hide();
				GameScene.show( new WndSongs() );
			}
		};
		back.setRect( 0, top, half, BTN_H );
		add( back );

		RedButton create = new RedButton( Messages.get(this, "new"), 7 ) {
			@Override
			protected void onClick() {
				askName( Messages.get(WndPlaylists.class, "new_title"), "", new NameChosen() {
					@Override
					public void chosen( String name ) {
						if (!SongPlayer.createPlaylist( name )) {
							GLog.w( Messages.get(WndPlaylists.class, "name_taken") );
							return;
						}
						hide();
						//straight to adding songs, the usual next step
						WndSongs.addingTo = SongPlayer.cleanName( name );
						GameScene.show( new WndSongs() );
					}
				} );
			}
		};
		create.setRect( half + 2, top, half, BTN_H );
		add( create );
		top += BTN_H + GAP;

		resize( WIDTH, height );

		ScrollingListPane list = new ScrollingListPane();
		add( list );
		list.setRect( 0, top, WIDTH, height - top );

		if (SongPlayer.playlistNames().isEmpty()) {
			list.addItem( new ScrollingListPane.ListItem( null, Messages.get(this, "none") ) );
		}
		for (final String name : SongPlayer.playlistNames()) {
			int count = SongPlayer.playlist( name ).size();
			ScrollingListPane.ListItem item = new ScrollingListPane.ListItem( Icons.get(Icons.AUDIO),
					Messages.get(this, "entry", name, count) ) {
				@Override
				public boolean onClick( float x, float y ) {
					if (!inside( x, y )) return false;
					WndPlaylists.this.hide();
					GameScene.show( new WndPlaylist( name ) );
					return true;
				}
			};
			if (name.equals( SongPlayer.playingList )) item.hardlight( TITLE_COLOR );
			list.addItem( item );
		}

		list.setRect( 0, top, WIDTH, height - top );
	}

	interface NameChosen {
		void chosen( String name );
	}

	/** Asks for a playlist name and hands it on, unless the player cancels or leaves it blank. */
	static void askName( String title, String start, final NameChosen then ) {
		GameScene.show( new WndTextInput( title, null, start, 30, false,
				Messages.get(WndPlaylists.class, "ok"), Messages.get(WndPlaylists.class, "cancel") ) {
			@Override
			public void onSelect( boolean positive, String text ) {
				if (!positive) return;
				if (SongPlayer.cleanName( text ).isEmpty()) {
					GLog.w( Messages.get(WndPlaylists.class, "name_empty") );
					return;
				}
				then.chosen( text );
			}
		} );
	}
}
