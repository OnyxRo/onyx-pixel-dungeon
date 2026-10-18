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
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SwarmSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Swarm extends Mob {

	{
		spriteClass = SwarmSprite.class;
		
		HP = HT = 50;
		defenseSkill = 5;

		EXP = 3;
		maxLvl = 9;
		
		flying = true;

		loot = PotionOfHealing.class;
		lootChance = 0.1667f; //by default, see lootChance()
	}
	
	private static final float SPLIT_DELAY	= 1f;
	
	int generation	= 0;
	
	private static final String GENERATION	= "generation";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( GENERATION, generation );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		generation = bundle.getInt( GENERATION );
		if (generation > 0) EXP = 0;
	}

	@Override
	public void die(Object cause) {
		flying = false;
		super.die(cause);
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 1, 4 );
	}
	
	@Override
	public int defenseProc( Char enemy, int damage ) {

		tryToSplit( damage );
		return super.defenseProc(enemy, damage);
	}

	//from Transformation PD, so its swarms can change when they split
	protected boolean canSplit( int damage ) {
		//Pixel Dungeon Reforged Randomizer, Depleted Numbers
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(Swarm.class) == 3 && Random.Float() > 0.5f) return false;
		return HP >= damage + 2;
	}

	protected Swarm createSplit() {
		return new Swarm();
	}

	protected void tryToSplit( int damage ) {
		if (canSplit( damage )) {
			ArrayList<Integer> candidates = new ArrayList<>();
			
			int[] neighbours = {pos + 1, pos - 1, pos + Dungeon.level.width(), pos - Dungeon.level.width()};
			for (int n : neighbours) {
				if (!Dungeon.level.solid[n]
						&& Actor.findChar( n ) == null
						&& (Dungeon.level.passable[n] || Dungeon.level.avoid[n])
						&& (!properties().contains(Property.LARGE) || Dungeon.level.openSpace[n])) {
					candidates.add( n );
				}
			}
	
			if (candidates.size() > 0) {
				
				Swarm clone = split();
				clone.pos = Random.element( candidates );
				clone.state = clone.HUNTING;
				GameScene.add( clone, SPLIT_DELAY ); //we add before assigning HP due to ascension

				clone.HP = (HP - damage) / 2;
				Actor.add( new Pushing( clone, pos, clone.pos ) );

				Dungeon.level.occupyCell(clone);
				
				HP -= clone.HP;
			}
		}
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 10;
	}
	
	private Swarm split() {
		Swarm clone = createSplit();
		clone.alignment = alignment;
		clone.generation = generation + 1;
		clone.EXP = 0;
		if (buff( Burning.class ) != null) {
			Buff.affect( clone, Burning.class ).reignite( clone );
		}
		if (buff( Poison.class ) != null) {
			Buff.affect( clone, Poison.class ).set(2);
		}
		for (Buff b : buffs()){
			if (b.revivePersists) {
				Buff.affect(clone, b.getClass());
			}
		}
		return clone;
	}

	@Override
	public float lootChance() {
		lootChance = 1f/(6 * (generation+1) );
		return super.lootChance() * (5f - Dungeon.LimitedDrops.SWARM_HP.count) / 5f;
	}
	
	@Override
	public Item createLoot(){
		Dungeon.LimitedDrops.SWARM_HP.count++;
		return super.createLoot();
	}

	//Pixel Dungeon Reforged Randomizer. Piercing and Slashing Resistance, Fast Swarm (the speed of a crab), Bloodsuckers,
	//Weakened Swarm, Magic Vulnerability, Depleted Numbers (above)
	@Override
	protected void onAdd() {
		boolean first = firstAdded;
		super.onAdd();
		if (first && generation == 0 && com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(Swarm.class) == 1) {
			HP = (int) (HT * Random.Float(0.5f, 1.0f));
		}
	}

	@Override
	public float speed() {
		return super.speed() * (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureBuff(Swarm.class) == 2 ? 2f : 1f);
	}

	@Override
	public void damage( int dmg, Object src ) {
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureBuff(Swarm.class) == 1 && (com.shatteredpixel.shatteredpixeldungeon.Randomizer.isSlashing(src) || com.shatteredpixel.shatteredpixeldungeon.Randomizer.isPiercing(src))) {
			dmg = (int) Math.ceil(dmg / 2f);
		} else if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(Swarm.class) == 2 && com.shatteredpixel.shatteredpixeldungeon.Randomizer.isEnergy(src)) {
			dmg *= 3;
		}
		super.damage( dmg, src );
	}

	@Override
	public int attackProc( Char enemy, int damage ) {
		damage = super.attackProc( enemy, damage );
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureBuff(Swarm.class) == 3 && damage > 0 && Random.Int(2) == 0) {
			com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.affect(enemy, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding.class).set(3);
		}
		return damage;
	}
}
