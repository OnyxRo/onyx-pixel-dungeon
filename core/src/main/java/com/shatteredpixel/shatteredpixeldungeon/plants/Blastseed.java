package com.shatteredpixel.shatteredpixeldungeon.plants;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

//Deistic Pixel Dungeon's blastseed (BombFruit), grown by the Wooden Effigy. It explodes like a bomb when touched.
//It has no picture of its own, so it borrows the firebloom's.
public class Blastseed extends Plant {

	{
		image = 1;
		seedClass = Seed.class;
	}

	@Override
	public void activate( Char ch ) {
		new Bomb().explode(pos);
	}

	public static class Seed extends Plant.Seed {
		{
			image = ItemSpriteSheet.SEED_FIREBLOOM;
			plantClass = Blastseed.class;
		}
	}
}
