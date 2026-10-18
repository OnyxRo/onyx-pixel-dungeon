/*
 * Onyx's Pixel Dungeon
 * Based on Shattered Pixel Dungeon, Copyright (C) 2014-2025 Evan Debenham
 * Transformation PD's tower boss arenas, used as the boss floors of a normal run.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.levels.towers;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.WorldShuffle;

/**
 * Transformation PD only meets its tower bosses inside its tower. In a normal run a boss floor built after
 * one of its worlds gets a tower boss arena at floors 10, 15, 20 and 25 instead, wired to the ordinary
 * stairs. The boss is picked by the floor (Gentleman Elf, Pestilence Knight, Hunger Knight, Death Knight)
 * so every run meets them in the same order.
 */
public class TransformationRunBossLevel extends TowerBossLevel {

	{
		transitionBranch = 0;
		TowerBossGenerator.forcedId = bossFor( Dungeon.depth );
	}

	public static String bossFor( int depth ){
		switch (depth) {
			case 10: return TowerBossGenerator.GENTLEMAN_ELF_ID;
			case 15: return TowerBossGenerator.PESTILENCE_KNIGHT_ID;
			case 20: return TowerBossGenerator.HUNGER_KNIGHT_ID;
			default: return TowerBossGenerator.DEATH_KNIGHT_ID;
		}
	}

	@Override
	public void unseal() {
		super.unseal();
		//the tower bosses give no mask or crown, so the run's progression items come from here
		WorldShuffle.dropProgressionItem( this );
	}
}
