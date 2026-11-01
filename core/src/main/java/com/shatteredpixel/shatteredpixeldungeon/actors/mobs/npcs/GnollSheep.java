/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Re-Remixed Dungeon, by QuasiStellar, ported into Onyx's Pixel Dungeon
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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.audio.Sample;

//reremixed, the Gnoll's Decoy. Monsters that see it go for it, and after a few turns it blows up like a bomb.
public class GnollSheep extends Sheep {

	{
		alignment = Alignment.ALLY;
	}

	@Override
	protected boolean act() {
		int cell = pos;
		HP = 0;
		destroy();
		if (sprite != null) sprite.die();
		new Bomb().explode( cell );
		return true;
	}

	public static GnollSheep spawn( int cell ) {
		GnollSheep sheep = new GnollSheep();
		sheep.pos = cell;
		GameScene.add( sheep );
		sheep.initialize( 5 );
		Dungeon.level.occupyCell( sheep );
		for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
			if (mob != sheep && mob.alignment == Alignment.ENEMY && Dungeon.level.heroFOV[mob.pos]) {
				mob.aggro( sheep );
			}
		}
		if (sheep.sprite != null) {
			CellEmitter.get( cell ).burst( Speck.factory( Speck.WOOL ), 4 );
			Sample.INSTANCE.play( Assets.Sounds.PUFF );
		}
		return sheep;
	}
}
