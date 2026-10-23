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

import static com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent.*;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Drowsy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Stamina;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.active.BABicycle;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Berry;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.BAAR;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.BAGun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL.BAGL;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.BAHG;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.BAMG;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.BASG;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.BASMG;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.BASR;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;

/** Blue Archive PD's talent trees, the buffs its talents use, and the talent hooks Shattered's Talent calls. */
public class BATalents {

	//tier talents of each student, from Blue Archive PD's Talent.initClassTalents
	public static void classTier( HeroClass cls, int tier, ArrayList<Talent> list ){
		if (!BAHeroes.isBA(cls)) return;
		list.clear(); //Shattered's switch falls to the Warrior's talents for classes it does not list
		switch (tier){
			case 1:
				switch (cls){
					case BA_ARIS: default: Collections.addAll(list, BA_FOR_LIGHT, BA_ROBOTS_INTUITION, BA_TEST_SUBJECT, BA_ACCEL_ENERGY); break;
					case BA_NONOMI:  Collections.addAll(list, BA_RESTING, BA_MG_INTUITION, BA_MAX_HEALTH, BA_NO_WAY); break;
					case BA_MIYAKO:  Collections.addAll(list, BA_PLATE_ADD, BA_SMG_INTUITION, BA_SUPPLY, BA_DISTURB); break;
					case BA_HOSHINO: Collections.addAll(list, BA_EMERGENCY_HEALING, BA_SG_INTUITION, BA_ADDITIONAL_SHOT, BA_RELOADING_SHIELD); break;
					case BA_SHIROKO: Collections.addAll(list, BA_BICYCLE_CHARGE, BA_AR_INTUITION, BA_ENHANCED_EXPLODE, BA_DEFENSIVE_FEEDBACK); break;
					case BA_NOA:     Collections.addAll(list, BA_SPEEDY_MOVE, BA_HG_INTUITION, BA_HEALING_FACTOR, BA_LAST_BULLET); break;
					case BA_MIYU:    Collections.addAll(list, BA_FAST_HAND, BA_SR_INTUITION, BA_CLAM_BREATHING, BA_NATURE_FRIENDLY); break;
					case BA_YUZU:    Collections.addAll(list, BA_CRIT_DAMAGE, BA_GL_INTUITION, BA_AMMO_BOX, BA_DEFENSIVE_MATRIX); break;
					case BA_IZUNA:   Collections.addAll(list, BA_BASIC_NINJUTSU, BA_ARMOR_INTUITION, BA_SURPRISE_ATTACK, BA_ENERGY_COLLECT); break;
				}
				break;
			case 2:
				switch (cls){
					case BA_ARIS: default: Collections.addAll(list, IRON_STOMACH, LIQUID_WILLPOWER, RUNIC_TRANSFERENCE, BA_EMPOWERING_MAGIC, BA_BALANCE_COLLAPSE); break;
					case BA_NONOMI:  Collections.addAll(list, BA_RELOADING_MEAL, BA_GOLD_CARD, BA_LARGE_MAGAZINE, BA_SURPRISE, BA_MG_MASTER); break;
					case BA_MIYAKO:  Collections.addAll(list, BA_TACTICAL_MEAL, BA_INTELLIGENCE, BA_CQC, BA_TACTICAL_MOVE, BA_SMG_MASTER); break;
					case BA_HOSHINO: Collections.addAll(list, BA_SLEEPING_MEAL, BA_QUICK_RETREAT, BA_FORESIGHT_EYES, BA_MAGIC_SHIELD, BA_SG_MASTER); break;
					case BA_SHIROKO: Collections.addAll(list, BA_SPEEDY_MEAL, BA_INHERENT_WILDNESS, BA_ELECTRIC_BICYCLE, BA_RAPID_SHOOTING, BA_AR_MASTER); break;
					case BA_NOA:     Collections.addAll(list, BA_HEALING_MEAL, BA_PATH_PREDICTION, BA_SKILLED_SEARCH, BA_DOUBLE_TAP, BA_HG_MASTER); break;
					case BA_MIYU:    Collections.addAll(list, BA_INVISIBLE_PRESENCE, BA_BLURRY_FIGURE, BA_HIDE_ON_BUSH, BA_BEST_SNIPER, BA_SR_MASTER); break;
					case BA_YUZU:    Collections.addAll(list, BA_DEBUGGING_MEAL, BA_SERIAL_COMBO, BA_QUEST_CLEAR, BA_QUALITY_ASSURANCE, BA_GL_MASTER); break;
					case BA_IZUNA:   Collections.addAll(list, BA_MEAL_SET, BA_HEALING_SCROLL, BA_QUIET_MOVE, BA_WIDER_SEARCH, BA_THROW_MASTER); break;
				}
				break;
			case 3:
				switch (cls){
					case BA_ARIS: default: Collections.addAll(list, BA_ROBOT_CLEANER, STRONGMAN); break;
					case BA_NONOMI:  Collections.addAll(list, BA_MG_FAST_RELOAD, ALLY_WARP); break;
					case BA_MIYAKO:  Collections.addAll(list, BA_SMG_FAST_RELOAD, BA_RABBIT_OPEN_UP); break;
					case BA_HOSHINO: Collections.addAll(list, BA_SG_FAST_RELOAD, BA_HARD_SHIELD); break;
					case BA_SHIROKO: Collections.addAll(list, BA_AR_FAST_RELOAD, BA_SHOOTING_WEAKNESS); break;
					case BA_NOA:     Collections.addAll(list, BA_HG_FAST_RELOAD, BA_HG_SWEEP); break;
					case BA_MIYU:    Collections.addAll(list, BA_SR_FAST_RELOAD, BA_SNIPERS_INTUITION); break;
					case BA_YUZU:    Collections.addAll(list, BA_GL_FAST_RELOAD, BA_ROCKET_JUMP); break;
					case BA_IZUNA:   Collections.addAll(list, BA_DURABLE_THROW, BA_SECRET_WEAPON); break;
				}
				break;
		}
	}

