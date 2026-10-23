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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArmorEnhance;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Awakening;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BowMasterSkill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cloaking;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Enduring;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.EvasiveMove;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FirstAidBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HorseRiding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Juggling;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Pray;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RouletteOfDeath;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Undead;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.gunner.ReinforcedArmor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.gunner.Riot;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.medic.AngelWing;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Monk;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EnergyParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Dewdrop;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.KnightsShield;
import com.shatteredpixel.shatteredpixeldungeon.items.Sheath;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Afterimage;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Obfuscation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfTalent;
import com.shatteredpixel.shatteredpixeldungeon.items.remains.BrokenShield;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfArcana;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.DeathSword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.LargeSword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Nunchaku;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.alchemy.Lance;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.alchemy.LanceNShield;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.alchemy.ObsidianShield;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.alchemy.UnholyBible;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.bow.BowWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.Gun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

/**
 * Everything Re-ARranged PD added to Shattered's Hero for its six heroes and their gear (the Knight's shield, the
 * Samurai's sheath and critical hits, guns, bows, its new weapons). Hero calls each method from one line.
 * The parts for its rework of Shattered's own heroes and its five challenges are left out, see PORTING.md.
 */
public class RearrangedHero {

	public static int bonusHT( Hero hero ){
		int ht = 0;
		if (hero.buff(ElixirOfTalent.ElixirOfTalentHTBoost.class) != null){
			ht += hero.buff(ElixirOfTalent.ElixirOfTalentHTBoost.class).boost();
		}
		if (hero.buff(DeathSword.MaxHPBoost.class) != null) {
			ht += hero.buff(DeathSword.MaxHPBoost.class).HTBonus();
		}
		ht += RearrangedRework.bonusHT(hero);
		return ht;
	}

	public static int bonusTalentPoints( Hero hero, int tier ){
		if (tier == 3 && hero.subClass != HeroSubClass.NONE && hero.buff(ElixirOfTalent.BonusTalentTracker.class) != null
				&& hero.lvl >= Talent.tierLevelThresholds[tier]-1) {
			return 4;
		}
		return 0;
	}

	public static float accuracy( Hero hero, KindOfWeapon wep, float accuracy ){
		if (hero.subClass.is(HeroSubClass.GUNSLINGER) && hero.justMoved && wep instanceof MissileWeapon) {
			accuracy *= 0.25f*(1+0.5f*hero.pointsInTalent(Talent.MOVING_SHOT));
		}
		if (hero.hasTalent(Talent.ACC_ENHANCE)) {
			accuracy *= 1 + 0.05f * hero.pointsInTalent(Talent.ACC_ENHANCE);
		}
		if (hero.buff(LargeSword.LargeSwordBuff.class) != null) {
			accuracy *= hero.buff(LargeSword.LargeSwordBuff.class).getAccuracyFactor();
		}
		if (hero.buff(Sheath.Sheathing.class) != null) {
			accuracy *= 1.2f;
		}
		accuracy *= Juggling.accuracyFactor(hero);
		accuracy = com.shatteredpixel.shatteredpixeldungeon.RearrangedChallenges.accuracy(accuracy);
		if (RearrangedRework.neverMisses(hero)) return Float.POSITIVE_INFINITY;
		if (hero.buff(UnholyBible.Demon.class) != null) {
			return Float.POSITIVE_INFINITY;
		}
		if (hero.subClass.is(HeroSubClass.MASTER) &&
				hero.buff(Sheath.Sheathing.class) != null &&
				hero.buff(Sheath.FlashSlashCooldown.class) == null &&
				hero.buff(Sheath.DashAttackTracker.class) == null) {
			return Float.POSITIVE_INFINITY;
		}
		if (hero.buff(BowWeapon.PenetrationShotBuff.class) != null && wep instanceof BowWeapon.Arrow) {
			return Float.POSITIVE_INFINITY;
		}
		return accuracy;
	}

