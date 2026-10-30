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

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

//after a kill worth its while, horror stops growing for a few turns
public class FellEnemy extends FlavourBuff {

	{
		type = buffType.POSITIVE;
		announced = false;
	}

	@Override
	public int icon() {
		return BuffIndicator.MOMENTUM;
	}

	@Override
	public float iconFadePercent() {
		return Math.max(0, 1f - visualcooldown() / 40f);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns());
	}
}
