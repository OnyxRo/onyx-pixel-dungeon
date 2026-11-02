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

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorruptGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GoldOrcSprite;
import com.watabou.utils.Random;

public class GoldOrc extends Mob {

	{
		spriteClass = GoldOrcSprite.class;
		state = SLEEPING;

		HP = HT = 500;
		defenseSkill = 35;

		EXP = 25;

		properties.add(Property.ORC);
		properties.add(Property.DEMONIC);
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(55, 115);
	}

	@Override
	public int attackSkill( Char target) {
		return 55;
	}

	@Override
	public float attackDelay() {
		return 1.5f;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(16, 32);
	}

	@Override
	public void damage( int dmg, Object src ) {
		if (dmg > HT/8){
			GameScene.add(Blob.seed(pos, 30, CorruptGas.class));
			}
		super.damage( dmg, src );
	}		

	{
		immunities.add(Amok.class);
		immunities.add(Terror.class);
		immunities.add(CorruptGas.class);
		immunities.add(Vertigo.class);

	}

}
