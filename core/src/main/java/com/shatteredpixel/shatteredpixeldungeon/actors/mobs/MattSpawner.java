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

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.GuildLevel;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The monsters Matt Edition puts in its own two areas. Its thieves' guild is home to burglars
 * and snatchers, and its ice caves to frozen swarms, frost DM-100s, ice snakes and ice golems.
 * Matt Edition gave those areas fixed monsters whatever the floor, so depth is not used here.
 */
public class MattSpawner {

	public static ArrayList<Class<? extends Mob>> getMobRotation( Level level ){
		ArrayList<Class<? extends Mob>> mobs;
		if (level instanceof GuildLevel) {
			mobs = new ArrayList<>(Arrays.asList(
					Burglar.class, Burglar.class, Burglar.class,
					Snatcher.class, Snatcher.class,
					DM151.class));
		} else {
			mobs = new ArrayList<>(Arrays.asList(
					FrozenSwarm.class, FrozenSwarm.class,
					DM100F.class,
					IceSnake.class, IceSnake.class,
					IceGolem.class,
					GnollBaby.class));
			//Matt Edition's rare gnoll, at the odds it had there
			if (Random.Float() < 0.02f) mobs.add( GnollMyth.class );
		}
		Random.shuffle( mobs );
		return mobs;
	}

}
