/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Required items window from Shattered Pixel Dungeon Radar, ported into Onyx's Pixel Dungeon
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

import com.shatteredpixel.shatteredpixeldungeon.RadarSearch;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

//names of items the run must find by floor 1, 2, 5 and 10. Starting a run then tries seeds until one has them all.
public class WndRadarItems extends Window {

	private static final int WIDTH      = 130;
	private static final int BTN_HEIGHT = 18;
	private static final int GAP        = 2;

	private final RedButton[] buttons = new RedButton[RadarSearch.FLOORS.length];

	public WndRadarItems(){
		super();

		RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(this, "title"), 9 );
		title.hardlight( TITLE_COLOR );
		title.setPos( (WIDTH - title.width()) / 2, 0 );
		PixelScene.align( title );
		add( title );

		RenderedTextBlock desc = PixelScene.renderTextBlock( Messages.get(this, "desc", RadarSearch.MAX_SEEDS), 6 );
		desc.maxWidth( WIDTH );
		desc.setPos( 0, title.bottom() + GAP );
		add( desc );

		float pos = desc.bottom() + 2*GAP;
		for (int i = 0; i < RadarSearch.FLOORS.length; i++){
			final int slot = i;
			buttons[i] = new RedButton( "" ){
				@Override
				protected void onClick() {
					final int floor = RadarSearch.FLOORS[slot];
					ShatteredPixelDungeon.scene().addToFront( new WndTextInput(
							Messages.get(WndRadarItems.class, "by_floor", floor),
							Messages.get(WndRadarItems.class, "input_desc"),
							RadarSearch.items(slot), 200, true,
							Messages.get(WndRadarItems.class, "save"),
							Messages.get(WndRadarItems.class, "cancel")){
						@Override
						public void onSelect( boolean positive, String text ) {
							if (positive){
								RadarSearch.items(slot, text);
								refresh();
							}
						}
					});
				}
			};
			buttons[i].multiline = true;
			buttons[i].setRect( 0, pos, WIDTH, BTN_HEIGHT );
			add( buttons[i] );
			pos = buttons[i].bottom() + GAP;
		}

		RedButton clear = new RedButton( Messages.get(this, "clear") ){
			@Override
			protected void onClick() {
				for (int i = 0; i < RadarSearch.FLOORS.length; i++) RadarSearch.items(i, "");
				refresh();
			}
		};
		clear.setRect( 0, pos + GAP, WIDTH, BTN_HEIGHT );
		add( clear );

		resize( WIDTH, (int) clear.bottom() );
		refresh();
	}

	private void refresh(){
		for (int i = 0; i < buttons.length; i++){
			String items = RadarSearch.items(i).trim().replace("\n", ", ");
			String floor = Messages.get(this, "by_floor", RadarSearch.FLOORS[i]);
			buttons[i].text( items.isEmpty() ? floor : floor + " " + items );
		}
	}
}
