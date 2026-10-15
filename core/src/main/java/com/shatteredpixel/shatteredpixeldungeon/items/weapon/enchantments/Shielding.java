/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShieldBuff;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite.Glowing;

public class Shielding extends Weapon.Enchantment {

	private static ItemSprite.Glowing SILVER = new ItemSprite.Glowing( 0x909396 );
	
	@Override
	public int proc( Weapon weapon, Char attacker, Char defender, int damage ) {
		//Now guaranteed to proc, but less powerful
		//heals for 25% of damage dealt
		int healAmt = Math.round(damage * 0.25f);
		int curShielding;
		Barrier barrier = attacker.buff(Barrier.class);

		if (barrier != null) {
			curShielding = barrier.shielding();
		} else {
			curShielding = 0;
		}

		if (healAmt > curShielding && attacker.isAlive()) {

			Buff.affect(attacker, Barrier.class).setShield(healAmt);
			attacker.sprite.emitter().start( Speck.factory( Speck.DISCOVER ), 0.4f, 1 );
			attacker.sprite.showStatus( CharSprite.POSITIVE, Integer.toString( healAmt ) );
		}

		return damage;
	}
	
	@Override
	public Glowing glowing() {
		return SILVER;
	}
}
