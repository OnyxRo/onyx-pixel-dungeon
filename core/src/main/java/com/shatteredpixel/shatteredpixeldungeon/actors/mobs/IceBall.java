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
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SnowballSprite;
import com.watabou.utils.Callback;


public class IceBall extends Mob {
	{
		spriteClass = SnowballSprite.class;
		baseSpeed = 0.5f;

		HP = HT = 10;
		defenseSkill = 0;
		EXP = 1;
		maxLvl = 1;

		properties.add(Property.ELEMENT);
		properties.add(Property.MECH);
	}

	@Override
	protected boolean canAttack(Char enemy) {
		return false;
	}
	
	@Override
	protected Char chooseEnemy() {
		return null;
	}	
	
	@Override
	public int damageRoll() {
		return 100;
	}

	@Override
	public int attackSkill( Char target) {
		return 1000;
	}

	@Override
	public int drRoll() {
		return 0;
	}
	
	@Override
	public boolean add( Buff buff ) {
		return false;
	}

	@Override
	public void die(Object cause) {
		final int spos = pos;
		for (int n : new int[]{ +1 + Dungeon.level.width(), +1 - Dungeon.level.width(), -1 + Dungeon.level.width(), -1 - Dungeon.level.width() }) {
			final int cell = pos + n;
			if (Dungeon.level.passable[cell] && Actor.findChar(cell) == null) {
				Mob mob = com.shatteredpixel.shatteredpixeldungeon.actors.mobs.sps.SpsBestiary.mob(86);
				mob.pos = cell;
				mob.state = mob.HUNTING;
				GameScene.add(mob,1f);
				mob.sprite.jump(spos,cell, new Callback() {
					@Override
					public void call() {
						move(cell);
					}
				});
			}
		}



		   super.die(cause);

	}
	

}
