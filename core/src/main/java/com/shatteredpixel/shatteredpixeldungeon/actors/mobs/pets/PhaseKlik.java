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
package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.AirElemental;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ChaosMage;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Eye;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Warlock;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PhaseKlikSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.HashSet;
import java.util.Iterator;

//port-oldstats
public class PhaseKlik extends PET implements Callback{
	
	{
		spriteClass = PhaseKlikSprite.class;
		flying=true;
		state = HUNTING;
		level = 1;
		type = 5;
		cooldown=1000;
        immunities.add( Freezing.class );
        immunities.add( Frost.class );
        immunities.add( Chill.class );
        immunities.add( Electricity.class );
        immunities.add( Eye.class );
        immunities.add( AirElemental.class );
    }

	private static final float TIME_TO_ZAP = 2f;
	private static final String TXT_LIGHTNING_KILLED = "%s's lightning bolt killed you...";

	@Override
	public float attackDelay() {
		return 0.8f;
	}
	
	
	@Override
    public int drRoll() {
        return level*3;
    }
	
	//Frames 1-4 are idle, 5-8 are moving, 9-12 are attack and the last are for death 

	//flame on!
	//spits fire
	//feed meat
			
	protected int regen = 1;	
	protected float regenChance = 0.2f;	
		

	@Override
	public void adjustStats(int level) {
		this.level = level;
		HT = (level) * 10;
		defenseSkill = 2 + level*level;
		cooldown = super.calccooldown(1000, this.level);
	}
	

	@Override
	public int attackSkill(Char target) {
		return defenseSkill;
	}

	@Override
	public int damageRoll() {
        return Random.NormalIntRange(HP / 5, HP / 2);
	}

	@Override
	protected boolean act() {
		
		if (cooldown>0){
			cooldown--;
			if (cooldown==0) {GLog.w("The air crackles around your phase shifting klik!");}
		}
		
		if (Random.Float()<regenChance && HP<HT){HP+=regen; }

		return super.act();
	}
	
	
	@Override
	protected boolean canAttack(Char enemy) {
		if (cooldown>0){
		  return Dungeon.level.adjacent(pos, enemy.pos);
		} else {
		  return new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE ).collisionPos == enemy.pos;
		}
	}


	
	@Override
	protected boolean doAttack(Char enemy) {

        telefrag();

		if (Dungeon.level.adjacent(pos, enemy.pos)) {

			return super.doAttack(enemy);

		} else {

			boolean visible = Dungeon.level.heroFOV[pos]
					|| Dungeon.level.heroFOV[enemy.pos];
			if (visible) {
				sprite.zap(enemy.pos);
			}

			spend(TIME_TO_ZAP);
			cooldown = super.calccooldown(1000, this.level);
			yell("KLIK!");

			if (hit(this, enemy, true)) {
				int dmg = damageRoll()*2;
				if (Dungeon.level.water[enemy.pos] && !enemy.flying) {
					dmg *= 1.5f;
				}
				enemy.damage(dmg, new Electricity());

				enemy.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
				enemy.sprite.flash();

				if (enemy == Dungeon.hero) {

					Camera.main.shake(2, 0.3f);

					if (!enemy.isAlive()) {
						Dungeon.fail( this );
						GLog.n(TXT_LIGHTNING_KILLED, name());
					}
				}
			} else {
				enemy.sprite
						.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
			}

			return !visible;
		}
	}


	
	@Override
	public void call() {
		next();
	}
		
	

	@Override
	public void interact() {

		if (this.buff(MagicalSleep.class) != null) {
			Buff.detach(this, MagicalSleep.class);
		}
		
		if (state == SLEEPING) {
			state = HUNTING;
		}
		if (buff(Paralysis.class) != null) {
			Buff.detach(this, Paralysis.class);
			GLog.i("You shake your %s out of paralysis.", name());
		}
		
		int curPos = pos;

		moveSprite(pos, Dungeon.hero.pos);
		move(Dungeon.hero.pos);

		Dungeon.hero.sprite.move(Dungeon.hero.pos, curPos);
		Dungeon.hero.move(curPos);

		Dungeon.hero.spend(1 / Dungeon.hero.speed());
		Dungeon.hero.busy();
	}

	public void telefrag() {
        // 20% chance to telefragg on attack if more than one enemy is in sight.
        if (Random.Int(5)==0) {
            HashSet<Mob> enemies = new HashSet<Mob>();
            for (Mob mob : Dungeon.level.mobs) {
                if (mob.alignment == Char.Alignment.ENEMY && Dungeon.level.heroFOV[mob.pos]) {
                    enemies.add(mob);
                }
            }
            Iterator it = enemies.iterator();
            Mob enemy1 = enemies.size() > 2 ? (Mob) it.next() : null;
            Mob enemy2 = enemies.size() > 2 ? (Mob) it.next() : null;
            if (enemy1 != null && enemy2 != null) {
                enemy1.pos = enemy2.pos;
                enemy1.sprite.place(enemy2.pos);
                Splash.at(enemy2.sprite.center(), 0xFFFF00FF, 8);
                Sample.INSTANCE.play(Assets.Sounds.RAY);
                enemy2.die(this);
                GLog.p(enemy2.name() + " has been telefragged.");
            }
        }
    }

}