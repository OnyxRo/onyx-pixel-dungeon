/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Xun's Pixel Dungeon, by X-U-N-123, ported into Onyx's Pixel Dungeon
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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.CursedWand;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.watabou.utils.Random;

//xun, every hook of Xun's PD's three heroes and their talents. Each call site in a shared class is one line naming a method here.
public class XunTalents {

	//Thrown Evil (Wraith), a thrown weapon may also fire a cursed wand effect at its target
	public static void thrownEvilProc(int cell){
		final Hero hero = Dungeon.hero;
		if (hero == null || !hero.hasTalent(Talent.THROWN_EVIL)) return;
		if (Random.Float() < 0.1f * hero.pointsInTalent(Talent.THROWN_EVIL) && hero.buff(MagicImmune.class) == null){
			Actor.add(new Actor() {
				{ actPriority = VFX_PRIO; }
				@Override
				protected boolean act() {
					Actor.remove(this);
					CursedWand.cursedZap(null, hero, new Ballistica(hero.pos, cell, Ballistica.MAGIC_BOLT), this::next);
					return false;
				}
			});
		}
	}

}
