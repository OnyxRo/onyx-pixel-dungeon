/*
 * Soft Pixel Dungeon by wolispace (github.com/wolispace/soft-pixel-dungeon), on Shattered 0.2.3.
 * Its "Not so hard" setting, ported as a game mode. Tall grass sometimes holds a useful item
 * and searching reaches one tile further. The mod's save and load to the SD card is Android only
 * and left out.
 */
package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.ScrollHolder;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.VelvetPouch;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.Dart;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.watabou.utils.Random;

public class SoftPD {

	public static final String NOT_SO_HARD = "soft_not_so_hard";
	public static final String MOD = "Soft Pixel Dungeon";

	public static void register(){
		GameModes.register(NOT_SO_HARD, MOD, 1, null);
	}

	public static boolean on(){
		return GameModes.on(NOT_SO_HARD);
	}

	//the mod's chances, rolled one after another, so a tuft can hold more than one item
	public static void grassLoot( Level level, int pos ){
		if (!on()) return;
		if (Random.Int(10) == 0) drop(level, pos, Generator.randomUsingDefaults(Generator.Category.FOOD));
		if (Random.Int(15) == 0) drop(level, pos, new PotionOfHealing().identify());
		if (Random.Int(15) == 0) drop(level, pos, new Dart().quantity(Random.Int(3) + 1));
		if (Random.Int(20) == 0) drop(level, pos, new ScrollOfIdentify().identify());
		if (Random.Int(40) == 0) drop(level, pos, new ScrollHolder());
		if (Random.Int(40) == 0) drop(level, pos, new VelvetPouch());
		if (Random.Int(60) == 0) drop(level, pos, Generator.randomUsingDefaults(Generator.Category.RING));
		if (Random.Int(60) == 0) drop(level, pos, Generator.randomUsingDefaults(Generator.Category.SCROLL));
	}

	private static void drop( Level level, int pos, Item item ){
		if (item == null) return;
		level.drop(item, pos).sprite.drop();
	}

	//the mod searched one tile further
	public static int searchBonus(){
		return on() ? 1 : 0;
	}
}
