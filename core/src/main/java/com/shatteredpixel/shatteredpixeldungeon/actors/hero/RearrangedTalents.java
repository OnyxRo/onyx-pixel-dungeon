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

import static com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent.*;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArmorEnhance;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.GreaterHaste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HorseRiding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.InfiniteBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Pray;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RadioactiveMutation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SwordAura;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WeaponEnhance;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.adventurer.TreasureMap;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.gunner.ReinforcedArmor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.medic.HealingGenerator;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.samurai.ShadowBlade;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.building.WatchTower;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.effects.TalentSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.BulletItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.Sheath;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.Elixir;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.ExoticPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.pills.Pill;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfForce;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAggression;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.ShardOfOblivion;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Elastic;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blooming;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Lucky;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Bible;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.DualDagger;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.alchemy.UnholyBible;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.bow.Bow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.bow.BowWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.bow.GreatBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.bow.LongBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.bow.ShortBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.Gun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * Re-ARranged PD's talents for its six heroes. Talent calls each of these once from the matching hook, so the
 * talent code stays together here. Copied from the mod's Talent class, the parts for its rework of Shattered's
 * own heroes are left out (see PORTING.md).
 */
public class RearrangedTalents {

	public static void addClassTalents( HeroClass cls, int tier, ArrayList<Talent> t ){
		switch (tier){
			case 1:
				switch (cls){
					case GUNNER:     Collections.addAll(t, RELOADING_MEAL, GUNNERS_INTUITION, SPEEDY_MOVE, SAFE_RELOAD, CLOSE_COMBAT); break;
					case SAMURAI:    Collections.addAll(t, BASIC_PRACTICE, MASTERS_INTUITION, DRAWING_ENHANCE, PARRING, ADRENALINE_SURGE); break;
					case ADVENTURER: Collections.addAll(t, HARVEST_BERRY, SAFE_POTION, ROOT, PROTECTIVE_SLASH, KINETIC_ATTACK); break;
					case KNIGHT:     Collections.addAll(t, RA_TOUGH_MEAL, KNIGHTS_INTUITION, KINETIC_BATTLE, HARD_SHIELD, WAR_CRY); break;
					case MEDIC:      Collections.addAll(t, SCAR_ATTACK, DOCTORS_INTUITION, FINISH_ATTACK, FIRST_AID_TREAT, BREAKTHROUGH); break;
					case ARCHER:     Collections.addAll(t, FORCE_SAVING, ARCHERS_INTUITION, LEG_SWEEP, SURVIVAL_TECHNIQUE, DEXTERITY); break;
					default: break;
				}
				break;
			case 2:
				switch (cls){
					case GUNNER:     Collections.addAll(t, INFINITE_BULLET_MEAL, INSCRIBED_BULLET, MIND_VISION, CAMOUFLAGE, LARGER_MAGAZINE, BULLET_COLLECT); break;
					case SAMURAI:    Collections.addAll(t, CRITICAL_MEAL, INSCRIBED_LETHALITY, UNEXPECTED_SLASH, DRAGONS_EYE, WEAPON_MASTERY, CRITICAL_THROW); break;
					case ADVENTURER: Collections.addAll(t, NATURES_MEAL, PHARMACEUTICS, HERB_EXTRACTION, FIREWATCH, ROPE_REBOUND, WEAKENING_POISON); break;
					case KNIGHT:     Collections.addAll(t, IMPREGNABLE_MEAL, SMITHING_SPELL, ARMOR_ADAPTION, CHIVALRY, PROTECTION, FLAG_OF_CONQUEST); break;
					case MEDIC:      Collections.addAll(t, HEALING_MEAL, RECYCLING, HIGH_POWER, RADIATION, STRONG_HEALPOWER, DIET); break;
					case ARCHER:     Collections.addAll(t, FIGHTING_MEAL, FULLY_POTION, NATURE_FRIENDLY, PUSHBACK, ARCHERS_FORESIGHT, POWERFUL_CRIT); break;
					default: break;
				}
				break;
			case 3:
				switch (cls){
					case GUNNER:     Collections.addAll(t, STREET_BATTLE, RA_FAST_RELOAD); break;
					case SAMURAI:    Collections.addAll(t, QUICK_SHEATHING, LETHAL_POWER); break;
					case ADVENTURER: Collections.addAll(t, LONG_MACHETE, BLOOMING_WEAPON); break;
					case KNIGHT:     Collections.addAll(t, CRAFTMANS_SKILLS, TACKLE); break;
					case MEDIC:      Collections.addAll(t, STRONG_NEXUS, TARGET_SET); break;
					case ARCHER:     Collections.addAll(t, MAKESHIFT_BOW, FOLLOWUP_SHOOT); break;
					default: break;
				}
				break;
		}
	}

