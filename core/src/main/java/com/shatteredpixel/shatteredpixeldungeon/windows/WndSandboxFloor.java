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
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.WorldShuffle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollingListPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Game;

/**
 * The floor and world part of the sandbox panel, opened from WndSandbox.
 * Skips to any floor and forces the world that new floors are built in.
 */
public class WndSandboxFloor extends Window {

	private static final int WIDTH      = 124;
	private static final int GAP        = 2;
	private static final int FLOORS     = 26;
	private static final int COLS       = 7;
	private static final int FLOOR_SIZE = 16;
	private static final int BTN_HEIGHT = 18;

	private int floor;

	private RedButton[] floorBtns = new RedButton[FLOORS];
	private RedButton btnWorld;
	private RedButton btnMods;
	private RenderedTextBlock note;
	private RedButton btnGo;

	public WndSandboxFloor() {

		super();

		floor = Dungeon.depth;

		RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(WndSandbox.class, "floors"), 12 );
		title.hardlight( TITLE_COLOR );
		title.setPos( (WIDTH - title.width()) / 2, 0 );
		PixelScene.align(title);
		add( title );

		RenderedTextBlock floorLabel = PixelScene.renderTextBlock( Messages.get(WndSandbox.class, "floor"), 7 );
		floorLabel.setPos( 0, title.bottom() + 2*GAP );
		add( floorLabel );

		float top = floorLabel.bottom() + GAP;
		for (int i = 0; i < FLOORS; i++) {
			final int depth = i + 1;
			floorBtns[i] = new RedButton( Integer.toString(depth), 7 ) {
				@Override
				protected void onClick() {
					floor = depth;
					refresh();
				}
			};
			floorBtns[i].setRect(
					(i % COLS) * (FLOOR_SIZE + GAP),
					top + (i / COLS) * (FLOOR_SIZE + GAP),
					FLOOR_SIZE, FLOOR_SIZE );
			add( floorBtns[i] );
		}
		float pos = top + ((FLOORS - 1) / COLS + 1) * (FLOOR_SIZE + GAP);

		RenderedTextBlock worldLabel = PixelScene.renderTextBlock( Messages.get(WndSandbox.class, "world"), 7 );
		worldLabel.setPos( 0, pos + GAP );
		add( worldLabel );

		btnWorld = new RedButton( "", 8 ) {
			@Override
			protected void onClick() {
				GameScene.show( new WndWorlds() );
			}
		};
		btnWorld.setRect( 0, worldLabel.bottom() + GAP, WIDTH, BTN_HEIGHT );
		add( btnWorld );

		btnMods = new RedButton( "", 8 ) {
			@Override
			protected void onClick() {
				GameScene.show( new WndMods() );
			}
		};
		btnMods.setRect( 0, btnWorld.bottom() + GAP, WIDTH, BTN_HEIGHT );
		add( btnMods );

		note = PixelScene.renderTextBlock( 6 );
		add( note );

		btnGo = new RedButton( "" ) {
			@Override
			protected void onClick() {
				hide();
				Level.beforeTransition();
				InterlevelScene.mode = InterlevelScene.Mode.SANDBOX;
				InterlevelScene.returnDepth = floor;
				Game.switchScene( InterlevelScene.class );
			}
		};
		btnGo.textColor( TITLE_COLOR );
		add( btnGo );

