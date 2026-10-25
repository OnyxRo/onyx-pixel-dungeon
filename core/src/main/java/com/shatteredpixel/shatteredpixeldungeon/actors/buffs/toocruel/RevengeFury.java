/*
 * Too Cruel Pixel Dungeon Trials (GPL-3.0), ported into Onyx's Pixel Dungeon.
 * Based on Shattered Pixel Dungeon by Evan Debenham.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.toocruel;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

//Revenge Fury, a monster that saw another die deals double damage and takes a fifth more
public class RevengeFury extends FlavourBuff implements TcBuff.AttackAmp, TcBuff.DamageAmp {

	@Override
	public float attackMultiplier() {
		return 2f;
	}

	@Override
	public float damageMultiplier( Object src ) {
		if (target.properties().contains( Char.Property.BOSS )) return 1f;
		return 1.2f;
	}

	@Override
	public int icon() {
		return BuffIndicator.RAGE;
	}

	@Override
	public String desc() {
		return Messages.get( this, "desc", dispTurns() );
	}
}
