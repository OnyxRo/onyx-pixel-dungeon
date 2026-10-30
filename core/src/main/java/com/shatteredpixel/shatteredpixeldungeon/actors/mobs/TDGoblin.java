/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Trapskill;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ReclaimTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollTricksterSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TDGoblinSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class TDGoblin extends Mob {

	{
		spriteClass = TDGoblinSprite.class;

		HP = HT = (1 + Dungeon.depth) * 3;
		EXP = 1 + Dungeon.depth/2;
		defenseSkill = attackSkill( null ) / 2;
		loot = new ReclaimTrap();
		lootChance = 1f;
	}

	@Override
	public void move( int step, boolean travelling ) {
		PathFinder.buildDistanceMap( pos, BArray.not( Dungeon.level.solid, null ), 3 );
		boolean laughed = false;
		if(state != WANDERING || this.buff(Terror.class) != null){
			for (int i = 0; i < PathFinder.distance.length; i++) {
				if (PathFinder.distance[i] < Integer.MAX_VALUE) {
					Trap t = Dungeon.level.traps.get(i);
					if (t != null && t.active) {
						//t.reveal();
						//t.activate();
						//t.disarm();
						t.trigger();
						if (!laughed) {
							if (Dungeon.level.heroFOV[pos]) {
								Sample.INSTANCE.play(Assets.Sounds.MIMIC, 0.7f, 0.7f, 3f);
								CellEmitter.get(i).start(Speck.factory(Speck.STAR), 0.05f, 16);
							}
							laughed = true;
						}
					}
				}
			}
		}
		super.move( step, travelling );
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( HT / 6, (HT / 3) +1 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 9 + Dungeon.depth;
	}
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 1+Dungeon.depth/2);
	}
}
