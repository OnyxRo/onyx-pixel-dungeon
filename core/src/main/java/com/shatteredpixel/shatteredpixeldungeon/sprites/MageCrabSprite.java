package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.MageCrab;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class MageCrabSprite extends MobSprite {

    public MageCrabSprite() {
        super();

        texture( Assets.Sprites.MAGECRAB );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 5, true );
        idle.frames( frames, 0, 1, 0, 2 );

        run = new Animation( 15, true );
        run.frames( frames, 3, 4, 5, 6 );

        attack = new Animation( 12, false );
        attack.frames( frames, 7, 8, 9 );

        die = new Animation( 12, false );
        die.frames( frames, 10, 11, 12, 13 );

        play( idle );
    }

    @Override
    public int blood() {
        return 0xFFFFEA80;
    }

    public void zap( int cell ) {

        turnTo( ch.pos , cell );
        play( zap );

        MagicMissile.boltFromChar( parent,
                MagicMissile.FROST,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((MageCrab)ch).onZapComplete();
                    }
                } );
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
    }

    @Override
    public void onComplete( Animation anim ) {
        if (anim == zap) {
            idle();
        }
        super.onComplete( anim );
    }
}
