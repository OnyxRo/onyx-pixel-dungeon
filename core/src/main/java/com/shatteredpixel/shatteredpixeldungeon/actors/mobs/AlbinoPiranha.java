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
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.vegetable.NutVegetable;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.AlbinoPiranhaSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class AlbinoPiranha extends Mob {
	
	private static final String TXT_KILLCOUNT = "Albino Piranha Kill Count: %s";

	{
		spriteClass = AlbinoPiranhaSprite.class;

		baseSpeed = 1f;

		EXP = 5;
		
		lootChance = 0.1f;	
		
		properties.add(Property.FISHER);
	}

	public AlbinoPiranha() {
		super();

		HP = HT = 20 + com.shatteredpixel.shatteredpixeldungeon.SpsStats.albinoPiranhasKilled*2;
		defenseSkill = 40 + com.shatteredpixel.shatteredpixeldungeon.SpsStats.albinoPiranhasKilled/5;
	}

	protected boolean checkwater(int cell){
		return Dungeon.level.water[cell];
	}
	
		
	@Override
	protected boolean act() {
		
		if (!Dungeon.level.water[pos]) {
			die(null);
			return true;
					
				
		} else {
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
			}
	
			return super.act();
		}
	}


	@Override
	public int damageRoll() {
		return Random.NormalIntRange(com.shatteredpixel.shatteredpixeldungeon.SpsStats.albinoPiranhasKilled/2, 4 + com.shatteredpixel.shatteredpixeldungeon.SpsStats.albinoPiranhasKilled);
	}

	@Override
	public int attackSkill( Char target) {
		return 20 + com.shatteredpixel.shatteredpixeldungeon.SpsStats.albinoPiranhasKilled;
	}

	@Override
	public int drRoll() {
		return com.shatteredpixel.shatteredpixeldungeon.SpsStats.albinoPiranhasKilled;
	}

	@Override
	public void die(Object cause) {
		
		super.die(cause);	
		
		com.shatteredpixel.shatteredpixeldungeon.SpsStats.albinoPiranhasKilled++;
		GLog.w(Messages.get(Mob.class,"killcount",com.shatteredpixel.shatteredpixeldungeon.SpsStats.albinoPiranhasKilled));
			
		
		if(Random.Int(105-Math.min(com.shatteredpixel.shatteredpixeldungeon.SpsStats.albinoPiranhasKilled,100))==0){
		  Dungeon.level.drop(new NutVegetable(), pos).sprite.drop();
		}
		
	if(com.shatteredpixel.shatteredpixeldungeon.SpsStats.albinoPiranhasKilled == 25) {
	}
		
	if(com.shatteredpixel.shatteredpixeldungeon.SpsStats.albinoPiranhasKilled == 50) {
	}	
	
	if(com.shatteredpixel.shatteredpixeldungeon.SpsStats.albinoPiranhasKilled == 100) {
	}		
	
	if (com.shatteredpixel.shatteredpixeldungeon.SpsStats.goldThievesKilled>99 && com.shatteredpixel.shatteredpixeldungeon.SpsStats.skeletonsKilled>99 
			&& com.shatteredpixel.shatteredpixeldungeon.SpsStats.albinoPiranhasKilled == 100 && com.shatteredpixel.shatteredpixeldungeon.SpsStats.archersKilled>99){
	}	
		
		


	}

	@Override
	public boolean reset() {
		return true;
	}

	@Override
	protected boolean getCloser(int target) {

		if (rooted) {
			return false;
		}

		int step = Dungeon.findStep( this, target, Dungeon.level.water, Dungeon.level.heroFOV, true );
		if (step != -1) {
			move(step);
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected boolean getFurther(int target) {
		int step = Dungeon.flee( this, target, Dungeon.level.water, Dungeon.level.heroFOV, true );
		if (step != -1) {
			move(step);
			return true;
		} else {
			return false;
		}
	}

	{
		immunities.add(Burning.class);
		immunities.add(Paralysis.class);
		immunities.add(ToxicGas.class);
		immunities.add(Roots.class);
		immunities.add(Frost.class);
	}

}
