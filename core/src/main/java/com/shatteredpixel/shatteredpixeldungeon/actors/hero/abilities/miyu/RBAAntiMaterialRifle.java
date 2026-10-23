package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.miyu;


import com.shatteredpixel.shatteredpixeldungeon.actors.hero.RBAHero;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.RBAGun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.RBASR_SP;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class RBAAntiMaterialRifle extends ArmorAbility {
    {
        baseChargeUse = 50f;
    }

    @Override
    public int icon() {
        return HeroIcon.RBA_MIYU_3;
    }

    @Override
    protected void activate(ClassArmor armor, Hero hero, Integer target) {
        if (target == null) return;
        if (!(hero.belongings.weapon() instanceof RBAGun)) {
            RBAHero.yellW(hero, "need_gun");
            return;
        }

        if (hero.buff(GotRifleTracker.class) != null) {
            RBAHero.yellW(hero, "already_got");
            return;
        }

        RBAGun gun = (RBAGun) hero.belongings.weapon();
        RBASR_SP sr = new RBASR_SP();
        int level;
        if (gun.curseInfusionBonus) {
            gun.curseInfusionBonus = false;
            level = gun.level();
            gun.curseInfusionBonus = true;
        } else {
            level = gun.level();
        }
        sr.upgrade(level);
        sr.enchantment          = gun.enchantment;
        sr.curseInfusionBonus   = gun.curseInfusionBonus;
        sr.masteryPotionBonus   = gun.masteryPotionBonus;
        sr.levelKnown           = gun.levelKnown;
        sr.cursedKnown          = gun.cursedKnown;
        sr.cursed               = gun.cursed;
        sr.augment              = gun.augment;
        sr.enchantHardened      = gun.enchantHardened;
        sr.keptThoughLostInvent = gun.keptThoughLostInvent;
        sr.barrelMod = gun.barrelMod;
        sr.magazineMod = gun.magazineMod;
        sr.bulletMod = gun.bulletMod;
        sr.weightMod = gun.weightMod;
        sr.attachMod = gun.attachMod;
        sr.enchantMod = gun.enchantMod;
        sr.inscribeMod = gun.inscribeMod;
        sr.set(gun);
        if (hero.hasTalent(Talent.RBA_MIYU_ARMOR3_3)) {
            switch (hero.pointsInTalent(Talent.RBA_MIYU_ARMOR3_3)) {
                case 1: default:
                    if (Random.Float() < 0.5f) {
                        sr.manualReload(1, true);
                        gun.quickReload();
                    }
                    break;
                case 2:
                    sr.manualReload(1, true);
                    gun.quickReload();
                    break;
                case 3:
                    sr.manualReload(1, true);
                    gun.quickReload();
                    if (Random.Float() < 0.5f) {
                        sr.manualReload(2, true);
                        gun.manualReload(1, true);
                    }
                    break;
                case 4:
                    sr.manualReload(2, true);
                    gun.quickReload();
                    gun.manualReload(2, true);
                    break;
            }
        }
        hero.belongings.weapon = sr;
        int slot = Dungeon.quickslot.getSlot(gun);
        if (slot != -1
                && sr.defaultAction() != null){
            Dungeon.quickslot.setSlot(slot, sr);
        }
        RBAHero.yellI(hero, "switching_new");
        Item.updateQuickslot();

        Buff.affect(hero, GotRifleTracker.class);

        hero.sprite.operate(hero.pos);
        Sample.INSTANCE.play(Assets.Sounds.UNLOCK);

        Invisibility.dispel();
        armor.charge -= chargeUse(hero);
        armor.updateQuickslot();
    }

    @Override
    public Talent[] talents() {
        return new Talent[]{Talent.RBA_MIYU_ARMOR3_1, Talent.RBA_MIYU_ARMOR3_2, Talent.RBA_MIYU_ARMOR3_3, Talent.HEROIC_ENERGY};
    }

    public static class GotRifleTracker extends Buff {
        {revivePersists = true;}
    }

    public static class QuickSlotSet extends FlavourBuff {
        RBAGun gun;
        int slot = -1;

        public void set(RBAGun gun, int slot) {
            this.gun = gun;
            this.slot = slot;
        }

        @Override
        public void detach() {
            if (slot != -1) Dungeon.quickslot.setSlot(slot, gun);
            super.detach();
        }

        private static final String GUN = "gun";
        private static final String SLOT = "slot";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(GUN, gun);
            bundle.put(SLOT, slot);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            gun = (RBAGun) bundle.get(GUN);
            slot = bundle.getInt(SLOT);
        }
    }
}
