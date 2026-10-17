package com.shatteredpixel.shatteredpixeldungeon.ui.towerlist;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Albino;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ChiefRat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.RatKing;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.CampRat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.CampRatArcher;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.CampRatKnife;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.CampRatLeader;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.CampRatMage;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.CampRatShield;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.Tower;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerCShooting;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerCannon1;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerCannon2;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerCannon3;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerCannonNuke;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerCannonMissileLauncher;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerCrossbow1;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerCrossbow2;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerCrossbow3;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerCrossbowBallista;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerCrossbowGatling;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerDartgun1;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerDartgun2;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerDartgun3;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerDartgunSniper;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerDartgunSpitter;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerDisintegration1;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerDisintegration2;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerDisintegration3;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerGrave1;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerGrave2;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerGrave3;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerGraveCrypt;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerGraveElite;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerGuard1;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerGuard2;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerGuard3;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerGuardPaladin;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerGuardSpearman;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerLightning1;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerLightning2;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerLightning3;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerMiner;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerPylonBroken;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerRatCamp;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerTntLog;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerTotem;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerWall1;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerWall2;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerWall3;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerWallRunic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerWallSpiked;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerWand1;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerWand2;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerWand3;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Berry;
import com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbTrAngerTheDead;
import com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbTrArrowVolley;
import com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbTrBombVolley;
import com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbTrDartgunAlly;
import com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbTrGreatWall;
import com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbTrHyperats;
import com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbTrLightningStrike;
import com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbTrNullify;
import com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbTrOrder;
import com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbTrOvercharge;
import com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbTrPlanB;
import com.shatteredpixel.shatteredpixeldungeon.items.herospells.HeroSpell;
import com.shatteredpixel.shatteredpixeldungeon.items.towerspawners.SpawnerCamp;
import com.shatteredpixel.shatteredpixeldungeon.items.towerspawners.SpawnerCannon;
import com.shatteredpixel.shatteredpixeldungeon.items.towerspawners.SpawnerCrossbow;
import com.shatteredpixel.shatteredpixeldungeon.items.towerspawners.SpawnerDartgun;
import com.shatteredpixel.shatteredpixeldungeon.items.towerspawners.SpawnerDisintegration;
import com.shatteredpixel.shatteredpixeldungeon.items.towerspawners.SpawnerGrave;
import com.shatteredpixel.shatteredpixeldungeon.items.towerspawners.SpawnerGuard;
import com.shatteredpixel.shatteredpixeldungeon.items.towerspawners.SpawnerLightning;
import com.shatteredpixel.shatteredpixeldungeon.items.towerspawners.SpawnerTntlog;
import com.shatteredpixel.shatteredpixeldungeon.items.towerspawners.SpawnerWall;
import com.shatteredpixel.shatteredpixeldungeon.items.towerspawners.SpawnerWand;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BossTrollSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.DeviceCompat;

import java.util.Locale;

public class TowerInfo extends Component {

    public enum AllTowers{
        //always unlocked
        CROSSBOW,
        MAGICMISSILE,
        WALL,
        //level unlockable
        RATCAMP,
        CANNON,
        GUARD,
        GRAVE,
        LIGHTNING,
        TNTLOG,
        DARTGUN,
        DISINTEGRATION,
        //dungeon towers
        TOTEM,
        PYLON,
        MINER,
        //general meanings for towers. Locked is a grey lock, Unlocked is a basic green lock icon, Dungeon is a basic stairs icon
        LOCKED,
        UNLOCKED,
        DUNGEON,


    }

    public static Image getTowerPreviewImage(AllTowers sometower) {
        //the mod drew a splash picture per tower, here the tower's own sprite stands in
        Tower t = getTowerNewInstance(sometower);
        return t == null ? new Image() : t.sprite();
    }

    public enum Lock{
        LOCKED,
        UNLOCKED,
        DUNGEON,

        NONE
    }
    public static int getTowerUnlockLevel(AllTowers sometower){ // the level you must REACH to unlock the tower
        switch (sometower){
            //internal
            case UNLOCKED:
            case LOCKED:
            case DUNGEON: return  -1;

            //from the beginning
            case CROSSBOW:
            case MAGICMISSILE:
            case WALL: return 0;

            //unlocked through completing levels
            case RATCAMP:          return 5;
            case CANNON:           return 5;
            case GUARD:            return 7;
            case GRAVE:            return 9;
            case LIGHTNING:        return 10;
            case TNTLOG:           return 11;
            case DARTGUN:          return 14;
            case DISINTEGRATION:   return 18;

                //dungeon ones
            case TOTEM:
            case MINER:
            case PYLON:
                return -1;
        }
        return -1;
    }

