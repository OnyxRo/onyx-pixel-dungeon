/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Cthulhu;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.RectF;

//Cthulhu, drawn from the animation page in art-for-design/cthulhu-animations.
//He fills the pit with his eight floor tentacles, so the sprite is one big fixed picture and every animation has its own sheet.
public class CthulhuSprite extends CharSprite {

	public static final String DIR = "sprites/cthulhu/";
	//the beam sheet charges up to this frame and fires after it
	private static final int BEAM_FIRE = 16;

	private Animation idle1, idle3, scream, eye, mind1, mind3, beamCharge, beamFire;

	private int phase = 1;

	public CthulhuSprite() {
		super();
		perspectiveRaise = 0;

		idle1 = sheet( "idle1", CthulhuArt.IDLE1, 0, -1, true );
		idle3 = sheet( "idle3", CthulhuArt.IDLE3, 0, -1, true );
		scream = sheet( "hurt", CthulhuArt.HURT, 0, -1, false );
		eye = sheet( "eye", CthulhuArt.EYE, 0, -1, false );
		mind1 = sheet( "mind1", CthulhuArt.MIND1, 0, -1, false );
		mind3 = sheet( "mind3", CthulhuArt.MIND3, 0, -1, false );
		beamCharge = sheet( "beam", CthulhuArt.BEAM, 0, BEAM_FIRE, false );
		beamFire = sheet( "beam", CthulhuArt.BEAM, BEAM_FIRE, -1, false );
		die = sheet( "death", CthulhuArt.DEATH, 0, -1, false );

		idle = idle1;
		run = idle1;
		//never played, but CharSprite treats finishing these as an attack or an action, so they must not be the looping idle
		attack = idle1.clone(); attack.looped = false;
		operate = attack.clone();
		zap = attack.clone();
		play( idle );
	}

	private static class Sheet extends Animation {
		final SmartTexture tex;
		Sheet( SmartTexture tex, boolean looped ){
			super( 12, looped );
			this.tex = tex;
		}
	}

	private static Sheet sheet( String name, int[] seq, int from, int to, boolean looped ){
		SmartTexture tx = TextureCache.get( DIR + name + ".png" );
		TextureFilm film = new TextureFilm( tx, CthulhuArt.BOSS_W, CthulhuArt.BOSS_H );
		int end = to < 0 ? seq.length : to;
		Sheet a = new Sheet( tx, looped );
		RectF[] frames = new RectF[end - from];
		for (int i = from; i < end; i++) frames[i - from] = film.get( seq[i] );
		a.frames = frames;
		return a;
	}

	@Override
	public synchronized void play( Animation anim, boolean force ) {
		if (anim instanceof Sheet && texture != ((Sheet) anim).tex) texture( ((Sheet) anim).tex );
		super.play( anim, force );
	}

	@Override
	public void link( Char ch ) {
		super.link( ch );
		if (ch instanceof Cthulhu) phase( ((Cthulhu) ch).phase() );
	}

	//the picture is fixed over the pit, whatever cell he is linked to
	@Override
	public PointF worldToCamera( int cell ) {
		int w = Dungeon.level.width();
		return new PointF( CthulhuArt.BOSS_X + (cell % w - 20) * 16, CthulhuArt.BOSS_Y + (cell / w - 21) * 16 );
	}

	@Override
	public void turnTo( int from, int to ) {
		//never flipped
	}

	@Override
	public void update() {
		super.update();
		//he is far bigger than his cell, so he stays drawn when that one cell is out of sight
		visible = true;
	}

	public void phase( int p ){
		phase = p;
		boolean wasIdle = curAnim == idle;
		idle = p >= 3 ? idle3 : idle1;
		if (wasIdle) play( idle );
	}

	private void once( Animation anim, Callback after ){
		play( anim, true );
		animCallback = new Callback() {
			@Override
			public void call() {
				idle();
				if (after != null) after.call();
			}
		};
	}

	public void scream(){ once( scream, null ); }

	public void openEye(){ once( eye, null ); }

	public void mindBlast(){ once( phase >= 3 ? mind3 : mind1, null ); }

	public void chargeBeam(){ once( beamCharge, null ); }

	public void fireBeam(){ once( beamFire, null ); }

	//where the beam leaves his third eye
	public static PointF eye(){
		return new PointF( CthulhuArt.EYE_X, CthulhuArt.EYE_Y );
	}

	//One of the page's effect animations played from a sheet at a fixed spot, for the tentacle strike, the Void Grasp and the cleanse
	public static class Fx extends Image {

		public interface Hold { boolean active(); }

		private final TextureFilm film;
		private final int[] seq;
		private float time;
		private int holdFrame = -1;
		private Hold hold;
		private int callFrame = -1;
		private Callback atFrame;
		private Callback onEnd;

		public Fx( String sheet, int w, int h, int[] seq, float x, float y ){
			super( DIR + sheet + ".png" );
			film = new TextureFilm( texture, w, h );
			this.seq = seq;
			frame( film.get( seq[0] ) );
			this.x = x;
			this.y = y;
		}

		//stays on this frame while the hold is active
		public Fx hold( int frame, Hold hold ){
			holdFrame = frame;
			this.hold = hold;
			return this;
		}

		public Fx at( int frame, Callback c ){
			callFrame = frame;
			atFrame = c;
			return this;
		}

		public Fx onEnd( Callback c ){
			onEnd = c;
			return this;
		}

		@Override
		public void update() {
			super.update();
			time += Game.elapsed;
			int f = (int)(time * 12);
			if (holdFrame >= 0 && f >= holdFrame && hold != null && hold.active()){
				f = holdFrame;
				time = holdFrame / 12f;
			}
			if (atFrame != null && f >= callFrame){
				Callback c = atFrame;
				atFrame = null;
				c.call();
			}
			if (f >= seq.length){
				if (atFrame != null){
					Callback c = atFrame;
					atFrame = null;
					c.call();
				}
				if (onEnd != null) onEnd.call();
				killAndErase();
				return;
			}
			frame( film.get( seq[f] ) );
		}

		//a still picture of one frame of a sheet, for the void holes and his remains
		public static Image still( String sheet, int w, int h, int[] seq, int index, float x, float y ){
			Image im = new Image( DIR + sheet + ".png" );
			im.frame( new TextureFilm( im.texture, w, h ).get( seq[index] ) );
			im.x = x;
			im.y = y;
			return im;
		}
	}
}
