package com.shatteredpixel.shatteredpixeldungeon.items.bags;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.powers.Power;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

//Cursed PD keeps its spells in this bag. It holds as many as the hero's magic skill.
public class PowerHolder extends Bag {

	{
		image = ItemSpriteSheet.SPELLBOOK;

		unique = true;

		bones = false;
	}

	@Override
	public boolean canHold( Item item ) {
		return item instanceof Power && super.canHold( item );
	}

	@Override
	public int capacity() {
		if (Dungeon.hero == null) return 35;
		return Math.max( 5, Dungeon.hero.magicSkill );
	}

}
