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
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ConfusionGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.LightningTrap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.OtiluckStoneSprite;
import com.watabou.noosa.Camera;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import static com.shatteredpixel.shatteredpixeldungeon.actors.damagetype.DamageType.ENERGY_DAMAGE;

public class LitTower extends Mob implements Callback {
	
	private static final float TIME_TO_ZAP = 2f;

	private static final String TXT_LIGHTNING_KILLED = "%s's lightning bolt killed you...";

	{
		spriteClass = OtiluckStoneSprite.class;

		HP = HT = 600;
		defenseSkill = 1000;

		EXP = 25;
		state = PASSIVE;
		
		lootChance = 1f;

		properties.add(Property.MECH);
		properties.add(Property.BOSS);
		properties.add(Property.MAGICER);
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
	public int attackSkill( Char target) {
		return 100;
	}

	@Override
	public int drRoll() {
		return 1000;
	}
	

	@Override
	public void damage( int dmg, Object src ) {
	}
	
	@Override
	protected boolean act() {
		if(Dungeon.level.distance(pos, Dungeon.hero.pos)<5 && Dungeon.hero.isAlive() && checkOtiluke()){
			zapAll(Dungeon.hero.pos);
		}
		return super.act();
	}
	
	@Override
	public void call() {
		next();
	}
	
	protected boolean checkOtiluke(){
      boolean check = false;
		
		for (Mob mob : Dungeon.level.mobs) {
			if (mob instanceof Otiluke) {
			check=true;	
			}
		}
		return check;
	}

	@Override
	protected boolean canAttack(Char enemy) {
		return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
	}

	@Override
	protected boolean doAttack(Char enemy) {
		return false;
	}


	public void zapAll(int loc){
		
		yell(Messages.get(this,"zap"));
		
		Char hero=Dungeon.hero;
				
	    int mobDmg=Random.Int(100, 200);
		
		
		 boolean visible = Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[loc];
			
			
			  if (visible) {
				sprite.zap(loc);
			  }
			
			  
			  hero.damage(mobDmg,ENERGY_DAMAGE,2);

			  hero.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
			  hero.sprite.flash();
			
			  Camera.main.shake(2, 0.3f);			
	}
	
	@Override
	public boolean add( Buff buff ) {
		return false;
	}
	

	{
		
		resistances.add(LightningTrap.Electricity.class);

		immunities.add(ToxicGas.class);
		immunities.add(Terror.class);
		immunities.add(ConfusionGas.class);
	}

}
