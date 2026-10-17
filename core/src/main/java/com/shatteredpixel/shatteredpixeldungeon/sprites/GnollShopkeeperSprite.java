package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class GnollShopkeeperSprite extends MobSprite {

	public GnollShopkeeperSprite() {
		super();
		
		texture( Assets.Sprites.GNOLLSHOPKEEPER );
		
		TextureFilm frames = new TextureFilm( texture, 12, 15 );
		
		idle = new Animation( 2, true );
		idle.frames( frames, 0, 0, 0, 1, 0, 0, 1, 1 );
		
		run = idle.clone();
		
		attack = idle.clone();
		
		die = idle.clone();
		
		play( idle );
	}
}
