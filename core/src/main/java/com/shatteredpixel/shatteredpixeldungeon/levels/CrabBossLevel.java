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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.sps.CrabKing;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.sps.HermitCrab;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Shell;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.WornKey;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.Scene;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class CrabBossLevel extends Level {

	//sps, SPS levels are 48 by 48
	private static final int SPS_WIDTH = 48;
	private static final int SPS_HEIGHT = 48;


	{
		color1 = 0x4b6636;
		color2 = 0xf2f2f2;
	}

	private static final int TOP = 2;
	private static final int HALL_WIDTH = 13;
	private static final int HALL_HEIGHT = 15;
	private static final int CHAMBER_HEIGHT = 3;

	private static final int LEFT = (SPS_WIDTH - HALL_WIDTH) / 2;
	private static final int CENTER = LEFT + HALL_WIDTH / 2;

	private int arenaDoor;
	private boolean enteredArena = false;
	private boolean keyDropped = false;

	@Override
	public String tilesTex() {
		return Assets.Sprites.TILES_BEACH;
	}

	@Override
	public String waterTex() {
		return Assets.Sprites.WATER_PRISON;
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

		Painter.fill(this, LEFT, TOP, HALL_WIDTH, HALL_HEIGHT, Terrain.WATER);
		Painter.fill(this, CENTER, TOP, 1, HALL_HEIGHT, Terrain.WATER);

		int y = TOP + 1;
		while (y < TOP + HALL_HEIGHT) {
			map[y * SPS_WIDTH + CENTER - 2] = Terrain.STATUE;
			map[y * SPS_WIDTH + CENTER + 2] = Terrain.STATUE;
			y += 2;
		}
		
		exit = (TOP - 1) * SPS_WIDTH + CENTER;
		map[exit] = Terrain.LOCKED_EXIT;

		arenaDoor = (TOP + HALL_HEIGHT) * SPS_WIDTH + CENTER;
		map[arenaDoor] = Terrain.DOOR;

		Painter.fill(this, LEFT, TOP + HALL_HEIGHT + 1, HALL_WIDTH,
				CHAMBER_HEIGHT, Terrain.EMPTY);
		Painter.fill(this, LEFT, TOP + HALL_HEIGHT + 1, 1, CHAMBER_HEIGHT,
				Terrain.WATER);
		Painter.fill(this, LEFT + HALL_WIDTH - 1, TOP + HALL_HEIGHT + 1, 1,
				CHAMBER_HEIGHT, Terrain.WATER);

		entrance = (TOP + HALL_HEIGHT + 2 + Random.Int(CHAMBER_HEIGHT - 1))
				* SPS_WIDTH + LEFT + (/* 1 + */Random.Int(HALL_WIDTH - 2));
		map[entrance] = Terrain.PEDESTAL;

		map[exit] = Terrain.WALL;

		return true;
	}

	
	protected void decorate() {

		for (int i = 0; i < length(); i++) {
			if (map[i] == Terrain.EMPTY && Random.Int(10) == 0) {
				map[i] = Terrain.EMPTY_DECO;
			} else if (map[i] == Terrain.WALL && Random.Int(8) == 0) {
				map[i] = Terrain.WALL_DECO;
			}
		}

		//int sign = arenaDoor + width() + 1;
		//map[sign] = Terrain.EMPTY_DECO;
	}

	public static int pedestal(boolean left) {
		if (left) {
			return (TOP + HALL_HEIGHT / 2) * SPS_WIDTH + CENTER - 2;
		} else {
			return (TOP + HALL_HEIGHT / 2) * SPS_WIDTH + CENTER + 2;
		}
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
			//locked = true;

			Mob boss = new CrabKing();
			Mob shell = new Shell();
			Mob crab1 = new HermitCrab();
			Mob crab2 = new HermitCrab();
			Mob crab3 = new HermitCrab();
			Mob crab4 = new HermitCrab();
			boss.state = boss.HUNTING;
			crab1.state = crab1.HUNTING;
			crab2.state = crab2.HUNTING;
			crab3.state = crab3.HUNTING;
			crab4.state = crab4.HUNTING;
			int count = 0;
			do {
				boss.pos = Random.Int(length());
				shell.pos = (TOP + 1) * SPS_WIDTH + CENTER;
				crab1.pos = (TOP + 1) * SPS_WIDTH + CENTER+1;
				crab2.pos = (TOP + 1) * SPS_WIDTH + CENTER-1;
				crab3.pos = (TOP + 2) * SPS_WIDTH + CENTER;
				crab4.pos = (TOP + 0) * SPS_WIDTH + CENTER;
			} while (!passable[boss.pos] 
					|| !outsideEntraceRoom(boss.pos)
					|| (heroFOV[boss.pos] && count++ < 20));
			
			GameScene.add(boss);
			GameScene.add(shell);
			
			GameScene.add(crab1);
			GameScene.add(crab2);
			GameScene.add(crab3);
			GameScene.add(crab4);

			if (heroFOV[boss.pos]) {
				boss.notice();
				boss.sprite.alpha(0);
				boss.sprite.parent.add(new AlphaTweener(boss.sprite, 1, 0.1f));
			}

			//set(arenaDoor, Terrain.WALL);
			//GameScene.updateMap(arenaDoor);
			Dungeon.observe();
		}
	}

	@Override
	public Heap drop(Item item, int cell) {

		if (!keyDropped && item instanceof WornKey) {

			keyDropped = true;
			locked = false;

			set(arenaDoor, Terrain.DOOR);
			GameScene.updateMap(arenaDoor);
			Dungeon.observe();
		}

		return super.drop(item, cell);
	}

	private boolean outsideEntraceRoom(int cell) {
		return cell / SPS_WIDTH < arenaDoor / SPS_WIDTH;
	}

	@Override
	public String tileName(int tile) {
		switch (tile) {
		case Terrain.WATER:
			return "Crystal clear pools.";
		case Terrain.HIGH_GRASS:
			return "Seaweed Tangles";
		default:
			return super.tileName(tile);
		}
	}

	@Override
	public String tileDesc(int tile) {
		switch (tile) {
		case Terrain.EMPTY_DECO:
			return "Small crabs and shell fish litter the sandy floor.";
		case Terrain.EMPTY_SP:
			return "Thick carpet covers the floor.";
		case Terrain.STATUE:
		case Terrain.STATUE_SP:
			return "A large sea shell is propped up in the sand.";
		case Terrain.BOOKSHELF:
			return "Mostly beach reads.";
		default:
			return super.tileDesc(tile);
		}
	}

	@Override
	public com.watabou.noosa.Group addVisuals() {
		super.addVisuals();
		CityLevel.addCityVisuals(this, visuals);
		return visuals;
	}
}
