package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArtifactRecharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBACallOfStar;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBAChaseMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBAConversation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.CounterBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Drowsy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.EnhancedRings;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBALittleAngry;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBANoticeTracker;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PhysicalEmpower;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBARabbitSquadBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RevealedArea;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ScrollEmpower;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBAShootAllBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBASuperNovaCharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBASupportDrone;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WandEmpower;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBAYuzuStatus;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.Ratmogrify;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.aris.RBADivision;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.hoshino.RBASpikeShield;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mika.RBAPerfectDeception;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.nonomi.RBABipod;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.miracles.RBALightBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.DivineSense;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.RecallInscription;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EnergyParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.active.RBABicycle;
import com.shatteredpixel.shatteredpixeldungeon.items.active.RBAIronHorus;
import com.shatteredpixel.shatteredpixeldungeon.items.active.RBATrashBin;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClothArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Viscosity;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RBACrossNecklace;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RBANinjaCape;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RBAScrunchie;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.RBARollCake;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.RBAEmptyScroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.Runestone;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfClairvoyance;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfIntuition;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.ShardOfOblivion;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Elastic;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Gloves;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RBASuperNova;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.RBAAR;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL.RBAGL;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.RBAGun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.RBAHG;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.RBAMG;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.RBAMG_SP;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.RBASG;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.RBASMG;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.RBASR;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

/** Re Blue Archive PD, buffs and trackers the mod kept inside Talent, and its talent hooks. */
public class RBATalents {

	public static class AmbushShotTracker extends Buff {}

	public static class AutoReloadBuff extends Buff {
		{
			revivePersists = true;
		}

		@Override
		public boolean act() {
			Hero hero = Dungeon.hero;
			if (hero == null || target != Dungeon.hero) {
				detach();
				return true;
			}

			if (hero.hasTalent(Talent.RBA_NONOMI_T3_2) && hero.belongings.weapon instanceof RBAGun && ((RBAGun)hero.belongings.weapon).round() < ((RBAGun)hero.belongings.weapon).maxRound()) {
				if (Random.Float() < 0.05f * hero.pointsInTalent(Talent.RBA_NONOMI_T3_2)) ((RBAGun)hero.belongings.weapon).manualReload();
			}
			spend(TICK);
			return true;
		}
	}

	public static class BlessOfPlentyTracker extends Buff{
		@Override
		public int icon() {
			return BuffIndicator.SPELL_FOOD;
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", 1+2*Dungeon.hero.pointsInTalent(Talent.RBA_MARI_T1_1));
		}
	}

	public static class CharmTracker extends Buff {}

	public static class ConfusionTracker extends Buff{}

	public static class DestructionInstinct extends FlavourBuff {

		public static float DURATION = 5f;

		@Override
		public int icon() {
			return BuffIndicator.UPGRADE;
		}

		@Override
		public void tintIcon(Image icon) {
			icon.hardlight(1f, 0, 0);
		}

		@Override
		public float iconFadePercent() {
			return Math.max(0, (DURATION - visualcooldown()) / DURATION);
		}

	}

