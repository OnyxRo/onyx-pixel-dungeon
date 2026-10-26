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
	public static int fuckGeneratorAlone;
	public static boolean dm720Fight = false;
	//added in the Magic Ling finish, the mod's random mode, timers and story flags, all left at their defaults
	public static boolean RandMode = false;
	public static boolean NoTime = false;
	public static boolean fireGirlAnary = false;
	public static boolean TrueYogNoDied = false;
	public static boolean winGame = false;
	public static int LenaAnaryTotal;
	public static boolean beatMerchant = false;
	public static boolean stoleItem = false;
	public static boolean Tomb_Reach = false;
	public static int AbyssCityRules;
	public static boolean isSmallLeaf = false;
	public static boolean mustTengu = false;
	public static boolean mimicking = false;
	public static int difficultyDLCEXLevel;
	public static int CrivusbossTeleporter;
	public static boolean gooFight = false;
	//the hollow's minigames and towers, scores are kept for the run only
	public static int RepaierTowerCount;
	public static int miniGamesTotalLevel;
	public static int getMoveBoxScore;
	public static int readBooks;
	public static int getPacManScore;
	public static int getAlLSearchScore;
	public static int moveBoxScoreMax;
	public static boolean godGirl = false;
	public static int ChaicBlood;
	private static int moveBoxRecord, pacManRecord;
	public static int MoveBoxScore() { return moveBoxRecord; }
	public static void MoveBoxScore( int value ) { moveBoxRecord = Math.max(moveBoxRecord, value); }
	public static int PacManScore() { return pacManRecord; }
	public static void PacManScore( int value ) { pacManRecord = Math.max(pacManRecord, value); }

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
		fuckGeneratorAlone = 0;
		dm720Fight = false;
	}

	//added by scripts/stats-shim.py from the mod's Statistics and SPDSettings
	public static int BzmdrCJHeroSTR = 0;
	public static int BzmdrCJHeroViewDistance = 0;
	public static boolean CatFirst = false;
	public static boolean OnlyBloodUpgrade = false;
	public static boolean PinkFox = false;
	public static int gdzHelpDungeon;
	public static int hcDialogLevel;
	public static boolean moonlowgetAloneRoom;
	public static boolean onlyLing = false;
	public static int zeroItemLevel;
	private static boolean _catSee = false;
	public static boolean CatSee() { return _catSee; }
	public static void CatSee( boolean value ) { _catSee = value; }
	private static int _recordFlowerChallengs = 0;
	public static int RecordFlowerChallengs() { return _recordFlowerChallengs; }
	public static void RecordFlowerChallengs( int value ) { _recordFlowerChallengs = value; }
	private static boolean _smallLeafGetCoin = false;
	public static boolean SmallLeafGetCoin() { return _smallLeafGetCoin; }
	public static void SmallLeafGetCoin( boolean value ) { _smallLeafGetCoin = value; }
	private static boolean _smallLeafHCGetCoin = false;
	public static boolean SmallLeafHCGetCoin() { return _smallLeafHCGetCoin; }
	public static void SmallLeafHCGetCoin( boolean value ) { _smallLeafHCGetCoin = value; }
	private static boolean _blood = false;
	public static boolean blood() { return _blood; }
	public static void blood( boolean value ) { _blood = value; }
	private static int _currentBGM = 3;
	public static int currentBGM() { return _currentBGM; }
	public static void currentBGM( int value ) { _currentBGM = value; }
	private static String _unlockItem = "";
	public static String unlockItem() { return _unlockItem; }
	public static void unlockItem( String itemName ) { _unlockItem = itemName; }
	public static void unlockItem( String itemName, boolean allowMulti ) { }
	public static void unlockItem( String itemName, boolean allowMulti, int limit ) { }

	//the mod saved these with its run, without this a boss fight's phase was lost on reload and carried into the next run
	public static void storeInBundle( com.watabou.utils.Bundle bundle ) {
		for (java.lang.reflect.Field f : MagicLingStats.class.getFields()) {
			int m = f.getModifiers();
			if (!java.lang.reflect.Modifier.isStatic(m) || java.lang.reflect.Modifier.isFinal(m)) continue;
			try {
				if (f.getType() == boolean.class) bundle.put("mling_" + f.getName(), f.getBoolean(null));
				else if (f.getType() == int.class) bundle.put("mling_" + f.getName(), f.getInt(null));
			} catch (IllegalAccessException ignored) { }
		}
	}

	public static void restoreFromBundle( com.watabou.utils.Bundle bundle ) {
		reset();
		for (java.lang.reflect.Field f : MagicLingStats.class.getFields()) {
			int m = f.getModifiers();
			if (!java.lang.reflect.Modifier.isStatic(m) || java.lang.reflect.Modifier.isFinal(m)) continue;
			String key = "mling_" + f.getName();
			if (!bundle.contains(key)) continue;
			try {
				if (f.getType() == boolean.class) f.setBoolean(null, bundle.getBoolean(key));
				else if (f.getType() == int.class) f.setInt(null, bundle.getInt(key));
			} catch (IllegalAccessException ignored) { }
		}
	}
}
