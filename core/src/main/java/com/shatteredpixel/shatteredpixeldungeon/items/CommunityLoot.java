/*
 * Onyx's Pixel Dungeon
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

import com.shatteredpixel.shatteredpixeldungeon.items.Generator.Category;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.NecklaceOfIce;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.BrokenTreasureBox;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.AlchemicalAkrafena;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.CommunityWakizashi;

//Community PD's items in the loot decks, with the weights the mod's own Generator gave them.
//The two tier 4 weapons weigh 2 like the others, the Necklace of Ice is a normal artifact and the
//Broken Treasure Box a normal trinket. Called from Generator after the other mods' loot.
public class CommunityLoot {

	private static void add( Category cat, Class<?>[] classes, float[] probs ){
		int n = cat.classes == null ? 0 : cat.classes.length;
		Class<?>[] c = new Class<?>[n + classes.length];
		if (n > 0) System.arraycopy(cat.classes, 0, c, 0, n);
		System.arraycopy(classes, 0, c, n, classes.length);
		cat.classes = c;
		float[] base = cat.defaultProbs != null ? cat.defaultProbs : (cat.probs != null ? cat.probs : new float[n]);
		float[] p = new float[n + probs.length];
		System.arraycopy(base, 0, p, 0, Math.min(n, base.length));
		System.arraycopy(probs, 0, p, n, probs.length);
		if (cat.defaultProbs != null) cat.defaultProbs = p;
		if (cat.defaultProbs2 != null) {
			float[] p2 = new float[n + probs.length];
			System.arraycopy(cat.defaultProbs2, 0, p2, 0, Math.min(n, cat.defaultProbs2.length));
			System.arraycopy(probs, 0, p2, n, probs.length);
			cat.defaultProbs2 = p2;
		}
		cat.probs = p.clone();
	}

	public static void apply(){
		add(Category.WEP_T4, new Class<?>[]{ AlchemicalAkrafena.class, CommunityWakizashi.class }, new float[]{ 2, 2 });
		add(Category.ARTIFACT, new Class<?>[]{ NecklaceOfIce.class }, new float[]{ 1 });
		add(Category.TRINKET, new Class<?>[]{ BrokenTreasureBox.class }, new float[]{ 1 });
	}
}
