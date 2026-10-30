/*
 * Music Implant SPD, ported into Onyx's Pixel Dungeon (key musicimplant)
 * https://github.com/CatAzreal/Jinkeloid-MISPD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.lightsource.Candle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.ArrayList;

//refills a candle by 300 turns, the emptiest candle in the pack first
public class WaxNWick extends Item {

	public static final String AC_RECHARGE = "RECHARGE";

	public static final float TIME_TO_RECHARGE = 2;

	{
		image = ItemSpriteSheet.WAX_N_STRING;
		stackable = true;
		defaultAction = AC_RECHARGE;
	}

	private static Candle candleOf( Hero hero ){
		Candle best = null;
		for (Item i : hero.belongings){
			if (i instanceof Candle && (best == null || ((Candle) i).charge < best.charge)) best = (Candle) i;
		}
		return best;
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_RECHARGE);
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {
		super.execute(hero, action);
		if (action.equals(AC_RECHARGE)){
			Candle candle = candleOf(hero);
			if (candle == null){
				GLog.w(Messages.get(this, "no_candle"));
				return;
			}
			candle.addCharge(300);
			hero.spend(TIME_TO_RECHARGE);
			hero.busy();
			hero.sprite.operate(hero.pos);
			GLog.i(Messages.get(this, "refilled"));
			detach(hero.belongings.backpack);
		}
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
		return 6 * quantity;
	}
}
