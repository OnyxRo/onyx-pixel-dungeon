/*
 * Too Cruel Pixel Dungeon Trials (GPL-3.0), ported into Onyx's Pixel Dungeon.
 * Based on Shattered Pixel Dungeon by Evan Debenham.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.toocruel;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShieldBuff;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

//Certainty of Steel, health stays at 1 and all healing becomes shielding
public class SteelBody extends ShieldBuff {

	{
		actPriority = HERO_PRIO - 2;
		detachesAtZero = false;
	}

	public static final float REDUCTION_THRESHOLD = 1f;
	public static final float MAX_SHIELD = 2f;

	private float partialShielding = 0f;

	@Override
	public boolean attachTo( Char target ) {
		if (super.attachTo( target )) {
			if (shielding() == 0) {
				target.HP = 1;
				incShield( target.HT );
			}
			return true;
		}
		return false;
	}

	@Override
	public int absorbDamage( int dmg ) {
		return absorbNoDetach( this, dmg );
	}

	//shielding that never detaches, worked through shielding() and decShield() so Golden Colossus can change them
	static int absorbNoDetach( ShieldBuff b, int dmg ) {
		int s = b.shielding();
		if (s <= 0) return dmg;
		if (s >= dmg) {
			b.decShield( dmg );
			return 0;
		}
		b.decShield( s );
		return dmg - s;
	}

	@Override
	public boolean act() {
		if (target.HP > 1) {
			float add = target.HP - 1;
			float reductionThreshold = target.HT * REDUCTION_THRESHOLD;
			float maxShield = target.HT * MAX_SHIELD;
			if (shielding() > reductionThreshold) {
				add *= GameMath.gate( 0f, (maxShield - shielding()) / reductionThreshold, 1f );
			}
			add += partialShielding;
			if (add > 0) {
				int intAdd = (int) add;
				partialShielding = add - intAdd;
				incShield( intAdd );
			}
			if (shielding() > maxShield) {
				decShield( shielding() - (int) maxShield );
			}
			target.HP = 1;
		}
		wakeUp( target );
		spend( TICK );
		return true;
	}

	public static void wakeUp( Char target ) {
		MagicalSleep sleep = target.buff( MagicalSleep.class );
		if (sleep != null) {
			if (GameScene.scene != null) GameScene.add( Blob.seed( target.pos, 1, Electricity.class ) );
			if (Random.Float() < 0.1f) sleep.detach();
		}
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( "partialShielding", partialShielding );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		partialShielding = bundle.getFloat( "partialShielding" );
	}

	//Golden Colossus, the shield is the hero's gold
	public static class GoldenBody extends ShieldBuff {

		{
			actPriority = HERO_PRIO - 2;
			detachesAtZero = false;
		}

		private static final int LIMIT = 2;
		private static final float CURVE = 4.57f;

		@Override
		public boolean attachTo( Char target ) {
			if (super.attachTo( target )) {
				if (shielding() == 0) {
					target.HP = 1;
					incShield( target.HT );
				}
				return true;
			}
			return false;
		}

		@Override
		public int absorbDamage( int dmg ) {
			return absorbNoDetach( this, dmg );
		}

		private double scaling() {
			return CURVE * (1.0 + Dungeon.scalingDepth() / 50.0);
		}

		private int goldToShield( int gold ) {
			if (gold <= 0 || target == null) return 0;
			int upper = target.HT * LIMIT - 1;
			double s = LIMIT * target.HT;
			return Math.min( (int) (2.0 * s / (1.0 + Math.exp( -gold / (scaling() * s) )) - s), upper );
		}

		private int shieldToGold( int shield ) {
			if (shield <= 0) return 0;
			int max = target.HT * LIMIT - 1;
			if (shield > max) shield = max;
			double s = LIMIT * target.HT;
			return (int) (-scaling() * s * Math.log( 2.0 * s / (shield + s) - 1.0 ));
		}

		private int healingToGoldRatio() {
			return Dungeon.scalingDepth() / 5 + 1;
		}

		private void recalculate() {
			int gold = shielding();
			int stored = super.shielding();
			if (gold > stored) super.incShield( gold - stored );
			else if (gold < stored) super.decShield( stored - gold );
		}

		@Override
		public boolean act() {
			recalculate();
			if (target.HP > 1) {
				int addGold = (target.HP - 1) * healingToGoldRatio();
				target.HP = 1;
				if (shielding() < target.HT) {
					Dungeon.gold += addGold;
					if (target.sprite != null) target.sprite.showStatusWithIcon( CharSprite.NEUTRAL, Integer.toString( addGold ), FloatingText.GOLD );
				}
			}
			wakeUp( target );
			spend( TICK );
			return true;
		}

		@Override
		public void incShield( int amt ) {
			if (target == null) return;
			int shield = shielding();
			int max = target.HT * LIMIT - 1;
			if (shield >= max) return;
			int newShielding = Math.min( shield + Math.max( amt, 0 ), max );
			Dungeon.gold = Math.max( Dungeon.gold, shieldToGold( newShielding ) );
			recalculate();
		}

		@Override
		public void decShield( int amt ) {
			if (amt == 0 || target == null) return;
			int shield = shielding();
			if (amt >= shield) {
				Dungeon.gold = 0;
				super.decShield( super.shielding() );
				return;
			}
			int gold = shieldToGold( shield );
			int newGold = shieldToGold( shield - amt );
			Dungeon.gold = Math.max( 0, Dungeon.gold - (gold - newGold) );
			recalculate();
		}

		@Override
		public int shielding() {
			return goldToShield( Dungeon.gold );
		}
	}
}
