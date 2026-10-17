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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostIce;
import com.shatteredpixel.shatteredpixeldungeon.actors.damagetype.DamageType;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.StoneOre;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.plants.Icecap;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.IceBugSprite;
import com.watabou.utils.Random;

public class IceBug extends Mob {

	{
		spriteClass = IceBugSprite.class;

		HP = HT = 80+(adj(0)*Random.NormalIntRange(2, 5));
		defenseSkill = 15+adj(0);
		baseSpeed = 1.5f;

		EXP = 9;
		maxLvl = 25;
		
		loot = new StoneOre();
		lootChance = 0.3f;		
		
		properties.add(Property.BEAST);
	}

	@Override
	public Item SupercreateLoot(){
		return new Icecap.Seed();
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(15, 20+adj(0));
	}
	
	@Override
	public int attackProc(Char enemy, int damage) {
		if (Random.Int(5) == 0) {
			Buff.affect(enemy, FrostIce.class).level(5);
		}

		return damage;
	}	

	@Override
	public int attackSkill( Char target) {
		return 16+adj(0);
	}

	@Override
	public int drRoll() {
	    return Random.NormalIntRange(2, 5);
	}

	public static void spawnAround(int pos) {
		for (int n : PathFinder.NEIGHBOURS4) {
			int cell = pos + n;
			if (Dungeon.level.passable[cell] && Actor.findChar(cell) == null) {
				spawnAt(cell);
			}
		}
	}

	public static IceBug spawnAt(int pos) {
		if (Dungeon.level.passable[pos] && Actor.findChar(pos) == null) {

			IceBug w = new IceBug();
			w.pos = pos;
			w.state = w.HUNTING;
			GameScene.add(w, 1f);
			return w;

		} else {
			return null;
		}
	}

	{
		resistances.add(com.shatteredpixel.shatteredpixeldungeon.actors.damagetype.DamageType.IceDamage.class);

		immunities.add(FrostIce.class);
		
	}


			
}