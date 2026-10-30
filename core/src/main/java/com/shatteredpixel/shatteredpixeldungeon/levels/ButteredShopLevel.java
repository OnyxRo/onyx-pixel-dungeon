/*
 * Buttered Pixel Dungeon's Arena shop (its ArenaShopLevel), a Pixel Mart reached with the key.
 * Only an entrance and a shop, the stairs lead back up to the arena. Stock follows the floor
 * like any shop, floors 6, 11, 16 and 21.
 */
package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.levels.builders.Builder;
import com.shatteredpixel.shatteredpixeldungeon.levels.builders.LineBuilder;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.HallsPainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.ShopRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.EmptyRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.entrance.EntranceRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.noosa.Group;

import java.util.ArrayList;

public class ButteredShopLevel extends RegularLevel {

	{
		color1 = 0x4b6636;
		color2 = 0xf2f2f2;
		viewDistance = 8;
	}

	@Override
	public String tilesTex() {
		return Assets.Environment.TILES_HALLS;
	}

	@Override
	public String waterTex() {
		return Assets.Environment.WATER_HALLS;
	}

	@Override
	protected boolean build() {
		if (!super.build()) return false;
		for (int i = 0; i < length(); i++) {
			if (map[i] == Terrain.SECRET_DOOR) map[i] = Terrain.DOOR;
		}
		//the stairs up arrive on the arena's own stairs, in its centre
		for (LevelTransition t : transitions) {
			if (t.type == LevelTransition.Type.REGULAR_ENTRANCE) {
				t.destDepth = 1;
				t.destBranch = 0;
				t.destType = LevelTransition.Type.REGULAR_ENTRANCE;
			}
		}
		return true;
	}

	@Override
	protected ArrayList<Room> initRooms() {
		ArrayList<Room> rooms = new ArrayList<>();
		rooms.add( roomEntrance = new EntranceRoom() );
		rooms.add( new EmptyRoom() );
		rooms.add( new ShopRoom() );
		return rooms;
	}

	@Override
	protected Builder builder() {
		return new LineBuilder()
				.setPathVariance(0f)
				.setPathLength(1f, new float[]{1})
				.setTunnelLength(new float[]{0, 0, 1}, new float[]{1});
	}

	@Override
	protected Painter painter() {
		return new HallsPainter()
				.setWater( 0.35f, 4 )
				.setGrass( 0.30f, 3 );
	}

	@Override
	protected int nTraps() {
		return 0;
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
		return -1;
	}

	@Override
	public String tileName( int tile ) {
		switch (tile) {
			case Terrain.WATER:
				return Messages.get(HallsLevel.class, "water_name");
			case Terrain.HIGH_GRASS:
				return Messages.get(HallsLevel.class, "high_grass_name");
			default:
				return super.tileName( tile );
		}
	}

	@Override
	public String tileDesc( int tile ) {
		switch (tile) {
			case Terrain.ENTRANCE:
			case Terrain.ENTRANCE_SP:
				return Messages.get(ButteredShopLevel.class, "entrance_desc");
			case Terrain.WALL_DECO:
			case Terrain.EMPTY_DECO:
				return Messages.get(ButteredShopLevel.class, "deco_desc");
			case Terrain.EMPTY_SP:
				return Messages.get(ButteredShopLevel.class, "carpet_desc");
			default:
				return super.tileDesc( tile );
		}
	}

	@Override
	public Group addVisuals() {
		super.addVisuals();
		HallsLevel.addHallsVisuals(this, visuals);
		return visuals;
	}
}
