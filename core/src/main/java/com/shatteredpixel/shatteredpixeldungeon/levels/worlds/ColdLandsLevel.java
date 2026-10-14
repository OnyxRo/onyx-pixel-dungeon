/*
 * Onyx's Pixel Dungeon
 *
 * Lost Tomb world, ported from Magic Ling Pixel Dungeon (https://github.com/LingASDJ/Magic_Ling_Pixel_Dungeon).
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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.PrisonPainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.RegularPainter;
import com.watabou.utils.Random;

/** The Cold Lands, from Magic Ling Pixel Dungeon by LingASDJ. */
public class ColdLandsLevel extends WorldLevel {

	{
		color1 = 0x6a723d;
		color2 = 0x88924c;
	}

	@Override
	public String tilesTex() {
		return "environment/worlds/tiles_coldlands.png";
	}

	@Override
	public String waterTex() {
		return Assets.Environment.WATER_PRISON;
	}

	@Override
	protected RegularPainter basePainter() {
		return new PrisonPainter();
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
		return MusicSet.PRISON;
	}

	@Override
	protected Ambience ambience() {
		return Ambience.PRISON;
	}
}
