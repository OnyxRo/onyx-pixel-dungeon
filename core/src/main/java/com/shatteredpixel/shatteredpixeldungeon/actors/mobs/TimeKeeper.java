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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WatchOut;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMindVision;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TimeKeeperSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import static com.shatteredpixel.shatteredpixeldungeon.actors.damagetype.DamageType.ENERGY_DAMAGE;

public class TimeKeeper extends Mob {

	{
		spriteClass = TimeKeeperSprite.class;
		
		HP = HT = 80+(adj(0)*Random.NormalIntRange(2, 5));
		defenseSkill = 15+adj(0);

		EXP = 10;
		maxLvl = 25;

		loot = Generator.Category.SCROLL;
		lootChance = 0.15f;
		
		properties.add(Property.UNKNOW);
		properties.add(Property.MAGICER);
	}


	@Override
	public int damageRoll() {
		return Random.NormalIntRange(14, 20+adj(0));
	}

	@Override
	public int attackSkill( Char target) {
		return 16+adj(0);
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 4);
	}
	
	@Override
	public int attackProc(Char enemy, int damage) {
		
		enemy.damage(damageRoll(), ENERGY_DAMAGE,2);
		damage = 0;

		if (enemy.isAlive() && Random.Int(5)==1) {
			if (enemy instanceof Hero) {
				ScrollOfTeleportation.teleportHero((Hero) enemy);
				((Hero) enemy).curAction = null;
			} else {
				int count = 10;
				int pos;
				do {
					pos = Dungeon.level.randomRespawnCell( null );
					if (count-- <= 0) {
						break;
					}
				} while (pos == -1);

				enemy.pos = pos;
				enemy.sprite.place(enemy.pos);
				enemy.sprite.visible = Dungeon.level.heroFOV[pos];

			}
		}
		return damage;
	}	

	@Override
	public void damage( int dmg, Object src ) {
		if ( HP - dmg < HT/2 && !skilluse){
			dmg = 0;
			skilluse = true;
			Buff.affect(this,WatchOut.class,200f);
			int newPos;
			do {
				newPos = Random.Int(Dungeon.level.length());
			} while (!Dungeon.level.passable[newPos]
					|| Dungeon.level.adjacent(newPos, Dungeon.hero.pos)
					|| Actor.findChar(newPos) != null);

			sprite.move(pos, newPos);
			move(newPos);
			yell( Messages.get(this, "yell") );
			if (Dungeon.level.heroFOV[newPos]) {
				CellEmitter.get(newPos).burst(Speck.factory(Speck.WOOL), 6);
				Sample.INSTANCE.play(Assets.Sounds.PUFF);
			}

			spend(1 / speed());

		}
		super.damage( dmg, src );
	}

}
