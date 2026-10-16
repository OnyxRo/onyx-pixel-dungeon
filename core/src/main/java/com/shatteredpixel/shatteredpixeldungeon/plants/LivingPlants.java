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

package com.shatteredpixel.shatteredpixeldungeon.plants;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.livingplants.*;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfRegrowth;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.WorldShuffle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Lovecraft PD, where half of the plants a hero steps on or throws a seed at wake up as a living
 * plant monster instead of doing their usual effect. Only floors built in a Lovecraft world do this.
 */
public class LivingPlants {

	private static final HashMap<Class<? extends Plant>, Class<? extends LivingPlant>> AWAKE = new HashMap<>();
	static {
		AWAKE.put( Blindweed.class, LivingPlantBlindWeed.class );
		AWAKE.put( Dreamfoil.class, LivingPlantDreamFoil.class );
		AWAKE.put( Earthroot.class, LivingPlantEarthRoot.class );
		AWAKE.put( Fadeleaf.class, LivingPlantFadeLeaf.class );
		AWAKE.put( Firebloom.class, LivingPlantFireBloom.class );
		AWAKE.put( Icecap.class, LivingPlantIceCap.class );
		AWAKE.put( Sorrowmoss.class, LivingPlantSorrowMoss.class );
		AWAKE.put( Starflower.class, LivingPlantStarFlower.class );
		AWAKE.put( Steamweed.class, LivingPlantSteamweed.class );
		AWAKE.put( Stormvine.class, LivingPlantStormVine.class );
		AWAKE.put( Sungrass.class, LivingPlantSunGrass.class );
		AWAKE.put( WandOfRegrowth.Dewcatcher.class, LivingPlantDewCatcher.class );
		AWAKE.put( WandOfRegrowth.Seedpod.class, LivingPlantSeedPod.class );
	}

	/** True when the plant woke up as a monster, so its usual effect should not happen. */
	public static boolean awaken( Plant plant ){
		if (Dungeon.level == null || !"lovecraft".equals( WorldShuffle.modKey( Dungeon.level ) )) return false;
		Class<? extends LivingPlant> cls = AWAKE.get( plant.getClass() );
		if (cls == null || Random.Int( 2 ) != 0) return false;

		ArrayList<Integer> spawnPoints = new ArrayList<>();
		for (int i : PathFinder.NEIGHBOURS8) {
			int p = plant.pos + i;
			if (Actor.findChar( p ) == null && (Dungeon.level.passable[p] || Dungeon.level.avoid[p]) && !Dungeon.level.pit[p]) {
				spawnPoints.add( p );
			}
		}
		if (spawnPoints.isEmpty()) return false;

		Mob living = Reflection.newInstance( cls );
		living.pos = Random.element( spawnPoints );
		GameScene.add( living );
		Actor.addDelayed( new Pushing( living, plant.pos, living.pos ), -1 );

		if (Dungeon.level.heroFOV[plant.pos]) CellEmitter.get( plant.pos ).burst( Speck.factory( Speck.LIGHT ), 4 );
		if (Dungeon.level.heroFOV[living.pos]) CellEmitter.get( living.pos ).burst( Speck.factory( Speck.LIGHT ), 4 );
		int m = Dungeon.level.map[living.pos];
		if (m == Terrain.EMBERS || m == Terrain.EMPTY_DECO || m == Terrain.EMPTY || m == Terrain.HIGH_GRASS) {
			Dungeon.level.map[living.pos] = Terrain.GRASS;
		}
		return true;
	}
}
