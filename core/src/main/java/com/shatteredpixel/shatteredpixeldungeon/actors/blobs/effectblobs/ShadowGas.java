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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShadowCurse;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlackFlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

public class ShadowGas extends Blob {

	@Override
	protected void evolve() {

		int from = Dungeon.level.width() + 1;
		int to = Dungeon.level.length() - Dungeon.level.width() - 1;

		for (int pos = from; pos < to; pos++) {

			int dark;
			if (cur[pos] > 0) {
				dark(pos);
				dark = cur[pos] - 1;
				if (dark <= 0){ }
			} else {
				dark = 0;
			}
			
			volume += (off[pos] = dark);

		}

	}

	private void dark(int pos) {
		Char ch = Actor.findChar(pos);
		if (ch != null && !ch.isImmune(this.getClass())) {
			if (ch.buff(ShadowCurse.class) == null) {
				Buff.affect(ch, ShadowCurse.class);
			}

			Heap heap = Dungeon.level.heaps.get(pos);
			if (heap != null) heap.darkhit();
		}

	}

	@Override
	public void seed(int cell, int amount) {
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
		emitter.start(BlackFlameParticle.FACTORY, 0.1f, 0);
	}
	@Override
	public String tileDesc() {
		return Messages.get(this, "desc");
	}
}
