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
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.DungeonBomb;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.damageblobs.FireEffectDamage;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.GlassShield;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.STRdown;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Chains;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.EtherealChains;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.WornKey;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Blindweed;
import com.shatteredpixel.shatteredpixeldungeon.plants.Firebloom;
import com.shatteredpixel.shatteredpixeldungeon.plants.Icecap;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sorrowmoss;
import com.shatteredpixel.shatteredpixeldungeon.plants.Stormvine;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PrisonWanderSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SeekingBombSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.actors.damagetype.DamageType.SHOCK_DAMAGE;

public class PrisonWander extends Mob {

	private boolean chainsUsed = false;

	{
		spriteClass = PrisonWanderSprite.class;

		HP = HT = 800;
		EXP = 40;
		defenseSkill = 20;
		viewDistance = 7;

		properties.add(Property.HUMAN);
		properties.add(Property.BOSS);

		loot = new EtherealChains();
		lootChance = 0.2f;

		lootChanceOther = 1f;

	}
	
	private int breaks=0;
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange(12, 21);
	}

	public void spawnBomb() {
		int newPos2;
		do {
			newPos2 = Random.Int(Dungeon.level.length());
		} while (!Dungeon.level.heroFOV[newPos2] || !Dungeon.level.passable[newPos2]
				|| Dungeon.level.adjacent(newPos2, hero.pos)
				|| Actor.findChar(newPos2) != null);
		SeekBombP bomb1 = new SeekBombP();
		bomb1.pos = newPos2;
		GameScene.add(bomb1);
	}

	@Override
	public float attackDelay() {
		return 0.75f;
	}
	
	@Override
	protected boolean act() {
		if( 7 - breaks > 8 * HP / HT && HP > 0) {
			breaks++;
				
			if (breaks < 4) {
				Buff.affect(this, GlassShield.class).turns(1);
				spawnBomb();
				chainsUsed = false;
			}
			return true;
		}
		if (!chainsUsed && state == HUNTING &&
				paralysed <= 0 &&
				enemy != null &&
				enemy.invisible == 0 &&
				Dungeon.level.heroFOV[enemy.pos] &&
				Dungeon.level.distance( pos, enemy.pos ) < 5 && !Dungeon.level.adjacent( pos, enemy.pos ) &&
				Random.Int(3) == 0 &&
				chain(enemy.pos) && HP > 0) {
			return false;
		} else	if(Random.Int(10)<1 && breaks < 4 ){
				Plant.Seed seed = (Plant.Seed) Generator.random(Generator.Category.SEED);
				Dungeon.level.plant(seed, this.pos);
				return true;
		} else {
			return super.act();
		}
	}
	
	private boolean chain(int target){
		//if (chainsUsed || enemy.properties().contains(Property.IMMOVABLE))
			//return false;
		Ballistica chain = new Ballistica(pos, target, Ballistica.PROJECTILE);

		if (chain.collisionPos != enemy.pos || Dungeon.level.pit[chain.path.get(1)])
			return false;
		else {
			int newPos = -1;
			for (int i : chain.subPath(1, chain.dist)){
				if (!Dungeon.level.solid[i] && Actor.findChar(i) == null){
					newPos = i;
					break;
				}
			}

			if (newPos == -1){
				return false;
			} else {
				final int newPosFinal = newPos;
				yell( Messages.get(this, "scorpion") );
				sprite.parent.add(new Chains(sprite.center(), enemy.sprite.destinationCenter(), new Callback() {
					public void call() {
						Actor.addDelayed(new Pushing(enemy, enemy.pos, newPosFinal), -1);
						enemy.pos = newPosFinal;
						Dungeon.level.occupyCell( enemy );
						Cripple.prolong(enemy, Cripple.class, 4f);
						Vertigo.prolong(enemy, Vertigo.class, 8f);
						if (enemy == hero) {
							hero.interrupt();
							Dungeon.observe();
						}
						next();
					}
				}));
			}
		}
		chainsUsed = true;
		return true;
	}	

	@Override
	public int attackSkill( Char target) {
		return 35;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(7, 12);
	}



	@Override
	public int attackProc(Char enemy, int damage) {
		if (Random.Int(10)<2){
		int oppositeDefender = enemy.pos + (enemy.pos - pos);
		Ballistica trajectory = new Ballistica(enemy.pos, oppositeDefender, Ballistica.MAGIC_BOLT);
		com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave.throwChar(enemy, trajectory, 1, false, true, this);
		} else if ((Random.Int(10)<2)) {
			int newPos;
			do {
				newPos = Random.Int(Dungeon.level.length());
			} while (!Dungeon.level.heroFOV[newPos] || !Dungeon.level.passable[newPos]
					|| Dungeon.level.adjacent(newPos, hero.pos)
					|| Actor.findChar(newPos) != null);

			Buff.affect(hero, STRdown.class,5f);
			hero.sprite.move(pos, newPos);
			hero.move(newPos);

			if (Dungeon.level.heroFOV[newPos]) {
				CellEmitter.get(newPos).burst(Speck.factory(Speck.WOOL), 6);
				Sample.INSTANCE.play(Assets.Sounds.PUFF);
			}

			spend(1f);
		}

		return damage;
	}

	@Override
	public void notice() {
		super.notice();
		yell(Messages.get(this, "notice"));
	
	}
	
	
	@Override
	public void die(Object cause) {

		yell(Messages.get(this,"die"));
		
		GameScene.bossSlain();
		
		Badges.validateBossSlain();
	    
	    Dungeon.level.unseal();


		Dungeon.level.drop(new WornKey(Dungeon.depth), pos).sprite.drop();

		
	    super.die(cause);
	}
	
	private final String CHAINSUSED = "chainsused";
    private static final String BREAKS	= "breaks";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(CHAINSUSED, chainsUsed);
		bundle.put( BREAKS, breaks );
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		chainsUsed = bundle.getBoolean(CHAINSUSED);
		breaks = bundle.getInt( BREAKS );
	}	

	{

		immunities.add(Burning.class);
		resistances.add(ToxicGas.class);
		immunities.add(Poison.class);
		immunities.add(Chill.class);
		immunities.add(Blindness.class);
		immunities.add(Vertigo.class);
		//resistances.add(EnchantmentDark.class);
		

		immunities.add(Icecap.class);
		immunities.add(Firebloom.class);
		immunities.add(Blindweed.class);
		immunities.add(Stormvine.class);
		immunities.add(Sorrowmoss.class);
	}

	public static class SeekBombP extends Mob {

		{
			spriteClass = SeekingBombSprite.class;

			HP = HT = 10;
			defenseSkill = 0;
			baseSpeed = 0.5f;
			EXP = 0;

			state = HUNTING;

			properties.add(Property.MECH);
			properties.add(Property.MINIBOSS);
		}

		@Override
		public int attackProc(Char enemy, int damage) {
			enemy.damage(damageRoll(), SHOCK_DAMAGE,2);
			damage = 0;
			return damage;
		}

		@Override
		public int attackSkill( Char target) {
			return 20;
		}

		@Override
		public int damageRoll() {
			return Random.NormalIntRange(6, 10);
		}

		@Override
		public int drRoll() {
			return 3;
		}

		@Override
		public boolean act() {
			yell(""+HP/2+"!");
			return super.act();
		}

		@Override
		public void move(int step, boolean travelling) {
			super.move(step, travelling);
			//HP+=4;
		}

		@Override
		public void damage( int dmg, Object src ) {
			//if (!(src instanceof SelfDestroy) ) dmg = 0;
			super.damage( dmg, src );
		}

		public void die(Object cause) {
			DungeonBomb xbomb = new DungeonBomb();
			xbomb.explode(pos);
			GameScene.add(Blob.seed(pos, 20, FireEffectDamage.class));
			super.die(cause);
		}

	}

}
