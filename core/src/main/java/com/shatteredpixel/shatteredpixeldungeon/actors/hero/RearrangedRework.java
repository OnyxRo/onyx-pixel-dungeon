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

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.chargearea.ArtifactRechargeArea;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.chargearea.BarrierRechargeArea;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.chargearea.WandRechargeArea;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.HolyMantle;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.PowerOfLife;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.RaResurrection;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Monk;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RearrangedRings;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfForce;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.spellbook.SpellBook;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Elastic;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.FencerDash;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

/**
 * Re-ARranged PD's rework of Shattered's six heroes. Their new tier 1 and 2 talents, three more tier 3 talents
 * for each subclass, the four general tier 3 talents and a third subclass each. All of it only shows with the
 * Re-ARranged heroes option (Challenges.REARRANGED_HEROES), so with it off the heroes are Shattered's.
 */
public class RearrangedRework {

	public static boolean on(){
		//on the hero select screen the trees shown follow the option picked there
		if (com.watabou.noosa.Game.instance != null && com.watabou.noosa.Game.scene() instanceof com.shatteredpixel.shatteredpixeldungeon.scenes.HeroSelectScene) {
			return (com.shatteredpixel.shatteredpixeldungeon.SPDSettings.challenges() & Challenges.REARRANGED_HEROES) != 0;
		}
		return Dungeon.isChallenged(Challenges.REARRANGED_HEROES);
	}

	public static boolean isShatteredHero( HeroClass cls ){
		switch (cls){
			case WARRIOR: case MAGE: case ROGUE: case HUNTRESS: case DUELIST: case CLERIC:
				return true;
			default:
				return false;
		}
	}

	public static HeroSubClass thirdSubclass( HeroClass cls ){
		switch (cls){
			case WARRIOR:  return HeroSubClass.VETERAN;
			case MAGE:     return HeroSubClass.WIZARD;
			case ROGUE:    return HeroSubClass.CHASER;
			case HUNTRESS: return HeroSubClass.FIGHTER;
			case DUELIST:  return HeroSubClass.FENCER;
			case CLERIC:   return HeroSubClass.ENCHANTER;
			default:       return null;
		}
	}

	public static HeroSubClass[] subClasses( HeroClass cls, HeroSubClass[] base ){
		HeroSubClass third = thirdSubclass(cls);
		if (third == null || !on() || base.length != 2) return base;
		return new HeroSubClass[]{ base[0], base[1], third };
	}

	//tier is 1 or 2, the talents go after Shattered's own in the same tier
	public static void extendClassTier( HeroClass cls, int tier, LinkedHashMap<Talent, Integer> into, LinkedHashMap<Talent, Talent> replacements ){
		if (!on()) return;
		Talent t = null;
		switch (cls){
			case WARRIOR:  t = tier == 1 ? MAX_HEALTH : PARRY; break;
			case MAGE:     t = tier == 1 ? CHARGE_PRESERVE : FASTER_CHARGER; break;
			case ROGUE:    t = tier == 1 ? EMERGENCY_ESCAPE : MOVESPEED_ENHANCE; break;
			case HUNTRESS: t = tier == 1 ? WATER_FRIENDLY : ADDED_MEAL; break;
			case DUELIST:  t = tier == 1 ? SKILLED_HAND : ACCUMULATION; break;
			case CLERIC:   t = tier == 1 ? WARDING_LIGHT : DIVINE_BLAST; break;
			default: return;
		}
		if (replacements != null && replacements.containsKey(t)) t = replacements.get(t);
		if (!into.containsKey(t)) into.put(t, 0);
	}

