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

package com.shatteredpixel.shatteredpixeldungeon;

import com.badlogic.gdx.Gdx;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 * Every song from every mod, the songs Onyx has liked, and shuffle autoplay.
 * Songs from mods are listed in music/mods/index.txt, one per line as path, mod, name.
 */
public class SongPlayer {

	public static class Song {
		public final String path, mod, name;
		public Song( String path, String mod, String name ){
			this.path = path; this.mod = mod; this.name = name;
		}
	}

	public enum Shuffle { OFF, ALL, LIKED, PLAYLIST, IN_ORDER }

	private static final String KEY_LIKED = "liked_songs";

	/** The song chosen by hand or by shuffle, or null while each floor plays its own music. */
	public static String current = null;
	public static Shuffle shuffle = Shuffle.OFF;

	private static LinkedHashSet<String> liked = null;
	private static ArrayList<Song> modSongs = null;

	// liked songs

	private static LinkedHashSet<String> likedSet(){
		if (liked == null) {
			liked = new LinkedHashSet<>();
			String saved = SPDSettings.getString( KEY_LIKED, "", 200000 );
			for (String p : saved.split( "\\|" )) {
				if (!p.isEmpty()) liked.add( p );
			}
		}
		return liked;
	}

	public static boolean isLiked( String path ){
		return likedSet().contains( path );
	}

	public static void toggleLiked( String path ){
		if (!likedSet().remove( path )) likedSet().add( path );
		StringBuilder sb = new StringBuilder();
		for (String p : likedSet()) sb.append( p ).append( '|' );
		SPDSettings.put( KEY_LIKED, sb.toString() );
	}

	// playlists, each a name and its songs in order, kept in the settings file

	private static final String KEY_PLAYLISTS = "song_playlists";
	private static final String KEY_PLAYLIST  = "song_playlist_";

	private static LinkedHashMap<String, ArrayList<String>> playlists = null;

	private static LinkedHashMap<String, ArrayList<String>> playlistMap(){
		if (playlists == null) {
			playlists = new LinkedHashMap<>();
			for (String name : SPDSettings.getString( KEY_PLAYLISTS, "", 200000 ).split( "\\|" )) {
				if (name.isEmpty()) continue;
				ArrayList<String> songs = new ArrayList<>();
				for (String p : SPDSettings.getString( KEY_PLAYLIST + name, "", 200000 ).split( "\\|" )) {
					if (!p.isEmpty() && !songs.contains( p )) songs.add( p );
				}
				playlists.put( name, songs );
			}
		}
		return playlists;
	}

	private static void savePlaylists(){
		StringBuilder names = new StringBuilder();
		for (String name : playlistMap().keySet()) {
			names.append( name ).append( '|' );
			savePlaylist( name );
		}
		SPDSettings.put( KEY_PLAYLISTS, names.toString() );
	}

	private static void savePlaylist( String name ){
		StringBuilder sb = new StringBuilder();
		ArrayList<String> songs = playlistMap().get( name );
		if (songs != null) for (String p : songs) sb.append( p ).append( '|' );
		SPDSettings.put( KEY_PLAYLIST + name, sb.toString() );
	}

	/** Names may not hold the separator, and are trimmed. */
	public static String cleanName( String name ){
		return name == null ? "" : name.replace( "|", "" ).trim();
	}

	public static ArrayList<String> playlistNames(){
		return new ArrayList<>( playlistMap().keySet() );
	}

	public static boolean hasPlaylist( String name ){
		return playlistMap().containsKey( name );
	}

	/** The songs of a playlist in order, empty if there is no such playlist. */
	public static ArrayList<String> playlist( String name ){
		ArrayList<String> songs = playlistMap().get( name );
		return songs == null ? new ArrayList<String>() : new ArrayList<>( songs );
	}

	/** Makes an empty playlist. False if the name is empty or taken. */
	public static boolean createPlaylist( String name ){
		name = cleanName( name );
		if (name.isEmpty() || playlistMap().containsKey( name )) return false;
		playlistMap().put( name, new ArrayList<String>() );
		savePlaylists();
		return true;
	}

	/** Gives a playlist a new name, keeping its place in the list. False if the new name is empty or taken. */
	public static boolean renamePlaylist( String from, String to ){
		to = cleanName( to );
		if (to.isEmpty() || !playlistMap().containsKey( from ) || playlistMap().containsKey( to )) return false;
		LinkedHashMap<String, ArrayList<String>> renamed = new LinkedHashMap<>();
		for (java.util.Map.Entry<String, ArrayList<String>> e : playlistMap().entrySet()) {
			renamed.put( e.getKey().equals( from ) ? to : e.getKey(), e.getValue() );
		}
		playlists = renamed;
		SPDSettings.put( KEY_PLAYLIST + from, "" );
		if (from.equals( playingList )) playingList = to;
		savePlaylists();
		return true;
	}

