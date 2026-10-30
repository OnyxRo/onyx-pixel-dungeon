/*
 * Onyx's Pixel Dungeon
 *
 * The Goo's moisture, from Shattered Pixel Lore by ReallyBasicGames (ReallyBasicGames/Shattered-Pixel-Lore),
 * where it was called Moisture. Standing in water raises it, it falls by 10 every 10 turns, and the
 * Goo's attacks hit harder the wetter it is. Once arid the Goo takes damage over time and stops healing.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.LoreGoo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class GooMoisture extends Buff {

	{
		type = buffType.POSITIVE;
	}

	private static final float STEP = 10f;

	public static final float MAX   = 100f;
	public static final float MOIST = 50f;
	public static final float DRY   = 30f;
	public static final float ARID  = 10f;

	private float level = 0;
	private float partialDamage;

	@Override
	public boolean act() {
		if (target.isAlive() && target instanceof Hero && LoreGoo.on()) {
			Hero hero = (Hero)target;
			if (isArid()) {
				partialDamage += STEP * target.HT / 1000f;
				if (partialDamage > 1){
					target.damage( (int)partialDamage, this );
					partialDamage -= (int)partialDamage;
				}
				GLog.w( Messages.get(this, "desc_intro_arid") );
			} else {
				float newLevel = level - STEP;
				if (newLevel <= ARID) {
					GLog.n( Messages.get(this, "onarid") );
					hero.resting = false;
					hero.damage( 1, this );
					hero.interrupt();
				} else if (newLevel <= DRY && level > DRY) {
					GLog.w( Messages.get(this, "ondry") );
				}
				level = newLevel;
			}
		}
		spend( STEP );
		return true;
	}

	public boolean isArid() {
		return level <= ARID;
	}

	public float level() {
		return level;
	}

	public void set( int value ) {
		level = Math.min(MAX, value + level);
		if (level <= 0) level = 1;
	}

	@Override
	public int icon() {
		return BuffIndicator.OOZE;
	}

	@Override
	public void tintIcon(Image icon) {
		if (level > MOIST)      icon.hardlight(0.3f, 0.6f, 1f);
		else if (level > DRY)   icon.hardlight(0.5f, 0.9f, 0.9f);
		else if (level > ARID)  icon.hardlight(1f, 0.8f, 0.3f);
		else                    icon.hardlight(1f, 0.3f, 0.2f);
	}

	@Override
	public float iconFadePercent() {
		return Math.max(0, (MAX - level) / MAX);
	}

	@Override
	public String iconTextDisplay() {
		return Integer.toString((int)Math.ceil(level));
	}

	@Override
	public String name() {
		if (level > DRY)        return Messages.get(this, "moist");
		else if (level > ARID)  return Messages.get(this, "dry");
		else                    return Messages.get(this, "arid");
	}

	@Override
	public String desc() {
		String result;
		if (level > MOIST)      result = Messages.get(this, "desc_intro_moist");
		else if (level > DRY)   result = Messages.get(this, "desc_intro_dry");
		else                    result = Messages.get(this, "desc_intro_arid");
		return result + Messages.get(this, "desc");
	}

	private static final String LEVEL         = "level";
	private static final String PARTIALDAMAGE = "partialDamage";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( LEVEL, level );
		bundle.put( PARTIALDAMAGE, partialDamage );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		level = bundle.getFloat( LEVEL );
		partialDamage = bundle.getFloat( PARTIALDAMAGE );
	}
}
