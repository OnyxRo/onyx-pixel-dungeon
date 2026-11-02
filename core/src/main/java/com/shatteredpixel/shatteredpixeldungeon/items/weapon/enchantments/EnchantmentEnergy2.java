/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShieldArmor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

import static com.shatteredpixel.shatteredpixeldungeon.actors.damagetype.DamageType.ENERGY_DAMAGE;

public class EnchantmentEnergy2 extends Weapon.Enchantment {

    private static ItemSprite.Glowing GRAY = new ItemSprite.Glowing( 0x888888 );
	
	@Override
	public int proc(Weapon weapon, Char attacker, Char defender, int damage) {
		// lvl 0 - 13%
		// lvl 1 - 22%
		// lvl 2 - 30%
		Object fcb = null; //sps, the Four Clover is not in this game
		int level = Math.min(20, attacker.HT/10);
		int maxdmg = level + weapon.level();
		defender.damage((int)(Random.Int(level,maxdmg)*0.75), ENERGY_DAMAGE,2);
		if(fcb != null && Random.Int(2) == 1){
			defender.damage((int)(Random.Int(level,maxdmg)*0.50), ENERGY_DAMAGE,2);
		}
		if (attacker.buff(ShieldArmor.class) == null) {
			Buff.affect(attacker, ShieldArmor.class).level(Random.Int(level,maxdmg));
		} else Buff.prolong(defender, Vertigo.class,3f);
			//Buff.prolong(attacker, DefenceUp.class,5f).level(Math.min(30,lvl+1));
			attacker.sprite.emitter().start(Speck.factory(Speck.UP), 0.2f, 3);
			return damage;
		//} else {
			//return false;
		//}
	}

	@Override
	public Glowing glowing() {
		return GRAY;
	}

}
