package com.shatteredpixel.shatteredpixeldungeon.effects.particles;

import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class RBASilverParticle extends PixelParticle.Shrinking {
    public static final Emitter.Factory FACTORY = new Emitter.Factory() {
        @Override
        public void emit( Emitter emitter, int index, float x, float y ) {
            ((RBASilverParticle)emitter.recycle( RBASilverParticle.class )).reset( x, y );
        }
    };

    public RBASilverParticle() {
        super();

        color( 0xCCCCCC );
        lifespan = 0.2f;

        acc.set( Random.Float(-5, +5), Random.Float(-5, +5) );
    }

    public void reset( float x, float y ) {
        revive();

        this.x = x;
        this.y = y;

        left = lifespan;

        size = 5;
        speed.set( 0 );
    }

    @Override
    public void update() {
        super.update();
        float p = left / lifespan;
        am = p > 0.6f ? (1 - p) * 2.5f : 1;
    }
}
