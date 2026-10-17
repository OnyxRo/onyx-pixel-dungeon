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

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SquidSprite;
import com.watabou.utils.Random;

public class Squid extends UMob {

    {
        name = "deep dweller";
        spriteClass = SquidSprite.class;
        dmgRed = Dungeon.depth;
        dmgMin = Dungeon.depth * 2;
        dmgMax = 4 + Dungeon.depth * 3;
        TYPE_ANIMAL = true;

        baseSpeed = 2f;

        EXP = 0;
    }

    public Squid() {
        super();

        HP = HT = 18 + Dungeon.depth * 6;
        defenseSkill = 14 + Dungeon.depth * 2;
        atkSkill = 24 + Dungeon.depth * 2;
    }

    @Override
    protected boolean act() {
        if (!Dungeon.level.water[pos]) {
            die( null );
            return true;
        } else {
            //this causes squid to move away when a door is closed on them.
            Dungeon.level.updateFieldOfView( this, fieldOfView );
            enemy = chooseEnemy();
            if (state == this.HUNTING &&
                    !(enemy != null && enemy.isAlive() && fieldOfView[enemy.pos] && enemy.invisible <= 0)){
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

        int step = Dungeon.findStep( this, target, Dungeon.level.water, fieldOfView, true );
        if (step != -1) {
            move( step );
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean getFurther( int target ) {
        int step = Dungeon.flee( this, target, Dungeon.level.water, fieldOfView, true );
        if (step != -1) {
            move( step );
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String description() {
        return
                "All tentacles and bite, this creature isn't normally found in dungeons. " +
                        "They were bred specifically to protect flooded treasure vaults.";
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
        if (Random.Int( 4 ) == 0) {
            Buff.prolong(enemy, Cripple.class, Cripple.DURATION);
        }

        return damage;
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
    static {
        IMMUNITIES.add( Burning.class );
        IMMUNITIES.add( Paralysis.class );
        IMMUNITIES.add( ToxicGas.class );
        IMMUNITIES.add( Roots.class );
        IMMUNITIES.add( Frost.class );
    }

    { immunities.addAll( IMMUNITIES ); }

    /*
    @Override
    public boolean reset() {
        return true;
    }
   */
}
