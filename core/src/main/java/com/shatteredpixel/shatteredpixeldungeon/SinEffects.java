/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
 *
 * Matt Edition
 * Copyright (C) 2025-2026 Dum Matt
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.watabou.utils.Random;

//Matt Edition's Seven Sins, the small helpers that Hero, Item, Mob and the level builder call.
//Keeping them here means each hook in the core classes is a single line.
public class SinEffects {

	//Envy, how empty the pack is. Strength gives the bonus to strength, otherwise the speed multiplier.
	public static float envyMultiplier( Hero hero, boolean strength ){
		if (!Dungeon.isSinActive(Sins.ENVY)) return 1f;

		//one more free item every region, two fewer with Desire
		int depthBonus = Dungeon.depth / 5;
		if (Dungeon.isSinActive(Sins.DESIRE)){
			depthBonus -= 2;
		}
		int items = Math.max(0, hero.belongings.totalItemsCount() - depthBonus);

		if (strength){
			if (items <= 16) {
				return Math.min(4, (float)Math.floor((16 - items) / 4f));
			} else {
				return 0f;
			}
		}

		float mult = 1;
		if (items <= 4) {
			float base = 1.0f + (11 - 4) * 0.03f;
			mult *= base + (4 - items) * 0.12f;
		} else if (items <= 11) {
			mult *= 1.0f + (11 - items) * 0.03f;
		} else if (items <= 16) {
			mult *= 1.0f - (items - 11) * 0.03f;
		} else {
			float base = 1.0f - (16 - 11) * 0.03f;
			mult *= base - (items - 16) * 0.06f;
		}

		//never lower than x0.1
		return Math.max(mult, 0.1f);
	}

	//Desire, the starting level of new equipment is rolled higher
	public static int desireRoll( int normal ){
		if (!Dungeon.isSinActive(Sins.DESIRE)) return normal;

		//+0 50%, +1 30%, +2 15%, +3 5%
		int n = 0;
		if (Random.Int(2) == 0) {
			n++;
			if (Random.Int(4) == 0){
				n++;
				if (Random.Int(5) == 0){
					n++;
				}
			}
		}
		return n;
	}

	//Desire, upgradeable items found in the dungeon are often destroyed, more so each region
	public static boolean shouldDesireDestroy( Item item ){
		if (item == null) return false;
		if (!Dungeon.isSinActive(Sins.DESIRE)) return false;
		if (!item.isUpgradable()) return false;

		int region = 1 + (Dungeon.depth - 1) / 5;
		float destroyChance = 0.5f + 0.08f * (region - 1);
		destroyChance = Math.min(destroyChance, 0.95f);

		return Random.Float() < destroyChance;
	}

	//Gluttony, hunger arrives sooner
	public static float gluttonyHungerFactor(){
		return Dungeon.isSinActive(Sins.GLUTTONY) ? 1.9f : 1f;
	}
}
