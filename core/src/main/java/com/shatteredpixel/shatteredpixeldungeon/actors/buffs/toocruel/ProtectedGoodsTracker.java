/*
 * Too Cruel Pixel Dungeon Trials (GPL-3.0), ported into Onyx's Pixel Dungeon.
 * Based on Shattered Pixel Dungeon by Evan Debenham.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.toocruel;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.TooCruel;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Awareness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

//Protected Goods, upgrade scrolls, strength potions and styli wait for the next boss to fall
public class ProtectedGoodsTracker extends Buff implements TcBuff.OnBossSlain {

	{
		actPriority = HERO_PRIO + 1;
	}

	private final ArrayList<Item> items = new ArrayList<>();

	public void addItem( Item item ) {
		items.add( item.duplicate() );
	}

	public int count() {
		return items.size();
	}

	@Override
	public boolean attachTo( Char target ) {
		if (super.attachTo( target )) {
			diactivate();
			return true;
		}
		return false;
	}

	@Override
	public boolean act() {
		if (items.isEmpty() || Dungeon.level == null) {
			diactivate();
			return true;
		}
		PathFinder.buildDistanceMap( target.pos, BArray.or( Dungeon.level.passable, Dungeon.level.avoid, null ) );
		ArrayList<Integer> cells = new ArrayList<>();
		for (int i = 0; i < Dungeon.level.length(); i++) {
			int d = PathFinder.distance[i];
			if (d <= 0 || d == Integer.MAX_VALUE) continue;
			int t = Dungeon.level.map[i];
			if (!Dungeon.level.pit[i] && t != Terrain.TRAP && t != Terrain.SECRET_TRAP && Dungeon.level.getTransition( i ) == null) {
				cells.add( i );
			}
		}
		Collections.sort( cells, new Comparator<Integer>() {
			@Override
			public int compare( Integer a, Integer b ) {
				int c = Integer.compare( PathFinder.distance[a], PathFinder.distance[b] );
				return c != 0 ? c : Integer.compare( a, b );
			}
		} );
		if (cells.isEmpty()) cells.add( target.pos );
		for (int i = 0; i < items.size(); i++) {
			int cell = cells.get( i % cells.size() );
			TooCruel.furrowCell( Dungeon.level, cell );
			Heap heap = Dungeon.level.drop( items.get( i ), cell );
			if (TooCruel.mimicsEncaseHeap( Dungeon.level, heap )) {
				Dungeon.level.heaps.remove( cell );
				if (heap.sprite != null) heap.sprite.kill();
			} else if (Dungeon.level.heroFOV[cell] && heap.sprite != null) {
				heap.sprite.drop();
			}
		}
		items.clear();
		Buff.affect( target, Awareness.class, Awareness.DURATION );
		if (GameScene.scene != null) {
			Dungeon.observe();
			GameScene.updateMap();
		}
		diactivate();
		return true;
	}

	@Override
	public void onBossSlain() {
		spend( -cooldown() );
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( "items", items );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		items.clear();
		for (Bundlable b : bundle.getCollection( "items" )) items.add( (Item) b );
	}
}
