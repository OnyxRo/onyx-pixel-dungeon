/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon;

public class Assets {

	public static class Effects {
		public static final String EFFECTS      = "effects/effects.png";
		public static final String FIREBALL     = "effects/fireball.png";
		public static final String SPECKS       = "effects/specks.png";
		public static final String SPELL_ICONS  = "effects/spell_icons.png";
		public static final String TEXT_ICONS   = "effects/text_icons.png";
	}

	public static class Environment {
		public static final String TILES_TRUE_VOID = "environment/devoted/tiles_truevoid.png"; //ported, devoted
		public static final String GARDENS_BOSS = "environment/custom_tiles/devoted/gardens_boss.png"; //ported, devoted
		public static final String WATER_GARDEN = "environment/devoted/water5.png"; //ported, devoted
		public static final String TILES_GARDEN = "environment/devoted/tiles_garden.png"; //ported, devoted
		public static final String COLDHOUSE_BOSS = "environment/custom_tiles/devoted/coldhouse_boss.png"; //ported, devoted
		public static final String WATER_COLDHOUSE = "environment/devoted/water6.png"; //ported, devoted
		public static final String TILES_COLDHOUSE = "environment/devoted/tiles_coldhouse.png"; //ported, devoted
		public static final String VOID_SP = "environment/custom_tiles/devoted/void_special.png"; //ported, devoted
		public static final String WATER_VOID = "environment/devoted/water9.png"; //ported, devoted
		public static final String TILES_VOID = "environment/devoted/tiles_void.png"; //ported, devoted
		public static final String CITADEL_BOSS = "environment/custom_tiles/devoted/citadel_boss.png"; //ported, devoted
		public static final String WATER_CITADEL = "environment/devoted/water8.png"; //ported, devoted
		public static final String TILES_CITADEL = "environment/devoted/tiles_citadel.png"; //ported, devoted
		public static final String FORGE_BOSS = "environment/custom_tiles/devoted/forge_boss.png"; //ported, devoted
		public static final String WATER_FORGE = "environment/devoted/water7.png"; //ported, devoted
		public static final String TILES_FORGE = "environment/devoted/tiles_forge.png"; //ported, devoted
		public static final String TERRAIN_FEATURES = "environment/terrain_features.png";
		public static final String RAISED_TERRAIN = "environment/raised_terrain.png";

		public static final String VISUAL_GRID          = "environment/visual_grid.png";
		public static final String WALL_BLOCKING        = "environment/wall_blocking.png";
		public static final String OCCLUSION_SHADOWS    = "environment/occlusion_shadows.png";

		public static final String TILES_SEWERS = "environment/tiles_sewers.png";
		public static final String TILES_PRISON = "environment/tiles_prison.png";
		public static final String TILES_CAVES  = "environment/tiles_caves.png";
		public static final String TILES_CITY   = "environment/tiles_city.png";
		public static final String TILES_HALLS  = "environment/tiles_halls.png";

		public static final String TILES_CAVES_CRYSTAL  = "environment/tiles_caves_crystal.png";
		public static final String TILES_CAVES_GNOLL    = "environment/tiles_caves_gnoll.png";

		public static final String WATER_SEWERS = "environment/water0.png";
		public static final String WATER_PRISON = "environment/water1.png";
		public static final String WATER_CAVES  = "environment/water2.png";
		public static final String WATER_CITY   = "environment/water3.png";
		public static final String WATER_HALLS  = "environment/water4.png";

		public static final String CARPET           = "environment/custom_tiles/carpet.png";
		public static final String RAT_KING_ROOM    = "environment/custom_tiles/rat_king_room.png";
		public static final String WEAK_FLOOR       = "environment/custom_tiles/weak_floor.png";
		public static final String SEWER_BOSS       = "environment/custom_tiles/sewer_boss.png";
		public static final String PRISON_QUEST     = "environment/custom_tiles/prison_quest.png";
		public static final String PRISON_EXIT      = "environment/custom_tiles/prison_exit.png";
		public static final String CAVES_QUEST      = "environment/custom_tiles/caves_quest.png";
		public static final String CAVES_BOSS       = "environment/custom_tiles/caves_boss.png";
		public static final String CITY_QUEST       = "environment/custom_tiles/city_quest.png";
		public static final String CITY_BOSS        = "environment/custom_tiles/city_boss.png";
		public static final String HALLS_SP         = "environment/custom_tiles/halls_special.png";
	}
	
