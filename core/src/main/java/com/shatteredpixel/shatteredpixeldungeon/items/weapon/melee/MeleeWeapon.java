/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArtifactRecharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.GreaterHaste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MonkEnergy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.HolyWeapon;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfForce;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Crystal;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.Visual;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class MeleeWeapon extends Weapon {

	public static String AC_ABILITY = "ABILITY";
	public static String AC_SCRAP = "SCRAP"; //rearranged, the Gunner and the Archer take weapons apart

	@Override
	public void activate(Char ch) {
		super.activate(ch);
		if (ch instanceof Hero && ((Hero) ch).heroClass == HeroClass.DUELIST){
			Buff.affect(ch, Charger.class);
		}
	}

	@Override
	public String defaultAction() {
		if (Dungeon.hero != null && (Dungeon.hero.heroClass == HeroClass.DUELIST
			|| Dungeon.hero.hasTalent(Talent.SWIFT_EQUIP))
			&& !(this instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.Gun)
			&& !(this instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.bow.BowWeapon)){
			return AC_ABILITY;
		} else {
			return super.defaultAction();
		}
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (isEquipped(hero) && hero.heroClass == HeroClass.DUELIST){
			actions.add(AC_ABILITY);
		}
		//rearranged
		if (!isEquipped(hero) && (hero.heroClass == HeroClass.GUNNER || hero.heroClass == HeroClass.ARCHER)
				&& !(this instanceof com.shatteredpixel.shatteredpixeldungeon.items.quest.Pickaxe)) {
			actions.add(AC_SCRAP);
		}
		return actions;
	}

	@Override
	public String actionName(String action, Hero hero) {
		if (action.equals(AC_ABILITY)){
			return Messages.upperCase(Messages.get(this, "ability_name"));
		} else {
			return super.actionName(action, hero);
		}
	}

	@Override
	public void execute(Hero hero, String action) {
		super.execute(hero, action);

		//rearranged
		if (action.equals(AC_UNEQUIP) && hero.buff(com.shatteredpixel.shatteredpixeldungeon.items.Sheath.Sheathing.class) != null) {
			hero.buff(com.shatteredpixel.shatteredpixeldungeon.items.Sheath.Sheathing.class).detach();
		}
		if (action.equals(AC_SCRAP)) {
			RearrangedWeapons.scrap(this, hero);
		}

		if (action.equals(AC_ABILITY)){
			usesTargeting = false;
			if (!isEquipped(hero)) {
				if (hero.hasTalent(Talent.SWIFT_EQUIP)){
					if (hero.buff(Talent.SwiftEquipCooldown.class) == null
						|| hero.buff(Talent.SwiftEquipCooldown.class).hasSecondUse()){
						execute(hero, AC_EQUIP);
					} else if (hero.heroClass == HeroClass.DUELIST) {
						GLog.w(Messages.get(this, "ability_need_equip"));
					}
				} else if (hero.heroClass == HeroClass.DUELIST) {
					GLog.w(Messages.get(this, "ability_need_equip"));
				}
			} else if (hero.heroClass != HeroClass.DUELIST){
				//do nothing
			} else if (STRReq() > hero.STR()){
				GLog.w(Messages.get(this, "ability_low_str"));
			} else if ((Buff.affect(hero, Charger.class).charges + Buff.affect(hero, Charger.class).partialCharge) < abilityChargeUse(hero, null)
					&& !com.shatteredpixel.shatteredpixeldungeon.actors.hero.TransformationTalents.yinYangCovers(hero, Buff.affect(hero, Charger.class).charges + Buff.affect(hero, Charger.class).partialCharge, abilityChargeUse(hero, null))) { //transformation, Yin Yang Balance
				GLog.w(Messages.get(this, "ability_no_charge"));
			} else {

				if (targetingPrompt() == null){
					duelistAbility(hero, hero.pos);
					updateQuickslot();
				} else {
					usesTargeting = useTargeting();
					GameScene.selectCell(new CellSelector.Listener() {
						@Override
						public void onSelect(Integer cell) {
							if (cell != null) {
								duelistAbility(hero, cell);
								updateQuickslot();
							}
						}

						@Override
						public String prompt() {
							return targetingPrompt();
						}
					});
				}
			}
		}
	}

	//leave null for no targeting
	public String targetingPrompt(){
		return null;
	}

	public boolean useTargeting(){
		return targetingPrompt() != null;
	}

	@Override
	public int targetingPos(Hero user, int dst) {
		return dst; //weapon abilities do not use projectile logic, no autoaim
	}

	protected void duelistAbility( Hero hero, Integer target ){
		//do nothing by default
	}

	public static boolean canUseWeaponAbility( Hero hero ) {
		return hero != null
				&& (hero.heroClass == com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass.DUELIST
				|| hero.subClass == com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass.CHAMPION
				|| hero.hasMartialMastery()
				|| hero.hasTalent(com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent.MARTIAL_TRAIN));
	}

	public String abilityName() {
		return Messages.titleCase(Messages.get(this, "ability_name"));
	}

	//rearranged, Re-ARranged PD's weapons describe their abilities through these
	public String abilityDesc() {
		return Messages.get(this, "ability_desc");
	}

	//rearranged, its guns and bows change the tier shown and saved
	public int tier() {
		return this.tier;
	}

	public boolean canUseWeaponAbilityAction( Hero hero ){
		return hero != null;
	}

	//from Transformation PD, hooks its tier 6 weapons use. Hero calls them around attacks and steps
	public boolean hasTrait(WeaponTrait trait, Char owner) { return false; }
	public void beforeHeroAttack(Hero hero, Char target) {}
	public void afterHeroAttack(Hero hero, Char target, boolean hit) {}
	public void afterHeroAttackDelayResolved(Hero hero) {}
	public void onHeroStep(Hero hero, int from, int to) {}
	public boolean forcesSurpriseAttack(Hero hero, Char target) { return false; }

	//from Transformation PD, the weapon traits some of its weapons name
	public enum WeaponTrait {
		DAGGER,
		BLOCKING,
		ONE_SWORD_BLADE,
		FLAIL
	}

	//from Transformation PD, the item window lists what each upgrade adds to an ability
	public enum UpgradeAbilityStatType {
		LEGACY(null),
		DAMAGE("ability_damage"),
		AMBUSH_DAMAGE("ability_ambush_damage"),
		ATTACK_RANGE("feature_attack_range"),
		DURATION("ability_duration"),
		MIRROR_HEALTH("ability_mirror_health"),
		KNOCKBACK_DISTANCE("ability_knockback_distance"),
		EFFECT_RANGE("ability_effect_range"),
		BONUS_MAGIC_DAMAGE("ability_bonus_magic_damage"),
		EXTRA_ATTACK_RANGE("ability_extra_range"),
		COUNTER_DAMAGE("ability_counter_damage"),
		THROW_DAMAGE("feature_throw_damage"),
		BLADE_SHADOW_DAMAGE("feature_blade_shadow_damage"),
		PROC_CHANCE("feature_proc_chance"),
		DRAW_DAMAGE("feature_draw_damage"),
		DRAW_RANGE("feature_draw_range"),
		SOUL_TEAR_CHANCE("feature_soul_tear_chance"),
		RELEASE_DURATION("feature_release_duration");

		private final String messageKey;

		UpgradeAbilityStatType(String messageKey) {
			this.messageKey = messageKey;
		}

		public String messageKey() {
			return messageKey;
		}
	}

	public static class UpgradeAbilityStat {
		public final UpgradeAbilityStatType type;
		public final String value;

		public UpgradeAbilityStat(UpgradeAbilityStatType type, String value) {
			this.type = type;
			this.value = value;
		}
	}

	protected UpgradeAbilityStat abilityStat(UpgradeAbilityStatType type, String value) {
		return new UpgradeAbilityStat(type, value);
	}

	public ArrayList<UpgradeAbilityStat> upgradeAbilityStats(int level) {
		ArrayList<UpgradeAbilityStat> result = new ArrayList<>();
		String value = upgradeAbilityStat(level);
		if (value != null) {
			result.add(new UpgradeAbilityStat(UpgradeAbilityStatType.LEGACY, value));
		}
		return result;
	}

	public Integer upgradeBlockingStat(int level) {
		return null;
	}

	public ArrayList<UpgradeAbilityStat> upgradeFeatureStats(int level) {
		return new ArrayList<>();
	}

	public void beforeAbilityUsed(Hero hero, Char target){
		hero.belongings.abilityWeapon = this;
		com.shatteredpixel.shatteredpixeldungeon.actors.hero.TransformationTalents.onWeaponAbility(hero); //transformation, Skilled Parry
		Charger charger = Buff.affect(hero, Charger.class);

		charger.partialCharge -= abilityChargeUse(hero, target);
		while (charger.partialCharge < 0 && charger.charges > 0) {
			charger.charges--;
			charger.partialCharge++;
		}
		com.shatteredpixel.shatteredpixeldungeon.actors.hero.TransformationTalents.yinYangPay(hero, charger); //transformation, Yin Yang Balance

		if (hero.heroClass == HeroClass.DUELIST
				&& hero.hasTalent(Talent.AGGRESSIVE_BARRIER)
				&& (hero.HP / (float)hero.HT) <= 0.5f){
			int shieldAmt = 1 + 2*hero.pointsInTalent(Talent.AGGRESSIVE_BARRIER);
			Buff.affect(hero, Barrier.class).setShield(shieldAmt);
			hero.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(shieldAmt), FloatingText.SHIELDING);
		}

		if (hero.hasTalent(Talent.SKILLED_HAND)) Buff.affect(hero, Talent.SkilledHandTracker.class); //rearranged

		updateQuickslot();
	}

	public void afterAbilityUsed( Hero hero ){
		hero.belongings.abilityWeapon = null;
		if (hero.hasTalent(Talent.PRECISE_ASSAULT)){
			Buff.prolong(hero, Talent.PreciseAssaultTracker.class, hero.cooldown()+4f);
		}
		if (hero.hasTalent(Talent.VARIED_CHARGE)){
			Talent.VariedChargeTracker tracker = hero.buff(Talent.VariedChargeTracker.class);
			if (tracker == null || tracker.weapon == getClass() || tracker.weapon == null){
				Buff.affect(hero, Talent.VariedChargeTracker.class).weapon = getClass();
			} else {
				tracker.detach();
				Charger charger = Buff.affect(hero, Charger.class);
				charger.gainCharge(hero.pointsInTalent(Talent.VARIED_CHARGE) / 6f);
				ScrollOfRecharging.charge(hero);
			}
		}
		if (hero.hasTalent(Talent.COMBINED_LETHALITY)) {
			Talent.CombinedLethalityAbilityTracker tracker = hero.buff(Talent.CombinedLethalityAbilityTracker.class);
			if (tracker == null || tracker.weapon == this || tracker.weapon == null){
				Buff.affect(hero, Talent.CombinedLethalityAbilityTracker.class, hero.cooldown()).weapon = this;
			} else {
				//we triggered the talent, so remove the tracker
				tracker.detach();
			}
		}
		if (hero.hasTalent(Talent.COMBINED_ENERGY)){
			Talent.CombinedEnergyAbilityTracker tracker = hero.buff(Talent.CombinedEnergyAbilityTracker.class);
			if (tracker == null || !tracker.monkAbilused){
				Buff.prolong(hero, Talent.CombinedEnergyAbilityTracker.class, 5f).wepAbilUsed = true;
			} else {
				tracker.wepAbilUsed = true;
				Buff.affect(hero, MonkEnergy.class).processCombinedEnergy(tracker);
			}
		}
		if (hero.hasTalent(Talent.AGGRESSIVE_MOVEMENT)) { //rearranged, the Fencer's next dash costs half
			Buff.prolong(hero, Talent.AgressiveMovementAbilityTracker.class, hero.cooldown()).wepAbilUsed = true;
		}
		if (hero.buff(Talent.CounterAbilityTacker.class) != null){
			Charger charger = Buff.affect(hero, Charger.class);
			charger.gainCharge(hero.pointsInTalent(Talent.COUNTER_ABILITY)*0.375f);
			hero.buff(Talent.CounterAbilityTacker.class).detach();
		}
	}

	public static void onAbilityKill( Hero hero, Char killed ){
		if (killed.alignment == Char.Alignment.ENEMY && hero.hasTalent(Talent.LETHAL_HASTE)){
			//effectively 3/5 turns of greater haste
			Buff.affect(hero, GreaterHaste.class).set(2 + 2*hero.pointsInTalent(Talent.LETHAL_HASTE));
		}
	}

	protected int baseChargeUse(Hero hero, Char target){
		return 1; //abilities use 1 charge by default
	}

	public final float abilityChargeUse(Hero hero, Char target){
		return baseChargeUse(hero, target);
	}

	public int tier;

	//shim for Cursed PD, its weapons scale their top damage by this
	public float damageMultiplier = 1f;

	//shims for Tower PD weapons. Rarity is only a label, the damage modifier is 1, and a weapon with
	//a critical chance multiplies a hit by critMult now and then.
	public int rarity = 0;
	public float critChance = 0f;
	public float critMult = 1f;
	protected float damageModifier(){
		return 1f;
	}

	@Override
	public int proc(Char attacker, Char defender, int damage) {
		if (critChance > 0f && com.watabou.utils.Random.Float() < critChance) {
			damage = Math.round(damage * critMult);
		}
		return super.proc(attacker, defender, damage);
	}

	@Override
	public int min(int lvl) {
		//Shittened PD's prefix changes the low end
		return (int) Math.floor((tier + lvl) * (1f + prefix.min()));
	}

	@Override
	public int max(int lvl) {
		int base = 5*(tier+1) +    //base
				lvl*(tier+1);   //level scaling
		int result = damageMultiplier == 1f ? base : Math.max(1, Math.round(base*damageMultiplier));
		return trueMax(result);
	}

	//Shittened PD, every weapon's top damage goes through this so the prefix can change it
	public int trueMax(int baseDmg) {
		return (int) Math.ceil(baseDmg * (1f + prefix.max()));
	}

	public int STRReq(int lvl){
		int req = STRReq(tier, lvl);
		if (masteryPotionBonus){
			req -= 2;
		}
		return req;
	}

	private static boolean evaluatingTwinUpgrades = false;
	@Override
	public int buffedLvl() {
		if (!evaluatingTwinUpgrades && Dungeon.hero != null && isEquipped(Dungeon.hero) && Dungeon.hero.hasTalent(Talent.TWIN_UPGRADES)){
			KindOfWeapon other = null;
			if (Dungeon.hero.belongings.weapon() != this) other = Dungeon.hero.belongings.weapon();
			if (Dungeon.hero.belongings.secondWep() != this) other = Dungeon.hero.belongings.secondWep();

			if (other instanceof MeleeWeapon) {
				evaluatingTwinUpgrades = true;
				int otherLevel = other.buffedLvl();
				evaluatingTwinUpgrades = false;

				//weaker weapon needs to be 2/1/0 tiers lower, based on talent level
				if ((tier + (3 - Dungeon.hero.pointsInTalent(Talent.TWIN_UPGRADES))) <= ((MeleeWeapon) other).tier
						&& otherLevel > super.buffedLvl()) {
					return otherLevel;
				}

			}
		}
		return super.buffedLvl();
	}

	@Override
	public int damageRoll(Char owner) {
		int damage = augment.damageFactor(super.damageRoll( owner ));
		if (prefix == com.shatteredpixel.shatteredpixeldungeon.mechanics.Prefix.MUSICAL) {
			com.watabou.noosa.audio.Sample.INSTANCE.play(Assets.Sounds.NOTE, 1.4f, Math.round(com.watabou.utils.Random.Float(0.6f,1.4f) * 10) / 10f);
		}

		if (owner instanceof Hero) {
			int exStr = ((Hero)owner).STR() - STRReq();
			if (exStr > 0) {
				damage += Hero.heroDamageIntRange( 0, exStr );
			}
		}
		return damage;
	}
	
	@Override
	public String info() {

		String info = super.info();

		if (levelKnown) {
			info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_known", tier(), augment.damageFactor(min()), augment.damageFactor(max()), STRReq());
			if (Dungeon.hero != null) {
				if (STRReq() > Dungeon.hero.STR()) {
					info += " " + Messages.get(Weapon.class, "too_heavy");
				} else if (Dungeon.hero.STR() > STRReq()) {
					info += " " + Messages.get(Weapon.class, "excess_str", Dungeon.hero.STR() - STRReq());
				}
			}
		} else {
			info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_unknown", tier(), min(0), max(0), STRReq(0));
			if (Dungeon.hero != null && STRReq(0) > Dungeon.hero.STR()) {
				info += " " + Messages.get(MeleeWeapon.class, "probably_too_heavy");
			}
		}

		String statsInfo = statsInfo();
		if (!statsInfo.equals("")) info += "\n\n" + statsInfo;

		switch (augment) {
			case SPEED:
				info += " " + Messages.get(Weapon.class, "faster");
				break;
			case DAMAGE:
				info += " " + Messages.get(Weapon.class, "stronger");
				break;
			case NONE:
		}

		if (isEquipped(Dungeon.hero) && !hasCurseEnchant() && Dungeon.hero.buff(HolyWeapon.HolyWepBuff.class) != null
				&& (Dungeon.hero.subClass != HeroSubClass.PALADIN || enchantment == null)){
			info += "\n\n" + Messages.capitalize(Messages.get(Weapon.class, "enchanted", Messages.get(HolyWeapon.class, "ench_name", Messages.get(Enchantment.class, "enchant"))));
			info += " " + Messages.get(HolyWeapon.class, "ench_desc");
		} else if (enchantment != null && (cursedKnown || !enchantment.curse())){
			info += "\n\n" + Messages.capitalize(Messages.get(Weapon.class, "enchanted", enchantment.name()));
			if (enchantHardened) info += " " + Messages.get(Weapon.class, "enchant_hardened");
			info += " " + enchantment.desc();
		} else if (enchantHardened){
			info += "\n\n" + Messages.get(Weapon.class, "hardened_no_enchant");
		}

		if (cursed && isEquipped( Dungeon.hero )) {
			info += "\n\n" + Messages.get(Weapon.class, "cursed_worn");
		} else if (cursedKnown && cursed) {
			info += "\n\n" + Messages.get(Weapon.class, "cursed");
		} else if (!isIdentified() && cursedKnown){
			if (enchantment != null && enchantment.curse()) {
				info += "\n\n" + Messages.get(Weapon.class, "weak_cursed");
			} else {
				info += "\n\n" + Messages.get(Weapon.class, "not_cursed");
			}
		}

		//the mage's staff has no ability as it can only be gained by the mage
		if (Dungeon.hero != null && Dungeon.hero.heroClass == HeroClass.DUELIST && !(this instanceof MagesStaff)){
			info += "\n\n" + abilityInfo();
		}

		//rearranged, critical hit chance for the Samurai and the Archer
		if (Dungeon.hero != null && isEquipped(Dungeon.hero) && Dungeon.hero.critChance(this) > 0) {
			info += "\n\n" + Messages.get(Weapon.class, "critchance", Messages.decimalFormat("#.##", 100*Dungeon.hero.critChance(this)));
		}
		
		if (modify != null) info += "\n\n" + Messages.get(KindOfWeapon.class, "has_modify", modify.title(), modDurability) + modify.desc(); //xun
		return info;
	}
	
	public String statsInfo(){
		return Messages.get(this, "stats_desc");
	}

	public String abilityInfo() {
		return Messages.get(this, "ability_desc");
	}

	public String upgradeAbilityStat(int level){
		return null;
	}

	@Override
	public String status() {
		if (isEquipped(Dungeon.hero)
				&& Dungeon.hero.buff(Charger.class) != null) {
			Charger buff = Dungeon.hero.buff(Charger.class);
			return buff.charges + "/" + buff.chargeCap();
		} else {
			return super.status();
		}
	}

	@Override
	public int value() {
		int price = 20 * tier;
		if (hasGoodEnchant()) {
			price *= 1.5f;
			if (enchantment instanceof Crystal){
				price *= 3;
			}
		}
		if (cursedKnown && (cursed || hasCurseEnchant())) {
			price /= 2;
		}
		if (levelKnown && level() > 0) {
			price *= (level() + 1);
		}
		if (price < 1) {
			price = 1;
		}
		return price;
	}

	public static class Charger extends Buff implements ActionIndicator.Action {

		{
			//so that duelist keeps weapon charge on ankh revive
			revivePersists = true;
		}

		public int charges = 2;
		public float partialCharge;

		@Override
		public boolean act() {
			if (charges < chargeCap()){
				if (Regeneration.regenOn()){
					//60 to 45 turns per charge
					float chargeToGain = 1/(60f-1.5f*(chargeCap()-charges));

					//40 to 30 turns per charge for champion
					if (Dungeon.hero.subClass == HeroSubClass.CHAMPION){
						chargeToGain *= com.shatteredpixel.shatteredpixeldungeon.actors.hero.TransformationTalents.championRechargeBonus(Dungeon.hero); //transformation, Weapon Ability Master
					}

					chargeToGain *= FencerDash.chargeMulti(Dungeon.hero); //rearranged

					//50% slower charge gain with brawler's stance enabled, even if buff is inactive
					if (Dungeon.hero.buff(RingOfForce.BrawlersStance.class) != null){
						chargeToGain *= 0.50f;
					}

					partialCharge += chargeToGain;
				}

				int points = ((Hero)target).pointsInTalent(Talent.WEAPON_RECHARGING);
				if (points > 0 && target.buff(Recharging.class) != null || target.buff(ArtifactRecharge.class) != null){
					//1 every 15 turns at +1, 10 turns at +2
					partialCharge += 1/(20f - 5f*points);
				}

				if (partialCharge >= 1){
					charges++;
					partialCharge--;
					updateQuickslot();
				}
			} else {
				partialCharge = 0;
			}

			if (ActionIndicator.action != this && FencerDash.showsAction()) { //rearranged, the Fencer too
				ActionIndicator.setAction(this);
			}

			spend(TICK);
			return true;
		}

		//rearranged, the Fencer's dash
		public void useCharges(float amount) {
			partialCharge -= amount;
			while (partialCharge < 0 && charges > 0) {
				charges--;
				partialCharge++;
			}
		}

		@Override
		public void fx(boolean on) {
			if (on && FencerDash.showsAction()) { //rearranged
				ActionIndicator.setAction(this);
			}
		}

		@Override
		public void detach() {
			super.detach();
			ActionIndicator.clearAction(this);
		}

		public int chargeCap(){
			//Transformation PD's Martial Train, a small pool for heroes that only have the talent
			if (Dungeon.hero.heroClass != com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass.DUELIST
					&& Dungeon.hero.subClass != HeroSubClass.CHAMPION && !Dungeon.hero.hasMartialMastery()
					&& Dungeon.hero.hasTalent(com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent.MARTIAL_TRAIN)){
				return 1 + Dungeon.hero.pointsInTalent(com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent.MARTIAL_TRAIN);
			}
			//caps at level 19 with 8 or 10 charges
			if (Dungeon.hero.subClass == HeroSubClass.CHAMPION){
				return Math.min(10, 4 + (Dungeon.hero.lvl - 1) / 3) + com.shatteredpixel.shatteredpixeldungeon.actors.hero.TransformationTalents.championChargeBonus(Dungeon.hero)
						+ Dungeon.hero.pointsInTalent(Talent.ACCUMULATION); //rearranged
			} else {
				return Math.min(8, 2 + (Dungeon.hero.lvl - 1) / 3) + Dungeon.hero.pointsInTalent(Talent.ACCUMULATION); //rearranged
			}
		}

		public void gainCharge( float charge ){
			if (charges < chargeCap()) {
				partialCharge += charge;
				while (partialCharge >= 1f) {
					charges++;
					partialCharge--;
				}
				if (charges >= chargeCap()){
					partialCharge = 0;
					charges = chargeCap();
				}
				updateQuickslot();
			}
		}

		public static final String CHARGES          = "charges";
		private static final String PARTIALCHARGE   = "partialCharge";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(CHARGES, charges);
			bundle.put(PARTIALCHARGE, partialCharge);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			charges = bundle.getInt(CHARGES);
			partialCharge = bundle.getFloat(PARTIALCHARGE);
		}

		@Override
		public String actionName() {
			if (FencerDash.isFencer()) return Messages.get(MeleeWeapon.class, "dash"); //rearranged
			return Messages.get(MeleeWeapon.class, "swap");
		}

		@Override
		public int actionIcon() {
			if (FencerDash.isFencer()) return 0; //rearranged
			return HeroIcon.WEAPON_SWAP;
		}

		@Override
		public Visual primaryVisual() {
			Image ico;
			if (FencerDash.isFencer()){
				ico = FencerDash.icon(); //rearranged
			} else if (Dungeon.hero.belongings.weapon == null){
				ico = new HeroIcon(this);
 			} else {
				ico = new ItemSprite(Dungeon.hero.belongings.weapon);
			}
			ico.width += 4; //shift slightly to the left to separate from smaller icon
			return ico;
		}

		@Override
		public Visual secondaryVisual() {
			Image ico;
			if (Dungeon.hero.belongings.secondWep == null){
				ico = new HeroIcon(this);
			} else {
				ico = new ItemSprite(Dungeon.hero.belongings.secondWep);
			}
			ico.scale.set(PixelScene.align(0.51f));
			ico.brightness(0.6f);
			return ico;
		}

		@Override
		public int indicatorColor() {
			return 0x5500BB;
		}

		@Override
		public void doAction() {
			if (FencerDash.isFencer()) { FencerDash.doAction(this); return; } //rearranged
			if (Dungeon.hero.subClass != HeroSubClass.CHAMPION){
				return;
			}

			if (Dungeon.hero.belongings.secondWep == null && Dungeon.hero.belongings.backpack.items.size() >= Dungeon.hero.belongings.backpack.capacity()){
				GLog.w(Messages.get(MeleeWeapon.class, "swap_full"));
				return;
			}

			KindOfWeapon temp = Dungeon.hero.belongings.weapon;
			Dungeon.hero.belongings.weapon = Dungeon.hero.belongings.secondWep;
			Dungeon.hero.belongings.secondWep = temp;

			Dungeon.hero.sprite.operate(Dungeon.hero.pos);
			Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
			FencerDash.onSwap(Dungeon.hero); //rearranged, Quick Followup

			ActionIndicator.setAction(this);
			Item.updateQuickslot();
			AttackIndicator.updateState();
		}
	}

}
