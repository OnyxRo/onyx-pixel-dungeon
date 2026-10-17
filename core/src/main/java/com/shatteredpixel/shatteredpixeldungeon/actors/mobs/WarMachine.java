/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paranoid;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WarMachineSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class WarMachine extends Mob {

    {
        spriteClass = WarMachineSprite.class;

        HP = HT = 85;
        defenseSkill = 18;

        EXP = 12;
        maxLvl = 22;
        properties.add(Property.INORGANIC);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(25, 40);
    }

    @Override
    public int attackSkill(Char target) {
        return 28;
    }

    @Override
    public float attackDelay() {
        return 1.5f;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 12);
    }

    @Override
    public void die(Object cause) {

        super.die(cause);
    }

    {
    }

    {
        immunities.add(Paranoid.class);
        immunities.add(Terror.class);
        immunities.add(Sleep.class);
    }
}
