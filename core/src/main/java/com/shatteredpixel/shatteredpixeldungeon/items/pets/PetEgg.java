/*
 * Onyx's Pixel Dungeon
 * The egg from Goblins Pixel Dungeon (locastan), the mod's EasterEgg, found now and then in garden rooms.
 * A summoning trap on it hatches a Fairy (a Sugarplum Fairy in December), a fire hatches a Velocirooster and
 * ten turns in the pack hatch a Bunny.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.items.pets;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.Bunny;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.Fairy;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.PET;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.PetVelocirooster;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.SugarplumFairy;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.Calendar;

public class PetEgg extends PetEggItem {

	public static final int VELOCIROOSTER = 1;
	public static final int FAIRY = 1;
	public static final int BUNNY = 10;

	{
		image = ItemSpriteSheet.GOBLINS_PET_EGG;
	}

	@Override
	public PET hatchling() {
		if (summons >= FAIRY) {
			return Calendar.getInstance().get(Calendar.MONTH) == Calendar.DECEMBER ? new SugarplumFairy() : new Fairy();
		}
		if (burns >= VELOCIROOSTER) return new PetVelocirooster();
		if (moves >= BUNNY) return new Bunny();
		return null;
	}

	@Override
	protected String shakeAlive() {
		return Messages.get(this, hatchling() instanceof PetVelocirooster ? "kicks" : "scratch");
	}
}
