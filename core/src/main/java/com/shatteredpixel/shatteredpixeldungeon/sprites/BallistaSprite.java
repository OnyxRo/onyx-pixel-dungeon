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
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class BallistaSprite extends MobSprite {

	public BallistaSprite() {
		this( 0 );
	}

	protected BallistaSprite( int o ) {
		super();

		texture( Assets.Sprites.BALLISTA );

		TextureFilm frames = new TextureFilm( texture, 16, 16 );

		idle = new Animation( 2, true );
		idle.frames( frames, 0+o, 0+o, 0+o, 1+o );

		run = new Animation( 2, true );
		run.frames( frames, 0+o, 2+o );

		attack = new Animation( 8, false );
		attack.frames( frames, 0+o, 2+o, 3+o );

		die = new Animation( 12, false );
		die.frames( frames, 4+o, 5+o, 6+o );

		play( idle );
	}

	@Override
	public void attack( int cell ) {
		if (!Dungeon.level.adjacent( cell, ch.pos )) {

			((MissileSprite)parent.recycle( MissileSprite.class )).
					reset( this, cell, new BallistaShot(), new Callback() {
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

	@Override
	public void onComplete( Animation anim ) {
		if (anim == die) {
			emitter().burst( ElmoParticle.FACTORY, 4 );
		}
		super.onComplete( anim );
	}

	public static class BallistaShot extends Item {
		{
			image = ItemSpriteSheet.FISHING_SPEAR;
		}
	}
}
