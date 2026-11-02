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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfDisintegration;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MagicEyeSprite;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Random;

import static com.shatteredpixel.shatteredpixeldungeon.actors.damagetype.DamageType.LIGHT_DAMAGE;

public class MagicEye extends Mob {

	private static final String TXT_DEATHGAZE_KILLED = "%s's deathgaze killed you...";
	protected static final float SPAWN_DELAY = 2f;

	{
		spriteClass = MagicEyeSprite.class;

		HP = HT = 100+(adj(0)*Random.NormalIntRange(4, 7));
		defenseSkill = 40+adj(1);
		viewDistance = Light.DISTANCE;

		EXP = 16;
	
		flying = true;

		loot = Generator.Category.SEED;
		lootChance = 0.1f;
		
		properties.add(Property.DEMONIC);
		properties.add(Property.MAGICER);
		properties.add(Property.ELEMENT);
	}

	private Ballistica beam;

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 20);
	}

	@Override
	protected boolean canAttack(Char enemy) {

		beam = new Ballistica( pos, enemy.pos, Ballistica.STOP_SOLID);

		return beam.subPath(1, beam.dist).contains(enemy.pos);
	}

	@Override
	public int attackSkill( Char target) {
		return 30+adj(0);
	}

	@Override
	public float attackDelay() {
		return 2f;
	}
	
	@Override
	public int attackProc(Char enemy, int damage) {
		
		enemy.damage(damageRoll()*3/4, LIGHT_DAMAGE,2);
		damage = damage/4;

		return damage;
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
			attack( enemy );
			return true;
		}
	}

	@Override
	public boolean attack( Char enemy, float dmgMulti, float dmgBonus, float accMulti ) {
		if (beam == null) beam = new Ballistica( pos, enemy.pos, Ballistica.STOP_SOLID ); //sps, a direct attack before any canAttack check

		for (int pos : beam.subPath(1, beam.dist)) {

			Char ch = Actor.findChar(pos);
			if (ch == null) {
				continue;
			}

			if (hit(this, ch, true)) {
				ch.damage(Random.NormalIntRange(20, 50),LIGHT_DAMAGE,2);
				damage(Random.NormalIntRange(20, 50),LIGHT_DAMAGE,2 );
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

	public static void spawnAroundChance(int pos) {
		for (int n : PathFinder.NEIGHBOURS4) {
			int cell = pos + n;
			if (Dungeon.level.passable[cell] && Actor.findChar(cell) == null && Random.Float()<0.50f) {
				spawnAt(cell);
			}
		}
	}

	public static MagicEye spawnAt(int pos) {
		if (Dungeon.level.passable[pos] && Actor.findChar(pos) == null) {
          
			MagicEye e = new MagicEye();
			e.pos = pos;
			e.state = e.HUNTING;
			GameScene.add(e, SPAWN_DELAY);

			e.sprite.alpha(0);
			e.sprite.parent.add(new AlphaTweener(e.sprite, 1, 0.5f));

		return e;
  			
		} else {
			return null;
		}
	}

    {
		resistances.add(WandOfDisintegration.class);

		immunities.add(Terror.class);
	}

}
