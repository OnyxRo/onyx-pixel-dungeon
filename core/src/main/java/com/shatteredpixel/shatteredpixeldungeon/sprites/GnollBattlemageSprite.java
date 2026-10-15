package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GnollBattlemage;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

/**
 * Cursed PD's Gnoll Battlemage uses the shaman's look, but ShamanSprite finishes its bolt by casting its
 * character to Shaman and is abstract here. This does the same with a battlemage.
 */
public class GnollBattlemageSprite extends ShamanSprite.Purple {

	@Override
	public void zap( int cell ) {
		turnTo( ch.pos, cell );
		play( zap );

		MagicMissile.boltFromChar( parent,
				boltType,
				this,
				cell,
				new Callback() {
					@Override
					public void call() {
						((GnollBattlemage)ch).call();
					}
				} );
		Sample.INSTANCE.play( Assets.Sounds.ZAP );
	}
}
