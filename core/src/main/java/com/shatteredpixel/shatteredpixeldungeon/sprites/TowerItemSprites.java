package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;

//Tower PD items drawn after its first port. Each picture is its own small file under sprites/tower/, so items.png
//never grows (several ports run at once and all of them would edit it). An item image number from BASE up to
//BASE + PICTURES.length draws from these files. Add a picture by appending a row to PICTURES.
public final class TowerItemSprites {

	public static final int BASE = 0xE000;

	//file, width, height
	private static final Object[][] PICTURES = {
			{ "sprites/tower/curved_knife.png", 10, 11 },
	};

	public static final int CURVED_KNIFE = BASE + 0;

	public static boolean is( int image ){
		return image >= BASE && image < BASE + PICTURES.length;
	}

	private static String file( int image ){
		return (String)PICTURES[image - BASE][0];
	}

	private static int width( int image ){
		return (Integer)PICTURES[image - BASE][1];
	}

	private static int height( int image ){
		return (Integer)PICTURES[image - BASE][2];
	}

	/** Called first in ItemSprite.frame. Returns true when the picture was one of these. */
	static boolean frame( ItemSprite sprite, int image ){
		if (!is( image )) return false;
		SmartTexture tx = TextureCache.get( file( image ) );
		if (sprite.texture != tx) sprite.texture( tx );
		sprite.frame( tx.uvRect( 0, 0, width( image ), height( image ) ) );
		int h = height( image );
		if (h < 8) sprite.perspectiveRaise = (5 + 8 - h) / 16f;
		return true;
	}

	/** Called first in ItemSprite.pick, for the colour of one pixel. */
	static int pick( int image, int x, int y ){
		return TextureCache.get( file( image ) ).getPixel( x, y );
	}
}
