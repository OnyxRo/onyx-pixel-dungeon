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

import com.watabou.utils.PathFinder;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.damageblobs.FireEffectDamage;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.damageblobs.IceEffectDamage;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AttackUp;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.DefenceUp;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SuccubusSprite;
import com.watabou.utils.Random;

public class FireSuccubus extends Succubus {

	{
		spriteClass = SuccubusSprite.class;

		HP = HT = 150+(adj(0)*Random.NormalIntRange(5, 7));
		defenseSkill = 30+adj(1);
		viewDistance = Light.DISTANCE;

		EXP = 14;
		maxLvl = 35;

		properties.add(Property.DEMONIC);
	}

	@Override
	public boolean act() {
		for (int i = 0; i < PathFinder.NEIGHBOURS9.length; i++) {
			GameScene.add(Blob.seed(pos + PathFinder.NEIGHBOURS9[i], 2,
					FireEffectDamage.class));
		}
		return super.act();
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(15, 25+adj(0));
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		Buff.affect(this, DefenceUp.class,10f).level(30);
		GameScene.add(Blob.seed(enemy.pos, 3, IceEffectDamage.class));
		return super.attackProc(enemy, damage);
	}

	@Override
	public void damage( int dmg, Object src ) {
		if (dmg> HT/6) {
			dmg =(int)Math.max(HT/6,1);
		}
		Buff.affect(this, AttackUp.class,10f).level(30);
		super.damage( dmg, src );

	}

	@Override
	public int attackSkill( Char target) {
		return 42+adj(1);
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(5, 10);
	}

    {
		immunities.add(Sleep.class);
		immunities.add(FireEffectDamage.class);
	}

}
