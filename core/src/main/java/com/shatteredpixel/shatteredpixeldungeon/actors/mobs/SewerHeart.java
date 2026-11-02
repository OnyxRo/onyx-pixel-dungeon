/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Badges.Badge;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.GrowSeed;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.RatKing;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.WornKey;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Rotberry;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SewerHeartSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SewerLasherSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

public class SewerHeart extends Mob {

	{
		spriteClass = SewerHeartSprite.class;

		HP = HT = 500;
		defenseSkill = 0;

		EXP = 30;

		loot = new Rotberry.Seed();
		lootChance = 0.2f;
		
		lootOther = new com.shatteredpixel.shatteredpixeldungeon.items.food.Berry();
		lootChanceOther = 1f;
		
		properties.add(Property.PLANT);
		properties.add(Property.BOSS);
	}

	private Ballistica beam;
	private int beamTarget = -1;
	private int breaks=0;
	private int beamCooldown=0;
	public boolean beamCharged;

	@Override
	public void notice() {
		super.notice();
		//yell("GLURP-GLURP!");
		Dungeon.level.seal();
		if (!spawnedLasher){
			Buff.affect(hero, LasherSpawner.class);
            spawnedLasher = true;
		}
		state = PASSIVE;
	}


	@Override
	public boolean act() {

		if( 5 - breaks > 6 * HP / HT ) {
			breaks++;
			return true;
		}

		if (breaks ==3 && state == PASSIVE){
			state = HUNTING;
		}

		if (beamCharged && state != HUNTING){
			beamCharged = false;
		}
		if (beam == null && beamTarget != -1) {
			beam = new Ballistica(pos, beamTarget, Ballistica.STOP_SOLID);
			sprite.turnTo(pos, beamTarget);
		}
		if (beamCooldown > 0)
			beamCooldown--;
		return super.act();
	}

