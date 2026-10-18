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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SlimeSprite;
import com.watabou.utils.Random;

public class Slime extends Mob {
	
	{
		spriteClass = SlimeSprite.class;
		
		HP = HT = 20;
		defenseSkill = 5;
		
		EXP = 4;
		maxLvl = 9;
		
		lootChance = 0.2f; //by default, see lootChance()
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 2, 5 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 12;
	}
	
	@Override
	public void damage(int dmg, Object src) {
		float scaleFactor = AscensionChallenge.statModifier(this);
		float scaledDmg = dmg/scaleFactor;
		//Pixel Dungeon Reforged Randomizer, Slashing weapons do three times the damage (Blade Weakness)
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(Slime.class) == 3 && com.shatteredpixel.shatteredpixeldungeon.Randomizer.isSlashing(src)) scaledDmg *= 3;
		//the damage cap starts at 5 normally. Enhanced Resilience brings it to 4, Softened Membrane to 10
		int threshold = com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureBuff(Slime.class) == 1 ? 4 : (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(Slime.class) == 2 ? 10 : 5);
		if (scaledDmg >= threshold){
			//takes 5/6/7/8/9/10 dmg at 5/7/10/14/19/25 incoming dmg
			scaledDmg = (threshold - 1) + (float)(Math.sqrt(8*(scaledDmg - (threshold - 1)) + 1) - 1)/2;
		}
		dmg = (int)(scaledDmg*AscensionChallenge.statModifier(this));
		super.damage(dmg, src);
	}

	@Override
	public float lootChance(){
		//each drop makes future drops 1/4 as likely
		// so loot chance looks like: 1/5, 1/20, 1/80, 1/320, etc.
		return super.lootChance() * (float)Math.pow(1/4f, Dungeon.LimitedDrops.SLIME_WEP.count);
	}
	
	@Override
	public Item createLoot() {
		Dungeon.LimitedDrops.SLIME_WEP.count++;
		Generator.Category c = Generator.Category.WEP_T2;
		MeleeWeapon w = (MeleeWeapon)Generator.randomUsingDefaults(Generator.Category.WEP_T2);
		w.level(0);
		return w;
	}

	//Pixel Dungeon Reforged Randomizer. Enhanced Resilience, Sticky Coating, Chameleon Ooze, Caustic Carriers,
	//Softened Membrane, Blade Weakness (all above, or in the mob spawner and the attack proc below)
	@Override
	public int attackProc( Char enemy, int damage ) {
		damage = super.attackProc( enemy, damage );
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureBuff(Slime.class) == 2 && Random.Int(2) == 0) {
			com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.affect(enemy, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow.class, 3f);
		}
		return damage;
	}

	@Override
	protected void onAdd() {
		boolean first = firstAdded;
		super.onAdd();
		//Chameleon Ooze, slimes stay nearly invisible until they attack
		if (first && com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureBuff(Slime.class) == 3) {
			com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.prolong(this, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility.class, 10000f);
		}
	}

	@Override
	public void rollToDropLoot() {
		super.rollToDropLoot();
		//Caustic Carriers, every slime drops a goo blob
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(Slime.class) == 1 && Dungeon.hero.lvl <= maxLvl + 2) {
			Dungeon.level.drop(new com.shatteredpixel.shatteredpixeldungeon.items.quest.GooBlob(), pos).sprite.drop();
		}
	}
}
