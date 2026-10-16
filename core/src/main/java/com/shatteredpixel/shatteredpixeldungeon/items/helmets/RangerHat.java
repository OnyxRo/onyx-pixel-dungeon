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

package com.shatteredpixel.shatteredpixeldungeon.items.helmets;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;

public class RangerHat extends Helmet {

	{
		image = ItemSpriteSheet.HELMET_RANGER;
	}

	@Override
	public int procGivenDamage(Char hero, Char enemy, int dmg, boolean ranged) {
		if (ranged) {
			int v = Math.max(dmg / 5, 2);
			dmg += cursed ? -v : v;
		}
		return dmg;
	}
}
