package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class GuardianSprite extends MobSprite {

    private int cellToAttack;

    public GuardianSprite() {
        super();

        texture( Assets.Sprites.GUARDIAN );

        TextureFilm frames = new TextureFilm( texture, 12, 15 );

        idle = new Animation( 2, true );
        idle.frames( frames, 0, 0, 0, 0, 0, 1, 1 );

        run = new Animation( 15, true );
        run.frames( frames, 2, 3, 4, 5, 6, 7 );



        attack = new Animation( 12, false );
        attack.frames( frames, 8, 9, 10 );
        zap = attack.clone();
        die = new Animation( 5, false );
        die.frames( frames, 11, 12, 13, 14, 15, 15 );

        play( idle );
    }

    @Override
    public int blood() {
        return 0xFFcdcdb7;
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
                    reset( ch.pos, cellToAttack, new GuardianShot(), new Callback() {
                        @Override
                        public void call() {
                            ch.onAttackComplete();
                        }
                    } );
        } else {
            super.onComplete( anim );
        }
    }

    public class GuardianShot extends Item {
        {
            image = ItemSpriteSheet.FISHING_SPEAR;
        }
    }
}

