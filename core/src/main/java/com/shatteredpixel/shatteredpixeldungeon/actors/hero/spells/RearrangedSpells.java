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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;

import java.util.ArrayList;

/**
 * The holy tome spells of Re-ARranged PD's rework of the Cleric. Divine Blast at tier 2, three more spells each for
 * the Priest and the Paladin, and the Enchanter's Spell Burst and six spells. ClericSpell calls these.
 */
public class RearrangedSpells {

	public static void addSpells( Hero cleric, int tier, ArrayList<ClericSpell> spells ){
		if (tier == 2) {
			if (cleric.hasTalent(Talent.DIVINE_BLAST)) spells.add(DivineBlast.INSTANCE);
		} else if (tier == 3) {
			if (cleric.subClass == HeroSubClass.ENCHANTER) spells.add(SpellBurst.INSTANCE);
			if (cleric.hasTalent(Talent.DIVINE_RAY)) spells.add(DivineRay.INSTANCE);
			if (cleric.hasTalent(Talent.HOLY_BOMB)) spells.add(HolyBombSpell.INSTANCE);
			if (cleric.hasTalent(Talent.RA_RESURRECTION)) spells.add(RaResurrection.INSTANCE);
			if (cleric.hasTalent(Talent.HOLY_MANTLE)) spells.add(HolyMantle.INSTANCE);
			if (cleric.hasTalent(Talent.POWER_OF_LIFE)) spells.add(PowerOfLife.INSTANCE);
			if (cleric.hasTalent(Talent.INDUCE_AGGRO)) spells.add(InduceAggro.INSTANCE);
			if (cleric.hasTalent(Talent.TIME_AMP)) spells.add(TimeAmp.INSTANCE);
			if (cleric.hasTalent(Talent.WEAKENING_HEX)) spells.add(WeakeningHex.INSTANCE);
			if (cleric.hasTalent(Talent.STUN)) spells.add(Stun.INSTANCE);
			if (cleric.hasTalent(Talent.THUNDER_IMBUE)) spells.add(ThunderImbueSpell.INSTANCE);
			if (cleric.hasTalent(Talent.ARCANE_ARMOR)) spells.add(ArcaneArmorSpell.INSTANCE);
			if (cleric.hasTalent(Talent.ENCHANT)) spells.add(Enchant.INSTANCE);
		}
	}

	public static ArrayList<ClericSpell> allSpells(){
		ArrayList<ClericSpell> spells = new ArrayList<>();
		spells.add(DivineBlast.INSTANCE);
		spells.add(HolyMantle.INSTANCE);
		spells.add(PowerOfLife.INSTANCE);
		spells.add(InduceAggro.INSTANCE);
		spells.add(DivineRay.INSTANCE);
		spells.add(HolyBombSpell.INSTANCE);
		spells.add(RaResurrection.INSTANCE);
		spells.add(SpellBurst.INSTANCE);
		spells.add(TimeAmp.INSTANCE);
		spells.add(WeakeningHex.INSTANCE);
		spells.add(Stun.INSTANCE);
		spells.add(ThunderImbueSpell.INSTANCE);
		spells.add(ArcaneArmorSpell.INSTANCE);
		spells.add(Enchant.INSTANCE);
		return spells;
	}
}
