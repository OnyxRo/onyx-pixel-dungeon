/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.DangerousBomb;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.EnergyArmor;


import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Badges.Badge;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ConfusionGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.DarkGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShieldArmor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Tar;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.WornKey;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Door;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HybridSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

import static com.shatteredpixel.shatteredpixeldungeon.actors.damagetype.DamageType.ENERGY_DAMAGE;
import static com.shatteredpixel.shatteredpixeldungeon.actors.damagetype.DamageType.SHOCK_DAMAGE;

public class Hybrid extends Mob {

	{
		spriteClass = HybridSprite.class;

		HP = HT = 800;
		defenseSkill = 10;
		baseSpeed = 1.5f;

		EXP = 50;
		lootChance = 0.2f;

		lootOther = new PotionOfExperience();
		lootChanceOther = 1f;
		
		FLEEING = new Fleeing();

		properties.add(Property.BEAST);
		properties.add(Property.ALIEN);
		properties.add(Property.UNDEAD);
		properties.add(Property.MECH);
		properties.add(Property.BOSS);
	}

	private int breaks=0;

	@Override
	public boolean act() {
        DangerousBomb bomb = new DangerousBomb();
		if (3 - breaks > 4 * HP / HT) {
			breaks++;
			bomb.explode(pos);
            Buff.affect(this,ShieldArmor.class).level(200);
			return true;
		}
		
		return super.act();
	}

	@Override
	public void move(int step, boolean travelling) {
		super.move(step, travelling);

		if (Dungeon.level.map[step] == Terrain.INACTIVE_TRAP) {
	       if (state == FLEEING){
			   Buff.affect(this,EnergyArmor.class).level(200);
			   yell(Messages.get(this, "shield"));
			   state = HUNTING;
		   }
	
		}

	}	
	
	@Override
	public int attackProc(Char enemy, int damage) {
		if (Random.Int(5) == 0) {
			Buff.affect(enemy, Poison.class).set(
					Random.Int(8, 10));
			state = FLEEING;
		}

		enemy.damage(damage/2, ENERGY_DAMAGE,2);
		damage = damage/2;

	
		return damage;
	}		
	
	@Override
	public int defenseProc(Char enemy, int damage) {
		if (Random.Int(3) == 0) {
			Buff.affect(enemy, Tar.class);
			state = HUNTING;
		}
		
		return super.defenseProc(enemy, damage);
	}
	
	
	
	@Override
	public void damage( int dmg, Object src ) {
		if ( breaks > 2 && dmg > 5){
			ArrayList<Integer> candidates = new ArrayList<Integer>();
			boolean[] passable = Dungeon.level.passable;

			int[] neighbours = { pos + 1, pos - 1, pos + Dungeon.level.width(),
					pos - Dungeon.level.width() };
			for (int n : neighbours) {
				if (passable[n] && Actor.findChar(n) == null) {
					candidates.add(n);
				}
			}

			if (candidates.size() > 0) {

				Mixers clone = new Mixers();
				clone.HT = dmg;
				clone.HP = dmg;
				clone.pos = Random.element(candidates);
				clone.state = clone.HUNTING;

				if (Dungeon.level.map[clone.pos] == Terrain.DOOR) {
					Door.enter(clone.pos);
				}

				GameScene.add(clone, 1f);
				Actor.addDelayed(new Pushing(clone, pos, clone.pos), -1);
			}
		}

		super.damage( dmg, src );
	}

	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	protected boolean canAttack(Char enemy) {
		return Dungeon.level.distance( pos, enemy.pos ) <= 2 ;
	}

	
	@Override
	public void die(Object cause) {
		super.die(cause);
		GameScene.bossSlain();
		Dungeon.level.unseal();
		Dungeon.level.drop(new WornKey(Dungeon.depth), pos).sprite.drop();
		Badges.validateBossSlain();

		

		yell(Messages.get(this,"die"));
		
	}
	
	@Override
	public void notice() {
		super.notice();
		yell(Messages.get(this, "notice"));
	}	

	@Override
	public int damageRoll() {
		return false ? Random.NormalIntRange(0, 1) : Random.NormalIntRange(30, 42);
	}
	
	

	@Override
	public int attackSkill( Char target) {
		return 50;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(10, 15);
	}
	
    private static final String BREAKS	= "breaks";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( BREAKS, breaks );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        breaks = bundle.getInt( BREAKS );
    }	

	{
		immunities.add(ToxicGas.class );
		immunities.add(ParalyticGas.class);
		immunities.add(DarkGas.class);
		immunities.add(ConfusionGas.class);
	}
	
	public static class Mixers extends Mob {

		{
			spriteClass = HybridSprite.class;

			HP = HT = 1;
			defenseSkill = 0;
			baseSpeed = 1f;
			properties.add(Property.UNKNOW);
			properties.add(Property.BOSS);
		}

		private static final float SPAWN_DELAY = 1f;

		@Override
		public int damageRoll() {
			return Random.NormalIntRange(25, 55);
		}

		@Override
		public int attackSkill( Char target) {
			return 100;
		}

	@Override
	public int attackProc(Char enemy, int damage) {

		enemy.damage(damage/4, SHOCK_DAMAGE,2);
		damage = damage*3/4;

		return damage;
	}		
			
		
		@Override
		public int drRoll() {
			return 0;
		}

		public static Mixers spawnAt(int pos) {

			Mixers b = new Mixers();

			b.pos = pos;
			b.state = b.HUNTING;
			GameScene.add(b, SPAWN_DELAY);

			return b;

		}
	}
	
}