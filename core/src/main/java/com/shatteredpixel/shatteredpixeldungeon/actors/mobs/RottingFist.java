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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RottingFistSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class RottingFist extends UMob {

    private static final int REGENERATION	= 4;

    {
        name = "rotting fist";
        spriteClass = RottingFistSprite.class;

        HP = HT = 300;
        defenseSkill = 25;
        atkSkill = 36;
        dmgRed = 15;
        dmgMin = 24;
        dmgMax = 36;

        EXP = 0;

        TYPE_EVIL = true;
        state = WANDERING;
    }

    private String TXT_DESC = "The Rotting Fist is an extension of an ancient god into our dimension.";

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

    @Override
    public String description() {
        return TXT_DESC;

    }

    private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
    static {
        RESISTANCES.add( ToxicGas.class );
        RESISTANCES.add( Grim.class );
        RESISTANCES.add( ScrollOfPsionicBlast.class );
    }

    { resistances.addAll( RESISTANCES ); }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
    static {
        IMMUNITIES.add( Amok.class );
        IMMUNITIES.add( Sleep.class );
        IMMUNITIES.add( Terror.class );
        IMMUNITIES.add( Poison.class );
        IMMUNITIES.add( Vertigo.class );
    }

    { immunities.addAll( IMMUNITIES ); }
}
