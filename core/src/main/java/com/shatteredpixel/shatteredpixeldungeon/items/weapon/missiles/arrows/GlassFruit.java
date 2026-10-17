package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.arrows;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.Dart;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

//SPS. A fruit of glass that cuts. Written on our Dart, SPS's arrow and bow system is not here.
public class GlassFruit extends Dart {

	{
		image = ItemSpriteSheet.SEED_SIOFLOWER;
		tier = 1;
	}

	public GlassFruit() {
		this(1);
	}

	public GlassFruit(int number) {
		super();
		quantity = number;
	}

	@Override
	public int proc(Char attacker, Char defender, int damage) {
		Buff.affect(defender, Bleeding.class).set(Math.max(1, damage / 2));
		return super.proc(attacker, defender, damage);
	}

	@Override
	public int value() {
		return 4 * quantity;
	}
}
