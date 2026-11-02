/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon.Enchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SentinelSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class SokobanSentinel extends Mob {	

	{
		spriteClass = SentinelSprite.class;
		
		baseSpeed = 0.5f;

		EXP = 18;
		state = PASSIVE;
		
		properties.add(Property.MECH);
	}

	private Weapon weapon;

	public SokobanSentinel() {
		super();

		do {
			weapon = (Weapon) Generator.random(Generator.Category.WEAPON);
		} while (!(weapon instanceof MeleeWeapon));
		if (weapon.level() < 3) weapon.upgrade(3 - weapon.level()); //sps, this game never rolls a +3 weapon, the old loop never ended

		weapon.identify();
		weapon.enchant(Enchantment.random());
		weapon.upgrade();
		weapon.upgrade();
		weapon.upgrade();
		weapon.upgrade();
		weapon.upgrade();
		
		
		HP = HT = 500;
		defenseSkill = 40;
	}

	private static final String WEAPON = "weapon";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(WEAPON, weapon);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		weapon = (Weapon) bundle.get(WEAPON);
	}

	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange(weapon.min(), weapon.max());
	}

	@Override
	public int attackSkill( Char target) {
		return 40;
	}

	@Override
	public float attackDelay() {
		return weapon.DLY;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, Dungeon.depth);
	}

	@Override
	public void damage( int dmg, Object src ) {

		if (state == PASSIVE) {
			state = HUNTING;
		}

		super.damage( dmg, src );
	}
	
	@Override
	protected boolean getCloser(int target) {

		if (rooted) {
			return false;
		}

		int step = Dungeon.findStep( this, target, Dungeon.level.passable, Dungeon.level.heroFOV, true );
		if (step != -1 && !Dungeon.level.avoid[step]) {
			move(step);
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	protected boolean act() {
		
				
			// this causes pirahna to move away when a door is closed on them.
			enemy = chooseEnemy();
			if (state == this.HUNTING
					&& !(enemy.isAlive() && Dungeon.level.heroFOV[enemy.pos] && enemy.invisible <= 0)) {
				state = this.WANDERING;
				int oldPos = pos;
				int i = 0;
				do {
					i++;
					target = Dungeon.level.randomDestination( this );
					if (i == 100)
						return true;
				} while (!getCloser(target));
				moveSprite(oldPos, pos);
				return true;
			} else if (state == this.PASSIVE
					&& !(enemy.isAlive() && Dungeon.level.heroFOV[enemy.pos] && enemy.invisible <= 0)){
				    state = this.HUNTING;				
			}

			return super.act();
		
	}		
	@Override
	public int attackProc(Char enemy, int damage) {
		weapon.proc(this, enemy, damage);
		return damage;
	}

	@Override
	public void beckon(int cell) {
		// Do nothing
	}

	@Override
	public void die(Object cause) {
		//Dungeon.level.drop(weapon, pos).sprite.drop();		
		super.die(cause);
	}

	@Override
	public String description() {
		return Messages.get(this, "desc", weapon.name());
	}

	{
		resistances.add(ToxicGas.class);
		resistances.add(Poison.class);
		//resistances.add(EnchantmentDark.class);
		
	}


}