	//TODO include other font assets here? Some are platform specific though...
	public static class Fonts {
		public static final String PIXELFONT= "fonts/pixel_font.png";
	}

	public static class Interfaces {
		public static final String ARCS_BG  = "interfaces/arcs1.png";
		public static final String ARCS_FG  = "interfaces/arcs2.png";

		public static final String BANNERS  = "interfaces/banners.png";
		public static final String ONYX_LOGO = "interfaces/onyx_logo.png";
		public static final String BADGES   = "interfaces/badges.png";
		public static final String LOCKED   = "interfaces/locked_badge.png";

		public static final String CHROME   = "interfaces/chrome.png";
		public static final String ICONS    = "interfaces/icons.png";
		public static final String STATUS   = "interfaces/status_pane.png";
		public static final String MENU     = "interfaces/menu_pane.png";
		public static final String MENU_BTN = "interfaces/menu_button.png";
		public static final String TOOLBAR  = "interfaces/toolbar.png";
		public static final String SHADOW   = "interfaces/shadow.png";
		public static final String BOSSHP   = "interfaces/boss_hp.png";

		public static final String SURFACE  = "interfaces/surface.png";

		public static final String BUFFS_SMALL      = "interfaces/buffs.png";
		public static final String BUFFS_LARGE      = "interfaces/large_buffs.png";

		public static final String TALENT_ICONS     = "interfaces/talent_icons.png";
		public static final String TALENT_BUTTON    = "interfaces/talent_button.png";

		public static final String HERO_ICONS       = "interfaces/hero_icons.png";

		public static final String RADIAL_MENU      = "interfaces/radial_menu.png";

		public static final String CHANGE_ICONS    = "interfaces/change_icons.png";
	}

	//these points to resource bundles, not raw asset files
	public static class Messages {
		public static final String ACTORS   = "messages/actors/actors";
		public static final String ITEMS    = "messages/items/items";
		public static final String JOURNAL  = "messages/journal/journal";
		public static final String LEVELS   = "messages/levels/levels";
		public static final String MISC     = "messages/misc/misc";
		public static final String PLANTS   = "messages/plants/plants";
		public static final String SCENES   = "messages/scenes/scenes";
		public static final String UI       = "messages/ui/ui";
		public static final String WINDOWS  = "messages/windows/windows";
	}

	public static class Music {
		public static final String SHADOW_REALM = "music/devoted/10.1-LiturgiaEclipsis-Vel'Taleth.ogg"; //ported, devoted
		public static final String GARDENS_BOSS = "music/devoted/02-PromisedParadise-GnollEmperor.ogg"; //ported, devoted
		public static final String GARDENS = "music/devoted/01-TheSecretofSpring-TheGardens.ogg"; //ported, devoted
		public static final String COLDHOUSE = "music/devoted/03-IridescentWaltz-Coldhouse.ogg"; //ported, devoted
		public static final String COLDHOUSE_BOSS = "music/devoted/04-TheIcyBeast-RatMonstrosity.ogg"; //ported, devoted
		public static final String VOID_BOSS = "music/devoted/10-InNomineNihilo-Vel'Taleth.ogg"; //ported, devoted
		public static final String CITADEL = "music/devoted/07-TheAshenRite-DwarvenCitadel.ogg"; //ported, devoted
		public static final String CITADEL_BOSS = "music/devoted/08-TheInfernalTribunal-DwarvenCourt.ogg"; //ported, devoted
		public static final String FORGE = "music/devoted/05-MudAndIron-TrollMines.ogg"; //ported, devoted
		public static final String FORGE_BOSS = "music/devoted/06-MechanismInTheDepths-FurnaceGolem.ogg"; //ported, devoted
		public static final String THEME_1              = "music/theme_1.ogg";
		public static final String THEME_2              = "music/theme_2.ogg";
		public static final String THEME_FINALE         = "music/theme_finale.ogg";

