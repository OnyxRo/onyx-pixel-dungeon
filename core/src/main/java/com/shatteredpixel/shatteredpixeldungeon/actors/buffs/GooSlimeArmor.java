/*
 * Onyx's Pixel Dungeon
 *
 * The Goo's slime armor, from Shattered Pixel Lore by ReallyBasicGames (ReallyBasicGames/Shattered-Pixel-Lore),
 * where it was called SlimeArmor. Built up in water with the Hard Goo talent, it blocks between half
 * its level and its level of each hit, and wears off by two a turn as in the mod.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class GooSlimeArmor extends Buff {

	{
		type = buffType.POSITIVE;
	}

	private int level = 0;
	private int interval = 1;

	@Override
	public boolean act() {
		if (target.isAlive()) {
			level--;
			spend( interval );
			if (--level <= 0) detach();
		} else {
			detach();
		}
		return true;
	}

	public int level() {
		return level;
	}

	public void set( int value, int time ) {
		level = Math.min(20, value + level);
		interval = time;
	}

	@Override
	public int icon() {
		return BuffIndicator.ARMOR;
	}

	@Override
	public void tintIcon(Image icon) {
		icon.hardlight(0.4f, 1f, 0.4f);
	}

	@Override
	public float iconFadePercent() {
		return Math.max(0, (20 - level) / 20f);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", level, dispTurns(visualcooldown()));
	}

	private static final String LEVEL    = "level";
	private static final String INTERVAL = "interval";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( INTERVAL, interval );
		bundle.put( LEVEL, level );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		interval = bundle.getInt( INTERVAL );
		level = bundle.getInt( LEVEL );
	}
}
