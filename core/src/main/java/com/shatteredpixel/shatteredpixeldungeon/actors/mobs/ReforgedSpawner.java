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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The monsters Reforged puts in its Kobold Caves. Kobolds are most of the crowd, with spitters
 * and unholy priests among them, and the heavier ballistas and fiends only turn up deeper.
 */
public class ReforgedSpawner {

	public static ArrayList<Class<? extends Mob>> getMobRotation( int depth ){
		ArrayList<Class<? extends Mob>> mobs = new ArrayList<>(Arrays.asList(
				Kobold.class, Kobold.class, Kobold.class, Kobold.class,
				Spitter.class, Spitter.class,
				UnholyPriest.class, UnholyPriest.class));
		if (depth >= 8) {
			mobs.add( Ballista.class );
		}
		if (depth >= 14) {
			mobs.add( Fiend.class );
			mobs.add( QuickFiringBallista.class );
		}
		Random.shuffle( mobs );
		return mobs;
	}

}
