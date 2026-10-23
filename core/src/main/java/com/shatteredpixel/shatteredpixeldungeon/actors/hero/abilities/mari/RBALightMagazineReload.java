package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mari;


import com.shatteredpixel.shatteredpixeldungeon.actors.hero.RBAHero;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.miracles.RBALightBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.RBAGun;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.utils.Callback;

public class RBALightMagazineReload extends ArmorAbility {
    {
        baseChargeUse = 25f;
    }

    @Override
    public int icon() {
        return HeroIcon.RBA_MARI_1;
    }

    @Override
    protected void activate(ClassArmor armor, Hero hero, Integer target) {
        if (!(hero.belongings.weapon instanceof RBAGun)) {
            RBAHero.yellW(hero, "need_gun");
            return;
        }

        RBAGun gun = (RBAGun) hero.belongings.weapon;

        if (gun.round() == gun.maxRound()) {
            RBAHero.yellW(hero, "already_loaded");
            return;
        }

        hero.busy();
        hero.sprite.operate(hero.pos, new Callback() {
            @Override
            public void call() {
                hero.sprite.idle();
                hero.next();
            }
        });

        int bulletsLoaded = gun.maxRound() - gun.round();
        Buff.affect(hero, RBALightBullet.LightMagazine.class).stack(gun, bulletsLoaded);
        gun.quickReload();

        armor.charge -= chargeUse(hero);
        armor.updateQuickslot();
    }

    @Override
    public Talent[] talents() {
        return new Talent[]{Talent.RBA_MARI_ARMOR1_1, Talent.RBA_MARI_ARMOR1_2, Talent.RBA_MARI_ARMOR1_3, Talent.HEROIC_ENERGY};
    }
}
