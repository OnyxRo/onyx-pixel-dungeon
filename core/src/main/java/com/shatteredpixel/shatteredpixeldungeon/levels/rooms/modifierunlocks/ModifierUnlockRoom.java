/*
 * Onyx's Pixel Dungeon
 * Based on Shattered Pixel Dungeon, Copyright (C) 2014-2025 Evan Debenham
 * Modifier unlock rooms ported from Pixel Dungeon Reforged by nathanlink169
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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.modifierunlocks;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.watabou.utils.Bundle;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Arrays;

//Pixel Dungeon Reforged. Each run, if any modifier (challenge) is still locked, one of four trial rooms can appear
//on a floor picked from the seed. At the end of the trial is a Challenge Cup that unlocks one modifier for good.
public abstract class ModifierUnlockRoom extends Room {

	private static final ArrayList<Class<? extends ModifierUnlockRoom>> ALL_SECRETS = new ArrayList<>( Arrays.asList(
			LevitationModifierUnlockRoom.class, BarrierModifierUnlockRoom.class,
			SummoningModifierUnlockRoom.class, PressurePlateModifierUnlockRoom.class ));

	private static int m_DepthToSpawn = -1;
	public static int GetDepth() { return m_DepthToSpawn; }

	public static void initForRun() {
		//sandbox runs keep every modifier unlocked, there is nothing to find. Normal runs only get a room
		//while something is still locked. The floor comes from its own generator, so the same seed always
		//picks the same floor and the rest of the dungeon is not shifted by it.
		//-Donyx.notrial switches the rooms off, for telling level generation bugs apart in the headless tests
		if (System.getProperty("onyx.notrial") != null || Dungeon.sandbox || !Challenges.HasAnyChallengesToUnlock()) {
			m_DepthToSpawn = -1;
			return;
		}
		Random.pushGenerator( Dungeon.seed + 1234567L );
		do {
			m_DepthToSpawn = Random.Int( 25 );
		} while (m_DepthToSpawn % 5 == 0);
		Random.popGenerator();
	}

	@Override
	public int maxConnections(int direction) {
		return 1;
	}

	public static ModifierUnlockRoom createRoom(){
		return Reflection.newInstance(ALL_SECRETS.get(Random.Int(ALL_SECRETS.size())));
	}

	private static final String DEPTH_TAG = "modifier_unlock_room_depth";

	public static void restoreRoomsFromBundle( Bundle bundle ) {
		if (bundle.contains( DEPTH_TAG )) {
			m_DepthToSpawn = bundle.getInt(DEPTH_TAG);
		} else {
			m_DepthToSpawn = -1;
		}
	}

	public static void storeRoomsInBundle( Bundle bundle ) {
		bundle.put(DEPTH_TAG, m_DepthToSpawn);
	}

	@Override
	public boolean canPlaceWater(Point p){
		return false;
	}

	@Override
	public boolean canPlaceGrass(Point p){
		return false;
	}

	@Override
	public boolean canPlaceTrap(Point p){
		return false;
	}

	@Override
	public boolean canPlaceItem(Point p, Level l){
		return false;
	}

	@Override
	public boolean canPlaceCharacter(Point p, Level l){
		return false;
	}

}
