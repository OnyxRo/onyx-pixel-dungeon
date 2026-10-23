/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dobrokhotov
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.watabou.glwrap.Blending;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.utils.PointF;

/** Blue Archive PD's silent shot ray for the sniper rifle's piercing shot (its Beam.ShotRay). */
public class BAShotRay extends Image {

	private static final double A = 180 / Math.PI;
	private float duration;
	private float timeLeft;

	public BAShotRay( PointF s, PointF e ){
		super( "effects/bluearchive_shotray.png" );
		origin.set( 0, height / 2 );
		x = s.x - origin.x;
		y = s.y - origin.y;
		float dx = e.x - s.x;
		float dy = e.y - s.y;
		angle = (float)(Math.atan2( dy, dx ) * A);
		scale.x = (float)Math.sqrt( dx * dx + dy * dy ) / width;
		timeLeft = duration = 0.5f;
	}

	@Override
	public void update() {
		super.update();
		float p = timeLeft / duration;
		alpha( p );
		scale.set( scale.x, p );
		if ((timeLeft -= Game.elapsed) <= 0) {
			killAndErase();
		}
	}

	@Override
	public void draw() {
		Blending.setLightMode();
		super.draw();
		Blending.setNormalMode();
	}
}
