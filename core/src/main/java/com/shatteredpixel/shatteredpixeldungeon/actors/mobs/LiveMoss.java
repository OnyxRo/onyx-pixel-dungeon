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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.GrowSeed;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.LiveMossSprite;
import com.watabou.utils.Random;

public class LiveMoss extends Mob {
	
	{
		spriteClass = LiveMossSprite.class;

		HP = HT = 50+(adj(0)*Random.NormalIntRange(1, 3));
		defenseSkill = 5+adj(1);

		EXP = 5;
		maxLvl = 9;
		lootChance = 0.1f;
		
		properties.add(Property.PLANT);
	}


	@Override
	public int damageRoll() {
		return Random.NormalIntRange(3, 6+adj(0));
	}

	@Override
	public int attackSkill( Char target) {
		return 12+adj(0);
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 4);
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		if (Random.Int(3) == 0 && !skilluse)  {
			Buff.affect(enemy, GrowSeed.class).set(5f);
			skilluse = true;
		}

		return damage;
	}

}
