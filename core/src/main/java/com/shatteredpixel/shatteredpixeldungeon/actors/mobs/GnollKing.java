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
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorruptGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.STRdown;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollKeeperSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollKingSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class GnollKing extends Mob {

    protected int breaks = 0;
	
	{
		spriteClass = GnollKingSprite.class;

		HP = HT = 1000;
		defenseSkill = 0;

		EXP = 30;
		
		baseSpeed = 0.75f;

		lootChance = 1f;

		FLEEING = new Fleeing();

		properties.add(Property.ORC);
		properties.add(Property.BOSS);
	}


	@Override
	public int damageRoll() {
		return Random.NormalIntRange(15,25);
	}

	@Override
	public int attackSkill( Char target) {
		return 100;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 5);
	}
	
	@Override
	public float speed() {
		if (breaks == 1) return 4*super.speed();
		else return super.speed();
	}

	
	@Override
    public boolean act() {

        if( 3 - breaks > 4 * HP / HT ) {
            breaks++;
			yell(Messages.get(this,"angry"));
            return true;
        } 
		
	    if (breaks == 1){
		    state = FLEEING;
		}
		
		if (breaks == 2){
			state = HUNTING;
		}
		
		if (breaks > 0){
			int newPos = -1;
				for (int i = 0; i < 10; i++) {
				newPos = Dungeon.level.randomRespawnCell(null);
				if (newPos != -1) {
					break;
				    }
			    }
		    if (Dungeon.level.mobs.size()<4){
				GnollKeeper.spawnAroundChance(newPos);
		    }  	
		}
        return super.act();
    }
	
	@Override
	public int attackProc(Char enemy, int damage) {
	
		if (breaks == 0){
		    if (Random.Int(2) == 0) {
			    if(enemy == Dungeon.hero){
			      Buff.prolong(enemy, Cripple.class, 3 );
			    }
		    }
		}
	
		
		if (breaks == 2){
			if (Random.Int(2) == 0) {
			    if(enemy == Dungeon.hero){
			       Buff.prolong(enemy, Roots.class, 3 );
			    }
		    }
		}
		
		if (breaks == 3){
			if (Random.Int(2) == 0) {
			    if(enemy == Dungeon.hero){
			    Buff.prolong(enemy, STRdown.class, 3);
			    }
		    }
		}

		return damage;
	}	
	
	@Override
	public int defenseProc(Char enemy, int damage) {

		int dmg = Random.IntRange(0, 20);
				
		if (breaks == 3){
		    if (dmg > 0 || (Random.Int(3) == 0 )) {
			enemy.damage(dmg, this,1);
		    }
        }
		return super.defenseProc(enemy, damage);
	}

	@Override
	public void damage( int dmg, Object src ) {
	
		dmg = Math.min(dmg,20);
		
		if (breaks == 2){
		    if (dmg > 15){
			    GameScene.add(Blob.seed(pos, 30, CorruptGas.class));
			}
		}
		super.damage( dmg, src );
	}	
	
	@Override
	protected boolean canAttack(Char enemy) {
		
		if (breaks == 2){
		return Dungeon.level.distance( pos, enemy.pos ) <= 2 ;}
		else return Dungeon.level.distance( pos, enemy.pos ) <= 1;
	}

	@Override
	public void die(Object cause) {
		
        super.die(cause);
		
		GameScene.bossSlain();
		Dungeon.level.drop(new Gold(Random.Int(1000, 1500)), pos).sprite.drop();
		
		
		
		yell(Messages.get(this,"die"));
		
	}
	
	@Override
	public void notice() {
		super.notice();
		yell(Messages.get(this,"notice"));
	}
	
	
    {
		resistances.add(ToxicGas.class);
		resistances.add(Poison.class);
		//resistances.add(EnchantmentDark.class);
		immunities.add(Terror.class);
		immunities.add(Amok.class);
		immunities.add(Charm.class);
		immunities.add(Sleep.class);
		immunities.add(Burning.class);
		immunities.add(ToxicGas.class);
		immunities.add(Vertigo.class);
		immunities.add(Paralysis.class);
	    immunities.add(Bleeding.class);
		immunities.add(CorruptGas.class);
	}
	
	
	private static final String BREAKS	= "breaks";
	
    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( BREAKS, breaks );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        breaks = bundle.getInt( BREAKS );
    }
	public static class GnollKeeper extends Mob {
		
		private static final float SPAWN_DELAY = 10f;
		
		{
			//name = "Gnoll Keeper";
			spriteClass = GnollKeeperSprite.class;

			HP = HT = 5;
			defenseSkill = 10;

			EXP = 0;

			state = WANDERING;
		}

		@Override
		public int attackSkill( Char target) {
			return 36;
		}

		@Override
		public int damageRoll() {
			return Random.NormalIntRange(0, 0);
		}

		@Override
		public int drRoll() {
			return 1;
		}

		@Override
		public int attackProc(Char enemy, int damage) {
			yell(Messages.get(this ,"safe"));
			return damage;
		}

		@Override
		public boolean act() {
			
			return super.act();
		}
		
		public static void spawnAround(int pos) {
		for (int n : PathFinder.NEIGHBOURS4) {
			int cell = pos + n;
			if (Dungeon.level.passable[cell] && Actor.findChar(cell) == null) {
				spawnAt(cell);
			}
		}
	}	
	
	public static void spawnAroundChance(int pos) {
		for (int n : PathFinder.NEIGHBOURS4) {
			int cell = pos + n;
			if (Dungeon.level.passable[cell] && Actor.findChar(cell) == null && Random.Float() < 0.75f) {
				spawnAt(cell);
			}
		}
	}
	
	public static GnollKeeper spawnAt(int pos) {

		GnollKeeper g1 = new GnollKeeper();
    	
			g1.pos = pos;
			g1.state = g1.HUNTING;
			GameScene.add(g1, SPAWN_DELAY);

			return g1;
     
        }

	}

}