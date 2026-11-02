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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostIce;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Tar;
import com.shatteredpixel.shatteredpixeldungeon.actors.damagetype.DamageType;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FishProtectorSprite;
import com.watabou.utils.Random;

public class FishProtector extends Mob {

	private static final float TIME_TO_ZAP = 2f;

	private static final String TXT_LIGHTNING_KILLED = "%s's lightning bolt killed you...";

	{
		spriteClass = FishProtectorSprite.class;

		EXP = 1;
		state = HUNTING;
		//flying = true;
		
		HP = HT = 300;
		defenseSkill = 25;
		
		lootChance = 1f;

		properties.add(Property.ELEMENT);
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(8+Math.round(com.shatteredpixel.shatteredpixeldungeon.SpsStats.albinoPiranhasKilled/10), 10+Math.round(com.shatteredpixel.shatteredpixeldungeon.SpsStats.albinoPiranhasKilled/5));
	}

	@Override
	public int attackSkill( Char target) {
		return 25;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(5, 15);
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		enemy.damage(damageRoll(), com.shatteredpixel.shatteredpixeldungeon.actors.damagetype.DamageType.ICE_DAMAGE,2);
		damage = 0;
		return damage;
	}

	@Override
	public void die(Object cause) {
		super.die(cause);
		Level.set(pos, Terrain.WATER);
		GameScene.updateMap(pos);
	}


	@Override
	public boolean add( Buff buff ) {
		if (buff instanceof FrostIce) {
			if (HP < HT && this.isAlive()) {
				HP+=HT/10;
				sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
			}
		} else if (buff instanceof Tar) {
			if (Dungeon.level.water[this.pos])
				damage(Random.NormalIntRange(1, HT * 2 / 3), buff,3);
			else
				damage(Random.NormalIntRange(HT / 2, HT), buff,3);
		} else {
			return super.add(buff);
		}
		return false;
	}

	{
	}

}
