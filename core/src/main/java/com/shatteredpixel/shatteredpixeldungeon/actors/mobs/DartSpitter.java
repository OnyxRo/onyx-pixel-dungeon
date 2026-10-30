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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DartSpitterSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ScorpioSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class DartSpitter extends Mob {
	
	{
		spriteClass = DartSpitterSprite.class;
		
		HP = HT = 45;
		defenseSkill = 13;
		viewDistance = Light.DISTANCE;
		baseSpeed = 2f;

		EXP = 8;
		maxLvl = 16;
		
		loot = Generator.Category.MISSILE;
		lootChance = 0.2f;

	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 6, 21 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 18;
	}
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 7);
	}
	
	@Override
	protected boolean canAttack( Char enemy ) {

		Ballistica attack = new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE);
		return attack.collisionPos == enemy.pos;
	}

	@Override
	public int defenseProc( Char enemy, int damage ) {

		if(HP-damage<HT/2 && Random.Int( 3 )>0){
			Buff.affect( this, Terror.class, 10f+Random.Int( 16 ) );
		}
		return super.defenseProc( enemy, damage );
	}
	
}
