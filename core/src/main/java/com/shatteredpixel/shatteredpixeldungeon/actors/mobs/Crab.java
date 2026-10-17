/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrabSprite;
import com.watabou.utils.Random;

public class Crab extends Mob {

	{
		spriteClass = CrabSprite.class;
		
		HP = HT = 15;
		defenseSkill = 5;
		baseSpeed = 2f;
		
		EXP = 4;
		maxLvl = 9;
		
		loot = MysteryMeat.class;
		lootChance = 0.167f;
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 1, 7 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		//Pixel Dungeon Reforged Randomizer, Clumsy Claws
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(Crab.class) == 2) return 8;
		return 12;
	}
	
	@Override
	public int drRoll() {
		//Pixel Dungeon Reforged Randomizer, Molting Season
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(Crab.class) == 3) return 0;
		return super.drRoll() + Random.NormalIntRange(0, 4);
	}

	//Pixel Dungeon Reforged Randomizer. Hermit Invasion (in the mob spawner), Flurry Claws, Lightning Legs,
	//Hermit Treasures, Clumsy Claws (above), Molting Season (above)
	private boolean movedLastTurn = false;

	@Override
	public float speed() {
		return super.speed() * (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureBuff(Crab.class) == 3 ? 1.75f : 1f);
	}

	@Override
	public float attackDelay() {
		if (!movedLastTurn && com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureBuff(Crab.class) == 2) {
			return super.attackDelay() * 0.5f;
		}
		return super.attackDelay();
	}

	@Override
	protected boolean act() {
		movedLastTurn = false;
		return super.act();
	}

	@Override
	public void move( int step, boolean travelling ) {
		int oldPos = pos;
		super.move( step, travelling );
		movedLastTurn = oldPos != pos;
	}

	@Override
	public void rollToDropLoot() {
		super.rollToDropLoot();
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(Crab.class) == 1 && Dungeon.hero.lvl <= maxLvl + 2 && Random.Float() > 0.5f) {
			Dungeon.level.drop(com.shatteredpixel.shatteredpixeldungeon.items.Generator.randomArmor(), pos).sprite.drop();
		}
	}
}
