/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Helmets are from Ryuse's PD (Kotlin there, rewritten in Java here). The mod's helmets sit in a
 * head slot of their own and lean on its own damage system, so each effect here is written on
 * Shattered's damage instead.
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

package com.shatteredpixel.shatteredpixeldungeon.items.helmets;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.inscriptions.Inscription;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Helmet extends EquipableItem {

	protected Inscription inscription = null;

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean doEquip(Hero hero) {
		Helmet old = hero.belongings.helmet;
		if (old != null && old != this) {
			//swap with the helmet already worn, it goes back to the pack
			if (!old.doUnequip(hero, true, false)) {
				return false;
			}
		}

		detach(hero.belongings.backpack);
		hero.belongings.helmet = this;

		identify();
		cursedKnown = true;
		if (cursed) {
			equipCursed(hero);
			GLog.n(Messages.get(Helmet.class, "equip_cursed"));
		}
		activate(hero);
		hero.updateHT(false);
		Dungeon.observe();
		if (inscription != null && !inscription.curse()) com.shatteredpixel.shatteredpixeldungeon.Badges.validateInscribed();

		hero.spendAndNext(timeToEquip(hero));
		return true;
	}

	@Override
	public boolean doUnequip(Hero hero, boolean collect, boolean single) {
		if (super.doUnequip(hero, collect, single)) {
			if (hero.belongings.helmet == this) hero.belongings.helmet = null;
			hero.updateHT(false);
			Dungeon.observe();
			return true;
		}
		return false;
	}

	@Override
	public boolean isEquipped(Hero hero) {
		return hero != null && hero.belongings.helmet() == this;
	}

	@Override
	protected float timeToEquip(Hero hero) {
		return 2f / hero.speed();
	}

	@Override
	public Item random() {
		cursed = Random.Float() < 0.1f;
		if (!cursed && Random.Float() < 0.15f) {
			inscription = Inscription.randomInscription();
		}
		return this;
	}

	@Override
	public int value() {
		int price = 75;
		if (cursed && cursedKnown) {
			price /= 2;
		}
		return Math.max(1, price);
	}

	@Override
	public String name() {
		if (inscription != null && (cursedKnown || !inscription.curse())) {
			return inscription.name(super.name());
		}
		return super.name();
	}

	//false for the helmets that have nothing to describe
	protected boolean hasEffect() {
		return true;
	}

	@Override
	public String desc() {
		String desc = super.desc();
		if (isIdentified() && hasEffect()) {
			desc += "\n\n" + Messages.get(this, "effect_desc");
			if (cursed) {
				desc += "\n\n" + Messages.get(Helmet.class, "cursed_desc");
			}
		}
		return desc;
	}

	@Override
	public String info() {
		String info = super.info();
		if (inscription != null && (cursedKnown || !inscription.curse())) {
			info += "\n\n" + Messages.get(Helmet.class, "inscribed", inscription.name());
			info += "\n\n" + inscription.desc();
		}
		return info;
	}

	public void inscribe(Inscription ins) {
		inscription = ins;
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return inscription != null && (cursedKnown || !inscription.curse()) ? inscription.glowing() : null;
	}

	//==== effects, each helmet overrides what it needs. Cursed ones usually do the opposite ====

	//damage the hero deals, ranged is true for thrown weapons
	public int procGivenDamage(Char hero, Char enemy, int dmg, boolean ranged) {
		return dmg;
	}

	//damage the hero takes, src is whatever caused it
	public float procTakenDamage(Char hero, float dmg, Object src) {
		if (inscription != null) dmg = inscription.procTakenDamage(this, dmg, src);
		return dmg;
	}

	//changes how far the hero sees
	public int viewAmend() {
		return 0;
	}

	//below 1 is faster
	public float regenDelayMultiplier() {
		return 1f;
	}

	public float hungerDelayMultiplier() {
		return 1f;
	}

	public float wandChargeMultiplier() {
		return 1f;
	}

	public float accuracyMultiplier() {
		return 1f;
	}

	//a chance for the next attack to hit no matter what
	public boolean sureHit() {
		return false;
	}

	public void onKill(Hero hero, Char victim) {
	}

	public int maxHPFix(int hp) {
		return inscription != null ? inscription.mhpFix(hp) : hp;
	}

	//==== helpers for the rest of the game, all safe to call with no helmet ====

	public static Helmet worn(Char ch) {
		return ch instanceof Hero ? ((Hero) ch).belongings.helmet() : null;
	}

	public static int onAttackProc(Hero hero, Char enemy, int dmg) {
		Helmet h = worn(hero);
		if (h == null) return dmg;
		boolean ranged = hero.belongings.thrownWeapon != null;
		return Math.max(0, h.procGivenDamage(hero, enemy, dmg, ranged));
	}

	public static float onDamageTaken(Hero hero, float dmg, Object src) {
		Helmet h = worn(hero);
		if (h == null) return dmg;
		return Math.max(0, h.procTakenDamage(hero, dmg, src));
	}

	public static int viewDelta(Hero hero) {
		Helmet h = worn(hero);
		return h == null ? 0 : h.viewAmend();
	}

	public static float regenDelay(Char ch) {
		Helmet h = worn(ch);
		return h == null ? 1f : h.regenDelayMultiplier();
	}

	public static float hungerDelay(Char ch) {
		Helmet h = worn(ch);
		return h == null ? 1f : h.hungerDelayMultiplier();
	}

	public static float wandCharge(Char ch) {
		Helmet h = worn(ch);
		return h == null ? 1f : h.wandChargeMultiplier();
	}

	public static float accuracy(Hero hero) {
		Helmet h = worn(hero);
		return h == null ? 1f : h.accuracyMultiplier();
	}

	public static boolean guaranteedHit(Hero hero) {
		Helmet h = worn(hero);
		return h != null && h.sureHit();
	}

	public static int maxHP(Hero hero, int hp) {
		Helmet h = worn(hero);
		return h == null ? hp : h.maxHPFix(hp);
	}

	public static void kill(Hero hero, Char victim) {
		Helmet h = worn(hero);
		if (h != null) h.onKill(hero, victim);
	}

	//attacks from a distance, and from spell casters, are told apart here
	public static boolean isRanged(Char hero, Object src) {
		return src instanceof Char && !Dungeon.level.adjacent(hero.pos, ((Char) src).pos);
	}

	public static boolean isMagic(Object src) {
		if (src instanceof Wand) return true;
		if (!(src instanceof Char)) return false;
		String n = src.getClass().getSimpleName();
		return n.startsWith("Shaman") || n.startsWith("Warlock") || n.startsWith("DM100")
				|| n.startsWith("Eye") || n.startsWith("Elemental") || n.startsWith("Necromancer")
				|| n.startsWith("Spinner") && false;
	}

	private static final String INSCRIPTION = "inscription";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		if (inscription != null) bundle.put(INSCRIPTION, inscription);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(INSCRIPTION)) inscription = (Inscription) bundle.get(INSCRIPTION);
	}
}
