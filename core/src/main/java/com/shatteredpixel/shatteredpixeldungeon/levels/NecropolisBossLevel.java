/*
 * Onyx's Pixel Dungeon
 *
 * Tengu's arena in the Necropolis from Re-Remixed Dungeon by QuasiStellar (QuasiStellar/Re-Remixed_Dungeon).
 * In the mod it is Shattered 0.7.4's prison boss arena with the Necropolis tiles, used when the run took the
 * Necropolis in place of the usual region. Here it extends OldPrisonBossLevel, the same old arena that came with
 * Abyssalnite, and only swaps the tiles for this game's Necropolis sheet.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.levels;

public class NecropolisBossLevel extends OldPrisonBossLevel {

	@Override
	public String tilesTex() {
		return "environment/worlds/tiles_necropolis.png";
	}

	@Override
	public String waterTex() {
		return "environment/worlds/water_necropolis.png";
	}
}
