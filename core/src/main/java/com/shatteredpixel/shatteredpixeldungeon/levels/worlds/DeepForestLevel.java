/*
 * Onyx's Pixel Dungeon
 *
 * Forest world, ported from Deep Pixel Dungeon (https://github.com/Mochiee/deep-pixel-dungeon).
 * Tileset and water from that mod, moved into this game's tile layout by scripts/convert-oldtiles.py.
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

/** The Forest, the opening stage of Deep Pixel Dungeon by Mochiee (its floors 1 to 7). */
public class DeepForestLevel extends WorldLevel {

	{
		color1 = 0x48763c;
		color2 = 0x59994a;
	}

	@Override
	public String tilesTex() {
		return "environment/worlds/tiles_deep_forest.png";
	}

	@Override
	public String waterTex() {
		return "environment/worlds/water_deep_forest.png";
	}

	@Override
	protected RegularPainter basePainter() {
		return new SewerPainter();
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
		return Ambience.SEWERS;
	}
}