	public static void subclassTier( HeroSubClass sub, ArrayList<Talent> list ){
		if (sub == null || !sub.name().startsWith("BA_")) return;
		list.clear();
		switch (sub){
			case BA_ARIS_EX_SUPERNOVA: default:       Collections.addAll(list, BA_ENERGY_DRAIN, BA_PIEZOELECTRICITY, BA_HOLYSWORD); break;
			case BA_ARIS_EX_CHARGE:                   Collections.addAll(list, BA_INCREASING_OUTPUT, BA_OVERCHARGE, BA_CHARGE_ACCEL); break;
			case BA_NONOMI_EX_RIOT:                   Collections.addAll(list, BA_RIOT_1, BA_RIOT_2, BA_RIOT_3); break;
			case BA_NONOMI_EX_SHOOTALL:               Collections.addAll(list, BA_SHOOTALL_1, BA_SHOOTALL_2, BA_SHOOTALL_3); break;
			case BA_MIYAKO_EX_STUNDRONE:              Collections.addAll(list, BA_STUNDRONE_1, BA_STUNDRONE_2, BA_STUNDRONE_3); break;
			case BA_MIYAKO_EX_DRONESTRIKE:            Collections.addAll(list, BA_DRONESTRIKE_1, BA_DRONESTRIKE_2, BA_DRONESTRIKE_3); break;
			case BA_HOSHINO_EX_TACTICAL_PRESS:        Collections.addAll(list, BA_TACTICAL_PRESS_1, BA_TACTICAL_PRESS_2, BA_TACTICAL_PRESS_3); break;
			case BA_HOSHINO_EX_TACTICAL_SHIELD:       Collections.addAll(list, BA_TACTICAL_SHIELD_1, BA_TACTICAL_SHIELD_2, BA_TACTICAL_SHIELD_3); break;
			case BA_SHIROKO_EX_ELEMENTAL_BULLET:      Collections.addAll(list, BA_ELEMENTAL_BULLET_1, BA_ELEMENTAL_BULLET_2, BA_ELEMENTAL_BULLET_3); break;
			case BA_SHIROKO_EX_PROFESSIONAL_RIDE:     Collections.addAll(list, BA_PROFESSIONAL_RIDE_1, BA_PROFESSIONAL_RIDE_2, BA_PROFESSIONAL_RIDE_3); break;
			case BA_NOA_EX_LARGE_MAGAZINE:            Collections.addAll(list, BA_LARGE_MAGAZINE_1, BA_LARGE_MAGAZINE_2, BA_LARGE_MAGAZINE_3); break;
			case BA_NOA_EX_DOUBLE_BARREL:             Collections.addAll(list, BA_DOUBLE_BARREL_1, BA_DOUBLE_BARREL_2, BA_DOUBLE_BARREL_3); break;
			case BA_MIYU_EX_PENETRATION_SHOT:         Collections.addAll(list, BA_PENETRATION_SHOT_1, BA_PENETRATION_SHOT_2, BA_PENETRATION_SHOT_3); break;
			case BA_MIYU_EX_SNIPING_BULLET:           Collections.addAll(list, BA_SNIPING_BULLET_1, BA_SNIPING_BULLET_2, BA_SNIPING_BULLET_3); break;
			case BA_YUZU_EX_GAME_START:               Collections.addAll(list, BA_GAME_START_1, BA_GAME_START_2, BA_GAME_START_3); break;
			case BA_YUZU_EX_STICKY_GRENADE:           Collections.addAll(list, BA_STICKY_GRENADE_1, BA_STICKY_GRENADE_2, BA_STICKY_GRENADE_3); break;
			case BA_IZUNA_EX_TELEPORT:                Collections.addAll(list, BA_TELEPORT_1, BA_TELEPORT_2, BA_TELEPORT_3); break;
			case BA_IZUNA_EX_EXPLOSION:               Collections.addAll(list, BA_EXPLOSION_1, BA_EXPLOSION_2, BA_EXPLOSION_3); break;
		}
	}

