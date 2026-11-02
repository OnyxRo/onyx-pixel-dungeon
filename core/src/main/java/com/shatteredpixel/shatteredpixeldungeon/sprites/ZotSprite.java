/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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

import com.watabou.utils.PathFinder;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class ZotSprite extends MobSprite {

	private Animation cast;
	
	public ZotSprite() {
		super();

		texture(Assets.Sprites.ZOT);

		TextureFilm frames = new TextureFilm(texture, 18, 18);

		idle = new Animation(2, true);
		idle.frames(frames, 0, 0, 0, 1, 0);

		run = new Animation(8, false);
		run.frames(frames, 0, 1, 2);

		attack = new Animation(8, false);
		attack.frames(frames, 0, 2, 2);
		
		cast = new Animation(8, false);
		cast.frames(frames, 2, 3, 4);

		die = new Animation(8, false);
		die.frames(frames, 0, 5, 6, 7, 8, 9, 8);

		play(run.clone());
	}

	@Override
	public void move(int from, int to) {

		place(to);

		play(run);
		turnTo(from, to);

		isMoving = true;

		if (Dungeon.level.water[to]) {
			GameScene.ripple(to);
		}

		ch.onMotionComplete();
	}

	@Override
	public void attack(int cell) {
		if (!Dungeon.level.adjacent(cell, ch.pos)) {
			//Char enemy = Actor.findChar(cell);
				  ((MissileSprite) parent.recycle(MissileSprite.class)).reset(ch.pos,
					cell, new com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingStone(), new Callback() {
						@Override
						public void call() {
							ch.onAttackComplete();
						}
				});
		 	  
		  			  		
			play(cast);
			turnTo(ch.pos, cell);
			explode(cell);			
			
		} else {

			super.attack(cell);

		}
	}

	public void explode(int cell) {
		
		Sample.INSTANCE.play(Assets.Sounds.BLAST, 2);

		if (Dungeon.level.heroFOV[cell]) {
			CellEmitter.center(cell).burst(BlastParticle.FACTORY, 30);
		}

		boolean terrainAffected = false;
		for (int n : PathFinder.NEIGHBOURS9) {
			int c = cell + n;
			if (c >= 0 && c < Dungeon.level.length()) {
				
				if (Dungeon.level.flamable[c]) {
					Level.set(c, Terrain.EMBERS);
					GameScene.updateMap(c);
					terrainAffected = true;
				}

				Char ch = Actor.findChar(c);
				if (ch != null && ch==Dungeon.hero) {
					// those not at the center of the blast take damage less
					// consistently.
					int minDamage = c == cell ? Dungeon.depth + 5 : 1;
					int maxDamage = 10 + Dungeon.depth;

					int dmg = Random.NormalIntRange(minDamage, maxDamage)
							- Math.max(ch.drRoll(),0);
					if (dmg > 0) {
						ch.damage(dmg, this,2);
					}
				}
			}
		}

		if (terrainAffected) {
			Dungeon.observe();
		}
		
	}

	
	@Override
	public void onComplete(Animation anim) {
		if (anim == run) {
			isMoving = false;
			idle();
		} else {
			super.onComplete(anim);
		}
	}
}
