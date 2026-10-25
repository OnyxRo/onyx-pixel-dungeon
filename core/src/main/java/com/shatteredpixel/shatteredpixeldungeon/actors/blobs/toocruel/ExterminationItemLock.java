/*
 * Too Cruel Pixel Dungeon Trials (GPL-3.0), ported into Onyx's Pixel Dungeon.
 * Based on Shattered Pixel Dungeon by Evan Debenham.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.blobs.toocruel;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.TooCruel;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.toocruel.Exterminating;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Statue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.TcStoredHeapData;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

//Postpaid Loot, the items of a floor are locked away until its extermination is complete
public class ExterminationItemLock extends Blob {

	private final LinkedHashMap<Integer, TcStoredHeapData> originalHeaps = new LinkedHashMap<>();

	@Override
	protected void evolve() {
		int w = Dungeon.level.width();
		for (int i = area.top - 1; i <= area.bottom; i++) {
			for (int j = area.left - 1; j <= area.right; j++) {
				int cell = j + i * w;
				if (Dungeon.level.insideMap( cell )) {
					off[cell] = cur[cell];
					volume += off[cell];
				}
			}
		}
		if (TooCruel.bossOrSpecial()) unlockAll( Dungeon.level );
	}

	@Override
	public String tileDesc() {
		return Messages.get( this, "desc" );
	}

	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );
		emitter.pour( Speck.factory( Speck.STAR ), 0.3f );
	}

	private void clearAll( Level level ) {
		volume = 0;
		area.setEmpty();
		cur = new int[level.length()];
		off = new int[level.length()];
	}

	//returns true when everything was let out and no second wave of holders came with it
	public boolean unlockAll( Level level ) {
		if (volume <= 0 && originalHeaps.isEmpty()) return true;
		for (Map.Entry<Integer, TcStoredHeapData> e : new ArrayList<>( originalHeaps.entrySet() )) {
			e.getValue().restoreAtPos( level, e.getKey(), true );
		}
		originalHeaps.clear();
		clearAll( level );

		if (TooCruel.on( TooCruel.MIMICS_ALL )) {
			int added = 0;
			for (Mob mob : level.mobs) {
				if (mob instanceof Mimic || mob instanceof Statue) {
					Buff.affect( mob, Exterminating.class );
					Buff.affect( mob, Exterminating.Reveal.class, Exterminating.Reveal.DURATION );
					added++;
				}
			}
			if (added > 0) {
				if (level == Dungeon.level && GameScene.scene != null) {
					GLog.w( Messages.get( TooCruel.class, "extermination_second_round", added ) );
					Sample.INSTANCE.play( Assets.Sounds.DEGRADE, 0.5f, 1.5f );
					Dungeon.observe();
					GameScene.updateFog();
					Dungeon.hero.checkVisibleMobs();
				}
				return false;
			}
		}
		return true;
	}

	public void transformItems( TcStoredHeapData.ItemFn fn ) {
		for (TcStoredHeapData d : originalHeaps.values()) d.transformItems( fn );
	}

	public Map<Integer, TcStoredHeapData> lockedItems() {
		return originalHeaps;
	}

	private void mark( Level level, int pos ) {
		if (cur == null || cur[pos] <= 0) seed( level, pos, 1 );
	}

	public void lockItem( Level level, Heap heap ) {
		mark( level, heap.pos );
		originalHeaps.put( heap.pos, TcStoredHeapData.fromHeap( heap ) );
		if (level == Dungeon.level && heap.sprite != null) {
			heap.destroy();
		} else {
			level.heaps.remove( heap.pos );
		}
	}

	public void lockMob( Level level, Mob mob ) {
		mark( level, mob.pos );
		originalHeaps.put( mob.pos, TcStoredHeapData.fromMob( mob ) );
		level.mobs.remove( mob );
		Actor.remove( mob );
		if (mob.sprite != null) mob.sprite.killAndErase();
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		int[] pos = new int[originalHeaps.size()];
		ArrayList<TcStoredHeapData> heaps = new ArrayList<>();
		int i = 0;
		for (Map.Entry<Integer, TcStoredHeapData> e : originalHeaps.entrySet()) {
			pos[i++] = e.getKey();
			heaps.add( e.getValue() );
		}
		bundle.put( "original_heaps_idx", pos );
		bundle.put( "original_heaps_types", heaps );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		originalHeaps.clear();
		int[] pos = bundle.getIntArray( "original_heaps_idx" );
		ArrayList<Bundlable> heaps = new ArrayList<>( bundle.getCollection( "original_heaps_types" ) );
		for (int i = 0; pos != null && i < pos.length && i < heaps.size(); i++) {
			originalHeaps.put( pos[i], (TcStoredHeapData) heaps.get( i ) );
		}
	}
}
