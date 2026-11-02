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

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DragonRiderSprite;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;


public class DragonRider extends Mob {
	{
		spriteClass = DragonRiderSprite.class;
		baseSpeed = 1.5f;

		HP = HT = 140+(adj(0)*Random.NormalIntRange(7, 5));
		defenseSkill = 30+adj(1);
		EXP = 14;
		maxLvl = 30;

		flying = true;

		properties.add(Property.DRAGON);
	}


	@Override
	public int damageRoll() {
		return Random.NormalIntRange(40, 50);
	}

	@Override
	public int attackSkill( Char target) {
		return 36+adj(1);
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(20, 40);
	}

	@Override
	public void die(Object cause) {

			Mob mob = com.shatteredpixel.shatteredpixeldungeon.actors.mobs.sps.SpsBestiary.mob(84);
			mob.pos = pos;
			mob.state = mob.HUNTING;
			GameScene.add(mob,1f);
		   mob.sprite.jump(pos-4,pos, new Callback() {
			@Override
			public void call() {
				move(pos);
			}
		});

		   super.die(cause);

	}
	
	
	@Override
	protected boolean doAttack(Char enemy) {

		return super.doAttack(enemy);

	}

	{
		resistances.add(ToxicGas.class);
		resistances.add(Poison.class);
		
	}
}
