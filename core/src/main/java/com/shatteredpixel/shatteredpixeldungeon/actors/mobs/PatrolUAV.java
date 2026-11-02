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
package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.watabou.utils.PathFinder;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.effectblobs.ElectriShock;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EnergyParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.StoneOre;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLightning;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PatrolUAVSprite;
import com.watabou.utils.Random;

public class PatrolUAV extends Mob {
	

	{
		spriteClass = PatrolUAVSprite.class;

		HP = HT = 50+(Dungeon.depth *Random.NormalIntRange(1, 3));
		defenseSkill = adj(1);
		
		EXP = 5;
		maxLvl = 10;
		
		state = WANDERING;
		
		flying = true;
		
		loot = new StoneOre();
		lootChance = 0.4f;
		
		properties.add(Property.MECH);
	}

	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange(4, 7);
	}

	@Override
	public int attackSkill( Char target) {
		return 5;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(5, 10);
	}
	
	@Override
	public void die(Object cause) {

		super.die(cause);
		for (int i : PathFinder.NEIGHBOURS9) {
			if (Dungeon.level.insideMap(pos+i) && !Dungeon.level.solid[pos+i]) {
				GameScene.add(Blob.seed(pos + i, 3, ElectriShock.class));
				CellEmitter.get(pos + i).burst(EnergyParticle.FACTORY, 5);
			}
		}

	}

	
		{
			immunities.add(Amok.class);
			immunities.add(Sleep.class);
			immunities.add(Terror.class);
			immunities.add(Burning.class);
			immunities.add(Vertigo.class);
			immunities.add(ElectriShock.class);
			immunities.add(WandOfLightning.class);
		}

	
}
