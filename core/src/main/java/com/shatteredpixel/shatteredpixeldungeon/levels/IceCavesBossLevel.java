/*
 * Onyx's Pixel Dungeon
 *
 * DM-300's arena in the Ice Caves from Re-Remixed Dungeon by QuasiStellar (QuasiStellar/Re-Remixed_Dungeon).
 * In the mod it is Shattered 0.7.4's caves boss arena with the Ice Caves tiles, used when the run took the
 * Ice Caves in place of the usual region. Here it extends OldCavesBossLevel, the same old arena that came with
 * Abyssalnite, and only swaps the tiles for this game's Ice Caves sheet.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.levels;

public class IceCavesBossLevel extends OldCavesBossLevel {

	@Override
	public String tilesTex() {
		return "environment/worlds/tiles_ice_caves.png";
	}

	@Override
	public String waterTex() {
		return "environment/worlds/water_ice_caves.png";
	}
}
