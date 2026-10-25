/*
 * Too Cruel Pixel Dungeon Trials (GPL-3.0), ported into Onyx's Pixel Dungeon.
 * Based on Shattered Pixel Dungeon by Evan Debenham.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.toocruel;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.TooCruel;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

//Arrowhead, each kill makes the hero take 30% more damage and monsters take 10% more
public class Arrowhead extends Buff implements TcBuff.DamageAmp {

	{
		type = buffType.POSITIVE;
	}

	private int stacks = 0;

	private static float cooldownTime() {
		return TooCruel.on( TooCruel.THUNDERSTRUCK ) ? 420f : 20f;
	}

	@Override
	public boolean act() {
		stacks--;
		if (stacks <= 0) {
			detach();
			return true;
		}
		spend( cooldownTime() );
		return true;
	}

	@Override
	public int icon() {
		return BuffIndicator.FURY;
	}

	@Override
	public String iconTextDisplay() {
		return Integer.toString( stacks );
	}

	@Override
	public String desc() {
		return Messages.get( this, "desc", stacks, Math.round( stacks * 100 * 0.3f ), Math.round( stacks * 100 * 0.1f ) );
	}

	@Override
	public float damageMultiplier( Object src ) {
		if (src instanceof Hunger) return 1f;
		return 1 + stacks * 0.3f;
	}

	public int stacks() {
		return stacks;
	}

	public Arrowhead addStack() {
		stacks++;
		postpone( cooldownTime() );
		return this;
	}

	public Arrowhead set( int stacks ) {
		this.stacks = stacks;
		postpone( cooldownTime() );
		return this;
	}

	private static final String STACKS = "arrowhead_stacks";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( STACKS, stacks );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		stacks = bundle.getInt( STACKS );
	}

	//monsters take more damage for each stack the hero has
	public static class MobArrowhead extends Buff implements TcBuff.DamageAmp {
		{
			revivePersists = true;
		}

		@Override
		public float damageMultiplier( Object src ) {
			Arrowhead a = Dungeon.hero == null ? null : Dungeon.hero.buff( Arrowhead.class );
			return a == null ? 1f : 1 + a.stacks * 0.1f;
		}
	}
}
