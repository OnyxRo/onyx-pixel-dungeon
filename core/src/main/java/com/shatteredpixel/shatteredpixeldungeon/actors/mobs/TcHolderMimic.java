/*
 * Too Cruel Pixel Dungeon Trials (GPL-3.0), ported into Onyx's Pixel Dungeon.
 * Based on Shattered Pixel Dungeon by Evan Debenham.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.TooCruel;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MimicSprite;

//Too Cruel's weak mimic, holds items or a monster and gives no prize of its own
public class TcHolderMimic extends Mimic {

	{
		spriteClass = MimicSprite.class;
	}

	@Override
	public void setLevel( int level ) {
		//Treasure Chest Party Quest makes them as strong as normal mimics
		if (TooCruel.on( TooCruel.MIMICS_GRIND )) super.setLevel( level );
		else super.setLevel( Math.round( level * 0.75f ) );
	}

	@Override
	protected void generatePrize( boolean useDecks ) {
		if (TooCruel.on( TooCruel.MIMICS_GRIND )) super.generatePrize( useDecks );
	}
}
