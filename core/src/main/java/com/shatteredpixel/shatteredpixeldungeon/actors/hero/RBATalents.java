package com.shatteredpixel.shatteredpixeldungeon.actors.hero;



import com.shatteredpixel.shatteredpixeldungeon.RBAShims;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBAConversation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Drowsy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBALittleAngry;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBANoticeTracker;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBARabbitSquadBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBAShootAllBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBASuperNovaCharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBASupportDrone;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBAYuzuStatus;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.aris.RBADivision;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.hoshino.RBASpikeShield;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mika.RBAPerfectDeception;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.nonomi.RBABipod;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.shiroko.RBAGPSRoute;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.miracles.RBALightBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EnergyParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.active.RBABicycle;
import com.shatteredpixel.shatteredpixeldungeon.items.active.RBAIronHorus;
import com.shatteredpixel.shatteredpixeldungeon.items.active.RBATrashBin;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Viscosity;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RBACrossNecklace;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RBANinjaCape;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RBAScrunchie;
import com.shatteredpixel.shatteredpixeldungeon.items.food.RBARollCake;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.RBAEmptyScroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfClairvoyance;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.ShardOfOblivion;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Elastic;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RBASuperNova;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.RBAGun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.RBAAR;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL.RBAGL;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.RBAHG;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.RBAMG;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.RBAMG_SP;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.RBASG;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.RBASMG;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.RBASR;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import java.util.ArrayList;
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


	// ---- talent trees ------------------------------------------------------------------------------

	private static final HeroClass[] STUDENTS = {HeroClass.RBA_ARIS, HeroClass.RBA_NONOMI, HeroClass.RBA_MIYAKO, HeroClass.RBA_HOSHINO,
			HeroClass.RBA_SHIROKO, HeroClass.RBA_NOA, HeroClass.RBA_MIYU, HeroClass.RBA_YUZU, HeroClass.RBA_IZUNA, HeroClass.RBA_MIKA, HeroClass.RBA_MARI};

	/** The row of a student on the mod's talent sheet, -1 for other heroes. */
	public static int row( HeroClass cls ){
		for (int i = 0; i < STUDENTS.length; i++) if (STUDENTS[i] == cls) return i;
		return -1;
	}

	private static String key( HeroClass cls ){
		return cls.name().substring(4); //RBA_ARIS is ARIS
	}

	/** Adds a student's class talents for tier 1, 2 or 3, named like ARIS_T1_1 in the mod. */
	public static void addClassTalents( HeroClass cls, int tier, java.util.List<Talent> list ){
		int n = tier == 1 ? 4 : tier == 2 ? 5 : 2;
		for (int i = 1; i <= n; i++){
			list.add(Talent.valueOf("RBA_" + key(cls) + "_T" + tier + "_" + i));
		}
	}

	/** A student subclass's three tier 3 talents, EX1 for the first subclass and EX2 for the second. */
	public static boolean addSubclassTalents( HeroSubClass sub, java.util.List<Talent> list ){
		for (HeroClass cls : STUDENTS){
			HeroSubClass[] subs = cls.subClasses();
			for (int s = 0; s < subs.length; s++){
				if (subs[s] == sub){
					for (int i = 1; i <= 3; i++) list.add(Talent.valueOf("RBA_" + key(cls) + "_EX" + (s+1) + "_" + i));
					return true;
				}
			}
		}
		return false;
	}

	/** Heroic Energy's icon for a student, cell 26 of her row on the mod's sheet. */
	public static int heroicEnergyIcon( HeroClass cls ){
		return 50000 + 26 + 32*row(cls);
	}

	// ---- talent hooks, called from Talent's own ------------------------------------------------------

	public static void onTalentUpgraded( Hero hero, Talent talent ){
		if (talent == Talent.RBA_NONOMI_T3_2 && hero.buff(AutoReloadBuff.class) == null) {
			Buff.affect(hero, AutoReloadBuff.class);
		}
		if (talent == Talent.RBA_IZUNA_T1_4 && hero.invisible > 0){
			Buff.affect(hero, ShadowHideTracker.class);
		}
		if (talent == Talent.RBA_IZUNA_T3_2 && hero.heroClass == HeroClass.RBA_IZUNA){
			for (Item item : Dungeon.hero.belongings.backpack){
				if (item instanceof RBANinjaCape && (!hero.belongings.lostInventory() || item.keptThroughLostInventory())) {
					((RBANinjaCape) item).activate(Dungeon.hero);
				}
			}
		}
		if (talent == Talent.RBA_MIKA_T3_2 && hero.heroClass == HeroClass.RBA_MIKA){
			for (Item item : Dungeon.hero.belongings.backpack){
				if (item instanceof RBAScrunchie && (!hero.belongings.lostInventory() || item.keptThroughLostInventory())) {
					((RBAScrunchie) item).activate(Dungeon.hero);
				}
			}
		}
		if (talent == Talent.RBA_MARI_T3_2 && hero.heroClass == HeroClass.RBA_MARI){
			for (Item item : Dungeon.hero.belongings.backpack){
				if (item instanceof RBACrossNecklace && (!hero.belongings.lostInventory() || item.keptThroughLostInventory())) {
					((RBACrossNecklace) item).activate(Dungeon.hero);
				}
			}
		}
		if (talent == Talent.RBA_MIYAKO_T2_3 || talent == Talent.RBA_MIYAKO_EX2_3 || talent == Talent.RBA_HOSHINO_T2_3
				|| talent == Talent.RBA_SHIROKO_T2_3 || talent == Talent.RBA_MARI_ARMOR2_2){
			Dungeon.observe();
		}
		if (talent == Talent.RBA_ARIS_T1_2 && hero.pointsInTalent(Talent.RBA_ARIS_T1_2) == 2 && !ShardOfOblivion.passiveIDDisabled()){
			if (hero.belongings.armor() != null) hero.belongings.armor.identify();
			if (hero.belongings.weapon() != null) hero.belongings.weapon.identify();
		}
		identifyKind(hero, talent, Talent.RBA_NONOMI_T1_2, RBAMG.class);
		identifyKind(hero, talent, Talent.RBA_MIYAKO_T1_2, RBASMG.class);
		identifyKind(hero, talent, Talent.RBA_HOSHINO_T1_2, RBASG.class);
		identifyKind(hero, talent, Talent.RBA_SHIROKO_T1_2, RBAAR.class);
		identifyKind(hero, talent, Talent.RBA_NOA_T1_2, RBAHG.class);
		identifyKind(hero, talent, Talent.RBA_MIYU_T1_2, RBASR.class);
		identifyKind(hero, talent, Talent.RBA_YUZU_T1_2, RBAGL.class);
		if (talent == Talent.RBA_MIKA_T1_2 && !ShardOfOblivion.passiveIDDisabled()) {
			if (hero.pointsInTalent(Talent.RBA_MIKA_T1_2) == 1 && hero.belongings.armor != null) hero.belongings.armor.identify();
			if (hero.pointsInTalent(Talent.RBA_MIKA_T1_2) == 2) for (Item i : hero.belongings.getAllItems(Armor.class)) i.identify();
		}
		if (talent == Talent.RBA_NONOMI_T3_1 && hero.pointsInTalent(Talent.RBA_NONOMI_T3_1) == 1) {
			Item item = new RBAMG_SP();
			item.identify();
			if (!item.collect()) {
				Dungeon.level.drop(item, hero.pos).sprite.drop();
			}
		}
		if (talent == Talent.RBA_NONOMI_T3_1 || talent == Talent.RBA_NONOMI_EX1_1 || talent == Talent.RBA_MIYAKO_T2_2
				|| talent == Talent.RBA_IZUNA_T1_1 || talent == Talent.RBA_IZUNA_EX2_3 || talent == Talent.RBA_MIKA_T3_1) {
			Item.updateQuickslot();
		}
		if (talent == Talent.RBA_MIYAKO_T2_5) {
			hero.updateHT(true);
		}
		if (talent == Talent.RBA_MIYU_T3_2) {
			Buff.affect(hero, RBANoticeTracker.class);
			Item.updateQuickslot();
		}
	}

	private static void identifyKind( Hero hero, Talent talent, Talent which, Class<? extends Item> kind ){
		if (talent != which || ShardOfOblivion.passiveIDDisabled()) return;
		if (hero.pointsInTalent(which) == 1) {
			if (kind.isInstance(hero.belongings.weapon())) hero.belongings.weapon.identify();
		} else if (hero.pointsInTalent(which) == 2) {
			for (Item i : hero.belongings.getAllItems(kind)) i.identify();
		}
	}

	public static void onFoodEaten( Hero hero, float foodVal, Item foodSource ){
		if (hero.hasTalent(Talent.RBA_NONOMI_T1_1) && hero.HP/(float)hero.HT <= 0.4f) {
			hero.heal(1 + 2 * hero.pointsInTalent(Talent.RBA_NONOMI_T1_1));
		}
		if (hero.hasTalent(Talent.RBA_ARIS_T2_1) && hero.cooldown() > 0) {
			Buff.affect(hero, Talent.WarriorFoodImmunity.class, hero.cooldown());
		}
		if (hero.hasTalent(Talent.RBA_IZUNA_T2_1)){
			ArtifactRecharge buff = Buff.affect( hero, ArtifactRecharge.class);
			int turns = 1 + 2*hero.pointsInTalent(Talent.RBA_IZUNA_T2_1);
			if (buff.left() < turns){
				buff.set(turns).ignoreHornOfPlenty = foodSource instanceof HornOfPlenty;
			}
			ScrollOfRecharging.charge( hero );
		}
		if (hero.hasTalent(Talent.RBA_NONOMI_T2_1)) {
			KindOfWeapon weapon = hero.belongings.weapon();
			if (weapon instanceof RBAGun) {
				((RBAGun) weapon).quickReload();
				if (hero.pointsInTalent(Talent.RBA_NONOMI_T2_1) == 2) {
					((RBAGun) weapon).manualReload(1, true);
				}
			}
		}
		if (hero.hasTalent(Talent.RBA_MIYAKO_T2_1)) {
			Buff.affect(hero, Barrier.class).setShield((int)(hero.HT*(0.05f+0.05f*hero.pointsInTalent(Talent.RBA_MIYAKO_T2_1))));
		}
		if (hero.hasTalent(Talent.RBA_HOSHINO_T2_1) && hero.buff(Drowsy.class) == null) {
			Buff.affect(hero, Drowsy.class, 275-75*hero.pointsInTalent(Talent.RBA_HOSHINO_T2_1));
		}
		if (hero.hasTalent(Talent.RBA_SHIROKO_T1_1)) {
			hero.heal(hero.pointsInTalent(Talent.RBA_SHIROKO_T1_1));
		}
		if (hero.hasTalent(Talent.RBA_SHIROKO_T2_1)) {
			Buff.affect(hero, Haste.class, 1+hero.pointsInTalent(Talent.RBA_SHIROKO_T2_1));
		}
		if (hero.hasTalent(Talent.RBA_NOA_T2_1)) {
			Buff.affect(hero, Healing.class).setHeal(2+3*hero.pointsInTalent(Talent.RBA_NOA_T2_1), 0, 1);
		}
		if (hero.hasTalent(Talent.RBA_MIYU_T2_1)) {
			Buff.affect(hero, Invisibility.class, 1+2*hero.pointsInTalent(Talent.RBA_MIYU_T2_1));
		}
		if (hero.hasTalent(Talent.RBA_YUZU_T1_1)) {
			Buff.affect(hero, RBAYuzuStatus.CertainCritBuff.class).countUp(1+hero.pointsInTalent(Talent.RBA_YUZU_T1_1));
		}
		if (hero.hasTalent(Talent.RBA_YUZU_T2_1)) {
			RBAShims.cleanseDebuff(hero, hero.pointsInTalent(Talent.RBA_YUZU_T2_1) == 2 ? 2 : 0);
		}
		if (hero.hasTalent(Talent.RBA_MIKA_T2_1)) {
			Buff.affect(hero, Swiftthistle.TimeBubble.class).reset(1+2*hero.pointsInTalent(Talent.RBA_MIKA_T2_1));
		}
		if (hero.hasTalent(Talent.RBA_MIKA_T2_2) && Random.Float() < 0.1f+0.1f*hero.pointsInTalent(Talent.RBA_MIKA_T2_2)) {
			Dungeon.level.drop(new RBARollCake(), hero.pos).sprite.drop();
		}
		if (hero.hasTalent(Talent.RBA_MARI_T1_1)){
			Buff.affect(hero, BlessOfPlentyTracker.class);
		}
		if (hero.hasTalent(Talent.RBA_MARI_T2_1)){
			RBACrossNecklace cross = hero.belongings.getItem(RBACrossNecklace.class);
			if (cross != null) {
				cross.directCharge( 2+2*hero.pointsInTalent(Talent.RBA_MARI_T2_1) );
				ScrollOfRecharging.charge(hero);
			}
		}
	}

	/** How much faster a student's talents identify an item, multiplied into Shattered's factor. */
	public static float itemIDSpeedFactor( Hero hero, Item item ){
		float factor = 1f;
		if (item instanceof MeleeWeapon || item instanceof Armor){
			factor *= 1f + 1.5f*hero.pointsInTalent(Talent.RBA_ARIS_T1_2);
		}
		if (item instanceof MissileWeapon){
			factor *= 1f + 2.0f*hero.pointsInTalent(Talent.RBA_IZUNA_T1_2);
		}
		return factor;
	}

	public static void onPotionUsed( Hero hero, int cell, float factor ){
		if (hero.hasTalent(Talent.RBA_SHIROKO_T2_2)) {
			RBABicycle bicycle = hero.belongings.getItem(RBABicycle.class);
			if (bicycle != null) {
				bicycle.chargeUp((10+10*hero.pointsInTalent(Talent.RBA_SHIROKO_T2_2))*factor);
				hero.sprite.centerEmitter().burst(EnergyParticle.FACTORY, 10);
				Item.updateQuickslot();
			}
		}
		if (hero.hasTalent(Talent.RBA_MIYU_T2_2)) {
			ArrayList<Integer> grassCells = new ArrayList<>();
			for (int i : PathFinder.NEIGHBOURS9){
				grassCells.add(cell+i);
			}
			Random.shuffle(grassCells);
			for (int grassCell : grassCells){
				Char ch = Actor.findChar(grassCell);
				if (ch != null && ch.alignment == Char.Alignment.ENEMY){
					Buff.affect(ch, Roots.class, factor * hero.pointsInTalent(Talent.RBA_MIYU_T2_2));
				}
				if (Dungeon.level.map[grassCell] == Terrain.EMPTY ||
						Dungeon.level.map[grassCell] == Terrain.EMBERS ||
						Dungeon.level.map[grassCell] == Terrain.EMPTY_DECO){
					Level.set(grassCell, Terrain.GRASS);
					GameScene.updateMap(grassCell);
				}
				CellEmitter.get(grassCell).burst(LeafParticle.LEVEL_SPECIFIC, 4);
			}
			int totalGrassCells = (int) (factor * (2 + 2 * hero.pointsInTalent(Talent.RBA_MIYU_T2_2)));
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
	}

	public static void onScrollUsed( Hero hero, int pos, float factor ){
		if (hero.hasTalent(Talent.RBA_HOSHINO_T2_2)) {
			RBAShims.clairvoyance(pos, (int)Math.ceil((2+4*hero.pointsInTalent(Talent.RBA_HOSHINO_T2_2))*factor));
		}
		if (hero.hasTalent(Talent.RBA_NOA_T2_2) && Random.Float() < (0.1f+0.1f*hero.pointsInTalent(Talent.RBA_NOA_T2_2))*factor) {
			Item scroll = new RBAEmptyScroll();
			if (!scroll.doPickUp(hero)) {
				Dungeon.level.drop(scroll, hero.pos).sprite.drop(hero.pos);
			}
		}
		if (hero.subClass == HeroSubClass.RBA_CONVERSATION && hero.hasTalent(Talent.RBA_NOA_EX2_1)) {
			Buff.affect(hero, RBAConversation.class).charge(2*factor);
		}
		if (hero.hasTalent(Talent.RBA_YUZU_T2_2)) {
			RBAShims.clairvoyance(pos, (int)Math.ceil((2+4*hero.pointsInTalent(Talent.RBA_YUZU_T2_2))*factor));
		}
		if (hero.hasTalent(Talent.RBA_IZUNA_T2_2)) {
			Buff.affect(hero, Barrier.class).setShield((int)Math.ceil((5+5*hero.pointsInTalent(Talent.RBA_IZUNA_T2_2))*factor));
		}
		if (hero.hasTalent(Talent.RBA_MARI_T2_2) && hero.belongings.weapon instanceof RBAGun) {
			int maxRounds = ((RBAGun) hero.belongings.weapon).maxRound();
			int bulletsToLoad = (int)GameMath.gate(1, (int)Math.ceil(maxRounds*0.25f*hero.pointsInTalent(Talent.RBA_MARI_T2_2)*factor), maxRounds);
			((RBAGun) hero.belongings.weapon).quickReload();
			Buff.affect(hero, RBALightBullet.LightMagazine.class).set(((RBAGun) hero.belongings.weapon), bulletsToLoad);
		}
	}

	public static void onRunestoneUsed( Hero hero ){
		if (hero.subClass == HeroSubClass.RBA_CONVERSATION && hero.pointsInTalent(Talent.RBA_NOA_EX2_1) >= 2) {
			Buff.affect(hero, RBAConversation.class).charge();
		}
	}

	/** True when a student's talent identifies this item as it is equipped. */
	public static boolean identifiesOnEquip( Hero hero, Item item ){
		return (hero.pointsInTalent(Talent.RBA_ARIS_T1_2) == 2 && (item instanceof Weapon || item instanceof Armor))
				|| (hero.hasTalent(Talent.RBA_NONOMI_T1_2) && item instanceof RBAMG)
				|| (hero.hasTalent(Talent.RBA_MIYAKO_T1_2) && item instanceof RBASMG)
				|| (hero.hasTalent(Talent.RBA_HOSHINO_T1_2) && item instanceof RBASG)
				|| (hero.hasTalent(Talent.RBA_SHIROKO_T1_2) && item instanceof RBAAR)
				|| (hero.hasTalent(Talent.RBA_NOA_T1_2) && item instanceof RBAHG)
				|| (hero.hasTalent(Talent.RBA_MIYU_T1_2) && item instanceof RBASR)
				|| (hero.hasTalent(Talent.RBA_YUZU_T1_2) && item instanceof RBAGL)
				|| (hero.hasTalent(Talent.RBA_MIKA_T1_2) && item instanceof Armor);
	}

	public static void onItemCollected( Hero hero, Item item ){
		boolean identify = (hero.pointsInTalent(Talent.RBA_NONOMI_T1_2) == 2 && item instanceof RBAMG)
				|| (hero.pointsInTalent(Talent.RBA_MIYAKO_T1_2) == 2 && item instanceof RBASMG)
				|| (hero.pointsInTalent(Talent.RBA_HOSHINO_T1_2) == 2 && item instanceof RBASG)
				|| (hero.pointsInTalent(Talent.RBA_SHIROKO_T1_2) == 2 && item instanceof RBAAR)
				|| (hero.pointsInTalent(Talent.RBA_NOA_T1_2) == 2 && item instanceof RBAHG)
				|| (hero.pointsInTalent(Talent.RBA_MIYU_T1_2) == 2 && item instanceof RBASR)
				|| (hero.pointsInTalent(Talent.RBA_YUZU_T1_2) == 2 && item instanceof RBAGL)
				|| (hero.pointsInTalent(Talent.RBA_MIKA_T1_2) == 2 && item instanceof Armor);
		if (identify && !ShardOfOblivion.passiveIDDisabled()){
			item.identify();
		}
	}

	public static int onAttackProc( Hero hero, Char enemy, int dmg ){
		if (hero.hasTalent(Talent.RBA_MIYU_T1_3)
				&& enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)
				&& enemy.buff(AmbushShotTracker.class) == null
				&& hero.belongings.attackingWeapon() instanceof RBAGun.Bullet){
			dmg += 1+hero.pointsInTalent(Talent.RBA_MIYU_T1_3);
			Buff.affect(enemy, AmbushShotTracker.class);
		}
		if (hero.hasTalent(Talent.RBA_ARIS_T1_1)) {
			dmg += hero.pointsInTalent(Talent.RBA_ARIS_T1_1);
		}
		if (hero.hasTalent(Talent.RBA_ARIS_T2_2) && enemy.buff(HikariyoTracker.class) == null){
			Buff.affect(enemy, Blindness.class, 1+hero.pointsInTalent(Talent.RBA_ARIS_T2_2));
			Buff.affect(enemy, HikariyoTracker.class);
		}
		if (hero.hasTalent(Talent.RBA_ARIS_EX1_3) && hero.buff(RBASuperNova.SuperNovaCooldown.class) != null) {
			hero.buff(RBASuperNova.SuperNovaCooldown.class).hit(hero.pointsInTalent(Talent.RBA_ARIS_EX1_3));
		}
		if (hero.subClass == HeroSubClass.RBA_BATTERY_CHARGE) {
			Buff.affect(hero, RBASuperNovaCharge.class).hit();
		}
		if (hero.buff(RBADivision.DivisionBuff.class) != null) {
			dmg = hero.buff(RBADivision.DivisionBuff.class).attackProc(hero, enemy, dmg);
		}
		if (hero.hasTalent(Talent.RBA_NONOMI_T2_5) && enemy.buff(PushingTracker.class) == null && !(hero.belongings.attackingWeapon() instanceof MissileWeapon)) {
			Buff.affect(enemy, PushingTracker.class);
			Elastic.pushEnemy(hero.belongings.weapon(), hero, enemy, 1 + hero.pointsInTalent(Talent.RBA_NONOMI_T2_5));
			if (Random.Float() < 0.2f) {
				RBAHero.yellI(hero, "push");
			}
		}
		if (hero.hasTalent(Talent.RBA_NONOMI_EX1_3)
				&& !(hero.belongings.attackingWeapon() instanceof MissileWeapon)
				&& hero.buff(RBAShootAllBuff.OverHeat.class) != null) {
			if (Random.Float() < 0.1f * (hero.buff(RBAShootAllBuff.OverHeat.class).duration() + hero.pointsInTalent(Talent.RBA_NONOMI_EX1_3) - 1)) {
				Buff.affect(enemy, Burning.class).reignite(enemy);
			}
		}
		if (hero.hasTalent(Talent.RBA_NONOMI_EX1_2)
				&& !(hero.belongings.attackingWeapon() instanceof MissileWeapon)
				&& hero.buff(RBAShootAllBuff.OverHeat.class) != null) {
			if (Random.Float() < hero.pointsInTalent(Talent.RBA_NONOMI_EX1_2) / 3f) {
				hero.buff(RBAShootAllBuff.OverHeat.class).hit();
			}
		}
		if (hero.buff(RBABipod.BipodBuff.class) != null && !(hero.belongings.attackingWeapon() instanceof RBAGun.Bullet)) {
			hero.buff(RBABipod.BipodBuff.class).detach();
		}
		if (hero.hasTalent(Talent.RBA_MIYAKO_T1_3) && hero.belongings.attackingWeapon() instanceof RBASMG) {
			dmg += Random.IntRange(1, hero.pointsInTalent(Talent.RBA_MIYAKO_T1_3));
		}
		if (hero.hasTalent(Talent.RBA_MIYAKO_T1_4)
				&& enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)
				&& enemy.isAlive()
				&& enemy.buff(ConfusionTracker.class) == null){
			Buff.affect(enemy, Vertigo.class, 1+hero.pointsInTalent(Talent.RBA_MIYAKO_T1_4));
			Buff.affect(enemy, ConfusionTracker.class);
		}
		if (hero.buff(RBARabbitSquadBuff.class) != null) {
			hero.buff(RBARabbitSquadBuff.class).attack(enemy);
		}
		if (hero.buff(RBASupportDrone.class) != null && hero.belongings.attackingWeapon() instanceof RBAGun.Bullet) {
			hero.buff(RBASupportDrone.class).attackProc(enemy);
		}
		if (hero.buff(TakingAimTracker.class) != null) hero.buff(TakingAimTracker.class).detach();
		if (hero.buff(DestructionInstinct.class) != null) {
			dmg += 2+3*hero.pointsInTalent(Talent.RBA_MIKA_T1_1);
			hero.buff(DestructionInstinct.class).detach();
		}
		if (hero.subClass == HeroSubClass.RBA_CALL_OF_STAR && hero.buff(RBACallOfStar.CallOfStarCooldown.class) == null) {
			Buff.affect(hero, RBACallOfStar.class).onHit(hero.belongings.attackingWeapon());
		}
		if (hero.subClass == HeroSubClass.RBA_LITTLE_ANGRY) {
			if (enemy.buff(Paralysis.class) != null && enemy.buff(WandOfBlastWave.BWaveOnHitTracker.class) == null){
				enemy.buff(Paralysis.class).detach();
				int bonusDmg = Hero.heroDamageIntRange(hero.lvl + hero.STR()*2, 2*(hero.lvl + hero.STR()*2));
				if (hero.hasTalent(Talent.RBA_MIKA_EX2_2)) {
					bonusDmg = Math.round(bonusDmg*(1+(hero.pointsInTalent(Talent.RBA_MIKA_EX2_2)/6f)*((hero.HT-hero.HP)/(float)hero.HT)));
				}
				enemy.damage(bonusDmg, RBALittleAngry.class);
				WandOfBlastWave.BlastWave.blast(enemy.pos);
				Sample.INSTANCE.play( Assets.Sounds.BLAST );
				Buff.prolong(enemy, WandOfBlastWave.BWaveOnHitTracker.class, 3f);
			}
		}
		if (hero.buff(RBAPerfectDeception.DeceptionBuff.class) != null) {
			dmg = hero.buff(RBAPerfectDeception.DeceptionBuff.class).onAttack(dmg, enemy);
		}
		return dmg;
	}

	public static int onDefenseProc(Hero hero, Char enemy, int damage) {
		if (hero.buff(FirstAidCooldown.class) == null && hero.HP <= hero.HT/4 && hero.hasTalent(Talent.RBA_HOSHINO_T1_1)) {
			hero.heal(2+2*hero.pointsInTalent(Talent.RBA_HOSHINO_T1_1));
			Buff.affect(hero, FirstAidCooldown.class, 50f);
		}
		if (hero.buff(RBASpikeShield.SpikeShieldBuff.class) != null && (hero.buff(RBAIronHorus.TacticalShieldBuff.class) != null || hero.buff(RBAIronHorus.LightTacticalShieldBuff.class) != null)) {
			hero.buff(RBASpikeShield.SpikeShieldBuff.class).onHit(hero, enemy);
		}
		if (hero.hasTalent(Talent.RBA_YUZU_T2_3)) {
			RBAShims.applyViscosity(hero, 1+2*hero.pointsInTalent(Talent.RBA_YUZU_T2_3));
		}
		if (hero.hasTalent(Talent.RBA_IZUNA_T1_3) && Random.Float() < 0.5f) {
			Buff.affect(enemy, Bleeding.class).set(Random.IntRange(1, hero.pointsInTalent(Talent.RBA_IZUNA_T1_3)));
		}
		if (hero.hasTalent(Talent.RBA_MIKA_T1_3)) {
			Buff.prolong(hero, Adrenaline.class, 1+2*hero.pointsInTalent(Talent.RBA_MIKA_T1_3));
		}
		if (hero.hasTalent(Talent.RBA_MIKA_T1_4)
				&& hero.buff(PitifulGirlCooldown.class) == null
				&& hero.HP < hero.HT*0.05f*hero.pointsInTalent(Talent.RBA_MIKA_T1_4)) {
			Buff.affect(hero, Barrier.class).setShield(10);
			Buff.affect(hero, PitifulGirlCooldown.class, PitifulGirlCooldown.DURATION);
		}
		return damage;
	}

	public static void onTrampleGrass(Hero hero) {
		if (hero.hasTalent(Talent.RBA_MIYU_T1_4)) {
			Buff.affect(hero, Barrier.class).setShield(1+hero.pointsInTalent(Talent.RBA_MIYU_T1_4));
		}
		if (hero.hasTalent(Talent.RBA_MIYU_T2_3)) {
			Buff.affect(hero, Invisibility.class, 1+hero.pointsInTalent(Talent.RBA_MIYU_T2_3));
		}
		if (hero.hasTalent(Talent.RBA_MIYU_EX2_3)) {
			Buff.affect(hero, RBATrashBin.EvasionBuff.class, RBATrashBin.EvasionBuff.DURATION);
		}
	}

	// ---- on arrival at a floor, Aris's compass, Shiroko's lockpicker sense and Noa's trap sense ----------

	public static boolean[] compassed = new boolean[64];

	public static void onLevelArrive(){
		Hero hero = Dungeon.hero;
		if (hero == null || Dungeon.level == null) return;
		if (hero.hasTalent(Talent.RBA_ARIS_T3_2) && Dungeon.branch == 0 && Dungeon.depth < compassed.length && !compassed[Dungeon.depth]) {
			if (Random.Float() < hero.pointsInTalent(Talent.RBA_ARIS_T3_2)/3f) {
				int len = Dungeon.level.length();
				boolean[] passable = new boolean[len];
				for (int i = 0; i < len; i++) {
					passable[i] = (Dungeon.level.passable[i] || Dungeon.level.secret[i]) && !Dungeon.level.avoid[i];
				}
				PathFinder.Path path = Dungeon.findPath(hero, Dungeon.level.exit(), passable, passable, false);
				if (path == null || PathFinder.distance[Dungeon.level.exit()] == Integer.MAX_VALUE){
					RBAHero.yellN(hero, "cant_find_path");
				} else {
					for (int i : path) reveal(i);
					GameScene.updateFog();
					RBAHero.yellP(hero, "found_path");
				}
			}
			compassed[Dungeon.depth] = true;
		}
		if (hero.pointsInTalent(Talent.RBA_SHIROKO_T2_4) == 2) {
			for (int i = 0; i < Dungeon.level.length(); i++){
				if (Dungeon.level.map[i] == Terrain.LOCKED_DOOR){
					for (int j : PathFinder.NEIGHBOURS9) reveal(i + j);
				}
			}
			GameScene.updateFog();
		}
		if (hero.buff(RBAGPSRoute.RouteBuff.class) != null) hero.buff(RBAGPSRoute.RouteBuff.class).detach();
	}

	/** Noa's trap sense, rolled once per floor when it is first built. */
	public static void onLevelCreated(){
		Hero hero = Dungeon.hero;
		if (hero == null || !hero.hasTalent(Talent.RBA_NOA_T2_5) || !(Dungeon.level instanceof RegularLevel) || Dungeon.branch != 0) return;
		Random.pushGenerator(Dungeon.seedCurDepth()+1);
		if (Random.Float() < 0.1f+0.2f*hero.pointsInTalent(Talent.RBA_NOA_T2_5)){
			for (int i = 0; i < Dungeon.level.length(); i++) {
				if (Dungeon.level.traps.get( i ) != null) {
					Dungeon.level.mapped[i] = true;
					Dungeon.level.discover( i );
				}
			}
			GameScene.updateFog();
			RBAHero.yellP(hero, "trap_discover");
		}
		Random.popGenerator();
	}

	private static void reveal( int c ){
		if (c < 0 || c >= Dungeon.level.length() || !Dungeon.level.discoverable[c]) return;
		int terr = Dungeon.level.map[c];
		Dungeon.level.mapped[c] = true;
		if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {
			Dungeon.level.discover( c );
			if (Dungeon.level.heroFOV[c]) {
				GameScene.discoverTile( c, terr );
				ScrollOfMagicMapping.discover( c );
			}
		}
	}

}
