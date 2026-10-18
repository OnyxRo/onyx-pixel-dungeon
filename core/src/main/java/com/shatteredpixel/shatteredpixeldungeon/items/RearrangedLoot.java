/*
 * Onyx's Pixel Dungeon
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

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.items.Generator.Category;
import com.shatteredpixel.shatteredpixeldungeon.items.pills.*;
import com.shatteredpixel.shatteredpixeldungeon.items.spellbook.*;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.*;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.*;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.alchemy.*;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.bow.*;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.*;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.FT.*;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL.*;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.*;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.LG.*;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.*;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.RL.*;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.*;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.*;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.*;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.alchemy.*;

/**
 * Re-ARranged PD's loot table entries, appended to Generator's decks in one call so Generator only gains one line.
 * Weights are the mod's own (2 for melee weapons and bows, 1 for most guns).
 */
public class RearrangedLoot {

	private static void add( Category cat, Class<?>[] classes, float[] probs ){
		int n = cat.classes == null ? 0 : cat.classes.length;
		Class<?>[] c = new Class<?>[n + classes.length];
		if (n > 0) System.arraycopy(cat.classes, 0, c, 0, n);
		System.arraycopy(classes, 0, c, n, classes.length);
		cat.classes = c;
		float[] base = cat.defaultProbs != null ? cat.defaultProbs : (cat.probs != null ? cat.probs : new float[n]);
		float[] p = new float[n + probs.length];
		System.arraycopy(base, 0, p, 0, Math.min(n, base.length));
		System.arraycopy(probs, 0, p, n, probs.length);
		if (cat.defaultProbs != null) cat.defaultProbs = p;
		if (cat.defaultProbs2 != null) {
			float[] p2 = new float[n + probs.length];
			System.arraycopy(cat.defaultProbs2, 0, p2, 0, Math.min(n, cat.defaultProbs2.length));
			System.arraycopy(probs, 0, p2, n, probs.length);
			cat.defaultProbs2 = p2;
		}
		cat.probs = p.clone();
	}

	private static void set( Category cat, Class<?>[] classes, float[] probs ){
		cat.classes = classes;
		cat.defaultProbs = probs;
		cat.probs = probs.clone();
	}

	public static void apply(){
		add(Category.WEP_T1, new Class<?>[]{ WornKatana.class, AR_T1.class, Shovel.class, Machete.class, Scalpel.class, WornShortBow.class },
				new float[]{ 2, 2, 0, 0, 2, 2 });
		add(Category.WEP_T2, new Class<?>[]{ Nunchaku.class, DualDagger.class, Knife.class, ShortKatana.class, AR_T2.class, HG_T2.class, SMG_T2.class, ShortBow.class },
				new float[]{ 2, 2, 2, 2, 1, 1, 1, 2 });
		add(Category.WEP_T3, new Class<?>[]{ Bible.class, NormalKatana.class, AR_T3.class, MG_T3.class, SG_T3.class, SR_T3.class, GL_T3.class, Bow.class },
				new float[]{ 2, 2, 1, 1, 1, 1, 1, 2 });
		add(Category.WEP_T4, new Class<?>[]{ LongKatana.class, AR_T4.class, SMG_T4.class, RL_T4.class, LongBow.class },
				new float[]{ 2, 1, 1, 1, 2 });
		add(Category.WEP_T5, new Class<?>[]{ LargeSword.class, LargeKatana.class, AR_T5.class, HG_T5.class, SMG_T5.class, MG_T5.class, SG_T5.class,
						SR_T5.class, GL_T5.class, RL_T5.class, FT_T5.class, LG_T5.class, GreatBow.class },
				new float[]{ 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2 });

		set(Category.WEP_AL_T3, new Class<?>[]{ SpearNShield.class }, new float[]{ 0 });
		set(Category.WEP_AL_T4, new Class<?>[]{ UnholyBible.class }, new float[]{ 0 });
		set(Category.WEP_AL_T5, new Class<?>[]{ TrueRunicBlade.class, ChainWhip.class, UnformedBlade.class }, new float[]{ 0, 0, 0 });
		set(Category.WEP_AL_T6, new Class<?>[]{ AR_T6.class, SR_T6.class, HG_T6.class, GL_T6.class, RL_T6.class, ChainFlail.class, ForceGlove.class,
				Lance.class, ObsidianShield.class, DualGreatSword.class, HugeSword.class, MeisterHammer.class, BeamSaber.class, SharpKatana.class,
				AssassinsSpear.class }, new float[15]);
		set(Category.WEP_AL_T7, new Class<?>[]{ LanceNShield.class, TacticalShield.class, HolySword.class }, new float[]{ 0, 0, 0 });
		set(Category.WEP_SP, new Class<?>[]{ BattleShovel.class, MinersTool.class, EnhancedMachete.class }, new float[]{ 0, 0, 0 });
		set(Category.MIS_SP, new Class<?>[]{ Cross.class, PotOThunder.class }, new float[]{ 0, 0 });

		set(Category.SPELLBOOK, new Class<?>[]{ BookOfMagic.class, BookOfThunderBolt.class, BookOfDisintegration.class, BookOfFire.class,
				BookOfCorrosion.class, BookOfBlast.class, BookOfEarth.class, BookOfFrost.class, BookOfLight.class, BookOfWarding.class,
				BookOfTransfusion.class, BookOfCorruption.class, BookOfRegrowth.class },
				new float[]{ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
		set(Category.PILL, new Class<?>[]{ PillOfAwakening.class, PillOfHealing.class, PillOfMindFocus.class, PillOfFrost.class, PillOfFlame.class,
				PillOfToxin.class, PillOfAcceleration.class, PillOfInvisibility.class, PillOfLevitation.class, PillOfParalysis.class,
				PillOfAntibiotics.class, PillOfVitamin.class },
				new float[]{ 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1 });
		set(Category.POSITIVE_PILL, new Class<?>[]{ PillOfHealing.class, PillOfMindFocus.class, PillOfAcceleration.class, PillOfInvisibility.class,
				PillOfLevitation.class, PillOfAntibiotics.class, PillOfVitamin.class },
				new float[]{ 2, 2, 2, 2, 2, 2, 1 });

		add(Category.TRINKET, new Class<?>[]{ MagicalCompass.class, CrystalBall.class, BrokenMagnifyingGlass.class, SuspiciousKey.class,
				PinkGem.class, Necklace.class, GrindStone.class }, new float[]{ 1, 1, 1, 1, 1, 1, 1 });
	}
}
