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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArmorBreak;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Locked;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GraveProtectorSprite;
import com.watabou.utils.Random;

public class GraveProtector extends Mob{
	{
		spriteClass = GraveProtectorSprite.class;

		EXP = 1;
		state = HUNTING;
		//flying = true;
		
		HP = HT = 350;
		defenseSkill = 15;
		
		lootChance = 1f;
		
		properties.add(Property.TROLL);
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(8+Math.round(com.shatteredpixel.shatteredpixeldungeon.SpsStats.skeletonsKilled/10), 15+Math.round(com.shatteredpixel.shatteredpixeldungeon.SpsStats.skeletonsKilled/5));
	}

	@Override
	public int attackSkill( Char target) {
		return 20;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 8);
	}

	@Override
	protected boolean canAttack(Char enemy) {		if (buff(Locked.class) != null){
			return Dungeon.level.adjacent(pos, enemy.pos) && (!isCharmedBy(enemy));
		} else
		return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		if (Dungeon.level.adjacent(pos, enemy.pos)) {
			damage = damage / 2;
		} else {
			Buff.prolong(enemy,Slow.class,5f);
			Buff.affect(enemy,ArmorBreak.class,5f).level(20);
		}

		return damage;
	}

	{
		weakness.add(Blindness.class);
	}


}
