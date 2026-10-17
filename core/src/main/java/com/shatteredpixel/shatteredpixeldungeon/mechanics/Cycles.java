/*
 * Onyx's Pixel Dungeon
 * Cycle system from Experienced PD. Experienced replaces Dungeon.cycle with 64 bit
 * numbers on every monster. This game keeps ordinary ints, so the same idea is built as
 * multipliers on top of each monster's own stats. Nothing here does anything while
 * Dungeon.cycle is 0, which is every normal run.
 */
package com.shatteredpixel.shatteredpixeldungeon.mechanics;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;

public class Cycles {

	public static final int MAX_CYCLE = 5;

	//index is the cycle, 0 is the normal game
	private static final float[] HP     = { 1f, 3f, 9f, 27f, 80f, 240f };
	private static final float[] DAMAGE = { 1f, 2f, 4f, 8f, 16f, 32f };
	private static final float[] SKILL  = { 1f, 1.3f, 1.7f, 2.2f, 2.8f, 3.5f };
	private static final float[] EXP    = { 1f, 2f, 4f, 8f, 16f, 32f };
	//the hero grows too, so a cycle stays playable
	private static final float[] HERO   = { 1f, 2f, 4f, 7f, 12f, 20f };

	private static int c(){
		return Math.max(0, Math.min(MAX_CYCLE, Dungeon.cycle));
	}

	public static boolean active(){
		return Dungeon.cycle > 0;
	}

	private static boolean isEnemy(Char ch){
		return ch != null && ch != Dungeon.hero && ch.alignment == Char.Alignment.ENEMY;
	}

	//once per monster, called from Mob.act
	public static void scale( Mob m ){
		if (m.cycleScaled) return;
		m.cycleScaled = true;
		if (c() == 0 || m.alignment != Char.Alignment.ENEMY) return;
		m.HT = capInt(m.HT * HP[c()]);
		m.HP = capInt(m.HP * HP[c()]);
		m.EXP = capInt(m.EXP * EXP[c()]);
	}

	public static float damage( Char attacker ){
		if (c() == 0) return 1f;
		if (attacker == Dungeon.hero) return HERO[c()];
		return isEnemy(attacker) ? DAMAGE[c()] : 1f;
	}

	public static float skill( Char ch ){
		return c() != 0 && isEnemy(ch) ? SKILL[c()] : 1f;
	}

	private static int capInt( float v ){
		return (int)Math.min(Integer.MAX_VALUE / 4, Math.max(1, v));
	}
}
