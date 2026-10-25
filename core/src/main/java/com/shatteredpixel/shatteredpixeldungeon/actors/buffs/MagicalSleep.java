/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class MagicalSleep extends Buff {

	private static final float STEP = 1f;
	public float xunPartialHeal = 0; //xun, Sleepwalking

	@Override
	public boolean attachTo( Char target ) {
		if (!target.isImmune(Sleep.class) && super.attachTo( target )) {
			
			if (!com.shatteredpixel.shatteredpixeldungeon.actors.hero.XunTalents.sleepwalks(target)) target.paralysed++; //xun
			
			if (target.alignment == Char.Alignment.ALLY) {
				if (target.HP == target.HT) {
					if (target instanceof  Hero) GLog.i(Messages.get(this, "toohealthy"));
					detach();
					return true;
				} else {
					if (target instanceof  Hero) GLog.i(Messages.get(this, "fallasleep"));
				}
			}

			if (target instanceof Mob && !com.shatteredpixel.shatteredpixeldungeon.actors.hero.XunTalents.sleepwalks(target)) { //xun
				((Mob) target).state = ((Mob) target).SLEEPING;
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean act(){
		if (target instanceof Mob && ((Mob) target).state != ((Mob) target).SLEEPING
				&& !com.shatteredpixel.shatteredpixeldungeon.actors.hero.XunTalents.sleepwalks(target)){ //xun
			detach();
			return true;
		}
		if (target.alignment == Char.Alignment.ALLY) {
			target.HP = Math.min(target.HP + com.shatteredpixel.shatteredpixeldungeon.actors.hero.XunTalents.sleepHeal(this), target.HT); //xun
			if (target instanceof  Hero && !com.shatteredpixel.shatteredpixeldungeon.actors.hero.XunTalents.sleepwalks(target)) ((Hero) target).resting = true; //xun
			if (target.HP == target.HT) {
				if (target instanceof  Hero) GLog.p(Messages.get(this, "wakeup"));
				detach();
			}
		}
		spend( STEP );
		return true;
	}

	@Override
	public void detach() {
		if (target.paralysed > 0 && !com.shatteredpixel.shatteredpixeldungeon.actors.hero.XunTalents.sleepwalks(target)) { //xun
			target.paralysed--;
		}
		if (target instanceof Hero) {
			((Hero) target).resting = false;
		} else if (target instanceof Mob && target.alignment == Char.Alignment.ALLY && ((Mob) target).state == ((Mob) target).SLEEPING){
			((Mob) target).state = ((Mob) target).WANDERING;
		}
		super.detach();
	}

	@Override
	public int icon() {
		return BuffIndicator.MAGIC_SLEEP;
	}

	@Override
	public void fx(boolean on) {
		if (!on && (target.paralysed <= 1) ) {
			//in case the character has visual paralysis from another source
			target.sprite.remove(CharSprite.State.PARALYSED);
		}
	}
}