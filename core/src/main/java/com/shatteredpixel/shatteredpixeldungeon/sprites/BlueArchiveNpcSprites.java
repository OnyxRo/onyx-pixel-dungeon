/*
 * Onyx's Pixel Dungeon
 *
 * The NPC pictures of Blue Archive PD and Re Blue Archive PD (Hoto-Mocha). Sora keeps the shop,
 * Cherino works the forge and Akira stands in for the Imp. BlueArchiveModes decides when they show,
 * each Shattered sprite calls in here once at the end of its constructor.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.BlueArchiveModes;
import com.watabou.noosa.MovieClip;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.TextureFilm;

public class BlueArchiveNpcSprites {

	public static final String SORA    = "sprites/bluearchive/sora.png";
	public static final String CHERINO = "sprites/bluearchive/cherino.png";
	public static final String AKIRA   = "sprites/rebluearchive/akira.png";

	//true when the picture was swapped, so the sprite can skip its own extras (the shopkeeper's coin)
	public static boolean shopkeeper( MobSprite s ){
		if (!BlueArchiveModes.studentNpcs()) return false;
		s.texture( SORA );
		TextureFilm film = new TextureFilm( s.texture, 13, 16 );

		s.idle = new MovieClip.Animation( 4, true );
		s.idle.frames( film, 1, 1, 1, 0 );

		s.operate = new MovieClip.Animation( 4, false );
		s.operate.frames( film, 2, 3, 2, 3 );

		s.die = new MovieClip.Animation( 20, false );
		s.die.frames( film, 0 );

		s.run = s.idle.clone();
		s.attack = s.idle.clone();

		s.idle();
		return true;
	}

	//both mods took the shopkeeper's coin away
	public static boolean isSora( MobSprite s ){
		return s.texture != null && s.texture == TextureCache.get( SORA );
	}

	public static boolean blacksmith( MobSprite s ){
		if (!BlueArchiveModes.studentNpcs()) return false;
		s.texture( CHERINO );
		TextureFilm frames = new TextureFilm( s.texture, 13, 18 );

		s.idle = new MovieClip.Animation( 2, true );
		s.idle.frames( frames, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 2, 3, 4, 2, 3, 4 );

		s.run = new MovieClip.Animation( 20, true );
		s.run.frames( frames, 0 );

		s.die = new MovieClip.Animation( 20, false );
		s.die.frames( frames, 0 );

		s.play( s.idle );
		return true;
	}

	public static boolean imp( MobSprite s ){
		if (!BlueArchiveModes.akiraImp()) return false;
		s.texture( AKIRA );
		TextureFilm frames = new TextureFilm( s.texture, 13, 17 );

		s.idle = new MovieClip.Animation( 2, true );
		s.idle.frames( frames, 0, 0, 0, 0, 0, 1 );

		s.run = new MovieClip.Animation( 20, true );
		s.run.frames( frames, 0 );

		s.die = new MovieClip.Animation( 10, false );
		s.die.frames( frames, 0 );

		s.play( s.idle );
		return true;
	}
}
