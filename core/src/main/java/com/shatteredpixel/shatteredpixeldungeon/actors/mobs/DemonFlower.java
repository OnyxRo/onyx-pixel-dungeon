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
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArmorBreak;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AttackDown;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.DefenceUp;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.GrowSeed;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silent;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DemonflowerSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class DemonFlower extends Mob {

	private static final int DEBUFF_DELAY = 6;

	{
		spriteClass = DemonflowerSprite.class;

		HP = HT = 450;
		EXP = 25;
		maxLvl = 35;
		defenseSkill = 5;

		properties.add(Property.PLANT);
		properties.add(Property.DEMONIC);

		loot = Generator.Category.SEED;
		lootChance = 0.5f;

	}

	private int addDebuff = DEBUFF_DELAY;

	@Override
	public Item SupercreateLoot(){
		return Generator.random(Generator.Category.PILL);
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(26, 37);
	}

	@Override
	public int attackSkill( Char target) {
		return 35+adj(1);
	}

	@Override
	public float attackDelay() {
		return 0.33f;
	}	
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 5);
	}

	@Override
	protected boolean doAttack(Char enemy) {
		addDebuff--;
		if (addDebuff <= 0 && Dungeon.level.adjacent(pos, enemy.pos) && (buff(Silent.class) == null)) {
			debuffadd();
			return true;
		} else {
			return super.doAttack(enemy);
		}
	}

	@Override
	public int defenseProc(Char enemy, int damage) {

		int dmg = damage;
		if (dmg < HT/9 && buff(DefenceUp.class) == null) {
			Buff.affect(this,DefenceUp.class,3f).level(dmg);
		}

		return super.defenseProc(enemy, damage);
	}	
	
	private void debuffadd() {
		addDebuff = DEBUFF_DELAY;

		int pos = enemy.pos;
		Char ch;
		if ((ch = Actor.findChar(pos)) != null) {
			Buff.prolong(ch, AttackDown.class,5f).level(30);
			Buff.prolong(ch, ArmorBreak.class,5f).level(30);
		}
		GLog.n(Messages.get(this,"debuff"));

		spend(1f);
	}

	{
		weakness.add(Burning.class);
		
		resistances.add(ToxicGas.class);
		resistances.add(Poison.class);
		
	    immunities.add(GrowSeed.class);
		
	}

}