	public static void addSubclassTalents( HeroSubClass cls, ArrayList<Talent> t ){
		//every Re-ARranged subclass also gets four general talents
		Collections.addAll(t, ATK_SPEED_ENHANCE, ACC_ENHANCE, EVA_ENHANCE, BETTER_CHOICE);
		switch (cls){
			case OUTLAW:         Collections.addAll(t, ROLLING, PERFECT_FOCUSING, HONORABLE_SHOT, BULLET_TIME, INEVITABLE_DEATH, HEADSHOT); break;
			case GUNSLINGER:     Collections.addAll(t, QUICK_RELOAD, MOVING_SHOT, ELEMENTAL_BULLET, IMPROVISATION, SOUL_BULLET, LIGHT_MOVEMENT); break;
			case SPECIALIST:     Collections.addAll(t, STEALTH_MASTER, SKILLFUL_RUNNER, STEALTH, INTO_THE_SHADOW, RANGED_SNIPING, TELESCOPE); break;
			case SLASHER:        Collections.addAll(t, MIND_FOCUSING, STORED_POWER, ARCANE_POWER, ENERGY_COLLECT, ENERGY_SAVING, WIND_BLAST); break;
			case MASTER:         Collections.addAll(t, ENHANCED_CRIT, POWERFUL_SLASH, STATIC_PREPARATION, ACCELERATION, INNER_EYE, DYNAMIC_PREPARATION); break;
			case SLAYER:         Collections.addAll(t, AFTERIMAGE, FASTER_THAN_LIGHT, QUICK_RECOVER, HASTE_EVASION, ACCELERATED_LETHALITY, STABLE_BARRIER); break;
			case ENGINEER:       Collections.addAll(t, BARRICADE, WIRE, WATCHTOWER, CANNON, MACHINEGUN, MORTAR); break;
			case EXPLORER:       Collections.addAll(t, JUNGLE_EXPLORE, DURABLE_ROPE, LASSO, ROPE_COLLECTOR, ROPE_MASTER, VINE_BIND); break;
			case RESEARCHER:     Collections.addAll(t, BIOLOGY_PROJECT, RAPID_GROWTH, BIO_ENERGY, WATER_ABSORB, POWERFUL_ACID, STICKY_OOZE); break;
			case DEATHKNIGHT:    Collections.addAll(t, ARMY_OF_DEATH, DEATHS_CHILL, OVERCOME, RESENTMENT, UNDEAD, DEATHS_FEAR); break;
			case HORSEMAN:       Collections.addAll(t, SHOCKWAVE, ARMORED_HORSE, DASH_ENHANCE, BUFFER, PARKOUR, PILOTING); break;
			case CRUSADER:       Collections.addAll(t, HOLY_SHIELD, PRAY_FOR_DEAD, GODS_JUDGEMENT, CLEANSING_PRAY, PUNISHMENT, ANTI_DEMON); break;
			case SAVIOR:         Collections.addAll(t, RECRUIT, DELAYED_HEALING, APPEASE, RA_ADRENALINE, STIMPACK, MEDICAL_RAY); break;
			case THERAPIST:      Collections.addAll(t, OINTMENT, COMPRESS_BANDAGE, ANTIBIOTICS, QUICK_PREPARE, SPLINT, DEFIBRILLATOR); break;
			case MEDICALOFFICER: Collections.addAll(t, MOVE_CMD, STIMPACK_CMD, ENGINEER_CMD, PROMOTE_CMD, EXPLOSION_CMD, CAS_CMD); break;
			case BOWMASTER:      Collections.addAll(t, FASTSHOT, EXPANDED_POWER, MOVING_FOCUS, BOTTLE_EXPANSION, SPECTRE_ARROW, UNENCUMBERED_STEP); break;
			case JUGGLER:        Collections.addAll(t, HABITUAL_HAND, SWIFT_JUGGLING, SKILLFUL_JUGGLING, FOCUS_MAINTAIN, FANCY_PERFORMANCE, TOUR_PERFORMANCE); break;
			case SHARPSHOOTER:   Collections.addAll(t, HURRICANE, PERFECT_SHOT, FOCUS_SHOT, RA_BULLSEYE, RANGED_LETHALITY, CHANNELING); break;
			default: break;
		}
	}

