/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dobrokhotov
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArtifactRecharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BABlastMissile;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BABulletMomentumTracker;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BACriticalAttack;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BADoubleBarrelMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BADroneStrike;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BADroneStrikeCooldown;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BAInfiniteBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BAQuestEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BAShockBulletCooldown;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BAStickyGrenade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BAStunDrone;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BAStunDroneCooldown;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BATeleport;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.elementals.BAAPBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.BAAmmoBox;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.active.BABicycle;
import com.shatteredpixel.shatteredpixeldungeon.items.active.BABlastGrenade;
import com.shatteredpixel.shatteredpixeldungeon.items.active.BAClaymore;
import com.shatteredpixel.shatteredpixeldungeon.items.active.BAFlashGrenade;
import com.shatteredpixel.shatteredpixeldungeon.items.active.BAHPGrenade;
import com.shatteredpixel.shatteredpixeldungeon.items.active.BAHandGrenade;
import com.shatteredpixel.shatteredpixeldungeon.items.active.BAIronHorus;
import com.shatteredpixel.shatteredpixeldungeon.items.active.BALaser;
import com.shatteredpixel.shatteredpixeldungeon.items.active.BALevelAnalyzer;
import com.shatteredpixel.shatteredpixeldungeon.items.active.BARainbowGrenade;
import com.shatteredpixel.shatteredpixeldungeon.items.active.BARegrowGrenade;
import com.shatteredpixel.shatteredpixeldungeon.items.active.BASmokeGrenade;
import com.shatteredpixel.shatteredpixeldungeon.items.active.BATrashBin;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfWealth;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.BAThrowingSet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Lucky;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.BASuperNova;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.BAGun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.BAHG;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.BASG;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

/** Blue Archive PD's changes to the hero, chars and monsters, each called from one line in Shattered's classes.
 *  Every hook checks for a student or one of her talents, so other heroes play as before. */
public class BAHero {

	//the students talk in the log, in the mod's colours
	public static void yellI( Char ch, String str ){ GLog.newLine(); GLog.i( "%s: \"%s\" ", Messages.titleCase(ch.name()), str ); }
	public static void yellP( Char ch, String str ){ GLog.newLine(); GLog.p( "%s: \"%s\" ", Messages.titleCase(ch.name()), str ); }
	public static void yellN( Char ch, String str ){ GLog.newLine(); GLog.n( "%s: \"%s\" ", Messages.titleCase(ch.name()), str ); }
	public static void yellW( Char ch, String str ){ GLog.newLine(); GLog.w( "%s: \"%s\" ", Messages.titleCase(ch.name()), str ); }
	public static void yellH( Char ch, String str ){ GLog.newLine(); GLog.h( "%s: \"%s\" ", Messages.titleCase(ch.name()), str ); }

	private static String key( Hero hero, String what ){
		return hero.heroClass.name().replace("BA_", "") + what;
	}

	public static void heal( Char ch, int amount ){
		amount = Math.min( amount, ch.HT - ch.HP );
		if (amount > 0 && ch.isAlive()) {
			ch.HP += amount;
			if (ch.sprite != null) {
				ch.sprite.emitter().start( Speck.factory( Speck.HEALING ), 0.4f, 1 );
				ch.sprite.showStatus( CharSprite.POSITIVE, Integer.toString( amount ) );
			}
		}
	}

	//cell offsets the mod added to PathFinder, a 5x5 circle and a 7x7 square, both with the middle
	public static int[] neighbours21(){
		int w = Dungeon.level.width();
		return new int[]{-2*w-1, -2*w, -2*w+1, -w-2, -w-1, -w, -w+1, -w+2, -2, -1, 0, +1, +2, +w-2, +w-1, +w, +w+1, +w+2, +2*w-1, +2*w, +2*w+1};
	}

	public static int[] neighbours49(){
		int w = Dungeon.level.width();
		int[] out = new int[49];
		int n = 0;
		for (int dy = -3; dy <= 3; dy++) for (int dx = -3; dx <= 3; dx++) out[n++] = dx + dy * w;
		return out;
	}

