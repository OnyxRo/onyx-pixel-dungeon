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

import com.watabou.utils.PathFinder;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BugSlow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.sps.SpiderBotSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class SpiderBot extends Mob {

	{
		spriteClass = SpiderBotSprite.class;

		HP = HT = 150+(adj(0)*Random.NormalIntRange(4, 7));
		defenseSkill = 25+adj(1);

		EXP = 12;
		maxLvl = 30;

		lootChance = 0.3f;

		//FLEEING = new Fleeing();
		
		properties.add(Property.BEAST);
	}


	@Override
	public int damageRoll() {
		return Random.NormalIntRange(15+adj(0), 40+adj(1));
	}

	@Override
	public int attackSkill( Char target) {
		return  30+adj(0);
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 20);
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		if( !skilluse ){
			spanbug(this.pos);
			skilluse = true;
		}
		return damage;
	}

	@Override
	public int defenseProc(Char enemy, int damage) {
		Buff.affect(enemy,BugSlow.class);
		return super.defenseProc(enemy, damage);
	}

	@Override
	public void die(Object cause) {

		super.die(cause);
		Item bugfood = new com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat();

		//Dungeon.level.drop( bugfood, pos ).sprite.drop();

		for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
			Char ch = findChar(pos + PathFinder.NEIGHBOURS8[i]);
			if (ch != null && ch.isAlive()) {
				Buff.affect(ch,BugSlow.class);
			}
			if (ch instanceof Hero){
				if (!bugfood.collect(Dungeon.hero.belongings.backpack)) {
					Dungeon.level.drop( bugfood, Dungeon.hero.pos ).sprite.drop();
				} else {
					GLog.n(Messages.get(this, "yell"));
				}
			}
		}

		if (Dungeon.level.heroFOV[pos]) {
			Sample.INSTANCE.play(Assets.Sounds.BONES);
		}
	}


	{
		resistances.add(Bleeding.class);
		immunities.add(Roots.class);
	}

	private void spanbug(int pos) {
		Char ch = Dungeon.hero;
		Item bugfood = new com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat();
		int dist = Dungeon.level.distance(pos, Dungeon.hero.pos);
		if (dist < 2) {
			if (!bugfood.collect(Dungeon.hero.belongings.backpack)) {
				Dungeon.level.drop( bugfood, ch.pos ).sprite.drop();
			} else {
				GLog.n(Messages.get(this, "yell"));
			}
		} else Dungeon.level.drop( bugfood, pos ).sprite.drop();
	}


	//private class Fleeing extends Mob.Fleeing {
	//	@Override
	//	protected void nowhereToRun() {
	//		if (buff(Terror.class) == null) {
	//			state = HUNTING;
	//		} else {
	//			super.nowhereToRun();
	//		}
	//	}
//	}
}