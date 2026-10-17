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
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MinotaurSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Minotaur extends UMob {
    {
        name = "minotaur";
        spriteClass = MinotaurSprite.class;

        HP = HT = 120;
        defenseSkill = 22;
        atkSkill = 32;
        dmgRed = 14;
        dmgMin = 12;
        dmgMax = 30;

        EXP = 11;
        maxLvl = 28;
        mobType = MOBTYPE_TOUGH;
        TYPE_ANIMAL = true;
        TYPE_EVIL = true;

        state = HUNTING;
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
    public String description() {
        return
                "The Minotaur is half-man, half-bull and all angry. " +
                "It prowls the dungeon and gores intruders with it's mighty horns.";
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

    private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
    static {
        RESISTANCES.add( Grim.class );
        RESISTANCES.add( ScrollOfPsionicBlast.class );
    }

    { resistances.addAll( RESISTANCES ); }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
    static {
        IMMUNITIES.add( Terror.class );
        IMMUNITIES.add( Vertigo.class );
        IMMUNITIES.add( Charm.class );
    }

    { immunities.addAll( IMMUNITIES ); }
}
