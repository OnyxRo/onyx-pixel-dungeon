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
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.TerrainFeaturesTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollingListPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

/**
 * The trap part of the sandbox panel, the one piece of Sandbox PD's level editor that fits a normal run.
 * Lists every trap in the journal and sets the one clicked on an open floor cell next to the hero.
 */
public class WndSandboxTraps extends Window {

	private static final int WIDTH = 126;
	private static final int GAP   = 2;

	public WndSandboxTraps() {

		super();

		int height = (int)Math.min( 200, PixelScene.uiCamera.height - 40 );

		RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(WndSandbox.class, "traps_title"), 12 );
		title.hardlight( TITLE_COLOR );
		title.setPos( (WIDTH - title.width()) / 2, 0 );
		PixelScene.align(title);
		add( title );

		float top = title.bottom() + GAP;

		resize( WIDTH, height );

		ScrollingListPane list = new ScrollingListPane();
		add( list );
		list.setRect( 0, top, WIDTH, height - top );

		for (final Class<?> cls : Bestiary.TRAP.entities()) {
			if (!Trap.class.isAssignableFrom(cls)) continue;
			Image icon;
			String name;
			try {
				Trap sample = (Trap) Reflection.newInstance(cls);
				icon = TerrainFeaturesTilemap.getTrapVisual(sample);
				name = Messages.titleCase(sample.name());
			} catch (Exception e) {
				continue;
			}
			if (icon == null) continue;

			list.addItem( new ScrollingListPane.ListItem( icon, name ) {
				@Override
				public boolean onClick( float x, float y ) {
					if (inside( x, y )) {
						place( cls );
						return true;
					}
					return false;
				}
			} );
		}

		list.setRect( 0, top, WIDTH, height - top );
	}

	private static boolean plainFloor( int terrain ){
		return terrain == Terrain.EMPTY || terrain == Terrain.EMPTY_DECO || terrain == Terrain.EMPTY_SP
				|| terrain == Terrain.GRASS || terrain == Terrain.EMBERS;
	}

	/** Sets a revealed trap of this kind on a free floor cell next to the hero. Returns the cell, or -1. */
	public static int place( Class<?> cls ) {
		ArrayList<Integer> candidates = new ArrayList<>();
		for (int offset : PathFinder.NEIGHBOURS8) {
			int cell = Dungeon.hero.pos + offset;
			if (cell < 0 || cell >= Dungeon.level.length()) continue;
			if (plainFloor( Dungeon.level.map[cell] ) && Actor.findChar( cell ) == null
					&& Dungeon.level.heaps.get( cell ) == null && Dungeon.level.traps.get( cell ) == null) {
				candidates.add( cell );
			}
		}
		if (candidates.isEmpty()) {
			GLog.w( Messages.get(WndSandbox.class, "no_room") );
			return -1;
		}

		Trap trap = (Trap) Reflection.newInstance(cls);
		if (trap == null) return -1;
		int cell = Random.element( candidates );
		Dungeon.level.setTrap( trap, cell );
		Level.set( cell, Terrain.TRAP );
		trap.reveal();
		if (com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon.scene() instanceof GameScene) GameScene.updateMap( cell );

		GLog.p( Messages.get(WndSandbox.class, "trap_set", Messages.titleCase(trap.name())) );
		return cell;
	}

}
