/*
 * Onyx's Pixel Dungeon
 *
 * The boss arena from Deep Pixel Dungeon (https://github.com/Mochiee/deep-pixel-dungeon), where its
 * ForestBossLevel, GnollBossLevel and CavesAnnex all used the same hard coded map. Deep wrote the three
 * but never linked them into its dungeon, so here they guard floor 5 (Elder Shroom), floor 10 (Shaman
 * Leader) and the Caves Annex special floor (Terminator).
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

package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.YellowMushroom;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.ElderShroom;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.ShamanLeader;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.Terminator;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public abstract class DeepBossLevel extends Level {

	{
		color1 = 0x48763c;
		color2 = 0x59994a;
	}

	private static final String STARTED = "started";
	private boolean started = false;

	/** The boss of this arena. */
	protected abstract Mob makeBoss();

	@Override
	public String waterTex() {
		return "environment/worlds/water_deep_forest.png";
	}

	@Override
	protected boolean build() {
		setSize(32, 32);
		map = MAP.clone();
		buildFlagMaps();
		cleanWalls();
		entrance = 15 + 30 * width;
		exit = 15 + 5 * width;
		return true;
	}

	@Override
	public Mob createMob() {
		return null;
	}

	@Override
	protected void createMobs() {
	}

	@Override
	public Actor addRespawner() {
		return null;
	}

	@Override
	protected void createItems() {
	}

	@Override
	public int randomRespawnCell( Char ch ) {
		int cell;
		int tries = 100;
		do {
			cell = Random.Int(length());
		} while (--tries > 0 && (!passable[cell] || Actor.findChar(cell) != null));
		return tries > 0 ? cell : -1;
	}

	@Override
	public void occupyCell( Char ch ) {
		super.occupyCell( ch );
		//the hero walks through the gap above the entrance room, the boss wakes in the middle of the arena
		if (ch == Dungeon.hero && !started) {
			int x = ch.pos % width, y = ch.pos / width;
			if (x >= 14 && x <= 16 && y >= 24 && y <= 26) {
				started = true;
				seal();
				Mob boss = makeBoss();
				boss.pos = 15 + 15 * width;
				boss.state = boss.HUNTING;
				GameScene.add( boss );
				boss.notice();
			}
		}
	}

	@Override
	public void seal() {
		if (!locked) {
			super.seal();
			Game.runOnRenderThread(new Callback() {
				@Override
				public void call() {
					Music.INSTANCE.play(Assets.Music.SEWERS_BOSS, true);
				}
			});
		}
	}

	@Override
	public void unseal() {
		if (locked) {
			super.unseal();
			WorldShuffle.dropProgressionItem( this );
			Game.runOnRenderThread(new Callback() {
				@Override
				public void call() {
					Music.INSTANCE.end();
				}
			});
		}
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( STARTED, started );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		started = bundle.getBoolean( STARTED );
	}

	@Override
	public String tileName( int tile ) {
		if (tile == Terrain.WATER) return Messages.get(SewerLevel.class, "water_name");
		return super.tileName( tile );
	}

	/** Floor 5, the Elder Shroom in the Forest. */
	public static class Forest extends DeepBossLevel {
		@Override
		public String tilesTex() {
			return "environment/worlds/tiles_deep_forest.png";
		}

		@Override
		protected Mob makeBoss() {
			return new ElderShroom();
		}

		@Override
		protected void createMobs() {
			//two of the Elder Shroom's mushrooms wait in the corners
			for (int pos : new int[]{ 3 + 10 * 32, 27 + 10 * 32 }) {
				YellowMushroom m = new YellowMushroom();
				m.pos = pos;
				mobs.add( m );
			}
		}
	}

	/** Floor 10, the Shaman Leader in the Gnoll Camp. */
	public static class Gnoll extends DeepBossLevel {
		{
			color1 = 0x6a5030;
			color2 = 0x8a6a40;
		}

		@Override
		public String tilesTex() {
			return "environment/worlds/tiles_deep_gnoll.png";
		}

		@Override
		protected Mob makeBoss() {
			return new ShamanLeader();
		}
	}

	/** The Caves Annex, Terminator's arena. A special floor, reached with Deep's Porter. */
	public static class Annex extends DeepBossLevel {
		{
			color1 = 0xb0b0b0;
			color2 = 0xd04040;
		}

		@Override
		public String tilesTex() {
			return "environment/worlds/tiles_deep_annex.png";
		}

		@Override
		public String waterTex() {
			return "environment/worlds/water_deep_annex.png";
		}

		@Override
		protected Mob makeBoss() {
			return new Terminator();
		}
	}

	private static final int W = Terrain.WALL;
	private static final int e = Terrain.EMPTY;
	private static final int E = Terrain.ENTRANCE;
	private static final int X = Terrain.EXIT;
	private static final int M = Terrain.WALL_DECO;

	//MAP_START from Deep's ForestBossLevel, with its locked exit made a plain exit (the floor is sealed during the fight instead)
	private static final int[] MAP = {
			M, W, W, W, W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, M, W, W, W, W, W, W, M, W, W, W, W, W,
			W, W, W, W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, M, W, W, W, W, W, W, M, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, M, W, W, W, W, W, W, M, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, M, W, W, W, W, W, W, M, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, M, W, W, W, W, W, W, M, W, W, W, W, W, W,
			M, e, e, e, W, W, e, e, e, e, e, e, e, e, W, X, W, e, e, e, e, e, e, e, W, W, e, e, e, W, W, W,
			M, e, e, W, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W, e, e, W, W, W,
			M, e, W, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W, e, W, W, W,
			M, W, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W, W, W, W,
			M, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W, W, W,
			M, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W, W,
			M, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W, W,
			M, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W, W,
			M, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W, W,
			M, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W, W,
			M, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W, W,
			M, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W, W,
			M, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W, W,
			M, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W, W,
			M, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W, W,
			M, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W, W,
			M, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W, W,
			M, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W, W,
			M, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W, W,
			M, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W, W,
			M, e, e, e, e, e, e, e, e, e, e, e, W, W, W, e, W, W, W, e, e, e, e, e, e, e, e, e, e, W, W, W,
			M, W, e, e, e, e, e, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, e, e, e, e, W, W, W, W,
			M, W, W, e, e, e, e, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, e, e, e, W, W, W, W, W,
			M, e, W, W, e, e, e, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, e, e, W, W, e, W, W, W,
			M, e, e, W, W, e, e, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, e, W, W, e, e, W, W, W,
			M, e, e, e, W, W, e, e, e, e, e, e, W, e, e, E, e, e, W, e, e, e, e, e, W, W, e, e, e, W, W, W,
			M, W, W, W, W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, M, W, W, W, W, W, W, M, W, W, W, W, W };
}
