/*
 * Onyx's Pixel Dungeon
 * Copyright (C) 2026 Onyx Rothman
 *
 * Based on Shattered Pixel Dungeon, Copyright (C) 2014-2026 Evan Debenham
 * and Shattered Pixel Dungeon Matt Edition by mr-matthias1829
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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.GuildLevel;
import com.watabou.utils.Random;

import java.util.ArrayList;

/**
 * The monsters Matt Edition puts in its own two areas, as its ThievesGuildLevel and IceCavesLevel build them.
 * The guild is 4 thieves, 3 burglars and 3 snatchers on every floor. The ice caves are a branch of four floors
 * (Matt's 14 to 17) with a table for each, here picked by the floor's place in its region.
 */
public class MattSpawner {

	public static ArrayList<Class<? extends Mob>> getMobRotation( Level level ){
		ArrayList<Class<? extends Mob>> mobs = new ArrayList<>();
		if (level instanceof GuildLevel) {
			add( mobs, Thief.class, 4 );
			add( mobs, Burglar.class, 3 );
			add( mobs, Snatcher.class, 3 );
		} else {
			switch ((Dungeon.depth - 1) % 5) {
				case 0: default:
					add( mobs, Elemental.FrostElemental.class, 1 );
					add( mobs, DM200.class, 3 );
					add( mobs, Bat.class, 3 );
					add( mobs, FrozenSwarm.class, 3 );
					add( mobs, DM100F.class, 5 );
					add( mobs, IceSnake.class, 5 );
					break;
				case 1:
					add( mobs, Elemental.FrostElemental.class, 1 );
					add( mobs, Bat.class, 2 );
					add( mobs, FrozenSwarm.class, 2 );
					add( mobs, DM100F.class, 2 );
					add( mobs, IceSnake.class, 2 );
					add( mobs, IceGolem.class, 2 );
					break;
				case 2:
					add( mobs, Elemental.FrostElemental.class, 1 );
					add( mobs, FrozenSwarm.class, 1 );
					add( mobs, DM100F.class, 1 );
					add( mobs, IceSnake.class, 1 );
					add( mobs, IceGolem.class, 2 );
					break;
				case 3:
					add( mobs, Elemental.FrostElemental.class, 2 );
					add( mobs, IceGolem.class, 5 );
					add( mobs, FrozenSwarm.class, 1 );
					add( mobs, DM100F.class, 1 );
					add( mobs, IceSnake.class, 1 );
					break;
			}
		}
		Random.shuffle( mobs );
		return mobs;
	}

	private static void add( ArrayList<Class<? extends Mob>> mobs, Class<? extends Mob> mob, int count ){
		for (int i = 0; i < count; i++) mobs.add( mob );
	}

}
