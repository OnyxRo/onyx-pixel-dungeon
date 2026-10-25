/*
 * Too Cruel Pixel Dungeon Trials (GPL-3.0), ported into Onyx's Pixel Dungeon.
 * Based on Shattered Pixel Dungeon by Evan Debenham.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.toocruel;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.TcStoredHeapData;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

//the items or monster a Too Cruel mimic holds, let out where it dies
public class HoldingHeap extends Buff implements TcBuff.OnDeath {

	private TcStoredHeapData heap = new TcStoredHeapData();

	public HoldingHeap set( TcStoredHeapData heap ) {
		if (this.heap.isNothing()) {
			this.heap = heap;
		} else {
			heap.mergeInto( this.heap );
		}
		return this;
	}

	public TcStoredHeapData heap() {
		return heap;
	}

	@Override
	public int icon() {
		if (heap.items.isEmpty() && heap.childHeaps.isEmpty()) return BuffIndicator.NONE;
		return BuffIndicator.NOINV;
	}

	@Override
	public String desc() {
		if (!heap.items.isEmpty() && !heap.childHeaps.isEmpty()) return Messages.get( this, "desc_both", heap.items.size(), heap.childHeaps.size() );
		if (!heap.items.isEmpty()) return Messages.get( this, "desc_items", heap.items.size() );
		if (!heap.childHeaps.isEmpty()) return Messages.get( this, "desc_heaps", heap.childHeaps.size() );
		return Messages.get( this, "desc_empty" );
	}

	@Override
	public void onDeathProc() {
		ArrayList<TcBuff.PersistNesting> effects = new ArrayList<>();
		for (Buff b : target.buffs()) {
			if (b instanceof TcBuff.PersistNesting) effects.add( (TcBuff.PersistNesting) b );
		}
		ArrayList<Char> ignored = new ArrayList<>();
		ignored.add( target );
		heap.restoreAtPos( Dungeon.level, target.pos, ignored, effects, false );
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( "heap", heap );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		heap = (TcStoredHeapData) bundle.get( "heap" );
		if (heap == null) heap = new TcStoredHeapData();
	}
}
