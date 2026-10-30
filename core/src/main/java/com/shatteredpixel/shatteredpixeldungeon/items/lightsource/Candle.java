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

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

//a dim but long lasting light, 1500 turns, refilled with wax and wick
public class Candle extends Lightsource {

	{
		image = ItemSpriteSheet.MI_CANDLE;
		maxCharge = charge = 1500;
		active = false;
	}

	@Override
	public int brightness() {
		return 1;
	}

	@Override
	public void updateSprite() {
		image = isActivated() ? ItemSpriteSheet.MI_CANDLELIT : ItemSpriteSheet.MI_CANDLE;
	}

	@Override
	public int value() {
		return 30;
	}
}
