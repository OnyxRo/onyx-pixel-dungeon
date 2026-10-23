/*
 * Onyx's Pixel Dungeon
 * DERanged PD's Rat King hero picture (from Rat King PD 2 by zrp200), GPL-3.0. Its frames are 16 by 17 and laid
 * out its own way, so HeroSprite asks this class for the King's animations instead of reading a tier row.
 */
package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.Image;
import com.watabou.noosa.MovieClip.Animation;
import com.watabou.noosa.TextureFilm;

public class RatKingHeroSprite {

	private static final int FRAME_WIDTH = 16;
	private static final int FRAME_HEIGHT = 17;

	static void setup( HeroSprite s ){
		TextureFilm film = new TextureFilm( s.texture, FRAME_WIDTH, FRAME_HEIGHT );

		s.idle = new Animation( 2, true );
		s.idle.frames( film, 0, 0, 0, 1 );

		s.run = new Animation( 20, true );
		s.run.frames( film, 6, 7, 8, 9, 10 );

		s.attack = new Animation( 15, false );
		s.attack.frames( film, 2, 3, 4, 5, 0 );

		s.die = new Animation( 10, false );
		s.die.frames( film, 11, 12, 13, 14 );

		s.zap = s.attack.clone();

		s.operate = new Animation( 8, false );
		s.operate.frames( film, 2, 6, 2, 6 );

		s.fly = new Animation( 1, true );
		s.fly.frames( film, 0 );

		s.read = s.operate;
	}

	static Image avatar(){
		Image avatar = new Image( Assets.Sprites.RAT_KING_HERO );
		avatar.frame( avatar.texture.uvRect( 0, 0, FRAME_WIDTH, FRAME_HEIGHT ) );
		return avatar;
	}
}