		public static final String SEWERS_1             = "music/sewers_1.ogg";
		public static final String SEWERS_2             = "music/sewers_2.ogg";
		public static final String SEWERS_3             = "music/sewers_3.ogg";
		public static final String SEWERS_TENSE         = "music/sewers_tense.ogg";
		public static final String SEWERS_BOSS          = "music/sewers_boss.ogg";

		public static final String PRISON_1             = "music/prison_1.ogg";
		public static final String PRISON_2             = "music/prison_2.ogg";
		public static final String PRISON_3             = "music/prison_3.ogg";
		public static final String PRISON_TENSE         = "music/prison_tense.ogg";
		public static final String PRISON_BOSS          = "music/prison_boss.ogg";

		public static final String CAVES_1              = "music/caves_1.ogg";
		public static final String CAVES_2              = "music/caves_2.ogg";
		public static final String CAVES_3              = "music/caves_3.ogg";
		public static final String CAVES_TENSE          = "music/caves_tense.ogg";
		public static final String CAVES_BOSS           = "music/caves_boss.ogg";
		public static final String CAVES_BOSS_FINALE    = "music/caves_boss_finale.ogg";

		public static final String CITY_1               = "music/city_1.ogg";
		public static final String CITY_2               = "music/city_2.ogg";
		public static final String CITY_3               = "music/city_3.ogg";
		public static final String CITY_TENSE           = "music/city_tense.ogg";
		public static final String CITY_BOSS            = "music/city_boss.ogg";
		public static final String CITY_BOSS_FINALE     = "music/city_boss_finale.ogg";

		public static final String HALLS_1              = "music/halls_1.ogg";
		public static final String HALLS_2              = "music/halls_2.ogg";
		public static final String HALLS_3              = "music/halls_3.ogg";
		public static final String HALLS_TENSE          = "music/halls_tense.ogg";
		public static final String HALLS_BOSS           = "music/halls_boss.ogg";
		public static final String HALLS_BOSS_FINALE    = "music/halls_boss_finale.ogg";
	}

	public static class Sounds {
		public static final String EXPLOSION = "sounds/devoted/giant_explosion.mp3"; //ported, devoted
		public static final String BLACKHOLE = "sounds/devoted/black-hole.mp3"; //ported, devoted
		public static final String HOWL = "sounds/devoted/howl.mp3"; //ported, devoted
		public static final String CLICK    = "sounds/click.mp3";
		public static final String BADGE    = "sounds/badge.mp3";
		public static final String GOLD     = "sounds/gold.mp3";

		public static final String OPEN     = "sounds/door_open.mp3";
		public static final String UNLOCK   = "sounds/unlock.mp3";
		public static final String ITEM     = "sounds/item.mp3";
		public static final String DEWDROP  = "sounds/dewdrop.mp3";
		public static final String STEP     = "sounds/step.mp3";
		public static final String WATER    = "sounds/water.mp3";
		public static final String GRASS    = "sounds/grass.mp3";
		public static final String TRAMPLE  = "sounds/trample.mp3";
		public static final String STURDY   = "sounds/sturdy.mp3";

		public static final String HIT              = "sounds/hit.mp3";
		public static final String MISS             = "sounds/miss.mp3";
		public static final String HIT_SLASH        = "sounds/hit_slash.mp3";
		public static final String HIT_STAB         = "sounds/hit_stab.mp3";
		public static final String HIT_CRUSH        = "sounds/hit_crush.mp3";
		public static final String HIT_MAGIC        = "sounds/hit_magic.mp3";
		public static final String HIT_STRONG       = "sounds/hit_strong.mp3";
		public static final String HIT_PARRY        = "sounds/hit_parry.mp3";
		public static final String HIT_ARROW        = "sounds/hit_arrow.mp3";
		public static final String ATK_SPIRITBOW    = "sounds/atk_spiritbow.mp3";
		public static final String ATK_CROSSBOW     = "sounds/atk_crossbow.mp3";
		public static final String HEALTH_WARN      = "sounds/health_warn.mp3";
		public static final String HEALTH_CRITICAL  = "sounds/health_critical.mp3";

