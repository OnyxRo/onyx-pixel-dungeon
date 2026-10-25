/*
 * Too Cruel Pixel Dungeon Trials (GPL-3.0), ported into Onyx's Pixel Dungeon.
 * Based on Shattered Pixel Dungeon by Evan Debenham.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.toocruel;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;

//the buff interfaces Too Cruel's hooks look for, read by TooCruel.java
public class TcBuff {

	public interface Timescale {
		float speedFactor();
	}

	//multiplier on damage the holder takes
	public interface DamageAmp {
		float damageMultiplier( Object src );
	}

	public interface DefRoll {
		float defRollMultiplier( Char attacker );
	}

	public interface AttackAmp {
		default float flatAttackBonus(){ return 0; }
		default float attackMultiplier(){ return 1; }
		default float flatAttackBonusPostMult(){ return 0; }
	}

	public interface AttackProc {
		void attackProc( Char enemy, int damage );
	}

	public interface DefenseProc {
		void defenseProc( Char enemy, int damage );
	}

	public interface Invulnerable {
		boolean isInvulnerable( Class<?> effect );
	}

	public interface OnDeath {
		void onDeathProc();
	}

	public interface OnDamageTaken {
		void onDamageTaken( int damage, Object src );
	}

	//effects that carry over to a monster that comes out of a mimic
	public interface PersistNesting {
		void applyNestingEffect( Char target );
	}

	public interface DrRoll {
		int drRollBonus();
		default float drMultBonus(){ return 1; }
	}

	public interface HtBoost {
		float htMultiplier();
	}

	public interface SwitchLevel {
		void onSwitchLevel();
	}

	public interface XpMultiplier {
		float xpMultiplier( Class<?> source );
	}

	public interface OnBossSlain {
		void onBossSlain();
	}

	//monsters with this buff are seen by the hero as if with mind vision, in a small radius
	public interface Revealed {
	}
}
