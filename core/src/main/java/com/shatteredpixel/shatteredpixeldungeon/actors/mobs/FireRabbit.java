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
package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.damageblobs.FireEffectDamage;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArmorBreak;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Locked;
import com.shatteredpixel.shatteredpixeldungeon.actors.damagetype.DamageType;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FireRabbitSprite;
import com.watabou.utils.Random;

public class FireRabbit extends Mob {

	{
		spriteClass = FireRabbitSprite.class;

		HP = HT = 80+(adj(0)*Random.NormalIntRange(3, 5));
		defenseSkill = 8+adj(0);

		EXP = 5;
        maxLvl = 20;

		loot = new PotionOfLiquidFlame();
		lootChance = 0.1f;
		
		properties.add(Property.ORC);
	}

	private Ballistica beam;



	@Override
	public Item SupercreateLoot(){
		return Generator.random(Generator.Category.FOOD);
	}

	@Override
	public boolean attack( Char enemy, float dmgMulti, float dmgBonus, float accMulti ) {

		if (beam != null) for (int hitpos : beam.subPath(1, beam.dist)) {
			GameScene.add(Blob.seed(hitpos, 5, FireEffectDamage.class));
		}
		return super.attack( enemy, dmgMulti, dmgBonus, accMulti );
	}


	@Override
	public int damageRoll() {
		return Random.NormalIntRange(6, 8+adj(0));
	}

	@Override
	public int attackSkill( Char target) {
		return 12;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(5, 10);
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		if (Random.Int(3) == 0) {
			Buff.affect(enemy, Burning.class).reignite(enemy);
			yell( Messages.get(this, "yell") );
		}
        if (Dungeon.level.distance( pos, enemy.pos ) == 3){
			damage = 0;
			Buff.affect(enemy, ArmorBreak.class,5f).level(20);
		}
		return damage;
	}
	
	@Override
	protected boolean canAttack(Char enemy) {
		beam = new Ballistica( pos, enemy.pos, Ballistica.STOP_TARGET);
		return beam.subPath(1, beam.dist).contains(enemy.pos) && Dungeon.level.distance( pos, enemy.pos ) <= 3 ;
	}		


	{
		immunities.add(Locked.class);
		immunities.add(Burning.class);
		immunities.add(com.shatteredpixel.shatteredpixeldungeon.actors.damagetype.DamageType.FireDamage.class);
		immunities.add(Fire.class);
	}

}
