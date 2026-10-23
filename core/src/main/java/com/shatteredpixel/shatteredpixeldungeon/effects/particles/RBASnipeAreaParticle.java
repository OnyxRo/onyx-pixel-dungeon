package com.shatteredpixel.shatteredpixeldungeon.effects.particles;

import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.Random;

public class RBASnipeAreaParticle extends PixelParticle.Shrinking {

	public static final Emitter.Factory FACTORY = new Emitter.Factory() {
		@Override
		public void emit( Emitter emitter, int index, float x, float y ) {
			((RBASnipeAreaParticle)emitter.recycle( RBASnipeAreaParticle.class )).reset( x, y );
		}
		@Override
		public boolean lightMode() {
			return false;
		}
	};

	public RBASnipeAreaParticle() {
		super();

		lifespan = 0.6f;

		color( 0xFFFF00 );
	}

	public void reset( float x, float y){
		revive();

		this.x = x;
		this.y = y;

		left = lifespan;
		size = 8;

		speed.set( Random.Float( -4, +4 ), Random.Float( -8, -16 ) );
	}

	@Override
	public void update() {
		super.update();

		am = 1 - left / lifespan;
	}

}
