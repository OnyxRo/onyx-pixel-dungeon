package com.shatteredpixel.shatteredpixeldungeon.sprites;
//magicling, the mod's tumulus wisp sprite, our WispSprite is Devoted's

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class TumulusWispSprite extends MobSprite {

    public TumulusWispSprite() {
        super();

        texture( "sprites/magicling/tumulus/wisp.png");

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 5, true );
        idle.frames( frames, 0,1,2,3,4,5,6,7);

        run = new Animation( 10, true );
        run.frames( frames, 8,9,10,11,12,13 );

        attack = new Animation( 14, false );
        attack.frames( frames, 14,15,16,17,18);

        die = new Animation( 14, false );
        die.frames( frames, 19,20,21,22,23,24);

        play( idle );
    }

}