		public static final String DESCEND  = "sounds/descend.mp3";
		public static final String EAT      = "sounds/eat.mp3";
		public static final String READ     = "sounds/read.mp3";
		public static final String LULLABY  = "sounds/lullaby.mp3";
		public static final String DRINK    = "sounds/drink.mp3";
		public static final String SHATTER  = "sounds/shatter.mp3";
		public static final String ZAP      = "sounds/zap.mp3";
		public static final String LIGHTNING= "sounds/lightning.mp3";
		public static final String LEVELUP  = "sounds/levelup.mp3";
		public static final String DEATH    = "sounds/death.mp3";
		public static final String CHALLENGE= "sounds/challenge.mp3";
		public static final String CURSED   = "sounds/cursed.mp3";
		public static final String TRAP     = "sounds/trap.mp3";
		public static final String BALLISTA_RELOAD = "sounds/ballista_reload.mp3"; //ported, reforged
		public static final String EVOKE    = "sounds/evoke.mp3";
		public static final String TOMB     = "sounds/tomb.mp3";
		public static final String ALERT    = "sounds/alert.mp3";
		public static final String MELD     = "sounds/meld.mp3";
		public static final String BOSS     = "sounds/boss.mp3";
		public static final String BLAST    = "sounds/blast.mp3";
		public static final String PLANT    = "sounds/plant.mp3";
		public static final String RAY      = "sounds/ray.mp3";
		public static final String BEACON   = "sounds/beacon.mp3";
		public static final String TELEPORT = "sounds/teleport.mp3";
		public static final String CHARMS   = "sounds/charms.mp3";
		public static final String MASTERY  = "sounds/mastery.mp3";
		public static final String PUFF     = "sounds/puff.mp3";
		public static final String ROCKS    = "sounds/rocks.mp3";
		public static final String BURNING  = "sounds/burning.mp3";
		public static final String FALLING  = "sounds/falling.mp3";
		public static final String GHOST    = "sounds/ghost.mp3";
		public static final String SECRET   = "sounds/secret.mp3";
		public static final String BONES    = "sounds/bones.mp3";
		public static final String BEE      = "sounds/bee.mp3";
		public static final String DEGRADE  = "sounds/degrade.mp3";
		public static final String MIMIC    = "sounds/mimic.mp3";
		public static final String DEBUFF   = "sounds/debuff.mp3";
		public static final String CHARGEUP = "sounds/chargeup.mp3";
		public static final String GAS      = "sounds/gas.mp3";
		public static final String CHAINS   = "sounds/chains.mp3";
		public static final String SCAN     = "sounds/scan.mp3";
		public static final String SHEEP    = "sounds/sheep.mp3";
		public static final String MINE    = "sounds/mine.mp3";

		//Devoted Pixel Dungeon
		public static final String DIG = "sounds/devoted_digging.mp3";
		public static final String WHAIL = "sounds/devoted_whail.mp3";

		public static final String[] all = new String[]{
				DIG, WHAIL, EXPLOSION, BLACKHOLE, HOWL,
				CLICK, BADGE, GOLD,

				OPEN, UNLOCK, ITEM, DEWDROP, STEP, WATER, GRASS, TRAMPLE, STURDY,

				HIT, MISS, HIT_SLASH, HIT_STAB, HIT_CRUSH, HIT_MAGIC, HIT_STRONG, HIT_PARRY,
				HIT_ARROW, ATK_SPIRITBOW, ATK_CROSSBOW, HEALTH_WARN, HEALTH_CRITICAL,

				DESCEND, EAT, READ, LULLABY, DRINK, SHATTER, ZAP, LIGHTNING, LEVELUP, DEATH,
				CHALLENGE, CURSED, TRAP, EVOKE, TOMB, ALERT, MELD, BOSS, BLAST, PLANT, RAY, BEACON,
				TELEPORT, CHARMS, MASTERY, PUFF, ROCKS, BURNING, FALLING, GHOST, SECRET, BONES,
				BEE, DEGRADE, MIMIC, DEBUFF, CHARGEUP, GAS, CHAINS, SCAN, SHEEP, MINE, BALLISTA_RELOAD
		};
	}

