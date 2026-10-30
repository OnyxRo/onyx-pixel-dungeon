package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;

//Easier Sprouted Pixel Dungeon (G2159687/ESPD), its easy options as game modes.
//Each one is off unless picked in the Game modes window, so normal runs are unchanged.
public class EspdModes {

	public static final String MOD = "Easier Sprouted Pixel Dungeon";

	public static final String HP      = "espd_hp";
	public static final String ATK     = "espd_atk";
	public static final String DEF     = "espd_def";
	public static final String STR     = "espd_str";
	public static final String SHOPS   = "espd_shops";
	public static final String CHEAP   = "espd_cheap";
	public static final String UPGRADE = "espd_upgrade";
	public static final String LOOT    = "espd_loot";
	public static final String QUEST   = "espd_quest";

	public static void register(){
		for (String id : new String[]{HP, ATK, DEF, STR, SHOPS, CHEAP, UPGRADE, LOOT, QUEST}){
			GameModes.register(id, MOD, 1, null);
		}
	}

	//ESPD's HeroClass init, the numbers are the mod's own
	public static void onStart( Hero hero ){
		if (GameModes.on(HP)){
			hero.HTBoost += 200;
			hero.updateHT(true);
			hero.HP = hero.HT;
		}
		if (GameModes.on(ATK)) hero.attackSkill += 100;
		if (GameModes.on(DEF)) hero.defenseSkill += 100;
		if (GameModes.on(STR)) hero.STR += 10;
	}

	//ESPD put a shop on every regular floor of each region
	public static boolean moreShops(){
		if (!GameModes.on(SHOPS)) return false;
		int d = Dungeon.depth;
		return d >= 1 && d <= 24 && d % 5 != 0 && d != 21;
	}

	public static int sellPrice( int price ){
		return GameModes.on(CHEAP) ? 1 : price;
	}

	//a scroll of upgrade gives ten levels instead of one
	public static void extraUpgrades( Item item ){
		if (GameModes.on(UPGRADE) && item != null) item.upgrade(9);
	}

	public static float lootChance( float chance ){
		return GameModes.on(LOOT) ? Math.min(1f, chance * 3f) : chance;
	}

	//quest rewards (Ghost, Wandmaker, Imp) get thirty more levels
	public static void questReward( Item item ){
		if (GameModes.on(QUEST) && item != null) item.upgrade(30);
	}
}
