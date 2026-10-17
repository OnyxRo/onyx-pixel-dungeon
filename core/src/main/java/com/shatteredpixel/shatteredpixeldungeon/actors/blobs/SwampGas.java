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
package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.watabou.utils.PathFinder;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArmorBreak;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SpeedSlow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.StandDown;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Random;


public class SwampGas extends Blob {

	@Override
	protected void evolve() {
		super.evolve();
		
		Char ch;
		for (int i = 0; i < Dungeon.level.length(); i++) {
			if (cur[i] > 0 && (ch = Actor.findChar(i)) != null) {
				if (!ch.isImmune(this.getClass())){
					Buff.affect(ch,ArmorBreak.class,3f).level(20);
					if (ch.buff(StandDown.class) != null){
						Buff.affect(ch, StandDown.class, 2f);
					} else {
						Buff.affect(ch, SpeedSlow.class, 3f);
						SpeedSlow speedslow = ch.buff(SpeedSlow.class);
						if (speedslow != null && speedslow.cooldown() >= 10f){
							Buff.affect(ch, StandDown.class, 5f);
						}
					}
                }	
			}				
        }
		
        Blob blob = Dungeon.level.blobs.get( Fire.class );
        if (blob != null) {

            for (int pos=0; pos < Dungeon.level.length(); pos++) {

                if ( cur[pos] > 0 && blob.cur[ pos ] < 2 ) {

                    int flammability = 0;

                    for (int n : PathFinder.NEIGHBOURS8) {
                        if ( blob.cur[ pos + n ] > 0 ) {
                            flammability++;
                        }
                    }
                    if( Random.Int( 4 ) < flammability ) {
                        blob.volume += ( blob.cur[ pos ] = 2 );
                        volume -= ( cur[pos] / 2 );
                        cur[pos] -=( cur[pos] / 2 );
                    }
                }
            }
        }			
	}

	@Override
	public void use(BlobEmitter emitter) {
		super.use(emitter);

		emitter.pour(Speck.factory(Speck.TARGAS,true ), 0.6f);
	}

	@Override
	public String tileDesc() {
		return Messages.get(this, "desc");
	}
}