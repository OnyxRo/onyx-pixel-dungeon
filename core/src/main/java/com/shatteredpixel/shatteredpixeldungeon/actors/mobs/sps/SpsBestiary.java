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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.sps;

import com.watabou.utils.PathFinder;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.*;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

/**
 * SPS's Bestiary.mob(84), (85) and (86), the mixed lists some of its monsters summon from (the Dragon Rider's dragon
 * and the Ice Ball's shards). Every entry has the same weight in the mod. Shattered's own monsters stand in for
 * SPS's versions of them.
 */
public class SpsBestiary {

	private static final Class<?>[] MIX84 = {
		Rat.class, BrownBat.class, DustElement.class, LiveMoss.class,
		Swarm.class, Crab.class, PatrolUAV.class,
		Thief.class, Gnoll.class, TimeKeeper.class,
		Guard.class, Assassin.class, TrollWarrior.class, Zombie.class,
		Bat.class, Skeleton.class, Brute.class,
		GnollShaman.class, Spinner.class, BrokenRobot.class, SandMob.class,
		FireElemental.class, Warlock.class, Monk.class,
		Golem.class, SpiderBot.class, Musketeer.class, DwarfLich.class,
		BambooMob.class, Greatmoss.class, Piranha.class, DragonRider.class
	};

	private static final Class<?>[] MIX85 = {
		Rat.class, BrownBat.class, DustElement.class, LiveMoss.class,
		Swarm.class, Crab.class, PatrolUAV.class,
		Thief.class, Gnoll.class, BambooMob.class,
		Guard.class, Assassin.class, TrollWarrior.class, Zombie.class,
		Bat.class, Skeleton.class, Brute.class, TimeKeeper.class,
		GnollShaman.class, Spinner.class, BrokenRobot.class, SandMob.class, IceBug.class,
		FireElemental.class, Warlock.class, Monk.class, DragonRider.class,
		Golem.class, SpiderBot.class, Musketeer.class, DwarfLich.class,
		Succubus.class, Eye.class, DemonGoo.class,
		Scorpio.class, ThiefImp.class, DemonFlower.class, Sufferer.class,
		BlueWraith.class,
		Orc.class, FlyingProtector.class,
		GoldOrc.class, Fiend.class
	};

	private static final Class<?>[] MIX86 = {
		Rat.class, BrownBat.class, DustElement.class, LiveMoss.class,
		Swarm.class, Crab.class, PatrolUAV.class,
		Thief.class, Gnoll.class, BambooMob.class,
		Guard.class, Assassin.class, TrollWarrior.class, Zombie.class,
		Bat.class, Skeleton.class, Brute.class, TimeKeeper.class,
		GnollShaman.class, Spinner.class, BrokenRobot.class, SandMob.class, IceBug.class,
		FireElemental.class, Warlock.class, Monk.class, DragonRider.class,
		Golem.class, SpiderBot.class, Musketeer.class, DwarfLich.class,
		Succubus.class, Eye.class, DemonGoo.class,
		Scorpio.class, ThiefImp.class, DemonFlower.class, Sufferer.class,
		BlueWraith.class, GnollArcher.class, ForestProtector.class, MossySkeleton.class, GraveProtector.class,
		FishProtector.class, GoldThief.class, VaultProtector.class,
		Orc.class, FlyingProtector.class, MonsterBox.class,
		Fiend.class
	};

	/** SPS's Rat.spawnAround, a rat in each free cell beside the cell, used by the Rat Boss when it notices the hero. */
	public static void ratsAround( int pos ){
		for (int n : com.watabou.utils.PathFinder.NEIGHBOURS4) {
			int cell = pos + n;
			if (com.shatteredpixel.shatteredpixeldungeon.Dungeon.level.passable[cell]
					&& com.shatteredpixel.shatteredpixeldungeon.actors.Actor.findChar( cell ) == null) {
				Rat rat = new Rat();
				rat.pos = cell;
				rat.state = rat.HUNTING;
				com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene.add( rat, 1f );
			}
		}
	}

	public static Mob mob( int list ){
		Class<?>[] pick = list == 84 ? MIX84 : list == 85 ? MIX85 : MIX86;
		return (Mob) Reflection.newInstance( Random.element( pick ) );
	}
}
