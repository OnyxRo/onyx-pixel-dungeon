/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
 *
 * Lovecraft Pixel Dungeon
 * Copyright (C) 2016-2017 Leon Horn
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This Program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without eben the implied warranty of
 * GNU General Public License for more details.
 *
 * You should have have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses>
 */
package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.RainParticle;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.RandomL;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Storm extends Blob {
	
	@Override
	protected void evolve() {
		super.evolve();

		Char ch;
		int cell;

		for (int i = area.left; i < area.right; i++) {
			for (int j = area.top; j < area.bottom; j++) {
				cell = i + j*Dungeon.level.width();
				if (cur[cell] > 0 && (ch = Actor.findChar( cell )) != null) {
					if (!ch.isImmune(this.getClass())) {
						if(Random.Int(0, (int) ch.speed()*2) == 0 && RandomL.randomBoolean() && RandomL.randomBoolean()){
							GameScene.add(Blob.seed(cell, 1, Electricity.class));
						}
					}
				}
				if(Dungeon.level.map[cell] == Terrain.EMPTY ||
						Dungeon.level.map[cell] == Terrain.EMPTY_DECO ||
						Dungeon.level.map[cell] == Terrain.EMBERS ||
						Dungeon.level.map[cell] == Terrain.EMPTY_SP ||
						Dungeon.level.map[cell] == Terrain.GRASS ||
						Dungeon.level.map[cell] == Terrain.INACTIVE_TRAP){
						Dungeon.level.discover(cell);
					Dungeon.level.set(cell, Terrain.WATER);
					for (int n : PathFinder.NEIGHBOURS8) {
						int pos = cell+n;
						GameScene.updateMap(pos);
					}
				}
				if(Dungeon.level.map[cell] == Terrain.SECRET_TRAP){
					Dungeon.level.disarmTrap(Dungeon.level.map[cell]);
				}
			}
		}
	}
	
	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );
		
		emitter.pour(RainParticle.FACTORY, 1f);
	}
	
	@Override
	public String tileDesc() {
		return Messages.get(this, "desc");
	}
}