	public static com.shatteredpixel.shatteredpixeldungeon.effects.CheckedCell checkedCell( int pos, int visSource ){
		com.shatteredpixel.shatteredpixeldungeon.effects.CheckedCell cc = new com.shatteredpixel.shatteredpixeldungeon.effects.CheckedCell();
		cc.reset(pos, visSource);
		return cc;
	}

	//the scroll of talent reset takes back every talent point
	public static void resetTalents( Hero hero ){
		for (java.util.LinkedHashMap<Talent, Integer> tier : hero.talents){
			for (Talent f : tier.keySet()){
				tier.put(f, 0);
			}
		}
	}

	private static boolean bullet( Object wep ){
		return wep instanceof BAGun.Bullet;
	}

	// ---- Hero ----

	public static int bonusHT( Hero hero ){
		return 5 * hero.pointsInTalent(Talent.BA_MAX_HEALTH);
	}

	//Yuzu's bullets can land a critical hit
	public static float critMulti( Hero hero, MissileWeapon wep, float dmgMulti ){
		if (bullet(wep) && hero.heroClass == HeroClass.BA_YUZU) {
			float critChance = 0.1f + 0.01f * hero.lvl;
			if (Random.Float() < critChance) {
				dmgMulti *= 1.2f + 0.5f * hero.pointsInTalent(Talent.BA_CRIT_DAMAGE);
				Buff.affect(hero, BACriticalAttack.class);
			}
		}
		return dmgMulti;
	}

	/** True when the double tap talent makes this shot sure to hit. */
	public static boolean doubleTap( Hero hero, Char target, KindOfWeapon wep ){
		if (bullet(wep) && hero.buff(BATalents.DoubleTapTracker.class) != null) {
			if (hero.pointsInTalent(Talent.BA_DOUBLE_TAP) == 2) {
				Buff.affect(target, Cripple.class, 3f);
			}
			hero.buff(BATalents.DoubleTapTracker.class).detach();
			return true;
		}
		return false;
	}

	public static float accuracy( Hero hero, Char target, KindOfWeapon wep, float accuracy ){
		if (wep instanceof BASG.SGBullet) {
			if (!Dungeon.level.adjacent(hero.pos, target.pos) && hero.subClass != HeroSubClass.BA_HOSHINO_EX_TACTICAL_PRESS) {
				return 0;
			}
			accuracy *= 2f;
		}
		if (hero.buff(BABicycle.BicycleBuff.class) != null && hero.subClass != HeroSubClass.BA_SHIROKO_EX_PROFESSIONAL_RIDE) {
			accuracy *= 0.25f;
		}
		BALaser laser = hero.belongings.getItem(BALaser.class);
		if (wep instanceof MissileWeapon && laser != null && target.buff(BALaser.LaserTargeted.class) != null) {
			accuracy *= 1 + 0.2f * (laser.buffedLvl()+1);
		}
		return accuracy;
	}

	public static float evasionMulti( Hero hero ){
		if (hero.hasTalent(Talent.BA_PROFESSIONAL_RIDE_2) && hero.buff(BABicycle.BicycleBuff.class) != null) {
			return 1 + hero.speed() * hero.pointsInTalent(Talent.BA_PROFESSIONAL_RIDE_2) / 6f;
		}
		return 1f;
	}

	public static int drBonus( Hero hero ){
		BAIronHorus.TacticalShieldBuff shield = hero.buff(BAIronHorus.TacticalShieldBuff.class);
		return shield != null ? shield.armorBonus() : 0;
	}

	public static float speedMulti( Hero hero ){
		float speed = 1f;
		if (hero.buff(BABicycle.BicycleBuff.class) != null) speed *= 2f;
		if (hero.hasTalent(Talent.BA_ROBOT_CLEANER)) speed *= 1 + 0.1f * hero.pointsInTalent(Talent.BA_ROBOT_CLEANER);
		if (hero.hasTalent(Talent.BA_SPEEDY_MOVE) && hero.belongings.weapon instanceof BAHG) {
			speed *= 1 + 0.1f * hero.pointsInTalent(Talent.BA_SPEEDY_MOVE);
		}
		return speed;
	}

