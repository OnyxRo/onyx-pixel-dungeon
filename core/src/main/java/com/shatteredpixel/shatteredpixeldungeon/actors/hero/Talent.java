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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArcaneArmor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invulnerability;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArtifactRecharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.CounterBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.EnhancedRings;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PhysicalEmpower;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RevealedArea;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ScrollEmpower;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WandEmpower;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.Ratmogrify;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.DivineSense;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.RecallInscription;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClothArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.Runestone;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfIntuition;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.ShardOfOblivion;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Gloves;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

public enum Talent {

	//Warrior T1
	HEARTY_MEAL(0), VETERANS_INTUITION(1), PROVOKED_ANGER(2), IRON_WILL(3),
	//Warrior T2
	IRON_STOMACH(4), LIQUID_WILLPOWER(5), RUNIC_TRANSFERENCE(6), LETHAL_MOMENTUM(7), IMPROVISED_PROJECTILES(8),
	//Warrior T3
	HOLD_FAST(9, 3), STRONGMAN(10, 3),
	//Berserker T3
	ENDLESS_RAGE(11, 3), DEATHLESS_FURY(12, 3), ENRAGED_CATALYST(13, 3),
	//Gladiator T3
	CLEAVE(14, 3), LETHAL_DEFENSE(15, 3), ENHANCED_COMBO(16, 3),
	//Heroic Leap T4
	BODY_SLAM(17, 4), IMPACT_WAVE(18, 4), DOUBLE_JUMP(19, 4),
	//Shockwave T4
	EXPANDING_WAVE(20, 4), STRIKING_WAVE(21, 4), SHOCK_FORCE(22, 4),
	//Endure T4
	SUSTAINED_RETRIBUTION(23, 4), SHRUG_IT_OFF(24, 4), EVEN_THE_ODDS(25, 4),

	//Mage T1
	EMPOWERING_MEAL(32), SCHOLARS_INTUITION(33), LINGERING_MAGIC(34), BACKUP_BARRIER(35),
	//Mage T2
	ENERGIZING_MEAL(36), INSCRIBED_POWER(37), WAND_PRESERVATION(38), ARCANE_VISION(39), SHIELD_BATTERY(40),
	//Mage T3
	DESPERATE_POWER(41, 3), ALLY_WARP(42, 3),
	//Battlemage T3
	EMPOWERED_STRIKE(43, 3), MYSTICAL_CHARGE(44, 3), EXCESS_CHARGE(45, 3),
	//Warlock T3
	SOUL_EATER(46, 3), SOUL_SIPHON(47, 3), NECROMANCERS_MINIONS(48, 3),
	//Elemental Blast T4
	BLAST_RADIUS(49, 4), ELEMENTAL_POWER(50, 4), REACTIVE_BARRIER(51, 4),
	//Wild Magic T4
	WILD_POWER(52, 4), FIRE_EVERYTHING(53, 4), CONSERVED_MAGIC(54, 4),
	//Warp Beacon T4
	TELEFRAG(55, 4), REMOTE_BEACON(56, 4), LONGRANGE_WARP(57, 4),

	//Rogue T1
	CACHED_RATIONS(64), THIEFS_INTUITION(65), SUCKER_PUNCH(66), PROTECTIVE_SHADOWS(67),
	//Rogue T2
	MYSTICAL_MEAL(68), INSCRIBED_STEALTH(69), WIDE_SEARCH(70), SILENT_STEPS(71), ROGUES_FORESIGHT(72),
	//Rogue T3
	ENHANCED_RINGS(73, 3), LIGHT_CLOAK(74, 3),
	//Assassin T3
	ENHANCED_LETHALITY(75, 3), ASSASSINS_REACH(76, 3), BOUNTY_HUNTER(77, 3),
	//Freerunner T3
	EVASIVE_ARMOR(78, 3), PROJECTILE_MOMENTUM(79, 3), SPEEDY_STEALTH(80, 3),
	//Smoke Bomb T4
	HASTY_RETREAT(81, 4), BODY_REPLACEMENT(82, 4), SHADOW_STEP(83, 4),
	//Death Mark T4
	FEAR_THE_REAPER(84, 4), DEATHLY_DURABILITY(85, 4), DOUBLE_MARK(86, 4),
	//Shadow Clone T4
	SHADOW_BLADE(87, 4), CLONED_ARMOR(88, 4), PERFECT_COPY(89, 4),

	//Huntress T1
	NATURES_BOUNTY(96), SURVIVALISTS_INTUITION(97), FOLLOWUP_STRIKE(98), NATURES_AID(99),
	//Huntress T2
	INVIGORATING_MEAL(100), LIQUID_NATURE(101), REJUVENATING_STEPS(102), HEIGHTENED_SENSES(103), DURABLE_PROJECTILES(104),
	//Huntress T3
	POINT_BLANK(105, 3), SEER_SHOT(106, 3),
	//Sniper T3
	FARSIGHT(107, 3), SHARED_ENCHANTMENT(108, 3), SHARED_UPGRADES(109, 3),
	//Warden T3
	DURABLE_TIPS(110, 3), BARKSKIN(111, 3), SHIELDING_DEW(112, 3),
	//Spectral Blades T4
	FAN_OF_BLADES(113, 4), PROJECTING_BLADES(114, 4), SPIRIT_BLADES(115, 4),
	//Natures Power T4
	GROWING_POWER(116, 4), NATURES_WRATH(117, 4), WILD_MOMENTUM(118, 4),
	//Spirit Hawk T4
	EAGLE_EYE(119, 4), GO_FOR_THE_EYES(120, 4), SWIFT_SPIRIT(121, 4),

	//Duelist T1
	STRENGTHENING_MEAL(128), ADVENTURERS_INTUITION(129), PATIENT_STRIKE(130), AGGRESSIVE_BARRIER(131),
	//Duelist T2
	FOCUSED_MEAL(132), LIQUID_AGILITY(133), WEAPON_RECHARGING(134), LETHAL_HASTE(135), SWIFT_EQUIP(136),
	//Duelist T3
	PRECISE_ASSAULT(137, 3), DEADLY_FOLLOWUP(138, 3),
	//Champion T3
	VARIED_CHARGE(139, 3), TWIN_UPGRADES(140, 3), COMBINED_LETHALITY(141, 3),
	//Monk T3
	UNENCUMBERED_SPIRIT(142, 3), MONASTIC_VIGOR(143, 3), COMBINED_ENERGY(144, 3),
	//Challenge T4
	CLOSE_THE_GAP(145, 4), INVIGORATING_VICTORY(146, 4), ELIMINATION_MATCH(147, 4),
	//Elemental Strike T4
	ELEMENTAL_REACH(148, 4), STRIKING_FORCE(149, 4), DIRECTED_POWER(150, 4),
	//Feint T4
	FEIGNED_RETREAT(151, 4), EXPOSE_WEAKNESS(152, 4), COUNTER_ABILITY(153, 4),

	//Cleric T1
	SATIATED_SPELLS(160), HOLY_INTUITION(161), SEARING_LIGHT(162), SHIELD_OF_LIGHT(163),
	//Cleric T2
	ENLIGHTENING_MEAL(164), RECALL_INSCRIPTION(165), SUNRAY(166), DIVINE_SENSE(167), BLESS(168),
	//Cleric T3
	CLEANSE(169, 3), LIGHT_READING(170, 3),
	//Priest T3
	HOLY_LANCE(171, 3), HALLOWED_GROUND(172, 3), MNEMONIC_PRAYER(173, 3),
	//Paladin T3
	LAY_ON_HANDS(174, 3), AURA_OF_PROTECTION(175, 3), WALL_OF_LIGHT(176, 3),
	//Ascended Form T4
	DIVINE_INTERVENTION(177, 4), JUDGEMENT(178, 4), FLASH(179, 4),
	//Trinity T4
	BODY_FORM(180, 4), MIND_FORM(181, 4), SPIRIT_FORM(182, 4),
	//Power of Many T4
	BEAMING_RAY(183, 4), LIFE_LINK(184, 4), STASIS(185, 4),

	//Zealot T1, from Devoted PD
	ARCANE_MEAL(192, 2), DEMONIC_INTUITION(193, 2), WRATHFUL_STRIKE(194, 2), PAIN_IS_GAIN(195, 2),
	//Zealot T2
	CLEANSING_MEAL(196, 2), CONFIRMED_BELIEF(197, 2), HIDDEN_POWER(198, 2), INSTANT_GRATIFICATION(199, 2), ESOTERIC_MIND(200, 2),
	//Zealot T3
	DEMOLITIONIST(201, 3), UNHOLY_WRATH(202, 3),
	//Conjurer T3
	ELDRITCH_ENERGY(203, 3), HIVE_MIND(204, 3), TAG_TEAM(205, 3),
	//Devotee T3
	NIHILISM(206, 3), GOURMAND(207, 3), DEVOUR_RITUAL(208, 3),
	//Calling Beyond T4
	FORBIDDEN_KNOWLEDGE(209, 4), FORTIFIED_CONCIOUSNESS(210, 4), CAUTION_TO_THE_WIND(211, 4),
	//Eternal Balance T4
	SUSTAINED_PRESSURE(212, 4), SUBZERO_KICK(213, 4), GRAY_MATTER(214, 4),
	//Weal and Woe T4
	WOE_IS_ME(215, 4), AND_WOE_IS_YOU(216, 4), WEIGHING_THE_DIE(217, 4),

	//Artificer T1, from Pixel Dungeon Reforged (icons moved up by 27 since the Zealot uses the same cells)
	TINKERERS_MEAL(219), PATTERN_RECOGNITION(220), VOLATILE_CHAIN(221), ARCSHIELDING(222),
	//Artificer T2
	QUICK_CALIBRATION(223), REACTIVE_CHEMISTRY(224), OVERCLOCKED_SYSTEMS(225), VOLATILE_SALVAGE(226), ADAPTIVE_MINEFIELD(227),
	//Artificer T3
	MECHANICAL_GOGGLES(228, 3), EFFECTIVE_SHOT(229, 3),
	//Constructor T3
	CONSTRUCT_VISION(230, 3), CONSTRUCT_MOBILITY(231, 3), CONSTRUCT_LETHALITY(232, 3),
	//Armorer T3
	WEAPON_MOD_AUTOLOAD(233, 3), ARMOR_MOD_INFINITE_FALLING(234, 3), ARMOR_MOD_EMERGENCY_DEFENSE(235, 3),
	//Quickdraw T4
	POWERFUL_SHOT(236, 4), DOUBLE_BARREL(237, 4), MULTISHOT(238, 4),
	//Truesight T4
	ENHANCED_VISION(239, 4), SONAR(240, 4), BRIGHT_LIGHT(241, 4),
	//Reflection T4
	ADRENALINE(242, 4), ENDURANCE(243, 4), POWERFUL_REFLECTION(244, 4),

	//Friar T1, from Transformation PD (icons are the mod's cell numbers, copied by scripts/port-icons.py)
	TRANQUIL_TINCTURE(576), RAVENS_EYE(577), CRYSTAL_GUNPOWDER(578), BULWARK_GREATSHIELD(579),
	//Friar T2
	WHISPERING_MEAL(580), LIQUID_BARRIER(581), FAST_RELOAD(582), OTHERWORLD_BANE(583), BULLSEYE(584),
	//Friar T3
	MIDAS_TOUCH(585, 3), HUMAN_GLORY(586, 3),
	//Alchemist T3
	ALCHEMY_SHIELD(587, 3), CLOSE_BLAST(588, 3), CONSERVATION(589, 3),
	//Pious T3
	NEVER_COMPROMISE(590, 3), REWIND_TIME(591, 3), BRIGHT_WARRIOR(592, 3),
	//Reason Dose T4
	HEART_DOSE(593, 4), SHIELD_DOSE(594, 4), CALM_DOSE(595, 4),
	//Concentrated Potion T4
	CONCENTRATED_ESSENCE(596, 4), REAGENT_ENHANCEMENT(597, 4), REUSE_REAGENT(598, 4),
	//Holy Prayer T4
	PIOUS_FAITH(599, 4), SUPREME_BLESSING(600, 4), HATRED_OF_EVIL(601, 4),

	//Slime Girl T1, from Transformation PD
	RESILIENT_MEAL(416), LIQUID_PERCEPTION(417), WATER_WAVE(418), LIQUID_ARMOR(419),
	//Slime Girl T2
	TOUGH_MEAL(420), SLIME_GREENHOUSE(421), ENERGY_ABSORPTION(422), NATURAL_AFFINITY(423), QUALITY_ABSORPTION(424),
	//Slime Girl T3
	ORIGINAL_MONSTER(425, 3), EMPOWERING_LIFE(426, 3),
	//Water Slime T3
	WATER_BODY(427, 3), WATER_REVIVAL(428, 3), WATER_REGENERATION(429, 3),
	//Dark Slime T3
	POTENT_OOZE(430, 3), DARK_GAS(431, 3), DARK_LIQUID(432, 3),
	//Spring Spell T4
	POTENT_HEALING(433, 4), EFFICIENT_HEALING(434, 4), MASS_HEALING(435, 4),
	//Rapid Waterfall T4
	VIOLENT_STORM(436, 4), NEW_TRAP(437, 4), HOLY_BATH(438, 4),
	//Mad Slime T4
	NO_PICK(439, 4), FOOD_BONUS(440, 4), DELICIOUS_DIGESTION(441, 4),

	//Ninja T1, from Transformation PD
	NINJA_MEAL(448), HUNTING_INTUITION(449), AGILE_ATTACK(450), YOU_SCARED_ME(451),
	//Ninja T2
	YUNYING_MEAL(452), XIA(453), QUICK_SEARCH(454), NINJA_SOCIAL(455), FEINT(456),
	//Ninja T3
	QIANFA_THROWING(457, 3), LIGHT_BOX(458, 3),
	//Tatteki Ninja T3
	SOKO(459, 3), KONO_FUKUSA(460, 3), KUNIKUCHI(461, 3),
	//Ninja Master T3
	SOUL_HUNTING(462, 3), USE_ENVIRONMENT(463, 3), MIND_WATER(464, 3),
	//Decoy T4
	DEVERSION(465, 4), SHINKAGE(466, 4), ALLHUNTING(467, 4),
	//Spider Jar T4
	POWER_GUNPOWDER(468, 4), TEA_STAINS(469, 4), FIREWORK(470, 4),
	//One Sword T4
	OFFENSIVE(471, 4), GLIMPSE(472, 4), KILL_CONTINUE(473, 4),

	//DM400 T1, from Transformation PD
	MARK_MEAL(480), OVER_CODE(481), TARGET_TARGETING(482), TBM(483),
	//DM400 T2
	OVER_MEAL(484), RECOVER_CHARGE(485), BODY_REINFORCE(486), EFFICIENT_ORDER(487), ROCKET_FIST(488),
	//DM400 T3
	BIG_FIST(489, 3), QUICK_TOOL(490, 3),
	//AT400 T3
	SUSTAIN_MARK(491, 3), BATTLE_UPGRADE(492, 3), FLY_DRONE(493, 3),
	//AU400 T3
	SPECIAL_MARK(494, 3), ASSIST_UPGRADE(495, 3), FAST_CRUISE(496, 3),
	//Routine T4
	OVER_EXTEND(497, 4), TERROR_MACH(498, 4), STRONG_PERSERVE(499, 4),
	//Discharge Gas T4
	OVER_TIME(500, 4), DEADLY_GAS(501, 4), DISCHARGE_HAPPY(502, 4),
	//Honey Comb T4
	PROCESS_EXTEND(503, 4), GLORIOUS_DEAD(504, 4), UPON_WAVE(505, 4),

	//Talents of Transformation PD's common pool that its Friar items lean on. Icons that clash with our cells sit at 1024 plus the mod's cell.
	VEGETARIANISM(1086), STRENGTH_GREATEST(1053), FALSEHOOD_POWER(413),

	//Princess T1, from Transformation PD
	ENCHANT_MEAL(544), MANA_SENSE(545), RUNE_BLADE(546), ARCANE_SHIELD(547),
	//Princess T2
	ROYAL_MEAL(548), RUNE_EXPERT(549), MORE_RING(550), MANA_WREATH(551), MAGIC_ARROW(552),
	//Princess T3
	STR_RUNE(553, 3), RING_BOND(554, 3),
	//Rune Mage T3
	DARKMARK(555, 3), RUNE_BLAST(556, 3), RUNE_SURGE(557, 3),
	//Combat Master T3
	FLUENT(558, 3), STANCE_MASTERY(559, 3), INELEMENT(560, 3),
	//March Forward T4
	SPEEDUP(561, 4), RUN_ATTACK(562, 4), DELAY_TACTIC(563, 4),
	//Taboo Magic T4
	ELEMENT_CURSE(564, 4), AFFLICTED_ILLNESS(565, 4), SPREAD_PAIN(566, 4),
	//King Blade T4
	KING_GAZE(567, 4), KING_POWER(568, 4), KING_MIGHT(569, 4),

	//Talents of Transformation PD's negative pool that its Princess items mention
	CURSEDMAN(511),

	//universal T4
	HEROIC_ENERGY(26, 4), //See icon() and title() for special logic for this one
	//Ratmogrify T4
	RATSISTANCE(247, 4), RATLOMACY(248, 4), RATFORCEMENTS(249, 4),

