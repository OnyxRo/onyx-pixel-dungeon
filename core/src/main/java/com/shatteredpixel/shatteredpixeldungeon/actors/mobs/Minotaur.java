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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MinotaurSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

//port-oldstats
public class Minotaur extends Mob {
    {
        spriteClass = MinotaurSprite.class;

        HP = HT = 120;
        defenseSkill = 22;

        EXP = 11;
        maxLvl = 28;

        state = HUNTING;
        immunities.add( Terror.class );
        immunities.add( Vertigo.class );
        immunities.add( Charm.class );
        resistances.add( Grim.class );
    }

    @Override
    public int attackSkill( Char target ) {
        return 32;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 12, 30 );
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange( 0, 14 );
    }


    @Override
    public int attackProc(Char enemy, int damage) {
        if (Random.Int(3) == 0) {
            Buff.affect(enemy, Bleeding.class).set(Random.Int(5, 8));
            Buff.prolong(enemy, Cripple.class, Cripple.DURATION );
            state = FLEEING;
        }

        return damage;
    }

    @Override
    protected boolean act() {
        boolean result = super.act();

        if (state == FLEEING && enemy != null && enemySeen && enemy.buff( Bleeding.class ) == null) {
            state = HUNTING;
        }
        return result;
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );
        Dungeon.level.drop(new PotionOfMight(), pos).sprite.drop();
    }

    @Override
    public void notice() {
        super.notice();
        yell( "You shall not pass!" );
    }
}