	//the mod gives every subclass the four general talents first, then its own six
	public static void extendSubclassTalents( HeroSubClass sub, ArrayList<Talent> tierTalents ){
		if (!on()) return;
		Talent[] extra;
		boolean third = false;
		switch (sub){
			case BERSERKER:  extra = new Talent[]{LETHAL_RAGE, MAX_RAGE, RA_ENDURANCE}; break;
			case GLADIATOR:  extra = new Talent[]{LIGHT_WEAPON, OFFENSIVE_DEFENSE, SKILL_REPEAT}; break;
			case BATTLEMAGE: extra = new Talent[]{BATTLE_MAGIC, MAGIC_RUSH, MAGICAL_CIRCLE}; break;
			case WARLOCK:    extra = new Talent[]{MADNESS, ENHANCED_MARK, MARK_OF_WEAKNESS}; break;
			case ASSASSIN:   extra = new Talent[]{ENERGY_DRAW, PERFECT_ASSASSIN, CAUTIOUS_PREP}; break;
			case FREERUNNER: extra = new Talent[]{QUICK_PREP, OVERCOMING, MOMENTARY_FOCUSING}; break;
			case SNIPER:     extra = new Talent[]{KICK, SHOOTING_EYES, TARGET_SPOTTING}; break;
			case WARDEN:     extra = new Talent[]{LIVING_GRASS, ATTRACTION, HEALING_DEW}; break;
			case CHAMPION:   extra = new Talent[]{FASTER_CHARGE, QUICK_FOLLOWUP, TWIN_SWORD}; break;
			case MONK:       extra = new Talent[]{RESTORED_ENERGY, ENERGY_BARRIER, HARMONY}; break;
			case PRIEST:     extra = new Talent[]{DIVINE_RAY, HOLY_BOMB, RA_RESURRECTION}; break;
			case PALADIN:    extra = new Talent[]{HOLY_MANTLE, POWER_OF_LIFE, INDUCE_AGGRO}; break;
			case VETERAN:    third = true; extra = new Talent[]{POWERFUL_TACKLE, MYSTICAL_TACKLE, DELAYED_GRENADE, INCAPACITATION, SUPER_ARMOR, IMPROVED_TACKLE}; break;
			case WIZARD:     third = true; extra = new Talent[]{SPELL_ENHANCE, BRIG_BOOST, ENERGY_REMAINS, MAGIC_EMPOWER, SECOND_EFFECT, LIFE_ENERGY}; break;
			case CHASER:     third = true; extra = new Talent[]{POISONOUS_BLADE, LETHAL_SURPRISE, CHAIN_CLOCK, SOUL_COLLECT, TRAIL_TRACKING, MASTER_OF_CLOAKING}; break;
			case FIGHTER:    third = true; extra = new Talent[]{SWIFT_MOVEMENT, LESS_RESIST, RING_KNUCKLE, MYSTICAL_PUNCH, QUICK_STEP, RA_COUNTER_ATTACK}; break;
			case FENCER:     third = true; extra = new Talent[]{CLAM_STEPS, CRITICAL_MOMENTUM, KINETIC_MOVEMENT, AGGRESSIVE_MOVEMENT, UNENCUMBERED_MOVEMENT, SOULIZE}; break;
			case ENCHANTER:  third = true; extra = new Talent[]{TIME_AMP, WEAKENING_HEX, STUN, THUNDER_IMBUE, ARCANE_ARMOR, ENCHANT}; break;
			default: return;
		}
		if (third) tierTalents.clear();
		ArrayList<Talent> general = new ArrayList<>();
		Collections.addAll(general, ATK_SPEED_ENHANCE, ACC_ENHANCE, EVA_ENHANCE, BETTER_CHOICE);
		general.removeAll(tierTalents);
		tierTalents.addAll(0, general);
		Collections.addAll(tierTalents, extra);
	}

	/* ------------------------------------------------------------------------------------------------------
	 * What the new talents and subclasses do. Each Shattered class calls one of these from one line, most of
	 * them through the RearrangedHero and RearrangedTalents hooks. A talent that is not on the tree does
	 * nothing, so only the few changes to Shattered's own talents check on().
	 * ------------------------------------------------------------------------------------------------------ */

	//Hero.updateHT, Physical Enhancement Training
	public static int bonusHT( Hero hero ){
		return 5 * hero.pointsInTalent(MAX_HEALTH);
	}

	//Hero.defenseSkill, Parry
	public static boolean dodgesEverything( Hero hero, Char enemy ){
		if (enemy != null && hero.buff(Talent.ParryTracker.class) != null){
			if (hero.canAttack(enemy) && !hero.isCharmedBy(enemy)){
				Buff.affect(hero, Talent.RiposteTracker.class).enemy = enemy;
			}
			return true;
		}
		return false;
	}

	public static float evasion( Hero hero, float evasion ){
		if (hero.hasTalent(SWIFT_MOVEMENT)) {
			evasion += hero.STR()-10;
		}
		return evasion;
	}

	//Hero.defenseVerb
	public static String defenseVerb( Hero hero ){
		Talent.ParryTracker parryTracker = hero.buff(Talent.ParryTracker.class);
		if (hero.hasTalent(PARRY) && parryTracker != null) {
			parryTracker.detach();
			return Messages.get(Monk.class, "parried");
		}
		return null;
	}

	public static boolean harmonized( Hero hero ){
		if (!hero.subClass.is(HeroSubClass.MONK)) return false;
		MonkEnergy energy = hero.buff(MonkEnergy.class);
		return energy != null && energy.harmonized(hero);
	}

	//Hero.speed
	public static float speed( Hero hero, float speed ){
		if (hero.hasTalent(MOVESPEED_ENHANCE)) {
			speed *= 1 + 0.1f*hero.pointsInTalent(MOVESPEED_ENHANCE);
		}
		if (harmonized(hero)) speed *= 1.5f;
		return speed;
	}

