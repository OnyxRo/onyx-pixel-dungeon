/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Shocked;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

import static com.shatteredpixel.shatteredpixeldungeon.actors.damagetype.DamageType.SHOCK_DAMAGE;

public class EnchantmentShock2 extends Weapon.Enchantment {

private static ItemSprite.Glowing GREEN = new ItemSprite.Glowing( 0x00FF00 );

	@Override
	public int proc(Weapon weapon, Char attacker, Char defender, int damage) {
		// lvl 0 - 25%
		// lvl 1 - 40%
		// lvl 2 - 50%
		Object fcb = null; //sps, the Four Clover is not in this game
		int level = Math.min(20, attacker.HT/10);
		int maxdmg = level + weapon.level();
		defender.damage((int)(Random.Int(level,maxdmg)*0.25), SHOCK_DAMAGE,2);
		if(fcb != null && Random.Int(2) == 1){
			defender.damage((int)(Random.Int(level,maxdmg)*0.50), SHOCK_DAMAGE,2);
		}		
		if (Random.Int(4) == 1) {
		Buff.affect(defender, Shocked.class).level(3);
		}
		return damage;
	}
	@Override
	public Glowing glowing() {
		return GREEN;
	}
}

