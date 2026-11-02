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
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ConfusionGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.DarkGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.TarGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Tar;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sungrass;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GreatMossSprite;
import com.watabou.utils.Random;

public class Greatmoss extends Mob {

	{
		spriteClass = GreatMossSprite.class;

		HP = HT = 120+(adj(0)*Random.NormalIntRange(5, 7));
		defenseSkill = adj(0);

		EXP = 11;
		maxLvl = 30;

		baseSpeed = 0.5f;
		lootChance = 0.3f;

		state = PASSIVE;
		properties.add(Property.PLANT);
	}

	@Override
	public Item SupercreateLoot(){
		return new Sungrass.Seed();
	}

	@Override
	public boolean act() {
        //GameScene.add(Blob.seed(pos, 30, TarGas.class));
		return super.act();
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange(20+adj(0), 60+adj(1));
	}

	@Override
	public int attackSkill( Char target) {
		return 28+adj(1);
	}

	@Override
	public float attackDelay() {
		return 2f;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(20, 25);
	}

	@Override
	public void damage( int dmg, Object src ) {

		if (state == PASSIVE) {
			state = HUNTING;
		}

		super.damage( dmg, src );
	}


	@Override
	public int defenseProc(Char enemy, int damage) {

        switch (Random.Int (4)) {
				case 0:
					GameScene.add(Blob.seed(pos, 25, ToxicGas.class));
					break;
				case 1:
					GameScene.add(Blob.seed(pos, 25, ConfusionGas.class));
					break;
				case 2:
					GameScene.add(Blob.seed(pos, 25, ParalyticGas.class));
					break;
				case 3:
					GameScene.add(Blob.seed(pos, 25, DarkGas.class));
					break;
				default:
					break;
		}

		return super.defenseProc(enemy, damage);
	}

	@Override
	public void beckon(int cell) {
		// Do nothing
	}


	@Override
	public void die(Object cause) {
	
		super.die(cause);
	}


	{
		immunities.add(Amok.class);
		immunities.add(Terror.class);
		immunities.add(Sleep.class);
		immunities.add(TarGas.class);
		immunities.add(Tar.class);

	}


}
