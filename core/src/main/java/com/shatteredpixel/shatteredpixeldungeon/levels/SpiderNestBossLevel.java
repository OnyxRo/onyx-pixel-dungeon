/*
 * Onyx's Pixel Dungeon
 *
 * The Dwarf King's throne room in the Spider Nest from Re-Remixed Dungeon by QuasiStellar (QuasiStellar/Re-Remixed_Dungeon).
 * In the mod it is Shattered 0.7.4's city boss arena with the Spider Nest tiles, used when the run took the
 * Spider Nest in place of the usual region. Here it extends OldCityBossLevel, the same old arena that came with
 * Abyssalnite, and only swaps the tiles for this game's Spider Nest sheet.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.levels;

public class SpiderNestBossLevel extends OldCityBossLevel {

	@Override
	public String tilesTex() {
		return "environment/worlds/tiles_spider_nest.png";
	}

	@Override
	public String waterTex() {
		return "environment/worlds/water_spider_nest.png";
	}
}
