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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.RatDust;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;

import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpectralSamuraiSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpectralWizardSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class SpectralWizard extends Mob implements Callback {

	{
		spriteClass = SpectralWizardSprite.class;
		HP = HT = 170;
		defenseSkill = 24;
		EXP = 16;
		maxLvl = 30;
		loot = new RatDust();
		lootChance = 1f;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 20, 32 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 35;
	}
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 14);
	}

	@Override
	protected boolean canAttack( Char enemy ) {
		return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
	}

	protected boolean doAttack( Char enemy ) {

		if (Dungeon.level.adjacent( pos, enemy.pos )) {

			return super.doAttack( enemy );

		} else {

			boolean visible = fieldOfView[pos] || fieldOfView[enemy.pos];
			if (visible) {
				sprite.zap( enemy.pos );
			} else {
				zap();
			}

			return !visible;
		}
	}

	public static class DarkBolt{}

	private void zap() {
		spend( 1 );

		if (hit( this, enemy, true )) {

			int dmg = Random.Int( 16, 28 );
			enemy.damage( dmg, new SpectralWizard.DarkBolt() );

			if (!enemy.isAlive() && enemy == Dungeon.hero) {
				Dungeon.fail( getClass() );
				GLog.n( "You were killed by the arcane bolt..." );
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
	public int attackProc( Char enemy, int damage ){
		damage = super.attackProc( enemy, damage );
		if(enemy instanceof Hero && Random.Float()<0.5f){
			ScrollOfTeleportation.teleportHero(Dungeon.hero);
		}
		return damage;
	}

	@Override
	public void call() {
		next();
	}
}
