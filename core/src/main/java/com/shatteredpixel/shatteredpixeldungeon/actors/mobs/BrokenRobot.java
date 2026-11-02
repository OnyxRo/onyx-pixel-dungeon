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
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.TarGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.StoneOre;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BrokenRobotSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import static com.shatteredpixel.shatteredpixeldungeon.actors.damagetype.DamageType.LIGHT_DAMAGE;

public class BrokenRobot extends Mob {

	private static final String TXT_DEATHGAZE_KILLED = "%s's deathray killed you...";
	private static final float SPAWN_DELAY = 2f;

	{
		spriteClass = BrokenRobotSprite.class;

		HP = HT = 120+(adj(0)*Random.NormalIntRange(4, 7));
		defenseSkill = 20+adj(1);
		viewDistance = Light.DISTANCE;

		EXP = 13;
		maxLvl = 25;

		
		lootOther = new StoneOre();
		lootChanceOther = 0.25f;
		
		loot = new ScrollOfRecharging();
		lootChance = 0.25f; // by default, see die()
		
		properties.add(Property.MAGICER);
		properties.add(Property.MECH);
	}

	private Ballistica beam;


	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 5);
	}

	@Override
	public boolean act() {
        GameScene.add(Blob.seed(pos,30, TarGas.class));
		if(enemySeen){
		  switch (Random.Int(50)) {
		  case 1:
			GLog.n(Messages.get(this,"explode") );
			explode(pos);
			if (HP<1){destroy();}
		  break;
		 }
		}

		return super.act();
	}



	@Override
	protected boolean canAttack(Char enemy) {

		beam = new Ballistica( pos, enemy.pos, Ballistica.STOP_SOLID);

		return beam.subPath(1, beam.dist).contains(enemy.pos);
	}

	@Override
	public int attackSkill( Char target) {
		return 20+adj(0);
	}

	@Override
	public float attackDelay() {
		return 3f;
	}
	
	@Override
	protected boolean doAttack(Char enemy) {

		spend(attackDelay());

		boolean rayVisible = false;

		for (int i : beam.subPath(0, beam.dist)) {
			if (Dungeon.level.heroFOV[i]) {
				rayVisible = true;
			}
		}

		if (rayVisible) {
			sprite.attack(beam.collisionPos);
			return false;
		} else {
			attack(enemy);
			return true;
		}
	}

	@Override
	public boolean attack( Char enemy, float dmgMulti, float dmgBonus, float accMulti ) {
		if (beam == null) beam = new Ballistica( pos, enemy.pos, Ballistica.STOP_SOLID ); //sps, a direct attack before any canAttack check

		for (int pos : beam.subPath(1, beam.dist)) {

			Char ch = Actor.findChar( pos );
			if (ch == null) {
				continue;
			}

			if (hit(this, ch, true)) {
				ch.damage(Random.NormalIntRange(2, 8+adj(0)), LIGHT_DAMAGE,2);

				if (Dungeon.level.heroFOV[pos]) {
					ch.sprite.flash();
					CellEmitter.center(pos).burst(PurpleParticle.BURST,
							Random.IntRange(1, 2));
				}

				//if (!ch.isAlive() && ch == Dungeon.hero) {
				//	Dungeon.fail(Messages.format(ResultDescriptions.LOSE));
					//GLog.n(Messages.get(this, "kill"));
				//}
			} else {
				ch.sprite.showStatus(CharSprite.NEUTRAL, ch.defenseVerb());
			}
		}

		return true;
	}

	public static void spawnAround(int pos) {
		for (int n : PathFinder.NEIGHBOURS4) {
			int cell = pos + n;
			if (Dungeon.level.passable[cell] && Actor.findChar(cell) == null) {
				spawnAt(cell);
			}
		}
	}
	
	public static BrokenRobot spawnAt(int pos) {
		
		BrokenRobot b = new BrokenRobot();  
    	
			b.pos = pos;
			b.state = b.HUNTING;
			GameScene.add(b, SPAWN_DELAY);

			return b;
     
     }
	
	
	public void explode(int cell) {
		// We're blowing up, so no need for a fuse anymore.
	
		Sample.INSTANCE.play(Assets.Sounds.BLAST, 2);

		if (Dungeon.level.heroFOV[cell]) {
			CellEmitter.center(cell).burst(BlastParticle.FACTORY, 30);
		}

		boolean terrainAffected = false;
		for (int n : PathFinder.NEIGHBOURS9) {
			int c = cell + n;
			if (c >= 0 && c < Dungeon.level.length()) {
				if (Dungeon.level.heroFOV[c]) {
					CellEmitter.get(c).burst(SmokeParticle.FACTORY, 4);
				}

				if (Dungeon.level.flamable[c]) {
					Level.set(c, Terrain.EMBERS);
					GameScene.updateMap(c);
					terrainAffected = true;
				}

				// destroys items / triggers bombs caught in the blast.
				Heap heap = Dungeon.level.heaps.get(c);
				if (heap != null)
					heap.explode();

				Char ch = Actor.findChar(c);
				if (ch != null) {
					// those not at the center of the blast take damage less
					// consistently.
					int minDamage = c == cell ? Dungeon.depth + 5 : 1;
					int maxDamage = 10 + Dungeon.depth * 2;

					int dmg = Random.NormalIntRange(minDamage, maxDamage)
							- Math.max(ch.drRoll(),0);
					if (dmg > 0) {
						ch.damage(dmg, this,1);
					}
 
					if (ch == this && HP<1){	
						die(this);
					}
				}
			}
		}

		if (terrainAffected) {
			Dungeon.observe();
		}
	}
	
	{
		
		immunities.add(Terror.class);
		immunities.add(ToxicGas.class);
	}

}
