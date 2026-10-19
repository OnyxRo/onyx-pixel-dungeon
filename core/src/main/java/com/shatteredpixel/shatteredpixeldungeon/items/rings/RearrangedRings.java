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

package com.shatteredpixel.shatteredpixeldungeon.items.rings;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.CounterBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Daze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Enduring;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.EvasiveMove;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MonkEnergy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PinCushion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PoisonParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.UnstableBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.ExoticPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfDivineInspiration;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMetamorphosis;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.TelekineticGrab;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.UnstableSpell;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.ExoticCrystals;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blooming;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Kinetic;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Lucky;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Shocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Visual;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Re-ARranged PD's Mystical Punch (the Fighter, Huntress's third subclass). An unarmed hit can trigger the worn
 * rings' own on hit effect. The mod gave each ring an onHit method, here they live together.
 */
public class RearrangedRings {

	public static int onHit( Ring ring, Hero hero, Char enemy, int damage ){
		if (ring instanceof RingOfAccuracy) return onAccuracy(hero, enemy, damage);
		if (ring instanceof RingOfArcana) return onArcana(hero, enemy, damage);
		if (ring instanceof RingOfElements) return onElements(hero, enemy, damage);
		if (ring instanceof RingOfEnergy) return onEnergy(hero, enemy, damage);
		if (ring instanceof RingOfEvasion) return onEvasion(hero, enemy, damage);
		if (ring instanceof RingOfForce) return onForce(hero, enemy, damage);
		if (ring instanceof RingOfFuror) return onFuror(hero, enemy, damage);
		if (ring instanceof RingOfHaste) return onHaste(hero, enemy, damage);
		if (ring instanceof RingOfMight) return onMight(hero, enemy, damage);
		if (ring instanceof RingOfSharpshooting) return onSharpshooting(hero, enemy, damage);
		if (ring instanceof RingOfTenacity) return onTenacity(hero, enemy, damage);
		if (ring instanceof RingOfWealth) return onWealth(hero, enemy, damage);
		return damage;
	}

	private static int onAccuracy(Hero hero, Char enemy, int damage) {
		float damageMulti = 1f;
		float heroAcc = hero.attackSkill(enemy);
		float enemyEva = enemy.defenseSkill(hero)*2; 
		if (Random.Float() < heroAcc/enemyEva) {
			damageMulti = GameMath.gate(1, heroAcc/enemyEva, 2);
			Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
			if (Random.Float() < 0.1f) {
				Buff.affect(enemy, Paralysis.class, 1f);
			}
			if (Random.Float() < 0.1f) {
				Buff.affect(enemy, Daze.class, 2f);
			}
		}
		return (int)(damage * damageMulti);
	}

