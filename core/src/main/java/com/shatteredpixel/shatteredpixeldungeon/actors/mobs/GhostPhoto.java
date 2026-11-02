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
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.damageblobs.IceEffectDamage;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Wet;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.LivePhotoSprite;
import com.watabou.utils.Random;

public class GhostPhoto extends Mob {

	public Item item;

	{
		spriteClass = LivePhotoSprite.class;

		HP = HT = 100+(adj(0)*Random.NormalIntRange(3, 5));
		defenseSkill = 5+adj(0);

		flying = true;

		EXP = 3;
        maxLvl = 20;
		
		lootChance = 0.1f;
		
		properties.add(Property.UNKNOW);
	}

	@Override
	public Item SupercreateLoot(){
		return Generator.random(Generator.Category.SEED);
	}

	@Override
	public int damageRoll() {
		return 0;
	}

	@Override
	public float attackDelay() {
		return 0.5f;
	}

	@Override
	public void die(Object cause) {
		super.die(cause);
		for (int i : PathFinder.NEIGHBOURS9) {
			if (Dungeon.level.insideMap(pos+i) && !Dungeon.level.solid[pos+i]) {
				GameScene.add(Blob.seed(pos + i, 8, IceEffectDamage.class));
			}
		}
	}

	@Override
	public Item createLoot() {
			return super.createLoot();
	}

	@Override
	public int attackSkill( Char target) {
		return 25;
	}

	@Override
	public int drRoll() {
		return 0;
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		Buff.affect(enemy, Wet.class,10f);
		return damage;
	}

}
