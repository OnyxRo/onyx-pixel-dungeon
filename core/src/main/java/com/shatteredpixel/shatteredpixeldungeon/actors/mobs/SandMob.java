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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dry;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HiddenShadow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.damagetype.DamageType;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLightning;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SandmobSprite;
import com.watabou.utils.Random;

public class SandMob extends Mob {

	{
		spriteClass = SandmobSprite.class;

		HP = HT = 90+(adj(0)*Random.NormalIntRange(2, 5));
		defenseSkill = 5+adj(0);
		baseSpeed = 0.5f;

		EXP = 10;
		maxLvl = 25;

		//loot = new PotionOfMending(); potential nerf
		lootChance = 0.5f; // by default, see die()
		
		properties.add(Property.ELEMENT);
	}

	@Override
	public Item SupercreateLoot(){
		return Generator.random(Generator.Category.GOLD);
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(17, 25+adj(0));
	}

	@Override
	public int attackSkill( Char target) {
		return 10+adj(0);
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 10);
	}


	@Override
	public int attackProc(Char enemy, int damage) {
		if (!skilluse && enemy == Dungeon.hero) {
			skilluse = true;
			Buff.affect(this,HiddenShadow.class,6f);
		}

		if (Random.Int(5) == 0) {
			Buff.prolong(enemy, Dry.class, 10f);
		}
			
		if (Random.Int(5) == 0) {
			Buff.prolong(enemy, Slow.class, 10f);
		}			

		return damage;
	}

	
	@Override
	public boolean add( Buff buff ) {
		if (buff instanceof Dry ) {
			if (HP < HT) {
				HP+=HT/10;
				sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
			}
		} else if (buff instanceof Vertigo) {
			if (Dungeon.level.water[this.pos])
				damage(Random.NormalIntRange(HT / 2, HT), buff,3);
			else
				damage(Random.NormalIntRange(1, HT * 2 / 3), buff,3);
		} else {
			return super.add(buff);
		}
		return false;
	}

	{
		resistances.add(WandOfLightning.class);
	}
	
	@Override
	public void die(Object cause) {
        MiniSand.spawnAround(this.pos);
		super.die(cause);
	}

    public static class MiniSand extends Mob {

		protected static final float SPAWN_DELAY = 1f;
		{
			spriteClass = SandmobSprite.class;

			HP = HT = 45+(adj(0)*Random.NormalIntRange(2, 5));
			defenseSkill = 25;

			EXP = 0;

			state = WANDERING;
		}

		@Override
		public int damageRoll() {
			return Random.NormalIntRange(15, 20+adj(0));
		}

		@Override
		public int attackSkill( Char target) {
			return 30+adj(0);
		}

		@Override
		public int drRoll() {
			return 0;
		}

		@Override
		public int attackProc(Char enemy, int damage) {
			if (Random.Int(5) == 0) {
				Buff.prolong(enemy, Dry.class, 10f);
			}
			
			if (Random.Int(5) == 0) {
				Buff.prolong(enemy, Slow.class, 10f);
			}			

			return damage;
		}
		
	public static void spawnAround(int pos) {
		for (int n : PathFinder.NEIGHBOURS4) {
			int cell = pos + n;
			if (Dungeon.level.passable[cell] && Actor.findChar(cell) == null) {
				spawnAt(cell);
			}
		}
	}

	public static MiniSand spawnAt(int pos) {
		if (Dungeon.level.passable[pos] && Actor.findChar(pos) == null) {
          
			MiniSand w = new MiniSand();
			w.pos = pos;
			w.state = w.HUNTING;
			GameScene.add(w, SPAWN_DELAY);
			return w;
  			
		} else {
			return null;
		}
	}

		@Override
		public boolean add( Buff buff ) {
			 if (buff instanceof Vertigo) {
				if (Dungeon.level.water[this.pos])
					damage(Random.NormalIntRange(HT / 2, HT), buff,3);
				else
					damage(Random.NormalIntRange(1, HT * 2 / 3), buff,3);
			} else {
				return super.add(buff);
			}
		return false;
	}

	}	

}
