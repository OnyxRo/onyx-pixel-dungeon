/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Seed radar from Shattered Pixel Dungeon Radar, ported into Onyx's Pixel Dungeon
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
import com.shatteredpixel.shatteredpixeldungeon.RadarSearch;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Game;

//shows the seed search while it runs on its own thread, then starts the run with the seed it found
public class WndRadarSearch extends Window {

	private static final int WIDTH = 120;

	private final RadarSearch search = new RadarSearch();
	private final RenderedTextBlock text;
	private volatile boolean done = false;
	private volatile long found = -1;
	private boolean failedShown = false;

	public WndRadarSearch(){
		super();

		text = PixelScene.renderTextBlock( 6 );
		text.maxWidth( WIDTH );
		add( text );
		text.text( Messages.get(this, "searching", 0), WIDTH );

		RedButton cancel = new RedButton( Messages.get(this, "cancel") ){
			@Override
			protected void onClick() {
				search.cancelled = true;
				hide();
			}
		};
		cancel.setRect( 0, 30, WIDTH, 18 );
		add( cancel );
		resize( WIDTH, 48 );

		Thread t = new Thread( new Runnable() {
			@Override
			public void run() {
				try {
					found = search.search();
				} catch (Throwable e){
					found = -1;
				}
				done = true;
			}
		});
		t.setDaemon( true );
		t.start();
	}

	@Override
	public void update() {
		super.update();
		if (!done){
			text.text( Messages.get(this, "searching", search.checked), WIDTH );
			return;
		}
		if (search.cancelled) return;
		if (found >= 0){
			search.cancelled = true;
			hide();
			Dungeon.hero = null;
			Dungeon.daily = Dungeon.dailyReplay = false;
			Dungeon.seed = found;
			Dungeon.customSeedText = "";
			ActionIndicator.clearAction();
			InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
			Game.switchScene( InterlevelScene.class );
		} else if (!failedShown){
			failedShown = true;
			text.text( Messages.get(this, "failed", RadarSearch.MAX_SEEDS), WIDTH );
		}
	}

	@Override
	public void onBackPressed() {
		search.cancelled = true;
		super.onBackPressed();
	}
}