	public static class Splashes {
		public static final String WARRIOR  = "splashes/warrior.jpg";
		public static final String MAGE     = "splashes/mage.jpg";
		public static final String ROGUE    = "splashes/rogue.jpg";
		public static final String HUNTRESS = "splashes/huntress.jpg";
		public static final String DUELIST  = "splashes/duelist.jpg";
		public static final String CLERIC   = "splashes/cleric.jpg";

		public static final String SEWERS   = "splashes/sewers.jpg";
		public static final String PRISON   = "splashes/prison.jpg";
		public static final String CAVES    = "splashes/caves.jpg";
		public static final String CITY     = "splashes/city.jpg";
		public static final String HALLS    = "splashes/halls.jpg";

		public static class Title {
			public static final String ARCHS         = "splashes/title/archs.png";
			public static final String BACK_CLUSTERS = "splashes/title/back_clusters.png";
			public static final String MID_MIXED     = "splashes/title/mid_mixed.png";
			public static final String FRONT_SMALL   = "splashes/title/front_small.png";
		}
	}

	public static class Sprites {
		public static final String HOOKS = "sprites/experienced/hooks.png"; //ported, experienced
		public static final String WATERLARVA = "sprites/cursed/snowslug.png"; //ported, cursed
		public static final String WATERELEMENTAL = "sprites/cursed/waterelemental.png"; //ported, cursed
		public static final String WATERCHALLENGEGOO = "sprites/cursed/waterchallengegoo.png"; //ported, cursed
		public static final String SONICBAT = "sprites/cursed/sonic_bat.png"; //ported, cursed
		public static final String SHINOBI = "sprites/cursed/assassin.png"; //ported, cursed
		public static final String MAGECRAB = "sprites/cursed/Magic_Crab.png"; //ported, cursed
		public static final String KUPUA = "sprites/cursed/kupua.png"; //ported, cursed
		public static final String FOSSILSKELETON = "sprites/cursed/fossil_skeleton.png"; //ported, cursed
		public static final String ADULTELEMENTAL = "sprites/cursed/elemental_adult.png"; //ported, cursed
		public static final String NECRO2 = "sprites/matt/necromancer2.png"; //ported, matt
		public static final String ICESNAKE = "sprites/matt/icesnake.png"; //ported, matt
		public static final String KOBOLD = "sprites/reforged/kobold.png"; //ported, reforged
		public static final String SPITTER = "sprites/reforged/spitter.png"; //ported, reforged
		public static final String BALLISTA = "sprites/reforged/ballista.png"; //ported, reforged
		public static final String FIEND = "sprites/reforged/fiend.png"; //ported, reforged
		public static final String UNHOLY_PRIEST = "sprites/reforged/unholy_priest.png"; //ported, reforged
		public static final String RAT_USURPER = "sprites/reforged/rat_usurper.png"; //ported, reforged
		public static final String DEMON_GOO = "sprites/reforged/demon_goo.png"; //ported, reforged
		public static final String ICEGOLEM = "sprites/matt/ice_golem.png"; //ported, matt
		public static final String FROZENSWARM = "sprites/matt/frozenswarm.png"; //ported, matt
		public static final String GNOLL2 = "sprites/matt/gnoll2.png"; //ported, matt
		public static final String DM166 = "sprites/matt/dm166.png"; //ported, matt
		public static final String DM151 = "sprites/matt/dm151.png"; //ported, matt
		public static final String DM100F = "sprites/matt/dm100f.png"; //ported, matt
		public static final String DARKKING = "sprites/matt/king_dark.png"; //ported, matt
		public static final String THIEF2 = "sprites/matt/thief2.png"; //ported, matt
		public static final String COALELEMENTAL = "sprites/devoted/coal_elemental.png"; //ported, devoted
		public static final String VELTALETH = "sprites/devoted/Vel-Taleth.png"; //ported, devoted
		public static final String VAULT = "sprites/devoted/vault.png"; //ported, devoted
		public static final String TROLLSPEARMAN = "sprites/devoted/troll_spearman.png"; //ported, devoted
		public static final String TROLL_KNIGHT = "sprites/devoted/troll_knight.png"; //ported, devoted
		public static final String COURT = "sprites/devoted/council.png"; //ported, devoted
		public static final String SAGE = "sprites/devoted/sagecorpse.png"; //ported, devoted
		public static final String RATBEAST = "sprites/devoted/ratbeast.png"; //ported, devoted
		public static final String QUAKE_WOLF = "sprites/devoted/QuakeWolf.png"; //ported, devoted
		public static final String PYRONAUT = "sprites/devoted/pyronaut.png"; //ported, devoted
		public static final String NEURON = "sprites/devoted/Neuron.png"; //ported, devoted
		public static final String NEURON_SENTRY = "sprites/devoted/neuron_sentry.png"; //ported, devoted
		public static final String LAVA_PIRANHA = "sprites/devoted/lava_piranha.png"; //ported, devoted
		public static final String FURNACEGOLEM = "sprites/devoted/furnace_golem.png"; //ported, devoted
		public static final String FROGEAT = "sprites/devoted/Frogeat.png"; //ported, devoted
		public static final String ESSENCE = "sprites/devoted/Vel-Essence.png"; //ported, devoted
		public static final String EMPEROR = "sprites/devoted/emperor.png"; //ported, devoted
		public static final String EMPERORCRYSTAL = "sprites/devoted/emperorcrystal.png"; //ported, devoted
		public static final String CRAG_WOLF = "sprites/devoted/CragWolf.png"; //ported, devoted
		public static final String BRAWLER = "sprites/devoted/troll_brawler.png"; //ported, devoted
		public static final String BRAIN = "sprites/devoted/eldritchbrain.png"; //ported, devoted
		public static final String BOUNTYHUNTER = "sprites/devoted/BountyHunter.png"; //ported, devoted
		public static final String BLASTFURNACE = "sprites/devoted/furnace.png"; //ported, devoted
		public static final String BLACKHOLE = "sprites/devoted/BlackHole.png"; //ported, devoted
		public static final String ITEMS        = "sprites/items.png";
		public static final String ITEM_ICONS   = "sprites/item_icons.png";

