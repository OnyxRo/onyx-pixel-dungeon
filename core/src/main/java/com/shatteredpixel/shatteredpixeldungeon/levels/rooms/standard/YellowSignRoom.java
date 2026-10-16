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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.items.quest.YellowSign;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Point;

/** Lovecraft PD, a bare room with a ritual sign on the floor. Drop the yellow sign on it to call something up. */
public class YellowSignRoom extends StandardRoom {

	@Override
	public int minWidth() {
		return Math.max(super.minWidth(), 5);
	}

	@Override
	public int minHeight() {
		return Math.max(super.minHeight(), 5);
	}

	@Override
	public void paint( Level level ) {
		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}

		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.EMPTY );

		Point c = center();
		YellowSignMaker vis = new YellowSignMaker();
		vis.pos( c.x - 1, c.y - 1 );
		level.customTiles.add( vis );

		Painter.fill( level, c.x - 1, c.y - 1, 3, 3, Terrain.EMPTY_DECO );

		level.addItemToSpawn( new YellowSign() );

		YellowSign.ritualPos = c.x + (level.width() * c.y);
	}

	public static class YellowSignMaker extends CustomTilemap {

		{
			texture = "environment/custom_tiles/lovecraft/yellow_sign.png";
			tileW = tileH = 3;
		}

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			v.map( mapSimpleImage( 0, 0, 64 ), 3 );
			return v;
		}

		@Override
		public String name( int tileX, int tileY ) {
			return Messages.get( YellowSign.class, "name" );
		}

		@Override
		public String desc( int tileX, int tileY ) {
			return Messages.get( YellowSign.class, "desc" );
		}
	}
}
