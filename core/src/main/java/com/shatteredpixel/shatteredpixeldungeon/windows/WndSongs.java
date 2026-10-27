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

	/** While set, the mark on each song adds it to or takes it out of this playlist instead of liking it. */
	public static String addingTo = null;
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

		if (addingTo != null && !SongPlayer.hasPlaylist( addingTo )) addingTo = null;

		RenderedTextBlock title = PixelScene.renderTextBlock( addingTo == null ? Messages.get(this, "title")
				: Messages.get(this, "adding_title", addingTo), addingTo == null ? 12 : 9 );
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

		if (addingTo != null) {
			RedButton done = new RedButton( Messages.get(this, "adding_done"), 7 ) {
				@Override
				protected void onClick() {
					String name = addingTo;
					addingTo = null;
					hide();
					GameScene.show( new WndPlaylist( name ) );
				}
			};
			done.setRect( 0, top, WIDTH, BTN_H );
			add( done );
			top += BTN_H + GAP;
		} else {

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

		RedButton lists = new RedButton( Messages.get(this, "playlists"), 7 ) {
			@Override
			protected void onClick() {
				hide();
				GameScene.show( new WndPlaylists() );
			}
		};
		lists.setRect( 0, top, WIDTH, BTN_H );
		add( lists );
		top += BTN_H + GAP;
		}

		//the window has to be at its final size before the list is placed, or the list draws off centre
		resize( WIDTH, height );

		ScrollingListPane list = new ScrollingListPane();
		add( list );
		list.setRect( 0, top, WIDTH, height - top );

		if (addingTo == null) list.addItem( new ScrollingListPane.ListItem( Icons.get(Icons.REPEAT), Messages.get(this, "floor_music") ) {
			@Override
			public boolean onClick( float x, float y ) {
				if (!inside( x, y )) return false;
				clearChoice();
				if (Dungeon.level != null) Dungeon.level.playLevelMusic();
				GLog.i( Messages.get(WndSongs.class, "floor_music_msg") );
				return true;
			}
		} );

		list.holdLayout = true;
		String q = query.toLowerCase();
		boolean any = false;
		SongItem playing = null;
		String now = SongPlayer.nowPlaying();
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
				SongItem item = new SongItem( song );
				if (song.path.equals( now )) playing = item;
				list.addItem( item );
			}
		}
		if (!any) {
			list.addItem( new ScrollingListPane.ListItem( null, Messages.get(this, "none") ) );
		}

		list.holdLayout = false;
		list.setRect( 0, top, WIDTH, height - top );

		//open on the song playing now, a little below the top so its mod title shows
		if (playing != null) list.scrollTo( 0, playing.top() - 2 * playing.height() );
	}

	@Override
	public void onBackPressed() {
		//closing the window ends adding, so the next time it opens it likes songs again
		addingTo = null;
		super.onBackPressed();
	}

	private void reopen(){
		hide();
		GameScene.show( new WndSongs() );
	}

	//each of the three marks on the right of a song takes this much room
	private static final int MARK_W = 15;

	/**
	 * A song in the list. On the right, from left to right, the add button picks a playlist for it,
	 * the cross bans it from shuffles and the box likes it. The rest of the row plays it.
	 * While adding to a playlist only the box shows, and it adds or takes out the song.
	 */
	private static class SongItem extends ScrollingListPane.ListItem {

		private final Song song;
		private Image heart, ban, plus;

		SongItem( Song song ) {
			super( Icons.get(Icons.AUDIO), song.name );
			this.song = song;
			if (song.path.equals( SongPlayer.nowPlaying() )) hardlight( TITLE_COLOR );
			else if (addingTo == null && SongPlayer.isBanned( song.path )) label.alpha( 0.4f );
			refreshMarks();
		}

		private void refreshMarks(){
			for (Image i : new Image[]{ heart, ban, plus }) {
				if (i != null) {
					remove( i );
					i.destroy();
				}
			}
			if (addingTo != null) {
				heart = Icons.get( SongPlayer.inPlaylist( addingTo, song.path ) ? Icons.CHECKED : Icons.UNCHECKED );
				ban = plus = null;
				add( heart );
			} else {
				heart = Icons.get( SongPlayer.isLiked( song.path ) ? Icons.CHECKED : Icons.UNCHECKED );
				ban = Icons.get( Icons.CLOSE );
				if (!SongPlayer.isBanned( song.path )) ban.alpha( 0.3f );
				plus = Icons.get( Icons.PLUS );
				add( heart );
				add( ban );
				add( plus );
			}
		}

		@Override
		protected void layout() {
			super.layout();
			if (heart == null) return;
			int marks = addingTo != null ? 1 : 3;
			label.maxWidth( (int)(width - 16 - 1 - MARK_W * marks) );
			Image[] row = { heart, ban, plus };
			for (int i = 0; i < marks; i++) {
				Image m = row[i];
				m.x = x + width - MARK_W * i - (MARK_W + m.width()) / 2f;
				m.y = y + 1 + (height() - 1 - m.height()) / 2f;
				PixelScene.align( m );
			}
		}

		@Override
		public boolean onClick( float x, float y ) {
			if (!inside( x, y )) return false;
			float fromRight = this.x + width - x;
			if (addingTo != null && fromRight < MARK_W) {
				SongPlayer.togglePlaylist( addingTo, song.path );
				refreshMarks();
				layout();
				return true;
			}
			if (addingTo == null && fromRight < MARK_W * 3) {
				if (fromRight < MARK_W) {
					SongPlayer.toggleLiked( song.path );
				} else if (fromRight < MARK_W * 2) {
					SongPlayer.toggleBanned( song.path );
					label.alpha( SongPlayer.isBanned( song.path ) ? 0.4f : 1f );
					GLog.i( Messages.get(WndSongs.class, SongPlayer.isBanned( song.path ) ? "banned" : "unbanned", song.name) );
				} else {
					GameScene.show( new WndAddToPlaylist( song ) );
				}
				refreshMarks();
				layout();
				return true;
			}
			chosen = song.path;
			SongPlayer.play( song.path );
			GLog.p( Messages.get(WndSongs.class, "playing", song.name) );
			return true;
		}
	}

	private static LinkedHashMap<String, ArrayList<Song>> songCache = null;
	private static LinkedHashMap<String, Song> pathCache = null;

	/** Every song by its path, for showing songs by name. Built once, like the list itself. */
	public static LinkedHashMap<String, Song> byPath() {
		if (pathCache == null) {
			pathCache = new LinkedHashMap<>();
			for (ArrayList<Song> g : songs().values()) for (Song s : g) pathCache.put( s.path, s );
		}
		return pathCache;
	}

	/** A song's name, or its file name when it is not in the list. */
	public static String nameOf( String path ) {
		Song s = byPath().get( path );
		return s != null ? s.name : pretty( path );
	}

	//every song, grouped by the mod it came from. Building it looks at every music file and makes
	//a level for every world, which stalled the game long enough to cut the music, so it is built once
	static LinkedHashMap<String, ArrayList<Song>> songs() {
		if (songCache == null) songCache = buildSongs();
		return songCache;
	}

	private static LinkedHashMap<String, ArrayList<Song>> buildSongs() {
		LinkedHashMap<String, ArrayList<Song>> groups = new LinkedHashMap<>();
		groups.put( SHATTERED, new ArrayList<Song>() );
		HashSet<String> seen = new HashSet<>();

		for (Field f : Assets.Music.class.getFields()) {
			if (f.getType() != String.class || !Modifier.isStatic( f.getModifiers() )) continue;
			try {
				String path = (String) f.get( null );
				if (path == null || !path.startsWith("music/") || !seen.add( path )) continue;
				if (!Gdx.files.internal( path ).exists()) continue;
				String mod = path.contains("devoted") ? DEVOTED : path.contains("/ryuse/") ? "Ryuse's Pixel Dungeon" : SHATTERED;
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
