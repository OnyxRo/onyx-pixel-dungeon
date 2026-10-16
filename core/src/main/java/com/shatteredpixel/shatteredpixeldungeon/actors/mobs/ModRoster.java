/*
 * Onyx's Pixel Dungeon
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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tmobs.*;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The one place a ported monster is registered. Add a line to {@link #ROSTER} and the monster
 * spawns in that mod's worlds, shows in the bestiary and the sandbox list, and is covered by the
 * tests. Nothing else needs touching.
 *
 * The key is the mod's icon file name without the extension, like "reforged" for
 * interfaces/mods/reforged.png (see WorldShuffle.modKey). Weight is how many copies go in the
 * rotation. A monster is only used on floors from minDepth to maxDepth.
 *
 * Devoted and Matt Edition have their own spawners (DevotedSpawner, MattSpawner) and are not
 * listed here, since their rotations depend on more than depth.
 */
public class ModRoster {

	public static class Entry {
		public final String mod;
		public final Class<? extends Mob> mob;
		public final int weight, minDepth, maxDepth;

		Entry( String mod, Class<? extends Mob> mob, int weight, int minDepth, int maxDepth ){
			this.mod = mod;
			this.mob = mob;
			this.weight = weight;
			this.minDepth = minDepth;
			this.maxDepth = maxDepth;
		}
	}

	public static final ArrayList<Entry> ROSTER = new ArrayList<>();

	private static void add( String mod, Class<? extends Mob> mob, int weight, int minDepth, int maxDepth ){
		ROSTER.add( new Entry( mod, mob, weight, minDepth, maxDepth ) );
	}

