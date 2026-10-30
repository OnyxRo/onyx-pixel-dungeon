/*
 * WuWuWu by 3oiDburg (github.com/3oiDburg/WuWuWu), a joke mod on the libGDX port of Shattered 0.6.5c.
 * Its two changes to a run, ported as game modes. The hero always has a million gold, and each boss
 * floor from 5 to 20 has a one in four chance to be a plain floor of its region instead.
 * Its Boomerang "Buff" action is left out, Shattered's boomerang no longer exists and the buff did nothing.
 */
package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.items.KingsCrown;
import com.shatteredpixel.shatteredpixeldungeon.items.TengusMask;
import com.shatteredpixel.shatteredpixeldungeon.levels.CavesLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.CityLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.PrisonLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.SewerLevel;

public class WuWuWu {

	public static final String GOLD = "wuwuwu_gold";
	public static final String SKIP_BOSSES = "wuwuwu_skip_bosses";
	public static final String MOD = "WuWuWu";

	public static final int MILLION = 1000000;

	public static void register(){
		GameModes.register(GOLD, MOD, 1, null);
		GameModes.register(SKIP_BOSSES, MOD, 1, null);
	}

	//the mod set the gold to a million at the start and on every load, so it never runs out
	public static void topUpGold(){
		if (GameModes.on(GOLD) && Dungeon.gold < MILLION) Dungeon.gold = MILLION;
	}

	//seeded, so a seed always skips the same bosses
	public static boolean skipsBoss( int depth ){
		if (!GameModes.on(SKIP_BOSSES) || Dungeon.branch != 0) return false;
		if (depth != 5 && depth != 10 && depth != 15 && depth != 20) return false;
		return new java.util.Random(Dungeon.seedForDepth(depth, 0)).nextFloat() < 0.25f;
	}

	public static Level plainFloor( int depth ){
		RegularLevel level;
		if (depth <= 5)       level = new SewerLevel();
		else if (depth <= 10) level = new PrisonLevel();
		else if (depth <= 15) level = new CavesLevel();
		else                  level = new CityLevel();
		//the boss would have given these, so the plain floor holds them instead
		Hero hero = Dungeon.hero;
		if (hero != null && depth == 10 && hero.subClass == HeroSubClass.NONE) level.addItemToSpawn(new TengusMask());
		if (hero != null && depth == 20 && hero.armorAbility == null) level.addItemToSpawn(new KingsCrown());
		return level;
	}
}
