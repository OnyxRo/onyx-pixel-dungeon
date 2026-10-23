package com.shatteredpixel.shatteredpixeldungeon.items.weapon.thrown;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.watabou.utils.Random;

//Combusted PD's throwable melee weapons in the missile loot table, at a low weight.
//Tiers are matched by top damage, a missile weapon of tier t hits for 5*t at +0.
//Dagger (8), Dirk (12) and Hand Axe (12) sit with tier 2, the Spear (20) with tier 4.
public class CombustedLoot {

	//chance that a missile weapon drop is one of these instead
	public static final float CHANCE = 0.05f;

	public static Item maybeRoll(){
		if (Random.Float() >= CHANCE) return null;
		int floorSet = Math.min(Dungeon.depth / 5, 4);
		//rough share of tier 2 against tier 4 drops per floor set, from Generator.floorSetTierProbs
		float[] t2 = {75, 25, 0, 0, 0};
		float[] t4 = {4, 20, 50, 40, 20};
		if (t2[floorSet] + t4[floorSet] <= 0) return null;
		Item item;
		if (Random.Float(t2[floorSet] + t4[floorSet]) < t2[floorSet]){
			switch (Random.Int(3)){
				case 0: item = new ThrowableDagger(); break;
				case 1: item = new ThrowableDirk(); break;
				default: item = new ThrowableHandAxe(); break;
			}
		} else {
			item = new ThrowableSpear();
		}
		return item.random();
	}
}
