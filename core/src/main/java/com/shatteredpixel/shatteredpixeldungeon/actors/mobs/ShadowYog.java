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
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SummoningTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShadowYogSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class ShadowYog extends Mob  {
	
	{
		spriteClass = ShadowYogSprite.class;

		HP = HT = 50*Dungeon.hero.lvl;
		
		baseSpeed = 2f;
		defenseSkill = 32;

		EXP = 100;

		state = PASSIVE;

		properties.add(Property.UNKNOW);
		properties.add(Property.BOSS);
	}
	
	private int yogsAlive = 0;
	private int breaks=0;
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

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(43, 83);
	}

	@Override
	public int attackSkill( Char target) {
		return 50;
	}

	public ShadowYog() {
		super();
	}

	@Override
	public int drRoll() {
		return 25;
	}

	@Override
	public boolean act() {

		if (5 - breaks > 6 * HP / HT) {
			breaks++;
			int newPos = -1;
			for (int i = 0; i < 20; i++) {
				newPos = Dungeon.level.randomRespawnCell(null);
				if (newPos != -1) {
					break;
				}
			}
			if (newPos != -1) {
					CellEmitter.get(pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
					pos = newPos;
					sprite.place(pos);
					sprite.visible = Dungeon.level.heroFOV[pos];
					GLog.n(Messages.get(this, "blink"));
			}
			return true;
		}
		return super.act();
	}

	@Override
	public void damage( int dmg, Object src ) {

			//for (Mob mob : Dungeon.level.mobs) {
			 //	mob.beckon(pos);
			//	}
			
			for (int i = 0; i < 4; i++) {
				int trapPos;
				int _tries1 = 0; //sps, gives up away from its own arena
				do {
					trapPos = Random.Int(Dungeon.level.length());
				} while ((!Dungeon.level.heroFOV[trapPos] || !Dungeon.level.passable[trapPos]) && ++_tries1 < 500);
				if (_tries1 >= 500) return;

				if (Dungeon.level.map[trapPos] == Terrain.INACTIVE_TRAP) {
					Dungeon.level.setTrap( new SummoningTrap().reveal(), trapPos );
					Level.set(trapPos,Terrain.TRAP);
					GameScene.updateMap(trapPos);
				}
			}
		if (Dungeon.level.mobs.size()<Dungeon.hero.lvl*2){
			//sps, the mod also called a fiend here, this game's Fiend has no spawner
		}
			super.damage( dmg, src );
	}

	@Override
	public int defenseProc(Char enemy, int damage) {
        return super.defenseProc(enemy, damage);
	}
	

	
	@Override
	public void beckon(int cell) {

	}

	@SuppressWarnings("unchecked")
	@Override
	public void die(Object cause) {
		super.die(cause);
		
		//Statistics.shadowYogsKilled++;

      for (Mob mob : Dungeon.level.mobs) {
			
			if (mob instanceof ShadowYog){
				   yogsAlive++;
				 }
			}
			
		 if(yogsAlive==0){
			GameScene.bossSlain();
			
			
			for (Mob mob : (Iterable<Mob>) Dungeon.level.mobs.clone()) {
				if (mob instanceof Rat || mob instanceof GoldOrc || mob instanceof Fiend || mob instanceof Eye) {
					mob.die(cause);
				}
			}
			
			yell(Messages.get(this,"die"));
		 }
	}

	@Override
	public void notice() {
		super.notice();
		yell(Messages.get(this,"illusion"));
	}
	
	{

		immunities.add(Terror.class);
		immunities.add(Amok.class);
		immunities.add(Charm.class);
		immunities.add(Sleep.class);
		immunities.add(Burning.class);
		immunities.add(ToxicGas.class);
		immunities.add(Paralysis.class);
		immunities.add(Vertigo.class);
	}



	}
