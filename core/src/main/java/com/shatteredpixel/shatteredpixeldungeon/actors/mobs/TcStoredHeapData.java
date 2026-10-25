/*
 * Too Cruel Pixel Dungeon Trials (GPL-3.0), ported into Onyx's Pixel Dungeon.
 * Based on Shattered Pixel Dungeon by Evan Debenham.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.toocruel.HoldingHeap;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.toocruel.TcBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GuardianTrap;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

//Too Cruel's mimic family. A pile of items, maybe held by a monster, that can be stored and put back.
//Kept in the mobs package so it can reach the weapon of a statue and the prize of a mimic.
public class TcStoredHeapData implements Bundlable {

	public interface ItemFn {
		//return null to remove the item
		Item apply( Item item );
	}

	public boolean isLevelGenStatue = false;
	public boolean extraMimicLoot = false;
	public Class<? extends Mob> holderClass = null;
	public final ArrayList<TcStoredHeapData> childHeaps = new ArrayList<>();
	public Heap.Type heapType = null;
	public final ArrayList<Item> items = new ArrayList<>();

	private static Char findCharAt( Level level, int pos ) {
		if (Dungeon.hero != null && Dungeon.hero.pos == pos) return Dungeon.hero;
		return level.findMob( pos );
	}

	private boolean blocking( Char c, List<Char> ignored ) {
		return c != null && !ignored.contains( c )
				&& (holderClass != null || c.properties().contains( Char.Property.IMMOVABLE ) || c instanceof NPC);
	}

	public void restoreAtPos( Level level, int pos, boolean spawnPassive ) {
		restoreAtPos( level, pos, new ArrayList<Char>(), new ArrayList<TcBuff.PersistNesting>(), spawnPassive );
	}

	public void restoreAtPos( Level level, int pos, List<Char> ignored, List<TcBuff.PersistNesting> effects, boolean spawnPassive ) {
		Heap there = level.heaps.get( pos );
		boolean heapConflict = (heapType != null && heapType != Heap.Type.HEAP) || (there != null && there.type != Heap.Type.HEAP);

		if ((holderClass == null && heapConflict) || blocking( findCharAt( level, pos ), ignored )) {
			PathFinder.buildDistanceMap( pos, BArray.or( level.passable, level.avoid, null ) );
			HashSet<Integer> valid = new HashSet<>();
			int minDistance = Integer.MAX_VALUE;
			for (int i = 0; i < PathFinder.distance.length; i++) {
				int dist = PathFinder.distance[i];
				if (dist > minDistance) continue;
				if (level.heaps.get( i ) != null || !(level.passable[i] || level.avoid[i]) || level.pit[i]) continue;
				if (blocking( findCharAt( level, i ), ignored )) continue;
				if (dist == minDistance) {
					valid.add( i );
				} else {
					valid.clear();
					valid.add( i );
					minDistance = dist;
				}
			}
			if (!valid.isEmpty()) pos = Random.element( valid );
		}

		if (holderClass != null) {
			Class<? extends Mob> cl = holderClass;
			int wepIdx = indexOf( MeleeWeapon.class );
			int armorIdx = indexOf( Armor.class );
			if (cl == TcHolderStatue.class && wepIdx < 0) {
				cl = TcHolderMimic.class;
			} else {
				if (cl == ArmoredStatue.class && armorIdx < 0) cl = Statue.class;
				if (Statue.class.isAssignableFrom( cl ) && wepIdx < 0) cl = Mimic.class;
			}

			Mob mob;
			if (Mimic.class.isAssignableFrom( cl ) && cl != TcHolderMimic.class) {
				mob = Mimic.spawnAt( pos, cl, false );
			} else if (cl == TcHolderMimic.class) {
				mob = new TcHolderMimic();
				((Mimic) mob).setLevel( Dungeon.scalingDepth() );
			} else if (cl == TcHolderStatue.class || cl == Statue.class || cl == GuardianTrap.Guardian.class) {
				Statue s = cl == TcHolderStatue.class ? new TcHolderStatue()
						: cl == Statue.class ? new Statue() : new GuardianTrap.Guardian();
				s.weapon = (MeleeWeapon) items.remove( wepIdx );
				s.levelGenStatue = isLevelGenStatue;
				mob = s;
			} else if (cl == ArmoredStatue.class) {
				ArmoredStatue s = new ArmoredStatue();
				s.weapon = (MeleeWeapon) items.remove( wepIdx );
				if (armorIdx > wepIdx) armorIdx--;
				s.armor = (Armor) items.remove( armorIdx );
				s.levelGenStatue = isLevelGenStatue;
				mob = s;
			} else {
				mob = Reflection.newInstance( cl );
				if (mob == null) mob = new TcHolderMimic();
			}

			for (TcStoredHeapData child : childHeaps) {
				Buff.append( mob, HoldingHeap.class ).set( child );
			}
			childHeaps.clear();
			HoldingHeap holder = Buff.append( mob, HoldingHeap.class ).set( withoutHolder().copy() );
			if (mob instanceof Mimic && (heapType == null || heapType == Heap.Type.HEAP)) {
				Mimic m = (Mimic) mob;
				if (m.items == null) m.items = new ArrayList<>();
				if (extraMimicLoot) holder.heap().items.addAll( m.items );
				m.items.clear();
			}
			mob.pos = pos;
			for (TcBuff.PersistNesting ef : effects) {
				ef.applyNestingEffect( mob );
			}
			if (!spawnPassive) mob.state = mob.HUNTING;
			if (level == Dungeon.level && GameScene.scene != null) {
				GameScene.add( mob );
			} else {
				level.mobs.add( mob );
			}
		} else {
			for (TcStoredHeapData child : childHeaps) {
				child.restoreAtPos( level, pos, ignored, effects, spawnPassive );
			}
			childHeaps.clear();
			if (!items.isEmpty()) {
				Heap heap;
				do {
					heap = level.drop( items.remove( 0 ), pos );
				} while ((heap == null || heap.isEmpty()) && !items.isEmpty());
				if (heap != null) {
					if (!items.isEmpty()) heap.items.addAll( items );
					items.clear();
					if (heapType != null) {
						heap.type = heapType;
						if (heap.sprite != null) {
							heap.sprite.link();
							heap.sprite.drop();
						}
					}
				}
			}
		}
	}

	private int indexOf( Class<?> cls ) {
		for (int i = 0; i < items.size(); i++) if (cls.isInstance( items.get( i ) )) return i;
		return -1;
	}

	//merges this data into another one and clears this one
	public void mergeInto( TcStoredHeapData into ) {
		if (isPlainContainer() && into.isPlainContainer() && heapType == into.heapType) {
			into.items.addAll( items );
		} else {
			into.childHeaps.add( copy() );
		}
		clear();
	}

	public boolean isNothing() {
		return holderClass == null && heapType == null && items.isEmpty() && childHeaps.isEmpty();
	}

	public boolean isPlainContainer() {
		return holderClass == null && childHeaps.isEmpty()
				&& (heapType == null || heapType == Heap.Type.HEAP || heapType == Heap.Type.CHEST || heapType == Heap.Type.SKELETON);
	}

	public TcStoredHeapData withoutHolder() {
		if (holderClass == null) return this;
		TcStoredHeapData d = new TcStoredHeapData();
		d.heapType = heapType;
		d.items.addAll( items );
		return d;
	}

	public void transformItems( ItemFn fn ) {
		filterMap( items, fn );
		for (TcStoredHeapData child : childHeaps) child.transformItems( fn );
	}

	public static void filterMap( Collection<Item> items, ItemFn fn ) {
		ArrayList<Item> out = new ArrayList<>();
		for (Item i : items) {
			Item r = fn.apply( i );
			if (r != null) out.add( r );
		}
		items.clear();
		items.addAll( out );
	}

	public TcStoredHeapData copy() {
		Bundle b = new Bundle();
		storeInBundle( b );
		TcStoredHeapData d = new TcStoredHeapData();
		d.restoreFromBundle( b );
		return d;
	}

	public void clear() {
		items.clear();
		childHeaps.clear();
		holderClass = null;
		heapType = null;
	}

	private static final String HEAP_TYPE = "heap_type";
	private static final String IS_LEVEL_GEN_STATUE = "is_level_gen_statue";
	private static final String EXTRA_MIMIC_LOOT = "extra_mimic_loot";
	private static final String HOLDER_CLASS = "holder_class";
	private static final String CHILD_HEAPS = "child_heaps";
	private static final String ITEMS = "items";

	@SuppressWarnings("unchecked")
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		heapType = bundle.contains( HEAP_TYPE ) ? bundle.getEnum( HEAP_TYPE, Heap.Type.class ) : null;
		isLevelGenStatue = bundle.getBoolean( IS_LEVEL_GEN_STATUE );
		extraMimicLoot = bundle.getBoolean( EXTRA_MIMIC_LOOT );
		holderClass = null;
		if (bundle.contains( HOLDER_CLASS )) {
			Class<?> cl = bundle.getClass( HOLDER_CLASS );
			if (cl != null && Mob.class.isAssignableFrom( cl )) holderClass = (Class<? extends Mob>) cl;
		}
		childHeaps.clear();
		if (bundle.contains( CHILD_HEAPS )) {
			for (Bundlable b : bundle.getCollection( CHILD_HEAPS )) childHeaps.add( (TcStoredHeapData) b );
		}
		items.clear();
		if (bundle.contains( ITEMS )) {
			for (Bundlable b : bundle.getCollection( ITEMS )) items.add( (Item) b );
		}
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		if (heapType != null) bundle.put( HEAP_TYPE, heapType );
		bundle.put( IS_LEVEL_GEN_STATUE, isLevelGenStatue );
		bundle.put( EXTRA_MIMIC_LOOT, extraMimicLoot );
		if (holderClass != null) bundle.put( HOLDER_CLASS, holderClass );
		if (!childHeaps.isEmpty()) bundle.put( CHILD_HEAPS, childHeaps );
		bundle.put( ITEMS, items );
	}

	public static TcStoredHeapData fromHeap( Heap heap ) {
		TcStoredHeapData d = new TcStoredHeapData();
		d.items.addAll( heap.items );
		d.heapType = heap.type;
		return d;
	}

	//returns false for heaps that cannot be held, such as the locked chests of other mods
	public static boolean transformHeapIntoMimic( Level level, Heap heap, boolean extraLoot, boolean weakHolders ) {
		TcStoredHeapData data = fromHeap( heap );
		Class<? extends Mob> cl;
		switch (data.heapType) {
			case HEAP: case FOR_SALE: case CHEST: case TOMB: case SKELETON: case REMAINS:
				int weaponIdx = data.indexOf( MeleeWeapon.class );
				int armorIdx = data.indexOf( Armor.class );
				if (weaponIdx >= 0) {
					data.items.add( data.items.remove( weaponIdx ) );
					if (armorIdx > weaponIdx) armorIdx--;
					if (weakHolders) {
						cl = TcHolderStatue.class;
					} else if (armorIdx >= 0) {
						data.items.add( data.items.remove( armorIdx ) );
						cl = ArmoredStatue.class;
					} else {
						cl = Statue.class;
					}
				} else {
					cl = weakHolders ? TcHolderMimic.class : Mimic.class;
				}
				break;
			case LOCKED_CHEST: cl = GoldenMimic.class; break;
			case CRYSTAL_CHEST: cl = CrystalMimic.class; break;
			default: return false;
		}
		TcStoredHeapData holder = new TcStoredHeapData();
		holder.holderClass = cl;
		holder.childHeaps.add( data );
		holder.extraMimicLoot = extraLoot;
		holder.restoreAtPos( level, heap.pos, true );
		return true;
	}

	public static TcStoredHeapData fromMob( Mob mob ) {
		if (mob instanceof Mimic) return fromMimic( (Mimic) mob );
		if (mob instanceof Statue) return fromStatue( (Statue) mob );
		return fromMobRaw( mob );
	}

	public static TcStoredHeapData fromMimic( Mimic mimic ) {
		TcStoredHeapData d = fromMobRaw( mimic );
		if (mimic.items != null) d.items.addAll( mimic.items );
		return d;
	}

	public static TcStoredHeapData fromStatue( Statue statue ) {
		TcStoredHeapData d = fromMobRaw( statue );
		if (statue.weapon != null) d.items.add( statue.weapon );
		if (statue instanceof ArmoredStatue && ((ArmoredStatue) statue).armor != null) d.items.add( ((ArmoredStatue) statue).armor );
		return d;
	}

	private static TcStoredHeapData fromMobRaw( Mob mob ) {
		TcStoredHeapData d = new TcStoredHeapData();
		d.holderClass = mob.getClass();
		for (HoldingHeap buff : mob.buffs( HoldingHeap.class ).toArray( new HoldingHeap[0] )) {
			buff.heap().mergeInto( d );
			buff.detach();
		}
		return d;
	}

	//changes the items a monster holds, returns a replacement holder mimic if a statue lost its weapon
	public static Mob transformMobItems( Mob mob, ItemFn fn ) {
		boolean intoMimic = false;
		ArrayList<Item> extra = new ArrayList<>();
		if (mob instanceof Mimic) {
			if (((Mimic) mob).items != null) filterMap( ((Mimic) mob).items, fn );
		} else if (mob instanceof Statue && ((Statue) mob).weapon != null) {
			Statue s = (Statue) mob;
			Item newWeapon = fn.apply( s.weapon );
			Item newArmor = s instanceof ArmoredStatue && ((ArmoredStatue) s).armor != null ? fn.apply( ((ArmoredStatue) s).armor ) : null;
			if (newWeapon instanceof MeleeWeapon && s instanceof ArmoredStatue && newArmor instanceof Armor) {
				s.weapon = (MeleeWeapon) newWeapon;
				((ArmoredStatue) s).armor = (Armor) newArmor;
			} else if (newWeapon instanceof MeleeWeapon && !(s instanceof ArmoredStatue)) {
				s.weapon = (MeleeWeapon) newWeapon;
			} else {
				intoMimic = true;
				if (newWeapon != null) extra.add( newWeapon );
				if (newArmor != null) extra.add( newArmor );
			}
		}
		for (HoldingHeap h : mob.buffs( HoldingHeap.class )) {
			h.heap().transformItems( fn );
		}
		if (!extra.isEmpty()) {
			TcStoredHeapData d = new TcStoredHeapData();
			d.items.addAll( extra );
			Buff.append( mob, HoldingHeap.class ).set( d );
		}
		if (!intoMimic) return null;
		TcHolderMimic m = new TcHolderMimic();
		m.setLevel( Dungeon.scalingDepth() );
		for (HoldingHeap h : mob.buffs( HoldingHeap.class )) {
			if (h.heap().isNothing()) continue;
			Buff.append( m, HoldingHeap.class ).set( h.heap().copy() );
		}
		m.pos = mob.pos;
		return m;
	}

	//lets a waiting mimic or statue wake up and hunt
	public static void forceBeckon( Char ch, int cell ) {
		if (!(ch instanceof Mob)) return;
		Mob m = (Mob) ch;
		if (m instanceof Mimic && m.state == m.PASSIVE) {
			m.alignment = Char.Alignment.ENEMY;
			((Mimic) m).stopHiding();
		} else if (m instanceof Statue && m.state == m.PASSIVE) {
			m.state = m.HUNTING;
		}
		m.beckon( cell );
	}
}
