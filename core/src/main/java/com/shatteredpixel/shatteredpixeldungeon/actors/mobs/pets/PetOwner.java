/*
 * Onyx's Pixel Dungeon
 * The pet owner's numbers from Goblins Pixel Dungeon (locastan). The mod kept them as fields on its Hero (haspet,
 * petfollow, petType, petLevel, petKills, petHP, petExperience, petCooldown, petCount). Here they sit in this
 * hidden buff on the hero, which saves with the hero and needs no change to Hero itself. Each turn it counts the
 * turns any pet egg spends in the pack and copies the current pet's numbers, as the mod did on the stairs.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.pets.PetEggItem;
import com.watabou.utils.Bundle;

public class PetOwner extends Buff {

	{
		type = buffType.NEUTRAL;
		revivePersists = true;
	}

	public boolean haspet = false;
	public boolean petfollow = false;
	public int petType = 0;
	public int petLevel = 0;
	public int petKills = 0;
	public int petHP = 0;
	public int petExperience = 0;
	public int petCooldown = 0;
	public int petCount = 0;

	@Override
	public boolean act() {
		if (target instanceof Hero) {
			for (PetEggItem egg : ((Hero) target).belongings.getAllItems(PetEggItem.class)) egg.moves++;
		}
		PET pet = Dungeon.level == null ? null : PET.currentPet();
		if (pet != null) {
			haspet = true;
			remember(pet);
		}
		spend(TICK);
		return true;
	}

	public void remember( PET pet ) {
		petType = pet.type;
		petLevel = pet.level;
		petKills = pet.kills;
		petHP = pet.HP;
		petExperience = pet.experience;
		petCooldown = pet.cooldown;
	}

	/** Called when the pet dies or leaves, so a new one can hatch. */
	public static void petGone( boolean died ) {
		if (Dungeon.hero == null) return;
		PetOwner owner = Dungeon.hero.buff(PetOwner.class);
		if (owner == null) return;
		owner.haspet = false;
		owner.petfollow = false;
		if (died) owner.petCount++;
	}

	private static final String HASPET = "haspet";
	private static final String PETFOLLOW = "petfollow";
	private static final String PETTYPE = "petType";
	private static final String PETLEVEL = "petLevel";
	private static final String PETKILLS = "petKills";
	private static final String PETHP = "petHP";
	private static final String PETEXP = "petExperience";
	private static final String PETCOOLDOWN = "petCooldown";
	private static final String PETCOUNT = "petCount";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put(HASPET, haspet);
		bundle.put(PETFOLLOW, petfollow);
		bundle.put(PETTYPE, petType);
		bundle.put(PETLEVEL, petLevel);
		bundle.put(PETKILLS, petKills);
		bundle.put(PETHP, petHP);
		bundle.put(PETEXP, petExperience);
		bundle.put(PETCOOLDOWN, petCooldown);
		bundle.put(PETCOUNT, petCount);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		haspet = bundle.getBoolean(HASPET);
		petfollow = bundle.getBoolean(PETFOLLOW);
		petType = bundle.getInt(PETTYPE);
		petLevel = bundle.getInt(PETLEVEL);
		petKills = bundle.getInt(PETKILLS);
		petHP = bundle.getInt(PETHP);
		petExperience = bundle.getInt(PETEXP);
		petCooldown = bundle.getInt(PETCOOLDOWN);
		petCount = bundle.getInt(PETCOUNT);
	}
}