	//rearranged, the talents of Re-ARranged PD's six heroes. Icons are 10000 plus the cell on its own sheet
	//Gunner T1
	RELOADING_MEAL(10264),
	GUNNERS_INTUITION(10265),
	SPEEDY_MOVE(10266),
	SAFE_RELOAD(10267),
	CLOSE_COMBAT(10268),
	//Gunner T2
	INFINITE_BULLET_MEAL(10269),
	INSCRIBED_BULLET(10270),
	MIND_VISION(10271),
	CAMOUFLAGE(10272),
	LARGER_MAGAZINE(10273),
	BULLET_COLLECT(10274),
	//Gunner T3
	STREET_BATTLE(10275, 3),
	RA_FAST_RELOAD(10276, 3),
	//Outlaw T3
	ROLLING(10277, 3),
	PERFECT_FOCUSING(10278, 3),
	HONORABLE_SHOT(10279, 3),
	BULLET_TIME(10280, 3),
	INEVITABLE_DEATH(10281, 3),
	HEADSHOT(10282, 3),
	//Gunslinger T3
	QUICK_RELOAD(10283, 3),
	MOVING_SHOT(10284, 3),
	ELEMENTAL_BULLET(10285, 3),
	IMPROVISATION(10286, 3),
	SOUL_BULLET(10287, 3),
	LIGHT_MOVEMENT(10288, 3),
	//Specialist T3
	STEALTH_MASTER(10289, 3),
	SKILLFUL_RUNNER(10290, 3),
	STEALTH(10291, 3),
	INTO_THE_SHADOW(10292, 3),
	RANGED_SNIPING(10293, 3),
	TELESCOPE(10294, 3),
	//Riot T4
	HASTE_MOVE(10295, 4),
	SHOT_CONCENTRATION(10296, 4),
	ROUND_PRESERVE(10297, 4),
	//ReinforcedArmor T4
	BAYONET(10298, 4),
	TACTICAL_SIGHT(10299, 4),
	PLATE_ADD(10300, 4),
	//FirstAidKit T4
	ADDITIONAL_MEDS(10301, 4),
	THERAPEUTIC_BANDAGE(10302, 4),
	FASTER_HEALING(10303, 4),
	//Samurai T1
	BASIC_PRACTICE(10308),
	MASTERS_INTUITION(10309),
	DRAWING_ENHANCE(10310),
	PARRING(10311),
	ADRENALINE_SURGE(10312),
	//Samurai T2
	CRITICAL_MEAL(10313),
	INSCRIBED_LETHALITY(10314),
	UNEXPECTED_SLASH(10315),
	DRAGONS_EYE(10316),
	WEAPON_MASTERY(10317),
	CRITICAL_THROW(10318),
	//Samurai T3
	QUICK_SHEATHING(10319, 3),
	LETHAL_POWER(10320, 3),
	//Slasher T3
	MIND_FOCUSING(10321, 3),
	STORED_POWER(10322, 3),
	ARCANE_POWER(10323, 3),
	ENERGY_COLLECT(10324, 3),
	ENERGY_SAVING(10325, 3),
	WIND_BLAST(10326, 3),
	//Master T3
	ENHANCED_CRIT(10327, 3),
	POWERFUL_SLASH(10328, 3),
	STATIC_PREPARATION(10329, 3),
	ACCELERATION(10330, 3),
	INNER_EYE(10331, 3),
	DYNAMIC_PREPARATION(10332, 3),
	//Slayer T3
	FASTER_THAN_LIGHT(10333, 3),
	AFTERIMAGE(10334, 3),
	QUICK_RECOVER(10335, 3),
	HASTE_EVASION(10336, 3),
	ACCELERATED_LETHALITY(10337, 3),
	STABLE_BARRIER(10338, 3),
	//Awake T4
	AWAKE_LIMIT(10339, 4),
	AWAKE_DURATION(10340, 4),
	INSURANCE(10341, 4),
	//ShadowBlade T4
	DOUBLE_BLADE_PRACTICE(10342, 4),
	CRITICAL_SHADOW(10343, 4),
	HERBAL_SHADOW(10344, 4),
	//Kunai T4
	KUNAI_OF_DOOM(10345, 4),
	MYSTICAL_KUNAI(10346, 4),
	CORROSIVE_KUNAI(10347, 4),
	//Adventurer T1
	HARVEST_BERRY(10352),
	SAFE_POTION(10353),
	ROOT(10354),
	PROTECTIVE_SLASH(10355),
	KINETIC_ATTACK(10356),
	//Adventurer T2
	NATURES_MEAL(10357),
	PHARMACEUTICS(10358),
	HERB_EXTRACTION(10359),
	FIREWATCH(10360),
	ROPE_REBOUND(10361),
	WEAKENING_POISON(10362),
	//Adventurer T3
	LONG_MACHETE(10363, 3),
	BLOOMING_WEAPON(10364, 3),
	//Engineer T3
	BARRICADE(10365, 3),
	WIRE(10366, 3),
	WATCHTOWER(10367, 3),
	CANNON(10368, 3),
	MACHINEGUN(10369, 3),
	MORTAR(10370, 3),
	//Explorer T3
	JUNGLE_EXPLORE(10371, 3),
	DURABLE_ROPE(10372, 3),
	LASSO(10373, 3),
	ROPE_COLLECTOR(10374, 3),
	ROPE_MASTER(10375, 3),
	VINE_BIND(10376, 3),
	//Researcher T3
	BIOLOGY_PROJECT(10377, 3),
	RAPID_GROWTH(10378, 3),
	BIO_ENERGY(10379, 3),
	WATER_ABSORB(10380, 3),
	POWERFUL_ACID(10381, 3),
	STICKY_OOZE(10382, 3),
	//Sprout T4
	JUNGLE(10383, 4),
	FOREST(10384, 4),
	REGROWTH(10385, 4),
	//TreasureMap T4
	LONG_LUCK(10386, 4),
	FORESIGHT(10387, 4),
	GOLD_HUNTER(10388, 4),
	//Root T4
	POISONOUS_ROOT(10389, 4),
	ROOT_SPREAD(10390, 4),
	ROOT_ARMOR(10391, 4),
	//Knight T1
	RA_TOUGH_MEAL(10396),
	KNIGHTS_INTUITION(10397),
	KINETIC_BATTLE(10398),
	HARD_SHIELD(10399),
	WAR_CRY(10400),
	//Knight T2
	IMPREGNABLE_MEAL(10401),
	SMITHING_SPELL(10402),
	ARMOR_ADAPTION(10403),
	CHIVALRY(10404),
	PROTECTION(10405),
	FLAG_OF_CONQUEST(10406),
	//Knight T3
	CRAFTMANS_SKILLS(10407, 3),
	TACKLE(10408, 3),
	//DeathKnight T3
	ARMY_OF_DEATH(10409, 3),
	DEATHS_CHILL(10410, 3),
	OVERCOME(10411, 3),
	RESENTMENT(10412, 3),
	UNDEAD(10413, 3),
	DEATHS_FEAR(10414, 3),
	//Horseman T3
	SHOCKWAVE(10415, 3),
	ARMORED_HORSE(10416, 3),
	DASH_ENHANCE(10417, 3),
	BUFFER(10418, 3),
	PARKOUR(10419, 3),
	PILOTING(10420, 3),
	//Crusader T3
	HOLY_SHIELD(10421, 3),
	PRAY_FOR_DEAD(10422, 3),
	GODS_JUDGEMENT(10423, 3),
	CLEANSING_PRAY(10424, 3),
	PUNISHMENT(10425, 3),
	ANTI_DEMON(10426, 3),
	//HolyShield T4
	BUFFER_BARRIER(10427, 4),
	HOLY_LIGHT(10428, 4),
	BLESSING(10429, 4),
	//StimPack T4
	BURDEN_RELIEF(10430, 4),
	LASTING_PACK(10431, 4),
	TIME_STOP(10432, 4),
	//UnstableAnkh T4
	BLESSED_ANKH(10433, 4),
	ANKH_ENHANCE(10434, 4),
	COMPLETE_ANKH(10435, 4),
	//Medic T1
	SCAR_ATTACK(10440),
	DOCTORS_INTUITION(10441),
	FINISH_ATTACK(10442),
	FIRST_AID_TREAT(10443),
	BREAKTHROUGH(10444),
	//Medic T2
	HEALING_MEAL(10445),
	RECYCLING(10446),
	HIGH_POWER(10447),
	RADIATION(10448),
	STRONG_HEALPOWER(10449),
	DIET(10450),
	//Medic T3
	STRONG_NEXUS(10451, 3),
	TARGET_SET(10452, 3),
	//Savior T3
	RECRUIT(10453, 3),
	DELAYED_HEALING(10454, 3),
	APPEASE(10455, 3),
	RA_ADRENALINE(10456, 3),
	STIMPACK(10457, 3),
	MEDICAL_RAY(10458, 3),
	//Therapist T3
	OINTMENT(10459, 3),
	COMPRESS_BANDAGE(10460, 3),
	ANTIBIOTICS(10461, 3),
	QUICK_PREPARE(10462, 3),
	SPLINT(10463, 3),
	DEFIBRILLATOR(10464, 3),
	//MedicalOfficer T3
	MOVE_CMD(10465, 3),
	STIMPACK_CMD(10466, 3),
	ENGINEER_CMD(10467, 3),
	PROMOTE_CMD(10468, 3),
	EXPLOSION_CMD(10469, 3),
	CAS_CMD(10470, 3),
	//HealGenerator T4
	HEALING_AMP(10471, 4),
	SHIELD_GEN(10472, 4),
	DURABLE_GEN(10473, 4),
	//AngelWing T4
	LIGHT_LIKE_FEATHER(10474, 4),
	ANGELS_BLESS(10475, 4),
	HEALING_WING(10476, 4),
	//GammaRayEmmit T4
	TRANSMOG_BIAS(10477, 4),
	IMPRINTING_EFFECT(10478, 4),
	SHEEP_TRANSMOG(10479, 4),
	//Archer T1
	FORCE_SAVING(10484),
	ARCHERS_INTUITION(10485),
	LEG_SWEEP(10486),
	SURVIVAL_TECHNIQUE(10487),
	DEXTERITY(10488),
	//Archer T2
	FIGHTING_MEAL(10489),
	FULLY_POTION(10490),
	NATURE_FRIENDLY(10491),
	PUSHBACK(10492),
	ARCHERS_FORESIGHT(10493),
	POWERFUL_CRIT(10494),
	//Archer T3
	MAKESHIFT_BOW(10495, 3),
	FOLLOWUP_SHOOT(10496, 3),
	//BowMaster T3
	FASTSHOT(10497, 3),
	EXPANDED_POWER(10498, 3),
	MOVING_FOCUS(10499, 3),
	BOTTLE_EXPANSION(10500, 3),
	SPECTRE_ARROW(10501, 3),
	UNENCUMBERED_STEP(10502, 3),
	//Juggler T3
	HABITUAL_HAND(10503, 3),
	SWIFT_JUGGLING(10504, 3),
	SKILLFUL_JUGGLING(10505, 3),
	FOCUS_MAINTAIN(10506, 3),
	FANCY_PERFORMANCE(10507, 3),
	TOUR_PERFORMANCE(10508, 3),
	//SharpShooter T3
	HURRICANE(10509, 3),
	PERFECT_SHOT(10510, 3),
	FOCUS_SHOT(10511, 3),
	RA_BULLSEYE(10512, 3),
	RANGED_LETHALITY(10513, 3),
	CHANNELING(10514, 3),
	//Dash T4
	LONGRANGE_DASH(10515, 4),
	DUST_SPREAD(10516, 4),
	KINETIC_DASH(10517, 4),
	//Hunt T4
	CHASE(10518, 4),
	CHILL_BACK(10519, 4),
	CANNOT_ESCAPE(10520, 4),
	//Snipe T4
	ENHANCED_ARROW(10521, 4),
	ACCURATE_SNIPE(10522, 4),
	KNOCKING_ARROW(10523, 4),
	//universal T3
	ATK_SPEED_ENHANCE(10528, 4),
	ACC_ENHANCE(10529, 4),
	EVA_ENHANCE(10530, 4),
	BETTER_CHOICE(10531, 3),
	//transformation
	//Transformation PD's common, negative and Freeman talent catalog. Icons are 4096 plus the mod's cell, drawn
	//from interfaces/transformation/talent_icons.png. Hooks are in TransformationTalents.
	ELITE_RECRUIT(4603, 4, 4, TalentType.ARMOR), PRIM_ACCU(4604, 4, 4, TalentType.ARMOR), SOUL_CONTRACT(4605, 4, 4, TalentType.ARMOR), ACTIVE_MUSCLES(4360, 3, 3, TalentType.ASSIST),
	AFRAID_DEATH(4328, 3, 3, TalentType.ASSIST), AUTO_PICK(4480, 2, 1, TalentType.ASSIST), BEHEST(4274, 3, 3, TalentType.ASSIST), BEYOND_LIMIT(4397, 3, 3, TalentType.ASSIST),
	BOMB_MANIAC(4293, 2, 1, TalentType.ASSIST), BURNING_BLOOD(4124, 2, 2, TalentType.ASSIST), CONCEPT_GRID(4352, 3, 3, TalentType.ASSIST), CRAZY_DANCER(4507, 2, 1, TalentType.ASSIST),
	DROP_RESISTANT(4302, 2, 1, TalentType.ASSIST), EXPLORATION_INTUITION(4391, 2, 2, TalentType.ASSIST), EXTREME_REACTION(4419, 3, 3, TalentType.ASSIST), GOD_LEFTHAND(4310, 2, 2, TalentType.ASSIST),
	GOD_RIGHTHAND(4315, 2, 2, TalentType.ASSIST), HEAVY_APPLICATION(4291, 2, 2, TalentType.ASSIST), HERO_NAME(4331, 3, 3, TalentType.ASSIST), HOLY_FAITH(4429, 3, 3, TalentType.ASSIST),
	HOMETOWN_CLOUD(4337, 3, 3, TalentType.ASSIST), LIGHT_APPLICATION(4290, 2, 2, TalentType.ASSIST), NIRVANA(4478, 2, 2, TalentType.ASSIST), POTENTIAL_ENERGY(4477, 2, 1, TalentType.ASSIST),
	PROTECT_CURSE(4476, 2, 2, TalentType.ASSIST), SAVAGE_PHYSIQUE(4319, 2, 1, TalentType.ASSIST), SEA_WIND(4361, 3, 3, TalentType.ASSIST), SPIDER_SENSE(4539, 2, 1, TalentType.ASSIST),
	STRENGTH_TRAIN(4303, 2, 1, TalentType.ASSIST), THICKENED_ARMOR(4298, 2, 1, TalentType.ASSIST), WIDE_KNOWLEDGE(4342, 3, 3, TalentType.ASSIST), ABACUS(4353, 3, 3, TalentType.ATTACK),
	ACCUMULATE_STEADILY(4126, 3, 3, TalentType.ATTACK), AMAZING_EYESIGHT(4289, 2, 2, TalentType.ATTACK), ASHES_BOW(4447, 3, 3, TalentType.ATTACK), ATTACK_DOOR(4223, 2, 1, TalentType.ATTACK),
	CICADA_DANCE(4424, 3, 3, TalentType.ATTACK), COUNTER_ATTACK(4272, 3, 3, TalentType.ATTACK), COVER_SCAR(4305, 2, 1, TalentType.ATTACK), DEEP_FREEZE(4338, 3, 3, TalentType.ATTACK),
	DISTURB_ATTACK(4282, 2, 1, TalentType.ATTACK), FEAR_INCARNATION(4273, 2, 1, TalentType.ATTACK), FRENZIED_ATTACK(4339, 2, 2, TalentType.ATTACK), FUDI_CHOUXIN(4392, 2, 2, TalentType.ATTACK),
	GHOLL_WITCHCRAFT(4283, 2, 2, TalentType.ATTACK), GIANT_KILLER(4156, 2, 2, TalentType.ATTACK), HAND_DESTRUCTION(4482, 3, 3, TalentType.ATTACK), HEDONISM(4396, 2, 2, TalentType.ATTACK),
	JUSTICE_PUNISH(4277, 2, 2, TalentType.ATTACK), LOVE_BACKSTAB(4351, 3, 3, TalentType.ATTACK), MARTIAL_TRAIN(4297, 3, 3, TalentType.ATTACK), OVERWHELMING(4278, 3, 3, TalentType.ATTACK),
	POISON_INBODY(4393, 2, 2, TalentType.ATTACK), POSION_DAGGER(4190, 2, 1, TalentType.ATTACK), PRECISE_SHOT(4390, 3, 3, TalentType.ATTACK), RAGE_ATTACK(4307, 3, 3, TalentType.ATTACK),
	SKY_EARTH(4332, 3, 3, TalentType.ATTACK), STRONG_ATTACK(4268, 2, 1, TalentType.ATTACK), STRONG_THROW(4271, 2, 2, TalentType.ATTACK), SWEET_SLEEP(4617, 2, 2, TalentType.ATTACK),
	THROWING_RECYCLING(4508, 3, 3, TalentType.ATTACK), WATER_ATTACK(4287, 2, 1, TalentType.ATTACK), WEIRD_THROW(4285, 2, 2, TalentType.ATTACK), WELLFED_MEAL(4219, 3, 3, TalentType.ATTACK),
	AQUATIC_RECOVER(4256, 2, 2, TalentType.BOSS), BOSS_TALENT_SLOT_1(4626, 1, 1, TalentType.BOSS), BOSS_TALENT_SLOT_2(4626, 1, 1, TalentType.BOSS), BOSS_TALENT_SLOT_3(4626, 1, 2, TalentType.BOSS),
	BOSS_TALENT_SLOT_4(4626, 1, 2, TalentType.BOSS), BOSS_TALENT_SLOT_5(4626, 1, 3, TalentType.BOSS), BREAK_ENEMY_RANKS(4494, 2, 1, TalentType.BOSS), COMBO_PACKAGE(4493, 2, 1, TalentType.BOSS),
	FASTING(4264, 2, 2, TalentType.BOSS), KILL_SPREE(4609, 2, 1, TalentType.BOSS), KING_PROTECT(4265, 2, 2, TalentType.BOSS), OOZE_ATTACK(4258, 2, 2, TalentType.BOSS),
	PHANTOM_STEP(4611, 2, 1, TalentType.BOSS), PUMP_ATTACK(4257, 2, 2, TalentType.BOSS), RUSH(4261, 2, 1, TalentType.BOSS), SEAOFPEOPLE(4610, 2, 1, TalentType.BOSS),
	SHADOW_KILLER(4608, 2, 1, TalentType.BOSS), STRONGEST_SHIELD(4492, 2, 1, TalentType.BOSS), SUMMON_FOLLOWER(4266, 2, 2, TalentType.BOSS), SURPRISE_THROW(4259, 2, 1, TalentType.BOSS),
	THUNDER_STRIKE(4262, 2, 2, TalentType.BOSS), WOLFISH_GAZE(4267, 2, 2, TalentType.BOSS), YOG_FIST(4326, 3, 3, TalentType.BOSS), YOG_LARVA(4325, 3, 3, TalentType.BOSS),
	YOG_RAY(4327, 3, 3, TalentType.BOSS), AID_STOMACH(4255, 2, 1, TalentType.EFFECT), ANESTHESIA(4358, 2, 1, TalentType.EFFECT), ASH_LEDGER(4346, 2, 1, TalentType.EFFECT),
	BACKFIRED(4417, 2, 2, TalentType.EFFECT), BLESS_MEAL(4292, 2, 2, TalentType.EFFECT), BOOZY(4619, 2, 1, TalentType.EFFECT), COLLECTION_GOLD(4296, 2, 1, TalentType.EFFECT),
	DELICIOUS_FLYING(4334, 2, 2, TalentType.EFFECT), DETOX_DAMAGE(4252, 3, 3, TalentType.EFFECT), EARTH_MEAL(4322, 3, 3, TalentType.EFFECT), EATEN_SLOWLY(4323, 2, 1, TalentType.EFFECT),
	ENDLESS_MEAL(4362, 3, 3, TalentType.EFFECT), FLAME_INCARNATION(4510, 3, 3, TalentType.EFFECT), FLASH_GENIUS(4485, 2, 1, TalentType.EFFECT), GET_UP(4127, 2, 2, TalentType.EFFECT),
	HEALTHY_FOOD(4398, 3, 3, TalentType.EFFECT), ICE_MEAL(4542, 3, 3, TalentType.EFFECT), INSERT_BID(4300, 2, 1, TalentType.EFFECT), INVINCIBLE(4324, 2, 2, TalentType.EFFECT),
	INVINCIBLE_MEAL(4191, 3, 3, TalentType.EFFECT), JASMINE_TEA(4309, 2, 1, TalentType.EFFECT), MEAL_SHIELD(4301, 2, 1, TalentType.EFFECT), NO_VIEWRAPE(4344, 3, 3, TalentType.EFFECT),
	NURTRITIOUS_MEAL(4306, 2, 1, TalentType.EFFECT), OVERLOAD_CHARGE(4624, 2, 2, TalentType.EFFECT), PERSONAL_ATTACK(4427, 2, 1, TalentType.EFFECT), POWERFUL_CALCULATIONS(4299, 2, 1, TalentType.EFFECT),
	READ_PROFITABLE(4356, 3, 3, TalentType.EFFECT), REVERSE_POLARITY(4330, 3, 3, TalentType.EFFECT), SECRET_LIGHTING(4347, 2, 1, TalentType.EFFECT), SHOCK_BOMB(4220, 2, 1, TalentType.EFFECT),
	SURVIVAL_VOLITION(4269, 2, 2, TalentType.EFFECT), THORNY_ROSE(4336, 2, 1, TalentType.EFFECT), TREAT_MEAL(4304, 2, 1, TalentType.EFFECT), WANLING_POTION(4359, 3, 3, TalentType.EFFECT),
	WATER_ISFOOD(4333, 3, 3, TalentType.EFFECT), WITCH_POTION(4486, 2, 2, TalentType.EFFECT), WORD_STUN(4159, 2, 2, TalentType.EFFECT), ANGEL_STANCE(4335, 3, 3, TalentType.MAGIC),
	BURNING_CURSE(4275, 2, 2, TalentType.MAGIC), DAMAGED_CORE(4355, 2, 1, TalentType.MAGIC), EAT_MIND(4502, 2, 2, TalentType.MAGIC), EXTREME_CASTING(4389, 3, 3, TalentType.MAGIC),
	FIRE_BALL(4484, 2, 1, TalentType.MAGIC), GAS_SPURT(4625, 2, 2, TalentType.MAGIC), ICE_BREAKING(4123, 2, 1, TalentType.MAGIC), INSINUATION(4479, 2, 1, TalentType.MAGIC),
	MAGIC_GIRL(4341, 2, 2, TalentType.MAGIC), MAGIC_RECYCLING(4280, 3, 3, TalentType.MAGIC), MORONITY(4349, 3, 3, TalentType.MAGIC), PENETRATING_CAST(4629, 2, 2, TalentType.MAGIC),
	QUANTUM_HACKING(4423, 2, 2, TalentType.MAGIC), STATIC_LIGHT(4572, 2, 1, TalentType.MAGIC), ALCHEMY_ACCIDENT(4621, 2, 0, TalentType.NEGATIVE), BAT_SERUM(4378, 2, 0, TalentType.NEGATIVE),
	BONE_FIRE(4443, 2, 0, TalentType.NEGATIVE), BURNOUT_CHAMPION(4403, 2, 0, TalentType.NEGATIVE), CHILL_WATER(4386, 2, 0, TalentType.NEGATIVE), COWBOY(4382, 2, 0, TalentType.NEGATIVE),
	DEEP_FEAR(4420, 2, 0, TalentType.NEGATIVE), DISASTER_CURSE(4446, 2, 0, TalentType.NEGATIVE), DUMP_TRUCK(4402, 2, 0, TalentType.NEGATIVE), EATER(4372, 2, 0, TalentType.NEGATIVE),
	ENDLESS_MALICE(4374, 2, 0, TalentType.NEGATIVE), EXPLOSION_MEAL(4384, 2, 0, TalentType.NEGATIVE), FAST_DIE(4411, 2, 0, TalentType.NEGATIVE), FATE_DECISION(4373, 2, 0, TalentType.NEGATIVE),
	FEEBLE(4375, 2, 0, TalentType.NEGATIVE), FIRE_WOOD(4407, 2, 0, TalentType.NEGATIVE), FULLFIGHTING(4415, 2, 0, TalentType.NEGATIVE), FULLPASSION(4414, 2, 0, TalentType.NEGATIVE),
	GRASS_MOB(4445, 2, 0, TalentType.NEGATIVE), HANDON_GROUND(4385, 2, 0, TalentType.NEGATIVE), HAND_SLIP(4481, 2, 0, TalentType.NEGATIVE), HUNGRY_GHOST(4574, 2, 0, TalentType.NEGATIVE),
	JIULONGLA_COFFIN(4381, 2, 0, TalentType.NEGATIVE), JUMP_FACE_SILICONE(4444, 2, 0, TalentType.NEGATIVE), LAND_SWIMMING(4380, 2, 0, TalentType.NEGATIVE), LIFE_SPORT(4387, 2, 0, TalentType.NEGATIVE),
	MALNUTRITION(4376, 2, 0, TalentType.NEGATIVE), MAL_CURSE(4623, 2, 0, TalentType.NEGATIVE), MAMBA_OUT(4383, 2, 0, TalentType.NEGATIVE), MENTAL_COLLAPSE(4399, 2, 0, TalentType.NEGATIVE),
	MYOPIA(4379, 2, 0, TalentType.NEGATIVE), OUTCONTROL_MAGIC(4405, 2, 0, TalentType.NEGATIVE), PARASITISM(4401, 2, 0, TalentType.NEGATIVE), PHOTOPHOBY(4404, 2, 0, TalentType.NEGATIVE),
	SHELL_CRY(4622, 2, 0, TalentType.NEGATIVE), SHORTSIGHTED(4377, 2, 0, TalentType.NEGATIVE), STEAM_BEAN(4620, 2, 0, TalentType.NEGATIVE), THORNS_SPRANG(4413, 2, 0, TalentType.NEGATIVE),
	UNABLE_REST(4575, 2, 0, TalentType.NEGATIVE), UNAVOIDABLE(4400, 2, 0, TalentType.NEGATIVE), UNBEAR_HUNGER(4409, 2, 0, TalentType.NEGATIVE), UPDRAFT(4422, 2, 0, TalentType.NEGATIVE),
	USURY(4612, 2, 0, TalentType.NEGATIVE), VIP_MEAL(4410, 2, 0, TalentType.NEGATIVE), WASH_HAND(4412, 2, 0, TalentType.NEGATIVE), WEAKEN_CHALICE(4388, 2, 0, TalentType.NEGATIVE),
	WINTER_SWIMMING(4406, 2, 0, TalentType.NEGATIVE), CHOCOLATE_COINS(4425, 2, 1, TalentType.OTHER), ENGINEER_REFIT(4284, 2, 2, TalentType.OTHER), FISHING_TIME(4189, 2, 1, TalentType.OTHER),
	POTENTIAL_1(4369, 2, 1, TalentType.OTHER), POTENTIAL_2(4369, 2, 1, TalentType.OTHER), POTENTIAL_3(4369, 2, 2, TalentType.OTHER), POTENTIAL_4(4369, 2, 2, TalentType.OTHER),
	POTENTIAL_5(4369, 2, 2, TalentType.OTHER), POTENTIAL_6(4370, 3, 3, TalentType.OTHER), POTENTIAL_7(4370, 3, 3, TalentType.OTHER), POTENTIAL_8(4370, 3, 3, TalentType.OTHER),
	POTENTIAL_9(4370, 3, 3, TalentType.OTHER), SHARP_HEAD(4366, 3, 3, TalentType.OTHER), SHOOT_SATELLITE(4428, 2, 2, TalentType.OTHER), WAKE_SNAKE(4295, 2, 2, TalentType.OTHER),
	WATER_GHOST(4345, 2, 1, TalentType.OTHER), BIRTHDAY_GIFT(4364, 3, 3, TalentType.RESOURCE), CHANGQI_BOOKSTORE(4432, 3, 3, TalentType.RESOURCE), COLLECT_PLANTS(4365, 3, 3, TalentType.RESOURCE),
	DOUBLE_TRINKETS(4188, 3, 3, TalentType.RESOURCE), DUNGEON_HERO(4614, 2, 1, TalentType.RESOURCE), EXPERIENCE_MEAL(4363, 2, 1, TalentType.RESOURCE), EXP_SOLID(4615, 2, 2, TalentType.RESOURCE),
	FAST_BREAK(4506, 2, 2, TalentType.RESOURCE), GHOST_GIFT(4426, 2, 1, TalentType.RESOURCE), GOLDOFBOOK(4418, 2, 1, TalentType.RESOURCE), GOLD_FORMATION(4316, 3, 3, TalentType.RESOURCE),
	GOLD_MEAL(4343, 2, 1, TalentType.RESOURCE), ILLUSION_FEED(4221, 2, 1, TalentType.RESOURCE), INSTANT_REFINING(4431, 2, 2, TalentType.RESOURCE), INVISIBILITY_SHADOWS(4276, 2, 1, TalentType.RESOURCE),
	MILITARY_WATERSKIN(4308, 2, 1, TalentType.RESOURCE), MIRACLE_ALCHEMY(4573, 2, 2, TalentType.RESOURCE), MORE_CHANCE(4288, 2, 2, TalentType.RESOURCE), MORE_TALENT(4155, 2, 1, TalentType.RESOURCE),
	NOVICE_BENEFITS(4187, 2, 1, TalentType.RESOURCE), PRECIOUS_EXPERIENCE(4281, 2, 2, TalentType.RESOURCE), PREDICTIVE_LOVER(4483, 2, 1, TalentType.RESOURCE), PYROMANIAC(4329, 3, 3, TalentType.RESOURCE),
	RETURNING_HONOR(4253, 3, 3, TalentType.RESOURCE), SECRET_STASH(4321, 3, 3, TalentType.RESOURCE), SEED_RECYCLING(4395, 2, 2, TalentType.RESOURCE), THRID_HAND(4286, 2, 1, TalentType.RESOURCE),
	TREASURE_SENSE(4394, 3, 3, TalentType.RESOURCE), WANT_ALL(4157, 2, 2, TalentType.RESOURCE), WEALTH_STATUE(4616, 3, 3, TalentType.RESOURCE), WEAPON_MAKE(4320, 3, 3, TalentType.RESOURCE),
	ZHUOJUN_BUTCHER(4254, 2, 1, TalentType.RESOURCE), ASCENSION_CURSE(4433, 2, 1, TalentType.SPELL), BLADE_STAR(4497, 3, 3, TalentType.SPELL), CONVERSION_HOLY(4436, 2, 2, TalentType.SPELL),
	CREDULOUS(4628, 2, 1, TalentType.SPELL), DIVINE_PROTECTION(4434, 2, 2, TalentType.SPELL), DIVINE_STORM(4438, 3, 3, TalentType.SPELL), EQUIPMENT_BLESS(4499, 2, 2, TalentType.SPELL),
	FOCUS_LIGHT(4489, 2, 1, TalentType.SPELL), GENESIS(4439, 2, 2, TalentType.SPELL), HOLY_GRENADE(4496, 3, 3, TalentType.SPELL), HOTLIGHT(4500, 2, 2, TalentType.SPELL),
	INDULGENCE(4440, 2, 2, TalentType.SPELL), PURIFYING_EVIL(4437, 3, 3, TalentType.SPELL), RESURRECTION(4442, 3, 3, TalentType.SPELL), REVELATION(4495, 2, 2, TalentType.SPELL),
	SACRED_BLADE(4498, 3, 3, TalentType.SPELL), SHEPHERD_INTENTION(4435, 2, 1, TalentType.SPELL), SILVER_LANGUAGE(4441, 2, 1, TalentType.SPELL), SPRINT_SPELL(4488, 2, 2, TalentType.SPELL),
	STRENGTHEN_CHALICE(4318, 2, 2, TalentType.SPELL), THORN_WHIP(4491, 2, 2, TalentType.SPELL), TRAITOROUS_SPELL(4487, 2, 1, TalentType.SPELL), ZHUANYU_SPELL(4490, 3, 3, TalentType.SPELL),
	ARCANE_CONFLUENCE(4775, 3, 3, TalentType.SUBCLASS), COMBO_FOCUS(4771, 3, 3, TalentType.SUBCLASS), FINE_TASTING(4777, 3, 3, TalentType.SUBCLASS), INNER_PEACE(4796, 3, 3, TalentType.SUBCLASS),
	LONG_ARM(4774, 3, 3, TalentType.SUBCLASS), RELENTLESS_COMBAT(4772, 3, 3, TalentType.SUBCLASS), SKILLED_PARRY(4793, 3, 3, TalentType.SUBCLASS), WEAPON_ABILITY_MASTER(4792, 3, 3, TalentType.SUBCLASS),