	public static class FirstAidCooldown extends FlavourBuff {
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0.15f, 0.2f, 0.5f); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 50); }
	}

	public static class HardTackDropped extends CounterBuff{{revivePersists = true;}}

	public static class HikariyoTracker extends Buff{}

	public static class InstantFocusingTracker extends FlavourBuff{
		{ type = buffType.POSITIVE; }
		public int icon() { return BuffIndicator.INVERT_MARK; }
		public void tintIcon(Image icon) { icon.hardlight(1f, 1f, 0.0f); }
		public float iconFadePercent() { return Math.max(0, 1f - (visualcooldown() / 5)); }
	}

	public static class InstantThrowCooldown extends FlavourBuff {

		public static int DURATION = 15;

		@Override
		public int icon() {
			return BuffIndicator.TIME;
		}

		@Override
		public void tintIcon(Image icon) {
			icon.hardlight(0xFDA082);
		}

		@Override
		public float iconFadePercent() {
			return Math.max(0, (DURATION - visualcooldown()) / DURATION);
		}

	}

	public static class InstantThrowTracker extends FlavourBuff {
		@Override
		public int icon() {
			return BuffIndicator.RBA_THROWN_WEP;
		}

		@Override
		public void tintIcon(Image icon) {
			icon.hardlight(0xFDA082);
		}

		public void onThrow() {
			Buff.affect(target, InstantThrowCooldown.class, 20f-5f*Dungeon.hero.pointsInTalent(Talent.RBA_IZUNA_EX2_1)-1);
			detach();
		}

		@Override
		public String iconTextDisplay() {
			return "";
		}
	}

	public static class IntimidateBonusDamageBuff extends Buff {
		public int left;
		public int dmgBonus = 0;

		@Override
		public int icon() {
			return BuffIndicator.UPGRADE;
		}

		@Override
		public void tintIcon(Image icon) {
			icon.hardlight(Math.min(2f, 1+0.1f*dmgBonus), 0.5f, 0);
		}

		@Override
		public String iconTextDisplay() {
			return Integer.toString(left);
		}

		@Override
		public float iconFadePercent() {
			return 1-left/(float)(1+Dungeon.hero.pointsInTalent(Talent.RBA_HOSHINO_T2_5));
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", dmgBonus, left);
		}

		public void set() {
			left = 1+Dungeon.hero.pointsInTalent(Talent.RBA_HOSHINO_T2_5);
			dmgBonus += 1;
			dmgBonus = Math.min(10, dmgBonus);
		}

		public void use() {
			left--;
			if (left <= 0) detach();
		}

		public int dmgBonus() {
			return dmgBonus;
		}

		private static final String LEFT	    = "left";
		private static final String DMG_BONUS    = "dmgBonus";

		@Override
		public void storeInBundle( Bundle bundle ) {
			super.storeInBundle( bundle );
			bundle.put( LEFT, left );
			bundle.put( DMG_BONUS, dmgBonus );
		}

		@Override
		public void restoreFromBundle( Bundle bundle ) {
			super.restoreFromBundle( bundle );
			left = bundle.getInt( LEFT );
			dmgBonus = bundle.getInt( DMG_BONUS );
		}
	}

	public static class PerfectPrecisionTracker extends FlavourBuff{
		{ type = buffType.POSITIVE; }
		public int icon() { return BuffIndicator.INVERT_MARK; }
		public void tintIcon(Image icon) { icon.hardlight(1f, 1f, 0.0f); }
		public float iconFadePercent() { return Math.max(0, 1f - (visualcooldown() / 5)); }
	}

	public static class PitifulGirlCooldown extends FlavourBuff {
		public static final float DURATION = 100f;

		@Override
		public int icon() {
			return BuffIndicator.TIME;
		}

		@Override
		public void tintIcon(Image icon) {
			icon.hardlight(0xF284F1);
		}

		@Override
		public float iconFadePercent() {
			return Math.max(0, (DURATION-visualcooldown())/DURATION);
		}
	}

	public static class PushingTracker extends Buff {}

	public static class RiposteTracker extends Buff {
		{
			actPriority = VFX_PRIO;
		}

		public Char enemy;

		@Override
		public boolean act() {
			target.sprite.attack(enemy.pos, new Callback() {
				@Override
				public void call() {
					AttackIndicator.target(enemy);
					if (target.attack(enemy, 1f, 0, 1)) {
						Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
					}

					next();
				}
			});
			detach();
			return false;
		}
	}

	public static class SafetyTriggerCooldown extends FlavourBuff {
		public static final float DURATION = 10f;

		@Override
		public int icon() {
			return BuffIndicator.TIME;
		}

		@Override
		public void tintIcon(Image icon) {
			icon.hardlight(0xE2A865);
		}

		@Override
		public float iconFadePercent() {
			return Math.max(0, (DURATION-visualcooldown())/DURATION);
		}
	}

	public static class ShadowHideTracker extends Buff {
		float barrierInc = 0.5f;

		@Override
		public boolean act() {
			//barrier every 2/1 turns, to a max of 3/5
			if (((Hero)target).hasTalent(Talent.RBA_IZUNA_T1_4) && target.invisible > 0){
				Barrier barrier = Buff.affect(target, Barrier.class);
				if (barrier.shielding() < 1 + 2*((Hero)target).pointsInTalent(Talent.RBA_IZUNA_T1_4)) {
					barrierInc += 0.5f * ((Hero) target).pointsInTalent(Talent.RBA_IZUNA_T1_4);
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

	public static class TacticalSuppressTracker extends Buff {}

	public static class TakingAimTracker extends CounterBuff {}

	public static boolean hasFoodTalent(Hero hero) {
		if (hero == null) return false;
		return hero.hasTalent(Talent.IRON_STOMACH)
				|| hero.hasTalent(Talent.ENERGIZING_MEAL)
				|| hero.hasTalent(Talent.MYSTICAL_MEAL)
				|| hero.hasTalent(Talent.INVIGORATING_MEAL)
				|| hero.hasTalent(Talent.FOCUSED_MEAL)
				|| hero.hasTalent(Talent.ENLIGHTENING_MEAL)
				|| hero.hasTalent(Talent.RBA_ARIS_T2_1)
				|| hero.hasTalent(Talent.RBA_NONOMI_T2_1)
				|| hero.hasTalent(Talent.RBA_MIYAKO_T2_1)
				|| hero.hasTalent(Talent.RBA_HOSHINO_T2_1)
				|| hero.hasTalent(Talent.RBA_SHIROKO_T2_1)
				|| hero.hasTalent(Talent.RBA_NOA_T2_1)
				|| hero.hasTalent(Talent.RBA_MIYU_T2_1)
				|| hero.hasTalent(Talent.RBA_YUZU_T2_1)
				|| hero.hasTalent(Talent.RBA_IZUNA_T2_1)
				|| hero.hasTalent(Talent.RBA_MIKA_T2_1)
				|| hero.hasTalent(Talent.RBA_MARI_T2_1);
	}

}