	private static void give( Hero hero, Item item ){
		if (item.doPickUp( hero )) {
			GLog.i( Messages.get(hero, "you_now_have", item.name() ));
			hero.spend(-1);
		} else {
			Dungeon.level.drop( item, hero.pos ).sprite.drop();
		}
	}

	public static void onTalentUpgraded( Hero hero, Talent talent ){
		RearrangedRework.onTalentUpgraded(hero, talent);
		if (talent == BETTER_CHOICE){
			switch (hero.pointsInTalent(BETTER_CHOICE)) {
				case 1: give(hero, new StoneOfEnchantment()); break;
				case 2: give(hero, new ScrollOfEnchantment()); break;
				case 3: give(hero, new ScrollOfUpgrade()); break;
				default: break;
			}
		}
		if (talent == TELESCOPE || talent == DRAGONS_EYE){
			Dungeon.observe();
		}
		if ((talent == GUNNERS_INTUITION && hero.belongings.weapon instanceof Gun) && !ShardOfOblivion.passiveIDDisabled()){
			hero.belongings.weapon.identify();
		}
		if (talent == LARGER_MAGAZINE) {
			Item.updateQuickslot();
		}
		if (talent == MASTERS_INTUITION) {
			if (hero.belongings.weapon instanceof MeleeWeapon && !(hero.belongings.weapon instanceof Gun) && !ShardOfOblivion.passiveIDDisabled()) {
				hero.belongings.weapon.identify();
			}
		}
		if (talent == KNIGHTS_INTUITION) {
			if (hero.belongings.armor != null && !ShardOfOblivion.passiveIDDisabled()) {
				hero.belongings.armor.identify();
			}
		}
		if (talent == STORED_POWER) {
			BuffIndicator.refreshHero();
		}
		if (talent == WATCHTOWER && hero.pointsInTalent(WATCHTOWER) > 1) {
			for (Char ch : Actor.chars()) {
				if (ch instanceof WatchTower) {
					((WatchTower) ch).updateFOV();
				}
			}
		}
		if (talent == DOCTORS_INTUITION) {
			identifyPotions(1+2*hero.pointsInTalent(talent));
		}
		if (talent == MAKESHIFT_BOW && hero.heroClass == HeroClass.ARCHER) {
			BowWeapon bow = null;
			switch (hero.pointsInTalent(MAKESHIFT_BOW)) {
				case 1: bow = new Bow(); break;
				case 2: bow = new LongBow(); break;
				case 3: bow = new GreatBow(); break;
				default: break;
			}
			if (bow != null) {
				bow.identify();
				give(hero, bow);
			}
		}
		if (talent == ARCHERS_INTUITION && hero.heroClass == HeroClass.ARCHER && hero.pointsInTalent(ARCHERS_INTUITION) == 2) {
			BowWeapon bow = new ShortBow();
			bow.identify();
			give(hero, bow);
		}
	}