	private static int onArcana(Hero hero, Char enemy, int damage) {
		int level = (int)Math.ceil(hero.lvl / 5f);
		float powerMulti = Math.max(1f, RingOfArcana.enchantPowerMultiplier(hero));
		float procChance;
		switch (Random.Int(15)) {
			case 0: default:
				//Blazing Effect
				procChance = (level+1f)/(level+3f) * powerMulti;
				if (Random.Float() < procChance) {
					if (enemy.buff(Burning.class) != null){
						Buff.affect(enemy, Burning.class).reignite(enemy, 8f);
						int burnDamage = Random.NormalIntRange( 1, 3 + Dungeon.scalingDepth()/4 );
						enemy.damage( Math.round(burnDamage * 0.67f * powerMulti), hero );
					} else {
						Buff.affect(enemy, Burning.class).reignite(enemy, 8f);
					}

					enemy.sprite.emitter().burst( FlameParticle.FACTORY, level + 1 );
				}
				return damage;
			case 1:
				//Blocking Effect
				procChance = (level+4f)/(level+40f) * powerMulti;

				if (Random.Float() < procChance){
					float powerLeft = Math.max(1f, procChance);

					Blocking.BlockBuff b = Buff.affect(hero, Blocking.BlockBuff.class);
					b.setShield(Math.round(powerLeft * (2 + level)));
					hero.sprite.emitter().burst(Speck.factory(Speck.LIGHT), 5);
				}
				return damage;
			case 2:
				//Blooming Effect
				procChance = (level+1f)/(level+3f) * powerMulti;
				if (Random.Float() < procChance) {
					float plants = (1f + 0.1f*level) * powerMulti;
					if (Random.Float() < plants%1){
						plants = (float)Math.ceil(plants);
					} else {
						plants = (float)Math.floor(plants);
					}

					Blooming blooming = new Blooming();
					if (blooming.plantGrass(enemy.pos)){
						plants--;
						if (plants <= 0){
							return damage;
						}
					}

					ArrayList<Integer> positions = new ArrayList<>();
					for (int i : PathFinder.NEIGHBOURS8){
						if (enemy.pos + i != hero.pos) {
							positions.add(enemy.pos + i);
						}
					}
					Random.shuffle( positions );

					//The attacker's position is always lowest priority
					if (Dungeon.level.adjacent(hero.pos, enemy.pos)){
						positions.add(hero.pos);
					}

					for (int i : positions){
						if (blooming.plantGrass(i)){
							plants--;
							if (plants <= 0) {
								return damage;
							}
						}
					}
				}
				return damage;
			case 3:
				//Chilling Effect
				procChance = (level+1f)/(level+4f) * powerMulti;
				if (Random.Float() < procChance) {
					float durationToAdd = 3f * powerMulti;
					Chill existing = enemy.buff(Chill.class);
					if (existing != null){
						durationToAdd = Math.min(durationToAdd, (6f*powerMulti)-existing.cooldown());
					}

					Buff.affect( enemy, Chill.class, durationToAdd );
					Splash.at( enemy.sprite.center(), 0xFFB2D6FF, 5);
				}
				return damage;
			case 4:
				//Kinetic Effect, See RingOfForce.damageRoll too
				int conservedDamage = 0;
				if (hero.buff(Kinetic.ConservedDamage.class) != null) {
					conservedDamage = hero.buff(Kinetic.ConservedDamage.class).damageBonus();
					hero.buff(Kinetic.ConservedDamage.class).detach();
				}

				Buff.affect(hero, Kinetic.KineticTracker.class).conservedDamage = conservedDamage;
				return damage;
			case 5:
				//Corrupting Effect
				procChance = (level+5f)/(level+25f) * powerMulti;
				if (damage >= enemy.HP
						&& Random.Float() < procChance
						&& !enemy.isImmune(Corruption.class)
						&& enemy.buff(Corruption.class) == null
						&& enemy instanceof Mob
						&& enemy.isAlive()){
					Corruption.corruptionHeal(enemy);

					AllyBuff.affectAndLoot((Mob)enemy, hero, Corruption.class);

					float powerLeft = Math.max(1f, procChance);
					if (powerLeft > 1.1f){
						//1 turn of adrenaline for each 20% above 100% proc rate
						Buff.affect(enemy, Adrenaline.class, Math.round(5*(powerLeft-1f)));
					}

					return 0;
				}
				return damage;
			case 6:
				//Elastic Effect
				procChance = (level+1f)/(level+5f) * powerMulti;
				if (Random.Float() < procChance) {
					//trace a ballistica to our target (which will also extend past them
					Ballistica trajectory = new Ballistica(hero.pos, enemy.pos, Ballistica.STOP_TARGET);
					//trim it to just be the part that goes past them
					trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size()-1), Ballistica.PROJECTILE);
					//knock them back along that ballistica
					WandOfBlastWave.throwChar(enemy,
							trajectory,
							Math.round(2 * powerMulti),
							true,
							true,
							hero);
				}
				return damage;
			case 7:
				//Grim Effect
				if (enemy.isImmune(Grim.class)) {
					return damage;
				}
				level = Math.max( 0, level );
				//scales from 0 - 50% based on how low hp the enemy is, plus 0-5% per level
				float maxChance = 0.5f + .05f*level;
				maxChance *= powerMulti;

				//we defer logic using an actor here so we can know the true final damage
				//see Char.damage
				Buff.affect(enemy, Grim.GrimTracker.class).maxChance = maxChance;

				if (enemy.buff(Grim.GrimTracker.class) != null){
					enemy.buff(Grim.GrimTracker.class).qualifiesForBadge = true;
				}
				return damage;
			case 8:
				//Lucky Effect
				procChance = (level+4f)/(level+40f) * powerMulti;
				if (enemy.HP <= damage && Random.Float() < procChance){

					float powerLeft = Math.max(1f, procChance);

					//default is -5: 80% common, 20% uncommon, 0% rare
					//ring level increases by 1 for each 20% above 100% proc rate
					Buff.affect(enemy, Lucky.LuckProc.class).ringLevel = -10 + Math.round(5*powerLeft);
				}
				return damage;
			case 9:
				//Shocking Effect
				procChance = (level+1f)/(level+4f) * powerMulti;
				if (Random.Float() < procChance) {
					ArrayList<Lightning.Arc> arcs = new ArrayList<>();
					ArrayList<Char> affected = new ArrayList<>();
					affected.clear();
					arcs.clear();

					Shocking.arc(hero, enemy, 2, affected, arcs);

					affected.remove(enemy); //defender isn't hurt by lightning
					for (Char ch : affected) {
						if (ch.alignment != hero.alignment) {
							ch.damage(Math.round(damage * 0.4f * powerMulti), hero);
						}
					}
				}
				return damage;
			case 10:
				//Vampiric Effect
				float missingPercent = (hero.HT - hero.HP) / (float)hero.HT;
				float healChance = 0.05f + .25f*missingPercent;

				healChance *= powerMulti;

