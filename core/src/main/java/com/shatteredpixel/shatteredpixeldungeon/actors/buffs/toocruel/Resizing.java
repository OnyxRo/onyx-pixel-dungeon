/*
 * Too Cruel Pixel Dungeon Trials (GPL-3.0), ported into Onyx's Pixel Dungeon.
 * Based on Shattered Pixel Dungeon by Evan Debenham.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.toocruel;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

//Crowd Diversity and Recursive Hierarchy, a monster of a random size
public class Resizing extends Buff implements TcBuff.AttackAmp, TcBuff.Timescale, TcBuff.HtBoost,
		TcBuff.DrRoll, TcBuff.DefRoll, TcBuff.PersistNesting {

	public static final float MIN_THRESHOLD = 0.5f;
	public static final float MAX_THRESHOLD = 1.75f;

	private float factor = 1f;
	//the monster's health before any resizing
	private int originalHT = -1;

	@Override
	public boolean attachTo( Char target ) {
		if (target.properties().contains( Char.Property.BOSS ) || target.properties().contains( Char.Property.MINIBOSS )) {
			return false;
		}
		if (super.attachTo( target )) {
			target.needsShieldUpdate = true;
			return true;
		}
		return false;
	}

	@Override
	public void detach() {
		target.needsShieldUpdate = true;
		super.detach();
	}

	public Resizing multiply( float factor ) {
		this.factor *= factor;
		if (this.factor < MIN_THRESHOLD) this.factor = MIN_THRESHOLD;
		recalculateHT();
		return this;
	}

	public void multiplyRandom() {
		multiply( Random.NormalFloat( MIN_THRESHOLD, MAX_THRESHOLD ) );
	}

	private void recalculateHT() {
		if (!(target instanceof Mob) || target instanceof NPC || target.properties().contains( Char.Property.BOSS )) return;
		if (originalHT < 0) originalHT = target.HT;
		float percent = target.HT > 0 ? target.HP / (float) target.HT : 1f;
		target.HT = Math.max( 1, (int) (originalHT * htMultiplier()) );
		target.HP = Math.max( 1, (int) (target.HT * percent) );
		if (fxOn) fx( true );
	}

	@Override
	public float attackMultiplier() {
		return forwardBuff();
	}

	@Override
	public float speedFactor() {
		return backwardAsimptBuff();
	}

	@Override
	public float htMultiplier() {
		float b = forwardBuff();
		if (factor > 1.25f) b *= 1.25f;
		return b;
	}

	@Override
	public int drRollBonus() {
		return 0;
	}

	@Override
	public float drMultBonus() {
		return forwardBuff();
	}

	@Override
	public float defRollMultiplier( Char attacker ) {
		return backwardsToZeroBuff();
	}

	@Override
	public void applyNestingEffect( Char target ) {
		Buff.affect( target, Resizing.class ).multiply( factor );
	}

	private float forwardBuff() {
		float f = Math.max( factor, MIN_THRESHOLD );
		if (f < MAX_THRESHOLD) return 0.333777f * f * f * f + 0.66622293f;
		return f * 1.4f + 0.00506f;
	}

	private float backwardAsimptBuff() {
		float f = Math.max( factor, MIN_THRESHOLD );
		return 0.491147f / (f * f) + 0.5088528f;
	}

	private float backwardsToZeroBuff() {
		if (factor < 1f) return backwardAsimptBuff();
		return Math.max( 1.5f - factor / 2, 0f );
	}

	private static String two( float v ) {
		return Messages.decimalFormat( "##.##", Math.round( v * 100 ) / 100.0 );
	}

	@Override
	public int icon() {
		return BuffIndicator.UPGRADE;
	}

	@Override
	public void tintIcon( Image icon ) {
		icon.hardlight( 0x00FFFF );
	}

	@Override
	public String desc() {
		return Messages.get( this, "desc", Math.round( factor * 100f ), two( attackMultiplier() ), two( drMultBonus() ),
				two( backwardsToZeroBuff() ), two( htMultiplier() ), two( speedFactor() ) );
	}

	private boolean fxOn = false;

	@Override
	public void fx( boolean on ) {
		fxOn = on;
		if (target.sprite == null) return;
		if (on) {
			target.sprite.scale.set( GameMath.gate( MIN_THRESHOLD, factor, MAX_THRESHOLD ) );
			target.sprite.place( target.pos );
		} else {
			target.sprite.scale.set( 1f );
		}
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( "factor", factor );
		bundle.put( "original_ht", originalHT );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		factor = bundle.getFloat( "factor" );
		originalHT = bundle.contains( "original_ht" ) ? bundle.getInt( "original_ht" ) : -1;
	}
}