	//rearranged, Re-ARranged PD's rework of Shattered's six heroes, only on their trees with the Re-ARranged heroes option.
	//Icons are 10000 plus the cell on its sheet. ENDURANCE, COUNTER_ATTACK and RESURRECTION were taken, they are RA_ here
	//Warrior T1
	MAX_HEALTH(10004),
	//Warrior T2
	PARRY(10010),
	//Berserker T3
	LETHAL_RAGE(10016, 3),
	MAX_RAGE(10017, 3),
	RA_ENDURANCE(10018, 3),
	//Gladiator T3
	LIGHT_WEAPON(10022, 3),
	OFFENSIVE_DEFENSE(10023, 3),
	SKILL_REPEAT(10024, 3),
	//Veteran T3
	POWERFUL_TACKLE(10025, 3),
	MYSTICAL_TACKLE(10026, 3),
	DELAYED_GRENADE(10027, 3),
	INCAPACITATION(10028, 3),
	SUPER_ARMOR(10029, 3),
	IMPROVED_TACKLE(10030, 3),
	//Mage T1
	CHARGE_PRESERVE(10048),
	//Mage T2
	FASTER_CHARGER(10054),
	//Battlemage T3
	BATTLE_MAGIC(10060, 3),
	MAGIC_RUSH(10061, 3),
	MAGICAL_CIRCLE(10062, 3),
	//Warlock T3
	MADNESS(10066, 3),
	ENHANCED_MARK(10067, 3),
	MARK_OF_WEAKNESS(10068, 3),
	//Wizard T3
	SPELL_ENHANCE(10069, 3),
	BRIG_BOOST(10070, 3),
	ENERGY_REMAINS(10071, 3),
	MAGIC_EMPOWER(10072, 3),
	SECOND_EFFECT(10073, 3),
	LIFE_ENERGY(10074, 3),
	//Rogue T1
	EMERGENCY_ESCAPE(10092),
	//Rogue T2
	MOVESPEED_ENHANCE(10098),
	//Assassin T3
	ENERGY_DRAW(10104, 3),
	PERFECT_ASSASSIN(10105, 3),
	CAUTIOUS_PREP(10106, 3),
	//Freerunner T3
	QUICK_PREP(10110, 3),
	OVERCOMING(10111, 3),
	MOMENTARY_FOCUSING(10112, 3),
	//Chaser T3
	POISONOUS_BLADE(10113, 3),
	LETHAL_SURPRISE(10114, 3),
	CHAIN_CLOCK(10115, 3),
	SOUL_COLLECT(10116, 3),
	TRAIL_TRACKING(10117, 3),
	MASTER_OF_CLOAKING(10118, 3),
	//Huntress T1
	WATER_FRIENDLY(10136),
	//Huntress T2
	ADDED_MEAL(10142),
	//Sniper T3
	KICK(10148, 3),
	SHOOTING_EYES(10149, 3),
	TARGET_SPOTTING(10150, 3),
	//Warden T3
	LIVING_GRASS(10154, 3),
	ATTRACTION(10155, 3),
	HEALING_DEW(10156, 3),
	//Fighter T3
	SWIFT_MOVEMENT(10157, 3),
	LESS_RESIST(10158, 3),
	RING_KNUCKLE(10159, 3),
	MYSTICAL_PUNCH(10160, 3),
	QUICK_STEP(10161, 3),
	RA_COUNTER_ATTACK(10162, 3),
	//Duelist T1
	SKILLED_HAND(10180),
	//Duelist T2
	ACCUMULATION(10186),
	//Champion T3
	FASTER_CHARGE(10192, 3),
	QUICK_FOLLOWUP(10193, 3),
	TWIN_SWORD(10194, 3),
	//Monk T3
	RESTORED_ENERGY(10198, 3),
	ENERGY_BARRIER(10199, 3),
	HARMONY(10200, 3),
	//Fencer T3
	CLAM_STEPS(10201, 3),
	CRITICAL_MOMENTUM(10202, 3),
	KINETIC_MOVEMENT(10203, 3),
	AGGRESSIVE_MOVEMENT(10204, 3),
	UNENCUMBERED_MOVEMENT(10205, 3),
	SOULIZE(10206, 3),
	//Cleric T1
	WARDING_LIGHT(10224),
	//Cleric T2
	DIVINE_BLAST(10230),
	//Priest T3
	DIVINE_RAY(10236, 3),
	HOLY_BOMB(10237, 3),
	RA_RESURRECTION(10238, 3),
	//Paladin T3
	HOLY_MANTLE(10242, 3),
	POWER_OF_LIFE(10243, 3),
	INDUCE_AGGRO(10244, 3),
	//Enchanter T3
	TIME_AMP(10245, 3),
	WEAKENING_HEX(10246, 3),
	STUN(10247, 3),
	THUNDER_IMBUE(10248, 3),
	ARCANE_ARMOR(10249, 3),
	ENCHANT(10250, 3),
	//transformation, the talents of the second catalog run
	CEASELESS_RAGE(4768, 3, 3, TalentType.SUBCLASS), MIRRORED_REVENGE(4769, 3, 3, TalentType.SUBCLASS), BLOODTHIRSTY_BERSERK(4770, 3, 3, TalentType.SUBCLASS), COMBO_MASTERY(4773, 3, 3, TalentType.SUBCLASS),
	FOCUSED_CASTING(4776, 3, 3, TalentType.SUBCLASS), BONE_DEEP(4778, 3, 3, TalentType.SUBCLASS), MIND_IMPRISONMENT(4779, 3, 3, TalentType.SUBCLASS), UNEXPECTED_STRIKE(4780, 3, 3, TalentType.SUBCLASS),
	CLOSING_STAGE(4781, 3, 3, TalentType.SUBCLASS), PERFECT_FINALE(4782, 3, 3, TalentType.SUBCLASS), FREERUNNER_AFTERIMAGE(4783, 3, 3, TalentType.SUBCLASS), MOMENTUM_RESERVE(4784, 3, 3, TalentType.SUBCLASS),
	WARMUP_PREPARATION(4785, 3, 3, TalentType.SUBCLASS), ALTERNATING_WEAPONS(4794, 3, 3, TalentType.SUBCLASS), NATURAL_WAY(4795, 3, 3, TalentType.SUBCLASS), YIN_YANG_BALANCE(4797, 3, 3, TalentType.SUBCLASS),
	SMOKE_MASK(4260, 2, 1, TalentType.BOSS), DIRECTIONAL_COLLAPSE(4263, 2, 2, TalentType.BOSS), ENERGY_CONVERSION(4279, 2, 2, TalentType.BOSS), TRAP_MASTER(4270, 3, 3, TalentType.RESOURCE),
	PHANTOM_SHOOTER(4294, 3, 3, TalentType.ATTACK), STRENGTHEN_CHAIN(4317, 2, 2, TalentType.SPELL), HEAVY_BURDEN(4222, 2, 2, TalentType.ASSIST), ARROW_PENETRATION(4251, 2, 2, TalentType.ATTACK),
	WULEI_ZHENGFA(4340, 2, 2, TalentType.MAGIC), ABYSSAL_GAZE(4348, 2, 2, TalentType.MAGIC), JOURNEY_NATURE(4350, 2, 2, TalentType.SPELL), TIME_SAND(4354, 3, 3, TalentType.SPELL),
	STRENGTH_CLOAK(4357, 3, 3, TalentType.SPELL), STRENGTH_ARMBAND(4367, 3, 3, TalentType.SPELL), STRENGTH_BOOK(4368, 2, 2, TalentType.SPELL), ETERNAL_CURSE(4371, 2, 0, TalentType.NEGATIVE),
	BE_INCONSTANT(4408, 2, 0, TalentType.NEGATIVE), PHASECLAW(4416, 2, 0, TalentType.NEGATIVE), NO_DOOR(4421, 2, 0, TalentType.NEGATIVE), HONEY_FISH(4430, 2, 1, TalentType.OTHER),
	SWIFT_CHURCH(4475, 3, 3, TalentType.MAGIC), HEALATTACK(4501, 2, 1, TalentType.SPELL), DISABLIITY_POSION(4503, 3, 3, TalentType.MAGIC), LIGHT_CROP(4504, 2, 1, TalentType.MAGIC),
	DEVIL_FLAME(4505, 3, 3, TalentType.MAGIC), MARKSMAN(4511, 2, 1, TalentType.MAGIC), NO_MORE_MOB(4540, 2, 1, TalentType.OTHER), KEBI(4541, 2, 2, TalentType.RESOURCE),
	MORE_FAVORS(4543, 2, 1, TalentType.RESOURCE), HARDWARE_ELEMENTS(4571, 3, 3, TalentType.RESOURCE), ARMED_UPRISING(4613, 2, 1, TalentType.ATTACK), LIGHT_SHOCK(4618, 2, 2, TalentType.MAGIC),
	ERODING_SOUL(4627, 2, 1, TalentType.MAGIC), BOUNTIFUL_ENHANCEMENT(4630, 3, 3, TalentType.SPELL), ICE_HELL(4631, 3, 3, TalentType.MAGIC), HUNTING_TECHNIQUE(4699, 2, 2, TalentType.BOSS),
	NATURAL_CHILD(4700, 2, 2, TalentType.BOSS), FALCON_EYE(4701, 2, 2, TalentType.BOSS),

	//xun, the talents of Xun's PD's Explorer, Wraith and Engineer. Icons are 40000 plus the cell on
	//interfaces/xun/talent_icons.png, hooks are in XunTalents
	//Explorer T1
	KEEN_MEAL(40288), SECRET_FORESIGHT(40289), TESTED_AWARENESS(40290), HOME_ADVANTAGE(40291), SAFE_SURVEY(40292),
	//Explorer T2
	PREPARING_MEAL(40293), LIQUID_CLAIRVOYANCE(40294), BARBED_WIRE(40295), WINDING_PORCH(40296), REKINDLED_EMBER(40297), AGGRESSIVE_ROADBLOCK(40298),
	//Explorer T3
	DEMOLITION(40299, 3), DIG_THE_WELL(40300, 3), CONVENIENT_SHOVEL(40301, 3),
	//WaveChaser T3
	RIVER_EROSION(40302, 3), SON_OF_SEA(40303, 3), DROWNING(40304, 3), LAKE_DEVELOPMENT(40305, 3), UNDERCURRENT(40306, 3),
	//Trapper T3
	XUN_TRAP_MASTER(40307, 3), FRIENDLY_MECHANISM(40308, 3), SIMPLE_STRUCTURE(40309, 3), SENSITIVE_PEDAL(40310, 3), LIQUID_COLLECTING(40311, 3),
	//Rocksy T3
	METEROIC_IRON(40312, 3), ROCK_PROTECTOR(40313, 3), MIND_CONTROL(40314, 3), DESTRUCTIVE_STRIKE(40315, 3), METEOR_CRATER(40316, 3),
	//Optical Camouflage T4
	LASTING_DISGUISE(40317, 4), STRAIN_CAPACITY(40318, 4), STANDBY(40319, 4), ENERGY_SURPLUS(40320, 4),
	//Sandstorm T4
	GLOOM_ABOVE(40321, 4), SAND_FLOW(40322, 4), HEART_OF_STORM(40323, 4), DRIFT_SAND(40324, 4),
	//Underpass T4
	EXPRESS_UNDERWAY(40325, 4), MONITOR_BENEATH(40326, 4), SLY_RABBIT(40327, 4), GRENADE_COVER(40328, 4),

	//Wraith T1
	ANCESTRAL_TRIBUTE(40336), BLOOD_INTUITION(40337), TESTED_ANTIMAGIC(40338), BURIAL_CEREMONY(40339), SAFE_PRICK(40340),
	//Wraith T2
	TEARING_MEAL(40341), INSCRIBED_REGENERATION(40342), BLURING_BODY(40343), PSIONIC_BLAST(40344), SCAPEGOAT(40345), THROWN_EVIL(40346),
	//Wraith T3
	VICIOUS_BETRAYAL(40347, 3), CURSED_POWER(40348, 3), WICKED_GROWTH(40349, 3),
	//Incubus T3
	LULLABY(40350, 3), SLEEPWALKING(40351, 3), SLEEPING_IN(40352, 3), WRONG_SIDE_OF_THE_BED(40353, 3), NIGHTMARE_HAUNTING(40354, 3),
	//PlagueGod T3
	CORPSE_DECAY(40355, 3), HOMEMADE_DRUG(40356, 3), MAGICAL_VENT(40357, 3), ACID_RAIN(40358, 3), PLAGUE_EUCHARIST(40359, 3),
	//SoulHandler T3
	IMMEDIATE_USE(40360, 3), VENGEFUL_SPIRIT(40361, 3), SOUL_CAGING(40362, 3), DEVOUR_SURGING(40363, 3), OVERFLOW(40364, 3),
	//Transfusion T4
	EMERGENCY_TRANSFUSION(40365, 4), BLOOD_INFECTION(40366, 4), BIOLOGICAL_CONTROL(40367, 4), HEMATOPOIESIS(40368, 4),
	//EvilUnfold T4
	BUFFED_NERF(40369, 4), XUN_ARMY_OF_DEATH(40370, 4), STRANGLING(40371, 4), IMMORTAL_EVIL(40372, 4),
	//GhostWander T4
	FACE_TO_FACE_FRIGHT(40373, 4), SOUL_VANISHING(40374, 4), FEAR_SPREADING(40375, 4), SOULFREE_GHOST(40376, 4),

	//Engineer T1
	EAT_LITTLE_AND_OFTEN(40384), FINE_INTUITION(40385), TESTED_MAINTENANCE(40386), GENERAL_DISARM(40387), PULSE_ENERGY(40388),
	//Engineer T2
	TOILSOME_MEAL(40389), IONIC_LIQUID(40390), STRONG_PULSE(40391), RESONANT_SENSING(40392), APART_ANYTHING(40393), REMOTE_DESTRUCTION(40394),
	//Engineer T3
	DURABLE_MODIFIES(40395, 3), ELECTRONIC_REPAIR(40396, 3), IONIZING_RADIATION(40397, 3),
	//Craftsman T3
	ACTIVE_REPAIR(40398, 3), MULTI_MODIFY(40399, 3), PART_RECYCLING(40400, 3), FAVORITE_WORK(40401, 3), KINETIC_FRAGMENT(40402, 3),
	//Hacker T3
	BACKFIRE(40403, 3), DIFFRACTION(40404, 3), ELECTRIC_CHARGE(40405, 3), DARK_MAGIC(40406, 3), CHARISMA(40407, 3),
	//Grenadier T4
	EXPLOSION_PROOF(40408, 3), XUN_SHOCKWAVE(40409, 3), ADDED_POWDER(40410, 3), MAGICAL_EXPLOSION(40411, 3), BALLISTICA_CALC(40412, 3),
	//ForceField T4
	MECHANICAL_REFINEMENT(40413, 4), MOMENTUM_TRANSFORM(40414, 4), ACTIVE_DEFENSE(40415, 4), REPAIR_ABILITY(40416, 4),
	//Summoning Beacon T4
	POWERED_DEVICE(40417, 4), SKYNET(40418, 4), VIOLENT_LEAP(40419, 4), ASSAULT(40420, 4),
	//Detonator T4
	STACKED_CHARGE(40421, 4), ALCHEMY_CRAFT(40422, 4), DECOY(40423, 4), PORTABLE_DETONATOR(40424, 4);

