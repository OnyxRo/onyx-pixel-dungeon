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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silent;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.LightningTrap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FlyingProtectorSprite;
import com.watabou.noosa.Camera;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class FlyingProtector extends Mob implements Callback {

	private static final float TIME_TO_ZAP = 2f;
	
	private static final float SPAWN_DELAY = 0.2f;
	

	private static final String TXT_LIGHTNING_KILLED = "%s's lightning bolt killed you...";

	{
		spriteClass = FlyingProtectorSprite.class;

		EXP = 5;
		state = HUNTING;
		flying = true;
		
		HP = HT = 50 + Dungeon.depth * 4;
		defenseSkill = 4 + Dungeon.depth * 1;

		properties.add(Property.ELEMENT);
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(20, 30);
	}

	@Override
	public int attackSkill( Char target) {
		return (9 + Dungeon.depth);
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, Dungeon.depth);
	}

	@Override
	protected boolean canAttack(Char enemy) {		if (buff(Silent.class) != null){
			return Dungeon.level.adjacent(pos, enemy.pos) && (!isCharmedBy(enemy));
		} else
		return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
	}

	@Override
	protected boolean doAttack(Char enemy) {

		if (Dungeon.level.distance(pos, enemy.pos) <= 1) {

			return super.doAttack(enemy);

		} else {

			boolean visible = Dungeon.level.heroFOV[pos]
					|| Dungeon.level.heroFOV[enemy.pos];
			if (visible) {
				sprite.zap(enemy.pos);
			}

			spend(TIME_TO_ZAP);

			if (hit(this, enemy, true)) {
				int dmg = Random.Int(25, 40);
				if (Dungeon.level.water[enemy.pos] && !enemy.flying) {
					dmg *= 1.5f;
				}
				enemy.damage(dmg, this,2);

				enemy.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
				enemy.sprite.flash();

				if (enemy == Dungeon.hero) {

					Camera.main.shake(2, 0.3f);

					//if (!enemy.isAlive()) {
					//	Dungeon.fail(Messages.format(ResultDescriptions.LOSE));
						//GLog.n(Messages.get(this, "kill"));
					//}
				}
			} else {
				enemy.sprite
						.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
			}

			return !visible;
		}
	}
	
	 public static FlyingProtector spawnAt(int pos) {
			
		 FlyingProtector b = new FlyingProtector();  
	    	
				b.pos = pos;
				b.state = b.HUNTING;
				GameScene.add(b, SPAWN_DELAY);
				return b;	     
	     }

	@Override
	public void call() {
		next();
	}

	{
		resistances.add(LightningTrap.Electricity.class);
	}

}
