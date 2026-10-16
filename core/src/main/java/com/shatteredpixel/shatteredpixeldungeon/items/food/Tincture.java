package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Panic;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Reason;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

// Transformation PD. A plain meal that also calms a hero who carries the Reason meter.
// The mod's Vegetarianism talent and the extra meal talents are not ported, see PORTING.md.
public class Tincture extends Food {

	{
		image = ItemSpriteSheet.TINCTURE;
		energy = Hunger.HUNGRY/3f; //100 food value
	}

	@Override
	protected void satisfy(Hero hero) {
		if (hero != null && hero.buff(Reason.class) != null) {
			Reason.gainReason(hero, 50);
			if (hero.buff(Panic.class) != null) hero.buff(Panic.class).detach();
		}
		super.satisfy( hero );
	}

	@Override
	public int value() {
		return 5 * quantity;
	}
}
