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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Wet;
import com.shatteredpixel.shatteredpixeldungeon.actors.damagetype.DamageType;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DustElementSprite;
import com.watabou.utils.Random;

import static com.shatteredpixel.shatteredpixeldungeon.actors.damagetype.DamageType.EARTH_DAMAGE;

public class DustElement extends Mob {

	{
		spriteClass = DustElementSprite.class;

		HP = HT = 35 + (Dungeon.depth * Random.NormalIntRange(1, 3));
		defenseSkill = 4 + (Math.round((Dungeon.depth) / 2));

		EXP = 2;
		maxLvl = 8;

		flying = true;

		loot = Generator.random(Generator.Category.SEED);
		lootChance = 0.5f;

		properties.add(Property.ELEMENT);
	}


	@Override
	public int damageRoll() {
		return Random.NormalIntRange(2, 5 + (Dungeon.depth));
	}

	@Override
	public int attackSkill( Char target) {
		return 11 + (Dungeon.depth);
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		if (Random.Int(10) == 0) {
			Buff.prolong(enemy, Blindness.class, Random.Int(3, 10));
			//GLog.w(Messages.get(this, "blind"));
			//Dungeon.observe();
		}

		enemy.damage(damageRoll(), EARTH_DAMAGE,2);
		damage = 0;
		return damage;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 2);
	}

	@Override
	public boolean add( Buff buff ) {
		if (buff instanceof Roots) {
			if (HP < HT && this.isAlive()) {
				HP+=HT/10;
				sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
			}
		} else if (buff instanceof Wet) {
			if (Dungeon.level.water[this.pos])
				damage(Random.NormalIntRange(HT / 2, HT), buff,3);
			else
				damage(Random.NormalIntRange(1, HT * 2 / 3), buff,3);
		} else {
			return super.add(buff);
		}
		return false;
	}

	{
		resistances.add(Ooze.class);
	}
}
