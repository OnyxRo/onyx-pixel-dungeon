/*
 * Unleashed Pixel Dungeon
 * Copyright (C) 2015  David Mitchell
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
package com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

//Unleashed PD. An artifact weapon grows by itself, every few killing blows it may attune to its wielder and gain a level
public class Ancient extends Weapon.Enchantment {

	private static final int LEVEL_CAP = 10;
	private static final String NUMKILLS = "numkills";
	private static ItemSprite.Glowing LTGRAY = new ItemSprite.Glowing( 0x888888 );

	public int numKills = 0;

	@Override
	public int proc( Weapon weapon, Char attacker, Char defender, int damage ) {
		int level = Math.max( 0, weapon.buffedLvl() );
		int defHP = defender.HP;
		int extra = 0;

		if (Random.Int( level + 3 ) >= 2) {
			extra = Random.Int( 1, level + 2 );
			defender.damage( extra, this );
		}

		if (weapon.level() < LEVEL_CAP && (extra + damage) >= defHP) {
			numKills++;
			if (numKills > 10) {
				numKills = 0;
				int testValue = LEVEL_CAP * LEVEL_CAP;
				int failRate = testValue - (level * (level - 1));
				if (level < 3 || Random.Int( testValue ) < failRate) {
					GLog.p( "The %s attunes itself to you.", weapon.name() );
					weapon.upgrade();
				}
			}
		}
		return damage;
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( NUMKILLS, numKills );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		numKills = bundle.getInt( NUMKILLS );
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return LTGRAY;
	}
}