	public static void onFoodEaten( Hero hero, float foodVal, Item foodSource ){
		if (hero.hasTalent(RELOADING_MEAL)) {
			if (hero.belongings.weapon instanceof Gun) {
				((Gun)hero.belongings.weapon).quickReload();
				if (hero.pointsInTalent(RELOADING_MEAL) > 1) {
					((Gun)hero.belongings.weapon).manualReload(1, true);
				}
			}
		}
		if (hero.hasTalent(INFINITE_BULLET_MEAL)) {
			Buff.affect(hero, InfiniteBullet.class, 1+hero.pointsInTalent(INFINITE_BULLET_MEAL));
		}
		if (hero.hasTalent(CRITICAL_MEAL)) {
			Buff.affect(hero, Sheath.CertainCrit.class).set(hero.pointsInTalent(CRITICAL_MEAL));
		}
		if (hero.hasTalent(NATURES_MEAL)) {
			int[] around = hero.pointsInTalent(NATURES_MEAL) == 1 ? PathFinder.NEIGHBOURS4 : PathFinder.NEIGHBOURS8;
			for (int i : around) {
				int c = Dungeon.level.map[hero.pos + i];
				if (c == Terrain.EMPTY || c == Terrain.EMPTY_DECO
						|| c == Terrain.EMBERS || c == Terrain.GRASS) {
					Level.set(hero.pos + i, Terrain.HIGH_GRASS);
					GameScene.updateMap(hero.pos + i);
					CellEmitter.get(hero.pos + i).burst(LeafParticle.LEVEL_SPECIFIC, 4);
				}
			}
		}
		if (hero.hasTalent(RA_TOUGH_MEAL)) {
			Buff.affect(hero, ArmorEmpower.class).set(3, 1+hero.pointsInTalent(RA_TOUGH_MEAL));
		}
		if (hero.hasTalent(IMPREGNABLE_MEAL)) {
			Buff.affect(hero, ArmorEnhance.class).set(hero.pointsInTalent(IMPREGNABLE_MEAL), 3);
		}
		if (hero.hasTalent(HEALING_MEAL)) {
			if (hero.isHeroDebuffed()) {
				PotionOfCleansing.cleanse(hero);
			} else if (hero.pointsInTalent(HEALING_MEAL) > 1) {
				hero.heal(3);
			}
		}
		if (hero.hasTalent(FORCE_SAVING)) {
			Buff.affect(hero, ForceSavingTracker.class);
		}
		if (hero.hasTalent(FIGHTING_MEAL)) {
			Buff.affect(hero, Adrenaline.class, 1+hero.pointsInTalent(FIGHTING_MEAL));
		}
	}

	public static void onPotionUsed( Hero hero, int cell, float factor ){
		if (hero.hasTalent(PHARMACEUTICS)) {
			hero.heal(Math.round(factor*(2+3*hero.pointsInTalent(PHARMACEUTICS))));
		}
		if (hero.hasTalent(FULLY_POTION) && cell == hero.pos) {
			for (Buff b : hero.buffs()){
				if (b instanceof Hunger){
					((Hunger) b).satisfy(factor * (10+20*hero.pointsInTalent(FULLY_POTION)));
				}
			}
		}
	}

	//Recycling, a drunk potion can leave pills of the same kind behind. Called from Potion when a potion is used up
	public static void onPotionUsed( Hero hero, Potion potion ){
		if (hero.hasTalent(RECYCLING) && !(potion instanceof PotionOfStrength || potion instanceof ExoticPotion || potion instanceof Elixir)) {
			Class<?extends Pill> pillClass = Potion.PotionToPill.pills.get(potion.getClass());
			if (pillClass == null) return;
			Pill pill = Reflection.newInstance(pillClass);
			if (pill != null) {
				int amount = Random.IntRange(hero.pointsInTalent(RECYCLING)-1, hero.pointsInTalent(RECYCLING));
				if (amount <= 0) return;
				pill.quantity(amount);
				give(hero, pill);
			}
		}
	}

	public static void onScrollUsed( Hero hero, int pos, float factor ){
		RearrangedRework.onScrollUsed(hero, factor);
		if (hero.hasTalent(INSCRIBED_BULLET)) {
			BulletItem bulletItem = new BulletItem();
			bulletItem.quantity((int)(factor * 5 * hero.pointsInTalent(INSCRIBED_BULLET)));
			bulletItem.doPickUp(hero);
		}
		if (hero.hasTalent(INSCRIBED_LETHALITY)) {
			Buff.affect(hero, Sheath.CertainCrit.class).set((int)(factor * hero.pointsInTalent(INSCRIBED_LETHALITY)));
		}
		if (hero.hasTalent(SMITHING_SPELL)) {
			Buff.affect(hero, WeaponEnhance.class).set(hero.pointsInTalent(SMITHING_SPELL), Math.round(10*factor));
			Buff.affect(hero, ArmorEnhance.class).set(hero.pointsInTalent(SMITHING_SPELL), Math.round(10*factor));
		}
	}

