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
import com.shatteredpixel.shatteredpixeldungeon.sprites.SlimeRedSprite;
import com.watabou.utils.Random;

public class SlimeRed  extends UMob {

    {
        name = "red slime";
//port-oldstats
public class SlimeRed  extends Mob {

    {
        spriteClass = SlimeRedSprite.class;

        HP = HT = 15;
        defenseSkill = 5;
        atkSkill = 11;
        dmgRed = 4;
        dmgMin = 3;
        dmgMax = 6;

        EXP = 5;
        maxLvl = 11;

        mobType = MOBTYPE_DEBUFF;
    }

    @Override

        EXP = 5;
        maxLvl = 11;
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
        return Random.NormalIntRange( 0, 4 );
    }


    @Override
    public int attackProc( Char enemy, int damage ) {

        if (Random.Int( 3 ) == 0) {
            Buff.affect(enemy, Poison.class).set(Random.Int(1, 3) * Poison.durationFactor(enemy));
        }

        return damage;
    }

    @Override
    public String description() {
        return
                "Slimes look like icky little piles of goo, but they can pack a bite." +
                        "The red slimes are poisonous in addition to other icky characteristics.";
    }
}
