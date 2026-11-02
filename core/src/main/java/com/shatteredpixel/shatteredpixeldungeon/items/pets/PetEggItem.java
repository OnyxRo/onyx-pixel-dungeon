/*
 * Onyx's Pixel Dungeon
 * The pet eggs of Goblins Pixel Dungeon (locastan), shared part. An egg counts what happens to it (turns carried
 * in the pack, fires, frosts, poison, lightning and summoning traps on the floor where it lies), and breaking it
 * open hatches the pet that those counts call for, or nothing if it never developed. Shaking it hints whether
 * something is alive inside. Only one pet at a time, the owner's pet numbers live in the PetOwner buff.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.items.pets;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.GoblinsPets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.PET;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.PetOwner;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public abstract class PetEggItem extends Item {

	public static final float TIME_TO_USE = 1;

	public static final String AC_BREAK = "BREAK";
	public static final String AC_SHAKE = "SHAKE";

	{
		unique = true;
		stackable = false;
	}

	public int moves = 0;
	public int burns = 0;
	public int freezes = 0;
	public int poisons = 0;
	public int lits = 0;
	public int summons = 0;

	/** The pet these counts would hatch, or null when nothing developed. */
	public abstract PET hatchling();

	/** The message for a shake when something lives inside. */
	protected abstract String shakeAlive();

	public void count( int kind ) {
		switch (kind) {
			case GoblinsPets.BURN:   burns++;   break;
			case GoblinsPets.FREEZE: freezes++; break;
			case GoblinsPets.POISON: poisons++; break;
			case GoblinsPets.LIT:    lits++;    break;
			case GoblinsPets.SUMMON: summons++; break;
		}
	}

	@Override
	public boolean collect( Bag container ) {
		if (!super.collect(container)) return false;
		//the pack counts the turns it is carried through the owner buff
		if (Dungeon.hero != null && container == Dungeon.hero.belongings.backpack) Buff.affect(Dungeon.hero, PetOwner.class);
		return true;
	}

	@Override
	public boolean doPickUp( Hero hero, int pos ) {
		GLog.w(Messages.get(PetEggItem.class, "likes_pack"));
		if (hero.belongings.getItem(getClass()) != null) GLog.w(Messages.get(PetEggItem.class, "only_one"));
		return super.doPickUp(hero, pos);
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_BREAK);
		actions.add(AC_SHAKE);
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {
		super.execute(hero, action);
		if (action.equals(AC_BREAK)) {
			PET pet = hatchling();
			if (pet == null) {
				detach(hero.belongings.backpack);
				GLog.w(Messages.get(this, "yolk"));
				hero.spendAndNext(TIME_TO_USE);
				return;
			}
			hatch(hero, pet);
		} else if (action.equals(AC_SHAKE)) {
			GLog.w(hatchling() != null ? shakeAlive() : Messages.get(PetEggItem.class, "slosh"));
		}
	}

	/** Hatches the pet next to the hero. Returns false when the pet could not come out yet. */
	public boolean hatch( Hero hero, PET pet ) {
		PetOwner owner = Buff.affect(hero, PetOwner.class);
		int cell = spawnCell(hero);
		if (cell == -1 || owner.haspet || PET.currentPet() != null) {
			hero.spendAndNext(TIME_TO_USE);
			GLog.w(Messages.get(PetEggItem.class, "not_ready"));
			return false;
		}

		pet.spawn(1);
		pet.HP = pet.HT;
		pet.pos = cell;
		pet.state = pet.WANDERING;
		GameScene.add(pet);
		if (pet.sprite != null && pet.sprite.parent != null) {
			pet.sprite.alpha(0);
			pet.sprite.parent.add(new AlphaTweener(pet.sprite, 1, 0.15f));
		}

		detach(hero.belongings.backpack);
		GLog.p(Messages.get(PetEggItem.class, "hatch"));
		owner.haspet = true;
		owner.petfollow = true;
		owner.remember(pet);
		hero.spendAndNext(TIME_TO_USE);
		return true;
	}

	private static int spawnCell( Hero hero ) {
		ArrayList<Integer> candidates = new ArrayList<>();
		for (int n : PathFinder.NEIGHBOURS8) {
			int c = hero.pos + n;
			if (Dungeon.level.passable[c] && Actor.findChar(c) == null) candidates.add(c);
		}
		return candidates.isEmpty() ? -1 : Random.element(candidates);
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public int value() {
		return 500 * quantity;
	}

	private static final String MOVES = "moves";
	private static final String BURNS = "burns";
	private static final String FREEZES = "freezes";
	private static final String POISONS = "poisons";
	private static final String LITS = "lits";
	private static final String SUMMONS = "summons";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put(MOVES, moves);
		bundle.put(BURNS, burns);
		bundle.put(FREEZES, freezes);
		bundle.put(POISONS, poisons);
		bundle.put(LITS, lits);
		bundle.put(SUMMONS, summons);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		moves = bundle.getInt(MOVES);
		burns = bundle.getInt(BURNS);
		freezes = bundle.getInt(FREEZES);
		poisons = bundle.getInt(POISONS);
		lits = bundle.getInt(LITS);
		summons = bundle.getInt(SUMMONS);
	}
}
