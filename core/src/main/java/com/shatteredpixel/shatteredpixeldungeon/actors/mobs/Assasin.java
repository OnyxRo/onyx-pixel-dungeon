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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfBlink;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.AssasinSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Assasin extends Mob {
	
	private static final int BLINK_DELAY	= 1;
	
	private int delay = 0;
	
	{
		spriteClass = AssasinSprite.class;
		
		HP = HT = 22;
		defenseSkill = 7;
		viewDistance = Light.DISTANCE;
		//baseSpeed = 0.66667f;

		EXP = 5;
		maxLvl = 13;
		
		loot = new StoneOfBlink();
		lootChance = 0.5f;

		properties.add(Property.UNDEAD);
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 6, 13 );
	}

	//@Override
	//public float attackDelay() {
	//	return super.attackDelay() * 1.5f;
	//}



	@Override
	protected boolean getCloser( int target ) {
		if (fieldOfView[target] && Dungeon.level.distance( pos, target ) > 2 && delay <= 0) {
			
			blink( target );
			spend( -1 / speed() );
			return true;
			
		} else {
			
			delay--;
			return super.getCloser( target );
			
		}
	}
	
	private void blink( int target ) {
		
		Ballistica route = new Ballistica( pos, target, Ballistica.PROJECTILE);
		int cell = route.collisionPos;

		//can't occupy the same cell as another char, so move back one.
		if (Actor.findChar( cell ) != null && cell != this.pos)
			cell = route.path.get(route.dist-1);

		if (Dungeon.level.avoid[ cell ]){
			ArrayList<Integer> candidates = new ArrayList<>();
			for (int n : PathFinder.NEIGHBOURS8) {
				cell = route.collisionPos + n;
				if (Dungeon.level.passable[cell] && Actor.findChar( cell ) == null) {
					candidates.add( cell );
				}
			}
			if (candidates.size() > 0) {
				cell = Random.element(candidates);
			}else {
				delay = BLINK_DELAY;
				return;
			}
		}
		if (Dungeon.level.heroFOV[pos]) CellEmitter.get( pos ).burst( Speck.factory( Speck.WOOL ), 6 );
		com.shatteredpixel.shatteredpixeldungeon.TrapDungeon.silencedAppear( this, cell );
		if (Dungeon.level.heroFOV[pos]) {
			Sample.INSTANCE.play(Assets.Sounds.PUFF);
		}
		if (Dungeon.level.heroFOV[pos]) CellEmitter.get( pos ).burst( Speck.factory( Speck.WOOL ), 6 );
		delay = BLINK_DELAY;
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 16;
	}
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 4);
	}
}
