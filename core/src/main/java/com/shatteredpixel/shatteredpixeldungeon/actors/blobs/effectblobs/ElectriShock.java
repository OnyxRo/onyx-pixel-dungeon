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
package com.shatteredpixel.shatteredpixeldungeon.actors.blobs.effectblobs;

import com.watabou.utils.Random;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Shocked;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EnergyParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

public class ElectriShock extends Blob {

	@Override
	protected void evolve() {

		//boolean[] shockable = Level.shockable;

		int from = Dungeon.level.width() + 1;
		int to = Dungeon.level.length() - Dungeon.level.width() - 1;


		for (int pos = from; pos < to; pos++) {

			int shock;
			boolean shelf = false;
			
			if (cur[pos] > 0) {

				shocking(pos);

				shock = cur[pos] - 1;
				
                if (shock <= 0){   }
			} else {
				shock = 0;
			}
 
            volume += (off[pos] = shock);
		}

	}

	private void shocking(int pos) {
		Char ch = Actor.findChar(pos);
		if (ch != null && !ch.isImmune(this.getClass()) ) {
			//Buff.detach(ch,Corruption.class);
			//ch.damage( Math.max( 1, Random.Int( ch.HP / 100, ch.HP / 50 ) ), this );
			Buff.affect(ch, Shocked.class).set(10f);
			
		}
		Heap heap = Dungeon.level.heaps.get(pos);
		if (heap != null) {
			heap.shockhit();
		}

	}

	@Override
	public void seed( Level level, int cell, int amount ) { if (cur == null) cur = new int[level.length()]; if (off == null) off = new int[cur.length];
		if (cur[cell] == 0) {
			volume += amount;
			cur[cell] = amount;
		/*} else {
			volume += amount - cur[cell];
			cur[cell] = amount;*/
		}
	}

	@Override
	public void use(BlobEmitter emitter) {
		super.use(emitter);
		emitter.start(EnergyParticle.FACTORY, 0.05f, 0);
	}
	@Override
	public String tileDesc() {
		return Messages.get(this, "desc");
	}

}