	public static void deletePlaylist( String name ){
		if (playlistMap().remove( name ) == null) return;
		SPDSettings.put( KEY_PLAYLIST + name, "" );
		savePlaylists();
	}

	public static boolean inPlaylist( String name, String path ){
		ArrayList<String> songs = playlistMap().get( name );
		return songs != null && songs.contains( path );
	}

	/** Adds the song to the end of the playlist, or takes it out if it is already there. */
	public static void togglePlaylist( String name, String path ){
		ArrayList<String> songs = playlistMap().get( name );
		if (songs == null) return;
		if (!songs.remove( path )) songs.add( path );
		savePlaylist( name );

		//Onyx's badges, a playlist with songs from 5 mods
		java.util.LinkedHashMap<String, Song> all = com.shatteredpixel.shatteredpixeldungeon.windows.WndSongs.byPath();
		ArrayList<String> mods = new ArrayList<>();
		for (String p : songs) {
			Song s = all.get( p );
			if (s != null) mods.add( s.mod );
		}
		Badges.validateMixtape( mods );
	}

	// songs from mods

	public static ArrayList<Song> modSongs(){
		if (modSongs == null) {
			modSongs = new ArrayList<>();
			try {
				String text = Gdx.files.internal( "music/mods/index.txt" ).readString( "UTF-8" );
				for (String line : text.split( "\r?\n" )) {
					String[] f = line.split( "\t" );
					if (f.length < 3 || !Gdx.files.internal( f[0] ).exists()) continue;
					modSongs.add( new Song( f[0], f[1], f[2] ) );
				}
			} catch (Exception e) {
				//no index means no mod songs, the list still shows the game's own
			}
		}
		return modSongs;
	}

	// playing

	/** Plays one song and keeps it going, floor after floor. Turns shuffle off. */
	public static void play( String path ){
		shuffle = Shuffle.OFF;
		playingList = null;
		current = path;
		Music.INSTANCE.play( path, true );
	}

	/** Plays random songs one after another until another song or the floor's own music is chosen. */
	public static void startShuffle( Shuffle mode, ArrayList<Song> pool ){
		shuffle = mode;
		playingList = null;
		pool = new ArrayList<>( pool );
		if (pool.isEmpty()) {
			shuffle = Shuffle.OFF;
			return;
		}
		next( pool );
	}

	/** The playlist playing now, or null. */
	public static String playingList = null;

	/**
	 * Plays a playlist from the song at start, in order or shuffled, looping back to the top.
	 * Songs added or removed while it plays are picked up at the next song.
	 */
	public static void playPlaylist( String name, int start, boolean shuffled ){
		ArrayList<String> songs = playlist( name );
		if (songs.isEmpty()) return;
		playingList = name;
		shuffle = shuffled ? Shuffle.PLAYLIST : Shuffle.IN_ORDER;
		int i = Math.max( 0, Math.min( start, songs.size() - 1 ) );
		if (shuffled && start < 0) i = Random.Int( songs.size() );
		playListSong( songs.get( i ) );
	}

	private static void playListSong( String path ){
		current = path;
		Music.INSTANCE.playOnce( path, new Runnable() {
			@Override
			public void run() {
				if ((shuffle != Shuffle.PLAYLIST && shuffle != Shuffle.IN_ORDER) || playingList == null) return;
				ArrayList<String> songs = playlist( playingList );
				if (songs.isEmpty()) {
					clear();
					return;
				}
				String pick;
				if (shuffle == Shuffle.PLAYLIST) {
					pick = songs.get( Random.Int( songs.size() ) );
					for (int tries = 0; tries < 5 && songs.size() > 1 && pick.equals( current ); tries++) {
						pick = songs.get( Random.Int( songs.size() ) );
					}
				} else {
					//a song taken out while it played sends the list back to the top
					pick = songs.get( (songs.indexOf( current ) + 1) % songs.size() );
				}
				playListSong( pick );
			}
		} );
	}

	private static void next( final ArrayList<Song> pool ){
		if (shuffle != Shuffle.ALL && shuffle != Shuffle.LIKED) return;
		Song pick = pool.get( Random.Int( pool.size() ) );
		if (pool.size() > 1) {
			for (int tries = 0; tries < 5 && pick.path.equals( current ); tries++) {
				pick = pool.get( Random.Int( pool.size() ) );
			}
		}
		current = pick.path;
		Music.INSTANCE.playOnce( pick.path, new Runnable() {
			@Override
			public void run() {
				//the liked list may have changed while a song played
				ArrayList<Song> now = pool;
				if (shuffle == Shuffle.LIKED) {
					ArrayList<Song> likedNow = new ArrayList<>();
					for (Song s : pool) if (isLiked( s.path )) likedNow.add( s );
					if (!likedNow.isEmpty()) now = likedNow;
				}
				next( now );
			}
		} );
	}

	/** Go back to each floor's own music. */
	public static void clear(){
		current = null;
		shuffle = Shuffle.OFF;
		playingList = null;
	}
}
