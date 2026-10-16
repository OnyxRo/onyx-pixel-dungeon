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

public class GuardHelmet extends Helmet {

	{
		image = ItemSpriteSheet.HELMET_GUARD;
	}

	@Override
	public float procTakenDamage(Hero hero, float dmg, Object src) {
		return super.procTakenDamage(hero, dmg * (cursed ? 1.06f : 0.94f), src);
	}
}
