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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.CountDown;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Boomerang;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BanditKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class BanditKing extends Mob {

	public Item item;

	{
		spriteClass = BanditKingSprite.class;
		HP = HT = 300; 
		EXP = 10;
		maxLvl = 25;
		flying = true;
		
		// 1 in 30 chance to be a crazy bandit, equates to overall 1/90 chance.
		lootChance = 0.2f;
		defenseSkill = 20; //20
		

		properties.add(Property.ELF);
		properties.add(Property.MINIBOSS);
	}

	@Override
	public Item SupercreateLoot(){
		return new Boomerang();
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(10, 20);
	}

	@Override
	public float speed() {
		return 2f;
   	}
	
	@Override
	public int attackProc(Char enemy, int damage) {
		if(enemy.buff(CountDown.class) == null){
			Buff.affect(enemy, CountDown.class);	
			state = FLEEING;
		}

		return damage;
	}

	@Override
	public void die(Object cause) {
		super.die(cause);
		if (Dungeon.depth <25){
		yell(Messages.get(this, "die"));
		GLog.n(Messages.get(this, "dis"));
		if (false) {
		yell(Messages.get(this, "spork"));	
		}
	  }
	}
}
