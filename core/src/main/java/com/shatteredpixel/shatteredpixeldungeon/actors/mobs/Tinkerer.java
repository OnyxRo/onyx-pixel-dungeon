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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicalInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TinkererSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

//port-oldstats
public class Tinkerer extends Mob {

    {
        spriteClass = TinkererSprite.class;

        HP = HT = 85;
        defenseSkill = 25;


        viewDistance = Light.DISTANCE;

        EXP = 11;
        maxLvl = 25;
        state = HUNTING;
        immunities.add( Terror.class );
        immunities.add( Vertigo.class );
        immunities.add( Charm.class );
        resistances.add( Grim.class );
    }

    @Override
    public int attackSkill( Char target ) {
        return 25;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 16, 20 );
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange( 0, 8 );
    }


    @Override
    protected boolean canAttack( Char enemy ) {
        Ballistica attack = new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE);
        return !Dungeon.level.adjacent(pos, enemy.pos) && attack.collisionPos == enemy.pos;
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        if ((!Dungeon.level.adjacent(pos, enemy.pos)) && Random.Int( 2 ) == 0) {
            Buff.prolong(enemy, Cripple.class, Cripple.DURATION);
            damage += Random.IntRange(4, 20);
        }

        return damage;
    }

    @Override
    protected boolean getCloser( int target ) {
        if (state == HUNTING) {
            return enemySeen && getFurther( target );
        } else {
            return super.getCloser( target );
        }
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );
        Dungeon.level.drop(new ScrollOfMagicalInfusion(), pos).sprite.drop();
        yell( "Avenge me my robots!" );
    }

    @Override
    public void notice() {
        super.notice();
        yell( "I will not let you harm my creations!" );
    }}
