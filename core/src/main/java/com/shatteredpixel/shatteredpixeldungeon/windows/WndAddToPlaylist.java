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
import com.shatteredpixel.shatteredpixeldungeon.SongPlayer.Song;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollingListPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.ArrayList;

/**
 * Adds one song to a playlist. The top button uses the playlist a song was last added to,
 * the list below picks any other, and New playlist makes one for it.
 */
public class WndAddToPlaylist extends Window {

	private static final int WIDTH = 140;
	private static final int GAP   = 2;
	private static final int BTN_H = 16;

	public WndAddToPlaylist( final Song song ) {

		super();

		RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(this, "title", song.name), 9 );
		title.hardlight( TITLE_COLOR );
		title.maxWidth( WIDTH );
		title.align( RenderedTextBlock.CENTER_ALIGN );
		title.setPos( (WIDTH - title.width()) / 2, 0 );
		PixelScene.align(title);
		add( title );

		float top = title.bottom() + GAP + 1;

		final String last = SongPlayer.lastPlaylist();
		if (last != null) {
			RedButton quick = new RedButton( label( last, song ), 8 ) {
				@Override
				protected void onClick() {
					toggle( last, song );
				}
			};
			quick.textColor( TITLE_COLOR );
			quick.setRect( 0, top, WIDTH, BTN_H + 2 );
			add( quick );
			top += BTN_H + 2 + GAP;
		}

		RedButton create = new RedButton( Messages.get(this, "new"), 7 ) {
			@Override
			protected void onClick() {
				WndPlaylists.askName( Messages.get(WndPlaylists.class, "new_title"), "", new WndPlaylists.NameChosen() {
					@Override
					public void chosen( String name ) {
						if (!SongPlayer.createPlaylist( name )) {
							GLog.w( Messages.get(WndPlaylists.class, "name_taken") );
							return;
						}
						toggle( SongPlayer.cleanName( name ), song );
					}
				} );
			}
		};
		create.setRect( 0, top, WIDTH, BTN_H );
		add( create );
		top += BTN_H + GAP;

		ArrayList<String> others = SongPlayer.playlistNames();
		others.remove( last );

		if (others.isEmpty()) {
			resize( WIDTH, (int)top );
			return;
		}

		RenderedTextBlock pick = PixelScene.renderTextBlock( Messages.get(this, last == null ? "pick" : "pick_other"), 6 );
		pick.setPos( 0, top + 1 );
		PixelScene.align(pick);
		add( pick );
		top = pick.bottom() + GAP + 1;

		int height = (int)Math.min( top + others.size() * 18, PixelScene.uiCamera.height - 40 );
		resize( WIDTH, height );

		ScrollingListPane list = new ScrollingListPane();
		add( list );
		list.holdLayout = true;
		for (final String name : others) {
			list.addItem( new ScrollingListPane.ListItem(
					Icons.get( SongPlayer.inPlaylist( name, song.path ) ? Icons.CHECKED : Icons.UNCHECKED ), name ) {
				@Override
				public boolean onClick( float x, float y ) {
					if (!inside( x, y )) return false;
					toggle( name, song );
					return true;
				}
			} );
		}
		list.holdLayout = false;
		list.setRect( 0, top, WIDTH, height - top );
	}

	private static String label( String playlist, Song song ){
		return Messages.get(WndAddToPlaylist.class,
				SongPlayer.inPlaylist( playlist, song.path ) ? "take_out" : "add_to", playlist );
	}

	private void toggle( String playlist, Song song ){
		SongPlayer.togglePlaylist( playlist, song.path );
		GLog.p( Messages.get(WndAddToPlaylist.class,
				SongPlayer.inPlaylist( playlist, song.path ) ? "added" : "removed", song.name, playlist ) );
		hide();
	}
}
