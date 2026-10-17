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
package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;

import com.shatteredpixel.shatteredpixeldungeon.sprites.RedSnakeSprite;
import com.watabou.utils.Random;

//port-oldstats
public class RedSnake extends Mob {

    {
        spriteClass = RedSnakeSprite.class;

        HP = HT = 12;
        defenseSkill = 3;
        EXP = 3;

        maxLvl = 10;
    }

    @Override
    public int attackSkill( Char target ) {
        return 9;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 1, 5 );
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange( 0, 1 );
    }


    @Override
    public int attackProc(Char enemy, int damage ) {

        if (Random.Int( 3 ) == 0) {
            Buff.affect(enemy, Poison.class).set(Random.Int(2, 4) * Poison.durationFactor(enemy));
        }

        return damage;
    }
}

