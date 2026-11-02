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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.OrbOfZotSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class OrbOfZotMob extends Mob {

	private static final String TXT_DEATHGAZE_KILLED = "%s's deathray killed you...";

	{
		spriteClass = OrbOfZotSprite.class;
		state = HUNTING;
		HP = HT= 500;
		defenseSkill = 35;	
		
		properties.add(Property.MECH);
	}

	private Ballistica beam;
	
	private static final float SPAWN_DELAY = 1f;
	
    
	@Override
	public int drRoll() {
		return 0;
	}

	
	@Override
	protected boolean act() {
		
		for (int n : PathFinder.NEIGHBOURS8) {
			int c = pos + n;
			if (c< Dungeon.level.length() && c>0){
			    Char ch = Actor.findChar(c);
			}
		}
		//Dungeon.level.heroFOV[Dungeon.hero.pos] &&
		
		boolean result = super.act();
		return result;
	}
	
	@Override
	public void move( int step, boolean travelling ) {		
	}
		
	@Override
	protected Char chooseEnemy() {

		if (enemy == null || !enemy.isAlive()) {
			HashSet<Mob> enemies = new HashSet<Mob>();
			for (Mob mob : Dungeon.level.mobs) {
				if ((mob.alignment == Char.Alignment.ENEMY) && Dungeon.level.heroFOV[mob.pos]) {
					enemies.add(mob);
				}
			}

			enemy = enemies.size() > 0 ? Random.element(enemies) : null;
		}

		return enemy;
	}

	
	
    public static OrbOfZotMob spawnAt(int pos) {
		
    	OrbOfZotMob b = new OrbOfZotMob();  
    	
			b.pos = pos;
			b.state = b.HUNTING;
			GameScene.add(b, SPAWN_DELAY);

			return b;
     
     }
	
	private int hitCell;

	@Override
	protected boolean canAttack(Char enemy) {

		beam = new Ballistica( pos, enemy.pos, Ballistica.STOP_SOLID);

		return beam.subPath(1, beam.dist).contains(enemy.pos);
	}

	@Override
	public int attackSkill( Char target) {
		return 70+(Dungeon.depth);
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
			sprite.attack( beam.collisionPos );
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
				ch.damage(Random.NormalIntRange(100, 200), this,2);
				damage(Random.NormalIntRange(10, 20), this,3);


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

	@Override
	public void beckon(int cell) {
	}
	
	@Override
	public void die(Object cause) {

		yell(Messages.get(this, "die"));
		super.die(cause);
	}	
	
	{
		immunities.add(Terror.class);
		immunities.add(ToxicGas.class);
	}

}
