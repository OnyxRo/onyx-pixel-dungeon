/*
 * Onyx's Pixel Dungeon
 *
 * Chinese Hall world, ported from Transformation Pixel Dungeon (https://github.com/dongyusangyu/transformation-pixel-dungeon-master).
 * Tileset, water art and decoration painter from that mod.
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

import com.shatteredpixel.shatteredpixeldungeon.levels.painters.TowerPainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.RegularPainter;
import com.watabou.utils.Random;

/** The Chinese Hall, from Transformation Pixel Dungeon. */
public class ChineseHallLevel extends WorldLevel {

	{
		color1 = 0x7B2D26;
		color2 = 0xD7B867;
	}

	@Override
	public String tilesTex() {
		return "environment/worlds/tiles_chinese_hall.png";
	}

	@Override
	public String waterTex() {
		return "environment/worlds/water_chinese_hall.png";
	}

	@Override
	protected RegularPainter basePainter() {
		return new TowerPainter();
	}

	@Override
	protected float waterFill() {
		return feeling == Feeling.WATER ? 0.85f : 0.30f;
	}

	@Override
	protected int waterSmoothing() {
		return 4;
	}

	@Override
	protected float grassFill() {
		return feeling == Feeling.GRASS ? 0.80f : 0.20f;
	}

	@Override
	protected int grassSmoothing() {
		return 3;
	}

	@Override
	protected int standardRooms(boolean forceMax) {
		if (forceMax) return 7;
		return 5 + Random.chances(new float[]{1, 3, 1});
	}

	@Override
	protected int specialRooms(boolean forceMax) {
		if (forceMax) return 2;
		return 1 + Random.chances(new float[]{1, 4});
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
