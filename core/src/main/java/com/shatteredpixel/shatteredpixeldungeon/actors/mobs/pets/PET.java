/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * Goblins Pixel Dungeon, pets by Mario Braun (locastan), based on Unleashed Pixel Dungeon
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
package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

/**
 * A Goblins PD pet. It is an ordinary ally here (Shattered's own ally AI follows the hero and
 * comes down the stairs when next to the hero), plus the mod's level, experience, cooldown and
 * "your pet may leave you" rules. The mod's hero fields (haspet, petType and so on) and its pet
 * tab are not ported, the pet keeps its own numbers and saves them itself.
 */
public abstract class PET extends Mob {

	{
		HP = HT = 1;
		EXP = 0;

		alignment = Alignment.ALLY;
		intelligentAlly = true;
		state = WANDERING;
	}

	public int level;
	public int kills;
	public int type;
	public int experience;
	public int cooldown;
	public int goaways = 0;
	public boolean callback = false;
	public boolean stay = false;

	private static final String KILLS = "kills";
	private static final String LEVEL = "level";
	private static final String TYPE = "type";
	private static final String EXPERIENCE = "experience";
	private static final String COOLDOWN = "cooldown";
	private static final String GOAWAYS = "goaways";
	private static final String CALLBACK = "callback";
	private static final String STAY = "stay";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(KILLS, kills);
		bundle.put(LEVEL, level);
		bundle.put(TYPE, type);
		bundle.put(EXPERIENCE, experience);
		bundle.put(COOLDOWN, cooldown);
		bundle.put(GOAWAYS, goaways);
		bundle.put(CALLBACK, callback);
		bundle.put(STAY, stay);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		kills = bundle.getInt(KILLS);
		level = bundle.getInt(LEVEL);
		type = bundle.getInt(TYPE);
		experience = bundle.getInt(EXPERIENCE);
		cooldown = bundle.getInt(COOLDOWN);
		goaways = bundle.getInt(GOAWAYS);
		callback = bundle.getBoolean(CALLBACK);
		stay = bundle.getBoolean(STAY);
		int hp = HP;
		adjustStats(level);
		HP = Math.min(hp, HT);
	}

	//each pet writes its own numbers for a level here
	public void adjustStats(int level) {
	}

	public void spawn(int level) {
		this.level = level;
		adjustStats(level);
	}

	//the mod's pets are tied to one owner, so only one lives at a time
	public static PET currentPet() {
		if (Dungeon.level == null) return null;
		for (Mob mob : Dungeon.level.mobs) {
			if (mob instanceof PET) return (PET) mob;
		}
		return null;
	}

	@Override
	public void damage(int dmg, Object src) {
		if (src instanceof Hero) {
			goaways++;
			GLog.n("Keep that up and your %s may leave you!", name());
			if (goaways > 2) {
				flee(false);
				return;
			}
		}
		super.damage(dmg, src);
	}

	@Override
	public void die(Object cause) {
		GLog.n("Your %s dies.", name());
		PetOwner.petGone(true);
		super.die(cause);
	}

	public void flee(boolean ownerisdead) {
		if (ownerisdead) {
			GLog.w("Your %s retreats to fight another day.", name());
		} else {
			GLog.n("Your %s knows when it's not wanted!", name());
		}
		CellEmitter.get(pos).burst(ElmoParticle.FACTORY, 6);
		PetOwner.petGone(false);
		destroy();
		if (sprite != null) sprite.killAndErase();
	}

	public int calccooldown(int basecool, int level) {
		return Math.max(basecool / (Math.max(level, 1) * 5), 0);
	}

	//the mod counts a kill when the target it was fighting is dead
	@Override
	protected Char chooseEnemy() {
		if (enemy != null && !enemy.isAlive() && enemy instanceof Mob && enemy.alignment == Alignment.ENEMY) {
			kills++;
			experience += ((Mob) enemy).EXP;
			if (experience >= level * (level + level) * 3 && level < 20) {
				level++;
				GLog.p("Your pet %s gains a level!", name());
				int hp = HP;
				adjustStats(level);
				HP = Math.min(HT, hp + (HT - hp) / 4);
				experience = 0;
			}
		}
		return super.chooseEnemy();
	}

	@Override
	protected boolean getCloser(int target) {
		if (stay) return false;
		return super.getCloser(target);
	}

	//pets are never tempted by noise
	@Override
	public void beckon(int cell) {
	}

	//the mod pets had a no argument hook for a click on the pet, the swap is Shattered's own now
	public void interact() {
	}
}
