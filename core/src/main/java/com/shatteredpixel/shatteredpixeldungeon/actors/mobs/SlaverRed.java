/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SlaverRedSprite;
import com.watabou.utils.Random;

public class SlaverRed extends Mob {

	{
		spriteClass = SlaverRedSprite.class;
		HP = HT = 45;
		defenseSkill = 8;
		EXP = 2;
		maxLvl = 30;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 4, 10 );
	}

	@Override
	public int defenseProc( Char enemy, int damage ) {

		for (Mob mob : Dungeon.level.mobs) {
			if (mob instanceof SlaverBoss || mob instanceof SlaverBlue) {
				mob.aggro( enemy );
			}
		}
		return super.defenseProc(enemy, damage);

	}

	@Override
	public int attackProc( Char enemy, int damage ) {
		damage = super.attackProc( enemy, damage );
		if (Random.Int( 2 ) == 0) {
			Buff.affect( enemy, Vulnerable.class, 5 );
		}else{
			damage = Math.round(damage * 1.5f);
		}

		return damage;
	}

	@Override
	public int attackSkill( Char target ) {
		return 17;
	}
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 4);
	}

}
