/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ToxicTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Scene;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class SpsCavesBossLevel extends Level {

	//sps, SPS levels are 48 by 48
	private static final int SPS_WIDTH = 48;
	private static final int SPS_HEIGHT = 48;

	/** SPS picked the boss from its Bestiary for the floor, here each variant names its own. */
	protected Mob makeBoss(){
		return new com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Hybrid();
	}


	{
		color1 = 0x534f3e;
		color2 = 0xb9d661;

		viewDistance = 6;
	}

	private static final int ROOM_LEFT = SPS_WIDTH / 2 - 2;
	private static final int ROOM_RIGHT = SPS_WIDTH / 2 + 2;
	private static final int ROOM_TOP = SPS_HEIGHT / 2 - 2;
	private static final int ROOM_BOTTOM = SPS_HEIGHT / 2 + 2;

	private int arenaDoor;
	private int stairs = -1;
	private boolean enteredArena = false;

	@Override
	public String tilesTex() {
		return Assets.Sprites.TILES_CAVES;
	}

	@Override
	public String waterTex() {
		return Assets.Sprites.WATER_CAVES;
	}

	private static final String DOOR = "door";
	private static final String ENTERED = "entered";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(DOOR, arenaDoor);
		bundle.put(ENTERED, enteredArena);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		arenaDoor = bundle.getInt(DOOR);
		enteredArena = bundle.getBoolean(ENTERED);
	}

	@Override
	protected boolean build() {
		setSize(SPS_WIDTH, SPS_HEIGHT);

		int topMost = Integer.MAX_VALUE;

		for (int i = 0; i < 8; i++) {
			int left, right, top, bottom;
			if (Random.Int(2) == 0) {
				left = Random.Int(1, ROOM_LEFT - 3);
				right = ROOM_RIGHT + 3;
			} else {
				left = ROOM_LEFT - 3;
				right = Random.Int(ROOM_RIGHT + 3, SPS_WIDTH - 1);
			}
			if (Random.Int(2) == 0) {
				top = Random.Int(2, ROOM_TOP - 3);
				bottom = ROOM_BOTTOM + 3;
			} else {
				top = ROOM_LEFT - 3;
				bottom = Random.Int(ROOM_TOP + 3, SPS_HEIGHT - 1);
			}

			Painter.fill(this, left, top, right - left + 1, bottom - top + 1,
					Terrain.EMPTY);

			if (top < topMost) {
				topMost = top;
				exit = Random.Int(left, right) + (top - 1) * SPS_WIDTH;
			}
		}

		map[exit] = Terrain.LOCKED_EXIT;

		for (int i = 0; i < length(); i++) {
			if (map[i] == Terrain.EMPTY && Random.Int(12) == 0) {
				map[i] = Terrain.INACTIVE_TRAP;
				Trap t = new ToxicTrap().reveal();
				t.active = false;
				setTrap(t, i);				
			}
		}

		Painter.fill(this, ROOM_LEFT - 1, ROOM_TOP - 1, ROOM_RIGHT - ROOM_LEFT
				+ 3, ROOM_BOTTOM - ROOM_TOP + 3, Terrain.WALL);
		Painter.fill(this, ROOM_LEFT, ROOM_TOP + 1, ROOM_RIGHT - ROOM_LEFT + 1,
				ROOM_BOTTOM - ROOM_TOP, Terrain.EMPTY);

		Painter.fill(this, ROOM_LEFT, ROOM_TOP, ROOM_RIGHT - ROOM_LEFT + 1, 1,
				Terrain.INACTIVE_TRAP);

		arenaDoor = Random.Int(ROOM_LEFT, ROOM_RIGHT) + (ROOM_BOTTOM + 1)
				* SPS_WIDTH;
		map[arenaDoor] = Terrain.DOOR;

		entrance = Random.Int(ROOM_LEFT + 1, ROOM_RIGHT - 1)
				+ Random.Int(ROOM_TOP + 1, ROOM_BOTTOM - 1) * SPS_WIDTH;
		map[entrance] = Terrain.ENTRANCE;

		boolean[] patch = Patch.generate(width(), height(), 0.45f, 6, true);
		for (int i = 0; i < length(); i++) {
			if (map[i] == Terrain.EMPTY && patch[i]) {
				map[i] = Terrain.WATER;
			}
		}

		return true;
	}

	
	protected void decorate() {

		for (int i = SPS_WIDTH + 1; i < length() - SPS_WIDTH; i++) {
			if (map[i] == Terrain.EMPTY) {
				int n = 0;
				if (map[i + 1] == Terrain.WALL) {
					n++;
				}
				if (map[i - 1] == Terrain.WALL) {
					n++;
				}
				if (map[i + SPS_WIDTH] == Terrain.WALL) {
					n++;
				}
				if (map[i - SPS_WIDTH] == Terrain.WALL) {
					n++;
				}
				if (Random.Int(8) <= n) {
					map[i] = Terrain.EMPTY_DECO;
				}
			}
		}

		for (int i = 0; i < length(); i++) {
			if (map[i] == Terrain.WALL && Random.Int(8) == 0) {
				map[i] = Terrain.WALL_DECO;
			}
		}

		int sign;
		do {
			sign = Random.Int(ROOM_LEFT, ROOM_RIGHT)
					+ Random.Int(ROOM_TOP, ROOM_BOTTOM) * SPS_WIDTH;
		} while (sign == entrance || map[sign] == Terrain.INACTIVE_TRAP);
		map[sign] = Terrain.EMPTY_DECO;
	}

	@Override
	protected void createMobs() {
	}

	
	public Actor respawner() {
		return null;
	}

	@Override
	protected void createItems() {

	}

	@Override
	public int randomRespawnCell( Char ch ) {
		return -1;
	}

	@Override
	public void occupyCell( Char hero ) {

		super.occupyCell( hero );

		int cell = hero.pos;

		if (!enteredArena && outsideEntraceRoom(cell) && hero == Dungeon.hero) {

			enteredArena = true;
			seal();

			Mob boss = makeBoss();
			boss.state = boss.HUNTING;
			do {
				boss.pos = Random.Int(length());
			} while (!passable[boss.pos] || !outsideEntraceRoom(boss.pos)
					|| (heroFOV[boss.pos] && !Dungeon.seeAll()));
			GameScene.add(boss);
            boss.notice();
			
			
			set(arenaDoor, Terrain.WALL);
			GameScene.updateMap(arenaDoor);
			Dungeon.observe();

			CellEmitter.get(arenaDoor).start(Speck.factory(Speck.ROCK), 0.07f,
					10);
			Camera.main.shake(3, 0.7f);
			Sample.INSTANCE.play(Assets.Sounds.ROCKS);
		}
	}

	public void seal() {
		if (entrance != 0) {

			locked = true;

			set(arenaDoor, Terrain.WALL);
			GameScene.updateMap(arenaDoor);
			Dungeon.observe();

			CellEmitter.get(arenaDoor).start(Speck.factory(Speck.ROCK), 0.07f,
					10);
			Camera.main.shake(3, 0.7f);
			Sample.INSTANCE.play(Assets.Sounds.ROCKS);
			
			set(entrance, Terrain.WALL_DECO);
			GameScene.updateMap(entrance);
			GameScene.ripple(entrance);			

		}
	}

	public void unseal() {
		if (stairs != 0) {
			locked = false;
			CellEmitter.get(arenaDoor).start(Speck.factory(Speck.ROCK), 0.07f,
					10);
			set(arenaDoor, Terrain.EMPTY_DECO);
			GameScene.updateMap(arenaDoor);
			Dungeon.observe();
		    set(entrance, Terrain.ENTRANCE);
			GameScene.updateMap(entrance);			

		}
	}			

	private boolean outsideEntraceRoom(int cell) {
		int cx = cell % SPS_WIDTH;
		int cy = cell / SPS_WIDTH;
		return cx < ROOM_LEFT - 1 || cx > ROOM_RIGHT + 1 || cy < ROOM_TOP - 1
				|| cy > ROOM_BOTTOM + 1;
	}

	@Override
	public String tileName(int tile) {
		switch (tile) {
			case Terrain.GRASS:
				return Messages.get(CavesLevel.class, "grass_name");
			case Terrain.HIGH_GRASS:
				return Messages.get(CavesLevel.class, "high_grass_name");
			case Terrain.WATER:
				return Messages.get(CavesLevel.class, "water_name");
			default:
				return super.tileName( tile );
		}
	}

	@Override
	public String tileDesc(int tile) {
		switch (tile) {
			case Terrain.ENTRANCE:
				return Messages.get(CavesLevel.class, "entrance_desc");
			case Terrain.EXIT:
				return Messages.get(CavesLevel.class, "exit_desc");
			case Terrain.HIGH_GRASS:
				return Messages.get(CavesLevel.class, "high_grass_desc");
			case Terrain.WALL_DECO:
				return Messages.get(CavesLevel.class, "wall_deco_desc");
			case Terrain.BOOKSHELF:
				return Messages.get(CavesLevel.class, "bookshelf_desc");
			default:
				return super.tileDesc( tile );
		}
	}

	@Override
	public com.watabou.noosa.Group addVisuals() {
		super.addVisuals();
		CavesLevel.addCavesVisuals(this, visuals);
		return visuals;
	}

	/** SPS rolled the Spider Queen here as often as the other boss, this variant always has it. */
	public static class Queen extends SpsCavesBossLevel {
		@Override
		protected Mob makeBoss(){
			return new com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SpiderQueen();
		}
	}
}
