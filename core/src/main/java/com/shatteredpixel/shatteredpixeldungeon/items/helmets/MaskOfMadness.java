/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * From Ryuse's PD. This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.items.helmets;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

public class MaskOfMadness extends Helmet {

	{
		image = ItemSpriteSheet.MASK_OF_MADNESS;
		unique = true;
	}

	@Override
	protected boolean hasEffect() {
		return false;
	}

	private int exp = 0;

	@Override
	public boolean isUpgradable() {
		return true;
	}

	@Override
	public boolean doUnequip(Hero hero, boolean collect, boolean single) {
		GLog.n(Messages.get(this, "cannot_unequip"));
		return false;
	}

	@Override
	public int procGivenDamage(Hero hero, Char enemy, int dmg, boolean ranged) {
		float ratio = 2.1f - 1.5f / ((float) Math.exp(level() / 3f) + 0.5f);
		return (int) (dmg * ratio);
	}

	@Override
	public float procTakenDamage(Hero hero, float dmg, Object src) {
		float ratio = 1.8f - 1.5f / ((float) Math.exp(level() / 3f) + 1f) + 0.05f * level();
		return super.procTakenDamage(hero, dmg * ratio, src);
	}

	@Override
	public void onKill(Hero hero, Char victim) {
		exp += victim.properties().contains(Char.Property.BOSS) ? 3 : 1;
		if (exp >= level() * 2 && level() < 10) {
			exp -= level() * 2;
			upgrade();
			GLog.p(Messages.get(this, "levelup"));
		}
	}

	@Override
	public int value() {
		return 0;
	}

	private static final String EXP = "exp";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(EXP, exp);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		exp = bundle.getInt(EXP);
	}
}