	/** The heroic energy talent picture for a student (cell 26 of her row on the mod's sheet), or -1 for other heroes. */
	public static int heroicEnergyIcon( HeroClass cls ){
		if (!BAHeroes.isBA(cls)) return -1;
		return 30000 + 26 + 32 * (cls.ordinal() - HeroClass.BA_ARIS.ordinal());
	}

	//buffs Blue Archive PD kept inside Talent
	public static class NoWayCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0.15f, 0.2f, 0.5f); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 30); }
	};
	public static class EmergencyHealingCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0xFF9AB0); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 50); }
	};
	public static class TacticalInvisibilityTracker extends Buff{};
	public static class EmpoweringMagic extends FlavourBuff{
		public int icon() { return BuffIndicator.UPGRADE; }
		public void tintIcon(Image icon) { icon.hardlight(0.15f, 0.2f, 0.5f); }
		public float iconFadePercent() { return Math.max(0, (5 - visualcooldown()) / 5); }
	};
	public static class ScanCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight( 0xFFC4E5 ); }
		public float iconFadePercent() { return Math.max(0, 1f - (visualcooldown() / (60-10*Dungeon.hero.pointsInTalent(Talent.BA_DRONESTRIKE_2)))); }
	};
	public static class ShootingWeaknessCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight( 0xC7C4C9 ); }
		public float iconFadePercent() { return Math.max(0, 1f - (visualcooldown() / (40-10*Dungeon.hero.pointsInTalent(Talent.BA_SHOOTING_WEAKNESS)))); }
	};
	public static class BulletTimeCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0xD71394); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 30); }
	};
	public static class DoubleTapTracker extends Buff{};
	public static class FastHandTracker extends Buff{};
	public static class SuppressingFireCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0xFFC61A); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 20); }
	};
	public static class PerfectHandTracker extends Buff{
		int killed = 0;
		public void set(int killed) {
			this.killed = killed;
		}
		public int getKilled() {
			return this.killed;
		}
	};
	public static class ForLightTracker extends Buff{};
	public static class DefensiveMatrixTracker extends Buff{};

	//hooks, each called from one line in Shattered's Talent

	public static void onTalentUpgraded( Hero hero, Talent talent ){
		if (talent == BA_ROBOTS_INTUITION && hero.pointsInTalent(BA_ROBOTS_INTUITION) == 2){
			if (hero.belongings.armor() != null)  hero.belongings.armor.identify();
		}
		if (hero.pointsInTalent(talent) == 2 && hero.belongings.weapon() != null && gunIntuition(talent, hero.belongings.weapon())){
			hero.belongings.weapon.identify();
		}
		if (talent == BA_MAX_HEALTH) {
			hero.updateHT(true);
		}
		if (talent == BA_LARGE_MAGAZINE || talent == BA_HG_SWEEP || talent == BA_DOUBLE_BARREL_1) {
			Item.updateQuickslot();
		}
		if (talent == BA_INTELLIGENCE || talent == BA_SNIPERS_INTUITION || talent == BA_SNIPING_BULLET_2){
			Dungeon.observe();
		}
		if (talent == BA_HOLYSWORD && hero.pointsInTalent(talent) == 3) {
			Item.updateQuickslot();
		}
	}

	//the intuition talent that matches a gun type
	private static boolean gunIntuition( Talent talent, Item item ){
		return (talent == BA_MG_INTUITION && item instanceof BAMG)
				|| (talent == BA_SMG_INTUITION && item instanceof BASMG)
				|| (talent == BA_SG_INTUITION && item instanceof BASG)
				|| (talent == BA_AR_INTUITION && item instanceof BAAR)
				|| (talent == BA_HG_INTUITION && item instanceof BAHG)
				|| (talent == BA_SR_INTUITION && item instanceof BASR)
				|| (talent == BA_GL_INTUITION && item instanceof BAGL);
	}

	private static final Talent[] GUN_INTUITIONS = {BA_MG_INTUITION, BA_SMG_INTUITION, BA_SG_INTUITION, BA_AR_INTUITION,
			BA_HG_INTUITION, BA_SR_INTUITION, BA_GL_INTUITION};

	public static void onFoodEaten( Hero hero ){
		if (!BAHeroes.isBA(hero)) return;
		if (hero.hasTalent(BA_RESTING)){
			if (hero.HP <= hero.HT/2) {
				int healing = 1 + hero.pointsInTalent(BA_RESTING);
				if (hero.HP <= hero.HT/4) healing = 1 + 2 * hero.pointsInTalent(BA_RESTING);
				hero.HP = Math.min(hero.HP + healing, hero.HT);
				hero.sprite.showStatusWithIcon(com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite.POSITIVE, Integer.toString(healing), com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText.HEALING);
			}
		}
		if (hero.hasTalent(BA_RELOADING_MEAL) && hero.belongings.weapon instanceof BAGun) {
			((BAGun)hero.belongings.weapon).manualReload(hero.pointsInTalent(BA_RELOADING_MEAL), true);
		}
		if (hero.hasTalent(BA_TACTICAL_MEAL)) {
			Buff.affect(hero, Barrier.class).incShield(Math.round(hero.HT/10f*hero.pointsInTalent(BA_TACTICAL_MEAL)));
		}
		if (hero.hasTalent(BA_SLEEPING_MEAL)) {
			Buff.affect(hero, Drowsy.class).set(90-20*hero.pointsInTalent(BA_SLEEPING_MEAL));
		}
		if (hero.hasTalent(BA_SPEEDY_MEAL)) {
			Buff.affect(hero, Stamina.class, 1+7*hero.pointsInTalent(BA_SPEEDY_MEAL));
		}
		if (hero.hasTalent(BA_MEAL_SET)) {
			if (Random.Float() < 1/(float)(5-hero.pointsInTalent(BA_MEAL_SET))) {
				Dungeon.level.drop(new Berry(), hero.pos);
			}
		}
		if (hero.hasTalent(BA_BICYCLE_CHARGE)) {
			BABicycle bicycle = hero.belongings.getItem(BABicycle.class);
			if (bicycle != null) {
				bicycle.chargeUp(100*hero.pointsInTalent(BA_BICYCLE_CHARGE)/8f);
				ScrollOfRecharging.charge(hero);
			}
		}
		if (hero.hasTalent(BA_HEALING_MEAL)) {
			Buff.affect(hero, Healing.class).setHeal(1+7*hero.pointsInTalent(BA_HEALING_MEAL), 0, 1);
		}
		if (hero.hasTalent(BA_FAST_HAND)) {
			Buff.affect(hero, FastHandTracker.class);
		}
		if (hero.hasTalent(BA_INVISIBLE_PRESENCE)) {
			Buff.affect(hero, Invisibility.class, 1+2*hero.pointsInTalent(BA_INVISIBLE_PRESENCE));
		}
		if (hero.hasTalent(BA_DEBUGGING_MEAL)) {
			for (Buff b : hero.buffs()){
				if (b.type == Buff.buffType.NEGATIVE && !(b instanceof AllyBuff) && !(b instanceof LostInventory)){
					b.detach();
				}
			}
			if (hero.pointsInTalent(BA_DEBUGGING_MEAL) > 1) {
				Buff.affect(hero, PotionOfCleansing.Cleanse.class, 2);
			}
		}
	}

	/** True when a student's meal talent makes eating faster, as Shattered's meal talents do. */
	public static boolean fastMeal( Hero hero ){
		return BAHeroes.isBA(hero) && (hero.hasTalent(BA_RELOADING_MEAL) || hero.hasTalent(BA_TACTICAL_MEAL)
				|| hero.hasTalent(BA_SLEEPING_MEAL) || hero.hasTalent(BA_SPEEDY_MEAL) || hero.hasTalent(BA_HEALING_MEAL)
				|| hero.hasTalent(BA_INVISIBLE_PRESENCE) || hero.hasTalent(BA_DEBUGGING_MEAL) || hero.hasTalent(BA_MEAL_SET));
	}

	public static float itemIDSpeedFactor( Hero hero, Item item ){
		float factor = 1f;
		if (item instanceof MeleeWeapon || item instanceof Armor){
			factor *= 1f + 1.5f*hero.pointsInTalent(BA_ROBOTS_INTUITION);
		}
		return factor;
	}

	//Aris's test subject, the Warrior's old talent from Shattered 2.3
	public static void onItemIdentified( Hero hero ){
		if (hero.hasTalent(BA_TEST_SUBJECT)){
			int heal = 1 + hero.pointsInTalent(BA_TEST_SUBJECT);
			hero.HP = Math.min(hero.HP + heal, hero.HT);
			if (hero.sprite != null) {
				hero.sprite.showStatusWithIcon(com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite.POSITIVE, Integer.toString(heal), com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText.HEALING);
			}
		}
	}

	public static void onScrollUsed( Hero hero ){
		if (hero.hasTalent(BA_HEALING_SCROLL)) {
			Buff.affect(hero, Healing.class).setHeal(5*hero.pointsInTalent(BA_HEALING_SCROLL), 0, 1);
		}
	}

	public static void onItemEquipped( Hero hero, Item item ){
		if (hero.pointsInTalent(BA_ROBOTS_INTUITION) == 2 && (item instanceof Armor || item instanceof MeleeWeapon)){
			item.identify();
		}
		for (Talent t : GUN_INTUITIONS){
			if (hero.pointsInTalent(t) == 2 && gunIntuition(t, item)) item.identify();
		}
		if (hero.hasTalent(BA_ARMOR_INTUITION) && item instanceof Armor) {
			item.identify();
		}
	}

	public static void onItemCollected( Hero hero, Item item ){
		for (Talent t : GUN_INTUITIONS){
			if (hero.hasTalent(t) && gunIntuition(t, item)) item.cursedKnown = true;
		}
		if (hero.pointsInTalent(BA_ARMOR_INTUITION) == 2 && item instanceof Armor) {
			item.cursedKnown = true;
		}
	}

	public static int onAttackProc( Hero hero, Char enemy, int dmg ){
		if (!BAHeroes.isBA(hero)) return dmg;
		if (hero.hasTalent(BA_SURPRISE) && enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)
				&& enemy.buff(Talent.SuckerPunchTracker.class) == null){
			dmg *= 1 + (0.05f*hero.pointsInTalent(BA_SURPRISE));
		}
		if (hero.hasTalent(BA_DISTURB) && enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)){
			Buff.prolong(enemy, Vertigo.class, 1+hero.pointsInTalent(BA_DISTURB));
		}
		if (hero.hasTalent(BA_SURPRISE_ATTACK) && enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)
				&& enemy.buff(Talent.SuckerPunchTracker.class) == null){
			dmg += Random.IntRange(hero.pointsInTalent(BA_SURPRISE_ATTACK) , 2);
			Buff.affect(enemy, Talent.SuckerPunchTracker.class);
		}
		if (hero.hasTalent(BA_FOR_LIGHT) && enemy instanceof Mob && enemy.buff(ForLightTracker.class) == null){
			Buff.affect(enemy, Blindness.class, 1+hero.pointsInTalent(BA_FOR_LIGHT));
			Buff.affect(enemy, ForLightTracker.class);
		}
		if (hero.hasTalent(BA_DEFENSIVE_MATRIX) && enemy instanceof Mob && enemy.buff(DefensiveMatrixTracker.class) == null){
			Buff.affect(hero, Barrier.class).setShield(1+2*hero.pointsInTalent(BA_DEFENSIVE_MATRIX));
			Buff.affect(enemy, DefensiveMatrixTracker.class);
		}
		return dmg;
	}
}
