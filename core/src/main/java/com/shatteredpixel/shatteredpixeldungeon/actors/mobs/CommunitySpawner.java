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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.RatSkull;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.watabou.utils.Random;

import java.util.ArrayList;

/**
 * Community PD has no worlds of its own. Its monsters were made for Shattered's floors, so they join
 * the rotation of any floor, the way the mod's MobSpawner did it.
 *
 * Wind Spirit, Water Strider, Angry Thorn and DM-XXX belong to the chasm, water, grass and trap level
 * feelings. Two entries of the matching one go into that floor's rotation. The Failed Defender is a
 * rare stand in for a prison guard (1 in 50, more with the rat skull), and the Poison Elemental takes
 * half of the shock elementals' places.
 *
 * Only in branch 0 and only when the run uses every mod (an empty mods pick), so a run that picks
 * certain mods sees only those.
 */
public class CommunitySpawner {

	public static boolean active(){
		return Dungeon.branch == 0 && Dungeon.chosenMods.isEmpty();
	}

	public static void adjust( ArrayList<Class<? extends Mob>> rotation, Level level ){
		if (rotation == null || rotation.isEmpty() || level == null || !active()) return;

		float altChance = 1 / 50f * RatSkull.exoticChanceMultiplier();
		for (int i = 0; i < rotation.size(); i++) {
			Class<? extends Mob> cl = rotation.get(i);
			if (cl == Guard.class && Random.Float() < altChance) {
				rotation.set(i, FailedDefender.class);
			} else if (cl == Elemental.ShockElemental.class && Random.Int(2) == 0) {
				rotation.set(i, CommunityPoisonElemental.class);
			}
		}

		Class<? extends Mob> cl = null;
		if (level.feeling == Level.Feeling.WATER) {
			cl = WaterStrider.class;
		} else if (level.feeling == Level.Feeling.CHASM) {
			cl = WindSpirit.class;
		} else if (level.feeling == Level.Feeling.TRAPS) {
			cl = DMXXX.class;
		} else if (level.feeling == Level.Feeling.GRASS) {
			cl = AngryThorn.class;
		}
		if (cl != null) {
			//two entries, as in the mod, to raise the odds
			rotation.set(Random.index(rotation), cl);
			rotation.add(cl);
		}
	}
}
