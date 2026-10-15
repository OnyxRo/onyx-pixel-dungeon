/*
 * Onyx's Pixel Dungeon
 * Based on Shattered Pixel Dungeon, Copyright (C) 2014-2025 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.watabou.noosa.Game;

import java.util.ArrayList;
import java.util.function.Supplier;

/**
 * Levels from mods that are not part of the normal run, like Cursed PD's challenge floors and its hub, or
 * Abyssalnite's final arena. A sandbox run can visit any of them from the Floors panel, and the mods' own
 * NPCs and items use travel() to move between them.
 */
public class SpecialFloors {

	public static class Entry {
		public final String mod;
		public final String label;
		public final Supplier<Level> make;

		Entry( String mod, String label, Supplier<Level> make ){
			this.mod = mod;
			this.label = label;
			this.make = make;
		}
	}

	public static final ArrayList<Entry> LIST = new ArrayList<>();

	private static void add( String mod, String label, Supplier<Level> make ){
		LIST.add( new Entry( mod, label, make ) );
	}

	static {
		add( "Abyssalnite PD", "Final arena, septium pillars and Cthulhu", FinalLevel::new );

		add( "Cursed PD", "Start hub", StartLevel::new );
		add( "Cursed PD", "Safe room", SafeRoom::new );
		add( "Cursed PD", "Grinding level", GrindingLevel::new );
		add( "Cursed PD", "Amulet level", AmuletLevel::new );
		add( "Cursed PD", "Water challenge", WaterChallengeLevel::new );
		add( "Cursed PD", "Water challenge boss, the sewer golem", WaterChallengeBossLevel::new );
		add( "Cursed PD", "Fire challenge", FireChallengeLevel::new );
		add( "Cursed PD", "Fire challenge boss", FireChallengeBossLevel::new );
		add( "Cursed PD", "Air challenge", AirChallengeLevel::new );
		add( "Cursed PD", "Air challenge boss, Tengu", AirChallengeBossLevel::new );
		add( "Cursed PD", "Earth challenge", EarthChallengeLevel::new );
		add( "Cursed PD", "Earth challenge, lore floor", EarthChallengeLoreLevel::new );
	}

	/**
	 * Builds this level in place of the next floor and moves the hero to it. Only works in a sandbox run,
	 * which always builds floors fresh and never keeps them.
	 */
	public static void travel( Supplier<Level> make ){
		Dungeon.sandboxSpecial = make;
		Level.beforeTransition();
		InterlevelScene.mode = InterlevelScene.Mode.SANDBOX;
		InterlevelScene.returnDepth = Dungeon.depth;
		Game.switchScene( InterlevelScene.class );
	}
}
