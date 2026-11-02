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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.STRdown;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silent;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.OtilukeSprite;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import static com.shatteredpixel.shatteredpixeldungeon.actors.damagetype.DamageType.ENERGY_DAMAGE;

public class Otiluke extends Mob implements Callback {

	private static final float TIME_TO_ZAP = 1f;

	private static final String TXT_SHADOWBOLT_KILLED = "%s's shadow bolt killed you...";

	{
		spriteClass = OtilukeSprite.class;

		HP = HT = 10000;
		defenseSkill = 40;
		
		state=PASSIVE;

		EXP = 101;
		
		lootChance = 1f;

		properties.add(Property.ELEMENT);
		properties.add(Property.MAGICER);
		properties.add(Property.BOSS);
		
	}	
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange(150, 150+adj(0));
	}

	@Override
	public int attackSkill( Char target) {
		return 150+adj(0);
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(20, 40);
	}
	
	@Override
	public void damage( int dmg, Object src ) {

		if (state == PASSIVE) {
			state = HUNTING;
		}
		
		if(state==HUNTING){
			
			for (Mob mob : Dungeon.level.mobs) {
				if (mob != null && mob instanceof MineSentinel &&  Random.Int(20)<2) {
					if (mob.state==PASSIVE){
						mob.damage(1, this,3);
						mob.state = HUNTING;
					}
					break;
			}			
		  }
		}

		super.damage( dmg, src );
	}
	
	
	@Override
	public int attackProc(Char enemy, int damage) {
		
		enemy.damage(damageRoll()/2, ENERGY_DAMAGE,2);
		damage = damage/2;

		return damage;
	}	

	
	@Override
	protected boolean canAttack(Char enemy) {		if (buff(Silent.class) != null){
			return Dungeon.level.adjacent(pos, enemy.pos) && (!isCharmedBy(enemy));
		} else
		return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
	}

	@Override
	protected boolean doAttack(Char enemy) {

		if (Dungeon.level.adjacent(pos, enemy.pos)) {

			return super.doAttack(enemy);

		} else {

			boolean visible = Dungeon.level.heroFOV[pos]
					|| Dungeon.level.heroFOV[enemy.pos];
			if (visible) {
				sprite.zap(enemy.pos);
			} else {
				zap();
			}

			return !visible;
		}
	}

	private void zap() {
		spend(TIME_TO_ZAP);

		if (hit(this, enemy, true)) {
			if (enemy == Dungeon.hero && Random.Int(2) == 0) {
				Buff.prolong(enemy, STRdown.class, 6f);
			}

			int dmg = Random.Int(100, 160+adj(0));
			enemy.damage(dmg, this, 2);

			//if (!enemy.isAlive() && enemy == Dungeon.hero) {
			//	Dungeon.fail(Messages.format(ResultDescriptions.LOSE));
				//GLog.n(Messages.get(this, "kill"));
			//}
		} else {
			enemy.sprite.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
		}
	}

	public void onZapComplete() {
		zap();
		next();
	}

	@Override
	public void call() {
		next();
	}
	

	

	@Override
	public void die(Object cause) {
		super.die(cause);
		Dungeon.level.locked=false;
		
	}

	{
		resistances.add(ToxicGas.class);
		resistances.add(Poison.class);
		//resistances.add(EnchantmentDark.class);
		
		//immunities.add(EnchantmentDark.class);
		immunities.add(Terror.class);
		immunities.add(Amok.class);
		immunities.add(Charm.class);
		immunities.add(Sleep.class);
		immunities.add(Burning.class);
		immunities.add(ToxicGas.class);
		immunities.add(Vertigo.class);
		immunities.add(Paralysis.class);
	}

}
