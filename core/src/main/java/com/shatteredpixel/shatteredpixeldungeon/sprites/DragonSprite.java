package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class DragonSprite extends  MobSprite {

    public DragonSprite() {
        super();

        texture( Assets.Sprites.PETDRAGON );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new MovieClip.Animation( 2, true );
        idle.frames( frames, 0, 0, 0, 1 );

        attack = new MovieClip.Animation( 10, true );
        attack.frames( frames, 6, 7, 8, 9, 10 );

        run = new MovieClip.Animation( 15, false );
        run.frames( frames, 2, 3, 4, 5, 0 );

        die = new MovieClip.Animation( 10, false );
        die.frames( frames, 11, 12, 13, 14 );

        play( idle );
    }
}
