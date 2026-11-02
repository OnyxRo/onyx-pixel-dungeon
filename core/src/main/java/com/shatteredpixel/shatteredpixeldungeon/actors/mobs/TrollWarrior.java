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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AttackUp;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.DefenceUp;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.StoneOre;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TrollWarriorSprite;
import com.watabou.utils.Random;

public class TrollWarrior extends Mob {
	
	{
		spriteClass = TrollWarriorSprite.class;
		baseSpeed = 1.2f;

		HP = HT = 80+(5*Random.NormalIntRange(2, 5));
		EXP = 10;
		maxLvl = 20;
		defenseSkill = 15;
		
		loot = new StoneOre();
		lootChance = 0.2f;

		properties.add(Property.TROLL);
		
	}


	@Override
    public boolean act() {
        if( HP < HT && !skilluse ) {
			Buff.affect(this,AttackUp.class,8f).level(20);
			Buff.affect(this,DefenceUp.class,8f).level(80);
            skilluse = true;
			yell(Messages.get(this,"angry"));}
        return super.act();
    }
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange(15, 30);
	}

	@Override
	public int attackSkill( Char target) {
		return 30;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(5, 7);
	}
	@Override
	public float attackDelay() {
		return 1.2f;
	}

	{
		resistances.add(ToxicGas.class);
		resistances.add(Poison.class);
	}

}
