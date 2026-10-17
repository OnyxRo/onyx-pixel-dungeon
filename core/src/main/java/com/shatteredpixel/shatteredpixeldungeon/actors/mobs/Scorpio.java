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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ScorpioSprite;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

public class Scorpio extends Mob {
	
	{
		spriteClass = ScorpioSprite.class;
		
		HP = HT = 110;
		defenseSkill = 24;
		viewDistance = Light.DISTANCE;
		
		EXP = 14;
		maxLvl = 27;
		
		loot = Generator.Category.POTION;
		lootChance = 0.5f;

		properties.add(Property.DEMONIC);
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 30, 40 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 36;
	}
	
	@Override
	public int drRoll() {
		return super.drRoll() + Random.NormalIntRange(0, 16);
	}
	
	@Override
	protected boolean canAttack( Char enemy ) {
		return !Dungeon.level.adjacent( pos, enemy.pos )
				&& (super.canAttack(enemy) || new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos);
	}
	
	@Override
	public int attackProc( Char enemy, int damage ) {
		damage = super.attackProc( enemy, damage );
		if (Random.Int( 2 ) == 0) {
			Buff.prolong( enemy, Cripple.class, Cripple.DURATION );
		}
		
		return damage;
	}
	
	@Override
	protected boolean getCloser( int target ) {
		if (state == HUNTING) {
			return enemySeen && getFurther( target );
		} else {
			return super.getCloser( target );
		}
	}
	
	@Override
	public void aggro(Char ch) {
		//cannot be aggroed to something it can't see
		//skip this check if FOV isn't initialized
		if (ch == null || fieldOfView == null
				|| fieldOfView.length != Dungeon.level.length() || fieldOfView[ch.pos]) {
			super.aggro(ch);
		}
	}

	@Override
	public Item createLoot() {
		//Pixel Dungeon Reforged Randomizer, Acidic Carriers now and then drop a potion of experience
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(Scorpio.class) == 2 && Random.Int(10) == 0) {
			return new com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience();
		}
		Class<?extends Potion> loot;
		do{
			loot = (Class<? extends Potion>) Random.oneOf(Generator.Category.POTION.classes);
		} while (loot == PotionOfHealing.class || loot == PotionOfStrength.class);

		return Reflection.newInstance(loot);
	}
	

	//Pixel Dungeon Reforged Randomizer. Acidic Infestation (in the mob spawner), Acidic Carriers (above), Lightning Fast,
	//Territorial Hunters, Sluggish Crawl, Brittle Shells
	private int lastEnemyPosition = -1;
	private static final String LAST_ENEMY_POSITION = "last_enemy_position";

	{
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(Scorpio.class) == 3) HP = HT = Math.max(1, HT / 3);
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureBuff(Scorpio.class) == 3) WANDERING = new Territorial();
	}

	@Override
	public void storeInBundle( com.watabou.utils.Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( LAST_ENEMY_POSITION, lastEnemyPosition );
	}

	@Override
	public void restoreFromBundle( com.watabou.utils.Bundle bundle ) {
		super.restoreFromBundle( bundle );
		if (bundle.contains( LAST_ENEMY_POSITION )) lastEnemyPosition = bundle.getInt( LAST_ENEMY_POSITION );
	}

	@Override
	protected boolean act() {
		if (enemy != null && enemySeen) lastEnemyPosition = enemy.pos;
		return super.act();
	}

	@Override
	public float speed() {
		float speed = super.speed();
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureBuff(Scorpio.class) == 2) speed *= 2f;
		else if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(Scorpio.class) == 1) speed *= 0.25f;
		return speed;
	}

	//Territorial Hunters, when wandering it picks places that do not go past where it last saw the hero
	private class Territorial extends Mob.Wandering {
		@Override
		protected int randomDestination() {
			if (lastEnemyPosition == -1) return super.randomDestination();

			int len = Dungeon.level.length();
			boolean[] passable = new boolean[len];
			for (int i = 0; i < len; i++) {
				passable[i] = Dungeon.level.passable[i] && (Dungeon.level.visited[i] || Dungeon.level.mapped[i]);
			}

			com.watabou.utils.Point seen = Dungeon.level.cellToPoint( lastEnemyPosition );
			int destination = super.randomDestination();
			for (int tries = 0; tries < 100; tries++) {
				destination = super.randomDestination();
				PathFinder.Path path = Dungeon.findPath( Scorpio.this, destination, passable, fieldOfView, true );
				boolean valid = true;
				if (path != null) {
					for (int step : path) {
						com.watabou.utils.Point p = Dungeon.level.cellToPoint( step );
						if ((p.x - seen.x) * (p.x - seen.x) + (p.y - seen.y) * (p.y - seen.y) < 3) {
							valid = false;
							break;
						}
					}
				}
				if (valid) break;
			}
			return destination;
		}
	}
}
