/*
 * Music Implant SPD, ported into Onyx's Pixel Dungeon (key musicimplant)
 * https://github.com/CatAzreal/Jinkeloid-MISPD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.items.lightsource;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

//a brighter light than the candle that burns for 500 turns and is gone after
public class FireTorch extends Lightsource {

	{
		image = ItemSpriteSheet.MI_TORCH;
		maxCharge = charge = 500;
		active = false;
	}

	@Override
	public int brightness() {
		return 2;
	}

	@Override
	public void updateSprite() {
		image = isActivated() ? ItemSpriteSheet.MI_TORCHLIT : ItemSpriteSheet.MI_TORCH;
	}

	@Override
	public void deactivate( Hero hero, boolean voluntary ) {
		super.deactivate(hero, voluntary);
		if (charge <= 0){
			detachAll(hero.belongings.backpack);
			GLog.i(Messages.get(this, "depleted"));
		}
	}

	@Override
	public int value() {
		return 25;
	}
}
