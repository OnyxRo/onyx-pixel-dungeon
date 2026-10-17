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
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GobRottingFistSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

//port-oldstats
public class RottingFist extends Mob {

    private static final int REGENERATION	= 4;

    {
        spriteClass = GobRottingFistSprite.class;

        HP = HT = 300;
        defenseSkill = 25;

        EXP = 0;

        state = WANDERING;
        immunities.add( Amok.class );
        immunities.add( Sleep.class );
        immunities.add( Paralysis.class );
        immunities.add( Terror.class );
        immunities.add( Poison.class );
        immunities.add( Vertigo.class );
        resistances.add( ToxicGas.class );
        resistances.add( Grim.class );
    }

    @Override
    public int attackSkill( Char target ) {
        return 36;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 24, 36 );
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange( 0, 15 );
    }


    private String TXT_DESC = "This Rotting Fist was once the right hand of the Sacred.";

    @Override
    public int attackProc( Char enemy, int damage ) {
        if (Random.Int(3) == 0) {
            Buff.affect(enemy, Ooze.class);
            enemy.sprite.burst( 0xFF000000, 5 );
        }

        return damage;
    }

    @Override
    public boolean act() {

        if (Dungeon.level.water[pos] && HP < HT) {
            sprite.emitter().burst( ShadowParticle.UP, 2 );
            HP += REGENERATION;
        }

        return super.act();
    }
}
