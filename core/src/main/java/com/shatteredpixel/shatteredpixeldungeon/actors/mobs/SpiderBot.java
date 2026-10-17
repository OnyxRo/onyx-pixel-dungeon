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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpiderBotSprite;

//port-oldstats
public class SpiderBot extends Mob {

    {
        spriteClass = SpiderBotSprite.class;

        HP = HT = 60;
        defenseSkill = 12;


        viewDistance = Light.DISTANCE;

        EXP = 10;
        maxLvl = 20;
    }

    @Override
    public int attackSkill( Char target ) {
        return 25;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 12, 16 );
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange( 0, 8 );
    }

}