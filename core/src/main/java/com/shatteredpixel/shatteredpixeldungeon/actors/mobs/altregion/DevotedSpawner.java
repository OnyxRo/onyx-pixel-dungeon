/*
 * Onyx's Pixel Dungeon
 * Copyright (C) 2026 Onyx Rothman
 *
 * Based on Shattered Pixel Dungeon, Copyright (C) 2014-2026 Evan Debenham
 * and Devoted Pixel Dungeon by Arackulele
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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.altregion;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Crab;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Gnoll;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.RatSkull;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * The monsters Devoted Pixel Dungeon spawns in its own regions, floor by floor, taken from
 * that mod's MobSpawner. Used for floors built in one of Devoted's worlds.
 */
public class DevotedSpawner {

	public static ArrayList<Class<? extends Mob>> getMobRotation( int depth ){
		ArrayList<Class<? extends Mob>> mobs = standardMobRotation( depth );
		swapMobAlts( mobs );
		Random.shuffle( mobs );
		return mobs;
	}

	private static ArrayList<Class<? extends Mob>> standardMobRotation( int depth ){
		switch (depth) {

			//Gardens
			case 1: default:
				return new ArrayList<>(Arrays.asList(
						Toad.class, Toad.class, Toad.class,
						Grub.class));
			case 2:
				return new ArrayList<>(Arrays.asList(
						Toad.class, Toad.class, Grub.class,
						Gnoll.class, Gnoll.class));
			case 3:
				return new ArrayList<>(Arrays.asList(
						Toad.class, Grub.class, Butterfly.class,
						Gnoll.class, Gnoll.class, Druid.class));
			case 4: case 5:
				return new ArrayList<>(Arrays.asList(
						Druid.class, Druid.class, Butterfly.class,
						Gnoll.class, ThornLasher.class, ThornLasher.class));

			//Coldhouse
			case 6:
				return new ArrayList<>(Arrays.asList(
						ColdCorpse.class, ColdCorpse.class, ColdCorpse.class,
						MeatRack.class,
						Crab.class));
			case 7:
				return new ArrayList<>(Arrays.asList(
						ColdCorpse.class, ColdCorpse.class, ColdCorpse.class,
						MeatRack.class,
						Banshee.class,
						GlassKnight.class));
			case 8:
				return new ArrayList<>(Arrays.asList(
						ColdCorpse.class, ColdCorpse.class,
						MeatRack.class,
						Banshee.class, Banshee.class,
						GlassKnight.class, GlassKnight.class,
						Wererat.class));
			case 9: case 10:
				return new ArrayList<>(Arrays.asList(
						ColdCorpse.class,
						MeatRack.class,
						Banshee.class, Banshee.class,
						GlassKnight.class, GlassKnight.class,
						Wererat.class, Wererat.class));

			//Forge
			case 11:
				return new ArrayList<>(Arrays.asList(
						GraniteTroll.class, GraniteTroll.class, GraniteTroll.class,
						TrollRanger.class));
			case 12:
				return new ArrayList<>(Arrays.asList(
						GraniteTroll.class, GraniteTroll.class,
						TrollRanger.class,
						Mole.class));
			case 13:
				return new ArrayList<>(Arrays.asList(
						GraniteTroll.class,
						TrollRanger.class, TrollRanger.class,
						Mole.class, Mole.class,
						RockGolem.class));
			case 14: case 15:
				return new ArrayList<>(Arrays.asList(
						GraniteTroll.class,
						TrollRanger.class, TrollRanger.class,
						Mole.class, Mole.class,
						RockGolem.class, RockGolem.class));

			//Citadel
			case 16:
				return new ArrayList<>(Arrays.asList(
						Cultist.class, Cultist.class, Cultist.class,
						Giant.class,
						Leader.class));
			case 17:
				return new ArrayList<>(Arrays.asList(
						Cultist.class,
						Giant.class, Giant.class,
						Leader.class,
						Trapper.class));
			case 18:
				return new ArrayList<>(Arrays.asList(
						Cultist.class,
						Giant.class,
						Leader.class, Leader.class,
						Trapper.class, Trapper.class,
						Warden.class));
			case 19: case 20:
				return new ArrayList<>(Arrays.asList(
						Cultist.class,
						Leader.class, Leader.class,
						Trapper.class, Trapper.class,
						Warden.class, Warden.class, Warden.class));

			//Ashen Void
			case 21:
				return new ArrayList<>(Arrays.asList(
						Puppet.class));
			case 22:
				return new ArrayList<>(Arrays.asList(
						Sacrifice.class,
						BedrockGolem.class,
						Puppet.class));
			case 23:
				return new ArrayList<>(Arrays.asList(
						Sacrifice.class,
						BedrockGolem.class, BedrockGolem.class,
						Puppet.class));
			case 24: case 25: case 26:
				return new ArrayList<>(Arrays.asList(
						Sacrifice.class,
						BedrockGolem.class, BedrockGolem.class,
						Puppet.class));
		}
	}

	//switches out regular mobs for their rare versions, at the same odds as Shattered
	private static void swapMobAlts( ArrayList<Class<? extends Mob>> rotation ){
		float altChance = 1 / 50f * RatSkull.exoticChanceMultiplier();
		for (int i = 0; i < rotation.size(); i++) {
			if (Random.Float() < altChance) {
				Class<? extends Mob> alt = RARE_ALTS.get( rotation.get(i) );
				if (alt != null) {
					rotation.set( i, alt );
				}
			}
		}
	}

	private static final HashMap<Class<? extends Mob>, Class<? extends Mob>> RARE_ALTS = new HashMap<>();
	static {
		RARE_ALTS.put( Toad.class,         PoisonToad.class );
		RARE_ALTS.put( Grub.class,         RoyalGrub.class );
		RARE_ALTS.put( MeatRack.class,     PrimeRib.class );
		RARE_ALTS.put( Banshee.class,      Revenant.class );
		RARE_ALTS.put( GraniteTroll.class, GraniteElder.class );
		RARE_ALTS.put( Mole.class,         FlamingMole.class );
		RARE_ALTS.put( Leader.class,       Conjurer.class );
	}

}