	public static void onItemEquipped( Hero hero, Item item ){
		if (hero.hasTalent(GUNNERS_INTUITION) && item instanceof Gun) {
			item.identify();
		}
		if (hero.hasTalent(MASTERS_INTUITION) && item instanceof MeleeWeapon && !(item instanceof Gun)) {
			item.identify();
		}
		if (hero.hasTalent(KNIGHTS_INTUITION) && item instanceof Armor) {
			item.identify();
		}
		if (hero.hasTalent(ARCHERS_INTUITION) && item instanceof BowWeapon) {
			item.identify();
		}
	}

	public static void onItemCollected( Hero hero, Item item ){
		if (hero.pointsInTalent(GUNNERS_INTUITION) == 2 && item instanceof Gun) {
			item.cursedKnown = true;
		}
		if (hero.pointsInTalent(MASTERS_INTUITION) == 2 && item instanceof MeleeWeapon && !(item instanceof Gun)) {
			item.cursedKnown = true;
		}
		if (hero.pointsInTalent(KNIGHTS_INTUITION) == 2 && item instanceof Armor) {
			item.cursedKnown = true;
		}
		if (hero.pointsInTalent(ARCHERS_INTUITION) == 2 && item instanceof BowWeapon) {
			item.cursedKnown = true;
		}
	}

	public static int onDefenseProc( Hero hero, Char enemy, int damage ){
		if (hero.hasTalent(FIRST_AID_TREAT) && hero.buff(FirstAidTreatCooldown.class) == null && damage > (11-3*hero.pointsInTalent(FIRST_AID_TREAT))) {
			hero.heal(3);
			Buff.affect(hero, FirstAidTreatCooldown.class, 50f);
		}
		return damage;
	}

	public static void onLevelUp( Hero hero ){
		if (hero.hasTalent(FLAG_OF_CONQUEST)) {
			for (Char ch : Actor.chars()) {
				if (Dungeon.level.heroFOV[ch.pos] && ch.alignment == Char.Alignment.ENEMY && !ch.properties().contains(Char.Property.BOSS) && !ch.properties().contains(Char.Property.MINIBOSS)){
					if (ch.sprite != null) ch.sprite.emitter().burst(ShadowParticle.UP, 10);
					ch.damage(Math.round(0.25f*(1+hero.pointsInTalent(FLAG_OF_CONQUEST))*ch.HT), hero);
					GameScene.flash(0x30FFFF40);
					Sample.INSTANCE.play(Assets.Sounds.BLAST);
				}
			}
			TalentSprite.show(hero, FLAG_OF_CONQUEST);
		}
		if (hero.buff(HorseRiding.class) != null) {
			hero.buff(HorseRiding.class).onLevelUp();
		}
	}