	public static class ImprovisedProjectileCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0.15f, 0.2f, 0.5f); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 50); }
	};
	public static class LethalMomentumTracker extends FlavourBuff{};
	public static class StrikingWaveTracker extends FlavourBuff{};
	public static class WandPreservationCounter extends CounterBuff{{revivePersists = true;}};
	public static class EmpoweredStrikeTracker extends FlavourBuff{
		//blast wave on-hit doesn't resolve instantly, so we delay detaching for it
		public boolean delayedDetach = false;
	};
	public static class ProtectiveShadowsTracker extends Buff {
		float barrierInc = 0.5f;

		@Override
		public boolean act() {
			//barrier every 2/1 turns, to a max of 3/5
			if (((Hero)target).hasTalent(Talent.PROTECTIVE_SHADOWS) && target.invisible > 0){
				Barrier barrier = Buff.affect(target, Barrier.class);
				if (barrier.shielding() < 1 + 2*((Hero)target).pointsInTalent(Talent.PROTECTIVE_SHADOWS)) {
					barrierInc += 0.5f * ((Hero) target).pointsInTalent(Talent.PROTECTIVE_SHADOWS);
				}
				if (barrierInc >= 1){
					barrierInc = 0;
					barrier.incShield(1);
				} else {
					barrier.incShield(0); //resets barrier decay
				}
			} else {
				detach();
			}
			spend( TICK );
			return true;
		}

		private static final String BARRIER_INC = "barrier_inc";
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put( BARRIER_INC, barrierInc);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			barrierInc = bundle.getFloat( BARRIER_INC );
		}
	}
	public static class BountyHunterTracker extends FlavourBuff{};
	//Pixel Dungeon Reforged's Artificer
	public static class ArtificerFoodDamageBonus extends FlavourBuff {
		{ actPriority = HERO_PRIO + 1; }
	}
	public static class ArtificerFoodEvasionBonus extends FlavourBuff {
		{ actPriority = HERO_PRIO + 1; }
	}
	public static class VolatileChainTracker extends FlavourBuff{
		public int object;
		{ type = Buff.buffType.POSITIVE; }
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0f, 0.75f, 1f); }
		public float iconFadePercent() { return Math.max(0, 1f - (visualcooldown() / 5)); }
		private static final String OBJECT    = "object";
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(OBJECT, object);
		}
		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			object = bundle.getInt(OBJECT);
		}
	}
	//Devoted PD's Zealot, after stabbing themselves the next melee hit makes the enemy bleed
	public static class UnholyWrathTracker extends FlavourBuff{
		{ type = Buff.buffType.POSITIVE; }
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0.8f, 0f, 0f); }
		public float iconFadePercent() { return Math.max(0, 1f - (visualcooldown() / 20)); }
	};
	public static class RejuvenatingStepsCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0f, 0.35f, 0.15f); }
		public float iconFadePercent() { return GameMath.gate(0, visualcooldown() / (15 - 5*Dungeon.hero.pointsInTalent(REJUVENATING_STEPS)), 1); }
	};
	public static class RejuvenatingStepsFurrow extends CounterBuff{{revivePersists = true;}};
	public static class SeerShotCooldown extends FlavourBuff{
		public int icon() { return target.buff(RevealedArea.class) != null ? BuffIndicator.NONE : BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0.7f, 0.4f, 0.7f); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 20); }
	};
	public static class SpiritBladesTracker extends FlavourBuff{};
	public static class PatientStrikeTracker extends Buff {
		public int pos;
		{ type = Buff.buffType.POSITIVE; }
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0.5f, 0f, 1f); }
		@Override
		public boolean act() {
			if (pos != target.pos) {
				detach();
			} else {
				spend(TICK);
			}
			return true;
		}
		private static final String POS = "pos";
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(POS, pos);
		}
		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			pos = bundle.getInt(POS);
		}
	};
	public static class AggressiveBarrierCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0.35f, 0f, 0.7f); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 50); }
	};
	public static class LiquidAgilEVATracker extends FlavourBuff{
		{
			//detaches after hero acts, not after mobs act
			actPriority = HERO_PRIO+1;
		}
	};
	public static class LiquidAgilACCTracker extends FlavourBuff{
		public int uses;

		{ type = buffType.POSITIVE; }
		public int icon() { return BuffIndicator.INVERT_MARK; }
		public void tintIcon(Image icon) { icon.hardlight(0.5f, 0f, 1f); }
		public float iconFadePercent() { return Math.max(0, 1f - (visualcooldown() / 5)); }

		private static final String USES = "uses";
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(USES, uses);
		}
		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			uses = bundle.getInt(USES);
		}
	};
	public static class LethalHasteCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0.35f, 0f, 0.7f); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 100); }
	};
	public static class SwiftEquipCooldown extends FlavourBuff{
		public boolean secondUse;
		public boolean hasSecondUse(){
			return secondUse;
		}

		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) {
			if (hasSecondUse()) icon.hardlight(0.85f, 0f, 1.0f);
			else                icon.hardlight(0.35f, 0f, 0.7f);
		}
		public float iconFadePercent() { return GameMath.gate(0, visualcooldown() / 20f, 1); }

		private static final String SECOND_USE = "second_use";
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(SECOND_USE, secondUse);
		}
		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			secondUse = bundle.getBoolean(SECOND_USE);
		}
	};
	public static class DeadlyFollowupTracker extends FlavourBuff{
		public int object;
		{ type = Buff.buffType.POSITIVE; }
		public int icon() { return BuffIndicator.INVERT_MARK; }
		public void tintIcon(Image icon) { icon.hardlight(0.5f, 0f, 1f); }
		public float iconFadePercent() { return Math.max(0, 1f - (visualcooldown() / 5)); }
		private static final String OBJECT    = "object";
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(OBJECT, object);
		}
		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			object = bundle.getInt(OBJECT);
		}
	}
	public static class PreciseAssaultTracker extends FlavourBuff{
		{ type = buffType.POSITIVE; }
		public int icon() { return BuffIndicator.INVERT_MARK; }
		public void tintIcon(Image icon) { icon.hardlight(1f, 1f, 0.0f); }
		public float iconFadePercent() { return Math.max(0, 1f - (visualcooldown() / 5)); }
	};
	public static class VariedChargeTracker extends Buff{
		public Class weapon;

		private static final String WEAPON    = "weapon";
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(WEAPON, weapon);
		}
		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			weapon = bundle.getClass(WEAPON);
		}
	}
	public static class CombinedLethalityAbilityTracker extends FlavourBuff{
		public MeleeWeapon weapon;
	};
	public static class CombinedEnergyAbilityTracker extends FlavourBuff{
		public boolean monkAbilused = false;
		public boolean wepAbilUsed = false;

		private static final String MONK_ABIL_USED  = "monk_abil_used";
		private static final String WEP_ABIL_USED   = "wep_abil_used";
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(MONK_ABIL_USED, monkAbilused);
			bundle.put(WEP_ABIL_USED, wepAbilUsed);
		}
		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			monkAbilused = bundle.getBoolean(MONK_ABIL_USED);
			wepAbilUsed = bundle.getBoolean(WEP_ABIL_USED);
		}
	}
	public static class CounterAbilityTacker extends FlavourBuff{}
	public static class SatiatedSpellsTracker extends Buff{
		@Override
		public int icon() {
			return BuffIndicator.SPELL_FOOD;
		}
	}
	//used for metamorphed searing light
	public static class SearingLightCooldown extends FlavourBuff{
		@Override
		public int icon() {
			return BuffIndicator.TIME;
		}
		public void tintIcon(Image icon) { icon.hardlight(0f, 0f, 1f); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 20); }
	}

	int icon;
	int maxPoints;

	// tiers 1/2/3/4 start at levels 2/7/13/21
	public static int[] tierLevelThresholds = new int[]{0, 2, 7, 13, 21, 31};

	Talent( int icon ){
		this(icon, 2);
	}

	Talent( int icon, int maxPoints ){
		this.icon = icon;
		this.maxPoints = maxPoints;
	}

	//Transformation PD's catalog talents carry their own tier and kind
	private int fixedTier = -1;
	private TalentType fixedType = null;

	Talent( int icon, int maxPoints, int tier, TalentType type ){
		this(icon, maxPoints);
		this.fixedTier = tier;
		this.fixedType = type;
	}

	public boolean isCatalogTalent(){
		return fixedType != null;
	}

	public int icon(){
		if (this == HEROIC_ENERGY){
			if (Ratmogrify.useRatroicEnergy){
				return 250;
			}
			HeroClass cls = Dungeon.hero != null ? Dungeon.hero.heroClass : GamesInProgress.selectedClass;
			switch (cls){
				case WARRIOR: default:
					return 26;
				//xun, Xun's PD's sheet keeps 48 cells per hero, heroic energy is cell 41 of each
				case EXPLORER: return 40000 + 6*48 + 41;
				case WRAITH:   return 40000 + 7*48 + 41;
				case ENGINEER: return 40000 + 8*48 + 41;
				case MAGE:
					return 58;
				case ROGUE:
					return 90;
				case HUNTRESS:
					return 122;
				case DUELIST:
					return 154;
				case CLERIC:
					return 186;
				case ZEALOT:
					return 218;
				case ARTIFICER:
					return 245;
				case SUMMONER:
					return 26;
				case VIKING:
					return 26;
				case DRAGONKNIGHT:
					return 26;
				case FRIAR:
					return 602;
				case SLIMEGIRL:
					return 442;
				case NINJA:
					return 474;
				case DM400:
					return 506;
				case PRINCESS:
					return 570;
				//rearranged, cell 43 of each hero's row on Re-ARranged PD's sheet
				case GUNNER:     return 10000 + 43 + 44*6;
				case SAMURAI:    return 10000 + 43 + 44*7;
				case ADVENTURER: return 10000 + 43 + 44*8;
				case KNIGHT:     return 10000 + 43 + 44*9;
				case MEDIC:      return 10000 + 43 + 44*10;
				case ARCHER:     return 10000 + 43 + 44*11;
				//summoning
				case CONJURER:
					return 26;
				case FREEMAN:
					return com.shatteredpixel.shatteredpixeldungeon.ui.TalentIcon.TRANSFORMATION_BASE + 510;
			}
		} else {
			return icon;
		}
	}

	public int maxPoints(){
		return maxPoints;
	}

	//---- Transformation PD asks every talent for its tier and kind (the Hunger Knight swallows talents by tier)
	public enum TalentType {
		EFFECT, ASSIST, ATTACK, RESOURCE, MAGIC, SPELL, SUBCLASS, ARMOR, BOSS, OTHER, NEGATIVE;
		public boolean isCommon(){
			return this != SUBCLASS && this != ARMOR && this != BOSS && this != NEGATIVE;
		}
	}

	private static java.util.HashMap<Talent, Integer> tierMap;
	private static java.util.HashSet<Talent> subclassTalentSet;

	private static void buildTierMap(){
		tierMap = new java.util.HashMap<>();
		subclassTalentSet = new java.util.HashSet<>();
		for (HeroClass cls : HeroClass.values()){
			ArrayList<LinkedHashMap<Talent, Integer>> lists = new ArrayList<>();
			initClassTalents(cls, lists);
			for (int i = 0; i < lists.size(); i++){
				for (Talent t : lists.get(i).keySet()) if (!tierMap.containsKey(t)) tierMap.put(t, i + 1);
			}
			for (HeroSubClass sub : cls.subClasses()){
				ArrayList<LinkedHashMap<Talent, Integer>> subLists = new ArrayList<>();
				initSubclassTalents(sub, subLists);
				for (LinkedHashMap<Talent, Integer> m : subLists){
					for (Talent t : m.keySet()){
						if (!tierMap.containsKey(t)) tierMap.put(t, 3);
						subclassTalentSet.add(t);
					}
				}
			}
			for (ArmorAbility a : cls.armorAbilities()){
				for (Talent t : a.talents()) if (!tierMap.containsKey(t)) tierMap.put(t, 4);
			}
		}
	}

	public int tier(){
		if (fixedTier >= 0) return fixedTier;
		if (tierMap == null) buildTierMap();
		Integer v = tierMap.get(this);
		return v == null ? 2 : v;
	}

	public TalentType type(){
		if (fixedType != null) return fixedType;
		if (tierMap == null) buildTierMap();
		if (tier() == 4) return TalentType.ARMOR;
		if (subclassTalentSet.contains(this)) return TalentType.SUBCLASS;
		String n = name();
		if (n.endsWith("_MEAL")) return TalentType.EFFECT;
		if (n.endsWith("_INTUITION")) return TalentType.ASSIST;
		return TalentType.OTHER;
	}

	public boolean isCommonTalentType(){
		return type().isCommon();
	}

	//boss talents (the mod's sublimation slots) are not here
	public static boolean isBossTalent( Talent t ){ return t.type() == TalentType.BOSS && !TransformationBossTalents.isSlot(t); }
	public static boolean isBossTalentPlaceholder( Talent t ){ return TransformationBossTalents.isSlot(t); }

	public String title(){
		if (this == HEROIC_ENERGY && Ratmogrify.useRatroicEnergy){
			return Messages.get(this, name() + ".rat_title");
		}
		return Messages.get(this, name() + ".title");
	}

	public final String desc(){
		return desc(false);
	}

	public String desc(boolean metamorphed){
		String desc = Messages.get(this, name() + ".desc");
		if (metamorphed){
			String metaDesc = Messages.get(this, name() + ".meta_desc");
			if (!metaDesc.equals(Messages.NO_TEXT_FOUND)){
				desc += "\n\n" + metaDesc;
			}
		}
		//Transformation PD's talents carry a short remark
		String comment = Messages.get(this, name() + ".comment");
		if (!comment.equals(Messages.NO_TEXT_FOUND) && !comment.isEmpty()){
			desc += "\n\n" + comment;
		}
		return desc;
	}

	//---- Transformation PD, the Friar ------------------------------------------------------------

	public static class AlchemyShield extends Buff {
		private static final String LAYERS = "layers";
		private static final int MAX_LAYERS = 3;
		{ type = buffType.POSITIVE; }
		private int layers = 0;

		public void gain( int amount ){
			if (amount <= 0) return;
			layers = Math.min(MAX_LAYERS, layers + amount);
			BuffIndicator.refreshHero();
		}

		public int absorb( int damage, Object src ){
			if (damage <= 0 || layers <= 0 || !(src instanceof Char)) return damage;
			if (((Char)src).alignment == target.alignment) return damage;
			int reduction = currentReduction();
			layers--;
			if (layers <= 0) detach(); else BuffIndicator.refreshHero();
			return Math.max(0, damage - reduction);
		}

		private int currentReduction(){ return Math.min(20, 5 + Dungeon.energy); }
		@Override public int icon() { return BuffIndicator.ARMOR; }
		@Override public String iconTextDisplay() { return Integer.toString(layers); }
		@Override public String desc() { return Messages.get(this, "desc", currentReduction(), layers); }
		@Override public void storeInBundle( Bundle bundle ) { super.storeInBundle(bundle); bundle.put(LAYERS, layers); }
		@Override public void restoreFromBundle( Bundle bundle ) { super.restoreFromBundle(bundle); layers = bundle.getInt(LAYERS); }
	}

	public static class DyingWill extends Buff {
		{ type = buffType.POSITIVE; }
		@Override public int icon() { return BuffIndicator.CORRUPT; }
		@Override public void tintIcon(Image icon) { icon.hardlight(0xFF2222); }
		@Override
		public boolean act() {
			if (!hasSufferingOrVirtue(target)){
				detach();
				Buff.affect(target, HeavyWound.class, 100f);
				if (target.sprite != null) target.sprite.showStatus(CharSprite.WARNING, Messages.get(this, "heavy_wound"));
				GLog.w(Messages.get(this, "heavy_wound_log"));
			}
			spend(TICK);
			return true;
		}
	}

	public static class HeavyWound extends FlavourBuff {
		{ type = buffType.NEGATIVE; }
		@Override public int icon() { return BuffIndicator.CORRUPT; }
		@Override public void tintIcon(Image icon) { icon.hardlight(0x555555); }
	}

	public static class RewindTimeUsed extends FlavourBuff {
		{ type = buffType.POSITIVE; }
		@Override public int icon() { return BuffIndicator.TIME; }
		@Override public void tintIcon(Image icon) { icon.hardlight(0.64f, 0.53f, 0.42f); }
		@Override public float iconFadePercent() { return Math.max(0, visualcooldown() / 50); }
	}

	//Transformation PD calls this after every armor ability, boss talents hang off it. None are here yet.
	public static void onArmorAbility( Hero hero, float chargeUse ){
		TransformationBossTalents.onArmorAbility(hero);
	}

	public static boolean canRavensEyeInspect( Hero hero, Item item ){
		return hero != null && item != null && hero.hasTalent(RAVENS_EYE)
				&& (item instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon
					|| item instanceof com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor
					|| item instanceof com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact
					|| item instanceof com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring
					|| item instanceof com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand)
				&& !item.isIdentified() && !item.cursedKnown;
	}

	public static void doRavensEyeInspect( final Hero hero, final Item item ){
		if (!canRavensEyeInspect(hero, item)) return;
		com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene.show(new com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions(
				new com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite(item), RAVENS_EYE.title(),
				Messages.get(Item.class, "eye_prompt"), Messages.get(Item.class, "eye_yes"), Messages.get(Item.class, "eye_no")){
			@Override
			protected void onSelect(int index) {
				if (index == 0) inspectWithRavensEye(hero, item);
			}
		});
	}

	private static void inspectWithRavensEye( Hero hero, Item item ){
		if (!canRavensEyeInspect(hero, item)) return;
		int points = hero.pointsInTalent(RAVENS_EYE);
		if (hero.heroClass == HeroClass.FRIAR){
			int cost = points == 1 ? 30 : 20;
			com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Reason reason = hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Reason.class);
			if (reason == null || reason.reason < cost){
				GLog.w(Messages.get(Item.class, "no_reason"));
				return;
			}
			com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Reason.loseReason(hero, cost);
		} else {
			Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding.class).set(points == 1 ? 6 : 4);
		}
		item.cursedKnown = true;
		Item.updateQuickslot();
		if (item.cursed) GLog.w(Messages.get(Item.class, "cursed")); else GLog.i(Messages.get(Item.class, "uncursed"));
		hero.busy();
		hero.sprite.operate(hero.pos);
		hero.sprite.parent.add( new com.shatteredpixel.shatteredpixeldungeon.effects.Identification( hero.sprite.center().offset( 0, -16 ) ) );
		Sample.INSTANCE.play(Assets.Sounds.READ);
		hero.spendAndNext(1f);
	}

	private static boolean hasBlockingWeapon( Hero hero ){
		com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon w = hero.belongings.weapon();
		return w instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon
				&& ((com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon) w).hasTrait(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon.WeaponTrait.BLOCKING, hero);
	}

	public static int alchemyEnergyCost( Hero hero, int cost ){
		if (hero == null || cost <= 0 || !hero.subClass.is(HeroSubClass.ALCHEMIST)) return cost;
		return Math.max(1, cost - (3 + hero.pointsInTalent(ALCHEMY_SHIELD)));
	}

	public static void onAlchemyEnergyConsumed( Hero hero ){
		if (hero != null && hero.subClass.is(HeroSubClass.ALCHEMIST) && hero.hasTalent(ALCHEMY_SHIELD)){
			Buff.affect(hero, AlchemyShield.class).gain(hero.pointsInTalent(ALCHEMY_SHIELD));
		}
	}

	public static float alchemistCloseBlastMultiplier( Hero hero, int distance, int maxRange ){
		if (hero == null || !hero.subClass.is(HeroSubClass.ALCHEMIST) || !hero.hasTalent(CLOSE_BLAST)) return 1f;
		int closerSteps = Math.min(4, Math.max(0, maxRange - distance));
		return 1f + closerSteps * (0.05f + 0.05f * hero.pointsInTalent(CLOSE_BLAST));
	}

	public static boolean alchemistSprayChargePreserved( Hero hero ){
		if (hero == null || !hero.subClass.is(HeroSubClass.ALCHEMIST) || !hero.hasTalent(CONSERVATION)) return false;
		return Random.Float() < 0.1f * hero.pointsInTalent(CONSERVATION);
	}

	public static boolean alchemistPotionLoadPreserved( Hero hero ){
		if (hero == null || !hero.subClass.is(HeroSubClass.ALCHEMIST) || !hero.hasTalent(CONSERVATION)) return false;
		return Random.Float() < 0.15f * (hero.pointsInTalent(CONSERVATION) + 1);
	}

	public static float bullseyeAccuracyFactor( Hero hero ){
		if (hero != null && hero.hasTalent(BULLSEYE)) return hero.pointsInTalent(BULLSEYE) == 1 ? 1.5f : 1.75f;
		return 1f;
	}

	private static boolean hasSuffering(Char ch){
		return ch.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Suffering.Fear.class) != null
				|| ch.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Suffering.Despair.class) != null
				|| ch.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Suffering.Paranoia.class) != null
				|| ch.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Suffering.Ecstasy.class) != null;
	}

	private static boolean hasSufferingOrVirtue(Char ch){
		return hasSuffering(ch)
				|| ch.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Virtue.Firm.class) != null
				|| ch.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Virtue.Fearless.class) != null
				|| ch.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Virtue.Inspire.class) != null;
	}

	private static boolean triggerDyingWill(Hero hero){
		if (hero == null || !hero.hasTalent(NEVER_COMPROMISE) || !hasSufferingOrVirtue(hero)) return false;
		int shift = 60 - 10 * hero.pointsInTalent(NEVER_COMPROMISE);
		if (hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Suffering.Fear.class) != null) Buff.extend(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Suffering.Fear.class, shift);
		else if (hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Suffering.Despair.class) != null) Buff.extend(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Suffering.Despair.class, shift);
		else if (hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Suffering.Paranoia.class) != null) Buff.extend(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Suffering.Paranoia.class, shift);
		else if (hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Suffering.Ecstasy.class) != null) Buff.extend(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Suffering.Ecstasy.class, shift);
		else if (hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Virtue.Firm.class) != null) Buff.extend(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Virtue.Firm.class, -shift);
		else if (hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Virtue.Fearless.class) != null) Buff.extend(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Virtue.Fearless.class, -shift);
		else if (hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Virtue.Inspire.class) != null) Buff.extend(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Virtue.Inspire.class, -shift);
		Buff.affect(hero, DyingWill.class);
		if (hero.sprite != null) hero.sprite.showStatus(CharSprite.NEGATIVE, Messages.get(DyingWill.class, "name"));
		GLog.n(Messages.get(DyingWill.class, "get"));
		return true;
	}

	public static int beforeSufferingReasonLoss(Hero hero, int lose){
		com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Reason r = hero == null ? null : hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Reason.class);
		if (r == null || !hero.hasTalent(REWIND_TIME) || hero.buff(RewindTimeUsed.class) != null) return lose;
		if (hasSuffering(hero) && r.reason >= 20 && r.reason - lose < 20){
			com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Reason.gainReason(hero, 20 + 10 * hero.pointsInTalent(REWIND_TIME));
			Buff.detach(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Panic.class);
			Buff.affect(hero, RewindTimeUsed.class, 50f);
		}
		return lose;
	}

	//called from Hero.damage for every hit, returns the damage left
	public static int onHeroDamage( Hero hero, int dmg, Object src ){
		dmg = TransformationTalents.onHeroDamage(hero, dmg, src);
		if (hero.hasTalent(BULWARK_GREATSHIELD) && src instanceof Char && hasBlockingWeapon(hero)){
			dmg = Math.round(dmg * (hero.pointsInTalent(BULWARK_GREATSHIELD) == 1 ? 0.90f : 0.85f));
		}
		dmg = onDMDamage(hero, dmg);
		dmg = onPrincessDamage(hero, dmg, src);
		AlchemyShield shield = hero.buff(AlchemyShield.class);
		if (shield != null) dmg = shield.absorb(dmg, src);
		if (hero.subClass.is(HeroSubClass.WATERSLIME)){
			SlimeMucusCooldown mucus = hero.buff(SlimeMucusCooldown.class);
			if (mucus != null) mucus.Reduce();
		}
		if (hero.hasTalent(ENERGY_ABSORPTION) && !(src instanceof Hunger)){
			float before = dmg;
			if (hero.heroClass == HeroClass.SLIMEGIRL){
				dmg = (int)Math.min(dmg, hero.HT/(4+2*hero.pointsInTalent(ENERGY_ABSORPTION)));
			} else {
				dmg = (int)Math.min(dmg, hero.HT*(0.4f-0.1f*hero.pointsInTalent(ENERGY_ABSORPTION)));
			}
			if (before >= 1) dmg = Math.max(dmg, 1);
		}
		if (dmg >= hero.HP && triggerDyingWill(hero)){
			dmg = Math.max(0, hero.HP - 1);
			Sample.INSTANCE.play( Assets.Sounds.DEATHSDOOR, 1, 1, Random.Float( 0.9f, 1.1f ) );
		}
		processFriarReasonLoss(hero, dmg, src);
		return dmg;
	}

	//the Friar loses sanity (Reason) when monsters hurt them
	private static void processFriarReasonLoss( Hero hero, int dmg, Object src ){
		if (hero.heroClass != HeroClass.FRIAR || !(src instanceof com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob)) return;
		com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob mob = (com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob) src;
		Class R = com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Reason.class;
		if (mob instanceof com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith){
			com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Reason.loseReason(hero, 5);
			if (Random.Int(3) == 0){
				Buff.extend(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Panic.class, 10);
				Sample.INSTANCE.play( Assets.Sounds.PANIC, 1, 1, Random.Float( 0.9f, 1.1f ) );
			}
		} else if (mob.properties().contains(Char.Property.UNDEAD) || mob.properties().contains(Char.Property.DEMONIC)){
			if (hero.HP < hero.HT/2){
				com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Reason.loseReason(hero, Math.min(dmg + Random.Int(2, 6), 30));
				boolean yog = mob instanceof com.shatteredpixel.shatteredpixeldungeon.actors.mobs.YogDzewa || mob instanceof com.shatteredpixel.shatteredpixeldungeon.actors.mobs.YogFist;
				if ((yog && Random.Int(2) == 0) || Random.Int(5) == 0){
					Buff.extend(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Panic.class, 10);
					Sample.INSTANCE.play( Assets.Sounds.PANIC, 1, 1, Random.Float( 0.9f, 1.1f ) );
				}
			} else {
				com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Reason.loseReason(hero, Math.min(dmg + Random.Int(1, 3), 30));
			}
		} else if (hero.HP < hero.HT/2){
			com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Reason.loseReason(hero, Math.min(dmg + Random.Int(2, 6), 30));
		}
		if (hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Suffering.Fear.class) != null && Random.Int(10) < 3){
			com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Reason.sufferingReason(hero, 5);
		}
	}

	//called when the hero's side kills an enemy, the Friar's rewards for it
	public static void onFriarKill( Hero hero, Char enemy ){
		onNinjaKill(hero, enemy);
		Class P = com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Panic.class;
		if (hero.heroClass == HeroClass.FRIAR){
			int virtueKillExtend = hero.subClass.is(HeroSubClass.PIOUS) && hero.hasTalent(BRIGHT_WARRIOR) ? 3 + hero.pointsInTalent(BRIGHT_WARRIOR)*3 : 3;
			if (enemy instanceof com.shatteredpixel.shatteredpixeldungeon.actors.mobs.YogDzewa){
				com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Reason.gainReason(hero, 100);
				Buff.detach(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Panic.class);
			} else if (enemy.properties().contains(Char.Property.BOSS)){
				com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Reason.gainReason(hero, 50);
				Buff.detach(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Panic.class);
			} else if (enemy.properties().contains(Char.Property.MINIBOSS)){
				com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Reason.gainReason(hero, 10);
			} else if (enemy instanceof com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith){
				com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Reason.gainReason(hero, 2);
				Buff.detach(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Panic.class);
			} else if (enemy.properties().contains(Char.Property.UNDEAD) || enemy.properties().contains(Char.Property.DEMONIC)){
				if (hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Virtue.Firm.class) != null
						|| hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Virtue.Fearless.class) != null
						|| hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Virtue.Inspire.class) != null){
					Buff.extend(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Virtue.Inspire.class, virtueKillExtend);
				}
				if (Random.Int(4) == 0) hero.heal(hero.HT/10);
			}
		}
		if ((enemy.properties().contains(Char.Property.UNDEAD) || enemy.properties().contains(Char.Property.DEMONIC)) && hero.hasTalent(OTHERWORLD_BANE)){
			Buff.affect( hero, MindVision.class, 1+hero.pointsInTalent(OTHERWORLD_BANE) );
			com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite.show(hero, com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite.VISION, 1, 0.77f, 0.9f);
			Dungeon.observe();
		}
		if (hero.subClass.is(HeroSubClass.PIOUS)){
			com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RitualDagger ritualDagger = hero.belongings.getItem(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RitualDagger.class);
			if (ritualDagger != null) ritualDagger.onKill(hero);
		}
	}

	//---- Transformation PD, the Slime Girl ------------------------------------------------------

	public static class ResilientArmor extends Buff {
		{ type = buffType.POSITIVE; }
		public int left;
		private static final String LEFT = "left";
		@Override public int icon() { return BuffIndicator.ARMOR; }
		@Override public void tintIcon(Image icon) { icon.hardlight(1, 1, 0); }
		@Override public float iconFadePercent() { return Math.max(0, (3-left) / 3f); }
		@Override public String iconTextDisplay() { return Integer.toString(left); }
		@Override public String desc() { return Messages.get(this, "desc", left); }
		public void set(int shots){ left = Math.max(left, shots); }
		@Override public void storeInBundle( Bundle bundle ) { super.storeInBundle(bundle); bundle.put(LEFT, left); }
		@Override public void restoreFromBundle( Bundle bundle ) { super.restoreFromBundle(bundle); left = bundle.getInt(LEFT); }
	}

	public static class SlimeMucusCooldown extends FlavourBuff {
		@Override public int icon() { return BuffIndicator.TIME; }
		@Override public void tintIcon(Image icon) { icon.hardlight(0.3f, 1f, 0.3f); }
		@Override public float iconFadePercent() { return Math.max(0, visualcooldown() / 30); }
		public void Reduce(){
			spend(-1);
			if (visualcooldown() <= 0) detach();
		}
	}

	public static class DarkHookCooldown extends FlavourBuff {
		@Override public int icon() { return BuffIndicator.TIME; }
		@Override public void tintIcon(Image icon) { icon.hardlight(0.5f, 0.2f, 0.7f); }
		@Override public float iconFadePercent() { return Math.max(0, visualcooldown() / 100); }
		@Override
		public void detach(){
			if (Dungeon.hero != null && Dungeon.hero.subClass.is(HeroSubClass.DARKSLIME)){
				ActionIndicator.setAction(Dungeon.hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.DarkHook.class));
				BuffIndicator.refreshHero();
			}
			if (target.remove( this ) && target.sprite != null) fx( false );
		}
	}

	//eating, for the Slime Girl
	private static void onSlimeFoodEaten( Hero hero ){
		if (hero.hasTalent(RESILIENT_MEAL)){
			Buff.affect( hero, ResilientArmor.class).set(1 + hero.pointsInTalent(RESILIENT_MEAL)*2);
		}
		if (hero.hasTalent(TOUGH_MEAL)){
			int healing;
			if (hero.pointsInTalent(TOUGH_MEAL) == 1){
				healing = Math.min(Math.max((int)(0.1 * (hero.HT - hero.HP)), 3), 15);
			} else {
				healing = Math.min(Math.max((int)(0.15 * (hero.HT - hero.HP)), 5), 25);
			}
			if (hero.hasTalent(ORIGINAL_MONSTER)) healing = (int)(healing * 1.5);
			if (hero.subClass == HeroSubClass.WARDEN) {
				Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing.class).setHeal(healing, 0, 1);
			} else {
				Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.plants.Sungrass.Health.class).boost(healing);
			}
		}
	}

	//drinking a potion, Liquid Perception tells you what one unknown potion or scroll is
	private static void onSlimePotionUsed( Hero hero, float factor ){
		if (hero.hasTalent(LIQUID_PERCEPTION) && factor == 2){
			java.util.HashSet<Class<? extends com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion>> potions = com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion.getUnknown();
			java.util.HashSet<Class<? extends com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll>> scrolls = com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll.getUnknown();
			if (hero.pointsInTalent(LIQUID_PERCEPTION) == 2 && Random.Int(2) == 1 && !scrolls.isEmpty()){
				com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll s = com.watabou.utils.Reflection.newInstance(Random.element(scrolls));
				if (s != null){ s.identify(); GLog.p(Messages.get(Talent.class, "liquid_perception_found", s.name())); }
			} else if (!potions.isEmpty()){
				com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion p = com.watabou.utils.Reflection.newInstance(Random.element(potions));
				if (p != null){ p.identify(); GLog.p(Messages.get(Talent.class, "liquid_perception_found", p.name())); }
			}
		}
	}

	//the Slime Girl's and Dark Slime's reactions when the hero is hit by an enemy in melee. Returns the damage left.
	public static int onSlimeDefense( Hero hero, Char enemy, int damage ){
		if (hero.pointsInTalent(DARK_GAS) > 0 && hero.subClass.is(HeroSubClass.DARKSLIME)){
			boolean gas = false;
			switch (hero.pointsInTalent(DARK_GAS)){
				case 3:
					if (Random.Int( 5 ) == 0){
						com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene.add(com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob.seed(hero.pos, 20, com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StenchGas.class));
						gas = true;
					}
				case 2:
					if (Random.Int( 5 ) == 0){
						com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene.add(com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob.seed(hero.pos, 20, com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas.class));
						gas = true;
					}
				case 1:
					if (Random.Int( 5 ) == 0){
						com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene.add(com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob.seed(hero.pos, 20, com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas.class));
						gas = true;
					}
					break;
			}
			if (gas) Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity.class, 4);
		}
		if (hero.buff(ResilientArmor.class) != null){
			hero.buff(ResilientArmor.class).left--;
			if (hero.buff(ResilientArmor.class).left <= 0) hero.buff(ResilientArmor.class).detach();
		}
		if (hero.subClass.is(HeroSubClass.DARKSLIME) && Random.Int(20) < 3){
			Buff.affect(enemy, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze.class).set(20);
		}
		if (hero.subClass.is(HeroSubClass.WATERSLIME) && hero.buff(SlimeMucusCooldown.class) == null){
			ArrayList<Integer> pos = new ArrayList<>();
			PathFinder.buildDistanceMap( hero.pos, com.watabou.utils.BArray.not( Dungeon.level.solid, null ), 4 );
			for (int i = 0; i < PathFinder.distance.length; i++) {
				if (PathFinder.distance[i] < Integer.MAX_VALUE
						&& Dungeon.level.traps.get(i) == null
						&& com.shatteredpixel.shatteredpixeldungeon.actors.Actor.findChar( i ) == null
						&& Dungeon.level.passable[i]
						&& Dungeon.level.map[i] != com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP) {
					pos.add(i);
				}
			}
			if (!pos.isEmpty()){
				int hpToReduce = Math.max((int)(0.1 * hero.HP), 1);
				if (hero.pointsInTalent(WATER_REVIVAL) == 3) hpToReduce = Math.max((int)(0.05 * hero.HP), 1);
				if (hero.HP > hpToReduce){
					Buff.affect(hero, SlimeMucusCooldown.class, 30f);
					hero.HP -= hpToReduce;
					hero.sprite.showStatusWithIcon(CharSprite.NEGATIVE, Integer.toString(hpToReduce), FloatingText.PHYS_DMG);
					com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.SlimeMucus slime = new com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.SlimeMucus();
					slime.pos = Random.element(pos);
					com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene.add(slime, 1f);
					Dungeon.level.occupyCell(slime);
					Dungeon.level.pressCell(slime.pos);
				}
			}
		}
		return damage;
	}

	//attacking, for the Slime Girl
	private static int onSlimeAttack( Hero hero, Char enemy, int dmg ){
		if (hero.subClass.is(HeroSubClass.DARKSLIME) && Random.Int(10) < 5){
			Buff.affect(enemy, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze.class).set(20);
		}
		if (hero.pointsInTalent(WATER_WAVE) > 0 && Dungeon.level.water[enemy.pos]){
			dmg += hero.pointsInTalent(WATER_WAVE);
		}
		return dmg;
	}

	//The Slime Girl's wands, shots and heals are bigger when she has a shield
	public static int empoweringLifeDamage( Hero hero, int dmg ){
		if (hero != null && hero.hasTalent(EMPOWERING_LIFE) && hero.shielding() > 0){
			dmg = (int)(dmg * (1f + 0.1f * hero.pointsInTalent(EMPOWERING_LIFE)));
		}
		return dmg;
	}

	//---- Transformation PD, the Ninja ---------------------------------------------------------

	public static class AgileAttack extends Buff {
		{ type = buffType.POSITIVE; }
		@Override public int icon() { return BuffIndicator.WEAPON; }
		@Override public void tintIcon(Image icon) { icon.hardlight(1f, 0.8f, 0.85f); }
	}

	public static class NinjaSocial extends Buff {
		{ type = buffType.POSITIVE; }
		public int left;
		private static final String LEFT = "left";
		public void set(int uses){ left = Math.max(left, uses); }
		@Override public int icon() { return BuffIndicator.INVERT_MARK; }
		@Override public void tintIcon(Image icon) { icon.hardlight(1.0f, 0f, 0f); }
		@Override public float iconFadePercent() { return Math.max(0, (5-left) / 5f); }
		@Override public String iconTextDisplay() { return Integer.toString(left); }
		@Override public String desc() { return Messages.get(this, "desc", left); }
		@Override public void storeInBundle( Bundle bundle ) { super.storeInBundle(bundle); bundle.put(LEFT, left); }
		@Override public void restoreFromBundle( Bundle bundle ) { super.restoreFromBundle(bundle); left = bundle.getInt(LEFT); }
	}

	public static class XiaDef extends FlavourBuff {
		@Override public int icon() { return BuffIndicator.WEAPON; }
		@Override public void tintIcon(Image icon) { icon.hardlight(1f, 0.75f, 0.79f); }
		@Override public float iconFadePercent() { return Math.max(0, (5 - visualcooldown()) / 5); }
	}

	public static class LightBox extends FlavourBuff {
		@Override public int icon() { return BuffIndicator.NONE; }
	}

	public static class FeintCooldown extends FlavourBuff {
		@Override public int icon() { return BuffIndicator.TIME; }
		@Override public void tintIcon(Image icon) { icon.hardlight(0.6f, 0.6f, 0.6f); }
		@Override public float iconFadePercent() { return Math.max(0, visualcooldown() / 20); }
	}

	private static void onNinjaFoodEaten( Hero hero, Item foodSource ){
		if (hero.hasTalent(NINJA_MEAL)){
			com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Shuriken_Box box = hero.belongings.getItem(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Shuriken_Box.class);
			if (box != null){
				box.directCharge(hero.pointsInTalent(NINJA_MEAL));
				com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging.charge(hero);
			} else {
				Buff.affect( hero, ArtifactRecharge.class).set(hero.pointsInTalent(NINJA_MEAL)).ignoreHornOfPlenty = foodSource instanceof com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
			}
		}
		if (hero.hasTalent(YUNYING_MEAL)){
			Buff.affect(hero, Invisibility.class, hero.pointsInTalent(YUNYING_MEAL)*2);
		}
	}

	//a thrown item that is not a weapon draws every enemy in sight to where it lands (Feint)
	public static void onItemThrownDistraction( Hero hero, int cell ){
		if (hero.hasTalent(FEINT) && hero.buff(FeintCooldown.class) == null){
			for (com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob mob : Dungeon.level.mobs.toArray( new com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob[0] )) {
				if (mob.alignment != Char.Alignment.ALLY && Dungeon.level.heroFOV[mob.pos]) {
					if (mob.state != mob.PASSIVE) mob.state = mob.WANDERING;
					mob.beckon( cell );
				}
			}
			Buff.affect(hero, FeintCooldown.class, 25 - hero.pointsInTalent(FEINT)*5);
		}
	}

	//an enemy missed the hero
	public static void onHeroDodged( Hero hero, Char attacker ){
		if (hero.hasTalent(AGILE_ATTACK)){
			Buff.affect(hero, AgileAttack.class);
		}
		if (hero.hasTalent(YOU_SCARED_ME) && hero.shielding() < (hero.pointsInTalent(YOU_SCARED_ME)+1)*2){
			Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier.class).incShield(
					Math.min(1+hero.pointsInTalent(YOU_SCARED_ME), (hero.pointsInTalent(YOU_SCARED_ME)+1)*2-hero.shielding()));
		}
		if (hero.heroClass == HeroClass.FRIAR && hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Suffering.Paranoia.class) != null
				&& hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Reason.class) != null && Random.Int(10) < 3){
			com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Reason.sufferingReason(hero, 5);
		}
	}

	private static int onNinjaAttack( Hero hero, Char enemy, int dmg ){
		if (hero.hasTalent(KONO_FUKUSA) && enemy instanceof com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob
				&& (((com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob) enemy).surprisedBy(hero)
					|| ((com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob) enemy).state == ((com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob) enemy).HUNTING)){
			dmg *= 1 + 0.2*hero.pointsInTalent(KONO_FUKUSA);
		}
		if (hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ninja.OneSword.OKU_OneSword.class) != null
				&& hero.belongings.attackingWeapon() instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon){
			float onesword = 1.3f;
			if (((com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon) hero.belongings.attackingWeapon()).hasTrait(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon.WeaponTrait.ONE_SWORD_BLADE, hero)){
				onesword += 0.2f;
			}
			if (hero.hasTalent(OFFENSIVE)) onesword += 0.15f*hero.pointsInTalent(OFFENSIVE);
			dmg *= onesword;
		}
		if (enemy.buff(com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ninja.Decoy.ShadowMark.class) != null && hero.hasTalent(ALLHUNTING)){
			dmg *= 1+0.15f*hero.pointsInTalent(ALLHUNTING);
		}
		if (hero.hasTalent(AGILE_ATTACK) && hero.buff(AgileAttack.class) != null){
			hero.buff(AgileAttack.class).detach();
			dmg += 1+2*hero.pointsInTalent(AGILE_ATTACK);
		}
		return dmg;
	}

	private static void onNinjaKill( Hero hero, Char enemy ){
		if (hero.hasTalent(KILL_CONTINUE) && hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ninja.OneSword.OKU_OneSword.class) != null
				&& hero.belongings.attackingWeapon() instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon){
			Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ninja.OneSword.OKU_OneSword.class, hero.pointsInTalent(KILL_CONTINUE));
		}
		if (hero.hasTalent(NINJA_SOCIAL)){
			Buff.affect(hero, NinjaSocial.class).set(1+2*hero.pointsInTalent(NINJA_SOCIAL));
		}
	}

	//---- Transformation PD, the DM400 ---------------------------------------------------------

	public static class MarkMeal extends Buff {
		{ type = buffType.POSITIVE; }
		public int left;
		private static final String LEFT = "left";
		@Override public int icon() { return BuffIndicator.WEAPON; }
		@Override public void tintIcon(Image icon) { icon.hardlight(0.5f, 0.5f, 1f); }
		@Override public String iconTextDisplay() { return Integer.toString(left); }
		@Override public String desc() { return Messages.get(this, "desc", left); }
		@Override public void storeInBundle( Bundle bundle ) { super.storeInBundle(bundle); bundle.put(LEFT, left); }
		@Override public void restoreFromBundle( Bundle bundle ) { super.restoreFromBundle(bundle); left = bundle.getInt(LEFT); }
	}

	private static void onDMFoodEaten( Hero hero ){
		if (hero.hasTalent(MARK_MEAL)){
			Buff.affect( hero, MarkMeal.class).left = 3;
		}
	}

	//the DM400's marks and fists, and the Overload state of Routine. Returns the damage.
	private static int onDMAttack( Hero hero, Char enemy, int dmg ){
		MarkMeal b = hero.buff(MarkMeal.class);
		if (hero.heroClass == HeroClass.DM400){
			int turn = 3;
			if (b != null){
				turn += 1 + hero.pointsInTalent(MARK_MEAL);
				b.left -= 1;
				if (b.left <= 0) b.detach();
			}
			if (enemy.alignment != Char.Alignment.ALLY){
				Buff.affect(enemy, com.shatteredpixel.shatteredpixeldungeon.items.artifacts.InstructionTool.InstructionMark.class).reset(turn);
			}
		} else if (b != null){
			Buff.affect(enemy, Vertigo.class, 1 + hero.pointsInTalent(MARK_MEAL));
			b.left -= 1;
			if (b.left <= 0) b.detach();
		}
		if (enemy.buff(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.InstructionTool.InstructionMark.class) != null && hero.pointsInTalent(SPECIAL_MARK) > Random.Int(6)){
			Buff.affect(enemy, com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAggression.Aggression.class, 3);
		}
		if (hero.hasTalent(TARGET_TARGETING)){
			if (enemy.buff(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.InstructionTool.InstructionMark.class) != null){
				dmg += hero.pointsInTalent(TARGET_TARGETING);
			} else if (hero.heroClass != HeroClass.DM400){
				dmg += hero.pointsInTalent(TARGET_TARGETING);
			}
		}
		if (hero.hasTalent(ROCKET_FIST) && hero.belongings.attackingWeapon() == null && Random.Int(10) < 6){
			com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica trajectory = new com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica(hero.pos, enemy.pos, com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica.STOP_TARGET);
			trajectory = new com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size()-1), com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica.PROJECTILE);
			com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave.throwChar(enemy, trajectory, hero.pointsInTalent(ROCKET_FIST), false, true, hero);
		}
		if (hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.dm400.Routine.preOverLoad.class) != null){
			dmg *= 0.5f;
		}
		if (hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.dm400.Routine.OverLoad.class) != null){
			if (hero.hasTalent(STRONG_PERSERVE)){
				dmg *= 1-0.05f*hero.pointsInTalent(STRONG_PERSERVE);
			} else if (hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.dm400.Routine.preOverLoad.class) == null){
				dmg *= 1.5f;
			}
		}
		return dmg;
	}

	//damage the hero takes, for the DM400's talents and its Overload
	private static int onDMDamage( Hero hero, int dmg ){
		if (hero.hasTalent(BODY_REINFORCE) && hero.heroClass != HeroClass.DM400 && dmg > 0){
			dmg *= 1f-0.05f*hero.pointsInTalent(BODY_REINFORCE);
		}
		if (hero.hasTalent(TBM) && hero.buff(TBMCooldown.class) == null){
			Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier.class).incShield(1+2*hero.pointsInTalent(TBM));
			Buff.affect(hero, TBMCooldown.class, 60);
		}
		return dmg;
	}

	public static class TBMCooldown extends FlavourBuff {
		@Override public int icon() { return BuffIndicator.NONE; }
	}

	//---- Transformation PD, the Princess --------------------------------------------------------

	public static class ManaWreath extends Buff {
		{ type = buffType.NEGATIVE; }
		@Override public int icon() { return BuffIndicator.NONE; }
		@Override public boolean act() { detach(); return super.act(); }
	}

	public static class EnchantMeal extends Buff {
		{ type = buffType.POSITIVE; }
		public int left;
		private static final String LEFT = "left";
		@Override public int icon() { return BuffIndicator.UPGRADE; }
		@Override public void tintIcon(Image icon) { icon.hardlight(0.537f, 0.0f, 1.0f); }
		@Override public float iconFadePercent() { return Math.max(0, (5-left) / 5f); }
		@Override public String iconTextDisplay() { return Integer.toString(left); }
		@Override public String desc() { return Messages.get(this, "desc", left); }
		public void set(int shots){ left = Math.max(left, shots); }
		@Override public void storeInBundle( Bundle bundle ) { super.storeInBundle(bundle); bundle.put(LEFT, left); }
		@Override public void restoreFromBundle( Bundle bundle ) { super.restoreFromBundle(bundle); left = bundle.getInt(LEFT); }
	}

	public static class RoyalMeal extends FlavourBuff {
		@Override public int icon() { return BuffIndicator.UPGRADE; }
		@Override public void tintIcon(Image icon) { icon.hardlight(1.0f, 0.604f, 0.012f); }
	}

	public static class RoyalMeal2 extends RoyalMeal {
	}

	public static class ArcaneShieldCooldown extends FlavourBuff {
		@Override public int icon() { return BuffIndicator.TIME; }
		@Override public void tintIcon(Image icon) { icon.hardlight(0.5f, 0.3f, 1f); }
		@Override public float iconFadePercent() { return Math.max(0, visualcooldown() / 20); }
	}

	private static com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfKing kingRing( Hero hero ){
		com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfKing ring = hero.belongings.getItem(com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfKing.class);
		return ring != null && ring.isEquipped(hero) && !ring.cursed ? ring : null;
	}

	private static void onPrincessFoodEaten( Hero hero ){
		if (hero.hasTalent(ENCHANT_MEAL)){
			Buff.affect( hero, EnchantMeal.class).left = 1 + 2*hero.pointsInTalent(ENCHANT_MEAL);
		}
		if (hero.hasTalent(ROYAL_MEAL)){
			if (hero.belongings.getItem(com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfKing.class) != null){
				Buff.affect( hero, RoyalMeal.class, 10);
			} else {
				Buff.affect( hero, RoyalMeal2.class, 5*hero.pointsInTalent(ROYAL_MEAL));
			}
		}
	}

	//items with a rune or glyph are understood quicker (Mana Sense)
	public static float manaSenseFactor( Hero hero, Item item ){
		float f = 1f;
		if (hero.pointsInTalent(MANA_SENSE) > 0){
			if (item instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon && ((com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon) item).enchantment != null){
				f *= 1.0f + 4.0f*hero.pointsInTalent(MANA_SENSE);
			}
			if (item instanceof com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor && ((com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor) item).glyph != null){
				f *= 1.0f + 4.0f*hero.pointsInTalent(MANA_SENSE);
			}
		}
		return f;
	}

	//the stances of the Combat Master. kind is 0 accuracy, 1 evasion, 2 armor, 3 damage, 4 speed
	public static float stanceFactor( Hero hero, int kind ){
		com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FightStance s = hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FightStance.class);
		if (s == null) return 1f;
		int p = hero.pointsInTalent(STANCE_MASTERY);
		switch (kind){
			case 0:
				if (s.stance == s.balance) return 1.1f + Math.min(1, p)*0.05f;
				if (s.stance == s.parry) return 0.7f + (p/3)*0.15f;
				break;
			case 1:
				if (s.stance == s.balance) return 1.1f + Math.min(1, p)*0.05f;
				if (s.stance == s.invasion) return 0.7f + (p/3)*0.15f;
				break;
			case 2:
				if (s.stance == s.balance) return 1.1f + Math.min(1, p)*0.05f;
				if (s.stance == s.parry) return 1.2f + (p/2)*0.1f;
				break;
			case 3:
				if (s.stance == s.balance) return 1.1f + Math.min(1, p)*0.05f;
				if (s.stance == s.invasion) return 1.2f + (p/2)*0.1f;
				break;
			case 4:
				if (s.stance == s.balance && p > 2) return 1.15f;
				break;
		}
		return 1f;
	}

	private static int onPrincessAttack( Hero hero, Char enemy, int dmg ){
		dmg = Math.round(dmg * stanceFactor(hero, 3));
		com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfKing ring = kingRing(hero);
		com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon weapon = hero.belongings.attackingWeapon();
		boolean enchanted = (weapon instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon
				&& ((com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon) weapon).enchantment != null) || (ring != null && ring.enchantment != null);
		boolean ranged = weapon instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon
				&& !(weapon instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon);
		if (hero.hasTalent(MAGIC_ARROW) && ((ranged && enchanted) || (ring != null && ring.enchantment != null))){
			dmg *= 1.0f + 0.15f*hero.pointsInTalent(MAGIC_ARROW);
		}
		if (hero.hasTalent(RUNE_BLADE) && enemy.isAlive() && enchanted){
			enemy.damage(hero.pointsInTalent(RUNE_BLADE), new com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfMagicMissile());
		}
		if (hero.hasTalent(MANA_WREATH) && enchanted){
			Buff.affect(enemy, ManaWreath.class);
		}
		return dmg;
	}

	//the Rune Mage marks what she hits with the runes on her weapon and ring
	public static void princessRuneMark( Hero hero, Char defender, Weapon.Enchantment... enchants ){
		if (!hero.subClass.is(HeroSubClass.RUNEMAGE)) return;
		com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RuneMark runeMark = defender.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RuneMark.class);
		if (runeMark == null) runeMark = Buff.affect(defender, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RuneMark.class);
		for (Weapon.Enchantment e : enchants) if (e != null) runeMark.addEnchantment(e);
		if (runeMark.enchantments.isEmpty()){
			runeMark.detach();
		} else {
			if (hero.pointsInTalent(DARKMARK) > 2) Buff.affect(defender, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness.class, 3);
			if (hero.pointsInTalent(DARKMARK) > 1) Buff.affect(defender, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable.class, 3);
			if (hero.pointsInTalent(DARKMARK) > 0) Buff.affect(defender, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex.class, 3);
		}
	}

	//a hit on the Princess, the shielding of Arcane Shield and the slow of Delay Tactic
	private static int onPrincessDamage( Hero hero, int dmg, Object src ){
		if (hero.hasTalent(ARCANE_SHIELD) && src instanceof Char){
			com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor a = hero.belongings.armor();
			com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfKing ring = kingRing(hero);
			if (((a != null && a.glyph != null) || (ring != null && ring.glyph != null))
					&& hero.buff(ArcaneShieldCooldown.class) == null){
				Buff.affect(hero, ArcaneShieldCooldown.class, 20);
				Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier.class).incShield(1 + hero.pointsInTalent(ARCANE_SHIELD));
			}
		}
		com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.princess.MarchForward.Forward f = hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.princess.MarchForward.Forward.class);
		if (f != null && dmg > 0){
			f.left -= 1;
			if (f.left <= 0) f.detach();
			if (hero.hasTalent(DELAY_TACTIC) && src instanceof Char){
				Buff.affect((Char) src, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow.class, 2*hero.pointsInTalent(DELAY_TACTIC));
			}
		}
		return dmg;
	}

	public static void onTalentUpgraded( Hero hero, Talent talent ){
		RearrangedTalents.onTalentUpgraded(hero, talent); //rearranged
		TransformationTalents.onTalentUpgraded(hero, talent);
		if (talent == MIDAS_TOUCH && hero.hasTalent(MIDAS_TOUCH)){
			Dungeon.level.drop(new com.shatteredpixel.shatteredpixeldungeon.items.LiquidMetal().quantity(20 * hero.pointsInTalent(MIDAS_TOUCH)), hero.pos).sprite.drop(hero.pos);
		}
		if (talent == PATTERN_RECOGNITION) {
			java.util.ArrayList<Class> potions = hero.PatternRecognitionPotions;
			for (com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion p : hero.belongings.getAllItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion.class)) {
				if (!p.isKnown()) {
					if (hero.pointsInTalent(PATTERN_RECOGNITION) == 2) {
						p.setKnown();
					} else if (!potions.contains(p.getClass())) {
						potions.add(p.getClass());
						if (Random.Int(2) == 0) {
							p.setKnown();
						}
					}
				}
			}
			if (hero.pointsInTalent(PATTERN_RECOGNITION) != 1) potions.clear();
		}

		//for metamorphosis
		if (talent == IRON_WILL && hero.heroClass != HeroClass.WARRIOR){
			Buff.affect(hero, BrokenSeal.WarriorShield.class);
		}

		if (talent == VETERANS_INTUITION && hero.pointsInTalent(VETERANS_INTUITION) == 2){
			if (hero.belongings.armor() != null && !ShardOfOblivion.passiveIDDisabled())  {
				hero.belongings.armor.identify();
			}
		}
		if (talent == THIEFS_INTUITION && hero.pointsInTalent(THIEFS_INTUITION) == 2){
			if (hero.belongings.ring instanceof Ring && !ShardOfOblivion.passiveIDDisabled()) {
				hero.belongings.ring.identify();
			}
			if (hero.belongings.misc instanceof Ring && !ShardOfOblivion.passiveIDDisabled()) {
				hero.belongings.misc.identify();
			}
			for (Ring extra : hero.belongings.extraRings){
				if (extra != null && !ShardOfOblivion.passiveIDDisabled()) extra.identify();
			}
			for (Item item : Dungeon.hero.belongings){
				if (item instanceof Ring){
					((Ring) item).setKnown();
				}
			}
		}
		if (talent == THIEFS_INTUITION && hero.pointsInTalent(THIEFS_INTUITION) == 1){
			if (hero.belongings.ring instanceof Ring) hero.belongings.ring.setKnown();
			if (hero.belongings.misc instanceof Ring) ((Ring) hero.belongings.misc).setKnown();
		}
		if (talent == ADVENTURERS_INTUITION && hero.pointsInTalent(ADVENTURERS_INTUITION) == 2){
			if (hero.belongings.weapon() != null && !ShardOfOblivion.passiveIDDisabled()){
				hero.belongings.weapon().identify();
			}
			if (hero.belongings.secondWep() != null && !ShardOfOblivion.passiveIDDisabled()){
				hero.belongings.secondWep().identify();
			}
		}

		if (talent == PROTECTIVE_SHADOWS && hero.invisible > 0){
			Buff.affect(hero, Talent.ProtectiveShadowsTracker.class);
		}

		if (talent == LIGHT_CLOAK && hero.heroClass == HeroClass.ROGUE){
			for (Item item : Dungeon.hero.belongings.backpack){
				if (item instanceof CloakOfShadows){
					if (!hero.belongings.lostInventory() || item.keptThroughLostInventory()) {
						((CloakOfShadows) item).activate(Dungeon.hero);
					}
				}
			}
		}

		if (talent == HEIGHTENED_SENSES || talent == FARSIGHT || talent == DIVINE_SENSE){
			Dungeon.observe();
		}

		if (talent == TWIN_UPGRADES || talent == DESPERATE_POWER
				|| talent == STRONGMAN || talent == DURABLE_PROJECTILES){
			Item.updateQuickslot();
		}

		if (talent == UNENCUMBERED_SPIRIT && hero.pointsInTalent(talent) == 3){
			Item toGive = new ClothArmor().identify();
			if (!toGive.collect()){
				Dungeon.level.drop(toGive, hero.pos).sprite.drop();
			}
			toGive = new Gloves().identify();
			if (!toGive.collect()){
				Dungeon.level.drop(toGive, hero.pos).sprite.drop();
			}
		}

		if (talent == LIGHT_READING && hero.heroClass == HeroClass.CLERIC){
			for (Item item : Dungeon.hero.belongings.backpack){
				if (item instanceof HolyTome){
					if (!hero.belongings.lostInventory() || item.keptThroughLostInventory()) {
						((HolyTome) item).activate(Dungeon.hero);
					}
				}
			}
		}

		//if we happen to have spirit form applied with a ring of might
		if (talent == SPIRIT_FORM){
			Dungeon.hero.updateHT(false);
		}
	}

	public static class CachedRationsDropped extends CounterBuff{{revivePersists = true;}};
	public static class NatureBerriesDropped extends CounterBuff{{revivePersists = true;}};

	public static void onFoodEaten( Hero hero, float foodVal, Item foodSource ){
		RearrangedTalents.onFoodEaten(hero, foodVal, foodSource); //rearranged
		XunTalents.onFoodEaten(hero); //xun
		TransformationTalents.onFoodEaten(hero, foodSource);
		onSlimeFoodEaten(hero);
		onNinjaFoodEaten(hero, foodSource);
		onDMFoodEaten(hero);
		onPrincessFoodEaten(hero);
		if (hero.hasTalent(WHISPERING_MEAL)){
			float duration = hero.pointsInTalent(WHISPERING_MEAL) == 1 ? 2f : 4f;
			for (com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob mob : Dungeon.level.mobs.toArray(new com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob[0])){
				if (mob.alignment == Char.Alignment.ENEMY && Dungeon.level.heroFOV[mob.pos]){
					Buff.affect(mob, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror.class, duration).object = hero.id();
				}
			}
		}
		if (hero.hasTalent(HEARTY_MEAL)){
			//4/6 HP healed, when hero is below 33% health (with a little rounding up)
			if (hero.HP/(float)hero.HT < 0.334f) {
				int healing = 2 + 2 * hero.pointsInTalent(HEARTY_MEAL);
				hero.HP = Math.min(hero.HP + healing, hero.HT);
				hero.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(healing), FloatingText.HEALING);

			}
		}
		if (hero.hasTalent(IRON_STOMACH)){
			if (hero.cooldown() > 0) {
				Buff.affect(hero, WarriorFoodImmunity.class, hero.cooldown());
			}
		}
		if (hero.hasTalent(EMPOWERING_MEAL)){
			//2/3 bonus wand damage for next 3 zaps
			Buff.affect( hero, WandEmpower.class).set(1 + hero.pointsInTalent(EMPOWERING_MEAL), 3);
			ScrollOfRecharging.charge( hero );
		}
		int wandChargeTurns = 0;
		if (hero.hasTalent(ENERGIZING_MEAL)){
			//5/8 turns of recharging
			wandChargeTurns += 2 + 3*hero.pointsInTalent(ENERGIZING_MEAL);
		}
		int artifactChargeTurns = 0;
		if (hero.hasTalent(MYSTICAL_MEAL)){
			//3/5 turns of recharging
			artifactChargeTurns += 1 + 2*hero.pointsInTalent(MYSTICAL_MEAL);
		}
		if (hero.hasTalent(INVIGORATING_MEAL)){
			//effectively 1/2 turns of haste
			Buff.prolong( hero, Haste.class, 0.67f+hero.pointsInTalent(INVIGORATING_MEAL));
		}
		if (hero.hasTalent(STRENGTHENING_MEAL)){
			//3 bonus physical damage for next 2/3 attacks
			Buff.affect( hero, PhysicalEmpower.class).set(3, 1 + hero.pointsInTalent(STRENGTHENING_MEAL));
		}
		if (hero.hasTalent(TINKERERS_MEAL)) {
			if (hero.heroClass == HeroClass.ARTIFICER) {
				com.shatteredpixel.shatteredpixeldungeon.items.weapon.Gun gun = hero.belongings.getItem(com.shatteredpixel.shatteredpixeldungeon.items.weapon.Gun.class);
				if (gun != null) {
					gun.setLoaded(true);

					if (hero.pointsInTalent(TINKERERS_MEAL) == 2) {
						Buff.affect(hero, ArtificerFoodDamageBonus.class, hero.cooldown() > 0 ? hero.cooldown() : 5f);
					}
				}
			} else {
				Dungeon.energy += hero.pointsInTalent(TINKERERS_MEAL);
			}
		}
		if (hero.hasTalent(QUICK_CALIBRATION)){
			Buff.affect(hero, ArtificerFoodEvasionBonus.class, hero.cooldown() > 0 ? hero.cooldown() : 3f);
		}
		if (hero.hasTalent(ARCANE_MEAL)){
			Buff.affect(hero, ArcaneArmor.class).set(5, 2 + hero.pointsInTalent(ARCANE_MEAL));
		}
		if (hero.hasTalent(CLEANSING_MEAL)){

			Buff.detach(hero, Cripple.class);
			Buff.detach(hero, Weakness.class);
			Buff.detach(hero, Vulnerable.class);
			Buff.detach(hero, Bleeding.class);
			if (hero.pointsInTalent(CLEANSING_MEAL) == 2) {
				Buff.detach(hero, Blindness.class);
				Buff.detach(hero, Slow.class);
				Buff.detach(hero, Vertigo.class);
				Buff.detach(hero, Degrade.class);
			}
		}
		if (hero.hasTalent(FOCUSED_MEAL)){
			if (hero.heroClass == HeroClass.DUELIST){
				//0.67/1 charge for the duelist
				Buff.affect( hero, MeleeWeapon.Charger.class ).gainCharge((hero.pointsInTalent(FOCUSED_MEAL)+1)/3f);
				ScrollOfRecharging.charge( hero );
			} else {
				// lvl/3 / lvl/2 bonus dmg on next hit for other classes
				Buff.affect( hero, PhysicalEmpower.class).set(Math.round(hero.lvl / (4f - hero.pointsInTalent(FOCUSED_MEAL))), 1);
			}
		}
		if (hero.hasTalent(SATIATED_SPELLS)){
			if (hero.heroClass == HeroClass.CLERIC) {
				Buff.affect(hero, SatiatedSpellsTracker.class);
			} else {
				//3/5 shielding, delayed up to 10 turns
				int amount = 1 + 2*hero.pointsInTalent(SATIATED_SPELLS);
				Barrier b = Buff.affect(hero, Barrier.class);
				if (b.shielding() <= amount){
					b.setShield(amount);
					b.delay(Math.max(10-b.cooldown(), 0));
				}
			}
		}
		if (hero.hasTalent(ENLIGHTENING_MEAL)){
			if (hero.heroClass == HeroClass.CLERIC) {
				HolyTome tome = hero.belongings.getItem(HolyTome.class);
				if (tome != null) {
					// 2/3 of a charge at +1, 1 full charge at +2
					tome.directCharge( (1+hero.pointsInTalent(ENLIGHTENING_MEAL))/3f );
					ScrollOfRecharging.charge(hero);
				}
			} else {
				//2/3 turns of recharging, both kinds
				wandChargeTurns += 1 + hero.pointsInTalent(ENLIGHTENING_MEAL);
				artifactChargeTurns += 1 + hero.pointsInTalent(ENLIGHTENING_MEAL);
			}
		}

		//we process these at the end as they can stack together from some talents
		if (wandChargeTurns > 0){
			Buff.prolong( hero, Recharging.class, wandChargeTurns );
			ScrollOfRecharging.charge( hero );
			SpellSprite.show(hero, SpellSprite.CHARGE);
		}
		if (artifactChargeTurns > 0){
			ArtifactRecharge buff = Buff.affect( hero, ArtifactRecharge.class);
			if (buff.left() < artifactChargeTurns){
				buff.set(artifactChargeTurns).ignoreHornOfPlenty = foodSource instanceof HornOfPlenty;
			}
			ScrollOfRecharging.charge( hero );
			SpellSprite.show(hero, SpellSprite.CHARGE, 0, 1, 1);
		}
	}

	public static class WarriorFoodImmunity extends FlavourBuff{
		{ actPriority = HERO_PRIO+1; }
	}

	public static float itemIDSpeedFactor( Hero hero, Item item ){
		float manaSense = manaSenseFactor(hero, item);
		if (manaSense != 1f) return manaSense * itemIDSpeedFactorBase(hero, item);
		return itemIDSpeedFactorBase(hero, item);
	}

	private static float itemIDSpeedFactorBase( Hero hero, Item item ){
		float factor = 1f;

		// Affected by both Warrior(1.75x/2.5x) and Duelist(2.5x/inst.) talents
		if (item instanceof MeleeWeapon){
			factor *= 1f + 1.5f*hero.pointsInTalent(ADVENTURERS_INTUITION); //instant at +2 (see onItemEquipped)
			factor *= 1f + 0.75f*hero.pointsInTalent(VETERANS_INTUITION);
		}
		// Affected by both Warrior(2.5x/inst.) and Duelist(1.75x/2.5x) talents
		if (item instanceof Armor){
			factor *= 1f + 0.75f*hero.pointsInTalent(ADVENTURERS_INTUITION);
			factor *= 1f + hero.pointsInTalent(VETERANS_INTUITION); //instant at +2 (see onItemEquipped)
		}
		// 3x/instant for Mage (see Wand.wandUsed())
		if (item instanceof Wand){
			factor *= 1f + 2.0f*hero.pointsInTalent(SCHOLARS_INTUITION);
		}
		// 3x/instant speed with Huntress talent (see MissileWeapon.proc)
		if (item instanceof MissileWeapon){
			factor *= 1f + 2.0f*hero.pointsInTalent(SURVIVALISTS_INTUITION);
		}
		// 2x/instant for Rogue (see onItemEqupped), also id's type on equip/on pickup
		if (item instanceof Ring){
			factor *= 1f + hero.pointsInTalent(THIEFS_INTUITION);
		}
		return factor;
	}

	public static void onPotionUsed( Hero hero, int cell, float factor ){
		RearrangedTalents.onPotionUsed(hero, cell, factor); //rearranged
		XunTalents.onPotionUsed(hero, factor); //xun
		if (cell != hero.pos) TransformationTalents.onPotionThrown(hero, cell);
		onSlimePotionUsed(hero, factor);
		if (hero.hasTalent(LIQUID_BARRIER)){
			float duration = hero.pointsInTalent(LIQUID_BARRIER) == 1 ? 5f : 10f;
			Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity.class, duration * factor);
		}
		if (hero.hasTalent(LIQUID_WILLPOWER)){
			// 6.5/10% of max HP
			int shieldToGive = Math.round( factor * hero.HT * (0.030f + 0.035f*hero.pointsInTalent(LIQUID_WILLPOWER)));
			hero.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(shieldToGive), FloatingText.SHIELDING);
			Buff.affect(hero, Barrier.class).setShield(shieldToGive);
		}
		if (hero.hasTalent(LIQUID_NATURE)){
			ArrayList<Integer> grassCells = new ArrayList<>();
			for (int i : PathFinder.NEIGHBOURS9){
				grassCells.add(cell+i);
			}
			Random.shuffle(grassCells);
			for (int grassCell : grassCells){
				Char ch = Actor.findChar(grassCell);
				if (ch != null && ch.alignment == Char.Alignment.ENEMY){
					//1/2 turns of roots
					Buff.affect(ch, Roots.class, factor * hero.pointsInTalent(LIQUID_NATURE));
				}
				if (Dungeon.level.map[grassCell] == Terrain.EMPTY ||
						Dungeon.level.map[grassCell] == Terrain.EMBERS ||
						Dungeon.level.map[grassCell] == Terrain.EMPTY_DECO){
					Level.set(grassCell, Terrain.GRASS);
					GameScene.updateMap(grassCell);
				}
				CellEmitter.get(grassCell).burst(LeafParticle.LEVEL_SPECIFIC, 4);
			}
			// 4/6 cells total
			int totalGrassCells = (int) (factor * (2 + 2 * hero.pointsInTalent(LIQUID_NATURE)));
			while (grassCells.size() > totalGrassCells){
				grassCells.remove(0);
			}
			for (int grassCell : grassCells){
				int t = Dungeon.level.map[grassCell];
				if ((t == Terrain.EMPTY || t == Terrain.EMPTY_DECO || t == Terrain.EMBERS
						|| t == Terrain.GRASS || t == Terrain.FURROWED_GRASS)
						&& Dungeon.level.plants.get(grassCell) == null){
					Level.set(grassCell, Terrain.HIGH_GRASS);
					GameScene.updateMap(grassCell);
				}
			}
			Dungeon.observe();
		}
		if (hero.hasTalent(LIQUID_AGILITY)){
			Buff.prolong(hero, LiquidAgilEVATracker.class, hero.cooldown() + Math.max(0, factor-1));
			if (factor >= 0.5f){
				Buff.prolong(hero, LiquidAgilACCTracker.class, 5f).uses = Math.round(factor);
			}
		}
		if (hero.hasTalent(REACTIVE_CHEMISTRY)) {
			ArrayList<Char> affected = new ArrayList<>();
			PathFinder.buildDistanceMap( cell, com.watabou.utils.BArray.not( Dungeon.level.solid, null ), 2 );
			for (int i = 0; i < PathFinder.distance.length; i++) {
				if (PathFinder.distance[i] < Integer.MAX_VALUE && Actor.findChar(i) != null) {
					Char thisChar = Actor.findChar(i);
					if (thisChar.alignment == Char.Alignment.ENEMY) {
						affected.add(thisChar);
					}
				}
			}
			for (Char ch : affected){
				if (hero.pointsInTalent(REACTIVE_CHEMISTRY) == 2) {
					ch.damage(6, hero);

					//knock them back along the line from the hero to them
					if (ch.isAlive()) {
						com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica trajectory = new com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica(hero.pos, ch.pos, com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica.STOP_TARGET);
						trajectory = new com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size()-1), com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica.PROJECTILE);
						com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave.throwChar(ch, trajectory, 2, false, true, hero);
					}
				}
				else {
					ch.damage(4, hero);
				}
			}
		}
	}

	public static void onScrollUsed( Hero hero, int pos, float factor, Class<?extends Item> cls ){
		RearrangedTalents.onScrollUsed(hero, pos, factor); //rearranged
		XunTalents.onScrollUsed(hero, factor); //xun
		TransformationTalents.onScrollUsed(hero);
		if (hero.hasTalent(INSCRIBED_POWER)){
			// 2/3 empowered wand zaps
			Buff.affect(hero, ScrollEmpower.class).reset((int) (factor * (1 + hero.pointsInTalent(INSCRIBED_POWER))));
		}
		if (hero.hasTalent(INSCRIBED_STEALTH)){
			// 3/5 turns of stealth
			Buff.affect(hero, Invisibility.class, factor * (1 + 2*hero.pointsInTalent(INSCRIBED_STEALTH)));
			Sample.INSTANCE.play( Assets.Sounds.MELD );
		}
		if (hero.hasTalent(RECALL_INSCRIPTION) && Scroll.class.isAssignableFrom(cls) && cls != ScrollOfUpgrade.class){
			if (hero.heroClass == HeroClass.CLERIC){
				Buff.prolong(hero, RecallInscription.UsedItemTracker.class, hero.pointsInTalent(RECALL_INSCRIPTION) == 2 ? 300 : 10).item = cls;
			} else {
				// 10/15%
				if (Random.Int(20) < 1 + hero.pointsInTalent(RECALL_INSCRIPTION)){
					Reflection.newInstance(cls).collect();
					GLog.p(Messages.get(Talent.class, RECALL_INSCRIPTION.name() + ".refunded"));
				}
			}
		}
	}

	public static void onRunestoneUsed( Hero hero, int pos, Class<?extends Item> cls ){
		if (hero.hasTalent(RECALL_INSCRIPTION) && Runestone.class.isAssignableFrom(cls)){
			if (hero.heroClass == HeroClass.CLERIC){
				Buff.prolong(hero, RecallInscription.UsedItemTracker.class, hero.pointsInTalent(RECALL_INSCRIPTION) == 2 ? 300 : 10).item = cls;
			} else {

				//don't trigger on 1st intuition use
				if (cls.equals(StoneOfIntuition.class) && hero.buff(StoneOfIntuition.IntuitionUseTracker.class) != null){
					return;
				}
				// 10/15%
				if (Random.Int(20) < 1 + hero.pointsInTalent(RECALL_INSCRIPTION)){
					Reflection.newInstance(cls).collect();
					GLog.p(Messages.get(Talent.class, RECALL_INSCRIPTION.name() + ".refunded"));
				}
			}
		}
	}

	public static void onArtifactUsed( Hero hero ){
		TransformationBossTalents.onArtifactUsed(hero);
		if (hero.hasTalent(ENHANCED_RINGS)){
			Buff.prolong(hero, EnhancedRings.class, 3f*hero.pointsInTalent(ENHANCED_RINGS));
		}

		if (Dungeon.hero.heroClass != HeroClass.CLERIC
				&& Dungeon.hero.hasTalent(Talent.DIVINE_SENSE)){
			Buff.prolong(Dungeon.hero, DivineSense.DivineSenseTracker.class, Dungeon.hero.cooldown()+1);
		}

		// 10/20/30%
		if (Dungeon.hero.heroClass != HeroClass.CLERIC
				&& Dungeon.hero.hasTalent(Talent.CLEANSE)
				&& Random.Int(10) < Dungeon.hero.pointsInTalent(Talent.CLEANSE)){
			boolean removed = false;
			for (Buff b : Dungeon.hero.buffs()) {
				if (b.type == Buff.buffType.NEGATIVE
						&& !(b instanceof LostInventory)) {
					b.detach();
					removed = true;
				}
			}
			if (removed && Dungeon.hero.sprite != null) {
				new Flare( 6, 32 ).color(0xFF4CD2, true).show( Dungeon.hero.sprite, 2f );
			}
		}
	}

	public static void onItemEquipped( Hero hero, Item item ){
		RearrangedTalents.onItemEquipped(hero, item); //rearranged
		boolean identify = false;
		if (hero.pointsInTalent(VETERANS_INTUITION) == 2 && item instanceof Armor){
			identify = true;
		}
		if (hero.hasTalent(THIEFS_INTUITION) && item instanceof Ring){
			if (hero.pointsInTalent(THIEFS_INTUITION) == 2){
				identify = true;
			}
			((Ring) item).setKnown();
		}
		if (hero.pointsInTalent(ADVENTURERS_INTUITION) == 2 && item instanceof Weapon){
			identify = true;
		}

		if (identify) {
			if (ShardOfOblivion.passiveIDDisabled()) {
				if (item instanceof Weapon){
					((Weapon) item).setIDReady();
				} else if (item instanceof Armor){
					((Armor) item).setIDReady();
				} else if (item instanceof Ring){
					((Ring) item).setIDReady();
				}
			} else {
				item.identify();
			}
		}
	}

	public static void onItemCollected( Hero hero, Item item ){
		RearrangedTalents.onItemCollected(hero, item); //rearranged
		TransformationTalents.onItemCollected(hero, item);
		if (hero.pointsInTalent(THIEFS_INTUITION) == 2){
			if (item instanceof Ring) ((Ring) item).setKnown();
		}
	}

	public static int onAttackProc( Hero hero, Char enemy, int dmg ){
		dmg = RearrangedRework.dashAttack(hero, dmg); //rearranged
		if (hero.hasTalent(STRENGTH_GREATEST)){
			dmg += Hero.heroDamageIntRange( 0, hero.pointsInTalent(STRENGTH_GREATEST) );
		}
		dmg = onSlimeAttack(hero, enemy, dmg);
		dmg = onNinjaAttack(hero, enemy, dmg);
		dmg = onDMAttack(hero, enemy, dmg);
		dmg = onPrincessAttack(hero, enemy, dmg);

		//Transformation PD, the Friar's Crystal Gunpowder and Bullseye
		if (hero.hasTalent(CRYSTAL_GUNPOWDER)){
			dmg += Math.min(2 * hero.pointsInTalent(CRYSTAL_GUNPOWDER), Dungeon.energy * hero.pointsInTalent(CRYSTAL_GUNPOWDER));
		}
		if (hero.belongings.attackingWeapon() instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.Dart
				&& hero.hasTalent(BULLSEYE) && enemy.alignment != hero.alignment){
			dmg += hero.pointsInTalent(BULLSEYE) == 1 ? 3 : 5;
		}

		if (hero.hasTalent(Talent.PROVOKED_ANGER)
			&& hero.buff(ProvokedAngerTracker.class) != null){
			dmg += 1 + 2*hero.pointsInTalent(Talent.PROVOKED_ANGER);
			hero.buff(ProvokedAngerTracker.class).detach();
		}

		if (hero.hasTalent(Talent.LINGERING_MAGIC)
				&& hero.buff(LingeringMagicTracker.class) != null){
			dmg += Random.IntRange(hero.pointsInTalent(Talent.LINGERING_MAGIC) , 2);
			hero.buff(LingeringMagicTracker.class).detach();
		}

		if (hero.hasTalent(Talent.VOLATILE_CHAIN)
				&& hero.buff(VolatileChainTracker.class) != null
				&& hero.buff(VolatileChainTracker.class).object == enemy.id()){
			dmg += 1 + hero.pointsInTalent(Talent.VOLATILE_CHAIN);
			hero.buff(VolatileChainTracker.class).detach();
		}

		if (hero.hasTalent(Talent.UNHOLY_WRATH)
				&& hero.buff(UnholyWrathTracker.class) != null){
			Buff.affect( enemy, Bleeding.class ).set( Math.round(dmg * (Dungeon.hero.pointsInTalent(Talent.PAIN_IS_GAIN)*0.05  + 0.1) ));
			hero.buff(UnholyWrathTracker.class).detach();
		}

		if (hero.hasTalent(Talent.WRATHFUL_STRIKE)
				&& hero.buff(Invulnerability.class) != null){
			dmg += hero.pointsInTalent(Talent.WRATHFUL_STRIKE);
		}

		if (hero.hasTalent(Talent.SUCKER_PUNCH)
				&& enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)
				&& enemy.buff(SuckerPunchTracker.class) == null){
			dmg += Random.IntRange(hero.pointsInTalent(Talent.SUCKER_PUNCH) , 2);
			Buff.affect(enemy, SuckerPunchTracker.class);
		}

		if (hero.hasTalent(Talent.FOLLOWUP_STRIKE) && enemy.isAlive() && enemy.alignment == Char.Alignment.ENEMY) {
			if (hero.belongings.attackingWeapon() instanceof MissileWeapon) {
				Buff.prolong(hero, FollowupStrikeTracker.class, 5f).object = enemy.id();
			} else if (hero.buff(FollowupStrikeTracker.class) != null
					&& hero.buff(FollowupStrikeTracker.class).object == enemy.id()){
				dmg += 1 + hero.pointsInTalent(FOLLOWUP_STRIKE);
				RearrangedRework.followupStrike(hero, enemy); //rearranged
				hero.buff(FollowupStrikeTracker.class).detach();
			}
		}

		if (hero.buff(Talent.SpiritBladesTracker.class) != null
				&& Random.Int(10) < 3*hero.pointsInTalent(Talent.SPIRIT_BLADES)){
			SpiritBow bow = hero.belongings.getItem(SpiritBow.class);
			if (bow != null) dmg = bow.proc( hero, enemy, dmg );
			hero.buff(Talent.SpiritBladesTracker.class).detach();
		}

		if (hero.hasTalent(PATIENT_STRIKE)){
			if (hero.buff(PatientStrikeTracker.class) != null
					&& !(hero.belongings.attackingWeapon() instanceof MissileWeapon)){
				hero.buff(PatientStrikeTracker.class).detach();
				dmg += Random.IntRange(hero.pointsInTalent(Talent.PATIENT_STRIKE), 2);
			}
		}

		if (hero.hasTalent(DEADLY_FOLLOWUP) && enemy.alignment == Char.Alignment.ENEMY) {
			if (hero.belongings.attackingWeapon() instanceof MissileWeapon) {
				if (!(hero.belongings.attackingWeapon() instanceof SpiritBow.SpiritArrow)) {
					Buff.prolong(hero, DeadlyFollowupTracker.class, 5f).object = enemy.id();
				}
			} else if (hero.buff(DeadlyFollowupTracker.class) != null
					&& hero.buff(DeadlyFollowupTracker.class).object == enemy.id()){
				dmg = Math.round(dmg * (1.0f + .1f*hero.pointsInTalent(DEADLY_FOLLOWUP)));
			}
		}

		dmg = RearrangedTalents.onAttackProc(hero, enemy, dmg); //rearranged

		dmg = TransformationTalents.onAttackProc(hero, enemy, dmg);
		return dmg;
	}

	//rearranged, buffs of the talents in its rework of Shattered's heroes
	public static class AgressiveMovementAbilityTracker extends FlavourBuff{
		public boolean wepAbilUsed = false;
	}
	public static class ChainCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0.2f, 0.5f, 0.8f); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 10); }
		public String toString() { return Messages.get(this, "name"); }
		public String desc() { return Messages.get(this, "desc", dispTurns(visualcooldown())); }
		public void spendTime() { spend(-1f); }
	};
	public static class ChaseCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0.4f, 0.4f, 0.8f); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / (15)); }
		public String toString() { return Messages.get(this, "name"); }
		public String desc() { return Messages.get(this, "desc", dispTurns(visualcooldown())); }
		public void spendTime() { spend(-1f); }
	};
	public static class CounterAttackTracker extends Buff{};
	public static class KickCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0xF27318); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 10); }
		public String toString() { return Messages.get(this, "name"); }
		public String desc() { return Messages.get(this, "desc", dispTurns(visualcooldown())); }
	};
	public static class LethalCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0.8f, 0.1f, 0.1f); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 5); }
		public String toString() { return Messages.get(this, "name"); }
		public String desc() { return Messages.get(this, "desc", dispTurns(visualcooldown())); }
		public void spendTime() { spend(-1f); }
	};
	public static class MaxRageCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0xFF3333); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 50); }
		public String toString() { return Messages.get(this, "name"); }
		public String desc() { return Messages.get(this, "desc", dispTurns(visualcooldown())); }
	};
	public static class ParryCooldown extends Buff{
		float cooldown;
		public void set() {
			cooldown = 90-20*Dungeon.hero.pointsInTalent(Talent.PARRY)+1;
		}
		@Override
		public boolean act() {
			cooldown -= Actor.TICK;
			if (cooldown <= 0) {
				Buff.affect(target, ParryTracker.class);
				detach();
			}
			if (!Dungeon.hero.hasTalent(Talent.PARRY)) {
				detach();
			}
			spend(Actor.TICK);
			return true;
		}
	};
	public static class ParryTracker extends Buff{
		{ actPriority = HERO_PRIO+1;}

		{
			type = buffType.NEUTRAL;
			announced = true;
		}

		@Override
		public int icon() {
			return BuffIndicator.PARRY;
		}

		@Override
		public boolean act() {
			if (!Dungeon.hero.hasTalent(Talent.PARRY)) {
				detach();
			}
			spend(Actor.TICK);
			return true;
		}

		@Override
		public void detach() {
			super.detach();
			Buff.affect(target, ParryCooldown.class).set();
		}
	};
	public static class QuickFollowupCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0x651F66); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 10); }
		public String toString() { return Messages.get(this, "name"); }
		public String desc() { return Messages.get(this, "desc", dispTurns(visualcooldown())); }
	};
	public static class QuickFollowupTracker extends FlavourBuff{};
	public static class QuickStep extends FlavourBuff {

		{
			type = buffType.NEUTRAL;
			announced = false;
		}

		public static final float DURATION	= 1f;

		@Override
		public int icon() {
			return BuffIndicator.HASTE;
		}

		@Override
		public void tintIcon(Image icon) {
			icon.hardlight(0x2364BC);
		}

		@Override
		public float iconFadePercent() {
			return Math.max(0, (DURATION - visualcooldown()) / DURATION);
		}

		@Override
		public String toString() {
			return Messages.get(this, "name");
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", dispTurns());
		}
	}
	public static class RiposteTracker extends Buff {
		{
			actPriority = VFX_PRIO;
		}

		public Char enemy;

		@Override
		public boolean act() {
			target.sprite.attack(enemy.pos, new com.watabou.utils.Callback() {
				@Override
				public void call() {
					com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator.target(enemy);
					if (Dungeon.hero.attack(enemy, 1f, 0, 1)) {
						Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
					}

					next();
				}
			});
			detach();
			return false;
		}
	}
	public static class ShootingEyesTracker extends Buff{};
	public static class SkilledHandTracker extends Buff{};
	public static class WardingLightCooldown extends FlavourBuff{
		@Override
		public int icon() {
			return BuffIndicator.TIME;
		}
		public void tintIcon(Image icon) { icon.hardlight(0f, 1f, 1f); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 20); }
	}
	public static class ProvokedAngerTracker extends FlavourBuff{
		{ type = Buff.buffType.POSITIVE; }
		public int icon() { return BuffIndicator.WEAPON; }
		public void tintIcon(Image icon) { icon.hardlight(1.43f, 1.43f, 1.43f); }
		public float iconFadePercent() { return Math.max(0, 1f - (visualcooldown() / 5)); }
	}
	public static class LingeringMagicTracker extends FlavourBuff{
		{ type = Buff.buffType.POSITIVE; }
		public int icon() { return BuffIndicator.WEAPON; }
		public void tintIcon(Image icon) { icon.hardlight(1.43f, 1.43f, 0f); }
		public float iconFadePercent() { return Math.max(0, 1f - (visualcooldown() / 5)); }
	}
	public static class SuckerPunchTracker extends Buff{};
	public static class FollowupStrikeTracker extends FlavourBuff{
		public int object;
		{ type = Buff.buffType.POSITIVE; }
		public int icon() { return BuffIndicator.INVERT_MARK; }
		public void tintIcon(Image icon) { icon.hardlight(0f, 0.75f, 1f); }
		public float iconFadePercent() { return Math.max(0, 1f - (visualcooldown() / 5)); }
		private static final String OBJECT    = "object";
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(OBJECT, object);
		}
		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			object = bundle.getInt(OBJECT);
		}
	};

	public static final int MAX_TALENT_TIERS = 4;

	public static void initClassTalents( Hero hero ){
		initClassTalents( hero.heroClass, hero.talents, hero.metamorphedTalents );
	}

	public static void initClassTalents( HeroClass cls, ArrayList<LinkedHashMap<Talent, Integer>> talents){
		initClassTalents( cls, talents, new LinkedHashMap<>());
	}

	public static void initClassTalents( HeroClass cls, ArrayList<LinkedHashMap<Talent, Integer>> talents, LinkedHashMap<Talent, Talent> replacements ){
		while (talents.size() < MAX_TALENT_TIERS){
			talents.add(new LinkedHashMap<>());
		}

		ArrayList<Talent> tierTalents = new ArrayList<>();

		//tier 1
		switch (cls){
			case WARRIOR: default:
				Collections.addAll(tierTalents, HEARTY_MEAL, VETERANS_INTUITION, PROVOKED_ANGER, IRON_WILL);
				break;
			case MAGE:
				Collections.addAll(tierTalents, EMPOWERING_MEAL, SCHOLARS_INTUITION, LINGERING_MAGIC, BACKUP_BARRIER);
				break;
			case ROGUE:
				Collections.addAll(tierTalents, CACHED_RATIONS, THIEFS_INTUITION, SUCKER_PUNCH, PROTECTIVE_SHADOWS);
				break;
			case HUNTRESS:
				Collections.addAll(tierTalents, NATURES_BOUNTY, SURVIVALISTS_INTUITION, FOLLOWUP_STRIKE, NATURES_AID);
				break;
			case DUELIST:
				Collections.addAll(tierTalents, STRENGTHENING_MEAL, ADVENTURERS_INTUITION, PATIENT_STRIKE, AGGRESSIVE_BARRIER);
				break;
			case CLERIC:
				Collections.addAll(tierTalents, SATIATED_SPELLS, HOLY_INTUITION, SEARING_LIGHT, SHIELD_OF_LIGHT);
				break;
			case ZEALOT:
				Collections.addAll(tierTalents, ARCANE_MEAL, DEMONIC_INTUITION, WRATHFUL_STRIKE, PAIN_IS_GAIN);
				break;
			case ARTIFICER:
				Collections.addAll(tierTalents, TINKERERS_MEAL, PATTERN_RECOGNITION, VOLATILE_CHAIN, ARCSHIELDING);
				break;
			case SUMMONER:
				//Ryuse's PD had no talents of its own for the Summoner, these are Shattered's picked to suit her
				Collections.addAll(tierTalents, HEARTY_MEAL, SURVIVALISTS_INTUITION, SCHOLARS_INTUITION, BACKUP_BARRIER);
				break;
			case VIKING:
				//Dragonic PD gave its heroes no talents, these are Shattered's picked to suit them
				Collections.addAll(tierTalents, HEARTY_MEAL, VETERANS_INTUITION, PROVOKED_ANGER, IRON_WILL);
				break;
			case DRAGONKNIGHT:
				Collections.addAll(tierTalents, HEARTY_MEAL, SURVIVALISTS_INTUITION, PROVOKED_ANGER, BACKUP_BARRIER);
				break;
			case FRIAR:
				Collections.addAll(tierTalents, TRANQUIL_TINCTURE, RAVENS_EYE);
				break;
			case SLIMEGIRL:
				Collections.addAll(tierTalents, RESILIENT_MEAL, LIQUID_ARMOR);
				break;
			case NINJA:
				Collections.addAll(tierTalents, NINJA_MEAL, HUNTING_INTUITION);
				break;
			case DM400:
				Collections.addAll(tierTalents, MARK_MEAL, TARGET_TARGETING);
				break;
			case PRINCESS:
				Collections.addAll(tierTalents, ENCHANT_MEAL, RUNE_BLADE);
				break;
			//rearranged
			case GUNNER: case SAMURAI: case ADVENTURER: case KNIGHT: case MEDIC: case ARCHER:
				RearrangedTalents.addClassTalents(cls, 1, tierTalents);
				break;
			//summoning, Summoning PD gave the Conjurer no talents of her own, these are Shattered's picked to suit her
			case CONJURER:
				Collections.addAll(tierTalents, HEARTY_MEAL, EMPOWERING_MEAL, SCHOLARS_INTUITION, NATURES_BOUNTY);
				break;
			case FREEMAN:
				Collections.addAll(tierTalents, POTENTIAL_1, POTENTIAL_2, BOSS_TALENT_SLOT_1, BOSS_TALENT_SLOT_2);
				break;
			//tower, Tower PD gave its heroes no talents, these are Shattered's picked to suit them
			case NECROHERO:
				Collections.addAll(tierTalents, CACHED_RATIONS, SCHOLARS_INTUITION, SUCKER_PUNCH, PATIENT_STRIKE);
				break;
			case TANK:
				Collections.addAll(tierTalents, HEARTY_MEAL, VETERANS_INTUITION, PROVOKED_ANGER, AGGRESSIVE_BARRIER);
				break;
			case TOWERPRIEST:
				Collections.addAll(tierTalents, NATURES_BOUNTY, SURVIVALISTS_INTUITION, NATURES_AID, AGGRESSIVE_BARRIER);
				break;
			//xun, Xun's PD's three heroes
			case EXPLORER:
				Collections.addAll(tierTalents, KEEN_MEAL, SECRET_FORESIGHT, TESTED_AWARENESS, HOME_ADVANTAGE, SAFE_SURVEY);
				break;
			case WRAITH:
				Collections.addAll(tierTalents, ANCESTRAL_TRIBUTE, BLOOD_INTUITION, TESTED_ANTIMAGIC, BURIAL_CEREMONY, SAFE_PRICK);
				break;
			case ENGINEER:
				Collections.addAll(tierTalents, EAT_LITTLE_AND_OFTEN, FINE_INTUITION, TESTED_MAINTENANCE, GENERAL_DISARM, PULSE_ENERGY);
				break;
		}
		for (Talent talent : tierTalents){
			if (replacements.containsKey(talent)){
				talent = replacements.get(talent);
			}
			talents.get(0).put(talent, 0);
		}
		tierTalents.clear();
		RearrangedRework.extendClassTier(cls, 1, talents.get(0), replacements); //rearranged

		//tier 2
		switch (cls){
			case WARRIOR: default:
				Collections.addAll(tierTalents, IRON_STOMACH, LIQUID_WILLPOWER, RUNIC_TRANSFERENCE, LETHAL_MOMENTUM, IMPROVISED_PROJECTILES);
				break;
			case MAGE:
				Collections.addAll(tierTalents, ENERGIZING_MEAL, INSCRIBED_POWER, WAND_PRESERVATION, ARCANE_VISION, SHIELD_BATTERY);
				break;
			case ROGUE:
				Collections.addAll(tierTalents, MYSTICAL_MEAL, INSCRIBED_STEALTH, WIDE_SEARCH, SILENT_STEPS, ROGUES_FORESIGHT);
				break;
			case HUNTRESS:
				Collections.addAll(tierTalents, INVIGORATING_MEAL, LIQUID_NATURE, REJUVENATING_STEPS, HEIGHTENED_SENSES, DURABLE_PROJECTILES);
				break;
			case DUELIST:
				Collections.addAll(tierTalents, FOCUSED_MEAL, LIQUID_AGILITY, WEAPON_RECHARGING, LETHAL_HASTE, SWIFT_EQUIP);
				break;
			case CLERIC:
				Collections.addAll(tierTalents, ENLIGHTENING_MEAL, RECALL_INSCRIPTION, SUNRAY, DIVINE_SENSE, BLESS);
				break;
			case ZEALOT:
				Collections.addAll(tierTalents, CLEANSING_MEAL, CONFIRMED_BELIEF, HIDDEN_POWER, INSTANT_GRATIFICATION, ESOTERIC_MIND);
				break;
			case ARTIFICER:
				Collections.addAll(tierTalents, QUICK_CALIBRATION, REACTIVE_CHEMISTRY, OVERCLOCKED_SYSTEMS, VOLATILE_SALVAGE, ADAPTIVE_MINEFIELD);
				break;
			case SUMMONER:
				Collections.addAll(tierTalents, INVIGORATING_MEAL, LIQUID_NATURE, ARCANE_VISION, HEIGHTENED_SENSES, SHIELD_BATTERY);
				break;
			case VIKING:
				Collections.addAll(tierTalents, IRON_STOMACH, LIQUID_WILLPOWER, RUNIC_TRANSFERENCE, LETHAL_MOMENTUM, IMPROVISED_PROJECTILES);
				break;
			case DRAGONKNIGHT:
				Collections.addAll(tierTalents, IRON_STOMACH, LIQUID_NATURE, RUNIC_TRANSFERENCE, LETHAL_MOMENTUM, IMPROVISED_PROJECTILES);
				break;
			case FRIAR:
				Collections.addAll(tierTalents, WHISPERING_MEAL, LIQUID_BARRIER, FAST_RELOAD);
				break;
			case SLIMEGIRL:
				Collections.addAll(tierTalents, TOUGH_MEAL, SLIME_GREENHOUSE, ENERGY_ABSORPTION);
				break;
			case NINJA:
				Collections.addAll(tierTalents, YUNYING_MEAL, XIA, FEINT);
				break;
			case DM400:
				Collections.addAll(tierTalents, OVER_MEAL, RECOVER_CHARGE, BODY_REINFORCE);
				break;
			case PRINCESS:
				Collections.addAll(tierTalents, ROYAL_MEAL, RUNE_EXPERT, MORE_RING);
				break;
			//rearranged
			case GUNNER: case SAMURAI: case ADVENTURER: case KNIGHT: case MEDIC: case ARCHER:
				RearrangedTalents.addClassTalents(cls, 2, tierTalents);
				break;
			//summoning
			case CONJURER:
				Collections.addAll(tierTalents, INVIGORATING_MEAL, LIQUID_NATURE, ARCANE_VISION, HEIGHTENED_SENSES, SHIELD_BATTERY);
				break;
			case FREEMAN:
				Collections.addAll(tierTalents, POTENTIAL_3, POTENTIAL_4, POTENTIAL_5, BOSS_TALENT_SLOT_3, BOSS_TALENT_SLOT_4);
				break;
			//tower
			case NECROHERO:
				Collections.addAll(tierTalents, MYSTICAL_MEAL, INSCRIBED_STEALTH, WIDE_SEARCH, SILENT_STEPS, ROGUES_FORESIGHT);
				break;
			case TANK:
				Collections.addAll(tierTalents, IRON_STOMACH, LIQUID_WILLPOWER, LETHAL_MOMENTUM, IMPROVISED_PROJECTILES, SWIFT_EQUIP);
				break;
			case TOWERPRIEST:
				Collections.addAll(tierTalents, INVIGORATING_MEAL, LIQUID_NATURE, REJUVENATING_STEPS, HEIGHTENED_SENSES, DURABLE_PROJECTILES);
				break;
			//xun
			case EXPLORER:
				Collections.addAll(tierTalents, PREPARING_MEAL, LIQUID_CLAIRVOYANCE, BARBED_WIRE, WINDING_PORCH, REKINDLED_EMBER, AGGRESSIVE_ROADBLOCK);
				break;
			case WRAITH:
				Collections.addAll(tierTalents, TEARING_MEAL, INSCRIBED_REGENERATION, BLURING_BODY, PSIONIC_BLAST, SCAPEGOAT, THROWN_EVIL);
				break;
			case ENGINEER:
				Collections.addAll(tierTalents, TOILSOME_MEAL, IONIC_LIQUID, STRONG_PULSE, RESONANT_SENSING, APART_ANYTHING, REMOTE_DESTRUCTION);
				break;
		}
		for (Talent talent : tierTalents){
			if (replacements.containsKey(talent)){
				talent = replacements.get(talent);
			}
			talents.get(1).put(talent, 0);
		}
		tierTalents.clear();
		RearrangedRework.extendClassTier(cls, 2, talents.get(1), replacements); //rearranged

		//tier 3
		switch (cls){
			case WARRIOR: default:
				Collections.addAll(tierTalents, HOLD_FAST, STRONGMAN);
				break;
			case MAGE:
				Collections.addAll(tierTalents, DESPERATE_POWER, ALLY_WARP);
				break;
			case ROGUE:
				Collections.addAll(tierTalents, ENHANCED_RINGS, LIGHT_CLOAK);
				break;
			case HUNTRESS:
				Collections.addAll(tierTalents, POINT_BLANK, SEER_SHOT);
				break;
			case DUELIST:
				Collections.addAll(tierTalents, PRECISE_ASSAULT, DEADLY_FOLLOWUP);
				break;
			case CLERIC:
				Collections.addAll(tierTalents, CLEANSE, LIGHT_READING);
				break;
			case ZEALOT:
				Collections.addAll(tierTalents, DEMOLITIONIST, UNHOLY_WRATH);
				break;
			case ARTIFICER:
				Collections.addAll(tierTalents, MECHANICAL_GOGGLES, EFFECTIVE_SHOT);
				break;
			case SUMMONER:
				Collections.addAll(tierTalents, ALLY_WARP, HOLD_FAST);
				break;
			case VIKING:
			case DRAGONKNIGHT:
				Collections.addAll(tierTalents, HOLD_FAST, STRONGMAN);
				break;
			case FRIAR:
				Collections.addAll(tierTalents, HUMAN_GLORY);
				break;
			case SLIMEGIRL:
				Collections.addAll(tierTalents, ORIGINAL_MONSTER);
				break;
			case NINJA:
				Collections.addAll(tierTalents, LIGHT_BOX);
				break;
			case DM400:
				Collections.addAll(tierTalents, QUICK_TOOL);
				break;
			case PRINCESS:
				Collections.addAll(tierTalents, RING_BOND);
				break;
			//rearranged
			case GUNNER: case SAMURAI: case ADVENTURER: case KNIGHT: case MEDIC: case ARCHER:
				RearrangedTalents.addClassTalents(cls, 3, tierTalents);
				break;
			//summoning
			case CONJURER:
				Collections.addAll(tierTalents, ALLY_WARP, HOLD_FAST);
				break;
			case FREEMAN:
				Collections.addAll(tierTalents, POTENTIAL_6, POTENTIAL_7, POTENTIAL_8, POTENTIAL_9, BOSS_TALENT_SLOT_5);
				break;
			//tower
			case NECROHERO:
				Collections.addAll(tierTalents, ALLY_WARP, ENHANCED_RINGS);
				break;
			case TANK:
				Collections.addAll(tierTalents, HOLD_FAST, STRONGMAN);
				break;
			case TOWERPRIEST:
				Collections.addAll(tierTalents, ALLY_WARP, HOLD_FAST);
				break;
			//xun
			case EXPLORER:
				Collections.addAll(tierTalents, DEMOLITION, DIG_THE_WELL, CONVENIENT_SHOVEL);
				break;
			case WRAITH:
				Collections.addAll(tierTalents, VICIOUS_BETRAYAL, CURSED_POWER, WICKED_GROWTH);
				break;
			case ENGINEER:
				Collections.addAll(tierTalents, DURABLE_MODIFIES, ELECTRONIC_REPAIR, IONIZING_RADIATION);
				break;
		}
		for (Talent talent : tierTalents){
			if (replacements.containsKey(talent)){
				talent = replacements.get(talent);
			}
			talents.get(2).put(talent, 0);
		}
		tierTalents.clear();

		//tier4
		//TBD
	}

	public static void initSubclassTalents( Hero hero ){
		initSubclassTalents( hero.subClass, hero.talents );
		//transformation, subclass talents swapped by a metamorphosis prism
		if (hero.talents.size() > 2){
			LinkedHashMap<Talent, Integer> tier = hero.talents.get(2);
			for (Talent k : hero.metamorphedTalents.keySet()){
				Talent v = hero.metamorphedTalents.get(k);
				if (tier.containsKey(k) && !tier.containsKey(v)){
					LinkedHashMap<Talent, Integer> newTier = new LinkedHashMap<>();
					for (Talent t : tier.keySet()) newTier.put(t == k ? v : t, tier.get(t));
					hero.talents.set(2, newTier);
					tier = newTier;
				}
			}
		}
	}

	public static void initSubclassTalents( HeroSubClass cls, ArrayList<LinkedHashMap<Talent, Integer>> talents ){
		if (cls == HeroSubClass.NONE) return;

		while (talents.size() < MAX_TALENT_TIERS){
			talents.add(new LinkedHashMap<>());
		}

		ArrayList<Talent> tierTalents = new ArrayList<>();

		//tier 3
		switch (cls){
			case BERSERKER: default:
				Collections.addAll(tierTalents, ENDLESS_RAGE, DEATHLESS_FURY, ENRAGED_CATALYST);
				break;
			case GLADIATOR:
				Collections.addAll(tierTalents, CLEAVE, LETHAL_DEFENSE, ENHANCED_COMBO);
				break;
			case BATTLEMAGE:
				Collections.addAll(tierTalents, EMPOWERED_STRIKE, MYSTICAL_CHARGE, EXCESS_CHARGE);
				break;
			case WARLOCK:
				Collections.addAll(tierTalents, SOUL_EATER, SOUL_SIPHON, NECROMANCERS_MINIONS);
				break;
			case ASSASSIN:
				Collections.addAll(tierTalents, ENHANCED_LETHALITY, ASSASSINS_REACH, BOUNTY_HUNTER);
				break;
			case FREERUNNER:
				Collections.addAll(tierTalents, EVASIVE_ARMOR, PROJECTILE_MOMENTUM, SPEEDY_STEALTH);
				break;
			case SNIPER:
				Collections.addAll(tierTalents, FARSIGHT, SHARED_ENCHANTMENT, SHARED_UPGRADES);
				break;
			case WARDEN:
				Collections.addAll(tierTalents, DURABLE_TIPS, BARKSKIN, SHIELDING_DEW);
				break;
			case CHAMPION:
				Collections.addAll(tierTalents, VARIED_CHARGE, TWIN_UPGRADES, COMBINED_LETHALITY);
				break;
			case MONK:
				Collections.addAll(tierTalents, UNENCUMBERED_SPIRIT, MONASTIC_VIGOR, COMBINED_ENERGY);
				break;
			case PRIEST:
				Collections.addAll(tierTalents, HOLY_LANCE, HALLOWED_GROUND, MNEMONIC_PRAYER);
				break;
			case PALADIN:
				Collections.addAll(tierTalents, LAY_ON_HANDS, AURA_OF_PROTECTION, WALL_OF_LIGHT);
				break;
			case CONJURER:
				Collections.addAll(tierTalents, ELDRITCH_ENERGY, HIVE_MIND, TAG_TEAM);
				break;
			case DEVOTEE:
				Collections.addAll(tierTalents, NIHILISM, GOURMAND, DEVOUR_RITUAL);
				break;
			case CONSTRUCTOR:
				Collections.addAll(tierTalents, CONSTRUCT_VISION, CONSTRUCT_MOBILITY, CONSTRUCT_LETHALITY);
				break;
			case ARMORER:
				Collections.addAll(tierTalents, WEAPON_MOD_AUTOLOAD, ARMOR_MOD_INFINITE_FALLING, ARMOR_MOD_EMERGENCY_DEFENSE);
				break;
			case DEVORANDUM:
				Collections.addAll(tierTalents, BARKSKIN, SHIELDING_DEW, SOUL_EATER);
				break;
			case MESSOREM:
				Collections.addAll(tierTalents, ENHANCED_LETHALITY, EMPOWERED_STRIKE, ASSASSINS_REACH);
				break;
			case DRUID:
				Collections.addAll(tierTalents, BARKSKIN, SHIELDING_DEW, EVASIVE_ARMOR);
				break;
			case ICEBREAKER:
				Collections.addAll(tierTalents, EMPOWERED_STRIKE, EVASIVE_ARMOR, LAY_ON_HANDS);
				break;
			case EIDOLON:
				Collections.addAll(tierTalents, NECROMANCERS_MINIONS, SOUL_SIPHON, SHARED_UPGRADES);
				break;
			case ATTRACTOR:
				Collections.addAll(tierTalents, AURA_OF_PROTECTION, WALL_OF_LIGHT, SHIELDING_DEW);
				break;
			case ALCHEMIST:
				Collections.addAll(tierTalents, ALCHEMY_SHIELD, CLOSE_BLAST, CONSERVATION);
				break;
			case PIOUS:
				Collections.addAll(tierTalents, NEVER_COMPROMISE, REWIND_TIME, BRIGHT_WARRIOR);
				break;
			case WATERSLIME:
				Collections.addAll(tierTalents, WATER_BODY, WATER_REVIVAL, WATER_REGENERATION);
				break;
			case DARKSLIME:
				Collections.addAll(tierTalents, POTENT_OOZE, DARK_GAS, DARK_LIQUID);
				break;
			case TATTEKI_NINJA:
				Collections.addAll(tierTalents, SOKO, KONO_FUKUSA, KUNIKUCHI);
				break;
			case NINJA_MASTER:
				Collections.addAll(tierTalents, SOUL_HUNTING, USE_ENVIRONMENT, MIND_WATER);
				break;
			case AT400:
				Collections.addAll(tierTalents, SUSTAIN_MARK, BATTLE_UPGRADE, FLY_DRONE);
				break;
			case AU400:
				Collections.addAll(tierTalents, SPECIAL_MARK, ASSIST_UPGRADE, FAST_CRUISE);
				break;
			case RUNEMAGE:
				Collections.addAll(tierTalents, DARKMARK, RUNE_BLAST, RUNE_SURGE);
				break;
			case COMBATMASTER:
				Collections.addAll(tierTalents, FLUENT, STANCE_MASTERY, INELEMENT);
				break;
			//rearranged
			case OUTLAW: case GUNSLINGER: case SPECIALIST: case SLASHER: case MASTER: case SLAYER:
			case ENGINEER: case EXPLORER: case RESEARCHER: case DEATHKNIGHT: case HORSEMAN: case CRUSADER:
			case SAVIOR: case THERAPIST: case MEDICALOFFICER: case BOWMASTER: case JUGGLER: case SHARPSHOOTER:
				RearrangedTalents.addSubclassTalents(cls, tierTalents);
				break;
			//summoning, Shattered talents picked for Summoning PD's Conjurer subclasses
			case SOUL_REAVER:
				Collections.addAll(tierTalents, NECROMANCERS_MINIONS, SOUL_SIPHON, SHARED_UPGRADES);
				break;
			case OCCULTIST:
				Collections.addAll(tierTalents, ENHANCED_LETHALITY, ASSASSINS_REACH, BOUNTY_HUNTER);
				break;
			//xun, Xun's PD's subclasses have five talents each
			case WAVECHASER:
				Collections.addAll(tierTalents, RIVER_EROSION, SON_OF_SEA, DROWNING, LAKE_DEVELOPMENT, UNDERCURRENT);
				break;
			case TRAPPER:
				Collections.addAll(tierTalents, XUN_TRAP_MASTER, FRIENDLY_MECHANISM, SIMPLE_STRUCTURE, SENSITIVE_PEDAL, LIQUID_COLLECTING);
				break;
			case ROCKSY:
				Collections.addAll(tierTalents, METEROIC_IRON, ROCK_PROTECTOR, MIND_CONTROL, DESTRUCTIVE_STRIKE, METEOR_CRATER);
				break;
			case INCUBUS:
				Collections.addAll(tierTalents, LULLABY, SLEEPWALKING, SLEEPING_IN, WRONG_SIDE_OF_THE_BED, NIGHTMARE_HAUNTING);
				break;
			case PLAGUEGOD:
				Collections.addAll(tierTalents, CORPSE_DECAY, HOMEMADE_DRUG, MAGICAL_VENT, ACID_RAIN, PLAGUE_EUCHARIST);
				break;
			case SOULHANDLER:
				Collections.addAll(tierTalents, IMMEDIATE_USE, VENGEFUL_SPIRIT, SOUL_CAGING, DEVOUR_SURGING, OVERFLOW);
				break;
			case CRAFTSMAN:
				Collections.addAll(tierTalents, ACTIVE_REPAIR, MULTI_MODIFY, PART_RECYCLING, FAVORITE_WORK, KINETIC_FRAGMENT);
				break;
			case HACKER:
				Collections.addAll(tierTalents, BACKFIRE, DIFFRACTION, ELECTRIC_CHARGE, DARK_MAGIC, CHARISMA);
				break;
			case GRENADIER:
				Collections.addAll(tierTalents, EXPLOSION_PROOF, XUN_SHOCKWAVE, ADDED_POWDER, MAGICAL_EXPLOSION, BALLISTICA_CALC);
				break;
		}
		RearrangedRework.extendSubclassTalents(cls, tierTalents); //rearranged
		for (Talent talent : tierTalents){
			talents.get(2).put(talent, 0);
		}
		tierTalents.clear();

	}

	public static void initArmorTalents( Hero hero ){
		initArmorTalents( hero.armorAbility, hero.talents);
	}

	public static void initArmorTalents(ArmorAbility abil, ArrayList<LinkedHashMap<Talent, Integer>> talents ){
		if (abil == null) return;

		while (talents.size() < MAX_TALENT_TIERS){
			talents.add(new LinkedHashMap<>());
		}

		for (Talent t : abil.talents()){
			talents.get(3).put(t, 0);
		}
	}

	private static final String TALENT_TIER = "talents_tier_";

	public static void storeTalentsInBundle( Bundle bundle, Hero hero ){
		for (int i = 0; i < MAX_TALENT_TIERS; i++){
			LinkedHashMap<Talent, Integer> tier = hero.talents.get(i);
			Bundle tierBundle = new Bundle();

			for (Talent talent : tier.keySet()){
				if (tier.get(talent) > 0){
					tierBundle.put(talent.name(), tier.get(talent));
				}
				if (tierBundle.contains(talent.name())){
					tier.put(talent, Math.min(tierBundle.getInt(talent.name()), talent.maxPoints()));
				}
			}
			bundle.put(TALENT_TIER+(i+1), tierBundle);
		}

		Bundle replacementsBundle = new Bundle();
		for (Talent t : hero.metamorphedTalents.keySet()){
			replacementsBundle.put(t.name(), hero.metamorphedTalents.get(t));
		}
		bundle.put("replacements", replacementsBundle);

		//transformation, negative talents are kept with the tier they sit in
		Bundle negBundle = new Bundle();
		for (int i = 0; i < MAX_TALENT_TIERS; i++){
			for (Talent t : hero.talents.get(i).keySet()){
				if (t.type() == TalentType.NEGATIVE) negBundle.put(t.name(), i);
			}
		}
		bundle.put("negative_talents", negBundle);
	}

	private static final HashSet<String> removedTalents = new HashSet<>();
	static{
		//nothing atm
	}

	private static final HashMap<String, String> renamedTalents = new HashMap<>();
	static{
		//nothing atm
	}

	public static void restoreTalentsFromBundle( Bundle bundle, Hero hero ){
		if (bundle.contains("replacements")){
			Bundle replacements = bundle.getBundle("replacements");
			for (String key : replacements.getKeys()){
				String value = replacements.getString(key);
				if (renamedTalents.containsKey(key)) key = renamedTalents.get(key);
				if (renamedTalents.containsKey(value)) value = renamedTalents.get(value);
				if (!removedTalents.contains(key) && !removedTalents.contains(value)){
					try {
						hero.metamorphedTalents.put(Talent.valueOf(key), Talent.valueOf(value));
					} catch (Exception e) {
						ShatteredPixelDungeon.reportException(e);
					}
				}
			}
		}

		if (hero.heroClass != null)     initClassTalents(hero);
		if (hero.subClass != null)      initSubclassTalents(hero);
		if (hero.armorAbility != null)  initArmorTalents(hero);

		//transformation, negative talents from the Cursed talents challenge
		if (bundle.contains("negative_talents")){
			Bundle neg = bundle.getBundle("negative_talents");
			for (String key : neg.getKeys()){
				try {
					Talent t = Talent.valueOf(key);
					int tier = neg.getInt(key);
					if (tier >= 0 && tier < hero.talents.size()) hero.talents.get(tier).put(t, 0);
				} catch (Exception e) {
					ShatteredPixelDungeon.reportException(e);
				}
			}
		}

		for (int i = 0; i < MAX_TALENT_TIERS; i++){
			LinkedHashMap<Talent, Integer> tier = hero.talents.get(i);
			Bundle tierBundle = bundle.contains(TALENT_TIER+(i+1)) ? bundle.getBundle(TALENT_TIER+(i+1)) : null;

			if (tierBundle != null){
				for (String tName : tierBundle.getKeys()){
					int points = tierBundle.getInt(tName);
					if (renamedTalents.containsKey(tName)) tName = renamedTalents.get(tName);
					if (!removedTalents.contains(tName)) {
						try {
							Talent talent = Talent.valueOf(tName);
							if (tier.containsKey(talent)) {
								tier.put(talent, Math.min(points, talent.maxPoints()));
							}
						} catch (Exception e) {
							ShatteredPixelDungeon.reportException(e);
						}
					}
				}
			}
		}
	}


	//rearranged, Re-ARranged PD's talent buffs
	public static class HarvestBerriesDropped extends CounterBuff{{revivePersists = true;}};
	//gunner talent's buff
	public static class SpeedyMoveTracker extends Buff{};
	public static class RollingTracker extends FlavourBuff{};
	public static class HonorableShotTracker extends FlavourBuff{};
	public static class SkillfulRunnerCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight( 0xFFFF00 ); }
		public float iconFadePercent() { return Math.max(0, 1-visualcooldown() / 30); }
		public String toString() { return Messages.get(this, "name"); }
		public String desc() { return Messages.get(this, "desc", dispTurns(visualcooldown())); }
	};
	public static class IntoTheShadowCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight( 0x7FA9D2 ); }
		public float iconFadePercent() { return Math.max(0, 1-visualcooldown() / 15); }
		public String toString() { return Messages.get(this, "name"); }
		public String desc() { return Messages.get(this, "desc", dispTurns(visualcooldown())); }
	};


	//adventurer talent buff
	//1-4
	public static class ProtectiveSlashCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0.8f, 0.8f, 0.8f); }
		public float iconFadePercent() { return Math.max(0, 1f - (visualcooldown() / 10)); }
		public String toString() { return Messages.get(this, "name"); }
		public String desc() { return Messages.get(this, "desc", dispTurns(visualcooldown())); }
	};
	//1-5
	public static class KineticAttackTracker extends FlavourBuff {
		{ type = buffType.POSITIVE; }
		public int icon() { return BuffIndicator.INVERT_MARK; }
		public void tintIcon(Image icon) { icon.hardlight(0f, 0.75f, 0f); }
		public float iconFadePercent() { return Math.max(0, 1f - (visualcooldown() / 5)); }
	};

	//Samurai 1-3 meta
	public static class DrawEnhanceMetaTracker extends Buff {}

	//Knight 1-1
	public static class ArmorEmpower extends Buff {

		{
			type = buffType.POSITIVE;
		}

		@Override
		public int icon() {
			return BuffIndicator.ARMOR;
		}

		@Override
		public void tintIcon(Image icon) {
			icon.hardlight(1, 0.5f, 0);
		}

		@Override
		public float iconFadePercent() {
			float max = 1 + Dungeon.hero.pointsInTalent(Talent.RA_TOUGH_MEAL);
			return Math.max(0, (max-left) / max);
		}

		@Override
		public String iconTextDisplay() {
			return Integer.toString(left);
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", armorBoost, left);
		}

		public int armorBoost;
		public int left;

		public void set(int armor, int hits){
			if (armor*hits > armorBoost*left) {
				armorBoost = armor;
				left = hits;
			}
		}

		public int proc(int damage) {
			damage = Math.max(0, damage-armorBoost);
			left--;
			if (left <= 0) detach();
			return damage;
		}

		private static final String BOOST = "boost";
		private static final String LEFT = "left";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put( BOOST, armorBoost);
			bundle.put( LEFT, left );
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			armorBoost = bundle.getInt( BOOST );
			left = bundle.getInt( LEFT );
		}

	}

	public static class KineticBattle extends Buff {

		{
			type = buffType.POSITIVE;
		}

		public float duration = 0f;
		public float maxDuration = 0f;
		public int dmgBoost = 0;

		@Override
		public int icon() {
			return BuffIndicator.WEAPON;
		}

		@Override
		public void tintIcon(Image icon) {
			icon.hardlight(0, 1f, 0);
		}

		public void set() {
			dmgBoost = Math.min(5, ++dmgBoost);
			maxDuration = 1+2*Dungeon.hero.pointsInTalent(Talent.KINETIC_BATTLE);
			duration = maxDuration;
		}

		public int proc(int damage) {
			damage += this.dmgBoost;
			return damage;
		}

		@Override
		public boolean act() {
			if (--duration <= 0) {
				detach();
			} else {
				spend(TICK);
			}
			return true;
		}

		@Override
		public String iconTextDisplay() {
			return Integer.toString((int)duration);
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", dmgBoost, duration);
		}

		@Override
		public float iconFadePercent() {
			return Math.max(0, (maxDuration-duration) / maxDuration);
		}

		private static final String DURATION = "duration";
		private static final String MAX_DURATION = "maxDuration";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put( DURATION, duration );
			bundle.put( MAX_DURATION, maxDuration );
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			duration = bundle.getFloat( DURATION );
			maxDuration = bundle.getFloat( MAX_DURATION );
		}
	}

	public static class WarCryTracker extends Buff {}

	public static class PrayForDeadTracker extends FlavourBuff {
		public static final float DURATION = 1f;
	}

	public static class JudgementTracker extends FlavourBuff {

		public static final float DURATION = 5f;

		{
			type = buffType.NEUTRAL;
			announced = true;
		}

		@Override
		public int icon() {
			return BuffIndicator.JUDGEMENT;
		}

		@Override
		public void tintIcon(Image icon) {
			icon.hardlight(0xFFFF66);
		}

		@Override
		public float iconFadePercent() {
			return Math.max(0, (DURATION - visualcooldown()) / DURATION);
		}

		@Override
		public void fx(boolean on) {
			if (on) target.sprite.add(CharSprite.State.JUDGED);
			else target.sprite.remove(CharSprite.State.JUDGED);
		}

	}

	public static class FirstAidTreatCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0.2f, 1f, 0.2f); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / (50)); }
		public String toString() { return Messages.get(this, "name"); }
		public String desc() { return Messages.get(this, "desc", dispTurns(visualcooldown())); }
	};

	//Archer T1
	public static class ForceSavingTracker extends Buff {
		public int icon() { return BuffIndicator.WEAPON; }
		public void tintIcon(Image icon) { icon.hardlight(1f, 1f, 0); }
		public String toString() { return Messages.get(this, "name"); }
		public String desc() { return Messages.get(this, "desc", dispTurns(visualcooldown())); }
	}

	//Archer 3-2
	public static class FollowupShootTracker extends FlavourBuff{
		public int object;
		{ type = buffType.POSITIVE; }
		public int icon() { return BuffIndicator.INVERT_MARK; }
		public void tintIcon(Image icon) { icon.hardlight(1f, 0f, 0.5f); }
		public float iconFadePercent() { return Math.max(0, 1f - (visualcooldown() / 5)); }
		private static final String OBJECT    = "object";
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(OBJECT, object);
		}
		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			object = bundle.getInt(OBJECT);
		}
	}

	public static class SurprisePanicTracker extends Buff {}

}
