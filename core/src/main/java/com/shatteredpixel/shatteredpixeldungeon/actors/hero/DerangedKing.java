/*
 * Onyx's Pixel Dungeon
 * DERanged PD's Rat King hero (2026-10-22). Ported from DERanged PD by its authors, built on Re-ARranged PD,
 * whose Rat King itself comes from Rat King PD 2 by zrp200. GPL-3.0.
 *
 * The Rat King counts as every class of Shattered and Re-ARranged (HeroClass.is), and as every subclass whose
 * RK_ talent he has put a point in (HeroSubClass.is). His own talents each stand for a group of other heroes'
 * talents, the BORROWED table below says which, and Hero.pointsInTalent asks it for any talent he does not have.
 */
package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Preparation;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.Ratmogrify;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ratking.KingsWrath;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ratking.OmniAbility;
import com.shatteredpixel.shatteredpixeldungeon.items.ArrowBag;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.BulletBelt;
import com.shatteredpixel.shatteredpixeldungeon.items.GammaRayGun;
import com.shatteredpixel.shatteredpixeldungeon.items.KnightsShield;
import com.shatteredpixel.shatteredpixeldungeon.items.Sheath;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.MedicKit;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.CheesyCheest;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfMagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Machete;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Shovel;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.bow.WornShortBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.AR_T1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;

public class DerangedKing {

	//the classes the Rat King counts as, Shattered's six and Re-ARranged's six
	private static final EnumSet<HeroClass> SHARED = EnumSet.of(HeroClass.WARRIOR, HeroClass.MAGE, HeroClass.ROGUE,
			HeroClass.HUNTRESS, HeroClass.DUELIST, HeroClass.CLERIC, HeroClass.GUNNER, HeroClass.SAMURAI,
			HeroClass.ADVENTURER, HeroClass.KNIGHT, HeroClass.MEDIC, HeroClass.ARCHER);

	public static boolean sharesClass( HeroClass cls ){
		return SHARED.contains(cls);
	}

	private static EnumMap<HeroSubClass, Talent> subTalents;

	private static EnumMap<HeroSubClass, Talent> subTalents(){
		if (subTalents == null){
			EnumMap<HeroSubClass, Talent> m = new EnumMap<>(HeroSubClass.class);
			for (HeroSubClass s : HeroSubClass.values()){
				try {
					m.put(s, Talent.valueOf("RK_" + s.name()));
				} catch (IllegalArgumentException e){
					//not a subclass the King can take on
				}
			}
			subTalents = m;
		}
		return subTalents;
	}

	//the King takes on a subclass once he has put a point in its RK_ talent
	public static boolean sharesSubclass( HeroSubClass sub ){
		Hero hero = Dungeon.hero;
		if (hero == null || hero.subClass != HeroSubClass.KING) return false;
		Talent t = subTalents().get(sub);
		return t != null && hero.pointsInTalent(t) > 0;
	}

	public static ArmorAbility[] armorAbilities(){
		return new ArmorAbility[]{new KingsWrath(), new Ratmogrify(), new OmniAbility()};
	}

	public static void addClassTalents( int tier, ArrayList<Talent> list ){
		if (tier == 1){
			Collections.addAll(list, Talent.RK_ROYAL_PRIVILEGE, Talent.RK_ROYAL_INTUITION, Talent.RK_KINGS_WISDOM,
					Talent.RK_NOBLE_CAUSE, Talent.RK_EXTRA_BULK, Talent.RK_ROYAL_FOCUS, Talent.RK_ROYAL_PERCEPTIVITY,
					Talent.RK_AMBUSH, Talent.RK_WELL_PROTECTED, Talent.RK_EXTRA_POWER);
		} else if (tier == 2){
			Collections.addAll(list, Talent.RK_ROYAL_MEAL, Talent.RK_RESTORATION, Talent.RK_POWER_WITHIN,
					Talent.RK_KINGS_VISION, Talent.RK_PURSUIT, Talent.RK_ENERGY_SURGE, Talent.RK_ROYAL_FEAST,
					Talent.RK_TEMPORARY_DRAUGHT, Talent.RK_PERFECT_COLLECTION, Talent.RK_KINGS_WRATH,
					Talent.RK_THE_PROTECTOR, Talent.RK_NOBLE_CALL);
		}
	}

