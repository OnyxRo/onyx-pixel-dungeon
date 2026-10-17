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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.Ratmogrify;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Rat extends Mob {

	{
		spriteClass = RatSprite.class;
		
		HP = HT = 8;
		defenseSkill = 2;

		maxLvl = 5;
	}

	@Override
	protected boolean act() {
		if (alignment != Alignment.ALLY
				&& Dungeon.level.heroFOV[pos]
				&& Dungeon.hero.armorAbility instanceof Ratmogrify){
			alignment = Alignment.NEUTRAL;
			if (enemy == Dungeon.hero) enemy = null;
			if (state == SLEEPING) state = WANDERING;
		}
		//Shittened PD, a rat that has seen you may squeal and speed up
		if (Dungeon.level.heroFOV[pos] && Random.Int(90) == 1 && state == HUNTING) {
			com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.affect(this,
					com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste.class, 1f);
			com.watabou.noosa.audio.Sample.INSTANCE.playForDuration( com.shatteredpixel.shatteredpixeldungeon.Assets.Sounds.FREE_RAT,
					Random.NormalFloat(0.2f, Random.Float(1f,4f)) );
		}
		return super.act();
	}

	//Shittened PD, counting rats for the rat killer badges, the odd banner, and the rat album for a music player
	@Override
	public void die(Object cause) {
		super.die(cause);
		if (alignment == Alignment.ALLY) return;
		com.shatteredpixel.shatteredpixeldungeon.Statistics.ratsSlain++;
		com.shatteredpixel.shatteredpixeldungeon.Badges.validateRats();
		if (Random.Int(0,18) == 2) {
			com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene.ratSlain();
		}
		com.shatteredpixel.shatteredpixeldungeon.items.artifacts.MusicPlayer player =
				Dungeon.hero.belongings.getItem( com.shatteredpixel.shatteredpixeldungeon.items.artifacts.MusicPlayer.class );
		if (player != null && Dungeon.LimitedDrops.RAT_ALBUM.count == 0 && Random.Int(10) == 5) {
			Dungeon.level.drop( new com.shatteredpixel.shatteredpixeldungeon.items.albums.RatAlbum(), pos ).sprite.drop();
			Dungeon.LimitedDrops.RAT_ALBUM.count++;
		}
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 1, 4 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 8;
	}
	
	@Override
	public int drRoll() {
		return super.drRoll() + Random.NormalIntRange(0, 1);
	}

	private static final String RAT_ALLY = "rat_ally";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		if (alignment == Alignment.ALLY) bundle.put(RAT_ALLY, true);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(RAT_ALLY)) alignment = Alignment.ALLY;
	}
}