		refresh();
	}

	private static String worldLabel( int world ){
		return world == -1
				? Messages.get(WndSandbox.class, "shuffle")
				: Messages.titleCase(WorldShuffle.menuName(world));
	}

	private void refresh() {
		for (int i = 0; i < FLOORS; i++) {
			floorBtns[i].textColor( i + 1 == floor ? TITLE_COLOR : 0xFFFFFF );
		}

		btnWorld.text( worldLabel(Dungeon.sandboxWorld) );
		btnWorld.textColor( Dungeon.sandboxWorld == -1 ? 0xFFFFFF : TITLE_COLOR );

		if (Dungeon.sandboxWorld != -1 && WorldShuffle.modBossLevel(Dungeon.sandboxWorld, floor) != null) {
			note.text( Messages.get(WndSandbox.class, "note_boss", floor, WorldShuffle.modName(Dungeon.sandboxWorld)), WIDTH );
		} else if (Dungeon.sandboxWorld != -1 && !WorldShuffle.canForce(floor)) {
			note.text( Messages.get(WndSandbox.class, "note_fixed", floor), WIDTH );
		} else {
			note.text( Messages.get(WndSandbox.class, "note"), WIDTH );
		}
		int mods = WorldShuffle.mods().size();
		int on = mods;
		for (String mod : WorldShuffle.mods()) {
			if (Dungeon.sandboxOffMods.contains( mod )) on--;
		}
		btnMods.text( Messages.get(WndSandbox.class, "mods_count", on, mods) );
		btnMods.textColor( on == mods ? 0xFFFFFF : TITLE_COLOR );

		note.setPos( 0, btnMods.bottom() + 2*GAP );

		btnGo.text( Messages.get(WndSandbox.class, "go", floor) );
		btnGo.setRect( 0, note.bottom() + 2*GAP, WIDTH, BTN_HEIGHT );

		resize( WIDTH, (int)btnGo.bottom() );
	}

	//the list of worlds to force, grouped by the mod each comes from
	private class WndWorlds extends Window {

		private static final int LIST_WIDTH = 126;

		public WndWorlds() {

			super();

			int height = (int)Math.min( 200, PixelScene.uiCamera.height - 40 );

			RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(WndSandbox.class, "pick"), 12 );
			title.hardlight( TITLE_COLOR );
			title.setPos( (LIST_WIDTH - title.width()) / 2, 0 );
			PixelScene.align(title);
			add( title );

			float top = title.bottom() + GAP;

			//the window has to be at its final size before the list is placed, or the list draws off centre
			resize( LIST_WIDTH, height );

			ScrollingListPane list = new ScrollingListPane();
			add( list );
			list.setRect( 0, top, LIST_WIDTH, height - top );

			addWorld( list, -1 );

			String lastMod = null;
			for (int world = 0; world < WorldShuffle.worldCount(); world++) {
				String mod = WorldShuffle.modName( world );
				if (!mod.equals( lastMod )) {
					list.addTitle( mod );
					lastMod = mod;
				}
				addWorld( list, world );
			}

			list.setRect( 0, top, LIST_WIDTH, height - top );
		}

		private void addWorld( ScrollingListPane list, final int world ){
			ScrollingListPane.ListItem item = new ScrollingListPane.ListItem( null, worldLabel(world) ) {
				@Override
				public boolean onClick( float x, float y ) {
					if (inside( x, y )) {
						Dungeon.sandboxWorld = world;
						WndWorlds.this.hide();
						refresh();
						return true;
					}
					return false;
				}
			};
			if (world == Dungeon.sandboxWorld) item.hardlight( TITLE_COLOR );
			list.addItem( item );
		}
	}

	//switches each mod's worlds in or out of the shuffle for floors built from now on
	private class WndMods extends Window {

		private static final int LIST_WIDTH = 140;

		public WndMods() {

			super();

			int height = (int)Math.min( 200, PixelScene.uiCamera.height - 40 );

			RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(WndSandbox.class, "mods_title"), 12 );
			title.hardlight( TITLE_COLOR );
			title.setPos( (LIST_WIDTH - title.width()) / 2, 0 );
			PixelScene.align(title);
			add( title );

			float top = title.bottom() + GAP;

			//the window has to be at its final size before the list is placed, or the list draws off centre
			resize( LIST_WIDTH, height );

			ScrollingListPane list = new ScrollingListPane();
			add( list );
			list.setRect( 0, top, LIST_WIDTH, height - top );

			list.addItem( new ScrollingListPane.ListItem( Icons.get(Icons.CHECKED), Messages.get(WndSandbox.class, "mods_all_on") ) {
				@Override
				public boolean onClick( float x, float y ) {
					if (!inside( x, y )) return false;
					Dungeon.sandboxOffMods.clear();
					reopen();
					return true;
				}
			} );
			list.addItem( new ScrollingListPane.ListItem( Icons.get(Icons.UNCHECKED), Messages.get(WndSandbox.class, "mods_all_off") ) {
				@Override
				public boolean onClick( float x, float y ) {
					if (!inside( x, y )) return false;
					Dungeon.sandboxOffMods.addAll( WorldShuffle.mods() );
					reopen();
					return true;
				}
			} );

			for (final String mod : WorldShuffle.mods()) {
				final boolean on = !Dungeon.sandboxOffMods.contains( mod );
				ScrollingListPane.ListItem item = new ScrollingListPane.ListItem( Icons.get(on ? Icons.CHECKED : Icons.UNCHECKED), mod ) {
					@Override
					public boolean onClick( float x, float y ) {
						if (!inside( x, y )) return false;
						if (on) Dungeon.sandboxOffMods.add( mod );
						else    Dungeon.sandboxOffMods.remove( mod );
						reopen();
						return true;
					}
				};
				list.addItem( item );

				list.addItem( new ScrollingListPane.ListItem( Icons.get(Icons.CHECKED), Messages.get(WndSandbox.class, "mods_only", mod) ) {
					@Override
					public boolean onClick( float x, float y ) {
						if (!inside( x, y )) return false;
						Dungeon.sandboxOffMods.addAll( WorldShuffle.mods() );
						Dungeon.sandboxOffMods.remove( mod );
						reopen();
						return true;
					}
				} );
			}

			list.setRect( 0, top, LIST_WIDTH, height - top );
		}

		private void reopen() {
			hide();
			refresh();
			GameScene.show( new WndMods() );
		}
	}

}
