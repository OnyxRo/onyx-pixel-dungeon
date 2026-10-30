/*
 * Clairvoyant Pixel Dungeon, ported into Onyx's Pixel Dungeon (key clairvoyant)
 * https://github.com/ifritdiezel/Clairvoyant-Pixel-Dungeon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.StableElemental;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EnergyParticle;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;

public class StableElementalSprite extends MobSprite {

	private Emitter particles;

	public StableElementalSprite() {
		super();

		texture(Assets.Sprites.STABLE_ELEMENTAL);

		TextureFilm frames = new TextureFilm( texture, 12, 14 );

		idle = new Animation( 10, true );
		idle.frames( frames, 0, 1, 2 );

		run = new Animation( 12, true );
		run.frames( frames, 0, 1, 3 );

		attack = new Animation( 15, false );
		attack.frames( frames, 4, 5, 6 );

		zap = attack.clone();

		die = new Animation( 15, false );
		die.frames( frames, 7, 8, 9, 10, 11, 12, 13, 12 );

		play( idle );
	}

	@Override
	public void link( Char ch ) {
		super.link( ch );
		if (particles == null) {
			particles = emitter();
			particles.pour(EnergyParticle.FACTORY, 0.1f);
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

	public void zap( int cell ) {
		turnTo( ch.pos, cell );
		play( zap );
		MagicMissile.boltFromChar( parent, MagicMissile.RAINBOW, this, cell, new Callback() {
			@Override
			public void call() {
				if (ch instanceof StableElemental) ((StableElemental) ch).onZapComplete();
				else ch.next();
			}
		} );
		Sample.INSTANCE.play( Assets.Sounds.ZAP );
	}

	@Override
	public void onComplete( Animation anim ) {
		if (anim == zap) idle();
		super.onComplete( anim );
	}

	@Override
	public int blood() {
		return 0xFFFFBB33;
	}
}
