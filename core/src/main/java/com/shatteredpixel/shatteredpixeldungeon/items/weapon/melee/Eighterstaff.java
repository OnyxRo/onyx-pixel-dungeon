/*
 * Clairvoyant Pixel Dungeon, ported into Onyx's Pixel Dungeon (key clairvoyant)
 * https://github.com/ifritdiezel/Clairvoyant-Pixel-Dungeon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

//the Apprentice's starting staff, half a quarterstaff, tier 1 with 1 extra defence
public class Eighterstaff extends MeleeWeapon {

	{
		image = ItemSpriteSheet.EIGHTERSTAFF;
		hitSound = Assets.Sounds.HIT_CRUSH;
		hitSoundPitch = 1f;
		tier = 1;
		bones = false;
	}

	@Override
	public int max( int lvl ) {
		return 4*(tier+1) +    //8 base
				lvl*(tier+1);  //scaling unchanged
	}

	@Override
	public int defenseFactor( Char owner ) {
		return 1;
	}
}
