/*
 * Onyx's Pixel Dungeon
 * Hooks for the pet eggs of Goblins Pixel Dungeon (locastan), so each place in Shattered's code is one line.
 * Heaps hit by fire, frost, poison, lightning or a summoning trap count it on any egg lying there. A klik soulorb
 * turns up now and then on a regular floor (the mod mixed it into its potion drops, about one potion in thirty),
 * and an egg in one garden room in ten (the mod only did that in April and May, here it is all year so it can be
 * found). Both only appear when every mod is in the run, like the other mods without worlds.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.pets.KlikSoulorb;
import com.shatteredpixel.shatteredpixeldungeon.items.pets.PetEgg;
import com.shatteredpixel.shatteredpixeldungeon.items.pets.PetEggItem;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.watabou.utils.Random;

public class GoblinsPets {

	public static final int BURN = 0;
	public static final int FREEZE = 1;
	public static final int POISON = 2;
	public static final int LIT = 3;
	public static final int SUMMON = 4;

	/** Chance of a klik soulorb on a regular floor. */
	public static final float SOULORB_CHANCE = 0.04f;
	/** Chance of an egg in a garden room. */
	public static final float EGG_CHANCE = 0.1f;

	public static void heapEffect( Heap heap, int kind ) {
		if (heap == null || heap.type != Heap.Type.HEAP) return;
		for (Item i : heap.items) {
			if (i instanceof PetEggItem) ((PetEggItem) i).count(kind);
		}
	}

	public static void heapAt( int cell, int kind ) {
		if (Dungeon.level == null || com.shatteredpixel.shatteredpixeldungeon.ModsUnlock.locked()) return;
		heapEffect(Dungeon.level.heaps.get(cell), kind);
	}

	private static boolean allowed() {
		return !com.shatteredpixel.shatteredpixeldungeon.ModsUnlock.locked() && Dungeon.branch == 0 && Dungeon.chosenMods.isEmpty() && Dungeon.depth % 5 != 0;
	}

	/** Whether this regular floor gets a klik soulorb. Rolled on its own seed, so the rest of the floor is unchanged. */
	public static boolean rollSoulorb( Level level ) {
		if (!allowed()) return false;
		Random.pushGenerator(Dungeon.seedCurDepth() + 0x60B1L);
		boolean roll = Random.Float() < SOULORB_CHANCE;
		Random.popGenerator();
		return roll;
	}

	public static Item soulorb() {
		return new KlikSoulorb();
	}

	/** Puts an egg in a garden room now and then. Rolled on its own seed. */
	public static void gardenEgg( Level level, Room room ) {
		if (!allowed()) return;
		Random.pushGenerator(Dungeon.seedCurDepth() + 0xE66L + room.left * 31L + room.top);
		if (Random.Float() < EGG_CHANCE) {
			int cell = level.pointToCell(room.random(1));
			if (level.heaps.get(cell) == null) level.drop(new PetEgg(), cell);
		}
		Random.popGenerator();
	}
}
