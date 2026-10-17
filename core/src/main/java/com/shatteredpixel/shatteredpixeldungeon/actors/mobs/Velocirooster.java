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
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.sprites.VelociroosterSprite;

//port-oldstats
public class Velocirooster extends Mob {

    {
        spriteClass = VelociroosterSprite.class;

        HP = HT = 18;
        defenseSkill = 6;
        baseSpeed = 2f;

        EXP = 4;
        maxLvl = 11;

        loot = new MysteryMeat();
        lootChance = 0.167f;
    }

    @Override
    public int attackSkill( Char target ) {
        return 11;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 3, 6 );
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange( 0, 2 );
    }


    @Override
    public String defenseVerb() {
        return "Dodged";
    }
}
