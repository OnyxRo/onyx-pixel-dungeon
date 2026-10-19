/*
 * Onyx's Pixel Dungeon
 *
 * Safe Town world, from SPecial Surprise Pixel Dungeon (https://github.com/hmdzl001/SPS-PD).
 * Tile art redrawn into this game's tile layout from that mod's sheets, water is the mod's own.
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

import com.shatteredpixel.shatteredpixeldungeon.levels.painters.SewerPainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.RegularPainter;
import com.watabou.utils.Random;

/** The Safe Town, from SPecial Surprise Pixel Dungeon. */
public class SpsTownLevel extends WorldLevel {

	{
		color1 = 0x60864a;
		color2 = 0x474642;
	}

	@Override
	public String tilesTex() {
		return "environment/worlds/tiles_sps_town.png";
	}

	@Override
	public String waterTex() {
		return "environment/worlds/water_sps_town.png";
	}

	@Override
	protected RegularPainter basePainter() {
		return new SewerPainter();
	}

	@Override
	protected int waterSmoothing() {
		return 5;
	}

	@Override
	protected int grassSmoothing() {
		return 4;
	}

	@Override
	protected int standardRooms(boolean forceMax) {
		if (forceMax) return 6;
		return 4 + Random.chances(new float[]{1, 3, 1});
	}

	@Override
	protected int specialRooms(boolean forceMax) {
		if (forceMax) return 2;
		return 1 + Random.chances(new float[]{1, 4});
	}

	@Override
	protected MusicSet musicSet() {
		return MusicSet.SEWERS;
	}

	@Override
	protected Ambience ambience() {
		return Ambience.NONE;
	}
}
