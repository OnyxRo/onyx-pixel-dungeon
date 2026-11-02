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
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AttackUp;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.DefenceUp;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.GlassShield;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Knuckles;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BeastYearSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class YearBeast2 extends Mob {
	protected static final float SPAWN_DELAY = 1f;
	{
		//name = "YearBeast";
		spriteClass = BeastYearSprite.class;
		baseSpeed = 1f;

		HP = HT = 1000;
		EXP = 0;
		defenseSkill = 30;
		viewDistance = 6;
		baseSpeed=1.5f;
		flying = true;
	}

	private int times=0;

	private static final String TIMES	= "times";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put( TIMES, times );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		times = bundle.getInt( TIMES );
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(40, 60);
	}

	@Override
	public int attackSkill( Char target) {
		return 40;
	}


	@Override
	public boolean act() {
		times++;
		return super.act();
	}
	@Override
	protected boolean canAttack(Char enemy) {
		return Dungeon.level.distance( pos, enemy.pos ) <= 2 ;
	}
	@Override
	public int attackProc(Char enemy, int damage) {

		if (Random.Int(2) == 0) {
			Buff.affect(enemy, Burning.class).reignite(enemy);
		} else {
			Buff.affect(enemy, Frost.class);
		}

		if (Random.Int(5) == 0) {
			Buff.affect(enemy, Charm.class,4f);
		}

		if (Random.Int(5) == 0) {
		int oppositeDefender = enemy.pos + (enemy.pos - pos);
		Ballistica trajectory = new Ballistica(enemy.pos, oppositeDefender, Ballistica.MAGIC_BOLT);
		com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave.throwChar(enemy, trajectory, 1, false, true, this);
		Buff.affect(enemy, Vertigo.class,3f);
		}

		if (enemy == Dungeon.hero) {
			Hero hero = Dungeon.hero;
			KindOfWeapon weapon = hero.belongings.weapon;
			com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor armor = hero.belongings.armor;
            if (Random.Int(10)==0){
            	if (Random.Int(2)==0) {
					if (weapon != null && !(weapon instanceof Knuckles)
							&& !weapon.cursed) {
						hero.belongings.weapon = null;
						Dungeon.quickslot.clearItem(weapon);
					    weapon.updateQuickslot();
						Dungeon.level.drop(weapon, hero.pos).sprite.drop();
						GLog.w(Messages.get(this, "disarm"));
					}
				} else {
					if (armor != null && true
							&& !armor.cursed) {
						hero.belongings.armor = null;
						Dungeon.level.drop(armor, hero.pos).sprite.drop();
						GLog.w(Messages.get(this, "disarm"));
					}
				}
            }
		}

		return damage;
	}

	@Override
	public int defenseProc(Char enemy, int damage) {

		int dmg = damage;
		if (dmg > 200 && buff(GlassShield.class) == null) {
			Buff.affect(this,GlassShield.class).turns(3);
		}
		Buff.prolong(this,DefenceUp.class,2f).level(times);
		Buff.prolong(this,AttackUp.class,4f).level(times);
		return super.defenseProc(enemy, damage);
	}

	@Override
	public void damage( int dmg, Object src ) {

		if(src instanceof Wand) {
			dmg = dmg/3;
		}

        if ( false) {
            times=0;
        }

	    if (times > 50)
		times-=3;
	
		super.damage( dmg, src );
	}


	@Override
	public void die(Object cause) {
		super.die(cause);
	}

	@Override
	public void notice() {
		super.notice();
		yell(Messages.get(this, "notice"));
	}

    {
		resistances.add(ToxicGas.class);
		resistances.add(Poison.class);
		immunities.add(Charm.class);
		immunities.add(Sleep.class);
		immunities.add(Terror.class);
		immunities.add(Fire.class);
		immunities.add(Vertigo.class);
		immunities.add(Burning.class);
	}

}
