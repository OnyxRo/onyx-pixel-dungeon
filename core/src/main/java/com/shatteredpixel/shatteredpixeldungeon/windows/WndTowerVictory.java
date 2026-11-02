/*
 * Onyx's Pixel Dungeon
 * Copyright (C) 2026 Onyx Rothman
 *
 * Based on Shattered Pixel Dungeon, Copyright (C) 2014-2026 Evan Debenham
 * and Pixel Dungeon, Copyright (C) 2012-2015 Oleg Dolya
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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.TowerDefense;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.towerlist.TowerIcons;

/**
 * Shown when a tower defense arena is won. Tower PD ended the run and went back to its level select here. This
 * window offers the next arena (a fresh run with the same hero class, as in the mod), the title screen, or staying
 * in the cleared arena. It also shows the run's tower numbers, which the mod kept in its statistics.
 */
public class WndTowerVictory extends WndOptions {

	public WndTowerVictory() {
		super( TowerIcons.AMULET.get(), Messages.get( WndTowerVictory.class, "title" ), message(), options() );
	}

	private static String message(){
		int waves = Dungeon.level instanceof Arena ? Dungeon.level.wave : 0;
		String msg = Messages.get( WndTowerVictory.class, "stats", waves, Statistics.towersbuilt,
				Statistics.notwallsdestroyed + Statistics.wallsdestroyed, Statistics.enemiesSlain, TowerDefense.wins() );
		if (TowerDefense.hasNext()) msg += "\n\n" + Messages.get( WndTowerVictory.class, "next_open" );
		return msg;
	}

	private static String[] options(){
		if (TowerDefense.hasNext()) {
			return new String[]{ Messages.get( WndTowerVictory.class, "next" ), Messages.get( WndTowerVictory.class, "title_screen" ), Messages.get( WndTowerVictory.class, "stay" ) };
		}
		return new String[]{ Messages.get( WndTowerVictory.class, "title_screen" ), Messages.get( WndTowerVictory.class, "stay" ) };
	}

	@Override
	protected void onSelect( int index ) {
		if (!TowerDefense.hasNext()) index++;
		if (index == 0) TowerDefense.startNext();
		else if (index == 1) TowerDefense.quit();
	}
}