	public static int onAttackProc( Hero hero, Char enemy, int damage ){
		damage = RearrangedRework.onAttackProc(hero, enemy, damage);
		KindOfWeapon wep;
		if (RingOfForce.fightingUnarmed(hero) && !RingOfForce.unarmedGetsWeaponEnchantment(hero)){
			wep = null;
		} else {
			wep = hero.belongings.attackingWeapon();
		}

		if (hero.hasTalent(FOLLOWUP_SHOOT) && enemy.alignment == Char.Alignment.ENEMY) {
			if (!(hero.belongings.attackingWeapon() instanceof MissileWeapon)) {
				Buff.prolong(hero, FollowupShootTracker.class, 5f).object = enemy.id();
			} else if (hero.buff(FollowupShootTracker.class) != null
					&& hero.buff(FollowupShootTracker.class).object == enemy.id()){
				damage = Math.round(damage * (1.0f + .1f*hero.pointsInTalent(FOLLOWUP_SHOOT)));
			}
		}

		if (hero.hasTalent(DRAWING_ENHANCE) && hero.buff(Sheath.Sheathing.class) != null) {
			damage += 1+hero.pointsInTalent(DRAWING_ENHANCE);
		}
		if (hero.heroClass != HeroClass.SAMURAI && hero.hasTalent(DRAWING_ENHANCE) && enemy.buff(DrawEnhanceMetaTracker.class) == null ) {
			damage += Hero.heroDamageIntRange(hero.pointsInTalent(DRAWING_ENHANCE), 2);
			Buff.affect(enemy, DrawEnhanceMetaTracker.class);
		}

		if (hero.buff(KineticBattle.class) != null) {
			damage = hero.buff(KineticBattle.class).proc(damage);
		}

		if (hero.hasTalent(TACKLE) && Dungeon.level.adjacent(enemy.pos, hero.pos) && hero.belongings.armor != null && (hero.belongings.attackingWeapon() instanceof MeleeWeapon || (hero.belongings.attackingWeapon() == null))) {
			damage += Math.round(hero.belongings.armor.DRMax()*0.05f*hero.pointsInTalent(TACKLE));
		}

		if (hero.hasTalent(SPEEDY_MOVE) && enemy instanceof Mob && enemy.buff(SpeedyMoveTracker.class) == null){
			Buff.affect(enemy, SpeedyMoveTracker.class);
			Buff.affect(hero, GreaterHaste.class).set(2 + hero.pointsInTalent(SPEEDY_MOVE));
		}

		if (hero.hasTalent(WAR_CRY) && enemy.buff(WarCryTracker.class) == null) {
			Buff.affect(enemy, WarCryTracker.class);
			Buff.prolong(hero, Adrenaline.class, hero.pointsInTalent(WAR_CRY));
		}

		if (hero.hasTalent(BLOOMING_WEAPON)
				&& Random.Float() < 0.05f*hero.pointsInTalent(BLOOMING_WEAPON)) {
			boolean secondPlant = Random.Float() <= 0.33f;
			ArrayList<Integer> positions = new ArrayList<>();
			Blooming blooming = new Blooming();
			for (int i : PathFinder.NEIGHBOURS8){
				positions.add(i);
			}
			Random.shuffle( positions );
			for (int i : positions){
				if (blooming.plantGrass(enemy.pos + i)) {
					if (secondPlant) secondPlant = false;
					else break;
				}
			}
		}

		if (hero.hasTalent(ANTI_DEMON) &&
				hero.buff(Bless.class) != null &&
				(Char.hasProp(enemy, Char.Property.DEMONIC) || Char.hasProp(enemy, Char.Property.UNDEAD))) {
			damage = Math.round(damage * 1f+hero.pointsInTalent(ANTI_DEMON)/3f);
		}

		if (hero.hasTalent(ARMY_OF_DEATH)) {
			float procChance = 0.1f * hero.pointsInTalent(ARMY_OF_DEATH);
			if (damage >= enemy.HP
					&& Random.Float() < procChance
					&& !enemy.isImmune(Corruption.class)
					&& enemy.buff(Corruption.class) == null
					&& enemy instanceof Mob
					&& enemy.isAlive()){
				Corruption.corruptionHeal(enemy);
				AllyBuff.affectAndLoot((Mob) enemy, hero, Corruption.class);
				damage = 0;
			}
		}

		if (hero.hasTalent(SCAR_ATTACK)) {
			int debuffs = enemy.buffs().size();
			if (debuffs > 0) {
				damage += debuffs * Random.NormalIntRange(1, hero.pointsInTalent(SCAR_ATTACK));
			}
		}

		if (hero.hasTalent(FINISH_ATTACK) && enemy.HP <= enemy.HT*0.25f) {
			damage += 1+hero.pointsInTalent(FINISH_ATTACK);
			Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
		}

		if (hero.hasTalent(STRONG_NEXUS)) {
			for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
				if (mob.alignment == Char.Alignment.ALLY && Dungeon.level.heroFOV[mob.pos]) {
					int healAmt = 3 * hero.pointsInTalent(STRONG_NEXUS) - Dungeon.level.distance(hero.pos, mob.pos) + 1;
					if (healAmt > 0) {
						mob.heal(healAmt);
					}
				}
			}
		}

		if (hero.hasTalent(TARGET_SET) && wep instanceof MissileWeapon) {
			int duration = hero.pointsInTalent(TARGET_SET);
			if (enemy.alignment != Char.Alignment.ENEMY) duration *= 5;
			Buff.prolong(enemy, StoneOfAggression.Aggression.class, duration);
		}

		if (hero.hasTalent(RADIATION) && hero.heroClass != HeroClass.MEDIC && enemy.buff(RadioactiveMutation.class) == null) {
			if (Random.Float() < 0.03f) {
				Buff.affect(enemy, RadioactiveMutation.class).set(6-hero.pointsInTalent(RADIATION));
			}
		}

