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
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silent;
import com.shatteredpixel.shatteredpixeldungeon.actors.damagetype.DamageType;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ForestProtectorSprite;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import static com.shatteredpixel.shatteredpixeldungeon.actors.damagetype.DamageType.EARTH_DAMAGE;

public class ForestProtector extends Mob implements Callback {

	private static final float TIME_TO_ZAP = 1f;

	private static final String TXT_LIGHTNING_KILLED = "%s's lightning bolt killed you...";

	{
		spriteClass = ForestProtectorSprite.class;
		

		EXP = 1;
		state = HUNTING;
		//flying = true;
		
		HP = HT = 250;
		defenseSkill = 10;
		
		lootChance = 1f;

		properties.add(Property.PLANT);
	}



	@Override
	public int damageRoll() {
		return Random.NormalIntRange(5+Math.round(com.shatteredpixel.shatteredpixeldungeon.SpsStats.archersKilled/10), 10+Math.round(com.shatteredpixel.shatteredpixeldungeon.SpsStats.archersKilled/5)) ;
	}

	@Override
	public int attackSkill( Char target) {
		return 26;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(5, 10);
	}

	@Override
	public int attackProc(Char enemy, int damage) {

		enemy.damage(damageRoll()/2, EARTH_DAMAGE,2);
		damage = damage*3;
		return damage;
	}

	@Override
	protected boolean canAttack(Char enemy) {		if (buff(Silent.class) != null){
			return Dungeon.level.adjacent(pos, enemy.pos) && (!isCharmedBy(enemy));
		} else
		return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
	}

	@Override
	protected boolean doAttack(Char enemy) {

		if (Dungeon.level.adjacent(pos, enemy.pos)) {
			return super.doAttack(enemy);

		} else {
			boolean visible = Dungeon.level.heroFOV[pos]
					|| Dungeon.level.heroFOV[enemy.pos];
			if (visible) {
				sprite.zap(enemy.pos);
			} else {
				zap();
			}

			return !visible;
		}
	}

	private void zap() {
		spend(TIME_TO_ZAP);
		if (hit(this, enemy, true)) {
			int dmg = Random.Int(5+Math.round(com.shatteredpixel.shatteredpixeldungeon.SpsStats.archersKilled/10), 10+Math.round(com.shatteredpixel.shatteredpixeldungeon.SpsStats.archersKilled/5));
			enemy.damage(dmg, EARTH_DAMAGE,2);
			//if (!enemy.isAlive() && enemy == Dungeon.hero) {
			//	Dungeon.fail(Messages.format(ResultDescriptions.LOSE));
				//GLog.n(Messages.get(this, "kill"));
			//}
		} else {
			enemy.sprite.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
		}
	}

	public void onZapComplete() {
		zap();
		next();
	}

	@Override
	public void call() {
		next();
	}

    {
	}


}
