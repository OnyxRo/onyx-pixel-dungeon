package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.particles.Emitter;

//ported from Tower PD, its wisp of light. Tower PD drew it in an extra row of the elemental sheet
public class TowerWispSprite extends MobSprite {

	private Emitter particles;

	public TowerWispSprite() {
		super();

		int c = 70;
		texture( Assets.Sprites.TOWER_ELEMENTAL );
		TextureFilm frames = new TextureFilm( texture, 12, 14 );

		idle = new Animation( 10, true );
		idle.frames( frames, c+0, c+1, c+2 );

		run = new Animation( 12, true );
		run.frames( frames, c+0, c+1, c+3 );

		attack = new Animation( 15, false );
		attack.frames( frames, c+4, c+5, c+6 );

		die = new Animation( 15, false );
		die.frames( frames, c+7, c+8, c+9, c+10, c+11, c+12, c+13, c+12 );

		play( idle );
	}

	@Override
	public void link( Char ch ) {
		super.link( ch );
		if (particles == null) {
			particles = emitter();
			particles.pour( SparkParticle.STATIC, 0.06f );
		}
	}

	@Override
	public void update() {
		super.update();
		if (particles != null) particles.visible = visible;
	}

	@Override
	public void die() {
		super.die();
		if (particles != null) particles.on = false;
	}

	@Override
	public void kill() {
		super.kill();
		if (particles != null) particles.killAndErase();
	}

	@Override
	public int blood() {
		return 0xFFFFFF99;
	}
}
