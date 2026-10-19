/*
 * Onyx's Pixel Dungeon
 *
 * Caves Annex world, ported from Deep Pixel Dungeon (https://github.com/Mochiee/deep-pixel-dungeon).
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

import com.shatteredpixel.shatteredpixeldungeon.levels.painters.CavesPainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.RegularPainter;

/** The Caves Annex, a side branch of the caves in Deep Pixel Dungeon by Mochiee, reached through a portal. */
public class CavesAnnexLevel extends WorldLevel {

	{
		color1 = 0xb0b0b0;
		color2 = 0xd04040;
	}

	@Override
	public String tilesTex() {
		return "environment/worlds/tiles_deep_annex.png";
	}

	@Override
	public String waterTex() {
		return "environment/worlds/water_deep_annex.png";
	}

	@Override
	protected RegularPainter basePainter() {
		return new CavesPainter();
	}

	@Override
	protected MusicSet musicSet() {
		return MusicSet.CAVES;
	}

	@Override
	protected Ambience ambience() {
		return Ambience.CAVES;
	}
}
