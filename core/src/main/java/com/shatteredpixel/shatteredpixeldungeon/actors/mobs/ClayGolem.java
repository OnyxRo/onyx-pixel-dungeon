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

public class ClayGolem extends UMob {

    {
        name = "clay golem";
//port-oldstats
public class ClayGolem extends Mob {

    {
        spriteClass = ClayGolemSprite.class;

        HP = HT = 40;
        defenseSkill = 20;
        atkSkill = 15;
        dmgRed = 10;
        dmgMin = 8;
        dmgMax = 18;

        EXP = 8;
        maxLvl = 18;
        mobType = MOBTYPE_TOUGH;
        TYPE_MINDLESS = true;
    }

    @Override

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

    @Override
    public String description() {
        return
                "Clay Golems are the result of powerful chaotic magic reacting with the dungeon, "+
                "although not truly alive they move through the dungeon with intelligence and seek "+
                "only to destroy.";
    }

    private static final HashSet<Class<?>> RESISTANCES = new HashSet<>();
    static {
    }

    { resistances.addAll( RESISTANCES ); }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();
    static {
        IMMUNITIES.add( Terror.class );
        IMMUNITIES.add( Sleep.class );
    }

    { immunities.addAll( IMMUNITIES ); }
}