	static {
		//Experienced Pixel Dungeon, the thief that takes an item and runs
		add( "experienced", OOFThief.class, 1, 5, 99 );
		//Pixel Dungeon Reforged. Kobolds are most of the crowd, the heavy ones only turn up deeper
		add( "reforged", Kobold.class, 4, 1, 99 );
		//the Wyrm is a mining quest boss that carves tunnels, it never spawns by itself (weight 0) but can be called up in the sandbox
		add( "reforged", Wyrm.class, 0, 1, 99 );
		add( "reforged", Spitter.class, 2, 1, 99 );
		add( "reforged", UnholyPriest.class, 2, 1, 99 );
		add( "reforged", Ballista.class, 1, 8, 99 );
		add( "reforged", Fiend.class, 1, 14, 99 );
		add( "reforged", QuickFiringBallista.class, 1, 14, 99 );
		add( "reforged", DemonGoo.class, 1, 23, 99 );
		add( "abyssalnite", ChronosSkull.class, 1, 1, 99 );
		add( "abyssalnite", FakeWraith.class, 1, 1, 99 );
		add( "abyssalnite", FlameDemon.class, 1, 1, 99 );
		add( "abyssalnite", GroundSpore.class, 1, 1, 99 );
		add( "abyssalnite", GroundStatue.class, 1, 1, 99 );
		add( "abyssalnite", HeavenDoor.class, 1, 1, 99 );
		add( "abyssalnite", Lavastone.class, 1, 1, 99 );
		add( "abyssalnite", PhantomSpirit.class, 1, 1, 99 );
		add( "abyssalnite", WaterFlower.class, 1, 1, 99 );
		add( "abyssalnite", WindSwarm.class, 1, 1, 99 );
		add( "cursed", AdultFireElemental.class, 1, 1, 99 );
		add( "cursed", AirElemental.class, 1, 1, 99 );
		add( "cursed", EarthenGuardian.class, 1, 1, 99 );
		add( "cursed", FossilSkeleton.class, 1, 1, 99 );
		add( "cursed", GnollBattlemage.class, 1, 1, 99 );
		add( "cursed", Kupua.class, 1, 1, 99 );
		add( "cursed", MageCrab.class, 1, 1, 99 );
		add( "cursed", Minion.class, 1, 1, 99 );
		add( "cursed", NonBossGoo.class, 1, 1, 99 );
		add( "cursed", Shielded.class, 1, 1, 99 );
		add( "cursed", Shinobi.class, 1, 1, 99 );
		add( "cursed", SonicBat.class, 1, 1, 99 );
		add( "cursed", WaterElemental.class, 1, 1, 99 );
		add( "cursed", WaterLarvae.class, 1, 1, 99 );
		//Shittened PD. The mod's own spawner mixes these in by depth, this keeps the odds close. Weight 0 means the monster only comes from other things (a room, an item, another monster) or the sandbox.
		add( "shittened", Capybara.class, 1, 1, 10 );
		add( "shittened", CapybaraPile.class, 0, 1, 99 );
		add( "shittened", Minibara.class, 0, 1, 99 );
		add( "shittened", Bucket.class, 0, 1, 99 );
		add( "shittened", CaveBat.class, 3, 11, 14 );
		add( "shittened", DM105.class, 0, 1, 99 );
		add( "shittened", DM16.class, 1, 11, 99 );
		add( "shittened", EvolvedSwarm.class, 0, 1, 99 );
		add( "shittened", Goldfish.class, 0, 1, 99 );
		add( "shittened", Stool.class, 0, 1, 99 );
		add( "shittened", Chair.class, 0, 1, 99 );
		add( "shittened", CursedEye.class, 1, 17, 99 );
		add( "shittened", DM102.class, 0, 1, 99 );
		add( "shittened", DM102a2.class, 0, 1, 99 );
		add( "shittened", DMS74UN.class, 0, 1, 99 );
		add( "shittened", DemonRat.class, 0, 1, 99 );
		add( "shittened", NormalRat.class, 2, 1, 5 );
		add( "shittened", Perch.class, 0, 1, 99 );
		add( "shittened", PinkCrab.class, 1, 2, 10 );
		add( "shittened", RaveBat.class, 1, 9, 99 );
		add( "shittened", Raven.class, 0, 1, 99 );
		add( "shittened", Table.class, 0, 1, 99 );
		add( "shittened", Boombox.class, 0, 1, 99 );
		add( "shittened", Cobra.class, 1, 2, 10 );
		add( "shittened", GlitchRat.class, 1, 11, 99 );
		add( "shittened", HeroRat.class, 1, 1, 10 );
		add( "shittened", RGBCrab.class, 1, 4, 14 );
		add( "shittened", DM249.class, 1, 11, 99 );
		add( "shittened", BabyGoblin.class, 0, 1, 99 );
		add( "shittened", Goblin.class, 0, 1, 99 );
		add( "shittened", MindGoblin.class, 0, 1, 99 );
		add( "shittened", Glassbox.class, 1, 1, 99 );
		add( "shittened", Ratatouille.class, 1, 1, 10 );
		add( "shittened", Adventurer.class, 0, 1, 99 );
		add( "shittened", Twingo.class, 0, 1, 99 );
		add( "shittened", Evan.class, 0, 1, 99 );
		add( "shittened", Tokmanni.class, 0, 1, 99 );
		add( "shittened", GreenGoo.class, 0, 1, 99 );
		add( "ryuse", PollutedCrab.class, 2, 2, 9 );
		add( "ryuse", PollutedRat.class, 3, 1, 9 );
		//Transformation PD. Its monsters have fixed stats of 100 to 250 HP, so they only turn up deep. The piranhas and the wraith come from other monsters or water
		add( "transformation", CamouflageGnoll.class, 1, 11, 20 );
		add( "transformation", ChainShadowThief.class, 1, 11, 20 );
		add( "transformation", CorrosiveSwarm.class, 1, 16, 25 );
		add( "transformation", HeavyCrabification.class, 1, 16, 25 );
		add( "transformation", LandPiranha.class, 0, 1, 99 );
		add( "transformation", MarshSlime.class, 1, 16, 25 );
		add( "transformation", MechanicalFist.class, 1, 21, 25 );
		add( "transformation", MimicCrocodile.class, 1, 11, 20 );
		add( "transformation", PowerfulWraith.class, 0, 11, 20 );
		add( "transformation", EarthlySerpent.class, 1, 21, 25 );
		add( "transformation", RuneSpinner.class, 1, 16, 25 );
		add( "transformation", PhantomLandPiranha.class, 0, 1, 99 );
		add( "transformation", RoastLambWarlock.class, 1, 16, 25 );
		add( "transformation", SoulCollector.class, 1, 16, 25 );
		add( "transformation", TapirCrocodile.class, 1, 21, 25 );
		add( "transformation", TransformMimic.class, 0, 1, 99 );
		add( "dragonic", Ainsley.class, 1, 22, 24 );
		add( "dragonic", DreadKnight.class, 0, 1, 99 );
		add( "dragonic", Shadow.class, 1, 22, 24 );
		add( "dragonic", SkeletonWarrior.class, 0, 1, 99 );
		add( "dragonic", SmallDrake.class, 1, 22, 24 );
		add( "dragonic", StatueArmor.class, 0, 1, 99 );
		add( "dragonic", FlamingSkull.class, 1, 22, 24 );
		add( "dragonic", Slug.class, 0, 1, 99 );
	}

	/** Every monster on the roster, in the order they were added. */
	public static ArrayList<Class<? extends Mob>> mobs(){
		ArrayList<Class<? extends Mob>> all = new ArrayList<>();
		for (Entry e : ROSTER) if (!all.contains( e.mob )) all.add( e.mob );
		return all;
	}

	public static boolean contains( Class<?> cls ){
		for (Entry e : ROSTER) if (e.mob == cls) return true;
		return false;
	}

	/** Mods that have monsters on the roster, with how many each. */
	public static Map<String, Integer> counts(){
		Map<String, Integer> counts = new LinkedHashMap<>();
		for (Entry e : ROSTER) counts.merge( e.mod, 1, Integer::sum );
		return counts;
	}

	public static boolean hasMod( String mod ){
		for (Entry e : ROSTER) if (e.mod.equals( mod )) return true;
		return false;
	}

	/** The rotation for a floor of a mod's world, shuffled like MobSpawner's. */
	public static ArrayList<Class<? extends Mob>> rotation( String mod, int depth ){
		ArrayList<Class<? extends Mob>> mobs = new ArrayList<>();
		for (Entry e : ROSTER) {
			if (!e.mod.equals( mod ) || depth < e.minDepth || depth > e.maxDepth) continue;
			for (int i = 0; i < e.weight; i++) mobs.add( e.mob );
		}
		Random.shuffle( mobs );
		return mobs;
	}

}
