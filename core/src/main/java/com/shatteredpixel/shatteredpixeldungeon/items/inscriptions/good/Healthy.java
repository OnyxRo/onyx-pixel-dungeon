/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * From Ryuse's PD. This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.items.inscriptions.good;

import com.shatteredpixel.shatteredpixeldungeon.items.inscriptions.Inscription;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;

public class Healthy extends Inscription {

	@Override
	public int mhpFix(int mhp) {
		return mhp + (int) (mhp * 0.05f);
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return new ItemSprite.Glowing(0xff0000);
	}
}
