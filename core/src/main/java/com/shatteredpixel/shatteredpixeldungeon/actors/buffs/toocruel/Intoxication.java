/*
 * Too Cruel Pixel Dungeon Trials (GPL-3.0), ported into Onyx's Pixel Dungeon.
 * Based on Shattered Pixel Dungeon by Evan Debenham.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.toocruel;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.TooCruel;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

//Intoxication, Plague and Bad Plumbing, a level that brings random bad effects as it rises
public class Intoxication extends Buff {

	{
		type = buffType.NEGATIVE;
	}

	public static final float POTION_INTOXICATION = 55f;
	public static final float EXOTIC_INTOXICATION = 80f;
	private static final float BASE = 50f;
	private static final float DANGER_1 = BASE * 2f;
	private static final float DANGER_2 = BASE * 3f;
	private static final float DANGER_3 = BASE * 4f;
	private static final float DANGER_4 = BASE * 5f;

	protected float level = 0f;

	@Override
	public boolean act() {
		if (level >= DANGER_1) {
			if (level > DANGER_2 || Random.Float() < (level - DANGER_1) / (DANGER_2 - DANGER_1)) {
				applyMinor( target );
			}
			if (level >= DANGER_2) {
				if (level > DANGER_3 || Random.Float() < (level - DANGER_3) / (DANGER_3 - DANGER_2)) {
					applyMajor( target );
				}
				if (level > DANGER_3 || Random.Float() < (level - DANGER_3) / (DANGER_3 - DANGER_2)) {
					applyMinor( target );
				}
				if (level >= DANGER_3) {
					if (level > DANGER_4 || Random.Float() < (level - DANGER_3) / (DANGER_4 - DANGER_3)) {
						applyMajor( target );
					}
				}
			}
		}
		int rnd = Random.Int( 4, 6 );
		if (level <= DANGER_2) {
			rnd = (int) (rnd * 1.6f);
		} else if (level <= DANGER_3) {
			rnd = (int) (rnd * 1.2f);
		}
		level -= rnd;
		if (level <= 0) detach();
		spend( rnd );
		return true;
	}

	public String toxicLevel() {
		String l;
		if (level > DANGER_4) l = "t_deadly";
		else if (level > DANGER_3) l = "t_heavy";
		else if (level > DANGER_2) l = "t_medium";
		else if (level > DANGER_1) l = "t_light";
		else l = "t_none";
		return Messages.get( this, l );
	}

	@Override
	public String desc() {
		return Messages.get( this, "desc", (int) level, toxicLevel() );
	}

	@Override
	public String iconTextDisplay() {
		return Integer.toString( (int) level );
	}

	@Override
	public int icon() {
		return BuffIndicator.POISON;
	}

	@Override
	public void tintIcon( Image icon ) {
		if (level > DANGER_4) icon.hardlight( 0x8000ff );
		else if (level > DANGER_3) icon.hardlight( 0xff0000 );
		else if (level > DANGER_2) icon.hardlight( 0xffaa00 );
		else if (level > DANGER_1) icon.hardlight( 0xffee00 );
		else icon.hardlight( 0x00ff00 );
	}

	public void extend( float amount ) {
		float previous = level;
		level += amount;
		if (previous < DANGER_1 && level >= DANGER_1) {
			GLog.w( Messages.get( this, "notify_light" ) );
		} else if (previous < DANGER_2 && level >= DANGER_2) {
			GLog.n( Messages.get( this, "notify_medium" ) );
		} else if (previous < DANGER_3 && level >= DANGER_3) {
			GLog.n( Messages.get( this, "notify_heavy" ) );
			Sample.INSTANCE.play( Assets.Sounds.HEALTH_WARN, 1f, 1.5f );
		} else if (previous < DANGER_4 && level >= DANGER_4) {
			GLog.n( Messages.get( this, "notify_deadly" ) );
			Sample.INSTANCE.play( Assets.Sounds.HEALTH_CRITICAL, 1f, 1.5f );
		}
	}

	//Plague, intoxication from damage taken, a quarter of health lost is one base level
	public void processHit( int damage, Object source ) {
		if (damage <= 0 || target.HT <= 0) return;
		if (source instanceof com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger
				|| source instanceof com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning
				|| source instanceof com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob
				|| source instanceof com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Viscosity.DeferedDamage
				|| source instanceof RacingTheDeath) {
			return;
		}
		float power = 1f * damage / target.HT;
		power *= 4 * BASE;
		power /= 1.5f;
		power += damage;
		extend( power );
	}

	private static final String LEVEL = "level";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( LEVEL, level );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		level = bundle.getFloat( LEVEL );
	}

	public static void applyMajor( Char targ ) {
		switch (Random.Int( 6 )) {
			case 0: Buff.affect( targ, Corrosion.class ).set( Random.NormalFloat( 3f, 5f ), targ.HT / 20 ); break;
			case 1: Buff.prolong( targ, Paralysis.class, Random.NormalFloat( 7f, 13f ) ); break;
			case 2: Buff.prolong( targ, Weakness.class, Random.NormalFloat( 15f, 25f ) ); break;
			case 3: Buff.prolong( targ, Frost.class, Random.NormalFloat( 15f, 25f ) ); break;
			case 4: Buff.prolong( targ, Slow.class, Random.NormalFloat( 20f, 35f ) ); break;
			case 5: Buff.prolong( targ, Degrade.class, Random.NormalFloat( 15f, 25f ) ); break;
		}
	}

	public static void applyMinor( Char targ ) {
		switch (Random.Int( 6 )) {
			case 0: Buff.affect( targ, Vulnerable.class, Random.Float( 9f, 15f ) ); break;
			case 1: Buff.prolong( targ, Blindness.class, Random.Float( 9f, 15f ) ); break;
			case 2: Buff.affect( targ, Chill.class, Random.NormalFloat( 7f, 13f ) ); break;
			case 3:
				if (TooCruel.on( TooCruel.RACING_THE_DEATH )) applyMinor( targ );
				else Buff.prolong( targ, Vertigo.class, Random.NormalFloat( 7f, 13f ) );
				break;
			case 4: Buff.prolong( targ, Cripple.class, Random.NormalFloat( 7f, 13f ) ); break;
			case 5: Buff.prolong( targ, Roots.class, Random.NormalFloat( 7f, 13f ) ); break;
		}
	}

	//Bad Plumbing, standing in water raises the level
	public static class ToxicWaterTracker extends Buff {
		@Override
		public boolean act() {
			if (Dungeon.level.map[target.pos] == Terrain.WATER) {
				Buff.affect( target, Intoxication.class ).extend( Random.NormalIntRange( 5, 10 ) );
			}
			spend( TICK );
			return true;
		}
	}
}
