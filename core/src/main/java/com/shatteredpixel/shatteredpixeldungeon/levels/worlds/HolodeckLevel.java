/*
 * Onyx's Pixel Dungeon
 *
 * Holodeck world, from Pixel Spacebase (https://github.com/codefitz/Pixel-Spacebase).
 * Tile and water art redrawn by hand into this game's tile layout from that mod's sheets.
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

import com.shatteredpixel.shatteredpixeldungeon.levels.painters.CityPainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.RegularPainter;
import com.watabou.utils.Random;

/** The Holodeck, from Pixel Spacebase. */
public class HolodeckLevel extends WorldLevel {

	{
		color1 = 0x3b434b;
		color2 = 0x363b49;
	}

	@Override
	public String tilesTex() {
		return "environment/worlds/tiles_spacebase_holodeck.png";
	}

	@Override
	public String waterTex() {
		return "environment/worlds/water_spacebase_holodeck.png";
	}

	@Override
	protected RegularPainter basePainter() {
		return new CityPainter();
	}

	@Override
	protected int waterSmoothing() {
		return 4;
	}

	@Override
	protected int grassSmoothing() {
		return 3;
	}

	@Override
	protected int standardRooms(boolean forceMax) {
		if (forceMax) return 8;
		return 6 + Random.chances(new float[]{1, 3, 1});
	}

	@Override
	protected int specialRooms(boolean forceMax) {
		if (forceMax) return 3;
		return 2 + Random.chances(new float[]{1, 1});
	}

	@Override
	protected MusicSet musicSet() {
		return MusicSet.CITY;
	}

	@Override
	protected Ambience ambience() {
		return Ambience.NONE;
	}
}
