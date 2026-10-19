/*
 * Onyx's Pixel Dungeon
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

package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Fatigue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PinCushion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.chargearea.MutationBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Sheep;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.bow.BowWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.DisposableMissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.TargetHealthIndicator;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

/**
 * Re-ARranged PD's five challenges, Superman, Pyromaniac, Cursed Dungeon, Fatigue and Mutation.
 * Each Shattered class calls one method from one line marked //rearranged.
 */
public class RearrangedChallenges {

	public static boolean superman(){ return Dungeon.isChallenged(Challenges.SUPERMAN); }
	public static boolean pyro(){ return Dungeon.isChallenged(Challenges.PYRO); }

	//Superman, 10 health that never grows with level
	public static int baseHT( int ht ){
		return superman() ? 10 : ht;
	}

	public static float accuracy( float accuracy ){
		return superman() ? accuracy * 2 : accuracy;
	}

	public static float evasion( float evasion ){
		return superman() ? evasion * 3 : evasion;
	}

	//Fire Imbue and Dragon's Blood last half as long under Pyromaniac
	public static float fireImbueDuration( float duration ){
		return pyro() ? duration / 2f : duration;
	}

	//start of the hero's attack proc, Superman, Pyromaniac and Fatigue
	public static int attackProc( Hero hero, Char enemy, int damage ){
		if (superman()) {
			damage *= 3;
		}
		if (pyro()) {
			Buff.affect(enemy, Burning.class).reignite(enemy, 8f);
		}
		if (Dungeon.isChallenged(Challenges.FATIGUE)) {
			Buff.affect(hero, Fatigue.class).hit(true);
		}
		return damage;
	}

	public static void defenseProc( Hero hero ){
		if (Dungeon.isChallenged(Challenges.FATIGUE)) {
			Buff.affect(hero, Fatigue.class).hit(false);
		}
	}

	public static void onEat( Hero hero ){
		if (hero.buff(Fatigue.class) != null) {
			hero.buff(Fatigue.class).detach();
		}
	}

	//Cursed Dungeon, half of the slain monsters leave a wraith
	public static void onMobDied( Mob mob ){
		if (!(mob instanceof Wraith) && Dungeon.isChallenged(Challenges.CURSED_DUNGEON) && Random.Float() < 0.5f) {
			Wraith.spawnAt(mob.pos, Wraith.class);
			if (Dungeon.level.heroFOV[mob.pos]) {
				CellEmitter.get(mob.pos).burst(ShadowParticle.CURSE, 6);
				Sample.INSTANCE.play(Assets.Sounds.CURSED);
			}
		}
	}

	private static final Class<?>[][] REGIONS = {
			{Rat.class, Albino.class, Snake.class, Gnoll.class, Swarm.class, Crab.class, Slime.class, CausticSlime.class},
			{Skeleton.class, Thief.class, Bandit.class, DM100.class, Guard.class, Necromancer.class, SpectralNecromancer.class},
			{Bat.class, Brute.class, Shaman.RedShaman.class, Shaman.BlueShaman.class, Shaman.PurpleShaman.class, Spinner.class, DM200.class, DM201.class},
			{Elemental.FireElemental.class, Elemental.FrostElemental.class, Elemental.ShockElemental.class, Elemental.ChaosElemental.class,
					Warlock.class, Ghoul.class, Monk.class, Senior.class, Golem.class},
			{Succubus.class, Eye.class, Scorpio.class, Acidic.class},
			{Soldier.class, SWAT.class, Researcher.class, Medic.class, Suppression.class, Tank.class}
	};

	//Mutation, end of the hero's attack proc. 5% of hits turn a normal enemy into another monster of the region
	public static void mutate( Hero hero, Char enemy, KindOfWeapon wep, int damage ){
		if (!Dungeon.isChallenged(Challenges.MUTATION)
				|| hero.buff(MutationBuff.class) != null
				|| (enemy instanceof Thief && ((Thief)enemy).item != null)
				|| Char.hasProp(enemy, Char.Property.BOSS)
				|| Char.hasProp(enemy, Char.Property.MINIBOSS)
				|| Char.hasProp(enemy, Char.Property.BOSS_MINION)
				|| enemy instanceof Statue
				|| enemy instanceof Mimic
				|| !(enemy instanceof Mob)
				|| enemy.alignment != Char.Alignment.ENEMY
				|| damage >= enemy.HP
				|| Dungeon.depth % 5 == 0
				|| Random.Float() >= 0.05f) {
			return;
		}
		Buff.affect(hero, MutationBuff.class);

		int region = 1 + (Dungeon.depth - 1) / 5;
		float chance = Random.Float();
		if (chance < 0.2f) {
			region--;
		} else if (chance > 0.95f) {
			region++;
		}
		region = Math.max(1, Math.min(6, region));

		Mob newEnemy = (Mob) Reflection.newInstance(Random.element(REGIONS[region - 1]));
		if (Random.Float() < 0.01f) {
			newEnemy = new Sheep();
		}
		if (newEnemy == null) return;

		if (wep instanceof MissileWeapon && !(wep instanceof DisposableMissileWeapon)) {
			if (((MissileWeapon) wep).durabilityLeft() > 0) {
				Dungeon.level.drop(wep, enemy.pos).sprite.drop();
			}
		}
		if (enemy.buff(PinCushion.class) != null) {
			enemy.buff(PinCushion.class).detach();
		}
		if (enemy.buff(BowWeapon.ArrowAttached.class) != null) {
			enemy.buff(BowWeapon.ArrowAttached.class).detach();
		}
		newEnemy.pos = enemy.pos;

		float enemyHPPercent = (enemy.HP - damage) / (float) enemy.HT;

		Actor.remove(enemy);
		if (enemy.sprite != null) enemy.sprite.killAndErase();
		Dungeon.level.mobs.remove(enemy);

		if (!(newEnemy instanceof Sheep)) {
			newEnemy.HP = Math.max(1, (int) Math.ceil(newEnemy.HT * enemyHPPercent));
		}

		GameScene.add(newEnemy);

		if (TargetHealthIndicator.instance != null) TargetHealthIndicator.instance.target(null);
		CellEmitter.get(newEnemy.pos).burst(Speck.factory(Speck.WOOL), 4);
		Sample.INSTANCE.play(Assets.Sounds.PUFF);

		Dungeon.level.occupyCell(newEnemy);
	}
}
