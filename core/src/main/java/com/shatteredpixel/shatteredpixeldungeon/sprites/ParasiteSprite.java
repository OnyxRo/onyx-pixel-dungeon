/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Xun's Pixel Dungeon, by X-U-N-123, ported into Onyx's Pixel Dungeon
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

import com.watabou.noosa.TextureFilm;

//xun, Xun's PD's parasite, drawn from the mod's own ripper sheet which has a second row for it
public class ParasiteSprite extends RipperSprite {

	public ParasiteSprite(){
		super();

		texture( "sprites/xun/parasite.png" );

		TextureFilm frames = new TextureFilm( texture, 15, 14 );

		idle = new Animation( 4, true );
		idle.frames( frames, 18, 17, 18, 19);

		run = new Animation( 15, true );
		run.frames( frames, 20, 21, 22, 23, 24, 25);

		attack = new Animation( 12, false );
		attack.frames( frames, 17, 26, 27, 26);

		stab = new Animation( 12, false );
		stab.frames( frames, 17, 26, 28, 26);

		prep = new Animation( 1, true );
		prep.frames( frames, 26);

		leap = new Animation( 1, true );
		leap.frames( frames, 29);

		die = new Animation( 15, false );
		die.frames( frames, 18, 30, 31, 32, 33);

		play( idle );

	}
}
