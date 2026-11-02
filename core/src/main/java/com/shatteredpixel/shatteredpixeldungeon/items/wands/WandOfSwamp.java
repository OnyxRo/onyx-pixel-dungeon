/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.SwampGas;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class WandOfSwamp extends DamageWand {

	{
		image = ItemSpriteSheet.WAND_POISON;
		collisionProperties = Ballistica.PROJECTILE;
	}

	public int min(int lvl){
		return lvl;
	}

	public int max(int lvl){
		return 8+4*lvl;
	}		
	

	@Override
	public void onZap(Ballistica bolt) {
		
		Char ch = Actor.findChar(bolt.collisionPos);
		if (ch != null) {

			wandProc(ch, chargesPerCast());
			
		    ch.damage((int)( damageRoll() * 1), this,2);

		}	   
		
		Heap heap = Dungeon.level.heaps.get(bolt.collisionPos);
		if (heap != null) {
			heap.earthhit();
		}
		
        GameScene.add( Blob.seed(bolt.collisionPos, 100, SwampGas.class ) );
	}

	@Override
	public void fx(Ballistica bolt, Callback callback) {
		MagicMissile.boltFromChar(curUser.sprite.parent, MagicMissile.POISON, curUser.sprite, bolt.collisionPos, callback);
		Sample.INSTANCE.play(Assets.Sounds.ZAP);
	}


	//sps, SPS had no battlemage staff, so a staff holding this wand adds no effect on hit
	@Override
	public void onHit(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff staff, Char attacker, Char defender, int damage) {
	}
}
