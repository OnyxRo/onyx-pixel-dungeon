/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.AscendedForm;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.PowerOfMany;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;

import java.util.ArrayList;

public abstract class ClericSpell {

	public abstract void onCast(HolyTome tome, Hero hero);

	public float chargeUse( Hero hero ){
		return 1;
	}

	public boolean canCast( Hero hero ){
		return true;
	}

	public String name(){
		return Messages.get(this, "name");
	}

	public String shortDesc(){
		return Messages.get(this, "short_desc") + " " + Messages.get(this, "charge_cost", (int)chargeUse(Dungeon.hero));
	}

	public String desc(){
		return Messages.get(this, "desc") + "\n\n" + Messages.get(this, "charge_cost", (int)chargeUse(Dungeon.hero));
	}

	public boolean usesTargeting(){
		return false;
	}

	public int targetingFlags(){
		return -1; //-1 for no targeting
	}

	public int icon(){
		return HeroIcon.NONE;
	}

	public void onSpellCast(HolyTome tome, Hero hero){
		Invisibility.dispel();
		if (hero.hasTalent(Talent.SATIATED_SPELLS) && hero.buff(Talent.SatiatedSpellsTracker.class) != null){
			int amount = 1 + 2*hero.pointsInTalent(Talent.SATIATED_SPELLS);
			Buff.affect(hero, Barrier.class).setShield(amount);
			Char ally = PowerOfMany.getPoweredAlly();
			if (ally != null && ally.buff(LifeLinkSpell.LifeLinkSpellBuff.class) != null){
				Buff.affect(ally, Barrier.class).setShield(amount);
			}
			hero.buff(Talent.SatiatedSpellsTracker.class).detach();
		}
		tome.spendCharge(chargeUse(hero));
		Talent.onArtifactUsed(hero);
		if (hero.subClass == HeroSubClass.PALADIN){
			if (this != HolyWeapon.INSTANCE && hero.buff(HolyWeapon.HolyWepBuff.class) != null){
				hero.buff(HolyWeapon.HolyWepBuff.class).extend(10*chargeUse(hero));
			}
			if (this != HolyWard.INSTANCE && hero.buff(HolyWard.HolyArmBuff.class) != null){
				hero.buff(HolyWard.HolyArmBuff.class).extend(10*chargeUse(hero));
			}
		}

		if (hero.buff(AscendedForm.AscendBuff.class) != null){
			hero.buff(AscendedForm.AscendBuff.class).spellCasts++;
			hero.buff(AscendedForm.AscendBuff.class).incShield((int)(10*chargeUse(hero)));
		}
	}

