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
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.AddBomb;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SelfDestroy;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.XBomb;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.StoneOre;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.WornKey;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfDisintegration;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BatteryTombSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.LichDancerSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SeekingBombSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import static com.shatteredpixel.shatteredpixeldungeon.actors.damagetype.DamageType.ENERGY_DAMAGE;

public class LichDancer extends Mob {

	private static final int MAX_ARMY_SIZE = 4;


	{
		spriteClass = LichDancerSprite.class;

		HP = HT = 1000;
		EXP = 60;
		defenseSkill = 25; 
		baseSpeed = 1f;
		
		lootChance = 0.2f;
		lootChanceOther= 1f;

		properties.add(Property.UNDEAD);
		properties.add(Property.MAGICER);
		properties.add(Property.BOSS);

	}

	private int breaks=0;
	
	public void spawnTomb() {
		BatteryTomb a = new BatteryTomb();
		a.pos = Terrain.PEDESTAL;
			int _tries1 = 0; //sps, gives up away from its own arena
			do {
				a.pos = Random.Int(Dungeon.level.randomRespawnCell(null));
			} while ((Dungeon.level.map[a.pos] != Terrain.PEDESTAL
					|| Actor.findChar(a.pos) != null) && ++_tries1 < 500);
			if (_tries1 >= 500) return;
			GameScene.add(a);
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange(20, 38);
	}

	@Override
	public int attackSkill( Char target) {
		return 65;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 20);
	}

	@Override
	protected boolean act() {
		
        if( 2 - breaks > 3 * HP / HT ) {
			breaks++;
			jump();
			addbomb();
            return true;
        }
		if (HP < HT) {
			//sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
			HP = HP + 3;
		}
		
		return super.act();
	}

	private void jump() {
		int newPos;
		int _tries2 = 0; //sps, gives up away from its own arena
		do {
			newPos = Random.Int(Dungeon.level.length());
		} while ((Dungeon.level.map[newPos] != Terrain.WELL && Dungeon.level.map[newPos] != Terrain.STATUE_SP) && ++_tries2 < 500);
		if (_tries2 >= 500) return;
		sprite.move(pos, newPos);
		move(newPos);

		if (Dungeon.level.heroFOV[newPos]) {
			CellEmitter.get(newPos).burst(Speck.factory(Speck.WOOL), 6);
			Sample.INSTANCE.play(Assets.Sounds.PUFF);
		}
		spawnTomb();
		spend(1 / speed());
	}

	private void addbomb() {
		int bombPos;
		int _tries3 = 0; //sps, gives up away from its own arena
		do {
			bombPos = Random.Int(Dungeon.level.length());
		} while ((!Dungeon.level.heroFOV[bombPos] || !Dungeon.level.passable[bombPos]
				|| Dungeon.level.adjacent(bombPos, enemy.pos)
				|| Actor.findChar(bombPos) != null) && ++_tries3 < 500);
		if (_tries3 >= 500) return;

		Mob	m = new LinkAddBomb();
		GameScene.add(m);
		ScrollOfTeleportation.appear(m, bombPos);
	}



	@Override
	public int attackProc(Char enemy, int damage) {
		if (Random.Int(5) == 0) {
			Buff.prolong(enemy, Vertigo.class,3f);
		}
		
		enemy.damage(damageRoll()/2, ENERGY_DAMAGE,2);
		damage = damage/2;

        return damage;
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

	@Override
	public void die(Object cause) {

		 
		 GameScene.bossSlain();
		Dungeon.level.unseal();

		Dungeon.level.drop(new WornKey(Dungeon.depth), pos).sprite.drop();
		Dungeon.level.drop(new Gold(Random.Int(1000, 2000)), pos).sprite.drop();


		Badges.validateBossSlain();
		//summonLiches(findTomb);
		//GLog.n(Messages.get(this, "liches"));

		super.die(cause);

	}

	private int maxArmySize() {
		return 2;
	}
	
    public boolean checkBattery() {

        int batteryAlive = 0;
        if (Dungeon.level.mobs != null) {
            for (Mob mob : Dungeon.level.mobs) {
                if (mob instanceof BatteryTomb) {
                    batteryAlive++;
                }
            }
        }
        return batteryAlive > 0;
    }	
	
	@Override
	public void damage( int dmg, Object src ) {
		if (checkBattery()) {
			dmg = Random.Int(10);
			//Buff.affect(this,ShieldArmor.class).level(100);
		}
		super.damage( dmg, src );
        }	
	
	@Override
	public void notice() {
		super.notice();
		yell(Messages.get(this,"notice"));
	}

	{
		resistances.add(ToxicGas.class);
		
		resistances.add(WandOfDisintegration.class);

		immunities.add(Paralysis.class);
		immunities.add(Vertigo.class);
	}

	
    public static class BatteryTomb extends Mob  {

	{
		spriteClass = BatteryTombSprite.class;

		HP = HT = 200;
		defenseSkill = 0;

		EXP = 10;
		state = PASSIVE;
		
		loot = new StoneOre();
		lootChance = 0.05f;

		properties.add(Property.MECH);
		properties.add(Property.MINIBOSS);
	}
	
	@Override
    public boolean act() {

        if( Random.Int(20) == 0 && Dungeon.level.mobs.size()< 6) {
			ManySkeleton.spawnAround(pos);
        } 
        return super.act();
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
	public void damage( int dmg, Object src ) {
	    if ( dmg > 50 ) dmg = 50;
		super.damage( dmg, src );

	}
}
    public static class LinkAddBomb extends Mob {
        {
            spriteClass = SeekingBombSprite.class;

            HP = HT = 8;
            defenseSkill = 0;
            baseSpeed = 1f;
            EXP = 0;

            state = PASSIVE;

            properties.add(Property.MECH);
            properties.add(Property.MINIBOSS);
        }

		@Override
        public int drRoll() {
            return 0;
        }

        @Override
        public boolean act() {
            yell(""+HP/2+"!");
            return super.act();
        }

		@Override
		public void damage( int dmg, Object src ) {
			if (!(src instanceof SelfDestroy) ) dmg = 0;
			super.damage( dmg, src );
		}

		public void die(Object cause) {
			AddBomb addbomb = new AddBomb();
			addbomb.explode(pos);
			for (Mob mob : Dungeon.level.mobs) {
				if (mob instanceof LichDancer) {
					Mob	ma = new LinkXBomb();
					GameScene.add(ma);
					ScrollOfTeleportation.appear(ma, pos);
				}
			}
			super.die(cause);
		}
	}

	public static class LinkXBomb extends Mob {
		{
			spriteClass = SeekingBombSprite.class;

			HP = HT = 8;
			defenseSkill = 0;
			baseSpeed = 1f;
			EXP = 0;

			state = PASSIVE;

			properties.add(Property.MECH);
			properties.add(Property.MINIBOSS);
		}

		@Override
		public int drRoll() {
			return 0;
		}

		@Override
		public boolean act() {
			yell(""+HP/2+"!");
			return super.act();
		}

		@Override
		public void damage( int dmg, Object src ) {
			if (!(src instanceof SelfDestroy) ) dmg = 0;
			super.damage( dmg, src );
		}

		public void die(Object cause) {
			XBomb xbomb = new XBomb();
			xbomb.explode(pos);
			for (Mob mob : Dungeon.level.mobs) {
				if (mob instanceof LichDancer) {
					Mob	mb = new LinkAddBomb();
					GameScene.add(mb);
					ScrollOfTeleportation.appear(mb, pos);
				}
			}
			super.die(cause);
		}
	}
}
