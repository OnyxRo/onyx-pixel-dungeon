/*
 * Onyx's Pixel Dungeon
 * Based on Shattered Pixel Dungeon, Copyright (C) 2014-2025 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.levels.SpecialFloors;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollingListPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

/**
 * Lists the levels from mods that are not part of a normal run, grouped by mod. Clicking one builds it
 * and moves the hero there. See SpecialFloors.
 */
public class WndSpecialFloors extends Window {

	private static final int WIDTH = 140;
	private static final int GAP = 2;

	public WndSpecialFloors() {

		super();

		int height = (int)Math.min( 200, PixelScene.uiCamera.height - 40 );

		RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(WndSandbox.class, "special_title"), 12 );
		title.hardlight( TITLE_COLOR );
		title.setPos( (WIDTH - title.width()) / 2, 0 );
		PixelScene.align( title );
		add( title );

		float top = title.bottom() + GAP;

		//the window has to be at its final size before the list is placed, or the list draws off centre
		resize( WIDTH, height );

		ScrollingListPane list = new ScrollingListPane();
		add( list );
		list.setRect( 0, top, WIDTH, height - top );

		String lastMod = null;
		for (final SpecialFloors.Entry entry : SpecialFloors.LIST) {
			if (!entry.mod.equals( lastMod )) {
				list.addTitle( entry.mod );
				lastMod = entry.mod;
			}
			list.addItem( new ScrollingListPane.ListItem( null, entry.label ) {
				@Override
				public boolean onClick( float x, float y ) {
					if (!inside( x, y )) return false;
					WndSpecialFloors.this.hide();
					SpecialFloors.travel( entry.make );
					return true;
				}
			} );
		}

		list.setRect( 0, top, WIDTH, height - top );
	}
}