	//Hero.attackDelay, unarmed
	public static float unarmedSpeed( Hero hero, float speed ){
		if (hero.hasTalent(LESS_RESIST) && hero.belongings.attackingWeapon() == null && hero.belongings.armor != null) {
			int aEnc = hero.belongings.armor.STRReq() - hero.STR();
			if (aEnc < 0) {
				speed *= 1 + 0.05f * hero.pointsInTalent(LESS_RESIST) * (-aEnc);
			}
		}
		if (hero.hasTalent(QUICK_FOLLOWUP) && hero.buff(Talent.QuickFollowupTracker.class) != null) {
			speed *= 1+hero.pointsInTalent(QUICK_FOLLOWUP)/3f;
		}
		if (harmonized(hero)) speed *= 1.5f;
		if (hero.hasTalent(ATK_SPEED_ENHANCE)) {
			speed *= 1 + 0.05f * hero.pointsInTalent(ATK_SPEED_ENHANCE);
		}
		return speed;
	}

	//Weapon.speedMultiplier, armed
	public static float weaponSpeed( Char owner ){
		if (!(owner instanceof Hero)) return 1f;
		Hero hero = (Hero) owner;
		float multi = 1f;
		if (hero.hasTalent(LIGHT_WEAPON) && hero.belongings.attackingWeapon() instanceof MeleeWeapon) {
			int aEnc = ((MeleeWeapon)hero.belongings.attackingWeapon()).STRReq() - hero.STR();
			if (aEnc < 0) {
				multi *= 1 + 0.05f * hero.pointsInTalent(LIGHT_WEAPON) * (-aEnc);
			}
		}
		if (hero.hasTalent(QUICK_FOLLOWUP) && hero.buff(Talent.QuickFollowupTracker.class) != null) {
			multi *= 1+hero.pointsInTalent(QUICK_FOLLOWUP)/3f;
		}
		if (harmonized(hero)) multi *= 1.5f;
		if (hero.belongings.weapon != null && hero.belongings.secondWep != null
				&& hero.pointsInTalent(TWIN_SWORD) > 2
				&& hero.belongings.weapon.getClass() == hero.belongings.secondWep.getClass()) {
			multi *= 2;
		}
		return multi;
	}

	//Hero.act
	public static void onAct( Hero hero ){
		if (hero.hasTalent(PARRY) && hero.buff(Talent.ParryCooldown.class) == null && hero.buff(Talent.ParryTracker.class) == null){
			Buff.affect(hero, Talent.ParryTracker.class);
		}
	}

	//Hero.rest, the Chaser fades from sight while she waits
	public static void onRest( Hero hero ){
		if (hero.subClass.is(HeroSubClass.CHASER)
				&& hero.buff(Talent.ChaseCooldown.class) == null
				&& hero.buff(Invisibility.class) == null
				&& hero.buff(CloakOfShadows.cloakStealth.class) == null ) {
			Buff.affect(hero, Invisibility.class, hero.hasTalent(MASTER_OF_CLOAKING) ? 6f : 5f);
			Buff.affect(hero, Talent.ChaseCooldown.class, hero.pointsInTalent(MASTER_OF_CLOAKING) > 1 ? 10f : 15f);
		}
	}

	//Hero.attackSkill, a dash attack never misses
	public static boolean neverMisses( Hero hero ){
		return hero.buff(FencerDash.DashAttack.class) != null;
	}