    public static Lock getTowerLock(AllTowers sometower){
        if (DeviceCompat.isDebug()) {

            switch (sometower){
                case UNLOCKED:
                case LOCKED:
                case DUNGEON: return  Lock.NONE;

                //random ones




                //dungeon ones
                case TOTEM:
                case PYLON:
                case MINER:
                    return Lock.DUNGEON;



                default: return Lock.UNLOCKED;
            }
        }
        switch (sometower){
            //internal
            case UNLOCKED:
            case LOCKED:
            case DUNGEON: return  Lock.NONE;

            //from the beginning
            case CROSSBOW:
            case MAGICMISSILE:
            case WALL: return Lock.UNLOCKED;

            //unlocked through completing levels
            case RATCAMP:
            case CANNON:
            case GUARD:
            case GRAVE:
            case LIGHTNING:
            case TNTLOG:
            case DARTGUN:
            case DISINTEGRATION: if (TowerInfo.getTowerUnlockLevel(sometower)<=SPDSettings.maxlevelunlocked()) return Lock.UNLOCKED; else return Lock.LOCKED;

            //dungeon ones
            case TOTEM:
            case MINER:
            case PYLON:
                return Lock.DUNGEON;


        }
        return Lock.LOCKED;
    }
    public static HeroSpell getHeroAbility(AllTowers sometower){

        switch (sometower){
            default:
            case CANNON:         return new AbTrBombVolley();
            case CROSSBOW:       return new AbTrArrowVolley();
            case MAGICMISSILE:   return new AbTrOvercharge();
            case LIGHTNING:      return new AbTrLightningStrike();
            case DISINTEGRATION: return new AbTrNullify();
            case WALL:           return new AbTrGreatWall();
            case GRAVE:          return new AbTrAngerTheDead();
            case GUARD:          return new AbTrOrder();
            case DARTGUN:        return new AbTrDartgunAlly();
            case TNTLOG:         return new AbTrPlanB();
            case RATCAMP:        return new AbTrHyperats();

        }
    }


    public static Image getTowerIcon(AllTowers sometower) {
        Tower t = getTowerNewInstance(sometower);
        return t == null ? new Image() : t.sprite();
    }

    public static int getTowerIndex(AllTowers sometower){
        switch (sometower){
            case CANNON: return 301;
            case CROSSBOW: return 302;
            case MAGICMISSILE: return 303;
            case LIGHTNING: return 304;
            case DISINTEGRATION: return 305;
            case WALL: return 306;
            case GRAVE: return 307;
            case GUARD: return 308;
            case DARTGUN: return 309;
            case TNTLOG: return 310;
            case RATCAMP: return 311;
        }
        return 306;
    }

    public static AllTowers getTowerByIndex(int i){
        switch (i){
            case 301: return AllTowers.CANNON;
            case 302: return AllTowers.CROSSBOW;
            case 303: return AllTowers.MAGICMISSILE;
            case 304: return AllTowers.LIGHTNING;
            case 305: return AllTowers.DISINTEGRATION;
            case 306: return AllTowers.WALL;
            case 307: return AllTowers.GRAVE;
            case 308: return AllTowers.GUARD;
            case 309: return AllTowers.DARTGUN;
            case 310: return AllTowers.TNTLOG;
            case 311: return AllTowers.RATCAMP;
        }
        return null;
    }

    public static String getTowerName(AllTowers sometower){
        return Messages.get(TowerInfo.class, sometower.name().toLowerCase(Locale.ROOT)+"_name");
    }