				if (Random.Float() < healChance){

					float powerLeft = Math.max(1f, healChance);

					//heals for 50% of damage dealt
					int healAmt = Math.round(damage * 0.5f * powerLeft);
					hero.heal(healAmt);
				}
				return damage;
			case 11:
				//Eldritch Effect
				Buff.prolong( enemy, Terror.class, 10f*powerMulti + 5f ).object = hero.id();
				return damage;
			case 12:
				//Stunning Effect
				Buff.prolong( enemy, Paralysis.class, 1f * powerMulti );
				enemy.sprite.emitter().burst(Speck.factory(Speck.LIGHT), 12 );
				return damage;
			case 13:
				//Venomous Effect
				Buff.affect( enemy, Poison.class ).extend( (level/2f + 1) * powerMulti );
				CellEmitter.center(enemy.pos).burst( PoisonParticle.SPLASH, 5 );
				return damage;
			case 14:
				//Vorpal Effect
				Buff.affect(enemy, Bleeding.class).set((damage/10f) * powerMulti);
				Splash.at( enemy.sprite.center(), -PointF.PI / 2, PointF.PI / 6, enemy.sprite.blood(), 10 );
				return damage;
		}
	}

	private static int onElements(Hero hero, Char enemy, int damage) {
		if (Random.Float() < 0.2f) {
			for (Buff buff : hero.buffs()) {
				if (buff.type == Buff.buffType.NEGATIVE
						&& !(buff instanceof AllyBuff)
						&& !(buff instanceof LostInventory)){
					buff.detach();
				}
				hero.sprite.emitter().burst(Speck.factory(Speck.LIGHT), 12 );
			}
		}
		return damage;
	}

	private static int onEnergy(Hero hero, Char enemy, int damage) {
		if (Random.Float() < 0.25f) {
			for (Buff b : hero.buffs()){
				if (b instanceof Artifact.ArtifactBuff && !((Artifact.ArtifactBuff) b).isCursed() ) {
					((Artifact.ArtifactBuff) b).charge(hero, 0.5f);
				}
			}
			ScrollOfRecharging.charge(hero);
		}

		return damage;
	}

	private static int onEvasion(Hero hero, Char enemy, int damage) {
		float enemyAcc = enemy.attackSkill(hero)*4;	
		int heroEva = hero.defenseSkill(enemy);
		if (Random.Float() < heroEva/enemyAcc) {
			Buff.prolong(hero, EvasiveMove.class, 1.0001f);
		}
		return damage;
	}

	private static int onForce(Hero hero, Char enemy, int damage) {
		switch (Random.Int(20)) {
			case 0:
				Buff.affect(enemy, Paralysis.class, 1f);
				Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
				return damage;
			case 1:
				Buff.affect(enemy, Daze.class, 3f);
				Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
				return damage;
			case 2:
				Buff.affect(enemy, Cripple.class, 3f);
				Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
				return damage;
			case 3:
				Buff.affect(enemy, Vertigo.class, 3f);
				Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
				return damage;
			case 4:
				Buff.affect(enemy, Weakness.class, 3f);
				Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
				return damage;
			default:
				return damage;
		}
	}

	private static int onFuror(Hero hero, Char enemy, int damage) {
		if (Random.Float() < 0.5f) {
			Buff.affect(hero, Adrenaline.class, 1f);
		}
		return damage;
	}

	private static int onHaste(Hero hero, Char enemy, int damage) {
		if (Random.Float() < 0.2f) {
			Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste.class, 2f);
		}

		return damage;
	}

	private static int onMight(Hero hero, Char enemy, int damage) {
		return (int)(damage*(1+0.05f*(hero.STR()-10)));
	}

	private static int onSharpshooting(Hero hero, Char enemy, int damage) {
		if (enemy.buff(PinCushion.class) != null) {
			Item item = enemy.buff(PinCushion.class).grabOne();
			if (item.doPickUp(hero, enemy.pos)) {
				hero.spend(-1f); //casting the spell already takes a turn
				GLog.i( Messages.capitalize(Messages.get(hero, "you_now_have", item.name())) );
			} else {
				GLog.w(Messages.get(TelekineticGrab.class, "cant_grab"));
				Dungeon.level.drop(item, enemy.pos).sprite.drop();
			}
			WandOfBlastWave.throwChar(enemy, new Ballistica(hero.pos, enemy.pos, Ballistica.MAGIC_BOLT), 2, true, false, hero);
		}

		return damage;
	}

	private static int onTenacity(Hero hero, Char enemy, int damage) {
		Buff.prolong(hero, Enduring.class, 1.0001f);
		return damage;
	}

	private static int onWealth(Hero hero, Char enemy, int damage) {
		if (Random.Float() < 0.01f*RingOfWealth.dropChanceMultiplier(hero)) {
			Item prize = RingOfWealth.genConsumableDrop(-10);
			Dungeon.level.drop(prize, enemy.pos).sprite.drop();
			RingOfWealth.showFlareForBonusDrop(enemy.sprite);
		}
		return damage;
	}
}
