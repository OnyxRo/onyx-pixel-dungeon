/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Pixel Towers / Towers Pixel Dungeon
 * Copyright (C) 2024-2025 FixAkaTheFix (initials R. A. A.)
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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class MoltenDagger extends MeleeWeapon {

	{
		image = ItemSpriteSheet.WORN_SHORTSWORD;
		hitSound = Assets.Sounds.HIT_SLASH;
		hitSoundPitch = 1.1f;
		rarity = 1;
		tier = 5;
		critChance = 0.05f;
	}

	@Override
	protected int baseChargeUse(Hero hero, Char target) {
		if (hero.buff(Sword.CleaveTracker.class) != null){
			return 0;
		} else {
			return super.baseChargeUse(hero, target);
		}
	}

	@Override
	public int min(int lvl) {
		return  Math.round(7*damageModifier() +
				2*lvl*damageModifier());
	}

	@Override
	public int max(int lvl) {
		return  Math.round(10*damageModifier() +
				3*lvl*damageModifier());
	}

	@Override
	public int proc(Char attacker, Char defender, int damage) {
		ArrayList<Integer> respawnPoints = new ArrayList<>();
		for (int iz = 0; iz < PathFinder.NEIGHBOURS25.length; iz++) {
			int p = defender.pos + PathFinder.NEIGHBOURS25[iz];
			if (p >= 0 && p < Dungeon.level.length() && Actor.findChar(p) == null && Dungeon.level.passable[p]) {
				respawnPoints.add(p);
			}
		}
		if (!respawnPoints.isEmpty())GameScene.add(Blob.seed(Random.element(respawnPoints), 3, Fire.class));
		if (level()>0) GameScene.add(Blob.seed(attacker.pos, 3, Fire.class));
		return super.proc(attacker, defender, damage);
	}

	@Override
	public String targetingPrompt() {
		return Messages.get(this, "prompt");
	}

	@Override
	protected void duelistAbility(Hero hero, Integer target) {
		Sword.cleaveAbility(hero, target, 1.30f, 0, this);
	}

}
