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
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.StoneOre;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.sprites.LevelCheckerSprite;
import com.watabou.utils.Random;

public class LevelChecker extends Mob {

	{
		spriteClass = LevelCheckerSprite.class;

		HP = HT = 200+Math.min(800,Dungeon.hero.lvl*20);
		defenseSkill = Dungeon.hero.lvl/2;
		baseSpeed = 1f;
		flying = true;

		//state = WANDERING;

		loot = new StoneOre();
		lootChance = 0.1f;

		properties.add(Property.MECH);
	}


	//@Override
	//protected boolean getCloser(int target) {
		//return super.getCloser(Dungeon.hero.pos);
//	}

	@Override
	public int damageRoll() {
		return (int)(Dungeon.hero.lvl*1.5);
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		if (enemy == Dungeon.hero) {
			if (!skilluse) {
				skilluse = true;
				Dungeon.hero.lvl++;
			} else {
				enemy.damage(1,Item.class,3);
			}
		} else {
			enemy.damage(1, Item.class,3);
		}
		return damage;

	}

	@Override
	public int attackSkill( Char target) {
		return Dungeon.hero.lvl;
	}

	@Override
	public int drRoll() {
		return Dungeon.hero.lvl;
	}

	@Override
	public void die(Object cause) {

		if (Random.Int(2) == 0)

		super.die(cause);

	}
	
	
	{
		weakness.add(Wand.class);
	}
	
}
