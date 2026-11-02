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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Disarm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.EnergyArmor;

import com.watabou.utils.PathFinder;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.DarkGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.effectblobs.ElectriShock;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArmorBreak;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AttackDown;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.GlassShield;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.GrowSeed;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.StoneOre;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.WornKey;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfDisintegration;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ElderAvatarSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GolemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MonkSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MusketeerSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ObeliskSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WarlockSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class ElderAvatar extends Mob {

	{
		spriteClass = ElderAvatarSprite.class;

		HP = HT = 600;
		EXP = 50;
		defenseSkill = 25;
		baseSpeed = 1f;
		
		lootChance = 0.2f;
		lootChanceOther = 1f;

		properties.add(Property.ALIEN);
		properties.add(Property.BOSS);
	}

	private int orbAlive = 0;
	private int waves = 0;
	public static int breaks = 0;

	private static final String WAVES	= "waves";
	private static final String BREAKS	= "breaks";
	private static final String ORBALIVE	= "orbAlive";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put( WAVES, waves );
		bundle.put( BREAKS, breaks );
		bundle.put( ORBALIVE, orbAlive );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		waves = bundle.getInt( WAVES );
		breaks = bundle.getInt( BREAKS );
		orbAlive  = bundle.getInt( ORBALIVE );
	}


	public void spawnObe() {
		Obelisk a = new Obelisk();

		a.pos = Terrain.WELL;
		do {
			a.pos = Random.Int(Dungeon.level.randomRespawnCell(null));
		} while (Dungeon.level.map[a.pos] != Terrain.WELL
				|| Actor.findChar(a.pos) != null);
		GameScene.add(a);
	}

	@Override
	public int damageRoll() {
		return  false ? Random.NormalIntRange(0, 1) : Random.NormalIntRange(25, 40);
	}

	@Override
	public int attackSkill( Char target) {
		return 58;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(8, 12);
	}

	@Override
	protected boolean canAttack(Char enemy) {
		if (HP > 49) return Dungeon.level.distance(pos, enemy.pos) <= 3;
		else return Dungeon.level.distance(pos, enemy.pos) <= 0;
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		if (Random.Int(5) == 0) {
			new com.shatteredpixel.shatteredpixeldungeon.items.bombs.MiniBomb().explode(enemy.pos);
		}
		if (Random.Int(8) == 0) {
			Buff.affect(enemy, Charm.class,  Random.IntRange(3, 5)).object = id();
			enemy.sprite.centerEmitter().start(Speck.factory(Speck.HEART),
					0.2f, 5);
			Sample.INSTANCE.play(Assets.Sounds.CHARMS);
		}
		Hero hero = Dungeon.hero;
		com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor armor = hero.belongings.armor;
		if (Random.Int(10) == 0) {
			if (armor != null && true
					&& !armor.cursed) {
				hero.belongings.armor = null;
				Dungeon.level.drop(armor, hero.pos).sprite.drop();
				GLog.w(Messages.get(this, "disarm"));
			}
		}
		return damage;
	}

    public boolean checkObelisk() {

        int obeliskAlive = 0;
        if (Dungeon.level.mobs != null) {
            for (Mob mob : Dungeon.level.mobs) {
                if (mob instanceof Obelisk && mob.HP > 10) {
                    obeliskAlive++;
                }
            }
        }
        return obeliskAlive > 0;
    }

	@Override
	public void damage( int dmg, Object src ) {
		if (dmg > this.HP && checkObelisk()) {
			//dmg = Random.Int(this.HP);
			//Buff.affect(this,EnergyArmor.class).level(100);
			dmg=0;
			this.HP = 30;
		}
		super.damage( dmg, src );
        }

	@Override
	protected boolean act() {

		if (orbAlive < 1) {
			spawnObe();
			orbAlive++;

			return true;
		}

		if (HP < 50 && waves == 0 && breaks == 0) {
			summonHunter(pos);
			waves++;
			Buff.affect(this,EnergyArmor.class).level(100);
			sprite.centerEmitter().start(Speck.factory(Speck.SCREAM), 0.4f, 2);
			Sample.INSTANCE.play(Assets.Sounds.CHALLENGE);
			return true;
		}

		if (HP < 50 && waves == 1 && breaks == 1) {
			summonWarlock(pos);
			waves++;
			Buff.affect(this,EnergyArmor.class).level(100);
			sprite.centerEmitter().start(Speck.factory(Speck.SCREAM), 0.4f, 2);
			Sample.INSTANCE.play(Assets.Sounds.CHALLENGE);
			return true;
		}

		if (HP < 50 && waves == 2 && breaks == 2) {
			summonMonk(pos);
			waves++;
			Buff.affect(this,EnergyArmor.class).level(100);
			sprite.centerEmitter().start(Speck.factory(Speck.SCREAM), 0.4f, 2);
			Sample.INSTANCE.play(Assets.Sounds.CHALLENGE);
			return true;
		}

		if (HP < 50 && waves == 3 && breaks == 3) {
			summonMech(pos);
			waves++;
			Buff.affect(this,EnergyArmor.class).level(100);
			sprite.centerEmitter().start(Speck.factory(Speck.SCREAM), 0.4f, 2);
			Sample.INSTANCE.play(Assets.Sounds.CHALLENGE);
			return true;
		}

		return super.act();
	}

	private void summonHunter(int pos) {
		TheHunter.spawnAround(pos);
	}

	private void summonWarlock(int pos) {
		TheWarlock.spawnAround(pos);
	}

	private void summonMonk(int pos) {
		TheMonk.spawnAround(pos);
	}

	private void summonMech(int pos) {
		TheMech.spawnAround(pos);
	}

	
	@Override
	public void die(Object cause) {

		yell(Messages.get(this, "died"));

		GameScene.bossSlain();
		Dungeon.level.unseal();
		Dungeon.level.drop(new WornKey(Dungeon.depth), pos).sprite.drop();
		Dungeon.level.drop(new Gold(Random.Int(4900, 10000)), pos).sprite.drop();


		Badges.validateBossSlain();

		super.die(cause);
	}

	@Override
	public void notice() {
		super.notice();
		yell(Messages.get(this, "notice"));
	}

    {
		resistances.add(ToxicGas.class);
		resistances.add(Amok.class);
		resistances.add(Vertigo.class);

		resistances.add(WandOfDisintegration.class);

		immunities.add(Paralysis.class);
		immunities.add(Charm.class);
	}


	public static class TheHunter extends Mob {

		{
			spriteClass = MusketeerSprite.class;

			HP = HT = 100;
			defenseSkill = 15;
			EXP = 0;
			baseSpeed = 3f;
			viewDistance = 6;
			state = WANDERING;
			properties.add(Property.ALIEN);
			properties.add(Property.BOSS);
		}

		@Override
		public int damageRoll() {
			return Random.NormalIntRange(20, 35);
		}

		@Override
		public int attackSkill( Char target) {
			return 60;
		}

		@Override
		protected boolean canAttack(Char enemy) {
			if (buff(Disarm.class) != null){
				return false;
			} else
				return new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos;
		}

		@Override
		public int attackProc(Char enemy, int damage) {
             Buff.affect(this, Disarm.class,5f);
             Buff.affect(enemy,ArmorBreak.class,3f).level(30);
			return damage;
		}

		@Override
		public void damage( int dmg, Object src ) {
			super.damage( dmg, src );
			if (src instanceof ToxicGas) {
				((ToxicGas) src).clear(pos);
			}
		}

		@Override
		public void die(Object cause) {
			super.die(cause);
		}

		@Override
		public int drRoll() {
			return 5;
		}

		public static void spawnAround(int pos) {
			for (int n : PathFinder.NEIGHBOURS4) {
				int cell = pos + n;
				if (Dungeon.level.passable[cell] && Actor.findChar(cell) == null) {
					spawnAt(cell);
				}
			}
		}

		public static TheHunter spawnAt(int pos) {

			TheHunter d = new TheHunter();

			d.pos = pos;
			d.state = d.HUNTING;
			GameScene.add(d, 2f);

			return d;

		}

		{
			immunities.add(Charm.class);
			immunities.add(Amok.class);
			immunities.add(Paralysis.class);
		}

	}

	public static class TheWarlock extends Mob {
		{
			spriteClass = WarlockSprite.class;

			HP = HT = 150;
			defenseSkill = 15;
			EXP = 0;
			state = WANDERING;
			properties.add(Property.ALIEN);
			properties.add(Property.BOSS);
		}

		@Override
		public int damageRoll() {
			return Random.NormalIntRange(25, 45);
		}

		@Override
		public int attackSkill( Char target) {
			return 60;
		}

		@Override
		public int attackProc(Char enemy, int damage) {
			Buff.prolong(enemy,Vertigo.class,3f);
			Buff.prolong(enemy,AttackDown.class,3f).level(20);
			return damage;
		}

		@Override
		public void damage( int dmg, Object src ) {
			dmg = dmg/2;
			super.damage( dmg, src );
		}

		@Override
		public void die(Object cause) {
			super.die(cause);
		}

		@Override
		public int drRoll() {
			return 5;
		}

		public static void spawnAround(int pos) {
			for (int n : PathFinder.NEIGHBOURS4) {
				int cell = pos + n;
				if (Dungeon.level.passable[cell] && Actor.findChar(cell) == null) {
					spawnAt(cell);
				}
			}
		}

		public static TheWarlock spawnAt(int pos) {

			TheWarlock d = new TheWarlock();

			d.pos = pos;
			d.state = d.HUNTING;
			GameScene.add(d, 2f);

			return d;

		}

		{
			immunities.add(Charm.class);
			immunities.add(Amok.class);
			immunities.add(Paralysis.class);
		}


	}

	public static class TheMonk extends Mob {

		{
			spriteClass = MonkSprite.class;

			HP = HT = 100;
			defenseSkill = 30;
			EXP = 0;
			baseSpeed = 2f;
			viewDistance = 5;
			state = WANDERING;
			properties.add(Property.ALIEN);
			properties.add(Property.BOSS);
		}

		@Override
		public float attackDelay() {
			return 0.5f;
		}

		@Override
		public int damageRoll() {
			return Random.NormalIntRange(15, 30);
		}

		@Override
		public int attackSkill( Char target) {
			return 60;
		}

		@Override
		public int attackProc(Char enemy, int damage) {
			if (Random.Int(6) == 0) {
			 Buff.affect(this,GlassShield.class).turns(1);
			}
			Buff.prolong(enemy,Blindness.class,3f);
			return damage;
		}

		@Override
		public void damage( int dmg, Object src ) {
			super.damage( dmg, src );
			if (src instanceof ToxicGas) {
				((ToxicGas) src).clear(pos);
			}
		}

		@Override
		public void die(Object cause) {
			super.die(cause);
		}

		@Override
		public int drRoll() {
			return 5;
		}

		public static void spawnAround(int pos) {
			for (int n : PathFinder.NEIGHBOURS4) {
				int cell = pos + n;
				if (Dungeon.level.passable[cell] && Actor.findChar(cell) == null) {
					spawnAt(cell);
				}
			}
		}

		public static TheMonk spawnAt(int pos) {

			TheMonk d = new TheMonk();

			d.pos = pos;
			d.state = d.HUNTING;
			GameScene.add(d, 2f);

			return d;

		}

		 {
			immunities.add(Charm.class);
			immunities.add(Amok.class);
			immunities.add(Paralysis.class);
		}

	}

	public static class TheMech extends Mob {

		{
			spriteClass = GolemSprite.class;

			HP = HT = 200;
			defenseSkill = 15;
			EXP = 0;
			state = WANDERING;
			baseSpeed = 0.5f;
			properties.add(Property.ALIEN);
			properties.add(Property.MECH);
			properties.add(Property.BOSS);
		}
		private int addshield = 0;
		@Override
		public int damageRoll() {
			return Random.NormalIntRange(45, 70);
		}

		@Override
		public int attackSkill( Char target) {
			return 30;
		}

		@Override
		protected boolean act() {

			if (addshield < 1) {
				Buff.affect(this,EnergyArmor.class).level(100);
				addshield++;
				return true;
			}
			return super.act();
		}

		@Override
		public int attackProc(Char enemy, int damage) {
			Buff.affect(enemy, Burning.class).reignite(enemy);
			return damage;
		}

		@Override
		public void die(Object cause) {
			super.die(cause);
		}

		@Override
		public int drRoll() {
			return 10;
		}

		public static void spawnAround(int pos) {
			for (int n : PathFinder.NEIGHBOURS4) {
				int cell = pos + n;
				if (Dungeon.level.passable[cell] && Actor.findChar(cell) == null) {
					spawnAt(cell);
				}
			}
		}

		public static TheMech spawnAt(int pos) {

			TheMech d = new TheMech();

			d.pos = pos;
			d.state = d.HUNTING;
			GameScene.add(d, 2f);

			return d;

		}

		{
			immunities.add(Burning.class);
			immunities.add(Frost.class);
			immunities.add(Chill.class);
			immunities.add(ElectriShock.class);
			immunities.add(ToxicGas.class);
			immunities.add(DarkGas.class);
			immunities.add(GrowSeed.class);
			immunities.add(Charm.class);
			immunities.add(Amok.class);
			immunities.add(Paralysis.class);
		}

	}

	public static class Obelisk extends Mob {

		{
			spriteClass = ObeliskSprite.class;

			HP = HT = 1000;
			defenseSkill = 0;

			EXP = 10;
			state = PASSIVE;

			loot = new StoneOre();
			lootChance = 0.05f;

			properties.add(Property.UNKNOW);
			properties.add(Property.BOSS);
		}

		@Override
		public void beckon(int cell) {
			// Do nothing
		}

		@Override
		public boolean add( Buff buff ) {
		return false;
	}

		@Override
		public int damageRoll() {
			return 0;
		}

		@Override
		public int attackSkill( Char target) {
			return 0;
		}

		@Override
		public int drRoll() {
			return 0;
		}

		@Override
		protected boolean act() {

			if (3 - ElderAvatar.breaks > 4 * HP / HT) {
				ElderAvatar.breaks++;
				for (Mob mob : Dungeon.level.mobs) {
					if (mob instanceof ElderAvatar) {
						mob.HP = 600;
					}
				}
				return true;
			}
			return super.act();
		}

	public boolean checkElder() {

		int elderAlive = 0;
		if (Dungeon.level.mobs != null) {
			for (Mob mob : Dungeon.level.mobs) {
				if (mob instanceof ElderAvatar && mob.HP > 50) {
					elderAlive++;
				}
			}
		}
        return elderAlive > 0;
	}

	@Override
	public void damage( int dmg, Object src ) {
		if (checkElder()) {
			yell(Messages.get(this, "impossible"));
		} else  {
			super.damage( dmg, src );
		}
	}
	
  }
}
