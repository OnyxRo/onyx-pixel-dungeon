/*
 * Onyx's Pixel Dungeon
 * Copyright (C) 2026 Onyx Rothman
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

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfSteam;
import com.shatteredpixel.shatteredpixeldungeon.items.tools.Scissors;
import com.shatteredpixel.shatteredpixeldungeon.items.tools.Tincturebottle;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfVenom;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.guns.*;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.*;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.CurareDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.GiantShuriken;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.PrimitiveSpear;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Tamahawk;
import com.shatteredpixel.shatteredpixeldungeon.plants.Dreamfoil;
import com.shatteredpixel.shatteredpixeldungeon.plants.Steamweed;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

/**
 * What a floor built in a Lovecraft world adds to its loot, the mod's own weapons, guns, tools and
 * plants. They are not in the normal loot decks, so every other world never rolls them.
 */
public class LovecraftLoot {

	private static class Entry {
		final Class<? extends Item> item;
		final float weight;
		final int minDepth;
		final int maxDepth;
		Entry( Class<? extends Item> item, float weight, int minDepth, int maxDepth ){
			this.item = item;
			this.weight = weight;
			this.minDepth = minDepth;
			this.maxDepth = maxDepth;
		}
	}

	private static final Entry[] POOL = {
			//melee weapons, by the tier they sit in
			new Entry( Axe.class, 2, 1, 9 ),
			new Entry( HeroicShortsword.class, 2, 1, 9 ),
			new Entry( Knuckles.class, 2, 1, 9 ),
			new Entry( KnifeGlove.class, 2, 3, 14 ),
			new Entry( Kusarigama.class, 2, 6, 19 ),
			new Entry( Scythe.class, 2, 6, 19 ),
			new Entry( SpikedGreatsword.class, 2, 11, 24 ),
			new Entry( Halberd.class, 2, 16, 24 ),
			//guns
			new Entry( Bow.class, 2, 1, 12 ),
			new Entry( Pistol.class, 2, 2, 12 ),
			new Entry( Luger.class, 2, 4, 16 ),
			new Entry( Rifle.class, 1, 8, 20 ),
			new Entry( Shotgun.class, 1, 8, 20 ),
			new Entry( FastFirePistol.class, 1, 10, 24 ),
			new Entry( Harpoon.class, 1, 6, 20 ),
			//thrown weapons
			new Entry( CurareDart.class, 2, 2, 14 ),
			new Entry( GiantShuriken.class, 1, 8, 24 ),
			new Entry( PrimitiveSpear.class, 2, 1, 9 ),
			new Entry( Tamahawk.class, 1, 6, 19 ),
			//bombs, tools and consumables
			new Entry( Tnt.class, 2, 3, 24 ),
			new Entry( TntBundle.class, 1, 8, 24 ),
			new Entry( WandOfVenom.class, 1, 3, 24 ),
			new Entry( PotionOfMight.class, 1, 5, 24 ),
			new Entry( PotionOfSteam.class, 2, 2, 24 ),
			new Entry( Scissors.class, 1, 2, 24 ),
			new Entry( Tincturebottle.class, 1, 2, 24 ),
			new Entry( Dreamfoil.Seed.class, 2, 1, 24 ),
			new Entry( Steamweed.Seed.class, 2, 1, 24 ),
	};

	/** One random item that suits the depth, or null if nothing does. */
	public static Item random( int depth ){
		float total = 0;
		for (Entry e : POOL) if (depth >= e.minDepth && depth <= e.maxDepth) total += e.weight;
		if (total <= 0) return null;
		float roll = Random.Float( total );
		for (Entry e : POOL) {
			if (depth < e.minDepth || depth > e.maxDepth) continue;
			roll -= e.weight;
			if (roll < 0) return Reflection.newInstance( e.item ).random();
		}
		return null;
	}

	/** Half of the floors in a Lovecraft world get one of the mod's items lying around. */
	public static Item forFloor(){
		return Random.Int( 2 ) == 0 ? random( Dungeon.depth ) : null;
	}
}