	public static void addSubclassTalents( ArrayList<Talent> list ){
		Collections.addAll(list, Talent.RK_BERSERKER, Talent.RK_GLADIATOR, Talent.RK_VETERAN, Talent.RK_BATTLEMAGE,
				Talent.RK_WARLOCK, Talent.RK_WIZARD, Talent.RK_ASSASSIN, Talent.RK_FREERUNNER, Talent.RK_CHASER,
				Talent.RK_SNIPER, Talent.RK_WARDEN, Talent.RK_FIGHTER, Talent.RK_CHAMPION, Talent.RK_MONK,
				Talent.RK_FENCER, Talent.RK_PRIEST, Talent.RK_PALADIN, Talent.RK_ENCHANTER, Talent.RK_OUTLAW,
				Talent.RK_GUNSLINGER, Talent.RK_SPECIALIST, Talent.RK_SLASHER, Talent.RK_MASTER, Talent.RK_SLAYER,
				Talent.RK_ENGINEER, Talent.RK_EXPLORER, Talent.RK_RESEARCHER, Talent.RK_DEATHKNIGHT,
				Talent.RK_HORSEMAN, Talent.RK_CRUSADER, Talent.RK_SAVIOR, Talent.RK_THERAPIST,
				Talent.RK_MEDICALOFFICER, Talent.RK_BOWMASTER, Talent.RK_JUGGLER, Talent.RK_SHARPSHOOTER);
	}

	//the talents a class has in a tier (1 to 3), used by the Kromer
	public static Talent[] talentList( HeroClass cls, int tier ){
		ArrayList<java.util.LinkedHashMap<Talent, Integer>> lists = new ArrayList<>();
		Talent.initClassTalents(cls, lists);
		if (tier < 1 || tier > lists.size()) return new Talent[0];
		return lists.get(tier - 1).keySet().toArray(new Talent[0]);
	}

	//the King earns half again as many talent points in each tier, as in the mod
	public static int talentPointsAvailable( Hero hero, int tier ){
		if (hero.lvl < (Talent.tierLevelThresholds[tier] - 1)
				|| (tier == 3 && hero.subClass == HeroSubClass.NONE)
				|| (tier == 4 && hero.armorAbility == null)) {
			return 0;
		}
		int earned;
		if (hero.lvl >= Talent.tierLevelThresholds[tier+1]){
			earned = Talent.tierLevelThresholds[tier+1] - Talent.tierLevelThresholds[tier];
		} else {
			earned = 1 + hero.lvl - Talent.tierLevelThresholds[tier];
		}
		return (int)(earned * 1.5f) - hero.talentPointsSpent(tier) + hero.bonusTalentPoints(tier);
	}

