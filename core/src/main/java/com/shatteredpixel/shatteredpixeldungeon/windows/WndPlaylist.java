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
import com.watabou.noosa.Image;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * One playlist. Play it in order or shuffled, add songs, rename or delete it.
 * Clicking a song plays the playlist from there, the cross on the right takes it out.
 */
public class WndPlaylist extends Window {

	private static final int WIDTH = 150;
	private static final int GAP   = 2;
	private static final int BTN_H = 16;

	private final String name;

	public WndPlaylist( final String name ) {

		super();
		this.name = name;

		int height = (int)Math.min( 200, PixelScene.uiCamera.height - 40 );

		RenderedTextBlock title = PixelScene.renderTextBlock( name, 12 );
		title.hardlight( TITLE_COLOR );
		title.maxWidth( WIDTH );
		title.setPos( (WIDTH - title.width()) / 2, 0 );
		PixelScene.align(title);
		add( title );

		float top = title.bottom() + GAP;
		float half = WIDTH / 2f - 1;

		final ArrayList<String> songs = SongPlayer.playlist( name );

		RedButton play = new RedButton( Messages.get(this, "play"), 7 ) {
			@Override
			protected void onClick() {
				start( 0, false );
			}
		};
		play.setRect( 0, top, half, BTN_H );
		play.enable( !songs.isEmpty() );
		add( play );

		RedButton shuffle = new RedButton( Messages.get(this, "shuffle"), 7 ) {
			@Override
			protected void onClick() {
				start( -1, true );
			}
		};
		shuffle.setRect( half + 2, top, half, BTN_H );
		shuffle.enable( !songs.isEmpty() );
		add( shuffle );
		top += BTN_H + GAP;

		RedButton addSongs = new RedButton( Messages.get(this, "add"), 7 ) {
			@Override
			protected void onClick() {
				hide();
				WndSongs.addingTo = name;
				GameScene.show( new WndSongs() );
			}
		};
		addSongs.setRect( 0, top, half, BTN_H );
		add( addSongs );

		RedButton rename = new RedButton( Messages.get(this, "rename"), 7 ) {
			@Override
			protected void onClick() {
				WndPlaylists.askName( Messages.get(WndPlaylist.class, "rename_title"), name, new WndPlaylists.NameChosen() {
					@Override
					public void chosen( String to ) {
						if (SongPlayer.cleanName( to ).equals( name )) return;
						if (!SongPlayer.renamePlaylist( name, to )) {
							GLog.w( Messages.get(WndPlaylists.class, "name_taken") );
							return;
						}
						WndPlaylist.this.hide();
						GameScene.show( new WndPlaylist( SongPlayer.cleanName( to ) ) );
					}
				} );
			}
		};
		rename.setRect( half + 2, top, half, BTN_H );
		add( rename );
		top += BTN_H + GAP;

		RedButton back = new RedButton( Messages.get(this, "back"), 7 ) {
			@Override
			protected void onClick() {
				hide();
				GameScene.show( new WndPlaylists() );
			}
		};
		back.setRect( 0, top, half, BTN_H );
		add( back );

		RedButton delete = new RedButton( Messages.get(this, "delete"), 7 ) {
			@Override
			protected void onClick() {
				GameScene.show( new WndOptions( Messages.get(WndPlaylist.class, "delete_title", name),
						Messages.get(WndPlaylist.class, "delete_body"),
						Messages.get(WndPlaylist.class, "delete_yes"), Messages.get(WndPlaylist.class, "delete_no") ) {
					@Override
					protected void onSelect( int index ) {
						if (index != 0) return;
						if (name.equals( SongPlayer.playingList )) {
							WndSongs.clearChoice();
							if (Dungeon.level != null) Dungeon.level.playLevelMusic();
						}
						SongPlayer.deletePlaylist( name );
						WndPlaylist.this.hide();
						GameScene.show( new WndPlaylists() );
					}
				} );
			}
		};
		delete.setRect( half + 2, top, half, BTN_H );
		delete.textColor( 0xFF6666 );
		add( delete );
		top += BTN_H + GAP;

		resize( WIDTH, height );

		ScrollingListPane list = new ScrollingListPane();
		add( list );
		list.setRect( 0, top, WIDTH, height - top );

		if (songs.isEmpty()) {
			list.addItem( new ScrollingListPane.ListItem( null, Messages.get(this, "empty") ) );
		}
		LinkedHashMap<String, Song> all = WndSongs.byPath();
		java.util.HashMap<String, Integer> nameCount = new java.util.HashMap<>();
		for (String path : songs) {
			Song s = all.get( path );
			if (s != null) nameCount.put( s.name, nameCount.containsKey( s.name ) ? nameCount.get( s.name ) + 1 : 1 );
		}
		for (int i = 0; i < songs.size(); i++) {
			Song song = all.get( songs.get( i ) );
			//a song whose file is gone still shows, so it can be taken out
			String label = song != null ? song.name : songs.get( i ).substring( songs.get( i ).lastIndexOf( '/' ) + 1 );
			//two songs with the same name are told apart by their mod
			if (song != null && nameCount.get( song.name ) > 1) {
				label += " (" + song.mod.replace( " Pixel Dungeon", "" ) + ")";
			}
			list.addItem( new ListSong( i, songs.get( i ), label ) );
		}

		list.setRect( 0, top, WIDTH, height - top );
	}

	private void start( int from, boolean shuffled ) {
		WndSongs.chosen = "playlist";
		SongPlayer.playPlaylist( name, from, shuffled );
		GLog.p( Messages.get(WndPlaylist.class, shuffled ? "shuffling" : "playing", name) );
		hide();
		GameScene.show( new WndPlaylist( name ) );
	}

	/** A song in the playlist. The cross on the right takes it out, the rest plays the playlist from here. */
	private class ListSong extends ScrollingListPane.ListItem {

		private final int index;
		private final String path;
		private final Image cross;

		ListSong( int index, String path, String label ) {
			super( Icons.get(Icons.AUDIO), label );
			this.index = index;
			this.path = path;
			if (path.equals( SongPlayer.current ) && name.equals( SongPlayer.playingList )) hardlight( TITLE_COLOR );
			cross = Icons.get( Icons.CLOSE );
			cross.scale.set( 0.6f );
			add( cross );
		}

		@Override
		protected void layout() {
			super.layout();
			if (cross == null) return;
			label.maxWidth( (int)(width - 16 - 1 - 14) );
			cross.x = x + width - cross.width() - 3;
			cross.y = y + 1 + (height() - 1 - cross.height()) / 2f;
			PixelScene.align( cross );
		}

		@Override
		public boolean onClick( float x, float y ) {
			if (!inside( x, y )) return false;
			if (x > this.x + width - 16) {
				SongPlayer.togglePlaylist( name, path );
				WndPlaylist.this.hide();
				GameScene.show( new WndPlaylist( name ) );
				return true;
			}
			start( index, false );
			return true;
		}
	}
}
