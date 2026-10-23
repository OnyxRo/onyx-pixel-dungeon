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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tboss.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hostile.criminals.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hostile.rats.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hostile.scorpions.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.livingplants.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.minibosses.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.ElderThing;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Gardner;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Pepe;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.allsearch.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.lb.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rlpt.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.galaxy.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.Assassin;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.Goolem;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.Terminator;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.GangBoss;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.ShamanLeader;
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
		add( "transformation", TwistedMirror.class, 0, 1, 99 );
		add( "dragonic", Ainsley.class, 1, 22, 24 );
		add( "dragonic", DreadKnight.class, 0, 1, 99 );
		add( "dragonic", Shadow.class, 1, 22, 24 );
		add( "dragonic", SkeletonWarrior.class, 0, 1, 99 );
		add( "dragonic", SmallDrake.class, 1, 22, 24 );
		add( "dragonic", StatueArmor.class, 0, 1, 99 );
		add( "dragonic", FlamingSkull.class, 1, 22, 24 );
		add( "dragonic", Slug.class, 0, 1, 99 );
		add( "dragonic", Dragon.class, 0, 1, 99 );
		add( "tower", GnollBlind.class, 1, 4, 9 );
		add( "tower", GnollThrower.class, 1, 6, 10 );
		add( "tower", GnollTwilight.class, 1, 9, 14 );
		add( "tower", GoblinFat.class, 1, 6, 11 );
		add( "tower", GoblinGiant.class, 1, 12, 18 );
		add( "tower", GoblinNinja.class, 1, 8, 14 );
		add( "tower", GoblinSand.class, 1, 6, 12 );
		add( "tower", HermitCrabNoShell.class, 1, 2, 7 );
		add( "tower", LicteriaLasher.class, 0, 1, 99 );
		add( "tower", MagiCrab.class, 1, 9, 14 );
		add( "tower", Slugger.class, 1, 8, 14 );
		add( "tower", SkeletonArmored.class, 1, 12, 18 );
		add( "tower", SkeletonArmoredShielded.class, 1, 14, 20 );
		add( "tower", TimelessSpirit.class, 1, 10, 20 );
		add( "tower", CampRatLeader.class, 1, 4, 8 );
		add( "tower", CampRatArcher.class, 1, 3, 7 );
		add( "tower", CampRatKnife.class, 1, 2, 6 );
		add( "tower", CampRatMage.class, 1, 3, 7 );
		add( "tower", CampRatShield.class, 1, 4, 8 );
		add( "tower", ChiefRat.class, 1, 3, 8 );
		add( "tower", GoblinShaman.ShamanStrength.class, 1, 7, 12 );
		add( "tower", GoblinShaman.ShamanShield.class, 1, 7, 12 );
		add( "tower", GoblinShaman.ShamanRegen.class, 1, 7, 12 );
		//Lovecraft PD. Spawning goes through LovecraftSpawner, so these are all weight 0 (bestiary, sandbox and tests only)
		add( "lovecraft", KindSpirit.class, 0, 1, 99 );

		add( "lovecraft", GreyRat.class, 0, 1, 99 );
		add( "lovecraft", LivingPlantBlindWeed.class, 0, 1, 99 );
		add( "lovecraft", LivingPlantDewCatcher.class, 0, 1, 99 );
		add( "lovecraft", LivingPlantDreamFoil.class, 0, 1, 99 );
		add( "lovecraft", LivingPlantEarthRoot.class, 0, 1, 99 );
		add( "lovecraft", LivingPlantFireBloom.class, 0, 1, 99 );
		add( "lovecraft", LivingPlantIceCap.class, 0, 1, 99 );
		add( "lovecraft", LivingPlantSeedPod.class, 0, 1, 99 );
		add( "lovecraft", LivingPlantSorrowMoss.class, 0, 1, 99 );
		add( "lovecraft", LivingPlantStarFlower.class, 0, 1, 99 );
		add( "lovecraft", LivingPlantSteamweed.class, 0, 1, 99 );
		add( "lovecraft", LivingPlantStormVine.class, 0, 1, 99 );
		add( "lovecraft", LivingPlantSunGrass.class, 0, 1, 99 );
		add( "lovecraft", Murderer.class, 0, 1, 99 );
		add( "lovecraft", RedScorpion.class, 0, 1, 99 );
		add( "lovecraft", ElderThing.class, 0, 1, 99 );
		add( "lovecraft", Gardner.class, 0, 1, 99 );
		add( "lovecraft", Pepe.class, 0, 1, 99 );
		add( "lovecraft", BanditLord.class, 0, 1, 99 );
		add( "lovecraft", LivingPlantFadeLeaf.class, 0, 1, 99 );
		//Unleashed PD, depths mapped from its 36 floors to our 26. Weight 0 are boss fight parts and water vault monsters, sandbox and tests only
		add( "unleashed", GreenSnake.class, 2, 1, 3 );
		add( "unleashed", Velocirooster.class, 2, 3, 5 );
		add( "unleashed", SewerFly.class, 1, 2, 6 );
		add( "unleashed", SlimeBrown.class, 1, 3, 4 );
		add( "unleashed", SlimeRed.class, 1, 4, 5 );
		add( "unleashed", Zombie.class, 2, 6, 8 );
		add( "unleashed", Assassin.class, 1, 8, 9 );
		add( "unleashed", ClayGolem.class, 2, 12, 14 );
		add( "unleashed", SpiderBot.class, 2, 13, 14 );
		add( "unleashed", BrownWolf.class, 2, 16, 19 );
		add( "unleashed", GrayWolf.class, 1, 17, 19 );
		add( "unleashed", Yeti.class, 1, 18, 19 );
		add( "unleashed", IceDemon.class, 1, 19, 19 );
		add( "unleashed", LostSoul.class, 1, 22, 24 );
		add( "unleashed", Squid.class, 0, 1, 99 );
		add( "unleashed", Tinkerer.class, 0, 1, 99 );
		add( "unleashed", Minotaur.class, 0, 1, 99 );
		add( "unleashed", ChaosMage.class, 0, 1, 99 );
		add( "unleashed", DemonLord.class, 0, 1, 99 );
		add( "unleashed", BurningFist.class, 0, 1, 99 );
		add( "unleashed", RottingFist.class, 0, 1, 99 );
		add( "unleashed", Larva.class, 0, 1, 99 );
		add( "unleashed", Undead.class, 0, 1, 99 );
		add( "transformation", WildDread.class, 0, 1, 99 );
		add( "transformation", DarkMechanicalFist.class, 0, 1, 99 );
		add( "transformation", DeathButterfly.class, 0, 1, 99 );
		add( "transformation", Corpse.class, 0, 1, 99 );
		add( "transformation", ElfWineCup.class, 0, 1, 99 );
		add( "transformation", AlienatedPrismaticGuard.class, 0, 1, 99 );
		add( "transformation", Obscura.class, 0, 1, 99 );
		//transformation, the four disciples, their base class is abstract
		add( "transformation", ChaosDisciples.EyeBoss.class, 0, 1, 99 );
		add( "transformation", ChaosDisciples.RipperBoss.class, 0, 1, 99 );
		add( "transformation", ChaosDisciples.ScorpioBoss.class, 0, 1, 99 );
		add( "transformation", ChaosDisciples.SuccBoss.class, 0, 1, 99 );
		add( "transformation", WarriorBoss.class, 0, 1, 99 );
		add( "transformation", HuntressBoss.class, 0, 1, 99 );
		add( "transformation", RogueBoss.class, 0, 1, 99 );
		add( "transformation", GreatDemon.class, 0, 1, 99 );
		//transformation, the four gold minions, their base class is abstract
		add( "transformation", GoldBoss.MonkMaster.class, 0, 1, 99 );
		add( "transformation", GoldBoss.GoldElemental.class, 0, 1, 99 );
		add( "transformation", GoldBoss.GoldGolem.class, 0, 1, 99 );
		add( "transformation", GoldBoss.Thymor.class, 0, 1, 99 );
		add( "transformation", GreatShoper.class, 0, 1, 99 );
		add( "transformation", MyriadBlackShadow.class, 0, 1, 99 );
		//transformation, the surface town folk and the two heralds of the mod's last fights, sandbox only
		add( "transformation", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.GreatImp.class, 0, 1, 99 );
		add( "transformation", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.ShopBoss.class, 0, 1, 99 );
		add( "transformation", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Dongyusangyu.class, 0, 1, 99 );
		add( "transformation", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.DungeonDoctor.class, 0, 1, 99 );
		add( "transformation", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.SurfaceShopkeeper.class, 0, 1, 99 );
		//Pixel Spacebase. No worlds yet (its art is being redrawn), so weight 0, sandbox and tests only
		add( "spacebase", BithAcolyte.class, 0, 1, 99 );
		add( "spacebase", ConfusedShapeshifter.class, 0, 1, 99 );
		add( "spacebase", Drone.class, 0, 1, 99 );
		add( "spacebase", Facehugger.class, 0, 1, 99 );
		add( "spacebase", HolodeckLegionary.class, 0, 1, 99 );
		add( "spacebase", Jawar.class, 0, 1, 99 );
		add( "spacebase", JedaKnight.class, 0, 1, 99 );
		add( "spacebase", MaintenanceCrawler.class, 0, 1, 99 );
		add( "spacebase", NewbornElemental.class, 0, 1, 99 );
		add( "spacebase", OldWarBot.class, 0, 1, 99 );
		add( "spacebase", OuterColonyPsion.class, 0, 1, 99 );
		add( "spacebase", OuterColonyScout.class, 0, 1, 99 );
		add( "spacebase", OuterColonyShockTrooper.class, 0, 1, 99 );
		add( "spacebase", ReplicatorSwarm.class, 0, 1, 99 );
		add( "spacebase", RupturedCrewSuit.class, 0, 1, 99 );
		add( "spacebase", SignalLeech.class, 0, 1, 99 );
		add( "spacebase", SignalSiren.class, 0, 1, 99 );
		add( "spacebase", SiphonDrone.class, 0, 1, 99 );
		add( "spacebase", Turret.class, 0, 1, 99 );
		add( "spacebase", WarMachine.class, 0, 1, 99 );
		add( "spacebase", Xenomorph.class, 0, 1, 99 );
		add( "spacebase", ArmoredCrawler.class, 0, 1, 99 );
		add( "sps", IceBug.class, 0, 1, 99 );
		add( "sps", BambooMob.class, 0, 1, 99 );
		add( "sps", Zombie.class, 0, 1, 99 );
		add( "sps", DemonRabbit.class, 0, 1, 99 );
		add( "sps", FireRabbit.class, 0, 1, 99 );
		add( "sps", Shit.class, 0, 1, 99 );
		add( "magicling", BrownBat.class, 1, 1, 99 );
		add( "magicling", DimandMimic.class, 1, 1, 99 );
		add( "magicling", NewBornCrab.class, 1, 1, 99 );
		add( "magicling", ShopGuard.class, 1, 1, 99 );
		add( "magicling", ShopGuardEye.class, 1, 1, 99 );

		add( "magicling", Aggregatus.class, 1, 1, 99 );
		add( "magicling", BlackHost.class, 1, 1, 99 );
		add( "magicling", BlackSoul.class, 1, 1, 99 );
		add( "magicling", BlueWraith.class, 1, 1, 99 );
		add( "magicling", Crumb.class, 1, 1, 99 );
		add( "magicling", DM111.class, 1, 1, 99 );
		add( "magicling", DrTerror.class, 1, 1, 99 );
		add( "magicling", DwarfFuze.class, 1, 1, 99 );
		add( "magicling", FireGhost.class, 1, 1, 99 );
		add( "magicling", FireGhostDead.class, 1, 1, 99 );
		add( "magicling", FlameB01.class, 1, 1, 99 );
		add( "magicling", FlowerSlime.class, 1, 1, 99 );
		add( "magicling", FourStone.class, 1, 1, 99 );
		add( "magicling", GhoulPlus.class, 1, 1, 99 );
		add( "magicling", GiantFlowerSlime.class, 1, 1, 99 );
		add( "magicling", GnollHero.class, 1, 1, 99 );
		add( "magicling", GreenSlting.class, 1, 1, 99 );
		add( "magicling", GuardCapital.class, 1, 1, 99 );
		add( "magicling", GunHuntsman.class, 1, 1, 99 );
		add( "magicling", Ice_Scorpio.class, 1, 1, 99 );
		add( "magicling", Katydid.class, 1, 1, 99 );
		add( "magicling", NecroPioneer.class, 1, 1, 99 );
		add( "magicling", NecroScout.class, 1, 1, 99 );
		add( "magicling", NecroWarlock.class, 1, 1, 99 );
		add( "magicling", OGPDNQHZ.class, 1, 1, 99 );
		add( "magicling", RedNecromancer.class, 1, 1, 99 );
		add( "magicling", SRPDHBLR.class, 1, 1, 99 );
		add( "magicling", SRPDICLR.class, 1, 1, 99 );
		add( "magicling", SRPDICLRPRO.class, 1, 1, 99 );
		add( "magicling", Salamander.class, 1, 1, 99 );
		add( "magicling", ShadowHunstman.class, 1, 1, 99 );
		add( "magicling", ShopGuardDead.class, 1, 1, 99 );
		add( "magicling", Slime_Lg.class, 1, 1, 99 );
		add( "magicling", Slime_Lt.class, 1, 1, 99 );
		add( "magicling", Slime_Orange.class, 1, 1, 99 );
		add( "magicling", Slime_Sz.class, 1, 1, 99 );
		add( "magicling", SmallLight.class, 1, 1, 99 );
		add( "magicling", SmallSkeletonDemon.class, 1, 1, 99 );
		add( "magicling", SuccubusQueen.class, 1, 1, 99 );
		add( "magicling", VeryColdRat.class, 1, 1, 99 );
		add( "magicling", WarlockHead.class, 1, 1, 99 );
		add( "magicling", Worm.class, 1, 1, 99 );
		add( "magicling", WormWhyHuman.class, 1, 1, 99 );
		add( "magicling", XTG100.class, 1, 1, 99 );
		add( "magicling", XTG200.class, 1, 1, 99 );
		add( "magicling", BloodBat.class, 1, 1, 99 );
		add( "magicling", BoomSkull.class, 1, 1, 99 );
		add( "magicling", CrossDiedTower.class, 1, 1, 99 );
		add( "magicling", DeadEye.class, 1, 1, 99 );
		add( "magicling", DemonLord.class, 1, 1, 99 );
		add( "magicling", Drake.class, 1, 1, 99 );
		add( "magicling", Fire_Scorpio.class, 1, 1, 99 );
		add( "magicling", GiantWorm.class, 1, 1, 99 );
		add( "magicling", Gorgon.class, 1, 1, 99 );
		add( "magicling", Mayfly.class, 1, 1, 99 );
		add( "magicling", OGPDLLS.class, 1, 1, 99 );
		add( "magicling", OGPDZSLS.class, 1, 1, 99 );
		add( "magicling", Prisoner.class, 1, 1, 99 );
		add( "magicling", RoyalGuard.class, 1, 1, 99 );
		add( "magicling", Slime_Qs.class, 1, 1, 99 );
		add( "magicling", Slime_Sn.class, 1, 1, 99 );
		add( "magicling", ThiefSoul.class, 1, 1, 99 );
		add( "magicling", TribemanOld.class, 1, 1, 99 );
		add( "magicling", ColdGurad.class, 1, 1, 99 );
		add( "magicling", GraveRat.class, 1, 1, 99 );
		add( "magicling", HelpTeleportPoint.class, 1, 1, 99 );
		add( "magicling", TPDoor.class, 1, 1, 99 );
		add( "magicling", DwarfSolider.class, 1, 1, 99 );
		add( "magicling", MolotovHuntsman.class, 1, 1, 99 );
		add( "magicling", NecroArcher.class, 1, 1, 99 );
		add( "magicling", NecroGuard.class, 1, 1, 99 );
		add( "magicling", xykl.class, 1, 1, 99 );
		add( "magicling", MiniCerberus.class, 1, 1, 99 );
		add( "magicling", MiniSaka.class, 1, 1, 99 );
		add( "magicling", ShieldHuntsman.class, 1, 1, 99 );
		add( "magicling", NecroCavalry.class, 1, 1, 99 );
		add( "goblins", Assassin.class, 0, 1, 99 );
		add( "goblins", BurningFist.class, 0, 1, 99 );
		add( "goblins", ChaosMage.class, 0, 1, 99 );
		add( "goblins", ClayGolem.class, 0, 1, 99 );
		add( "goblins", DemonLord.class, 0, 1, 99 );
		add( "goblins", Fingers.class, 0, 1, 99 );
		add( "goblins", GreenSnake.class, 0, 1, 99 );
		add( "goblins", IceDemon.class, 0, 1, 99 );
		add( "goblins", Larva.class, 0, 1, 99 );
		add( "goblins", LostSoul.class, 0, 1, 99 );
		add( "goblins", Minotaur.class, 0, 1, 99 );
		add( "goblins", RedSnake.class, 0, 1, 99 );
		add( "goblins", RottingFist.class, 0, 1, 99 );
		add( "goblins", SlimeBrown.class, 0, 1, 99 );
		add( "goblins", SlimeRed.class, 0, 1, 99 );
		add( "goblins", SpiderBot.class, 0, 1, 99 );
		add( "goblins", Tinkerer.class, 0, 1, 99 );
		add( "goblins", Undead.class, 0, 1, 99 );
		add( "goblins", Yeti.class, 0, 1, 99 );
		add( "goblins", Zombie.class, 0, 1, 99 );
		add( "goblins", Velocirooster.class, 0, 1, 99 );
		add( "goblins", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.Bunny.class, 0, 1, 99 );
		add( "goblins", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.Fairy.class, 0, 1, 99 );
		add( "goblins", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.FireKlik.class, 0, 1, 99 );
		add( "goblins", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.FrostKlik.class, 0, 1, 99 );
		add( "goblins", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.MetalKlik.class, 0, 1, 99 );
		add( "goblins", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.PhaseKlik.class, 0, 1, 99 );
		add( "goblins", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.PoisonKlik.class, 0, 1, 99 );
		add( "goblins", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.SpiderKlik.class, 0, 1, 99 );
		add( "goblins", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.SugarplumFairy.class, 0, 1, 99 );
		add( "goblins", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.PetBee.class, 0, 1, 99 );
		add( "goblins", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.Scorpion.class, 0, 1, 99 );
		add( "goblins", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.PetVelocirooster.class, 0, 1, 99 );
		add( "deep", Assassin.class, 0, 1, 99 );
		add( "deep", DarkRat.class, 1, 3, 8 );
		add( "deep", Dummy.class, 0, 1, 99 );
		add( "deep", Gangster.class, 1, 6, 12 );
		add( "deep", GnollKnight.class, 1, 3, 9 );
		add( "deep", GnollMarksman.class, 1, 2, 8 );
		add( "deep", GnollNinja.class, 0, 1, 99 );
		add( "deep", GnollThief.class, 1, 2, 8 );
		add( "deep", Goolem.class, 0, 1, 99 );
		add( "deep", Grieflord.class, 0, 1, 99 );
		add( "deep", Idol.class, 0, 1, 99 );
		add( "deep", KeyCreature.class, 0, 1, 99 );
		add( "deep", LesserRat.class, 1, 1, 5 );
		add( "deep", MandrakeClone.class, 0, 1, 99 );
		add( "deep", NewbornElemental.class, 0, 1, 99 );
		add( "deep", PurpleMushroom.class, 1, 4, 10 );
		add( "deep", RedMushroom.class, 1, 4, 10 );
		add( "deep", Sludge.class, 1, 4, 9 );
		add( "deep", Spirit.class, 1, 1, 4 );
		add( "deep", SummonIdol.class, 0, 1, 99 );
		add( "deep", Terminator.class, 0, 1, 99 );
		add( "deep", VoodooIdol.class, 0, 1, 99 );
		add( "deep", YellowMushroom.class, 1, 4, 10 );
		add( "deep", GangBoss.class, 0, 1, 99 );
		add( "deep", GnollNinja1.class, 0, 1, 99 );
		add( "deep", Mandrake.class, 1, 2, 8 );
		add( "deep", ShamanLeader.class, 0, 1, 99 );
		add( "deep", GnollNinja2.class, 0, 1, 99 );
		add( "deep", ElderShroom.class, 0, 1, 99 );
		add( "deep", FlyingCat.class, 0, 1, 99 );
		//rearranged, Re-ARranged PD. Its labs were floors 26 to 29, its Labs world here is floors 21 to 24
		add( "rearranged", Soldier.class, 2, 21, 24 );
		add( "rearranged", Researcher.class, 2, 21, 24 );
		add( "rearranged", Suppression.class, 1, 22, 24 );
		add( "rearranged", Tank.class, 1, 23, 24 );
		add( "rearranged", Medic.class, 1, 23, 24 );
		add( "rearranged", SWAT.class, 0, 1, 99 );
		add( "rearranged", TempleEbonyMimic.class, 0, 1, 99 );
		//summoning, Summoning PD Reincarnated. Its Abyss world spawns the abyss chapter monsters on floors 21 to 24.
		//Dog, the exploding mouse and the dwarf guard are on the floors the mod used, the rest come from rooms, staffs or bosses
		add( "summoning", Dog.class, 2, 2, 4 );
		add( "summoning", ExplodingTNT.class, 1, 11, 12 );
		add( "summoning", DwarfGuardMob.class, 1, 15, 16 );
		add( "summoning", SpectreRat.class, 2, 21, 99 );
		add( "summoning", DarkestElf.class, 2, 21, 99 );
		add( "summoning", GhostChicken.class, 2, 21, 99 );
		add( "summoning", Phantom.class, 2, 21, 99 );
		add( "summoning", BlinkingMan.class, 2, 21, 99 );
		add( "summoning", Trappet.class, 2, 21, 99 );
		add( "summoning", MajesticGuard.class, 0, 1, 99 );
		add( "summoning", Wolf.class, 0, 1, 99 );
		add( "summoning", TribeGnoll.class, 0, 1, 99 );
		add( "summoning", SpectralShaman.class, 0, 1, 99 );
		add( "summoning", WardingWraith.class, 0, 1, 99 );
		add( "summoning", FinalFroggit.class, 0, 1, 99 );
		add( "summoning", AbyssalSpawner.class, 0, 1, 99 );
		add( "summoning", LostSpirit.class, 0, 1, 99 );
		add( "summoning", AbyssalNightmare.class, 0, 1, 99 );
		//summoning, staff minions and the Knight's stand, allies of the hero
		add( "summoning", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.minions.Chicken.class, 0, 1, 99 );
		add( "summoning", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.minions.Froggit.class, 0, 1, 99 );
		add( "summoning", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.minions.FrostElemental.class, 0, 1, 99 );
		add( "summoning", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.minions.GnollHunter.class, 0, 1, 99 );
		add( "summoning", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.minions.GooMinion.class, 0, 1, 99 );
		add( "summoning", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.minions.GrayRat.class, 0, 1, 99 );
		add( "summoning", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.minions.Hacatu.class, 0, 1, 99 );
		add( "summoning", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.minions.Imp.class, 0, 1, 99 );
		add( "summoning", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.minions.ImpQueen.class, 0, 1, 99 );
		add( "summoning", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.minions.Mimic.class, 0, 1, 99 );
		add( "summoning", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.minions.Robo.class, 0, 1, 99 );
		add( "summoning", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.minions.Sheep.class, 0, 1, 99 );
		add( "summoning", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.minions.Skele.class, 0, 1, 99 );
		add( "summoning", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.minions.Slime.class, 0, 1, 99 );
		add( "summoning", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.minions.SoulFlame.class, 0, 1, 99 );
		add( "summoning", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.minions.Wizard.class, 0, 1, 99 );
		add( "summoning", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.minions.stationary.GasterBlaster.class, 0, 1, 99 );
		add( "summoning", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.minions.stationary.MagicMissileMinion.class, 0, 1, 99 );
		add( "summoning", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.minions.stationary.RoseWraith.class, 0, 1, 99 );
		add( "summoning", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.GoatClone.class, 0, 1, 99 );
		add( "summoning", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.ChaosSaber.class, 0, 1, 99 );
		add( "summoning", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Stenchy.class, 0, 1, 99 );
		add( "tower", FreeBee.class, 0, 1, 99 );
		add( "tower", MagicShard.class, 0, 1, 99 );
		add( "tower", GorematiaSpirit.class, 0, 1, 99 );
		add( "tower", TowerWisp.class, 0, 1, 99 );
		//rebluearchive, Re Blue Archive PD. The Fox squad only comes when Shiroko robs a shop (RBAShims.shopkeeperRun), so weight 0
		add( "rebluearchive", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.fox.RBAYukino.class, 0, 1, 99 );
		add( "rebluearchive", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.fox.RBANiko.class, 0, 1, 99 );
		add( "rebluearchive", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.fox.RBAKurumi.class, 0, 1, 99 );
		add( "rebluearchive", com.shatteredpixel.shatteredpixeldungeon.actors.mobs.fox.RBAOtogi.class, 0, 1, 99 );
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
