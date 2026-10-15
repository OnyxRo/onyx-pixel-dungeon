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
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite.Glowing;

public class Precise extends Weapon.Enchantment {

    private static ItemSprite.Glowing WHITE = new ItemSprite.Glowing( 0xFFFFFF );
    @Override
    public int proc( Weapon weapon, Char attacker, Char defender, int damage) {
        defender.sprite.emitter().start( Speck.factory( Speck.DISCOVER ), 0.01f, 5 * (weapon.level() + 1) );
        float evasion = defender.defenseSkill(attacker);
        float accuracy = attacker.attackSkill(defender);

        float hitChance;
        if (evasion >= accuracy){
            hitChance = (accuracy/evasion);
        } else {
            hitChance = 1f - (evasion/accuracy);
        }

        damage = (int)Math.ceil(damage * (hitChance * 1.5f));

        return damage;
    }


    @Override
    public Glowing glowing() {
        return WHITE;
    }
}