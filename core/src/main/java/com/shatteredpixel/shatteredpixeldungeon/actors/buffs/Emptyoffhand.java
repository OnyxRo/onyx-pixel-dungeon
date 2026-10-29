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

import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

//the mod's empty offhand. This game has no offhand slot, so the bonus holds while no light
//source is lit (MusicImplant.emptyHand), and Horror keeps this marker in step
public class Emptyoffhand extends Buff {

	{
		type = buffType.POSITIVE;
		announced = false;
	}

	public static final float evasionBonus = 0.05f;
	public static final float dmgBonus = 0.10f;

	@Override
	public int icon() {
		return BuffIndicator.DUEL_BRAWL;
	}
}