	public static boolean dodgesEverything( Hero hero, Char enemy ){
		if (RearrangedRework.dodgesEverything(hero, enemy)) return true;
		if (enemy != null && hero.buff(Nunchaku.ParryTracker.class) != null){
			if (hero.canAttack(enemy) && !hero.isCharmedBy(enemy)){
				Buff.affect(hero, Nunchaku.RiposteTracker.class).enemy = enemy;
			}
			return true;
		}
		return hero.buff(EvasiveMove.class) != null;
	}

	public static float evasion( Hero hero, float evasion ){
		if (hero.hasTalent(Talent.EVA_ENHANCE)) {
			evasion *= 1 + 0.05f * hero.pointsInTalent(Talent.EVA_ENHANCE);
		}
		if (hero.buff(UnholyBible.Demon.class) != null) {
			evasion /= 2;
		}
		evasion = RearrangedRework.evasion(hero, evasion);
		evasion = com.shatteredpixel.shatteredpixeldungeon.RearrangedChallenges.evasion(evasion);
		return evasion;
	}

	public static float evasionAfterArmor( Hero hero, float evasion ){
		KnightsShield shield = hero.belongings.getItem(KnightsShield.class);
		if (shield != null && shield.hasGlyph(Afterimage.class, hero)) {
			evasion *= Math.pow(1.2f, shield.buffedLvl()) * RingOfArcana.enchantPowerMultiplier(hero);
		}
		if (hero.hasTalent(Talent.BREAKTHROUGH) && hero.isHeroDebuffed()) {
			evasion += hero.defenseSkill * (0.9f+0.3f*hero.pointsInTalent(Talent.BREAKTHROUGH));
		}
		return evasion;
	}

