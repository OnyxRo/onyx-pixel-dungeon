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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ThiefSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Thief extends Mob {
	
	public Item item;
	
	{
		spriteClass = ThiefSprite.class;
		
		HP = HT = 20;
		defenseSkill = 12;
		
		EXP = 5;
		maxLvl = 11;

		loot = Random.oneOf(Generator.Category.RING, Generator.Category.ARTIFACT);
		lootChance = 0.03f; //initially, see lootChance()

		WANDERING = new Wandering();
		FLEEING = new Fleeing();

		properties.add(Property.UNDEAD);
	}

	private static final String ITEM = "item";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( ITEM, item );
		bundle.put( MAX_GOLD, maxGold );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		item = (Item)bundle.get( ITEM );
		maxGold = bundle.getInt( MAX_GOLD );
	}

	@Override
	public float speed() {
		if (item != null) return carryingSpeedMultiplier()*super.speed();
		else return super.speed();
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 1, 10 );
	}

	@Override
	public float attackDelay() {
		//Pixel Dungeon Reforged Randomizer, Single Strike takes away the second attack
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(Thief.class) == 2) return super.attackDelay();
		return super.attackDelay()*0.5f;
	}

	@Override
	public float lootChance() {
		//each drop makes future drops 1/3 as likely
		// so loot chance looks like: 1/33, 1/100, 1/300, 1/900, etc.
		return super.lootChance() * (float)Math.pow(1/3f, Dungeon.LimitedDrops.THEIF_MISC.count);
	}

	@Override
	public void rollToDropLoot() {
		if (item != null) {
			Dungeon.level.drop( item, pos ).sprite.drop();
			//updates position
			if (item instanceof Honeypot.ShatteredPot) ((Honeypot.ShatteredPot)item).dropPot( this, pos );
			item = null;
		}
		super.rollToDropLoot();
	}

	@Override
	public Item createLoot() {
		Dungeon.LimitedDrops.THEIF_MISC.count++;
		return super.createLoot();
	}

	@Override
	public int attackSkill( Char target ) {
		return 12;
	}

	@Override
	public int drRoll() {
		return super.drRoll() + Random.NormalIntRange(0, 3);
	}

	@Override
	public int attackProc( Char enemy, int damage ) {
		damage = super.attackProc( enemy, damage );
		
		//Clumsy Hands, only a third of the attempts go anywhere
		boolean clumsy = com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(Thief.class) == 1 && Random.Int(3) > 0;
		if (!clumsy && canStealOnAttack() && alignment == Alignment.ENEMY && item == null
				&& enemy instanceof Hero && steal( (Hero)enemy )) {
			state = FLEEING;
		}

		return damage;
	}

	@Override
	public int defenseProc(Char enemy, int damage) {
		if (state == FLEEING && !(com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(Thief.class) == 3)) {
			Dungeon.level.drop( new Gold(), pos ).sprite.drop();
		}

		return super.defenseProc(enemy, damage);
	}

	//from Transformation PD, so thief variants can change these
	protected float carryingSpeedMultiplier() {
		return 5f/6f;
	}

	protected boolean canStealOnAttack() {
		return true;
	}

	protected Class<?> theftMessageClass() {
		return Thief.class;
	}

	protected boolean steal( Hero hero ) {

		Item toSteal = hero.belongings.getThiefItemToSteal();

		if (toSteal != null ) {

			GLog.w( Messages.get(theftMessageClass(), "stole", toSteal.name()) );
			if (!toSteal.stackable) {
				Dungeon.quickslot.convertToPlaceholder(toSteal);
			}
			Item.updateQuickslot();

			//Bold Fingers can take something the hero was wearing, which is no longer in the pack
			item = hero.belongings.backpack.contains( toSteal ) ? toSteal.detach( hero.belongings.backpack ) : toSteal;
			if (item instanceof Honeypot){
				item = ((Honeypot)item).shatter(this, this.pos);
			} else if (item instanceof Honeypot.ShatteredPot) {
				((Honeypot.ShatteredPot)item).pickupPot(this);
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	public String description() {
		String desc = super.description();

		if (item != null) {
			desc += Messages.get(this, "carries", item.name() );
		}

		return desc;
	}
	
	protected class Wandering extends Mob.Wandering {
		
		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted) {
			super.act(enemyInFOV, justAlerted);
			
			//if an enemy is just noticed and the thief posses an item, run, don't fight.
			if (state == HUNTING && item != null){
				state = FLEEING;
			}
			
			return true;
		}
	}

	private class Fleeing extends Mob.Fleeing {
		@Override
		protected void escaped() {
			if (item != null
					&& !Dungeon.level.heroFOV[pos]
					&& Dungeon.level.distance(Dungeon.hero.pos, pos) >= 6) {

				int count = 32;
				int newPos;
				do {
					newPos = Dungeon.level.randomRespawnCell( Thief.this );
					if (count-- <= 0) {
						break;
					}
				} while (newPos == -1 || Dungeon.level.heroFOV[newPos] || Dungeon.level.distance(newPos, pos) < (count/3));

				if (newPos != -1) {

					pos = newPos;
					sprite.place( pos );
					sprite.visible = Dungeon.level.heroFOV[pos];
					if (Dungeon.level.heroFOV[pos]) CellEmitter.get(pos).burst(Speck.factory(Speck.WOOL), 6);

				}

				if (item != null) GLog.n( Messages.get(theftMessageClass(), "escapes", item.name()));
				item = null;
				state = WANDERING;
			} else {
				state = WANDERING;
			}
		}
	}

	//Pixel Dungeon Reforged Randomizer. Master Pickpocket and Bold Fingers (in Belongings.getThiefItemToSteal),
	//Bandit Recruitment (in the mob spawner), Clumsy Hands and Single Strike (above), Gold Bags
	private int maxGold = 0;
	private static final String MAX_GOLD = "max_gold";

	@Override
	public void damage( int dmg, Object src ) {
		int before = HP;
		super.damage( dmg, src );
		//Gold Bags, a thief spills 25 to 50 gold over its life whether it runs or not
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(Thief.class) == 3 && before > HP) {
			if (maxGold == 0) maxGold = Random.Int( 25, 50 );
			float share = (float)(before - HP) / (float)HT;
			int gold = (int)(maxGold * share);
			if (gold > 0) Dungeon.level.drop( new Gold( gold ), pos ).sprite.drop();
		}
	}
}
