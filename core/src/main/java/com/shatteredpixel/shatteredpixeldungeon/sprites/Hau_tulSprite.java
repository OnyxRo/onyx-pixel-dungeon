package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class Hau_tulSprite extends MobSprite {

    public Hau_tulSprite() {

        texture( Assets.Sprites.DARK_MAGE );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 7, true );
        idle.frames( frames, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 );

        die = new Animation( 4, true );
        die.frames( frames, 8, 9, 10, 11, 12 );

        play( idle );
    }
}
