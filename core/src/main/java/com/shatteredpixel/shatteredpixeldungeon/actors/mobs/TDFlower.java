/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.TDTreasureBag;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FlowerSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WarlockSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class TDFlower extends Mob implements Callback {
	
	private static final float TIME_TO_ZAP	= 1f;
	
	{
		spriteClass = FlowerSprite.class;
		
		HP = HT = 25;
		defenseSkill = 3;
		
		EXP = 6;
		maxLvl = 30;

		loot = new com.shatteredpixel.shatteredpixeldungeon.items.stones.TDTreasureBag();
		lootChance = 1f;

		properties.add(Property.IMMOVABLE);
		//properties.add(Property.MINIBOSS);
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 1, 5 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 13;
	}
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 3);
	}
	
	@Override
	protected boolean canAttack( Char enemy ) {
		return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
	}

	@Override
	public void beckon( int cell ) {
	}

	@Override
	protected boolean getCloser(int target) {
		return false;
	}

	protected boolean doAttack( Char enemy ) {
			
			boolean visible = fieldOfView[pos] || fieldOfView[enemy.pos];
			if (visible) {
				sprite.zap( enemy.pos );
			} else {
				zap();
			}
			
			return !visible;

	}
	
	//used so resistances can differentiate between melee and magical attacks
	public static class DarkBolt{}
	
	private void zap() {
		spend( TIME_TO_ZAP );
		
		if (hit( this, enemy, true )) {
				Buff.affect( enemy, Burning.class ).reignite( enemy );
			
			int dmg = Random.Int( 1, 5 );
			enemy.damage( dmg, new DarkBolt() );
			
			if (!enemy.isAlive() && enemy == Dungeon.hero) {
				Dungeon.fail( getClass() );
				GLog.n( "You were killed by the fireball..." );
			}
		} else {
			enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
		}
	}
	
	public void onZapComplete() {
		zap();
		next();
	}
	
	@Override
	public void call() {
		next();
	}
}
