package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class AdultElementalSprite extends MobSprite {
    public AdultElementalSprite() {
        super();

        texture( Assets.Sprites.ADULTELEMENTAL );

        TextureFilm frames = new TextureFilm( texture, 12, 14 );

        idle = new Animation( 10, true );
        idle.frames( frames, 0, 1, 2 );

        run = new Animation( 12, true );
        run.frames( frames, 0, 1, 3 );

        attack = new Animation( 15, false );
        attack.frames( frames, 4, 5, 6 );

        die = new Animation( 15, false );
        die.frames( frames, 7, 8, 9, 10, 11, 12, 13, 12 );

        play( idle );
    }
}
