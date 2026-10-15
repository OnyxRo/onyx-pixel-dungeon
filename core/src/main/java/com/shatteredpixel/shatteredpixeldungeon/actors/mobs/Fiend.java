/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Pixel Dungeon Reforged
 * Copyright (C) 2024-2025 Nathan Pringle
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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.MiasmaGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FiendSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

/** Reforged's fiend, which slows what it hits and bursts into miasma when it dies. */
public class Fiend extends Mob {

	{
		spriteClass = FiendSprite.class;

		HP = HT = 45;
		defenseSkill = 15;

		EXP = 11;
		maxLvl = 21;

		loot = Generator.Category.SCROLL;
		lootChance = 0.5f;

		properties.add(Property.UNDEAD);
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 2, 8 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 25;
	}

	@Override
	public int drRoll() {
		return super.drRoll() + Random.NormalIntRange(0, 8);
	}

	@Override
	public int attackProc( Char enemy, int damage ) {
		damage = super.attackProc( enemy, damage );
		if (Random.Int( 3 ) == 0) {
			Buff.affect( enemy, Slow.class, 5f );
		}
		return damage;
	}

	@Override
	public void die( Object cause ) {
		super.die( cause );

		if (cause == Chasm.class) return;

		int width = Dungeon.level.width();
		int[] neighbours = new int[] {
				(-width)*2-2,(-width)*2-1,(-width)*2,(-width)*2+1,(-width)*2+2,
				(-width)*1-2,(-width)*1-1,(-width)*1,(-width)*1+1,(-width)*1+2,
				          -2,          -1,/*no      ,*/         1,           2,
				(width)*1-2, (width)*1-1, (width)*1, (width)*1+1, (width)*1+2,
				(width)*2-2, (width)*2-1, (width)*2, (width)*2+1, (width)*2+2
		};

		for (int i = 0; i < neighbours.length; i++) {
			int cell = pos + neighbours[i];
			if (cell < 0 || cell >= Dungeon.level.length()) continue;
			Char ch = Actor.findChar( cell );
			if (ch != null && ch.isAlive()) {
				int damage = Random.NormalIntRange( 14, 20 );
				damage = Math.round( damage * AscensionChallenge.statModifier(this) );
				ch.damage( damage, new FiendExplosion() );
			}
			if (!Dungeon.level.solid[cell]) {
				GameScene.add( Blob.seed( cell, 20, MiasmaGas.class ) );
			}
		}

		if (Dungeon.level.heroFOV[pos]) {
			Sample.INSTANCE.play( Assets.Sounds.GAS );
		}
	}

	//used so resistances can differentiate between melee and magical attacks
	public static class FiendExplosion {}
}
