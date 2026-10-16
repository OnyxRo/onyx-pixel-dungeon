/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
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
package com.shatteredpixel.shatteredpixeldungeon.sprites.deep;

import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.deep.*;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Shuriken;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class GnollThiefBossSprite extends MobSprite {

    private int cellToAttack;

    public GnollThiefBossSprite() {
        super();

        texture( Assets.GnollThief );

        TextureFilm frames = new TextureFilm( texture, 15, 15 );

        idle = new Animation( 2, true );
        idle.frames( frames, 11, 11, 11, 12, 11, 11, 12, 12 );

        run = new Animation( 12, true );
        run.frames( frames, 15, 16, 17, 18 );

        attack = new Animation( 12, false );
        attack.frames( frames, 13, 14, 11 );
        zap = attack.clone();

        die = new Animation( 12, false );
        die.frames( frames, 19, 20, 21 );

        play( idle );
    }
    @Override
    public void attack( int cell ) {
        if (!Dungeon.level.adjacent( cell, ch.pos )) {

            cellToAttack = cell;
            turnTo( ch.pos , cell );
            play( zap );

        } else {

            super.attack( cell );

        }
    }

    @Override
    public void onComplete( Animation anim ) {
        if (anim == zap) {
            idle();

            ((MissileSprite)parent.recycle( MissileSprite.class )).
                    reset( ch.pos, cellToAttack, new Shuriken(), new Callback() {
                        @Override
                        public void call() {
                            ch.onAttackComplete();
                        }
                    } );
        } else {
            super.onComplete( anim );
        }
    }
}
