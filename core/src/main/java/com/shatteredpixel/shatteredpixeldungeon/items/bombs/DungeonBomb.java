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

import com.watabou.utils.PathFinder;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class DungeonBomb extends Bomb {

	{
		//name = "bomb";
		image = ItemSpriteSheet.BOMB;
	}

	@Override
	public void explode(int cell) {
		super.explode(cell);

		boolean terrainAffected = false;
		for (int n : PathFinder.NEIGHBOURS9) {
			int c = cell + n;
			if (c >= 0 && c < Dungeon.level.length()) {
				if (Dungeon.level.heroFOV[c]) {
					CellEmitter.get(c).burst(SmokeParticle.FACTORY, 4);
				}

				if (Dungeon.level.flamable[c]) {
					Level.set(c, Terrain.EMBERS);
					GameScene.updateMap(c);
					terrainAffected = true;
				}

				if ((Dungeon.level.map[c] == Terrain.WALL || Dungeon.level.map[c] == Terrain.WALL )&& Dungeon.level.insideMap(c)){
					Level.set(c, Terrain.EMPTY);
					GameScene.updateMap(c);
					terrainAffected = true;
				}

				Char ch = Actor.findChar(c);
				if (ch != null) {
					// those not at the center of the blast take damage less
					// consistently.
					if (ch.properties().contains(Char.Property.BOSS) || ch.properties().contains(Char.Property.MINIBOSS)){
					int minDamage = ch.HT/10;
					int maxDamage = ch.HT/5;

					int dmg = Random.NormalIntRange(minDamage, maxDamage)
							- Math.max(ch.drRoll(),0);
					if (dmg > 0) {
						ch.damage(dmg, this,1);
					}
					
					} else {
						
				
					int minDamage = ch.HT/5;
					int maxDamage = ch.HT/4;

					int dmg = Random.NormalIntRange(minDamage, maxDamage);
							//- Math.max(ch.drRoll(),0);
					if (dmg > 0) {
						ch.damage(dmg, this,1);
					}
				  }
				}
			}
		}

		if (terrainAffected) {
			Dungeon.observe();
		}
		
	}


	@Override
	public Item random() {
		switch (Random.Int(2)) {
		case 0:
		default:
			return this;
		case 1:
			return new DoubleBomb();
		}
	}


	@Override
	public int value() {
		return 10 * quantity;
	}

	public static class DoubleBomb extends DungeonBomb {

		{
			//name = "two bombs";
			image = ItemSpriteSheet.DBL_BOMB;
			stackable = false;
		}

		@Override
		public boolean doPickUp(Hero hero, int pos) {
			DungeonBomb bomb = new DungeonBomb();
			bomb.quantity(2);
			if (bomb.doPickUp(hero)) {
				// isaaaaac....
				hero.sprite.showStatus(CharSprite.NEUTRAL, "1+1 free!");
				return true;
			}
			return false;
		}
	}
}
