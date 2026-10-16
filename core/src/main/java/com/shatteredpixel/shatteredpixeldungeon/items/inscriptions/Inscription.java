/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * From Ryuse's PD. This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.items.inscriptions;

import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.inscriptions.good.AntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.inscriptions.good.Healthy;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

//Ryuse's PD, a small enchantment for helmets
public abstract class Inscription implements Bundlable {

	public float procTakenDamage(EquipableItem equipment, float dmg, Object src) {
		return dmg;
	}

	public int mhpFix(int mhp) {
		return mhp;
	}

	public abstract ItemSprite.Glowing glowing();

	public String name(String equipmentName) {
		return Messages.get(this, "name", equipmentName);
	}

	public String name() {
		return name(curse() ? Messages.get(Item.class, "curse") : Messages.get(this, "inscription"));
	}

	public String desc() {
		return Messages.get(this, "desc");
	}

	public boolean curse() {
		return false;
	}

	private static final Class<?>[] INSCRIPTIONS = {AntiMagic.class, Healthy.class};

	public static Inscription randomInscription() {
		try {
			return (Inscription) Random.element(INSCRIPTIONS).newInstance();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void storeInBundle(Bundle bundle) {
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
	}
}
