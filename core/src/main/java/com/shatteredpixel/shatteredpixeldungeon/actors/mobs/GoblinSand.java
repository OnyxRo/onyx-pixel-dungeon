/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Pixel Towers / Towers Pixel Dungeon
 * Copyright (C) 2024-2025 FixAkaTheFix (initials R. A. A.)
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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GoblinSandSprite;
import com.watabou.utils.Random;

public class GoblinSand extends Goblin {

    {
        spriteClass = GoblinSandSprite.class;

        HP = HT = 30;
        defenseSkill = 5;

        viewDistance = 20;
        baseSpeed = 1.1f;

        EXP = 5;
        maxLvl = 10;
    }

    @Override
    public int damageRoll() {
        //a 20 percent chance of a fivefold hit, Tower PD's critMult and critChance
        return Random.NormalIntRange( 3, 4 ) * (Random.Float() < 0.2f ? 5 : 1);
    }

    @Override
    public int attackSkill( Char target ) {
        return 30;
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if (Math.random()>0.3){
            Buff.affect(enemy, Blindness.class,5);
        }
        return super.attackProc(enemy, damage);
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0,1);
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return super.canAttack(enemy) || (distance(enemy)<=2);
    }
}
