/*
 * Onyx's Pixel Dungeon
 *
 * Forge world, ported from Devoted Pixel Dungeon (https://github.com/Arackulele/devoted-pixel-dungeon).
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

import com.shatteredpixel.shatteredpixeldungeon.levels.painters.ForgePainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.RegularPainter;
import com.watabou.utils.Random;

/** The Forge, from Devoted Pixel Dungeon. */
public class ForgeLevel extends WorldLevel {

	{
		color1 = 0x534f3e;
		color2 = 0xb9d661;
	}

	@Override
	public String tilesTex() {
		return "environment/worlds/tiles_forge.png";
	}

	@Override
	public String waterTex() {
		return "environment/worlds/water_forge.png";
	}

	@Override
	protected RegularPainter basePainter() {
		return new ForgePainter();
	}

	@Override
	protected float waterFill() {
		return feeling == Feeling.WATER ? 0.85f : 0.30f;
	}

	@Override
	protected int waterSmoothing() {
		return 6;
	}

	@Override
	protected float grassFill() {
		return feeling == Feeling.GRASS ? 0.65f : 0.15f;
	}

	@Override
	protected int grassSmoothing() {
		return 3;
	}

	@Override
	protected int standardRooms(boolean forceMax) {
		if (forceMax) return 7;
		return 6 + Random.chances(new float[]{2, 1});
	}

	@Override
	protected int specialRooms(boolean forceMax) {
		if (forceMax) return 3;
		return 2 + Random.chances(new float[]{4, 1});
	}

	@Override
	protected MusicSet musicSet() {
		return MusicSet.CAVES;
	}

	@Override
	protected Ambience ambience() {
		return Ambience.NONE;
	}
}
