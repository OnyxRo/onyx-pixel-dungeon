/*
 * Onyx's Pixel Dungeon
 *
 * Ice Caves world, ported from Combusted Pixel Dungeon (https://github.com/Inferno214221/Combusted-Pixel-Dungeon).
 * Tileset and water art from that mod.
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

package com.shatteredpixel.shatteredpixeldungeon.levels.worlds;

import com.shatteredpixel.shatteredpixeldungeon.levels.CavesLevel;

/** Ice Caves, from Combusted Pixel Dungeon. A reskin of Shattered's Caves, as in the original mod. */
public class IceCavesLevel extends CavesLevel implements ModWorld {

	@Override
	public String tilesTex() {
		return "environment/worlds/tiles_ice_caves.png";
	}

	@Override
	public String waterTex() {
		return "environment/worlds/water_ice_caves.png";
	}
}
