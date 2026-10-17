package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.watabou.noosa.TextureFilm;

public class NightmareBossRiftSprite extends MobSprite {
	private Animation broken;


	public NightmareBossRiftSprite() {
		super();
		
		texture( Assets.Sprites.PORTALBOSSENEMY );
		
		TextureFilm frames = new TextureFilm( texture, 17, 30);
		
		idle = new Animation( 9, true );
		idle.frames( frames, 0,1,2,3,4,5,6,7);
		
		run = idle.clone();
		
		attack = idle.clone();
		broken = new Animation( 11, true );
		broken.frames( frames, 8,9,10,11,12,13,14,15);
		
		die = new Animation( 8, false );
		die.frames( frames, 16,17,18,19,20,21,22,23 );
		
		play( idle );
	}

	public void broken(){
		play(broken);
	}

	@Override
	public void idle() {
		if (ch==null) play (idle); else linkVisuals(ch);
	}

	public int blood() {
		return 0xFFBB00BB;
	}
	@Override
	public void linkVisuals(Char ch) {
		super.linkVisuals(ch);
		if (ch.HP<0.5f*ch.HT) {
			broken();
		} else play(idle);

	}

	@Override
	public void move(int from, int to) {
		linkVisuals(ch);
		super.move(from, to);
		linkVisuals(ch);
	}
}