		public static final String WARRIOR  = "sprites/warrior.png";
		public static final String MAGE     = "sprites/mage.png";
		public static final String ROGUE    = "sprites/rogue.png";
		public static final String HUNTRESS = "sprites/huntress.png";
		public static final String DUELIST  = "sprites/duelist.png";
		public static final String CLERIC   = "sprites/cleric.png";
		public static final String AVATARS  = "sprites/avatars.png";
		public static final String PET      = "sprites/pet.png";
		public static final String AMULET   = "sprites/amulet.png";

		public static final String RAT      = "sprites/rat.png";
		//Devoted Pixel Dungeon monsters
		public static final String TOAD = "sprites/devoted/toad.png";
		public static final String BUTTERFLY = "sprites/devoted/butterfly.png";
		public static final String GRUB = "sprites/devoted/grub.png";
		public static final String THORNLASHER = "sprites/devoted/thornlasher.png";
		public static final String DRUID = "sprites/devoted/druid.png";
		public static final String COLDCORPSE = "sprites/devoted/coldcorpse.png";
		public static final String MEATRACK = "sprites/devoted/meatrack.png";
		public static final String GLASS_KNIGHT = "sprites/devoted/glass_knight.png";
		public static final String WERERAT = "sprites/devoted/wererat.png";
		public static final String BANSHEE = "sprites/devoted/banshee.png";
		public static final String GRANITETROLL = "sprites/devoted/granite_troll.png";
		public static final String TROLLRANGER = "sprites/devoted/troll_ranger.png";
		public static final String ROCKGOLEM = "sprites/devoted/rock_golem.png";
		public static final String MOLE = "sprites/devoted/subterranian_mole.png";
		public static final String CULTIST = "sprites/devoted/cultist.png";
		public static final String LEADER = "sprites/devoted/leader.png";
		public static final String TRAPPER = "sprites/devoted/trapper.png";
		public static final String WARDEN = "sprites/devoted/alchemist.png";
		public static final String GIANT = "sprites/devoted/giant.png";
		public static final String PUPPET = "sprites/devoted/puppet.png";
		public static final String BEDROCKGOLEM = "sprites/devoted/bedrock_golem.png";
		public static final String SACRIFICE = "sprites/devoted/sacrifice.png";

