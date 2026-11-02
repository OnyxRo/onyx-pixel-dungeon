/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ModsUnlock;
import com.shatteredpixel.shatteredpixeldungeon.levels.WorldShuffle;
import com.shatteredpixel.shatteredpixeldungeon.levels.worlds.PollutedSewersLevel;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.NoosaScript;
import com.watabou.noosa.NoosaScriptNoLighting;
import com.watabou.noosa.SkinnedBlock;

/**
 * Ryuse's PD loading screen. The mod drew loading screens as a scrolling 16x16 tile, and its only new
 * one was for its Polluted Sewers. Shattered now draws a large picture per region instead, so the tile
 * covers that picture only when the floor being loaded is built in the Polluted Sewers.
 */
public class RyuseLoading {

	public static final String TILE = "interfaces/ryuse/loading_polluted_sewers.png";

	//called by InterlevelScene once its background picture is in place
	public static void decorate( InterlevelScene scene, InterlevelScene.Mode mode, int depth ){
		if (!loadsPolluted( mode, depth )) return;

		final float scrollSpeed;
		switch (mode){
			case DESCEND: scrollSpeed = 5; break;
			case FALL:    scrollSpeed = 50; break;
			case ASCEND:  scrollSpeed = -5; break;
			case RETURN: case SANDBOX:
				scrollSpeed = depth > Dungeon.depth ? 15 : -15; break;
			default:      scrollSpeed = 0;
		}

		SkinnedBlock bg = new SkinnedBlock( Camera.main.width, Camera.main.height, TILE ){
			@Override
			protected NoosaScript script() {
				return NoosaScriptNoLighting.get();
			}

			@Override
			public void draw() {
				Blending.disable();
				super.draw();
				Blending.enable();
			}

			@Override
			public void update() {
				super.update();
				offset( 0, Game.elapsed * scrollSpeed );
			}
		};
		bg.scale( 4, 4 );
		bg.autoAdjust = true;
		scene.add( bg );
	}

	//true when the next floor is (or, for a floor not built yet, will be) the Polluted Sewers
	static boolean loadsPolluted( InterlevelScene.Mode mode, int depth ){
		if (ModsUnlock.locked() || mode == null) return false;
		switch (mode){
			case CONTINUE: case NONE: case RESURRECT:
				return false;
			case RESET:
				return Dungeon.level instanceof PollutedSewersLevel;
			default:
		}
		int branch = InterlevelScene.curTransition != null && (mode == InterlevelScene.Mode.DESCEND || mode == InterlevelScene.Mode.ASCEND)
				? InterlevelScene.curTransition.destBranch : Dungeon.branch;
		if (branch != 0 || Dungeon.bossCrawl) return false;

		int polluted = pollutedWorld();
		if (polluted < 0) return false;
		if (Dungeon.sandbox){
			int forced = Dungeon.sandboxOnceWorld != -1 ? Dungeon.sandboxOnceWorld : Dungeon.sandboxWorld;
			if (forced != -1 && WorldShuffle.canForce( depth )) return forced == polluted;
		}
		return WorldShuffle.shuffles( depth ) && WorldShuffle.bossWorldFor( depth ) == -1
				&& WorldShuffle.worldFor( depth ) == polluted;
	}

	private static int pollutedWorld(){
		String name = PollutedSewersLevel.class.getName();
		for (int w = 0; w < WorldShuffle.worldCount(); w++){
			if (name.equals( WorldShuffle.worldClassName( w ) )) return w;
		}
		return -1;
	}
}
