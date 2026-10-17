/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.journal.Document;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SnakeSprite;
import com.watabou.utils.Random;

public class Snake extends Mob {
	
	{
		spriteClass = SnakeSprite.class;
		
		HP = HT = 4;
		defenseSkill = 25;
		
		EXP = 2;
		maxLvl = 7;
		
		loot = Generator.Category.SEED;
		lootChance = 0.25f;
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 1, 4 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 10;
	}

	private static int dodges = 0;

	@Override
	public String defenseVerb() {
		if (Dungeon.level.heroFOV[pos]) {
			dodges++;
		}
		if ((dodges >= 2 && !Document.ADVENTURERS_GUIDE.isPageRead(Document.GUIDE_SURPRISE_ATKS))
				|| (dodges >= 4 && !Badges.isUnlocked(Badges.Badge.BOSS_SLAIN_1))){
			GameScene.flashForDocument(Document.ADVENTURERS_GUIDE, Document.GUIDE_SURPRISE_ATKS);
			dodges = 0;
		}
		return super.defenseVerb();
	}

	//Pixel Dungeon Reforged Randomizer. Venomous Bite, Phantom Scales, Hardened Hide, Paper Thin, Seed Hoarders, Rat Hunters
	{
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(Snake.class) == 1) HP = HT = 1;
	}

	@Override
	public int attackProc( Char enemy, int damage ) {
		damage = super.attackProc( enemy, damage );
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureBuff(Snake.class) == 1 && Random.Int(3) == 0) {
			int duration = Random.IntRange(1, 3);
			if (Random.Float() > 0.8f) duration++;
			duration = Math.round(duration * (com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge.statModifier(this) / 2f + 0.5f));
			com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.affect(enemy, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison.class).set(duration);
			state = FLEEING;
		}
		return damage;
	}

	@Override
	public boolean surprisedBy( Char enemy, boolean attacking ) {
		boolean surprised = super.surprisedBy( enemy, attacking );
		if (surprised && com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureBuff(Snake.class) == 2 && Random.Float() > 0.5f) {
			surprised = false;
		}
		return surprised;
	}

	@Override
	public int drRoll() {
		return super.drRoll() + (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureBuff(Snake.class) == 3 ? 2 : 0);
	}

	@Override
	public float lootChance() {
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(Snake.class) == 2) return 1f;
		return super.lootChance();
	}

	@Override
	protected void extraAttackTargets( java.util.HashSet<Char> enemies ) {
		if (!(com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(Snake.class) == 3) || alignment != Alignment.ENEMY) return;
		for (Mob mob : Dungeon.level.mobs) {
			if (mob instanceof Rat && fieldOfView[mob.pos] && mob.invisible <= 0) enemies.add(mob);
		}
	}
}
