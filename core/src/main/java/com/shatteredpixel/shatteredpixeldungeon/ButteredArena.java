/*
 * Buttered Pixel Dungeon by MrLolEthan (github.com/EthanJoyce/Buttered-Pixel-Dungeon), on Shattered 0.3.0.
 * Its Arena game mode. Floor 1 is an arena where waves of monsters come, one more wave each time the
 * hero levels up. Every fifth wave is a boss wave, and beating it gives a Pixel Mart Key to one of
 * the shop floors (6, 11, 16 and 21). After wave 25 the arena's stairs open to the Amulet floor.
 * Left out. Its own rankings and game slot per mode (this game keeps one list), and its arena badge art.
 */
package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.levels.ButteredArenaLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.ButteredShopLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ButteredArena {

	public static final String ARENA = "buttered_arena";
	public static final String MOD = "Buttered Pixel Dungeon";

	public static void register(){
		GameModes.register(ARENA, MOD, 1, "layout");
	}

	public static boolean on(){
		return GameModes.on(ARENA);
	}

	//floor 1 is the arena, floors 2 to 25 are only ever the shops the key leads to, floor 26 is the Amulet floor
	public static boolean builds( int depth ){
		return on() && depth >= 1 && depth <= 25;
	}

	public static Level level( int depth ){
		return depth == 1 ? new ButteredArenaLevel() : new ButteredShopLevel();
	}

	public static int shopDepth( int wave ){
		return Math.min(Math.max(wave / 5 * 5, 5), 20) + 1;
	}

	//stairs, the arena's exit goes to the Amulet floor and every other floor leads back up to the arena
	public static int nextDepth( int depth ){
		return depth == 1 ? 26 : depth + 1;
	}

	public static int prevDepth( int depth ){
		return depth <= 1 ? depth - 1 : 1;
	}

	//every level up heals in arena mode, and in the arena it starts the next wave
	public static void onLevelUp( Hero hero ){
		if (!on()) return;
		hero.HP = hero.HT;
		if (Dungeon.level instanceof ButteredArenaLevel) ((ButteredArenaLevel) Dungeon.level).heroLeveledUp();
	}

	//monsters in the arena always give experience, whatever the hero's level
	public static int killExp( Mob mob, int exp ){
		if (on() && exp == 0 && mob.EXP > 0 && Dungeon.level instanceof ButteredArenaLevel) return mob.EXP;
		return exp;
	}

	//a potion of experience would skip the boss, so it slips and breaks during a boss wave
	public static boolean blocksExperience( Hero hero ){
		if (on() && Dungeon.level instanceof ButteredArenaLevel && ((ButteredArenaLevel) Dungeon.level).bossWave()) {
			GLog.n(Messages.get(ButteredArena.class, "no_experience"));
			Sample.INSTANCE.play(Assets.Sounds.SHATTER);
			return true;
		}
		return false;
	}
}
