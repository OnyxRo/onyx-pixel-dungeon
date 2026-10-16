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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Pistol extends GunWeapon {

	{
		image = getImage();
		LOADING_TIME = 3;
	}

	private static int getImage(){
		switch (Random.Int(1, 3)){
			case 1:
				return ItemSpriteSheet.PISTOL;
			case 2:
				return ItemSpriteSheet.SILVER_PISTOL;
			case 3:
				return ItemSpriteSheet.GOLDEN_PISTOL;
		}
		return ItemSpriteSheet.PISTOL;
	}

	@Override
	public int min(int lvl) {
		return lvl+1;
	}

	@Override
	public int max(int lvl) {
		return lvl+2;
	}

	@Override
	public int STRReq(int lvl) {
		return 10;
	}
	
	@Override
	public int proc( Char attacker, Char defender, int damage ) {
		Buff.prolong( defender, Cripple.class, Cripple.DURATION );
		return super.proc( attacker, defender, damage );
	}
	
	@Override
	public int value() {
		return 100 * quantity;
	}
}
