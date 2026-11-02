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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silent;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.LightningTrap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShellSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Shell extends Mob implements Callback {
	
	private static final float TIME_TO_ZAP = 2f;

	{
		spriteClass = ShellSprite.class;

		HP = HT = 500;
		defenseSkill = 0;

		EXP = 50;
		state = PASSIVE;
		
		lootChance = 1f;

		properties.add(Property.MECH);
		properties.add(Property.BOSS);
	}

	private int shellCharge=0;

	private static final String SHELLCHARGE	= "shellCharge";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put( SHELLCHARGE, shellCharge );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		shellCharge = bundle.getInt( SHELLCHARGE );
	}


	@Override
	public void beckon(int cell) {
		// Do nothing
	}

	@Override
	public int damageRoll() {
		return 0;
	}

	@Override
	public void damage( int dmg, Object src ) {
		if(shellCharge>0){zapAround(1);}
		super.damage( dmg, src );
	}
	
	@Override
	public int attackSkill( Char target) {
		return 100;
	}

	@Override
	public int drRoll() {
		return 0;
	}
	
	@Override
	protected boolean act() {

		if(Random.Int(shellCharge)>20 && Dungeon.hero.isAlive()){
			zapAll(1);
		}
		return super.act();
	}
	
	@Override
	public void call() {
		next();
	}
	

	@Override
	protected boolean canAttack(Char enemy) {		if (buff(Silent.class) != null){
			return Dungeon.level.adjacent(pos, enemy.pos) && (!isCharmedBy(enemy));
		} else
		return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
	}

	@Override
	protected boolean doAttack(Char enemy) {

		if (Dungeon.level.distance(pos, enemy.pos) <= 1) {

			return super.doAttack(enemy);

		} else {

			boolean visible = Dungeon.level.heroFOV[pos]
					|| Dungeon.level.heroFOV[enemy.pos];
			if (visible) {
				sprite.zap(enemy.pos);
			}			
			zapAll(10);
			spend(TIME_TO_ZAP);

			if (hit(this, enemy, true)) {
				int dmg = Random.Int(Math.round(shellCharge/4), Math.round(shellCharge/2));
				shellCharge-=dmg;
				
				if (Dungeon.level.water[enemy.pos] && !enemy.flying) {
					dmg *= 1.5f;
				}
				enemy.damage(dmg, this,2);

				enemy.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
				enemy.sprite.flash();

				if (enemy == Dungeon.hero) {

					Camera.main.shake(2, 0.3f);

					//if (!enemy.isAlive()) {
					//	Dungeon.fail(Messages.format(ResultDescriptions.LOSE));
						//GLog.n(Messages.get(this, "kill"));
					//}
				}
			} else {
				enemy.sprite
						.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
			}

			return !visible;
		}
	}


	public void zapAll(int dmg){
		
		GLog.n(Messages.get(this, "zap"));
		
		int heroDmg=0;
		int mobDmg=Random.Int(1, 2+Math.round(dmg/4));
		
		for (Mob mob : Dungeon.level.mobs) {
				
			
		  if (Dungeon.level.distance(pos, mob.pos) > 1 && mob.isAlive()){
			  boolean visible = Dungeon.level.heroFOV[pos]
					|| Dungeon.level.heroFOV[mob.pos];
			
			
			  if (visible) {
				sprite.zap(mob.pos);
			  }

			  mob.damage(mobDmg, this,2);

			  mob.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
			  mob.sprite.flash();
			
			}
		}
		
		
		if (Dungeon.hero.isAlive()){
			
		       Char hero=Dungeon.hero;
		
		if (Dungeon.level.distance(pos, hero.pos) > 1){
		
		boolean visibleHero = Dungeon.level.heroFOV[pos]
				|| Dungeon.level.heroFOV[hero.pos];
		if (visibleHero && Random.Int(4) > 2 ) {
			sprite.zap(hero.pos);
		}
		
		heroDmg = Random.Int(Math.round(shellCharge/4), Math.round(shellCharge/2));
		shellCharge-=heroDmg;

		hero.damage(heroDmg, this,2);

		hero.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
		hero.sprite.flash();
		
		}
		}
		

	}
	
public void zapAround(int dmg){
		
		GLog.n(Messages.get(this, "zap"));
		
		int heroDmg=0;
		int mobDmg=Random.Int(1, 2+Math.round(dmg/4));
		
		for (int n : PathFinder.NEIGHBOURS8) {
			int c = pos + n;
			
			Char ch = Actor.findChar(c);
			if (ch != null && ch != Dungeon.hero && ch.isAlive()) {
				
					 boolean visible = Dungeon.level.heroFOV[pos]
							|| Dungeon.level.heroFOV[ch.pos];
					  
					  if (visible) {
						sprite.zap(ch.pos);
					  }
					
			  if (Dungeon.level.water[ch.pos] && !ch.flying) {
				  mobDmg *= 1.5f;
			  }
			  ch.damage(mobDmg, this,2);

			  ch.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
			  ch.sprite.flash();
			
			  
			}  else if (ch != null && ch == Dungeon.hero && ch.isAlive()){
				
				heroDmg = Random.Int(shellCharge/2, shellCharge);
				
				if(dmg<shellCharge){
				    shellCharge-=dmg;
				} else {
					shellCharge=0;
				}
				
				
				boolean visible = Dungeon.level.heroFOV[pos]
						|| Dungeon.level.heroFOV[ch.pos];
				  
				  if (visible) {
					sprite.zap(ch.pos);
				  }

				ch.damage(heroDmg, this,2);

				ch.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
				ch.sprite.flash();
				
			}
						
		}		

	}
	
	@Override
	public boolean add( Buff buff ) {
		return false;
	}
	
	@Override
	public void die(Object cause) {
		super.die(cause);
		shellCharge=0;
		
		
	}

	{
		//resistances.add(EnchantmentDark.class);
		
		resistances.add(LightningTrap.Electricity.class);
		immunities.add(ToxicGas.class);
		immunities.add(Terror.class);
	}	
}
