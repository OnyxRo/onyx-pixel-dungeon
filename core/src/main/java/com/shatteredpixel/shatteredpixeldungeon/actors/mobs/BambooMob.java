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
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.SwampGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.DefenceUp;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.effects.Wound;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.vegetable.NutVegetable;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobBambooSprite;
import com.watabou.utils.Random;

public class BambooMob extends Mob {

	{
		
		HP = HT = 40;
		defenseSkill = 0;

		EXP = 1;

		loot = new NutVegetable();
		lootChance = 0.4f;
		
		spriteClass = MobBambooSprite.class;
		
		properties.add(Property.PLANT);
	}

	@Override
	public Item SupercreateLoot(){
		return Generator.random(Generator.Category.ARMOR);
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(4, 12);
	}

		@Override
		public int attackSkill( Char target ) {
			return 15;
		}

		@Override
		public int drRoll() {
			return Random.NormalIntRange(1, 3);
		}		
	
	@Override
	public int attackProc(Char enemy, int damage) {
		if (Random.Int(3) == 0) {
			Buff.affect(this,DefenceUp.class,3f).level(20);
		}
		return super.attackProc(enemy, damage);
	}
	
	@Override
	public int defenseProc(Char enemy, int damage) {

		int dmg = Random.IntRange(0, damage) - enemy.drRoll();
		if (dmg > 0) {
			enemy.damage(dmg, this,1);
			Wound.hit(enemy);
		}

		return super.defenseProc(enemy, damage);
	}	

		@Override
		protected boolean getCloser(int target) {
			return true;
		}

		@Override
		protected boolean getFurther(int target) {
			return true;
		}

	{
		weakness.add(ToxicGas.class);
		weakness.add(Ooze.class);
		weakness.add(SwampGas.class);

		resistances.add(Roots.class);

		weakness.add(Wand.class);
		weakness.add(Poison.class);

	}

}
