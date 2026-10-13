/*
 * Onyx's Pixel Dungeon
 *
 * Dragon Cave world, ported from Magic Ling Pixel Dungeon (https://github.com/LingASDJ/Magic_Ling_Pixel_Dungeon).
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

/** The Dragon Cave, from Magic Ling Pixel Dungeon. */
public class DragonCaveLevel extends WorldLevel {

	{
		color1 = 0x8a2a14;
		color2 = 0xd8642a;
	}

	@Override
	public String tilesTex() {
		return "environment/worlds/tiles_dragon_cave.png";
	}

	@Override
	public String waterTex() {
		return "environment/water4.png";
	}

	@Override
	protected RegularPainter basePainter() {
		return new JunglePainter();
	}

	@Override
	protected float waterFill() {
		return feeling == Feeling.WATER ? 0.85f : 0.30f;
	}

	@Override
	protected int waterSmoothing() {
		return 5;
	}

	@Override
	protected float grassFill() {
		return feeling == Feeling.GRASS ? 0.80f : 0.20f;
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
		return MusicSet.HALLS;
	}

	@Override
	protected Ambience ambience() {
		return Ambience.HALLS;
	}
}
