/*
 * Onyx's Pixel Dungeon
 * The common, negative and resource talent catalog of Transformation PD (dongyusangyu), ported for the Freeman
 * and for any hero that metamorphoses into these talents. Every hook the mod spread through Hero, Mob, items and
 * levels lives here, and each call site is a single line naming this class.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.SmokeScreen;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Berserk;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.GreaterHaste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Momentum;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MonkEnergy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Preparation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SoulMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Stamina;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.MasterThievesArmband;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.UnstableSpellbook;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfDisintegration;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFireblast;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AdrenalineSurge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArtifactRecharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Awareness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.CounterBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Daze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FireImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invulnerability;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Levitation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PinCushion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WellFed;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM100;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Elemental;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RotLasher;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Skeleton;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Snake;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.YogDzewa;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.MirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Blandfruit;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Pasty;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.InfernalBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfDragonsBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfDragonsBreath;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfWealth;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMetamorphosis;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfSirensSong;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ReclaimTrap;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.SummonElemental;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAggression;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfIntuition;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.TrinketCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfCorruption;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLightning;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfMagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfPrismaticLight;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfRegrowth;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Earthroot;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class TransformationTalents {

	private static Hero hero(){
		return Dungeon.hero;
	}

	private static int p(Talent t){
		Hero h = Dungeon.hero;
		return h == null ? 0 : h.pointsInTalent(t);
	}

	private static int n(Talent t){
		Hero h = Dungeon.hero;
		return h == null ? 0 : h.pointsNegative(t);
	}

	private static void dropAtHero(Item item){
		Hero h = Dungeon.hero;
		if (h == null || Dungeon.level == null || item == null) return;
		com.shatteredpixel.shatteredpixeldungeon.items.Heap heap = Dungeon.level.drop(item, h.pos);
		if (heap.sprite != null) heap.sprite.drop();
	}

	private static boolean negativeDebuff(Buff b){
		return b.type == Buff.buffType.NEGATIVE && !(b instanceof AllyBuff) && !(b instanceof LostInventory);
	}

	private static int debuffCount(Char ch){
		int c = 0;
		for (Buff b : ch.buffs()) if (negativeDebuff(b)) c++;
		return c;
	}

	private static void clearDebuffs(Char ch){
		for (Buff b : ch.buffs().toArray(new Buff[0])) if (negativeDebuff(b)) b.detach();
	}

	//---- resource caps -------------------------------------------------------------------------

	//talents that hand out items share one limit per run, as in the mod (26, or 39 for the Freeman)
	public static class TalentItemCounter extends CounterBuff { { revivePersists = true; } }
	public static class MealCounter extends CounterBuff { { revivePersists = true; } }

	private static boolean takeItemSlot(Hero h){
		int max = h.heroClass == HeroClass.FREEMAN ? 39 : 26;
		TalentItemCounter c = h.buff(TalentItemCounter.class);
		float used = c == null ? 0 : c.count();
		if (used >= max){
			if (used == max){
				Buff.count(h, TalentItemCounter.class, 1);
				GLog.w(Messages.get(TransformationTalents.class, "item_limit"));
			}
			return false;
		}
		Buff.count(h, TalentItemCounter.class, 1);
		return true;
	}

	private static boolean takeMealSlot(Hero h){
		MealCounter c = h.buff(MealCounter.class);
		if (c != null && c.count() >= 100) return false;
		Buff.count(h, MealCounter.class, 1);
		return true;
	}

	//---- buffs ---------------------------------------------------------------------------------

	public static class TimedCooldown extends FlavourBuff {
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0.6f, 0.6f, 0.5f); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 50); }
	}
	public static class StrAtkCooldown extends TimedCooldown {}
	public static class AshesBowCooldown extends TimedCooldown {}
	public static class SurVolCooldown extends TimedCooldown {}
	public static class InvShaCooldown extends TimedCooldown {}
	public static class WordStunCooldown extends TimedCooldown {}
	public static class GasCooldown extends TimedCooldown {}
	public static class SurResistance extends FlavourBuff {}
	public static class HandSlipVulnerability extends FlavourBuff {}
	public static class NoSleep extends Buff {}
	public static class EnemyDies extends FlavourBuff {}
	public static class UnableRest extends Buff { { revivePersists = true; } }
	public static class SteamBeanMark extends Buff {}
	public static class NpcGiftGiven extends Buff {}

	//---- talent upgrades -----------------------------------------------------------------------

	public static void onTalentUpgraded(Hero hero, Talent talent){
		if (talent == null) return;
		int pts = hero.pointsInTalent(talent);

		if (talent == Talent.REVERSE_POLARITY){
			if (pts == 3) hero.HP = hero.HT;
			else if (pts == 2) hero.HP = Math.max(1, hero.HT/2);
			else if (pts == 1) hero.HP = 1;
		}
		if (talent == Talent.SAVAGE_PHYSIQUE || talent == Talent.JIULONGLA_COFFIN || talent == Talent.DEEP_FEAR){
			hero.updateHT(true);
		}
		if (talent == Talent.SHORTSIGHTED && Dungeon.level != null){
			Dungeon.observe();
		}
		if (talent == Talent.MARTIAL_TRAIN){
			Buff.affect(hero, MeleeWeapon.Charger.class);
		}
		if (talent == Talent.EXP_SOLID && pts > 0 && hero.lvl > 1){
			int down = Math.min(1, hero.lvl - 1);
			hero.lvl -= down;
			hero.exp = 0;
			hero.updateHT(false);
		}

		//items, under the shared resource limit
		Item give = null; int times = 1;
		switch (talent){
			case POTENTIAL_1: case POTENTIAL_2: case POTENTIAL_3: case POTENTIAL_4: case POTENTIAL_5:
				if (pts == 1) give = new ScrollOfMetamorphosis(); break;
			case POTENTIAL_6: case POTENTIAL_7: case POTENTIAL_8: case POTENTIAL_9:
				if (pts == 1 || pts == 2) give = new ScrollOfMetamorphosis(); break;
			case NOVICE_BENEFITS: give = new ScrollOfMetamorphosis(); break;
			case SHOCK_BOMB: case BOMB_MANIAC: if (pts > 0) give = new Bomb(); break;
			case EXP_SOLID: give = new PotionOfExperience(); break;
			case USURY: if (pts == 0) { give = new Gold().quantity(1000); GLog.p(Messages.get(TransformationTalents.class, "usury")); } break;
			case SECRET_STASH: give = new Gold().quantity(1000); break;
			case WEAPON_MAKE: {
				Weapon w = Generator.randomWeapon(2 + pts);
				w.identify();
				give = w; break;
			}
			case DOUBLE_TRINKETS: if (pts == 1) give = new TrinketCatalyst(); break;
			case ILLUSION_FEED: give = new Pasty(); break;
			case PYROMANIAC:
				if (pts == 3) give = new ElixirOfDragonsBlood();
				else if (pts == 2) give = new InfernalBrew().quantity(2);
				else give = new PotionOfDragonsBreath().quantity(3);
				break;
			case PERSONAL_ATTACK: give = new ScrollOfRage(); break;
			case FLASH_GENIUS: give = new StoneOfIntuition(); break;
			case COLLECT_PLANTS: times = pts + 2; break;
			default: break;
		}
		if (talent == Talent.COLLECT_PLANTS){
			if (takeItemSlot(hero)){
				for (int i = 0; i < times; i++) dropAtHero(Generator.random(Generator.Category.SEED));
				if (pts == 3) dropAtHero(new Blandfruit());
			}
		} else if (give != null && takeItemSlot(hero)){
			if (talent == Talent.EXP_SOLID) give.quantity(1);
			dropAtHero(give);
		}
		upgradedMore(hero, talent, pts);
	}

	//---- hero stats ----------------------------------------------------------------------------

	public static int maxHP(Hero h, int HT){
		if (h.hasTalent(Talent.SAVAGE_PHYSIQUE)) HT += 5 + 5*h.pointsInTalent(Talent.SAVAGE_PHYSIQUE);
		if (h.pointsNegative(Talent.JIULONGLA_COFFIN) > 0) HT -= (int)(HT*0.2f*h.pointsNegative(Talent.JIULONGLA_COFFIN));
		if (h.pointsNegative(Talent.DEEP_FEAR) > 0) HT -= Statistics.deepestFloor*(h.pointsNegative(Talent.DEEP_FEAR)+1);
		return HT;
	}

	public static int strBonus(Hero h){
		int b = 3*h.pointsInTalent(Talent.SHARP_HEAD);
		if (h.pointsInTalent(Talent.STRENGTH_TRAIN) == 2) b += 1;
		return b;
	}

	public static float accuracy(Hero h, Char target){
		if (target == null || Dungeon.level == null) return 1f;
		float acc = 1f;
		KindOfWeapon wep = h.belongings.attackingWeapon();
		int dist = Dungeon.level.distance(h.pos, target.pos);
		if (h.pointsNegative(Talent.PHOTOPHOBY) > 0 && h.buff(Light.class) != null) acc *= 1f - 0.2f*h.pointsNegative(Talent.PHOTOPHOBY);
		if (h.hasTalent(Talent.PRECISE_SHOT) && wep instanceof MissileWeapon && dist >= 3) acc *= 1f + 0.1f*h.pointsInTalent(Talent.PRECISE_SHOT);
		if (h.hasTalent(Talent.INSERT_BID) && target.buff(PinCushion.class) != null) acc *= 1f + 0.2f*h.pointsInTalent(Talent.INSERT_BID);
		if (h.hasTalent(Talent.OVERWHELMING) && h.attackDelay() > 1) acc *= 1f + (h.attackDelay()-1)*0.5f*h.pointsInTalent(Talent.OVERWHELMING);
		if (h.hasTalent(Talent.AMAZING_EYESIGHT) && wep instanceof MeleeWeapon && dist >= 4 - h.pointsInTalent(Talent.AMAZING_EYESIGHT)) acc *= 1.35f;
		if (h.hasTalent(Talent.SHARP_HEAD)) acc *= 1f - h.pointsInTalent(Talent.SHARP_HEAD)/6f;
		if (h.pointsNegative(Talent.MYOPIA) > 0) acc *= 1f - 0.2f*h.pointsNegative(Talent.MYOPIA);
		acc *= TransformationBossTalents.accuracy(h);
		acc *= comboFocusAccuracy(h);
		return acc;
	}

	public static float evasionBonus(Hero h){
		Armor a = h.belongings.armor();
		if (a == null || !h.hasTalent(Talent.LIGHT_APPLICATION)) return 0;
		return (5 - a.tier) * h.pointsInTalent(Talent.LIGHT_APPLICATION);
	}

	public static int armor(Hero h, int dr){
		if (h.pointsNegative(Talent.SHELL_CRY) > 0 && h.HT > 0){
			dr = (int)(dr*(1f - ((float)(h.HT-h.HP)/h.HT/2f)*h.pointsNegative(Talent.SHELL_CRY)));
		}
		return TransformationBossTalents.armor(h, dr);
	}

	public static int armorMax(Armor a){
		Hero h = hero();
		if (h == null || h.belongings.armor() != a) return 0;
		return h.pointsInTalent(Talent.HEAVY_APPLICATION)*a.tier + h.pointsInTalent(Talent.THICKENED_ARMOR);
	}

	public static float speed(Hero h){
		float s = 1f;
		boolean charging = h.buff(Recharging.class) != null || h.buff(ArtifactRecharge.class) != null;
		if (charging && h.pointsInTalent(Talent.OVERLOAD_CHARGE) == 1) s *= 1.5f;
		else if (charging && h.pointsInTalent(Talent.OVERLOAD_CHARGE) == 2) s *= 1.75f;
		if (h.HP < h.HT*0.3f && h.hasTalent(Talent.AFRAID_DEATH)) s *= 1.2f + 0.1f*h.pointsInTalent(Talent.AFRAID_DEATH);
		if (h.hasTalent(Talent.HOMETOWN_CLOUD) && h.belongings.getItem(com.shatteredpixel.shatteredpixeldungeon.items.Amulet.class) != null) s *= 1f + 0.2f*h.pointsInTalent(Talent.HOMETOWN_CLOUD);
		if (h.hasTalent(Talent.SEA_WIND) && Dungeon.level != null && h.pos >= 0 && Dungeon.level.water[h.pos]) s *= 1f + 0.2f*h.pointsInTalent(Talent.SEA_WIND);
		return s * rageSpeed(h) * TransformationBossTalents.speed(h);
	}

	public static int reachBonus(Char owner){
		return owner instanceof Hero && ((Hero) owner).pointsInTalent(Talent.ACCUMULATE_STEADILY) == 3 ? 1 : 0;
	}

	public static int reachBonus(Char owner, Item weapon){
		int bt = owner instanceof Hero && ((Hero) owner).hasTalent(Talent.BLOODTHIRSTY_BERSERK) && berserking((Hero) owner) ? 1 : 0;
		return reachBonus(owner) + longArm(owner, weapon) + bt;
	}

	public static int itemLevelBonus(Item item){
		Hero h = hero();
		if (h == null) return 0;
		int b = 0;
		//transformation, the Equipment Bless spell
		if ((item instanceof Weapon || item instanceof Armor) && item.isEquipped(h)
				&& h.buff(com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.Equipment_Bless.Blessbuff.class) != null){
			b += h.pointsInTalent(Talent.EQUIPMENT_BLESS);
		}
		b += focusedLevel(h, item);
		if (!item.cursed) return b;
		if ((item instanceof Weapon || item instanceof Armor) && h.hasTalent(Talent.PROTECT_CURSE)) b += h.pointsInTalent(Talent.PROTECT_CURSE);
		b -= h.pointsNegative(Talent.MAL_CURSE);
		return b;
	}

	public static float pickupDelay(float t){
		int pts = p(Talent.AUTO_PICK);
		if (pts == 2) return 0f;
		if (pts == 1) return t/2f;
		return t;
	}

	//---- hero actions --------------------------------------------------------------------------

	public static void onRest(Hero h){
		if (h.hasTalent(Talent.GET_UP)) Buff.affect(h, NoSleep.class);
		if (h.pointsNegative(Talent.LIFE_SPORT) > 0){
			h.damage(h.pointsNegative(Talent.LIFE_SPORT), Talent.LIFE_SPORT);
			if (!h.isAlive()){
				Dungeon.fail(Talent.LIFE_SPORT.getClass());
				GLog.n(Messages.get(TransformationTalents.class, "life_sport_kill"));
			}
		}
	}

	public static void onMove(Hero h){
		NoSleep ns = h.buff(NoSleep.class);
		if (ns != null) ns.detach();
		if (h.fieldOfView != null && h.pointsInTalent(Talent.EXPLORATION_INTUITION) > Random.Int(2)) h.search(false);
		if (h.pointsNegative(Talent.WINTER_SWIMMING) > Random.Int(20) && Dungeon.level.water[h.pos]) Buff.affect(h, Chill.class, 3);
		if (h.pointsNegative(Talent.BURNOUT_CHAMPION) > Random.Int(50)) Buff.affect(h, Burning.class).reignite(h, 3);
		if (h.pointsNegative(Talent.HAND_SLIP) > 0) Buff.prolong(h, HandSlipVulnerability.class, 1f);
		TransformationBossTalents.onMove(h);
	}

	public static void onLevelUp(Hero h){
		if (h.hasTalent(Talent.BEYOND_LIMIT)){
			Buff.affect(h, Haste.class, 20*h.pointsInTalent(Talent.BEYOND_LIMIT));
			Buff.affect(h, Adrenaline.class, 20*h.pointsInTalent(Talent.BEYOND_LIMIT));
		}
		if (h.hasTalent(Talent.HERO_NAME)) h.heal(h.HT*h.pointsInTalent(Talent.HERO_NAME)/5);
		if (h.hasTalent(Talent.ACTIVE_MUSCLES)) Buff.affect(h, AdrenalineSurge.class).reset(2*h.pointsInTalent(Talent.ACTIVE_MUSCLES), 400);
	}

	public static void onRestore(Hero h){
		if (h.pointsNegative(Talent.FAST_DIE) > 0){
			h.HP = Math.max(h.HP - (int)(h.HT*0.2f*h.pointsNegative(Talent.FAST_DIE)), 1);
		}
	}

	public static void onNewFloor(Hero h){
		com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Necronomicon.checkBookQuest();
		if (h.pointsNegative(Talent.USURY) > 0){
			int pp = 100 + 50*h.pointsNegative(Talent.USURY);
			Dungeon.gold = Math.max(0, Dungeon.gold - pp);
			GLog.w(Messages.get(TransformationTalents.class, "usury_interest", pp));
		}
		if (h.pointsInTalent(Talent.TREASURE_SENSE) > Random.Int(6)){
			Buff.affect(h, Awareness.class, Awareness.DURATION);
		}
	}

	//---- eating, drinking, reading -------------------------------------------------------------

	public static void onFoodEaten(Hero h, Item food){
		hornEaten(h, food);
		if (h.hasTalent(Talent.DELICIOUS_FLYING)) Buff.affect(h, Levitation.class, 5 + 10*h.pointsInTalent(Talent.DELICIOUS_FLYING));
		if (h.hasTalent(Talent.TREAT_MEAL)) Buff.affect(h, Healing.class).setHeal(1 + 2*h.pointsInTalent(Talent.TREAT_MEAL), 0.25f, 0);
		if (h.hasTalent(Talent.BLESS_MEAL)) Buff.affect(h, Bless.class, 15 + 10*h.pointsInTalent(Talent.BLESS_MEAL));
		if (h.hasTalent(Talent.MEAL_SHIELD)) Buff.affect(h, Barrier.class).setShield(4*h.pointsInTalent(Talent.MEAL_SHIELD));
		if (h.hasTalent(Talent.NURTRITIOUS_MEAL)){
			h.heal(2*h.pointsInTalent(Talent.NURTRITIOUS_MEAL));
			Buff.affect(h, Barrier.class).setShield(2*h.pointsInTalent(Talent.NURTRITIOUS_MEAL));
		}
		if (h.hasTalent(Talent.INVINCIBLE_MEAL) && takeMealSlot(h)){
			Buff.affect(h, Invulnerability.class, 1 + h.pointsInTalent(Talent.INVINCIBLE_MEAL));
		}
		if (h.hasTalent(Talent.EARTH_MEAL)) Barkskin.conditionallyAppend(h, (h.lvl*h.pointsInTalent(Talent.EARTH_MEAL))/3*2, 1);
		if (h.hasTalent(Talent.GOLD_MEAL)) dropAtHero(new Gold().quantity(15 + 10*h.pointsInTalent(Talent.GOLD_MEAL)));
		if (h.hasTalent(Talent.ENDLESS_MEAL)) Buff.affect(h, Swiftthistle.TimeBubble.class).reset(2*h.pointsInTalent(Talent.ENDLESS_MEAL));
		if (h.hasTalent(Talent.EXPERIENCE_MEAL) && takeMealSlot(h)){
			int exp = 1 + h.pointsInTalent(Talent.EXPERIENCE_MEAL);
			h.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(exp), FloatingText.EXPERIENCE);
			h.earnExp(exp, null);
		}
		if (h.pointsInTalent(Talent.BIRTHDAY_GIFT) > Random.Int(6) && takeMealSlot(h)) dropAtHero(RingOfWealth.genLowValueConsumable());
		if (h.pointsNegative(Talent.EXPLOSION_MEAL) > Random.Int(10)) new Bomb.ConjuredBomb().explode(h.pos);
		if (h.hasTalent(Talent.HEALTHY_FOOD)) Buff.affect(h, PotionOfCleansing.Cleanse.class, h.pointsInTalent(Talent.HEALTHY_FOOD) + 1);
		if (h.pointsNegative(Talent.VIP_MEAL) > 0) Dungeon.gold -= (int)(0.1f*h.pointsNegative(Talent.VIP_MEAL)*Dungeon.gold);
		if (h.hasTalent(Talent.ICE_MEAL)){
			Buff.affect(h, FrostImbue.class, Math.min(4, 2*h.pointsInTalent(Talent.ICE_MEAL)));
			if (h.pointsInTalent(Talent.ICE_MEAL) > 2){
				for (int i : PathFinder.NEIGHBOURS8){
					Char ch = Actor.findChar(h.pos + i);
					if (ch != null && ch.alignment != Char.Alignment.ALLY) Buff.affect(ch, Frost.class, Frost.DURATION);
				}
			}
		}
	}

	//the meal talents that make eating take one turn
	public static boolean fastMeal(Hero h){
		return h != null && (h.hasTalent(Talent.BLESS_MEAL) || h.hasTalent(Talent.ICE_MEAL));
	}

	public static float foodValue(Hero h, Item food, float val){
		if (h.hasTalent(Talent.EATEN_SLOWLY)) val *= 1f + 0.3f*h.pointsInTalent(Talent.EATEN_SLOWLY);
		if (h.pointsInTalent(Talent.AID_STOMACH) == 2 && food instanceof MysteryMeat) val *= 4/3f;
		return val;
	}

	public static float eatingTime(Hero h, float time){
		if (h != null && h.hasTalent(Talent.EATEN_SLOWLY)) return time + 10;
		return time;
	}

	public static void onPotionThrown(Hero h, int cell){
		if (h.hasTalent(Talent.WITCH_POTION)){
			Char mob = Actor.findChar(cell);
			if (mob != null && !(mob instanceof Hero)){
				Buff.affect(mob, Charm.class, 15).object = h.id();
				if (h.pointsInTalent(Talent.WITCH_POTION) == 2) Buff.affect(mob, Poison.class).set(5);
			}
		}
	}

	public static void onPotionDrunk(Hero h, Item potion){
		if (h.hasTalent(Talent.ANESTHESIA)) Buff.affect(h, Barrier.class).setShield(2 + 3*h.pointsInTalent(Talent.ANESTHESIA));
		if (h.hasTalent(Talent.WANLING_POTION)){
			Buff.affect(h, PotionOfCleansing.Cleanse.class, h.pointsInTalent(Talent.WANLING_POTION) + 1f);
			if (h.pointsInTalent(Talent.WANLING_POTION) == 3) clearDebuffs(h);
		}
		if (potion instanceof com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame && h.hasTalent(Talent.BACKFIRED)){
			Buff.affect(h, FireImbue.class).set(2 + 3*h.pointsInTalent(Talent.BACKFIRED));
		}
		if (h.hasTalent(Talent.BOOZY)){
			Buff.affect(h, Healing.class).setHeal(5*h.pointsInTalent(Talent.BOOZY), 0.2f, 1);
			Barkskin.conditionallyAppend(h, 5*h.pointsInTalent(Talent.BOOZY), 1);
		}
		if (h.pointsNegative(Talent.CHILL_WATER) == 2 && 6 > Random.Int(20)) Buff.affect(h, Chill.class, 3);
		if (h.hasTalent(Talent.WATER_ISFOOD)){
			Buff.affect(h, Hunger.class).satisfy(30);
			if (h.pointsInTalent(Talent.WATER_ISFOOD) == 3) Talent.onFoodEaten(h, 0, null);
		}
		if (potion instanceof PotionOfExperience
				|| potion instanceof com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfDivineInspiration){
			int pe = h.pointsInTalent(Talent.PRECIOUS_EXPERIENCE);
			if (pe >= 1) h.earnExp(h.maxExp()/2, potion.getClass());
			if (pe == 2) Buff.affect(h, Bless.class, 50);
		}
	}

	public static void onDewDrunk(Hero h, int drops){
		if (h.hasTalent(Talent.JASMINE_TEA)) Buff.affect(h, Bless.class, 2*h.pointsInTalent(Talent.JASMINE_TEA) + 1);
		if (h.pointsNegative(Talent.CHILL_WATER)*3 > Random.Int(20)) Buff.affect(h, Chill.class, 3);
		if (h.pointsInTalent(Talent.WATER_ISFOOD) >= 2){
			Buff.affect(h, Hunger.class).satisfy(drops*10);
			if (h.pointsInTalent(Talent.WATER_ISFOOD) == 3) Talent.onFoodEaten(h, 0, null);
		}
	}

	public static int waterskinBonus(){
		return 15*p(Talent.MILITARY_WATERSKIN);
	}

	public static void onDewCollected(Hero h){
		if (h.pointsNegative(Talent.WASH_HAND) > Random.Int(10)) Buff.affect(h, Poison.class).set(3);
	}

	public static void onScrollUsed(Hero h){
		if (h.hasTalent(Talent.MAGIC_GIRL)){
			Buff.affect(h, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HeroDisguise.class, 15 + 15*h.pointsInTalent(Talent.MAGIC_GIRL));
			if (h.pointsInTalent(Talent.MAGIC_GIRL) == 2) Buff.affect(h, Light.class, 10);
		}
		if (h.hasTalent(Talent.SECRET_LIGHTING)) Buff.affect(h, Light.class, 20*h.pointsInTalent(Talent.SECRET_LIGHTING));
		if (h.hasTalent(Talent.READ_PROFITABLE)){
			switch (Random.Int(h.pointsInTalent(Talent.READ_PROFITABLE) + 2)){
				case 0: Buff.affect(h, Levitation.class, 5); break;
				case 1: Buff.affect(h, PotionOfCleansing.Cleanse.class, 5); break;
				case 2: Buff.affect(h, Haste.class, 5); break;
				case 3: Buff.affect(h, Invisibility.class, 5); break;
				case 4: h.heal(h.HT/8); break;
			}
		}
		if (h.hasTalent(Talent.GOLDOFBOOK)) dropAtHero(new Gold().quantity(50*h.pointsInTalent(Talent.GOLDOFBOOK)));
		if (h.hasTalent(Talent.CHANGQI_BOOKSTORE) && 3*h.pointsInTalent(Talent.CHANGQI_BOOKSTORE) + 3 > Random.Int(20) && takeItemSlot(h)){
			dropAtHero(Generator.randomUsingDefaults(Generator.Category.STONE));
		}
	}

	public static void onItemCollected(Hero h, Item item){
		if (h.hasTalent(Talent.WIDE_KNOWLEDGE)) item.cursedKnown = true;
		if (h.pointsInTalent(Talent.WIDE_KNOWLEDGE) >= 2 && !item.isIdentified()){
			item.identify();
			if (h.pointsInTalent(Talent.WIDE_KNOWLEDGE) == 3) Buff.affect(h, Recharging.class, 3);
		}
	}

	public static void onItemPickedUp(Hero h){
		if (h.pointsNegative(Talent.ENDLESS_MALICE) > 0 && Random.Int(10) < 2 + h.pointsNegative(Talent.ENDLESS_MALICE)){
			h.damage((int)(h.HT*h.pointsNegative(Talent.ENDLESS_MALICE)*0.05f), Talent.ENDLESS_MALICE);
			if (!h.isAlive()){
				Dungeon.fail(Talent.ENDLESS_MALICE.getClass());
				GLog.n(Messages.get(TransformationTalents.class, "endless_malice_kill"));
			}
		}
	}

	public static void onGoldPickedUp(Hero h){
		if (h.hasTalent(Talent.COLLECTION_GOLD)) Buff.affect(h, Barrier.class).incShield(h.pointsInTalent(Talent.COLLECTION_GOLD));
		int gf = h.pointsInTalent(Talent.GOLD_FORMATION);
		if (gf == 1 && Dungeon.gold > 500){ Dungeon.gold -= 500; dropAtHero(RingOfWealth.genLowValueConsumable()); }
		else if (gf == 2 && Dungeon.gold > 1000){ Dungeon.gold -= 1000; dropAtHero(RingOfWealth.genMidValueConsumable()); }
		else if (gf == 3 && Dungeon.gold > 2000){ Dungeon.gold -= 2000; dropAtHero(RingOfWealth.genHighValueConsumable()); }
	}

	//---- combat --------------------------------------------------------------------------------

	public static int onAttackProc(Hero h, Char enemy, int dmg){
		KindOfWeapon wep = h.belongings.attackingWeapon();
		boolean melee = !(wep instanceof MissileWeapon);
		int dist = Dungeon.level.distance(h.pos, enemy.pos);

		//flat bonuses first
		if (h.hasTalent(Talent.HEDONISM) && h.buff(Hunger.class) != null && !h.buff(Hunger.class).isHungry()) dmg += 2*h.pointsInTalent(Talent.HEDONISM);
		if (h.hasTalent(Talent.FEAR_INCARNATION) && enemy.buff(Terror.class) != null) dmg += 1 + 2*h.pointsInTalent(Talent.FEAR_INCARNATION);
		if (h.hasTalent(Talent.ATTACK_DOOR) && Dungeon.level.map[enemy.pos] == Terrain.OPEN_DOOR) dmg += h.pointsInTalent(Talent.ATTACK_DOOR);
		if (h.hasTalent(Talent.WATER_ATTACK) && Dungeon.level.water[h.pos]) dmg += h.pointsInTalent(Talent.WATER_ATTACK);

		//multipliers
		float m = 1f;
		boolean charging = h.buff(Recharging.class) != null || h.buff(ArtifactRecharge.class) != null;
		if (melee && charging && h.pointsInTalent(Talent.OVERLOAD_CHARGE) == 1) m *= 1.3f;
		else if (melee && charging && h.pointsInTalent(Talent.OVERLOAD_CHARGE) == 2) m *= 1.5f;
		if (h.hasTalent(Talent.OVERWHELMING) && h.attackDelay() > 1) m *= 1f + (h.attackDelay()-1)*0.33f*h.pointsInTalent(Talent.OVERWHELMING);
		boolean evil = enemy.properties().contains(Char.Property.UNDEAD) || enemy.properties().contains(Char.Property.DEMONIC);
		if (h.hasTalent(Talent.JUSTICE_PUNISH) && evil) m *= 1.1f + 0.1f*h.pointsInTalent(Talent.JUSTICE_PUNISH);
		if (h.hasTalent(Talent.AMAZING_EYESIGHT) && melee && dist >= 4 - h.pointsInTalent(Talent.AMAZING_EYESIGHT)) m *= 1.35f;
		if (h.hasTalent(Talent.RAGE_ATTACK)) m *= 1f + 0.1f*h.pointsInTalent(Talent.RAGE_ATTACK);
		if (h.pointsInTalent(Talent.GOD_LEFTHAND) == 2 && h.pointsInTalent(Talent.GOD_RIGHTHAND) == 2) m *= 1.6f;
		else if (h.hasTalent(Talent.GOD_LEFTHAND) && h.hasTalent(Talent.GOD_RIGHTHAND)) m *= 1.3f;
		if (h.hasTalent(Talent.GIANT_KILLER) && enemy.properties().contains(Char.Property.LARGE)) m *= 1f + 0.35f*h.pointsInTalent(Talent.GIANT_KILLER);
		if (h.hasTalent(Talent.AFRAID_DEATH) && h.HP < h.HT*0.3f) m *= 0.4f + 0.1f*h.pointsInTalent(Talent.AFRAID_DEATH);
		if (h.hasTalent(Talent.SKY_EARTH) && (h.buff(Roots.class) != null || h.buff(Levitation.class) != null || h.buff(Earthroot.Armor.class) != null)) m *= 1f + 0.15f*h.pointsInTalent(Talent.SKY_EARTH);
		boolean cold = enemy.buff(Chill.class) != null || enemy.buff(Frost.class) != null;
		if (h.pointsInTalent(Talent.DEEP_FREEZE) == 3 && cold) m *= h.hasTalent(Talent.ICE_BREAKING) ? 1.5f : 1.2f;
		if (h.hasTalent(Talent.LOVE_BACKSTAB) && enemy.buff(Charm.class) != null) m *= 1f + 0.15f*h.pointsInTalent(Talent.LOVE_BACKSTAB);
		if (h.pointsNegative(Talent.FEEBLE) > 0) m *= 1f - 0.1f*h.pointsNegative(Talent.FEEBLE);
		if (h.hasTalent(Talent.PRECISE_SHOT) && !melee && dist >= 3) m *= 1f + 0.1f*h.pointsInTalent(Talent.PRECISE_SHOT);
		if (h.hasTalent(Talent.POISON_INBODY)) m *= 1f + 0.1f*h.pointsInTalent(Talent.POISON_INBODY)*debuffCount(h);
		if (h.pointsNegative(Talent.PHOTOPHOBY) > 0 && h.buff(Light.class) != null) m *= 1f - 0.2f*h.pointsNegative(Talent.PHOTOPHOBY);
		if (h.hasTalent(Talent.SHOOT_SATELLITE) && enemy.flying){
			m *= 1.1f + 0.1f*h.pointsInTalent(Talent.SHOOT_SATELLITE);
			if (wep instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingStone) m *= 2f;
		}
		if (h.hasTalent(Talent.SWEET_SLEEP) && enemy instanceof Mob && ((Mob) enemy).surprisedBy(h)) m *= 1.2f + 0.1f*h.pointsInTalent(Talent.SWEET_SLEEP);
		if (h.hasTalent(Talent.WELLFED_MEAL) && h.buff(WellFed.class) != null) m *= 1f + 0.15f*h.pointsInTalent(Talent.WELLFED_MEAL);
		dmg = Math.round(dmg * m);

		//on hit effects
		if (h.hasTalent(Talent.STRONG_ATTACK) && melee && h.buff(StrAtkCooldown.class) == null){
			Buff.affect(enemy, Vulnerable.class, 1 + 2*h.pointsInTalent(Talent.STRONG_ATTACK));
			Buff.affect(h, StrAtkCooldown.class, 15);
		}
		if (h.pointsInTalent(Talent.DISTURB_ATTACK) > Random.Int(10)) Buff.affect(enemy, Vertigo.class, 3);
		if (h.hasTalent(Talent.GHOLL_WITCHCRAFT) && enemy.buff(PinCushion.class) != null) Buff.affect(enemy, Hex.class, h.pointsInTalent(Talent.GHOLL_WITCHCRAFT) + 1);
		if (h.hasTalent(Talent.COVER_SCAR) && melee) Buff.affect(enemy, Bleeding.class).set(h.pointsInTalent(Talent.COVER_SCAR));
		if (h.hasTalent(Talent.ICE_BREAKING) && enemy.buff(Chill.class) != null && enemy.isAlive()) enemy.damage(h.pointsInTalent(Talent.ICE_BREAKING), new WandOfMagicMissile());
		if (h.hasTalent(Talent.POSION_DAGGER) && melee && wep instanceof MeleeWeapon && ((MeleeWeapon) wep).hasTrait(MeleeWeapon.WeaponTrait.DAGGER, h)){
			Buff.affect(enemy, Poison.class).set(2*h.pointsInTalent(Talent.POSION_DAGGER));
		}
		if (h.pointsInTalent(Talent.DEEP_FREEZE) >= 2) Buff.affect(enemy, Chill.class, 3);
		if (h.pointsInTalent(Talent.FRENZIED_ATTACK) > Random.Int(4)) Buff.affect(enemy, Amok.class, 2);
		if (h.hasTalent(Talent.ABACUS) && h.pointsInTalent(Talent.ABACUS) >= Random.Int(4)) Buff.affect(h, Barrier.class).incShield(2);
		if (h.hasTalent(Talent.DAMAGED_CORE) && enemy.isAlive()) enemy.damage(h.pointsInTalent(Talent.DAMAGED_CORE), new WandOfMagicMissile());
		if (h.pointsInTalent(Talent.FUDI_CHOUXIN) > Random.Int(4)) Buff.affect(enemy, Weakness.class, 2);
		if (h.pointsNegative(Talent.MENTAL_COLLAPSE) > Random.Int(10) && enemy.isAlive()) h.damage(dmg/2, new WandOfMagicMissile());
		if (h.pointsNegative(Talent.FIRE_WOOD) > Random.Int(20)){
			Buff.affect(h, Burning.class).reignite(h, 3);
			Buff.affect(enemy, Burning.class).reignite(enemy, 3);
			Buff.affect(h, Charm.class, 10).object = enemy.id();
			Buff.affect(enemy, Charm.class, 10).object = h.id();
		}
		if (h.hasTalent(Talent.ASHES_BOW) && !melee && h.buff(AshesBowCooldown.class) == null){
			Buff.affect(enemy, Burning.class).reignite(enemy, h.pointsInTalent(Talent.ASHES_BOW));
			Buff.affect(h, AshesBowCooldown.class, 15);
		}
		if (h.pointsInTalent(Talent.NIRVANA) == 2 && h.buff(Burning.class) != null) Buff.affect(enemy, Burning.class).reignite(enemy, 3);
		if (h.hasTalent(Talent.HAND_DESTRUCTION) && enemy.HT > 0 && enemy != h
				&& h.pointsInTalent(Talent.HAND_DESTRUCTION)*0.05f > (float)enemy.HP/enemy.HT
				&& !enemy.isImmune(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim.class)
				&& !enemy.properties().contains(Char.Property.BOSS)){
			if (enemy.sprite != null) enemy.sprite.emitter().burst(ShadowParticle.UP, 5);
			enemy.die(h);
		}
		if (h.pointsInTalent(Talent.THROWING_RECYCLING) > Random.Int(4) && enemy.buff(PinCushion.class) != null){
			while (enemy.buff(PinCushion.class) != null){
				Item item = enemy.buff(PinCushion.class).grabOne();
				if (!item.doPickUp(h, enemy.pos)) Dungeon.level.drop(item, enemy.pos).sprite.drop();
			}
		}
		dmg = TransformationBossTalents.onAttackProc(h, enemy, dmg);
		dmg += comboFocusDamage(h);
		onSoulMarkedHit(h, enemy);
		if (h.pointsNegative(Talent.STEAM_BEAN) > 0 && enemy.buff(SteamBeanMark.class) == null){
			Buff.affect(h, Daze.class, 4*h.pointsNegative(Talent.STEAM_BEAN));
			Buff.affect(enemy, SteamBeanMark.class);
		}
		Berserk bz = h.buff(Berserk.class);
		if (melee && bz != null && h.hasTalent(Talent.BLOODTHIRSTY_BERSERK)){
			int bpts = h.pointsInTalent(Talent.BLOODTHIRSTY_BERSERK);
			if (bz.raging()) h.HP = Math.min(h.HT, h.HP + Math.round(dmg*(0.125f + 0.125f*bpts)));
			else bz.addRage(dmg*0.005f*bpts);
		}
		dmg = alternatingWeapons(h, dmg);
		dmg = huntingTechnique(h, enemy, dmg);
		phantomShooter(h, enemy);
		return dmg;
	}

	//the hero is hit, after armor, as Hero.defenseProc
	public static int onDefenseProc(Hero h, Char enemy, int damage){
		if (h.hasTalent(Talent.MIRRORED_REVENGE)){
			BrokenSeal.WarriorShield ws = h.buff(BrokenSeal.WarriorShield.class);
			if (ws != null && ws.coolingDown()) ws.reduceCooldown(Random.IntRange(5, 10)/150f);
		}
		if (h.hasTalent(Talent.RAGE_ATTACK)) damage = Math.round(damage*(1f + 0.1f*h.pointsInTalent(Talent.RAGE_ATTACK)));
		damage = TransformationBossTalents.onDefenseProc(h, enemy, damage);
		if (h.hasTalent(Talent.SURVIVAL_VOLITION) && h.HP < h.HT*0.3f && h.buff(SurVolCooldown.class) == null){
			int duration = 1 + h.pointsInTalent(Talent.SURVIVAL_VOLITION);
			Buff.affect(h, Haste.class, duration);
			Buff.affect(h, SurVolCooldown.class, 80);
			Buff.affect(h, PotionOfCleansing.Cleanse.class, duration);
			Buff.affect(h, SurResistance.class, duration);
		}
		if (enemy == null) return damage;
		boolean adjacent = Dungeon.level.distance(h.pos, enemy.pos) < 2;
		if (h.hasTalent(Talent.COUNTER_ATTACK) && adjacent && Random.Int(100) < 5 + 15*h.pointsInTalent(Talent.COUNTER_ATTACK) && enemy.isAlive()){
			enemy.damage(Math.max(1, h.lvl/2), h);
		}
		if (h.buff(Light.class) != null && h.hasTalent(Talent.ANGEL_STANCE) && enemy.isAlive()){
			enemy.damage((int)(damage*(0.2f + 0.1f*h.pointsInTalent(Talent.ANGEL_STANCE))), new WandOfMagicMissile());
		}
		if (h.hasTalent(Talent.THORNY_ROSE) && enemy.isAlive()){
			Buff.affect(enemy, Bleeding.class).set(1);
			if (h.pointsInTalent(Talent.THORNY_ROSE) == 2) enemy.damage(1, h);
		}
		if (h.hasTalent(Talent.SPIDER_SENSE) && !Dungeon.level.heroFOV[enemy.pos]){
			Buff.affect(h, Swiftthistle.TimeBubble.class).reset(h.pointsInTalent(Talent.SPIDER_SENSE));
		}
		if (h.hasTalent(Talent.NO_VIEWRAPE) && !adjacent && enemy.isAlive()){
			Buff.affect(enemy, Blindness.class, h.pointsInTalent(Talent.NO_VIEWRAPE));
		}
		return damage;
	}

	//any damage the hero takes, before shields
	public static int onHeroDamage(Hero h, int dmg, Object src){
		if (dmg <= 0) return dmg;
		boolean hunger = src instanceof Hunger;
		if (h.hasTalent(Talent.WELLFED_MEAL) && h.buff(WellFed.class) != null) dmg = Math.round(dmg*(1f - 0.1f*h.pointsInTalent(Talent.WELLFED_MEAL)));
		if (h.buff(SurResistance.class) != null) dmg = Math.round(dmg*0.5f);
		NoSleep ns = h.buff(NoSleep.class);
		if (ns != null && h.hasTalent(Talent.GET_UP)){
			Buff.affect(h, Adrenaline.class, 2*h.pointsInTalent(Talent.GET_UP) + 1);
			ns.detach();
		}
		if (src instanceof Char && h.hasTalent(Talent.DETOX_DAMAGE) && 5*h.pointsInTalent(Talent.DETOX_DAMAGE) > Random.Int(20)){
			Buff.affect(h, PotionOfCleansing.Cleanse.class, 1);
			if (Random.Int(20) < 3) clearDebuffs(h);
		}
		if (h.hasTalent(Talent.CONCEPT_GRID) && !hunger) dmg = Math.max(0, dmg - h.pointsInTalent(Talent.CONCEPT_GRID));
		if (h.pointsNegative(Talent.UNAVOIDABLE) > 0 && !hunger && !(src instanceof Bleeding)){
			dmg = Math.max((int)(h.HT*0.05f*h.pointsNegative(Talent.UNAVOIDABLE)), dmg);
		}
		if (h.pointsNegative(Talent.HAND_SLIP) > 0 && h.buff(HandSlipVulnerability.class) != null){
			dmg = Math.round(dmg*(1f + 0.25f*h.pointsNegative(Talent.HAND_SLIP)));
		}
		if (h.hasTalent(Talent.MIRRORED_REVENGE) && src instanceof Char && src != h && ((Char) src).isAlive()){
			int absorbed = Math.min(dmg, h.shielding());
			if (absorbed > 0) ((Char) src).damage(Math.round(absorbed*(0.5f + 0.5f*h.pointsInTalent(Talent.MIRRORED_REVENGE))), Talent.MIRRORED_REVENGE);
		}
		return TransformationBossTalents.onHeroDamage(h, dmg, src);
	}

	public static void onHeroDodged(Hero h, Char attacker){
		if (attacker == null) return;
		if (h.hasTalent(Talent.EXTREME_REACTION)){
			Adrenaline a = h.buff(Adrenaline.class);
			if (a == null || a.cooldown() <= 5) Buff.affect(h, Adrenaline.class, h.pointsInTalent(Talent.EXTREME_REACTION));
		}
		if (h.hasTalent(Talent.CRAZY_DANCER)) Buff.affect(attacker, Amok.class, 1 + h.pointsInTalent(Talent.CRAZY_DANCER));
	}

	//---- monsters ------------------------------------------------------------------------------

	public static int mobAttackProc(Mob mob, Char enemy, int damage){
		Hero h = hero();
		if (h == null || mob instanceof NPC) return damage;
		if (h.pointsNegative(Talent.BAT_SERUM) > 0 && mob.alignment == Char.Alignment.ENEMY) mob.HP = Math.min(mob.HT, mob.HP + (int)(damage*0.2f*h.pointsNegative(Talent.BAT_SERUM)));
		if (h.pointsNegative(Talent.DUMP_TRUCK) > 0 && mob.properties().contains(Char.Property.LARGE)) damage = Math.round(damage*(1f + 0.5f*h.pointsNegative(Talent.DUMP_TRUCK)));
		if (enemy == h && h.pointsNegative(Talent.DISASTER_CURSE) > Random.Int(4)){
			Buff.affect(enemy, Slow.class, 1);
			Buff.affect(enemy, Weakness.class, 1);
		}
		if (mob instanceof Mimic && h.pointsNegative(Talent.FULLPASSION) > 0) damage = Math.round(damage*(1f + 0.3f*h.pointsNegative(Talent.FULLPASSION)));
		if (mob instanceof Wraith && enemy == h && h.pointsNegative(Talent.HUNGRY_GHOST) == 2 && h.buff(Hunger.class) != null){
			h.buff(Hunger.class).affectHunger(-5f);
		}
		if (mob.buff(com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.freeman.SalesContract.SaleSelf.class) != null
				&& enemy != h && h.hasTalent(Talent.PRIM_ACCU)){
			int quantity = 10*h.pointsInTalent(Talent.PRIM_ACCU);
			Dungeon.gold += quantity;
			Statistics.goldCollected += quantity;
			h.sprite.showStatusWithIcon(CharSprite.NEUTRAL, Integer.toString(quantity), FloatingText.GOLD);
		}
		damage = mindImprisonment(mob, enemy, damage);
		damage = armedUprising(mob, enemy, damage);
		if (enemy == h && h.pointsNegative(Talent.PHASECLAW) >= 2 && Random.Int(10) == 0) Buff.affect(h, Vulnerable.class, 3f);
		return damage;
	}

	public static float mobSpeed(Mob mob){
		Hero h = hero();
		if (h == null) return 1f;
		float s = 1f;
		if (h.pointsNegative(Talent.DUMP_TRUCK) > 0 && mob.properties().contains(Char.Property.LARGE)) s *= 1f + 0.5f*h.pointsNegative(Talent.DUMP_TRUCK);
		if (mob instanceof Mimic && h.pointsNegative(Talent.FULLPASSION) > 0) s *= 1f + 0.5f*h.pointsNegative(Talent.FULLPASSION);
		return s;
	}

	public static int mobDamageTaken(Mob mob, int dmg){
		Hero h = hero();
		if (h == null || dmg <= 0) return dmg;
		if (h.hasTalent(Talent.FISHING_TIME) && isFish(mob)) dmg = Math.round(dmg*1.3f);
		return dmg;
	}

	public static boolean isFish(Mob mob){
		return mob instanceof com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Piranha
				|| mob.getClass().getSimpleName().contains("Piranha");
	}

	public static void onMobAwaken(Mob mob){
		int jf = n(Talent.JUMP_FACE_SILICONE);
		if (jf > 0 && mob.alignment == Char.Alignment.ENEMY) Buff.affect(mob, Swiftthistle.TimeBubble.class).reset(jf);
	}

	//the Resurrection spell, a creature with its buff comes back once
	public static boolean revives(Char ch){
		com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.Resurrection.REsurrection b = ch.buff(com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.Resurrection.REsurrection.class);
		if (b == null || ch.properties().contains(Char.Property.BOSS) || ch.properties().contains(Char.Property.BOSS_MINION)) return false;
		b.detach();
		ch.HP = ch instanceof Hero ? 1 : ch.HT;
		if (p(Talent.RESURRECTION) == 3) Buff.affect(ch, Bless.class, 20);
		return true;
	}

	//true when the monster refuses to die (Unable Rest)
	public static boolean preventsDeath(Mob mob, Object cause){
		if (cause != com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm.class && revives(mob)) return true;
		Hero h = hero();
		if (h == null || h.pointsNegative(Talent.UNABLE_REST) <= 0 || mob.buff(UnableRest.class) != null) return false;
		if (mob.alignment != Char.Alignment.ENEMY || mob.properties().contains(Char.Property.BOSS)) return false;
		if (!(mob.properties().contains(Char.Property.UNDEAD) || mob.properties().contains(Char.Property.DEMONIC))) return false;
		if (cause == com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm.class) return false;
		mob.HP = 1;
		Buff.affect(mob, UnableRest.class);
		if (h.pointsNegative(Talent.UNABLE_REST) == 2) Buff.affect(mob, Barrier.class).setShield(mob.HT/2);
		return true;
	}

	public static int fastBreak(){
		Hero h = hero();
		return h != null && h.hasTalent(Talent.FAST_BREAK) ? h.pointsInTalent(Talent.FAST_BREAK) + 1 : 0;
	}

	public static int bonusExp(int exp){
		int r = p(Talent.RETURNING_HONOR);
		return r > 0 ? exp + (int)(exp*0.25f*r) : exp;
	}

	public static void onMobDie(Mob mob, Object cause){
		Hero h = hero();
		if (h == null || !h.isAlive()) return;
		boolean enemy = mob.alignment == Char.Alignment.ENEMY;
		boolean heroKill = cause == h;

		if (enemy){
			if (heroKill && h.hasTalent(Talent.CICADA_DANCE)){
				Buff.affect(h, Invisibility.class, 1 + h.pointsInTalent(Talent.CICADA_DANCE));
				Buff.affect(h, EnemyDies.class, 1f);
			}
			if (heroKill) TransformationBossTalents.onKill(h, mob);
			if (h.hasTalent(Talent.INVINCIBLE)) Buff.affect(h, Adrenaline.class, 2*h.pointsInTalent(Talent.INVINCIBLE) + 1);
			if (h.hasTalent(Talent.JUSTICE_PUNISH) && (mob.properties().contains(Char.Property.UNDEAD) || mob.properties().contains(Char.Property.DEMONIC))){
				Buff.affect(h, Bless.class, h.pointsInTalent(Talent.JUSTICE_PUNISH));
			}
			if (mob.buff(Burning.class) != null && h.pointsInTalent(Talent.FLAME_INCARNATION) > Random.Int(10)){
				Elemental el = new Elemental.FireElemental();
				GameScene.add(el);
				Buff.affect(el, SummonElemental.InvisAlly.class);
				el.setSummonedALly();
				el.HP = el.HT;
				ScrollOfTeleportation.appear(el, mob.pos);
			}
			if (h.hasTalent(Talent.BURNING_BLOOD)) h.heal(h.pointsInTalent(Talent.BURNING_BLOOD));
			if (h.pointsNegative(Talent.FULLFIGHTING)*3 > Random.Int(10)){
				for (Mob m : Dungeon.level.mobs) m.beckon(h.pos);
				if (Dungeon.level.heroFOV[mob.pos]) CellEmitter.center(mob.pos).start(Speck.factory(Speck.SCREAM), 0.3f, 3);
				Sample.INSTANCE.play(Assets.Sounds.ALERT);
				GLog.n(Messages.get(TransformationTalents.class, "fullfighting"));
			}
			if (mob.properties().contains(Char.Property.INORGANIC) && h.pointsInTalent(Talent.ENGINEER_REFIT) > Random.Int(8)
					&& !(mob instanceof Wraith) && !(mob instanceof Skeleton)){
				DM100 m = new DM100();
				m.pos = mob.pos;
				GameScene.add(m);
				Buff.affect(m, Corruption.class);
			}
			if (h.pointsNegative(Talent.PARASITISM) > Random.Int(10)){
				int cell = freeCellNear(mob.pos);
				if (cell != -1){
					YogDzewa.Larva l = new YogDzewa.Larva();
					l.pos = cell;
					GameScene.add(l);
				}
			}
			if (h.hasTalent(Talent.ASH_LEDGER)) Buff.affect(h, Barrier.class).setShield(2*h.pointsInTalent(Talent.ASH_LEDGER));
			if (!Dungeon.level.heroFOV[mob.pos] && h.hasTalent(Talent.POWERFUL_CALCULATIONS)) h.heal(h.pointsInTalent(Talent.POWERFUL_CALCULATIONS));
			if (h.pointsInTalent(Talent.ZHUOJUN_BUTCHER) > Random.Int(10) && !mob.properties().contains(Char.Property.INORGANIC)
					&& !mob.properties().contains(Char.Property.UNDEAD) && h.lvl <= mob.maxLvl + 2){
				Dungeon.level.drop(new MysteryMeat(), mob.pos).sprite.drop(mob.pos);
			}
			if (h.hasTalent(Talent.INSTANT_REFINING) && h.pointsInTalent(Talent.INSTANT_REFINING) + 1 > Random.Int(5) && h.lvl <= mob.maxLvl + 2){
				Dungeon.energy += 1;
				h.sprite.showStatusWithIcon(0x44CCFF, "1", FloatingText.ENERGY);
			}
			if (h.hasTalent(Talent.WATER_GHOST) && Dungeon.level.water[mob.pos] && !(mob instanceof Wraith) && Random.Int(5) == 1){
				Wraith w = Wraith.spawnAt(mob.pos, Wraith.class);
				if (w != null){
					if (h.pointsInTalent(Talent.WATER_GHOST) > 1) Buff.affect(w, Corruption.class);
					else Buff.affect(w, Amok.class, 5);
					if (Dungeon.level.heroFOV[mob.pos]){
						CellEmitter.get(mob.pos).burst(ShadowParticle.CURSE, 6);
						Sample.INSTANCE.play(Assets.Sounds.CURSED);
					}
				}
			}
		}
		com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.freeman.SalesContract.onContractedDeath(mob);
		if (heroKill) perfectFinale(h);
		erodingSoulDeath(mob);
	}

	public static void onAllyDie(Char ally){
		Hero h = hero();
		if (h != null && ally != h && ally.alignment == Char.Alignment.ALLY && h.hasTalent(Talent.BEHEST)){
			Buff.affect(h, Adrenaline.class, 5*h.pointsInTalent(Talent.BEHEST));
		}
	}

	private static int freeCellNear(int pos){
		ArrayList<Integer> cells = new ArrayList<>();
		for (int i : PathFinder.NEIGHBOURS8){
			int c = pos + i;
			if (Actor.findChar(c) == null && Dungeon.level.passable[c]) cells.add(c);
		}
		return cells.isEmpty() ? -1 : Random.element(cells);
	}

	//---- wands ---------------------------------------------------------------------------------

	public static void onWandProc(Char target){
		Hero h = hero();
		if (h == null || target == null || target == h) return;
		if (h.hasTalent(Talent.INSINUATION) && target instanceof Mob && ((Mob) target).state == ((Mob) target).SLEEPING){
			Buff.affect(target, Poison.class).set(1 + 2*h.pointsInTalent(Talent.INSINUATION));
		}
		if (h.hasTalent(Talent.BURNING_CURSE) && target.buff(Burning.class) != null){
			Buff.affect(target, Cripple.class, 2);
			if (h.pointsInTalent(Talent.BURNING_CURSE) == 2) Buff.affect(target, Blindness.class, 2);
		}
		if (h.hasTalent(Talent.MORONITY)){
			int d = h.pointsInTalent(Talent.MORONITY);
			switch (Random.Int(3)){
				case 0: Buff.affect(target, Weakness.class, d); break;
				case 1: Buff.affect(target, Vulnerable.class, d); break;
				default: Buff.affect(target, Hex.class, d); break;
			}
		}
		if (h.hasTalent(Talent.QUANTUM_HACKING) && !(target instanceof NPC)) Buff.affect(target, Vertigo.class, h.pointsInTalent(Talent.QUANTUM_HACKING));
		TransformationBossTalents.onWandProc(h, target);
		boneDeep(h, target);
	}

	public static void onWandZap(Wand wand, int target){
		Hero h = hero();
		if (h != null) onAnyZap(h);
		if (h == null || Dungeon.level == null || target < 0 || target >= Dungeon.level.length()) return;
		if (h.hasTalent(Talent.GAS_SPURT) && h.buff(GasCooldown.class) == null && Dungeon.level.distance(h.pos, target) > 2){
			if (h.pointsInTalent(Talent.GAS_SPURT) == 2) GameScene.add(Blob.seed(target, 100, CorrosiveGas.class).setStrength(2 + Dungeon.scalingDepth()/5));
			else GameScene.add(Blob.seed(target, 100, ToxicGas.class));
			Buff.affect(h, GasCooldown.class, 15);
		}
		if (h.hasTalent(Talent.FIRE_BALL) && Dungeon.level.flamable[target]) GameScene.add(Blob.seed(target, h.pointsInTalent(Talent.FIRE_BALL), Fire.class));
		if (h.pointsNegative(Talent.OUTCONTROL_MAGIC) > Random.Int(5)) new Bomb.ConjuredBomb().explode(h.pos);
		wandZapMore(h, wand, target);
	}

	//true when this cast should not use a charge
	public static boolean wandChargeSaved(){
		int m = p(Talent.MAGIC_RECYCLING);
		return m > 0 && 5*m + 5 > Random.Int(100);
	}

	public static float wandRechargeFactor(){
		Hero h = hero();
		return (1f - 0.1f*p(Talent.EXTREME_CASTING)) * (h == null ? 1f : momentumReserve(h));
	}

	//damage a wand deals to a creature, called from Char.damage
	public static int onWandDamage(Wand wand, Char target, int dmg){
		Hero h = hero();
		if (h == null || dmg <= 0 || target == h) return dmg;
		if (h.hasTalent(Talent.PENETRATING_CAST) && target.alignment == Char.Alignment.ENEMY){
			int removed = 0;
			for (Buff b : target.buffs().toArray(new Buff[0])){
				if (b.type == Buff.buffType.NEGATIVE && !(b instanceof com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom)){ b.detach(); removed++; }
			}
			if (removed > 0) dmg = Math.round(dmg*(1f + removed*0.1f*h.pointsInTalent(Talent.PENETRATING_CAST)));
		}
		if (h.hasTalent(Talent.MAGIC_GIRL) && h.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HeroDisguise.class) != null){
			dmg = Math.round(dmg*(1f + 0.2f*h.pointsInTalent(Talent.MAGIC_GIRL)));
		}
		if (h.hasTalent(Talent.EXTREME_CASTING)) dmg = Math.round(dmg*(1f + 0.2f*h.pointsInTalent(Talent.EXTREME_CASTING)));
		if (h.hasTalent(Talent.STATIC_LIGHT)){
			dmg += h.pointsInTalent(Talent.STATIC_LIGHT);
			int turns = (int)(dmg*(0.025f + 0.025f*h.pointsInTalent(Talent.STATIC_LIGHT)));
			if (turns > 0) Buff.affect(target, Paralysis.class, turns);
		}
		if (h.hasTalent(Talent.EAT_MIND) && h.buff(Hunger.class) != null) h.buff(Hunger.class).satisfy((0.1f + 0.1f*h.pointsInTalent(Talent.EAT_MIND))*dmg);
		return wandDamageMore(h, wand, target, dmg);
	}

	//---- other places --------------------------------------------------------------------------

	public static void onChasmLand(Hero h){
		if (h.hasTalent(Talent.POTENTIAL_ENERGY)) Buff.affect(h, Haste.class, 1 + 2*h.pointsInTalent(Talent.POTENTIAL_ENERGY));
		if (h.pointsNegative(Talent.MAMBA_OUT) > 0){
			com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Viscosity.DeferedDamage deferred =
					Buff.affect(h, com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Viscosity.DeferedDamage.class);
			deferred.extend(h.HT*0.25f*h.pointsNegative(Talent.MAMBA_OUT));
		}
	}

	//how hard a fall hurts, 0 normal, 1 no cripple, 2 nothing
	public static int fallProtection(Hero h){
		if (h.pointsInTalent(Talent.DROP_RESISTANT) == 2) return 2;
		if (h.pointsInTalent(Talent.DROP_RESISTANT) == 1 || h.hasTalent(Talent.POTENTIAL_ENERGY)) return 1;
		return 0;
	}

	public static void onGrassTrampled(int pos, Char ch){
		Hero h = hero();
		if (h == null || Dungeon.level == null) return;
		if (ch == h && h.pointsNegative(Talent.THORNS_SPRANG) > Random.Int(10)) Buff.affect(ch, Bleeding.class).set(1);
		if (h.pointsNegative(Talent.GRASS_MOB) > Random.Int(10)){
			int cell = freeCellNear(pos);
			if (cell != -1){
				RotLasher m = new RotLasher();
				m.pos = cell;
				GameScene.add(m);
			}
		}
		if (ch instanceof Mob && ch.alignment == Char.Alignment.ENEMY && h.pointsInTalent(Talent.WAKE_SNAKE) > Random.Int(10)){
			int cell = freeCellNear(pos);
			if (cell != -1){
				Snake m = new Snake();
				m.pos = cell;
				GameScene.add(m);
				Buff.affect(m, Corruption.class);
			}
		}
	}

	public static boolean seedRecycled(){
		return p(Talent.SEED_RECYCLING) > Random.Int(5);
	}

	public static void onBurningEnded(Char target){
		if (target instanceof Hero && ((Hero) target).hasTalent(Talent.NIRVANA) && target.HP < target.HT){
			target.heal(Math.max(1, ((Hero) target).lvl/2));
		}
	}

	public static void onBurning(Char target, float left){
		if (target instanceof Hero && ((Hero) target).pointsNegative(Talent.UPDRAFT) > 0 && target.buff(Levitation.class) == null && left > 1){
			Buff.affect(target, Levitation.class, left);
		}
	}

	public static void onLevitationStart(Char target){
		if (target instanceof Hero && ((Hero) target).pointsNegative(Talent.UPDRAFT) > 1 && target.buff(Burning.class) == null){
			Buff.affect(target, Burning.class).reignite(target, Levitation.DURATION);
		}
	}

	public static void onLevitationEnd(Char target){
		if (target instanceof Hero && ((Hero) target).pointsNegative(Talent.HANDON_GROUND) > 0){
			Buff.affect(target, Bleeding.class).set(0.1f*target.HP*((Hero) target).pointsNegative(Talent.HANDON_GROUND));
		}
	}

	public static boolean fireNeverDies(){
		return n(Talent.BONE_FIRE) > 0;
	}

	public static void onLightGained(Char target){
		if (!(target instanceof Hero)) return;
		Hero h = (Hero) target;
		if (h.hasTalent(Talent.HOLY_FAITH)){
			Buff.affect(h, Recharging.class, 3 + 2*h.pointsInTalent(Talent.HOLY_FAITH));
			Buff.affect(h, Bless.class, 3 + 2*h.pointsInTalent(Talent.HOLY_FAITH));
			if (h.pointsInTalent(Talent.HOLY_FAITH) == 3) Buff.affect(h, ArtifactRecharge.class).extend(4);
		}
	}

	public static void onInvisible(Char target){
		Hero h = hero();
		if (h == null || target != h || h.sprite == null || !h.hasTalent(Talent.INVISIBILITY_SHADOWS) || h.buff(InvShaCooldown.class) != null) return;
		int cell = freeCellNear(h.pos);
		if (cell == -1) return;
		MirrorImage mob = new MirrorImage();
		mob.duplicate(h);
		GameScene.add(mob);
		Buff.prolong(mob, StoneOfAggression.Aggression.class, StoneOfAggression.Aggression.DURATION);
		ScrollOfTeleportation.appear(mob, cell);
		Buff.affect(h, InvShaCooldown.class, 50 - 10*h.pointsInTalent(Talent.INVISIBILITY_SHADOWS));
	}

	//Cicada Dance keeps the hero hidden through the attack that made the kill
	public static boolean keepsInvisibility(Char ch){
		Hero h = hero();
		return h != null && ch == h && h.hasTalent(Talent.CICADA_DANCE) && h.buff(EnemyDies.class) != null;
	}

	public static void onScrollThrown(Hero h, int cell){
		Char enemy = Actor.findChar(cell);
		if (enemy != null && enemy != h && h.hasTalent(Talent.WORD_STUN) && h.buff(WordStunCooldown.class) == null){
			Buff.affect(enemy, Paralysis.class, 1 + h.pointsInTalent(Talent.WORD_STUN));
			Buff.affect(h, WordStunCooldown.class, 50);
		}
	}

	public static void onMeleeThrown(Hero h, MeleeWeapon w, int cell){
		Char enemy = Actor.findChar(cell);
		if (enemy == null || enemy == h) return;
		if (h.hasTalent(Talent.STRONG_THROW)) enemy.damage(2*w.tier*h.pointsInTalent(Talent.STRONG_THROW), h);
		if (h.hasTalent(Talent.WEIRD_THROW) && enemy.isAlive()) Buff.affect(enemy, Terror.class, h.pointsInTalent(Talent.WEIRD_THROW));
	}

	public static void onRageRead(Mob mob){
		Hero h = hero();
		if (h == null || !h.hasTalent(Talent.PERSONAL_ATTACK)) return;
		Buff.affect(mob, Vulnerable.class, 5);
		if (h.pointsInTalent(Talent.PERSONAL_ATTACK) == 2) Buff.affect(mob, Weakness.class, 5);
	}

	public static void onIntuitionSuccess(){
		Hero h = hero();
		if (h == null || !h.hasTalent(Talent.FLASH_GENIUS)) return;
		int pts = h.pointsInTalent(Talent.FLASH_GENIUS) + 1;
		int cnt = 0;
		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
			if (mob.alignment != Char.Alignment.ENEMY) continue;
			Buff.affect(mob, Blindness.class, pts);
			Buff.affect(mob, Vertigo.class, pts);
			cnt++;
		}
		if (cnt > 0){
			GameScene.flash(0x80FFFFFF);
			Sample.INSTANCE.play(Assets.Sounds.BLAST);
		}
	}

	public static int bombDamage(int dmg){
		int b = p(Talent.BOMB_MANIAC);
		return b > 0 ? Math.round(dmg*(1f + 0.25f*b)) : dmg;
	}

	public static void onBombHit(Char ch){
		Hero h = hero();
		if (h != null && ch != h && h.hasTalent(Talent.SHOCK_BOMB) && ch.isAlive()) Buff.affect(ch, Paralysis.class, h.pointsInTalent(Talent.SHOCK_BOMB));
	}

	public static int extraDivination(){
		return p(Talent.PREDICTIVE_LOVER);
	}

	//true when a bought item should not cost gold (Third Hand)
	public static boolean freePurchase(){
		Hero h = hero();
		if (h != null && h.pointsInTalent(Talent.THRID_HAND) > Random.Int(10)){
			GLog.p(Messages.get(TransformationTalents.class, "hand_steal"));
			return true;
		}
		return false;
	}

	public static boolean purchaseBecomesStone(){
		return n(Talent.COWBOY)*3 > Random.Int(20);
	}

	public static float viewDistanceFactor(Hero h){
		return 1f - 0.2f*h.pointsNegative(Talent.SHORTSIGHTED);
	}

	public static float hungerRate(Hero h){
		if (TransformationBossTalents.fasting(h)) return 0f;
		return 1f + 0.15f*h.pointsNegative(Talent.EATER);
	}

	//called when hunger hurts the hero, true when Chocolate Coins paid the damage away
	public static boolean onStarveDamage(Hero h){
		int cc = h.pointsInTalent(Talent.CHOCOLATE_COINS);
		if (cc > 0 && Dungeon.gold > 100 - 25*cc){
			Dungeon.gold -= 49 - 12*cc;
			return true;
		}
		if (h.pointsNegative(Talent.MALNUTRITION) == 2){
			Buff.affect(h, Hex.class, 10);
			Buff.affect(h, Weakness.class, 10);
		} else if (h.pointsNegative(Talent.MALNUTRITION) == 1){
			Buff.affect(h, Weakness.class, 10);
		}
		if (h.pointsNegative(Talent.HUNGRY_GHOST) > 0){
			int cell = freeCellNear(h.pos);
			if (cell != -1){
				Wraith.spawnAt(cell, Wraith.class);
				Sample.INSTANCE.play(Assets.Sounds.CURSED);
			}
		}
		return false;
	}

	public static int starveDamage(Hero h, int dmg){
		int u = h.pointsNegative(Talent.UNBEAR_HUNGER);
		return u > 0 ? dmg + Math.round(0.5f*u*dmg) : dmg;
	}

	public static float chaliceFactor(Hero h){
		float f = 1f;
		if (h.pointsInTalent(Talent.STRENGTHEN_CHALICE) == 2) f /= 2f;
		else if (h.pointsInTalent(Talent.STRENGTHEN_CHALICE) == 1) f /= 1.5f;
		if (h.pointsNegative(Talent.WEAKEN_CHALICE) > 0) f *= 1f + 0.5f*h.pointsNegative(Talent.WEAKEN_CHALICE);
		return f;
	}

	public static float chalicePrickFactor(){
		return 1f + 0.5f*n(Talent.WEAKEN_CHALICE);
	}

	public static void onNpcInteract(Mob npc){
		Hero h = hero();
		if (h == null || !h.hasTalent(Talent.DUNGEON_HERO) || npc.buff(NpcGiftGiven.class) != null) return;
		boolean mid = h.pointsInTalent(Talent.DUNGEON_HERO) == 2;
		for (int i = 0; i < 2; i++) dropAtHero(mid ? RingOfWealth.genMidValueConsumable() : RingOfWealth.genLowValueConsumable());
		Buff.affect(npc, NpcGiftGiven.class);
	}

	public static boolean wantAll(int needed){
		return p(Talent.WANT_ALL) >= needed;
	}

	public static void onStatueDie(Mob statue, boolean guardian){
		Hero h = hero();
		if (h == null || !h.hasTalent(Talent.WEALTH_STATUE) || !takeItemSlot(h)) return;
		com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring reward = (com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring) Generator.random(Generator.Category.RING);
		reward.level(h.pointsInTalent(Talent.WEALTH_STATUE) + (guardian ? 0 : 1));
		Dungeon.level.drop(reward, statue.pos).sprite.drop();
	}

	public static boolean ghostGivesBoth(){
		Hero h = hero();
		return h != null && h.hasTalent(Talent.GHOST_GIFT) && h.pointsInTalent(Talent.GHOST_GIFT) + 2 > Random.Int(4);
	}

	public static boolean alchemyAccident(){
		return n(Talent.ALCHEMY_ACCIDENT) > Random.Int(10);
	}

	public static boolean miracleAlchemy(){
		return p(Talent.MIRACLE_ALCHEMY) > Random.Int(5);
	}

	//---- metamorphosis from the catalog -------------------------------------------------------

	//set by the metamorphosis crystal, the next catalog metamorphosis shows every talent of the chosen kind
	public static boolean directed = false;

	public static boolean usesCatalog(Hero h, Talent t){
		return h != null && (directed || h.heroClass == HeroClass.FREEMAN || t.isCatalogTalent());
	}

	public static boolean moreChance(){
		return p(Talent.MORE_CHANCE) > Random.Int(10);
	}

	//the talents a catalog metamorphosis can land on
	public static boolean inPool(Talent t){
		if (!t.isCatalogTalent() || !t.type().isCommon()) return false;
		switch (t){
			case POTENTIAL_1: case POTENTIAL_2: case POTENTIAL_3: case POTENTIAL_4: case POTENTIAL_5:
			case POTENTIAL_6: case POTENTIAL_7: case POTENTIAL_8: case POTENTIAL_9:
				return false;
			default:
				return true;
		}
	}

	public static ArrayList<Talent> metamorphChoices(Hero h, int tier, Talent.TalentType preferred){
		java.util.Set<Talent> have = new java.util.HashSet<>();
		for (java.util.LinkedHashMap<Talent, Integer> t : h.talents) have.addAll(t.keySet());
		ArrayList<Talent> pool = new ArrayList<>();
		boolean strict = preferred == Talent.TalentType.SPELL;
		for (Talent t : Talent.values()){
			if (!inPool(t) || t.tier() != tier || have.contains(t)) continue;
			if (strict && t.type() != preferred) continue;
			pool.add(t);
			if (!strict && t.type() == preferred) for (int i = 0; i < 5; i++) pool.add(t);
		}
		if (pool.isEmpty() && strict) return metamorphChoices(h, tier, Talent.TalentType.OTHER);
		if (directed){
			directed = false;
			ArrayList<Talent> all = new ArrayList<>();
			for (Talent t : pool) if (!all.contains(t) && t.type() == preferred) all.add(t);
			if (!all.isEmpty()) return all;
		}
		int count = 4;
		if (h.pointsInTalent(Talent.MORE_TALENT) > Random.Int(2)) count++;
		count -= h.pointsNegative(Talent.FATE_DECISION);
		ArrayList<Talent> out = new ArrayList<>();
		while (out.size() < count && !pool.isEmpty()){
			Talent t = Random.element(pool);
			while (pool.remove(t));
			out.add(t);
		}
		return out;
	}

	//swaps one talent of the hero for another, the way a scroll of metamorphosis does, so a save keeps it
	public static void replaceTalent(Hero h, Talent replacing, Talent talent){
		for (int i = 0; i < h.talents.size(); i++){
			java.util.LinkedHashMap<Talent, Integer> tier = h.talents.get(i);
			if (!tier.containsKey(replacing)) continue;
			java.util.LinkedHashMap<Talent, Integer> newTier = new java.util.LinkedHashMap<>();
			for (Talent t : tier.keySet()) newTier.put(t == replacing ? talent : t, tier.get(t));
			h.talents.set(i, newTier);
			break;
		}
		if (!h.metamorphedTalents.containsValue(replacing)){
			h.metamorphedTalents.put(replacing, talent);
		} else {
			for (Talent t2 : new ArrayList<>(h.metamorphedTalents.keySet())){
				if (h.metamorphedTalents.get(t2) == replacing){
					if (t2 == talent) h.metamorphedTalents.remove(t2);
					else h.metamorphedTalents.put(t2, talent);
				}
			}
		}
	}

	//the transformation trap, one common talent of tier 1 to 3 becomes another of the same tier
	public static Talent[] randomTransformation(Hero h){
		ArrayList<Talent> from = new ArrayList<>();
		for (int i = 0; i < Math.min(3, h.talents.size()); i++){
			for (Talent t : h.talents.get(i).keySet()){
				if (!TransformationBossTalents.isSlot(t) && t.type().isCommon() && t.type() != Talent.TalentType.SUBCLASS) from.add(t);
			}
		}
		if (from.isEmpty()) return null;
		Talent old = Random.element(from);
		int tier = 1;
		for (int i = 0; i < h.talents.size(); i++) if (h.talents.get(i).containsKey(old)) tier = i + 1;
		ArrayList<Talent> to = metamorphChoices(h, tier, Talent.TalentType.OTHER);
		if (to.isEmpty()) return null;
		Talent neu = to.get(0);
		replaceTalent(h, old, neu);
		if (h.hasTalent(neu)) Talent.onTalentUpgraded(h, neu);
		return new Talent[]{old, neu};
	}

	public static final Talent.TalentType[] KINDS = {Talent.TalentType.ATTACK, Talent.TalentType.MAGIC, Talent.TalentType.EFFECT,
			Talent.TalentType.RESOURCE, Talent.TalentType.SPELL, Talent.TalentType.ASSIST, Talent.TalentType.OTHER};

	public static void chooseKind(Talent replacing, int tier){
		String[] names = new String[KINDS.length];
		for (int i = 0; i < KINDS.length; i++){
			names[i] = Messages.titleCase(Messages.get(ScrollOfMetamorphosis.class, KINDS[i].name().toLowerCase(java.util.Locale.ENGLISH)));
		}
		GameScene.show(new com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions(
				new com.shatteredpixel.shatteredpixeldungeon.ui.TalentIcon(replacing),
				Messages.get(ScrollOfMetamorphosis.class, "type_name"),
				Messages.get(ScrollOfMetamorphosis.class, "type_desc"),
				names){
			@Override
			protected void onSelect(int index){
				GameScene.show(new ScrollOfMetamorphosis.WndMetamorphReplace(replacing, tier, KINDS[index]));
			}
			@Override
			protected boolean hasInfo(int index){ return true; }
			@Override
			protected void onInfo(int index){
				GameScene.show(new com.shatteredpixel.shatteredpixeldungeon.windows.WndTalentCatalog(tier, KINDS[index]));
			}
			@Override
			public void onBackPressed(){
				//a kind must be chosen, the scroll is already spent
			}
		});
	}

	//---- the extra subclass talents, taken through a metamorphosis prism ------------------------

	public static class SkilledParry extends FlavourBuff {}
	public static class SkilledParryCooldown extends TimedCooldown {}
	public static class ArcaneConfluence extends FlavourBuff {
		public boolean zapped = false;
		@Override
		public void detach(){
			super.detach();
			if (!zapped && target instanceof Hero) restoreStaffCharge((Hero) target);
		}
	}

	//the extra talents each subclass can swap in, the subclass's own three come first
	public static Talent[] subclassExtras(HeroSubClass sub){
		switch (sub){
			case BERSERKER: return new Talent[]{Talent.CEASELESS_RAGE, Talent.MIRRORED_REVENGE, Talent.BLOODTHIRSTY_BERSERK};
			case GLADIATOR: return new Talent[]{Talent.COMBO_FOCUS, Talent.RELENTLESS_COMBAT, Talent.COMBO_MASTERY};
			case BATTLEMAGE: return new Talent[]{Talent.LONG_ARM, Talent.ARCANE_CONFLUENCE, Talent.FOCUSED_CASTING};
			case WARLOCK: return new Talent[]{Talent.FINE_TASTING, Talent.BONE_DEEP, Talent.MIND_IMPRISONMENT};
			case ASSASSIN: return new Talent[]{Talent.UNEXPECTED_STRIKE, Talent.CLOSING_STAGE, Talent.PERFECT_FINALE};
			case FREERUNNER: return new Talent[]{Talent.FREERUNNER_AFTERIMAGE, Talent.MOMENTUM_RESERVE, Talent.WARMUP_PREPARATION};
			case CHAMPION: return new Talent[]{Talent.WEAPON_ABILITY_MASTER, Talent.SKILLED_PARRY, Talent.ALTERNATING_WEAPONS};
			case MONK: return new Talent[]{Talent.INNER_PEACE, Talent.NATURAL_WAY, Talent.YIN_YANG_BALANCE};
			default: return new Talent[0];
		}
	}

	//the subclass talents a prism can swap between, the native three and the extras
	public static ArrayList<Talent> subclassGroup(Hero h){
		ArrayList<Talent> group = new ArrayList<>();
		if (h.subClass == null || subclassExtras(h.subClass).length == 0) return group;
		ArrayList<java.util.LinkedHashMap<Talent, Integer>> lists = new ArrayList<>();
		Talent.initSubclassTalents(h.subClass, lists);
		for (java.util.LinkedHashMap<Talent, Integer> m : lists) group.addAll(m.keySet());
		java.util.Collections.addAll(group, subclassExtras(h.subClass));
		return group;
	}

	public static boolean skilledParry(Hero h){
		return h.buff(SkilledParry.class) != null;
	}

	public static void onWeaponAbility(Hero h){
		if (h.hasTalent(Talent.SKILLED_PARRY) && h.buff(SkilledParryCooldown.class) == null){
			Buff.prolong(h, SkilledParry.class, 1 + h.pointsInTalent(Talent.SKILLED_PARRY));
			Buff.affect(h, SkilledParryCooldown.class, 30);
		}
	}

	public static int championChargeBonus(Hero h){
		return h.pointsInTalent(Talent.WEAPON_ABILITY_MASTER);
	}

	public static float championRechargeBonus(Hero h){
		return 1.5f + h.pointsInTalent(Talent.WEAPON_ABILITY_MASTER)/6f;
	}

	public static float attackDelayFactor(Hero h){
		com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Combo c = h.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Combo.class);
		if (h.hasTalent(Talent.RELENTLESS_COMBAT) && c != null && c.getComboCount() >= 8){
			return 1f / (1f + 0.5f*h.pointsInTalent(Talent.RELENTLESS_COMBAT)) / rageSpeed(h);
		}
		return 1f / rageSpeed(h);
	}

	private static int comboStep(Hero h){ return 5 - h.pointsInTalent(Talent.COMBO_FOCUS); }

	public static int comboFocusDamage(Hero h){
		com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Combo c = h.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Combo.class);
		if (!h.hasTalent(Talent.COMBO_FOCUS) || c == null) return 0;
		return Math.min(20, c.getComboCount() / comboStep(h));
	}

	public static float comboFocusAccuracy(Hero h){
		com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Combo c = h.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Combo.class);
		if (!h.hasTalent(Talent.COMBO_FOCUS) || c == null) return 1f;
		int pts = h.pointsInTalent(Talent.COMBO_FOCUS);
		float per = 0.025f + 0.025f*pts;
		return 1f + Math.min(per*20, per*(c.getComboCount() / comboStep(h)));
	}

	public static float innerPeace(Hero h){
		if (!h.hasTalent(Talent.INNER_PEACE)) return 1f;
		com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MonkEnergy e = h.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MonkEnergy.class);
		if (e == null) return 1f;
		return 1f + (0.125f + 0.025f*h.pointsInTalent(Talent.INNER_PEACE))*(int) e.energy;
	}

	public static int longArm(Char owner, Item weapon){
		if (!(owner instanceof Hero) || !(weapon instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff)) return 0;
		Hero h = (Hero) owner;
		if (!h.hasTalent(Talent.LONG_ARM)) return 0;
		int extra = h.STR() - ((com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff) weapon).STRReq();
		return Math.max(0, extra / (9 - h.pointsInTalent(Talent.LONG_ARM)));
	}

	public static void onStaffMeleeHit(Hero h){
		if (h.hasTalent(Talent.ARCANE_CONFLUENCE)){
			ArcaneConfluence a = h.buff(ArcaneConfluence.class);
			if (a != null){ a.zapped = true; a.detach(); }
			Buff.affect(h, ArcaneConfluence.class, 10f);
		}
	}

	public static void onAnyZap(Hero h){
		ArcaneConfluence a = h.buff(ArcaneConfluence.class);
		if (a != null){ a.zapped = true; a.detach(); }
	}

	private static void restoreStaffCharge(Hero h){
		com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff staff = h.belongings.getItem(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff.class);
		if (staff == null || !h.hasTalent(Talent.ARCANE_CONFLUENCE)) return;
		int pts = h.pointsInTalent(Talent.ARCANE_CONFLUENCE);
		float charge = 0.5f + 0.5f*pts;
		Wand w = staff.wand();
		if (w == null) return;
		float room = w.maxCharges - w.curCharges - w.partialCharge;
		float give = Math.min(room, charge);
		w.partialCharge += give;
		while (w.partialCharge >= 1){ w.curCharges++; w.partialCharge--; }
		float overflow = (charge - give) * (0.75f + 0.25f*pts);
		if (overflow > 0){
			for (Wand.Charger c : h.buffs(Wand.Charger.class)) c.gainCharge(overflow);
		}
		staff.updateQuickslot();
	}

	public static void onSoulMarkedHit(Hero h, Char enemy){
		if (h.hasTalent(Talent.FINE_TASTING) && enemy.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SoulMark.class) != null){
			h.heal(h.pointsInTalent(Talent.FINE_TASTING));
		}
	}

	public static int landSwimming(){ return n(Talent.LAND_SWIMMING); }
	public static int fishingTime(){ return p(Talent.FISHING_TIME); }

	//the pool room, Land Swimming swaps its fish for land piranhas and Fishing Time +2 adds one
	public static Mob poolFish(){
		if (landSwimming() > 0){
			return Random.Int(50) == 0 ? new com.shatteredpixel.shatteredpixeldungeon.actors.mobs.PhantomLandPiranha() : new com.shatteredpixel.shatteredpixeldungeon.actors.mobs.LandPiranha();
		}
		return com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Piranha.random();
	}
	//==== the second catalog run, talents that reach into single items and systems ===============

	private static boolean onScreen(){
		return com.watabou.noosa.Game.instance != null && com.watabou.noosa.Game.scene() instanceof GameScene;
	}

	private static boolean staffWand(Hero h, Wand w){
		KindOfWeapon kw = h.belongings.weapon();
		return w != null && kw instanceof MagesStaff && ((MagesStaff) kw).wand() == w;
	}

	//items and action buffs handed out when one of these talents is taken
	private static void upgradedMore(Hero hero, Talent talent, int pts){
		Item give = null;
		switch (talent){
			case WULEI_ZHENGFA: if (pts == 1) give = new WandOfLightning().identify(); break;
			case ERODING_SOUL: if (pts == 1) give = new WandOfCorruption().identify(); break;
			case LIGHT_SHOCK: if (pts == 1) give = new WandOfPrismaticLight().identify(); break;
			case SWIFT_CHURCH: if (pts == 1) give = new WandOfRegrowth().identify(); break;
			case TRAP_MASTER: give = new ReclaimTrap(); break;
			default: break;
		}
		if (give != null && takeItemSlot(hero)) dropAtHero(give);
		if (talent == Talent.SMOKE_MASK && pts > 0) Buff.affect(hero, SmokeMask.class);
		if (talent == Talent.NATURAL_CHILD && pts > 0) Buff.affect(hero, NaturalBreath.class);
	}

	//---- Berserker ----------------------------------------------------------------------------

	//Ceaseless Rage, rage at or below the threshold does not fade
	public static boolean ceaselessRage(Char target, float power){
		if (!(target instanceof Hero)) return false;
		int pts = ((Hero) target).pointsInTalent(Talent.CEASELESS_RAGE);
		return pts > 0 && power <= 0.2f + 0.1f*pts;
	}

	private static float rageSpeed(Hero h){
		if (!h.hasTalent(Talent.CEASELESS_RAGE)) return 1f;
		Berserk b = h.buff(Berserk.class);
		return b == null ? 1f : 1f + Math.min(1f, b.rage());
	}

	private static boolean berserking(Hero h){
		Berserk b = h.buff(Berserk.class);
		return b != null && b.raging();
	}

	//---- Gladiator ----------------------------------------------------------------------------

	public static boolean comboMastery(Char target){
		return target instanceof Hero && ((Hero) target).hasTalent(Talent.COMBO_MASTERY);
	}

	public static int comboDiscount(Char target){
		if (!(target instanceof Hero)) return 0;
		int pts = ((Hero) target).pointsInTalent(Talent.COMBO_MASTERY);
		return pts >= 3 ? 2 : (pts == 2 ? 1 : 0);
	}

	//the combo left after a move, a move that resets the combo keeps the rest with Combo Mastery
	public static int comboAfterMove(Char target, int count, int req, boolean resets){
		if (!comboMastery(target)) return resets ? 0 : count;
		return Math.max(0, count - Math.max(1, req - comboDiscount(target)));
	}

	//---- Battlemage ---------------------------------------------------------------------------

	public static class FocusedCasting extends FlavourBuff {
		{ type = buffType.POSITIVE; }
		public int stacks = 0;
		@Override public int icon(){ return BuffIndicator.RECHARGING; }
		@Override public void tintIcon(Image icon){ icon.hardlight(0.4f, 0.6f, 1f); }
		@Override public String iconTextDisplay(){ return "+" + stacks; }
		@Override public String desc(){ return Messages.get(this, "desc", stacks, dispTurns()); }
		@Override public void storeInBundle(com.watabou.utils.Bundle b){ super.storeInBundle(b); b.put("stacks", stacks); }
		@Override public void restoreFromBundle(com.watabou.utils.Bundle b){ super.restoreFromBundle(b); stacks = b.getInt("stacks"); }
	}

	private static void onStaffZap(Hero h, Wand w){
		if (!h.hasTalent(Talent.FOCUSED_CASTING) || !staffWand(h, w)) return;
		boolean had = h.buff(FocusedCasting.class) != null;
		FocusedCasting f = Buff.prolong(h, FocusedCasting.class, 5f);
		if (had) f.stacks = Math.min(1 + h.pointsInTalent(Talent.FOCUSED_CASTING), f.stacks + 1);
	}

	private static int focusedLevel(Hero h, Item item){
		if (!(item instanceof Wand)) return 0;
		FocusedCasting f = h.buff(FocusedCasting.class);
		return f != null && staffWand(h, (Wand) item) ? f.stacks : 0;
	}

	//---- Warlock ------------------------------------------------------------------------------

	private static void boneDeep(Hero h, Char target){
		int pts = h.pointsInTalent(Talent.BONE_DEEP);
		if (pts == 0 || target == h || target.alignment == h.alignment) return;
		int extra = pts == 1 ? 5 : (pts == 2 ? 8 : 10);
		if (target.buff(SoulMark.class) != null){
			Buff.prolong(target, SoulMark.class, 10 + extra);
		} else if (Random.Float() < (pts == 1 ? 0.08f : (pts == 2 ? 0.17f : 0.25f))){
			SoulMark.prolong(target, SoulMark.class, 10 + extra);
		}
	}

	private static int mindImprisonment(Mob mob, Char enemy, int dmg){
		Hero h = hero();
		if (h == null || enemy != h || !h.hasTalent(Talent.MIND_IMPRISONMENT)) return dmg;
		if (mob.buff(SoulMark.class) == null || Char.hasProp(mob, Char.Property.BOSS)) return dmg;
		if (Dungeon.level != null && !Dungeon.level.adjacent(mob.pos, enemy.pos)) return 0;
		return Math.round(dmg*(0.95f - 0.05f*h.pointsInTalent(Talent.MIND_IMPRISONMENT)));
	}

	//---- Assassin -----------------------------------------------------------------------------

	//Unexpected Strike, extra damage for each level of preparation (0 to 3)
	public static float unexpectedStrike(Char attacker, int level){
		if (!(attacker instanceof Hero)) return 0;
		int pts = ((Hero) attacker).pointsInTalent(Talent.UNEXPECTED_STRIKE);
		return pts * (level < 2 ? 0.05f : 0.10f);
	}

	//Closing Stage, haste when the preparation ends (level 1 to 4)
	public static void onPreparationEnd(Char target, int level){
		if (!(target instanceof Hero) || !target.isAlive()) return;
		int pts = ((Hero) target).pointsInTalent(Talent.CLOSING_STAGE);
		if (pts == 0) return;
		int[] turns = {1, 2, 4, 6};
		Buff.affect(target, Haste.class, turns[Math.max(0, Math.min(3, level - 1))] + pts - 1);
	}

	private static void perfectFinale(Hero h){
		int pts = h.pointsInTalent(Talent.PERFECT_FINALE);
		Preparation p = h.buff(Preparation.class);
		if (pts == 0 || p == null) return;
		float[] base = {1f, 1.5f, 3f, 4.5f};
		float turns = base[Math.max(0, Math.min(3, p.attackLevel() - 1))] + 0.5f*(pts - 1);
		for (Buff b : h.buffs().toArray(new Buff[0])){
			if (b instanceof com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact.ArtifactBuff){
				((com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact.ArtifactBuff) b).charge(h, turns);
			}
		}
	}

	//---- Freerunner ---------------------------------------------------------------------------

	public static void onFreerunEnded(Char target){
		if (!(target instanceof Hero)) return;
		Hero h = (Hero) target;
		int pts = h.pointsInTalent(Talent.FREERUNNER_AFTERIMAGE);
		if (pts > 0) Buff.affect(h, GreaterHaste.class).set(2 + Math.round(h.lvl*(0.05f + 0.05f*pts)));
	}

	public static void onFreerunRecovered(Momentum m){
		if (!(m.target instanceof Hero)) return;
		Hero h = (Hero) m.target;
		int pts = h.pointsInTalent(Talent.WARMUP_PREPARATION);
		if (pts == 0) return;
		for (int i = 0; i < 4; i++) m.gainStack();
		if (pts >= 2){
			CloakOfShadows cloak = h.belongings.getItem(CloakOfShadows.class);
			if (cloak != null) cloak.charge(h, 8f);
		}
		if (pts >= 3) Buff.affect(h, Stamina.class, 8f);
	}

	private static float momentumReserve(Hero h){
		int pts = h.pointsInTalent(Talent.MOMENTUM_RESERVE);
		Momentum m = h.buff(Momentum.class);
		return pts > 0 && m != null && m.recoveryLeft() > 0 ? 1f + 0.5f*pts : 1f;
	}

	//---- Champion -----------------------------------------------------------------------------

	public static class AlternatingWeapons extends FlavourBuff {
		{ type = buffType.POSITIVE; }
		public float bonus = 0;
		@Override public int icon(){ return BuffIndicator.WEAPON; }
		@Override public String iconTextDisplay(){ return Math.round(bonus*100) + "%"; }
		@Override public String desc(){ return Messages.get(this, "desc", Math.round(bonus*100), dispTurns()); }
		@Override public void storeInBundle(com.watabou.utils.Bundle b){ super.storeInBundle(b); b.put("bonus", bonus); }
		@Override public void restoreFromBundle(com.watabou.utils.Bundle b){ super.restoreFromBundle(b); bonus = b.getFloat("bonus"); }
	}

	private static KindOfWeapon lastAltWeapon = null;
	private static float lastAltTime = -10f;

	private static int alternatingWeapons(Hero h, int dmg){
		int pts = h.pointsInTalent(Talent.ALTERNATING_WEAPONS);
		KindOfWeapon w = h.belongings.attackingWeapon();
		if (pts > 0 && w != null && !(w instanceof MissileWeapon)){
			if (lastAltWeapon != null && lastAltWeapon != w && Actor.now() - lastAltTime <= 3f){
				AlternatingWeapons a = Buff.prolong(h, AlternatingWeapons.class, 5f);
				a.bonus = Math.min(0.1f + 0.2f*pts, a.bonus + 0.1f*pts);
			}
			lastAltWeapon = w;
			lastAltTime = Actor.now();
		}
		AlternatingWeapons a = h.buff(AlternatingWeapons.class);
		return a == null ? dmg : Math.round(dmg*(1f + a.bonus));
	}

	//---- Monk ---------------------------------------------------------------------------------

	//Natural Way, more energy per hero level, half for enemies worth half energy
	public static float naturalWay(Char target, float energyGain){
		if (!(target instanceof Hero)) return 0;
		Hero h = (Hero) target;
		int pts = h.pointsInTalent(Talent.NATURAL_WAY);
		if (pts == 0) return 0;
		float bonus = Math.min(0.5f + 0.5f*pts, h.lvl*(0.025f + 0.025f*pts));
		return energyGain < 1 ? bonus/2f : bonus;
	}

	//Yin Yang Balance, energy pays for the weapon charges a Monk lacks
	public static boolean yinYangCovers(Hero h, float have, float need){
		int pts = h.pointsInTalent(Talent.YIN_YANG_BALANCE);
		MonkEnergy e = h.buff(MonkEnergy.class);
		if (pts == 0 || e == null) return false;
		int missing = (int) Math.ceil(need - have);
		return (int) e.energy >= missing*(4 - pts);
	}

	public static void yinYangPay(Hero h, MeleeWeapon.Charger c){
		int pts = h.pointsInTalent(Talent.YIN_YANG_BALANCE);
		MonkEnergy e = h.buff(MonkEnergy.class);
		if (pts == 0 || e == null || c.partialCharge >= 0) return;
		int missing = (int) Math.ceil(-c.partialCharge);
		e.energy = Math.max(0, e.energy - missing*(4 - pts));
		c.partialCharge += missing;
		BuffIndicator.refreshHero();
	}

	//---- boss talents -------------------------------------------------------------------------

	public static class SmokeCooldown extends TimedCooldown {}
	public static class NaturalBreathCooldown extends TimedCooldown {}
	public static class CollapseCounter extends CounterBuff { { revivePersists = true; } }

	//an action button that comes back when its cooldown is over and goes when its talent is gone
	public static abstract class TalentAction extends Buff implements ActionIndicator.Action {
		{ type = buffType.POSITIVE; revivePersists = true; actPriority = HERO_PRIO + 1; }
		protected abstract Talent talent();
		protected abstract Class<? extends FlavourBuff> cooldownClass();
		protected abstract void use(Hero h, int pts);
		@Override
		public boolean act(){
			if (!(target instanceof Hero) || !((Hero) target).hasTalent(talent())){
				ActionIndicator.clearAction(this);
				detach();
				return true;
			}
			if (target.buff(cooldownClass()) == null && ActionIndicator.action == null) ActionIndicator.setAction(this);
			spend(TICK);
			return true;
		}
		@Override public int indicatorColor(){ return 0x3C7A3C; }
		@Override
		public void doAction(){
			Hero h = (Hero) target;
			if (h.buff(cooldownClass()) != null || !h.hasTalent(talent())) return;
			use(h, h.pointsInTalent(talent()));
			Buff.affect(h, cooldownClass(), 25f);
			ActionIndicator.clearAction(this);
			h.spendAndNext(1f);
		}
	}

	//Smoke Mask, a cloud of smoke around the hero
	public static class SmokeMask extends TalentAction {
		protected Talent talent(){ return Talent.SMOKE_MASK; }
		protected Class<? extends FlavourBuff> cooldownClass(){ return SmokeCooldown.class; }
		@Override public String actionName(){ return Messages.get(TransformationTalents.class, "smoke_action"); }
		@Override public int actionIcon(){ return com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon.SMOKEMASK; }
		@Override public int indicatorColor(){ return 0x666666; }
		protected void use(Hero h, int pts){
			GameScene.add(Blob.seed(h.pos, 50 + 50*pts, SmokeScreen.class));
			if (onScreen()) Sample.INSTANCE.play(Assets.Sounds.GAS);
		}
	}

	//Natural Child, high grass around the hero, barkskin and roots for enemies close by
	public static class NaturalBreath extends TalentAction {
		protected Talent talent(){ return Talent.NATURAL_CHILD; }
		protected Class<? extends FlavourBuff> cooldownClass(){ return NaturalBreathCooldown.class; }
		@Override public String actionName(){ return Messages.get(TransformationTalents.class, "natural_action"); }
		@Override public int actionIcon(){ return com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon.NATURAL_CHILD; }
		protected void use(Hero h, int pts){
			for (int i : PathFinder.NEIGHBOURS9){
				int c = h.pos + i;
				if (c < 0 || c >= Dungeon.level.length()) continue;
				int t = Dungeon.level.map[c];
				if (t == Terrain.EMPTY || t == Terrain.EMPTY_DECO || t == Terrain.EMBERS || t == Terrain.GRASS){
					com.shatteredpixel.shatteredpixeldungeon.levels.Level.set(c, Terrain.HIGH_GRASS);
					if (onScreen()) GameScene.updateMap(c);
				}
				Char ch = Actor.findChar(c);
				if (pts >= 2 && ch != null && ch.alignment == Char.Alignment.ENEMY) Buff.prolong(ch, Roots.class, 2f);
			}
			Barkskin.conditionallyAppend(h, 10 + 5*pts, 1);
			if (onScreen()) Sample.INSTANCE.play(Assets.Sounds.PLANT);
		}
	}

	//Hunting Technique, thrown weapons share some of the melee weapon's damage and its enchantment
	private static int huntingTechnique(Hero h, Char enemy, int dmg){
		int pts = h.pointsInTalent(Talent.HUNTING_TECHNIQUE);
		if (pts == 0 || !(h.belongings.attackingWeapon() instanceof MissileWeapon)) return dmg;
		KindOfWeapon melee = h.belongings.weapon();
		if (melee == null || melee instanceof MissileWeapon) return dmg;
		dmg += Math.round(0.25f*melee.damageRoll(h));
		if (pts >= 2 && melee instanceof Weapon && ((Weapon) melee).enchantment != null){
			dmg = ((Weapon) melee).enchantment.proc((Weapon) melee, h, enemy, dmg);
		}
		return dmg;
	}

	//Directional Collapse, every few zaps rocks fall around the target
	private static void directionalCollapse(Hero h, int target){
		int pts = h.pointsInTalent(Talent.DIRECTIONAL_COLLAPSE);
		if (pts == 0) return;
		Buff.count(h, CollapseCounter.class, 1);
		CollapseCounter cc = h.buff(CollapseCounter.class);
		if (cc == null || cc.count() < 5 - pts) return;
		cc.detach();
		for (int i : PathFinder.NEIGHBOURS9){
			int c = target + i;
			if (c < 0 || c >= Dungeon.level.length() || Dungeon.level.solid[c]) continue;
			Char ch = Actor.findChar(c);
			if (ch != null && ch != h && ch.alignment != Char.Alignment.ALLY){
				ch.damage(Random.NormalIntRange(10, 20), Talent.DIRECTIONAL_COLLAPSE);
				if (ch.isAlive()) Buff.prolong(ch, Paralysis.class, 3f);
			}
			if (onScreen() && Dungeon.level.heroFOV[c]) CellEmitter.get(c).start(Speck.factory(Speck.ROCK), 0.07f, 6);
		}
		if (onScreen()) Sample.INSTANCE.play(Assets.Sounds.ROCKS);
	}

	//Falcon Eye, nothing blocks sight close to the hero
	public static void falconEye(com.shatteredpixel.shatteredpixeldungeon.levels.Level l, Char c, boolean[] fov, float viewDist){
		if (!(c instanceof Hero) || l == null || c.pos < 0) return;
		int pts = ((Hero) c).pointsInTalent(Talent.FALCON_EYE);
		if (pts == 0) return;
		int r = (int) Math.floor(viewDist*(0.25f + 0.25f*pts));
		int w = l.width(), cx = c.pos % w, cy = c.pos / w;
		for (int y = Math.max(0, cy - r); y <= Math.min(l.height() - 1, cy + r); y++){
			for (int x = Math.max(0, cx - r); x <= Math.min(w - 1, cx + r); x++){
				if ((x - cx)*(x - cx) + (y - cy)*(y - cy) <= r*r) fov[x + y*w] = true;
			}
		}
	}

	//Energy Conversion, artifacts and wands charge with the hero's movement speed
	public static float energyConversion(Char target){
		if (!(target instanceof Hero)) return 1f;
		int pts = ((Hero) target).pointsInTalent(Talent.ENERGY_CONVERSION);
		if (pts == 0) return 1f;
		return Math.max(1f, ((Hero) target).speed()*0.5f*pts);
	}

	//---- wand talents -------------------------------------------------------------------------

	public static boolean wandChargeSaved(Wand wand){
		Hero h = hero();
		if (h != null && wand != null){
			if (h.hasTalent(Talent.MARKSMAN) && !(wand instanceof WandOfMagicMissile)
					&& h.buff(WandOfMagicMissile.MagicCharge.class) != null) return true;
			FocusedCasting f = h.buff(FocusedCasting.class);
			if (f != null && f.stacks > 0 && staffWand(h, wand) && Random.Int(2) == 0) return true;
		}
		return wandChargeSaved();
	}

	//charges a cast of this wand costs less, Devil Flame
	public static int chargeDiscount(Wand wand, int cost){
		Hero h = hero();
		if (h == null || !(wand instanceof WandOfFireblast)) return 0;
		int pts = h.pointsInTalent(Talent.DEVIL_FLAME);
		if (pts >= 2 && cost > 1) return 1;
		if (pts >= 1 && cost > 2) return 1;
		return 0;
	}

	private static int wandDamageMore(Hero h, Wand wand, Char target, int dmg){
		if (wand instanceof WandOfMagicMissile && h.pointsInTalent(Talent.MARKSMAN) >= 2) dmg += 3;
		if (wand instanceof WandOfDisintegration && h.pointsInTalent(Talent.ABYSSAL_GAZE) >= 2) dmg = Math.round(dmg*1.2f);
		if (wand instanceof WandOfFireblast && h.pointsInTalent(Talent.DEVIL_FLAME) >= 3) dmg = Math.round(dmg*1.2f);
		if (wand instanceof WandOfLightning && h.pointsInTalent(Talent.WULEI_ZHENGFA) >= 2) dmg = Math.round(dmg*1.3f);
		if (wand instanceof WandOfPrismaticLight && h.hasTalent(Talent.LIGHT_SHOCK) && target.buff(Blindness.class) != null){
			if (target.buff(Cripple.class) == null) Buff.affect(target, Cripple.class, Math.max(1, wand.buffedLvl()/2));
			if (h.pointsInTalent(Talent.LIGHT_SHOCK) >= 2) dmg = Math.round(dmg*1.3f);
		}
		if (wand instanceof WandOfFrost && h.hasTalent(Talent.ICE_HELL)){
			Chill chill = target.buff(Chill.class);
			if (chill != null) dmg = (int) Math.round(dmg / Math.pow(0.9333f, Math.min(10, chill.cooldown())));
		}
		return dmg;
	}

	private static void wandZapMore(Hero h, Wand wand, int target){
		directionalCollapse(h, target);
		onStaffZap(h, wand);
		Char ch = Actor.findChar(target);
		if (wand instanceof WandOfFrost && h.pointsInTalent(Talent.ICE_HELL) >= 2 && ch != null && ch != h && ch.buff(Frost.class) != null){
			ch.damage(Math.max(0, wand.buffedLvl())*4, wand);
		}
		if (wand instanceof WandOfFrost && h.pointsInTalent(Talent.ICE_HELL) >= 3 && Random.Int(2) == 0){
			for (int i : PathFinder.NEIGHBOURS9){
				int c = target + i;
				if (c >= 0 && c < Dungeon.level.length() && !Dungeon.level.solid[c]) GameScene.add(Blob.seed(c, 10, Freezing.class));
			}
		}
		if (wand instanceof WandOfLightning && h.pointsInTalent(Talent.LIGHT_CROP) >= 2) lightCrop(target, true);
		if (wand instanceof WandOfCorruption && h.hasTalent(Talent.ERODING_SOUL) && ch != null && ch != h
				&& ch.alignment == Char.Alignment.ENEMY && !Char.hasProp(ch, Char.Property.UNDEAD)){
			Buff.affect(ch, ErodingSoul.class);
			if (h.pointsInTalent(Talent.ERODING_SOUL) >= 2 && Random.Int(2) == 0){
				switch (Random.Int(4)){
					case 0: Buff.affect(ch, Weakness.class, 10f); break;
					case 1: Buff.affect(ch, Vulnerable.class, 10f); break;
					case 2: Buff.affect(ch, Cripple.class, 10f); break;
					default: Buff.affect(ch, Blindness.class, 10f); break;
				}
			}
		}
	}

	//Wulei Zhengfa, the wand of lightning heals the hero instead of hurting him
	public static boolean lightningHeals(Char ch, int dmg){
		Hero h = hero();
		if (h == null || ch != h || !h.hasTalent(Talent.WULEI_ZHENGFA)) return false;
		int heal = Math.max(1, Math.round(dmg*0.2f));
		h.HP = Math.min(h.HT, h.HP + heal);
		if (h.sprite != null) h.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(heal), FloatingText.HEALING);
		return true;
	}

	//Abyssal Gaze, the extra power from walls and creatures passed counts twice
	public static int abyssalGaze(int bonus){
		return p(Talent.ABYSSAL_GAZE) > 0 ? bonus : 0;
	}

	//Disability Poison, a stronger gas from the wand of corrosion
	public static int corrosionVolume(int vol){
		return p(Talent.DISABLIITY_POSION) > 0 ? Math.round(vol*1.3f) : vol;
	}

	public static int corrosionStrength(){
		return p(Talent.DISABLIITY_POSION) > 0 ? 2 : 0;
	}

	public static void onCorrosiveGas(Char ch){
		int pts = p(Talent.DISABLIITY_POSION);
		if (pts < 2 || ch instanceof Hero) return;
		Buff.affect(ch, Cripple.class, 1f);
		if (pts >= 3){
			Buff.affect(ch, Weakness.class, 1f);
			Buff.affect(ch, Daze.class, 1f);
		}
	}

	//Light Crop, electricity grows grass
	public static void lightCrop(int cell){
		lightCrop(cell, p(Talent.LIGHT_CROP) > 0);
	}

	private static void lightCrop(int cell, boolean on){
		if (!on || Dungeon.level == null || cell < 0 || cell >= Dungeon.level.length()) return;
		int c = Dungeon.level.map[cell];
		int to = -1;
		if (c == Terrain.GRASS) to = Terrain.HIGH_GRASS;
		else if (c == Terrain.EMPTY || c == Terrain.EMBERS || c == Terrain.EMPTY_DECO) to = Terrain.GRASS;
		if (to < 0) return;
		com.shatteredpixel.shatteredpixeldungeon.levels.Level.set(cell, to);
		if (onScreen()){
			GameScene.updateMap(cell);
			CellEmitter.get(cell).burst(com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle.LEVEL_SPECIFIC, 2);
		}
	}

	//Eroding Soul, a marked enemy may rise as a corrupted wraith
	public static class ErodingSoul extends Buff {
		{ type = buffType.NEGATIVE; announced = true; }
		@Override public int icon(){ return BuffIndicator.CORRUPT; }
		@Override public void tintIcon(Image icon){ icon.hardlight(0.6f, 0.2f, 0.8f); }
	}

	private static void erodingSoulDeath(Mob mob){
		if (mob.buff(ErodingSoul.class) == null || mob instanceof Wraith || Dungeon.level == null) return;
		int debuffs = 0;
		for (Buff b : mob.buffs()) if (b.type == Buff.buffType.NEGATIVE && !(b instanceof ErodingSoul)) debuffs++;
		boolean sure = mob.buff(Corruption.class) != null || mob.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom.class) != null;
		if (!sure && Random.Float() >= 0.2f + 0.05f*debuffs) return;
		Wraith w = Wraith.spawnAt(mob.pos);
		if (w != null){
			Buff.affect(w, Corruption.class);
			if (onScreen() && Dungeon.level.heroFOV[mob.pos]){
				CellEmitter.get(mob.pos).burst(ShadowParticle.CURSE, 6);
				Sample.INSTANCE.play(Assets.Sounds.CURSED);
			}
		}
	}

	//---- artifact talents ---------------------------------------------------------------------

	//the 20 percent faster charging of Time Sand, Strength Cloak, Strength Armband and Strength Book
	public static float artifactBoost(Item artifact){
		if (artifact instanceof TimekeepersHourglass && p(Talent.TIME_SAND) >= 3) return 1.2f;
		if (artifact instanceof CloakOfShadows && p(Talent.STRENGTH_CLOAK) >= 3) return 1.2f;
		if (artifact instanceof MasterThievesArmband && p(Talent.STRENGTH_ARMBAND) >= 1) return 1.2f;
		if (artifact instanceof UnstableSpellbook && p(Talent.STRENGTH_BOOK) >= 2) return 1.2f;
		return 1f;
	}

	//Time Sand, stasis skips twice the turns and a time freeze lasts a turn longer per charge
	public static int timeSandStasis(){
		return p(Talent.TIME_SAND) > 0 ? 2 : 1;
	}

	public static float timeFreezeTurns(){
		return p(Talent.TIME_SAND) >= 2 ? 3f : 2f;
	}

	//Strength Cloak, more turns of stealth per charge
	public static int cloakExtraTurns(){
		int pts = p(Talent.STRENGTH_CLOAK);
		if (pts == 1) return 1;
		if (pts == 2) return 1 + Random.Int(2);
		return pts >= 3 ? 2 : 0;
	}

	//Strength Armband, longer debuffs and adrenaline from a steal, cheaper shop steals
	public static int armbandSteal(Char thief, int duration){
		int pts = p(Talent.STRENGTH_ARMBAND);
		if (pts >= 2) duration = Math.round(duration*1.3f);
		if (pts >= 3 && thief != null) Buff.affect(thief, Adrenaline.class, 3f);
		return duration;
	}

	public static int armbandShopCharges(int charges){
		return p(Talent.STRENGTH_ARMBAND) >= 3 ? Math.max(1, charges - 1) : charges;
	}

	//Strength Book, the spellbook reads a scroll that suits a fight or a quiet moment
	public static com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll spellbookScroll(com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll s){
		Hero h = hero();
		if (s == null || h == null || !h.hasTalent(Talent.STRENGTH_BOOK)) return s;
		boolean fight = h.visibleEnemies() > 0;
		boolean quiet = s instanceof com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify
				|| s instanceof com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse
				|| s instanceof com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
		boolean loud = s instanceof com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMirrorImage
				|| s instanceof com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution
				|| s instanceof ScrollOfRage
				|| s instanceof com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTerror;
		if (fight && quiet) return null;
		if (!fight && loud) return null;
		return s;
	}

	//Journey Nature, footwear of nature roots cost half
	public static int sandalsCost(int cost){
		return p(Talent.JOURNEY_NATURE) >= 2 ? Math.max(1, cost/2) : cost;
	}

	public static float naturalismSeeds(){
		return p(Talent.JOURNEY_NATURE) > 0 ? 1.5f : 1f;
	}

	//Bountiful Enhancement, more food per horn charge and a second taste five turns later
	public static int hornBonus(){
		int pts = p(Talent.BOUNTIFUL_ENHANCEMENT);
		return pts >= 3 ? 30 : (pts == 2 ? 20 : 0);
	}

	public static class BountifulReserve extends FlavourBuff {
		{ type = buffType.POSITIVE; }
		@Override public int icon(){ return BuffIndicator.WELL_FED; }
		@Override public float iconFadePercent(){ return Math.max(0, visualcooldown()/5f); }
		@Override
		public void detach(){
			super.detach();
			if (target instanceof Hero && target.isAlive()){
				inReserve = true;
				try {
					Talent.onFoodEaten((Hero) target, Hunger.STARVING/5f, null);
				} finally {
					inReserve = false;
				}
			}
		}
	}
	private static boolean inReserve = false;

	private static void hornEaten(Hero h, Item food){
		if (!inReserve && food instanceof HornOfPlenty && h.hasTalent(Talent.BOUNTIFUL_ENHANCEMENT)){
			Buff.affect(h, BountifulReserve.class, 5f);
		}
	}

	//Strengthen Chain, the pulled enemy is crippled, at +2 the pull is free and costs a charge less
	public static int chainRefund(Char enemy, int chargeUse){
		int pts = p(Talent.STRENGTHEN_CHAIN);
		if (pts >= 1 && enemy != null && enemy.isAlive()) Buff.affect(enemy, Cripple.class, 10f);
		return pts >= 2 && chargeUse > 1 ? 1 : 0;
	}

	public static float chainTime(){
		return p(Talent.STRENGTHEN_CHAIN) >= 2 ? 0f : 1f;
	}

	//---- other systems ------------------------------------------------------------------------

	//Trap Master, a reclaimed trap may come back
	public static void onTrapReclaimed(Hero h){
		int pts = h == null ? 0 : h.pointsInTalent(Talent.TRAP_MASTER);
		if (pts == 0) return;
		float keep = pts == 1 ? 0.66f : (pts == 2 ? 0.75f : 0.8f);
		if (Random.Float() < keep){
			ReclaimTrap again = new ReclaimTrap();
			if (!again.collect()) dropAtHero(again);
		}
	}

	//Hardware Elements, summoning crystals keep their embers but need more energy
	public static int hardwareCost(int cost){
		int pts = p(Talent.HARDWARE_ELEMENTS);
		return pts > 0 ? 25 - 5*pts : cost;
	}

	public static boolean hardwareKeepsEmbers(){
		return p(Talent.HARDWARE_ELEMENTS) > 0;
	}

	//Kebi, the stylus enchants weapons, and rare glyphs and enchantments come up more often
	public static boolean kebi(){
		return p(Talent.KEBI) > 0;
	}

	public static Armor.Glyph kebiGlyph(Armor.Glyph g, Class<? extends Armor.Glyph> old){
		if (p(Talent.KEBI) >= 2 && Random.Int(5) == 0) return Armor.Glyph.randomRare(old);
		return g;
	}

	public static Weapon.Enchantment kebiEnchant(Weapon.Enchantment e, Class<? extends Weapon.Enchantment> old){
		if (p(Talent.KEBI) >= 2 && Random.Int(5) == 0) return Weapon.Enchantment.randomRare(old);
		return e;
	}

	public static void stylusOnWeapon(Item stylus, Weapon w){
		Hero h = hero();
		if (h == null) return;
		w.enchant();
		stylus.detach(h.belongings.backpack);
		GLog.p(Messages.get(TransformationTalents.class, "kebi_enchant", w.name()));
		if (h.sprite != null) h.sprite.operate(h.pos);
		h.spend(1f);
		h.busy();
		h.next();
	}

	//More Favors, the blacksmith counts more for each dark gold and his limit rises
	public static int favorPerGold(int per){
		int pts = p(Talent.MORE_FAVORS);
		return per + 25*pts;
	}

	public static int favorCap(int cap){
		return p(Talent.MORE_FAVORS) >= 2 ? cap + 1000 : cap;
	}

	//Eternal Curse (negative), removing curses takes more
	public static boolean eternalCurseBlocks(Item scroll){
		Hero h = hero();
		if (h == null || n(Talent.ETERNAL_CURSE) < 2) return false;
		if (scroll.quantity() >= 2){
			scroll.quantity(scroll.quantity() - 1);
			Item.updateQuickslot();
			return false;
		}
		GLog.w(Messages.get(TransformationTalents.class, "eternal_curse"));
		return true;
	}

	public static boolean removesCurseEnchant(){
		return n(Talent.ETERNAL_CURSE) != 1;
	}

	//Be Inconstant (negative), a weapon picked up may be equipped at once
	public static void onWeaponPickedUp(Hero h, Item item){
		int pts = h.pointsNegative(Talent.BE_INCONSTANT);
		if (pts > 0 && item instanceof MeleeWeapon && !h.subClass.is(HeroSubClass.CHAMPION)
				&& 10*pts > Random.Int(100) && !item.isEquipped(h)){
			((MeleeWeapon) item).doEquip(h);
		}
	}

	//Phaseclaw (negative), enemies reach one tile further and may leave the hero vulnerable
	public static boolean phaseclawReach(Mob mob, Char enemy){
		if (n(Talent.PHASECLAW) == 0 || enemy != hero() || Dungeon.level == null) return false;
		if (Dungeon.level.distance(mob.pos, enemy.pos) > 2) return false;
		return new com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica(mob.pos, enemy.pos,
				com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica.PROJECTILE).collisionPos == enemy.pos;
	}

	//No Door (negative), more hidden doors
	public static float noDoorFactor(){
		int pts = n(Talent.NO_DOOR);
		return pts >= 2 ? 2f : (pts == 1 ? 1.5f : 1f);
	}

	//Honey Fish, a honeypot makes water and a frenzied piranha instead of a bee
	public static boolean honeyFish(int pos){
		Hero h = hero();
		if (h == null || !h.hasTalent(Talent.HONEY_FISH) || Dungeon.level == null || pos < 0) return false;
		if (h.pointsInTalent(Talent.HONEY_FISH) >= 2) new com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.AquaBrew().shatter(pos);
		Dungeon.level.setCellToWater(true, pos);
		Mob piranha = com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Piranha.random();
		piranha.pos = pos;
		GameScene.add(piranha);
		Buff.affect(piranha, Amok.class, 50f);
		return true;
	}

	//No More Mob, the swarm wakes fewer enemies
	public static boolean noMoreMob(Mob mob){
		Hero h = hero();
		if (h == null || !h.hasTalent(Talent.NO_MORE_MOB)) return false;
		if (Random.Int(2) == 0) return true;
		if (h.pointsInTalent(Talent.NO_MORE_MOB) >= 2){
			mob.state = mob.WANDERING;
			return true;
		}
		return false;
	}

	//Heavy Burden, a higher score
	public static int scoreMultiplier(){
		Hero h = hero();
		return h == null ? 1 : 1 + h.pointsInTalent(Talent.HEAVY_BURDEN);
	}

	//Arrow Penetration, thrown weapons pass through walls to a nearby target
	public static int arrowPenetration(Hero user, int dst, Item item){
		if (!(item instanceof MissileWeapon) || user == null || Dungeon.level == null || dst < 0 || dst >= Dungeon.level.length()) return -1;
		int pts = user.pointsInTalent(Talent.ARROW_PENETRATION);
		if (pts == 0) return -1;
		int range = pts >= 2 ? 12 : 8;
		if ((Dungeon.level.passable[dst] || Dungeon.level.avoid[dst] || Actor.findChar(dst) != null)
				&& Dungeon.level.distance(user.pos, dst) <= range) return dst;
		return -1;
	}

	//Phantom Shooter, a second phantom throw
	private static boolean phantomThrowing = false;

	private static void phantomShooter(Hero h, Char enemy){
		int pts = h.pointsInTalent(Talent.PHANTOM_SHOOTER);
		if (pts == 0 || phantomThrowing || !(h.belongings.attackingWeapon() instanceof MissileWeapon)) return;
		if (Random.Int(10) >= pts || !enemy.isAlive()) return;
		phantomThrowing = true;
		try {
			h.attack(enemy, 1f, 0, 1f);
		} finally {
			phantomThrowing = false;
		}
	}

	//Healattack, hitting an enemy that holds the guiding light heals the hero
	public static void onIlluminatedHit(Char attacker){
		Hero h = hero();
		if (h == null || attacker != h || !h.hasTalent(Talent.HEALATTACK)) return;
		int heal = h.pointsInTalent(Talent.HEALATTACK) >= 2 ? Math.max(1, Math.round(h.lvl*0.3f)) : 3;
		h.HP = Math.min(h.HT, h.HP + heal);
		if (h.sprite != null) h.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(heal), FloatingText.HEALING);
	}

	//Armed Uprising, allies share the hero's on hit talents, at +2 their damage changes too
	private static boolean uprising = false;

	private static int armedUprising(Mob ally, Char enemy, int damage){
		Hero h = hero();
		if (h == null || uprising || ally.alignment != Char.Alignment.ALLY || !h.hasTalent(Talent.ARMED_UPRISING) || enemy == null || enemy == h) return damage;
		uprising = true;
		try {
			int changed = Talent.onAttackProc(h, enemy, damage);
			return h.pointsInTalent(Talent.ARMED_UPRISING) >= 2 ? changed : damage;
		} finally {
			uprising = false;
		}
	}

	//Swift Church, a time bubble only shrinks when the hero acts
	public static boolean bubbleSurvives(Char ch, Swiftthistle.TimeBubble bubble){
		int pts = ch == hero() ? p(Talent.SWIFT_CHURCH) : 0;
		if (pts < 2) return false;
		bubble.processTime(8 - 2*pts);
		return true;
	}
}
