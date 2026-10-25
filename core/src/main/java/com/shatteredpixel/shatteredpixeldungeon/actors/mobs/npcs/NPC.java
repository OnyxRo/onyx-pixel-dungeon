/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;

public abstract class NPC extends Mob {

	{
		HP = HT = 1;
		EXP = 0;

		alignment = Alignment.NEUTRAL;
		state = PASSIVE;
	}

	//magicling, older Shattered NPCs push items off the cell they stand on, Magic Ling's still call this
	protected void throwItem() {
		com.shatteredpixel.shatteredpixeldungeon.items.Heap heap = Dungeon.level.heaps.get( pos );
		if (heap != null && heap.type == com.shatteredpixel.shatteredpixeldungeon.items.Heap.Type.HEAP) {
			int n;
			int tries = 20;
			do {
				n = pos + com.watabou.utils.PathFinder.NEIGHBOURS8[com.watabou.utils.Random.Int( 8 )];
			} while (!Dungeon.level.passable[n] && !Dungeon.level.avoid[n] && --tries > 0);
			if (tries > 0) {
				com.shatteredpixel.shatteredpixeldungeon.items.Heap dropped = Dungeon.level.drop( heap.pickUp(), n );
				if (dropped.sprite != null) dropped.sprite.drop( pos );
			}
		}
	}

	@Override
	protected boolean act() {
		if (Dungeon.level.heroFOV[pos]){
			Bestiary.setSeen(getClass());
		}

		return super.act();
	}

	@Override
	public void beckon( int cell ) {
	}
	
}