	public static String defenseVerb( Hero hero ){
		String raParry = RearrangedRework.defenseVerb(hero);
		if (raParry != null) return raParry;
		Nunchaku.ParryTracker nunchakuParry = hero.buff(Nunchaku.ParryTracker.class);
		if (nunchakuParry != null){
			nunchakuParry.parried = true;
			if (hero.sprite != null && hero.sprite.visible) {
				Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY, 1, Random.Float(0.96f, 1.05f));
			}
			return Messages.get(Monk.class, "parried");
		}
		RouletteOfDeath roulette = hero.buff(RouletteOfDeath.class);
		if (hero.hasTalent(Talent.HONORABLE_SHOT) && roulette != null && roulette.overHalf()) {
			Buff.prolong(hero, Talent.HonorableShotTracker.class, 1f);
		}
		Awakening awakening = hero.buff(Awakening.class);
		if (awakening != null && awakening.isAwaken() && hero.hasTalent(Talent.HASTE_EVASION)) {
			Buff.prolong(hero, Haste.class, 1+hero.pointsInTalent(Talent.HASTE_EVASION));
		}
		return null;
	}

	public static int bonusDR( Hero hero ){
		int dr = 0;
		KnightsShield shield = hero.belongings.getItem(KnightsShield.class);
		if (shield != null) {
			int shieldDr = Random.NormalIntRange(0, shield.defenseFactor());
			if (shieldDr > 0) dr += shieldDr;
		}
		if (!hero.heroClass.is(HeroClass.KNIGHT) && hero.hasTalent(Talent.HARD_SHIELD)) {
			dr += Random.NormalIntRange(0, 2*hero.pointsInTalent(Talent.HARD_SHIELD));
		}
		if (hero.hasTalent(Talent.PARRING)) {
			dr += Random.NormalIntRange(0, 1+hero.pointsInTalent(Talent.PARRING));
		}
		ReinforcedArmor.ReinforcedArmorTracker reArmor = hero.buff(ReinforcedArmor.ReinforcedArmorTracker.class);
		if (reArmor != null) dr += reArmor.blockingRoll();
		return dr;
	}

	public static int rollCritical( Hero hero, KindOfWeapon wep, int dmg ){
		if (wep instanceof Weapon) {
			if (Random.Float() < critChance(hero, (Weapon)wep)) {
				dmg = criticalDamage(hero, dmg, (Weapon)wep);
			} else if (Sheath.isFlashSlash()) {
				Buff.prolong(hero, Sheath.FlashSlashCooldown.class, (30-5*hero.pointsInTalent(Talent.STATIC_PREPARATION))-1);
			}
		}
		return dmg;
	}

	public static float speed( Hero hero, float speed ){
		KnightsShield shield = hero.belongings.getItem(KnightsShield.class);
		if (shield != null) {
			speed = shield.speedFactor(hero, speed);
		}
		if (hero.buff(ReinforcedArmor.ReinforcedArmorTracker.class) != null && hero.hasTalent(Talent.PLATE_ADD)) {
			speed *= (1 - hero.pointsInTalent(Talent.PLATE_ADD)/8f);
		}
		if (hero.buff(Riot.RiotTracker.class) != null && hero.hasTalent(Talent.HASTE_MOVE)) {
			speed *= 1f + 0.25f * hero.pointsInTalent(Talent.HASTE_MOVE);
		}
		if (Dungeon.level != null && Dungeon.level.map[hero.pos] == Terrain.FURROWED_GRASS && hero.hasTalent(Talent.JUNGLE_EXPLORE)) {
			speed *= Math.pow(1.2f, hero.pointsInTalent(Talent.JUNGLE_EXPLORE));
		}
		if (hero.buff(HorseRiding.class) != null
				&& hero.hasTalent(Talent.PILOTING)
				&& hero.belongings.armor != null
				&& hero.STR() > hero.belongings.armor.STRReq()) {
			int aEnc = hero.STR() - hero.belongings.armor.STRReq();
			speed *= Math.pow(1+0.03f*hero.pointsInTalent(Talent.PILOTING), aEnc);
		}
		if (hero.buff(AngelWing.AngelWingBuff.class) != null) {
			speed *= 3f;
		}
		if (hero.hasTalent(Talent.LIGHT_MOVEMENT) && hero.belongings.armor != null) {
			int aEnc = hero.belongings.armor.STRReq() - hero.STR();
			if (aEnc < 0) {
				speed *= 1 + 0.05f * hero.pointsInTalent(Talent.LIGHT_MOVEMENT) * (-aEnc);
			}
		}
		speed *= BowMasterSkill.speedBoost(hero);
		speed = RearrangedRework.speed(hero, speed);
		return speed;
	}

	public static float critChance( Hero hero, Weapon wep ){
		float chance = 0;

		if (hero.heroClass.is(HeroClass.SAMURAI)) {
			chance = 0.01f;
			chance += 0.01f * (hero.lvl - 1);
			chance += Math.max(0, (0.02f + 0.005f*hero.pointsInTalent(Talent.WEAPON_MASTERY)) * (hero.STR() - wep.STRReq()));
		}

		if (hero.heroClass.is(HeroClass.ARCHER) && wep instanceof MissileWeapon) {
			chance = 0.03f;
			chance += 0.03f*(hero.lvl-1);
			chance = Math.min(chance, 0.3f);
			chance += Math.max(0, 0.03f*(hero.STR()-wep.STRReq()));
		}

		if (hero.buff(Sheath.CertainCrit.class) != null) {
			chance += 1f;
		}
		if (hero.hasTalent(Talent.BASIC_PRACTICE)) {
			chance += 0.02f * hero.pointsInTalent(Talent.BASIC_PRACTICE);
		}
		if (wep instanceof MissileWeapon && hero.hasTalent(Talent.CRITICAL_THROW)) {
			chance += 0.125f * hero.pointsInTalent(Talent.CRITICAL_THROW);
		}

		Awakening awakening = hero.buff(Awakening.class);
		if (awakening != null && awakening.isAwaken()) {
			if (hero.hasTalent(Talent.ACCELERATED_LETHALITY)) {
				chance += 0.1f*hero.pointsInTalent(Talent.ACCELERATED_LETHALITY);
			}
			chance += Math.max(0, 0.01f*(hero.defenseSkill(null) - (4 + hero.lvl)));
		}

		if (hero.hasTalent(Talent.UNEXPECTED_SLASH) && hero.buff(Sheath.Sheathing.class) != null) {
			chance += 0.1f*hero.pointsInTalent(Talent.UNEXPECTED_SLASH);
		}

		if (hero.buff(Sheath.Sheathing.class) != null) {
			if (hero.subClass.is(HeroSubClass.MASTER) &&
					hero.buff(Sheath.FlashSlashCooldown.class) == null &&
					hero.buff(Sheath.DashAttackTracker.class) == null &&
					hero.belongings.attackingWeapon() instanceof MeleeWeapon) {
				switch (hero.pointsInTalent(Talent.ENHANCED_CRIT)) {
					case 0: default: chance *= 1.5f; break;
					case 1: chance *= 1.6f; break;
					case 2: chance *= 1.75f; break;
					case 3: chance *= 2f; break;
				}
			} else {
				chance *= 1.2f;
			}
			chance += 0.05f;
		}

		return GameMath.gate(0, chance, 2);
	}

	public static int criticalDamage( Hero hero, int damage, Weapon wep ){
		int max = wep.max();
		if (hero.STR() > wep.STRReq() && !(wep instanceof Gun.Bullet)) {
			max += hero.STR()-wep.STRReq();
		}
		float multi = 1f+Math.max(0, critChance(hero, wep)-1);

		damage = (int)(max * 0.75f + damage * 0.25f);

		multi += 0.05f * hero.pointsInTalent(Talent.LETHAL_POWER);

		if (hero.hasTalent(Talent.POWERFUL_CRIT) && wep instanceof MissileWeapon) {
			multi += 0.1f * hero.pointsInTalent(Talent.POWERFUL_CRIT);
		}

		if (Sheath.isFlashSlash()) {
			multi += 0.15f * hero.pointsInTalent(Talent.POWERFUL_SLASH);
		}

		Buff.affect(hero, Sheath.CriticalAttack.class);

		Awakening awakening = hero.buff(Awakening.class);
		if (awakening != null && awakening.isAwaken() && hero.hasTalent(Talent.STABLE_BARRIER)) {
			int shield = 1;
			int maxShield = Math.round(hero.HT * 0.2f * hero.pointsInTalent(Talent.STABLE_BARRIER));
			int curShield = 0;
			if (hero.buff(Barrier.class) != null) curShield = hero.buff(Barrier.class).shielding();
			shield = Math.min(shield, maxShield-curShield);
			if (shield > 0) {
				Buff.affect(hero, Barrier.class).incShield(shield);
				if (hero.sprite != null) hero.sprite.showStatus( CharSprite.POSITIVE, Messages.get(Dewdrop.class, "shield", shield) );
			}
		}

		return Math.round(damage * multi);
	}

	public static void onAct( Hero hero ){
		RearrangedRework.onAct(hero);
		if (hero.subClass.is(HeroSubClass.SLAYER) && hero.buff(Awakening.class) == null) {
			Buff.affect(hero, Awakening.class).indicate();
		}
		if (hero.buff(Undead.class) != null) {
			hero.HP = 0;
		}
	}

	public static void onRest( Hero hero, boolean fullRest ){
		if (fullRest) return;
		RearrangedRework.onRest(hero);
		if (hero.belongings.weapon instanceof LargeSword || hero.belongings.secondWep instanceof LargeSword){
			Buff.affect(hero, LargeSword.LargeSwordBuff.class).setDamageFactor(hero.belongings.weapon.buffedLvl(), (hero.belongings.secondWep instanceof LargeSword));
			if (hero.sprite != null) {
				Emitter e = hero.sprite.centerEmitter();
				if (e != null) e.burst(EnergyParticle.FACTORY, 15);
			}
		}
		if (Dungeon.level.map[hero.pos] == Terrain.FURROWED_GRASS && hero.subClass.is(HeroSubClass.SPECIALIST)) {
			boolean adjacentMob = false;
			for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
				if (Dungeon.level.adjacent(hero.pos, mob.pos)) {
					adjacentMob = true;
					break;
				}
			}
			if (hero.pointsInTalent(Talent.STEALTH_MASTER) > 1) {
				adjacentMob = false;
			}
			if (!adjacentMob) {
				if (hero.hasTalent(Talent.INTO_THE_SHADOW) && hero.buff(Talent.IntoTheShadowCooldown.class) == null) {
					Buff.affect(hero, Invisibility.class, 3f*hero.pointsInTalent(Talent.INTO_THE_SHADOW));
					Buff.affect(hero, Talent.IntoTheShadowCooldown.class, 15);
				} else {
					Buff.affect(hero, Cloaking.class);
				}
			}
		}
	}

	public static int attackProc( Hero hero, Char enemy, KindOfWeapon wep, int damage ){
		damage = com.shatteredpixel.shatteredpixeldungeon.RearrangedChallenges.attackProc(hero, enemy, damage);
		if (hero.subClass.is(HeroSubClass.OUTLAW) && wep instanceof Gun.Bullet) {
			if (hero.hasTalent(Talent.HEADSHOT) && Random.Float() < 0.01f*hero.pointsInTalent(Talent.HEADSHOT)) {
				if (!Char.hasProp(enemy, Char.Property.BOSS) && !Char.hasProp(enemy, Char.Property.MINIBOSS) && enemy.alignment == Char.Alignment.ENEMY) {
					damage = enemy.HP;
				} else {
					damage = (int)(damage*1.2f);
				}
				if (enemy.sprite != null) enemy.sprite.emitter().burst( ShadowParticle.UP, 5 );
			}

			if (hero.buff(Talent.HonorableShotTracker.class) != null
					&& (enemy.HP/(float)enemy.HT) <= 0.4f*hero.pointsInTalent(Talent.HONORABLE_SHOT)/3f) {
				if (!Char.hasProp(enemy, Char.Property.BOSS) && !Char.hasProp(enemy, Char.Property.MINIBOSS)) {
					damage = enemy.HP;
				} else {
					damage = (int)(damage*1.2f);
				}
				if (enemy.sprite != null) enemy.sprite.emitter().burst( ShadowParticle.UP, 5 );
				hero.buff(Talent.HonorableShotTracker.class).detach();
			}

			if (hero.buff(RouletteOfDeath.class) == null || !hero.buff(RouletteOfDeath.class).timeToDeath()) {
				Buff.affect(hero, RouletteOfDeath.class).hit();
			} else {
				if (!Char.hasProp(enemy, Char.Property.BOSS) && !Char.hasProp(enemy, Char.Property.MINIBOSS)) {
					damage = enemy.HP;
					if (hero.belongings.weapon instanceof Gun) {
						((Gun)hero.belongings.weapon).quickReload();
					}
					if (hero.hasTalent(Talent.BULLET_TIME)) {
						for (Char ch : Actor.chars()) {
							if (Dungeon.level.heroFOV[ch.pos] && ch != hero && ch.alignment == Char.Alignment.ENEMY) {
								Buff.affect(ch, Slow.class, 4*hero.pointsInTalent(Talent.BULLET_TIME));
							}
						}
					}
				} else {
					damage = (int)(damage*1.2f);
				}
				if (enemy.sprite != null) enemy.sprite.emitter().burst( ShadowParticle.UP, 5 );
				hero.buff(RouletteOfDeath.class).detach();
			}
		}

		damage = RearrangedRework.attackProc(hero, enemy, wep, damage);
		com.shatteredpixel.shatteredpixeldungeon.RearrangedChallenges.mutate(hero, enemy, wep, damage);
		if (hero.buff(Sheath.CertainCrit.class) != null) {
			hero.buff(Sheath.CertainCrit.class).hit();
		}
		return damage;
	}

	//the Knight's shield blocks before armor glyphs work
	public static int shieldProc( Hero hero, Char enemy, int damage ){
		KnightsShield shield = hero.belongings.getItem(KnightsShield.class);
		if (shield != null) {
			damage = shield.proc( enemy, hero, damage );
		}
		return damage;
	}

	public static int defenseProc( Hero hero, Char enemy, int damage ){
		Talent.ArmorEmpower armorEmpower = hero.buff(Talent.ArmorEmpower.class);
		if (armorEmpower != null) {
			damage = armorEmpower.proc(damage);
		}
		if (hero.buff(Pray.Praying.class) != null) {
			hero.buff(Pray.Praying.class).defenseProc(enemy, damage);
		}
		if (hero.subClass.is(HeroSubClass.THERAPIST)) {
			if (hero.buff(FirstAidBuff.FirstAidBuffCooldown.class) == null) {
				Buff.affect(hero, FirstAidBuff.class).set(damage);
			}
		}
		com.shatteredpixel.shatteredpixeldungeon.RearrangedChallenges.defenseProc(hero);
		RearrangedRework.defenseProc(hero, enemy);
		if (hero.buff(HorseRiding.class) != null) {
			hero.buff(HorseRiding.class).onDamage(damage);
		}
		if (hero.buff(ArmorEnhance.class) != null) {
			hero.buff(ArmorEnhance.class).defenseProc();
		}
		damage = RearrangedTalents.onDefenseProc(hero, enemy, damage);
		if (hero.buff(BrokenShield.DRBuff.class) != null) {
			damage = 0;
			hero.buff(BrokenShield.DRBuff.class).detach();
		}
		return damage;
	}

	public static int damageTaken( Hero hero, int dmg, Object src ){
		dmg = RearrangedRework.damageTaken(hero, dmg);
		if (hero.hasTalent(Talent.PROTECTION)) {
			dmg = (int)Math.ceil(dmg * (1 - hero.pointsInTalent(Talent.PROTECTION)/6f * ((float)(hero.HT - hero.HP)/hero.HT)));
		}
		LargeSword.LargeSwordBuff largeSwordBuff = hero.buff(LargeSword.LargeSwordBuff.class);
		if (largeSwordBuff != null) {
			dmg = (int)Math.ceil(dmg * largeSwordBuff.getDefenseFactor());
		}
		if (src == null) return dmg;
		boolean magic = AntiMagic.RESISTS.contains(src.getClass());
		if (!magic) return dmg;
		KnightsShield shield = hero.belongings.getItem(KnightsShield.class);
		if (shield != null && shield.hasGlyph(AntiMagic.class, hero)) {
			dmg -= AntiMagic.drRoll(hero, shield.buffedLvl());
			dmg = Math.max(dmg, 0);
		}
		KindOfWeapon[] weps = { hero.belongings.weapon(), hero.belongings.secondWep() };
		for (KindOfWeapon w : weps) {
			if (w instanceof ObsidianShield) {
				dmg -= ObsidianShield.drRoll(w.buffedLvl());
				dmg = Math.max(dmg, 0);
			}
			if (w instanceof LanceNShield && !((LanceNShield) w).stance) {
				dmg -= LanceNShield.drRoll(w.buffedLvl());
				dmg = Math.max(dmg, 0);
			}
		}
		return dmg;
	}

	public static void onStep( Hero hero ){
		if (hero.belongings.weapon instanceof Lance ||
				hero.belongings.secondWep instanceof Lance ||
				(hero.belongings.weapon instanceof LanceNShield && ((LanceNShield)hero.belongings.weapon).stance) ||
				(hero.belongings.secondWep instanceof LanceNShield && ((LanceNShield)hero.belongings.secondWep).stance)) {
			Buff.affect(hero, Lance.LanceBuff.class).setDamageFactor(1 + (hero.speed()), hero.belongings.secondWep instanceof Lance || (hero.belongings.secondWep instanceof LanceNShield && ((LanceNShield) hero.belongings.secondWep).stance));
		}
		if (hero.buff(Talent.RollingTracker.class) != null && hero.belongings.weapon instanceof Gun && Dungeon.bullet > 1 && !((Gun) hero.belongings.weapon).isReloaded()) {
			Dungeon.bullet --;
			((Gun)hero.belongings.weapon).manualReload();
			hero.buff(Talent.RollingTracker.class).detach();
		}
		if (hero.hasTalent(Talent.QUICK_RELOAD)
				&& hero.belongings.weapon instanceof Gun
				&& Random.Float() < 0.03f * hero.pointsInTalent(Talent.QUICK_RELOAD)
				&& Dungeon.bullet > 1
				&& !((Gun) hero.belongings.weapon).isReloaded()) {
			Dungeon.bullet --;
			((Gun)hero.belongings.weapon).manualReload();
		}
		if (hero.hasTalent(Talent.MIND_VISION) && Random.Float() < 0.01f*hero.pointsInTalent(Talent.MIND_VISION)) {
			Buff.affect(hero, MindVision.class, 1f);
		}
		BowMasterSkill.move();
		Juggling.move();
	}

	public static boolean preventsDeath( Hero hero, Object cause ){
		if (RearrangedRework.preventsDeath(hero)) return true;
		if (hero.buff(Enduring.class) != null) {
			hero.HP = 1;
			return true;
		}
		if (hero.hasTalent(Talent.UNDEAD) && !(cause instanceof Undead)) {
			Buff.affect(hero, Undead.class, hero.pointsInTalent(Talent.UNDEAD));
			return true;
		}
		//the Death Knight spends collected souls to stay alive
		com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SoulCollect souls = hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SoulCollect.class);
		if (souls != null && souls.soulUsed(hero)) {
			return true;
		}
		return false;
	}

	public static boolean attackUsesTurn( Hero hero ){
		if (RearrangedRework.attackIsFree(hero)) return false;
		if (hero.buff(Awakening.class) != null && hero.buff(Awakening.class).isAwaken() && hero.buff(Sheath.CriticalAttack.class) != null) {
			return false;
		}
		return !Sheath.isFlashSlash();
	}

	public static void afterAttack( Hero hero, Char target ){
		if (hero.buff(Sheath.Sheathing.class) != null) {
			hero.buff(Sheath.Sheathing.class).detach();
			if (target != null && !target.isAlive() && Random.Float() < hero.pointsInTalent(Talent.QUICK_SHEATHING)/3f) {
				Buff.affect(hero, Sheath.Sheathing.class);
			}
		}
		if (!hero.heroClass.is(HeroClass.SAMURAI) && hero.hasTalent(Talent.QUICK_SHEATHING) && target != null && !target.isAlive()) {
			Buff.affect(hero, Haste.class, 3f*hero.pointsInTalent(Talent.QUICK_SHEATHING));
		}
		if (hero.buff(Sheath.CriticalAttack.class) != null) {
			hero.buff(Sheath.CriticalAttack.class).detach();
		}
	}

	public static float stealth( Hero hero, float stealth ){
		stealth += hero.pointsInTalent(Talent.STEALTH);
		KindOfWeapon wep = hero.belongings.weapon;
		if (wep instanceof Gun) {
			switch (((Gun) wep).attachMod) {
				case NORMAL_ATTACH: default:
					break;
				case LASER_ATTACH:
					stealth -= 1;
					break;
				case FLASH_ATTACH:
					stealth -= 2;
					break;
			}
		}
		KnightsShield shield = hero.belongings.getItem(KnightsShield.class);
		if (shield != null && shield.hasGlyph(Obfuscation.class, hero)) {
			stealth += (1 + shield.buffedLvl()/3f) * shield.glyph.raProcChance(hero);
		}
		return stealth;
	}
}
