/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
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

import com.watabou.utils.PathFinder;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.KindofMisc;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MonsterBoxSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

public class MonsterBox extends Mob {

	{
		//name = "monster box";
		spriteClass = MonsterBoxSprite.class;
		//HT = hero.HT;
		HP = HT = hero != null ? hero.HT : 20; //sps, as strong as the hero who opened it
		EXP = 1;
		//defenseSkill = hero.evadeSkill;
		//enemySeen = true;
		properties.add(Property.UNKNOW);
	}

	@Override
	public int damageRoll() {
		return Dungeon.hero.damageRoll();
	}

	@Override
	public int attackSkill( Char target) {
		return Dungeon.hero.attackSkill(null);
	}

	@Override
	public int defenseSkill( Char target) {
		return Dungeon.hero.defenseSkill(null);
	}


	@Override
	public boolean reset() {
		state = WANDERING;
		return true;
	}

	@Override
	public void die(Object cause) {

		super.die(cause);

		switch (Random.Int(5)){
			case 0:
				if (hero.belongings.weapon != null) {
					Class<? extends KindOfWeapon> weapon = hero.belongings.weapon.getClass();
					Dungeon.level.drop(com.shatteredpixel.shatteredpixeldungeon.SpsStats.copyOf(weapon), pos);
				}else break;
			break;
			case 1:
				if (hero.belongings.armor != null) {
					Class<? extends com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor> armor = hero.belongings.armor.getClass();
					Dungeon.level.drop(com.shatteredpixel.shatteredpixeldungeon.SpsStats.copyOf(armor), pos);
				} else break;
			break;
			case 2:
				if (hero.belongings.artifact != null) {
					Class<? extends KindofMisc> artifact = hero.belongings.artifact.getClass();
					Dungeon.level.drop(com.shatteredpixel.shatteredpixeldungeon.SpsStats.copyOf(artifact), pos);
				}else break;
			break;
			case 3:
				if (hero.belongings.misc != null) {
					Class<? extends KindofMisc> misc = hero.belongings.misc.getClass();
					Dungeon.level.drop(com.shatteredpixel.shatteredpixeldungeon.SpsStats.copyOf(misc), pos);
				}else break;
			break;
			case 4:
				if (hero.belongings.ring != null) {
					Class<? extends KindofMisc> ring = hero.belongings.ring.getClass();
					Dungeon.level.drop(com.shatteredpixel.shatteredpixeldungeon.SpsStats.copyOf(ring), pos);
				}else break;
			break;
		}
	}

	public static MonsterBox spawnAt(int pos) {
		Char ch = Actor.findChar(pos);
		if (ch != null) {
			ArrayList<Integer> candidates = new ArrayList<Integer>();
			for (int n : PathFinder.NEIGHBOURS8) {
				int cell = pos + n;
				if ((Dungeon.level.passable[cell] || Dungeon.level.avoid[cell])
						&& Actor.findChar(cell) == null) {
					candidates.add(cell);
				}
			}
			if (candidates.size() > 0) {
				int newPos = Random.element(candidates);
				Actor.addDelayed(new Pushing(ch, ch.pos, newPos), -1);

				ch.pos = newPos;
				// FIXME
				if (ch instanceof Mob) {
					Dungeon.level.occupyCell(ch);
				} else {
					Dungeon.level.occupyCell( ch );
				}
			} else {
				return null;
			}
		}

		MonsterBox m = new MonsterBox();
		m.HP = m.HT;
		m.pos = pos;
		m.state = m.HUNTING;
		GameScene.add(m, 1);

		m.sprite.turnTo(pos, hero.pos);
		//Buff.affect(m, Roots.class, 1000);

		if (Dungeon.level.heroFOV[m.pos]) {
			CellEmitter.get(pos).burst(Speck.factory(Speck.STAR), 10);
			Sample.INSTANCE.play(Assets.Sounds.MIMIC);
		}

		return m;
	}

}
