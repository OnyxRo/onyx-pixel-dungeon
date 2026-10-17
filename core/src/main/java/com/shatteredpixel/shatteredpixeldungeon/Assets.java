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
		public static final String SEWER_SP = "environment/custom_tiles/ryuse/sewers_special.png"; //ported, ryuse
		public static final String EYE_OF_WISDOM_TILE = "environment/custom_tiles/ryuse/eye_of_wisdom_tiles.png"; //ported, ryuse
		public static final String CAPYNEST_SP = "environment/custom_tiles/shittened/capybaranest.png"; //ported, shittened
		public static final String TILES_ICE2 = "environment/mattedition/tiles_ice_snow.png"; //ported, mattedition
		public static final String TILES_THIEF_GUILD = "environment/matt/tiles_guild.png"; //ported, matt
		public static final String PRISON_EXIT_OLD = "environment/custom_tiles/abyssalnite/prison_exit_old.png"; //ported, abyssalnite
		public static final String WATER_ARENA = "environment/experienced/water10.png"; //ported, experienced
		public static final String TILES_ARENA = "environment/experienced/tiles_arena.png"; //ported, experienced
		public static final String PRISON_EXIT_NEW = "environment/custom_tiles/abyssalnite/prison_exit_new.png"; //ported, abyssalnite
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
		public static final String BACKS1_2 = "music/transformation/chuboss.ogg"; //ported, transformation
		public static final String BACKS1_1 = "music/transformati/combat_area.ogg"; //ported, transformati
		public static final String BOSS_LOOP = "music/tower/track_boss_loop.ogg"; //ported, tower
		public static final String DESCENT = "music/tower/descent.ogg"; //ported, tower
		public static final String METROPOLIS = "music/tower/metropolis_track.ogg"; //ported, tower
		public static final String RAT_PUNCHER_SPEDUP = "music/tower/rat_puncher.ogg"; //ported, tower
		public static final String BOSS_TRIAL = "music/tower/boss_trial.ogg"; //ported, tower
		public static final String GOBLIN_GROTTO = "music/tower/goblin_grotto.ogg"; //ported, tower
		public static final String GNOLL_GROTTO = "music/tower/gnoll_grotto.ogg"; //ported, tower
		public static final String CITY_BOSS_SPEDUP = "music/tower/city_boss_spedup.ogg"; //ported, tower
		public static final String TWINGO_TANGO = "music/player/shittened/twingo_tango.ogg"; //ported, shittened
		public static final String TWINGO_TURBO = "music/player/shittened/twingo_turbo.ogg"; //ported, shittened
		public static final String TWIGIGIGI = "music/player/shittened/twigigigi.ogg"; //ported, shittened
		public static final String CANTO_TWINGO = "music/player/shittened/canto_twingo.ogg"; //ported, shittened
		public static final String ON_THE_STREETS = "music/player/shittened/on_the_streets.ogg"; //ported, shittened
		public static final String WESTERNESS = "music/player/shittened/westerness.ogg"; //ported, shittened
		public static final String ADVENTURE = "music/player/shittened/adventure.ogg"; //ported, shittened
		public static final String SUNSET = "music/player/shittened/sunset.ogg"; //ported, shittened
		public static final String HIGH_TIDE = "music/player/shittened/high_tide.ogg"; //ported, shittened
		public static final String SAILING_DREAM = "music/player/shittened/sailing_dream.ogg"; //ported, shittened
		public static final String STORMY_ROCK = "music/player/shittened/stormy_rock.ogg"; //ported, shittened
		public static final String REMIX_CITY_FINALE = "music/player/shittened/remix_city_finale.ogg"; //ported, shittened
		public static final String REMIX_PRISON_BOSS = "music/player/shittened/remix_prison_boss.ogg"; //ported, shittened
		public static final String REMIX_SEWER_BOSS = "music/player/shittened/remix_sewer_boss.ogg"; //ported, shittened
		public static final String REMIX_SEWER = "music/player/shittened/remix_sewer.ogg"; //ported, shittened
		public static final String THEME_3 = "music/shittened/theme_3.ogg"; //ported, shittened
		public static final String RAT_NOISES = "music/player/shittened/rat_noises.ogg"; //ported, shittened
		public static final String RATTYRAT = "music/player/shittened/rattyrat.ogg"; //ported, shittened
		public static final String RATRATRAT = "music/player/shittened/ratratrat.ogg"; //ported, shittened
		public static final String DANCE_STARS = "music/player/shittened/dance_stars.ogg"; //ported, shittened
		public static final String WOOOOOOH = "music/player/shittened/wooooooh.ogg"; //ported, shittened
		public static final String SHART_DAY = "music/player/shittened/shart_day.ogg"; //ported, shittened
		public static final String ECHOES = "music/player/shittened/echoes.ogg"; //ported, shittened
		public static final String UOAUOA = "music/player/shittened/uoauoa.ogg"; //ported, shittened
		public static final String PIPIKUKU = "music/player/shittened/pipikuku.ogg"; //ported, shittened
		public static final String WOWOWOWO = "music/player/shittened/wowowowo.ogg"; //ported, shittened
		public static final String AFWFSW = "music/player/shittened/afwfsw.ogg"; //ported, shittened
		public static final String RIDE_THE_HOG = "music/player/shittened/ride_the_hog.ogg"; //ported, shittened
		public static final String HOG_PHONK = "music/player/shittened/hog_phonk.ogg"; //ported, shittened
		public static final String HOG_EPIC = "music/player/shittened/hog_epic.ogg"; //ported, shittened
		public static final String MOKKITIE = "music/player/shittened/mokkitie.ogg"; //ported, shittened
		public static final String MISSA_TWINGO = "music/player/shittened/missa_twingo.ogg"; //ported, shittened
		public static final String YSILUOKKALAISET = "music/player/shittened/ysiluokkalaiset.ogg"; //ported, shittened
		public static final String STOOL_GONE = "music/player/shittened/stool_gone.ogg"; //ported, shittened
		public static final String MY_TABLE = "music/player/shittened/my_table.ogg"; //ported, shittened
		public static final String CHAIR = "music/player/shittened/chair.ogg"; //ported, shittened
		public static final String FLOW = "music/player/shittened/flow.ogg"; //ported, shittened
		public static final String REAWAKENING = "music/player/shittened/reawakening.ogg"; //ported, shittened
		public static final String FUJI = "music/player/shittened/fuji.ogg"; //ported, shittened
		public static final String DESTINATION = "music/player/shittened/destination.ogg"; //ported, shittened
		public static final String TALE = "music/player/shittened/fantasy_tale.ogg"; //ported, shittened
		public static final String RYUSE_GAME = "music/ryuse/game.ogg"; //ported, ryuse
		public static final String RYUSE_THEME = "music/ryuse/theme.ogg"; //ported, ryuse
		public static final String TOWER_GOBLIN_GROTTO = "music/tower/goblin_grotto.ogg"; //ported, tower
		public static final String TOWER_GNOLL_GROTTO = "music/tower/gnoll_grotto.ogg"; //ported, tower
		public static final String TOWER_METROPOLIS = "music/tower/metropolis_track.ogg"; //ported, tower
		public static final String RYUSE_SURFACE = "music/ryuse/surface.ogg"; //ported, ryuse
		public static final String ICE_CAVES = "music/mattedition/icecaves_calm.ogg"; //ported, mattedition
		public static final String ICE_CAVES_TENSE = "music/mattedition/icecaves.ogg"; //ported, mattedition
		public static final String BLACK_MIMIC_ARENA = "music/experienced/black_mimic_arena.ogg"; //ported, experienced
		public static final String BLACK_MIMIC_BOSS = "music/experienced/black_mimic.ogg"; //ported, experienced
		public static final String BLACK_MIMIC_BOSS_FINALE = "music/experienced/black_mimic_finale.ogg"; //ported, experienced
		public static final String ARENA = "music/experienced/arena.ogg"; //ported, experienced
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
		public static final String DOGWOOF = "sounds/magicling/puppy_bark.ogg"; //ported, magicling
		public static final String VIRTUE = "sounds/transformati/virtue.mp3"; //ported, transformati
		public static final String SUFFER = "sounds/transformati/suffer.mp3"; //ported, transformati
		public static final String WIN_SMALL = "sounds/shittened/win_small.mp3"; //ported, shittened
		public static final String WIN_MEDIUM = "sounds/shittened/win_medium.mp3"; //ported, shittened
		public static final String WIN_LARGE = "sounds/shittened/win_large.mp3"; //ported, shittened
		public static final String SLOT_TICK = "sounds/shittened/slot_tick.mp3"; //ported, shittened
		public static final String HORN = "sounds/shittened/horn.mp3"; //ported, shittened
		public static final String DEATH_11 = "sounds/shittened/death_11.mp3"; //ported, shittened
		public static final String STAIRS_1 = "sounds/movement/shittened/stairs_1.mp3"; //ported, shittened
		public static final String HARVEST = "sounds/shittened/harvest.mp3"; //ported, shittened
		public static final String GUN = "sounds/shittened/gun.mp3"; //ported, shittened
		public static final String BAT = "sounds/shittened/bat.mp3"; //ported, shittened
		public static final String EGG = "sounds/food/shittened/egg.mp3"; //ported, shittened
		public static final String CAPYBARA = "sounds/shittened/capybara.mp3"; //ported, shittened
		public static final String NOTE = "sounds/shittened/note.mp3"; //ported, shittened
		public static final String HIT_GLANCE = "sounds/shittened/hit_glance.mp3"; //ported, shittened
		public static final String HIT_PILE = "sounds/shittened/hit_pile.mp3"; //ported, shittened
		public static final String STICK_SNAP = "sounds/equipment/shittened/stick_snap.mp3"; //ported, shittened
		public static final String UPGRAGE_ARMOR = "sounds/equipment/shittened/upgrade_armor.mp3"; //ported, shittened
		public static final String SPLASH = "sounds/movement/shittened/splash.mp3"; //ported, shittened
		public static final String SPRAY = "sounds/shittened/spray.mp3"; //ported, shittened
		public static final String VINEBOOM = "sounds/shittened/vineboom.mp3"; //ported, shittened
		public static final String FLOUR = "sounds/food/shittened/flour.mp3"; //ported, shittened
		public static final String EGG_CRACK_2 = "sounds/food/shittened/egg_crack_2.mp3"; //ported, shittened
		public static final String EGG_CRACK = "sounds/food/shittened/egg_crack.mp3"; //ported, shittened
		public static final String CD_EJECT = "sounds/shittened/cd_eject.mp3"; //ported, shittened
		public static final String CD_INSERT = "sounds/shittened/cd_insert.mp3"; //ported, shittened
		public static final String PUDDING = "sounds/abyssalnite/pudding.mp3"; //ported, abyssalnite
		public static final String CURE = "sounds/abyssalnite/cureinfuse.mp3"; //ported, abyssalnite
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
		public static final String TURD     = "sounds/turd.mp3"; //ported, shittened
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
				BEE, DEGRADE, MIMIC, DEBUFF, CHARGEUP, GAS, TURD, CHAINS, SCAN, SHEEP, MINE, BALLISTA_RELOAD
		};
	}

	public static class Splashes {
		public static final String WARRIOR  = "splashes/warrior.jpg";
		public static final String MAGE     = "splashes/mage.jpg";
		public static final String ROGUE    = "splashes/rogue.jpg";
		public static final String HUNTRESS = "splashes/huntress.jpg";
		public static final String DUELIST  = "splashes/duelist.jpg";
		public static final String CLERIC   = "splashes/cleric.jpg";
		public static final String ZEALOT   = "splashes/zealot.png"; //ported, devoted
		public static final String ARTIFICER = "splashes/artificer.jpg"; //ported, reforged
		public static final String SUMMONER = "splashes/summoner.jpg"; //ported, ryuse
		public static final String SORCERESS = "splashes/sorceress.png"; //ported, ryuse, the Summoner's other picture

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
		public static final String WARRIORBOSS = "sprites/transformation/warriorboss.png"; //ported, transformation
		public static final String MYRIAD_BLACK_SHADOW_OVERLAY = "sprites/transformation/myriad_black_shadow_overlay.png"; //ported, transformation
		public static final String SUCCBOSS = "sprites/transformation/succboss.png"; //ported, transformation
		public static final String SCORPIOBOSS = "sprites/transformation/scorpioboss.png"; //ported, transformation
		public static final String ROGUEBOSS = "sprites/transformation/rogueboss.png"; //ported, transformation
		public static final String RIPPERBOSS = "sprites/transformation/ripperboss.png"; //ported, transformation
		public static final String MONKMASTER = "sprites/transformation/monkmaster.png"; //ported, transformation
		public static final String HUNTRESSBOSS = "sprites/transformation/huntressboss.png"; //ported, transformation
		public static final String GREATSHOPER = "sprites/transformation/greatshoper.png"; //ported, transformation
		public static final String GREATDEMON = "sprites/transformation/greatdemon.png"; //ported, transformation
		public static final String GOLDGOLEM = "sprites/transformation/goldgolem.png"; //ported, transformation
		public static final String GOLDELEMETAL = "sprites/transformation/goldelemental.png"; //ported, transformation
		public static final String EYEBOSS = "sprites/transformation/eyeboss.png"; //ported, transformation
		public static final String DIEDTENGU = "sprites/transformation/diedtengu.png"; //ported, transformation
		public static final String DEATH_BUTTERFLY = "sprites/transformation/death_butterfly.png"; //ported, transformation
		public static final String DARK_MECHANICAL_FIST = "sprites/transformation/dark_mechanical_fist.png"; //ported, transformation
		public static final String CORPSE = "sprites/transformation/corpse.png"; //ported, transformation
		public static final String ELF_WINE_CUP = "sprites/transformation/elf_wine_cup.png"; //ported, transformation
		public static final String SLIMEMUCUS = "sprites/transformati/slimemucus.png"; //ported, transformati
		public static final String SLIMEMUCUS4 = "sprites/transformati/slimemucus4.png"; //ported, transformati
		public static final String SLIMEMUCUS3 = "sprites/transformati/slimemucus3.png"; //ported, transformati
		public static final String SLIMEMUCUS2 = "sprites/transformati/slimemucus2.png"; //ported, transformati
		public static final String SLIMEMUCUS1 = "sprites/transformati/slimemucus1.png"; //ported, transformati
		public static final String XENOMORPH = "sprites/spacebase/xenomorph.png"; //ported, spacebase
		public static final String WAR_MACHINE = "sprites/spacebase/war_machine.png"; //ported, spacebase
		public static final String TURRET = "sprites/spacebase/turret.png"; //ported, spacebase
		public static final String SIPHON_DRONE = "sprites/spacebase/siphon_drone.png"; //ported, spacebase
		public static final String SIGNAL_SIREN = "sprites/spacebase/signal_siren.png"; //ported, spacebase
		public static final String SIGNAL_LEECH = "sprites/spacebase/signal_leech.png"; //ported, spacebase
		public static final String RUPTURED_CREW_SUIT = "sprites/spacebase/ruptured_crew_suit.png"; //ported, spacebase
		public static final String REPLICATOR_SWARM = "sprites/spacebase/replicator_swarm.png"; //ported, spacebase
		public static final String OUTER_COLONY_SHOCK_TROOPER = "sprites/spacebase/outer_colony_shock_trooper.png"; //ported, spacebase
		public static final String OUTER_COLONY_SCOUT = "sprites/spacebase/outer_colony_scout.png"; //ported, spacebase
		public static final String OUTER_COLONY_PSION = "sprites/spacebase/outer_colony_psion.png"; //ported, spacebase
		public static final String OLDWARBOT = "sprites/spacebase/old_war_bot.png"; //ported, spacebase
		public static final String MAINTENANCE_CRAWLER = "sprites/spacebase/maintenance_crawler.png"; //ported, spacebase
		public static final String JEDA_KNIGHT = "sprites/spacebase/jeda_knight.png"; //ported, spacebase
		public static final String JAWAR = "sprites/spacebase/jawar.png"; //ported, spacebase
		public static final String HOLODECK_MONARCH = "sprites/spacebase/holodeck_monarch.png"; //ported, spacebase
		public static final String HOLODECK_LEGIONARY = "sprites/spacebase/holodeck_legionary.png"; //ported, spacebase
		public static final String FACEHUGGER = "sprites/spacebase/facehugger.png"; //ported, spacebase
		public static final String DRONE = "sprites/spacebase/drone.png"; //ported, spacebase
		public static final String CONFUSEDSHAPESHIFTER = "sprites/spacebase/confused_shapeshifter.png"; //ported, spacebase
		public static final String BITH_ACOLYTE = "sprites/spacebase/bith_acolyte.png"; //ported, spacebase
		public static final String FIX = "sprites/tower/fix.png"; //ported, tower
		public static final String NECROHERO = "sprites/tower/necrohero.png"; //ported, tower
		public static final String TANK = "sprites/tower/tank.png"; //ported, tower
		public static final String PRIEST = "sprites/tower/priest.png"; //ported, tower
		public static final String BOSSRATKING = "sprites/tower/bossratking.png"; //ported, tower
		public static final String RATKINGAVATAR = "sprites/tower/ratkingavatar.png"; //ported, tower
		public static final String BOSSOOZE = "sprites/tower/bossooze.png"; //ported, tower
		public static final String BOSSTROLL = "sprites/tower/bosstroll.png"; //ported, tower
		public static final String DRILL = "sprites/tower/drill.png"; //ported, tower
		public static final String DRILLBIG = "sprites/tower/drill_big.png"; //ported, tower
		public static final String GNOLLSHOPKEEPER = "sprites/tower/gnollshopkeeper.png"; //ported, tower
		public static final String IT = "sprites/tower/it.png"; //ported, tower
		public static final String SHARD = "sprites/tower/shard.png"; //ported, tower
		public static final String MINER = "sprites/tower/miner.png"; //ported, tower
		public static final String SIGN = "sprites/tower/sign.png"; //ported, tower
		public static final String SKULLS = "sprites/tower/skulls.png"; //ported, tower
		public static final String TARGET = "sprites/tower/target.png"; //ported, tower
		public static final String LICTERIA = "sprites/tower/licteriaflower.png"; //ported, tower
		public static final String SHINOBIFACELESS = "sprites/tower/shinobifaceless.png"; //ported, tower
		public static final String SHINOBINORMAL = "sprites/tower/shinobinormal.png"; //ported, tower
		public static final String SHINOBISMILING = "sprites/tower/shinobismiling.png"; //ported, tower
		public static final String SHINOBIKNIGHT = "sprites/tower/shinobiknight.png"; //ported, tower
		public static final String EMPTY = "sprites/tower/empty.png"; //ported, tower
		public static final String BOSSNECRO = "sprites/tower/bossnecromancer.png"; //ported, tower
		public static final String PORTAL = "sprites/tower/portal.png"; //ported, tower
		public static final String PORTALENEMY = "sprites/tower/portalenemy.png"; //ported, tower
		public static final String PORTALBOSSENEMY = "sprites/tower/portalbossenemy.png"; //ported, tower
		public static final String PORTALUNSTABLE = "sprites/tower/portalunstable.png"; //ported, tower
		public static final String RED_SENTRY = "sprites/tower/red_sentry.png"; //ported, tower
		public static final String SPINNINGAMULET = "sprites/tower/spinningamulet.png"; //ported, tower
		public static final String TOWERCROSSBOW = "sprites/tower/towercrossbow.png"; //ported, tower
		public static final String TOWERWALL = "sprites/tower/towerwall.png"; //ported, tower
		public static final String TOWERWALLRUNIC = "sprites/tower/towerwallrunic.png"; //ported, tower
		public static final String TOWERWALLSPIKED = "sprites/tower/towerwallspiked.png"; //ported, tower
		public static final String TOWERWAND = "sprites/tower/towerwand.png"; //ported, tower
		public static final String TOWERGRAVE = "sprites/tower/towergrave.png"; //ported, tower
		public static final String TOWERCANNON = "sprites/tower/towercannon.png"; //ported, tower
		public static final String ROCKET = "sprites/tower/rocket.png"; //ported, tower
		public static final String TOWERTOTEM = "sprites/tower/towertotem.png"; //ported, tower
		public static final String TOWERGUARD = "sprites/tower/towerguard.png"; //ported, tower
		public static final String TOWERGUARDSPEARMAN = "sprites/tower/towerguardspearman.png"; //ported, tower
		public static final String TOWERLIGHTNING = "sprites/tower/towerlightning.png"; //ported, tower
		public static final String TOWERDISINTEGRATION = "sprites/tower/towerdisintegration.png"; //ported, tower
		public static final String TOWERDARTGUN = "sprites/tower/towerdartgun.png"; //ported, tower
		public static final String TOWERTNTLOG = "sprites/tower/towertntlog.png"; //ported, tower
		public static final String TOWERRATCAMP = "sprites/tower/towercamptent.png"; //ported, tower
		public static final String OBSERVER = "sprites/tower/observer.png"; //ported, tower
		public static final String ROSEBUSH = "sprites/tower/rosebush.png"; //ported, tower
		public static final String OBELISK_BLOODSTONE = "sprites/tower/obeliskbloodstone.png"; //ported, tower
		public static final String OBELISK_NECROTIC = "sprites/tower/obelisknecrotic.png"; //ported, tower
		public static final String OBELISK_PERMAFROST = "sprites/tower/obeliskpermafrost.png"; //ported, tower
		public static final String ICE_WALL = "sprites/tower/icewall.png"; //ported, tower
		public static final String UNSPEAKABLEHORROR = "sprites/tower/unspeakablehorror.png"; //ported, tower
		public static final String FLAGENEMY = "sprites/tower/enemyflag.png"; //ported, tower
		public static final String FLAGFRIEND = "sprites/tower/friendflag.png"; //ported, tower
		public static final String BLOODFOUNTAIN = "sprites/tower/bloodfountain.png"; //ported, tower
		public static final String SOKOBANSHEEP = "sprites/sps/sokobansheep.png"; //ported, sps
		public static final String ZOMBIE = "sprites/sps/zombie.png"; //ported, sps
		public static final String SHIT = "sprites/sps/shit.png"; //ported, sps
		public static final String MOB_BAMBOO = "sprites/sps/bamboo.png"; //ported, sps
		public static final String ICEBUG = "sprites/sps/icebug.png"; //ported, sps
		public static final String FIRERABBIT = "sprites/sps/firerabbit.png"; //ported, sps
		public static final String DEMON_RABBIT = "sprites/sps/demonrabbit.png"; //ported, sps
		public static final String WORM_WHYHUMAN = "sprites/tumulus/magicling/worm_whyhuman.png"; //ported, magicling
		public static final String WORM = "sprites/tumulus/magicling/tomb_worm.png"; //ported, magicling
		public static final String WARLOCKHEAD = "sprites/magicling/warlock_chief.png"; //ported, magicling
		public static final String VERYCOLDRAT = "sprites/magicling/rat_ice.png"; //ported, magicling
		public static final String TPDP = "sprites/boss/magicling/TPDoor.png"; //ported, magicling
		public static final String THIEF_SOUL = "sprites/tumulus/magicling/tomb_robber.png"; //ported, magicling
		public static final String TELE_FOCU = "sprites/hollow/minigame/magicling/teleportation_focu.png"; //ported, magicling
		public static final String SUCCUBUS_QUEEN = "sprites/magicling/succubus_queen.png"; //ported, magicling
		public static final String SMSLIGHT = "sprites/pets/magicling/smlt.png"; //ported, magicling
		public static final String SMACERS = "sprites/pets/magicling/cerberus_jr.png"; //ported, magicling
		public static final String SKFSBABY = "sprites/pets/magicling/sakababy.png"; //ported, magicling
		public static final String SHARD = "sprites/tumulus/magicling/shard.png"; //ported, magicling
		public static final String SAESD = "sprites/magicling/bluedragon.png"; //ported, magicling
		public static final String REDWRAITH = "sprites/magicling/bluewraith.png"; //ported, magicling
		public static final String NECRO_WARLOCK = "sprites/tumulus/magicling/necro_warlock.png"; //ported, magicling
		public static final String NECRO_SCOUT = "sprites/tumulus/magicling/necro_scout.png"; //ported, magicling
		public static final String NECRO_PIONEER = "sprites/tumulus/magicling/nec_striker.png"; //ported, magicling
		public static final String NECRO_GUARD = "sprites/tumulus/magicling/necro_soldier.png"; //ported, magicling
		public static final String NECRO_CAVALRY = "sprites/tumulus/magicling/necro_cavalry.png"; //ported, magicling
		public static final String NECRO_ARCHER = "sprites/tumulus/magicling/necro_arbalester.png"; //ported, magicling
		public static final String NECRORED = "sprites/magicling/rednecromancer_ex.png"; //ported, magicling
		public static final String KATID = "sprites/magicling/venom_flower.png"; //ported, magicling
		public static final String IFE_SCORPIO = "sprites/magicling/ife_scriopi.png"; //ported, magicling
		public static final String HEROGNOLL = "sprites/magicling/tribesman.png"; //ported, magicling
		public static final String GUARD_CAPITAL = "sprites/magicling/guard_captain.png"; //ported, magicling
		public static final String GREEN_KING = "sprites/magicling/greenslting_king.png"; //ported, magicling
		public static final String GREEN = "sprites/magicling/greenslting.png"; //ported, magicling
		public static final String GRAVERAT = "sprites/tumulus/magicling/rat_graveyard.png"; //ported, magicling
		public static final String GIANT_FLOWER_SLIME = "sprites/magicling/flowers_slime_giant.png"; //ported, magicling
		public static final String GHOUL_HILL = "sprites/magicling/ghoul_hill.png"; //ported, magicling
		public static final String FSOE = "sprites/boss/magicling/four_stone.png"; //ported, magicling
		public static final String FLOWER_SLIME = "sprites/magicling/flowers_slime.png"; //ported, magicling
		public static final String DXL = "sprites/boss/magicling/dwarf_soldier.png"; //ported, magicling
		public static final String DM111 = "sprites/magicling/dm111.png"; //ported, magicling
		public static final String DIED = "sprites/items/magicling/died.png"; //ported, magicling
		public static final String DEADEYE = "sprites/magicling/eye_doom.png"; //ported, magicling
		public static final String CRUMB = "sprites/hollow/magicling/crumb.png"; //ported, magicling
		public static final String CRID = "sprites/boss/magicling/ColdGuard.png"; //ported, magicling
		public static final String AGGREATUS = "sprites/tumulus/magicling/aggregatus_of_grudge.png"; //ported, magicling
		public static final String HEROGNOLLOLD = "sprites/gold/magicling/tribesman_old.png"; //ported, magicling
		public static final String BATEX = "sprites/magicling/BrownBat.png"; //ported, magicling
		public static final String NEWBORN_CRAB = "sprites/magicling/crab_newborn.png"; //ported, magicling
		public static final String SKULLS = "sprites/gold/magicling/skulls.png"; //ported, magicling
		public static final String ROYAL_GUARD = "sprites/gold/magicling/royal_guard.png"; //ported, magicling
		public static final String PRISONER = "sprites/gold/magicling/prisoner.png"; //ported, magicling
		public static final String MAYFLY = "sprites/gold/magicling/mayfly.png"; //ported, magicling
		public static final String GORGON = "sprites/gold/magicling/gorgon.png"; //ported, magicling
		public static final String GIANT_WORM = "sprites/gold/magicling/giant_worm.png"; //ported, magicling
		public static final String DRAKE = "sprites/gold/magicling/drake.png"; //ported, magicling
		public static final String DEMONLORD = "sprites/gold/magicling/demon_lord.png"; //ported, magicling
		public static final String TILES_LAIR = "sprites/dragonic/tiles_lair.png"; //ported, dragonic
		public static final String STATUE_ARMOR = "sprites/dragonic/statuearmor.png"; //ported, dragonic
		public static final String SMALLDRAKE = "sprites/dragonic/drake.png"; //ported, dragonic
		public static final String SLUG = "sprites/dragonic/snowslug.png"; //ported, dragonic
		public static final String SKELETON_WARRIOR = "sprites/dragonic/skeletonwarrior.png"; //ported, dragonic
		public static final String SHADOWMON = "sprites/dragonic/shadowmonfortomtom.png"; //ported, dragonic
		public static final String FLAMINGSKULL = "sprites/dragonic/flaminghead.png"; //ported, dragonic
		public static final String DREADKNIGHT = "sprites/dragonic/dreadk.png"; //ported, dragonic
		public static final String AINSLEY = "sprites/dragonic/ainsley.png"; //ported, dragonic
		public static final String TIMELESS_SOUL = "sprites/tower/timeless_soul.png"; //ported, tower
		public static final String TOWER_GNOLL = "sprites/tower/gnoll.png"; //ported, tower, a sheet with extra frames
		public static final String TOWER_RAT = "sprites/tower/rat.png"; //ported, tower
		public static final String TOWER_CRAB = "sprites/tower/crab.png"; //ported, tower
		public static final String TOWER_SKELETON = "sprites/tower/skeleton.png"; //ported, tower
		public static final String GOBLINSHAMAN = "sprites/tower/goblinshaman.png"; //ported, tower
		public static final String SLUGGER = "sprites/tower/slugger.png"; //ported, tower
		public static final String LICTERIALASHER = "sprites/tower/licterialasher.png"; //ported, tower
		public static final String GOBLINSAND = "sprites/tower/goblinsand.png"; //ported, tower
		public static final String GOBLINNINJA = "sprites/tower/goblinninja.png"; //ported, tower
		public static final String GOBLINGIANT = "sprites/tower/goblingiant.png"; //ported, tower
		public static final String GOBLINFAT = "sprites/tower/goblinfat.png"; //ported, tower
		public static final String TOWERCAMPRATSHIELD = "sprites/towers/campratshield.png"; //ported, tower
		public static final String TOWERCAMPRATMAGE = "sprites/towers/campratmage.png"; //ported, tower
		public static final String TOWERCAMPRATKNIFE = "sprites/towers/campratknife.png"; //ported, tower
		public static final String TOWERCAMPRATFLAG = "sprites/towers/campratflag.png"; //ported, tower
		public static final String TOWERCAMPRATARCHER = "sprites/towers/campratarcher.png"; //ported, tower
		public static final String PEPE = "npcs/lovecraft/pepe.png"; //ported, lovecraft
		public static final String GARDNER = "npcs/lovecraft/gardner.png"; //ported, lovecraft
		public static final String ELDERTHING = "mobs/lovecraft/elderthing.png"; //ported, lovecraft
		public static final String HELP_GRADNER = "helppictures/lovecraft/gardnerhelp.png"; //ported, lovecraft
		public static final String LIVINGPLANTS = "mobs/lovecraft/livingplants.png"; //ported, lovecraft
		public static final String SPIRIT = "mobs/lovecraft/kindspirit.png"; //ported, lovecraft
		public static final String EX_ITEMS = "sprites/transformati/ex_items.png"; //ported, transformati
		public static final String WILD_DREAD = "sprites/transformation/wild_dread.png"; //ported, transformation
		public static final String TAPIR_CROCODILE = "sprites/transformation/tapir_crocodile.png"; //ported, transformation
		public static final String SOUL_COLLECTOR = "sprites/transformation/soul_collector.png"; //ported, transformation
		public static final String RUNE_SPINNER = "sprites/transformation/rune_spinner.png"; //ported, transformation
		public static final String ROAST_LAMB_WARLOCK = "sprites/transformation/roast_lamb_warlock.png"; //ported, transformation
		public static final String POWERFUL_WRAITH = "sprites/transformation/powerful_wraith.png"; //ported, transformation
		public static final String OBSCURA = "sprites/transformation/obscura.png"; //ported, transformation
		public static final String MIMIC_CROCODILE = "sprites/transformation/mimic_crocodile.png"; //ported, transformation
		public static final String MECHANICAL_FIST = "sprites/transformation/mechanical_fist.png"; //ported, transformation
		public static final String MARSH_SLIME = "sprites/transformation/marsh_slime.png"; //ported, transformation
		public static final String HEAVY_CRABIFICATION = "sprites/transformation/heavy_crabification.png"; //ported, transformation
		public static final String EARTHLY_SERPENT = "sprites/transformation/earthly_serpent.png"; //ported, transformation
		public static final String CORROSIVE_SWARM = "sprites/transformation/corrosive_swarm.png"; //ported, transformation
		public static final String CHAIN_SHADOW_THIEF = "sprites/transformation/chain_shadow_thief.png"; //ported, transformation
		public static final String CAMOUFLAGE_GNOLL = "sprites/transformation/camouflage_gnoll.png"; //ported, transformation
		public static final String LANDPIRANHA = "sprites/transformation/landpiranha.png"; //ported, transformation
		public static final String SLOTMACHINE = "sprites/shittened/slotmachine.png"; //ported, shittened
		public static final String TWINGO = "sprites/shittened/twingo.png"; //ported, shittened
		public static final String ADVENTURER = "sprites/shittened/adventurer.png"; //ported, shittened
		public static final String TOKMANNI = "sprites/shittened/tokmanni.png"; //ported, shittened
		public static final String EVAN = "sprites/shittened/evan.png"; //ported, shittened
		public static final String RAVEN = "sprites/shittened/raven.png"; //ported, shittened
		public static final String GLASSBOX = "sprites/shittened/glass_box.png"; //ported, shittened
		public static final String FURNITURE = "sprites/shittened/furniture.png"; //ported, shittened
		public static final String CAPYBARA = "sprites/shittened/capybara.png"; //ported, shittened
		public static final String BUCKET = "sprites/shittened/bucket.png"; //ported, shittened
		public static final String BOOMBOX = "sprites/shittened/boombox.png"; //ported, shittened
		public static final String KETCHUPSPLAT = "sprites/shittened/ketchupsplat.png"; //ported, shittened
		public static final String GOBLIN = "sprites/shittened/goblin.png"; //ported, shittened
		public static final String WRETCH = "sprites/devoted/wretch.png"; //ported, devoted
		public static final String WISP = "sprites/devoted/wisp.png"; //ported, devoted
		public static final String WASP = "sprites/devoted/wasp.png"; //ported, devoted
		public static final String HOMONCULUS = "sprites/devoted/homonculus.png"; //ported, devoted
		public static final String BLUEIMP = "sprites/devoted/imp.png"; //ported, devoted
		public static final String WYRM = "sprites/reforged/wyrm.png"; //ported, reforged
		public static final String ROTTING = "sprites/cursed/rotting_fist.png"; //ported, cursed
		public static final String BURNING = "sprites/cursed/burning_fist.png"; //ported, cursed
		public static final String PRISON_EXIT = "custom_tiles/cursed/prison_exit.png"; //ported, cursed
		public static final String TILES_CAVES = "sprites/cursed/tiles_caves.png"; //ported, cursed
		public static final String DARK_MAGE = "sprites/cursed/dark_wizard.png"; //ported, cursed
		public static final String SEWERGOLEM = "sprites/cursed/hell_guardian.png"; //ported, cursed
		public static final String TILES_BOSS = "sprites/cursed/tiles_boss.png"; //ported, cursed
		public static final String TILES_SURFACE = "sprites/cursed/tiles_surface.png"; //ported, cursed
		public static final String WATER_HEAVEN = "sprites/cursed/water_heaven.png"; //ported, cursed
		public static final String TILES_HEAVEN = "sprites/cursed/tile_heaven.png"; //ported, cursed
		public static final String WATER_CITY = "sprites/cursed/water3.png"; //ported, cursed
		public static final String TILES_CITY = "sprites/cursed/tiles_city.png"; //ported, cursed
		public static final String WATER_CAVES = "sprites/cursed/water2.png"; //ported, cursed
		public static final String TILES_EARTH = "sprites/cursed/tiles_dolyahaven_new.png"; //ported, cursed
		public static final String WATER_HALLS = "sprites/cursed/water4.png"; //ported, cursed
		public static final String TILES_HALLS = "sprites/cursed/tiles_halls.png"; //ported, cursed
		public static final String TILES_AIR = "sprites/cursed/tiles_temple.png"; //ported, cursed
		public static final String TILES_PRISON = "sprites/cursed/tiles_prison.png"; //ported, cursed
		public static final String WATER_PRISON = "sprites/cursed/water1.png"; //ported, cursed
		public static final String LITTOWER = "sprites/cursed/littower.png"; //ported, cursed
		public static final String PILLAR = "sprites/abyssalnite/pillar.png"; //ported, abyssalnite
		public static final String EXP_GEN = "sprites/experienced/expgen.png"; //ported, experienced
		public static final String BBAT = "sprites/experienced/bbat.png"; //ported, experienced
		public static final String PETDRAGON_EXTRA = "sprites/cursed/dragon1.png"; //ported, cursed
		public static final String WATER_SEWERS = "sprites/cursed/water0.png"; //ported, cursed
		public static final String TILES_WATER = "sprites/cursed/tiles_beach.png"; //ported, cursed
		public static final String NOTES = "sprites/cursed/Notes.png"; //ported, cursed
		public static final String PETDRAGON = "sprites/cursed/petreddragon.png"; //ported, cursed
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
		public static final String PREFIX_ICONS = "sprites/prefix_icons.png"; //ported, shittened

		public static final String WARRIOR  = "sprites/warrior.png";
		public static final String MAGE     = "sprites/mage.png";
		public static final String ROGUE    = "sprites/rogue.png";
		public static final String HUNTRESS = "sprites/huntress.png";
		public static final String DUELIST  = "sprites/duelist.png";
		public static final String CLERIC   = "sprites/cleric.png";
		public static final String ZEALOT   = "sprites/zealot.png"; //ported, devoted
		public static final String ARTIFICER = "sprites/artificer.png"; //ported, reforged
		public static final String SUMMONER = "sprites/summoner.png"; //ported, ryuse
		public static final String VIKING = "sprites/viking.png"; //ported, dragonic
		public static final String DRAGONKNIGHT = "sprites/dragonknight.png"; //ported, dragonic
		public static final String DEMON = "sprites/demon.png"; //ported, ryuse, the Imp the Summoner sees
		public static final String SHIT_BAT = "sprites/shittened/bat.png"; //ported, shittened, the mod drew extra frames on this sheet
		public static final String SHIT_GOO = "sprites/shittened/goo.png"; //ported, shittened, the mod drew extra frames on this sheet
		public static final String SHIT_DM200 = "sprites/shittened/dm200.png"; //ported, shittened, the mod drew extra frames on this sheet
		public static final String SHIT_PIRANHA = "sprites/shittened/piranha.png"; //ported, shittened, the mod drew extra frames on this sheet
		public static final String SHIT_SNAKE = "sprites/shittened/snake.png"; //ported, shittened, the mod drew extra frames on this sheet
		public static final String SHIT_EYE = "sprites/shittened/eye.png"; //ported, shittened, the mod drew extra frames on this sheet
		public static final String SHIT_DM100 = "sprites/shittened/dm100.png"; //ported, shittened, the mod drew extra frames on this sheet
		public static final String SHIT_RAT = "sprites/shittened/rat.png"; //ported, shittened, the mod drew extra frames on this sheet
		public static final String SHIT_SWARM = "sprites/shittened/swarm.png"; //ported, shittened, the mod drew extra frames on this sheet
		public static final String CONSTRUCT = "sprites/construct.png"; //ported, reforged
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
		public static final String THIEF_OOF = "sprites/experienced/thief_oof.png"; //ported, experienced. Taller than the normal thief sheet, the OOF Thief is on its third row
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
		public static final String HALFRIPPER = "sprites/reforged/halfripper.png"; //ported, reforged
		public static final String WOLF = "sprites/unleashed/wolf.png"; //ported, unleashed
		public static final String CHAOS = "sprites/unleashed/chaos_mage.png"; //ported, unleashed
		public static final String EVILLORD = "sprites/unleashed/demonlord.png"; //ported, unleashed
		public static final String ICEDEMON = "sprites/unleashed/icedemon.png"; //ported, unleashed
		public static final String LOSTSOUL = "sprites/unleashed/lost_souls.png"; //ported, unleashed
		public static final String MINOTAUR = "sprites/unleashed/minotaur.png"; //ported, unleashed
		public static final String SPIDERBOT = "sprites/unleashed/spiderbot.png"; //ported, unleashed
		public static final String SQUID = "sprites/unleashed/squid.png"; //ported, unleashed
		public static final String ROOSTER = "sprites/unleashed/velocirooster.png"; //ported, unleashed
		public static final String ZOMBIE = "sprites/unleashed/zombie.png"; //ported, unleashed
		public static final String U_MONK = "sprites/unleashed/monk.png"; //ported, unleashed. The mod sheet has extra frames
		public static final String U_SLIME = "sprites/unleashed/slime.png"; //ported, unleashed. The mod sheet has extra frames
		public static final String U_KING = "sprites/unleashed/king.png"; //ported, unleashed. The mod sheet has extra frames
		public static final String U_GOLEM = "sprites/unleashed/golem.png"; //ported, unleashed. The mod sheet has extra frames
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
