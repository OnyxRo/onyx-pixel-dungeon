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

import com.shatteredpixel.shatteredpixeldungeon.BossCrawl;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.HeroSelectScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;

/**
 * Asked after the hero is picked for a boss crawl. Easy goes through a quarter of each floor's bosses,
 * medium half, hard three quarters and insane all of them. Which ones is picked by the run's seed.
 */
public class WndCrawlDifficulty extends WndOptions {

	private static final String[] LEVELS = { "easy", "medium", "hard", "insane" };

	public WndCrawlDifficulty() {
		super( Icons.get( Icons.SKULL ), Messages.get( WndCrawlDifficulty.class, "title" ),
				Messages.get( WndCrawlDifficulty.class, "desc" ), options() );
	}

	private static String[] options(){
		String[] opts = new String[LEVELS.length];
		for (int i = 0; i < LEVELS.length; i++) {
			opts[i] = Messages.get( WndCrawlDifficulty.class, LEVELS[i] );
		}
		return opts;
	}

	@Override
	protected void onSelect( int index ) {
		BossCrawl.difficulty = index + 1;
		BossCrawl.step = 0;
		HeroSelectScene.startRun();
	}
}
