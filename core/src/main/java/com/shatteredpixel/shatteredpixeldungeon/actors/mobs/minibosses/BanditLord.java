/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
 *
 * Lovecraft Pixel Dungeon
 * Copyright (C) 2016-2018 Sadsaltan
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
 * You should have have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses>
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.minibosses;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Thief;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BanditLordSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

/**
 * Lovecraft PD's king of the bandits. A thief that fights with a random enchanted melee weapon,
 * and poisons, blinds and cripples whoever he robs.
 */
public class BanditLord extends Thief {

	{
		spriteClass = BanditLordSprite.class;

		HP = HT = 60;
		defenseSkill = 20;
		EXP = 12;
		maxLvl = 22;

		properties.add(Property.HUMAN);
		properties.add(Property.MINIBOSS);

		lootChance = 0.333f;
	}

	protected Weapon weapon;

	public BanditLord() {
		super();

		do {
			weapon = (Weapon) Generator.random( Generator.Category.WEAPON );
		} while (!(weapon instanceof MeleeWeapon) || weapon.cursed);

		weapon.enchant( Weapon.Enchantment.random() );
	}

	private static final String WEAPON	= "weapon";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( WEAPON, weapon );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		weapon = (Weapon)bundle.get( WEAPON );
	}

	@Override
	public int damageRoll() {
		return weapon.damageRoll(this);
	}

	@Override
	public int attackSkill( Char target ) {
		return (int)((9 + Dungeon.depth) * weapon.accuracyFactor(this, target));
	}

	@Override
	public float attackDelay() {
		return super.attackDelay() * weapon.delayFactor( this );
	}

	@Override
	protected boolean canAttack(Char enemy) {
		return Dungeon.level.distance( pos, enemy.pos ) <= weapon.reachFactor(this);
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, Dungeon.depth + weapon.defenseFactor(this));
	}

	@Override
	public int attackProc( Char enemy, int damage ) {
		damage = super.attackProc( enemy, damage );
		return weapon.proc( this, enemy, damage );
	}

	@Override
	protected boolean steal( Hero hero ) {
		if (super.steal( hero )) {

			Buff.prolong( hero, Blindness.class, Random.Int( 2, 5 ) );
			Buff.affect( hero, Poison.class ).set(Random.Int(5, 7) * Poison.durationFactor(hero));
			Buff.prolong( hero, Cripple.class, Random.Int( 3, 8 ) );
			Dungeon.observe();

			return true;
		} else {
			return false;
		}
	}
}
