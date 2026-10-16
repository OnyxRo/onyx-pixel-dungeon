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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.watabou.utils.Random;

public class LittlePail extends Helmet {

	{
		image = ItemSpriteSheet.LITTLE_PAIL;
	}

	@Override
	public float procTakenDamage(Hero hero, float dmg, Object src) {
		if (!cursed) {
			dmg = Math.max(0, dmg - Random.NormalIntRange(0, 3));
		}
		return super.procTakenDamage(hero, dmg, src);
	}
}
