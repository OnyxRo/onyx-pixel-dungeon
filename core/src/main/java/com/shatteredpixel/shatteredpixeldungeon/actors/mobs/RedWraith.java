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
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RedWraithSprite;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class RedWraith extends Mob {

	protected static final float SPAWN_DELAY = 2f;

	protected int level;

	{
		spriteClass = RedWraithSprite.class;

		HP = HT = 10 + Dungeon.depth;
		EXP = 1 ;

		flying = true;
		
		lootChance = 0.5f;
		
		lootOther = Generator.Category.RING;
		lootChanceOther = 0.1f;
		
		properties.add(Property.UNDEAD);
		
	}

	protected static final String LEVEL = "level";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(LEVEL, level);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		level = bundle.getInt(LEVEL);
		adjustStats(level);
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(1, 3 + level);
	}

	@Override
	public int attackSkill( Char target) {
		return 10 + level;
	}

	public void adjustStats(int level) {
		this.level = level;
		defenseSkill = attackSkill(null) * 5;
		enemySeen = true;
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		if (Random.Int(4) == 0) {
			Buff.affect(enemy, Vertigo.class, 5f);
			Buff.affect(enemy, Terror.class, Terror.DURATION).object = this.id();
		}

		return damage;
	}
	
	@Override
	protected boolean canAttack(Char enemy) {
		return Dungeon.level.distance( pos, enemy.pos ) <= 2 ;
	}	
	
	@Override
	public void die(Object cause) {
		
		/*if (!Dungeon.limitedDrops.ringofhaste.dropped() && Random.Float()<0.1f) {
			Dungeon.limitedDrops.ringofhaste.drop();
			Dungeon.level.drop(new RingOfHaste(), pos).sprite.drop();
			explodeDew(pos);				
		} else {
			explodeDew(pos);
		}*/

		super.die(cause);
	
	}
	
	//public void damage( int dmg, Object src ) {
	//	if (enemySeen
	//			&& (src instanceof Wand || src instanceof LightningTrap.Electricity || src instanceof Char)) {
	//		GLog.n("The attack passes through the wraith.");
	//		sprite.showStatus(CharSprite.NEUTRAL, "missed");
	//	} else {
	//		super.damage( dmg, src );
	//	}
	//}

	@Override
	public boolean reset() {
		state = WANDERING;
		return true;
	}

	public static void spawnAround(int pos) {
		for (int n : PathFinder.NEIGHBOURS4) {
			int cell = pos + n;
			if (Dungeon.level.passable[cell] && Actor.findChar(cell) == null) {
				spawnAt(cell);
			}
		}
	}

	public static RedWraith spawnAt(int pos) {
		if (Dungeon.level.passable[pos] && Actor.findChar(pos) == null) {
          
			RedWraith w = new RedWraith();
			w.adjustStats(Dungeon.depth);
			w.pos = pos;
			w.state = w.HUNTING;
			GameScene.add(w, SPAWN_DELAY);

			w.sprite.alpha(0);
			w.sprite.parent.add(new AlphaTweener(w.sprite, 1, 0.5f));

			w.sprite.emitter().burst(ShadowParticle.CURSE, 5);

			return w;
  			
		} else {
			return null;
		}
	}

	{
		//immunities.add(EnchantmentDark.class);
		immunities.add(Terror.class);
		immunities.add(Amok.class);
		immunities.add(Charm.class);
		immunities.add(Sleep.class);
		immunities.add(ToxicGas.class);
		immunities.add(Vertigo.class);
		immunities.add(Burning.class);
		immunities.add(Paralysis.class);
		immunities.add(Roots.class);
		immunities.add(Frost.class);
	}

}
