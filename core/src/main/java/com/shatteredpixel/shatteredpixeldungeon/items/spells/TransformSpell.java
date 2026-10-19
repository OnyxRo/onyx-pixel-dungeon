/*
 * Onyx's Pixel Dungeon
 * The metamorphosis crystal from Transformation PD (dongyusangyu). A directed metamorphosis, the player picks the
 * kind and then sees every talent of that kind for the tier.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.TransformationTalents;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMetamorphosis;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class TransformSpell extends Spell {

	{
		image = ItemSpriteSheet.TRANSFORM_SPELL;
		talentChance = 0;
	}

	@Override
	protected void onCast(Hero hero) {
		updateQuickslot();
		Catalog.countUse(getClass());
		TransformationTalents.directed = true;
		GameScene.show(new ScrollOfMetamorphosis.WndMetamorphChoose());
	}

	@Override
	public int value() {
		return 60 * quantity;
	}

	@Override
	public int energyVal() {
		return 0;
	}

	public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {
		{
			inputs = new Class[]{ScrollOfMetamorphosis.class};
			inQuantity = new int[]{8};
			cost = 1;
			output = TransformSpell.class;
			outQuantity = 1;
		}
	}
}