	@Override
	public void damage( int dmg, Object src ) {
		if( (5 - breaks) > 6 * HP / HT ) {
			int newPos;
			int _tries1 = 0; //sps, gives up away from its own arena
			do {
				newPos = Random.Int(Dungeon.level.length());
			} while ((!Dungeon.level.heroFOV[newPos] || !Dungeon.level.passable[newPos]
					|| Dungeon.level.adjacent(newPos, enemy.pos)
					|| Actor.findChar(newPos) != null) && ++_tries1 < 500);
			if (_tries1 >= 500) return;

			sprite.move(pos, newPos);
			move(newPos);

			if (Dungeon.level.heroFOV[newPos]) {
				CellEmitter.get(pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
				Sample.INSTANCE.play(Assets.Sounds.PUFF);
			}

			GLog.n(Messages.get(this, "blink"));

			if (Dungeon.level.mobs.size()< hero.lvl*2){
				SewerLasher.spawnAroundChance(newPos);
				}
			}
			
		super.damage( dmg, src );
		
	}

	@Override
	public int defenseProc(Char enemy, int damage) {
		GameScene.add(Blob.seed(pos, 20, ToxicGas.class));

		return super.defenseProc(enemy, damage);
	}

	@Override
	protected boolean getCloser(int target) {
		return false;
	}

	@Override
	public void destroy() {
		super.destroy();
		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[Dungeon.level.mobs.size()])){
			if (mob instanceof SewerLasher){
				mob.die(null);
			}
		}
	}

	@Override
	protected boolean canAttack(Char enemy) {
		if (beamCooldown == 0) {
			Ballistica aim = new Ballistica(pos, enemy.pos, Ballistica.STOP_SOLID);

			if (enemy.invisible == 0 && !isCharmedBy(enemy) && Dungeon.level.heroFOV[enemy.pos] && aim.subPath(1, aim.dist).contains(enemy.pos)){
				beam = aim;
				beamTarget = aim.collisionPos;
				return true;
			} else
				//if the beam is charged, it has to attack, will aim at previous location of target.
				return beamCharged;
		} else
			return super.canAttack(enemy);
	}

	@Override
	protected boolean doAttack(Char enemy) {
		if (beamCooldown > 0) {
			return super.doAttack(enemy);
		} else if (!beamCharged){
			((SewerHeartSprite)sprite).charge( enemy.pos );
			spend( attackDelay()*2f );
			beamCharged = true;
			return true;
		} else {

			spend( attackDelay() );

			beam = new Ballistica(pos, beamTarget, Ballistica.STOP_SOLID);
			if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[beam.collisionPos] ) {
				sprite.zap( beam.collisionPos );
				return false;
			} else {
				deathGaze();
				return true;
			}
	}}

	public void deathGaze(){
		if (!beamCharged || beamCooldown > 0 || beam == null)
			return;

		beamCharged = false;
		beamCooldown = Random.IntRange(3, 6);

		boolean terrainAffected = false;

		for (int pos : beam.subPath(1, beam.dist)) {

			if (Dungeon.level.flamable[pos]) {

				Dungeon.level.destroy( pos );
				GameScene.updateMap( pos );
				terrainAffected = true;

			}

			Char ch = Actor.findChar( pos );
			if (ch == null) {
				continue;
			}

			if (hit( this, ch, true )) {
				ch.damage( Random.NormalIntRange( 20, 35 ), this,2 );

				if (Dungeon.level.heroFOV[pos]) {
					ch.sprite.flash();
					CellEmitter.center( pos ).burst( PurpleParticle.BURST, Random.IntRange( 1, 2 ) );
				}

			//	if (!ch.isAlive() && ch == Dungeon.hero) {
				//	Dungeon.fail(Messages.format(ResultDescriptions.LOSE));
			//	}
			} else {
				ch.sprite.showStatus( CharSprite.NEUTRAL,  ch.defenseVerb() );
			}
		}

		if (terrainAffected) {
			Dungeon.observe();
		}

		beam = null;
		beamTarget = -1;
	}
	

	@Override
	public void die(Object cause) {
		super.die(cause);
		Dungeon.level.unseal();

		GameScene.bossSlain();
		Dungeon.level.drop(new WornKey(Dungeon.depth), pos).sprite.drop();
		Badges.validateBossSlain();

		Buff.detach(hero, LasherSpawner.class);
		
		Dungeon.level.drop(new Gold(1500), pos).sprite.drop();

		ArrayList<Mob> mobs = new ArrayList<>();

		Mob mob = new RatKing();
		mob.state = mob.WANDERING;
		mob.pos = pos;
		GameScene.add( mob, 1f );
		mobs.add( mob );
		ScrollOfTeleportation.appear(mob, mob.pos);
		mob.yell(Messages.get(this,"heart"));
		//important to process the visuals and pressing of cells last, so spawned mobs have a chance to occupy cells first

	}

	@Override
	public int damageRoll() {
		int min = (HP*2 <= HT) ? 5 : 2;
		int max = (HP*2 <= HT) ? 16 : 8;
		return Random.NormalIntRange( min, max );

	}

	@Override
	public int attackSkill( Char target ) {
		return 30;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 2);
	}
	
	protected boolean spawnedLasher = false;
	

	private static final String BEAM_TARGET     = "beamTarget";
	private static final String BEAM_COOLDOWN   = "beamCooldown";
	private static final String BEAM_CHARGED    = "beamCharged";    
	private static final String BREAKS	= "breaks";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put( BEAM_TARGET, beamTarget);
		bundle.put( BEAM_COOLDOWN, beamCooldown );
		bundle.put( BEAM_CHARGED, beamCharged ); 
		bundle.put( BREAKS, breaks );
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(BEAM_TARGET))
			beamTarget = bundle.getInt(BEAM_TARGET);
		beamCooldown = bundle.getInt(BEAM_COOLDOWN);
		beamCharged = bundle.getBoolean(BEAM_CHARGED);
		breaks = bundle.getInt( BREAKS );
	}

	{
		immunities.add( ToxicGas.class );
	}

	public static class LasherSpawner extends Buff {

		int spawnLasher = 0;

		@Override
		public boolean act() {
			spawnLasher++;
			int lasher = 1; //we include the wraith we're trying to spawn
			for (Mob mob : Dungeon.level.mobs){
				if (mob instanceof SewerLasher){
					lasher++;
				}
			}

			int powerNeeded = Math.min(25, lasher);

			if (powerNeeded <= spawnLasher){
				spawnLasher -= powerNeeded;
				int pos = 0;
				int _tries2 = 0; //sps, gives up away from its own arena
				do{
					pos = Random.Int(Dungeon.level.randomRespawnCell(null));
				} while ((!Dungeon.level.passable[pos] || Actor.findChar( pos ) != null) && ++_tries2 < 500);
				if (_tries2 >= 500) return false;
				SewerLasher.spawnAt(pos);
				Sample.INSTANCE.play(Assets.Sounds.BURNING);
			}

			spend(TICK);
			return true;
		}

		public void dispel(){
			detach();
			for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
				if (mob instanceof SewerLasher){
					mob.die(null);
				}
			}
		}

		private static String SPAWNLASHER = "spawnlasher";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(SPAWNLASHER, spawnLasher);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			spawnLasher = bundle.getInt(SPAWNLASHER);
		}
	}
	public static class SewerLasher extends Mob {

		protected static final float SPAWN_DELAY = 2f;

		{
			spriteClass = SewerLasherSprite.class;

			HP = HT = 60;
			defenseSkill = 0;

			EXP = 1;

			loot = Generator.Category.SEED;
			lootChance = 0.2f;

			state = HUNTING;

			properties.add(Property.PLANT);
			properties.add(Property.MINIBOSS);
			//properties.add(Property.IMMOVABLE);
		}

		@Override
		protected boolean act() {
			if (enemy == null || !Dungeon.level.adjacent(pos, enemy.pos)) {
				HP = Math.min(HT, HP + 3);
			}
			return super.act();
		}

		@Override
		public void damage( int dmg, Object src ) {
			if (src instanceof Burning) {
				destroy();
				sprite.die();
			} else {

				super.damage( dmg, src );
			}
		}

		@Override
		public int attackProc( Char enemy, int damage) {
			damage = super.attackProc(enemy, damage);
			if (Random.Int(5) < 1) {
				Buff.affect(enemy, Cripple.class, 2f);
			} else
			if (Random.Int(4) < 1) {
				Buff.affect(enemy, GrowSeed.class).set(4f);
			} else
			if (Random.Int(3) < 1) {
				Buff.affect(enemy, Bleeding.class).set(damage);}

			return super.attackProc(enemy, damage);

		}
		@Override
		protected boolean getCloser(int target) {
			return true;
		}

		@Override
		protected boolean getFurther(int target) {
			return true;
		}

		@Override
		public int damageRoll() {
			return Random.NormalIntRange(4, 12);
		}

		@Override
		public int attackSkill( Char target ) {
			return 15;
		}

		@Override
		public int drRoll() {
			return Random.NormalIntRange(2, 8);
		}

		{
			immunities.add( ToxicGas.class );
		}

		public static void spawnAround(int pos) {
			for (int n : PathFinder.NEIGHBOURS8) {
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

		public static SewerLasher spawnAt(int pos) {

			SewerLasher b = new SewerLasher();

			b.pos = pos;
			b.state = b.HUNTING;
			GameScene.add(b, SPAWN_DELAY);

			return b;

		}


	}

}
