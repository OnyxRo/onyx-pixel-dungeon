/*
 * Onyx's Pixel Dungeon
 *
 * Hooks for Re Blue Archive PD (https://github.com/Hoto-Mocha/Re-Blue-Archive-Pixel-Dungeon), ported 2026-10-22.
 * The mod changed Hero, Char and Mob themselves. Here its additions sit in one place and those classes call them
 * with one line each. Everything here only does something for the mod's students, their talents and their buffs,
 * so other heroes play as before.
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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBAArmorBreak;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBAAvantGardeKunBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBACallOfStar;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBAChaseMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBAConversation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBADoubleBarrelMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.GreaterHaste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBALittleAngry;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBANoticeTracker;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBAQuestEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBARabbitSquadBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBASnipe;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBASupportDrone;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBASwiftMovement;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBAYuzuStatus;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.aris.RBADivision;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.hoshino.RBAShieldParry;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.izuna.RBABlink;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mari.RBAMariTrinity;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mika.RBAPerfectDeception;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.shiroko.RBAGPSRoute;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.shiroko.RBAPenetrationShot;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.miracles.RBAHolyMantle;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.miracles.RBALightBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.miracles.RBAMariMiracle;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.miracles.RBAPray;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.miracles.RBAProtection;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.fox.RBAKurumi;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.fox.RBANiko;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.fox.RBAOtogi;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.fox.RBAYukino;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EnergyParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.active.RBABicycle;
import com.shatteredpixel.shatteredpixeldungeon.items.active.RBAGrenade;
import com.shatteredpixel.shatteredpixeldungeon.items.active.RBAIronHorus;
import com.shatteredpixel.shatteredpixeldungeon.items.active.RBATrashBin;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RBACrossNecklace;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RBASuperNova;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.RBAGun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.RBABeyondTheLumination;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.quick.RBAQuickWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sungrass;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class RBAHero {

	/** True for the eleven students of Re Blue Archive PD. */
	public static boolean isStudent( HeroClass cls ){
		return cls != null && cls.name().startsWith("RBA_");
	}

	public static boolean isStudent( Hero hero ){
		return hero != null && isStudent(hero.heroClass);
	}

	/** Students other than Aris fight with guns, and find guns where others find melee weapons. */
	public static boolean usesGuns( Hero hero ){
		return isStudent(hero) && hero.heroClass != HeroClass.RBA_ARIS;
	}

	// ---- speech lines, the mod's yellI, yellP, yellN, yellW and yellH --------------------------

	private static String line( Hero hero, String str ){
		if (!isStudent(hero)) return null;
		if (hero.buff(RBADivision.DivisionBuff.class) != null) str = "...";
		String key = hero.heroClass.name() + "_" + str;
		String text = Messages.get(Hero.class, key);
		return text.equals(Messages.NO_TEXT_FOUND) ? null : text;
	}

	private static String say( Hero hero, String text ){
		GLog.newLine();
		return Messages.titleCase(hero.name()) + ": \"" + text + "\" ";
	}

	public static void yellI( Hero hero, String str ){
		String t = line(hero, str);
		if (t != null) GLog.i( "%s", say(hero, t) );
	}

	public static void yellP( Hero hero, String str ){
		String t = line(hero, str);
		if (t != null) GLog.p( "%s", say(hero, t) );
	}

	public static void yellN( Hero hero, String str ){
		String t = line(hero, str);
		if (t != null) GLog.n( "%s", say(hero, t) );
	}

	public static void yellW( Hero hero, String str ){
		String t = line(hero, str);
		if (t != null) GLog.w( "%s", say(hero, t) );
	}

	public static void yellH( Hero hero, String str ){
		String t = line(hero, str);
		if (t != null) GLog.h( "%s", say(hero, t) );
	}

	/** Sets every talent point back to 0, for the Scroll of Talent Reset. */
	public static void resetTalent( Hero hero ){
		for (LinkedHashMap<Talent, Integer> tier : hero.talents){
			for (Talent f : tier.keySet()){
				tier.put(f, 0);
			}
		}
	}

	// ---- Hero --------------------------------------------------------------------------------------

	public static int maxHP( Hero hero, int HT ){
		if (hero.hasTalent(Talent.RBA_MIYAKO_T2_5)) {
			HT = Math.round(HT*(1f+0.1f*hero.pointsInTalent(Talent.RBA_MIYAKO_T2_5))); //+10%/+20% at +1/+2
		}
		return HT;
	}

	public static int strBonus( Hero hero, int STR ){
		if (hero.hasTalent(Talent.RBA_MIKA_T3_1)){
			return (int)Math.floor(STR * (0.03f + 0.05f*hero.pointsInTalent(Talent.RBA_MIKA_T3_1)));
		}
		return 0;
	}

	/** The subclass buffs that come back when the hero is revived, read before live() clears buffs. */
	public static ArrayList<Class<? extends Buff>> liveKeep( Hero hero ){
		ArrayList<Class<? extends Buff>> keep = new ArrayList<>();
		if (hero.buff(RBAAvantGardeKunBuff.class) != null) keep.add(RBAAvantGardeKunBuff.class);
		if (hero.buff(RBAConversation.class) != null) keep.add(RBAConversation.class);
		if (hero.buff(RBARabbitSquadBuff.class) != null) keep.add(RBARabbitSquadBuff.class);
		return keep;
	}

	public static void liveRestore( Hero hero, ArrayList<Class<? extends Buff>> keep ){
		for (Class<? extends Buff> c : keep){
			Buff b = hero.buff(c);
			if (b != null) b.detach();
			Buff.affect(hero, c);
		}
	}

	public static void afterAttack( Hero hero ){
		if (!(hero.belongings.attackingWeapon() instanceof MissileWeapon)
				&& hero.buff(RBATalents.InstantFocusingTracker.class) != null){
			hero.buff(RBATalents.InstantFocusingTracker.class).detach();
		}
	}

	public static boolean infiniteAccuracy( Hero hero, KindOfWeapon wep ){
		return wep instanceof RBAGun.Bullet && hero.buff(RBALightBullet.LightMagazine.class) != null;
	}

	private static float focus( int points ){
		switch (points){
			default: case 1: return 2;
			case 2: return 5;
			case 3: return Float.POSITIVE_INFINITY;
		}
	}

	public static float accuracy( Hero hero, KindOfWeapon wep, float accuracy ){
		if (!(wep instanceof MissileWeapon)) {
			if (hero.buff(RBATalents.InstantFocusingTracker.class) != null) {
				accuracy *= focus(hero.pointsInTalent(Talent.RBA_HOSHINO_EX1_3));
			} else if (hero.buff(RBATalents.PerfectPrecisionTracker.class) != null) {
				accuracy *= focus(hero.pointsInTalent(Talent.RBA_NOA_T3_1));
			}
		}
		if (hero.buff(RBABicycle.BicycleBuff.class) != null) {
			accuracy *= 0.5f+0.25f*hero.pointsInTalent(Talent.RBA_SHIROKO_T3_1);
		}
		return accuracy;
	}

	public static int accuracyBonus( Hero hero ){
		if (hero.buff(RBATalents.TakingAimTracker.class) != null) {
			return 1+2*hero.pointsInTalent(Talent.RBA_NOA_T1_3) * (int) hero.buff(RBATalents.TakingAimTracker.class).count();
		}
		return 0;
	}

	public static boolean dodges( Hero hero, Char enemy ){
		if (hero.hasTalent(Talent.RBA_ARIS_EX2_2) && Random.Float() < 0.1f*hero.pointsInTalent(Talent.RBA_ARIS_EX2_2)){
			if (hero.canAttack(enemy) && !hero.isCharmedBy(enemy)){
				Buff.affect(hero, RBATalents.RiposteTracker.class).enemy = enemy;
			}
			return true;
		}
		if (hero.buff(RBAShieldParry.ParryBuff.class) != null) {
			hero.buff(RBAShieldParry.ParryBuff.class).onParry(hero, enemy);
			return true;
		}
		if (hero.buff(RBAYuzuStatus.UZQMode.class) != null) {
			hero.buff(RBAYuzuStatus.UZQMode.class).countDown(1);
			return true;
		}
		return false;
	}

	public static float evasion( Hero hero, float evasion ){
		if (hero.hasTalent(Talent.RBA_SHIROKO_EX1_2) && hero.buff(RBABicycle.BicycleBuff.class) != null) {
			evasion *= Math.max(1, 1+(hero.speed()-1)*hero.pointsInTalent(Talent.RBA_SHIROKO_EX1_2)/6f);
		}
		return evasion;
	}

	public static String defenseVerb( Hero hero ){
		if (hero.buff(RBATrashBin.EvasionBuff.class) != null){
			hero.buff(RBATrashBin.EvasionBuff.class).detach();
		}
		if (hero.hasTalent(Talent.RBA_MIYAKO_T2_4)) {
			if (hero.belongings.weapon() instanceof RBAGun && Random.Float() < 0.25f * hero.pointsInTalent(Talent.RBA_MIYAKO_T2_4)) {
				((RBAGun)hero.belongings.weapon()).manualReload();
			}
		}
		if (hero.buff(RBAShieldParry.ParryBuff.class) != null) {
			com.watabou.noosa.audio.Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY, 1, Random.Float(0.96f, 1.05f));
			return Messages.get(com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Monk.class, "parried");
		}
		return null;
	}

	public static int bonusDR( Hero hero ){
		int dr = 0;
		if (hero.belongings.weapon() instanceof RBABeyondTheLumination) {
			RBABeyondTheLumination.defenseProc(hero, hero.belongings.weapon(), 0);
		}
		if (hero.hasTalent(Talent.RBA_ARIS_EX1_2) && hero.buff(RBASuperNova.SuperNovaCooldown.class) != null) {
			dr += Random.NormalIntRange(hero.pointsInTalent(Talent.RBA_ARIS_EX1_2), 4*hero.pointsInTalent(Talent.RBA_ARIS_EX1_2));
		}
		if (RBAIronHorus.hasBuff(hero)) {
			dr += RBAIronHorus.drRoll(hero);
		}
		if (hero.buff(RBAAvantGardeKunBuff.OnBoard.class) != null) {
			dr += hero.buff(RBAAvantGardeKunBuff.OnBoard.class).drRoll();
		}
		if (hero.buff(RBAMariTrinity.TrinityBuff.class) != null) {
			dr += hero.buff(RBAMariTrinity.TrinityBuff.class).drRoll(hero);
		}
		return dr;
	}

	public static float speed( Hero hero, float speed ){
		if (hero.hasTalent(Talent.RBA_IZUNA_T2_3) && Dungeon.level != null && Dungeon.level.water[hero.pos]) {
			speed *= 1f + 0.5f*hero.pointsInTalent(Talent.RBA_IZUNA_T2_3);
		}
		if (hero.hasTalent(Talent.RBA_ARIS_T3_1)) {
			speed *= 1 + 0.05f*hero.pointsInTalent(Talent.RBA_ARIS_T3_1);
		}
		if (hero.buff(RBABicycle.BicycleBuff.class) != null) {
			speed *= 2f;
		}
		if (hero.buff(RBAGPSRoute.RouteBuff.class) != null && hero.buff(RBAGPSRoute.RouteBuff.class).onPath()) {
			speed *= 3f;
		}
		if (hero.pointsInTalent(Talent.RBA_MIYU_EX2_3) >= 3 && Dungeon.level != null) {
			int t = Dungeon.level.map[hero.pos];
			if (t == Terrain.FURROWED_GRASS || t == Terrain.HIGH_GRASS) {
				speed *= 2f;
			}
		}
		return speed;
	}

	public static void onAct( Hero hero ){
		if (hero.hasTalent(Talent.RBA_NONOMI_T3_2) && hero.buff(RBATalents.AutoReloadBuff.class) == null) {
			Buff.affect(hero, RBATalents.AutoReloadBuff.class);
		}
		if (hero.subClass == HeroSubClass.RBA_LITTLE_ANGRY && hero.buff(RBALittleAngry.class) == null) {
			Buff.affect(hero, RBALittleAngry.class);
		}
		if (hero.buff(RBANoticeTracker.class) != null) Item.updateQuickslot();
	}

	public static int attackProc( Hero hero, final Char enemy, final KindOfWeapon wep, int damage ){
		if (hero.subClass == HeroSubClass.RBA_DOUBLE_BARREL) {
			if (((wep instanceof RBAGun.Bullet && !((RBAGun.Bullet) wep).isSpecialShot()) || (hero.hasTalent(Talent.RBA_NOA_EX1_3) && !(wep instanceof RBAGun.Bullet)))
					&& enemy != hero && hero.buff(RBADoubleBarrelMark.class) == null) {
				Actor.add(new Actor() {
					{ actPriority = VFX_PRIO; }
					@Override
					protected boolean act() {
						if (enemy.isAlive()) {
							Buff.prolong(hero, RBADoubleBarrelMark.class, RBADoubleBarrelMark.DURATION).set(enemy.id(), 0);
						}
						Actor.remove(this);
						return true;
					}
				});
			}
		} else if (hero.subClass == HeroSubClass.RBA_CHASE) {
			if (wep instanceof MissileWeapon && !(wep instanceof RBAGun.Bullet) && enemy != hero) {
				Actor.add(new Actor() {
					{ actPriority = VFX_PRIO; }
					@Override
					protected boolean act() {
						if (enemy.isAlive()) {
							Buff.prolong(hero, RBAChaseMark.class, RBAChaseMark.DURATION+wep.buffedLvl()).set(enemy);
						}
						Actor.remove(this);
						return true;
					}
				});
			}
		}
		return damage;
	}

	public static int defenseProc( Hero hero, Char enemy, int damage ){
		if (hero.buff(RBASupportDrone.class) != null) {
			damage = hero.buff(RBASupportDrone.class).hit(hero, damage);
		}
		if (enemy == hero && hero.heroClass == HeroClass.RBA_YUZU && hero.belongings.attackingWeapon() instanceof RBAGun.Bullet) {
			damage = Math.round(damage * 0.25f);
		}
		if (hero.heroClass == HeroClass.RBA_MARI && enemy != null && enemy.sprite != null) {
			com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.fantasy.RBAThunder.thunderEffect(enemy.sprite);
			enemy.damage(RBAMariMiracle.PunishmentThunder.thunderDamage(hero), new RBAMariMiracle.PunishmentThunder());
		}
		if (hero.buff(RBAProtection.ProtectionBuff.class) != null){
			damage -= hero.buff(RBAProtection.ProtectionBuff.class).defenseBonus(hero);
		}
		damage = RBATalents.onDefenseProc(hero, enemy, damage);
		return damage;
	}

	public static int damageTaken( Hero hero, int dmg, Object src ){
		float damage = dmg;
		if (!(src instanceof Char) && hero.buff(RBAPray.PrayResistance.class) != null){
			RBACrossNecklace.CrossRecharge crossBuff = hero.buff(RBACrossNecklace.CrossRecharge.class);
			if (crossBuff != null) {
				damage *= 0.7f-0.5f*(1f-crossBuff.chargePercent());
				if (hero.hasTalent(Talent.RBA_MARI_T1_4)) {
					damage -= hero.pointsInTalent(Talent.RBA_MARI_T1_4);
				}
			}
		}
		if (hero.buff(Talent.WarriorFoodImmunity.class) != null){
			if (hero.pointsInTalent(Talent.RBA_ARIS_T2_1) == 1)       damage /= 4f;
			else if (hero.pointsInTalent(Talent.RBA_ARIS_T2_1) == 2)  damage = 0;
		}
		dmg = Math.max(0, Math.round(damage));
		if (hero.hasTalent(Talent.RBA_NONOMI_T2_3)) {
			int enemyNo = 0;
			for (Char ch : Actor.chars()) {
				if (ch.alignment == Char.Alignment.ENEMY && Dungeon.level.heroFOV[ch.pos]) enemyNo++;
			}
			dmg = (int)Math.ceil(dmg*(float)Math.pow((0.925-0.025*hero.pointsInTalent(Talent.RBA_NONOMI_T2_3)), enemyNo));
		}
		return dmg;
	}

	public static void healthWarn( Hero hero, boolean critical ){
		if (!isStudent(hero) || Random.Int(2) != 0) return;
		RBAHero.yellN(hero, (critical ? "health_critical_" : "health_warn_") + (Random.Int(3) + 1));
	}

	public static float moveDelay( Hero hero, float delay ){
		if (hero.buff(RBABicycle.AccelerationBuff.class) != null) {
			hero.buff(RBABicycle.AccelerationBuff.class).detach();
			return 0;
		}
		return delay;
	}

	public static void onStep( Hero hero ){
		RBABicycle bicycle = hero.belongings.getItem(RBABicycle.class);
		if (bicycle != null) {
			if (hero.buff(RBABicycle.BicycleBuff.class) != null) {
				bicycle.onRide();
				ActionIndicator.refresh();
			} else {
				bicycle.chargeUp();
			}
		}
	}

	public static void onLevelUp( Hero hero ){
		if (!isStudent(hero)) return;
		if (Random.Int(100) == 0) {
			RBAHero.yellP(hero, "levelup_rare");
		} else {
			RBAHero.yellP(hero, "levelup_" + (1 + Random.Int(5)));
		}
		RBAAvantGardeKunBuff.onLevelUp(hero);
	}

	public static void onDie( Hero hero, Object cause ){
		RBAHero.yellN(hero, "die");
		if (cause instanceof RBAYukino || cause instanceof RBANiko || cause instanceof RBAKurumi || cause instanceof RBAOtogi) {
			((Mob)cause).yell(Messages.get(cause.getClass(), "kill"));
		}
	}

	public static void onAttackComplete( Hero hero ){
		if (hero.buff(RBAQuickWeapon.QuickWeaponTracker.class) != null) {
			hero.buff(RBAQuickWeapon.QuickWeaponTracker.class).onAttack();
		}
	}

	public static int searchDistance( Hero hero ){
		int bonus = 0;
		if (hero.heroClass == HeroClass.RBA_NOA) bonus++;
		if (hero.hasTalent(Talent.RBA_NOA_T2_3)) bonus++;
		return bonus;
	}

	public static float searchChanceBonus( Hero hero ){
		return RBAYuzuStatus.yuzuSearchChanceBonus(hero);
	}

	public static float stealth( Hero hero, float stealth ){
		if (hero.buff(RBAPerfectDeception.DeceptionBuff.class) != null) return Char.INFINITE_ACCURACY;
		if (hero.heroClass == HeroClass.RBA_MIYU) stealth += 2;
		if (hero.subClass == HeroSubClass.RBA_CAMOUFLAGE) stealth += hero.lvl/3f;
		return stealth;
	}

	public static void onEnemyKill( Hero hero, Mob mob ){
		if (hero.heroClass == HeroClass.RBA_NONOMI) {
			int gain = (int)(Dungeon.gold*1.01f) - Dungeon.gold;
			Dungeon.gold = (int)(Dungeon.gold*1.01f);
			Item.updateQuickslot();
			if (gain > 0 && hero.sprite != null) hero.sprite.showStatusWithIcon( com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite.NEUTRAL, Integer.toString(gain), com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText.GOLD );
		}
		for (RBAGrenade g : hero.belongings.getAllItems(RBAGrenade.class)) {
			g.reloadByChance();
		}
		if (hero.subClass == HeroSubClass.RBA_SUPPORT_DRONE) {
			Buff.affect(hero, RBASupportDrone.class).kill();
		}
		if (hero.hasTalent(Talent.RBA_HOSHINO_T2_5)) {
			Buff.affect(hero, RBATalents.IntimidateBonusDamageBuff.class).set();
		}
		if (hero.buff(RBABicycle.DriftCooldown.class) != null) {
			hero.buff(RBABicycle.DriftCooldown.class).kill();
		}
		if (hero.hasTalent(Talent.RBA_SHIROKO_T1_4)) {
			Buff.affect(hero, Barrier.class).setShield(2+3*hero.pointsInTalent(Talent.RBA_SHIROKO_T1_4));
		}
		if (hero.pointsInTalent(Talent.RBA_NOA_EX1_3) >= 3 && Dungeon.level.adjacent(mob.pos, hero.pos)) {
			if (hero.belongings.weapon() instanceof RBAGun) ((RBAGun) hero.belongings.weapon()).quickReload();
			if (hero.belongings.secondWep() instanceof RBAGun) ((RBAGun) hero.belongings.secondWep()).quickReload();
		}
		if (hero.hasTalent(Talent.RBA_MIYU_T2_4)) {
			Buff.affect(hero, GreaterHaste.class).set(1+2*hero.pointsInTalent(Talent.RBA_MIYU_T2_4));
		}
		if (hero.subClass == HeroSubClass.RBA_TELESCOPE) {
			Buff.affect(hero, RBASnipe.class);
		}
		if (hero.hasTalent(Talent.RBA_MIYU_EX2_1) && hero.buff(RBATrashBin.TrashBinCooldown.class) != null
				&& Random.Float() < (float)hero.pointsInTalent(Talent.RBA_MIYU_EX2_1)/3) {
			hero.buff(RBATrashBin.TrashBinCooldown.class).detach();
		}
		if (hero.hasTalent(Talent.RBA_YUZU_EX1_1)) {
			RBAAvantGardeKunBuff.repairRobot(hero, hero.pointsInTalent(Talent.RBA_YUZU_EX1_1));
		}
		if (hero.hasTalent(Talent.RBA_IZUNA_T2_5) && !Dungeon.level.adjacent(hero.pos, mob.pos)) {
			Buff.affect(hero, GreaterHaste.class).set(1+2*hero.pointsInTalent(Talent.RBA_IZUNA_T2_5));
		}
		if (hero.hasTalent(Talent.RBA_IZUNA_EX2_2) && hero.buff(RBAChaseMark.ChaseTracker.class) != null) {
			final int pts = hero.pointsInTalent(Talent.RBA_IZUNA_EX2_2);
			new FlavourBuff() {
				{ actPriority = VFX_PRIO; }
				public boolean act() {
					Buff.affect(target, Invisibility.class, pts);
					Buff.affect(target, Haste.class, pts);
					return super.act();
				}
			}.attachTo(hero);
		}
		if (hero.buff(RBABlink.PerfectAssassination.class) != null) {
			hero.buff(RBABlink.PerfectAssassination.class).detach();
			Buff.affect(hero, Talent.LethalMomentumTracker.class, 0f);
		}
		if (hero.hasTalent(Talent.RBA_MIKA_EX2_3)) {
			Buff.affect(hero, Sungrass.Health.class).boost(hero.pointsInTalent(Talent.RBA_MIKA_EX2_3));
		}
	}

	// ---- Char ---------------------------------------------------------------------------------------

	/** The armor an enemy gets against the hero's attack, after Shattered's own changes to it. */
	public static int enemyArmor( Char attacker, Char enemy, int dr ){
		if (!(attacker instanceof Hero)) return dr;
		Hero h = (Hero) attacker;
		if (h.belongings.attackingWeapon() instanceof RBAGun.Bullet) {
			dr = Math.round(dr * ((RBAGun.Bullet) h.belongings.attackingWeapon()).whatBullet().armorFactor());
		}
		if (h.buff(RBAPenetrationShot.IgnoreArmor.class) != null){
			dr = 0;
		}
		if (h.heroClass == HeroClass.RBA_MARI && h.belongings.attackingWeapon() instanceof RBAGun.Bullet){
			dr = 0;
		}
		return dr;
	}

	public static float attackDamage( Char attacker, Char enemy, float dmg ){
		if (enemy.buff(RBAPray.PrayResistance.class) != null){
			RBACrossNecklace.CrossRecharge crossBuff = enemy.buff(RBACrossNecklace.CrossRecharge.class);
			if (crossBuff != null) {
				dmg *= 0.7f-0.5f*(1f-crossBuff.chargePercent());
				if (Dungeon.hero.hasTalent(Talent.RBA_MARI_T1_4)) {
					dmg -= Dungeon.hero.pointsInTalent(Talent.RBA_MARI_T1_4);
				}
			}
		}
		if (attacker instanceof Hero && attacker.buff(RBAYuzuStatus.class) != null) {
			dmg = attacker.buff(RBAYuzuStatus.class).criticalDamage((Hero)attacker, enemy, dmg);
		}
		return dmg;
	}

	private static void execute( Char attacker, Char enemy, float hpFraction ){
		if ( enemy.isAlive() && enemy.alignment != attacker.alignment
				&& !Char.hasProp(enemy, Char.Property.BOSS) && !Char.hasProp(enemy, Char.Property.MINIBOSS)
				&& (enemy.HP/(float)enemy.HT) <= hpFraction) {
			enemy.HP = 0;
			if (!enemy.isAlive()) {
				enemy.die(attacker);
			} else {
				enemy.damage(-1, attacker);
			}
			if (enemy.sprite != null) {
				enemy.sprite.showStatus(com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite.NEGATIVE,
						Messages.get(Talent.CombinedLethalityAbilityTracker.class, "executed"));
			}
		}
	}

	/** After the hero lands a hit, Hoshino's executes. */
	public static void afterHit( Char attacker, Char enemy ){
		if (!(attacker instanceof Hero)) return;
		Hero h = (Hero) attacker;
		RBATalents.TacticalSuppressTracker tacticalSuppress = enemy.buff(RBATalents.TacticalSuppressTracker.class);
		if (tacticalSuppress != null && h.belongings.attackingWeapon() instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon) {
			execute(attacker, enemy, 0.2f*h.pointsInTalent(Talent.RBA_HOSHINO_T3_1)/3f);
			tacticalSuppress.detach();
		}
		RBASwiftMovement.WeaknessTracker weakness = enemy.buff(RBASwiftMovement.WeaknessTracker.class);
		if (weakness != null){
			execute(attacker, enemy, 0.4f*h.pointsInTalent(Talent.RBA_HOSHINO_EX2_2)/3f);
			weakness.detach();
		}
	}

	public static void onMiss( Char attacker ){
		if (attacker instanceof Hero && ((Hero) attacker).hasTalent(Talent.RBA_NOA_T1_3)) {
			Buff.affect(attacker, RBATalents.TakingAimTracker.class).countUp(1);
		}
	}

	public static boolean alwaysDodges( Char defender ){
		return defender.buff(RBATrashBin.EvasionBuff.class) != null;
	}

	public static int armorBreak( Char ch ){
		RBAArmorBreak b = ch.buff(RBAArmorBreak.class);
		return b != null ? b.getDrDown() : 0;
	}

	public static float damageFactor( Char target, float damage, Object src ){
		if (Dungeon.hero == null || src == null) return damage;
		if (Dungeon.hero.subClass == HeroSubClass.RBA_CONVERSATION && target.alignment != Char.Alignment.ALLY && target != Dungeon.hero) {
			int debuffs = 0;
			for (Buff b : target.buffs()) {
				if (b.type == Buff.buffType.NEGATIVE) debuffs++;
			}
			damage *= 1+0.1f*debuffs;
		}
		if ((RBAMariMiracle.MIRACLE_DAMAGE.contains(src.getClass())
				|| (src == Dungeon.hero
				&& Dungeon.hero.heroClass == HeroClass.RBA_MARI
				&& Dungeon.hero.belongings.attackingWeapon() instanceof RBAGun.Bullet))
				&& Dungeon.hero.buff(RBAMariTrinity.TrinityBuff.class) != null
				&& Dungeon.hero.buff(RBAMariTrinity.TrinityBuff.class).isTrinityComplete()) {
			damage *= 1.5f;
		}
		return damage;
	}

	/** Damage taken after armor-like effects, before shields soak it. */
	public static int damageReduction( Char target, int dmg, Object src ){
		if (Dungeon.hero != null && Dungeon.hero.hasTalent(Talent.RBA_HOSHINO_T2_4)) {
			if (target.buff(RBAIronHorus.LightTacticalShieldBuff.class) != null) {
				dmg -= Math.round(target.buff(RBAIronHorus.LightTacticalShieldBuff.class).drRoll()*0.25f*(Dungeon.hero.pointsInTalent(Talent.RBA_HOSHINO_T2_4)+1));
			}
			if (target.buff(RBAIronHorus.TacticalShieldBuff.class) != null) {
				dmg -= Math.round(target.buff(RBAIronHorus.TacticalShieldBuff.class).drRoll()*0.25f*(Dungeon.hero.pointsInTalent(Talent.RBA_HOSHINO_T2_4)+1));
			}
			dmg = Math.max(dmg, 0);
		}
		boolean hunger = src instanceof com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
		if (target.buff(RBAProtection.ProtectionBuff.class) != null && !hunger) {
			dmg -= target.buff(RBAProtection.ProtectionBuff.class).damageReduction(Dungeon.hero);
			dmg = Math.max(dmg, 0);
		}
		if (target.buff(RBAHolyMantle.HolyMantleBuff.class) != null && !hunger && dmg > 0) {
			target.buff(RBAHolyMantle.HolyMantleBuff.class).detach();
			dmg = 0;
		}
		if (target.buff(RBAAvantGardeKunBuff.class) != null || target.buff(RBAAvantGardeKunBuff.OnBoard.class) != null) {
			dmg = RBAAvantGardeKunBuff.processDamage(target, dmg, src);
		}
		return dmg;
	}

	public static void onCharDie( Char ch ){
		for (Char c : Actor.chars()){
			RBADoubleBarrelMark m = c.buff(RBADoubleBarrelMark.class);
			if (m != null && m.object == ch.id()) m.detach();
		}
	}

	// ---- Mob ----------------------------------------------------------------------------------------

	public static int killExp( Mob mob, int exp ){
		if (Dungeon.hero.buff(RBAQuestEnemy.QuestEnemyTracker.class) != null) {
			exp *= 2;
			Dungeon.hero.buff(RBAQuestEnemy.QuestEnemyTracker.class).detach();
		}
		if (Dungeon.hero.subClass == HeroSubClass.RBA_CONVERSATION) {
			Buff.affect(Dungeon.hero, RBAConversation.class).charge();
		}
		return exp;
	}

	public static void onMobDie( Mob mob, Object cause ){
		Hero hero = Dungeon.hero;
		if (hero == null || !hero.isAlive()) return;
		if (mob.alignment == Char.Alignment.ENEMY && hero.hasTalent(Talent.RBA_MIKA_T2_4)) {
			Buff.affect(hero, Talent.LethalMomentumTracker.class, 0f);
			if (hero.pointsInTalent(Talent.RBA_MIKA_T2_4) >= 2) {
				Buff.affect(hero, GreaterHaste.class).set(3);
			}
		}
		if (cause == hero && Dungeon.level.heroFOV[mob.pos] && Random.Int(4) == 0) {
			RBAHero.yellI(hero, "enemy_defeated_" + (1 + Random.Int(5)));
		}
		if (cause instanceof Hero || cause instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon
				|| cause instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon.Enchantment
				|| cause instanceof com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb) {
			onEnemyKill(hero, mob);
		}
		if (hero.buff(RBAChaseMark.class) != null && hero.buff(RBAChaseMark.class).getEnemyID() == mob.id()) {
			hero.buff(RBAChaseMark.class).detach();
		}
	}

	public static float lootMulti(){
		return RBAYuzuStatus.yuzuDropMulti(Dungeon.hero);
	}

	public static String mobDesc( Mob mob ){
		String desc = "";
		for (Buff b : mob.buffs(RBAQuestEnemy.class)){
			desc += "\n\n_" + Messages.titleCase(b.name()) + "_\n" + b.desc();
		}
		return desc;
	}

	/** Izuna's quiet steps, like the Rogue's Silent Steps. */
	public static boolean silentSteps( Mob mob, Char ch ){
		return ch instanceof Hero && ((Hero) ch).hasTalent(Talent.RBA_IZUNA_T2_4)
				&& mob.distance(ch) >= 4 - ((Hero) ch).pointsInTalent(Talent.RBA_IZUNA_T2_4);
	}
}
