/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Cursed Pixel Dungeon, mana regeneration by Smujb
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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;

//Cursed PD gives the hero a mana pool that its Power items spend. This buff refills it, one point every 20 turns.
public class MpRegen extends Buff {

	{
		actPriority = HERO_PRIO - 1;
	}

	private static final float REGENERATION_DELAY = 20;

	@Override
	public boolean act() {
		if (target.isAlive() && target instanceof Hero) {
			Hero hero = (Hero) target;
			if (hero.MP < hero.MAX_MP) {
				LockedFloor lock = hero.buff(LockedFloor.class);
				if ((lock == null || lock.regenOn()) && hero.buff(MagicImmune.class) == null) {
					hero.MP += 1;
				}
			}
			spend( REGENERATION_DELAY );
		} else {
			diactivate();
		}
		return true;
	}
}
