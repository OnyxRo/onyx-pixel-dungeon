/*
 * Onyx's Pixel Dungeon
 * Copyright (C) 2026 Onyx Rothman
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

package com.shatteredpixel.shatteredpixeldungeon.levels.worlds;

import com.shatteredpixel.shatteredpixeldungeon.levels.CavesLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.CityLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.HallsLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.PrisonLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.SewerLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.WorldShuffle;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.RegularPainter;
import com.watabou.noosa.Group;
import com.watabou.noosa.audio.Music;

/**
 * Base for worlds ported from other Pixel Dungeon mods.
 *
 * A world supplies its look (tileset, water, decoration painter, ambient effects, music).
 * Everything that decides danger (monsters, loot, traps) comes from the actual depth,
 * so these classes carry no quests, monsters or depth-specific rules of their own.
 */
public abstract class WorldLevel extends RegularLevel implements ModWorld {

	/** Which of Shattered's ambient effect sets to reuse, if any. */
	protected enum Ambience { NONE, SEWERS, PRISON, CAVES, CITY, HALLS }

	/** Which of Shattered's music sets to use when the world has no soundtrack of its own. */
	protected enum MusicSet { SEWERS, PRISON, CAVES, CITY, HALLS }

	/** The decoration painter, before water, grass and traps are applied. */
	protected abstract RegularPainter basePainter();

	protected float waterFill(){ return feeling == Feeling.WATER ? 0.85f : 0.30f; }
	protected int waterSmoothing(){ return 5; }
	protected float grassFill(){ return feeling == Feeling.GRASS ? 0.80f : 0.20f; }
	protected int grassSmoothing(){ return 4; }

	/** A music file from the original mod, or null to use a Shattered music set. */
	protected String track(){ return null; }
	protected MusicSet musicSet(){ return MusicSet.CITY; }

	protected Ambience ambience(){ return Ambience.NONE; }

	/** The world's own song, or null if it plays one of Shattered's sets. For the song list. */
	public String musicTrack(){ return track(); }

	@Override
	protected Painter painter() {
		return basePainter()
				.setWater(waterFill(), waterSmoothing())
				.setGrass(grassFill(), grassSmoothing())
				.setTraps(nTraps(), WorldShuffle.trapClasses(this), WorldShuffle.trapChances(this));
	}

	@Override
	public void playLevelMusic() {
		if (track() != null){
			Music.INSTANCE.play(track(), true);
			return;
		}
		switch (musicSet()){
			case SEWERS: Music.INSTANCE.playTracks(SewerLevel.SEWER_TRACK_LIST, SewerLevel.SEWER_TRACK_CHANCES, false); break;
			case PRISON: Music.INSTANCE.playTracks(PrisonLevel.PRISON_TRACK_LIST, PrisonLevel.PRISON_TRACK_CHANCES, false); break;
			case CAVES:  Music.INSTANCE.playTracks(CavesLevel.CAVES_TRACK_LIST, CavesLevel.CAVES_TRACK_CHANCES, false); break;
			case CITY:   Music.INSTANCE.playTracks(CityLevel.CITY_TRACK_LIST, CityLevel.CITY_TRACK_CHANCES, false); break;
			case HALLS:  Music.INSTANCE.playTracks(HallsLevel.HALLS_TRACK_LIST, HallsLevel.HALLS_TRACK_CHANCES, false); break;
		}
	}

	@Override
	protected java.util.ArrayList<com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room> initRooms() {
		java.util.ArrayList<com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room> rooms = super.initRooms();
		//Lovecraft PD. Its floors 1, 4 and 14 had a garden, an elder sign room and a yellow sign room, those
		//floors never shuffle here, so they turn up now and then on any floor of a Lovecraft world instead
		if ("lovecraft".equals( WorldShuffle.modKey( this ) )) {
			if (com.watabou.utils.Random.Int( 6 ) == 0) rooms.add( new com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.GardenerRoom() );
			if (com.watabou.utils.Random.Int( 5 ) == 0) rooms.add( new com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.ElderSignRoom() );
			if (com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth >= 11 && com.watabou.utils.Random.Int( 5 ) == 0) rooms.add( new com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.YellowSignRoom() );
		}
		//Tower PD added eleven standard rooms to its room pool, floors of a Tower world get one or two of them
		if ("tower".equals( WorldShuffle.modKey( this ) )) {
			int n = 1 + com.watabou.utils.Random.Int( 2 );
			for (int i = 0; i < n; i++) rooms.add( com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.TowerRooms.random() );
		}
		return rooms;
	}

	@Override
	protected void createItems() {
		super.createItems();
		//Lovecraft PD floors lie about with the mod's own weapons, guns and tools
		if ("lovecraft".equals( WorldShuffle.modKey( this ) )) {
			com.shatteredpixel.shatteredpixeldungeon.items.Item extra = com.shatteredpixel.shatteredpixeldungeon.items.LovecraftLoot.forFloor();
			if (extra != null) drop( extra, randomDropCell() );
		}
	}

	@Override
	public Group addVisuals() {
		super.addVisuals();
		switch (ambience()){
			case SEWERS: SewerLevel.addSewerVisuals(this, visuals); break;
			case PRISON: PrisonLevel.addPrisonVisuals(this, visuals); break;
			case CAVES:  CavesLevel.addCavesVisuals(this, visuals); break;
			case CITY:   CityLevel.addCityVisuals(this, visuals); break;
			case HALLS:  HallsLevel.addHallsVisuals(this, visuals); break;
			case NONE: default: break;
		}
		return visuals;
	}
}
