/*
 * Music Implant SPD, ported into Onyx's Pixel Dungeon (key musicimplant)
 * https://github.com/CatAzreal/Jinkeloid-MISPD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.MusicImplant;

//counts turns for the dexterous and quick draw perks, which refill every 2 and 5 turns
public class RegenPerTurn extends Buff {

	{
		type = buffType.NEUTRAL;
		revivePersists = true;
	}

	@Override
	public boolean act() {
		if (target.isAlive()) MusicImplant.heroTurn();
		spend(TICK);
		return true;
	}
}
