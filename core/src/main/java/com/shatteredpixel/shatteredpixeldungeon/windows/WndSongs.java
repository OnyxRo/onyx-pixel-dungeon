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

import com.badlogic.gdx.Gdx;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SongPlayer;
import com.shatteredpixel.shatteredpixeldungeon.SongPlayer.Song;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.WorldShuffle;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.WorldLevel;
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
import com.watabou.noosa.audio.Music;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;

/**
 * A compendium of every song in the game and from every mod, grouped by mod.
 * Clicking a song plays it and keeps it playing, floor after floor, until another song
 * is picked or the floor's own music is chosen again. The mark on the right of a song
 * likes it. Liked only narrows the list, and shuffle plays random songs one after another,
 * from all songs or from liked ones. Search narrows the list by name.
 */
public class WndSongs extends Window {

	private static final int WIDTH = 150;
	private static final int GAP   = 2;
	private static final int BTN_H = 16;

	private static final String SHATTERED = "Shattered Pixel Dungeon";
	private static final String DEVOTED   = "Devoted Pixel Dungeon";

	private static boolean likedOnly = false;
	private static String query = "";

	/** Set while a song or shuffle was picked from the list, null while floors play their own music. */
	public static String chosen = null;

	/** Called whenever a floor starts its music, so a picked song carries on. */
	public static void applyChoice(){
		if (SongPlayer.current == null) return;
		//shuffle keeps its own song going
		if (SongPlayer.shuffle != SongPlayer.Shuffle.OFF) return;
		Music.INSTANCE.play( SongPlayer.current, true );
	}

	/** Back to each floor's own music. */
	public static void clearChoice(){
		SongPlayer.clear();
		chosen = null;
	}

	public WndSongs() {

		super();

		int height = (int)Math.min( 200, PixelScene.uiCamera.height - 40 );

		RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(this, "title"), 12 );
		title.hardlight( TITLE_COLOR );
		title.setPos( (WIDTH - title.width()) / 2, 0 );
		PixelScene.align(title);
		add( title );

		float top = title.bottom() + GAP;
		float half = WIDTH / 2f - 1;

		RedButton liked = new RedButton( Messages.get(this, likedOnly ? "liked_only_on" : "liked_only_off"), 7 ) {
			@Override
			protected void onClick() {
				likedOnly = !likedOnly;
				reopen();
			}
		};
		liked.setRect( 0, top, half, BTN_H );
		add( liked );

		RedButton search = new RedButton( query.isEmpty() ? Messages.get(this, "search")
				: Messages.get(this, "search_for", query), 7 ) {
			@Override
			protected void onClick() {
				GameScene.show( new WndTextInput( Messages.get(WndSongs.class, "search"), null, query, 30, false,
						Messages.get(WndSongs.class, "search_go"), Messages.get(WndSongs.class, "cancel") ) {
					@Override
					public void onSelect( boolean positive, String text ) {
						if (positive) {
							query = text.trim();
							reopen();
						}
					}
				} );
			}
		};
		search.setRect( half + 2, top, half, BTN_H );
		add( search );
		top += BTN_H + GAP;

		final LinkedHashMap<String, ArrayList<Song>> groups = songs();

		RedButton shuffleAll = new RedButton( Messages.get(this, "shuffle_all"), 7 ) {
			@Override
			protected void onClick() {
				ArrayList<Song> pool = new ArrayList<>();
				for (ArrayList<Song> g : groups.values()) pool.addAll( g );
				chosen = "shuffle";
				SongPlayer.startShuffle( SongPlayer.Shuffle.ALL, pool );
				GLog.p( Messages.get(WndSongs.class, "shuffle_msg") );
			}
		};
		shuffleAll.setRect( 0, top, half, BTN_H );
		add( shuffleAll );

		RedButton shuffleLiked = new RedButton( Messages.get(this, "shuffle_liked"), 7 ) {
			@Override
			protected void onClick() {
				ArrayList<Song> pool = new ArrayList<>();
				for (ArrayList<Song> g : groups.values()) {
					for (Song s : g) if (SongPlayer.isLiked( s.path )) pool.add( s );
				}
				if (pool.isEmpty()) {
					GLog.w( Messages.get(WndSongs.class, "no_liked") );
					return;
				}
				chosen = "shuffle";
				SongPlayer.startShuffle( SongPlayer.Shuffle.LIKED, pool );
				GLog.p( Messages.get(WndSongs.class, "shuffle_msg") );
			}
		};
		shuffleLiked.setRect( half + 2, top, half, BTN_H );
		add( shuffleLiked );
		top += BTN_H + GAP;

		//the window has to be at its final size before the list is placed, or the list draws off centre
		resize( WIDTH, height );

		ScrollingListPane list = new ScrollingListPane();
		add( list );
		list.setRect( 0, top, WIDTH, height - top );

		list.addItem( new ScrollingListPane.ListItem( Icons.get(Icons.REPEAT), Messages.get(this, "floor_music") ) {
			@Override
			public boolean onClick( float x, float y ) {
				if (!inside( x, y )) return false;
				clearChoice();
				if (Dungeon.level != null) Dungeon.level.playLevelMusic();
				GLog.i( Messages.get(WndSongs.class, "floor_music_msg") );
				return true;
			}
		} );

