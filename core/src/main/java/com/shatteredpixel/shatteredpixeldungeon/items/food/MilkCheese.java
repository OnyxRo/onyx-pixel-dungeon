package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

/**
 * Shittened PD's plain cheese, made from milk. It is called Cheese in the mod, but this game already has
 * Experienced PD's Cheese, so this one has its own name here.
 */
public class MilkCheese extends TastyFood {

	{
		image = ItemSpriteSheet.MILK_CHEESE;
		energy = 90f;
		carbs = 8f;
		fats = 27f;
		proteins = 4f;
		salt = 0.1f;
	}

	@Override
	public int value() {
		return 15 * quantity;
	}

	public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

		{
			inputs =  new Class[]{Milk.class};
			inQuantity = new int[]{1};

			cost = 0;

			output = MilkCheese.class;
			outQuantity = 1;
		}
	}
}
