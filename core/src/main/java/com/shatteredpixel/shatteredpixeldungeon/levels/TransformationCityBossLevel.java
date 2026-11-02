/*
 * Onyx's Pixel Dungeon
 * Transformation PD (dongyusangyu) leaves its shop owner on the Dwarf King's throne once the King is beaten.
 * A hero who comes back up with the Amulet can challenge him, and he turns into the Great Shopper.
 * This floor is the city boss floor with that shop owner added. In a sandbox run he fights without the Amulet.
 * Sandbox only until Onyx plays it.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.ShopBoss;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;

public class TransformationCityBossLevel extends CityBossLevel {

	private boolean shopOwnerPlaced = false;

	@Override
	public void unseal() {
		super.unseal();
		if (shopOwnerPlaced) return;
		shopOwnerPlaced = true;
		placeAt(new ShopBoss(), throne, this);
	}

	/** Puts a character on a cell, moving whoever stands there to a free neighbour. */
	static void placeAt( Char ch, int cell, Level level ) {
		Char there = Actor.findChar(cell);
		if (there != null) {
			for (int n : PathFinder.NEIGHBOURS8) {
				int c = cell + n;
				if (level.passable[c] && Actor.findChar(c) == null) {
					there.pos = c;
					if (there.sprite != null) there.sprite.place(c);
					break;
				}
			}
		}
		ch.pos = cell;
		if (level == com.shatteredpixel.shatteredpixeldungeon.Dungeon.level) {
			GameScene.add((com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob) ch);
		} else {
			level.mobs.add((com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob) ch);
		}
	}

	private static final String SHOP_OWNER = "shop_owner_placed";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put(SHOP_OWNER, shopOwnerPlaced);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		shopOwnerPlaced = bundle.getBoolean(SHOP_OWNER);
	}
}
