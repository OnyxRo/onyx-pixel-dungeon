/*
 * Too Cruel Pixel Dungeon Trials (GPL-3.0), ported into Onyx's Pixel Dungeon.
 * Based on Shattered Pixel Dungeon by Evan Debenham.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.blobs.toocruel;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.toocruel.PatronSaints;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.PathFinder;

//Patron Saints and Holy Water, saint auras that stay on the floor and strengthen monsters in them.
//Each cell keeps its aura stacks in the low four bits and its saint souls in the next four.
public class PatronSaintsBlob extends Blob {

	public static final int STACKS_MASK = 0b00001111;
	public static final int SOULS_MASK  = 0b11110000;
	public static final int MAX_STACKS = 8;
	public static final int SEED_SOUL  = 0x9870;
	public static final int SEED_BLESS = 0x9871;

	{
		actPriority = MOB_PRIO - 1;
	}

	private static int stacksOf( int v ) { return v & STACKS_MASK; }
	private static int soulsOf( int v ) { return (v & SOULS_MASK) >> 4; }
	private static int encode( int souls, int stacks ) { return (souls << 4) | stacks; }

	public int stacksAt( int pos ) {
		if (cur == null || pos < 0 || pos >= cur.length) return 0;
		return stacksOf( cur[pos] );
	}

	@Override
	protected void evolve() {
		int w = Dungeon.level.width();
		for (int i = area.top - 1; i <= area.bottom; i++) {
			for (int j = area.left - 1; j <= area.right; j++) {
				int cell = j + i * w;
				if (Dungeon.level.insideMap( cell )) {
					off[cell] = cur[cell];
					volume += off[cell];
				}
			}
		}
		for (Char ch : Actor.chars()) {
			if (ch.pos >= 0 && ch.pos < cur.length && cur[ch.pos] > 0 && ch.alignment != Char.Alignment.ALLY && ch != Dungeon.hero) {
				Buff.affect( ch, PatronSaints.class );
			}
		}
	}

	private void raise( Level level, int cell, int newValue ) {
		int old = cur == null ? 0 : cur[cell];
		if (newValue > old) super.seed( level, cell, newValue - old );
	}

	@Override
	public void seed( Level level, int cell, int amount ) {
		if (!level.insideMap( cell )) return;
		int center = cur == null ? 0 : cur[cell];
		if (amount == SEED_SOUL) {
			raise( level, cell, encode( Math.min( MAX_STACKS, soulsOf( center ) + 1 ), Math.min( MAX_STACKS, stacksOf( center ) + 1 ) ) );
			for (int o : PathFinder.NEIGHBOURS8) {
				int pos = cell + o;
				if (!level.insideMap( pos )) continue;
				int v = cur == null ? 0 : cur[pos];
				raise( level, pos, encode( soulsOf( v ), Math.min( MAX_STACKS, stacksOf( v ) + 1 ) ) );
			}
		} else if (amount == SEED_BLESS) {
			raise( level, cell, encode( soulsOf( center ), Math.max( 1, stacksOf( center ) ) ) );
		} else {
			super.seed( level, cell, amount );
		}
	}

	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );
		emitter.pour( Speck.factory( Speck.LIGHT ), 0.4f );
	}

	@Override
	public String tileDesc() {
		return Messages.get( this, "desc" );
	}
}
