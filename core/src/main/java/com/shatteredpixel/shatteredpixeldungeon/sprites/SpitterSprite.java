/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Pixel Dungeon Reforged
 * Copyright (C) 2024-2025 Nathan Pringle
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

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class SpitterSprite extends MobSprite {

	public SpitterSprite() {
		super();

		perspectiveRaise = 0f;

		texture( Assets.Sprites.SPITTER );

		TextureFilm frames = new TextureFilm( texture, 16, 16 );

		idle = new Animation( 10, true );
		idle.frames( frames, 0, 0, 0, 0, 0, 1, 0, 1 );

		run = new Animation( 15, true );
		run.frames( frames, 0, 2, 0, 3 );

		attack = new Animation( 12, false );
		attack.frames( frames, 0, 4, 5, 0 );

		die = new Animation( 12, false );
		die.frames( frames, 6, 7, 8, 9 );

		play( idle );
	}

	@Override
	public void link( Char ch ) {
		super.link( ch );
		if (parent != null) {
			parent.sendToBack( this );
			if (aura != null) {
				parent.sendToBack( aura );
			}
		}
		renderShadow = false;
	}

	@Override
	public int blood() {
		return 0xFFFF4422;
	}

	@Override
	public void attack( int cell ) {
		if (!Dungeon.level.adjacent( cell, ch.pos )) {

			((MissileSprite)parent.recycle( MissileSprite.class )).
					reset( this, cell, new SpitterShot(), new Callback() {
						@Override
						public void call() {
							ch.onAttackComplete();
						}
					} );

			play( attack );
			turnTo( ch.pos, cell );

		} else {
			super.attack( cell );
		}
	}

	public static class SpitterShot extends Item {
		{
			image = ItemSpriteSheet.FISHING_SPEAR;
		}
	}
}
