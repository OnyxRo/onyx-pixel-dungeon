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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BeOld;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MossySkeletonSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class MossySkeleton extends Mob {

	private static final String TXT_KILLCOUNT = "Mossy Skeleton Kill Count: %s";

	{
		spriteClass = MossySkeletonSprite.class;

		HP = HT = 90+(10*Random.NormalIntRange(7, 10));
		defenseSkill = 20;

		EXP = 1;
		
		baseSpeed = 0.5f+(Math.min(1f, com.shatteredpixel.shatteredpixeldungeon.SpsStats.skeletonsKilled/50));

		lootChance = 0.5f; // by default, see die()
			
		lootChanceOther = 0.1f; // by default, see die()
		
		properties.add(Property.UNDEAD);
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(20+Math.round(com.shatteredpixel.shatteredpixeldungeon.SpsStats.skeletonsKilled/10), 45+Math.round(com.shatteredpixel.shatteredpixeldungeon.SpsStats.skeletonsKilled/5));
		
	}

	@Override
	public float attackDelay() {
		return 2f-(Math.min(1.5f, com.shatteredpixel.shatteredpixeldungeon.SpsStats.skeletonsKilled/50));
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		if (Random.Int(3) == 0) {
			Buff.affect(enemy, BeOld.class).set(20);
		}
		return damage;
	}
	
	@Override
	public void die(Object cause) {

		super.die(cause);
		
		com.shatteredpixel.shatteredpixeldungeon.SpsStats.skeletonsKilled++;
		GLog.w(Messages.get(Mob.class,"killcount", com.shatteredpixel.shatteredpixeldungeon.SpsStats.skeletonsKilled));
		
		if ( Dungeon.depth <27) {
		} else {
		}
		
		if(com.shatteredpixel.shatteredpixeldungeon.SpsStats.skeletonsKilled == 25) {
		}
		
	if(com.shatteredpixel.shatteredpixeldungeon.SpsStats.skeletonsKilled == 50) {
	}	
	
	if(com.shatteredpixel.shatteredpixeldungeon.SpsStats.skeletonsKilled == 100) {
	}		
	
	if (com.shatteredpixel.shatteredpixeldungeon.SpsStats.goldThievesKilled>99 && com.shatteredpixel.shatteredpixeldungeon.SpsStats.skeletonsKilled == 100 
			&& com.shatteredpixel.shatteredpixeldungeon.SpsStats.albinoPiranhasKilled>99 && com.shatteredpixel.shatteredpixeldungeon.SpsStats.archersKilled>99){
	}	
	
		if (Dungeon.level.heroFOV[pos]) {
			Sample.INSTANCE.play(Assets.Sounds.BONES);
		}
	}

	
	

	@Override
	public int attackSkill( Char target) {
		return 28;
	}

	@Override
	public int drRoll() {
		return 10+com.shatteredpixel.shatteredpixeldungeon.SpsStats.skeletonsKilled/5;
	}

	{
		//immunities.add(EnchantmentDark.class);
	}

}
