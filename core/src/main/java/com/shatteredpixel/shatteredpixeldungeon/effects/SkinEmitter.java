/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Random;

//Shittened PD added five fields to watabou's Emitter so a skin's particles can spread around the middle
//of the item. Here they live in a subclass, so the shared Emitter stays as it is.
public class SkinEmitter extends Emitter {

	public boolean randomSize = false;
	public float randomWidth = 1;
	public float randomHeight = 1;
	public float centerOffsetX = 0;
	public float centerOffsetY = 0;

	@Override
	protected void emit( int index ) {
		if (!randomSize) {
			super.emit(index);
		} else if (target == null) {
			factory.emit(this, index,
					x + Random.Float(-randomWidth, randomWidth) + centerOffsetX + width / 2,
					y + Random.Float(-randomHeight, randomHeight) + centerOffsetY + height / 2);
		} else {
			factory.emit(this, index,
					target.x + Random.Float(-randomWidth, randomWidth) + centerOffsetX + target.width / 2,
					target.y + Random.Float(-randomHeight, randomHeight) + centerOffsetY + target.height / 2);
		}
	}
}
