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

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.WorldShuffle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;

/**
 * The intro shown on arriving at a floor built in a ported world. The source mod's icon
 * fades in with the world's name and the mod's name under it, holds, then fades away.
 */
public class ModIntro extends Component {

	private static final float FADE_IN  = 0.6f;
	private static final float HOLD     = 2.2f;
	private static final float FADE_OUT = 1.0f;

	private static final int LOGO_SIZE = 32;

	private Image logo;
	private RenderedTextBlock world;
	private RenderedTextBlock mod;

	private float time = 0;

	public ModIntro( Level level ) {
		super();

		logo = new Image( WorldShuffle.modLogo(level) );
		logo.scale.set( LOGO_SIZE / logo.height );
		add( logo );

		world = PixelScene.renderTextBlock( Messages.titleCase(WorldShuffle.menuName(WorldShuffle.worldOf(level))), 9 );
		world.hardlight( Window.TITLE_COLOR );
		add( world );

		mod = PixelScene.renderTextBlock( Messages.get(this, "from", Messages.get(level.getClass(), "mod")), 7 );
		add( mod );

		width = Math.max( LOGO_SIZE, Math.max( world.width(), mod.width() ) );
		height = LOGO_SIZE + 3 + world.height() + 2 + mod.height();

		setAlpha( 0 );
	}

	@Override
	protected void layout() {
		logo.x = x + (width - LOGO_SIZE) / 2f;
		logo.y = y;
		PixelScene.align( logo );

		world.setPos( x + (width - world.width()) / 2f, y + LOGO_SIZE + 3 );
		PixelScene.align( world );

		mod.setPos( x + (width - mod.width()) / 2f, world.bottom() + 2 );
		PixelScene.align( mod );
	}

	private void setAlpha( float alpha ) {
		logo.am = alpha;
		world.alpha( alpha );
		mod.alpha( alpha );
	}

	@Override
	public void update() {
		super.update();

		time += Game.elapsed;
		if (time < FADE_IN) {
			setAlpha( time / FADE_IN );
		} else if (time < FADE_IN + HOLD) {
			setAlpha( 1 );
		} else if (time < FADE_IN + HOLD + FADE_OUT) {
			setAlpha( 1 - (time - FADE_IN - HOLD) / FADE_OUT );
		} else {
			killAndErase();
			destroy();
		}
	}

}