	public static ArrayList<ClericSpell> getSpellList(Hero cleric, int tier){
		ArrayList<ClericSpell> spells = new ArrayList<>();

		if (tier == 1) {

			spells.add(GuidingLight.INSTANCE);
			spells.add(HolyWeapon.INSTANCE);
			spells.add(HolyWard.INSTANCE);

			if (cleric.hasTalent(Talent.HOLY_INTUITION)) {
				spells.add(HolyIntuition.INSTANCE);
			}

			if (cleric.hasTalent(Talent.SHIELD_OF_LIGHT)) {
				spells.add(ShieldOfLight.INSTANCE);
			}
			addTransformationSpells(cleric, 1, spells);

		} else if (tier == 2) {

			if (cleric.hasTalent(Talent.RECALL_INSCRIPTION)){
				spells.add(RecallInscription.INSTANCE);
			}

			if (cleric.hasTalent(Talent.SUNRAY)){
				spells.add(Sunray.INSTANCE);
			}

			if (cleric.hasTalent(Talent.DIVINE_SENSE)) {
				spells.add(DivineSense.INSTANCE);
			}

			if (cleric.hasTalent(Talent.BLESS)){
				spells.add(BlessSpell.INSTANCE);
			}

			addTransformationSpells(cleric, 2, spells);
		} else if (tier == 3){

			if (cleric.subClass == HeroSubClass.PRIEST) {
				spells.add(Radiance.INSTANCE);

			} else if (cleric.subClass == HeroSubClass.PALADIN){
				spells.add(Smite.INSTANCE);
			}

			if (cleric.hasTalent(Talent.CLEANSE)){
				spells.add(Cleanse.INSTANCE);
			}

			if (cleric.hasTalent(Talent.HOLY_LANCE)){
				spells.add(HolyLance.INSTANCE);
			}
			if (cleric.hasTalent(Talent.HALLOWED_GROUND)){
				spells.add(HallowedGround.INSTANCE);
			}
			if (cleric.hasTalent(Talent.MNEMONIC_PRAYER)){
				spells.add(MnemonicPrayer.INSTANCE);
			}

			if (cleric.hasTalent(Talent.LAY_ON_HANDS)){
				spells.add(LayOnHands.INSTANCE);
			}
			if (cleric.hasTalent(Talent.AURA_OF_PROTECTION)){
				spells.add(AuraOfProtection.INSTANCE);
			}
			if (cleric.hasTalent(Talent.WALL_OF_LIGHT)){
				spells.add(WallOfLight.INSTANCE);
			}
			addTransformationSpells(cleric, 3, spells);

		} else if (tier == 4){

			if (cleric.hasTalent(Talent.DIVINE_INTERVENTION)){
				spells.add(DivineIntervention.INSTANCE);
			}
			if (cleric.hasTalent(Talent.JUDGEMENT)){
				spells.add(Judgement.INSTANCE);
			}
			if (cleric.hasTalent(Talent.FLASH)){
				spells.add(Flash.INSTANCE);
			}

			if (cleric.hasTalent(Talent.BODY_FORM)){
				spells.add(BodyForm.INSTANCE);
			}
			if (cleric.hasTalent(Talent.MIND_FORM)){
				spells.add(MindForm.INSTANCE);
			}
			if (cleric.hasTalent(Talent.SPIRIT_FORM)){
				spells.add(SpiritForm.INSTANCE);
			}

			if (cleric.hasTalent(Talent.BEAMING_RAY)){
				spells.add(BeamingRay.INSTANCE);
			}
			if (cleric.hasTalent(Talent.LIFE_LINK)){
				spells.add(LifeLinkSpell.INSTANCE);
			}
			if (cleric.hasTalent(Talent.STASIS)){
				spells.add(Stasis.INSTANCE);
			}

		}

		return spells;
	}

	public static ArrayList<ClericSpell> getAllSpells() {
		ArrayList<ClericSpell> spells = new ArrayList<>();
		spells.add(GuidingLight.INSTANCE);
		spells.add(HolyWeapon.INSTANCE);
		spells.add(HolyWard.INSTANCE);
		spells.add(HolyIntuition.INSTANCE);
		spells.add(ShieldOfLight.INSTANCE);
		spells.add(RecallInscription.INSTANCE);
		spells.add(Sunray.INSTANCE);
		spells.add(DivineSense.INSTANCE);
		spells.add(BlessSpell.INSTANCE);
		spells.add(Cleanse.INSTANCE);
		spells.add(Radiance.INSTANCE);
		spells.add(Smite.INSTANCE);
		spells.add(LayOnHands.INSTANCE);
		spells.add(AuraOfProtection.INSTANCE);
		spells.add(WallOfLight.INSTANCE);
		spells.add(HolyLance.INSTANCE);
		spells.add(HallowedGround.INSTANCE);
		spells.add(MnemonicPrayer.INSTANCE);
		spells.add(DivineIntervention.INSTANCE);
		spells.add(Judgement.INSTANCE);
		spells.add(Flash.INSTANCE);
		spells.add(BodyForm.INSTANCE);
		spells.add(MindForm.INSTANCE);
		spells.add(SpiritForm.INSTANCE);
		spells.add(BeamingRay.INSTANCE);
		spells.add(LifeLinkSpell.INSTANCE);
		spells.add(Stasis.INSTANCE);
		//transformation
		for (int t = 1; t <= 3; t++) spells.addAll(transformationSpells(t));
		return spells;
	}

