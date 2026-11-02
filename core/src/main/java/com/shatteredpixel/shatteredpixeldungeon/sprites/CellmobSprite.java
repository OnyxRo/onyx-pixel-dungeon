package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam.DeathRay;
import com.watabou.noosa.TextureFilm;

public class CellmobSprite extends MobSprite {

	private int attackPos;

	public CellmobSprite() {
		super();

		texture(Assets.Sprites.CELL_MOB);

		TextureFilm frames = new TextureFilm(texture, 16, 18);

		idle = new Animation(2, true);
		idle.frames(frames, 1, 2, 3, 4);

		run = new Animation(12, true);
		run.frames(frames, 2, 3, 4);

		attack = new Animation(15, false);
		attack.frames(frames, 1, 5);

		die = new Animation(8, false);
		die.frames(frames, 1, 0, 6);

		play(idle);
	}

	@Override
	public void attack(int pos) {
		attackPos = pos;
		super.attack(pos);
	}
	

	@Override
	public void onComplete(Animation anim) {
		super.onComplete(anim);

		if (anim == attack) {
			if (Dungeon.level.heroFOV[ch.pos] || Dungeon.level.heroFOV[attackPos]) {
				parent.add(new DeathRay(center(), DungeonTilemap
						.tileCenterToWorld(attackPos)));
			}
		}
	}
}