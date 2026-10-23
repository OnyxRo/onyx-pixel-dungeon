/*
 * Onyx's Pixel Dungeon
 * DERanged PD marks its magic swords with this, and draws their slashes with a tinted wound (GPL-3.0).
 */
package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.rka;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.watabou.noosa.Group;

public interface RkaSpellblade {

	class Wound extends com.shatteredpixel.shatteredpixeldungeon.effects.Wound {
		int color;

		@Override
		public void update() {
			super.update();
			hardlight(color);
		}

		public static void hit( int pos, float angle, int color ) {
			if (Dungeon.hero == null || Dungeon.hero.sprite == null || Dungeon.hero.sprite.parent == null) return;
			Group parent = Dungeon.hero.sprite.parent;
			Wound w = (Wound) parent.recycle( Wound.class );
			parent.bringToFront( w );
			w.reset( pos );
			w.angle = angle;
			w.color = color;
		}
	}
}