	//other heroes' talents the King's talents stand for, from every two talent check in DERanged PD
	private static final String[] BORROWED = {
		"ACCELERATED_LETHALITY:RK_SLAYER", "ACCELERATION:RK_MASTER", "ACCUMULATION:RK_ENERGY_SURGE", "ADDED_MEAL:RK_ENERGY_SURGE",
		"RA_ADRENALINE:RK_SAVIOR", "ADVENTURERS_INTUITION:RK_ROYAL_INTUITION", "AFTERIMAGE:RK_SLAYER",
		"AGGRESSIVE_BARRIER:RK_NOBLE_CAUSE", "ALLY_WARP:RK_WARLOCK", "ANTIBIOTICS:RK_THERAPIST", "ANTI_DEMON:RK_CRUSADER",
		"APPEASE:RK_SAVIOR", "ARCANE_ARMOR:RK_ENCHANTER", "ARCANE_POWER:RK_SLASHER", "ARCANE_VISION:RK_KINGS_VISION",
		"ARCHERS_FORESIGHT:RK_KINGS_VISION", "ARMORED_HORSE:RK_HORSEMAN", "ARMOR_ADAPTION:RK_KINGS_WRATH",
		"ARMY_OF_DEATH:RK_DEATHKNIGHT", "ASSASSINS_REACH:RK_ASSASSIN", "ATTRACTION:RK_WARDEN", "AURA_OF_PROTECTION:RK_PALADIN",
		"BACKUP_BARRIER:RK_NOBLE_CAUSE", "BARKSKIN:RK_WARDEN", "BARRICADE:RK_ENGINEER", "BASIC_PRACTICE:RK_ROYAL_FOCUS",
		"BATTLE_MAGIC:RK_BATTLEMAGE", "BIOLOGY_PROJECT:RK_RESEARCHER", "BIO_ENERGY:RK_RESEARCHER", "BLESS:RK_POWER_WITHIN",
		"BLOOMING_WEAPON:RK_ENGINEER", "BOTTLE_EXPANSION:RK_BOWMASTER", "BOUNTY_HUNTER:RK_ASSASSIN", "BREAKTHROUGH:RK_EXTRA_BULK",
		"BRIG_BOOST:RK_WIZARD", "BUFFER:RK_HORSEMAN", "BULLET_COLLECT:RK_PERFECT_COLLECTION", "BULLET_TIME:RK_OUTLAW",
		"RA_BULLSEYE:RK_SHARPSHOOTER", "CACHED_RATIONS:RK_ROYAL_PRIVILEGE", "CAMOUFLAGE:RK_KINGS_WRATH", "CANNON:RK_ENGINEER",
		"CAS_CMD:RK_MEDICALOFFICER", "CAUTIOUS_PREP:RK_ASSASSIN", "CHANNELING:RK_SHARPSHOOTER", "CHARGE_PRESERVE:RK_EXTRA_BULK",
		"CHIVALRY:RK_THE_PROTECTOR", "CLAM_STEPS:RK_FENCER", "CLEANSE:RK_PRIEST", "CLEANSING_PRAY:RK_CRUSADER", "CLEAVE:RK_GLADIATOR",
		"CLOSE_COMBAT:RK_EXTRA_POWER", "COMBINED_ENERGY:RK_MONK", "COMBINED_LETHALITY:RK_CHAMPION", "COMPRESS_BANDAGE:RK_THERAPIST",
		"RA_COUNTER_ATTACK:RK_FIGHTER", "CRAFTMANS_SKILLS:RK_CRUSADER", "CRITICAL_MEAL:RK_ROYAL_FEAST", "CRITICAL_THROW:RK_NOBLE_CALL",
		"DASH_ENHANCE:RK_HORSEMAN", "DEADLY_FOLLOWUP:RK_MONK", "DEATHS_CHILL:RK_DEATHKNIGHT", "DEATHS_FEAR:RK_DEATHKNIGHT",
		"DEFIBRILLATOR:RK_THERAPIST", "DELAYED_GRENADE:RK_VETERAN", "DELAYED_HEALING:RK_SAVIOR", "DESPERATE_POWER:RK_BATTLEMAGE",
		"DIET:RK_THE_PROTECTOR", "DIVINE_BLAST:RK_NOBLE_CALL", "DIVINE_RAY:RK_PRIEST", "DIVINE_SENSE:RK_KINGS_VISION",
		"DRAGONS_EYE:RK_PERFECT_COLLECTION", "DRAWING_ENHANCE:RK_AMBUSH", "DURABLE_PROJECTILES:RK_PURSUIT", "DURABLE_ROPE:RK_EXPLORER",
		"DURABLE_TIPS:RK_WARDEN", "DYNAMIC_PREPARATION:RK_MASTER", "ELEMENTAL_BULLET:RK_GUNSLINGER", "EMPOWERED_STRIKE:RK_BATTLEMAGE",
		"EMPOWERING_MEAL:RK_ROYAL_PRIVILEGE", "ENCHANT:RK_ENCHANTER", "RA_ENDURANCE:RK_BERSERKER", "ENERGIZING_MEAL:RK_ROYAL_MEAL",
		"ENERGY_BARRIER:RK_MONK", "ENERGY_COLLECT:RK_SLASHER", "ENERGY_DRAW:RK_ASSASSIN", "ENERGY_REMAINS:RK_WIZARD",
		"ENERGY_SAVING:RK_SLASHER", "ENGINEER_CMD:RK_MEDICALOFFICER", "ENHANCED_COMBO:RK_GLADIATOR", "ENHANCED_CRIT:RK_MASTER",
		"ENHANCED_LETHALITY:RK_ASSASSIN", "ENHANCED_MARK:RK_WARLOCK", "ENHANCED_RINGS:RK_ASSASSIN", "ENLIGHTENING_MEAL:RK_ROYAL_MEAL",
		"EVASIVE_ARMOR:RK_FREERUNNER", "EXCESS_CHARGE:RK_BATTLEMAGE", "EXPANDED_POWER:RK_BOWMASTER", "EXPLOSION_CMD:RK_MEDICALOFFICER",
		"FANCY_PERFORMANCE:RK_JUGGLER", "FARSIGHT:RK_SNIPER", "FASTER_CHARGE:RK_CHAMPION", "FASTER_CHARGER:RK_ENERGY_SURGE",
		"FASTER_THAN_LIGHT:RK_SLAYER", "FASTSHOT:RK_BOWMASTER", "RA_FAST_RELOAD:RK_GUNSLINGER", "FIGHTING_MEAL:RK_ROYAL_FEAST",
		"FINISH_ATTACK:RK_AMBUSH", "FIREWATCH:RK_THE_PROTECTOR", "FIRST_AID_TREAT:RK_WELL_PROTECTED", "FLAG_OF_CONQUEST:RK_NOBLE_CALL",
		"FOCUSED_MEAL:RK_ROYAL_MEAL", "FOCUS_MAINTAIN:RK_JUGGLER", "FOCUS_SHOT:RK_SHARPSHOOTER", "FOLLOWUP_SHOOT:RK_JUGGLER",
		"FOLLOWUP_STRIKE:RK_KINGS_WISDOM", "FORCE_SAVING:RK_ROYAL_FOCUS", "FULLY_POTION:RK_TEMPORARY_DRAUGHT",
		"GODS_JUDGEMENT:RK_CRUSADER", "HABITUAL_HAND:RK_JUGGLER", "HALLOWED_GROUND:RK_PRIEST", "HARD_SHIELD:RK_WELL_PROTECTED",
		"HARMONY:RK_MONK", "HARVEST_BERRY:RK_ROYAL_FOCUS", "HASTE_EVASION:RK_SLAYER", "HEADSHOT:RK_OUTLAW", "HEALING_DEW:RK_WARDEN",
		"HEALING_MEAL:RK_ROYAL_FEAST", "HEARTY_MEAL:RK_ROYAL_PRIVILEGE", "HEIGHTENED_SENSES:RK_KINGS_VISION",
		"HERB_EXTRACTION:RK_TEMPORARY_DRAUGHT", "HIGH_POWER:RK_PERFECT_COLLECTION", "HOLD_FAST:RK_BERSERKER", "HOLY_BOMB:RK_PRIEST",
		"HOLY_INTUITION:RK_ROYAL_INTUITION", "HOLY_LANCE:RK_PRIEST", "HOLY_MANTLE:RK_PALADIN", "HOLY_SHIELD:RK_CRUSADER",
		"HONORABLE_SHOT:RK_OUTLAW", "HURRICANE:RK_SHARPSHOOTER", "IMPREGNABLE_MEAL:RK_ROYAL_FEAST", "IMPROVED_TACKLE:RK_VETERAN",
		"IMPROVISATION:RK_GUNSLINGER", "IMPROVISED_PROJECTILES:RK_KINGS_VISION", "INCAPACITATION:RK_VETERAN",
		"INDUCE_AGGRO:RK_PALADIN", "INEVITABLE_DEATH:RK_OUTLAW", "INFINITE_BULLET_MEAL:RK_ROYAL_FEAST", "INNER_EYE:RK_MASTER",
		"INSCRIBED_BULLET:RK_TEMPORARY_DRAUGHT", "INSCRIBED_LETHALITY:RK_TEMPORARY_DRAUGHT", "INSCRIBED_POWER:RK_RESTORATION",
		"INSCRIBED_STEALTH:RK_RESTORATION", "INTO_THE_SHADOW:RK_SPECIALIST", "INVIGORATING_MEAL:RK_ROYAL_MEAL",
		"IRON_STOMACH:RK_ROYAL_MEAL", "IRON_WILL:RK_NOBLE_CAUSE", "JUNGLE_EXPLORE:RK_EXPLORER", "KICK:RK_SNIPER",
		"KINETIC_ATTACK:RK_EXTRA_POWER", "KINETIC_BATTLE:RK_EXTRA_POWER", "LARGER_MAGAZINE:RK_PERFECT_COLLECTION", "LASSO:RK_EXPLORER",
		"LAY_ON_HANDS:RK_PALADIN", "LEG_SWEEP:RK_AMBUSH", "LESS_RESIST:RK_FIGHTER", "LETHAL_DEFENSE:RK_GLADIATOR",
		"LETHAL_HASTE:RK_PURSUIT", "LETHAL_MOMENTUM:RK_PURSUIT", "LETHAL_POWER:RK_SLASHER", "LETHAL_RAGE:RK_BERSERKER",
		"LIFE_ENERGY:RK_WIZARD", "LIGHT_CLOAK:RK_FREERUNNER", "LIGHT_MOVEMENT:RK_GUNSLINGER", "LIGHT_READING:RK_PALADIN",
		"LIGHT_WEAPON:RK_GLADIATOR", "LINGERING_MAGIC:RK_KINGS_WISDOM", "LIQUID_AGILITY:RK_RESTORATION",
		"LIQUID_NATURE:RK_RESTORATION", "LIQUID_WILLPOWER:RK_RESTORATION", "LIVING_GRASS:RK_WARDEN", "LONG_MACHETE:RK_EXPLORER",
		"MACHINEGUN:RK_ENGINEER", "MADNESS:RK_WARLOCK", "MAGICAL_CIRCLE:RK_BATTLEMAGE", "MAGIC_EMPOWER:RK_WIZARD",
		"MAGIC_RUSH:RK_BATTLEMAGE", "MAKESHIFT_BOW:RK_BOWMASTER", "MARK_OF_WEAKNESS:RK_WARLOCK", "MASTER_OF_CLOAKING:RK_CHASER",
		"MAX_HEALTH:RK_EXTRA_BULK", "MAX_RAGE:RK_BERSERKER", "MEDICAL_RAY:RK_SAVIOR", "MIND_FOCUSING:RK_SLASHER",
		"MIND_VISION:RK_THE_PROTECTOR", "MNEMONIC_PRAYER:RK_PRIEST", "MOMENTARY_FOCUSING:RK_FREERUNNER", "MONASTIC_VIGOR:RK_MONK",
		"MORTAR:RK_ENGINEER", "MOVESPEED_ENHANCE:RK_ENERGY_SURGE", "MOVE_CMD:RK_MEDICALOFFICER", "MOVING_FOCUS:RK_BOWMASTER",
		"MOVING_SHOT:RK_GUNSLINGER", "MYSTICAL_CHARGE:RK_BATTLEMAGE", "MYSTICAL_MEAL:RK_ROYAL_MEAL", "MYSTICAL_PUNCH:RK_FIGHTER",
		"MYSTICAL_TACKLE:RK_VETERAN", "NATURES_AID:RK_NOBLE_CAUSE", "NATURES_BOUNTY:RK_ROYAL_PRIVILEGE", "NATURES_MEAL:RK_ROYAL_FEAST",
		"NATURE_FRIENDLY:RK_THE_PROTECTOR", "NECROMANCERS_MINIONS:RK_WARLOCK", "OFFENSIVE_DEFENSE:RK_GLADIATOR",
		"OINTMENT:RK_THERAPIST", "OVERCOME:RK_DEATHKNIGHT", "OVERCOMING:RK_FREERUNNER", "PARKOUR:RK_HORSEMAN",
		"PARRING:RK_WELL_PROTECTED", "PARRY:RK_NOBLE_CALL", "PATIENT_STRIKE:RK_KINGS_WISDOM", "PERFECT_ASSASSIN:RK_ASSASSIN",
		"PERFECT_FOCUSING:RK_OUTLAW", "PERFECT_SHOT:RK_SHARPSHOOTER", "PHARMACEUTICS:RK_TEMPORARY_DRAUGHT", "PILOTING:RK_HORSEMAN",
		"POINT_BLANK:RK_SNIPER", "POISONOUS_BLADE:RK_CHASER", "POWERFUL_ACID:RK_RESEARCHER", "POWERFUL_CRIT:RK_NOBLE_CALL",
		"POWERFUL_SLASH:RK_MASTER", "POWERFUL_TACKLE:RK_VETERAN", "POWER_OF_LIFE:RK_PALADIN", "PRAY_FOR_DEAD:RK_CRUSADER",
		"PRECISE_ASSAULT:RK_CHAMPION", "PROJECTILE_MOMENTUM:RK_FREERUNNER", "PROMOTE_CMD:RK_MEDICALOFFICER",
		"PROTECTION:RK_THE_PROTECTOR", "PROTECTIVE_SHADOWS:RK_NOBLE_CAUSE", "PROTECTIVE_SLASH:RK_WELL_PROTECTED",
		"PROVOKED_ANGER:RK_KINGS_WISDOM", "PUNISHMENT:RK_CRUSADER", "PUSHBACK:RK_NOBLE_CALL", "QUICK_FOLLOWUP:RK_CHAMPION",
		"QUICK_PREP:RK_FREERUNNER", "QUICK_PREPARE:RK_THERAPIST", "QUICK_RECOVER:RK_SLAYER", "QUICK_RELOAD:RK_GUNSLINGER",
		"QUICK_SHEATHING:RK_MASTER", "QUICK_STEP:RK_FIGHTER", "RADIATION:RK_PERFECT_COLLECTION", "RANGED_LETHALITY:RK_SHARPSHOOTER",
		"RANGED_SNIPING:RK_SPECIALIST", "RAPID_GROWTH:RK_RESEARCHER", "RECALL_INSCRIPTION:RK_RESTORATION", "RECRUIT:RK_SAVIOR",
		"RECYCLING:RK_TEMPORARY_DRAUGHT", "REJUVENATING_STEPS:RK_POWER_WITHIN", "RELOADING_MEAL:RK_ROYAL_FOCUS",
		"RESENTMENT:RK_DEATHKNIGHT", "RESTORED_ENERGY:RK_MONK", "RA_RESURRECTION:RK_PRIEST", "RING_KNUCKLE:RK_FIGHTER",
		"ROGUES_FORESIGHT:RK_POWER_WITHIN", "ROLLING:RK_OUTLAW", "ROOT:RK_AMBUSH", "ROPE_COLLECTOR:RK_EXPLORER",
		"ROPE_MASTER:RK_EXPLORER", "ROPE_REBOUND:RK_PERFECT_COLLECTION", "RUNIC_TRANSFERENCE:RK_POWER_WITHIN",
		"SAFE_POTION:RK_ROYAL_PERCEPTIVITY", "SAFE_RELOAD:RK_WELL_PROTECTED", "SATIATED_SPELLS:RK_ROYAL_PRIVILEGE",
		"SCAR_ATTACK:RK_ROYAL_FOCUS", "SCHOLARS_INTUITION:RK_ROYAL_INTUITION", "SEARING_LIGHT:RK_KINGS_WISDOM",
		"SECOND_EFFECT:RK_WIZARD", "SEER_SHOT:RK_WARDEN", "SHARED_ENCHANTMENT:RK_SNIPER", "SHARED_UPGRADES:RK_SNIPER",
		"SHIELDING_DEW:RK_WARDEN", "SHIELD_BATTERY:RK_RESTORATION", "SHIELD_OF_LIGHT:RK_NOBLE_CAUSE", "SHOCKWAVE:RK_HORSEMAN",
		"SHOOTING_EYES:RK_SNIPER", "SILENT_STEPS:RK_PURSUIT", "SKILLED_HAND:RK_EXTRA_POWER", "SKILLFUL_JUGGLING:RK_JUGGLER",
		"SKILLFUL_RUNNER:RK_SPECIALIST", "SKILL_REPEAT:RK_GLADIATOR", "SMITHING_SPELL:RK_TEMPORARY_DRAUGHT",
		"SOUL_BULLET:RK_GUNSLINGER", "SOUL_COLLECT:RK_CHASER", "SOUL_EATER:RK_WARLOCK", "SOUL_SIPHON:RK_WARLOCK",
		"SPECTRE_ARROW:RK_BOWMASTER", "SPEEDY_MOVE:RK_AMBUSH", "SPEEDY_STEALTH:RK_FREERUNNER", "SPELL_ENHANCE:RK_WIZARD",
		"SPLINT:RK_THERAPIST", "STABLE_BARRIER:RK_SLAYER", "STATIC_PREPARATION:RK_MASTER", "STEALTH:RK_SPECIALIST",
		"STEALTH_MASTER:RK_SPECIALIST", "STICKY_OOZE:RK_RESEARCHER", "STIMPACK:RK_SAVIOR", "STIMPACK_CMD:RK_MEDICALOFFICER",
		"STORED_POWER:RK_SLASHER", "STREET_BATTLE:RK_OUTLAW", "STRENGTHENING_MEAL:RK_ROYAL_PRIVILEGE", "STRONGMAN:RK_BERSERKER",
		"STRONG_HEALPOWER:RK_THE_PROTECTOR", "STRONG_NEXUS:RK_SAVIOR", "STUN:RK_ENCHANTER", "SUCKER_PUNCH:RK_KINGS_WISDOM",
		"SUNRAY:RK_PURSUIT", "SUPER_ARMOR:RK_VETERAN", "SWIFT_EQUIP:RK_POWER_WITHIN", "SWIFT_JUGGLING:RK_JUGGLER",
		"SWIFT_MOVEMENT:RK_FIGHTER", "TACKLE:RK_DEATHKNIGHT", "TARGET_SET:RK_MEDICALOFFICER", "TARGET_SPOTTING:RK_SNIPER",
		"TELESCOPE:RK_SPECIALIST", "THIEFS_INTUITION:RK_ROYAL_INTUITION", "THUNDER_IMBUE:RK_ENCHANTER", "TIME_AMP:RK_ENCHANTER",
		"RA_TOUGH_MEAL:RK_ROYAL_FEAST,RK_ROYAL_FOCUS", "TOUR_PERFORMANCE:RK_JUGGLER", "TRAIL_TRACKING:RK_CHASER",
		"TWIN_SWORD:RK_CHAMPION", "TWIN_UPGRADES:RK_CHAMPION", "UNDEAD:RK_DEATHKNIGHT", "UNENCUMBERED_SPIRIT:RK_MONK",
		"UNENCUMBERED_STEP:RK_BOWMASTER", "UNEXPECTED_SLASH:RK_KINGS_WRATH", "VARIED_CHARGE:RK_CHAMPION",
		"VETERANS_INTUITION:RK_ROYAL_INTUITION", "VINE_BIND:RK_EXPLORER", "WALL_OF_LIGHT:RK_PALADIN",
		"WAND_PRESERVATION:RK_POWER_WITHIN", "WARDING_LIGHT:RK_EXTRA_BULK", "WAR_CRY:RK_AMBUSH", "WATCHTOWER:RK_ENGINEER",
		"WATER_ABSORB:RK_RESEARCHER", "WATER_FRIENDLY:RK_EXTRA_POWER", "WEAKENING_HEX:RK_ENCHANTER", "WEAKENING_POISON:RK_KINGS_WRATH",
		"WEAPON_MASTERY:RK_KINGS_WRATH", "WEAPON_RECHARGING:RK_POWER_WITHIN", "WIDE_SEARCH:RK_KINGS_VISION", "WIND_BLAST:RK_SLASHER",
		"WIRE:RK_ENGINEER",
	};

