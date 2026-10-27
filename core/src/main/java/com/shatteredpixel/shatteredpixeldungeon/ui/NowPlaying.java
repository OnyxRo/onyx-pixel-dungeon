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

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SongPlayer;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndNowPlaying;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndSongs;
import com.watabou.noosa.Image;

/**
 * The song playing now, shown at the top left under the mod logo. Clicking it opens
 * WndNowPlaying, to like, ban, skip or add the song to a playlist mid game.
 */
public class NowPlaying extends Button {

	private static final int MAX_W = 110;

	private Image icon;
	private RenderedTextBlock name;
	private String shown = "";

	public NowPlaying() {
		super();
		refresh();
	}

	@Override
	protected void createChildren() {
		super.createChildren();
		icon = Icons.get( Icons.AUDIO );
		icon.scale.set( 0.6f );
		add( icon );
		name = PixelScene.renderTextBlock( 6 );
		add( name );
	}

	@Override
	protected void layout() {
		super.layout();
		icon.x = x;
		icon.y = y + (height - icon.height()) / 2f;
		PixelScene.align( icon );
		name.setPos( icon.x + icon.width() + 2, y + (height - name.height()) / 2f );
		PixelScene.align( name );
	}

	private void refresh() {
		String now = SongPlayer.nowPlaying();
		String text = now == null ? "" : WndSongs.nameOf( now );
		if (text.equals( shown )) return;
		shown = text;
		name.text( text );
		name.maxWidth( MAX_W );
		visible = !text.isEmpty();
		layout();
	}

	@Override
	public void update() {
		super.update();
		refresh();
	}

	@Override
	protected void onClick() {
		String now = SongPlayer.nowPlaying();
		if (now != null && Dungeon.hero != null && Dungeon.hero.isAlive()) {
			GameScene.show( new WndNowPlaying( now ) );
		}
	}

	@Override
	protected void onPointerDown() {
		name.alpha( 0.6f );
	}

	@Override
	protected void onPointerUp() {
		name.alpha( 1f );
	}
}
