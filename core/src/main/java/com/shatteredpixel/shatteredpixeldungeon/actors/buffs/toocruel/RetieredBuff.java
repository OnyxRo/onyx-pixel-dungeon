/*
 * Too Cruel Pixel Dungeon Trials (GPL-3.0), ported into Onyx's Pixel Dungeon.
 * Based on Shattered Pixel Dungeon by Evan Debenham.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.toocruel;

import com.shatteredpixel.shatteredpixeldungeon.TooCruel;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;

//Retiered, Untiered and Tier Up, each weapon type gets a new tier for the whole run
public class RetieredBuff extends Buff implements TcBuff.OnBossSlain {

	{
		actPriority = HERO_PRIO - 1;
	}

	private HashMap<Class<?>, Integer> tiers = null;
	private int universalBonus = 0;
	//items in the pack when the game was saved were already given their Untiered levels
	private boolean justRestored = false;

	@Override
	public boolean attachTo( Char target ) {
		if (!(target instanceof Hero)) return false;
		if (super.attachTo( target )) {
			//a restored buff waits for its first turn, the pack is loaded after the buffs
			if (!justRestored) processInventory( (Hero) target );
			return true;
		}
		return false;
	}

	@Override
	public boolean act() {
		if (!(target instanceof Hero)) {
			detach();
			return true;
		}
		processInventory( (Hero) target );
		spend( TICK );
		return true;
	}

	private void initTiers() {
		if (tiers != null) return;
		tiers = new HashMap<>();
		if (!TooCruel.on( TooCruel.UNTIERED )) {
			pickTierSix( new Generator.Category[]{ Generator.Category.MIS_T2, Generator.Category.MIS_T3, Generator.Category.MIS_T4, Generator.Category.MIS_T5 } );
			pickTierSix( new Generator.Category[]{ Generator.Category.WEP_T2, Generator.Category.WEP_T3, Generator.Category.WEP_T4, Generator.Category.WEP_T5 } );
		}
	}

	private void pickTierSix( Generator.Category[] cats ) {
		ArrayList<Class<?>> items = new ArrayList<>();
		for (Generator.Category cat : cats) {
			if (cat.classes == null) continue;
			for (Class<?> cl : cat.classes) if (cl != null) items.add( cl );
		}
		if (!items.isEmpty()) tiers.put( items.get( Random.Int( items.size() ) ), 6 );
	}

	private int tierFor( Class<?> cls, int curTier ) {
		initTiers();
		Integer t = tiers.get( cls );
		if (t == null) {
			if (TooCruel.on( TooCruel.UNTIERED )) {
				t = 1;
			} else {
				int roll = Random.chances( new float[]{ 0.5f, curTier != 1 ? 0.20f : 0.25f, 0.25f, curTier != 1 ? 0.05f : 0f } );
				if (roll == 3) t = Random.Int( 1, 6 );
				else t = (int) GameMath.gate( 1, curTier + (roll - 1), 5 );
			}
			tiers.put( cls, t );
		}
		return t;
	}

	public void processInventory( Hero hero ) {
		boolean untiered = TooCruel.on( TooCruel.UNTIERED );
		boolean skipLevels = justRestored;
		justRestored = false;
		for (Item item : hero.belongings) {
			int deducted = 0;
			if (item instanceof MeleeWeapon) {
				MeleeWeapon w = (MeleeWeapon) item;
				int original = w.tier;
				w.tier = tierFor( w.getClass(), w.tier ) + universalBonus;
				deducted = original - w.tier;
			} else if (item instanceof MissileWeapon) {
				MissileWeapon w = (MissileWeapon) item;
				int original = w.tier;
				w.tier = tierFor( w.getClass(), w.tier ) + universalBonus;
				deducted = original - w.tier;
			}
			if (untiered && deducted > 0 && !skipLevels) {
				item.level( item.level() + deducted + universalBonus );
			}
		}
	}

	@Override
	public void onBossSlain() {
		if (!TooCruel.on( TooCruel.TIER_UP ) || !(target instanceof Hero)) return;
		universalBonus++;
		processInventory( (Hero) target );
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		initTiers();
		int[] t = new int[tiers.size()];
		Class<?>[] c = new Class<?>[tiers.size()];
		int i = 0;
		for (Class<?> k : tiers.keySet()) {
			c[i] = k;
			t[i] = tiers.get( k );
			i++;
		}
		bundle.put( "tiers", t );
		bundle.put( "classes", c );
		bundle.put( "universal_bonus", universalBonus );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		tiers = new HashMap<>();
		int[] t = bundle.getIntArray( "tiers" );
		Class<?>[] c = bundle.getClassArray( "classes" );
		if (t != null && c != null) {
			for (int i = 0; i < t.length && i < c.length; i++) if (c[i] != null) tiers.put( c[i], t[i] );
		}
		universalBonus = bundle.getInt( "universal_bonus" );
		justRestored = true;
	}
}
