/*
 * Clairvoyant Pixel Dungeon, ported into Onyx's Pixel Dungeon (key clairvoyant)
 * https://github.com/ifritdiezel/Clairvoyant-Pixel-Dungeon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

//the Apprentice's Philosophile talent, identifying an item gives 20% accuracy and faster wands
public class Motivation extends FlavourBuff {

	{
		type = buffType.POSITIVE;
		announced = true;
	}

	@Override
	public int icon() {
		return BuffIndicator.BLESS;
	}

	@Override
	public void tintIcon( Image icon ) {
		icon.hardlight(0, 1, 0);
	}

	@Override
	public float iconFadePercent() {
		return Math.max(0, 1f - visualcooldown() / 35f);
	}
}
