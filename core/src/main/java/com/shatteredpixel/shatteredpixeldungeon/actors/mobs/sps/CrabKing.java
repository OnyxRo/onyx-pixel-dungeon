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
package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.sps;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.*;
import com.shatteredpixel.shatteredpixeldungeon.sprites.*;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Locked;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.sps.CrabKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class CrabKing extends Mob {

	private static final int JUMP_DELAY = 5;

	{
		spriteClass = CrabKingSprite.class;
		baseSpeed = 2f;

		HP = HT = 1300;
		EXP = 20;
		defenseSkill = 30;

		properties.add(Property.FISHER);
		properties.add(Property.BOSS);
	}

	private int timeToJump = JUMP_DELAY;


	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange(20, 50);
	}

	@Override
	public int attackSkill( Char target) {
		return 35;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(5, 10);
	}

	@Override
	protected boolean act() {
		boolean result = super.act();


		if (HP < HT) {
			sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
			HP = HP + 10;
			GLog.n(Messages.get(this,"heal"));
		}
		return result;
	}
	
	@Override
	public void die(Object cause) {

		super.die(cause);
		
		GameScene.bossSlain();
		
		Dungeon.level.drop(new Gold(Random.Int(1900, 4000)), pos).sprite.drop();
		
		yell(Messages.get(this,"die"));

	}

	@Override
	protected boolean getCloser(int target) {
		if (Dungeon.level.heroFOV[target]) {
			jump();
			return true;
		} else {
			return super.getCloser(target);
		}
	}

	@Override
	protected boolean canAttack(Char enemy) {
				if (buff(Locked.class) != null){
			return Dungeon.level.adjacent(pos, enemy.pos) && (!isCharmedBy(enemy));
		} else
		return new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos;
	}

	@Override
	protected boolean doAttack(Char enemy) {
		timeToJump--;
		if (timeToJump <= 0 && Dungeon.level.adjacent(pos, enemy.pos)) {
			jump();
			return true;
		} else {
			return super.doAttack(enemy);
		}
	}

	private void jump() {
		timeToJump = JUMP_DELAY;
		
		int newPos;
		do {
			newPos = Random.Int(Dungeon.level.length());
		} while (!Dungeon.level.heroFOV[newPos] || !Dungeon.level.passable[newPos]
				|| Dungeon.level.adjacent(newPos, enemy.pos)
				|| Actor.findChar(newPos) != null);

		sprite.move(pos, newPos);
		move(newPos);

		if (Dungeon.level.heroFOV[newPos]) {
			CellEmitter.get(newPos).burst(Speck.factory(Speck.WOOL), 6);
			Sample.INSTANCE.play(Assets.Sounds.PUFF);
		}

		spend(1 / speed());
	}

	@Override
	public void notice() {
		super.notice();
		yell(Messages.get(this,"notice"));
	}

	{
		resistances.add(ToxicGas.class);
		resistances.add(Poison.class);
		
	}

}