	/** True when Miyu's kill with a bullet makes her next shot free, like lethal momentum. Uses up the tracker. */
	public static boolean freeAttack( Char ch ){
		BABulletMomentumTracker t = ch.buff(BABulletMomentumTracker.class);
		if (t != null){
			t.detach();
			return true;
		}
		return false;
	}

	public static void onAct( Hero hero ){
		if (!BAHeroes.isBA(hero)) return;
		if (hero.subClass == HeroSubClass.BA_MIYAKO_EX_STUNDRONE && hero.buff(BAStunDrone.class) == null) {
			Buff.affect(hero, BAStunDrone.class);
		}
		if (hero.subClass == HeroSubClass.BA_MIYAKO_EX_DRONESTRIKE && hero.buff(BADroneStrike.class) == null) {
			Buff.affect(hero, BADroneStrike.class);
		}
		//also when the subclass came from a plain Tengu's mask
		if (hero.subClass == HeroSubClass.BA_IZUNA_EX_TELEPORT && hero.buff(BATeleport.class) == null) {
			Buff.affect(hero, BATeleport.class);
		}
		if (hero.hasTalent(Talent.BA_PATH_PREDICTION)) {
			for (Char ch : Actor.chars()) {
				if (ch.alignment == Char.Alignment.ENEMY && ch.HP <= ch.HT*(hero.pointsInTalent(Talent.BA_PATH_PREDICTION))/4) {
					Buff.append(hero, TalismanOfForesight.CharAwareness.class, 2).charID = ch.id();
				}
			}
		}
	}

