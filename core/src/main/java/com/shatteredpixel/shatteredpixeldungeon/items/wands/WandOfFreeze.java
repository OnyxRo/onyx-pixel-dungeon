/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostIce;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class WandOfFreeze extends DamageWand {

	{
	    image = ItemSpriteSheet.WAND_FREEZE;
		collisionProperties = Ballistica.PROJECTILE;
	}

	public int min(int lvl){
		return 5+lvl*2;
	}

	public int max(int lvl){
		return 10+lvl*4;
	}	
	
	@Override
	public void onZap(Ballistica bolt) {
		
		Char ch = Actor.findChar(bolt.collisionPos);
		if (ch != null) {

		    int damage = damageRoll();

			if (ch.buff(Frost.class) != null){
				return; //do nothing, can't affect a frozen target
			}
			//if (ch.buff(Chill.class) != null){
			//	float chill = ch.buff(Chill.class).cooldown();
			//	damage = (int)(Math.round(damage * Math.pow(0.9f, chill)) * 1);
		//	} else {
			//	ch.sprite.burst( 0xFF99CCFF, level() / 2 + 2 );
			//}

			wandProc(ch, chargesPerCast());
			ch.damage(damage, this,2);

			if (ch.isAlive()){
				if (Dungeon.level.water[ch.pos]){
					//20+(10*level)% chance
					if (Random.Int(10) >= 8-level() )
						Buff.affect(ch, Frost.class, Frost.DURATION*Random.Float(2f, 4f));
					else
						Buff.affect(ch, FrostIce.class).level(5+level());
				} else {
					Buff.affect(ch, FrostIce.class).level(5+level());
				}
			}
			
		}
		
		Heap heap = Dungeon.level.heaps.get(bolt.collisionPos);
		if (heap != null) {
			heap.icehit();
		}	
		
	}
	
	@Override
	public void fx(Ballistica bolt, Callback callback) {
		MagicMissile.boltFromChar(curUser.sprite.parent, MagicMissile.FROST, curUser.sprite, bolt.collisionPos,
				callback);
		Sample.INSTANCE.play(Assets.Sounds.ZAP);
	}
	

	//sps, SPS had no battlemage staff, so a staff holding this wand adds no effect on hit
	@Override
	public void onHit(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff staff, Char attacker, Char defender, int damage) {
	}
}
