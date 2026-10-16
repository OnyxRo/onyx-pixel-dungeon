/*
 * Onyx's Pixel Dungeon
 * Copyright (C) 2026 Onyx Rothman
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

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hostile.criminals.Murderer;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hostile.rats.GreyRat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hostile.scorpions.RedScorpion;
import com.watabou.utils.Random;

import java.util.ArrayList;

/**
 * The monsters of a floor built in a Lovecraft Pixel Dungeon world. Lovecraft's own spawner is
 * Shattered's with a few swaps, so this takes Shattered's rotation for the depth and applies the
 * same swaps. Grey rats stand in for some rats on floors 1 to 3, murderers for the thief on
 * floors 7 and 9, and red scorpions join the scorpions on floors 23 and 24.
 */
public class LovecraftSpawner {

	public static ArrayList<Class<? extends Mob>> getMobRotation( int depth ){
		ArrayList<Class<? extends Mob>> mobs = MobSpawner.getMobRotation( depth );
		if (depth <= 3) {
			swap( mobs, Rat.class, GreyRat.class, depth == 1 ? 2 : 3 );
		} else if (depth == 7 || depth == 9) {
			swap( mobs, Thief.class, Murderer.class, 1 );
		} else if (depth == 23 || depth == 24) {
			swap( mobs, Scorpio.class, RedScorpion.class, 1 );
		}
		return mobs;
	}

	//replaces up to this many copies of a monster, or adds one if that monster is not in the rotation
	private static void swap( ArrayList<Class<? extends Mob>> mobs, Class<? extends Mob> from,
	                          Class<? extends Mob> to, int count ){
		boolean found = false;
		for (int i = 0; i < mobs.size() && count > 0; i++) {
			if (mobs.get( i ) == from && Random.Int( 3 ) == 0) {
				mobs.set( i, to );
				count--;
				found = true;
			}
		}
		if (!found) mobs.add( to );
	}
}