    public static String getTowerDescription(AllTowers sometower){
        switch (sometower){
            case CROSSBOW:{
                Tower lvl1 = new TowerCrossbow1();
                Tower lvl2 = new TowerCrossbow2();
                Tower lvl3 = new TowerCrossbow3();
                Tower b1 = new TowerCrossbowBallista();
                Tower b2 = new TowerCrossbowGatling();
                return Messages.get(TowerInfo.class, sometower.name().toLowerCase(Locale.ROOT)+"_desc",
                        lvl1.HT,
                        lvl2.HT,
                        lvl3.HT,
                        lvl1.damageMin,
                        lvl1.damageMax,
                        lvl2.damageMin,
                        lvl2.damageMax,
                        lvl3.damageMin,
                        lvl3.damageMax,
                        Math.round(lvl1.attackSkill(hero)*10),
                        Math.round(lvl2.attackSkill(hero)*10),
                        Math.round(lvl3.attackSkill(hero)*10),
                        Math.round(100/lvl1.baseAttackDelay),
                        Math.round(100/lvl2.baseAttackDelay),
                        Math.round(100/lvl3.baseAttackDelay),
                        ((TowerCShooting)lvl1).attackRange,
                        ((TowerCShooting)lvl2).attackRange,
                        ((TowerCShooting)lvl3).attackRange,
                        lvl1.cost,
                        lvl2.cost,
                        lvl3.cost,
                        Messages.get(TowerCrossbow1.class, "descstats"),
                        "_" + b1.name() + "_" + "\n" + b1.info(),
                        "_" + b2.name() + "_" + "\n" + b2.info());
            }
            case MAGICMISSILE:{
                Tower lvl1 = new TowerWand1();
                Tower lvl2 = new TowerWand2();
                Tower lvl3 = new TowerWand3();
                return Messages.get(TowerInfo.class, sometower.name().toLowerCase(Locale.ROOT)+"_desc",
                        lvl1.HT,
                        lvl2.HT,
                        lvl3.HT,
                        lvl1.damageMin,
                        lvl1.damageMax,
                        lvl2.damageMin,
                        lvl2.damageMax,
                        lvl3.damageMin,
                        lvl3.damageMax,
                        //Math.round(lvl1.attackSkill(hero)*10),
                        //Math.round(lvl2.attackSkill(hero)*10),
                        //Math.round(lvl3.attackSkill(hero)*10),
                        Math.round(100/lvl1.baseAttackDelay),
                        Math.round(100/lvl2.baseAttackDelay),
                        Math.round(100/lvl3.baseAttackDelay),
                        ((TowerCShooting)lvl1).attackRange,
                        ((TowerCShooting)lvl2).attackRange,
                        ((TowerCShooting)lvl3).attackRange,
                        lvl1.cost,
                        lvl2.cost,
                        lvl3.cost,
                        Messages.get(TowerWand1.class, "descstats"));

            }
            case WALL:{
                Tower lvl1 = new TowerWall1();
                Tower lvl2 = new TowerWall2();
                Tower lvl3 = new TowerWall3();
                Tower b1 = new TowerWallRunic();
                Tower b2 = new TowerWallSpiked();
                return Messages.get(TowerInfo.class, sometower.name().toLowerCase(Locale.ROOT)+"_desc",
                        lvl1.HT,
                        lvl2.HT,
                        lvl3.HT,
                        lvl1.defMin,
                        lvl1.defMax,
                        lvl2.defMin,
                        lvl2.defMax,
                        lvl3.defMin,
                        lvl3.defMax,
                        lvl1.cost,
                        lvl2.cost,
                        lvl3.cost,
                        Messages.get(TowerWall1.class, "descstats"),
                        "_" + b1.name() + "_" + "\n" + b1.info(),
                        "_" + b2.name() + "_" + "\n" + b2.info());
            }
            case CANNON:{
                Tower lvl1 = new TowerCannon1();
                Tower lvl2 = new TowerCannon2();
                Tower lvl3 = new TowerCannon3();
                Tower b1 = new TowerCannonNuke();
                Tower b2 = new TowerCannonMissileLauncher();
                return Messages.get(TowerInfo.class, sometower.name().toLowerCase(Locale.ROOT)+"_desc",
                        lvl1.HT,
                        lvl2.HT,
                        lvl3.HT,
                        lvl1.damageMin,
                        lvl1.damageMax,
                        lvl2.damageMin,
                        lvl2.damageMax,
                        lvl3.damageMin,
                        lvl3.damageMax,
                        Math.round(lvl1.attackSkill(hero)*10),
                        Math.round(lvl2.attackSkill(hero)*10),
                        Math.round(lvl3.attackSkill(hero)*10),
                        Math.round(100/lvl1.baseAttackDelay),
                        Math.round(100/lvl2.baseAttackDelay),
                        Math.round(100/lvl3.baseAttackDelay),
                        ((TowerCShooting)lvl1).attackRange,
                        ((TowerCShooting)lvl2).attackRange,
                        ((TowerCShooting)lvl3).attackRange,
                        lvl1.cost,
                        lvl2.cost,
                        lvl3.cost,
                        Messages.get(TowerCannon1.class, "descstats"),
                        "_" + b1.name() + "_" + "\n" + b1.info(),
                        "_" + b2.name() + "_" + "\n" + b2.info());
            }
            case GRAVE:{
                TowerGrave1 lvl1 = new TowerGrave1();
                TowerGrave2 lvl2 = new TowerGrave2();
                TowerGrave3 lvl3 = new TowerGrave3();
                TowerGraveElite b1 = new TowerGraveElite();
                TowerGraveCrypt b2 = new TowerGraveCrypt();
                return Messages.get(TowerInfo.class, sometower.name().toLowerCase(Locale.ROOT)+"_desc",
                        lvl1.HT,
                        lvl2.HT,
                        lvl3.HT,
                        lvl1.maxMinions,
                        lvl2.maxMinions,
                        lvl3.maxMinions,
                        lvl1.minionCooldown,
                        lvl2.minionCooldown,
                        lvl3.minionCooldown,
                        lvl1.minionHP,
                        lvl2.minionHP,
                        lvl3.minionHP,
                        lvl1.minionDamageMin,
                        lvl1.minionDamageMax,
                        lvl2.minionDamageMin,
                        lvl2.minionDamageMax,
                        lvl3.minionDamageMin,
                        lvl3.minionDamageMax,
                        lvl1.minionAttackSkill*10,
                        lvl2.minionAttackSkill*10,
                        lvl3.minionAttackSkill*10,
                        lvl1.minionDefenseSkill*20,
                        lvl2.minionDefenseSkill*20,
                        lvl3.minionDefenseSkill*20,
                        lvl1.minionDR,
                        lvl2.minionDR,
                        lvl3.minionDR,
                        lvl1.cost,
                        lvl2.cost,
                        lvl3.cost,
                        Messages.get(TowerGrave1.class, "descstats"),
                        "_" + b1.name() + "_" + "\n" + b1.infoWithoutCurrent(),
                        "_" + b2.name() + "_" + "\n" + b2.infoWithoutCurrent());
            }
            case GUARD:{
                TowerGuard1 lvl1 = new TowerGuard1();
                TowerGuard2 lvl2 = new TowerGuard2();
                TowerGuard3 lvl3 = new TowerGuard3();
                TowerGuardSpearman b1 = new TowerGuardSpearman();
                TowerGuardPaladin b2 = new TowerGuardPaladin();
                return Messages.get(TowerInfo.class, sometower.name().toLowerCase(Locale.ROOT)+"_desc",
                        lvl1.HT,
                        lvl2.HT,
                        lvl3.HT,
                        lvl1.damageMin,
                        lvl1.damageMax,
                        lvl2.damageMin,
                        lvl2.damageMax,
                        lvl3.damageMin,
                        lvl3.damageMax,
                        Math.round(lvl1.attackSkill(hero)*10),
                        Math.round(lvl2.attackSkill(hero)*10),
                        Math.round(lvl3.attackSkill(hero)*10),
                        Math.round(100/lvl1.baseAttackDelay),
                        Math.round(100/lvl2.baseAttackDelay),
                        Math.round(100/lvl3.baseAttackDelay),
                        lvl1.viewDistance,
                        lvl2.viewDistance,
                        lvl3.viewDistance,
                        lvl1.defMin,
                        lvl1.defMax,
                        lvl2.defMin,
                        lvl2.defMax,
                        lvl3.defMin,
                        lvl3.defMax,
                        lvl1.regenNum,
                        lvl2.regenNum,
                        lvl3.regenNum,
                        lvl1.cost,
                        lvl2.cost,
                        lvl3.cost,
                        Messages.get(TowerGuard1.class, "descstats"),
                        "_" + b1.name() + "_" + "\n" + b1.info(),
                        "_" + b2.name() + "_" + "\n" + b2.info());
            }
            case LIGHTNING:{
                TowerLightning1 lvl1 = new TowerLightning1();
                TowerLightning2 lvl2 = new TowerLightning2();
                TowerLightning3 lvl3 = new TowerLightning3();
                return Messages.get(TowerInfo.class, sometower.name().toLowerCase(Locale.ROOT)+"_desc",
                        lvl1.HT,
                        lvl2.HT,
                        lvl3.HT,
                        lvl1.damageMin,
                        lvl1.damageMax,
                        lvl2.damageMin,
                        lvl2.damageMax,
                        lvl3.damageMin,
                        lvl3.damageMax,
                        //Math.round(lvl1.attackSkill(hero)*10),
                        //Math.round(lvl2.attackSkill(hero)*10),
                        //Math.round(lvl3.attackSkill(hero)*10),
                        Math.round(100/lvl1.baseAttackDelay),
                        Math.round(100/lvl2.baseAttackDelay),
                        Math.round(100/lvl3.baseAttackDelay),
                        ((TowerCShooting)lvl1).attackRange,
                        ((TowerCShooting)lvl2).attackRange,
                        ((TowerCShooting)lvl3).attackRange,
                        lvl1.maxChainLength,
                        lvl2.maxChainLength,
                        lvl3.maxChainLength,
                        lvl1.cost,
                        lvl2.cost,
                        lvl3.cost,
                        Messages.get(TowerLightning1.class, "descstats"));
            }
            case TOTEM:{
                TowerTotem t1 = new TowerTotem.TotemHealing();
                TowerTotem t2 = new TowerTotem.TotemShield();
                TowerTotem t3 = new TowerTotem.TotemNecrotic();
                TowerTotem t4 = new TowerTotem.TotemAttack();

                return Messages.get(TowerInfo.class, sometower.name().toLowerCase(Locale.ROOT)+"_desc",
                        t1.name(),
                        t1.info(),
                        t2.name(),
                        t2.info(),
                        t3.name(),
                        t3.info(),
                        t4.name(),
                        t4.info());
            }
            case PYLON:return Messages.get(TowerInfo.class, sometower.name().toLowerCase(Locale.ROOT)+"_desc");
            case MINER:return Messages.get(TowerInfo.class, sometower.name().toLowerCase(Locale.ROOT)+"_desc");

            case DARTGUN:{
                Tower lvl1 = new TowerDartgun1();
                Tower lvl2 = new TowerDartgun2();
                Tower lvl3 = new TowerDartgun3();
                Tower b1 = new TowerDartgunSpitter();
                Tower b2 = new TowerDartgunSniper();
                return Messages.get(TowerInfo.class, sometower.name().toLowerCase(Locale.ROOT)+"_desc",
                        lvl1.HT,
                        lvl2.HT,
                        lvl3.HT,
                        lvl1.damageMin,
                        lvl1.damageMax,
                        lvl2.damageMin,
                        lvl2.damageMax,
                        lvl3.damageMin,
                        lvl3.damageMax,
                        Math.round(lvl1.attackSkill(hero)*10),
                        Math.round(lvl2.attackSkill(hero)*10),
                        Math.round(lvl3.attackSkill(hero)*10),
                        Math.round(100/lvl1.baseAttackDelay),
                        Math.round(100/lvl2.baseAttackDelay),
                        Math.round(100/lvl3.baseAttackDelay),
                        ((TowerCShooting)lvl1).attackRange,
                        ((TowerCShooting)lvl2).attackRange,
                        ((TowerCShooting)lvl3).attackRange,
                        lvl1.cost,
                        lvl2.cost,
                        lvl3.cost,
                        "_" + b1.name() + "_" + "\n" + b1.info(),
                        "_" + b2.name() + "_" + "\n" + b2.info());
            }
            case TNTLOG:{
                Tower lvl1 = new TowerTntLog();
                return Messages.get(TowerInfo.class, sometower.name().toLowerCase(Locale.ROOT)+"_desc",
                        lvl1.HT,
                        lvl1.damageMin,
                        lvl1.damageMax,
                        lvl1.cost,
                        Messages.get(TowerTntLog.class, "descstats"));
            }
            case DISINTEGRATION:{
                Tower lvl1 = new TowerDisintegration1();
                Tower lvl2 = new TowerDisintegration2();
                Tower lvl3 = new TowerDisintegration3();
                return Messages.get(TowerInfo.class, sometower.name().toLowerCase(Locale.ROOT)+"_desc",
                        lvl1.HT,
                        lvl2.HT,
                        lvl3.HT,
                        lvl1.damageMin,
                        lvl1.damageMax,
                        lvl2.damageMin,
                        lvl2.damageMax,
                        lvl3.damageMin,
                        lvl3.damageMax,
                        //Math.round(lvl1.attackSkill(hero)*10),
                        //Math.round(lvl2.attackSkill(hero)*10),
                        //Math.round(lvl3.attackSkill(hero)*10),
                        Math.round(100/lvl1.baseAttackDelay),
                        Math.round(100/lvl2.baseAttackDelay),
                        Math.round(100/lvl3.baseAttackDelay),
                        ((TowerCShooting)lvl1).attackRange,
                        ((TowerCShooting)lvl2).attackRange,
                        ((TowerCShooting)lvl3).attackRange,
                        lvl1.cost,
                        lvl2.cost,
                        lvl3.cost,
                        Messages.get(TowerWand1.class, "descstats"));
            }
            case RATCAMP:{
                Tower lvl1 = new TowerRatCamp();
                CampRat rat1 = new CampRatKnife();
                CampRat rat2 = new CampRatLeader();
                CampRat rat3 = new CampRatShield();
                CampRat rat4 = new CampRatArcher();
                CampRat rat5 = new CampRatMage();


                return Messages.get(TowerInfo.class, sometower.name().toLowerCase(Locale.ROOT)+"_desc",

                        rat1.name(), rat1.description(),
                        rat1.HT,
                        rat1.damageMin, rat1.damageMax,
                        rat1.defMin,rat1.defMax,
                        rat1.cost,

                        rat2.name(), rat2.description(),
                        rat2.HT,
                        rat2.damageMin, rat2.damageMax,
                        rat2.defMin,rat2.defMax,
                        rat2.cost,

                        rat3.name(), rat3.description(),
                        rat3.HT,
                        rat3.damageMin, rat3.damageMax,
                        rat3.defMin,rat3.defMax,
                        rat3.cost,

                        rat4.name(), rat4.description(),
                        rat4.HT,
                        rat4.damageMin, rat4.damageMax,
                        rat4.defMin,rat4.defMax,
                        rat4.cost,

                        rat5.name(), rat5.description(),
                        rat5.HT,
                        rat5.damageMin, rat5.damageMax,
                        rat5.defMin,rat5.defMax,
                        rat5.cost


                        );

            }
            case LOCKED: return Messages.get(TowerInfo.class, "locked_desc");
            case UNLOCKED: return Messages.get(TowerInfo.class, "unlocked_desc");
            case DUNGEON: return Messages.get(TowerInfo.class, "dungeon_desc");
        }

        return "OH NO FIX SCREWED UP AGAIN";
    }
    public static Item getTowerSpawner(AllTowers sometower){
        switch (sometower){
            case CANNON: return new SpawnerCannon();
            case CROSSBOW: return new SpawnerCrossbow();
            case MAGICMISSILE: return new SpawnerWand();
            case WALL: return new SpawnerWall();
            case GRAVE: return new SpawnerGrave();
            case GUARD: return new SpawnerGuard();
            case DISINTEGRATION: return new SpawnerDisintegration();
            case LIGHTNING: return new SpawnerLightning();
            case TNTLOG: return new SpawnerTntlog();
            case DARTGUN: return new SpawnerDartgun();
            case RATCAMP: return new SpawnerCamp();
        }
        return new Berry();
    }
    public static Class <? extends Mob> getTowerClass(AllTowers sometower){
        switch (sometower){
            case CANNON: return TowerCannon1.class;
            case CROSSBOW: return TowerCrossbow1.class;
            case MAGICMISSILE: return TowerWand1.class;
            case WALL: return TowerWall1.class;
            case GRAVE: return TowerGrave1.class;
            case GUARD: return TowerGuard1.class;
            case DISINTEGRATION: return TowerDisintegration1.class;
            case LIGHTNING: return TowerLightning1.class;
            case TNTLOG: return TowerTntLog.class;
            case DARTGUN: return TowerDartgun1.class;
            case LOCKED: return ChiefRat.class;
            case UNLOCKED: return RatKing.class;
            case DUNGEON: return Albino.class;
            case TOTEM: return TowerTotem.TotemShield.class;
            case MINER: return TowerMiner.class;
            case PYLON: return TowerPylonBroken.class;
            case RATCAMP: return TowerRatCamp.class;
        }
        return TowerPylonBroken.class;
    }
    public static Tower getTowerNewInstance(AllTowers sometower){
        switch (sometower){
            case CANNON: return new TowerCannon1();
            case CROSSBOW: return new TowerCrossbow1();
            case MAGICMISSILE: return new TowerWand1();
            case WALL: return new TowerWall1();
            case GRAVE: return new TowerGrave1();
            case GUARD: return new TowerGuard1();
            case DISINTEGRATION: return new TowerDisintegration1();
            case LIGHTNING: return new TowerLightning1();
            case TNTLOG: return new TowerTntLog();
            case DARTGUN: return new TowerDartgun1();
            case LOCKED:
            case UNLOCKED:
            case DUNGEON: return new TowerPylonBroken();
            case TOTEM: return new TowerTotem.TotemShield();
            case MINER: return new TowerMiner();
            case PYLON: return new TowerPylonBroken();
            case RATCAMP: return new TowerRatCamp();
        }
        return new TowerPylonBroken();
    }


}
