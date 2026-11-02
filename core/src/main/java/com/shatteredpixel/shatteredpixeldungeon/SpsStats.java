/*
 * Onyx's Pixel Dungeon
 * Based on Shattered Pixel Dungeon, Copyright (C) 2014-2025 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.watabou.utils.Bundle;
import com.watabou.utils.Reflection;

/**
 * Kill counters SPS (SPecial Surprise PD) kept in its Statistics for its challenge floor monsters. Each kill
 * makes the next one of that kind a little stronger. Saved with the run through Statistics.
 */
public class SpsStats {

	public static int archersKilled;
	public static int skeletonsKilled;
	public static int albinoPiranhasKilled;
	public static int goldThievesKilled;
	public static int assassinsKilled;

	public static void reset(){
		archersKilled = skeletonsKilled = albinoPiranhasKilled = goldThievesKilled = assassinsKilled = 0;
	}

	public static void store( Bundle bundle ){
		bundle.put( "sps_archers", archersKilled );
		bundle.put( "sps_skeletons", skeletonsKilled );
		bundle.put( "sps_albinopiranhas", albinoPiranhasKilled );
		bundle.put( "sps_goldthieves", goldThievesKilled );
		bundle.put( "sps_assassins", assassinsKilled );
	}

	public static void restore( Bundle bundle ){
		archersKilled = bundle.getInt( "sps_archers" );
		skeletonsKilled = bundle.getInt( "sps_skeletons" );
		albinoPiranhasKilled = bundle.getInt( "sps_albinopiranhas" );
		goldThievesKilled = bundle.getInt( "sps_goldthieves" );
		assassinsKilled = bundle.getInt( "sps_assassins" );
	}

	/** SPS's Item.copy(class), a fresh unidentified item of the same kind, used by the Monster Box. */
	public static Item copyOf( Class<? extends Item> cls ){
		return Reflection.newInstance( cls );
	}
}