	private static EnumMap<Talent, Talent[]> borrowed;

	private static EnumMap<Talent, Talent[]> borrowedMap(){
		if (borrowed == null){
			EnumMap<Talent, Talent[]> m = new EnumMap<>(Talent.class);
			for (String row : BORROWED){
				String[] kv = row.split(":");
				String[] ks = kv[1].split(",");
				Talent[] kings = new Talent[ks.length];
				for (int i = 0; i < ks.length; i++) kings[i] = Talent.valueOf(ks[i]);
				m.put(Talent.valueOf(kv[0]), kings);
			}
			borrowed = m;
		}
		return borrowed;
	}

	//the points the King has in a talent he does not hold himself, through the talent of his that stands for it
	public static int borrowedPoints( Hero hero, Talent talent ){
		Talent[] kings = borrowedMap().get(talent);
		if (kings == null) return 0;
		int pts = 0;
		for (Talent k : kings){
			pts = Math.max(pts, hero.pointsInTalent(k));
		}
		return Math.min(pts, talent.maxPoints());
	}

	//when the King raises a talent, the talents it stands for react as if they were raised
	public static void onTalentUpgraded( Hero hero, Talent talent ){
		if (hero.heroClass != HeroClass.RAT_KING) return;
		for (java.util.Map.Entry<Talent, Talent[]> e : borrowedMap().entrySet()){
			for (Talent k : e.getValue()){
				if (k == talent) {
					Talent.onTalentUpgraded(hero, e.getKey());
					break;
				}
			}
		}
		for (java.util.Map.Entry<HeroSubClass, Talent> e : subTalents().entrySet()){
			if (e.getValue() == talent && hero.pointsInTalent(talent) == 1){
				hero.ensureSubclassBuffs();
				if (e.getKey() == HeroSubClass.ASSASSIN && hero.invisible > 0){
					Buff.affect(hero, Preparation.class);
				}
			}
		}
	}

