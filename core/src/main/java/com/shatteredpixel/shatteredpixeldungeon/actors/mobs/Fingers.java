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

import java.util.HashSet;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SquidSprite;
import com.watabou.utils.Random;

//port-oldstats
public class Fingers extends Mob {

    {
        spriteClass = SquidSprite.class;

        baseSpeed = 2f;

        EXP = 0;
        immunities.add( Burning.class );
        immunities.add( Paralysis.class );
        immunities.add( ToxicGas.class );
        immunities.add( Roots.class );
        immunities.add( Grim.class );
    }

    @Override
    public int attackSkill( Char target ) {
        return 24 + Dungeon.depth * 2;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( Dungeon.depth * 2, 4 + Dungeon.depth * 3 );
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange( 0, Dungeon.depth );
    }


    public Fingers() {
        super();

        HP = HT = 18 + Dungeon.depth * 6;
        defenseSkill = 14 + Dungeon.depth * 2;
    }

    @Override
    protected boolean act() {
        if (!Dungeon.level.water[pos]) {
            die( null );
            return true;
        } else {
            //this causes squid to move away when a door is closed on them.
            enemy = chooseEnemy();
            if (state == this.HUNTING &&
                    !(enemy != null && enemy.isAlive() && Dungeon.level.heroFOV[enemy.pos] && enemy.invisible <= 0)){
                state = this.WANDERING;
                int oldPos = pos;
                int i = 0;
                do {
                    i++;
                    target = Dungeon.level.randomDestination( this );
                    if (i == 100) return true;
                } while (!getCloser(target));
                moveSprite( oldPos, pos );
                return true;
            }

            return super.act();
        }
    }

    @Override
    public void die( Object cause ) {
        Dungeon.level.drop( new MysteryMeat(), pos ).sprite.drop();
        super.die(cause);
    }

    @Override
    protected boolean getCloser( int target ) {

        if (rooted) {
            return false;
        }

        int step = Dungeon.findStep( this, target, Dungeon.level.water, Dungeon.level.heroFOV, true );
        if (step != -1) {
            move( step );
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean getFurther( int target ) {
        int step = Dungeon.flee( this, target, Dungeon.level.water, Dungeon.level.heroFOV, true );
        if (step != -1) {
            move( step );
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        if (Dungeon.level.adjacent( pos, enemy.pos )) {
            return true;
        } else {
            return false; // DSM-xxxx troubleshoot this next block...
            //Ballistica attack = new Ballistica(pos, enemy.pos, Ballistica.PROJECTILE);
            //return (attack.collisionPos == enemy.pos);
        }
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        if (enemy == Dungeon.hero) {
            if (Random.Int(4) == 0) {
                Buff.prolong(enemy, Cripple.class, Cripple.DURATION);
            } else if (Random.Int(4) == 1) {
                Buff.prolong(enemy, Weakness.class, Weakness.DURATION);
            }
        }

        return damage;
    }

    /*
    @Override
    public boolean reset() {
        return true;
    }
   */
}
