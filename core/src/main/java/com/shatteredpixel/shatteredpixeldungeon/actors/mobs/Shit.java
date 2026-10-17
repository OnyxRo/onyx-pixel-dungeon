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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BeOld;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Locked;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.throwing.ShitBall;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShitSprite;

public class Shit extends Mob {

	{
		spriteClass = ShitSprite.class;

		HP = HT = 30;
		defenseSkill = 0;




		EXP = 3;
		maxLvl = 18;

		loot = ShitBall.class;
		lootChance = 1f;
	
		properties.add(Property.ELF);
	}

	@Override
	public int damageRoll() {
		return 1;
	}

	@Override
	public float attackDelay() {
		return 2.5f;
	}

	@Override
	public Item SupercreateLoot(){
		return new PotionOfToxicGas();
	}

	@Override
	protected boolean canAttack(Char enemy) {if (buff(Locked.class) != null){
			return Dungeon.level.adjacent(pos, enemy.pos) && (!isCharmedBy(enemy));
		} else
		return Dungeon.level.distance( pos, enemy.pos ) <= 2 ;
	}	
	
	@Override
	public int attackProc(Char enemy, int damage) {		
		Buff.affect(enemy, BeOld.class).set(4);
		return damage;
	}	
	
	
	@Override
	public int attackSkill( Char target) {
		return 10+adj(0);
	}

	@Override
	public int drRoll() {
		return 0;
	}
	
}
