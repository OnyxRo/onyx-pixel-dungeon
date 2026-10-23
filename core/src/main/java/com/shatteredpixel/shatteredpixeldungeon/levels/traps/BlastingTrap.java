/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Ported from Polished Pixel Dungeon (GPL-3.0).
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

package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;

//Polished PD. Like an explosive trap with a 5x5 blast that deals 25% less damage, and it also destroys
//equipment up to +2 lying in the blast. Found in trap rooms when the Polished PD changes option is on.
public class BlastingTrap extends Trap {

	{
		color = ORANGE;
		shape = LARGE_DOT;
	}

	@Override
	public void activate() {
		affectTiles();

		new BlastingBomb().explode(pos);
		if (reclaimed && !Dungeon.hero.isAlive()) {
			Badges.validateDeathFromFriendlyMagic();
		}
	}

	private void affectTiles() {
		boolean[] explodable = new boolean[Dungeon.level.length()];
		BArray.not( Dungeon.level.solid, explodable);
		BArray.or( Dungeon.level.flamable, explodable, explodable);
		PathFinder.buildDistanceMap( pos, explodable, 2 );
		for (int i = 0; i < PathFinder.distance.length; i++) {
			if (PathFinder.distance[i] != Integer.MAX_VALUE) {
				Heap heap = Dungeon.level.heaps.get(i);
				if (heap != null) destroyEquipables(heap);

				Char ch = Actor.findChar(i);
				if (ch instanceof Mob){
					Buff.prolong(ch, Trap.HazardAssistTracker.class, HazardAssistTracker.DURATION);
				}
			}
		}
	}

	private static void destroyEquipables( Heap heap ) {
		if (heap.type != Heap.Type.HEAP || heap.isEmpty()) return;

		for (Item item : heap.items.toArray( new Item[0] )) {
			if (!item.unique && item.level() < 3 && (item.isUpgradable() || item instanceof EquipableItem)) {
				heap.items.remove(item);
			}
		}

		if (heap.isEmpty()){
			heap.destroy();
		} else if (heap.sprite != null) {
			heap.sprite.view(heap).place( heap.pos );
		}
	}

	public static class BlastingBomb extends Bomb {
		{
			int d = Dungeon.scalingDepth();
			minDamage = Math.round((4 + d) * 0.75f);
			maxDamage = Math.round((12 + 3*d) * 0.75f);
		}

		@Override
		protected int explosionRange() {
			return 2;
		}
	}
}
