/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
 *
 * Community Pixel Dungeon
 * Copyright (C) 2024-2025 Trashbox Bobylev and Pixel Dungeon's community
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

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PoisonParticle;
import com.watabou.noosa.particles.Emitter;

//Community PD's poison elemental. Its frames sit in empty cells of the mod's copy of the elemental sheet,
//which is the same size as ours, so only the texture changes.
public class CommunityPoisonElementalSprite extends ElementalSprite {

	{
		boltType = MagicMissile.POISON;
	}

	public CommunityPoisonElementalSprite() {
		super();
		texture( Assets.Sprites.COMMUNITY_POISON_ELEMENTAL );
		play( idle, true );
	}

	@Override
	protected int texOffset() {
		return 70;
	}

	@Override
	protected Emitter createEmitter() {
		Emitter emitter = emitter();
		emitter.pour( PoisonParticle.SPLASH, 0.06f );
		return emitter;
	}

	@Override
	public int blood() {
		return 0xFF87FF49;
	}
}
