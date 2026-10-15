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

	public enum Shuffle { OFF, ALL, LIKED }

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
		current = path;
		Music.INSTANCE.play( path, true );
	}

	/** Plays random songs one after another until another song or the floor's own music is chosen. */
	public static void startShuffle( Shuffle mode, ArrayList<Song> pool ){
		shuffle = mode;
		pool = new ArrayList<>( pool );
		if (pool.isEmpty()) {
			shuffle = Shuffle.OFF;
			return;
		}
		next( pool );
	}

	private static void next( final ArrayList<Song> pool ){
		if (shuffle == Shuffle.OFF) return;
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
	}
}
