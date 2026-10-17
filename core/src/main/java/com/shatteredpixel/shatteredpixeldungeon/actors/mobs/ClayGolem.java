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

import com.watabou.utils.Random;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ClayGolemSprite;

import java.util.HashSet;

//port-oldstats
public class ClayGolem extends Mob {

    {
        spriteClass = ClayGolemSprite.class;

        HP = HT = 40;
        defenseSkill = 20;

        EXP = 8;
        maxLvl = 18;
        immunities.add( Terror.class );
        immunities.add( Sleep.class );
    }

    @Override
    public int attackSkill( Char target ) {
        return 15;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 8, 18 );
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange( 0, 10 );
    }


    @Override
    public float attackDelay() {
        return 1.5f;
    }

    @Override
    public String defenseVerb() {
        return "blocked";
    }
}
