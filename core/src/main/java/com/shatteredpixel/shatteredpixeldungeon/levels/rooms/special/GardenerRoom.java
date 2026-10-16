/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Gardner;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

/** Lovecraft PD, a small garden with a Gardner who hands out scissors and a tincture bottle. */
public class GardenerRoom extends SpecialRoom {

	@Override
	public int minWidth() { return 4; }

	@Override
	public int minHeight() { return 4; }

	@Override
	public int maxHeight() { return 4; }

	@Override
	public int maxWidth() { return 4; }

	@Override
	public void paint( Level level ) {

		Door entrance = entrance();
		entrance.set( Door.Type.UNLOCKED );

		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.EMPTY );
		Painter.fill( level, left + 1, top + 1, width() - 2, 1, Terrain.GRASS );

		int heartX = Random.IntRange( left + 1, right - 1 );
		int heartY = Random.IntRange( top + 1, bottom - 1 );

		if (entrance.x == left) {
			heartX = right - 1;
		} else if (entrance.x == right) {
			heartX = left + 1;
		} else if (entrance.y == top) {
			heartY = bottom - 1;
		} else if (entrance.y == bottom) {
			heartY = top + 1;
		}

		placeGardner( level, heartX + heartY * level.width(), new Gardner() );
	}

	private static void placeGardner( Level level, int pos, Mob gardner ){
		gardner.pos = pos;
		level.mobs.add( gardner );

		for (int i : PathFinder.NEIGHBOURS8) {
			if (level.map[pos + i] == Terrain.GRASS) {
				level.plant( (Plant.Seed) Generator.random( Generator.Category.SEED ), pos + i );
			}
		}
	}
}
