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
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

/**
 * Opened by clicking the song name at the top left. Like, ban, skip or add the song
 * playing now without leaving the game, or open the full song list.
 */
public class WndNowPlaying extends Window {

	private static final int WIDTH = 130;
	private static final int GAP   = 2;
	private static final int BTN_H = 16;

	public WndNowPlaying( final String path ) {

		super();

		Song found = WndSongs.byPath().get( path );
		final Song song = found != null ? found : new Song( path, "", WndSongs.nameOf( path ) );

		RenderedTextBlock title = PixelScene.renderTextBlock( song.name, 9 );
		title.hardlight( TITLE_COLOR );
		title.maxWidth( WIDTH );
		title.align( RenderedTextBlock.CENTER_ALIGN );
		title.setPos( (WIDTH - title.width()) / 2, 0 );
		PixelScene.align(title);
		add( title );
		float top = title.bottom() + GAP;

		if (!song.mod.isEmpty()) {
			RenderedTextBlock mod = PixelScene.renderTextBlock( Messages.get(this, "from", song.mod), 6 );
			mod.maxWidth( WIDTH );
			mod.setPos( (WIDTH - mod.width()) / 2, top );
			PixelScene.align(mod);
			add( mod );
			top = mod.bottom() + GAP;
		}
		top += GAP;

		float half = WIDTH / 2f - 1;

		RedButton like = new RedButton( Messages.get(this, SongPlayer.isLiked( path ) ? "unlike" : "like"), 7 ) {
			@Override
			protected void onClick() {
				SongPlayer.toggleLiked( path );
				GLog.i( Messages.get(WndNowPlaying.class, SongPlayer.isLiked( path ) ? "liked" : "unliked", song.name) );
				reopen( path );
			}
		};
		like.setRect( 0, top, half, BTN_H );
		add( like );

		RedButton ban = new RedButton( Messages.get(this, SongPlayer.isBanned( path ) ? "unban" : "ban"), 7 ) {
			@Override
			protected void onClick() {
				SongPlayer.toggleBanned( path );
				GLog.i( Messages.get(WndSongs.class, SongPlayer.isBanned( path ) ? "banned" : "unbanned", song.name) );
				//a ban during a shuffle has already moved on, so the window shows the next song
				String next = SongPlayer.nowPlaying();
				if (next != null) reopen( next );
				else hide();
			}
		};
		ban.setRect( half + 2, top, half, BTN_H );
		add( ban );
		top += BTN_H + GAP;

		RedButton addTo = new RedButton( Messages.get(this, "add"), 7 ) {
			@Override
			protected void onClick() {
				hide();
				GameScene.show( new WndAddToPlaylist( song ) );
			}
		};
		addTo.setRect( 0, top, half, BTN_H );
		add( addTo );

		RedButton skip = new RedButton( Messages.get(this, "skip"), 7 ) {
			@Override
			protected void onClick() {
				SongPlayer.skip();
				hide();
			}
		};
		skip.setRect( half + 2, top, half, BTN_H );
		skip.enable( SongPlayer.canSkip() );
		add( skip );
		top += BTN_H + GAP;

		RedButton all = new RedButton( Messages.get(this, "songs"), 7 ) {
			@Override
			protected void onClick() {
				hide();
				GameScene.show( new WndSongs() );
			}
		};
		all.setRect( 0, top, WIDTH, BTN_H );
		add( all );
		top += BTN_H;

		resize( WIDTH, (int)top );
	}

	private void reopen( String path ){
		hide();
		GameScene.show( new WndNowPlaying( path ) );
	}
}