	//transformation, the spells Transformation PD's spell talents add to the holy tome
	private static ArrayList<ClericSpell> transformationSpells(int tier){
		ArrayList<ClericSpell> s = new ArrayList<>();
		if (tier == 1){
			s.add(Ascension_Curse.INSTANCE); s.add(Shepherd_Intention.INSTANCE); s.add(Silver_Language.INSTANCE);
			s.add(Traitorous_Spell.INSTANCE); s.add(Focus_Light.INSTANCE); s.add(Credulous.INSTANCE);
		} else if (tier == 2){
			s.add(Divine_Protection.INSTANCE); s.add(Conversion_Holy.INSTANCE); s.add(Genesis.INSTANCE);
			s.add(Indulgence.INSTANCE); s.add(Sprint_Spell.INSTANCE); s.add(Thorn_Whip.INSTANCE);
			s.add(Revelation.INSTANCE); s.add(Equipment_Bless.INSTANCE); s.add(HotLight.INSTANCE);
		} else if (tier == 3){
			s.add(Purifying_Evil.INSTANCE); s.add(Divine_Storm.INSTANCE); s.add(Resurrection.INSTANCE);
			s.add(Zhuanyu_Spell.INSTANCE); s.add(Holy_Grenade.INSTANCE); s.add(Blade_Star.INSTANCE);
			s.add(Sacred_Blade.INSTANCE);
		}
		return s;
	}

	public static Talent transformationTalent(ClericSpell spell){
		if (spell == Ascension_Curse.INSTANCE) return Talent.ASCENSION_CURSE;
		if (spell == Shepherd_Intention.INSTANCE) return Talent.SHEPHERD_INTENTION;
		if (spell == Silver_Language.INSTANCE) return Talent.SILVER_LANGUAGE;
		if (spell == Traitorous_Spell.INSTANCE) return Talent.TRAITOROUS_SPELL;
		if (spell == Focus_Light.INSTANCE) return Talent.FOCUS_LIGHT;
		if (spell == Credulous.INSTANCE) return Talent.CREDULOUS;
		if (spell == Divine_Protection.INSTANCE) return Talent.DIVINE_PROTECTION;
		if (spell == Conversion_Holy.INSTANCE) return Talent.CONVERSION_HOLY;
		if (spell == Genesis.INSTANCE) return Talent.GENESIS;
		if (spell == Indulgence.INSTANCE) return Talent.INDULGENCE;
		if (spell == Sprint_Spell.INSTANCE) return Talent.SPRINT_SPELL;
		if (spell == Thorn_Whip.INSTANCE) return Talent.THORN_WHIP;
		if (spell == Revelation.INSTANCE) return Talent.REVELATION;
		if (spell == Equipment_Bless.INSTANCE) return Talent.EQUIPMENT_BLESS;
		if (spell == HotLight.INSTANCE) return Talent.HOTLIGHT;
		if (spell == Purifying_Evil.INSTANCE) return Talent.PURIFYING_EVIL;
		if (spell == Divine_Storm.INSTANCE) return Talent.DIVINE_STORM;
		if (spell == Resurrection.INSTANCE) return Talent.RESURRECTION;
		if (spell == Zhuanyu_Spell.INSTANCE) return Talent.ZHUANYU_SPELL;
		if (spell == Holy_Grenade.INSTANCE) return Talent.HOLY_GRENADE;
		if (spell == Blade_Star.INSTANCE) return Talent.BLADE_STAR;
		if (spell == Sacred_Blade.INSTANCE) return Talent.SACRED_BLADE;
		return null;
	}

	//the mod's spell talents sit at tiers 1 to 3, the tome shows each with the spells of its talent's tier
	private static void addTransformationSpells(Hero cleric, int tier, ArrayList<ClericSpell> spells){
		for (int t = 1; t <= 3; t++){
			for (ClericSpell s : transformationSpells(t)){
				Talent talent = transformationTalent(s);
				if (t == tier && talent != null && cleric.hasTalent(talent)) spells.add(s);
			}
		}
	}
}
