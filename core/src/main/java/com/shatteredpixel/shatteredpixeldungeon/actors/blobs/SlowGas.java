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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SpeedSlow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.StandDown;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SnowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

public class SlowGas extends Blob {

	@Override
	protected void evolve() {

		int from = Dungeon.level.width() + 1;
		int to = Dungeon.level.length() - Dungeon.level.width() - 1;

		for (int pos = from; pos < to; pos++) {

			int ice;
	
			
			if (cur[pos] > 0) {

				ice(pos);

				ice = cur[pos] - 1;
				if (ice <= 0){ }
			} else {
				ice = 0;
			}
			
			volume += (off[pos] = ice);

		}

	}

	private void ice(int pos) {
					Char ch = Actor.findChar( pos );
					if (ch != null && !ch.isImmune(this.getClass())) {
						if (ch.buff(StandDown.class) != null){
							Buff.affect(ch, StandDown.class, 2f);
						} else {
							Buff.affect(ch, SpeedSlow.class, 3f);
							SpeedSlow chill = ch.buff(SpeedSlow.class);
							if (chill != null && chill.cooldown() >= 10f){
								Buff.affect(ch, StandDown.class, 5f);
							}
						}
					}
					
					Heap heap = Dungeon.level.heaps.get( pos );
					if (heap != null) heap.icehit();
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
		emitter.start(SnowParticle.FACTORY, 0.05f, 0);
	}
	@Override
	public String tileDesc() {
		return Messages.get(this, "desc");
	}
}
