/*
 * Onyx's Pixel Dungeon
 *
 * Ancient Mystery City world, ported from Magic Ling Pixel Dungeon (https://github.com/LingASDJ/Magic_Ling_Pixel_Dungeon).
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

import com.shatteredpixel.shatteredpixeldungeon.levels.painters.JunglePainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.RegularPainter;
import com.watabou.utils.Random;

/** The Ancient Mystery City, from Magic Ling Pixel Dungeon. */
public class AncientCityLevel extends WorldLevel {

	{
		color1 = 0x6b7a3a;
		color2 = 0x8fa35a;
	}

	@Override
	public String tilesTex() {
		return "environment/worlds/tiles_ancient.png";
	}

	@Override
	public String waterTex() {
		return "environment/worlds/water_ancient.png";
	}

	@Override
	protected RegularPainter basePainter() {
		return new JunglePainter();
	}

	@Override
	protected float waterFill() {
		return feeling == Feeling.WATER ? 0.90f : 0.90f;
	}

	@Override
	protected int waterSmoothing() {
		return 9;
	}

	@Override
	protected float grassFill() {
		return feeling == Feeling.GRASS ? 0.90f : 0.90f;
	}

	@Override
	protected int grassSmoothing() {
		return 9;
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
		return MusicSet.CITY;
	}

	@Override
	protected Ambience ambience() {
		return Ambience.NONE;
	}
}
