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
package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.sps;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.*;
import com.shatteredpixel.shatteredpixeldungeon.sprites.*;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.damagetype.DamageType;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFirebolt;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.plants.Firebloom;
import com.shatteredpixel.shatteredpixeldungeon.sprites.sps.FireElementalSprite;
import com.watabou.utils.Random;

public class FireElemental extends Mob {

	{
		spriteClass = FireElementalSprite.class;

		HP = HT = 120+(adj(0)*Random.NormalIntRange(4, 7));
		defenseSkill = 20+adj(0);

		EXP = 15;
		maxLvl = 30;

		flying = true;

		loot = new PotionOfLiquidFlame();
		lootChance = 0.1f;
		
		lootOther = new WandOfFirebolt();
		lootChanceOther = 0.02f; // by default, see die()
		
		properties.add(Property.ELEMENT);
		properties.add(Property.MAGICER);
	}

	@Override
	public Item SupercreateLoot(){
		return new Firebloom.Seed();
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(16, 20+adj(1));
	}

	@Override
	public int attackSkill( Char target) {
		return 25+adj(1);
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 5);
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		if (Random.Int(2) == 0) {
			Buff.affect(enemy, Burning.class).reignite(enemy);
		}
		enemy.damage(damageRoll(), com.shatteredpixel.shatteredpixeldungeon.actors.damagetype.DamageType.FIRE_DAMAGE,2);
		damage = 0;

		return damage;
	}
	
	@Override
	protected boolean canAttack(Char enemy) {
		return Dungeon.level.distance( pos, enemy.pos ) <= 2 ;
	}		

	@Override
	public boolean add( Buff buff ) {
		if (buff instanceof Burning) {
			if (HP < HT && this.isAlive()) {
				HP+=HT/10;
				sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
			}
		} else if (buff instanceof Frost || buff instanceof Chill) {
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
		//immunities.add(Burning.class);
		immunities.add(Fire.class);
		resistances.add(WandOfFirebolt.class);
	}

}
