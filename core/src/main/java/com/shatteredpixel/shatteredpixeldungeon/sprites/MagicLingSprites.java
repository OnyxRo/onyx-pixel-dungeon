/*
 * Onyx's Pixel Dungeon
 * Sprites that Magic Ling PD added as inner classes of Shattered's own sprite classes.
 * They live here so those classes stay as Shattered wrote them.
 */
package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class MagicLingSprites {

	public static class BatEDSprite extends MobSprite {

		public BatEDSprite() {
			super();

			texture(Assets.Sprites.BATEX);

			TextureFilm frames = new TextureFilm(texture, 15, 15);

			idle = new Animation(8, true);
			idle.frames(frames, 0, 1);

			run = new Animation(12, true);
			run.frames(frames, 0, 1);

			attack = new Animation(12, false);
			attack.frames(frames, 2, 3, 0, 1);

			die = new Animation(12, false);
			die.frames(frames, 4, 5, 6);

			play(idle);
		}
	}

	public static class NewBornCrabSprite extends MobSprite {

		public NewBornCrabSprite() {
			super();

			texture( Assets.Sprites.NEWBORN_CRAB );

			TextureFilm frames = new TextureFilm( texture, 16, 16 );

			idle = new Animation( 4, true );
			idle.frames( frames, 0, 1, 2, 3 );

			run = new Animation( 11, true );
			run.frames( frames, 4, 5, 6, 7 );

			attack = new Animation( 11, false );
			attack.frames( frames, 8, 9,10,11 );

			die = new Animation( 11, false );
			die.frames( frames, 12, 13,14,15 );

			play( idle );
		}

		@Override
		public int blood() {
			return 0xFFFFEA80;
		}
	}

	//the mod's diamond mimic had its own rows, here it borrows the crystal mimic's
	public static class Dimand extends MimicSprite.Crystal {
	}
}
