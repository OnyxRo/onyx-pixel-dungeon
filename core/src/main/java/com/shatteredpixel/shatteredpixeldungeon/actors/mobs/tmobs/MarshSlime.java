/*
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tmobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.DamageTag;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.GooBlob;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.tmobs.MarshSlimeSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.EnumSet;

public class MarshSlime extends Mob {

	private static final String ADAPTED_DAMAGE_TYPE = "adapted_damage_type";

	public enum AdaptedDamageType {
		PHYSICAL,
		MAGICAL
	}

	private AdaptedDamageType adaptedDamageType;

	{
		spriteClass = MarshSlimeSprite.class;

		HP = HT = 200;
		defenseSkill = 20;

		EXP = 13;
		maxLvl = 30;

		loot = GooBlob.class;
		lootChance = 1f / 4f;

		properties.add(Property.DARKSLIME);
		properties.add(Property.LARGE);
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(15, 25);
	}

	@Override
	public int attackSkill(Char target) {
		return 40;
	}

	@Override
	public int drRoll() {
		return 0;
	}

	@Override
	public void damage( int damage, Object source ) {
		AdaptedDamageType incomingType = damageType(source);
		if (adaptedDamageType == incomingType) {
			super.damage(0, source);
			showImmunity();
			return;
		}

		int durabilityBefore = HP + shielding();
		super.damage(damage, source);
		int durabilityLost = Math.max(0, durabilityBefore - HP - shielding());
		if (adaptedDamageType == null && damage > 0 && durabilityLost > 0) {
			adaptedDamageType = incomingType;
		}
	}

	//This game has no damage tags, so a hit from a character or a weapon is physical and anything else is magical.
	protected AdaptedDamageType damageType(Object source) {
		return (source instanceof Char || source instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon
				|| source instanceof com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb)
				? AdaptedDamageType.PHYSICAL
				: AdaptedDamageType.MAGICAL;
	}

	protected void showImmunity() {
		if (sprite != null && sprite.visible) {
			sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "immune"));
		}
	}

	protected AdaptedDamageType adaptedDamageType() {
		return adaptedDamageType;
	}

	@Override
	public String description() {
		String typeKey = adaptedDamageType == AdaptedDamageType.MAGICAL
				? "magical_damage"
				: "physical_damage";
		if (adaptedDamageType == null) {
			return super.description() + "\n\n" + Messages.get(this, "not_adapted");
		}
		return super.description() + "\n\n"
				+ Messages.get(this, "immune_to", Messages.get(this, typeKey));
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		if (adaptedDamageType != null) {
			bundle.put(ADAPTED_DAMAGE_TYPE, adaptedDamageType.name());
		}
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		adaptedDamageType = null;
		if (!bundle.contains(ADAPTED_DAMAGE_TYPE)) {
			return;
		}
		try {
			adaptedDamageType = AdaptedDamageType.valueOf(bundle.getString(ADAPTED_DAMAGE_TYPE));
		} catch (IllegalArgumentException ignored) {
			// Old or damaged saves safely return to the unadapted state.
		}
	}
}
