/*
 * Too Cruel Pixel Dungeon Trials (GPL-3.0), ported into Onyx's Pixel Dungeon.
 * Based on Shattered Pixel Dungeon by Evan Debenham.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.toocruel;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.toocruel.PatronSaintsBlob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

//Patron Saints, a monster standing in a saint aura hits harder and takes less damage
public class PatronSaints extends Buff implements TcBuff.AttackAmp, TcBuff.DamageAmp {

	private int lastShownStacks = 0;

	@Override
	public int icon() {
		return BuffIndicator.LIGHT;
	}

	@Override
	public void tintIcon( Image icon ) {
		icon.hardlight( 0xFFA500 );
	}

	@Override
	public boolean act() {
		int stacks = stacks();
		if (stacks == 0) {
			detach();
			return true;
		} else if (stacks != lastShownStacks) {
			fx( true );
		}
		spend( TICK );
		return true;
	}

	public int stacks() {
		if (Dungeon.level == null) return 0;
		PatronSaintsBlob blob = (PatronSaintsBlob) Dungeon.level.blobs.get( PatronSaintsBlob.class );
		return blob == null ? 0 : blob.stacksAt( target.pos );
	}

	@Override
	public float damageMultiplier( Object src ) {
		return 1 - Math.min( (float) Math.sqrt( stacks() / 9.8765f ), 0.90f );
	}

	@Override
	public float attackMultiplier() {
		return (float) Math.pow( 1.3f, stacks() );
	}

	@Override
	public void fx( boolean on ) {
		if (target.sprite == null) return;
		//champions already use the aura
		if (!target.buffs( ChampionEnemy.class ).isEmpty()) return;
		if (on) {
			lastShownStacks = stacks();
			target.sprite.aura( 0xFFA500, 3 + lastShownStacks );
		} else {
			target.sprite.clearAura();
		}
	}

	@Override
	public String desc() {
		return Messages.get( this, "desc", stacks(),
				Messages.decimalFormat( "##", (attackMultiplier() - 1) * 100.0 ),
				Messages.decimalFormat( "##", (1 - damageMultiplier( null )) * 100.0 ) );
	}
}
