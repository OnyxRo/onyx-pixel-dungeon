/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Pixel Dungeon Reforged
 * Copyright (C) 2024-2025 Nathan Pringle
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Spear;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KoboldSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

/** Reforged's kobold, a fiery cave dweller that comes in three colours. */
public class Kobold extends Mob {

	{
		spriteClass = KoboldSprite.Blue.class;

		HP = HT = 35;
		defenseSkill = 15;

		EXP = 7;
		maxLvl = 17;

		loot = Spear.class;
		lootChance = 0.1f;

		properties.add(Property.FIERY);
	}

	private int variant = -1;

	@Override
	public CharSprite sprite() {
		if (variant == -1) variant = Random.Int(3);
		switch (variant) {
			case 1: spriteClass = KoboldSprite.Red.class; break;
			case 2: spriteClass = KoboldSprite.Purple.class; break;
			default: spriteClass = KoboldSprite.Blue.class;
		}
		return super.sprite();
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 4, 12 );
	}

	@Override
	public int attackSkill( com.shatteredpixel.shatteredpixeldungeon.actors.Char target ) {
		return 20;
	}

	@Override
	public int drRoll() {
		return super.drRoll() + Random.NormalIntRange(0, 4);
	}

	private static final String VARIANT = "variant";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( VARIANT, variant );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		variant = bundle.getInt( VARIANT );
	}
}
