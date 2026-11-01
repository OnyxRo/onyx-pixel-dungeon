/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Re-Remixed Dungeon, by QuasiStellar, ported into Onyx's Pixel Dungeon
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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DeathlingSprite;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

//reremixed, the Necromancer's Deathling skill calls up to three of these. They grow with the hero's level
//and follow him around, two steps at a time when they fall behind.
public class DeathlingMob extends Mob {

	private int level;

	{
		spriteClass = DeathlingSprite.class;

		flying = true;
		alignment = Alignment.ALLY;
		intelligentAlly = true;
		WANDERING = new Following();
		state = HUNTING;

		//before other mobs
		actPriority = MOB_PRIO + 1;

		properties.add(Property.UNDEAD);
		immunities.add(Grim.class);
		immunities.add(Terror.class);
	}

	private static final String LEVEL = "level";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( LEVEL, level );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		int hp = HP;
		adjustStats( bundle.getInt( LEVEL ) );
		HP = Math.min(hp, HT);
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 1 + level/2, 2 + level );
	}

	@Override
	public int attackSkill( Char target ) {
		return 10 + level;
	}

	public void adjustStats( int level ) {
		this.level = level;
		HP = HT = 8 + level;
		defenseSkill = attackSkill( null ) / 2;
		enemySeen = true;
	}

	@Override
	public boolean reset() {
		state = WANDERING;
		return true;
	}

	public static DeathlingMob spawnAt( int pos ) {
		if (!Dungeon.level.passable[pos] || Actor.findChar( pos ) != null) return null;
		DeathlingMob d = new DeathlingMob();
		d.adjustStats( Dungeon.hero.lvl );
		d.pos = pos;
		GameScene.add( d );
		if (d.sprite != null && d.sprite.parent != null) {
			d.sprite.alpha( 0 );
			d.sprite.parent.add( new AlphaTweener( d.sprite, 1, 0.5f ) );
			d.sprite.emitter().burst( ShadowParticle.CURSE, 5 );
		}
		return d;
	}

	//always heads back to the hero when there is nothing to fight
	private class Following extends Mob.Wandering {

		@Override
		protected boolean continueWandering() {
			enemySeen = false;
			int oldPos = pos;
			target = Dungeon.hero.pos;
			if (getCloser( target )) {
				if (!Dungeon.level.adjacent( target, pos )) getCloser( target );
				spend( 1 / speed() );
				return moveSprite( oldPos, pos );
			}
			spend( TICK );
			return true;
		}
	}
}