	public static int attackProc( final Hero hero, final Char enemy, int damage, KindOfWeapon wep ){
		if (!BAHeroes.isBA(hero)) return damage;
		if (hero.subClass == HeroSubClass.BA_NOA_EX_DOUBLE_BARREL && bullet(wep) && enemy != hero
				&& hero.buff(BADoubleBarrelMark.class) == null && !((BAGun.Bullet) wep).isDoubleBarrel) {
			Actor.add(new Actor() {
				{ actPriority = VFX_PRIO; }
				@Override
				protected boolean act() {
					if (enemy.isAlive()) {
						Buff.prolong(hero, BADoubleBarrelMark.class, BADoubleBarrelMark.DURATION).set(enemy.id(), 0);
					}
					Actor.remove(this);
					return true;
				}
			});
		}
		if (hero.buff(BATalents.EmpoweringMagic.class) != null) {
			damage = Math.round(damage * (1 + 0.125f*hero.pointsInTalent(Talent.BA_EMPOWERING_MAGIC)));
		}
		if (hero.hasTalent(Talent.BA_PIEZOELECTRICITY) && hero.buff(BASuperNova.SuperNovaCooldown.class) != null) {
			hero.buff(BASuperNova.SuperNovaCooldown.class).hit();
		}
		if (Dungeon.level.adjacent(hero.pos, enemy.pos) && hero.hasTalent(Talent.BA_NO_WAY) && hero.buff(BATalents.NoWayCooldown.class) == null) {
			Ballistica trajectory = new Ballistica(hero.pos, enemy.pos, Ballistica.STOP_TARGET);
			trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size()-1), Ballistica.PROJECTILE);
			WandOfBlastWave.throwChar(enemy, trajectory, 1+hero.pointsInTalent(Talent.BA_NO_WAY), true, false, hero);
			Buff.affect(hero, BATalents.NoWayCooldown.class, 30f);
		}
		if (bullet(wep) && hero.hasTalent(Talent.BA_SHOOTING_WEAKNESS) && hero.buff(BATalents.ShootingWeaknessCooldown.class) == null) {
			Buff.affect(enemy, Vulnerable.class, 3f);
			Buff.affect(hero, BATalents.ShootingWeaknessCooldown.class, 40-10*hero.pointsInTalent(Talent.BA_SHOOTING_WEAKNESS));
		}
		return damage;
	}

	public static int defenseProc( Hero hero, Char enemy, int damage ){
		if (!BAHeroes.isBA(hero)) return damage;
		if (hero.hasTalent(Talent.BA_PLATE_ADD)) {
			int reduction = hero.pointsInTalent(Talent.BA_PLATE_ADD);
			damage = Math.max(0, damage - Random.IntRange(reduction, reduction+1));
			if (Random.Float() < 0.25f) {
				enemy.damage(1, hero);
			}
		}
		if (hero.HP - damage < hero.HT/4 && hero.hasTalent(Talent.BA_EMERGENCY_HEALING) && hero.buff(BATalents.EmergencyHealingCooldown.class) == null) {
			heal(hero, 2+3*hero.pointsInTalent(Talent.BA_EMERGENCY_HEALING));
			Buff.affect(hero, BATalents.EmergencyHealingCooldown.class, 50f);
		}
		return damage;
	}

	//Hoshino's tactical shield also blocks magic with the magic shield talent
	public static int magicShield( Hero hero, int dmg, Object src ){
		BAIronHorus.TacticalShieldBuff shield = hero.buff(BAIronHorus.TacticalShieldBuff.class);
		if (shield != null && src != null && hero.hasTalent(Talent.BA_MAGIC_SHIELD) && AntiMagic.RESISTS.contains(src.getClass())){
			dmg -= Math.round((0.25f + 0.25f*hero.pointsInTalent(Talent.BA_MAGIC_SHIELD)) * shield.armorBonus());
			dmg = Math.max(dmg, 0);
		}
		return dmg;
	}

	public static void healthYell( Hero hero, boolean critical ){
		if (!BAHeroes.isBA(hero) || Random.Int(2) != 0) return;
		yellN(hero, Messages.get(Hero.class, key(hero, (critical ? "_health_critical_" : "_health_warn_") + (Random.Int(3) + 1))));
	}

	public static void levelUpYell( Hero hero ){
		if (!BAHeroes.isBA(hero)) return;
		if (Random.Int(100) == 0) {
			yellP(hero, Messages.get(Hero.class, key(hero, "_levelup_rare")));
		} else {
			yellP(hero, Messages.get(Hero.class, key(hero, "_levelup_" + (1 + Random.Int(5)))));
		}
	}

	public static void dieYell( Hero hero ){
		if (BAHeroes.isBA(hero)) yellN(hero, Messages.get(Hero.class, key(hero, "_die")));
	}

	/** Bicycle charge while walking. Returns true when the acceleration buff makes this step free. */
	public static boolean onMove( Hero hero ){
		BABicycle bicycle = hero.belongings.getItem(BABicycle.class);
		if (bicycle != null) {
			if (hero.buff(BABicycle.BicycleBuff.class) != null) {
				bicycle.use();
				com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator.refresh();
				if (hero.hasTalent(Talent.BA_ELECTRIC_BICYCLE)) {
					if (Random.Float() < 0.05f) {
						Buff.prolong(hero, Recharging.class, 3);
					}
					if (hero.pointsInTalent(Talent.BA_ELECTRIC_BICYCLE) > 1 && Random.Float() < 0.03f) {
						Buff.affect(hero, ArtifactRecharge.class).set(2);
					}
				}
			} else {
				bicycle.chargeUp();
			}
		}
		if (hero.buff(BABicycle.AccelerationBuff.class) != null) {
			hero.buff(BABicycle.AccelerationBuff.class).detach();
			return true;
		}
		return false;
	}

	public static float stealthBonus( Hero hero ){
		float bonus = 0;
		if (hero.heroClass == HeroClass.BA_MIYU) {
			bonus += 1 + hero.lvl/9f + hero.pointsInTalent(Talent.BA_BLURRY_FIGURE);
		}
		//the mod gave this through worn armor
		if (hero.belongings.armor() != null) bonus += armorStealth(hero);
		return bonus;
	}

	public static int searchBonus( Hero hero ){
		int bonus = hero.pointsInTalent(Talent.BA_SKILLED_SEARCH);
		if (hero.heroClass == HeroClass.BA_IZUNA) bonus++; //searches as far as the Rogue
		if (hero.hasTalent(Talent.BA_WIDER_SEARCH)) bonus++;
		return bonus;
	}

	public static float searchChance( Hero hero ){
		return 1 + 0.25f * hero.pointsInTalent(Talent.BA_QUALITY_ASSURANCE);
	}

	// ---- Char ----

	public static int enemyDR( Char attacker, Char enemy, int dr ){
		if (!(attacker instanceof Hero) || !BAHeroes.isBA((Hero) attacker)) return dr;
		Hero h = (Hero) attacker;
		KindOfWeapon wep = h.belongings.attackingWeapon();
		//Izuna's thrown weapons pierce armor at range, as the Sniper's do
		if (wep instanceof MissileWeapon && h.heroClass == HeroClass.BA_IZUNA && !Dungeon.level.adjacent(h.pos, enemy.pos)) return 0;
		if (bullet(wep)) {
			if (h.heroClass == HeroClass.BA_HOSHINO || h.buff(BAAPBullet.class) != null) return 0;
			if (h.hasTalent(Talent.BA_DOUBLE_BARREL_3)) dr = Math.round(dr * (3 - h.pointsInTalent(Talent.BA_DOUBLE_BARREL_3)) / 3f);
		}
		return dr;
	}

	public static float damageMulti( Char attacker, Char enemy ){
		if (attacker instanceof Hero && ((Hero) attacker).heroClass == HeroClass.BA_NONOMI
				&& bullet(((Hero) attacker).belongings.attackingWeapon())
				&& enemy.properties().contains(Char.Property.INORGANIC)) {
			return 1.5f;
		}
		return 1f;
	}

	//the tactical move talent reloads when a student dodges
	public static void onDodge( Char defender ){
		if (defender instanceof Hero && ((Hero) defender).hasTalent(Talent.BA_TACTICAL_MOVE)
				&& Random.Float() < 0.25f * ((Hero) defender).pointsInTalent(Talent.BA_TACTICAL_MOVE)
				&& ((Hero) defender).belongings.weapon instanceof BAGun) {
			((BAGun) ((Hero) defender).belongings.weapon).manualReload();
		}
	}

	public static String damageText( String text ){
		if (Dungeon.hero != null && Dungeon.hero.buff(BACriticalAttack.class) != null) {
			Dungeon.hero.buff(BACriticalAttack.class).detach();
			return text + "!";
		}
		return text;
	}

	public static void onCharDie( Char dead ){
		for (Char ch : Actor.chars()) {
			if (ch.buff(BADoubleBarrelMark.class) != null && ch.buff(BADoubleBarrelMark.class).object == dead.id()) {
				ch.buff(BADoubleBarrelMark.class).detach();
			}
		}
	}

	public static float timeScale( Char ch ){
		if (ch.buff(BAStickyGrenade.class) != null && Dungeon.hero != null && Dungeon.hero.hasTalent(Talent.BA_STICKY_GRENADE_2)) {
			return (float) (1 - 1 / Math.pow(2, 4 - Dungeon.hero.pointsInTalent(Talent.BA_STICKY_GRENADE_2)));
		}
		return 1f;
	}

	// ---- Mob ----

	public static int expMulti( Mob mob, int exp ){
		Hero hero = Dungeon.hero;
		if (hero.hasTalent(Talent.BA_QUEST_CLEAR) && hero.buff(BAQuestEnemy.QuestEnemyTracker.class) != null) {
			exp *= 1 + hero.pointsInTalent(Talent.BA_QUEST_CLEAR);
			hero.buff(BAQuestEnemy.QuestEnemyTracker.class).detach();
		}
		return exp;
	}

	public static void onMobDie( Mob mob, Object cause ){
		Hero hero = Dungeon.hero;
		if (hero == null || !BAHeroes.isBA(hero)) return;
		if (bullet(cause) && hero.heroClass == HeroClass.BA_MIYU) {
			Buff.affect(hero, BABulletMomentumTracker.class, 0f);
		}
		if (cause == hero && Dungeon.level.heroFOV[mob.pos] && Random.Int(4) == 0) {
			yellI(hero, Messages.get(Hero.class, key(hero, "_enemy_defeated_" + (1 + Random.Int(5)))));
			GLog.newLine();
		}
		if (cause == hero) {
			if (hero.hasTalent(Talent.BA_DEFENSIVE_FEEDBACK)) {
				Buff.affect(hero, Barrier.class).setShield(2 + 3 * hero.pointsInTalent(Talent.BA_DEFENSIVE_FEEDBACK));
			}
			if (hero.buff(BABicycle.DriftCooldown.class) != null) hero.buff(BABicycle.DriftCooldown.class).kill();
			BASmokeGrenade smoke = hero.belongings.getItem(BASmokeGrenade.class);
			if (smoke != null && Random.Float() < smoke.dropChance) smoke.reload();
			BAHandGrenade grenade = hero.belongings.getItem(BAHandGrenade.class);
			if (grenade != null && Random.Float() < grenade.dropChance + 0.05f * hero.pointsInTalent(Talent.BA_SUPPLY)) grenade.reload();
			BAClaymore claymore = hero.belongings.getItem(BAClaymore.class);
			if (claymore != null && Random.Float() < claymore.dropChance + 0.025f * hero.pointsInTalent(Talent.BA_SUPPLY)) claymore.reload();
			BABlastGrenade blast = hero.belongings.getItem(BABlastGrenade.class);
			if (blast != null && Random.Float() < blast.dropChance) blast.reload();
			BAHPGrenade hpGrenade = hero.belongings.getItem(BAHPGrenade.class);
			if (hpGrenade != null && Random.Float() < hpGrenade.dropChance) hpGrenade.reload();
			BAFlashGrenade flash = hero.belongings.getItem(BAFlashGrenade.class);
			if (flash != null && Random.Float() < flash.dropChance) flash.reload();
			BARegrowGrenade regrow = hero.belongings.getItem(BARegrowGrenade.class);
			if (regrow != null && Random.Float() < regrow.dropChance) regrow.reload();
			BARainbowGrenade rainbow = hero.belongings.getItem(BARainbowGrenade.class);
			if (rainbow != null && Random.Float() < rainbow.dropChance) rainbow.reload();
			if (hero.hasTalent(Talent.BA_SERIAL_COMBO) && hero.belongings.weapon instanceof BAGun) {
				((BAGun) hero.belongings.weapon).quickReload();
				if (hero.pointsInTalent(Talent.BA_SERIAL_COMBO) > 1) {
					((BAGun) hero.belongings.weapon).manualReload(1, true);
				}
			}
			BAThrowingSet throwingSet = hero.belongings.getItem(BAThrowingSet.class);
			if (throwingSet != null) throwingSet.reload();
			if (hero.hasTalent(Talent.BA_ENERGY_COLLECT)) {
				ArtifactRecharge.chargeArtifacts(hero, hero.pointsInTalent(Talent.BA_ENERGY_COLLECT)/2f);
				ScrollOfRecharging.charge(hero);
			}
			if (hero.buff(BATeleport.TeleportCooldown.class) != null) hero.buff(BATeleport.TeleportCooldown.class).kill();
			if (hero.buff(BABlastMissile.class) != null) hero.buff(BABlastMissile.class).mobsUpdate(mob);
			BAInfiniteBullet infinite = hero.buff(BAInfiniteBullet.class);
			if (infinite != null && hero.hasTalent(Talent.BA_LARGE_MAGAZINE_2)) infinite.add();
		}
		//every kill shortens the students' gadget cooldowns
		if (hero.buff(BAStunDroneCooldown.class) != null) hero.buff(BAStunDroneCooldown.class).kill();
		if (hero.buff(BADroneStrikeCooldown.class) != null) hero.buff(BADroneStrikeCooldown.class).kill();
		if (hero.buff(BALaser.LaserCooldown.class) != null) hero.buff(BALaser.LaserCooldown.class).kill();
		if (hero.buff(BAInfiniteBullet.infiniteBulletCooldown.class) != null) hero.buff(BAInfiniteBullet.infiniteBulletCooldown.class).kill();
		if (hero.buff(BATrashBin.TrashBinCooldown.class) != null) hero.buff(BATrashBin.TrashBinCooldown.class).kill();
		if (hero.buff(BALevelAnalyzer.LevelAnalyzerCooldown.class) != null) hero.buff(BALevelAnalyzer.LevelAnalyzerCooldown.class).kill();
		if (hero.buff(BAAmmoBox.AmmoBoxCooldown.class) != null) hero.buff(BAAmmoBox.AmmoBoxCooldown.class).kill();
		if (hero.buff(BAShockBulletCooldown.class) != null) hero.buff(BAShockBulletCooldown.class).kill();
	}

	//Yuzu's quest enemies drop extra loot
	public static void questLoot( Mob mob ){
		if (mob.buff(BAQuestEnemy.class) == null) return;
		int amount = 1 + Dungeon.hero.pointsInTalent(Talent.BA_QUEST_CLEAR);
		for (int i = 0; i < amount; i++) {
			Dungeon.level.drop(RingOfWealth.genConsumableDrop(Dungeon.hero.lvl/5), mob.pos).sprite.drop();
			Lucky.showFlare(mob.sprite);
		}
	}

	// ---- weapons ----

	public static int throwMasterStr(){
		return Dungeon.hero == null ? 0 : Dungeon.hero.pointsInTalent(Talent.BA_THROW_MASTER);
	}

	/** Noa throws to a cell up to 2 tiles away (more with projecting) even past enemies, the mod's own rule. */
	public static boolean noaThrow( Hero user, int dst, boolean projecting ){
		if (user.heroClass != HeroClass.BA_NOA) return false;
		int maxDist = 2;
		if (projecting) maxDist += Math.round(4 * com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon.Enchantment.genericProcChanceMultiplier(user));
		return (Dungeon.level.passable[dst] || Dungeon.level.avoid[dst]) && Dungeon.level.distance(user.pos, dst) <= maxDist;
	}

	//Izuna's explosion talent, a thrown weapon that misses blasts the cell it lands on
	public static void missedThrow( Hero user, int cell, Object src ){
		if (!user.hasTalent(Talent.BA_EXPLOSION_1) || Actor.findChar(cell) != null) return;
		com.watabou.noosa.audio.Sample.INSTANCE.play( com.shatteredpixel.shatteredpixeldungeon.Assets.Sounds.BLAST );
		WandOfBlastWave.BlastWave.blast(cell);
		for (int i : com.watabou.utils.PathFinder.NEIGHBOURS8){
			Char ch = Actor.findChar(cell + i);
			if (ch != null && ch.pos == cell + i) {
				Ballistica trajectory = new Ballistica(ch.pos, ch.pos + i, Ballistica.MAGIC_BOLT);
				WandOfBlastWave.throwChar(ch, trajectory, user.pointsInTalent(Talent.BA_EXPLOSION_1), false, true, src);
			}
		}
	}

	public static void missileProc( Char attacker, Char defender ){
		if (attacker == Dungeon.hero && Dungeon.hero.subClass == HeroSubClass.BA_IZUNA_EX_EXPLOSION && defender instanceof Mob
				&& !(defender instanceof com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC) && defender.isAlive()) {
			Buff.affect(Dungeon.hero, BABlastMissile.class).hit((Mob)defender);
		}
	}

	public static float durability(){
		return Dungeon.hero == null ? 1f : 1 + Dungeon.hero.pointsInTalent(Talent.BA_DURABLE_THROW)/3f;
	}

	/** Noa's handguns are one level higher in her hands. */
	public static int hgBonus( MeleeWeapon wep ){
		return Dungeon.hero != null && Dungeon.hero.heroClass == HeroClass.BA_NOA && wep instanceof BAHG ? 1 : 0;
	}

	//Blue Archive PD's gun decks, seven guns per tier with equal chances, found by students other than Aris
	private static final String GUNS = "com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.";
	public static Class<?>[] gunTier( int tier ){
		String[] kinds = {"AR", "GL", "HG", "MG", "SG", "SMG", "SR"};
		Class<?>[] out = new Class<?>[kinds.length];
		for (int i = 0; i < kinds.length; i++){
			try {
				out[i] = Class.forName(GUNS + kinds[i] + ".BA" + kinds[i] + "_tier" + tier);
			} catch (ClassNotFoundException e){
				throw new RuntimeException(e);
			}
		}
		return out;
	}

	/** A random gun of a tier from 1 to 5, rolled like a melee weapon of that tier. */
	public static MeleeWeapon randomGun( int tier ){
		Class<?>[] tierGuns = gunTier(Math.max(1, Math.min(5, tier)));
		MeleeWeapon w = (MeleeWeapon) com.watabou.utils.Reflection.newInstance(tierGuns[Random.Int(tierGuns.length)]);
		return (MeleeWeapon) w.random();
	}

	public static float missileSpeed( Object item ){
		if (!(item instanceof BAGun.Bullet)) return 1.5f; //the spirit kunai and shuriken fly like spirit arrows
		float speed = 3f;
		Hero hero = Dungeon.hero;
		if (hero != null && hero.hasTalent(Talent.BA_DOUBLE_BARREL_3)) speed *= 1+0.5f*hero.pointsInTalent(Talent.BA_DOUBLE_BARREL_3);
		if (hero != null && hero.hasTalent(Talent.BA_SNIPING_BULLET_1) && hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BASnipingBullet.class) != null) {
			speed *= 1+0.5f*hero.pointsInTalent(Talent.BA_SNIPING_BULLET_1);
		}
		return speed;
	}

	// ---- Level ----

	public static void onGrassTrampled( Hero hero ){
		if (!BAHeroes.isBA(hero)) return;
		if (hero.hasTalent(Talent.BA_INHERENT_WILDNESS)) {
			Buff.prolong(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility.class, 1+hero.pointsInTalent(Talent.BA_INHERENT_WILDNESS));
			com.watabou.noosa.audio.Sample.INSTANCE.play( com.shatteredpixel.shatteredpixeldungeon.Assets.Sounds.MELD );
		}
		if (hero.hasTalent(Talent.BA_NATURE_FRIENDLY)) {
			Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin.class).set(1+hero.pointsInTalent(Talent.BA_NATURE_FRIENDLY), 3);
		}
		if (hero.hasTalent(Talent.BA_HIDE_ON_BUSH)) {
			Buff.prolong(hero, Haste.class, 1+hero.pointsInTalent(Talent.BA_HIDE_ON_BUSH));
		}
		if (hero.pointsInTalent(Talent.BA_SNIPERS_INTUITION) > 1 && Random.Float() < 0.20f) {
			Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision.class, 1f);
		}
	}

	/** Miyu walks through grass like the Huntress, leaving it furrowed. */
	public static boolean furrowsGrass( Char ch ){
		return ch instanceof Hero && ((Hero) ch).heroClass == HeroClass.BA_MIYU;
	}

	/** Miyu's sniper's intuition at +3 sees through grass, as the Warden does. */
	public static boolean seesThroughGrass( Char ch ){
		return ch instanceof Hero && ((Hero) ch).pointsInTalent(Talent.BA_SNIPERS_INTUITION) > 2;
	}

	public static float viewDistMulti( Hero hero ){
		return 1f + 0.25f * hero.pointsInTalent(Talent.BA_SNIPING_BULLET_2);
	}

	public static int mindVisionRange( Hero hero, int range ){
		if (hero.hasTalent(Talent.BA_INTELLIGENCE)) range = Math.max(range, 1 + hero.pointsInTalent(Talent.BA_INTELLIGENCE));
		if (hero.hasTalent(Talent.BA_SNIPERS_INTUITION)) range = Math.max(range, 2);
		return range;
	}

	public static float regenDelay( Char target ){
		if (target instanceof Hero && ((Hero) target).hasTalent(Talent.BA_HEALING_FACTOR)) {
			return (8 - ((Hero) target).pointsInTalent(Talent.BA_HEALING_FACTOR)) / 8f;
		}
		return 1f;
	}

	/** Hoshino's foresight eyes, a chance of a secret hint on arriving, like the Rogue's foresight. */
	public static int foresightPoints( Hero hero ){
		return hero.pointsInTalent(Talent.BA_FORESIGHT_EYES);
	}

	public static float armorStealth( Hero hero ){
		if (hero == null) return 0;
		float bonus = hero.pointsInTalent(Talent.BA_BASIC_NINJUTSU);
		if (hero.heroClass == HeroClass.BA_IZUNA) bonus += 2;
		return bonus;
	}

	public static void onLevelEnter(){
		if (Dungeon.hero != null && Dungeon.hero.hasTalent(Talent.BA_GAME_START_1)) {
			Buff.prolong(Dungeon.hero, Haste.class, 1+Dungeon.hero.pointsInTalent(Talent.BA_GAME_START_1));
		}
	}
}
