/*
 * Onyx's Pixel Dungeon
 * Counters that Magic Ling PD keeps in its own Statistics class, kept here so
 * Statistics itself stays as Shattered wrote it. They are not saved with the run,
 * so they restart at their defaults when a game is loaded. renames.py points
 * Statistics.<name> in ported Magic Ling code at this class.
 */
package com.shatteredpixel.shatteredpixeldungeon;

public class MagicLingStats {

	public static int survey_research_points;
	public static boolean unLockedFireDargon = false;
	public static boolean doNotLookLing = false;
	public static int sakaBackStage;
	public static int spawnersIce;
	public static int spawnersTombTownAlive;
	public static boolean lanterfireactive = false;
	public static boolean crivusfruitslevel2 = false;
	public static boolean crivusfruitslevel3 = false;
	public static int dimandchestmazeCollected;
	public static boolean skydeadFight = false;
	public static boolean defalult_deaddog = false;
	public static boolean bossRushMode = false;
	public static boolean dwarfKill = false;
	public static int killYogMobsAnargy;
	public static boolean attackIFGirl = false;
	public static int RandomQuest = 2;
	public static int GoldMobDead;
	public static int GoldMobLimit;
	public static boolean TPDoorDieds = false;
	public static int KillMazeMimic;
	public static int goldRefogreCount;

	//the mod kept this in its settings file, it counts boss weapon drops so every third kill gives one for sure
	private static final int[] bossWeaponCount = new int[6];
	public static int BossWeaponCount1() { return bossWeaponCount[1]; }
	public static void BossWeaponCount1( int value ) { bossWeaponCount[1] = value; }
	public static int BossWeaponCount2() { return bossWeaponCount[2]; }
	public static void BossWeaponCount2( int value ) { bossWeaponCount[2] = value; }
	public static int BossWeaponCount3() { return bossWeaponCount[3]; }
	public static void BossWeaponCount3( int value ) { bossWeaponCount[3] = value; }
	public static int BossWeaponCount4() { return bossWeaponCount[4]; }
	public static void BossWeaponCount4( int value ) { bossWeaponCount[4] = value; }
	public static int BossWeaponCount5() { return bossWeaponCount[5]; }
	public static void BossWeaponCount5( int value ) { bossWeaponCount[5] = value; }

	public static void reset() {
		survey_research_points = 0;
		unLockedFireDargon = false;
		doNotLookLing = false;
		sakaBackStage = 0;
		spawnersIce = 0;
		spawnersTombTownAlive = 0;
		lanterfireactive = false;
		crivusfruitslevel2 = false;
		crivusfruitslevel3 = false;
		dimandchestmazeCollected = 0;
		skydeadFight = false;
		defalult_deaddog = false;
		bossRushMode = false;
		dwarfKill = false;
		killYogMobsAnargy = 0;
		attackIFGirl = false;
		RandomQuest = 2;
		GoldMobDead = 0;
		GoldMobLimit = 0;
		TPDoorDieds = false;
		KillMazeMimic = 0;
		goldRefogreCount = 0;
	}
}
