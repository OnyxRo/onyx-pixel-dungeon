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

import com.shatteredpixel.shatteredpixeldungeon.levels.WorldShuffle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollingListPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

/**
 * A quick catalog of what each mod contributes to the game, mod by mod.
 * Worlds come straight from WorldShuffle, so new ones show up here on their own.
 */
public class WndCompendium extends Window {

	private static final int WIDTH = 140;
	private static final int GAP   = 2;

	private static final String SHATTERED = "Shattered Pixel Dungeon";
	private static final String DEVOTED   = "Devoted Pixel Dungeon";

	public WndCompendium() {

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

		java.util.ArrayList<String> mods = WorldShuffle.mods();
		mods.add( 0, SHATTERED );

		for (String mod : mods) {
			int count = 0;
			for (int world = 0; world < WorldShuffle.worldCount(); world++) {
				if (WorldShuffle.modName(world).equals(mod)) count++;
			}
			list.addTitle( Messages.get(this, "worlds", mod, count) );

			for (int world = 0; world < WorldShuffle.worldCount(); world++) {
				if (!WorldShuffle.modName(world).equals(mod)) continue;

				int region = WorldShuffle.regionOf(world);
				String stage = Messages.get(this, region == WorldShuffle.ANY_REGION ? "stage_any" : "stage_" + region);
				String logo = WorldShuffle.modLogo(world);
				Image icon = logo != null ? new Image(logo) : Icons.get(Icons.STAIRS);
				final float scale = logo != null ? 16f / icon.height : 1f;

				list.addItem( new ScrollingListPane.ListItem( icon, null,
						Messages.get(this, "world", Messages.titleCase(WorldShuffle.menuName(world)), stage) ) {
					{
						//mod icons are stored at twice the size of a list icon
						this.icon.scale.set( scale );
					}
				} );
			}

			if (mod.equals(SHATTERED)) {
				list.addItem( Icons.get(Icons.INFO), null, Messages.get(this, "shattered") );
			} else if (mod.equals(DEVOTED)) {
				list.addItem( Icons.get(Icons.SKULL), null, Messages.get(this, "devoted_mobs") );
				list.addItem( Icons.get(Icons.BACKPACK), null, Messages.get(this, "devoted_weapons") );
			}
		}

		list.setRect( 0, top, WIDTH, height - top );
	}

}