		if (hero.buff(Sheath.DashAttackTracker.class) != null) {
			if (hero.hasTalent(ACCELERATION)) {
				Buff.prolong(hero, Sheath.DashAttackAcceleration.class, Sheath.DashAttackAcceleration.DURATION).hit();
				Sheath.DashAttackAcceleration buff = hero.buff(Sheath.DashAttackAcceleration.class);
				if (buff != null) {
					damage = Math.round(damage*buff.getDmgMulti());
				}
			}
			hero.buff(Sheath.DashAttackTracker.class).detach();
		}

		if (hero.buff(Bible.Angel.class) != null) {
			hero.heal(Math.max(Math.round(0.2f*damage), 1));
		}

		if (hero.buff(UnholyBible.Demon.class) != null) {
			damage = Math.round(damage * 1.33f);
		}

		if (hero.buff(DualDagger.ReverseBlade.class) != null) {
			damage = Math.round(damage * 0.5f);
			Buff.affect(enemy, Bleeding.class).set(Random.NormalIntRange(1, 3));
			if (enemy.sprite != null && enemy.sprite.visible) {
				Splash.at( enemy.sprite.center(), -PointF.PI / 2, PointF.PI / 6,
						enemy.sprite.blood(), Math.min( 10 * Random.NormalIntRange(1, 3) / enemy.HT, 10 ) );
			}
		}

		if (hero.buff(ShadowBlade.shadowBladeTracker.class) != null) {
			damage = Math.round(damage * 0.5f);
		}

		if (hero.hasTalent(BIOLOGY_PROJECT)) {
			if (!(enemy.properties().contains(Char.Property.INORGANIC) || enemy.properties().contains(Char.Property.UNDEAD))){
				if (enemy.sprite != null) enemy.sprite.emitter().start( ShadowParticle.UP, 0.05f, 3 );
				Sample.INSTANCE.play(Assets.Sounds.BURNING);
				damage = damage * (int)Math.pow(1.1f, hero.pointsInTalent(BIOLOGY_PROJECT));
			}
		}

		if (hero.belongings.attackingWeapon() instanceof MeleeWeapon && hero.heroClass != HeroClass.ADVENTURER && hero.hasTalent(LONG_MACHETE)) {
			int dist = Dungeon.level.distance(hero.pos, enemy.pos)-1;
			dist = Math.min(dist, hero.pointsInTalent(LONG_MACHETE));
			damage = (int)Math.round(damage * Math.pow(0.8f, dist));
		}

		if (hero.buff(TreasureMap.GoldTracker.class) != null) {
			damage = Math.round(1 + 0.1f * hero.pointsInTalent(GOLD_HUNTER));
			hero.buff(TreasureMap.GoldTracker.class).detach();
		}

		HealingGenerator.RegenBuff regenBuff = hero.buff(HealingGenerator.RegenBuff.class);
		if (regenBuff != null) {
			regenBuff.attackProc();
		}

		if (hero.hasTalent(BAYONET) && hero.buff(ReinforcedArmor.ReinforcedArmorTracker.class) != null){
			if (hero.belongings.attackingWeapon() instanceof Gun) {
				Buff.affect( enemy, Bleeding.class ).set( 4 + hero.pointsInTalent(BAYONET));
			}
		}

		if (hero.subClass == HeroSubClass.SLASHER) {
			Buff.affect(hero, SwordAura.class).hit(damage);
		}

		if (hero.subClass == HeroSubClass.RESEARCHER && Random.Float() < 0.2f) {
			Buff.affect(enemy, Ooze.class).set(Ooze.DURATION/4f * (1+0.5f*hero.pointsInTalent(POWERFUL_ACID)));
		}

		if (hero.buff(TreasureMap.LuckTracker.class) != null
				&& enemy.HP <= damage) {
			Buff.affect(enemy, Lucky.LuckProc.class);
		}

		if (hero.subClass == HeroSubClass.CRUSADER && hero.buff(Bless.class) != null) {
			int healAmt = Math.round(damage*0.4f);
			int excessHeal = healAmt - (hero.HT - hero.HP);
			hero.heal(healAmt);
			if (hero.hasTalent(HOLY_SHIELD) && excessHeal > 0) {
				int maxShield = Math.round(hero.HT*0.2f*hero.pointsInTalent(HOLY_SHIELD));
				Barrier barrier = hero.buff(Barrier.class);
				if (barrier != null) {
					if (barrier.shielding()+excessHeal > maxShield) {
						excessHeal = maxShield - barrier.shielding();
					}
					Buff.affect(hero, Barrier.class).setShield(excessHeal);
				} else {
					Buff.affect(hero, Barrier.class).setShield(Math.min(excessHeal, maxShield));
				}
			}
		}

