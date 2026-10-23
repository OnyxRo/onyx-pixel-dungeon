package com.shatteredpixel.shatteredpixeldungeon.actors.hero.miracles;


import com.shatteredpixel.shatteredpixeldungeon.actors.hero.RBATalents;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RBACrossNecklace;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.RBAGun;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class RBAMariMiracle {

    public static final HashSet<Class> MIRACLE_DAMAGE = new HashSet<>();
    static {
        MIRACLE_DAMAGE.add( PunishmentThunder.class );
        MIRACLE_DAMAGE.add( RBALightBullet.class );
        MIRACLE_DAMAGE.add( RBASilverBullet.class );
        MIRACLE_DAMAGE.add( RBAHolyBullet.class );
        MIRACLE_DAMAGE.add( RBAThunderLance.class );
        MIRACLE_DAMAGE.add( RBABaptize.class );
    }

    public abstract void onCast(RBACrossNecklace cross, Hero hero);

    public float chargeUse( Hero hero ){
        return 1;
    }

    public boolean canCast( Hero hero ){
        return true;
    }

    public String name(){
        return Messages.get(this, "name");
    }

    public String shortDesc(){
        return Messages.get(this, "short_desc") + " " + Messages.get(this, "charge_cost", (int)chargeUse(Dungeon.hero));
    }

    public String desc(){
        return Messages.get(this, "desc") + "\n\n" + Messages.get(this, "charge_cost", (int)chargeUse(Dungeon.hero));
    }

    public boolean usesTargeting(){
        return false;
    }

    public int targetingFlags(){
        return -1; //-1 for no targeting
    }

    public int icon(){
        return HeroIcon.NONE;
    }

    public void onMiracleCast(RBACrossNecklace cross, Hero hero){
        Invisibility.dispel();

        if (hero.hasTalent(Talent.RBA_MARI_T1_1) && hero.buff(RBATalents.BlessOfPlentyTracker.class) != null){
            int amount = 1 + 2*hero.pointsInTalent(Talent.RBA_MARI_T1_1);
            Buff.affect(hero, Barrier.class).setShield(amount);
            hero.buff(RBATalents.BlessOfPlentyTracker.class).detach();
        }

        cross.spendCharge(chargeUse(hero));
        Talent.onArtifactUsed(hero);
    }

    public static RBAGun getEquippedGun(Hero hero) {
        if (!(hero.belongings.weapon instanceof RBAGun)) {
            return null;
        } else {
            return (RBAGun) hero.belongings.weapon;
        }
    }

    public static ArrayList<RBAMariMiracle> getMiracleList(Hero mari, int tier){
        ArrayList<RBAMariMiracle> miracles = new ArrayList<>();

        if (tier == 1) {
            miracles.add(RBALightBullet.INSTANCE);
            miracles.add(RBAProtection.INSTANCE);
            miracles.add(RBAPray.INSTANCE);

            if (mari.hasTalent(Talent.RBA_MARI_T1_2)) {
                miracles.add(RBAUnholyDetection.INSTANCE);
            }

        } else if (tier == 2) {
            if (mari.hasTalent(Talent.RBA_MARI_T2_3)) {
                miracles.add(RBASilverBullet.INSTANCE);
            }
            if (mari.hasTalent(Talent.RBA_MARI_T2_4)) {
                miracles.add(RBAMindBind.INSTANCE);
            }
            if (mari.hasTalent(Talent.RBA_MARI_T2_5)) {
                miracles.add(RBABlessOfCharity.INSTANCE);
            }

        } else if (tier == 3){
            if (mari.hasTalent(Talent.RBA_MARI_T3_1)) {
                miracles.add(RBAPurification.INSTANCE);
            }
            if (mari.subClass == HeroSubClass.RBA_DEVOUT_PRAYER) {
                miracles.add(RBAHolyBullet.INSTANCE);
            }
            if (mari.hasTalent(Talent.RBA_MARI_EX1_1)) {
                miracles.add(RBAHolyBlast.INSTANCE);
            }
            if (mari.hasTalent(Talent.RBA_MARI_EX1_2)) {
                miracles.add(RBAThunderLance.INSTANCE);
            }
            if (mari.hasTalent(Talent.RBA_MARI_EX1_3)) {
                miracles.add(RBAHolyMantle.INSTANCE);
            }
            if (mari.subClass == HeroSubClass.RBA_SYMBOL_OF_PEACE) {
                miracles.add(RBAHealingTouch.INSTANCE);
            }
            if (mari.hasTalent(Talent.RBA_MARI_EX2_1)) {
                miracles.add(RBABaptize.INSTANCE);
            }
            if (mari.hasTalent(Talent.RBA_MARI_EX2_2)) {
                miracles.add(RBASalvation.INSTANCE);
            }
            if (mari.hasTalent(Talent.RBA_MARI_EX2_3)) {
                miracles.add(RBARepent.INSTANCE);
            }

        } else if (tier == 4){
            if (mari.hasTalent(Talent.RBA_MARI_ARMOR1_1)) {
                miracles.add(RBAFlash.INSTANCE);
            }
            if (mari.hasTalent(Talent.RBA_MARI_ARMOR1_2)) {
                miracles.add(RBABlessOfLight.INSTANCE);
            }
            if (mari.hasTalent(Talent.RBA_MARI_ARMOR1_3)) {
                miracles.add(RBAPunishmentBullet.INSTANCE);
            }
            if (mari.hasTalent(Talent.RBA_MARI_ARMOR2_1)) {
                miracles.add(RBAHolyBody.INSTANCE);
            }
            if (mari.hasTalent(Talent.RBA_MARI_ARMOR2_2)) {
                miracles.add(RBAHolyMind.INSTANCE);
            }
            if (mari.hasTalent(Talent.RBA_MARI_ARMOR2_3)) {
                miracles.add(RBAHolySoul.INSTANCE);
            }
            if (mari.hasTalent(Talent.RBA_MARI_ARMOR3_1)) {
                miracles.add(RBAEncore.INSTANCE);
            }
            if (mari.hasTalent(Talent.RBA_MARI_ARMOR3_2)) {
                miracles.add(RBACharityKiss.INSTANCE);
            }
            if (mari.hasTalent(Talent.RBA_MARI_ARMOR3_3)) {
                miracles.add(RBAHeartOverflow.INSTANCE);
            }


        }

        return miracles;
    }

    public static ArrayList<RBAMariMiracle> getAllMiracles() {
        ArrayList<RBAMariMiracle> miracles = new ArrayList<>();
        miracles.add(RBALightBullet.INSTANCE);
        miracles.add(RBAProtection.INSTANCE);
        miracles.add(RBAPray.INSTANCE);
        miracles.add(RBAUnholyDetection.INSTANCE);
        miracles.add(RBASilverBullet.INSTANCE);
        miracles.add(RBAMindBind.INSTANCE);
        miracles.add(RBABlessOfCharity.INSTANCE);
        miracles.add(RBAPurification.INSTANCE);
        miracles.add(RBAHolyBullet.INSTANCE);
        miracles.add(RBAHolyBlast.INSTANCE);
        miracles.add(RBAThunderLance.INSTANCE);
        miracles.add(RBAHolyMantle.INSTANCE);
        miracles.add(RBAHealingTouch.INSTANCE);
        miracles.add(RBABaptize.INSTANCE);
        miracles.add(RBASalvation.INSTANCE);
        miracles.add(RBARepent.INSTANCE);
        miracles.add(RBAFlash.INSTANCE);
        miracles.add(RBABlessOfLight.INSTANCE);
        miracles.add(RBAPunishmentBullet.INSTANCE);
        miracles.add(RBAHolyBody.INSTANCE);
        miracles.add(RBAHolyMind.INSTANCE);
        miracles.add(RBAHolySoul.INSTANCE);
        miracles.add(RBAEncore.INSTANCE);
        miracles.add(RBACharityKiss.INSTANCE);
        miracles.add(RBAHeartOverflow.INSTANCE);

        return miracles;
    }

    //this is in the miracle class, but it is used for Mari's passive ability
    public static class PunishmentThunder {

        public static int thunderDamage(Hero hero) {

            int damage = (int)Math.ceil(hero.lvl/5f);

            if (hero.buff(RBAProtection.ProtectionBuff.class) != null) {
                damage += hero.buff(RBAProtection.ProtectionBuff.class).thunderDamageBonus(hero);

                if (hero.subClass == HeroSubClass.RBA_DEVOUT_PRAYER) {
                    RBAGun gun = getEquippedGun(hero);
                    if (gun != null) {
                        Buff.affect(hero, RBALightBullet.LightMagazine.class).stack(gun, 1);
                    }
                }
            }

            return damage;
        }
    }

}