	//end of Hero.attackProc, the subclass part
	public static int attackProc( final Hero hero, final Char enemy, KindOfWeapon wep, int damage ){
		switch (hero.subClass){
			case SNIPER:
				if (hero.hasTalent(KICK)
						&& enemy.buff(PinCushion.class) != null
						&& Dungeon.level.adjacent(hero.pos, enemy.pos)
						&& hero.buff(Talent.KickCooldown.class) == null) {
					Item item = enemy.buff(PinCushion.class).grabOne();
					if (item != null) {
						if (item.doPickUp(hero, enemy.pos)){
							hero.spend(-1); //attacking enemy already takes a turn
						} else {
							GLog.w(Messages.get(hero, "cant_grab"));
							Dungeon.level.drop(item, enemy.pos).sprite.drop();
						}
					}
					Ballistica trajectory = new Ballistica(hero.pos, enemy.pos, Ballistica.STOP_TARGET);
					trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size() - 1), Ballistica.PROJECTILE);
					WandOfBlastWave.throwChar(enemy, trajectory, hero.pointsInTalent(KICK), true, false, hero.getClass());
					Buff.affect(hero, Talent.KickCooldown.class, 10f);
				}
				if (wep instanceof MissileWeapon
						&& hero.hasTalent(SHOOTING_EYES)
						&& enemy.buff(Talent.ShootingEyesTracker.class) == null) {
					if (Random.Float() < hero.pointsInTalent(SHOOTING_EYES)/3f) {
						Buff.affect(enemy, Blindness.class, 2f);
					}
					Buff.affect(enemy, Talent.ShootingEyesTracker.class);
				}
				if (wep instanceof MissileWeapon
						&& hero.hasTalent(TARGET_SPOTTING)
						&& hero.buff(SnipersMark.class) != null
						&& hero.buff(SnipersMark.class).object == enemy.id()) {
					damage = (int)(damage*(1+0.1f*hero.pointsInTalent(TARGET_SPOTTING)));
				}
				break;
			case FIGHTER:
				if (wep == null && Random.Int(3) < hero.pointsInTalent(QUICK_STEP)) {
					Buff.prolong(hero, Talent.QuickStep.class, 1.0001f);
				}
				if (wep == null && hero.hasTalent(RING_KNUCKLE) && hero.buff(RingOfForce.Force.class) == null) {
					Buff.prolong(hero, EnhancedRingsCombo.class, (hero.pointsInTalent(RING_KNUCKLE) >= 2) ? 2f : 1f).hit();
					hero.updateHT(false);
					Item.updateQuickslot();
				}
				if (wep == null && Random.Float() < hero.pointsInTalent(MYSTICAL_PUNCH)/3f) {
					if (hero.belongings.ring != null) {
						damage = RearrangedRings.onHit(hero.belongings.ring, hero, enemy, damage);
					}
					if (hero.belongings.misc instanceof Ring) {
						damage = RearrangedRings.onHit((Ring)hero.belongings.misc, hero, enemy, damage);
					}
				}
				break;
			case CHAMPION:
				if (hero.belongings.weapon != null && hero.belongings.secondWep != null
						&& hero.pointsInTalent(TWIN_SWORD) > 1
						&& hero.belongings.weapon.getClass() == hero.belongings.secondWep.getClass()) {
					KindOfWeapon other = hero.belongings.secondWep;
					if (hero.belongings.secondWep == wep)  {
						other = hero.belongings.weapon;
					}
					damage = other.proc( hero, enemy, damage );
				}
				break;
			case VETERAN:
				if (Dungeon.level.adjacent(enemy.pos, hero.pos) && hero.buff(Tackle.TackleTracker.class) == null) {
					Actor.add(new Actor() {
						{
							actPriority = VFX_PRIO;
						}

						@Override
						protected boolean act() {
							if (enemy.isAlive()) {
								Buff.prolong(hero, Tackle.class, 1).set(enemy.id());
							}
							Actor.remove(this);
							return true;
						}
					});
				}
				break;
			default:
		}
		return damage;
	}

	//Hero.defenseProc
	public static void defenseProc( Hero hero, Char enemy ){
		if (hero.subClass.is(HeroSubClass.GLADIATOR) && Random.Float() < hero.pointsInTalent(OFFENSIVE_DEFENSE)/3f) {
			Buff.affect(hero, Combo.class).hit(enemy);
		}
		if (hero.hasTalent(EMERGENCY_ESCAPE) && Random.Float() < hero.pointsInTalent(EMERGENCY_ESCAPE)/50f) {
			Buff.prolong(hero, Invisibility.class, 3f);
		}
		if (hero.hasTalent(OVERCOMING)) {
			Momentum momentum = hero.buff(Momentum.class);
			if (momentum != null && momentum.freerunning()) {
				Buff.affect(hero, Haste.class, 2f);
				if (hero.pointsInTalent(OVERCOMING) > 1) Buff.affect(hero, Adrenaline.class, 2f);
				if (hero.pointsInTalent(OVERCOMING) > 2) Buff.affect(hero, EvasiveMove.class, 2f);
			}
		}
	}

	//Hero.damage, the Veteran's Super Armor right after a tackle
	public static int damageTaken( Hero hero, int dmg ){
		if (hero.buff(Tackle.SuperArmorTracker.class) != null) {
			switch (hero.pointsInTalent(SUPER_ARMOR)) {
				case 1: dmg = Math.round(dmg*0.67f); break;
				case 2: dmg = Math.round(dmg*0.33f); break;
				case 3: dmg = 0; break;
				default: break;
			}
		}
		return dmg;
	}

	//Hero.die, the Priest's Resurrection
	public static boolean preventsDeath( Hero hero ){
		if (hero.buff(RaResurrection.ResurrectionBuff.class) != null) {
			hero.interrupt();
			hero.HP = Math.max(1, hero.HT / 10);
			PotionOfHealing.cure(hero);
			Buff.prolong(hero, Invulnerability.class, Invulnerability.DURATION);
			SpellSprite.show(hero, SpellSprite.ANKH);
			GameScene.flash(0x80FFFF40);
			Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
			GLog.w(Messages.get(hero, "revive"));
			hero.buff(RaResurrection.ResurrectionBuff.class).detach();
			Buff.affect(hero, RaResurrection.ResurrectionCooldown.class, RaResurrection.ResurrectionCooldown.DURATION);
			return true;
		}
		return false;
	}

	//Hero.onAttackComplete, a Counter Attack is free
	public static boolean attackIsFree( Hero hero ){
		if (hero.buff(Talent.CounterAttackTracker.class) != null && hero.belongings.weapon == null) {
			hero.buff(Talent.CounterAttackTracker.class).detach();
			return true;
		}
		return false;
	}

	//Hero.onAttackComplete, after the hit is rolled
	public static void afterAttack( Hero hero, Char target, boolean hit, boolean wasEnemy ){
		if (hit && hero.subClass.is(HeroSubClass.BATTLEMAGE) && hero.belongings.attackingWeapon() instanceof MagesStaff && hero.hasTalent(BATTLE_MAGIC) && wasEnemy) {
			Buff.affect( hero, MagicalCombo.class).hit( target );
		}
		if (!hit && hero.belongings.weapon == null && hero.subClass.is(HeroSubClass.FIGHTER) && Random.Int(5) == 0 && hero.pointsInTalent(SWIFT_MOVEMENT) > 1) {
			Buff.prolong(hero, EvasiveMove.class, 0.9999f);
		}
	}

	//Hero.actMove, Quick Step makes the next step free
	public static void afterStep( Hero hero, float delay ){
		if (hero.buff(Talent.QuickStep.class) != null) {
			hero.spend(-delay);
			hero.buff(Talent.QuickStep.class).detach();
		}
	}

	//Talent.onTalentUpgraded
	public static void onTalentUpgraded( Hero hero, Talent talent ){
		if (talent == MAX_HEALTH) {
			hero.updateHT(true);
		}
		if (talent == PARRY) {
			Buff.affect(hero, Talent.ParryTracker.class);
		}
		if (talent == ACCUMULATION) {
			Item.updateQuickslot();
		}
	}

	//Talent.onScrollUsed, Magic Rush
	public static void onScrollUsed( Hero hero, float factor ){
		if (hero.hasTalent(MAGIC_RUSH)) {
			MagesStaff staff = hero.belongings.getItem(MagesStaff.class);
			if (staff != null) {
				staff.gainCharge(factor * hero.pointsInTalent(MAGIC_RUSH), false);
				ScrollOfRecharging.charge(hero);
			}
		}
	}

	//start of Talent.onAttackProc, the dash attack's bonus
	public static int dashAttack( Hero hero, int damage ){
		FencerDash.DashAttack dash = hero.buff(FencerDash.DashAttack.class);
		if (dash != null) {
			damage = Math.round(damage * dash.getDmgMulti());
			dash.detach();
		}
		return damage;
	}

	//Talent.onAttackProc
	public static int onAttackProc( Hero hero, Char enemy, int damage ){
		KindOfWeapon wep = hero.belongings.attackingWeapon();
		if (hero.hasTalent(WATER_FRIENDLY) && Dungeon.level.map[hero.pos] == Terrain.WATER) {
			damage += Hero.heroDamageIntRange(1, hero.pointsInTalent(WATER_FRIENDLY));
			Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
		}
		if (hero.buff(Talent.SkilledHandTracker.class) != null) {
			damage += 1+hero.pointsInTalent(SKILLED_HAND);
			hero.buff(Talent.SkilledHandTracker.class).detach();
			Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
		}
		if (hero.hasTalent(SKILLED_HAND) && !hero.heroClass.is(HeroClass.DUELIST)) {
			damage += Random.NormalIntRange(0, 1+hero.pointsInTalent(SKILLED_HAND));
		}
		if (enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)) {
			if (hero.hasTalent(POISONOUS_BLADE)) {
				Buff.affect(enemy, Poison.class).set(2+hero.pointsInTalent(POISONOUS_BLADE));
			}
			if (hero.hasTalent(SOUL_COLLECT) && damage >= enemy.HP) {
				int healAmt = 3*hero.pointsInTalent(SOUL_COLLECT);
				healAmt = Math.min( healAmt, hero.HT - hero.HP );
				if (healAmt > 0 && hero.isAlive()) {
					hero.HP += healAmt;
					if (hero.sprite != null) {
						hero.sprite.emitter().start( Speck.factory( Speck.HEALING ), 0.4f, 2 );
						hero.sprite.showStatus( CharSprite.POSITIVE, Integer.toString( healAmt ) );
					}
				}
			}
			if (hero.hasTalent(TRAIL_TRACKING) && damage >= enemy.HP) {
				Buff.affect(hero, MindVision.class, hero.pointsInTalent(TRAIL_TRACKING));
			}
			if (hero.pointsInTalent(MASTER_OF_CLOAKING) == 3) {
				if (hero.buff(Talent.ChaseCooldown.class) != null) hero.buff(Talent.ChaseCooldown.class).spendTime();
				if (hero.buff(Talent.ChainCooldown.class) != null) hero.buff(Talent.ChainCooldown.class).spendTime();
				if (hero.buff(Talent.LethalCooldown.class) != null) hero.buff(Talent.LethalCooldown.class).spendTime();
			}
		}
		if (Random.Float() < hero.pointsInTalent(MADNESS)/10f) {
			Buff.prolong(enemy, Amok.class, 3f);
		}
		SpellBook.SpellBookCoolDown spellBookCoolDown = hero.buff(SpellBook.SpellBookCoolDown.class);
		if (hero.hasTalent(BRIG_BOOST) && spellBookCoolDown != null) {
			spellBookCoolDown.hit(hero.pointsInTalent(BRIG_BOOST));
		}
		if (hero.buff(PowerOfLife.PowerOfLifeBarrier.class) != null) {
			hero.buff(PowerOfLife.PowerOfLifeBarrier.class).proc(damage);
		}
		if (enemy.alignment != Char.Alignment.ALLY
				&& !hero.heroClass.is(HeroClass.CLERIC)
				&& hero.hasTalent(DIVINE_BLAST)
				&& Random.Float() < 0.2f * hero.pointsInTalent(DIVINE_BLAST)){
			Elastic.pushEnemy(wep, hero, enemy, 1);
		}
		return damage;
	}

	//Talent.onAttackProc, Followup Strike from an unarmed Fighter stuns
	public static void followupStrike( Hero hero, Char enemy ){
		if (hero.belongings.weapon == null && hero.subClass.is(HeroSubClass.FIGHTER)) {
			Buff.affect( enemy, Paralysis.class, 1f );
		}
	}

	//Char.attack, Searing Light hits a little harder with the option on, and Warding Light shields
	public static int searingLight( int bonus ){
		return on() ? bonus + 1 : bonus;
	}

	public static void wardingLight( Hero hero ){
		if (hero.hasTalent(WARDING_LIGHT)){
			Buff.affect(hero, Barrier.class).setShield(1+2*hero.pointsInTalent(WARDING_LIGHT));
		}
	}

	//wands and artifacts used by a hero other than the Cleric, Warding Light
	public static void wardingLightItem(){
		Hero hero = Dungeon.hero;
		if (hero != null && !hero.heroClass.is(HeroClass.CLERIC)
				&& hero.hasTalent(WARDING_LIGHT)
				&& hero.buff(Talent.WardingLightCooldown.class) == null){
			Buff.affect(hero, Barrier.class).setShield(1+2*hero.pointsInTalent(WARDING_LIGHT));
			Buff.affect(hero, Talent.WardingLightCooldown.class, 20f);
		}
	}

	//Char.attack, the damage an enemy deals, Mark of Weakness
	public static float markOfWeakness( Char attacker, float dmg ){
		Hero hero = Dungeon.hero;
		if (hero != null && attacker.buff(SoulMark.class) != null && hero.hasTalent(MARK_OF_WEAKNESS) && attacker.alignment != Char.Alignment.ALLY) {
			dmg *= Math.pow(0.9f, hero.pointsInTalent(MARK_OF_WEAKNESS));
		}
		return dmg;
	}

	//Char.attack, after an assassination
	public static void energyDraw( Hero hero ){
		if (Random.Float() < hero.pointsInTalent(ENERGY_DRAW)/3f) {
			CloakOfShadows cloak = hero.belongings.getItem(CloakOfShadows.class);
			if (cloak != null) {
				cloak.directCharge(1);
				ScrollOfRecharging.charge(hero);
				SpellSprite.show(hero, SpellSprite.CHARGE);
			}
		}
	}

	//Char.attack, the hero dodged
	public static void onHeroDodged( Hero hero ){
		if (hero.pointsInTalent(SWIFT_MOVEMENT) == 3) {
			Buff.prolong(hero, Invisibility.class, 1.0001f);
		}
		if (Random.Int(5) < hero.pointsInTalent(RA_COUNTER_ATTACK)) {
			Buff.affect(hero, Talent.CounterAttackTracker.class);
		}
		if (hero.hasTalent(QUICK_PREP)) {
			Momentum momentum = hero.buff(Momentum.class);
			if (momentum != null) {
				momentum.quickPrep(hero.pointsInTalent(QUICK_PREP));
			}
		}
	}

	//Char.damage, Holy Mantle soaks one hit
	public static int holyMantle( Char ch, int damage ){
		if (ch.buff(HolyMantle.HolyMantleBuff.class) != null) {
			damage = Math.round(HolyMantle.HolyMantleBuff.DamageReduction(damage));
			ch.buff(HolyMantle.HolyMantleBuff.class).detach();
		}
		return damage;
	}

	//Mob.defenseProc, when the Chaser surprises an enemy
	public static void chaserSurprise( final Hero hero, Mob mob ){
		if (!hero.subClass.is(HeroSubClass.CHASER)) return;
		if (hero.hasTalent(CHAIN_CLOCK) && hero.buff(Talent.ChainCooldown.class) == null){
			new FlavourBuff() {
				{
					actPriority = VFX_PRIO;
				}

				public boolean act() {
					Buff.affect( hero, Invisibility.class, 1f * hero.pointsInTalent(CHAIN_CLOCK));
					return super.act();
				}
			}.attachTo(hero);
			Buff.affect( hero, Haste.class, 1f * hero.pointsInTalent(CHAIN_CLOCK));
			Buff.affect( hero, Talent.ChainCooldown.class, 10f);
			Sample.INSTANCE.play( Assets.Sounds.MELD );
		}
		if (hero.hasTalent(LETHAL_SURPRISE) && !mob.isAlive() && hero.buff(Talent.LethalCooldown.class) == null) {
			for (Mob m : Dungeon.level.mobs.toArray( new Mob[0] )) {
				if (m.alignment == Char.Alignment.ENEMY && Dungeon.level.heroFOV[m.pos] && m.state != m.SLEEPING) {
					Buff.affect( m, Vulnerable.class, 1f);
					if (hero.pointsInTalent(LETHAL_SURPRISE) >= 2) Buff.affect( m, Paralysis.class, 1f);
				}
			}
			Buff.affect(hero, Talent.LethalCooldown.class, 5f);
			if (hero.pointsInTalent(LETHAL_SURPRISE) == 3) {
				Buff.affect(hero, Swiftthistle.TimeBubble.class).reset(2);
			}
		}
	}

	//Mob.die, Energy Remains and Lethal Rage
	public static void onMobDeath( Mob mob, Object cause ){
		Hero hero = Dungeon.hero;
		if (hero == null) return;
		if (Dungeon.level.map[mob.pos] != Terrain.CHASM && hero.hasTalent(ENERGY_REMAINS)) {
			int point = hero.pointsInTalent(ENERGY_REMAINS);
			switch (Random.Int(6)) {
				case 0: if (point >= 1) Buff.affect(hero, WandRechargeArea.class).setup(mob.pos); break;
				case 1: if (point >= 2) Buff.affect(hero, ArtifactRechargeArea.class).setup(mob.pos); break;
				case 2: if (point >= 3) Buff.affect(hero, BarrierRechargeArea.class).setup(mob.pos); break;
				default: break;
			}
		}
		if (mob.alignment != Char.Alignment.ALLY && (cause == hero || cause instanceof com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand)
				&& hero.hasTalent(LETHAL_RAGE)) {
			Buff.affect(hero, Berserk.class).add(0.067f*hero.pointsInTalent(LETHAL_RAGE));
		}
	}

	//Berserk.damage, the mod's rage gain (a third of the damage, not a quarter) and its two talents
	public static float rageGain( Hero hero, float base, int damage ){
		if (!on()) return base;
		float powerAmt = 100*(damage/(float)hero.HT)/3f;
		if (hero.hasTalent(RA_ENDURANCE) && powerAmt > 0) {
			Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal.WarriorShield.class).spendCooldown(powerAmt * hero.pointsInTalent(RA_ENDURANCE)/3f);
		}
		return (damage/(float)hero.HT)/3f;
	}

	public static void maxRage( Hero hero, float power, float maxPower ){
		if (hero.hasTalent(MAX_RAGE) && power >= maxPower && hero.buff(Talent.MaxRageCooldown.class) == null) {
			Buff.affect(hero, Adrenaline.class, 10 * power * hero.pointsInTalent(MAX_RAGE));
			Buff.affect(hero, Talent.MaxRageCooldown.class, 50f);
		}
	}

	//Combo, Skill Repeat
	public static boolean[] skillRepeat( Hero hero, boolean clobberUsedOnKill, boolean parryUsedOnKill, boolean clobberUsed, boolean parryUsed ){
		int pts = hero.pointsInTalent(SKILL_REPEAT);
		if (pts == 3) return new boolean[]{false, false};
		if (pts > 0) {
			if (clobberUsedOnKill) clobberUsed = false;
			if (parryUsedOnKill && pts > 1) parryUsed = false;
		}
		return new boolean[]{clobberUsed, parryUsed};
	}

	//Preparation, Perfect Assassin
	public static float perfectAssassin( float dmgMulti ){
		Hero hero = Dungeon.hero;
		if (hero != null && hero.hasTalent(PERFECT_ASSASSIN)) {
			dmgMulti *= 1f + 0.1f * hero.pointsInTalent(PERFECT_ASSASSIN);
		}
		return dmgMulti;
	}

	//Momentum, freerunning ends
	public static void momentaryFocusing( Char target ){
		if (target instanceof Hero && ((Hero) target).hasTalent(MOMENTARY_FOCUSING)) {
			Buff.affect(target, EvasiveMove.class, ((Hero) target).pointsInTalent(MOMENTARY_FOCUSING));
		}
	}

	//Regeneration, Accumulation for heroes other than the Duelist
	public static float regenDelay( Char target, float delay ){
		if (target instanceof Hero && ((Hero)target).hasTalent(ACCUMULATION) && !((Hero) target).heroClass.is(HeroClass.DUELIST)) {
			delay /= 1f+0.1f*((Hero)target).pointsInTalent(ACCUMULATION);
		}
		return delay;
	}

	//CloakOfShadows, Cautious Preparation
	public static int cautiousPrep( Char target ){
		return target instanceof Hero ? ((Hero) target).pointsInTalent(CAUTIOUS_PREP) : 0;
	}

	//Food and the Horn of Plenty, Added Meal
	public static float addedMeal( Hero hero, float foodVal ){
		if (hero != null && hero.hasTalent(ADDED_MEAL)) {
			foodVal *= 1.05f + 0.1f * hero.pointsInTalent(ADDED_MEAL);
		}
		return foodVal;
	}

	//RingOfForce, the Fighter's unarmed blows
	public static int fighterDamage( Hero hero ){
		int damage = Hero.heroDamageIntRange((hero.STR() - 8), (hero.STR() - 8 + hero.lvl));
		if (hero.pointsInTalent(RING_KNUCKLE) == 3) {
			if (hero.belongings.ring != null) {
				damage += hero.belongings.ring.buffedLvl();
			}
			if (hero.belongings.misc instanceof Ring) {
				damage += hero.belongings.misc.buffedLvl();
			}
		}
		if (hero.buff(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Kinetic.ConservedDamage.class) != null) {
			damage += hero.buff(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Kinetic.ConservedDamage.class).damageBonus();
			hero.buff(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Kinetic.ConservedDamage.class).detach();
		}
		return damage;
	}

	//Shockwave, the Veteran tackles whoever it hit beside him
	public static void shockwaveHit( Hero hero, Char ch ){
		if (hero.subClass.is(HeroSubClass.VETERAN) && Dungeon.level.adjacent(ch.pos, hero.pos) && hero.buff(Tackle.class) == null) {
			Buff.prolong(hero, Tackle.class, 1).set(ch.id());
		}
	}

	//Armor.evasionFactor, Unencumbered Movement
	public static float unencumberedMovement( Hero hero, int aEnc, float evasion ){
		MeleeWeapon.Charger charger = hero.buff(MeleeWeapon.Charger.class);
		if (charger != null && hero.hasTalent(UNENCUMBERED_MOVEMENT) && hero.belongings.weapon instanceof MeleeWeapon) {
			int wEnc = ((MeleeWeapon)hero.belongings.weapon).STRReq() - hero.STR();
			evasion += 0.5f*Math.max(0, -aEnc);
			evasion += 0.5f*Math.max(0, -wEnc);
		}
		return evasion;
	}

	//Ring.buffedLvl, the Fighter's Ring Knuckle combo
	public static int ringBonus(){
		if (Dungeon.hero == null) return 0;
		EnhancedRingsCombo combo = Dungeon.hero.buff(EnhancedRingsCombo.class);
		return combo == null ? 0 : combo.getCombo();
	}

	//Wand.wandProc and the wand charger
	public static int enhancedMark( int wandLevel ){
		return wandLevel + (Dungeon.hero == null ? 0 : Dungeon.hero.pointsInTalent(ENHANCED_MARK));
	}

	public static void lifeEnergy( Char target, int chargesUsed ){
		Hero hero = Dungeon.hero;
		if (hero != null && target != hero && hero.hasTalent(LIFE_ENERGY)) {
			hero.heal(chargesUsed*hero.pointsInTalent(LIFE_ENERGY));
		}
	}

	public static boolean preservesCharge(){
		Hero hero = Dungeon.hero;
		if (hero == null) return false;
		return (hero.hasTalent(CHARGE_PRESERVE) && Random.Int(20) < hero.pointsInTalent(CHARGE_PRESERVE))
				|| (hero.pointsInTalent(MAGICAL_CIRCLE) > 1 && hero.buff(MagicalCircle.class) != null && Random.Int(2) == 0);
	}

	public static int empoweredWandLevel( Char charger ){
		MagicalEmpower e = charger.buff(MagicalEmpower.class);
		return e == null ? 0 : e.getUpgrades();
	}

	public static void useMagicalEmpower( Char user ){
		MagicalEmpower e = user.buff(MagicalEmpower.class);
		if (e != null) e.use();
	}

	public static void magicalCircle( Hero user, int charges ){
		if (user.subClass.is(HeroSubClass.BATTLEMAGE) && user.hasTalent(MAGICAL_CIRCLE)) {
			Buff.affect(user, MagicalCircle.class).setup(user.pos, charges*3+1);
		}
	}

	//the wand charger's per turn rate, Faster Charger, Magical Circle and the Wizard's books
	public static float wandChargeMulti( float multi ){
		Hero hero = Dungeon.hero;
		if (hero == null) return multi;
		multi *= (1f + 0.1f * hero.pointsInTalent(FASTER_CHARGER));
		if (hero.hasTalent(MAGICAL_CIRCLE) && hero.buff(MagicalCircle.class) != null) {
			multi += 1;
		}
		if (hero.subClass.is(HeroSubClass.WIZARD)) {
			//the mod wrote max(10, books) here, which made wands charge at once, 5% for each book up to ten is what it describes
			multi *= (float) Math.pow(1.05f, Math.min(10, hero.belongings.getAllItems(SpellBook.class).size()));
		}
		return multi;
	}

	//Monk energy
	public static float restoredEnergy( Hero hero ){
		return hero.hasTalent(RESTORED_ENERGY) ? (0.0075f + hero.pointsInTalent(RESTORED_ENERGY)*0.0025f) : 0;
	}

	public static void energyBarrier( Char target, int cost ){
		if (target instanceof Hero && ((Hero) target).hasTalent(ENERGY_BARRIER)) {
			Buff.affect(target, Barrier.class).incShield(cost*(3+2*(((Hero) target).pointsInTalent(ENERGY_BARRIER))));
		}
	}
}
