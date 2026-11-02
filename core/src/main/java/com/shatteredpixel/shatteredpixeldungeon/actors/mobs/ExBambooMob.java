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
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.SwampGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.damageblobs.EarthEffectDamage;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.DefenceUp;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.damagetype.DamageType;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.Wound;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ExBambooSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class ExBambooMob extends BambooMob {

	{
		
		HP = HT = 80;
		spriteClass = ExBambooSprite.class;
		properties.add(Property.PLANT);
	}
	
	@Override
	public boolean act() {
		if (isAlive() && state != SLEEPING) {
			for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
				GameScene.add(Blob.seed(pos + PathFinder.NEIGHBOURS8[i], 3, EarthEffectDamage.class));
			}
		}
		if( 1 > 2 * HP / HT && !skilluse && isAlive() ) {
			skilluse = true;
			this.HP = this.HT;
			PathFinder.buildDistanceMap( pos, BArray.not( Dungeon.level.solid, null ), 2 );
			for (int i = 0; i < PathFinder.distance.length; i++) {
				if (PathFinder.distance[i] < Integer.MAX_VALUE) {
					//for (int i = 0; i < PathFinder.NEIGHBOURS8DIST2.length; i++) {
						Char ch = findChar(i);
						if (ch != null && ch.isAlive()) {
							Buff.affect(ch,Roots.class,10f);
						}
					}
			}
			this.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
		}

		return super.act();
	}	

	@Override
	public Item SupercreateLoot(){
		return Generator.random(Generator.Category.ARMOR);
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(6, 24);
	}

		@Override
		public int attackSkill( Char target ) {
			return 20;
		}

		@Override
		public int drRoll() {
			return Random.NormalIntRange(5, 10);
		}		
	
	@Override
	public int attackProc(Char enemy, int damage) {
		if (Random.Int(3) == 0) {
			Buff.affect(this,DefenceUp.class,3f).level(20);
		}
		return super.attackProc(enemy, damage);
	}
	
	@Override
	public int defenseProc(Char enemy, int damage) {

		int dmg = Random.IntRange(0, damage) - enemy.drRoll();
		if (dmg > 0) {
			enemy.damage(dmg, this,1);
			Wound.hit(enemy);
		}

		return super.defenseProc(enemy, damage);
	}

	@Override
	public void damage( int dmg, Object src ) {
		 if(  1 > 2 * HP / HT && !skilluse ) {
			dmg = 0;
		} else if (dmg> HT/3) {
			dmg =(int)Math.max(HT/3,1);
		}
		super.damage( dmg, src );
	}

	
	//private static final HashSet<Class<?>> resistances = new HashSet<>();


	{
		weakness.add(ToxicGas.class);
		weakness.add(Ooze.class);
		weakness.add(SwampGas.class);

		resistances.add(Roots.class);

		weakness.add(Wand.class);
		weakness.add(Poison.class);


	}

}
