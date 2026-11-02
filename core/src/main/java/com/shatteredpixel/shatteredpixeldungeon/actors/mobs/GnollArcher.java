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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Locked;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollArcherSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class GnollArcher extends Mob {
	{
		//name = "gnollarcher";
		spriteClass = GnollArcherSprite.class;

		HP = HT = 25 + com.shatteredpixel.shatteredpixeldungeon.SpsStats.archersKilled;
		defenseSkill = 5;

		EXP = 1;
			
		baseSpeed = 0.9f;

		state = WANDERING;
		
		properties.add(Property.ORC);

	}
	
	@Override
	public int attackSkill( Char target) {
		return 30;
	}

	@Override
	protected boolean canAttack(Char enemy) {
		Ballistica attack = new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE);
		if (buff(Locked.class) != null){
			return Dungeon.level.adjacent(pos, enemy.pos) && (!isCharmedBy(enemy));
		} else
		
		return !Dungeon.level.adjacent(pos, enemy.pos) && attack.collisionPos == enemy.pos;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(1+Math.round(com.shatteredpixel.shatteredpixeldungeon.SpsStats.archersKilled/10), 8+Math.round(com.shatteredpixel.shatteredpixeldungeon.SpsStats.archersKilled/5));
	}

	@Override
	public int drRoll() {
		return 0;
	}
	
	@Override
	protected boolean getCloser(int target) {
		if (Dungeon.level.adjacent(pos, enemy.pos)) {
			return getFurther(target);
		} else {
			return super.getCloser(target);
		}
	}

	@Override
	public void die(Object cause) {
		super.die(cause);
		if (Dungeon.depth > 25) {
		}

		com.shatteredpixel.shatteredpixeldungeon.SpsStats.archersKilled++;
		GLog.w(Messages.get(Mob.class, "killcount", com.shatteredpixel.shatteredpixeldungeon.SpsStats.archersKilled));


		if ( Dungeon.depth < 27) {
		} else {
		}

		if (com.shatteredpixel.shatteredpixeldungeon.SpsStats.archersKilled == 25) {
		}

		if (com.shatteredpixel.shatteredpixeldungeon.SpsStats.archersKilled == 50) {
		}

		if (com.shatteredpixel.shatteredpixeldungeon.SpsStats.archersKilled == 100) {
		}

		if (com.shatteredpixel.shatteredpixeldungeon.SpsStats.goldThievesKilled > 99 && com.shatteredpixel.shatteredpixeldungeon.SpsStats.skeletonsKilled > 99
				&& com.shatteredpixel.shatteredpixeldungeon.SpsStats.albinoPiranhasKilled > 99 && com.shatteredpixel.shatteredpixeldungeon.SpsStats.archersKilled == 100 ) {
		}
	}
}