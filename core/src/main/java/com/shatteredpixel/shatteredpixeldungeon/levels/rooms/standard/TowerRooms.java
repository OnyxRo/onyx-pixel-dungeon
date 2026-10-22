package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

//Tower PD's own standard rooms, added to floors built in its worlds by WorldLevel.initRooms
public class TowerRooms {

	public static final Class<?>[] ROOMS = {
			BeeGardenRoom.class, BurnedGrassRoom.class, DiamondRoom.class, DiamondPondRoom.class,
			DiamondPondPiranhaRoom.class, FloodedMeetingRoom.class, LasherRoom.class, MassGraveLibraryRoom.class,
			RoomInARoomRoom.class, SeparatedHallsRoom.class, TrapSquareRoom.class
	};

	public static StandardRoom random() {
		return (StandardRoom) Reflection.newInstance( Random.element( ROOMS ) );
	}
}
