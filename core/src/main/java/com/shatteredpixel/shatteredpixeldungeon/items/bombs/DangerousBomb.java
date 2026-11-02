/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
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
package com.shatteredpixel.shatteredpixeldungeon.items.bombs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class DangerousBomb extends Bomb {

	{
		//name = "cluster bomb";
		image = ItemSpriteSheet.BOMB;
	}

	@Override
	public void explode(int cell) {
		super.explode(cell);
		boolean terrainAffected = false;
		PathFinder.buildDistanceMap( cell, BArray.not( Dungeon.level.solid, null ), 2 );
		for (int i = 0; i < PathFinder.distance.length; i++) {
			if (PathFinder.distance[i] < Integer.MAX_VALUE) {
				if (i >= 0 && i < Dungeon.level.length()) {
					if (Dungeon.level.heroFOV[i]) {
						CellEmitter.get(i).burst(SmokeParticle.FACTORY, 4);
					}

					if (Dungeon.level.flamable[i]) {
						Level.set(i, Terrain.EMBERS);
						GameScene.updateMap(i);
						terrainAffected = true;
					}

					if ((Dungeon.level.map[i] == Terrain.WALL || Dungeon.level.map[i] == Terrain.WALL) && Dungeon.level.insideMap(i)) {
						Level.set(i, Terrain.EMPTY);
						GameScene.updateMap(i);
						terrainAffected = true;
					}

					// destroys items / triggers bombs caught in the blast.
					Heap heap = Dungeon.level.heaps.get(i);
					if (heap != null)
						heap.explode();

					Char ch = Actor.findChar(i);
					if (ch != null) {
						// those not at the center of the blast take damage less
						// consistently.
						if (ch == Dungeon.hero) {

							int minDamage = ch.HT / 8;
							int maxDamage = ch.HT / 4;

							int dmg = Random.NormalIntRange(minDamage, maxDamage)
									- Math.max(ch.drRoll(), 0);
							if (dmg > 0) {
								ch.damage(dmg, this,1);
							}
							//if (ch == Dungeon.hero && !ch.isAlive())
								// constant is used here in the rare instance a player
								// is killed by a double bomb.
								//Dungeon.fail(Messages.format(ResultDescriptions.LOSE));
						}
					}
				}
			}

			if (terrainAffected) {
				Dungeon.observe();
			}
		}
	}

	@Override
	public int value() {
		return 20 * quantity;
	}

}
