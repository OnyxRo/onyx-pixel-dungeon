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

public class TurtleScarf extends Helmet {

	{
		image = ItemSpriteSheet.TURTLE_SCARF_BLUE;
	}

	@Override
	public float procTakenDamage(Hero hero, float dmg, Object src) {
		if (Helmet.isMagic(src)) {
			dmg *= cursed ? 1.25f : 0.8f;
		}
		return super.procTakenDamage(hero, dmg, src);
	}
}
