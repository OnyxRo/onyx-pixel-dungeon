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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

//shown while the hero is in a region whose boss is dead, horror there stays below horrified
public class RegionSecure extends Buff {

	{
		type = buffType.POSITIVE;
		announced = false;
	}

	@Override
	public boolean act() {
		if (!Horror.regionSecure()) detach();
		spend(TICK);
		return true;
	}

	@Override
	public int icon() {
		return BuffIndicator.BLESS;
	}

	@Override
	public String desc() {
		String[] keys = {"desc_goo", "desc_tengu", "desc_dm300", "desc_king", "desc_yog"};
		int region = Math.max(0, Math.min(4, (Dungeon.depth - 1) / 5));
		return Messages.get(this, keys[region]) + "\n\n" + Messages.get(this, "desc");
	}
}
