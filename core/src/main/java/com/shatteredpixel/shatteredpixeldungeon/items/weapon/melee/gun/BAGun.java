package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun;




import com.shatteredpixel.shatteredpixeldungeon.effects.BAShotRay;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.BATalents;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.BAHero;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BAElementalBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BAInfiniteBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BAShockBulletCooldown;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BASnipingBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BAStickyGrenade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.elementals.BAAPBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.elementals.BAElectricBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.elementals.BAElementalBulletBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.elementals.BAExplosiveBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.elementals.BAIceBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.elementals.BAIncendiaryBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.YellowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.BAAmmoBox;
import com.shatteredpixel.shatteredpixeldungeon.items.active.BAIronHorus;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Shocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Dagger;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL.BAGL;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class BAGun extends MeleeWeapon {
    public static final String AC_SHOOT		= "SHOOT";
    public static final String AC_RELOAD    = "RELOAD";
    public static final String AC_AMMO      = "AMMO";
    public static final String AC_JUMP      = "JUMP";
    public static final String AC_RIOT	 = "RIOT";
    public static final String AC_SHOOTALL = "SHOOTALL";

    protected int max_round; //최대 장탄수
    protected int round; //현재 장탄수
    protected float reload_time = 2f; //재장전 시간
    protected int shotPerShoot = 1; //발사 당 탄환의 개수
    protected float shootingSpeed = 1f; //발사 시 소모하는 턴의 배율. 낮을수록 빠르다
    protected float shootingAccuracy = 1f; //발사 시 탄환 정확성의 배율. 높을 수록 정확하다
    protected boolean explode = false; //탄환 폭발 여부
    public static final String TXT_STATUS = "%d/%d";

    private boolean riot = false;
    private boolean shootAll = false;

    public boolean doubleBarrelSpecial = false;

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
    private static final String EXPLODE = "explode";
    private static final String RIOT = "riot";
    private static final String SHOOTALL = "shootAll";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(MAX_ROUND, max_round);
        bundle.put(ROUND, round);
        bundle.put(RELOAD_TIME, reload_time);
        bundle.put(SHOT_PER_SHOOT, shotPerShoot);
        bundle.put(SHOOTING_SPEED, shootingSpeed);
        bundle.put(SHOOTING_ACCURACY, shootingAccuracy);
        bundle.put(EXPLODE, explode);
        bundle.put(RIOT, riot);
        bundle.put(SHOOTALL, shootAll);
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
        explode = bundle.getBoolean(EXPLODE);
        riot = bundle.getBoolean(RIOT);
        shootAll = bundle.getBoolean(SHOOTALL);
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (isEquipped( hero )) {
            actions.add(AC_SHOOT);
            actions.add(AC_RELOAD);
            if (hero.hasTalent(Talent.BA_AMMO_BOX)) {
                actions.add(AC_AMMO);
            }
            if (this instanceof BAGL && hero.hasTalent(Talent.BA_ROCKET_JUMP)) {
                actions.add(AC_JUMP);
            }
            if (hero.subClass == HeroSubClass.BA_NONOMI_EX_RIOT) {
                actions.add(AC_RIOT);
            }
            if (hero.subClass == HeroSubClass.BA_NONOMI_EX_SHOOTALL) {
                actions.add(AC_SHOOTALL);
            }
        }
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

        if (action.equals(AC_SHOOT)) {
            if (!isEquipped( hero )) {
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
                GLog.w(Messages.get(this, "already_loaded"));
            } else {
                reload();
            }
        }
        if (action.equals(AC_AMMO)) {
            int cell = Dungeon.level.map[hero.pos];
            if (cell == Terrain.CHASM || cell == Terrain.DOOR || cell == Terrain.OPEN_DOOR) {
                BAHero.yellW(hero, Messages.get(Hero.class, hero.heroClass.name().replace("BA_", "") + "_cannot_ammo_place"));     //"이 곳엔 설치할 수 없어요..."
            } else {
                if (hero.buff(BAAmmoBox.AmmoBoxCooldown.class) != null) {
                    BAHero.yellW(hero, Messages.get(Hero.class, hero.heroClass.name().replace("BA_", "") + "_ammo_place_cooldown"));     //"아직 사용할 수 없어요..."
                } else {
                    hero.sprite.operate(hero.pos);
                    Dungeon.level.drop(new BAAmmoBox(1+2*hero.pointsInTalent(Talent.BA_AMMO_BOX)), hero.pos);
                    hero.spendAndNext(Actor.TICK);
                    BAHero.yellP(hero, Messages.get(Hero.class, hero.heroClass.name().replace("BA_", "") + "_ammo_place"));    //"탄약 상자를 설치하겠습니다...!"
                    Buff.affect(hero, BAAmmoBox.AmmoBoxCooldown.class).set();
                }
            }
        }
        if (action.equals(AC_JUMP)) {
            if (isAllLoaded()) {
                usesTargeting = false;
                curUser = hero;
                curItem = this;
                GameScene.selectCell(jumper);
            } else {
                BAHero.yellW(hero, Messages.get(Hero.class, hero.heroClass.name().replace("BA_", "") + "_ammo_lack"));    //장탄수가 부족해요..."
            }
        }
        if (action.equals(AC_RIOT)) {
            riot = !riot;
            hero.sprite.operate(hero.pos);
            Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
            if (riot) {
                BAHero.yellI(hero, Messages.get(Hero.class, hero.heroClass.name().replace("BA_", "") + "_riot_on"));
            } else {
                BAHero.yellI(hero, Messages.get(Hero.class, hero.heroClass.name().replace("BA_", "") + "_riot_off"));
            }
        }
        if (action.equals(AC_SHOOTALL)) {
            shootAll = !shootAll;
            hero.sprite.operate(hero.pos);
            Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
            if (shootAll) {
                BAHero.yellI(hero, Messages.get(Hero.class, hero.heroClass.name().replace("BA_", "") + "_shootall_on"));
            } else {
                BAHero.yellI(hero, Messages.get(Hero.class, hero.heroClass.name().replace("BA_", "") + "_shootall_off"));
            }
        }
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
        Dagger.sneakAbility(hero, target, 6, 4, this);
    }

    public void reload() {
        quickReload();
        hero.busy();
        hero.sprite.operate(hero.pos);

        if (hero.belongings.secondWep != null) {
            if (hero.belongings.secondWep instanceof BAGun) {
                BAGun secondGun = ((BAGun) hero.belongings.secondWep);
                if (hero.hasTalent(Talent.BA_DOUBLE_BARREL_2) && secondGun.round < secondGun.maxRound()) {
                    secondGun.quickReload();
                    hero.spendAndNext(Math.max(3-hero.pointsInTalent(Talent.BA_DOUBLE_BARREL_2), 0));
                }
            }
        }

        onReload();
        Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
        hero.spendAndNext(reloadTime());
        GLog.i(Messages.get(this, "reload"));
    }

    public void onReload() {
        if (hero.hasTalent(Talent.BA_RELOADING_SHIELD)) {
            Buff.affect(hero, Barrier.class).setShield(1+2*hero.pointsInTalent(Talent.BA_RELOADING_SHIELD));
        }
        if (hero.hasTalent(Talent.BA_TACTICAL_SHIELD_3) && hero.buff(BAIronHorus.TacticalShieldBuff.class) != null && hero.buff(BATalents.TacticalInvisibilityTracker.class) == null) {
            Buff.affect(hero, Invisibility.class, 3f*hero.pointsInTalent(Talent.BA_TACTICAL_SHIELD_3));
            Buff.affect(hero, BATalents.TacticalInvisibilityTracker.class);
        }
        for (Buff buff : hero.buffs()) {
            if (buff instanceof BAElementalBulletBuff) {
                buff.detach();
            }
        }
        if (hero.subClass == HeroSubClass.BA_SHIROKO_EX_ELEMENTAL_BULLET) {
            Buff.affect(hero, BAElementalBullet.class).set(reloadTime()+1);
        }
        if (hero.subClass == HeroSubClass.BA_NOA_EX_LARGE_MAGAZINE && hero.buff(BAInfiniteBullet.infiniteBulletCooldown.class) == null) {
            Buff.affect(hero, BAInfiniteBullet.class).set(reloadTime());
            Buff.affect(hero, BAInfiniteBullet.infiniteBulletCooldown.class).set();
        }
        if (hero.hasTalent(Talent.BA_LARGE_MAGAZINE_3) && hero.buff(BATalents.BulletTimeCooldown.class) == null) {
            for (Char ch : Actor.chars()) {
                if (Dungeon.level.heroFOV[ch.pos] && ch.alignment == Char.Alignment.ENEMY) {
                    Buff.affect(ch, Slow.class, (reloadTime()+3*hero.pointsInTalent(Talent.BA_LARGE_MAGAZINE_3)-1));
                }
            }
            Buff.affect(hero, BATalents.BulletTimeCooldown.class, 30f);
        }
        if (hero.subClass == HeroSubClass.BA_MIYU_EX_SNIPING_BULLET) {
            Buff.affect(hero, BASnipingBullet.class);
        }
        if (hero.buff(BATalents.FastHandTracker.class) != null) hero.buff(BATalents.FastHandTracker.class).detach();
        if (hero.buff(BATalents.PerfectHandTracker.class) != null) hero.buff(BATalents.PerfectHandTracker.class).detach();
        if (hero.hasTalent(Talent.BA_GAME_START_3)) {
            int area[];
            switch (hero.pointsInTalent(Talent.BA_GAME_START_3)) {
                case 1: default:
                    area = PathFinder.NEIGHBOURS9;
                    break;
                case 2:
                    area = PathFinder.NEIGHBOURS25;
                    break;
                case 3:
                    area = BAHero.neighbours49();
                    break;
            }
            for (int i : area) {
                int cell = hero.pos + i;
                int terrain = Dungeon.level.map[cell];
                if (terrain == Terrain.GRASS || terrain == Terrain.EMBERS) {
                    int toTerrain = Terrain.FURROWED_GRASS;
                    if (Random.Float() < 0.25f && Regeneration.regenOn()) {
                        toTerrain = Terrain.HIGH_GRASS;
                    }
                    Level.set(cell, toTerrain);
                    GameScene.updateMap( cell );
                }
            }
        }
    }

    public void quickReload() {
        round = maxRound();
        updateQuickslot();
    }

    public void manualReload() {
        manualReload(1, false);
    }

    public void manualReload(int amount, boolean overReload) {
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

    public int shotPerShoot() { //발사 당 탄환의 수
        if (shootAll) {
            return (round + Random.IntRange(0, hero.pointsInTalent(Talent.BA_SHOOTALL_1))) * shotPerShoot;
        }
        return shotPerShoot;
    }

    public int maxRound() { //최대 장탄수
        int amount = max_round;

        amount += Dungeon.hero.pointsInTalent(Talent.BA_LARGE_MAGAZINE);

        return amount;
    }

    public int round() {
        return round;
    }

    public float reloadTime() { //재장전에 소모하는 턴
        if (hero.buff(BAIronHorus.TacticalShieldBuff.class) != null && hero.subClass == HeroSubClass.BA_HOSHINO_EX_TACTICAL_SHIELD) {
            return 0;
        }

        float amount = reload_time;

        amount += hero.pointsInTalent(Talent.BA_LARGE_MAGAZINE);

        if (hero.buff(BATalents.FastHandTracker.class) != null) {
            amount -= hero.pointsInTalent(Talent.BA_FAST_HAND);
        }

        if (hero.hasTalent(Talent.BA_LARGE_MAGAZINE_1)) {
            ArrayList<Char> chars = new ArrayList<>();
            for (Char ch : Actor.chars()) {
                if (Dungeon.level.heroFOV[ch.pos] && ch.alignment == Char.Alignment.ENEMY) {
                    chars.add(ch);
                }
            }
            if (chars.size() > 0) {
                amount *= Math.pow(1/(1+0.05*hero.pointsInTalent(Talent.BA_LARGE_MAGAZINE_1)), chars.size());  //1.05/1.1/1.15로 시야 내의 적의 수만큼 나눔
            }
        }

        if (hero.buff(BATalents.PerfectHandTracker.class) != null) {
            amount *= Math.pow(1-0.2f*hero.pointsInTalent(Talent.BA_PENETRATION_SHOT_3), hero.buff(BATalents.PerfectHandTracker.class).getKilled());
        }

        return amount;
    }

    @Override
    public int max(int lvl) {
        int damage = 3*(tier+1) +
                     lvl*(tier+1); //근접 무기로서의 최대 데미지

        if (isEquipped(hero) && round == 0 && hero.hasTalent(Talent.BA_SHOOTALL_3)) {
            damage *= 1f + 0.2f/3f*hero.pointsInTalent(Talent.BA_SHOOTALL_3);
        }
        return Math.max(trueMax(damage), min(lvl)); //Shittened PD's prefixes, as for every melee weapon here

    }

    public int bulletMin(int lvl) {
        return tier +
                lvl +
                RingOfSharpshooting.levelDamageBonus(hero);
    }
    public int bulletMin() {
        return bulletMin(this.buffedLvl());
    }

    //need to be overridden
    public int bulletMax(int lvl) {
        return 0; //총알의 최대 데미지
    }
    public int bulletMax() {
        return bulletMax(this.buffedLvl());
    }

    public int bulletDamage() {
        int damage = Random.NormalIntRange(bulletMin(), bulletMax());
        if (doubleBarrelSpecial) {
            damage = Math.round(damage * 0.667f);

        }

        damage = augment.damageFactor(damage);  //증강에 따라 변화하는 효과
        return damage;
    }

    @Override
    protected float baseDelay(Char owner) {
        if (doubleBarrelSpecial){
            return 0f;
        } else{
            return super.baseDelay(owner);
        }
    }


    @Override
    public String info() {
        String info = super.info();
        //근접 무기의 설명을 모두 가져옴, 여기에서 할 것은 근접 무기의 설명에 추가로 생기는 문장을 더하는 것
        if (levelKnown) { //감정되어 있을 때
            info += "\n\n" + Messages.get(BAGun.class, "gun_desc",
                    augment.damageFactor(bulletMin(buffedLvl())), augment.damageFactor(bulletMax(buffedLvl())), shotPerShoot(), round, maxRound(), new DecimalFormat("#.##").format(reloadTime()));
        } else { //감정되어 있지 않을 때
            info += "\n\n" + Messages.get(BAGun.class, "gun_typical_desc",
                    augment.damageFactor(bulletMin(0)), augment.damageFactor(bulletMax(0)), shotPerShoot(), round, maxRound(), new DecimalFormat("#.##").format(reloadTime()));
        }
        //DecimalFormat("#.##")은 .format()에 들어가는 매개변수(실수)를 "#.##"형식으로 표시하는데 사용된다.
        //가령 5.55555가 .format()안에 들어가서 .format(5.55555)라면, new DecimalFormat("#.##").format(5.55555)는 5.55라는 String 타입의 값을 반환한다.

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

    public class Bullet extends MissileWeapon {

        public boolean isDoubleBarrel = false;

        {
            hitSound = Assets.Sounds.PUFF;
            tier = BAGun.this.tier;
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
            if (isDebuffed && hero.hasTalent(Talent.BA_ELEMENTAL_BULLET_3)) {
                damage = Math.round(damage*(1 + 0.2f * hero.pointsInTalent(Talent.BA_ELEMENTAL_BULLET_3)/3f)); //+6.67%/+13.33%/+20%
            }
            if (hero.subClass == HeroSubClass.BA_HOSHINO_EX_TACTICAL_SHIELD && hero.buff(BAIronHorus.TacticalShieldBuff.class) != null) {
                Buff.affect(hero, Barrier.class).set(1, 10+5*hero.pointsInTalent(Talent.BA_TACTICAL_SHIELD_2));
                BuffIndicator.refreshHero();
            }
            if (hero.heroClass == HeroClass.BA_MIYAKO && Random.Float() < 0.1f) {
                Buff.affect(defender, Vulnerable.class, 3f);
            }
            if (hero.buff(BAAPBullet.class) != null) {
                damage *= 1-0.2f+0.05f*hero.pointsInTalent(Talent.BA_ELEMENTAL_BULLET_1);
            }
            if (hero.buff(BAIncendiaryBullet.class) != null ) {
                if (Random.Float() < 0.2f+0.1f*hero.pointsInTalent(Talent.BA_ELEMENTAL_BULLET_1)) {
                    Buff.affect(defender, Burning.class).reignite(defender);
                }
            }
            if (hero.buff(BAIceBullet.class) != null ) {
                Buff.affect(defender, Chill.class, 2+hero.pointsInTalent(Talent.BA_ELEMENTAL_BULLET_1));
                if (defender.buff(Chill.class) != null) {
                    if (Dungeon.level.map[defender.pos] == Terrain.WATER && defender.buff(Chill.class).cooldown() > 20-3*hero.pointsInTalent(Talent.BA_ELEMENTAL_BULLET_1)) {
                        new FlavourBuff() {
                            {
                                actPriority = VFX_PRIO;
                            }

                            public boolean act() {
                                Buff.affect(target, Frost.class, Frost.DURATION);
                                return super.act();
                            }
                        }.attachTo(defender);
                    }
                }
            }
            if (hero.buff(BAElectricBullet.class) != null) {
                ArrayList<Char> affected = new ArrayList<>();
                ArrayList<Lightning.Arc> arcs = new ArrayList<>();
                Shocking.arc( attacker, defender, 2, affected, arcs );
                affected.remove(defender);  //직접 명중한 대상은 제외
                for (Char ch : affected) {
                    ch.damage( Math.round( damage * (0.2f+0.1f*hero.pointsInTalent(Talent.BA_ELEMENTAL_BULLET_1)) ), Shocking.class );
                }
            }
            if (hero.heroClass == HeroClass.BA_SHIROKO) {
                int amount = 0;
                float chance = 1/(float)((BAGun) hero.belongings.weapon).shotPerShoot();
                amount ++;
                if (hero.buff(BAExplosiveBullet.class) != null) {
                    amount += 2+hero.pointsInTalent(Talent.BA_ELEMENTAL_BULLET_1);
                }
                if (hero.hasTalent(Talent.BA_RAPID_SHOOTING)) {
                    if (Random.Float() < 0.5f*hero.pointsInTalent(Talent.BA_RAPID_SHOOTING)) {
                        amount ++;
                    }
                }
                for (int i=0; i<amount; i++) {
                    if (Random.Float() < chance) {
                        int explodeDamage = Random.NormalIntRange(Dungeon.scalingDepth()/5, Dungeon.scalingDepth()/2+3);
                        explodeDamage += hero.pointsInTalent(Talent.BA_ENHANCED_EXPLODE);
                        explodeDamage -= defender.drRoll();
                        CellEmitter.center(defender.pos).burst(BlastParticle.FACTORY, 1);
                        defender.damage(explodeDamage, hero);
                    }
                }
            }
            if (BAGun.this.isAllLoaded() && hero.hasTalent(Talent.BA_DOUBLE_TAP)) {
                new FlavourBuff() {
                    {
                        actPriority = VFX_PRIO;
                    }

                    public boolean act() {
                        Buff.affect(target, BATalents.DoubleTapTracker.class);
                        return super.act();
                    }
                }.attachTo(hero);
            }
            if (hero.hasTalent(Talent.BA_BEST_SNIPER)) {
                if (damage >= defender.HT*(1-(1/(6f-hero.pointsInTalent(Talent.BA_BEST_SNIPER)))) && damage <= defender.HT) {
                    damage = defender.HT;
                    defender.sprite.emitter().start( ShadowParticle.UP, 0.05f, 10 );
                }
            }
            if (hero.buff(BASnipingBullet.class) != null) {
                int distance = Dungeon.level.distance(attacker.pos, defender.pos) - 1;
                float multi = ((Math.min(distance, 12)+4)/(8f-hero.pointsInTalent(Talent.BA_SNIPING_BULLET_1)));
                damage *= multi;
                if (hero.hasTalent(Talent.BA_SNIPING_BULLET_3) && (distance >= (12-2*hero.pointsInTalent(Talent.BA_SNIPING_BULLET_3)))) {
                    Buff.affect(defender, Paralysis.class, 3f);
                    Buff.affect(attacker, BAShockBulletCooldown.class).set();
                }
                hero.buff(BASnipingBullet.class).detach();
            }
            return BAGun.this.proc(attacker, defender, damage);
        }

        @Override
        public int buffedLvl(){
            return BAGun.this.buffedLvl();
        }

        @Override
        public int damageRoll(Char owner) {
            int damage = bulletDamage();
            return damage;
        }

        @Override
        public boolean hasEnchant(Class<? extends Enchantment> type, Char owner) {
            return BAGun.this.hasEnchant(type, owner);
        }

        @Override
        public float delayFactor(Char user) {
            return BAGun.this.delayFactor(user) * shootingSpeed;
        }
        
        @Override
        public float accuracyFactor(Char owner, Char target) {
            float ACC = super.accuracyFactor(owner, target);
            if (owner instanceof Hero) {
                ACC *= shootingAccuracy;
            }
            if (shootAll) {
                ACC *= 0.5f;
            }
            return ACC;
        }

        @Override
        public int STRReq(int lvl) {
            return BAGun.this.STRReq();
        }

        @Override
        protected void onThrow( int cell ) {
            boolean killedEnemy = false;
            if (explode || hero.subClass == HeroSubClass.BA_YUZU_EX_GAME_START) {
                Char chInPos = Actor.findChar(cell);
                if (hero.subClass == HeroSubClass.BA_YUZU_EX_STICKY_GRENADE && chInPos != null) {
                    Buff.affect(chInPos, BAStickyGrenade.class).attach(BAGun.this);
                    chInPos.sprite.flash();
                    Sample.INSTANCE.play(Assets.Sounds.PUFF);
                    if (chInPos instanceof Mob) ((Mob)chInPos).beckon(curUser.pos);
                } else {
                    ArrayList<Char> targets = new ArrayList<>();
                    int shootArea[] = PathFinder.NEIGHBOURS9;
                    if (explode && hero.subClass == HeroSubClass.BA_YUZU_EX_GAME_START) {
                        shootArea = BAHero.neighbours21();
                    }

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
                                targets.add(ch);
                            }
                        }
                    }

                    ArrayList<Char> additionalTargets = new ArrayList<>();
                    if (hero.hasTalent(Talent.BA_GAME_START_2)) {
                        for (Char target : targets) {
                            for (int i : PathFinder.NEIGHBOURS9) {
                                int c = target.pos + i;
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
                                    if (ch != null && !additionalTargets.contains(ch) && !targets.contains(ch)) {   //기존 폭발에 데미지를 입을 대상은 제외함
                                        additionalTargets.add(ch);
                                    }
                                }
                            }
                        }
                    }

                    targets.addAll(additionalTargets);

                    for (Char target : targets){
                        for (int i = 0; i < shotPerShoot(); i++) {
                            if (target == hero && hero.heroClass == HeroClass.BA_YUZU) {
                                curUser.shoot(target, this, 0.25f, 0, 1 );
                            } else {
                                curUser.shoot(target, this, 1f, 0, 1f );
                            }
                        }
                        if (!target.isAlive()) {
                            killedEnemy = true;
                        }
                        if (target == hero && !target.isAlive()) {
                            Dungeon.fail(getClass());
                            GLog.n(Messages.get(this, "ondeath"));
                        }
                    }

                    Sample.INSTANCE.play( Assets.Sounds.BLAST );
                }
            } else {
                Char enemy = Actor.findChar( cell );
                for (int i = 0; i < shotPerShoot(); i++) { //데미지 입히는 것과 발사 시 주변에서 나는 연기를 shotPerShoot만큼 반복
                    if (enemy == null || enemy == curUser) {
                        parent = null;
                        CellEmitter.get(cell).burst(SmokeParticle.FACTORY, 2);
                        CellEmitter.center(cell).burst(BlastParticle.FACTORY, 2);
                    } else {
                        if (!curUser.shoot( enemy, this, 1f, 0, 1f )) {
                            CellEmitter.get(cell).burst(SmokeParticle.FACTORY, 2);
                            CellEmitter.center(cell).burst(BlastParticle.FACTORY, 2);
                        }
                    }
                }
                if (enemy != null && !enemy.isAlive()) {
                    killedEnemy = true;
                }
            }

            if (shootAll) {
                if ( hero.hasTalent(Talent.BA_SHOOTALL_2) && killedEnemy && Random.Float() < 0.4f/3f*hero.pointsInTalent(Talent.BA_SHOOTALL_2)) {
                    quickReload();
                } else {
                    round = 0;
                }
            } else {
                round --;
                if (hero.buff(BAInfiniteBullet.class) != null) {
                    round ++;
                }
                if (round == 0 && hero.hasTalent(Talent.BA_LAST_BULLET)) {
                    Buff.affect(hero, Barrier.class).setShield(2+3*hero.pointsInTalent(Talent.BA_LAST_BULLET));
                }
            }

            for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) { //주변의 적들을 영웅의 위치로 모이게 하는 구문
                if (mob.paralysed <= 0
                        && Dungeon.level.distance(curUser.pos, mob.pos) <= 4
                        && mob.state != mob.HUNTING) {
                    mob.beckon( curUser.pos );
                }
            }
            updateQuickslot();
        }

        @Override
        public void throwSound() {
            Sample.INSTANCE.play( Assets.Sounds.HIT_CRUSH, 1, Random.Float(0.33f, 0.66f) );
        }

        @Override
        public void cast(final Hero user, final int dst) {
            cast(user, dst, false);
        }

        public void cast(final Hero user, final int dst, boolean doubleBarrel) {
            isDoubleBarrel = doubleBarrel;
            if (riot) {
                int cell = throwPos( user, dst );
                riot(cell);
            } else {
                super.cast(user, dst);
            }
        }

        private void riot(int cell) {
            Ballistica aim = new Ballistica(hero.pos, cell, Ballistica.WONT_STOP);

            int maxDist = 4+2*hero.pointsInTalent(Talent.BA_RIOT_2);
            int dist = Math.min(aim.dist, maxDist);

            ConeAOE cone = new ConeAOE(aim,
                    dist,
                    90-15*hero.pointsInTalent(Talent.BA_RIOT_2),
                    Ballistica.STOP_SOLID | Ballistica.STOP_TARGET);

            Invisibility.dispel();
            hero.sprite.zap(cell);

            ArrayList<Char> affected = new ArrayList<>();
            for (int c : cone.cells){
                CellEmitter.get(c).burst(SmokeParticle.FACTORY, 2);
                CellEmitter.center(c).burst(BlastParticle.FACTORY, 2);
                Char ch = Actor.findChar(c);
                if (ch != null && ch.alignment != hero.alignment){
                    affected.add(ch);
                }
            }
            float dmgMulti = 1f;
            float accMulti = 1f;
            float multi = Math.min(1f + 0.1f * hero.pointsInTalent(Talent.BA_RIOT_1) * affected.size(), 2.5f);
            dmgMulti *= 1 + (shotPerShoot()-1)/2f;
            dmgMulti *= multi;
            accMulti *= multi;

            for (Char ch : affected) {
                curUser.shoot(ch, this, dmgMulti, 0, accMulti);

                if (hero.hasTalent(Talent.BA_RIOT_3)
                        && Random.Float() < (hero.pointsInTalent(Talent.BA_RIOT_3) * (5-Dungeon.level.distance(hero.pos, ch.pos)) * 5) / 100f) { //(5-거리)*5*n%
                    Ballistica trajectory = new Ballistica(hero.pos, ch.pos, Ballistica.STOP_TARGET);
                    trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size()-1), Ballistica.PROJECTILE);
                    WandOfBlastWave.throwChar(ch,
                            trajectory,
                            1+dist-Dungeon.level.distance(hero.pos, ch.pos), //현재 위치로부터 발사 거리까지의 남은 타일 수
                            false,
                            true,
                            this);
                }
            }

            hero.spendAndNext(delayFactor(hero));
            updateQuickslot();
            round --;
            Sample.INSTANCE.play( Assets.Sounds.HIT_CRUSH, 1, Random.Float(0.33f, 0.66f) );
        }
    }

    private CellSelector.Listener shooter = new CellSelector.Listener() {
        @Override
        public void onSelect( Integer target ) {
            if (target != null) {
                if (target == curUser.pos) {
                    execute(hero, AC_RELOAD);
                } else {
                    //미유 EX 관통 사격 능력
                    if (hero.subClass == HeroSubClass.BA_MIYU_EX_PENETRATION_SHOT) {

                        Ballistica beam = new Ballistica(curUser.pos, target, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
                        ArrayList<Char> chars = new ArrayList<>();

                        for (int c : beam.subPath(1, beam.dist)) {
                            Char ch;

                            if ((ch = Actor.findChar( c )) != null) {
                                chars.add(ch);
                            }

                            CellEmitter.center( c ).burst( YellowParticle.BURST, Random.IntRange( 1, 2 ) );
                        }
                        int prevRound = round;
                        int damageBonus = Math.min(chars.size() * hero.pointsInTalent(Talent.BA_PENETRATION_SHOT_1), 15);
                        int killed = 0;
                        for (Char ch : chars) {
                            Bullet bullet = knockBullet();
                            bullet.proc(curUser, ch, bullet.damageRoll(curUser) + damageBonus);
                            bullet.onThrow(ch.pos);
                            if (!ch.isAlive()) killed++;
                        }
                        if (BAGun.this instanceof BAGL && chars.isEmpty()) {
                            Bullet bullet = knockBullet();
                            bullet.onThrow(beam.collisionPos);
                        }
                        if (hero.hasTalent(Talent.BA_PENETRATION_SHOT_3) && killed > 0 && hero.buff(BATalents.PerfectHandTracker.class) == null) {
                            Buff.affect(curUser, BATalents.PerfectHandTracker.class).set(killed);
                        }
                        round = prevRound - 1;
                        if (hero.buff(BAInfiniteBullet.class) != null) {
                            round = prevRound;
                        }
                        updateQuickslot();

                        Sample.INSTANCE.play( Assets.Sounds.HIT_CRUSH, 1, Random.Float(0.33f, 0.66f) );
                        CellEmitter.get(curUser.pos).burst(SmokeParticle.FACTORY, 2);
                        CellEmitter.center(curUser.pos).burst(BlastParticle.FACTORY, 2);

                        curUser.sprite.zap(target);
                        hero.spendAndNext(hero.attackDelay());

                        int cell = beam.path.get(Math.min(beam.dist, target));
                        curUser.sprite.parent.add(new BAShotRay(curUser.sprite.center(), DungeonTilemap.raisedTileCenterToWorld( cell )));

                        if (hero.hasTalent(Talent.BA_PENETRATION_SHOT_2) && hero.buff(BATalents.SuppressingFireCooldown.class) == null) {
                            boolean affected = false;
                            PathFinder.buildDistanceMap( curUser.pos, BArray.not( Dungeon.level.solid, null ), hero.pointsInTalent(Talent.BA_PENETRATION_SHOT_2) );
                            ArrayList<Char> chToTerror = new ArrayList<>();
                            for (int i = 0; i < PathFinder.distance.length; i++) {
                                if (PathFinder.distance[i] < Integer.MAX_VALUE) {
                                    Char ch = Actor.findChar(i);
                                    if (ch != null) {
                                        if (ch instanceof Mob) {
                                            chToTerror.add(ch);
                                        }
                                    }
                                }
                            }
                            for (Char ch : chToTerror) {
                                Buff.affect(ch, Terror.class, 3);
                                if (!affected) {
                                    affected = true;
                                }
                            }
                            if (affected) {
                                Buff.affect(curUser, BATalents.SuppressingFireCooldown.class, 20f);
                            }
                        }
                    } else {
                        knockBullet().cast(curUser, target);
                    }
                }
            }
        }
        @Override
        public String prompt() {
            return Messages.get(SpiritBow.class, "prompt");
        }
    };

    private CellSelector.Listener jumper = new CellSelector.Listener() {
        @Override
        public void onSelect( Integer target ) {
            if (target != null) {
                if (target == curUser.pos) {
                    BAHero.yellW(hero, Messages.get(Hero.class, hero.heroClass.name().replace("BA_", "") + "_cannot_jump"));   //"그런 곳으로... 점프를 하라고요...?"
                } else {
                    if (hero.rooted) {
                        BAHero.yellW(hero, Messages.get(Hero.class, hero.heroClass.name().replace("BA_", "") + "_cannot_jump_rooted"));    //"발이 묶여버렸어요..."
                        PixelScene.shake( 1, 1f );
                        return;
                    }

                    Ballistica route = new Ballistica(hero.pos, target, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
                    int cell = route.collisionPos;

                    //can't occupy the same cell as another char, so move back one.
                    int backTrace = route.dist-1;
                    while (Actor.findChar( cell ) != null && cell != hero.pos) {
                        cell = route.path.get(backTrace);
                        backTrace--;
                    }

                    final int dest = cell;
                    hero.busy();
                    hero.sprite.jump(hero.pos, cell, new Callback() {
                        @Override
                        public void call() {
                            hero.move(dest);
                            Dungeon.level.occupyCell(hero);
                            Dungeon.observe();
                            GameScene.updateFog();

                            Invisibility.dispel();
                            hero.spendAndNext(Actor.TICK);
                        }
                    });

                    int damage = 40-10*hero.pointsInTalent(Talent.BA_ROCKET_JUMP);
                    damage -= hero.drRoll();
                    CellEmitter.center(hero.pos).burst(BlastParticle.FACTORY, 1);
                    CellEmitter.get(hero.pos).burst(SmokeParticle.FACTORY, 3);
                    Sample.INSTANCE.play(Assets.Sounds.BLAST);
                    if (damage >= hero.HP) damage = hero.HP-1;
                    hero.damage(damage, this);

                    round = 0;
                    updateQuickslot();
                }
            }
        }
        @Override
        public String prompt() {
            return Messages.get(Hero.class, "rocketjump");
        }
    };
}
