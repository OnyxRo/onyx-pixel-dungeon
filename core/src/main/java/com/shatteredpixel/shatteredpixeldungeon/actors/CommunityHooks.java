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

package com.shatteredpixel.shatteredpixeldungeon.actors;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.NecklaceOfIce;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.BrokenTreasureBox;
import com.watabou.utils.Random;

//Community PD's hooks into Shattered's classes, one line at each call site.
public class CommunityHooks {

	//an uncursed Necklace of Ice keeps its wearer from being slowed by chill
	public static boolean necklaceWarm( Char ch ){
		if (ch == null) return false;
		NecklaceOfIce.necklaceRecharge n = ch.buff(NecklaceOfIce.necklaceRecharge.class);
		return n != null && !n.isCursed();
	}

	//while chilled, the necklace's wearer blocks some damage
	public static int necklaceBlock( Char ch, int damage ){
		if (ch == null || damage <= 0) return damage;
		if (ch.buff(NecklaceOfIce.necklaceRecharge.class) != null && ch.buff(Chill.class) != null){
			damage -= Math.min(damage, necklaceBlocking());
		}
		return damage;
	}

	public static int necklaceBlocking(){
		return (Dungeon.scalingDepth() + 5) / 3;
	}

	//the necklace levels up from experience
	public static void onHeroExp( Char hero, int exp ){
		NecklaceOfIce.necklaceRecharge n = hero.buff(NecklaceOfIce.necklaceRecharge.class);
		if (n != null) n.gainExp(exp);
	}

	//extra special rooms from the Broken Treasure Box, 0 without it
	public static int extraSpecialRooms(){
		float boost = BrokenTreasureBox.extraSpecialRoomChance();
		if (boost <= 0) return 0;
		int extra = 0;
		if (boost >= 1f){
			boost -= 1f;
			extra++;
		}
		if (Random.Float() < boost){
			extra++;
		}
		return extra;
	}
}
