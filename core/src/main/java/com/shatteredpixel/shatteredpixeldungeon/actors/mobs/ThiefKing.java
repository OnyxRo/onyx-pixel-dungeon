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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Locked;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.LightningTrap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ThiefKingSprite;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class ThiefKing extends Mob implements Callback {

	
	{
		spriteClass = ThiefKingSprite.class;

		HP = HT = 2000;
		defenseSkill = 28;

		EXP = 60;
		flying = true;

		loot = Generator.Category.SCROLL;
		lootChance = 1f;

		properties.add(Property.ELF);
		properties.add(Property.BOSS);
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(20, 70);
	}

	@Override
	public int attackSkill( Char target) {
		return 25;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(6, 14);
	}

	@Override
	protected boolean canAttack(Char enemy) {
		if (buff(Locked.class) != null){
			return Dungeon.level.adjacent(pos, enemy.pos) && (!isCharmedBy(enemy));
		} else
		return new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos;
	}

	@Override
	public void die(Object cause) {
		
        super.die(cause);

		GameScene.bossSlain();
		Dungeon.level.drop(new Gold(Random.Int(1900, 4000)), pos).sprite.drop();
		
		

		yell(Messages.get(this,"die"));
						
	}
	
	
	@Override
	public void call() {
		next();
	}

	@Override
	public void notice() {
		super.notice();
		yell(Messages.get(this,"notice",Dungeon.hero.name()));
	}

	{
		resistances.add(LightningTrap.Electricity.class);
	}

}
