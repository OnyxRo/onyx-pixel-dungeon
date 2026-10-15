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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

/**
 * Reforged's leech curse. A wielder who goes too long without dealing damage starts to bleed
 * for 1, then 2, then 4 and so on. Hero.spend calls triggerDamage as time passes.
 */
public class Leech extends Weapon.Enchantment implements Hero.Doom {

	private static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x000000 );

	private static final int TURNS_TO_ACTIVATE = 50;
	private static final int DAMAGE_INTERVAL = 20;

	private float turnsWithoutDamage = 0f;

	@Override
	public int proc( Weapon weapon, Char attacker, Char defender, int damage ) {
		if (damage > 0) turnsWithoutDamage = 0f;
		return damage;
	}

	//50 turns to activate, then damage every 20 turns, doubling each time
	public void triggerDamage( Char owner, float time ) {
		float before = turnsWithoutDamage;
		turnsWithoutDamage += time;
		int intTurns = (int)turnsWithoutDamage;
		if (intTurns < TURNS_TO_ACTIVATE || intTurns == (int)before
				|| (intTurns - TURNS_TO_ACTIVATE) % DAMAGE_INTERVAL != 0) {
			return;
		}
		int damage = (int)Math.pow( 2, (intTurns - TURNS_TO_ACTIVATE) / (float)DAMAGE_INTERVAL );
		owner.damage( damage, this );
	}

	@Override
	public boolean curse() {
		return true;
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return BLACK;
	}

	@Override
	public void onDeath() {
		Dungeon.fail( this );
		GLog.n( Messages.get(this, "ondeath") );
	}
}
