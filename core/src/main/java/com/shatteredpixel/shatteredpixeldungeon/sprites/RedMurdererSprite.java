package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

//ported, magicling. The mod's MurdererSprite.RedMuderer, the red row of its mud thief sheet
public class RedMurdererSprite extends MobSprite {

	public RedMurdererSprite() {
		super();
		int c = 21;
		texture( Assets.Sprites.ML_MUDTHIEF );
		TextureFilm film = new TextureFilm( texture, 12, 13 );

		idle = new Animation( 1, true );
		idle.frames( film, 0+c, 0+c, 0+c, 1+c, 0+c, 0+c, 0+c, 0+c, 1+c );

		run = new Animation( 15, true );
		run.frames( film, 0+c, 0+c, 2+c, 3+c, 3+c, 4+c );

		die = new Animation( 10, false );
		die.frames( film, 5+c, 6+c, 7+c, 8+c, 9+c );

		attack = new Animation( 12, false );
		attack.frames( film, 10+c, 11+c, 12+c, 0+c );

		idle();
	}
}
