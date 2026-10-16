/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Pixel Towers / Towers Pixel Dungeon
 * Copyright (C) 2024-2025 FixAkaTheFix (initials R. A. A.)
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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.DamageSource;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CampRatShieldSprite;

public class CampRatShield extends CampRat {

	{
		spriteClass = CampRatShieldSprite.class;

		HP = HT = 60;
		defenseSkill = 1;

		damageMin = 10;
		damageMax = 15;
		defMin = 1;
		defMax = 3;

		cost = 600;
	}
	@Override
	public int attackSkill( Char target ) {
		return 10;
	}

	@Override
	public float attackDelay() {
		return super.attackDelay() * 1.5f;
	}

	@Override
	public void damage(int dmg, Object src) {
		if (!(DamageSource.MAGICAL.contains(src.getClass())))dmg*=0.85f; //15% damage reduction
		super.damage(dmg, src);
	}
	@Override
	protected boolean canAttack(Char enemy) {
		return super.canAttack(enemy) || (distance(enemy)<=2);
	}
}
