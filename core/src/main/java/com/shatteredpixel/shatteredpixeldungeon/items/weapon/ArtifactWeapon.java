/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
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
package com.shatteredpixel.shatteredpixeldungeon.items.weapon;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.watabou.utils.Bundle;

//Deep PD. A melee weapon with a charge and a buff that fills it while it is held.
//Deep wrote this on the old Weapon class, here it sits on top of MeleeWeapon so damage, strength and upgrades work as usual.
public class ArtifactWeapon extends MeleeWeapon {

	public Buff passiveBuff;
	protected Buff activeBuff;

	public int charge = 0;
	public int chargeCap = 0;
	protected int cooldown = 0;

	@Override
	public boolean doEquip(Hero hero) {
		activate(hero);
		return super.doEquip(hero);
	}

	public void activate(Hero hero) {
		if (passiveBuff != null) passiveBuff.detach();
		passiveBuff = passiveBuff();
		if (passiveBuff != null) passiveBuff.attachTo(hero);
	}

	@Override
	public boolean doUnequip(Hero hero, boolean collect, boolean single) {
		if (super.doUnequip(hero, collect, single)) {
			if (passiveBuff != null) {
				passiveBuff.detach();
				passiveBuff = null;
			}
			return true;
		}
		return false;
	}

	protected WeaponBuff passiveBuff() {
		return null;
	}

	private static final String CHARGE = "charge";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(CHARGE, charge);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		charge = bundle.getInt(CHARGE);
	}

	public class WeaponBuff extends Buff {
	}
}
