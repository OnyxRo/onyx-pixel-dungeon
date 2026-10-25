/*
 * Too Cruel Pixel Dungeon Trials (GPL-3.0, by its authors), ported into Onyx's Pixel Dungeon.
 * Based on Shattered Pixel Dungeon by Evan Debenham.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WellWater;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.toocruel.ExterminationItemLock;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.toocruel.PatronSaintsBlob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invulnerability;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Levitation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SuperNovaTracker;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.toocruel.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroAction;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Snake;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Statue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.TcHolderMimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.TcStoredHeapData;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.Transmuting;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.Stylus;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.MasterThievesArmband;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ArcaneBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Firebomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Flashbang;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.FrostBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.HolyBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Noisemaker;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.RegrowthBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.SmokeBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.WoollyBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.Key;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.ExoticPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfMastery;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.AquaBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMysticalEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Alchemize;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.CurseInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.MagicalInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.PhaseShift;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ReclaimTrap;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Recycle;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAugmentation;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfClairvoyance;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfIntuition;
import com.shatteredpixel.shatteredpixeldungeon.items.toocruel.IOU;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.ExoticCrystals;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.RatSkull;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.SaltCube;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.ShardOfOblivion;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.ThirteenLeafClover;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.CursedWand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.Dewdrop;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.connection.ConnectionRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.entrance.EntranceRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GatewayTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrimTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WornDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.Callback;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

//Too Cruel Pixel Dungeon Trials, its challenge modifiers as game modes. Every hook shared Shattered code
//calls for these modes lives here, those calls are marked //toocruel.
public class TooCruel {

	/*****************************************************************
	 * The modes, named after the mod's own text. The number is the mod's modifier id, used by its trials.
	 *****************************************************************/

	public static final String CARDINAL_DISABILITY = "tc_cardinal_disability";
	public static final String RACING_THE_DEATH    = "tc_racing_the_death";
	public static final String HORDE               = "tc_horde";
	public static final String INVASION            = "tc_invasion";
	public static final String GREAT_MIGRATION     = "tc_great_migration";
	public static final String MUTAGEN             = "tc_mutagen";
	public static final String EVOLUTION           = "tc_evolution";
	public static final String ROTTEN_LUCK         = "tc_rotten_luck";
	public static final String ARROWHEAD           = "tc_arrowhead";
	public static final String THUNDERSTRUCK       = "tc_thunderstruck";
	public static final String SECOND_TRY          = "tc_second_try";
	public static final String CRYSTAL_SHELTER     = "tc_crystalline_shelter";
	public static final String CRYSTAL_BLOOD       = "tc_crystalline_blood";
	public static final String DEEPER_DANGER       = "tc_deeper_danger";
	public static final String HEAD_START          = "tc_head_start";
	public static final String BLINDNESS           = "tc_blindness";
	public static final String BLOODBAG            = "tc_bloodbag";
	public static final String REVENGE             = "tc_revenge_rage";
	public static final String REVENGE_FURY        = "tc_revenge_fury";
	public static final String PREPARED_ENEMIES    = "tc_prepared_enemies";
	public static final String REPEATER            = "tc_repeater";
	public static final String DUPLICATOR          = "tc_duplicator";
	public static final String EXTREME_CAUTION     = "tc_extreme_caution";
	public static final String COLOSSEUM           = "tc_colosseum";
	public static final String PATRON_SAINTS       = "tc_patron_saints";
	public static final String PERSISTENT_SAINTS   = "tc_persistent_saints";
	public static final String HOLY_WATER          = "tc_holy_water";
	public static final String INTOXICATION        = "tc_intoxication";
	public static final String PLAGUE              = "tc_plague";
	public static final String TOXIC_WATER         = "tc_bad_plumbing";
	public static final String CERTAINTY_OF_STEEL  = "tc_certainty_of_steel";
	public static final String PARADOX_LEVELGEN    = "tc_builder_paradox";
	public static final String RETIERED            = "tc_retiered";
	public static final String UNTIERED            = "tc_untiered";
	public static final String UNSTABLE_ACCESSORIES= "tc_unstable_accessories";
	public static final String PANDEMONIUM         = "tc_pandemonium";
	public static final String BARRIER_BREAKER     = "tc_barrier_breaker";
	public static final String MOLES               = "tc_moles";
	public static final String LOFT                = "tc_dungeon_loft";
	public static final String BULKY_FRAME         = "tc_bulky_frame";
	public static final String SLIDING             = "tc_slippery_floor";
	public static final String INSOMNIA            = "tc_insomnia";
	public static final String LOOT_PARADISE       = "tc_loot_paradise";
	public static final String GOLDEN_COLOSSUS     = "tc_golden_colossus";
	public static final String BOMBERMOB           = "tc_bombermob";
	public static final String CURSED              = "tc_cursed";
	public static final String CURSE_MAGNET        = "tc_curse_magnet";
	public static final String SHROUDING_PRESENCE  = "tc_shrouding_presence";
	public static final String EXTERMINATION       = "tc_extermination";
	public static final String POSTPAID_LOOT       = "tc_postpaid_loot";
	public static final String MIMICS              = "tc_fake_dungeon";
	public static final String MIMICS_ALL          = "tc_fort_knox";
	public static final String MIMICS_GRIND        = "tc_treasure_chest_party_quest";
	public static final String CONSTELLATION       = "tc_constellation";
	public static final String REPOPULATION        = "tc_repopulation";
	public static final String RESURRECTION        = "tc_resurrection";
	public static final String FRACTAL_HIVE        = "tc_fractal_hive";
	public static final String CROOKED_DIE         = "tc_crooked_die";
	public static final String CRUMBLED_STAIRS     = "tc_crumbled_stairs";
	public static final String MULTICLASSING       = "tc_multiclassing";
	public static final String EXOTIC_GOODS        = "tc_exotic_goods";
	public static final String DROUGHT             = "tc_drought";
	public static final String OVER_THE_EDGE       = "tc_over_the_edge";
	public static final String WHIPLASH            = "tc_whiplash";
	public static final String CASUAL_APPROACH     = "tc_casual_approach";
	public static final String LET_THEM_REST       = "tc_let_them_rest";
	public static final String ETERNAL_FLAMES      = "tc_eternal_flames";
	public static final String DOMAIN_OF_HELL      = "tc_domain_of_hell";
	public static final String JACK_IN_A_BOX       = "tc_jack_in_the_box";
	public static final String BOXED               = "tc_boxed";
	public static final String RECURSIVE_HIERARCHY = "tc_recursive_hierarchy";
	public static final String CROWD_DIVERSITY     = "tc_crowd_diversity";
	public static final String IN_YOUR_FACE        = "tc_in_your_face";
	public static final String BODY_TRAPS          = "tc_a_farewell_gift";
	public static final String PERFECT_INFORMATION = "tc_perfect_information";
	public static final String SAFETY_BUFFER       = "tc_safety_buffer";
	public static final String SKELETON_CREW       = "tc_skeleton_crew";
	public static final String ABANDONED_SHIP      = "tc_abandoned_ship";
	public static final String TIER_UP             = "tc_tier_up";
	public static final String UNINSPIRED_TO_LEARN = "tc_uninspired_to_learn";
	public static final String INFERTILITY         = "tc_infertility";
	public static final String HYDRA               = "tc_hydra";
	public static final String PROTECTED_GOODS     = "tc_protected_goods";
	public static final String COMPLETE_KNOWLEDGE  = "tc_complete_knowledge";
	public static final String THOUGHTLESS         = "tc_thoughtless";
	public static final String FORCED_PACIFISM     = "tc_forced_pacifism";
	public static final String BLESSING_FOR_THE_WORTHY = "tc_blessing_for_the_worthy";

	//the mod's modifier ids, 0 to 8 are Shattered's challenges, 66 and 93 are left out (see PORTING.md)
	private static final String[] BY_NUMBER = new String[98];
	private static final LinkedHashMap<String, String[]> DEPENDS = new LinkedHashMap<>();
	private static final String[][] CONFLICTS = { { ABANDONED_SHIP, HORDE }, { INFERTILITY, REPOPULATION } };
	//Shattered's challenges by the mod's ids
	private static final int[] VANILLA = { Challenges.NO_FOOD, Challenges.NO_ARMOR, Challenges.NO_HEALING, Challenges.NO_HERBALISM,
			Challenges.SWARM_INTELLIGENCE, Challenges.DARKNESS, Challenges.NO_SCROLLS, Challenges.CHAMPION_ENEMIES, Challenges.STRONGER_BOSSES };

	private static void mode( int number, String id, String... deps ) {
		BY_NUMBER[number] = id;
		if (deps.length > 0) DEPENDS.put( id, deps );
		GameModes.register( id, GameModes.MOD_TOOCRUEL, 1, null );
	}

	//called once from GameModes while it lists its modes
	public static void register() {
		mode( 9, CARDINAL_DISABILITY );
		mode( 10, RACING_THE_DEATH );
		mode( 11, HORDE );
		mode( 12, INVASION );
		mode( 13, GREAT_MIGRATION, INVASION );
		mode( 14, MUTAGEN );
		mode( 15, EVOLUTION, MUTAGEN );
		mode( 16, ROTTEN_LUCK );
		mode( 17, ARROWHEAD );
		mode( 18, THUNDERSTRUCK, ARROWHEAD );
		mode( 19, SECOND_TRY );
		mode( 20, CRYSTAL_SHELTER );
		mode( 21, CRYSTAL_BLOOD );
		mode( 22, DEEPER_DANGER );
		mode( 23, HEAD_START );
		mode( 24, BLINDNESS );
		mode( 25, BLOODBAG );
		mode( 26, REVENGE );
		mode( 27, REVENGE_FURY );
		mode( 28, PREPARED_ENEMIES );
		mode( 29, REPEATER );
		mode( 30, DUPLICATOR );
		mode( 83, BODY_TRAPS );
		mode( 31, EXTREME_CAUTION );
		mode( 32, COLOSSEUM );
		mode( 33, PATRON_SAINTS );
		mode( 34, PERSISTENT_SAINTS, PATRON_SAINTS );
		mode( 35, HOLY_WATER );
		mode( 36, INTOXICATION );
		mode( 37, PLAGUE );
		mode( 38, TOXIC_WATER );
		mode( 39, CERTAINTY_OF_STEEL );
		mode( 52, GOLDEN_COLOSSUS, CERTAINTY_OF_STEEL );
		mode( 40, PARADOX_LEVELGEN );
		mode( 41, RETIERED );
		mode( 42, UNTIERED, RETIERED );
		mode( 88, TIER_UP, UNTIERED );
		mode( 43, UNSTABLE_ACCESSORIES );
		mode( 44, PANDEMONIUM );
		mode( 45, BARRIER_BREAKER );
		mode( 46, MOLES );
		mode( 47, LOFT );
		mode( 48, BULKY_FRAME );
		mode( 56, SHROUDING_PRESENCE, BULKY_FRAME );
		mode( 49, SLIDING );
		mode( 50, INSOMNIA );
		mode( 51, LOOT_PARADISE );
		mode( 53, BOMBERMOB );
		mode( 62, CONSTELLATION, BOMBERMOB );
		mode( 54, CURSED );
		mode( 55, CURSE_MAGNET, CURSED );
		mode( 57, EXTERMINATION );
		mode( 58, POSTPAID_LOOT, EXTERMINATION );
		mode( 59, MIMICS );
		mode( 60, MIMICS_ALL, MIMICS );
		mode( 61, MIMICS_GRIND, MIMICS );
		mode( 78, JACK_IN_A_BOX );
		mode( 80, RECURSIVE_HIERARCHY, JACK_IN_A_BOX );
		mode( 79, BOXED );
		mode( 63, REPOPULATION );
		mode( 64, RESURRECTION, REPOPULATION );
		mode( 65, FRACTAL_HIVE, REPOPULATION );
		mode( 90, INFERTILITY );
		mode( 91, HYDRA, INFERTILITY );
		mode( 67, CROOKED_DIE );
		mode( 68, CRUMBLED_STAIRS );
		mode( 69, MULTICLASSING );
		mode( 70, EXOTIC_GOODS );
		mode( 71, DROUGHT );
		mode( 72, OVER_THE_EDGE );
		mode( 73, WHIPLASH );
		mode( 74, CASUAL_APPROACH );
		mode( 75, LET_THEM_REST );
		mode( 76, ETERNAL_FLAMES );
		mode( 77, DOMAIN_OF_HELL );
		mode( 81, CROWD_DIVERSITY );
		mode( 82, IN_YOUR_FACE );
		mode( 84, PERFECT_INFORMATION );
		mode( 94, COMPLETE_KNOWLEDGE );
		mode( 95, THOUGHTLESS, COMPLETE_KNOWLEDGE );
		mode( 85, SAFETY_BUFFER );
		mode( 86, SKELETON_CREW );
		mode( 87, ABANDONED_SHIP, SKELETON_CREW );
		mode( 89, UNINSPIRED_TO_LEARN );
		mode( 96, FORCED_PACIFISM, UNINSPIRED_TO_LEARN );
		mode( 92, PROTECTED_GOODS );
		mode( 97, BLESSING_FOR_THE_WORTHY );
	}

	public static boolean on( String id ) {
		return GameModes.on( id );
	}

	public static boolean isMode( String id ) {
		return id != null && id.startsWith( "tc_" );
	}

	//keeps the mod's rules, a mode turns on what it needs, turning one off turns off what needs it,
	//and the two conflicting pairs cannot both be on. Called from GameModes.set.
	public static void onSet( String id, int lvl ) {
		if (!isMode( id )) return;
		if (lvl > 0) {
			String[] deps = DEPENDS.get( id );
			if (deps != null) for (String d : deps) if (!on( d )) GameModes.set( d, 1 );
			for (String[] pair : CONFLICTS) {
				if (pair[0].equals( id ) && on( pair[1] )) GameModes.set( pair[1], 0 );
				if (pair[1].equals( id ) && on( pair[0] )) GameModes.set( pair[0], 0 );
			}
		} else {
			for (Map.Entry<String, String[]> e : DEPENDS.entrySet()) {
				for (String d : e.getValue()) {
					if (d.equals( id ) && on( e.getKey() )) GameModes.set( e.getKey(), 0 );
				}
			}
		}
	}

	public static String[] dependencies( String id ) {
		String[] d = DEPENDS.get( id );
		return d == null ? new String[0] : d;
	}

	/*****************************************************************
	 * Trials, the mod's built in sets of modifiers
	 *****************************************************************/

	public static class Trial {
		public final String name;
		public final String group;
		public final int[] ids;

		Trial( String group, String name, int... ids ) {
			this.group = group;
			this.name = name;
			this.ids = ids;
		}
	}

	public static final String[] TRIAL_GROUPS = { "normal", "hard", "extreme" };

	public static final Trial[] TRIALS = {
			new Trial( "normal", "Tier List", 8, 41 ),
			new Trial( "normal", "Bloodbath", 17, 25 ),
			new Trial( "normal", "Great Faith", 1, 33 ),
			new Trial( "normal", "Withered Garden", 3, 9 ),
			new Trial( "normal", "Champion's Path", 7, 8 ),
			new Trial( "normal", "Downgrade", 6, 41, 42 ),
			new Trial( "normal", "Amethyst", 20, 21 ),
			new Trial( "normal", "Clumsiness Unforgiven", 16, 28, 45, 49 ),
			new Trial( "normal", "Minefield", 29, 31 ),
			new Trial( "normal", "Toxic Spillage", 36, 38 ),
			new Trial( "normal", "Unarmored Statue", 0, 1, 19, 39 ),
			new Trial( "hard", "Boss Hunt", 8, 23, 41, 42, 86, 88, 89, 92, 93 ),
			new Trial( "hard", "9chal", 0, 1, 2, 3, 4, 5, 6, 7, 8 ),
			new Trial( "hard", "Mimic Madness", 39, 51, 52, 57, 58, 59, 60 ),
			new Trial( "hard", "Stargazing", 53, 62, 63, 65 ),
			new Trial( "hard", "Apocalypse", 3, 4, 14, 50 ),
			new Trial( "hard", "Wall Breakers", 4, 46 ),
			new Trial( "hard", "Operation Bloodmoon", 4, 11, 14, 15, 26, 27 ),
			new Trial( "hard", "The Dark Ages", 5, 24, 37, 38 ),
			new Trial( "hard", "Roarborers", 26, 46, 50 ),
			new Trial( "hard", "The Unblessed Machine", 1, 2, 11, 23, 28, 39, 45 ),
			new Trial( "hard", "Bastard of Shadows", 10, 17, 24, 41, 42, 47 ),
			new Trial( "hard", "Academy Dropout", 6, 40, 41, 42, 43, 44 ),
			new Trial( "extreme", "The Broken Shield", 2, 17, 18, 51, 59, 61 ),
			new Trial( "extreme", "The Challenger", 7, 8, 23, 32, 41 ),
			new Trial( "extreme", "Extinction Event", 12, 13, 22, 23, 29, 30, 31, 40 ),
			new Trial( "extreme", "The Holy City", 1, 7, 33, 34, 35 ),
			new Trial( "extreme", "Teh Chariot", 4, 11, 23, 46, 48, 49 ),
	};

	//turns on a trial's modes and Shattered challenges, turning off every other Too Cruel mode
	public static void applyTrial( Trial t ) {
		for (String id : new ArrayList<>( GameModes.active.keySet() )) {
			if (isMode( id )) GameModes.active.remove( id );
		}
		int challenges = 0;
		for (int n : t.ids) {
			if (n < VANILLA.length) {
				challenges |= VANILLA[n];
			} else if (n < BY_NUMBER.length && BY_NUMBER[n] != null) {
				GameModes.set( BY_NUMBER[n], 1 );
			}
		}
		SPDSettings.challenges( challenges );
	}

	//the trial's modifiers by name, for the trial list
	public static String trialSummary( Trial t ) {
		StringBuilder sb = new StringBuilder();
		for (int n : t.ids) {
			String name;
			if (n < VANILLA.length) name = Messages.get( Challenges.class, Challenges.NAME_IDS[n] );
			else if (BY_NUMBER[n] != null) name = Messages.get( GameModes.class, BY_NUMBER[n] );
			else name = Messages.get( TooCruel.class, "left_out_" + n );
			if (sb.length() > 0) sb.append( "\n" );
			sb.append( "_-_ " ).append( name );
		}
		return sb.toString();
	}

	/*****************************************************************
	 * Shared helpers
	 *****************************************************************/

	//true while a floor is being built, monsters and items then belong to a level that is not Dungeon.level yet
	public static boolean levelBuilding = false;

	public static boolean bossOrSpecial() {
		return Dungeon.bossLevel() || Dungeon.depth == 26 || Dungeon.branch != 0;
	}

	public static boolean[] fovOf( Char ch ) {
		if (ch.fieldOfView == null || ch.fieldOfView.length != Dungeon.level.length()) {
			ch.fieldOfView = new boolean[Dungeon.level.length()];
		}
		Dungeon.level.updateFieldOfView( ch, ch.fieldOfView );
		return ch.fieldOfView;
	}

	public static void furrowCell( Level level, int cell ) {
		if (level.map[cell] == Terrain.HIGH_GRASS || level.map[cell] == Terrain.FURROWED_GRASS) {
			level.map[cell] = Terrain.GRASS;
			level.losBlocking[cell] = false;
		}
	}

	public static void forceBeckon( Char ch, int cell ) {
		TcStoredHeapData.forceBeckon( ch, cell );
	}

	public static void showTransmuted( Hero hero, Item item, Item result ) {
		if (GameScene.scene != null) {
			Transmuting.show( hero, item, result );
			if (hero.sprite != null) hero.sprite.emitter().start( Speck.factory( Speck.CHANGE ), 0.2f, 10 );
		}
		GLog.p( Messages.get( ScrollOfTransmutation.class, "morph" ) );
	}

	//transmutes an item the hero carries or wears, keeping it worn and in its quickslot
	public static Item transmute( Item item, Hero hero ) {
		Item result = ScrollOfTransmutation.changeItem( item );
		if (result == null) return null;
		if (result != item) {
			int slot = Dungeon.quickslot.getSlot( item );
			if (item.isEquipped( hero ) && item instanceof EquipableItem && result instanceof EquipableItem) {
				item.cursed = false;
				if (item instanceof Artifact && result instanceof Ring) {
					((EquipableItem) item).doUnequip( hero, false );
					if (!result.collect()) Dungeon.level.drop( result, hero.pos );
				} else if (item instanceof KindOfWeapon && hero.belongings.secondWep() == item && result instanceof KindOfWeapon) {
					((EquipableItem) item).doUnequip( hero, false );
					((KindOfWeapon) result).equipSecondary( hero );
				} else {
					((EquipableItem) item).doUnequip( hero, false );
					((EquipableItem) result).doEquip( hero );
				}
				hero.spend( -hero.cooldown() );
			} else {
				item.detach( hero.belongings.backpack );
				if (!result.collect()) {
					Dungeon.level.drop( result, hero.pos );
				} else if (result.stackable && hero.belongings.getSimilar( result ) != null) {
					result = hero.belongings.getSimilar( result );
				}
			}
			if (slot != -1 && result.defaultAction() != null && hero.belongings.contains( result )) {
				Dungeon.quickslot.setSlot( slot, result );
			}
		}
		if (result.isIdentified()) Catalog.setSeen( result.getClass() );
		showTransmuted( hero, item, result );
		return result;
	}

	//Domain of Hell, standing in fire with Brimstone sets off a bomb and a curse
	public static void scorchedEarth( Char target, Item toCurse ) {
		new Bomb().explode( target.pos );
		Ballistica bal = new Ballistica( target.pos, target.pos, Ballistica.PROJECTILE );
		CursedWand.CursedEffect e = CursedWand.randomValidEffect( null, target, bal, false );
		if (e != null) e.effect( null, target, bal, false );
		if (toCurse != null) curse( toCurse, true );
	}

	public static void curse( Item item, boolean known ) {
		if (item instanceof Weapon) {
			Weapon w = (Weapon) item;
			w.cursed = true;
			w.cursedKnown = w.cursedKnown || known;
			if (w.enchantment == null) w.enchant( Weapon.Enchantment.randomCurse() );
		} else if (item instanceof Armor) {
			Armor a = (Armor) item;
			a.cursed = true;
			a.cursedKnown = a.cursedKnown || known;
			if (a.glyph == null) a.inscribe( Armor.Glyph.randomCurse() );
		} else if (item instanceof Wand || item instanceof Ring || item instanceof Artifact) {
			item.cursed = true;
			item.cursedKnown = item.cursedKnown || known;
		}
	}

	public static void igniteAllGrass( Level level ) {
		Fire fire = (Fire) level.blobs.get( Fire.class );
		for (int i = 0; i < level.length(); i++) {
			int t = level.map[i];
			if (t == Terrain.GRASS || t == Terrain.HIGH_GRASS || t == Terrain.FURROWED_GRASS) {
				if (fire == null || fire.cur == null || fire.cur[i] <= 0) {
					Blob b = Blob.seed( i, 4, Fire.class, level );
					if (fire == null) fire = (Fire) b;
					if (level == Dungeon.level && GameScene.scene != null && b != null && b.emitter == null) GameScene.add( b );
				}
			}
		}
	}

	//changes every item lying on a level or held by its monsters, null removes the item
	public static void transformLevelItems( Level level, TcStoredHeapData.ItemFn fn ) {
		for (Heap h : level.heaps.valueList()) {
			TcStoredHeapData.filterMap( h.items, fn );
			if (h.items.isEmpty()) {
				level.heaps.remove( h.pos );
				if (h.sprite != null) h.sprite.kill();
			} else if (h.sprite != null) {
				h.sprite.link();
			}
		}
		HashMap<Mob, Mob> replace = new HashMap<>();
		for (Mob m : level.mobs) {
			Mob r = TcStoredHeapData.transformMobItems( m, fn );
			if (r != null) replace.put( m, r );
		}
		for (Map.Entry<Mob, Mob> e : replace.entrySet()) {
			level.mobs.remove( e.getKey() );
			level.mobs.add( e.getValue() );
		}
		ExterminationItemLock lock = (ExterminationItemLock) level.blobs.get( ExterminationItemLock.class );
		if (lock != null) lock.transformItems( fn );
	}

	//Fake Dungeon and its add-ons, a heap becomes a mimic that holds it
	public static boolean mimicsEncaseHeap( Level level, Heap h ) {
		if (!on( MIMICS ) || h == null) return false;
		if (h.type == Heap.Type.CHEST || on( MIMICS_ALL )) {
			return TcStoredHeapData.transformHeapIntoMimic( level, h, on( MIMICS_GRIND ), !on( MIMICS_GRIND ) );
		}
		return false;
	}

	//Level.getTransition(int) reads Dungeon.level, which is not the floor being built
	private static boolean isTransition( Level level, int cell ) {
		for (LevelTransition t : level.transitions) if (t.cell() == cell) return true;
		return false;
	}

	private static int randomFreeCell( Level level ) {
		for (int tries = 0; tries < 500; tries++) {
			int i = Random.Int( level.length() );
			if (level.passable[i] && !level.solid[i] && !level.pit[i] && level.heaps.get( i ) == null
					&& !isTransition( level, i ) && level.findMob( i ) == null && level.insideMap( i )) {
				return i;
			}
		}
		return -1;
	}

	/*****************************************************************
	 * Char hooks
	 *****************************************************************/

	//before any multipliers, a negative result cancels the damage
	public static int incomingDamage( Char ch, int dmg, Object src ) {
		if (ch instanceof Mob && on( CARDINAL_DISABILITY ) && ch.alignment == Char.Alignment.ENEMY && Dungeon.hero != null) {
			int w = Dungeon.level.width();
			if (ch.pos % w != Dungeon.hero.pos % w && ch.pos / w != Dungeon.hero.pos / w) return -1;
		}
		return dmg;
	}

	public static float damageMultiplier( Char ch, float damage, Object src ) {
		if (src instanceof com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DwarfKing.KingDamager) return damage;
		for (Buff b : ch.buffs()) {
			if (b instanceof TcBuff.DamageAmp) damage *= ((TcBuff.DamageAmp) b).damageMultiplier( src );
		}
		return damage;
	}

	//right before shields, a negative result means the damage was taken some other way
	public static int beforeShield( Char ch, int dmg, Object src ) {
		if (src instanceof com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DwarfKing.KingDamager) return dmg;
		if (CrystalShield.blockIncomingDamage( ch, dmg )) return -1;
		if (ch instanceof Hero && on( BLOODBAG ) && !(src instanceof Bleeding) && dmg > 0) {
			Buff.affect( ch, TcTrackers.BloodbagBleeding.class ).add( Math.max( dmg / 2f, 1f ) );
			return -1;
		}
		return dmg;
	}

	//after health was lowered, before the death check
	public static void damageTaken( Char ch, int dmg, Object src ) {
		if (ch instanceof Mob) {
			if (ch.HP <= 0 && on( CRYSTAL_BLOOD ) && ch.buff( CrystalShield.DeathMarker.class ) == null
					&& !ch.properties().contains( Char.Property.BOSS ) && !(ch instanceof NPC)) {
				int overkill = ch.HP;
				ch.HP = 1;
				CrystalShield.applyShieldingLayers( ch, 5, "x5" );
				Buff.affect( ch, CrystalShield.DeathMarker.class );
				if (overkill < 0) dmg += overkill;
			}
			if (!ch.isAlive() && ch.alignment != Char.Alignment.ALLY && (on( REVENGE ) || on( REVENGE_FURY ))) {
				boolean rage = on( REVENGE ) && ch.HP < 0;
				boolean fury = on( REVENGE_FURY );
				if (rage || fury) {
					boolean[] fov = fovOf( ch );
					for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
						if (mob == ch || !fov[mob.pos] || mob instanceof NPC || mob.alignment == Char.Alignment.ALLY) continue;
						if (rage) Buff.affect( mob, RevengeRage.class ).add( -ch.HP );
						if (fury) Buff.append( mob, RevengeFury.class, 10f );
						if (mob.sprite != null) mob.sprite.emitter().start( Speck.factory( Speck.UP ), 0.2f, 3 );
					}
				}
			}
		} else if (ch instanceof Hero && on( PLAGUE ) && dmg > 0) {
			Buff.affect( ch, Intoxication.class ).processHit( dmg, src );
		}

		if (on( WHIPLASH ) && ch.isAlive() && dmg > 0 && !(src instanceof WandOfBlastWave.Knockback)
				&& !ch.properties().contains( Char.Property.IMMOVABLE ) && GameScene.scene != null) {
			whiplash( ch, src );
		}

		if (dmg > 0) {
			for (Buff b : ch.buffs().toArray( new Buff[0] )) {
				if (b instanceof TcBuff.OnDamageTaken) ((TcBuff.OnDamageTaken) b).onDamageTaken( dmg, src );
			}
		}
	}

	private static void whiplash( Char ch, Object src ) {
		boolean[] fov = fovOf( ch );
		ArrayList<Integer> targets = new ArrayList<>();
		for (Char c : Actor.chars()) {
			if (c != ch && fov[c.pos] && Dungeon.level.distance( ch.pos, c.pos ) > 1) targets.add( c.pos );
		}
		Integer cell = null;
		if (!targets.isEmpty()) {
			cell = Random.element( targets );
		} else {
			ArrayList<Integer> walls = new ArrayList<>();
			for (int i = 0; i < Dungeon.level.length(); i++) {
				if (fov[i] && Dungeon.level.solid[i] && Dungeon.level.distance( ch.pos, i ) > 1) walls.add( i );
			}
			if (!walls.isEmpty()) cell = Random.element( walls );
		}
		if (cell != null) {
			Ballistica trajectory = new Ballistica( ch.pos, cell, Ballistica.MAGIC_BOLT );
			WandOfBlastWave.throwChar( ch, trajectory, 999, true, false, new WandOfBlastWave.Knockback() );
		}
	}

	//attack damage before the defender's armor, flat bonus, multiplier and bonus after the multiplier
	public static float attackDamage( Char attacker, Char enemy, float dmg ) {
		float flat = 0, mult = 1, post = 0;
		for (Buff b : attacker.buffs()) {
			if (b instanceof TcBuff.AttackAmp) {
				TcBuff.AttackAmp a = (TcBuff.AttackAmp) b;
				flat += a.flatAttackBonus();
				mult *= a.attackMultiplier();
				post += a.flatAttackBonusPostMult();
			}
		}
		return (dmg + flat) * mult + post;
	}

	//after a hit landed, attack procs of the attacker and defense procs of the defender
	public static void afterHit( Char attacker, Char enemy, int damage ) {
		for (Buff b : attacker.buffs().toArray( new Buff[0] )) {
			if (b instanceof TcBuff.AttackProc) ((TcBuff.AttackProc) b).attackProc( enemy, damage );
		}
		for (Buff b : enemy.buffs().toArray( new Buff[0] )) {
			if (b instanceof TcBuff.DefenseProc) ((TcBuff.DefenseProc) b).defenseProc( attacker, damage );
		}
	}

	public static float defRollMult( Char attacker, Char defender ) {
		float m = 1;
		for (Buff b : defender.buffs()) {
			if (b instanceof TcBuff.DefRoll) m *= ((TcBuff.DefRoll) b).defRollMultiplier( attacker );
		}
		return m;
	}

	private static boolean drRunning = false;

	public static int drBonus( Char ch ) {
		if (drRunning) return 0;
		int bonus = 0;
		float mult = 1;
		for (Buff b : ch.buffs()) {
			if (b instanceof TcBuff.DrRoll) {
				bonus += ((TcBuff.DrRoll) b).drRollBonus();
				mult *= ((TcBuff.DrRoll) b).drMultBonus();
			}
		}
		if (mult != 1) {
			drRunning = true;
			int base = ch.drRoll();
			drRunning = false;
			bonus += Math.round( base * (mult - 1) );
		}
		return bonus;
	}

	public static float speed( Char ch, float speed ) {
		for (Buff b : ch.buffs()) {
			if (b instanceof TcBuff.Timescale) speed *= ((TcBuff.Timescale) b).speedFactor();
		}
		return speed;
	}

	public static boolean isInvulnerable( Char ch, Class<?> effect ) {
		for (Buff b : ch.buffs()) {
			if (b instanceof TcBuff.Invulnerable && ((TcBuff.Invulnerable) b).isInvulnerable( effect )) return true;
		}
		return false;
	}

	public static void onDeath( Char ch, Object src ) {
		boolean exterminating = ch.buff( Exterminating.class ) != null;
		for (Buff b : ch.buffs().toArray( new Buff[0] )) {
			if (b instanceof TcBuff.OnDeath) ((TcBuff.OnDeath) b).onDeathProc();
		}
		if (ch instanceof Mob && Dungeon.level != null && Dungeon.hero != null) {
			if (on( BOMBERMOB )) {
				if ((on( CONSTELLATION ) || Random.Float() < 0.01f) && !bossOrSpecial()) {
					SuperNovaTracker nova = Buff.append( Dungeon.hero, SuperNovaTracker.class );
					nova.pos = ch.pos;
					nova.harmsAllies = true;
					GLog.w( Messages.get( CursedWand.class, "supernova" ) );
				} else {
					igniteBomb( bombermobBomb(), ch.pos );
				}
			}
			if (on( BODY_TRAPS ) && Dungeon.level.insideMap( ch.pos ) && !Dungeon.level.pit[ch.pos]) {
				Trap t = (Trap) Reflection.newInstance( randomTrap( Dungeon.level ) );
				if (t != null) placeRevealedTrap( Dungeon.level, ch.pos, t );
			}
			if (ch.alignment != Char.Alignment.ALLY) {
				if (on( ARROWHEAD )) Buff.affect( Dungeon.hero, Arrowhead.class ).addStack();
				if (on( PATRON_SAINTS )) {
					Blob b = Blob.seed( ch.pos, PatronSaintsBlob.SEED_SOUL, PatronSaintsBlob.class );
					if (GameScene.scene != null) GameScene.add( b );
					if (on( PERSISTENT_SAINTS )) {
						Blob b2 = Blob.seed( Dungeon.hero.pos, PatronSaintsBlob.SEED_SOUL, PatronSaintsBlob.class );
						if (GameScene.scene != null) GameScene.add( b2 );
					}
				}
			}
			if (exterminating) Exterminating.processExtermination( Dungeon.level );
		}
	}

	private static final HashMap<Class<? extends Bomb>, Float> BOMBERMOB_BOMBS = new HashMap<>();
	private static final HashMap<Class<? extends Bomb>, Float> CONSTELLATION_BOMBS = new HashMap<>();
	static {
		BOMBERMOB_BOMBS.put( Bomb.class, 4f );
		BOMBERMOB_BOMBS.put( Bomb.DoubleBomb.class, 1f );
		BOMBERMOB_BOMBS.put( FrostBomb.class, 1f );
		BOMBERMOB_BOMBS.put( WoollyBomb.class, 0.1f );
		BOMBERMOB_BOMBS.put( Firebomb.class, 0.5f );
		BOMBERMOB_BOMBS.put( Noisemaker.class, 0.5f );
		BOMBERMOB_BOMBS.put( SmokeBomb.class, 1 / 3f );
		BOMBERMOB_BOMBS.put( Flashbang.class, 1 / 3f );
		BOMBERMOB_BOMBS.put( RegrowthBomb.class, 0.01f );
		BOMBERMOB_BOMBS.put( HolyBomb.class, 0.25f );
		BOMBERMOB_BOMBS.put( ArcaneBomb.class, 1 / 7f );

		CONSTELLATION_BOMBS.put( FrostBomb.class, 1f );
		CONSTELLATION_BOMBS.put( Firebomb.class, 0.5f );
		CONSTELLATION_BOMBS.put( Flashbang.class, 1 / 3f );
		CONSTELLATION_BOMBS.put( RegrowthBomb.class, 0.01f );
		CONSTELLATION_BOMBS.put( HolyBomb.class, 0.25f );
		CONSTELLATION_BOMBS.put( ArcaneBomb.class, 1 / 7f );
	}

	public static Bomb bombermobBomb() {
		return Reflection.newInstance( Random.chances( BOMBERMOB_BOMBS ) );
	}

	//Constellation, a supernova from a monster sets off a random special bomb on every cell
	public static Bomb novaBomb( boolean harmsAllies ) {
		if (on( CONSTELLATION ) && harmsAllies) return Reflection.newInstance( Random.chances( CONSTELLATION_BOMBS ) );
		return new Bomb.ConjuredBomb();
	}

	public static void igniteBomb( Bomb bomb, int pos ) {
		Heap h = Dungeon.level.drop( bomb, pos );
		if (h.sprite != null) h.sprite.drop( pos );
		Actor.addDelayed( bomb.fuse = new Bomb.Fuse().ignite( bomb ), 2 );
	}

	//Bombermob, a lit bomb cannot be picked up
	public static boolean blockBombPickup( Bomb bomb ) {
		if (bomb.fuse != null && on( BOMBERMOB )) {
			GLog.n( Messages.get( TooCruel.class, "bombermob_fuse" ) );
			return true;
		}
		return false;
	}

	public static Class<?> randomTrap( Level level ) {
		if (level instanceof RegularLevel) {
			try {
				java.lang.reflect.Method mc = RegularLevel.class.getDeclaredMethod( "trapClasses" );
				java.lang.reflect.Method mp = RegularLevel.class.getDeclaredMethod( "trapChances" );
				mc.setAccessible( true );
				mp.setAccessible( true );
				Class<?>[] classes = (Class<?>[]) mc.invoke( level );
				float[] chances = (float[]) mp.invoke( level );
				if (classes != null && chances != null && classes.length > 0 && classes.length == chances.length) {
					int i = Random.chances( chances );
					if (i >= 0) return classes[i];
				}
			} catch (Exception ignored) { }
		}
		return WornDartTrap.class;
	}

	public static void placeRevealedTrap( Level level, int pos, Trap trap ) {
		int old = level.map[pos];
		Trap t = level.setTrap( trap, pos );
		Level.set( pos, Terrain.TRAP, level );
		t.reveal();
		if (level == Dungeon.level && GameScene.scene != null) {
			GameScene.updateMap( pos );
			if (level.heroFOV[pos]) {
				GameScene.discoverTile( pos, old );
				ScrollOfMagicMapping.discover( pos );
			}
		}
	}

	//Duplicator and Repeater, returns true if the trap stays armed
	public static boolean keepTrap( Trap trap ) {
		if (on( DUPLICATOR ) && !trap.getClass().isAnonymousClass() && Dungeon.level != null) {
			ArrayList<Integer> cells = new ArrayList<>();
			for (int i = 0; i < Dungeon.level.length(); i++) {
				switch (Dungeon.level.map[i]) {
					case Terrain.EMPTY: case Terrain.GRASS: case Terrain.HIGH_GRASS: case Terrain.EMBERS:
					case Terrain.EMPTY_DECO: case Terrain.EMPTY_SP: case Terrain.INACTIVE_TRAP: case Terrain.WATER:
						if (Actor.findChar( i ) == null && Dungeon.level.heaps.get( i ) == null) cells.add( i );
				}
			}
			Random.shuffle( cells );
			for (int i = 0; i < Math.min( 2, cells.size() ); i++) {
				Trap copy = (Trap) Reflection.newInstance( trap.getClass() );
				if (copy != null) placeRevealedTrap( Dungeon.level, cells.get( i ), copy );
			}
		}
		return on( REPEATER );
	}

	public static void onMove( Char ch, int step, boolean travelling ) {
		if (ch instanceof Hero && on( BARRIER_BREAKER )) {
			int terrain = Dungeon.level.map[step];
			if (terrain == Terrain.OPEN_DOOR || terrain == Terrain.DOOR) {
				strongDestroy( Dungeon.level, step );
				if (GameScene.scene != null) {
					GameScene.updateMap( step );
					Sample.INSTANCE.play( Assets.Sounds.ROCKS, 0.25f, 1.5f );
					Sample.INSTANCE.play( Assets.Sounds.BURNING, 0.25f, 1.5f );
					CellEmitter.center( ch.pos ).burst( SmokeParticle.FACTORY, 5 );
				}
				((Hero) ch).spend( 1f );
			}
		} else if (ch instanceof Mob && on( MOLES ) && !bossOrSpecial()) {
			boolean destruction = false;
			Level level = Dungeon.level;
			if (Char.hasProp( ch, Char.Property.LARGE ) && !level.openSpace[step]) {
				for (int o : PathFinder.NEIGHBOURS8) {
					int n = step + o;
					if (level.solid[n] && destroyWall( level, n )) destruction = true;
				}
			}
			int terrain = level.map[step];
			if (level.solid[step] && terrain != Terrain.DOOR && terrain != Terrain.OPEN_DOOR) {
				if (destroyWall( level, step )) destruction = true;
			}
			if (destruction) {
				if (level.heroFOV[step] && GameScene.scene != null) {
					Sample.INSTANCE.play( Assets.Sounds.ROCKS, 0.25f, 1.5f );
					CellEmitter.center( step ).burst( SmokeParticle.FACTORY, 5 );
				}
				TcTrackers.FullSceneUpdater.requestFull();
			}
		}
	}

	private static boolean destroyWall( Level level, int cell ) {
		int t = level.map[cell];
		if (t == Terrain.WALL || t == Terrain.WALL_DECO || t == Terrain.STATUE || t == Terrain.STATUE_SP
				|| t == Terrain.SECRET_DOOR || t == Terrain.CRYSTAL_DOOR || t == Terrain.REGION_DECO
				|| t == Terrain.REGION_DECO_ALT || t == Terrain.BOOKSHELF) {
			strongDestroy( level, cell );
			return true;
		}
		return false;
	}

	private static void strongDestroy( Level level, int cell ) {
		if (!level.insideMap( cell )) return;
		Level.set( cell, Terrain.EMBERS, level );
		for (int o : PathFinder.NEIGHBOURS4) {
			int n = cell + o;
			int t = level.map[n];
			if (t == Terrain.DOOR || t == Terrain.OPEN_DOOR || t == Terrain.CRYSTAL_DOOR || t == Terrain.LOCKED_DOOR) {
				strongDestroy( level, n );
			}
		}
		level.destroy( cell );
	}

	//Moles, monsters can path through walls inside the map
	public static boolean[] modifyPassable( Char ch, boolean[] passable ) {
		if (ch instanceof Mob && on( MOLES ) && !bossOrSpecial() && Dungeon.level != null && passable.length == Dungeon.level.length()) {
			Level level = Dungeon.level;
			for (int i = 0; i < passable.length; i++) {
				if (level.solid[i] && level.insideMap( i )) passable[i] = true;
			}
		}
		return passable;
	}

	public static boolean molesPathable( Char ch, int cell ) {
		return ch instanceof Mob && on( MOLES ) && !bossOrSpecial() && Dungeon.level.insideMap( cell )
				&& Dungeon.level.solid[cell] && Actor.findChar( cell ) == null;
	}

	/*****************************************************************
	 * Mob hooks
	 *****************************************************************/

	public static void mobFirstAdded( Mob mob ) {
		if (mob instanceof NPC) return;
		applyModifiers( mob );
	}

	public static void mobBeforeAct( Mob mob, boolean enemyInFOV ) {
		if (!(mob instanceof NPC)) applyModifiers( mob );
		if (enemyInFOV && on( INSOMNIA )) {
			Buff.prolong( mob, TcTrackers.InsomniaSlowdown.class, TcTrackers.InsomniaSlowdown.DURATION );
		}
	}

	private static boolean anyMobMode() {
		return on( ARROWHEAD ) || on( CRYSTAL_SHELTER ) || on( PREPARED_ENEMIES ) || (on( LOFT ) && on( MOLES ))
				|| on( INSOMNIA ) || on( DOMAIN_OF_HELL ) || on( JACK_IN_A_BOX ) || on( CROWD_DIVERSITY )
				|| on( HYDRA ) || on( FORCED_PACIFISM );
	}

	private static void applyModifiers( Mob mob ) {
		if (!anyMobMode() || mob.buff( TcTrackers.ModifiersApplied.class ) != null) return;
		Buff.affect( mob, TcTrackers.ModifiersApplied.class );
		if (mob.buff( TcTrackers.ModifiersApplied.class ) == null) return;

		if (on( ARROWHEAD )) Buff.affect( mob, Arrowhead.MobArrowhead.class );
		if (on( CRYSTAL_SHELTER )) {
			Buff.affect( mob, CrystalShield.class );
			Buff.append( mob, CrystalShield.Layer.class );
		}
		if (on( PREPARED_ENEMIES ) && mob instanceof Snake) {
			mob.HP *= 4;
			mob.HT *= 4;
			mob.defenseSkill = 0;
		}
		if (on( LOFT ) && on( MOLES )) Buff.affect( mob, Levitation.class, 2e9f );
		if (on( INSOMNIA )) Buff.affect( mob, TcTrackers.InsomniaSpeed.class );
		if (on( DOMAIN_OF_HELL )) Buff.affect( mob, TcTrackers.FireResist.class );
		if (on( JACK_IN_A_BOX ) && mob instanceof Mimic) Buff.affect( mob, TcTrackers.DelayedBeckon.class );
		if (on( CROWD_DIVERSITY ) && mob.buff( Resizing.class ) == null) {
			Resizing r = Buff.affect( mob, Resizing.class );
			if (r != null) r.multiplyRandom();
		}
		if (on( HYDRA )) Buff.affect( mob, TcTrackers.Hydra.EnemyTracker.class );
		if (on( FORCED_PACIFISM )) Buff.affect( mob, TcTrackers.ForcedPacifism.class );
	}

	//Invasion and Great Migration, monsters come from deeper floors
	public static int mobDepth( int depth ) {
		if (!on( INVASION )) return depth;
		if (on( GREAT_MIGRATION ) && depth != 1) return Math.min( depth + 5, 26 );
		if (depth == 2) return 3;
		if (depth != 1 && depth < 25) return depth + 2;
		return depth;
	}

	//Mutagen and Evolution, rare monster variants
	public static float rareChance() {
		if (on( EVOLUTION )) return 1000f;
		if (on( MUTAGEN )) return 12.5f;
		return -1f;
	}

	public static boolean colosseum() {
		return on( COLOSSEUM );
	}

	/*****************************************************************
	 * Hero hooks
	 *****************************************************************/

	public static void heroLive( Hero hero ) {
		if (on( RACING_THE_DEATH )) Buff.affect( hero, RacingTheDeath.class );
		if (on( THUNDERSTRUCK )) Buff.affect( hero, Arrowhead.class ).set( 9001 );
		if (on( BLINDNESS )) Buff.affect( hero, TcTrackers.PermaBlind.class );
		if (on( TOXIC_WATER )) Buff.affect( hero, Intoxication.ToxicWaterTracker.class );
		if (on( CERTAINTY_OF_STEEL )) {
			if (on( GOLDEN_COLOSSUS )) Buff.affect( hero, SteelBody.GoldenBody.class );
			else Buff.affect( hero, SteelBody.class );
		}
		if (on( RETIERED )) Buff.affect( hero, RetieredBuff.class );
		if (on( UNSTABLE_ACCESSORIES )) Buff.affect( hero, TcTrackers.ControlledRandomness.class );
		if (on( PANDEMONIUM )) Buff.affect( hero, Pandemonium.class );
		if (on( INSOMNIA )) Buff.affect( hero, TcTrackers.Insomnia.class );
		if (on( CASUAL_APPROACH )) Buff.affect( hero, TcTrackers.CasualApproach.class );
		if (on( LET_THEM_REST )) Buff.affect( hero, TcTrackers.InvisibleResting.class );
		if (on( DOMAIN_OF_HELL )) {
			Buff.affect( hero, TcTrackers.BrimstoneNeutralizer.class );
			Buff.affect( hero, TcTrackers.GrassIgniter.class );
		}
		if (on( PERFECT_INFORMATION )) Buff.affect( hero, TcTrackers.PerfectInformation.class ).pushBackInTime();
		if (on( SAFETY_BUFFER )) Buff.affect( hero, SafetyBuffer.class );
		if (on( UNINSPIRED_TO_LEARN )) Buff.affect( hero, TcTrackers.UninspiredToLearn.class );
		if (on( BLESSING_FOR_THE_WORTHY )) Buff.affect( hero, TcTrackers.BlessingWorthy.class );
	}

	public static void heroSpent( Hero hero, float time ) {
		if (time > 0) {
			RacingTheDeath r = hero.buff( RacingTheDeath.class );
			if (r != null) r.tick();
		}
	}

	//Slippery Floor, the hero slides on along water
	public static int heroMove( Hero hero, int step ) {
		if (!on( SLIDING )) return step;
		int move = step - hero.pos;
		int slid = 0;
		Level level = Dungeon.level;
		while (true) {
			int cur = step;
			int next = step + move;
			if (next < 0 || next >= level.length() || !level.water[step]) break;
			if (Actor.findChar( next ) != null) break;
			if (!level.passable[next] && !level.avoid[next]) break;
			if (level.distance( cur, next ) > 1) break;
			if (Random.Float() < 0.2f) {
				Level.set( cur, Terrain.EMPTY, level );
				if (GameScene.scene != null) {
					GameScene.updateMap( cur );
					CellEmitter.get( cur ).burst( Speck.factory( Speck.STEAM ), 5 );
				}
			}
			if (GameScene.scene != null) GameScene.ripple( cur );
			step = next;
			slid++;
		}
		if (slid > 0) hero.interrupt();
		return step;
	}

	public static int earnExp( Hero hero, int exp, Class<?> source ) {
		float f = exp;
		for (Buff b : hero.buffs()) {
			if (b instanceof TcBuff.XpMultiplier) f *= ((TcBuff.XpMultiplier) b).xpMultiplier( source );
		}
		if (on( SKELETON_CREW )) f *= 2f;
		return (int) f;
	}

	//Rotten Luck, the lower of two rolls, and of three when the first is above the middle
	public static int rottenLuck( int min, int max ) {
		int dmg = Math.min( Random.IntRange( min, max ), Random.IntRange( min, max ) );
		if (dmg >= (max + min) / 2) dmg = Math.min( dmg, Random.IntRange( min, max ) );
		return dmg;
	}

	//Multiclassing, three subclasses of other heroes, the same three for a seed
	public static HeroSubClass[] subclassChoice( Hero hero ) {
		if (!on( MULTICLASSING )) return hero.heroClass.subClasses();
		Random.pushGenerator( Dungeon.seed );
		ArrayList<HeroSubClass> classes = new ArrayList<>();
		for (HeroClass cl : new HeroClass[]{ HeroClass.WARRIOR, HeroClass.MAGE, HeroClass.ROGUE, HeroClass.HUNTRESS, HeroClass.DUELIST, HeroClass.CLERIC }) {
			if (cl == hero.heroClass || cl.subClasses().length == 0) continue;
			classes.add( Random.element( cl.subClasses() ) );
		}
		Random.shuffle( classes );
		Random.popGenerator();
		return classes.subList( 0, Math.min( 3, classes.size() ) ).toArray( new HeroSubClass[0] );
	}

	//Multiclassing, the picked subclass's own class item
	public static void subclassPicked( Hero hero ) {
		if (!on( MULTICLASSING ) || hero.subClass == HeroSubClass.NONE) return;
		for (HeroSubClass s : hero.heroClass.subClasses()) if (s == hero.subClass) return;
		Item gift = null;
		for (HeroClass cl : new HeroClass[]{ HeroClass.WARRIOR, HeroClass.MAGE, HeroClass.ROGUE, HeroClass.HUNTRESS, HeroClass.DUELIST, HeroClass.CLERIC }) {
			boolean has = false;
			for (HeroSubClass s : cl.subClasses()) if (s == hero.subClass) has = true;
			if (!has) continue;
			switch (cl) {
				case WARRIOR: gift = new BrokenSeal(); break;
				case MAGE: gift = new MagesStaff(); break;
				case ROGUE: gift = new CloakOfShadows(); break;
				case HUNTRESS: gift = new SpiritBow(); break;
				case CLERIC: gift = new HolyTome(); break;
				default: break;
			}
			break;
		}
		if (gift != null) {
			gift.identify();
			Heap h = Dungeon.level.drop( gift, hero.pos );
			if (h.sprite != null) h.sprite.drop();
		}
	}

	public static void onBossSlain() {
		if (Dungeon.hero == null || !Dungeon.hero.isAlive()) return;
		for (Buff b : Dungeon.hero.buffs().toArray( new Buff[0] )) {
			if (b instanceof TcBuff.OnBossSlain) ((TcBuff.OnBossSlain) b).onBossSlain();
		}
	}

	public static void wandUsed( Hero hero, Wand wand ) {
		if (hero == null) return;
		Pandemonium p = hero.buff( Pandemonium.class );
		if (p != null) p.wandUsed( wand );
	}

	//Pandemonium, a thrown weapon may change when it hits, returns true when it was handled
	public static boolean rerollMissile( MissileWeapon w, int cell ) {
		if (!on( PANDEMONIUM ) || w.spawnedForEffect) return false;
		MissileWeapon changed = Pandemonium.rerollMissile( w );
		if (changed == w) return false;
		Heap h = Dungeon.level.drop( changed, cell );
		if (h.sprite != null) h.sprite.drop();
		return true;
	}

	public static void spiritBowShot( SpiritBow bow ) {
		if (on( PANDEMONIUM )) Pandemonium.spiritBowShot( bow );
	}

	/*****************************************************************
	 * Items
	 *****************************************************************/

	public static void onPickUp( Item item ) {
		if (on( COMPLETE_KNOWLEDGE ) && !item.isIdentified()) item.identify();
	}

	//Curse Magnet, cursed items cannot be dropped or thrown
	public static boolean actionBanned( Item item, String action ) {
		if (item.cursed && on( CURSE_MAGNET )) {
			return Item.AC_DROP.equals( action ) || Item.AC_THROW.equals( action );
		}
		return false;
	}

	//trinkets and items a mode turns off, as the mod did
	public static boolean isItemBlocked( Item item ) {
		if (on( EVOLUTION ) && item instanceof RatSkull) return true;
		if (on( ROTTEN_LUCK ) && item instanceof ThirteenLeafClover) return true;
		if (on( CERTAINTY_OF_STEEL ) && item instanceof SaltCube) return true;
		if (on( GOLDEN_COLOSSUS ) && item instanceof MasterThievesArmband) return true;
		if (on( EXOTIC_GOODS ) && item instanceof ExoticCrystals) return true;
		if (on( COMPLETE_KNOWLEDGE ) && item instanceof ShardOfOblivion) return true;
		return false;
	}

	public static void onDrink( Potion potion, Hero hero ) {
		if (on( INTOXICATION )) {
			Buff.affect( hero, Intoxication.class ).extend( potion instanceof ExoticPotion ? Intoxication.EXOTIC_INTOXICATION : Intoxication.POTION_INTOXICATION );
		}
	}

	public static void onShielding( Hero hero ) {
		if (on( THUNDERSTRUCK )) Buff.prolong( hero, Invulnerability.class, 3f );
	}

	//Certainty of Steel turns off hunger and natural regeneration
	public static boolean steelBody() {
		return on( CERTAINTY_OF_STEEL );
	}

	/*****************************************************************
	 * Level hooks
	 *****************************************************************/

	public static float mobsMult() {
		float m = 1;
		if (on( HORDE )) m *= 2;
		if (on( SKELETON_CREW )) m *= 0.5f;
		if (on( ABANDONED_SHIP )) m = 0;
		return m;
	}

	public static float waterMult() {
		float m = 1;
		if (on( DROUGHT )) m *= 0.5f;
		if (on( SLIDING )) m *= 1.5f;
		return m;
	}

	//a fill rate near or over 1 never finishes painting, so Slippery Floor stops at 85 percent
	public static float waterFill( float fill ) {
		return Math.min( fill * waterMult(), Math.max( fill, 0.85f ) );
	}

	public static float grassMult() {
		return on( DROUGHT ) ? 0.5f : 1f;
	}

	public static float trapsMult() {
		return on( EXTREME_CAUTION ) ? 4f : 1f;
	}

	public static int depthBonus() {
		return on( DEEPER_DANGER ) ? 10 : 0;
	}

	public static float respawnCooldown( Level level, float cooldown ) {
		if (on( RESURRECTION )) return 1f;
		if (on( REPOPULATION )) {
			int limit = Math.max( 1, level.mobLimit() );
			cooldown *= GameMath.gate( 0f, 1.1f * level.mobCount() / (float) limit - 0.1f, 1f );
			if (on( FRACTAL_HIVE )) cooldown = Math.min( cooldown, 25f );
		}
		return cooldown;
	}

	//Infertility stops respawns, Fractal Hive lifts the monster cap
	public static boolean noRespawn() {
		return on( INFERTILITY );
	}

	public static boolean noMobCap() {
		return on( FRACTAL_HIVE );
	}

	public static boolean[] blockingFov( Level level, Char c, boolean[] modifiable, boolean[] blocking ) {
		if (!(c instanceof Hero || c.alignment == Char.Alignment.ALLY)) return blocking;
		if (on( LOFT )) {
			if (blocking == null) { System.arraycopy( level.losBlocking, 0, modifiable, 0, modifiable.length ); blocking = modifiable; }
			for (int i = 0; i < blocking.length; i++) if (!blocking[i] && level.pit[i]) blocking[i] = true;
		}
		if (on( BULKY_FRAME )) {
			if (blocking == null) { System.arraycopy( level.losBlocking, 0, modifiable, 0, modifiable.length ); blocking = modifiable; }
			boolean shroud = on( SHROUDING_PRESENCE );
			for (Mob mob : level.mobs) {
				if (mob.alignment == Char.Alignment.ALLY || mob.pos < 0 || mob.pos >= blocking.length) continue;
				blocking[mob.pos] = true;
				if (shroud) {
					for (int o : PathFinder.NEIGHBOURS8) {
						int p = mob.pos + o;
						if (p >= 0 && p < blocking.length && p != c.pos) blocking[p] = true;
					}
				}
			}
		}
		return blocking;
	}

	//Extermination, monsters still to be slain show through walls for a moment
	public static void markMindVision( Level level, boolean[] heroMindFov ) {
		if (!on( EXTERMINATION )) return;
		for (Mob mob : level.mobs) {
			if (mob.buff( Exterminating.Reveal.class ) == null) continue;
			for (int o : PathFinder.NEIGHBOURS9) {
				int p = mob.pos + o;
				if (p >= 0 && p < heroMindFov.length) heroMindFov[p] = true;
			}
		}
	}

	private static boolean bypassTransition = false;
	private static boolean thoughtlessConfirmed = false;

	public static boolean canTransition( Level level, final Hero hero, LevelTransition transition ) {
		if (bypassTransition || hero == null) return true;
		if (on( EXTERMINATION ) && !Exterminating.exterminationDone( level )) return false;
		if (on( CRUMBLED_STAIRS )) {
			if (Statistics.amuletObtained) {
				if (transition.destDepth >= Dungeon.depth && transition.destBranch == Dungeon.branch) {
					showMessage( Messages.get( TooCruel.class, "crumbled_stairs_no_distractions" ) );
					return false;
				}
			} else if (transition.type == LevelTransition.Type.REGULAR_ENTRANCE) {
				showMessage( Messages.get( Hero.class, "leave" ) );
				return false;
			}
		}
		if (on( THOUGHTLESS )) return confirmThoughtless( hero );
		return true;
	}

	private static void showMessage( final String text ) {
		if (GameScene.scene == null) return;
		Game.runOnRenderThread( new Callback() {
			@Override
			public void call() {
				GameScene.show( new WndMessage( text ) );
			}
		} );
	}

	public static void beforeTransition() {
		if (on( THOUGHTLESS )) clearThoughtlessItems();
	}

	private static final Class<?>[] THOUGHTLESS_ITEMS = {
			ScrollOfUpgrade.class, ScrollOfEnchantment.class, ScrollOfMagicMapping.class, ScrollOfRecharging.class,
			ScrollOfTransmutation.class, ScrollOfMysticalEnergy.class, ScrollOfForesight.class, Stylus.class,
			StoneOfAugmentation.class, StoneOfEnchantment.class, StoneOfIntuition.class, StoneOfClairvoyance.class,
			PotionOfStrength.class, PotionOfMastery.class, ElixirOfMight.class, MagicalInfusion.class,
			CurseInfusion.class, Alchemize.class, ReclaimTrap.class, Recycle.class
	};

	public static boolean thoughtlessAffects( Item item ) {
		for (Class<?> c : THOUGHTLESS_ITEMS) if (c.isInstance( item )) return true;
		return false;
	}

	public static String thoughtlessNames() {
		StringBuilder sb = new StringBuilder();
		for (Class<?> c : THOUGHTLESS_ITEMS) {
			Item i = (Item) Reflection.newInstance( c );
			if (i == null) continue;
			if (sb.length() > 0) sb.append( ", " );
			sb.append( Messages.titleCase( i.trueName() ) );
		}
		return sb.toString();
	}

	private static String names( ArrayList<Item> items ) {
		StringBuilder sb = new StringBuilder();
		for (Item i : items) {
			if (sb.length() > 0) sb.append( ", " );
			sb.append( "_" ).append( Messages.titleCase( i.title() ) ).append( "_" );
		}
		return sb.toString();
	}

	private static boolean confirmThoughtless( final Hero hero ) {
		if (thoughtlessConfirmed) {
			thoughtlessConfirmed = false;
			return true;
		}
		ArrayList<Item> inv = new ArrayList<>();
		for (Item i : hero.belongings) if (thoughtlessAffects( i )) inv.add( i );
		ArrayList<Item> heaps = new ArrayList<>();
		for (Heap h : Dungeon.level.heaps.valueList()) {
			if (h.seen && !h.isEmpty() && thoughtlessAffects( h.peek() )) heaps.add( h.peek() );
		}
		if (inv.isEmpty() && heaps.isEmpty()) return true;
		if (GameScene.scene == null) return true;
		final int heroPos = hero.pos;
		String key = !inv.isEmpty() && !heaps.isEmpty() ? "thoughtless_ask_both" : !inv.isEmpty() ? "thoughtless_ask_inventory" : "thoughtless_ask_heaps";
		final String text = Messages.get( TooCruel.class, key, names( inv ), names( heaps ) );
		Game.runOnRenderThread( new Callback() {
			@Override
			public void call() {
				GameScene.show( new WndOptions( Messages.get( TooCruel.class, "thoughtless_ask_title" ), text,
						Messages.get( TooCruel.class, "thoughtless_yes" ), Messages.get( TooCruel.class, "thoughtless_no" ) ) {
					@Override
					protected void onSelect( int index ) {
						if (index == 0 && Dungeon.hero.pos == heroPos) {
							thoughtlessConfirmed = true;
							hero.lastAction = new HeroAction.LvlTransition( heroPos );
							hero.resume();
						}
					}
				} );
			}
		} );
		return false;
	}

	private static void clearThoughtlessItems() {
		Hero hero = Dungeon.hero;
		boolean inventory = false;
		for (Item item : new ArrayList<Item>( toList( hero ) )) {
			if (thoughtlessAffects( item ) && !item.isEquipped( hero )) {
				inventory = true;
				item.detachAll( hero.belongings.backpack );
			}
		}
		final boolean[] heaps = { false };
		transformLevelItems( Dungeon.level, new TcStoredHeapData.ItemFn() {
			@Override
			public Item apply( Item item ) {
				if (thoughtlessAffects( item )) {
					heaps[0] = true;
					return null;
				}
				return item;
			}
		} );
		if (heaps[0] || inventory) {
			TcTrackers.BurstVisual b = Buff.affect( hero, TcTrackers.BurstVisual.class );
			b.heaps = heaps[0];
			b.inventory = inventory;
		}
	}

	private static ArrayList<Item> toList( Hero hero ) {
		ArrayList<Item> list = new ArrayList<>();
		for (Item i : hero.belongings) list.add( i );
		return list;
	}

	//Crumbled Stairs turns off teleporting to other floors
	public static boolean blockInterfloor() {
		return on( CRUMBLED_STAIRS );
	}

	//Extermination, falling into a chasm while the floor is sealed teleports instead, returns true if handled
	public static boolean chasmFall() {
		if (!on( EXTERMINATION ) || Dungeon.hero == null || !Dungeon.hero.isAlive()) return false;
		if (Exterminating.exterminationDone( Dungeon.level )) return false;
		com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation.teleportChar( Dungeon.hero );
		com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm.heroLand();
		return true;
	}

	/*****************************************************************
	 * Building a floor
	 *****************************************************************/

	private static final ArrayList<Item> nearEntrance = new ArrayList<>();

	//after a floor has its monsters and items, still inside the floor's seeded generator
	public static void postCreate( Level level ) {
		if (GameModes.count( GameModes.MOD_TOOCRUEL ) == 0) return;
		levelBuilding = true;
		try {
			if (on( HEAD_START ) && Dungeon.depth == 1 && Dungeon.branch == 0) applyHeadStart( level );
			if (on( DROUGHT )) applyDrought( level );
			if (on( SECOND_TRY )) applySecondTry( level );
			if (on( HOLY_WATER )) applyHolyWater( level );
			if (on( LOFT ) && !bossOrSpecial()) applyLoft( level );
			if (on( LOOT_PARADISE )) applyLootParadise( level );
			if (on( CURSED )) {
				transformLevelItems( level, new TcStoredHeapData.ItemFn() {
					@Override
					public Item apply( Item item ) {
						curse( item, true );
						return item;
					}
				} );
			}
			if (on( MIMICS )) applyMimics( level );
			if (on( IN_YOUR_FACE )) applyInYourFace( level );
			if (on( EXTERMINATION )) applyExtermination( level );
			if (on( JACK_IN_A_BOX )) applyJackInTheBox( level );
			if (on( BOXED )) applyBoxed( level );
			if (on( RECURSIVE_HIERARCHY )) applyRecursiveHierarchy( level );
			if (on( THUNDERSTRUCK )) {
				for (Trap t : level.traps.valueList()) {
					t.reveal();
					if (level.map[t.pos] == Terrain.SECRET_TRAP) Level.set( t.pos, Terrain.TRAP, level );
				}
			}
			if (on( OVER_THE_EDGE )) applyOverTheEdge( level );
			if (on( DOMAIN_OF_HELL )) applyDomainOfHell( level );
			if (on( PROTECTED_GOODS )) applyProtectedGoods( level );
			if (on( FORCED_PACIFISM )) nearEntrance.add( new PhaseShift() );
			placeNearEntrance( level );
		} finally {
			levelBuilding = false;
			nearEntrance.clear();
		}
	}

	private static void placeNearEntrance( Level level ) {
		if (nearEntrance.isEmpty()) return;
		int entrance = level.entrance();
		if (entrance <= 0) return;
		ArrayList<Integer> valid = new ArrayList<>();
		for (int o : PathFinder.NEIGHBOURS8) {
			int p = entrance + o;
			if (p >= 0 && p < level.length() && level.passable[p]) valid.add( p );
		}
		for (Item item : nearEntrance) {
			int cell = valid.isEmpty() ? entrance : Random.element( valid );
			level.drop( item, cell );
			furrowCell( level, cell );
		}
	}

	private static void applyHeadStart( Level level ) {
		while (Dungeon.LimitedDrops.UPGRADE_SCROLLS.count < 2) {
			int cell = randomFreeCell( level );
			if (cell < 0) break;
			furrowCell( level, cell );
			level.drop( new ScrollOfUpgrade(), cell );
			Dungeon.LimitedDrops.UPGRADE_SCROLLS.count++;
		}
		while (Dungeon.LimitedDrops.STRENGTH_POTIONS.count < 1) {
			int cell = randomFreeCell( level );
			if (cell < 0) break;
			furrowCell( level, cell );
			level.drop( new PotionOfStrength(), cell );
			Dungeon.LimitedDrops.STRENGTH_POTIONS.count++;
		}
	}

	private static void applyDrought( Level level ) {
		for (int i = 0; i < level.length(); i++) {
			int t = level.map[i];
			boolean grass = t == Terrain.GRASS || t == Terrain.HIGH_GRASS || t == Terrain.FURROWED_GRASS;
			if ((grass || t == Terrain.WATER) && Random.Float() < 0.8f) level.map[i] = Terrain.EMPTY;
		}
		level.buildFlagMaps();
	}

	private static void applySecondTry( Level level ) {
		if (bossOrSpecial()) return;
		int barricades = 0;
		for (int i = 0; i < level.length(); i++) {
			if (level.map[i] == Terrain.LOCKED_DOOR) Level.set( i, Terrain.DOOR, level );
			if (level.map[i] == Terrain.CRYSTAL_DOOR) Level.set( i, Terrain.EMPTY, level );
			if (level.map[i] == Terrain.BARRICADE) {
				Level.set( i, Terrain.EMBERS, level );
				barricades++;
			}
		}
		for (Heap h : level.heaps.valueList()) {
			if (h.type == Heap.Type.FOR_SALE) continue;
			for (Item item : new ArrayList<>( h.items )) {
				if (item.getClass() == IronKey.class) { h.items.remove( item ); continue; }
				if (item.unique) continue;
				//only the items every floor is promised are kept, as the mod kept the floor's guaranteed items
				if (!(item instanceof ScrollOfUpgrade || item instanceof PotionOfStrength || item instanceof Stylus
						|| item instanceof com.shatteredpixel.shatteredpixeldungeon.items.trinkets.TrinketCatalyst
						|| item instanceof StoneOfEnchantment || item instanceof StoneOfIntuition
						|| item instanceof com.shatteredpixel.shatteredpixeldungeon.items.food.Food)) {
					h.items.remove( item );
				}
			}
			if (h.items.isEmpty()) level.heaps.remove( h.pos );
		}
		for (Blob b : new ArrayList<>( level.blobs.values() )) {
			if (b instanceof WellWater) level.blobs.remove( b.getClass() );
		}
	}

	private static void applyHolyWater( Level level ) {
		for (int i = 0; i < level.length(); i++) {
			if (level.map[i] == Terrain.WATER) Blob.seed( i, PatronSaintsBlob.SEED_BLESS, PatronSaintsBlob.class, level );
		}
	}

	private static void applyLoft( Level level ) {
		for (int i = 0; i < level.length(); i++) {
			if (level.map[i] == Terrain.WALL || level.map[i] == Terrain.WALL_DECO || !level.insideMap( i )) {
				level.map[i] = Terrain.CHASM;
			}
		}
		level.buildFlagMaps();
		level.cleanWalls();
	}

	private static void applyLootParadise( Level level ) {
		ArrayList<Integer> cells = new ArrayList<>();
		for (int i = 0; i < level.length(); i++) {
			if (!level.solid[i] && !level.pit[i] && !isTransition( level, i ) && level.insideMap( i )) cells.add( i );
		}
		Random.shuffle( cells );
		int amount = (3 + Random.chances( new float[]{ 6, 3, 1 } ) + (level.feeling == Level.Feeling.LARGE ? 2 : 0)) * 10;
		for (int i = 0; i < amount && i < cells.size(); i++) {
			Item item = Generator.random();
			if (item == null) continue;
			furrowCell( level, cells.get( i ) );
			level.drop( item, cells.get( i ) );
		}
	}

	private static void applyMimics( Level level ) {
		for (Heap h : level.heaps.valueList()) {
			if (h.type == Heap.Type.CHEST || on( MIMICS_ALL )) {
				level.heaps.remove( h.pos );
				if (!TcStoredHeapData.transformHeapIntoMimic( level, h, on( MIMICS_GRIND ), !on( MIMICS_GRIND ) )) {
					level.heaps.put( h.pos, h );
				}
			}
		}
	}

	private static void applyInYourFace( Level level ) {
		if (bossOrSpecial()) return;
		int entrance = level.entrance();
		if (entrance <= 0) return;
		ArrayList<Integer> cells = new ArrayList<>();
		for (int o : PathFinder.NEIGHBOURS8) {
			int c = entrance + o;
			if (c >= 0 && c < level.length() && !level.solid[c] && !level.pit[c] && (level.passable[c] || level.avoid[c])
					&& level.insideMap( c ) && level.findMob( c ) == null) {
				cells.add( c );
			}
		}
		Random.shuffle( cells );
		int count = Dungeon.depth == 1 ? 1 : Random.NormalIntRange( 1, 3 );
		while (count-- > 0 && !cells.isEmpty()) {
			Mob mob = level.createMob();
			if (mob == null) break;
			mob.pos = cells.remove( cells.size() - 1 );
			mob.state = mob.PASSIVE;
			Buff.affect( mob, TcTrackers.DelayedBeckon.class ).start( 0 );
			level.mobs.add( mob );
		}
	}

	private static void applyExtermination( Level level ) {
		if (bossOrSpecial()) return;
		boolean holders = on( MIMICS );
		boolean reachable = on( POSTPAID_LOOT );
		int entrance = level.entrance();
		if (reachable && entrance > 0) {
			PathFinder.buildDistanceMap( entrance, BArray.or( level.passable, level.avoid, null ) );
		}
		ExterminationItemLock lock = null;
		if (on( POSTPAID_LOOT )) {
			lock = new ExterminationItemLock();
			level.blobs.put( ExterminationItemLock.class, lock );
			for (Heap h : level.heaps.valueList()) {
				if (h.type == Heap.Type.FOR_SALE) continue;
				lock.lockItem( level, h );
			}
			for (Mob mob : level.mobs.toArray( new Mob[0] )) {
				if (mob instanceof Mimic || mob instanceof Statue) lock.lockMob( level, mob );
			}
		}
		boolean any = false;
		for (Mob m : level.mobs) {
			if (m instanceof NPC || m.alignment != Char.Alignment.ENEMY) continue;
			if (!holders && (m instanceof Mimic || m instanceof Statue)) continue;
			if (reachable && entrance > 0 && (m.pos < 0 || PathFinder.distance[m.pos] == Integer.MAX_VALUE)) continue;
			Buff.affect( m, Exterminating.class );
			any = true;
		}
		if (!any && lock != null) lock.unlockAll( level );
	}

	private static void transferHolding( Mob from, Mob to ) {
		if (from.buff( Exterminating.class ) != null) Buff.affect( to, Exterminating.class );
		TcTrackers.DelayedBeckon b = from.buff( TcTrackers.DelayedBeckon.class );
		if (b != null) Buff.affect( to, TcTrackers.DelayedBeckon.class ).start( b.getTicker() );
	}

	private static boolean holdable( Mob mob ) {
		return !(mob.properties().contains( Char.Property.BOSS ) || mob.properties().contains( Char.Property.MINIBOSS )
				|| mob.properties().contains( Char.Property.IMMOVABLE ) || mob instanceof Mimic || mob instanceof NPC
				|| mob.alignment != Char.Alignment.ENEMY);
	}

	private static void applyJackInTheBox( Level level ) {
		for (Mob mob : level.mobs.toArray( new Mob[0] )) {
			if (!holdable( mob )) continue;
			TcStoredHeapData data = TcStoredHeapData.fromMob( mob );
			TcHolderMimic m = new TcHolderMimic();
			m.setLevel( Dungeon.scalingDepth() );
			m.pos = mob.pos;
			Buff.affect( m, HoldingHeap.class ).set( data );
			transferHolding( mob, m );
			level.mobs.remove( mob );
			level.mobs.add( m );
		}
	}

	private static void applyBoxed( Level level ) {
		if (bossOrSpecial() || !(level instanceof RegularLevel)) return;
		RegularLevel rl = (RegularLevel) level;
		if (rl.rooms() == null) return;
		int w = level.width();
		for (Room room : rl.rooms()) {
			if (room instanceof EntranceRoom || room instanceof ConnectionRoom) continue;
			for (int x = room.left; x <= room.right; x++) {
				cells:
				for (int y = room.top; y <= room.bottom; y++) {
					if (x > room.left + 1 && x < room.right - 1 && y > room.top + 1 && y < room.bottom - 1) continue;
					int i = x + y * w;
					if (i < 0 || i >= level.length() || !level.insideMap( i )) continue;
					if (level.solid[i] || level.pit[i] || !(level.passable[i] || level.avoid[i])) continue;
					if (isTransition( level, i )) continue;
					boolean nearSolid = false;
					for (int j = 0; j < PathFinder.CIRCLE8.length; j++) {
						int cell = i + PathFinder.CIRCLE8[j];
						if (level.solid[cell]) nearSolid = true;
						if (j % 2 == 1 && isDoor( level, cell )) continue cells;
					}
					if (!nearSolid || level.findMob( i ) != null) continue;
					Mob mob = level.createMob();
					if (mob == null) continue;
					TcStoredHeapData data = TcStoredHeapData.fromMob( mob );
					TcHolderMimic m = new TcHolderMimic();
					Buff.affect( m, HoldingHeap.class ).set( data );
					Buff.affect( m, TcTrackers.InvulnerableUntilSeen.class );
					m.setLevel( Dungeon.scalingDepth() );
					m.pos = i;
					level.mobs.add( m );
				}
			}
		}
	}

	private static boolean isDoor( Level level, int cell ) {
		int t = level.map[cell];
		return t == Terrain.DOOR || t == Terrain.OPEN_DOOR || t == Terrain.LOCKED_DOOR || t == Terrain.SECRET_DOOR || t == Terrain.CRYSTAL_DOOR;
	}

	private static void applyRecursiveHierarchy( Level level ) {
		for (Mob mob : level.mobs.toArray( new Mob[0] )) {
			if (!(mob instanceof Mimic)) continue;
			Mob newMob = mob;
			int max = Dungeon.scalingDepth() / 5;
			int steps = 0;
			while (Random.Float() < 0.5f && steps < max) {
				steps++;
				TcStoredHeapData data = TcStoredHeapData.fromMob( newMob );
				TcHolderMimic m = new TcHolderMimic();
				m.setLevel( Dungeon.scalingDepth() );
				m.pos = mob.pos;
				Buff.affect( m, HoldingHeap.class ).set( data );
				newMob = m;
			}
			if (newMob != mob) {
				level.mobs.remove( mob );
				level.mobs.add( newMob );
				transferHolding( mob, newMob );
				Resizing r = Buff.affect( newMob, Resizing.class );
				if (r != null) {
					r.multiply( (float) (1 / Math.pow( 1.1f, steps - 1 )) );
					if (on( CROWD_DIVERSITY )) r.multiplyRandom();
				}
				Buff.affect( newMob, TcTrackers.RecursiveResizing.class ).set( 1.1f );
			}
		}
	}

	private static void applyDomainOfHell( Level level ) {
		nearEntrance.add( new AquaBrew() );
		int entrance = level.entrance();
		if (entrance > 0 && level instanceof RegularLevel) {
			Room room = ((RegularLevel) level).room( entrance );
			if (room != null) {
				for (Point p : room.getPoints()) {
					int pos = level.pointToCell( p );
					if (pos != entrance && pos >= 0 && pos < level.length() && level.flamable[pos] && !level.solid[pos]) {
						Level.set( pos, Terrain.WATER, level );
					}
				}
			}
		}
		igniteAllGrass( level );
	}

	private static void applyProtectedGoods( final Level level ) {
		if (bossOrSpecial()) return;
		final boolean headStart = on( HEAD_START ) && Dungeon.depth == 1;
		final int[] allowed = { headStart ? 1 : 0, headStart ? 2 : 0 };
		transformLevelItems( level, new TcStoredHeapData.ItemFn() {
			@Override
			public Item apply( Item it ) {
				if (it instanceof ScrollOfUpgrade || it instanceof PotionOfStrength || it instanceof Stylus) {
					if (headStart && it instanceof ScrollOfUpgrade && allowed[1] > 0) { allowed[1]--; return it; }
					if (headStart && it instanceof PotionOfStrength && allowed[0] > 0) { allowed[0]--; return it; }
					if (Dungeon.hero != null) Buff.affect( Dungeon.hero, ProtectedGoodsTracker.class ).addItem( it );
					return new IOU( it );
				}
				return it;
			}
		} );
	}

	private static void applyOverTheEdge( Level level ) {
		if (bossOrSpecial()) return;
		int entrance = level.entrance();
		if (entrance <= 0) return;
		level.buildFlagMaps();
		HashSet<Integer> noVoid = new HashSet<>();
		HashSet<Integer> voidCells = new HashSet<>();
		boolean[] passable = BArray.not( level.pit, null );
		BArray.and( level.avoid, passable, passable );
		BArray.or( level.passable, passable, passable );
		int w = level.width();
		for (int i = w; i < level.length() - w; i++) {
			if (canBeVoid( level, i )) voidCells.add( i );
			int t = level.map[i];
			if (t == Terrain.DOOR || t == Terrain.LOCKED_DOOR || t == Terrain.OPEN_DOOR || t == Terrain.SECRET_DOOR
					|| t == Terrain.CRYSTAL_DOOR || t == Terrain.BARRICADE || t == Terrain.BOOKSHELF || t == Terrain.ALCHEMY
					|| t == Terrain.PEDESTAL || t == Terrain.WELL) {
				noVoid.add( i );
				passable[i] = true;
				for (int o : PathFinder.NEIGHBOURS4) if (level.passable[i + o]) noVoid.add( i + o );
			}
		}
		for (int k : level.plants.keyArray()) noVoid.add( k );
		for (Mob mob : level.mobs) {
			if (mob.pos < 0 || mob.pos >= level.length()) continue;
			if (mob instanceof NPC) {
				voidCells.remove( mob.pos );
				passable[mob.pos] = false;
				for (int o : PathFinder.NEIGHBOURS4) if (passable[mob.pos + o]) noVoid.add( mob.pos + o );
			} else {
				noVoid.add( mob.pos );
			}
		}
		for (Heap h : level.heaps.valueList()) noVoid.add( h.pos );
		ExterminationItemLock lock = (ExterminationItemLock) level.blobs.get( ExterminationItemLock.class );
		if (lock != null) noVoid.addAll( lock.lockedItems().keySet() );
		for (Trap t : level.traps.valueList()) {
			noVoid.add( t.pos );
			passable[t.pos] = true;
			if (t instanceof GatewayTrap || t instanceof GrimTrap) {
				for (int o : PathFinder.NEIGHBOURS4) if (passable[t.pos + o]) noVoid.add( t.pos + o );
			}
		}
		for (com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap ct : level.customTiles) {
			for (int x = ct.tileX; x < ct.tileX + ct.tileW; x++) {
				for (int y = ct.tileY; y < ct.tileY + ct.tileH; y++) noVoid.add( x + y * w );
			}
		}
		ArrayList<Integer> shuffled = new ArrayList<>( voidCells );
		Collections.sort( shuffled );
		Random.shuffle( shuffled );
		for (int i = 0; i < shuffled.size() / 10; i++) noVoid.add( shuffled.get( i ) );

		HashSet<Integer> important = new HashSet<>( noVoid );
		PathFinder.buildDistanceMap( entrance, passable );
		important.removeIf( c -> c < 0 || c >= level.length() || PathFinder.distance[c] == Integer.MAX_VALUE );
		for (LevelTransition t : level.transitions) {
			int cell = t.cell();
			if (PathFinder.distance[cell] == Integer.MAX_VALUE) return;
			important.add( cell );
			for (int o : PathFinder.NEIGHBOURS4) if (passable[cell + o]) important.add( cell + o );
		}
		voidCells.removeAll( important );
		voidCells.removeAll( noVoid );

		boolean[] temp = passable.clone();
		HashSet<Integer> toDelete = new HashSet<>();
		int tries = 0;
		ArrayList<Integer> picked = new ArrayList<>();
		while (!voidCells.isEmpty() && tries < 100) {
			ArrayList<Integer> list = new ArrayList<>( voidCells );
			Collections.sort( list );
			int batch = tries > 50 ? 1 : Math.max( list.size() / 10, 1 );
			picked.clear();
			for (int i = 0; i < batch; i++) {
				int c = list.get( Random.Int( list.size() ) );
				if (!picked.contains( c )) {
					picked.add( c );
					temp[c] = false;
				}
			}
			PathFinder.buildDistanceMap( entrance, temp );
			boolean blocked = false;
			for (int c : important) {
				if (PathFinder.distance[c] == Integer.MAX_VALUE) { blocked = true; break; }
			}
			if (blocked) {
				System.arraycopy( passable, 0, temp, 0, passable.length );
				tries++;
			} else {
				System.arraycopy( temp, 0, passable, 0, passable.length );
				voidCells.removeAll( picked );
				toDelete.addAll( picked );
				tries = 0;
			}
		}
		ArrayList<Integer> del = new ArrayList<>( toDelete );
		Collections.sort( del );
		for (int i : del) {
			if (Random.Float() < 0.8f && level.heaps.get( i ) == null) level.map[i] = Terrain.CHASM;
		}
		level.buildFlagMaps();
	}

	private static boolean canBeVoid( Level level, int cell ) {
		if (!level.insideMap( cell )) return false;
		int t = level.map[cell];
		return t == Terrain.EMPTY || t == Terrain.EMPTY_SP || t == Terrain.EMPTY_DECO || t == Terrain.EMBERS
				|| t == Terrain.GRASS || t == Terrain.HIGH_GRASS || t == Terrain.FURROWED_GRASS || t == Terrain.WATER;
	}

	/*****************************************************************
	 * Starting a run and loading one
	 *****************************************************************/

	//after the hero has the starting kit
	public static void postInit( Hero hero ) {
		refresh();
		if (on( CURSED )) {
			for (Item item : hero.belongings) curse( item, true );
		}
		RetieredBuff r = hero.buff( RetieredBuff.class );
		if (r != null) r.processInventory( hero );
	}

	//keeps the engine's dice flag in step with the modes of the run
	public static void refresh() {
		Random.crookedDie = on( CROOKED_DIE );
	}

	public static void switchLevel( Hero hero ) {
		for (Buff b : hero.buffs().toArray( new Buff[0] )) {
			if (b instanceof TcBuff.SwitchLevel) ((TcBuff.SwitchLevel) b).onSwitchLevel();
		}
	}
}
