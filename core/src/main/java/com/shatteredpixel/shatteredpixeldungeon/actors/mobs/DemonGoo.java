/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
 *
 * Demon Goo from Pixel Dungeon Reforged, rebuilt on Shattered's damage.
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
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Door;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DemonGooSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class DemonGoo extends Mob {

	private static final float SPLIT_DELAY = 1f;

	{
		spriteClass = DemonGooSprite.class;

		HP = HT = 150;
		defenseSkill = 20;
		baseSpeed = 2f;

		EXP = 10;
		maxLvl = 29;

		loot = Generator.Category.POTION;
		lootChance = 0.5f;

		properties.add(Property.DEMONIC);
		properties.add(Property.ACIDIC);
	}

	//0 for the one that was spawned, more for each split. Only the first drops loot.
	private int generation = 0;

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 15, 30 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 35;
	}

	@Override
	protected boolean act() {
		boolean result = super.act();

		//heals while standing in water
		if (Dungeon.level.water[pos] && HP < HT) {
			sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
			HP++;
		}
		return result;
	}

	@Override
	public int attackProc( Char enemy, int damage ) {
		damage = super.attackProc( enemy, damage );
		if (Random.Int(3) == 0) {
			Buff.affect(enemy, Ooze.class).set(Ooze.DURATION);
			enemy.sprite.burst(0x000000, 5);
		}
		return damage;
	}

	@Override
	public void damage( int dmg, Object src ) {
		super.damage( dmg, src );
		if (!isAlive() || HP < 2) return;

		ArrayList<Integer> candidates = new ArrayList<>();
		int[] neighbours = { pos + 1, pos - 1, pos + Dungeon.level.width(), pos - Dungeon.level.width() };
		for (int n : neighbours) {
			if (Dungeon.level.passable[n] && Actor.findChar(n) == null) {
				candidates.add(n);
			}
		}
		if (candidates.isEmpty()) return;

		DemonGoo clone = new DemonGoo();
		clone.generation = generation + 1;
		clone.lootChance = 0f;
		clone.pos = Random.element(candidates);
		clone.state = clone.HUNTING;
		if (buff(Burning.class) != null) {
			Buff.affect(clone, Burning.class).reignite(clone);
		}
		if (buff(Poison.class) != null) {
			Buff.affect(clone, Poison.class).set(2);
		}

		if (Dungeon.level.map[clone.pos] == Terrain.DOOR) {
			Door.enter(clone.pos);
		}

		GameScene.add(clone, SPLIT_DELAY);
		Actor.add(new Pushing(clone, pos, clone.pos));

		//the two halves share what is left
		int splitHP = HP / 2;
		clone.HP = splitHP;
		HP -= splitHP;
	}

	private static final String GENERATION = "generation";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( GENERATION, generation );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		generation = bundle.getInt( GENERATION );
		if (generation > 0) lootChance = 0f;
	}
}
