package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class DrillBigSprite extends MobSprite {

    public DrillBigSprite() {
        super();
        perspectiveRaise = -4.9f;

        shadowHeight = 0.9f;
        shadowWidth     = 1.0f;
        shadowOffset    = 0.25f;


        texture( Assets.Sprites.DRILLBIG );

        TextureFilm frames = new TextureFilm( texture, 260, 174 );

        idle = new Animation( 30, true );
        idle.frames( frames, 0, 1);

        run = attack  = idle.clone();

        die = new Animation(15,true);
        die.frames(frames, 0,1,0,1,0,0,0,1,0,0,0,1,0,0);

        flipHorizontal = false;

        play( idle );
    }

    @Override
    public void turnTo(int from, int to) {
        flipHorizontal = false;
    }
}