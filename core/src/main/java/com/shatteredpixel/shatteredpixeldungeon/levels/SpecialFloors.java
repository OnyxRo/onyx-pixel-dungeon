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

import com.shatteredpixel.shatteredpixeldungeon.levels.endlessarenas.*;
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

		add( "Devoted PD", "Shadow realm, the Void trap", VoidTrapLevel::new );
		add( "Deep PD", "Caves Annex, Terminator's arena", DeepBossLevel.Annex::new );

		add( "Ryuse's PD", "Eye of Wisdom, the eye shaped chasm", EyeOfWisdomLevel::new );

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

		//Transformation PD, the boss arenas of its tower
		add( "Transformation PD", "Tower boss, Pestilence Knight", towerBoss( com.shatteredpixel.shatteredpixeldungeon.levels.towers.TowerBossGenerator.PESTILENCE_KNIGHT_ID ) );
		add( "Transformation PD", "Tower boss, Death Knight", towerBoss( com.shatteredpixel.shatteredpixeldungeon.levels.towers.TowerBossGenerator.DEATH_KNIGHT_ID ) );
		add( "Transformation PD", "Tower boss, Gentleman Elf", towerBoss( com.shatteredpixel.shatteredpixeldungeon.levels.towers.TowerBossGenerator.GENTLEMAN_ELF_ID ) );
		add( "Transformation PD", "Tower boss, Hunger Knight", towerBoss( com.shatteredpixel.shatteredpixeldungeon.levels.towers.TowerBossGenerator.HUNGER_KNIGHT_ID ) );
		//transformation, the village above the dungeon with its shop, the treasure hunt and the dungeon doctor
		add( "Transformation PD", "Surface town", SurfaceTownLevel::new );

		//Tower PD, the tower defense arenas. Each one expects the floor number it had in the mod, see towerArena
		add( "Tower PD", "Arena 0, Testing pathway", towerArena( Arena0::new ) );
		add( "Tower PD", "Arena 1, Sewer entrance", towerArena( Arena1::new ) );
		add( "Tower PD", "Arena 2, Deeper Sewers", towerArena( Arena2::new ) );
		add( "Tower PD", "Arena 3, Gnoll village", towerArena( Arena3::new ) );
		add( "Tower PD", "Arena 4, Crab lake", towerArena( Arena4::new ) );
		add( "Tower PD", "Arena 5, Caustic depths", towerArena( Arena5::new ) );
		add( "Tower PD", "Arena 6, Prison", towerArena( Arena6::new ) );
		add( "Tower PD", "Arena 7, Mage training grounds", towerArena( Arena7::new ) );
		add( "Tower PD", "Arena 8, Prison graveyard", towerArena( Arena8::new ) );
		add( "Tower PD", "Arena 9, Guard post", towerArena( Arena9::new ) );
		add( "Tower PD", "Arena 10, Protected cell block", towerArena( Arena10::new ) );
		add( "Tower PD", "Arena 11, Upper caves", towerArena( Arena11::new ) );
		add( "Tower PD", "Arena 12, Abandoned mines", towerArena( Arena12::new ) );
		add( "Tower PD", "Arena 13, The temple", towerArena( Arena13::new ) );
		add( "Tower PD", "Arena 14, Goblin outpost", towerArena( Arena14::new ) );
		add( "Tower PD", "Arena 15, Warehouses", towerArena( Arena15::new ) );
		add( "Tower PD", "Arena 16, City ruins", towerArena( Arena16::new ) );
		add( "Tower PD", "Arena 17, ???", towerArena( Arena17::new ) );
		add( "Tower PD", "Arena 18, Storm the gates", towerArena( Arena18::new ) );
		add( "Tower PD", "Arena 19, The treasury", towerArena( Arena19::new ) );
		add( "Tower PD", "Arena 20, The King's fall", towerArena( Arena20::new ) );
		add( "Tower PD", "Arena 21, Collapsed halls", towerArena( Arena21::new ) );
		add( "Tower PD", "Arena 22, The Forge", towerArena( Arena22::new ) );
		add( "Tower PD", "Arena 23, Soul Desert", towerArena( Arena23::new ) );
		add( "Tower PD", "Arena 24, Phantasmagoria", towerArena( Arena24::new ) );
		add( "Tower PD", "Arena 25, Altar", towerArena( Arena25::new ) );
		add( "Tower PD", "Endless arena 1, Sewer catacombs", towerArena( com.shatteredpixel.shatteredpixeldungeon.levels.endlessarenas.EndlessArena1::new ) );
		add( "Tower PD", "Endless arena 2, Rat Kingdom", towerArena( com.shatteredpixel.shatteredpixeldungeon.levels.endlessarenas.EndlessArena2::new ) );
		add( "Tower PD", "Endless arena 3, Sector 95", towerArena( com.shatteredpixel.shatteredpixeldungeon.levels.endlessarenas.EndlessArena3::new ) );
		add( "Tower PD", "Endless arena 4, Gnolls and Goblins", towerArena( com.shatteredpixel.shatteredpixeldungeon.levels.endlessarenas.EndlessArena4::new ) );
		add( "Tower PD", "Endless arena 5, Bot factory", towerArena( com.shatteredpixel.shatteredpixeldungeon.levels.endlessarenas.EndlessArena5::new ) );
		//rearranged, Re-ARranged PD's temple branch and the room past its labs. Built at floor 14 like the mod's temple
		add( "Re-ARranged PD", "Temple entrance, chambers of trials", rearranged( TempleNewLevel::new, 14 ) );
		add( "Re-ARranged PD", "Temple chasm", rearranged( TempleChasmLevel::new, 14 ) );
		add( "Re-ARranged PD", "Temple, the deep halls", rearranged( TempleLevel::new, 14 ) );
		add( "Re-ARranged PD", "Temple sanctum, the sentries", rearranged( TempleLastLevel::new, 14 ) );
		add( "Re-ARranged PD", "Room past the labs, the old amulet", rearranged( NewLastLevel::new, 26 ) );

		add( "Magic Ling PD", "Sky Dead boss, the side branch", com.shatteredpixel.shatteredpixeldungeon.levels.minilevels.MiniSkyShadowBossLevel::new );
		add( "Magic Ling PD", "DM-Zero, the boss rush finale", com.shatteredpixel.shatteredpixeldungeon.levels.DM920BossLevel::new );
		add( "Magic Ling PD", "Sothoth boss, the galaxy key", com.shatteredpixel.shatteredpixeldungeon.levels.spical.GalaxyKeyBossLevel::new );
		add( "Magic Ling PD", "White Yan boss, the peach blossom hall", com.shatteredpixel.shatteredpixeldungeon.levels.spical.PeachGodBossLevel::new );
		add( "Magic Ling PD", "Fire Dragon boss, the lava caves", com.shatteredpixel.shatteredpixeldungeon.levels.LaveCavesBossLevel::new );
		add( "Magic Ling PD", "Hollow theatre, Morpheus and the minigames", com.shatteredpixel.shatteredpixeldungeon.levels.hollow.TheatreLevel::new );
		add( "Magic Ling PD", "Cerberus boss, the hollow river", com.shatteredpixel.shatteredpixeldungeon.levels.hollow.CerDogBossLevel::new );
		add( "Magic Ling PD", "Morpheus boss, the hollow core and its towers", com.shatteredpixel.shatteredpixeldungeon.levels.hollow.MorpheusBossLevel::new );
		add( "Magic Ling PD", "Hollow minigame, the ghost chase", com.shatteredpixel.shatteredpixeldungeon.levels.hollow.PacmanHollowActorLevel::new );
		add( "Magic Ling PD", "Hollow minigame, the box pushing puzzle", com.shatteredpixel.shatteredpixeldungeon.levels.hollow.MoveBoxHollowActorLevel::new );
	}

	/** Re-ARranged PD's levels read the floor number, so it is set before they are built. */
	private static Supplier<Level> rearranged( Supplier<? extends Level> make, int depth ){
		return () -> {
			Dungeon.depth = depth;
			return make.get();
		};
	}

	/** A Transformation PD tower boss arena for one chosen boss. The tower puts its bosses on floors 5, 10 and so on, so the floor is set to 10. */
	private static Supplier<Level> towerBoss( String bossId ){
		return () -> {
			com.shatteredpixel.shatteredpixeldungeon.levels.towers.TowerBossGenerator.forcedId = bossId;
			Dungeon.depth = 10;
			return new com.shatteredpixel.shatteredpixeldungeon.levels.towers.TowerBossLevel();
		};
	}

	/** Tower PD's arenas read Dungeon.depth for their own rules, so the floor number is set to the arena's before it is built. */
	private static Supplier<Level> towerArena( Supplier<? extends Arena> make ){
		return () -> {
			Arena arena = make.get();
			if (arena.arenaDepth > 0) Dungeon.depth = arena.arenaDepth;
			return arena;
		};
	}

	/**
	 * Builds this level in place of the next floor and moves the hero to it. Only works in a sandbox run,
	 * which always builds floors fresh and never keeps them.
	 */
	//magicling, leaves a special floor for an ordinary floor at the same depth, the hollow's plots call it where the mod jumped to its own deep floors
	public static void leave(){
		Dungeon.sandboxSpecial = null;
		Level.beforeTransition();
		InterlevelScene.mode = InterlevelScene.Mode.SANDBOX;
		InterlevelScene.returnDepth = Dungeon.depth;
		Game.switchScene( InterlevelScene.class );
	}

	public static void travel( Supplier<Level> make ){
		Dungeon.sandboxSpecial = make;
		Level.beforeTransition();
		InterlevelScene.mode = InterlevelScene.Mode.SANDBOX;
		InterlevelScene.returnDepth = Dungeon.depth;
		Game.switchScene( InterlevelScene.class );
	}
}
