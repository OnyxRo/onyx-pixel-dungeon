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
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.WorldShuffle;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.WorldLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollingListPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Music;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;

/**
 * A compendium of every song in the game, grouped by the mod it came from.
 * Clicking a song plays it and keeps it playing, floor after floor, until another song
 * is picked or the floor's own music is chosen again.
 */
public class WndSongs extends Window {

	private static final int WIDTH = 150;
	private static final int GAP   = 2;

	private static final String SHATTERED = "Shattered Pixel Dungeon";
	private static final String DEVOTED   = "Devoted Pixel Dungeon";

	/** The song picked from the list, or null while each floor plays its own music. */
	public static String chosen = null;

	/** Called whenever a floor starts its music, so a picked song carries on. */
	public static void applyChoice(){
		if (chosen != null) Music.INSTANCE.play( chosen, true );
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

		//the window has to be at its final size before the list is placed, or the list draws off centre
		resize( WIDTH, height );

		ScrollingListPane list = new ScrollingListPane();
		add( list );
		list.setRect( 0, top, WIDTH, height - top );

		list.addItem( new ScrollingListPane.ListItem( Icons.get(Icons.REPEAT), Messages.get(this, "floor_music") ) {
			@Override
			public boolean onClick( float x, float y ) {
				if (!inside( x, y )) return false;
				chosen = null;
				if (Dungeon.level != null) Dungeon.level.playLevelMusic();
				GLog.i( Messages.get(WndSongs.class, "floor_music_msg") );
				return true;
			}
		} );

		for (java.util.Map.Entry<String, ArrayList<String[]>> group : songs().entrySet()) {
			list.addTitle( group.getKey().replace(" Pixel Dungeon", "") );
			for (final String[] song : group.getValue()) {
				ScrollingListPane.ListItem item = new ScrollingListPane.ListItem( Icons.get(Icons.AUDIO), song[1] ) {
					@Override
					public boolean onClick( float x, float y ) {
						if (!inside( x, y )) return false;
						chosen = song[0];
						Music.INSTANCE.play( song[0], true );
						GLog.p( Messages.get(WndSongs.class, "playing", song[1]) );
						return true;
					}
				};
				if (song[0].equals( chosen )) item.hardlight( TITLE_COLOR );
				list.addItem( item );
			}
		}

		list.setRect( 0, top, WIDTH, height - top );
	}

	//song path and display name, grouped by mod
	private static LinkedHashMap<String, ArrayList<String[]>> songs() {
		LinkedHashMap<String, ArrayList<String[]>> groups = new LinkedHashMap<>();
		groups.put( SHATTERED, new ArrayList<String[]>() );
		HashSet<String> seen = new HashSet<>();

		for (Field f : Assets.Music.class.getFields()) {
			if (f.getType() != String.class || !Modifier.isStatic( f.getModifiers() )) continue;
			try {
				String path = (String) f.get( null );
				if (path == null || !path.startsWith("music/") || !seen.add( path )) continue;
				if (!Gdx.files.internal( path ).exists()) continue;
				String mod = path.contains("devoted") ? DEVOTED : SHATTERED;
				if (!groups.containsKey( mod )) groups.put( mod, new ArrayList<String[]>() );
				groups.get( mod ).add( new String[]{ path, pretty( path ) } );
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
			if (!groups.containsKey( mod )) groups.put( mod, new ArrayList<String[]>() );
			groups.get( mod ).add( new String[]{ path, Messages.get(WndSongs.class, "theme", Messages.titleCase(WorldShuffle.menuName(world))) } );
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
