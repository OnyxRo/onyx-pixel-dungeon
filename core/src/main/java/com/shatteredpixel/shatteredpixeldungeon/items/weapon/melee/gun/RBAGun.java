package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun;



import com.shatteredpixel.shatteredpixeldungeon.actors.hero.RBATalents;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.RBAHero;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBAAlliedEnemyTracker;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBAAvantGardeKunBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.GreaterHaste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBANoticeTracker;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBAShootAllBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBASnipe;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBASwiftMovement;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mari.RBAMariTrinity;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.miyako.RBAWireHook;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.miyu.RBAAntiMaterialRifle;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.miyu.RBAHPBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.nonomi.RBABipod;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.miracles.RBALightBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.miracles.RBAMindBind;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.shops.RBAInfiniteAmmo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.RBAConversionKit;
import com.shatteredpixel.shatteredpixeldungeon.items.RBAGunSmithingTool;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.active.RBAIronHorus;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RBACrossNecklace;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.RBAAR;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.RBAAR_T1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.RBAAR_T2;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.RBAAR_T3;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.RBAAR_T4;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.RBAAR_T5;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.RBAUniqueIdea;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL.RBAGL;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL.RBAGL_T1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL.RBAGL_T2;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL.RBAGL_T3;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL.RBAGL_T4;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL.RBAGL_T5;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.RBAHG;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.RBAHG_T1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.RBAHG_T2;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.RBAHG_T3;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.RBAHG_T4;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.RBAHG_T5;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.RBAMG;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.RBAMG_T1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.RBAMG_T2;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.RBAMG_T3;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.RBAMG_T4;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.RBAMG_T5;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.RBASG;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.RBASG_T1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.RBASG_T2;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.RBASG_T3;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.RBASG_T4;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.RBASG_T5;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.RBASMG;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.RBASMG_T1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.RBASMG_T2;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.RBASMG_T3;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.RBASMG_T4;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.RBASMG_T5;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.RBASR;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.RBASR_SP;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.RBASR_T1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.RBASR_T2;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.RBASR_T3;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.RBASR_T4;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.RBASR_T5;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RBAGun extends MeleeWeapon {
    public static final String AC_SHOOT		= "SHOOT";
    public static final String AC_RELOAD    = "RELOAD";

    protected static final String AC_DETACH       = "DETACH";

    protected int max_round; //최대 장탄수
    protected int round; //현재 장탄수
    protected float reload_time = 2f; //재장전 시간
    protected int shotPerShoot = 1; //발사 당 탄환의 개수
    protected float shootingSpeed = 1f; //발사 시 소모하는 턴의 배율. 낮을수록 빠르다
    protected float shootingAccuracy = 1f; //발사 시 탄환 정확성의 배율. 높을 수록 정확하다.
    protected float adjacentShootingAccuracy = 0.5f; //근거리 발사 시 탄환 정확성의 배율. 높을 수록 정확하다.
    protected boolean explode = false; //탄환 폭발 여부
    protected boolean selfHarm = true; //탄환 폭발의 자가 피해 여부
    protected boolean spread = false; //산탄 여부. 멀리 떨어지면 탄환 위력이 감소한다.
    protected boolean quickUse = false; //장착하지 않고도 사용할 수 있는지 여부
    protected boolean ignoreWall = false; //탄도가 벽을 무시하는지 여부
    public static final String TXT_STATUS = "%d/%d";

    RBAConversionKit kit;

    public boolean isEmpowered = false;

    public enum BarrelMod {
        NORMAL_BARREL(1, 1),
        SHORT_BARREL(1.5f, 0.5f),
        LONG_BARREL(0.75f, 1.25f);

        private final float meleeAccFactor;
        private final float rangedAccFactor;

        BarrelMod(float meleeMulti, float rangedMulti) {
            this.meleeAccFactor = meleeMulti;
            this.rangedAccFactor = rangedMulti;
        }

        public float bulletAccuracyFactor(float accuracy, boolean adjacent) {
            if (adjacent) {
                return accuracy * meleeAccFactor;
            } else {
                return accuracy * rangedAccFactor;
            }
        }
    }

    public enum MagazineMod {
        NORMAL_MAGAZINE(1, 0),
        LARGE_MAGAZINE(1.5f, +1),
        QUICK_MAGAZINE(0.5f, -1);

        private final float magazineFactor;
        private final int reloadTimeFactor;

        MagazineMod(float magMulti, int reloadAdd) {
            this.magazineFactor = magMulti;
            this.reloadTimeFactor = reloadAdd;
        }

        public int magazineFactor(int magazine) {
            return (int)Math.floor(magazine*magazineFactor);
        }
        public float reloadTimeFactor(float time) {
            return time + reloadTimeFactor;
        }
    }

    public enum BulletMod {
        NORMAL_BULLET(1, 1),
        AP_BULLET(0, 0.85f),
        HP_BULLET(2, 1.3f);

        private final float armorMulti;
        private final float dmgMulti;

        BulletMod(float armorMulti, float dmgMulti) {
            this.armorMulti = armorMulti;
            this.dmgMulti = dmgMulti;
        }

        public float armorFactor() {
            return armorMulti;
        }
        public int damageFactor(int damage) {
            return Math.round(damage*dmgMulti);
        }
    }

    public enum WeightMod {
        NORMAL_WEIGHT,
        LIGHT_WEIGHT,
        HEAVY_WEIGHT;
    }

    public enum AttachMod {
        NORMAL_ATTACH,
        LASER_ATTACH,
        FLASH_ATTACH;
    }

    public enum EnchantMod {
        NORMAL_ENCHANT(1, 1),
        AMP_ENCHANT(2, 0.85f),
        SUP_ENCHANT(0.5f, 1.1f);

        private final float enchantMulti;
        private final float dmgMulti;

        EnchantMod(float enchantMulti, float dmgMulti) {
            this.enchantMulti = enchantMulti;
            this.dmgMulti = dmgMulti;
        }

        public float enchantFactor() {
            return enchantMulti;
        }
        public int damageFactor(int damage) {
            return Math.round(damage*dmgMulti);
        }
    }

    public enum InscribeMod {
        NORMAL(0),
        INSCRIBED(1);

        private final int shotBonus;

        InscribeMod(int shotBonus) {
            this.shotBonus = shotBonus;
        }

        public int shotBonus() {
            return shotBonus;
        }
    }

    public BarrelMod barrelMod = BarrelMod.NORMAL_BARREL;
    public MagazineMod magazineMod = MagazineMod.NORMAL_MAGAZINE;
    public BulletMod bulletMod = BulletMod.NORMAL_BULLET;
    public WeightMod weightMod = WeightMod.NORMAL_WEIGHT;
    public AttachMod attachMod = AttachMod.NORMAL_ATTACH;
    public EnchantMod enchantMod = EnchantMod.NORMAL_ENCHANT;
    public InscribeMod inscribeMod = InscribeMod.NORMAL;

    {
        defaultAction = AC_SHOOT;
        usesTargeting = true;

        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.8f;
    }

    private static final String ROUND = "round";
    private static final String MAX_ROUND = "max_round";
    private static final String RELOAD_TIME = "reload_time";
    private static final String SHOT_PER_SHOOT = "shotPerShoot";
    private static final String SHOOTING_SPEED = "shootingSpeed";
    private static final String SHOOTING_ACCURACY = "shootingAccuracy";
    private static final String ADJACENT_SHOOTING_ACCURACY = "adjacentShootingAccuracy";
    private static final String EXPLODE = "explode";
    private static final String SELF_HARM = "selfHarm";
    private static final String SPREAD = "spread";
    private static final String QUICK_USE = "quickUse";
    private static final String IGNORE_WALL = "ignoreWall";
    private static final String BARREL_MOD = "barrelMod";
    private static final String MAGAZINE_MOD = "magazineMod";
    private static final String BULLET_MOD = "bulletMod";
    private static final String WEIGHT_MOD = "weightMod";
    private static final String ATTACH_MOD = "attachMod";
    private static final String ENCHANT_MOD = "enchantMod";
    private static final String INSCRIBE_MOD = "inscribeMod";
    private static final String KIT = "kit";
    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(MAX_ROUND, max_round);
        bundle.put(ROUND, round);
        bundle.put(RELOAD_TIME, reload_time);
        bundle.put(SHOT_PER_SHOOT, shotPerShoot);
        bundle.put(SHOOTING_SPEED, shootingSpeed);
        bundle.put(SHOOTING_ACCURACY, shootingAccuracy);
        bundle.put(ADJACENT_SHOOTING_ACCURACY, adjacentShootingAccuracy);
        bundle.put(EXPLODE, explode);
        bundle.put(SELF_HARM, selfHarm);
        bundle.put(SPREAD, spread);
        bundle.put(QUICK_USE, quickUse);
        bundle.put(IGNORE_WALL, ignoreWall);
        bundle.put(BARREL_MOD, barrelMod);
        bundle.put(MAGAZINE_MOD, magazineMod);
        bundle.put(BULLET_MOD, bulletMod);
        bundle.put(WEIGHT_MOD, weightMod);
        bundle.put(ATTACH_MOD, attachMod);
        bundle.put(ENCHANT_MOD, enchantMod);
        bundle.put(INSCRIBE_MOD, inscribeMod);
        bundle.put(KIT, kit);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        max_round = bundle.getInt(MAX_ROUND);
        round = bundle.getInt(ROUND);
        reload_time = bundle.getFloat(RELOAD_TIME);
        shotPerShoot = bundle.getInt(SHOT_PER_SHOOT);
        shootingSpeed = bundle.getFloat(SHOOTING_SPEED);
        shootingAccuracy = bundle.getFloat(SHOOTING_ACCURACY);
        adjacentShootingAccuracy = bundle.getFloat(ADJACENT_SHOOTING_ACCURACY);
        explode = bundle.getBoolean(EXPLODE);
        selfHarm = bundle.getBoolean(SELF_HARM);
        spread = bundle.getBoolean(SPREAD);
        quickUse = bundle.getBoolean(QUICK_USE);
        ignoreWall = bundle.getBoolean(IGNORE_WALL);
        barrelMod = bundle.getEnum(BARREL_MOD, BarrelMod.class);
        magazineMod = bundle.getEnum(MAGAZINE_MOD, MagazineMod.class);
        bulletMod = bundle.getEnum(BULLET_MOD, BulletMod.class);
        weightMod = bundle.getEnum(WEIGHT_MOD, WeightMod.class);
        attachMod = bundle.getEnum(ATTACH_MOD, AttachMod.class);
        enchantMod = bundle.getEnum(ENCHANT_MOD, EnchantMod.class);
        inscribeMod = bundle.getEnum(INSCRIBE_MOD, InscribeMod.class);
        kit = (RBAConversionKit) bundle.get(KIT);
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (isEquipped( hero ) || quickUse) {
            actions.add(AC_SHOOT);
            actions.add(AC_RELOAD);
        }
        if (hero.buff(RBAShootAllBuff.OverHeat.class) != null) {
            actions.remove(AC_SHOOT);
            actions.remove(AC_EQUIP);
            if (isEquipped(hero)) {
                actions.remove(AC_UNEQUIP);
                actions.remove(AC_DROP);
                actions.remove(AC_THROW);
                actions.remove(AC_RELOAD);
            }
        }
        if (kit != null) actions.add(AC_DETACH);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

        if (action.equals(AC_SHOOT)) {
            if (hero.buff(RBAShootAllBuff.OverHeat.class) != null) {
                usesTargeting = false;
                if (round <= 0) {
                    GLog.w(Messages.get(this, "overheat_reload"));
                } else {
                    GLog.w(Messages.get(this, "overheat"));
                }
                return;
            }
            if (!isEquipped( hero ) && !quickUse) {
                usesTargeting = false;
                GLog.w(Messages.get(this, "not_equipped"));
            } else {
                if (round <= 0) { //현재 탄창이 0이면 AC_RELOAD 버튼을 눌렀을 때처럼 작동
                    execute(hero, AC_RELOAD);
                } else {
                    usesTargeting = true;
                    curUser = hero;
                    curItem = this;
                    GameScene.selectCell(shooter);
                }
            }
        }
        if (action.equals(AC_RELOAD)) {
            if (isAllLoaded()){
                if (hero.heroClass == HeroClass.RBA_HOSHINO && round == maxRound()) {
                    onReload();
                    manualReload(1, true);

                    hero.busy();
                    hero.sprite.operate(hero.pos);
                    Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
                    hero.spendAndNext(reloadTime());
                } else {
                    GLog.w(Messages.get(this, "already_loaded"));
                }
            } else {
                reload();
            }
        }
        if (action.equals(AC_DETACH) && kit != null){
            RBAConversionKit detaching = detachKit();
            GLog.i( Messages.get(RBAGun.class, "detach_kit") );
            hero.sprite.operate(hero.pos);
            if (!detaching.collect()){
                Dungeon.level.drop(detaching, hero.pos);
            }
            updateQuickslot();
        }
    }

    @Override
    public boolean doEquip(Hero hero) {
        RBAIronHorus.detachBuff(hero);
        if (hero.buff(RBAAntiMaterialRifle.GotRifleTracker.class) != null) {
            Item sr = hero.belongings.getItem(RBASR_SP.class);
            if (sr != null) {
                int slot = Dungeon.quickslot.getSlot(sr);
                Buff.affect(hero, RBAAntiMaterialRifle.QuickSlotSet.class).set(this, slot);
            } else {
                hero.buff(RBAAntiMaterialRifle.GotRifleTracker.class).detach();
            }
        }
        return super.doEquip(hero);
    }

    public boolean isAllLoaded() {
        return round >= maxRound();
    }

    @Override
    protected int baseChargeUse(Hero hero, Char target){
        return 2;
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {

    }

    public void reload() {
        if (hero.heroClass == HeroClass.RBA_MARI) {
            RBACrossNecklace necklace = hero.belongings.getItem(RBACrossNecklace.class);
            if (necklace == null || (!necklace.isEquipped(hero) && !hero.hasTalent(Talent.RBA_MARI_T3_2))) {
                RBAHero.yellW(hero, "no_necklace");
                return;
            }
            int missing = maxRound()-round();
            int bulletsToLoad = necklace.bulletsCanLoad(missing);
            if (bulletsToLoad == 0) {
                RBAHero.yellW(hero, "no_charge");
                return;
            }
            necklace.spendCharge(bulletsToLoad);
            manualReload(bulletsToLoad, false);
        } else {
            quickReload();
        }

        onReload();

        hero.busy();
        hero.sprite.operate(hero.pos);
        Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
        hero.spendAndNext(reloadTime());
        GLog.i(Messages.get(this, "reload"));
    }

    public void onReload() {	//재장전 시 작동하는 메서드. 특히 현재 장탄수가 바뀌기 전에 동작해야 한다
        if (hero.hasTalent(Talent.RBA_NONOMI_T1_4)) {
            Buff.affect(hero, Barrier.class).setShield((int)reloadTime() + Math.max(0, hero.pointsInTalent(Talent.RBA_NONOMI_T1_4)-1)); //reload time + 0 or 1, depends on talent level
        }

        RBAIronHorus.onReload(hero);

        if (hero.subClass == HeroSubClass.RBA_SWIFT_MOVEMENT) {
            Buff.affect(hero, RBASwiftMovement.class);
        }

        if (round == 0 && hero.hasTalent(Talent.RBA_HOSHINO_T1_4)) {
            Buff.affect(hero, Barrier.class).setShield(1+2*hero.pointsInTalent(Talent.RBA_HOSHINO_T1_4));
        }

        if (round == 0 && hero.hasTalent(Talent.RBA_NOA_T2_4)) {
            Buff.affect(hero, GreaterHaste.class).set(hero.pointsInTalent(Talent.RBA_NOA_T2_4));
        }

        if (hero.hasTalent(Talent.RBA_NOA_EX1_1) && hero.belongings.secondWep() instanceof RBAGun) {
            ((RBAGun)hero.belongings.secondWep()).manualReload(hero.pointsInTalent(Talent.RBA_NOA_EX1_1), false);
        }

        if (hero.buff(RBALightBullet.LightMagazine.class) != null) {
            hero.buff(RBALightBullet.LightMagazine.class).detach();
        }
    }

    public void quickReload() {	//다른 것들을 작동시키지 않고 탄창만 완전히 재장전하는 메서드
        round = maxRound();
        updateQuickslot();
    }

    public void manualReload() {	//탄환을 1발 장전하는 메서드
        manualReload(1, false);
    }

    public void manualReload(int amount, boolean overReload) {	//탄환을 지정한 수만큼 장전하는 메서드. overReload가 true일 경우 최대 장탄수를 넘어서 장전할 수 있다.
        round += amount;
        if (overReload) {
            if (round > maxRound() + amount) { //최대 장탄수를 넘을 수는 있지만, 중첩은 불가
                round = maxRound() + amount;
            }
        } else {
            if (round > maxRound()) {
                round = maxRound();
            }
        }

        updateQuickslot();
    }

    public boolean isReloaded() {
        return round >= maxRound();
    }

    public int shotPerShoot() { //발사 당 탄환의 수
        return shotPerShoot + inscribeMod.shotBonus();
    }

    public int maxRound() { //최대 장탄수
        int amount = max_round;

        amount = this.magazineMod.magazineFactor(amount);

        if (hero != null && hero.hasTalent(Talent.RBA_NONOMI_EX1_1)) {
            amount += hero.pointsInTalent(Talent.RBA_NONOMI_EX1_1);
        }

        if (kit != null) amount += 1;

        if (hero != null && hero.hasTalent(Talent.RBA_NOA_T3_2)) {
            amount += hero.pointsInTalent(Talent.RBA_NOA_T3_2);
        }

        return amount;
    }

    public int round() {
        return round;
    }

    public void useRound() {
        if (hero.buff(RBAInfiniteAmmo.InfiniteAmmoBuff.class) == null) {
            round--;
        }
        if (hero.hasTalent(Talent.RBA_NOA_T1_4) && round == 0) {
            Buff.affect(hero, Barrier.class).setShield(1+hero.pointsInTalent(Talent.RBA_NOA_T1_4));
        }
        updateQuickslot();
    }

    public float reloadTime() { //재장전에 소모하는 턴
        float amount = reload_time;

        amount = this.magazineMod.reloadTimeFactor(amount);

        if (hero != null && hero.pointsInTalent(Talent.RBA_MIYU_EX2_3) >= 2) {
            int t = Dungeon.level.map[hero.pos];
            if (t == Terrain.FURROWED_GRASS || t == Terrain.HIGH_GRASS) {
                amount -= 2;
            }
            amount = Math.max(1, amount);
        }

        if (hero != null && hero.hasTalent(Talent.RBA_NONOMI_EX1_1)) {
            amount += 1;
        }

        if (kit != null) {
            amount -= 1;
        }

        if (hero != null && hero.belongings.secondWep() == this) {
            amount += 1;
        }

        amount = Math.max(0, amount);
        return amount;
    }

    public int bulletUse() {
        return Math.max(0, (maxRound()-round)*shotPerShoot());
    }

    @Override
    public int tier() {
        int t = this.tier;
        switch (this.weightMod) {
            case NORMAL_WEIGHT: default:
                break;
            case HEAVY_WEIGHT:
                t++;
                break;
            case LIGHT_WEIGHT:
                t--;
                break;
        }
        return t;
    }

    @Override
    public int STRReq(int lvl) {
        int req = STRReq(tier(), lvl);
        if (masteryPotionBonus){
            req -= 2;
        }
        return req;
    }

    @Override
    public int min(int lvl) {
        if (hero != null && hero.heroClass == HeroClass.RBA_MIKA && isEquipped(hero)) {
            return Math.max(0, hero.STR()-10);
        }

        int damage = super.min(lvl);

        if (hero != null && hero.hasTalent(Talent.RBA_NOA_T1_1)) {
            if (kit != null) damage += hero.pointsInTalent(Talent.RBA_NOA_T1_1);
        }

        return damage;
    }

    @Override
    public int max(int lvl) {
        if (hero != null && hero.heroClass == HeroClass.RBA_MIKA && isEquipped(hero)) {
            return 2*(hero.STR()-10+hero.lvl)+10;
        }

        int damage;
        int talentBonus = 0;

        if (hero != null && hero.hasTalent(Talent.RBA_NOA_T1_1)) {
            if (kit != null) talentBonus += 2;
        }

        if (hero != null) {
            damage = 3*(tier()+1) +
                    lvl*(tier()+1) +
                    talentBonus; //근접 무기로서의 최대 데미지
        } else {
            damage = 3*(tier()+1) +
                    lvl*(tier()+1);
        }
        return damage;

    }

    protected int baseBulletMin(int lvl) {
        return tier() + lvl;
    }

    public int bulletMin(int lvl) {
        if (hero != null) {
            int damage = baseBulletMin(lvl) + RingOfSharpshooting.levelDamageBonus(hero);

            if (hero.buff(RBATalents.IntimidateBonusDamageBuff.class) != null) {
                damage += hero.buff(RBATalents.IntimidateBonusDamageBuff.class).dmgBonus();
            }

            return damage;
        } else {
            return baseBulletMin(lvl);
        }

    }

    public int bulletMin() {
        return bulletMin(this.buffedLvl());
    }

    //need to be overridden
    protected int baseBulletMax(int lvl) {
        return 0;
    }

    public int bulletMax(int lvl) {
        if (hero != null) {
            int damage = baseBulletMax(lvl+RingOfSharpshooting.levelDamageBonus(hero));

            if (hero.buff(RBATalents.IntimidateBonusDamageBuff.class) != null) {
                damage += hero.buff(RBATalents.IntimidateBonusDamageBuff.class).dmgBonus();
            }

            return damage;
        } else {
            return baseBulletMax(lvl);
        }
    }

    public int bulletMax() {
        if (hero != null) {
            return bulletMax(this.buffedLvl());
        }
        return bulletMax(this.buffedLvl());
    }

    public int bulletDamage() {
        int damage = Hero.heroDamageIntRange(augment.damageFactor(bulletMin()), augment.damageFactor(bulletMax()));
//        GLog.i("탄환 피해량: "+augment.damageFactor(bulletMin())+"~"+augment.damageFactor(bulletMax()));
//        GLog.i("\n피해량 결과: "+damage);
//        GLog.i("\n");

        if (hero.hasTalent(Talent.RBA_NONOMI_T1_3)) {
            if (hero.pointsInTalent(Talent.RBA_NONOMI_T1_3) == 2) {
                damage += 1; //adds 1
            } else {
                damage += Random.Int(2); //adds 0~1
            }
        }

        return damage;
    }

    @Override
    protected float baseDelay(Char owner) {
        return super.baseDelay(owner);
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        //다중 타격 총기가 아군이 된 적에게 추가로 피해를 입히는 경우를 방지
        //적이 아군이 아닐 때 버프 부여
        if (defender.alignment == Char.Alignment.ENEMY && defender.buff(RBAAlliedEnemyTracker.class) == null) {
            Buff.affect(defender, RBAAlliedEnemyTracker.class);
        }
        //적이 아군인데 앞서 부여한 버프가 있다면 이전에 적이었던 것이 되므로 피해를 0으로 만들고 명중 시 효과를 발동시키지 않음
        if (defender.alignment == Char.Alignment.ALLY && defender.buff(RBAAlliedEnemyTracker.class) != null) {
            return 0;
        }

        if (this.attachMod == AttachMod.FLASH_ATTACH) {
            if (Random.Int(10) > 5+Dungeon.level.distance(attacker.pos, defender.pos)-1) {
                Buff.prolong(defender, Blindness.class, 2f);
            }
        }

        return super.proc(attacker, defender, damage);
    }


    @Override
    public String info() {
        String info = super.info();
        //근접 무기의 설명을 모두 가져옴, 여기에서 할 것은 근접 무기의 설명에 추가로 생기는 문장을 더하는 것
        if (levelKnown) { //감정되어 있을 때
            info += "\n\n" + Messages.get(RBAGun.class, "gun_desc",
                    shotPerShoot(), augment.damageFactor(bulletMin(buffedLvl())), augment.damageFactor(bulletMax(buffedLvl())), round, maxRound(), new DecimalFormat("#.##").format(reloadTime()));
        } else { //감정되어 있지 않을 때
            info += "\n\n" + Messages.get(RBAGun.class, "gun_typical_desc",
                    shotPerShoot(), augment.damageFactor(bulletMin(0)), augment.damageFactor(bulletMax(0)), round, maxRound(), new DecimalFormat("#.##").format(reloadTime()));
        }
        //DecimalFormat("#.##")은 .format()에 들어가는 매개변수(실수)를 "#.##"형식으로 표시하는데 사용된다.
        //가령 5.55555가 .format()안에 들어가서 .format(5.55555)라면, new DecimalFormat("#.##").format(5.55555)는 5.55라는 String 타입의 값을 반환한다.

        boolean isModded = false;
        boolean[] whatModded = {false, false, false, false, false, false, false};

        if (barrelMod != BarrelMod.NORMAL_BARREL) {
            whatModded[0] = true;
            isModded = true;
        }
        if (magazineMod != MagazineMod.NORMAL_MAGAZINE) {
            whatModded[1] = true;
            isModded = true;
        }
        if (bulletMod != BulletMod.NORMAL_BULLET) {
            whatModded[2] = true;
            isModded = true;
        }
        if (weightMod != WeightMod.NORMAL_WEIGHT) {
            whatModded[3] = true;
            isModded = true;
        }
        if (attachMod != AttachMod.NORMAL_ATTACH) {
            whatModded[4] = true;
            isModded = true;
        }
        if (enchantMod != EnchantMod.NORMAL_ENCHANT) {
            whatModded[5] = true;
            isModded = true;
        }
        if (inscribeMod != InscribeMod.NORMAL) {
            whatModded[6] = true;
            isModded = true;
        }

        if (isModded) {
            info += "\n\n" + Messages.get(this, "modded_start");
            if (whatModded[0]) {
                info += Messages.get(RBAGunSmithingTool.WndMod.class, barrelMod.name());
                if (whatModded[1] || whatModded[2] || whatModded[3] || whatModded[4] || whatModded[5]) { //이 개조 이후에 추가로 개조된 것이 있는지를 확인
                    info += ", ";
                }
            }
            if (whatModded[1]) {
                info += Messages.get(RBAGunSmithingTool.WndMod.class, magazineMod.name());
                if (whatModded[2] || whatModded[3] || whatModded[4] || whatModded[5] || whatModded[6]) { //이 개조 이후에 추가로 개조된 것이 있는지를 확인
                    info += ", ";
                }
            }
            if (whatModded[2]) {
                info += Messages.get(RBAGunSmithingTool.WndMod.class, bulletMod.name());
                if (whatModded[3] || whatModded[4] || whatModded[5] || whatModded[6]) { //이 개조 이후에 추가로 개조된 것이 있는지를 확인
                    info += ", ";
                }
            }
            if (whatModded[3]) {
                info += Messages.get(RBAGunSmithingTool.WndMod.class, weightMod.name());
                if (whatModded[4] || whatModded[5] || whatModded[6]) { //이 개조 이후에 추가로 개조된 것이 있는지를 확인
                    info += ", ";
                }
            }
            if (whatModded[4]) {
                info += Messages.get(RBAGunSmithingTool.WndMod.class, attachMod.name());
                if (whatModded[5] || whatModded[6]) { //이 개조 이후에 추가로 개조된 것이 있는지를 확인
                    info += ", ";
                }
            }
            if (whatModded[5]) {
                info += Messages.get(RBAGunSmithingTool.WndMod.class, enchantMod.name());
                if (whatModded[6]) { //이 개조 이후에 추가로 개조된 것이 있는지를 확인
                    info += ", ";
                }
            }
            if (whatModded[6]) {
                info += Messages.get(RBAGun.class, inscribeMod.name());
            }
            info += Messages.get(this, "modded_end");
        }

        if (kit != null) {
            info += "\n\n" + Messages.get(RBAGun.class, "kit_attached");
        }

        return info;
    }

    @Override
    public String status() { //아이템 칸 오른쪽 위에 나타내는 글자
        return Messages.format(TXT_STATUS, round, maxRound()); //TXT_STATUS 형식(%d/%d)으로, round, maxRound() 변수를 순서대로 %d부분에 출력
    }

    @Override
    public int targetingPos(Hero user, int dst) {
        return knockBullet().targetingPos(user, dst);
    }

    //needs to be overridden
    public Bullet knockBullet(){
        return new Bullet();
    }

    public void affixKit(RBAConversionKit kit){
        this.kit = kit;
        if (kit.level() > 0){
            //doesn't trigger upgrading logic such as affecting curses/glyphs
            int newLevel = trueLevel()+1;
            level(newLevel);
            Badges.validateItemLevelAquired(this);
        }
    }

    public RBAConversionKit detachKit(){
        if (kit != null){

            RBAConversionKit detaching = kit;
            kit = null;

            if (detaching.level() > 0){
                degrade();
            }

            if (round() > maxRound()) quickReload();

            return detaching;
        } else {
            return null;
        }
    }

    public RBAConversionKit checkKit(){
        return kit;
    }

    @Override
    public Emitter emitter() {
        if (kit == null) return super.emitter();
        Emitter emitter = new Emitter();
        emitter.pos(ItemSpriteSheet.film.width(image)/2f + 2f, ItemSpriteSheet.film.height(image)/3f);
        emitter.fillTarget = false;
        emitter.pour(Speck.factory( Speck.BLUE_LIGHT ), 0.6f);
        return emitter;
    }

    @Override
    public Item upgrade() {
        if (kit != null && kit.level() == 0)
            kit.upgrade();
        return super.upgrade();
    }

    private static boolean evaluatingTwinUpgrades = false;
    @Override
    public int buffedLvl() {
        int level = super.buffedLvl();
        if (!evaluatingTwinUpgrades && hero != null && isEquipped(hero) && hero.hasTalent(Talent.RBA_NOA_EX1_2)){
            KindOfWeapon other = null;
            if (hero.belongings.weapon() != this) other = hero.belongings.weapon();
            if (hero.belongings.secondWep() != this) other = hero.belongings.secondWep();

            if (other instanceof RBAGun) {
                evaluatingTwinUpgrades = true;
                int otherLevel = other.buffedLvl();
                evaluatingTwinUpgrades = false;

                //weaker weapon needs to be 2/1/0 tiers lower, based on talent level
                if ((tier() + (3 - hero.pointsInTalent(Talent.RBA_NOA_EX1_2))) <= ((RBAGun) other).tier()
                        && otherLevel > super.buffedLvl()) {
                    return otherLevel;
                }

            }
        }
        if (hero != null && hero.buff(RBANoticeTracker.class) != null && !RBANoticeTracker.isNoticed() && this.isEquipped(hero)) {
            level += hero.pointsInTalent(Talent.RBA_MIYU_T3_2);
        }
        if (hero != null && hero.buff(RBAAvantGardeKunBuff.GunUpgradeBuff.class) != null) {
            level = Math.min(level+1+hero.pointsInTalent(Talent.RBA_YUZU_EX1_2), 3+hero.pointsInTalent(Talent.RBA_YUZU_EX1_2));
        }

        return level;
    }

    public class Bullet extends MissileWeapon {

        private boolean specialShot = false;
        private boolean snipeShot = false;

        {
            image = ItemSpriteSheet.RBA_SINGLE_BULLET;
            hitSound = Assets.Sounds.PUFF;
            tier = RBAGun.this.tier();
        }

        public BulletMod whatBullet() { //현재 탄환이 어떤 개조인지를 반환함. 탄환 피해의 적 방어력 적용량 결정에 쓰임
            return RBAGun.this.bulletMod;
        }

        public EnchantMod whatEnchant() {
            return RBAGun.this.enchantMod;
        }

        private float accMulti = 1f;
        private boolean ignoreWall = false;

        public void setAccMulti(float multi) {
            accMulti = multi;
        }

        public void setIgnoreWall(boolean isIgnore) {
            ignoreWall = isIgnore;
        }

        public void setSpecialShot(boolean isSpecial) {
            this.specialShot = isSpecial;
        }

        public boolean isSpecialShot() {
            return this.specialShot;
        }

        public void setSnipeShot(boolean isSnipe) {
            this.snipeShot = isSnipe;
        }

        public boolean isSnipeShot() {
            return this.snipeShot;
        }

        @Override
        public int proc(Char attacker, Char defender, int damage) {
            boolean isDebuffed = false;
            for (Buff buff : defender.buffs()) {
                if (buff.type == Buff.buffType.NEGATIVE) {
                    isDebuffed = true;
                    break;
                }
            }

            damage = this.whatBullet().damageFactor(damage);
            damage = this.whatEnchant().damageFactor(damage);

            int distance = Dungeon.level.distance(attacker.pos, defender.pos) - 1; //적과 나 사이의 간격, 근접한 경우 0
            float multiplier = 1;
            if (spread) {
                multiplier *= (float) Math.pow(0.9f, distance);
            }
            if (isSpecialShot()) {
                multiplier *= 0.67f;
            }
            damage = Math.round(damage * multiplier);

            damage += bulletDamageBonus(attacker, defender);

            if (attacker instanceof Hero) {
                Hero hero = (Hero) attacker;
                if (hero.hasTalent(Talent.RBA_HOSHINO_T2_5) && hero.buff(Barrier.class) == null) {
                    int shield = 2+3*hero.pointsInTalent(Talent.RBA_HOSHINO_T2_5)-distance;
                    if (shield > 0) {
                        Buff.affect(hero, Barrier.class).setShield(shield);
                    }
                }

                if (hero.hasTalent(Talent.RBA_HOSHINO_T3_1)) {
                    Buff.affect(defender, RBATalents.TacticalSuppressTracker.class);
                }

                if (hero.heroClass == HeroClass.RBA_SHIROKO) {
                    if (Random.Float() < 1/(float)shotPerShoot()) {
                        defender.damage((int)Math.ceil(Dungeon.scalingDepth()/5f) + hero.pointsInTalent(Talent.RBA_SHIROKO_T1_3), this);
                    }
                }

                if (hero.hasTalent(Talent.RBA_SHIROKO_T3_2)) {
                    int dur = 10*hero.pointsInTalent(Talent.RBA_SHIROKO_T3_2);
                    Buff.append(hero, TalismanOfForesight.CharAwareness.class, dur).charID = defender.id();
                }

                if (hero.hasTalent(Talent.RBA_NOA_T3_1) && hero.buff(RBATalents.PerfectPrecisionTracker.class) == null) {
                    Buff.affect(hero, RBATalents.PerfectPrecisionTracker.class, hero.cooldown()+4f);
                }

                if (hero.hasTalent(Talent.RBA_MIKA_T2_5) && !defender.isImmune(Charm.class) && defender.buff(RBATalents.CharmTracker.class) == null) {
                    new FlavourBuff() {
                        {
                            actPriority = VFX_PRIO;
                        }

                        public boolean act() {
                            Charm charm = Buff.affect(defender, Charm.class, 5f*hero.pointsInTalent(Talent.RBA_MIKA_T2_5));
                            charm.object = curUser.id();
                            charm.ignoreHeroAllies = true;
                            defender.sprite.centerEmitter().start( Speck.factory( Speck.HEART ), 0.2f, 3 );
                            return super.act();
                        }
                    }.attachTo(defender);
                    Buff.affect(defender, RBATalents.CharmTracker.class);
                }

                if (hero.buff(RBALightBullet.LightMagazine.class) != null) {
                    damage = hero.buff(RBALightBullet.LightMagazine.class).proc(hero, defender, damage);
                }
            }

            return RBAGun.this.proc(attacker, defender, damage);
        }

        public float projectileSpeed() {
            return 3f;
        }

        @Override
        public Item split(int amount) {
            return this;
        }

        @Override
        public Item merge(Item other) {
            return this;
        }

        private int bulletDamageBonus(Char attacker, Char defender) { //탄환 피해의 순수 증가량. 탄환 피해 배율 적용 이후에 적용됨
            int bonus = 0;

            if (hero.hasTalent(Talent.RBA_NONOMI_T2_4) && defender instanceof Mob && ((Mob) defender).surprisedBy(attacker)) {
                bonus += hero.pointsInTalent(Talent.RBA_NONOMI_T2_4);
            }

            return bonus;
        }

        @Override
        public int buffedLvl(){
            return RBAGun.this.buffedLvl();
        }

        @Override
        public int damageRoll(Char owner) {
            int damage = bulletDamage();
            if (owner instanceof Hero && ((Hero)owner).hasTalent(Talent.RBA_MIYU_T3_1)) {
                Hero hero = (Hero)owner;
                Char enemy = hero.attackTarget();
                if (enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)) {
                    //deals 25/33/50% toward max to max on surprise, instead of min to max.
                    int diff = max() - min();
                    damage = augment.damageFactor(Hero.heroDamageIntRange(
                            min() + Math.round(diff/(float)(5-((Hero)owner).pointsInTalent(Talent.RBA_MIYU_T3_1))),
                            max()));
                    int exStr = hero.STR() - STRReq();
                    if (exStr > 0) {
                        damage += Hero.heroDamageIntRange(0, exStr);
                    }
                }
            }
            if (isSnipeShot()) {
                Char enemy = hero.attackTarget();
                switch ((Dungeon.level.distance(owner.pos, enemy.pos)-1) / 3 + 1) {
                    case 1:
                        damage = Math.round(damage * 1.1f);
                        break;
                    case 2:
                        damage = Math.round(damage * 1.2f);
                        break;
                    case 3:
                        damage = Math.round(damage * 1.35f);
                        break;
                    case 4: default:
                        damage = Math.round(damage * 1.5f);
                        break;
                }
            }
            return damage;
        }

        @Override
        public boolean hasEnchant(Class<? extends Enchantment> type, Char owner) {
            return RBAGun.this.hasEnchant(type, owner);
        }

        @Override
        public float delayFactor(Char user) {
            if (user.buff(RBABipod.BipodBuff.class) != null) {
                return 0;
            }

            if (specialShot) return 0;

            float speed = RBAGun.this.delayFactor(user) * shootingSpeed;

            return speed;
        }

        @Override
        public float castDelay(Char user, int cell) {
            return delayFactor(user);
        }

        @Override
        public float accuracyFactor(Char owner, Char target) {
            if (isSnipeShot()) return Char.INFINITE_ACCURACY;

            float ACC = super.accuracyFactor(owner, target);
            ACC *= shootingAccuracy * accMulti;
            if (RBAGun.this.attachMod == AttachMod.LASER_ATTACH) {
                ACC *= 1.25f;
            }
            if (owner instanceof Hero && owner.buff(RBABipod.BipodBuff.class) != null) {
                ACC *= RBABipod.BipodBuff.bulletAccMultiplier();
            }
            ACC = RBAGun.this.barrelMod.bulletAccuracyFactor(ACC, Dungeon.level.adjacent(owner.pos, target.pos));
            return ACC;
        }

        @Override
        protected float adjacentAccFactor(Char owner, Char target) {
            float ACC;
            if (Dungeon.level.adjacent( owner.pos, target.pos )) {
                ACC = adjacentShootingAccuracy;

                if (owner instanceof Hero){
                    Hero hero = (Hero) owner;
                    if (hero.buff(RBAWireHook.PointBlankShot.class) != null) {
                        ACC *= 3f;
                    }
                    if (hero.hasTalent(Talent.RBA_HOSHINO_T3_2)) {
                        ACC *= 1+0.5f*((Hero)owner).pointsInTalent(Talent.RBA_HOSHINO_T3_2);
                    }
                    if (hero.pointsInTalent(Talent.RBA_NOA_EX1_3) >= 2 && isSpecialShot()) {
                        return Char.INFINITE_ACCURACY;
                    }
                }
            } else {
                ACC = 1;
            }

            return ACC;
        }

        @Override
        public int STRReq(int lvl) {
            return RBAGun.this.STRReq();
        }

        @Override
        protected void onThrow( int cell ) {
            shoot(cell, true);
        }

        public void shoot( int cell, boolean useRound ) {
            curUser = hero;
            boolean killedEnemy = false;
            boolean shootAll = hero.buff(RBAShootAllBuff.class) != null && hero.buff(RBAShootAllBuff.class).shootAll();
            do {
                if (explode) {
                    killedEnemy = explosiveShot(cell);
                } else {
                    killedEnemy = oneShot(cell);
                }

                onShoot(shootAll, useRound);
            } while (shootAll && round() > 0);
        }

        public void onShoot(boolean shootAll, boolean useRound) {
            if (useRound) {
                useRound();
            }

            boolean willAggroEnemy = true; //어그로를 끌지 않는 경우에 false

            if (hero.hasTalent(Talent.RBA_MIYU_T2_5) && Random.Float() < 0.5f*hero.pointsInTalent(Talent.RBA_MIYU_T2_5)) {
                willAggroEnemy = false;
            }

            if (willAggroEnemy) {
                aggro();
            }

            if (shootAll) {
                Buff.affect(hero, RBAShootAllBuff.OverHeat.class).add(1);
            }

            if (hero.buff(RBATalents.IntimidateBonusDamageBuff.class) != null) {
                hero.buff(RBATalents.IntimidateBonusDamageBuff.class).use();
            }

            if (hero.buff(RBATalents.PerfectPrecisionTracker.class) != null) {
                hero.buff(RBATalents.PerfectPrecisionTracker.class).detach();
            }

            if (hero.buff(RBALightBullet.LightMagazine.class) != null) {
                hero.buff(RBALightBullet.LightMagazine.class).onShoot(hero);
            }
        }

        private boolean oneShot(int cell) {
            boolean killedEnemy = false;
            Char enemy = Actor.findChar( cell );
            for (int i = 0; i < shotPerShoot(); i++) { //데미지 입히는 것과 발사 시 주변에서 나는 연기를 shotPerShoot만큼 반복
                if (enemy == null || enemy == curUser) {
                    parent = null;
                    CellEmitter.get(cell).burst(SmokeParticle.FACTORY, 2);
                    CellEmitter.center(cell).burst(BlastParticle.FACTORY, 2);
                } else {
                    if (curUser.buff(RBAHPBullet.HPBulletBuff.class) != null) {
                        if (!curUser.buff(RBAHPBullet.HPBulletBuff.class).proc(enemy, damageRoll(curUser))) {
                            if (!curUser.shoot( enemy, this )) {
                                CellEmitter.get(cell).burst(SmokeParticle.FACTORY, 2);
                                CellEmitter.center(cell).burst(BlastParticle.FACTORY, 2);
                            }
                        }
                    } else {
                        if (!curUser.shoot( enemy, this )) {
                            CellEmitter.get(cell).burst(SmokeParticle.FACTORY, 2);
                            CellEmitter.center(cell).burst(BlastParticle.FACTORY, 2);
                            if (curUser.hasTalent(Talent.RBA_YUZU_T1_4) && Dungeon.level.adjacent(curUser.pos, enemy.pos) && hero.buff(RBATalents.SafetyTriggerCooldown.class) == null) {
                                Buff.affect(hero, Barrier.class).setShield(2+3*curUser.pointsInTalent(Talent.RBA_YUZU_T1_4));
                                Buff.affect(hero, RBATalents.SafetyTriggerCooldown.class, RBATalents.SafetyTriggerCooldown.DURATION);
                            }
                        }
                    }
                }
            }
            if (enemy != null && !enemy.isAlive()) {
                killedEnemy = true;
            }
            return killedEnemy;
        }

        private boolean explosiveShot(int cell) {
            boolean killedEnemy = false;
            Char chInPos = Actor.findChar(cell);
            ArrayList<Char> targets = new ArrayList<>();
            int[] shootArea = PathFinder.NEIGHBOURS9;

            for (int i : shootArea){
                int c = cell + i;
                if (c >= 0 && c < Dungeon.level.length()) {
                    if (Dungeon.level.heroFOV[c]) {
                        CellEmitter.get(c).burst(SmokeParticle.FACTORY, 4);
                        CellEmitter.center(cell).burst(BlastParticle.FACTORY, 4);
                    }
                    if (Dungeon.level.flamable[c]) {
                        Dungeon.level.destroy(c);
                        GameScene.updateMap(c);
                    }
                    Char ch = Actor.findChar(c);
                    if (ch != null && !targets.contains(ch)) {
                        if (!(ch instanceof Hero && !selfHarm)) {
                            targets.add(ch);
                        }
                    }
                }
            }

            for (Char target : targets){
                for (int i = 0; i < shotPerShoot(); i++) {
                    if (curUser.buff(RBAHPBullet.HPBulletBuff.class) != null) {
                        if (!curUser.buff(RBAHPBullet.HPBulletBuff.class).proc(target, damageRoll(curUser))) curUser.shoot(target, this);
                    } else {
                        if (!curUser.shoot(target, this) || target instanceof Hero) {
                            if (curUser.hasTalent(Talent.RBA_YUZU_T1_4) && Dungeon.level.distance(curUser.pos, target.pos) <= 1 && hero.buff(RBATalents.SafetyTriggerCooldown.class) == null) {
                                Buff.affect(hero, Barrier.class).setShield(2+3*curUser.pointsInTalent(Talent.RBA_YUZU_T1_4));
                                Buff.affect(hero, RBATalents.SafetyTriggerCooldown.class, RBATalents.SafetyTriggerCooldown.DURATION);
                            }
                        }
                    }
                }
                if (!target.isAlive()) {
                    killedEnemy = true;
                }
                if (target == curUser && !target.isAlive()) {
                    Dungeon.fail(getClass());
                    Badges.validateDeathFromFriendlyMagic();
                    GLog.n(Messages.get(RBAGun.class, "ondeath"));
                }
            }

            Sample.INSTANCE.play( Assets.Sounds.BLAST );

            return killedEnemy;
        }

        private void aggro() { //주변의 적들을 학생의 위치로 모이게 하는 구문
            for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
                if (mob.paralysed <= 0
                        && Dungeon.level.distance(curUser.pos, mob.pos) <= 4
                        && mob.state != mob.HUNTING) {
                    mob.beckon( curUser.pos );
                }
            }
        }

        @Override
        public int throwPos(Hero user, int dst) {
            if (ignoreWall || RBAGun.this.ignoreWall) {
                return dst;
            } else {
                return super.throwPos(user, dst);
            }
        }

        @Override
        public void throwSound() {
            Sample.INSTANCE.play( Assets.Sounds.HIT_CRUSH, 1, Random.Float(0.33f, 0.66f) );
        }

        @Override
        public void cast(final Hero user, final int dst) {
            super.cast(user, dst);
        }
    }

    private CellSelector.Listener shooter = new CellSelector.Listener() {
        @Override
        public void onSelect( Integer target ) {
            if (target != null) {
                if (target == curUser.pos) {
                    execute(hero, AC_RELOAD);
                } else {
                    if (curUser.buff(RBASnipe.ScopedArea.class) != null && curUser.buff(RBASnipe.ScopedArea.class).posInArea(target) && Actor.findChar(target) != null) {
                        if (curUser.buff(RBASnipe.ScopedArea.class).posInArea(target)) {
                            curUser.buff(RBASnipe.ScopedArea.class).snipe(target, Actor.findChar(target), knockBullet());
                        }
                    } else {
                        Bullet bullet = knockBullet();

                        if (curUser.buff(RBAMindBind.MindBindBuff.class) != null && curUser.buff(RBAMindBind.MindBindBuff.class).aimingTarget(target)) {
                            bullet.setIgnoreWall(true);
                        }

                        if (curUser.buff(RBAMariTrinity.TrinityBuff.class) != null) {
                            bullet.setIgnoreWall(true);
                        }

                        bullet.cast(curUser, target);
                    }
                }
            }
        }
        @Override
        public String prompt() {
            return Messages.get(SpiritBow.class, "prompt");
        }
    };

    public interface Supplier<T> {
        T get();
    }

    private static final int MAX_TIER = 5;

    private static final Map<Class<? extends RBAGun>, Supplier<? extends RBAGun>[]> gunMap = new HashMap<>();

    static {
        gunMap.put(RBAHG.class, new Supplier[]{
                RBAHG_T1::new, RBAHG_T2::new, RBAHG_T3::new, RBAHG_T4::new, RBAHG_T5::new
        });
        gunMap.put(RBASMG.class, new Supplier[]{
                RBASMG_T1::new, RBASMG_T2::new, RBASMG_T3::new, RBASMG_T4::new, RBASMG_T5::new
        });
        gunMap.put(RBASG.class, new Supplier[]{
                RBASG_T1::new, RBASG_T2::new, RBASG_T3::new, RBASG_T4::new, RBASG_T5::new
        });
        gunMap.put(RBAMG.class, new Supplier[]{
                RBAMG_T1::new, RBAMG_T2::new, RBAMG_T3::new, RBAMG_T4::new, RBAMG_T5::new
        });
        gunMap.put(RBAAR.class, new Supplier[]{
                RBAAR_T1::new, RBAAR_T2::new, RBAAR_T3::new, RBAAR_T4::new, RBAAR_T5::new, RBAUniqueIdea::new
        });
        gunMap.put(RBAGL.class, new Supplier[]{
                RBAGL_T1::new, RBAGL_T2::new, RBAGL_T3::new, RBAGL_T4::new, RBAGL_T5::new
        });
        gunMap.put(RBASR.class, new Supplier[]{
                RBASR_T1::new, RBASR_T2::new, RBASR_T3::new, RBASR_T4::new, RBASR_T5::new
        });
    }

    public static RBAGun getGun(Class<? extends RBAGun> gunClass, int tier, int lvl) {
        Supplier<? extends RBAGun>[] suppliers = gunMap.get(gunClass);
        if (suppliers != null) {
            int index = Math.max(0, Math.min(tier - 1, MAX_TIER - 1));
            RBAGun gun = suppliers[index].get();
            gun.upgrade(lvl);
            return gun;
        }

        return new RBAAR_T1(); //만약 아무것도 반환하지 않을 경우 기본적으로 1티어 AR을 반환함
    }

    public PlaceHolder getPlaceHolder() {
        return new PlaceHolder();
    }

    public static class PlaceHolder extends RBAGun {
        @Override
        public boolean isSimilar(Item item) {
            return !(item instanceof RBASpecialGun);
        }

        @Override
        public String status() {
            return null;
        }

        @Override
        public String info() {
            return "";
        }
    }
}
