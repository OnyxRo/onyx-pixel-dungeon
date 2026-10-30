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
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

//a tier 3 bracelet that throws a cone of electricity past the enemy it hits
public class PowerBracelet extends MeleeWeapon {

	{
		image = ItemSpriteSheet.POWER_BRACELET;
		hitSound = Assets.Sounds.HIT;
		hitSoundPitch = 0.7f;
		tier = 3;
	}

	@Override
	public int max( int lvl ) {
		return 3*tier + 1 +   //10 base
				lvl*2;
	}

	@Override
	public int proc( Char attacker, Char defender, int damage ) {
		int zapto = defender.pos + (defender.pos - attacker.pos);
		if (zapto >= 0 && zapto < Dungeon.level.length()) {
			ConeAOE cone = new ConeAOE(new Ballistica(attacker.pos, zapto,
					Ballistica.STOP_TARGET | Ballistica.STOP_SOLID), 120);
			for (int cell : cone.cells) {
				if (cell == attacker.pos) continue;
				GameScene.add(Blob.seed(cell, 2, Electricity.class));
			}
		}
		return super.proc(attacker, defender, damage);
	}
}
