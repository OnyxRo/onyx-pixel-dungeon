/*
 * Too Cruel Pixel Dungeon Trials (GPL-3.0), ported into Onyx's Pixel Dungeon.
 * Based on Shattered Pixel Dungeon by Evan Debenham.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.StatueSprite;

//Too Cruel's Fort Knox, a squishy living statue that holds a weapon found on the floor
public class TcHolderStatue extends Statue {

	{
		spriteClass = StatueSprite.class;
	}

	public TcHolderStatue() {
		super();
		HP = HT = 10 + Dungeon.depth * 3;
	}
}