		String q = query.toLowerCase();
		boolean any = false;
		for (java.util.Map.Entry<String, ArrayList<Song>> group : groups.entrySet()) {
			String mod = group.getKey().replace(" Pixel Dungeon", "");
			boolean titled = false;
			for (final Song song : group.getValue()) {
				if (likedOnly && !SongPlayer.isLiked( song.path )) continue;
				if (!q.isEmpty() && !song.name.toLowerCase().contains( q ) && !mod.toLowerCase().contains( q )) continue;
				if (!titled) {
					list.addTitle( mod );
					titled = true;
				}
				any = true;
				list.addItem( new SongItem( song ) );
			}
		}
		if (!any) {
			list.addItem( new ScrollingListPane.ListItem( null, Messages.get(this, "none") ) );
		}

		list.setRect( 0, top, WIDTH, height - top );
	}

	private void reopen(){
		hide();
		GameScene.show( new WndSongs() );
	}

	/** A song in the list. The right edge likes or unlikes it, the rest plays it. */
	private static class SongItem extends ScrollingListPane.ListItem {

		private final Song song;
		private Image heart;

		SongItem( Song song ) {
			super( Icons.get(Icons.AUDIO), song.name );
			this.song = song;
			if (song.path.equals( SongPlayer.current )) hardlight( TITLE_COLOR );
			heart = Icons.get( SongPlayer.isLiked( song.path ) ? Icons.CHECKED : Icons.UNCHECKED );
			add( heart );
		}

		@Override
		protected void layout() {
			super.layout();
			if (heart == null) return;
			label.maxWidth( (int)(width - 16 - 1 - 14) );
			heart.x = x + width - heart.width() - 2;
			heart.y = y + 1 + (height() - 1 - heart.height()) / 2f;
			PixelScene.align( heart );
		}

		@Override
		public boolean onClick( float x, float y ) {
			if (!inside( x, y )) return false;
			if (x > this.x + width - 16) {
				SongPlayer.toggleLiked( song.path );
				remove( heart );
				heart.destroy();
				heart = Icons.get( SongPlayer.isLiked( song.path ) ? Icons.CHECKED : Icons.UNCHECKED );
				add( heart );
				layout();
				return true;
			}
			chosen = song.path;
			SongPlayer.play( song.path );
			GLog.p( Messages.get(WndSongs.class, "playing", song.name) );
			return true;
		}
	}

	//every song, grouped by the mod it came from
	private static LinkedHashMap<String, ArrayList<Song>> songs() {
		LinkedHashMap<String, ArrayList<Song>> groups = new LinkedHashMap<>();
		groups.put( SHATTERED, new ArrayList<Song>() );
		HashSet<String> seen = new HashSet<>();

		for (Field f : Assets.Music.class.getFields()) {
			if (f.getType() != String.class || !Modifier.isStatic( f.getModifiers() )) continue;
			try {
				String path = (String) f.get( null );
				if (path == null || !path.startsWith("music/") || !seen.add( path )) continue;
				if (!Gdx.files.internal( path ).exists()) continue;
				String mod = path.contains("devoted") ? DEVOTED : SHATTERED;
				if (!groups.containsKey( mod )) groups.put( mod, new ArrayList<Song>() );
				groups.get( mod ).add( new Song( path, mod, pretty( path ) ) );
			} catch (IllegalAccessException e) {
				//a field we cannot read has no song in it
			}
		}

		//songs only a ported world plays, which the main list does not know about
		for (int world = WorldShuffle.HALLS + 1; world < WorldShuffle.worldCount(); world++) {
			Level level = WorldShuffle.create( world );
			if (!(level instanceof WorldLevel)) continue;
			String path = ((WorldLevel) level).musicTrack();
			if (path == null || !seen.add( path ) || !Gdx.files.internal( path ).exists()) continue;
			String mod = WorldShuffle.modName( world );
			if (!groups.containsKey( mod )) groups.put( mod, new ArrayList<Song>() );
			groups.get( mod ).add( new Song( path, mod,
					Messages.get(WndSongs.class, "theme", Messages.titleCase(WorldShuffle.menuName(world))) ) );
		}

		//every other song found in the mods' own repositories
		for (Song s : SongPlayer.modSongs()) {
			if (!seen.add( s.path )) continue;
			if (!groups.containsKey( s.mod )) groups.put( s.mod, new ArrayList<Song>() );
			groups.get( s.mod ).add( s );
		}

		return groups;
	}

	//"music/01-TheSecretofSpring-TheGardens.ogg" becomes "The Secret of Spring (The Gardens)"
	private static String pretty( String path ) {
		String base = path.substring( path.lastIndexOf('/') + 1 );
		int dot = base.lastIndexOf('.');
		if (dot > 0) base = base.substring( 0, dot );
		base = base.replaceAll( "^[0-9.]+[-_ ]*", "" );

		String[] parts = base.split( "-" );
		String name = words( parts[0] );
		if (parts.length > 1 && !parts[1].isEmpty()) name += " (" + words( parts[1] ) + ")";
		return name;
	}

	private static String words( String s ) {
		s = s.replace( '_', ' ' );
		s = s.replaceAll( "(?<=[a-z])(of|and|in)(?=[A-Z])", " $1 " );
		s = s.replaceAll( "(?<=[a-z])(?=[A-Z])", " " );
		s = s.replaceAll( "\\s+", " " ).trim();
		return s.isEmpty() ? s : Character.toUpperCase( s.charAt(0) ) + s.substring( 1 );
	}

}
