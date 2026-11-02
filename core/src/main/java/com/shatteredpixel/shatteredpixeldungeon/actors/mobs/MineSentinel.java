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

import com.watabou.utils.PathFinder;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon.Enchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SentinelSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class MineSentinel extends Mob {
	

	{
		spriteClass = SentinelSprite.class;

		EXP = 25;
		state = PASSIVE;
		
		properties.add(Property.MECH);
	}
	private static final int REGENERATION = 100;

	private Weapon weapon;

	public MineSentinel() {
		super();

		do {
			weapon = (Weapon) Generator.random(Generator.Category.WEAPON);
		} while (!(weapon instanceof MeleeWeapon) || weapon.level() < 0);

		weapon.identify();
		weapon.enchant(Weapon.Enchantment.random());
		weapon.upgrade(10);
		

		HP = HT = 400 + Dungeon.depth * 10;
		//HP = HT = 5;
		defenseSkill = 15;
		//defenseSkill = 2;
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
	protected boolean act() {
		
		Hero hero = Dungeon.hero;
		ArrayList<Integer> spawnPoints = new ArrayList<Integer>();
		
		
		if(state==HUNTING){
			for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
				int p = pos + PathFinder.NEIGHBOURS8[i];
				Char ch = Actor.findChar(p);
				if (ch != null && ch instanceof MineSentinel &&  Random.Int(10)<2) {
					ch.damage(1, this,3);
					if (((Mob)ch).state==PASSIVE){
						((Mob)ch).state = HUNTING;
					}
				 break;
				}
			}
			
		}
		
		if (!heroNear() && Random.Float() < 0.50f && state==HUNTING){
			for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
				int p = hero.pos + PathFinder.NEIGHBOURS8[i];
				if (Actor.findChar(p) == null
						&& (Dungeon.level.passable[p] || Dungeon.level.avoid[p])) {
					spawnPoints.add(p);
				}
			}
			
			if (spawnPoints.size() > 0) {
				int newPos;
				newPos=Random.element(spawnPoints);
				
				CellEmitter.get(pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
				pos = newPos;
				sprite.place(pos);
				sprite.visible = Dungeon.level.heroFOV[pos];
			}
			
		} else {
		
		 if (HP<(HT/4) && Random.Float() < 0.50f && state!=PASSIVE){
			int newPos = -1;
				for (int i = 0; i < 20; i++) {
				newPos = Dungeon.level.randomRespawnCell(this);
				if (newPos != -1) {
					break;
				}
			}
			if (newPos != -1) {
				
				CellEmitter.get(pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
				pos = newPos;
				sprite.place(pos);
				sprite.visible = Dungeon.level.heroFOV[pos];
				HP += REGENERATION;
			}					
			
		 }
		}
		return super.act();
	}

	@Override
	protected boolean canAttack(Char enemy) {
		//return checkOtiluke();
		if (checkOtiluke()) {
			return false;
		}else return Dungeon.level.adjacent(pos, enemy.pos) && (!isCharmedBy(enemy));
	}

	@Override
	public int defenseProc(Char enemy, int damage) {

		int dmg = Random.IntRange(0, damage/3);
		if (dmg > 0 && checkOtiluke()) {
			enemy.damage(dmg, this,1);
		}

		return super.defenseProc(enemy, damage);
	}

	protected boolean heroNear (){
		boolean check=false;
		for (int i : PathFinder.NEIGHBOURS9){
			int cell=pos+i;
			if (Actor.findChar(cell) != null	
				&& (Actor.findChar(cell) instanceof Hero)
				){
				check=true;
			}			
		}		
		return check;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(weapon.min(), weapon.max());
	}

	@Override
	public int attackSkill( Char target) {
		return (int) ((30 + Dungeon.depth *2) * 1f);
	}

	@Override
	public float attackDelay() {
		return weapon.DLY;
	}

	@Override
	public int drRoll() {
		//return Dungeon.level*3;
		return Random.NormalIntRange(3, 30);
	}

	@Override
	public void damage( int dmg, Object src ) {

		if (state == PASSIVE) {
			state = HUNTING;
		}

		super.damage( dmg, src );
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
		super.die(cause);
		Dungeon.level.drop(weapon, pos).sprite.drop();
	}

	@Override
	public boolean reset() {
		state = PASSIVE;
		return true;
	}

	protected boolean checkOtiluke(){
		boolean check = false;

		for (Mob mob : Dungeon.level.mobs) {
			if (false) {
				check=true;
			}
		}
		return check;
	}

	@Override
	public String description() {
		return Messages.get(this, "desc", weapon.name());
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
