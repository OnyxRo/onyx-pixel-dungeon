/*
 * Too Cruel Pixel Dungeon Trials (GPL-3.0), ported into Onyx's Pixel Dungeon.
 * Based on Shattered Pixel Dungeon by Evan Debenham.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.toocruel;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

//Revenge Rage, overkill damage on a monster is added to the attacks of the monsters that saw it
public class RevengeRage extends Buff implements TcBuff.AttackAmp {

	{
		type = buffType.POSITIVE;
	}

	private int boost = 0;

	public void add( int amount ) {
		boost = Math.min( boost + amount, 9001 );
	}

	@Override
	public float flatAttackBonusPostMult() {
		return boost;
	}

	@Override
	public int icon() {
		return BuffIndicator.FURY;
	}

	@Override
	public String desc() {
		return Messages.get( this, "desc", boost );
	}

	private com.watabou.noosa.particles.Emitter emitter;

	@Override
	public void fx( boolean on ) {
		if (on) {
			if (target.sprite == null) return;
			emitter = target.sprite.emitter();
			emitter.pour( com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle.FACTORY, 0.3f );
		} else if (emitter != null) {
			emitter.on = false;
			emitter = null;
		}
	}

	private static final String BOOST = "attack_boost";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( BOOST, boost );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		boost = bundle.getInt( BOOST );
	}
}
