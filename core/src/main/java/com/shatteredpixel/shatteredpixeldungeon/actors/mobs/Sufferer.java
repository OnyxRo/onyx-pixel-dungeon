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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BeCorrupt;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.GlassShield;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.UnstableSpellbook;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SuffererSprite;
import com.watabou.utils.Random;

import static com.shatteredpixel.shatteredpixeldungeon.actors.damagetype.DamageType.DARK_DAMAGE;

 public class Sufferer extends Mob {

	{
		spriteClass = SuffererSprite.class;

		HP = HT = 180+(adj(0)*Random.NormalIntRange(4, 7));
		defenseSkill = 16+adj(1);

		EXP = 16;
		maxLvl = 35;
		
		loot = Generator.Category.SCROLL;
		lootChance = 0.35f;
		
		properties.add(Property.DEMONIC);
		properties.add(Property.MAGICER);
		properties.add(Property.HUMAN);
	}


	@Override
	public int damageRoll() {
		return Random.NormalIntRange(7+adj(0), 10+adj(1));
	}

	@Override
	public int attackSkill( Char target) {
		return 34+adj(1);
	}


	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 10);
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		if (Random.Int(3) == 0) {
			Buff.affect(enemy, BeCorrupt.class).level(20);
		}
		
		enemy.damage(damageRoll(), DARK_DAMAGE,2);
		damage = 0;

		return damage;
	}

	@Override
	public int defenseProc(Char enemy, int damage) {

		int dmg = damage;
		if (dmg > 50 && buff(GlassShield.class) == null) {
			Buff.affect(this,GlassShield.class).turns(4);
		}

		return super.defenseProc(enemy, damage);
	}	


	{
		immunities.add(Amok.class);
		immunities.add(Terror.class);
		immunities.add(Sleep.class);
	}

}
