/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
 *
 * Lovecraft Pixel Dungeon
 * Copyright (C) 2016-2017 Leon Horn
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This Program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without eben the implied warranty of
 * GNU General Public License for more details.
 *
 * You should have have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses>
 */

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.guns;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Rifle extends GunWeapon {

	{
		image = getImage();
		LOADING_TIME = 2;
	}

	private static int getImage(){
		switch (Random.Int(1, 2)){
			case 1:
				return ItemSpriteSheet.RIFLE;
			case 2:
				return ItemSpriteSheet.LONG_RIFLE;
		}
		return ItemSpriteSheet.RIFLE;
	}

	@Override
	public int min(int lvl) {
		return lvl+2;
	}

	@Override
	public int max(int lvl) {
		return lvl+3;
	}

	@Override
	public int STRReq(int lvl) {
		return 15;
	}
	
	@Override
	public int proc( Char attacker, Char defender, int damage ) {
		Buff.prolong( defender, Cripple.class, Cripple.DURATION );
		Buff.affect( defender, Bleeding.class);
		return super.proc( attacker, defender, damage );
	}
	
	@Override
	public int value() {
		return 100 * quantity;
	}
}
