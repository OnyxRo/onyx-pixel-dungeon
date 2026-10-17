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

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class ItemSpriteSheet {

	public static final int SIZE = 16;

	private static final int TX_WIDTH = 256;
	private static final int TX_HEIGHT = 3040;

	private static final int WIDTH = TX_WIDTH / SIZE;

	public static TextureFilm film = new TextureFilm( TX_WIDTH, TX_HEIGHT, SIZE, SIZE );

	private static int xy(int x, int y){
		x -= 1; y -= 1;
		return x + WIDTH*y;
	}

	private static void assignItemRect( int item, int width, int height ){
		int x = (item % WIDTH) * SIZE;
		int y = (item / WIDTH) * SIZE;
		film.add( item, x, y, x+width, y+height);
	}

	private static final int PLACEHOLDERS   =                               xy(1, 1);   //18 slots
	//SOMETHING is the default item sprite at position 0. May show up ingame if there are bugs.
	public static final int SOMETHING       = PLACEHOLDERS+0;
	public static final int WEAPON_HOLDER   = PLACEHOLDERS+1;
	public static final int ARMOR_HOLDER    = PLACEHOLDERS+2;
	public static final int MISSILE_HOLDER  = PLACEHOLDERS+3;
	public static final int WAND_HOLDER     = PLACEHOLDERS+4;
	public static final int RING_HOLDER     = PLACEHOLDERS+5;
	public static final int ARTIFACT_HOLDER = PLACEHOLDERS+6;
	public static final int TRINKET_HOLDER  = PLACEHOLDERS+7;
	public static final int FOOD_HOLDER     = PLACEHOLDERS+8;
	public static final int BOMB_HOLDER     = PLACEHOLDERS+9;
	public static final int POTION_HOLDER   = PLACEHOLDERS+10;
	public static final int SEED_HOLDER     = PLACEHOLDERS+11;
	public static final int SCROLL_HOLDER   = PLACEHOLDERS+12;
	public static final int STONE_HOLDER    = PLACEHOLDERS+13;
	public static final int ELIXIR_HOLDER   = PLACEHOLDERS+14;
	public static final int SPELL_HOLDER    = PLACEHOLDERS+15;
	public static final int MOB_HOLDER      = PLACEHOLDERS+16;
	public static final int DOCUMENT_HOLDER = PLACEHOLDERS+17;
	static{
		assignItemRect(SOMETHING,       8,  13);
		assignItemRect(WEAPON_HOLDER,   14, 14);
		assignItemRect(ARMOR_HOLDER,    14, 12);
		assignItemRect(MISSILE_HOLDER,  15, 15);
		assignItemRect(WAND_HOLDER,     14, 14);
		assignItemRect(RING_HOLDER,     8,  10);
		assignItemRect(ARTIFACT_HOLDER, 15, 15);
		assignItemRect(TRINKET_HOLDER,  16, 11);
		assignItemRect(FOOD_HOLDER,     15, 11);
		assignItemRect(BOMB_HOLDER,     10, 13);
		assignItemRect(POTION_HOLDER,   12, 14);
		assignItemRect(SEED_HOLDER,     10, 10);
		assignItemRect(SCROLL_HOLDER,   15, 14);
		assignItemRect(STONE_HOLDER,    14, 12);
		assignItemRect(ELIXIR_HOLDER,   12, 14);
		assignItemRect(SPELL_HOLDER,    8,  16);
		assignItemRect(MOB_HOLDER,      15, 14);
		assignItemRect(DOCUMENT_HOLDER, 10, 11);
	}

	private static final int UNCOLLECTIBLE  =                               xy(3, 2);   //14 slots
	public static final int GOLD            = UNCOLLECTIBLE+0;
	public static final int ENERGY          = UNCOLLECTIBLE+1;

	public static final int DEWDROP         = UNCOLLECTIBLE+3;
	public static final int PETAL           = UNCOLLECTIBLE+4;
	public static final int SANDBAG         = UNCOLLECTIBLE+5;
	public static final int SPIRIT_ARROW    = UNCOLLECTIBLE+6;
	
	public static final int TENGU_BOMB      = UNCOLLECTIBLE+8;
	public static final int TENGU_SHOCKER   = UNCOLLECTIBLE+9;
	public static final int GEO_BOULDER     = UNCOLLECTIBLE+10;
	static{
		assignItemRect(GOLD,        15, 13);
		assignItemRect(ENERGY,      16, 16);

		assignItemRect(DEWDROP,     10,  9);
		assignItemRect(PETAL,        8,  8);
		assignItemRect(SANDBAG,     10, 10);
		assignItemRect(SPIRIT_ARROW,11, 11);
		
		assignItemRect(TENGU_BOMB,      10, 10);
		assignItemRect(TENGU_SHOCKER,   10, 10);
		assignItemRect(GEO_BOULDER,     16, 15);
	}

	private static final int CONTAINERS     =                               xy(1, 3);   //16 slots
	public static final int BONES           = CONTAINERS+0;
	public static final int REMAINS         = CONTAINERS+1;
	public static final int TOMB            = CONTAINERS+2;
	public static final int GRAVE           = CONTAINERS+3;
	public static final int CHEST           = CONTAINERS+4;
	public static final int LOCKED_CHEST    = CONTAINERS+5;
	public static final int CRYSTAL_CHEST   = CONTAINERS+6;
	public static final int EBONY_CHEST     = CONTAINERS+7;
	static{
		assignItemRect(BONES,           14, 11);
		assignItemRect(REMAINS,         14, 11);
		assignItemRect(TOMB,            14, 15);
		assignItemRect(GRAVE,           14, 15);
		assignItemRect(CHEST,           16, 14);
		assignItemRect(LOCKED_CHEST,    16, 14);
		assignItemRect(CRYSTAL_CHEST,   16, 14);
		assignItemRect(EBONY_CHEST,     16, 14);
	}

	private static final int MISC_CONSUMABLE =                              xy(1, 4);   //32 slots
	public static final int ANKH            = MISC_CONSUMABLE +0;
	public static final int STYLUS          = MISC_CONSUMABLE +1;
	public static final int SEAL            = MISC_CONSUMABLE +2;
	public static final int TORCH           = MISC_CONSUMABLE +3;
	public static final int BEACON          = MISC_CONSUMABLE +4;
	public static final int HONEYPOT        = MISC_CONSUMABLE +5;
	public static final int SHATTPOT        = MISC_CONSUMABLE +6;
	public static final int IRON_KEY        = MISC_CONSUMABLE +7;
	public static final int GOLDEN_KEY      = MISC_CONSUMABLE +8;
	public static final int CRYSTAL_KEY     = MISC_CONSUMABLE +9;
	public static final int WORN_KEY        = MISC_CONSUMABLE +10;
	public static final int MASK            = MISC_CONSUMABLE +11;
	public static final int CROWN           = MISC_CONSUMABLE +12;
	public static final int AMULET          = MISC_CONSUMABLE +13;
	public static final int MASTERY         = MISC_CONSUMABLE +14;
	public static final int KIT             = MISC_CONSUMABLE +15;
	public static final int SEAL_SHARD      = MISC_CONSUMABLE +16;
	public static final int BROKEN_STAFF    = MISC_CONSUMABLE +17;
	public static final int CLOAK_SCRAP     = MISC_CONSUMABLE +18;
	public static final int BOW_FRAGMENT    = MISC_CONSUMABLE +19;
	public static final int BROKEN_HILT     = MISC_CONSUMABLE +20;
	public static final int TORN_PAGE       = MISC_CONSUMABLE +21;
	public static final int TRINKET_CATA    = MISC_CONSUMABLE +22;

	static{
		assignItemRect(ANKH,            10, 16);
		assignItemRect(STYLUS,          12, 13);
		
		assignItemRect(SEAL,            13, 13);
		assignItemRect(TORCH,           12, 15);
		assignItemRect(BEACON,          16, 15);
		
		assignItemRect(HONEYPOT,        14, 12);
		assignItemRect(SHATTPOT,        14, 12);
		assignItemRect(IRON_KEY,        8,  14);
		assignItemRect(GOLDEN_KEY,      8,  14);
		assignItemRect(CRYSTAL_KEY,     8,  14);
		assignItemRect(WORN_KEY,        8,  14);
		assignItemRect(MASK,            11,  9);
		assignItemRect(CROWN,           13,  7);
		assignItemRect(AMULET,          16, 16);
		assignItemRect(MASTERY,         13, 16);
		assignItemRect(KIT,             16, 15);

		assignItemRect(SEAL_SHARD,      12, 12);
		assignItemRect(BROKEN_STAFF,    14, 10);
		assignItemRect(CLOAK_SCRAP,      9,  9);
		assignItemRect(BOW_FRAGMENT,    12,  9);
		assignItemRect(BROKEN_HILT,      9,  9);
		assignItemRect(TORN_PAGE,       11, 13);

		assignItemRect(TRINKET_CATA,    12, 11);
	}
	
	private static final int BOMBS          =                               xy(1, 6);   //16 slots
	public static final int BOMB            = BOMBS+0;
	public static final int DBL_BOMB        = BOMBS+1;
	public static final int FIRE_BOMB       = BOMBS+2;
	public static final int FROST_BOMB      = BOMBS+3;
	public static final int REGROWTH_BOMB   = BOMBS+4;
	public static final int SMOKE_BOMB      = BOMBS+5;
	public static final int FLASHBANG       = BOMBS+6;
	public static final int HOLY_BOMB       = BOMBS+7;
	public static final int WOOLY_BOMB      = BOMBS+8;
	public static final int NOISEMAKER      = BOMBS+9;
	public static final int ARCANE_BOMB     = BOMBS+10;
	public static final int SHRAPNEL_BOMB   = BOMBS+11;
	
	static{
		assignItemRect(BOMB,            10, 13);
		assignItemRect(DBL_BOMB,        14, 13);
		assignItemRect(FIRE_BOMB,       13, 12);
		assignItemRect(FROST_BOMB,      13, 12);
		assignItemRect(REGROWTH_BOMB,   13, 12);
		assignItemRect(SMOKE_BOMB,      13, 12);
		assignItemRect(FLASHBANG,       10, 13);
		assignItemRect(HOLY_BOMB,       10, 13);
		assignItemRect(WOOLY_BOMB,      10, 13);
		assignItemRect(NOISEMAKER,      10, 13);
		assignItemRect(ARCANE_BOMB,     10, 13);
		assignItemRect(SHRAPNEL_BOMB,   10, 13);
	}

	private static final int WEP_TIER1      =                               xy(1, 7);   //8 slots
	public static final int WORN_SHORTSWORD = WEP_TIER1+0;
	public static final int CUDGEL          = WEP_TIER1+1;
	public static final int GLOVES          = WEP_TIER1+2;
	public static final int RAPIER          = WEP_TIER1+3;
	public static final int DAGGER          = WEP_TIER1+4;
	public static final int MAGES_STAFF     = WEP_TIER1+5;
	static{
		assignItemRect(WORN_SHORTSWORD, 13, 13);
		assignItemRect(CUDGEL,          15, 15);
		assignItemRect(GLOVES,          12, 16);
		assignItemRect(RAPIER,          13, 14);
		assignItemRect(DAGGER,          12, 13);
		assignItemRect(MAGES_STAFF,     15, 16);
	}

	private static final int WEP_TIER2      =                               xy(9, 7);   //8 slots
	public static final int SHORTSWORD      = WEP_TIER2+0;
	public static final int HAND_AXE        = WEP_TIER2+1;
	public static final int SPEAR           = WEP_TIER2+2;
	public static final int QUARTERSTAFF    = WEP_TIER2+3;
	public static final int DIRK            = WEP_TIER2+4;
	public static final int SICKLE          = WEP_TIER2+5;
	static{
		assignItemRect(SHORTSWORD,      13, 13);
		assignItemRect(HAND_AXE,        12, 14);
		assignItemRect(SPEAR,           16, 16);
		assignItemRect(QUARTERSTAFF,    16, 16);
		assignItemRect(DIRK,            13, 14);
		assignItemRect(SICKLE,          15, 15);
	}

	private static final int WEP_TIER3      =                               xy(1, 8);   //8 slots
	public static final int SWORD           = WEP_TIER3+0;
	public static final int MACE            = WEP_TIER3+1;
	public static final int SCIMITAR        = WEP_TIER3+2;
	public static final int ROUND_SHIELD    = WEP_TIER3+3;
	public static final int SAI             = WEP_TIER3+4;
	public static final int WHIP            = WEP_TIER3+5;
	static{
		assignItemRect(SWORD,           14, 14);
		assignItemRect(MACE,            15, 15);
		assignItemRect(SCIMITAR,        13, 16);
		assignItemRect(ROUND_SHIELD,    16, 16);
		assignItemRect(SAI,             16, 16);
		assignItemRect(WHIP,            14, 14);
	}

	private static final int WEP_TIER4      =                               xy(9, 8);   //8 slots
	public static final int LONGSWORD       = WEP_TIER4+0;
	public static final int BATTLE_AXE      = WEP_TIER4+1;
	public static final int FLAIL           = WEP_TIER4+2;
	public static final int RUNIC_BLADE     = WEP_TIER4+3;
	public static final int ASSASSINS_BLADE = WEP_TIER4+4;
	public static final int CROSSBOW        = WEP_TIER4+5;
	public static final int KATANA          = WEP_TIER4+6;
	static{
		assignItemRect(LONGSWORD,       15, 15);
		assignItemRect(BATTLE_AXE,      16, 16);
		assignItemRect(FLAIL,           14, 14);
		assignItemRect(RUNIC_BLADE,     14, 14);
		assignItemRect(ASSASSINS_BLADE, 14, 15);
		assignItemRect(CROSSBOW,        15, 15);
		assignItemRect(KATANA,          15, 16);
	}

	private static final int WEP_TIER5      =                               xy(1, 9);   //8 slots
	public static final int GREATSWORD      = WEP_TIER5+0;
	public static final int WAR_HAMMER      = WEP_TIER5+1;
	public static final int GLAIVE          = WEP_TIER5+2;
	public static final int GREATAXE        = WEP_TIER5+3;
	public static final int GREATSHIELD     = WEP_TIER5+4;
	public static final int GAUNTLETS       = WEP_TIER5+5;
	public static final int WAR_SCYTHE      = WEP_TIER5+6;
	static{
		assignItemRect(GREATSWORD,  16, 16);
		assignItemRect(WAR_HAMMER,  16, 16);
		assignItemRect(GLAIVE,      16, 16);
		assignItemRect(GREATAXE,    12, 16);
		assignItemRect(GREATSHIELD, 12, 16);
		assignItemRect(GAUNTLETS,   13, 15);
		assignItemRect(WAR_SCYTHE,  14, 15);
	}

	                                                                                    //8 free slots

	private static final int MISSILE_WEP    =                               xy(1, 10);  //16 slots. 3 per tier + bow
	public static final int SPIRIT_BOW      = MISSILE_WEP+0;
	
	public static final int THROWING_SPIKE  = MISSILE_WEP+1;
	public static final int THROWING_KNIFE  = MISSILE_WEP+2;
	public static final int THROWING_STONE  = MISSILE_WEP+3;
	
	public static final int FISHING_SPEAR   = MISSILE_WEP+4;
	public static final int SHURIKEN        = MISSILE_WEP+5;
	public static final int THROWING_CLUB   = MISSILE_WEP+6;
	
	public static final int THROWING_SPEAR  = MISSILE_WEP+7;
	public static final int BOLAS           = MISSILE_WEP+8;
	public static final int KUNAI           = MISSILE_WEP+9;
	
	public static final int JAVELIN         = MISSILE_WEP+10;
	public static final int TOMAHAWK        = MISSILE_WEP+11;
	public static final int BOOMERANG       = MISSILE_WEP+12;
	
	public static final int TRIDENT         = MISSILE_WEP+13;
	public static final int THROWING_HAMMER = MISSILE_WEP+14;
	public static final int FORCE_CUBE      = MISSILE_WEP+15;
	
	static{
		assignItemRect(SPIRIT_BOW,      16, 16);
		
		assignItemRect(THROWING_SPIKE,  11, 10);
		assignItemRect(THROWING_KNIFE,  12, 13);
		assignItemRect(THROWING_STONE,  12, 10);
		
		assignItemRect(FISHING_SPEAR,   11, 11);
		assignItemRect(SHURIKEN,        12, 12);
		assignItemRect(THROWING_CLUB,   12, 12);
		
		assignItemRect(THROWING_SPEAR,  13, 13);
		assignItemRect(BOLAS,           15, 14);
		assignItemRect(KUNAI,           15, 15);
		
		assignItemRect(JAVELIN,         16, 16);
		assignItemRect(TOMAHAWK,        13, 13);
		assignItemRect(BOOMERANG,       14, 14);
		
		assignItemRect(TRIDENT,         16, 16);
		assignItemRect(THROWING_HAMMER, 12, 12);
		assignItemRect(FORCE_CUBE,      11, 12);
	}
	
	public static final int DARTS    =                                      xy(1, 11);  //16 slots
	public static final int DART            = DARTS+0;
	public static final int ROT_DART        = DARTS+1;
	public static final int INCENDIARY_DART = DARTS+2;
	public static final int ADRENALINE_DART = DARTS+3;
	public static final int HEALING_DART    = DARTS+4;
	public static final int CHILLING_DART   = DARTS+5;
	public static final int SHOCKING_DART   = DARTS+6;
	public static final int POISON_DART     = DARTS+7;
	public static final int CLEANSING_DART  = DARTS+8;
	public static final int PARALYTIC_DART  = DARTS+9;
	public static final int HOLY_DART       = DARTS+10;
	public static final int DISPLACING_DART = DARTS+11;
	public static final int BLINDING_DART   = DARTS+12;
	static {
		for (int i = DARTS; i < DARTS+16; i++)
			assignItemRect(i, 15, 15);
	}
	
	//items ported from other mods. Row 13 of the sheet, 16 slots
	private static final int MOD_ITEMS      =                               xy(1, 13);

	public static final int WARPICK = MOD_ITEMS+0;
	public static final int WOODEN_BOOMERANG = MOD_ITEMS+1;
	public static final int MILK_CHEESE = xy(1, 150);
	public static final int HOWARD_CHEST = xy(2, 100);
	public static final int EVAN_CHEST = xy(3, 100);
	public static final int GOLD_CHEST = xy(4, 100);
	public static final int HEAVY_CROSSBOW = xy(0, 100);
	public static final int WEIGHT = xy(1, 100);
	public static final int CHAIN = xy(1, 128);
	public static final int ARMOR_DRAGON_KNIGHT = xy(7, 49);
	public static final int ARMOR_VIKING = xy(1, 49);
	public static final int CURARE_DART = xy(2, 49);
	public static final int DRAGON_DAGGER = xy(3, 49);
	public static final int KNUCKLEDUSTER = xy(4, 49);
	public static final int VIKING_MACE = xy(5, 49);
	public static final int WAND_VENOM = xy(6, 49);
	public static final int BAN_HAMMER = xy(1, 56);
	public static final int BONE_CLAYMORE = xy(2, 56);
	public static final int BONE_MACHETE = xy(3, 56);
	public static final int DUAL_HATCHET = xy(4, 56);
	public static final int EXOTIC_BLESSING = xy(5, 56);
	public static final int EXOTIC_DISCOVERIES = xy(6, 56);
	public static final int EXOTIC_GOLEMS = xy(7, 56);
	public static final int EXOTIC_RATLEGION = xy(8, 56);
	public static final int GOLDEN_SWORD = xy(9, 56);
	public static final int INFIPIKE = xy(10, 56);
	public static final int RED_KNIFE = xy(11, 56);
	public static final int RING_LETHALITY = xy(12, 56);
	public static final int SCROLL_ANIMATION = xy(13, 56);
	public static final int SCROLL_DROWSINESS = xy(14, 56);
	public static final int STONE_HAMMER = xy(15, 56);
	public static final int TRISAI = xy(16, 56);
	public static final int WAND_SNAKES = xy(1, 57);
	public static final int BOW = xy(3, 53);
	public static final int PLANT_ITEMS_BLANDFRUIT = xy(3, 51);
	public static final int BLANDFRUIT_HERB = xy(4, 51);
	public static final int PLANT_ITEMS_BLINDWEED = xy(5, 51);
	public static final int BLINDWEED_HERB = xy(6, 51);
	public static final int PLANT_ITEMS_DEWCATCHER = xy(7, 51);
	public static final int DEWCATCHER_HERB = xy(8, 51);
	public static final int PLANT_ITEMS_DREAMFOIL = xy(9, 51);
	public static final int DREAMFOIL_HERB = xy(10, 51);
	public static final int PLANT_ITEMS_EARTHROOT = xy(11, 51);
	public static final int EARTHROOT_HERB = xy(12, 51);
	public static final int PLANT_ITEMS_FADELEAF = xy(13, 51);
	public static final int FADELEAF_HERB = xy(14, 51);
	public static final int PLANT_ITEMS_FIREBLOOM = xy(15, 51);
	public static final int FIREBLOOM_HERB = xy(16, 51);
	public static final int PLANT_ITEMS_ICECAP = xy(1, 52);
	public static final int ICECAP_HERB = xy(2, 52);
	public static final int PLANT_ITEMS_ROTBERRY = xy(3, 52);
	public static final int ROTBERRY_HERB = xy(4, 52);
	public static final int PLANT_ITEMS_SEEDPOD = xy(5, 52);
	public static final int SEEDPOD_HERB = xy(6, 52);
	public static final int PLANT_ITEMS_SORROWMOSS = xy(7, 52);
	public static final int SORROWMOSS_HERB = xy(8, 52);
	public static final int PLANT_ITEMS_STARFLOWER = xy(9, 52);
	public static final int STARFLOWER_HERB = xy(10, 52);
	public static final int PLANT_ITEMS_STEAMWEED = xy(11, 52);
	public static final int STEAMWEED_HERB = xy(12, 52);
	public static final int PLANT_ITEMS_STORMVINE = xy(13, 52);
	public static final int STORMVINE_HERB = xy(14, 52);
	public static final int PLANT_ITEMS_SUNGRASS = xy(15, 52);
	public static final int SUNGRASS_HERB = xy(16, 52);
	public static final int AXE = xy(1, 55);
	public static final int CALIBER_PISTOL = xy(2, 55);
	public static final int CHAKRAM = xy(3, 55);
	public static final int ELDERSIGN = xy(5, 55);
	public static final int GIANT_SHURIKEN = xy(6, 55);
	public static final int GOLDEN_PISTOL = xy(7, 55);
	public static final int HALBERD = xy(8, 55);
	public static final int HARPOON = xy(9, 55);
	public static final int KNIFEGLOVE = xy(10, 55);
	public static final int KUSARIGAMA = xy(12, 55);
	public static final int LEAD_KEY = xy(13, 55);
	public static final int LONG_RIFLE = xy(14, 55);
	public static final int LUGER = xy(15, 55);
	public static final int PISTOL = xy(16, 55);
	public static final int PLATIN_KEY = xy(1, 50);
	public static final int PRIMITIVESPEAR = xy(2, 50);
	public static final int RIFLE = xy(3, 50);
	public static final int SCISSORS = xy(4, 50);
	public static final int SCYTHE = xy(5, 50);
	public static final int SEED_DREAMFOIL = xy(6, 50);
	public static final int SEED_STEAMWEED = xy(7, 50);
	public static final int SHORT_SWORD = xy(8, 50);
	public static final int SHOTGUN = xy(9, 50);
	public static final int SILVER_PISTOL = xy(10, 50);
	public static final int SPIKEDSWORD = xy(11, 50);
	public static final int TINCTUREBOTTLE = xy(12, 50);
	public static final int TITANIUM_KEY = xy(13, 50);
	public static final int TNT = xy(14, 50);
	public static final int TNT_BUNDLE = xy(15, 50);
	public static final int WOODEN_KEY = xy(1, 51);
	public static final int YELLOWSIGN = xy(2, 51);
	public static final int ARMOR_DM400 = xy(15, 47);
	public static final int ARMOR_FREEMAN = xy(16, 47);
	public static final int ARMOR_FRIAR = xy(1, 48);
	public static final int ARMOR_NINJA = xy(2, 48);
	public static final int ARMOR_SLIME = xy(3, 48);
	public static final int ARMOR_SLIMEGIRL = xy(4, 48);
	public static final int BOTTLEDENERGY = xy(5, 48);
	public static final int BROKEN_PACKAGE = xy(6, 48);
	public static final int BROKEN_TOOL = xy(7, 48);
	public static final int BRONZE_WATCH = xy(8, 48);
	public static final int GOLD_INGOT = xy(9, 48);
	public static final int MAGIC_FEATHER = xy(10, 48);
	public static final int MATCHAPUDDING = xy(11, 48);
	public static final int RED_ENVELOPE = xy(12, 48);
	public static final int ROYAL_JEWELRY = xy(13, 48);
	public static final int SMALLSKURIKEN = xy(14, 48);
	public static final int SPEAR_SHIELD = xy(15, 48);
	public static final int TINCTURE = xy(16, 48);
	public static final int BLOWPIPE = xy(4, 47);
	public static final int BRACER = xy(5, 47);
	public static final int LUCKYCOIN = xy(6, 47);
	public static final int LWHIP = xy(7, 47);
	public static final int SLIMEBALL = xy(8, 47);
	public static final int SPEARCONQUEROR = xy(9, 47);
	public static final int STEEL_WHIP = xy(10, 47);
	public static final int TAMARU = xy(11, 47);
	public static final int TATTEKI = xy(12, 47);
	public static final int WAKIZASHI = xy(13, 47);
	public static final int WALK_STICK = xy(14, 47);
	public static final int HELMET_HOLDER = xy(3, 47);
	public static final int HELMET_CRUSADER = xy(3, 46);
	public static final int HELMET_BARBARIAN = xy(4, 46);
	public static final int HELMET_APPRENTICE = xy(5, 46);
	public static final int HELMET_EMERALD = xy(6, 46);
	public static final int DWARF_CROWN = xy(7, 46);
	public static final int HEADDRESS_OF_REGENERATION = xy(8, 46);
	public static final int WIZARD_HAT = xy(9, 46);
	public static final int HELMET_CLOWN = xy(10, 46);
	public static final int HELMET_HORROR = xy(11, 46);
	public static final int HELMET_RANGER = xy(12, 46);
	public static final int TURTLE_SCARF_BLUE = xy(13, 46);
	public static final int LITTLE_PAIL = xy(14, 46);
	public static final int RIDER_MASK = xy(15, 46);
	public static final int HELMET_GUARD = xy(16, 46);
	public static final int STRAW_HAT = xy(1, 47);
	public static final int MASK_OF_MADNESS = xy(2, 47);
	public static final int ARMOR_SUMMONER = xy(15, 45);
	public static final int EYE_OF_WISDOM = xy(16, 45);
	public static final int SUMMONER_WHIP = xy(1, 46);
	public static final int TRAINING_WHIP = xy(2, 46);
	public static final int SEED_COFFEE = xy(12, 45);
	public static final int SEED_POTATO = xy(13, 45);
	public static final int SEED_TOMATO = xy(14, 45);
	public static final int POTION_RUSSET = xy(8, 45);
	public static final int POTION_MAROON = xy(9, 45);
	public static final int POTION_SEPIA = xy(10, 45);
	public static final int POTION_LILAC = xy(1, 53);
	public static final int POTION_OLIVE = xy(2, 53);
	public static final int SCROLL_EIHWAZ = xy(11, 45);
	public static final int RAT = xy(7, 45);
	public static final int ALBUM_BEAUTIFUL = xy(10, 39);
	public static final int ALBUM_CHAIR = xy(11, 39);
	public static final int ALBUM_FINNISH = xy(12, 39);
	public static final int ALBUM_HOG = xy(13, 39);
	public static final int ALBUM_NONSENSE = xy(14, 39);
	public static final int ALBUM_POP = xy(15, 39);
	public static final int ALBUM_RAT = xy(16, 39);
	public static final int ALBUM_REMIX = xy(1, 40);
	public static final int ALBUM_SEA = xy(2, 40);
	public static final int ALBUM_SYNTH = xy(3, 40);
	public static final int ALBUM_TWINGO = xy(4, 40);
	public static final int ARMADILLO = xy(5, 40);
	public static final int ARMOR_MALE = xy(6, 40);
	public static final int ARMOR_POST = xy(7, 40);
	public static final int ARTIFACT_MP3_PLAYER_1 = xy(8, 40);
	public static final int ARTIFACT_MP3_PLAYER_2 = xy(9, 40);
	public static final int BAKED_FISH = xy(10, 40);
	public static final int BASED_CAKE = xy(11, 40);
	public static final int BERRY_BALLS = xy(12, 40);
	public static final int BERRY_CAKE = xy(13, 40);
	public static final int BIGBAG = xy(14, 40);
	public static final int BRANCH = xy(15, 40);
	public static final int BREAD = xy(16, 40);
	public static final int BRUSCHETTA = xy(1, 41);
	public static final int CAKE_BASE = xy(2, 41);
	public static final int CAR = xy(3, 41);
	public static final int CHEESE_SLICE = xy(4, 41);
	public static final int CHOCOLATE_CAKE = xy(5, 41);
	public static final int COFFEE = xy(6, 41);
	public static final int COFFEE_BEAN = xy(7, 41);
	public static final int COMPLETE_BREAKFAST = xy(8, 41);
	public static final int COOKED_WARRIOR = xy(9, 41);
	public static final int COOKIES = xy(10, 41);
	public static final int CRISPY_BASS = xy(11, 41);
	public static final int DANISH_PASTRY = xy(12, 41);
	public static final int EGG = xy(13, 41);
	public static final int FISH = xy(14, 41);
	public static final int FLOUR = xy(15, 41);
	public static final int FOFFS = xy(16, 41);
	public static final int FOODCRATE = xy(1, 42);
	public static final int FRIED_EGG = xy(2, 42);
	public static final int FRIED_RAT = xy(3, 42);
	public static final int GOBLIN_CURSE = xy(4, 42);
	public static final int GOLDAMULET = xy(5, 42);
	public static final int GOLDBAR = xy(6, 42);
	public static final int GOLDENMEAT = xy(7, 42);
	public static final int GREATGREATSWORD = xy(8, 42);
	public static final int HASHBROWNS = xy(9, 42);
	public static final int ICE_CREAM = xy(10, 42);
	public static final int MEATBALLS = xy(11, 42);
	public static final int MILK = xy(12, 42);
	public static final int NUTS = xy(13, 42);
	public static final int OIL = xy(14, 42);
	public static final int OMELET = xy(15, 42);
	public static final int PAINTING_MANA_LISA = xy(16, 42);
	public static final int PAINTING_MEAT = xy(1, 43);
	public static final int PANCAKES = xy(2, 43);
	public static final int PANCAKES_CHEESE = xy(3, 43);
	public static final int PIETHON = xy(4, 43);
	public static final int PILE_OF_SAIS = xy(5, 43);
	public static final int PILLOW = xy(6, 43);
	public static final int PINKAMULET = xy(7, 43);
	public static final int PIZZA = xy(8, 43);
	public static final int PLATE_CARRIER = xy(9, 43);
	public static final int POTATO = xy(10, 43);
	public static final int PUG = xy(11, 43);
	public static final int RATATOUILLE = xy(12, 43);
	public static final int SASHIMI = xy(13, 43);
	public static final int SAW = xy(14, 43);
	public static final int SILVERAMULET = xy(15, 43);
	public static final int SILVER_KNIFE = xy(16, 43);
	public static final int SMALLBAG = xy(1, 44);
	public static final int SMALLFROZEN = xy(2, 44);
	public static final int SMALLGREATSWORD = xy(3, 44);
	public static final int SMALLMEAT = xy(4, 44);
	public static final int SMALLSTEAK = xy(5, 44);
	public static final int SPAGHETTI = xy(6, 44);
	public static final int SPARROW = xy(7, 44);
	public static final int SPEARAXE = xy(8, 44);
	public static final int SPRAYPAINT = xy(9, 44);
	public static final int STEWEDSMALL = xy(10, 44);
	public static final int STIR_FRY = xy(11, 44);
	public static final int STONEAMULET = xy(12, 44);
	public static final int STONE_REFORGE = xy(13, 44);
	public static final int SUGAR = xy(14, 44);
	public static final int TIDEPOD = xy(15, 44);
	public static final int TOMATO = xy(16, 44);
	public static final int TRIPLE_SHOT_ESPRESSO = xy(1, 45);
	public static final int UNTAXEDFOOD = xy(2, 45);
	public static final int WARRIORIUM = xy(3, 45);
	public static final int WAXBAR = xy(4, 45);
	public static final int WHEAT = xy(5, 45);
	public static final int WILLOW = xy(6, 45);
	public static final int MARKING_ROCK = xy(6, 39);
	public static final int ARCANE_ROOT = xy(7, 39);
	public static final int CRYSTAL_APPLE = xy(8, 39);
	public static final int LARGE_RADISH = xy(9, 39);
	public static final int VIAL_SHARD = xy(1, 39);
	public static final int ARMOR_ZEALOT = xy(2, 39);
	public static final int ARTIFACT_VIAL1 = xy(3, 39);
	public static final int ARTIFACT_VIAL2 = xy(4, 39);
	public static final int ARTIFACT_VIAL3 = xy(5, 39);
	public static final int CHAINS = xy(12, 38);
	public static final int ICYCORE = xy(13, 38);
	public static final int WAND_EXPLOSION = xy(14, 38);
	public static final int WAND_OF_ELEMENTS = xy(15, 38);
	public static final int WAND_OF_FIREBALL = xy(16, 38);
	public static final int ARMOR_ARTIFICER = xy(3, 38);
	public static final int ARTIFACT_RINGBOX = xy(4, 38);
	public static final int CRACKED_GUN = xy(5, 38);
	public static final int ELIXIR_CLAWS = xy(6, 38);
	public static final int GUN = xy(7, 38);
	public static final int LUCKY_DICE = xy(8, 38);
	public static final int STONE_BLIGHT = xy(9, 38);
	public static final int WANDERERS_MAP = xy(10, 38);
	public static final int WAND_DISPLACEMENT = xy(11, 38);
	public static final int HEROICLEAP = xy(15, 37);
	public static final int SMOKEBOMB = xy(16, 37);
	public static final int SPECTRALBLADES = xy(1, 38);
	public static final int SURPRISE = xy(2, 38);
	public static final int ALCHEMY_BAG = xy(8, 36);
	public static final int AVARITIA_HOOK = xy(9, 36);
	public static final int BASIC_HOOK = xy(10, 36);
	public static final int BIGGER_GAMBLE_BAG = xy(11, 36);
	public static final int BURNT_BAG = xy(12, 36);
	public static final int CHAOS_HOOK = xy(13, 36);
	public static final int CHEEST = xy(14, 36);
	public static final int DARK_CHEST = xy(15, 36);
	public static final int DK_BAG = xy(16, 36);
	public static final int DM300_BAG = xy(1, 37);
	public static final int ENDERIUM = xy(2, 37);
	public static final int ENERGITE = xy(3, 37);
	public static final int GAMBLE_BAG = xy(4, 37);
	public static final int GOLDEN_HOOK = xy(5, 37);
	public static final int GOO_BAG = xy(6, 37);
	public static final int IDEAL_BAG = xy(7, 37);
	public static final int NEUTRONIUM_HOOK = xy(8, 37);
	public static final int OSMIRIDIUM = xy(9, 37);
	public static final int PLUTONIUM = xy(10, 37);
	public static final int PSYCHE_CHEST = xy(11, 37);
	public static final int QUALITY_BAG = xy(12, 37);
	public static final int SPIRITUAL_HOOK = xy(13, 37);
	public static final int TENGU_BAG = xy(14, 37);
	public static final int FIREBOOSTER = xy(6, 36);
	public static final int RESPAWN = xy(7, 36);
	public static final int AMULET_WATER = xy(3, 35);
	public static final int BLINK = xy(4, 35);
	public static final int BROWNDRAGONCRYSTAL = xy(5, 35);
	public static final int BUBBLESHIELD = xy(6, 35);
	public static final int ENERGIZE = xy(7, 35);
	public static final int FOODBAG = xy(8, 35);
	public static final int GREED = xy(9, 35);
	public static final int HARP = xy(10, 35);
	public static final int LIGHTBLUEDRAGONCRYSTAL = xy(11, 35);
	public static final int LIGHTGREENDRAGONCRYSTAL = xy(12, 35);
	public static final int LUCKYBADGE = xy(13, 35);
	public static final int MOLTENEARTH = xy(14, 35);
	public static final int POISONBURST = xy(15, 35);
	public static final int RAISEDEAD = xy(16, 35);
	public static final int REDDRAGONCRYSTAL = xy(1, 36);
	public static final int SOUL = xy(2, 36);
	public static final int SPELLBOOK = xy(3, 36);
	public static final int TELEKINESIS = xy(4, 36);
	public static final int WATERPUMP = xy(5, 36);
	public static final int CHAINSAW_HAND = xy(9, 34);
	public static final int INSCRIBED_KINFE = xy(10, 34);
	public static final int LORSIONSGREATAXE = xy(11, 34);
	public static final int LOTURGOS_CRYSTAL = xy(12, 34);
	public static final int MARACARS_BLADES = xy(13, 34);
	public static final int NAHUSSWORD = xy(14, 34);
	public static final int NEPTUNES_TRIDENT = xy(15, 34);
	public static final int RA_ROTHS_NUNCHUCKS = xy(16, 34);
	public static final int THONOTHS_AXE = xy(1, 35);
	public static final int ADORNEDDRAGONCRYSTAL = xy(2, 35);
	public static final int ABYSSALBOTTLE = xy(16, 33);
	public static final int ABYSSALCURE = xy(1, 34);
	public static final int ABYSSALFRUIT = xy(2, 34);
	public static final int AQUA_BLAST = xy(3, 34);
	public static final int FEATHER_FALL = xy(4, 34);
	public static final int POTION_CATALYST = xy(5, 34);
	public static final int PUDDING = xy(6, 34);
	public static final int SCROLL_CATALYST = xy(7, 34);
	public static final int STONE_AFFECTION = xy(8, 34);
	public static final int CLAW = xy(15, 33);
	public static final int ARMOR_RAT_KING = xy(2, 33);
	public static final int CHEESE = xy(3, 33);
	public static final int CHEESE_CHUNK = xy(4, 33);
	public static final int IDENTIFY_BOMB = xy(5, 33);
	public static final int KING_BLADE = xy(6, 33);
	public static final int NEW_TENGU_BOMB = xy(7, 33);
	public static final int RUSTY_SHIELD = xy(8, 33);
	public static final int SHOCK_BOMB = xy(9, 33);
	public static final int STONE_DISARM = xy(10, 33);
	public static final int SUPER_PICKAXE = xy(11, 33);
	public static final int VAMPIRISM = xy(12, 33);
	public static final int WAND_AVALANCHE = xy(13, 33);
	public static final int WAND_UNSTABLE = xy(14, 33);
	public static final int FLUTE = xy(16, 19);
	public static final int SLEEP_DART = xy(1, 33);
	public static final int CREATIVE_GLOVES = xy(10, 19);
	public static final int FIRINGWHIP = xy(11, 19);
	public static final int GREEDSTAFF = xy(12, 19);
	public static final int GROWTHSWORD = xy(13, 19);
	public static final int PHANTDAGGER = xy(14, 19);
	public static final int STARHAMMER = xy(15, 19);
	public static final int GLOCK = xy(14, 13);
	public static final int GLOCK_BULLET = xy(15, 13);
	public static final int ARROW_SIGN = xy(16, 13);
	public static final int STORMBRINGER_BATTLE_AXE = xy(2, 19);
	public static final int EMERALD_ROUND_SHIELD = xy(3, 19);
	public static final int PURGE_GLOVES = xy(4, 19);
	public static final int DARKNESS_KATANA = xy(5, 19);
	public static final int BLOODLETTER_SICKLE = xy(6, 19);
	public static final int AMETHYST_SPEAR = xy(7, 19);
	public static final int MIDAS_GREAT_SWORD = xy(8, 19);
	public static final int FIRECRACKER_WHIP = xy(9, 19);
	public static final int ROYAL_SEAL = MOD_ITEMS+2;
	public static final int WAND_MALAISE = MOD_ITEMS+3;
	public static final int RING_ENDOTHERMIC = MOD_ITEMS+4;
	public static final int ARTIFACT_THORNCLOAK = MOD_ITEMS+5;
	public static final int ELIXIR_WILD = MOD_ITEMS+6;
	public static final int MEAL = MOD_ITEMS+7;
	public static final int WYVERNFRUIT = MOD_ITEMS+8;
	public static final int BLOCK = MOD_ITEMS+9;
	public static final int SPLINTER = MOD_ITEMS+10;
	public static final int ESSENCE = MOD_ITEMS+11;
	public static final int RUBY = MOD_ITEMS+12;
	static{
		assignItemRect(WARPICK, 15, 14);
		assignItemRect(WOODEN_BOOMERANG, 12, 12);
		assignItemRect(MILK_CHEESE, 15, 11);
		assignItemRect(HOWARD_CHEST, 16, 14);
		assignItemRect(EVAN_CHEST, 16, 14);
		assignItemRect(GOLD_CHEST, 16, 14);
		assignItemRect(HEAVY_CROSSBOW, 14, 14);
		assignItemRect(WEIGHT, 14, 12);
		assignItemRect(CHAIN, 15, 16);
		assignItemRect(ARMOR_DRAGON_KNIGHT, 15, 13);
		assignItemRect(ARMOR_VIKING, 11, 13);
		assignItemRect(CURARE_DART, 15, 15);
		assignItemRect(DRAGON_DAGGER, 13, 13);
		assignItemRect(KNUCKLEDUSTER, 15, 10);
		assignItemRect(VIKING_MACE, 16, 15);
		assignItemRect(WAND_VENOM, 14, 14);
		assignItemRect(BAN_HAMMER, 16, 16);
		assignItemRect(BONE_CLAYMORE, 16, 16);
		assignItemRect(BONE_MACHETE, 15, 16);
		assignItemRect(DUAL_HATCHET, 14, 14);
		assignItemRect(EXOTIC_BLESSING, 12, 13);
		assignItemRect(EXOTIC_DISCOVERIES, 15, 14);
		assignItemRect(EXOTIC_GOLEMS, 15, 14);
		assignItemRect(EXOTIC_RATLEGION, 15, 14);
		assignItemRect(GOLDEN_SWORD, 14, 14);
		assignItemRect(INFIPIKE, 11, 10);
		assignItemRect(RED_KNIFE, 14, 14);
		assignItemRect(RING_LETHALITY, 8, 10);
		assignItemRect(SCROLL_ANIMATION, 15, 14);
		assignItemRect(SCROLL_DROWSINESS, 15, 14);
		assignItemRect(STONE_HAMMER, 13, 13);
		assignItemRect(TRISAI, 13, 13);
		assignItemRect(WAND_SNAKES, 14, 14);
		assignItemRect(BOW, 14, 14);
		assignItemRect(PLANT_ITEMS_BLANDFRUIT, 16, 16);
		assignItemRect(BLANDFRUIT_HERB, 15, 12);
		assignItemRect(PLANT_ITEMS_BLINDWEED, 15, 14);
		assignItemRect(BLINDWEED_HERB, 15, 12);
		assignItemRect(PLANT_ITEMS_DEWCATCHER, 16, 16);
		assignItemRect(DEWCATCHER_HERB, 15, 12);
		assignItemRect(PLANT_ITEMS_DREAMFOIL, 16, 13);
		assignItemRect(DREAMFOIL_HERB, 15, 12);
		assignItemRect(PLANT_ITEMS_EARTHROOT, 15, 15);
		assignItemRect(EARTHROOT_HERB, 15, 12);
		assignItemRect(PLANT_ITEMS_FADELEAF, 15, 14);
		assignItemRect(FADELEAF_HERB, 15, 12);
		assignItemRect(PLANT_ITEMS_FIREBLOOM, 15, 14);
		assignItemRect(FIREBLOOM_HERB, 15, 12);
		assignItemRect(PLANT_ITEMS_ICECAP, 15, 14);
		assignItemRect(ICECAP_HERB, 15, 12);
		assignItemRect(PLANT_ITEMS_ROTBERRY, 16, 16);
		assignItemRect(ROTBERRY_HERB, 15, 12);
		assignItemRect(PLANT_ITEMS_SEEDPOD, 16, 15);
		assignItemRect(SEEDPOD_HERB, 15, 12);
		assignItemRect(PLANT_ITEMS_SORROWMOSS, 16, 14);
		assignItemRect(SORROWMOSS_HERB, 15, 12);
		assignItemRect(PLANT_ITEMS_STARFLOWER, 14, 16);
		assignItemRect(STARFLOWER_HERB, 15, 12);
		assignItemRect(PLANT_ITEMS_STEAMWEED, 16, 16);
		assignItemRect(STEAMWEED_HERB, 15, 12);
		assignItemRect(PLANT_ITEMS_STORMVINE, 16, 16);
		assignItemRect(STORMVINE_HERB, 15, 12);
		assignItemRect(PLANT_ITEMS_SUNGRASS, 16, 16);
		assignItemRect(SUNGRASS_HERB, 15, 12);
		assignItemRect(AXE, 12, 10);
		assignItemRect(CALIBER_PISTOL, 11, 16);
		assignItemRect(CHAKRAM, 14, 14);
		assignItemRect(ELDERSIGN, 15, 14);
		assignItemRect(GIANT_SHURIKEN, 16, 16);
		assignItemRect(GOLDEN_PISTOL, 12, 16);
		assignItemRect(HALBERD, 16, 16);
		assignItemRect(HARPOON, 14, 14);
		assignItemRect(KNIFEGLOVE, 14, 14);
		assignItemRect(KUSARIGAMA, 13, 14);
		assignItemRect(LEAD_KEY, 8, 14);
		assignItemRect(LONG_RIFLE, 13, 16);
		assignItemRect(LUGER, 10, 13);
		assignItemRect(PISTOL, 11, 14);
		assignItemRect(PLATIN_KEY, 8, 14);
		assignItemRect(PRIMITIVESPEAR, 14, 14);
		assignItemRect(RIFLE, 13, 16);
		assignItemRect(SCISSORS, 11, 14);
		assignItemRect(SCYTHE, 13, 15);
		assignItemRect(SEED_DREAMFOIL, 10, 10);
		assignItemRect(SEED_STEAMWEED, 10, 10);
		assignItemRect(SHORT_SWORD, 13, 13);
		assignItemRect(SHOTGUN, 16, 16);
		assignItemRect(SILVER_PISTOL, 13, 16);
		assignItemRect(SPIKEDSWORD, 16, 16);
		assignItemRect(TINCTUREBOTTLE, 8, 12);
		assignItemRect(TITANIUM_KEY, 8, 14);
		assignItemRect(TNT, 12, 11);
		assignItemRect(TNT_BUNDLE, 15, 14);
		assignItemRect(WOODEN_KEY, 8, 14);
		assignItemRect(YELLOWSIGN, 13, 16);
		assignItemRect(ARMOR_DM400, 14, 15);
		assignItemRect(ARMOR_FREEMAN, 15, 13);
		assignItemRect(ARMOR_FRIAR, 15, 16);
		assignItemRect(ARMOR_NINJA, 14, 13);
		assignItemRect(ARMOR_SLIME, 14, 13);
		assignItemRect(ARMOR_SLIMEGIRL, 12, 11);
		assignItemRect(BOTTLEDENERGY, 10, 12);
		assignItemRect(BROKEN_PACKAGE, 15, 11);
		assignItemRect(BROKEN_TOOL, 14, 13);
		assignItemRect(BRONZE_WATCH, 13, 15);
		assignItemRect(GOLD_INGOT, 15, 12);
		assignItemRect(MAGIC_FEATHER, 15, 15);
		assignItemRect(MATCHAPUDDING, 14, 13);
		assignItemRect(RED_ENVELOPE, 14, 14);
		assignItemRect(ROYAL_JEWELRY, 10, 10);
		assignItemRect(SMALLSKURIKEN, 10, 10);
		assignItemRect(SPEAR_SHIELD, 16, 14);
		assignItemRect(TINCTURE, 8, 16);
		assignItemRect(BLOWPIPE, 15, 15);
		assignItemRect(BRACER, 11, 13);
		assignItemRect(LUCKYCOIN, 13, 13);
		assignItemRect(LWHIP, 14, 14);
		assignItemRect(SLIMEBALL, 11, 13);
		assignItemRect(SPEARCONQUEROR, 15, 15);
		assignItemRect(STEEL_WHIP, 14, 14);
		assignItemRect(TAMARU, 8, 8);
		assignItemRect(TATTEKI, 14, 16);
		assignItemRect(WAKIZASHI, 12, 16);
		assignItemRect(WALK_STICK, 14, 16);
		assignItemRect(HELMET_HOLDER, 13, 14);
		assignItemRect(HELMET_CRUSADER, 15, 16);
		assignItemRect(HELMET_BARBARIAN, 16, 16);
		assignItemRect(HELMET_APPRENTICE, 15, 16);
		assignItemRect(HELMET_EMERALD, 15, 13);
		assignItemRect(DWARF_CROWN, 15, 13);
		assignItemRect(HEADDRESS_OF_REGENERATION, 14, 16);
		assignItemRect(WIZARD_HAT, 16, 15);
		assignItemRect(HELMET_CLOWN, 15, 15);
		assignItemRect(HELMET_HORROR, 15, 15);
		assignItemRect(HELMET_RANGER, 16, 16);
		assignItemRect(TURTLE_SCARF_BLUE, 16, 13);
		assignItemRect(LITTLE_PAIL, 16, 15);
		assignItemRect(RIDER_MASK, 15, 16);
		assignItemRect(HELMET_GUARD, 14, 16);
		assignItemRect(STRAW_HAT, 16, 13);
		assignItemRect(MASK_OF_MADNESS, 13, 13);
		assignItemRect(ARMOR_SUMMONER, 16, 16);
		assignItemRect(EYE_OF_WISDOM, 14, 12);
		assignItemRect(SUMMONER_WHIP, 16, 16);
		assignItemRect(TRAINING_WHIP, 14, 14);
		assignItemRect(SEED_COFFEE, 10, 10);
		assignItemRect(SEED_POTATO, 10, 10);
		assignItemRect(SEED_TOMATO, 10, 10);
		assignItemRect(POTION_RUSSET, 12, 14);
		assignItemRect(POTION_MAROON, 12, 14);
		assignItemRect(POTION_SEPIA, 12, 14);
		assignItemRect(POTION_LILAC, 12, 14);
		assignItemRect(POTION_OLIVE, 12, 14);
		assignItemRect(SCROLL_EIHWAZ, 15, 14);
		assignItemRect(RAT, 16, 8);
		assignItemRect(ALBUM_BEAUTIFUL, 11, 11);
		assignItemRect(ALBUM_CHAIR, 11, 11);
		assignItemRect(ALBUM_FINNISH, 11, 11);
		assignItemRect(ALBUM_HOG, 11, 11);
		assignItemRect(ALBUM_NONSENSE, 11, 11);
		assignItemRect(ALBUM_POP, 11, 11);
		assignItemRect(ALBUM_RAT, 11, 11);
		assignItemRect(ALBUM_REMIX, 11, 11);
		assignItemRect(ALBUM_SEA, 11, 11);
		assignItemRect(ALBUM_SYNTH, 11, 11);
		assignItemRect(ALBUM_TWINGO, 11, 11);
		assignItemRect(ARMADILLO, 16, 10);
		assignItemRect(ARMOR_MALE, 14, 12);
		assignItemRect(ARMOR_POST, 14, 12);
		assignItemRect(ARTIFACT_MP3_PLAYER_1, 13, 16);
		assignItemRect(ARTIFACT_MP3_PLAYER_2, 13, 16);
		assignItemRect(BAKED_FISH, 16, 16);
		assignItemRect(BASED_CAKE, 15, 16);
		assignItemRect(BERRY_BALLS, 16, 16);
		assignItemRect(BERRY_CAKE, 15, 16);
		assignItemRect(BIGBAG, 14, 15);
		assignItemRect(BRANCH, 9, 12);
		assignItemRect(BREAD, 16, 12);
		assignItemRect(BRUSCHETTA, 14, 14);
		assignItemRect(CAKE_BASE, 15, 14);
		assignItemRect(CAR, 16, 8);
		assignItemRect(CHEESE_SLICE, 8, 7);
		assignItemRect(CHOCOLATE_CAKE, 15, 15);
		assignItemRect(COFFEE, 11, 12);
		assignItemRect(COFFEE_BEAN, 10, 11);
		assignItemRect(COMPLETE_BREAKFAST, 16, 16);
		assignItemRect(COOKED_WARRIOR, 14, 14);
		assignItemRect(COOKIES, 14, 14);
		assignItemRect(CRISPY_BASS, 16, 16);
		assignItemRect(DANISH_PASTRY, 15, 10);
		assignItemRect(EGG, 10, 12);
		assignItemRect(FISH, 13, 10);
		assignItemRect(FLOUR, 12, 16);
		assignItemRect(FOFFS, 10, 11);
		assignItemRect(FOODCRATE, 12, 14);
		assignItemRect(FRIED_EGG, 16, 16);
		assignItemRect(FRIED_RAT, 16, 8);
		assignItemRect(GOBLIN_CURSE, 12, 13);
		assignItemRect(GOLDAMULET, 12, 14);
		assignItemRect(GOLDBAR, 16, 14);
		assignItemRect(GOLDENMEAT, 15, 11);
		assignItemRect(GREATGREATSWORD, 16, 16);
		assignItemRect(HASHBROWNS, 16, 16);
		assignItemRect(ICE_CREAM, 9, 15);
		assignItemRect(MEATBALLS, 16, 16);
		assignItemRect(MILK, 8, 15);
		assignItemRect(NUTS, 10, 8);
		assignItemRect(OIL, 12, 14);
		assignItemRect(OMELET, 16, 16);
		assignItemRect(PAINTING_MANA_LISA, 16, 16);
		assignItemRect(PAINTING_MEAT, 16, 16);
		assignItemRect(PANCAKES, 16, 16);
		assignItemRect(PANCAKES_CHEESE, 16, 16);
		assignItemRect(PIETHON, 16, 12);
		assignItemRect(PILE_OF_SAIS, 16, 16);
		assignItemRect(PILLOW, 14, 12);
		assignItemRect(PINKAMULET, 12, 14);
		assignItemRect(PIZZA, 16, 16);
		assignItemRect(PLATE_CARRIER, 13, 14);
		assignItemRect(POTATO, 14, 12);
		assignItemRect(PUG, 15, 15);
		assignItemRect(RATATOUILLE, 16, 12);
		assignItemRect(SASHIMI, 14, 14);
		assignItemRect(SAW, 16, 15);
		assignItemRect(SILVERAMULET, 12, 14);
		assignItemRect(SILVER_KNIFE, 9, 9);
		assignItemRect(SMALLBAG, 10, 11);
		assignItemRect(SMALLFROZEN, 10, 8);
		assignItemRect(SMALLGREATSWORD, 11, 11);
		assignItemRect(SMALLMEAT, 10, 8);
		assignItemRect(SMALLSTEAK, 10, 8);
		assignItemRect(SPAGHETTI, 16, 16);
		assignItemRect(SPARROW, 14, 11);
		assignItemRect(SPEARAXE, 14, 14);
		assignItemRect(SPRAYPAINT, 7, 13);
		assignItemRect(STEWEDSMALL, 10, 8);
		assignItemRect(STIR_FRY, 16, 16);
		assignItemRect(STONEAMULET, 12, 14);
		assignItemRect(STONE_REFORGE, 14, 12);
		assignItemRect(SUGAR, 12, 13);
		assignItemRect(TIDEPOD, 6, 6);
		assignItemRect(TOMATO, 12, 13);
		assignItemRect(TRIPLE_SHOT_ESPRESSO, 15, 14);
		assignItemRect(UNTAXEDFOOD, 16, 12);
		assignItemRect(WARRIORIUM, 10, 8);
		assignItemRect(WAXBAR, 6, 8);
		assignItemRect(WHEAT, 16, 16);
		assignItemRect(WILLOW, 15, 16);
		assignItemRect(MARKING_ROCK, 15, 12);
		assignItemRect(ARCANE_ROOT, 11, 12);
		assignItemRect(CRYSTAL_APPLE, 9, 11);
		assignItemRect(LARGE_RADISH, 9, 14);
		assignItemRect(VIAL_SHARD, 9, 12);
		assignItemRect(ARMOR_ZEALOT, 15, 15);
		assignItemRect(ARTIFACT_VIAL1, 13, 14);
		assignItemRect(ARTIFACT_VIAL2, 13, 14);
		assignItemRect(ARTIFACT_VIAL3, 13, 14);
		assignItemRect(CHAINS, 15, 16);
		assignItemRect(ICYCORE, 11, 14);
		assignItemRect(WAND_EXPLOSION, 14, 14);
		assignItemRect(WAND_OF_ELEMENTS, 15, 15);
		assignItemRect(WAND_OF_FIREBALL, 14, 14);
		assignItemRect(ARMOR_ARTIFICER, 14, 13);
		assignItemRect(ARTIFACT_RINGBOX, 16, 13);
		assignItemRect(CRACKED_GUN, 13, 13);
		assignItemRect(ELIXIR_CLAWS, 12, 14);
		assignItemRect(GUN, 12, 14);
		assignItemRect(LUCKY_DICE, 16, 16);
		assignItemRect(STONE_BLIGHT, 14, 12);
		assignItemRect(WANDERERS_MAP, 16, 16);
		assignItemRect(WAND_DISPLACEMENT, 14, 14);
		assignItemRect(HEROICLEAP, 14, 16);
		assignItemRect(SMOKEBOMB, 14, 16);
		assignItemRect(SPECTRALBLADES, 14, 16);
		assignItemRect(SURPRISE, 14, 16);
		assignItemRect(ALCHEMY_BAG, 14, 15);
		assignItemRect(AVARITIA_HOOK, 14, 13);
		assignItemRect(BASIC_HOOK, 14, 13);
		assignItemRect(BIGGER_GAMBLE_BAG, 14, 15);
		assignItemRect(BURNT_BAG, 14, 15);
		assignItemRect(CHAOS_HOOK, 14, 13);
		assignItemRect(CHEEST, 16, 14);
		assignItemRect(DARK_CHEST, 16, 14);
		assignItemRect(DK_BAG, 14, 15);
		assignItemRect(DM300_BAG, 14, 15);
		assignItemRect(ENDERIUM, 10, 10);
		assignItemRect(ENERGITE, 12, 14);
		assignItemRect(GAMBLE_BAG, 14, 15);
		assignItemRect(GOLDEN_HOOK, 14, 13);
		assignItemRect(GOO_BAG, 14, 15);
		assignItemRect(IDEAL_BAG, 14, 15);
		assignItemRect(NEUTRONIUM_HOOK, 14, 13);
		assignItemRect(OSMIRIDIUM, 10, 11);
		assignItemRect(PLUTONIUM, 12, 10);
		assignItemRect(PSYCHE_CHEST, 16, 14);
		assignItemRect(QUALITY_BAG, 14, 15);
		assignItemRect(SPIRITUAL_HOOK, 14, 13);
		assignItemRect(TENGU_BAG, 14, 15);
		assignItemRect(FIREBOOSTER, 10, 15);
		assignItemRect(RESPAWN, 11, 11);
		assignItemRect(AMULET_WATER, 16, 15);
		assignItemRect(BLINK, 14, 16);
		assignItemRect(BROWNDRAGONCRYSTAL, 15, 16);
		assignItemRect(BUBBLESHIELD, 14, 16);
		assignItemRect(ENERGIZE, 14, 16);
		assignItemRect(FOODBAG, 16, 15);
		assignItemRect(GREED, 14, 16);
		assignItemRect(HARP, 16, 15);
		assignItemRect(LIGHTBLUEDRAGONCRYSTAL, 15, 16);
		assignItemRect(LIGHTGREENDRAGONCRYSTAL, 15, 16);
		assignItemRect(LUCKYBADGE, 14, 12);
		assignItemRect(MOLTENEARTH, 14, 16);
		assignItemRect(POISONBURST, 14, 16);
		assignItemRect(RAISEDEAD, 14, 16);
		assignItemRect(REDDRAGONCRYSTAL, 15, 16);
		assignItemRect(SOUL, 12, 11);
		assignItemRect(SPELLBOOK, 14, 16);
		assignItemRect(TELEKINESIS, 14, 16);
		assignItemRect(WATERPUMP, 14, 16);
		assignItemRect(CHAINSAW_HAND, 13, 13);
		assignItemRect(INSCRIBED_KINFE, 13, 15);
		assignItemRect(LORSIONSGREATAXE, 15, 16);
		assignItemRect(LOTURGOS_CRYSTAL, 10, 15);
		assignItemRect(MARACARS_BLADES, 16, 15);
		assignItemRect(NAHUSSWORD, 14, 16);
		assignItemRect(NEPTUNES_TRIDENT, 16, 15);
		assignItemRect(RA_ROTHS_NUNCHUCKS, 16, 16);
		assignItemRect(THONOTHS_AXE, 16, 16);
		assignItemRect(ADORNEDDRAGONCRYSTAL, 15, 15);
		assignItemRect(ABYSSALBOTTLE, 10, 13);
		assignItemRect(ABYSSALCURE, 9, 13);
		assignItemRect(ABYSSALFRUIT, 10, 12);
		assignItemRect(AQUA_BLAST, 11, 16);
		assignItemRect(FEATHER_FALL, 14, 12);
		assignItemRect(POTION_CATALYST, 11, 15);
		assignItemRect(PUDDING, 16, 13);
		assignItemRect(SCROLL_CATALYST, 11, 13);
		assignItemRect(STONE_AFFECTION, 14, 12);
		assignItemRect(CLAW, 12, 10);
		assignItemRect(ARMOR_RAT_KING, 12, 6);
		assignItemRect(CHEESE, 16, 12);
		assignItemRect(CHEESE_CHUNK, 10, 8);
		assignItemRect(IDENTIFY_BOMB, 14, 16);
		assignItemRect(KING_BLADE, 15, 15);
		assignItemRect(NEW_TENGU_BOMB, 9, 15);
		assignItemRect(RUSTY_SHIELD, 12, 14);
		assignItemRect(SHOCK_BOMB, 10, 13);
		assignItemRect(STONE_DISARM, 14, 12);
		assignItemRect(SUPER_PICKAXE, 14, 14);
		assignItemRect(VAMPIRISM, 8, 16);
		assignItemRect(WAND_AVALANCHE, 14, 14);
		assignItemRect(WAND_UNSTABLE, 14, 14);
		assignItemRect(FLUTE, 14, 14);
		assignItemRect(SLEEP_DART, 15, 13);
		assignItemRect(CREATIVE_GLOVES, 12, 16);
		assignItemRect(FIRINGWHIP, 14, 14);
		assignItemRect(GREEDSTAFF, 16, 16);
		assignItemRect(GROWTHSWORD, 15, 16);
		assignItemRect(PHANTDAGGER, 12, 12);
		assignItemRect(STARHAMMER, 16, 16);
		assignItemRect(GLOCK, 16, 15);
		assignItemRect(GLOCK_BULLET, 11, 11);
		assignItemRect(ARROW_SIGN, 16, 16);
		assignItemRect(STORMBRINGER_BATTLE_AXE, 16, 16);
		assignItemRect(EMERALD_ROUND_SHIELD, 16, 16);
		assignItemRect(PURGE_GLOVES, 12, 16);
		assignItemRect(DARKNESS_KATANA, 15, 16);
		assignItemRect(BLOODLETTER_SICKLE, 15, 15);
		assignItemRect(AMETHYST_SPEAR, 16, 16);
		assignItemRect(MIDAS_GREAT_SWORD, 16, 16);
		assignItemRect(FIRECRACKER_WHIP, 14, 14);
		assignItemRect(ROYAL_SEAL, 13, 13);
		assignItemRect(WAND_MALAISE, 14, 14);
		assignItemRect(RING_ENDOTHERMIC, 8, 11);
		assignItemRect(ARTIFACT_THORNCLOAK, 16, 14);
		assignItemRect(ELIXIR_WILD, 12, 14);
		assignItemRect(MEAL, 16, 14);
		assignItemRect(WYVERNFRUIT, 12, 13);
		assignItemRect(BLOCK, 11, 12);
		assignItemRect(SPLINTER, 6, 12);
		assignItemRect(ESSENCE, 14, 13);
		assignItemRect(RUBY, 11, 10);
	}

	private static final int ARMOR          =                               xy(1, 12);  //16 slots
	public static final int ARMOR_CLOTH     = ARMOR+0;
	public static final int ARMOR_LEATHER   = ARMOR+1;
	public static final int ARMOR_MAIL      = ARMOR+2;
	public static final int ARMOR_SCALE     = ARMOR+3;
	public static final int ARMOR_PLATE     = ARMOR+4;
	public static final int ARMOR_WARRIOR   = ARMOR+5;
	public static final int ARMOR_MAGE      = ARMOR+6;
	public static final int ARMOR_ROGUE     = ARMOR+7;
	public static final int ARMOR_HUNTRESS  = ARMOR+8;
	public static final int ARMOR_DUELIST   = ARMOR+9;
	public static final int ARMOR_CLERIC    = ARMOR+10;
	static{
		assignItemRect(ARMOR_CLOTH,     15, 12);
		assignItemRect(ARMOR_LEATHER,   14, 13);
		assignItemRect(ARMOR_MAIL,      14, 12);
		assignItemRect(ARMOR_SCALE,     14, 11);
		assignItemRect(ARMOR_PLATE,     12, 12);
		assignItemRect(ARMOR_WARRIOR,   12, 12);
		assignItemRect(ARMOR_MAGE,      15, 15);
		assignItemRect(ARMOR_ROGUE,     14, 12);
		assignItemRect(ARMOR_HUNTRESS,  13, 15);
		assignItemRect(ARMOR_DUELIST,   12, 13);
		assignItemRect(ARMOR_CLERIC,    13, 14);
	}

	                                                                                    //16 free slots

	private static final int WANDS              =                           xy(1, 14);  //16 slots
	public static final int WAND_MAGIC_MISSILE  = WANDS+0;
	public static final int WAND_FIREBOLT       = WANDS+1;
	public static final int WAND_FROST          = WANDS+2;
	public static final int WAND_LIGHTNING      = WANDS+3;
	public static final int WAND_DISINTEGRATION = WANDS+4;
	public static final int WAND_PRISMATIC_LIGHT= WANDS+5;
	public static final int WAND_CORROSION      = WANDS+6;
	public static final int WAND_LIVING_EARTH   = WANDS+7;
	public static final int WAND_BLAST_WAVE     = WANDS+8;
	public static final int WAND_CORRUPTION     = WANDS+9;
	public static final int WAND_WARDING        = WANDS+10;
	public static final int WAND_REGROWTH       = WANDS+11;
	public static final int WAND_TRANSFUSION    = WANDS+12;
	static {
		for (int i = WANDS; i < WANDS+16; i++)
			assignItemRect(i, 14, 14);
	}

	private static final int RINGS          =                               xy(1, 15);  //16 slots
	public static final int RING_GARNET     = RINGS+0;
	public static final int RING_RUBY       = RINGS+1;
	public static final int RING_TOPAZ      = RINGS+2;
	public static final int RING_EMERALD    = RINGS+3;
	public static final int RING_ONYX       = RINGS+4;
	public static final int RING_OPAL       = RINGS+5;
	public static final int RING_TOURMALINE = RINGS+6;
	public static final int RING_SAPPHIRE   = RINGS+7;
	public static final int RING_AMETHYST   = RINGS+8;
	public static final int RING_QUARTZ     = RINGS+9;
	public static final int RING_AGATE      = RINGS+10;
	public static final int RING_DIAMOND    = RINGS+11;
	static {
		for (int i = RINGS; i < RINGS+16; i++)
			assignItemRect(i, 8, 10);
	}

	private static final int ARTIFACTS          =                            xy(1, 16);  //32 slots
	public static final int ARTIFACT_CLOAK      = ARTIFACTS+0;
	public static final int ARTIFACT_ARMBAND    = ARTIFACTS+1;
	public static final int ARTIFACT_CAPE       = ARTIFACTS+2;
	public static final int ARTIFACT_TALISMAN   = ARTIFACTS+3;
	public static final int ARTIFACT_HOURGLASS  = ARTIFACTS+4;
	public static final int ARTIFACT_TOOLKIT    = ARTIFACTS+5;
	public static final int ARTIFACT_SPELLBOOK  = ARTIFACTS+6;
	public static final int ARTIFACT_BEACON     = ARTIFACTS+7;
	public static final int ARTIFACT_CHAINS     = ARTIFACTS+8;
	public static final int ARTIFACT_HORN1      = ARTIFACTS+9;
	public static final int ARTIFACT_HORN2      = ARTIFACTS+10;
	public static final int ARTIFACT_HORN3      = ARTIFACTS+11;
	public static final int ARTIFACT_HORN4      = ARTIFACTS+12;
	public static final int ARTIFACT_CHALICE1   = ARTIFACTS+13;
	public static final int ARTIFACT_CHALICE2   = ARTIFACTS+14;
	public static final int ARTIFACT_CHALICE3   = ARTIFACTS+15;
	public static final int ARTIFACT_SANDALS    = ARTIFACTS+16;
	public static final int ARTIFACT_SHOES      = ARTIFACTS+17;
	public static final int ARTIFACT_BOOTS      = ARTIFACTS+18;
	public static final int ARTIFACT_GREAVES    = ARTIFACTS+19;
	public static final int ARTIFACT_ROSE1      = ARTIFACTS+20;
	public static final int ARTIFACT_ROSE2      = ARTIFACTS+21;
	public static final int ARTIFACT_ROSE3      = ARTIFACTS+22;
	public static final int ARTIFACT_TOME       = ARTIFACTS+23;
	public static final int ARTIFACT_KEY        = ARTIFACTS+24;
	static{
		assignItemRect(ARTIFACT_CLOAK,      9,  15);
		assignItemRect(ARTIFACT_ARMBAND,    16, 13);
		assignItemRect(ARTIFACT_CAPE,       16, 14);
		assignItemRect(ARTIFACT_TALISMAN,   15, 13);
		assignItemRect(ARTIFACT_HOURGLASS,  13, 16);
		assignItemRect(ARTIFACT_TOOLKIT,    15, 13);
		assignItemRect(ARTIFACT_SPELLBOOK,  13, 16);
		assignItemRect(ARTIFACT_BEACON,     16, 16);
		assignItemRect(ARTIFACT_CHAINS,     16, 16);
		assignItemRect(ARTIFACT_HORN1,      15, 15);
		assignItemRect(ARTIFACT_HORN2,      15, 15);
		assignItemRect(ARTIFACT_HORN3,      15, 15);
		assignItemRect(ARTIFACT_HORN4,      15, 15);
		assignItemRect(ARTIFACT_CHALICE1,   12, 15);
		assignItemRect(ARTIFACT_CHALICE2,   12, 15);
		assignItemRect(ARTIFACT_CHALICE3,   12, 15);
		assignItemRect(ARTIFACT_SANDALS,    16, 6 );
		assignItemRect(ARTIFACT_SHOES,      16, 6 );
		assignItemRect(ARTIFACT_BOOTS,      16, 9 );
		assignItemRect(ARTIFACT_GREAVES,    16, 14);
		assignItemRect(ARTIFACT_ROSE1,      14, 14);
		assignItemRect(ARTIFACT_ROSE2,      14, 14);
		assignItemRect(ARTIFACT_ROSE3,      14, 14);
		assignItemRect(ARTIFACT_TOME,       14, 16);
		assignItemRect(ARTIFACT_KEY,        8,  16);
	}

	private static final int TRINKETS        =                               xy(1, 18);  //32 slots
	public static final int RAT_SKULL       = TRINKETS+0;
	public static final int PARCHMENT_SCRAP = TRINKETS+1;
	public static final int PETRIFIED_SEED  = TRINKETS+2;
	public static final int EXOTIC_CRYSTALS = TRINKETS+3;
	public static final int MOSSY_CLUMP     = TRINKETS+4;
	public static final int SUNDIAL         = TRINKETS+5;
	public static final int CLOVER          = TRINKETS+6;
	public static final int TRAP_MECHANISM  = TRINKETS+7;
	public static final int MIMIC_TOOTH     = TRINKETS+8;
	public static final int WONDROUS_RESIN  = TRINKETS+9;
	public static final int EYE_OF_NEWT     = TRINKETS+10;
	public static final int SALT_CUBE       = TRINKETS+11;
	public static final int BLOOD_VIAL      = TRINKETS+12;
	public static final int OBLIVION_SHARD  = TRINKETS+13;
	public static final int CHAOTIC_CENSER  = TRINKETS+14;
	public static final int FERRET_TUFT     = TRINKETS+15;
	public static final int SPYGLASS        = TRINKETS+16;
	static{
		assignItemRect(RAT_SKULL,       16, 11);
		assignItemRect(PARCHMENT_SCRAP, 10, 14);
		assignItemRect(PETRIFIED_SEED,   9,  9);
		assignItemRect(EXOTIC_CRYSTALS, 15, 13);
		assignItemRect(MOSSY_CLUMP,     12, 11);
		assignItemRect(SUNDIAL,         16, 12);
		assignItemRect(CLOVER,          11, 15);
		assignItemRect(TRAP_MECHANISM,  13, 15);
		assignItemRect(MIMIC_TOOTH,     8,  15);
		assignItemRect(WONDROUS_RESIN,  12, 11);
		assignItemRect(EYE_OF_NEWT,     12, 12);
		assignItemRect(SALT_CUBE,       12, 13);
		assignItemRect(BLOOD_VIAL,      6,  15);
		assignItemRect(OBLIVION_SHARD,  7,  14);
		assignItemRect(CHAOTIC_CENSER,  13, 15);
		assignItemRect(FERRET_TUFT,     16, 15);
		assignItemRect(SPYGLASS,        15, 15);
	}

	private static final int SCROLLS        =                               xy(1, 20);  //16 slots
	public static final int SCROLL_KAUNAN   = SCROLLS+0;
	public static final int SCROLL_SOWILO   = SCROLLS+1;
	public static final int SCROLL_LAGUZ    = SCROLLS+2;
	public static final int SCROLL_YNGVI    = SCROLLS+3;
	public static final int SCROLL_GYFU     = SCROLLS+4;
	public static final int SCROLL_RAIDO    = SCROLLS+5;
	public static final int SCROLL_ISAZ     = SCROLLS+6;
	public static final int SCROLL_MANNAZ   = SCROLLS+7;
	public static final int SCROLL_NAUDIZ   = SCROLLS+8;
	public static final int SCROLL_BERKANAN = SCROLLS+9;
	public static final int SCROLL_ODAL     = SCROLLS+10;
	public static final int SCROLL_TIWAZ    = SCROLLS+11;

	public static final int ARCANE_RESIN    = SCROLLS+13;
	static {
		for (int i = SCROLLS; i < SCROLLS+16; i++)
			assignItemRect(i, 15, 14);
		assignItemRect(ARCANE_RESIN   , 12, 11);
	}
	
	private static final int EXOTIC_SCROLLS =                               xy(1, 21);  //16 slots
	public static final int EXOTIC_KAUNAN   = EXOTIC_SCROLLS+0;
	public static final int EXOTIC_SOWILO   = EXOTIC_SCROLLS+1;
	public static final int EXOTIC_LAGUZ    = EXOTIC_SCROLLS+2;
	public static final int EXOTIC_YNGVI    = EXOTIC_SCROLLS+3;
	public static final int EXOTIC_GYFU     = EXOTIC_SCROLLS+4;
	public static final int EXOTIC_RAIDO    = EXOTIC_SCROLLS+5;
	public static final int EXOTIC_ISAZ     = EXOTIC_SCROLLS+6;
	public static final int EXOTIC_MANNAZ   = EXOTIC_SCROLLS+7;
	public static final int EXOTIC_NAUDIZ   = EXOTIC_SCROLLS+8;
	public static final int EXOTIC_BERKANAN = EXOTIC_SCROLLS+9;
	public static final int EXOTIC_ODAL     = EXOTIC_SCROLLS+10;
	public static final int EXOTIC_TIWAZ    = EXOTIC_SCROLLS+11;
	static {
		for (int i = EXOTIC_SCROLLS; i < EXOTIC_SCROLLS+16; i++)
			assignItemRect(i, 15, 14);
	}
	
	private static final int STONES             =                           xy(1, 22);  //16 slots
	public static final int STONE_AGGRESSION    = STONES+0;
	public static final int STONE_AUGMENTATION  = STONES+1;
	public static final int STONE_FEAR          = STONES+2;
	public static final int STONE_BLAST         = STONES+3;
	public static final int STONE_BLINK         = STONES+4;
	public static final int STONE_CLAIRVOYANCE  = STONES+5;
	public static final int STONE_SLEEP         = STONES+6;
	public static final int STONE_DETECT        = STONES+7;
	public static final int STONE_ENCHANT       = STONES+8;
	public static final int STONE_FLOCK         = STONES+9;
	public static final int STONE_INTUITION     = STONES+10;
	public static final int STONE_SHOCK         = STONES+11;
	static {
		for (int i = STONES; i < STONES+16; i++)
			assignItemRect(i, 14, 12);
	}

	private static final int POTIONS        =                               xy(1, 23);  //16 slots
	public static final int POTION_CRIMSON  = POTIONS+0;
	public static final int POTION_AMBER    = POTIONS+1;
	public static final int POTION_GOLDEN   = POTIONS+2;
	public static final int POTION_JADE     = POTIONS+3;
	public static final int POTION_TURQUOISE= POTIONS+4;
	public static final int POTION_AZURE    = POTIONS+5;
	public static final int POTION_INDIGO   = POTIONS+6;
	public static final int POTION_MAGENTA  = POTIONS+7;
	public static final int POTION_BISTRE   = POTIONS+8;
	public static final int POTION_CHARCOAL = POTIONS+9;
	public static final int POTION_SILVER   = POTIONS+10;
	public static final int POTION_IVORY    = POTIONS+11;

	public static final int LIQUID_METAL    = POTIONS+13;
	static {
		for (int i = POTIONS; i < POTIONS+16; i++)
			assignItemRect(i, 12, 14);
		assignItemRect(LIQUID_METAL,    8, 15);
	}
	
	private static final int EXOTIC_POTIONS =                               xy(1, 24);  //16 slots
	public static final int EXOTIC_CRIMSON  = EXOTIC_POTIONS+0;
	public static final int EXOTIC_AMBER    = EXOTIC_POTIONS+1;
	public static final int EXOTIC_GOLDEN   = EXOTIC_POTIONS+2;
	public static final int EXOTIC_JADE     = EXOTIC_POTIONS+3;
	public static final int EXOTIC_TURQUOISE= EXOTIC_POTIONS+4;
	public static final int EXOTIC_AZURE    = EXOTIC_POTIONS+5;
	public static final int EXOTIC_INDIGO   = EXOTIC_POTIONS+6;
	public static final int EXOTIC_MAGENTA  = EXOTIC_POTIONS+7;
	public static final int EXOTIC_BISTRE   = EXOTIC_POTIONS+8;
	public static final int EXOTIC_CHARCOAL = EXOTIC_POTIONS+9;
	public static final int EXOTIC_SILVER   = EXOTIC_POTIONS+10;
	public static final int EXOTIC_IVORY    = EXOTIC_POTIONS+11;
	static {
		for (int i = EXOTIC_POTIONS; i < EXOTIC_POTIONS+16; i++)
			assignItemRect(i, 12, 13);
	}

	private static final int SEEDS              =                           xy(1, 25);  //16 slots
	public static final int SEED_ROTBERRY       = SEEDS+0;
	public static final int SEED_FIREBLOOM      = SEEDS+1;
	public static final int SEED_SWIFTTHISTLE   = SEEDS+2;
	public static final int SEED_SUNGRASS       = SEEDS+3;
	public static final int SEED_ICECAP         = SEEDS+4;
	public static final int SEED_STORMVINE      = SEEDS+5;
	public static final int SEED_SORROWMOSS     = SEEDS+6;
	public static final int SEED_MAGEROYAL = SEEDS+7;
	public static final int SEED_EARTHROOT      = SEEDS+8;
	public static final int SEED_STARFLOWER     = SEEDS+9;
	public static final int SEED_FADELEAF       = SEEDS+10;
	public static final int SEED_BLINDWEED      = SEEDS+11;
	static{
		for (int i = SEEDS; i < SEEDS+16; i++)
			assignItemRect(i, 10, 10);
	}
	
	private static final int BREWS          =                               xy(1, 26);  //8 slots
	public static final int BREW_INFERNAL   = BREWS+0;
	public static final int BREW_BLIZZARD   = BREWS+1;
	public static final int BREW_SHOCKING   = BREWS+2;
	public static final int BREW_CAUSTIC    = BREWS+3;
	public static final int BREW_AQUA       = BREWS+4;
	public static final int BREW_UNSTABLE   = BREWS+5;
	
	private static final int ELIXIRS        =                               xy(9, 26);  //8 slots
	public static final int ELIXIR_HONEY    = ELIXIRS+0;
	public static final int ELIXIR_AQUA     = ELIXIRS+1;
	public static final int ELIXIR_MIGHT    = ELIXIRS+2;
	public static final int ELIXIR_DRAGON   = ELIXIRS+3;
	public static final int ELIXIR_TOXIC    = ELIXIRS+4;
	public static final int ELIXIR_ICY      = ELIXIRS+5;
	public static final int ELIXIR_ARCANE   = ELIXIRS+6;
	public static final int ELIXIR_FEATHER  = ELIXIRS+7;
	static{
		for (int i = BREWS; i < BREWS+16; i++)
			assignItemRect(i, 12, 14);

		assignItemRect(BREW_INFERNAL,   11, 13);
		assignItemRect(BREW_BLIZZARD,   11, 13);
		assignItemRect(BREW_UNSTABLE,   11, 13);
		assignItemRect(BREW_AQUA,        9, 11);
	}
	
	private static final int SPELLS         =                               xy(1, 27);  //16 slots
	public static final int WILD_ENERGY     = SPELLS+0;
	public static final int PHASE_SHIFT     = SPELLS+1;
	public static final int TELE_GRAB       = SPELLS+2;
	public static final int UNSTABLE_SPELL  = SPELLS+3;
	public static final int CURSE_INFUSE    = SPELLS+4;
	public static final int MAGIC_INFUSE    = SPELLS+5;
	public static final int ALCHEMIZE       = SPELLS+6;
	public static final int RECYCLE         = SPELLS+7;
	public static final int RECLAIM_TRAP    = SPELLS+8;
	public static final int RETURN_BEACON   = SPELLS+9;
	public static final int SUMMON_ELE      = SPELLS+10;
	public static final int SUMMON_ELE_FIRE = SPELLS+11;
	public static final int SUMMON_ELE_FROST= SPELLS+12;
	public static final int SUMMON_ELE_SHOCK= SPELLS+13;
	public static final int SUMMON_ELE_CHAOS= SPELLS+14;

	static{
		assignItemRect(WILD_ENERGY,     6, 15);
		assignItemRect(PHASE_SHIFT,     12, 10);
		assignItemRect(TELE_GRAB,       10, 10);
		assignItemRect(UNSTABLE_SPELL,  12, 13);

		assignItemRect(CURSE_INFUSE,    10, 16);
		assignItemRect(MAGIC_INFUSE,    10, 14);
		assignItemRect(ALCHEMIZE,       12, 12);
		assignItemRect(RECYCLE,         12, 13);

		assignItemRect(RECLAIM_TRAP,    14, 11);
		assignItemRect(RETURN_BEACON,    8, 16);
		assignItemRect(SUMMON_ELE,       8, 16);
		assignItemRect(SUMMON_ELE_FIRE,  8, 16);
		assignItemRect(SUMMON_ELE_FROST, 8, 16);
		assignItemRect(SUMMON_ELE_SHOCK, 8, 16);
		assignItemRect(SUMMON_ELE_CHAOS, 8, 16);
	}
	
	private static final int FOOD       =                                   xy(1, 28);  //16 slots
	public static final int MEAT            = FOOD+0;
	public static final int STEAK           = FOOD+1;
	public static final int STEWED          = FOOD+2;
	public static final int OVERPRICED      = FOOD+3;
	public static final int CARPACCIO       = FOOD+4;
	public static final int RATION          = FOOD+5;
	public static final int PASTY           = FOOD+6;
	public static final int MEAT_PIE        = FOOD+7;
	public static final int BLANDFRUIT      = FOOD+8;
	public static final int BLAND_CHUNKS    = FOOD+9;
	public static final int BERRY           = FOOD+10;
	public static final int PHANTOM_MEAT    = FOOD+11;
	public static final int SUPPLY_RATION   = FOOD+12;
	static{
		assignItemRect(MEAT,            15, 11);
		assignItemRect(STEAK,           15, 11);
		assignItemRect(STEWED,          15, 11);
		assignItemRect(OVERPRICED,      14, 11);
		assignItemRect(CARPACCIO,       15, 11);
		assignItemRect(RATION,          16, 12);
		assignItemRect(PASTY,           16, 11);
		assignItemRect(MEAT_PIE,        16, 12);
		assignItemRect(BLANDFRUIT,      9,  12);
		assignItemRect(BLAND_CHUNKS,    14,  6);
		assignItemRect(BERRY,           9,  11);
		assignItemRect(PHANTOM_MEAT,    15, 11);
		assignItemRect(SUPPLY_RATION,   16, 12);
	}

	private static final int HOLIDAY_FOOD   =                               xy(1, 29);  //16 slots
	public static final int STEAMED_FISH    = HOLIDAY_FOOD+0;
	public static final int FISH_LEFTOVER   = HOLIDAY_FOOD+1;
	public static final int CHOC_AMULET     = HOLIDAY_FOOD+2;
	public static final int EASTER_EGG      = HOLIDAY_FOOD+3;
	public static final int RAINBOW_POTION  = HOLIDAY_FOOD+4;
	public static final int SHATTERED_CAKE  = HOLIDAY_FOOD+5;
	public static final int PUMPKIN_PIE     = HOLIDAY_FOOD+6;
	public static final int VANILLA_CAKE    = HOLIDAY_FOOD+7;
	public static final int CANDY_CANE      = HOLIDAY_FOOD+8;
	public static final int SPARKLING_POTION= HOLIDAY_FOOD+9;
	static{
		assignItemRect(STEAMED_FISH,    16, 12);
		assignItemRect(FISH_LEFTOVER,   16, 12);
		assignItemRect(CHOC_AMULET,     16, 16);
		assignItemRect(EASTER_EGG,      12, 14);
		assignItemRect(RAINBOW_POTION,  12, 14);
		assignItemRect(SHATTERED_CAKE,  14, 13);
		assignItemRect(PUMPKIN_PIE,     16, 12);
		assignItemRect(VANILLA_CAKE,    14, 13);
		assignItemRect(CANDY_CANE,      13, 16);
		assignItemRect(SPARKLING_POTION, 7, 16);
	}

	private static final int QUEST  =                                       xy(1, 30);  //16 slots
	public static final int DUST    = QUEST+1;
	public static final int CANDLE  = QUEST+2;
	public static final int EMBER   = QUEST+3;
	public static final int PICKAXE = QUEST+4;
	public static final int ORE     = QUEST+5;
	public static final int TOKEN   = QUEST+6;
	public static final int BLOB    = QUEST+7;
	public static final int SHARD   = QUEST+8;
	public static final int ESCAPE  = QUEST+9;
	public static final int STATUE  = QUEST+10;
	static{
		assignItemRect(DUST,    12, 11);
		assignItemRect(CANDLE,  12, 12);
		assignItemRect(EMBER,   12, 11);
		assignItemRect(PICKAXE, 14, 14);
		assignItemRect(ORE,     15, 15);
		assignItemRect(TOKEN,   12, 12);
		assignItemRect(BLOB,    10,  9);
		assignItemRect(SHARD,    8, 10);
		assignItemRect(ESCAPE,   8, 16);
		assignItemRect(STATUE,  10, 16);
	}

	private static final int BAGS       =                                   xy(1, 31);  //16 slots
	public static final int WATERSKIN   = BAGS+0;
	public static final int BACKPACK    = BAGS+1;
	public static final int POUCH       = BAGS+2;
	public static final int HOLDER      = BAGS+3;
	public static final int BANDOLIER   = BAGS+4;
	public static final int HOLSTER     = BAGS+5;
	public static final int VIAL        = BAGS+6;
	static{
		assignItemRect(WATERSKIN,   16, 14);
		assignItemRect(BACKPACK,    16, 16);
		assignItemRect(POUCH,       14, 15);
		assignItemRect(HOLDER,      16, 16);
		assignItemRect(BANDOLIER,   15, 16);
		assignItemRect(HOLSTER,     15, 16);
		assignItemRect(VIAL,        12, 12);
	}

	private static final int DOCUMENTS  =                                   xy(1, 32);  //16 slots
	public static final int GUIDE_PAGE  = DOCUMENTS+0;
	public static final int ALCH_PAGE   = DOCUMENTS+1;
	public static final int SEWER_PAGE  = DOCUMENTS+2;
	public static final int PRISON_PAGE = DOCUMENTS+3;
	public static final int CAVES_PAGE  = DOCUMENTS+4;
	public static final int CITY_PAGE   = DOCUMENTS+5;
	public static final int HALLS_PAGE  = DOCUMENTS+6;
	static{
		assignItemRect(GUIDE_PAGE,  10, 11);
		assignItemRect(ALCH_PAGE,   10, 11);
		assignItemRect(SEWER_PAGE,  10, 11);
		assignItemRect(PRISON_PAGE, 10, 11);
		assignItemRect(CAVES_PAGE,  10, 11);
		assignItemRect(CITY_PAGE,   10, 11);
		assignItemRect(HALLS_PAGE,  10, 11);
	}

	//for smaller 8x8 icons that often accompany an item sprite
	public static class Icons {

		private static final int WIDTH = 16;
		public static final int SIZE = 8;

		public static TextureFilm film = new TextureFilm( Assets.Sprites.ITEM_ICONS, SIZE, SIZE );

		private static int xy(int x, int y){
			x -= 1; y -= 1;
			return x + WIDTH*y;
		}

		private static void assignIconRect( int item, int width, int height ){
			int x = (item % WIDTH) * SIZE;
			int y = (item / WIDTH) * SIZE;
			film.add( item, x, y, x+width, y+height);
		}

		private static final int RINGS          =                            xy(1, 1);  //16 slots
		public static final int RING_ACCURACY   = RINGS+0;
		public static final int RING_ARCANA     = RINGS+1;
		public static final int RING_ELEMENTS   = RINGS+2;
		public static final int RING_ENERGY     = RINGS+3;
		public static final int RING_EVASION    = RINGS+4;
		public static final int RING_FORCE      = RINGS+5;
		public static final int RING_FUROR      = RINGS+6;
		public static final int RING_HASTE      = RINGS+7;
		public static final int RING_MIGHT      = RINGS+8;
		public static final int RING_SHARPSHOOT = RINGS+9;
		public static final int RING_TENACITY   = RINGS+10;
		public static final int RING_WEALTH     = RINGS+11;
		static {
			assignIconRect( RING_ACCURACY,      7, 7 );
			assignIconRect( RING_ARCANA,        7, 7 );
			assignIconRect( RING_ELEMENTS,      7, 7 );
			assignIconRect( RING_ENERGY,        7, 5 );
			assignIconRect( RING_EVASION,       7, 7 );
			assignIconRect( RING_FORCE,         5, 6 );
			assignIconRect( RING_FUROR,         7, 6 );
			assignIconRect( RING_HASTE,         6, 6 );
			assignIconRect( RING_MIGHT,         7, 7 );
			assignIconRect( RING_SHARPSHOOT,    7, 7 );
			assignIconRect( RING_TENACITY,      6, 6 );
			assignIconRect( RING_WEALTH,        7, 6 );
		}

		                                                                                //16 free slots

		private static final int SCROLLS        =                            xy(1, 3);  //16 slots
		public static final int SCROLL_UPGRADE  = SCROLLS+0;
		public static final int SCROLL_IDENTIFY = SCROLLS+1;
		public static final int SCROLL_REMCURSE = SCROLLS+2;
		public static final int SCROLL_MIRRORIMG= SCROLLS+3;
		public static final int SCROLL_RECHARGE = SCROLLS+4;
		public static final int SCROLL_TELEPORT = SCROLLS+5;
		public static final int SCROLL_LULLABY  = SCROLLS+6;
		public static final int SCROLL_MAGICMAP = SCROLLS+7;
		public static final int SCROLL_RAGE     = SCROLLS+8;
		public static final int SCROLL_RETRIB   = SCROLLS+9;
		public static final int SCROLL_TERROR   = SCROLLS+10;
		public static final int SCROLL_TRANSMUTE= SCROLLS+11;
		static {
			assignIconRect( SCROLL_UPGRADE,     7, 7 );
			assignIconRect( SCROLL_IDENTIFY,    4, 7 );
			assignIconRect( SCROLL_REMCURSE,    7, 7 );
			assignIconRect( SCROLL_MIRRORIMG,   7, 5 );
			assignIconRect( SCROLL_RECHARGE,    7, 5 );
			assignIconRect( SCROLL_TELEPORT,    7, 7 );
			assignIconRect( SCROLL_LULLABY,     7, 6 );
			assignIconRect( SCROLL_MAGICMAP,    7, 7 );
			assignIconRect( SCROLL_RAGE,        6, 6 );
			assignIconRect( SCROLL_RETRIB,      5, 6 );
			assignIconRect( SCROLL_TERROR,      5, 7 );
			assignIconRect( SCROLL_TRANSMUTE,   7, 7 );
		}

		private static final int EXOTIC_SCROLLS =                            xy(1, 4);  //16 slots
		public static final int SCROLL_ENCHANT  = EXOTIC_SCROLLS+0;
		public static final int SCROLL_DIVINATE = EXOTIC_SCROLLS+1;
		public static final int SCROLL_ANTIMAGIC= EXOTIC_SCROLLS+2;
		public static final int SCROLL_PRISIMG  = EXOTIC_SCROLLS+3;
		public static final int SCROLL_MYSTENRG = EXOTIC_SCROLLS+4;
		public static final int SCROLL_PASSAGE  = EXOTIC_SCROLLS+5;
		public static final int SCROLL_SIREN    = EXOTIC_SCROLLS+6;
		public static final int SCROLL_FORESIGHT= EXOTIC_SCROLLS+7;
		public static final int SCROLL_CHALLENGE= EXOTIC_SCROLLS+8;
		public static final int SCROLL_PSIBLAST = EXOTIC_SCROLLS+9;
		public static final int SCROLL_DREAD    = EXOTIC_SCROLLS+10;
		public static final int SCROLL_METAMORPH= EXOTIC_SCROLLS+11;
		static {
			assignIconRect( SCROLL_ENCHANT,     7, 7 );
			assignIconRect( SCROLL_DIVINATE,    7, 6 );
			assignIconRect( SCROLL_ANTIMAGIC,   7, 7 );
			assignIconRect( SCROLL_PRISIMG,     5, 7 );
			assignIconRect( SCROLL_MYSTENRG,    7, 5 );
			assignIconRect( SCROLL_PASSAGE,     5, 7 );
			assignIconRect( SCROLL_SIREN,       7, 6 );
			assignIconRect( SCROLL_FORESIGHT,   7, 5 );
			assignIconRect( SCROLL_CHALLENGE,   7, 7 );
			assignIconRect( SCROLL_PSIBLAST,    5, 6 );
			assignIconRect( SCROLL_DREAD,       5, 7 );
			assignIconRect( SCROLL_METAMORPH,   7, 7 );
		}

		                                                                                //16 free slots

		private static final int POTIONS        =                            xy(1, 6);  //16 slots
		public static final int POTION_STRENGTH = POTIONS+0;
		public static final int POTION_HEALING  = POTIONS+1;
		public static final int POTION_MINDVIS  = POTIONS+2;
		public static final int POTION_FROST    = POTIONS+3;
		public static final int POTION_LIQFLAME = POTIONS+4;
		public static final int POTION_TOXICGAS = POTIONS+5;
		public static final int POTION_HASTE    = POTIONS+6;
		public static final int POTION_INVIS    = POTIONS+7;
		public static final int POTION_LEVITATE = POTIONS+8;
		public static final int POTION_PARAGAS  = POTIONS+9;
		public static final int POTION_PURITY   = POTIONS+10;
		public static final int POTION_EXP      = POTIONS+11;
		static {
			assignIconRect( POTION_STRENGTH,    7, 7 );
			assignIconRect( POTION_HEALING,     6, 7 );
			assignIconRect( POTION_MINDVIS,     7, 5 );
			assignIconRect( POTION_FROST,       7, 7 );
			assignIconRect( POTION_LIQFLAME,    5, 7 );
			assignIconRect( POTION_TOXICGAS,    7, 7 );
			assignIconRect( POTION_HASTE,       6, 6 );
			assignIconRect( POTION_INVIS,       5, 7 );
			assignIconRect( POTION_LEVITATE,    6, 7 );
			assignIconRect( POTION_PARAGAS,     7, 7 );
			assignIconRect( POTION_PURITY,      5, 7 );
			assignIconRect( POTION_EXP,         7, 7 );
		}

		private static final int EXOTIC_POTIONS =                            xy(1, 7);  //16 slots
		public static final int POTION_MASTERY  = EXOTIC_POTIONS+0;
		public static final int POTION_SHIELDING= EXOTIC_POTIONS+1;
		public static final int POTION_MAGISIGHT= EXOTIC_POTIONS+2;
		public static final int POTION_SNAPFREEZ= EXOTIC_POTIONS+3;
		public static final int POTION_DRGBREATH= EXOTIC_POTIONS+4;
		public static final int POTION_CORROGAS = EXOTIC_POTIONS+5;
		public static final int POTION_STAMINA  = EXOTIC_POTIONS+6;
		public static final int POTION_SHROUDFOG= EXOTIC_POTIONS+7;
		public static final int POTION_STRMCLOUD= EXOTIC_POTIONS+8;
		public static final int POTION_EARTHARMR= EXOTIC_POTIONS+9;
		public static final int POTION_CLEANSE  = EXOTIC_POTIONS+10;
		public static final int POTION_DIVINE   = EXOTIC_POTIONS+11;

		//from Shittened PD
		public static final int SCROLL_SHIT = SCROLLS+12;
		public static final int SCROLL_ENSHIT = EXOTIC_SCROLLS+12;
		public static final int POTION_ACC = POTIONS+12;
		public static final int POTION_KET = POTIONS+13;
		public static final int POTION_FUEL = POTIONS+14;
		public static final int POTION_FUROR = EXOTIC_POTIONS+12;
		public static final int POTION_KET_STORM = EXOTIC_POTIONS+13;
		static {
			assignIconRect( SCROLL_SHIT, 7, 4 );
			assignIconRect( SCROLL_ENSHIT, 8, 8 );
			assignIconRect( POTION_ACC, 7, 6 );
			assignIconRect( POTION_KET, 7, 2 );
			assignIconRect( POTION_FUEL, 5, 7 );
			assignIconRect( POTION_FUROR, 7, 6 );
			assignIconRect( POTION_KET_STORM, 7, 7 );
		}
		static {
			assignIconRect( POTION_MASTERY,     7, 7 );
			assignIconRect( POTION_SHIELDING,   6, 6 );
			assignIconRect( POTION_MAGISIGHT,   7, 5 );
			assignIconRect( POTION_SNAPFREEZ,   7, 7 );
			assignIconRect( POTION_DRGBREATH,   7, 7 );
			assignIconRect( POTION_CORROGAS,    7, 7 );
			assignIconRect( POTION_STAMINA,     6, 6 );
			assignIconRect( POTION_SHROUDFOG,   7, 7 );
			assignIconRect( POTION_STRMCLOUD,   7, 7 );
			assignIconRect( POTION_EARTHARMR,   6, 6 );
			assignIconRect( POTION_CLEANSE,     7, 7 );
			assignIconRect( POTION_DIVINE,      7, 7 );
		}

		                                                                                //16 free slots

	}


	public static class Prefix {

		private static final int WIDTH = 16;
		public static final int SIZE = 8;

		public static TextureFilm film = new TextureFilm(Assets.Sprites.PREFIX_ICONS, SIZE, SIZE);

		private static int xy(int x, int y) {
			x -= 1;
			y -= 1;
			return x + WIDTH * y;
		}

		private static void assignIconRect(int item, int width, int height) {
			int x = (item % WIDTH) * SIZE;
			int y = (item / WIDTH) * SIZE;
			film.add(item, x, y, x + width, y + height);
		}

		private static final int PREFIXES = xy(1, 1);
		public static final int ANNOYING 		= PREFIXES + 0;
		public static final int LONG 			= PREFIXES + 1;
		public static final int MUSICAL 		= PREFIXES + 2;
		public static final int FEARSOME 		= PREFIXES + 3;
		public static final int DULL 			= PREFIXES + 4;
		public static final int BORING 			= PREFIXES + 5;
		public static final int CATASTROPHIC 	= PREFIXES + 6;
		public static final int SLOW 			= PREFIXES + 7;
		public static final int WEIGHTLESS 		= PREFIXES + 8;
		public static final int SILENT 			= PREFIXES + 9;
		public static final int LOUD 			= PREFIXES + 10;
		public static final int BOLD 			= PREFIXES + 11;
		public static final int CONSTRUCTIVE 	= PREFIXES + 12;
		public static final int QUICK 			= PREFIXES + 13;
		public static final int UNSTOPPABLE 	= PREFIXES + 14;
		public static final int MASTERFUL		= PREFIXES + 15;
		public static final int SCREAMING		= PREFIXES + 16;
		public static final int MUTED 			= PREFIXES + 17;
		public static final int BLAND 			= PREFIXES + 18;
		public static final int HINDERED 		= PREFIXES + 19;
		public static final int EXCITED 		= PREFIXES + 20;
		public static final int CARELESS 		= PREFIXES + 21;
		public static final int AWKWARD 		= PREFIXES + 22;
		public static final int HURTFUL 		= PREFIXES + 23;
		public static final int DEAD 			= PREFIXES + 24;
		public static final int FLIMSY 			= PREFIXES + 25;
		public static final int FAITHFUL 		= PREFIXES + 26;
		public static final int SPIKED 			= PREFIXES + 27;
		public static final int HEROIC 			= PREFIXES + 28;
		public static final int RUTHLESS 		= PREFIXES + 29;
		public static final int THUNDERING 		= PREFIXES + 30;
		public static final int GLISTERING 		= PREFIXES + 31;
		public static final int CELESTIAL 		= PREFIXES + 32;
		public static final int VENOMOUS 		= PREFIXES + 33;
		public static final int ETERNAL 		= PREFIXES + 34;
		public static final int SAVAGE 			= PREFIXES + 35;
		public static final int JAGGED 			= PREFIXES + 36;
		public static final int FRUSTRATING 	= PREFIXES + 37;
		public static final int SOOTHING 		= PREFIXES + 38;
		public static final int LUMINOUS 		= PREFIXES + 39;
		public static final int ELUSIVE 		= PREFIXES + 40;
		public static final int WORN 			= PREFIXES + 41;
		public static final int CRYING 			= PREFIXES + 42;
		public static final int GARGANTUAN 		= PREFIXES + 43;
		public static final int PROTECTIVE 		= PREFIXES + 44;
		public static final int STEADFAST 		= PREFIXES + 45;
		public static final int VOLATILE 		= PREFIXES + 46;
		public static final int SINISTER 		= PREFIXES + 47;
		public static final int ABYSMAL 		= PREFIXES + 48;
		public static final int KNOCKOFF 		= PREFIXES + 49;
		public static final int PLASTIC 		= PREFIXES + 50;
		public static final int BENT 			= PREFIXES + 51;
		public static final int STICKY 			= PREFIXES + 52;
		public static final int HUMBLE 			= PREFIXES + 53;
		public static final int BULKY 			= PREFIXES + 54;
		public static final int WOBBLY 			= PREFIXES + 55;
		public static final int DUSTY 			= PREFIXES + 56;
		public static final int SHATTERED 		= PREFIXES + 57;
		static {
			assignIconRect(ANNOYING 	, 				8, 8);
			assignIconRect(LONG 		, 				8, 8);
			assignIconRect(MUSICAL 		, 				8, 8);
			assignIconRect(FEARSOME 	, 				8, 8);
			assignIconRect(DULL 		, 				8, 8);
			assignIconRect(BORING 		, 				8, 8);
			assignIconRect(CATASTROPHIC	, 				8, 8);
			assignIconRect(SLOW 		, 				8, 8);
			assignIconRect(WEIGHTLESS 	, 				8, 8);
			assignIconRect(SILENT 		, 				8, 8);
			assignIconRect(LOUD 		, 				8, 8);
			assignIconRect(BOLD 		, 				8, 8);
			assignIconRect(CONSTRUCTIVE	, 				8, 8);
			assignIconRect(QUICK 		, 				8, 8);
			assignIconRect(UNSTOPPABLE 	, 				8, 8);
			assignIconRect(MASTERFUL	, 				8, 8);
			assignIconRect(SCREAMING	, 				8, 8);
			assignIconRect(MUTED 		, 				8, 8);
			assignIconRect(BLAND 		, 				8, 8);
			assignIconRect(HINDERED 	, 				8, 8);
			assignIconRect(EXCITED 		, 				8, 8);
			assignIconRect(CARELESS 	, 				8, 8);
			assignIconRect(AWKWARD 		, 				8, 8);
			assignIconRect(HURTFUL 		, 				8, 8);
			assignIconRect(DEAD 		, 				8, 8);
			assignIconRect(FLIMSY 		, 				8, 8);
			assignIconRect(FAITHFUL 	, 				8, 8);
			assignIconRect(SPIKED 		, 				8, 8);
			assignIconRect(HEROIC 		, 				8, 8);
			assignIconRect(RUTHLESS 	, 				8, 8);
			assignIconRect(THUNDERING 	, 				8, 8);
			assignIconRect(GLISTERING 	, 				8, 8);
			assignIconRect(CELESTIAL 	, 				8, 8);
			assignIconRect(VENOMOUS 	, 				8, 8);
			assignIconRect(ETERNAL 		, 				8, 8);
			assignIconRect(SAVAGE 		, 				8, 8);
			assignIconRect(JAGGED 		, 				8, 8);
			assignIconRect(FRUSTRATING 	, 				8, 8);
			assignIconRect(SOOTHING 	, 				8, 8);
			assignIconRect(LUMINOUS 	, 				8, 8);
			assignIconRect(ELUSIVE 		, 				8, 8);
			assignIconRect(WORN 		, 				8, 8);
			assignIconRect(CRYING 		, 				8, 8);
			assignIconRect(GARGANTUAN 	, 				8, 8);
			assignIconRect(PROTECTIVE 	, 				8, 8);
			assignIconRect(STEADFAST 	, 				8, 8);
			assignIconRect(VOLATILE 	, 				8, 8);
			assignIconRect(SINISTER 	, 				8, 8);
			assignIconRect(ABYSMAL 		, 				8, 8);
			assignIconRect(KNOCKOFF 	, 				8, 8);
			assignIconRect(PLASTIC 		, 				8, 8);
		}
	}
}
