/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * Summoning Pixel Dungeon Reincarnated, TrashboxBobylev
 * Ported to Onyx's Pixel Dungeon on Shattered 4.0.2's patch room API.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;

//a room filled with patches of wooden barricades, a path through it is always kept clear
public class BarricadeRoom extends PatchRoom {

	@Override
	public float[] sizeCatProbs() {
		return new float[]{8, 4, 1};
	}

	@Override
	protected float fill() {
		//fill scales from ~30% at 4x4, to ~60% at 18x18
		return 0.25f + (width()*height())/900f;
	}

	@Override
	protected int clustering() {
		return 3;
	}

	@Override
	protected boolean ensurePath() {
		return true;
	}

	@Override
	protected boolean cleanEdges() {
		return true;
	}

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1 , Terrain.EMPTY );
		for (Door door : connected.values()) {
			door.set( Door.Type.EMPTY );
		}

		setupPatch(level);
		fillPatch(level, Terrain.BARRICADE);
	}

}
