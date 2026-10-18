/*
 * Onyx's Pixel Dungeon
 * Based on Shattered Pixel Dungeon, Copyright (C) 2014-2025 Evan Debenham
 * Pressure Plate Trap ported from Pixel Dungeon Reforged by nathanlink169
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

package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

//Pixel Dungeon Reforged. A floor plate that opens the locked gates it is linked to, used by the pressure plate
//modifier unlock room. Its picture sits in cells 105 (active) and 106 (pressed) of terrain_features.png.
public class PressurePlateTrap extends Trap {

	public static final int ACTIVE_ID = 105;
	public static final int INACTIVE_ID = 106;

	private ArrayList<Integer> m_Gates = new ArrayList<>();

	{
		color = GREEN;
		shape = CROSSHAIR;

		canBeHidden = false;
		avoidsClosedSpaces = true;
	}

	public void AddGateToLower(int gate) {
		if (active) {
			m_Gates.add(gate);
		} else {
			if (Dungeon.level.map[gate] == Terrain.LOCKED_GATE) {
				Level.set(gate, Terrain.UNLOCKED_GATE);
				GameScene.updateMap( gate );
			}
		}
	}

	@Override
	public void activate() {
		Sample.INSTANCE.play( Assets.Sounds.CLICK );

		for (int i = 0; i < m_Gates.size(); ++i) {
			if (Dungeon.level.map[m_Gates.get(i)] == Terrain.LOCKED_GATE) {
				Level.set(m_Gates.get(i), Terrain.UNLOCKED_GATE);
				GameScene.updateMap( m_Gates.get(i) );
			}
		}
	}

	private static final String GATES_KEY = "gates";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);

		int[] arr = new int[m_Gates.size()];
		for (int i = 0; i < m_Gates.size(); i++) {
			arr[i] = m_Gates.get(i);
		}
		bundle.put(GATES_KEY, arr);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		m_Gates = new ArrayList<>();
		if (bundle.contains(GATES_KEY)) {
			for (int value : bundle.getIntArray(GATES_KEY)) {
				m_Gates.add(value);
			}
		}
	}
}