	//every class item of Shattered's and Re-ARranged's heroes, as in the mod
	public static void init( Hero hero ){
		//warrior
		if (hero.belongings.armor != null){
			hero.belongings.armor.affixSeal(new BrokenSeal());
		}
		//mage
		MagesStaff staff = new MagesStaff(new WandOfMagicMissile());
		(hero.belongings.weapon = staff).identify();
		hero.belongings.weapon.activate(hero);
		//rogue
		CloakOfShadows cloak = new CloakOfShadows();
		(hero.belongings.artifact = cloak).identify();
		hero.belongings.artifact.activate(hero);
		//huntress
		SpiritBow bow = new SpiritBow();
		bow.identify().collect();
		//cleric
		HolyTome tome = new HolyTome();
		(hero.belongings.misc = tome).identify();
		hero.belongings.misc.activate(hero);
		//Re-ARranged's heroes
		CheesyCheest chest = new CheesyCheest();
		chest.collect();
		new BulletBelt().quantity(5).collect();
		new AR_T1().identify().collect();
		new Sheath().collect();
		new Machete().identify().collect();
		new Shovel().identify().collect();
		new KnightsShield().collect();
		new GammaRayGun().collect();
		new MedicKit().identify().collect();
		new WornShortBow().identify().collect();
		new ArrowBag().collect();

		Dungeon.quickslot.setSlot(0, bow);
		Dungeon.quickslot.setSlot(1, cloak);
		Dungeon.quickslot.setSlot(2, staff);
		Dungeon.quickslot.setSlot(3, tome);
		Dungeon.quickslot.setSlot(4, chest);
	}
}
