/*
 * Onyx's Pixel Dungeon
 * The klik soulorb from Goblins Pixel Dungeon (locastan), the mod's Egg. Which klik hatches depends on what the
 * orb went through. Five frosts make a Frost Klik (one in December), five poisonings a Poison Klik, five lightning
 * strikes a Phase Klik, fifteen fires a Fire Klik, a thousand turns in the pack a Metal Klik and two hundred a
 * Spider Klik.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.items.pets;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.FireKlik;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.FrostKlik;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.MetalKlik;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.PET;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.PhaseKlik;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.PoisonKlik;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.SpiderKlik;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.Calendar;

public class KlikSoulorb extends PetEggItem {

	public static final int FIREKLIK = 15;
	public static final int PHASEKLIK = 5;
	public static final int POISONKLIK = 5;
	public static final int SPIDER = 200;
	public static final int KLIK = 1000;

	public static int frostKlik() {
		return Calendar.getInstance().get(Calendar.MONTH) == Calendar.DECEMBER ? 1 : 5;
	}

	{
		image = ItemSpriteSheet.GOBLINS_KLIK_SOULORB;
	}

	@Override
	public PET hatchling() {
		if (freezes >= frostKlik()) return new FrostKlik();
		if (poisons >= POISONKLIK) return new PoisonKlik();
		if (lits >= PHASEKLIK) return new PhaseKlik();
		if (burns >= FIREKLIK) return new FireKlik();
		if (moves >= KLIK) return new MetalKlik();
		if (moves >= SPIDER) return new SpiderKlik();
		return null;
	}

	@Override
	protected String shakeAlive() {
		//only the spider klik squirms, every other klik clicks back
		PET p = hatchling();
		return Messages.get(this, p instanceof SpiderKlik ? "slithers" : "kicks");
	}
}