		if (hero.buff(Pray.Punishment.class) != null) {
			hero.buff(Pray.Punishment.class).hit(enemy, damage);
		}

		if (wep instanceof Gun && hero.hasTalent(BULLET_COLLECT)) {
			if (Random.Float() < 0.05f * hero.pointsInTalent(BULLET_COLLECT)) {
				((Gun)wep).manualReload(1, true);
			}
		}

		if (hero.buff(WeaponEnhance.class) != null) {
			hero.buff(WeaponEnhance.class).attackProc();
		}

		if (hero.hasTalent(PROTECTIVE_SLASH)
				&& hero.buff(ProtectiveSlashCooldown.class) == null
				&& !Dungeon.level.adjacent(hero.pos, enemy.pos)) {
			Buff.affect(hero, Barrier.class).setShield(1+2*hero.pointsInTalent(PROTECTIVE_SLASH));
			Buff.affect(hero, ProtectiveSlashCooldown.class, 10);
		}

		if (hero.buff(KineticAttackTracker.class) != null) {
			damage += Random.IntRange(hero.pointsInTalent(KINETIC_ATTACK), 2);
			Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
			hero.buff(KineticAttackTracker.class).detach();
		}

		if (hero.buff(ForceSavingTracker.class) != null) {
			Elastic.pushEnemy(wep, hero, enemy, 1+hero.pointsInTalent(FORCE_SAVING));
			hero.buff(ForceSavingTracker.class).detach();
		}

		if (enemy instanceof Mob && enemy.buff(SurprisePanicTracker.class) == null && hero.hasTalent(LEG_SWEEP) && !enemy.flying) {
			if (((Mob)enemy).surprisedBy(hero)) {
				final int turns = 1+hero.pointsInTalent(LEG_SWEEP);
				new FlavourBuff() {
					{
						actPriority = VFX_PRIO;
					}

					public boolean act() {
						Buff.affect(target, Cripple.class, turns);
						return super.act();
					}
				}.attachTo(enemy);
			}
			Buff.affect(enemy, SurprisePanicTracker.class);
		}

		if (hero.hasTalent(PUSHBACK) && !(wep instanceof BowWeapon)) {
			if (Dungeon.level.adjacent(hero.pos, enemy.pos)
					&& ((enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)) || Random.Float() < 0.4f)) {
				Elastic.pushEnemy(wep, hero, enemy, hero.pointsInTalent(PUSHBACK));
			}
		}

		return damage;
	}

	//Doctor's Intuition, from the mod (like a Scroll of Divination)
	private static void identifyPotions(int amount) {
		HashSet<Class<? extends Potion>> potions = Potion.getUnknown();

		ArrayList<Item> IDed = new ArrayList<>();
		int left = amount;

		while (left > 0) {
			if (potions.isEmpty()) {
				break;
			}
			Potion p = Reflection.newInstance(Random.element(potions));
			p.identify();
			IDed.add(p);
			potions.remove(p.getClass());
			left --;
		}

		if (!IDed.isEmpty()) {
			GameScene.show(new WndIdentify(IDed, DOCTORS_INTUITION));
		}
	}

	public static class WndIdentify extends Window {

		private static final int WIDTH = 120;

		WndIdentify(ArrayList<Item> IDed, Talent talent ){
			IconTitle cur = new IconTitle(new TalentIcon(talent),
					Messages.titleCase(Messages.get(this, "name")));
			cur.setRect(0, 0, WIDTH, 0);
			add(cur);

			RenderedTextBlock msg = PixelScene.renderTextBlock(Messages.get(this, "desc"), 6);
			msg.maxWidth(120);
			msg.setPos(0, cur.bottom() + 2);
			add(msg);

			float pos = msg.bottom() + 10;

			for (Item i : IDed){
				cur = new IconTitle(i);
				cur.setRect(0, pos, WIDTH, 0);
				add(cur);
				pos = cur.bottom() + 2;
			}

			resize(WIDTH, (int)pos);
		}
	}
}
