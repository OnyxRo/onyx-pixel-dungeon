/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HeroDisguise;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.RectF;

public class HeroSprite extends CharSprite {
	//Tower PD, the cast animation of the hero spells
	public void castSpell(com.watabou.utils.Callback callback){
		animCallback = callback;
		play(read);
	}

	
	private static final int FRAME_WIDTH	= 12;
	private static final int FRAME_HEIGHT	= 15;
	
	private static final int RUN_FRAMERATE	= 20;
	
	private static TextureFilm tiers;
	
	protected Animation fly; //rebluearchive, protected for RBASprites
	protected Animation read; //rebluearchive, protected for RBASprites

	public HeroSprite() {
		super();
		
		texture( Dungeon.hero.heroClass.spritesheet() );
		updateArmor();
		updateHair( Dungeon.hero.hairColor().ID() );
		
		link( Dungeon.hero );

		if (ch.isAlive())
			idle();
		else
			die();
	}

	//tint for each hair color ID, in the order of Shittened's HairColor
	private static final int[] HAIR_TINTS = {
			-1, 0xFF8800, 0xFFE000, 0x40C040, 0x40C0FF, 0x20C0A8, 0x9040D0, 0xFF70B0,
			0xC09060, 0x303030, 0x909090, 0xF0F0F0, 0x8B5A2B, 0xC02020, 0x5A3A1A
	};

	public void updateHair( int hairID ) {
		if (hairID <= 0 || hairID >= HAIR_TINTS.length) {
			resetColor();
		} else {
			tint( HAIR_TINTS[hairID], 0.3f );
		}
	}

	public void disguise(HeroClass cls){
		texture( cls.spritesheet() );
		updateArmor();
	}
	
	public void updateArmor() {

		TextureFilm film = new TextureFilm( tiers(), Dungeon.hero.tier(), FRAME_WIDTH, FRAME_HEIGHT );
		//rebluearchive, the students are drawn 12 by 17 with one look for every armor
		if (com.shatteredpixel.shatteredpixeldungeon.actors.hero.RBAHero.isStudent(Dungeon.hero)
				&& Dungeon.hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HeroDisguise.class) == null){
			film = new TextureFilm( texture, FRAME_WIDTH, RBASprites.STUDENT_HEIGHT );
		}
		
		idle = new Animation( 1, true );
		idle.frames( film, 0, 0, 0, 1, 0, 0, 1, 1 );
		
		run = new Animation( RUN_FRAMERATE, true );
		run.frames( film, 2, 3, 4, 5, 6, 7 );
		
		die = new Animation( 20, false );
		die.frames( film, 8, 9, 10, 11, 12, 11 );
		
		attack = new Animation( 15, false );
		attack.frames( film, 13, 14, 15, 0 );
		
		zap = attack.clone();
		
		operate = new Animation( 8, false );
		operate.frames( film, 16, 17, 16, 17 );
		
		fly = new Animation( 1, true );
		fly.frames( film, 18 );

		read = new Animation( 20, false );
		read.frames( film, 19, 20, 20, 20, 20, 20, 20, 20, 20, 19 );
		
		if (Dungeon.hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBAAvantGardeKunBuff.OnBoard.class) != null) RBASprites.replaceAvantGardeSprite(this); //rebluearchive

		if (Dungeon.hero.isAlive())
			idle();
		else
			die();
	}

	@Override
	public void place( int p ) {
		super.place( p );
		if (Game.scene() instanceof GameScene) Camera.main.panFollow(this, 5f);
	}

	@Override
	public void move( int from, int to ) {
		super.move( from, to );
		if (ch != null && ch.flying) {
			play( fly );
		}
		Camera.main.panFollow(this, 20f);
	}

	@Override
	public void idle() {
		super.idle();
		if (ch != null && ch.flying) {
			play( fly );
		}
	}

	@Override
	public void jump( int from, int to, float height, float duration,  Callback callback ) {
		super.jump( from, to, height, duration, callback );
		play( fly );
		Camera.main.panFollow(this, 20f);
	}

	public synchronized void read() {
		animCallback = new Callback() {
			@Override
			public void call() {
				idle();
				ch.onOperateComplete();
			}
		};
		play( read );
	}

	@Override
	public void bloodBurstA(PointF from, int damage) {
		//Does nothing.

		/*
		 * This is both for visual clarity, and also for content ratings regarding violence
		 * towards human characters. The heroes are the only human or human-like characters which
		 * participate in combat, so removing all blood associated with them is a simple way to
		 * reduce the violence rating of the game.
		 */
	}

	@Override
	public void update() {
		sleeping = ch.isAlive() && ((Hero)ch).resting;
		
		super.update();
	}
	
	public void sprint( float speed ) {
		run.delay = 1f / speed / RUN_FRAMERATE;
	}
	
	public static TextureFilm tiers() {
		if (tiers == null) {
			SmartTexture texture = TextureCache.get( Assets.Sprites.ROGUE );
			tiers = new TextureFilm( texture, texture.width, FRAME_HEIGHT );
		}
		
		return tiers;
	}

	public static Image avatar( Hero hero ){
		if (hero.buff(HeroDisguise.class) != null){
			return avatar(hero.buff(HeroDisguise.class).getDisguise(), hero.tier());
		} else {
			return avatar(hero.heroClass, hero.tier());
		}
	}
	
	public static Image avatar( HeroClass cl, int armorTier ) {
		//rebluearchive, the students' first frame, 12 by 17
		if (com.shatteredpixel.shatteredpixeldungeon.actors.hero.RBAHero.isStudent(cl)){
			Image avatar = new Image( cl.spritesheet() );
			avatar.frame( avatar.texture.uvRect( 0, 0, FRAME_WIDTH, RBASprites.STUDENT_HEIGHT ) );
			return avatar;
		}
		
		RectF patch = tiers().get( armorTier );
		Image avatar = new Image( cl.spritesheet() );
		RectF frame = avatar.texture.uvRect( 1, 0, FRAME_WIDTH, FRAME_HEIGHT );
		frame.shift( patch.left, patch.top );
		avatar.frame( frame );
		
		return avatar;
	}
}
