/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Pixel Dungeon Reforged
 * Copyright (C) 2024-2025 Nathan Pringle
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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BallistaSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

/** Reforged's ballista, a siege weapon that fires one heavy bolt and then has to reload. */
public class Ballista extends Mob {

	{
		spriteClass = BallistaSprite.class;

		HP = HT = 80;
		defenseSkill = 12;

		EXP = 9;
		maxLvl = 17;

		HUNTING = new Hunting();

		loot = Generator.Category.MISSILE;
		lootChance = 0.25f;

		properties.add(Property.INORGANIC);
		properties.add(Property.IMMOVABLE);
		properties.add(Property.STATIC);

		immunities.add( Amok.class );
		immunities.add( Terror.class );
		immunities.add( Sleep.class );
		immunities.add( Bleeding.class );
	}

	protected int ammo = ammoCapacity();

	protected int ammoCapacity() {
		return 1;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 20, 30 );
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
		return !Dungeon.level.adjacent( pos, enemy.pos ) && ammo > 0
				&& (super.canAttack(enemy) || new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos);
	}

	private void reload() {
		ammo = ammoCapacity();
		if (Dungeon.hero != null && Dungeon.level.heroFOV[pos]) {
			Sample.INSTANCE.play( Assets.Sounds.BALLISTA_RELOAD );
			yell( Messages.get(this, "loaded") );
		}
	}

	@Override
	public void aggro( Char ch ) {
		//cannot be aggroed to something it can't see
		if (ch == null || fieldOfView == null
				|| fieldOfView.length != Dungeon.level.length() || fieldOfView[ch.pos]) {
			super.aggro( ch );
		}
	}

	@Override
	public int attackProc( Char enemy, int damage ) {
		damage = super.attackProc( enemy, damage );
		if (Random.Int( 4 ) == 0) {
			int oppositeAdjacent = enemy.pos + (enemy.pos - pos);
			Ballistica trajectory = new Ballistica( enemy.pos, oppositeAdjacent, Ballistica.MAGIC_BOLT );
			WandOfBlastWave.throwChar( enemy, trajectory, 2, false, false, this );
		}
		return damage;
	}

	@Override
	public void onAttackComplete() {
		ammo--;
		super.onAttackComplete();
	}

	private class Hunting extends Mob.Hunting {
		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			if (ammo <= 0) {
				reload();
				spend( attackDelay() );
				return true;
			}
			return super.act( enemyInFOV, justAlerted );
		}
	}

	private static final String AMMO = "ammo";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( AMMO, ammo );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		ammo = bundle.getInt( AMMO );
	}
}
