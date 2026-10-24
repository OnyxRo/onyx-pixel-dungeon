/*
 * Onyx's Pixel Dungeon
 *
 * magicling, the gas neutralizing potion found in Magic Ling's jungle boss arenas (Crivus Fruits and Qliphoth).
 * The mod kept it inside its PotionOfPurity. It is always known, worth nothing, and shatterR clears the
 * smoke screen around a cell, which the Qliphoth fight uses.
 */
package com.shatteredpixel.shatteredpixeldungeon.items.potions;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.SmokeScreen;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;

public class PotionOfPurityLing extends PotionOfPurity {

	{
		icon = ItemSpriteSheet.Icons.POTION_PURITY;
		image = ItemSpriteSheet.GREEN_LING;
	}

	@Override
	public int energyVal() {
		return 0;
	}

	@Override
	public int value() {
		return 0;
	}

	@Override
	public boolean isKnown() {
		return true;
	}

	public void shatterR( int cell, int dist ) {
		PathFinder.buildDistanceMap( cell, BArray.not( Dungeon.level.solid, null ), dist );
		Blob smoke = Dungeon.level.blobs.get( SmokeScreen.class );
		if (smoke == null || smoke.volume <= 0) return;
		for (int i = 0; i < Dungeon.level.length(); i++) {
			if (PathFinder.distance[i] < Integer.MAX_VALUE) {
				smoke.clear( i );
			}
		}
	}
}