		public static final String BRUTE    = "sprites/brute.png";
		public static final String SPINNER  = "sprites/spinner.png";
		public static final String DM300    = "sprites/dm300.png";
		public static final String WRAITH   = "sprites/wraith.png";
		public static final String UNDEAD   = "sprites/undead.png";
		public static final String KING     = "sprites/king.png";
		public static final String PIRANHA  = "sprites/piranha.png";
		public static final String EYE      = "sprites/eye.png";
		public static final String GNOLL    = "sprites/gnoll.png";
		public static final String CRAB     = "sprites/crab.png";
		public static final String GOO      = "sprites/goo.png";
		public static final String SWARM    = "sprites/swarm.png";
		public static final String SKELETON = "sprites/skeleton.png";
		public static final String SHAMAN   = "sprites/shaman.png";
		public static final String THIEF    = "sprites/thief.png";
		public static final String TENGU    = "sprites/tengu.png";
		public static final String SHEEP    = "sprites/sheep.png";
		public static final String KEEPER   = "sprites/shopkeeper.png";
		public static final String BAT      = "sprites/bat.png";
		public static final String ELEMENTAL= "sprites/elemental.png";
		public static final String MONK     = "sprites/monk.png";
		public static final String WARLOCK  = "sprites/warlock.png";
		public static final String GOLEM    = "sprites/golem.png";
		public static final String STATUE   = "sprites/statue.png";
		public static final String SUCCUBUS = "sprites/succubus.png";
		public static final String SCORPIO  = "sprites/scorpio.png";
		public static final String FISTS    = "sprites/yog_fists.png";
		public static final String YOG      = "sprites/yog.png";
		public static final String LARVA    = "sprites/larva.png";
		public static final String GHOST    = "sprites/ghost.png";
		public static final String MAKER    = "sprites/wandmaker.png";
		public static final String TROLL    = "sprites/blacksmith.png";
		public static final String IMP      = "sprites/imp.png";
		public static final String RATKING  = "sprites/ratking.png";
		public static final String BEE      = "sprites/bee.png";
		public static final String MIMIC    = "sprites/mimic.png";
		public static final String ROT_LASH = "sprites/rot_lasher.png";
		public static final String ROT_HEART= "sprites/rot_heart.png";
		public static final String GUARD    = "sprites/guard.png";
		public static final String WARDS    = "sprites/wards.png";
		public static final String GUARDIAN = "sprites/guardian.png";
		public static final String SLIME    = "sprites/slime.png";
		public static final String SNAKE    = "sprites/snake.png";
		public static final String NECRO    = "sprites/necromancer.png";
		public static final String GHOUL    = "sprites/ghoul.png";
		public static final String RIPPER   = "sprites/ripper.png";
		public static final String SPAWNER  = "sprites/spawner.png";
		public static final String DM100    = "sprites/dm100.png";
		public static final String PYLON    = "sprites/pylon.png";
		public static final String DM200    = "sprites/dm200.png";
		public static final String LOTUS    = "sprites/lotus.png";
		public static final String NINJA_LOG        = "sprites/ninja_log.png";
		public static final String SPIRIT_HAWK      = "sprites/spirit_hawk.png";
		public static final String SENTRY           = "sprites/sentry.png";
		public static final String CRYSTAL_WISP     = "sprites/crystal_wisp.png";
		public static final String CRYSTAL_GUARDIAN = "sprites/crystal_guardian.png";
		public static final String CRYSTAL_SPIRE    = "sprites/crystal_spire.png";
		public static final String GNOLL_GUARD      = "sprites/gnoll_guard.png";
		public static final String GNOLL_SAPPER     = "sprites/gnoll_sapper.png";
		public static final String GNOLL_GEOMANCER  = "sprites/gnoll_geomancer.png";
		public static final String FUNGAL_SPINNER   = "sprites/fungal_spinner.png";
		public static final String FUNGAL_SENTRY    = "sprites/fungal_sentry.png";
		public static final String FUNGAL_CORE      = "sprites/fungal_core.png";
		public static final String VAULT_TOKENS_DOOR= "sprites/vault_tokens_door.png";
		public static final String VAULT_MIRROR     = "sprites/vault_mirror.png";
		public static final String VAULT_BOSS_ELEMENTAL= "sprites/vault_boss_elemental.png";
	}
}
