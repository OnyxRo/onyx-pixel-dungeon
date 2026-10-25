package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class ZeroTombSprites extends MobSprite {
    public ZeroTombSprites(){
        super();

        texture( Assets.Sprites.TOMB );

        TextureFilm frames = new TextureFilm( texture, 16, 32 );

        idle = new MovieClip.Animation( 12, true );
        idle.frames( frames, 1);

        run = new MovieClip.Animation( 14, true );
        run.frames( frames, 0,1 ); //magicling, the tomb sheet has two frames, the mod named frames past its end

        attack = new MovieClip.Animation( 14, false );
        attack.frames( frames, 0,1 );

        die = new MovieClip.Animation( 11, false );
        die.frames( frames, 1 );

        play( idle );
    }
}

