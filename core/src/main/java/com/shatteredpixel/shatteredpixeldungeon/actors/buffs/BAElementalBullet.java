package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.BAHero;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.elementals.BAAPBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.elementals.BAElectricBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.elementals.BAExplosiveBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.elementals.BAIceBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.elementals.BAIncendiaryBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.active.BABicycle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.windows.BAWndElementalBullet;
import com.shatteredpixel.shatteredpixeldungeon.windows.BAWndProfessionalRide;
import com.watabou.utils.Bundle;
public class BAElementalBullet extends Buff implements ActionIndicator.Action {

    float duration = 0;

    {
        type = buffType.NEUTRAL;
        announced = false;
    }

    @Override
    public boolean act() {
        ActionIndicator.setAction(BAElementalBullet.this);
        spend(TICK);
        duration--;
        if (duration < 0) {
            detach();
        }
        return true;
    }

    public void set(float amount) {
        duration = amount;
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        ActionIndicator.setAction(BAElementalBullet.this);
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public void detach() {
        super.detach();
        ActionIndicator.clearAction();
    }

    public enum ElementalReload {
        AP_BULLET		    (0xC7C4C9),
        INCENDIARY_BULLET   (0xFFAA33),
        EXPLOSIVE_BULLET    (0xFF2A00),
        ICE_BULLET  	    (0x5674B9),
        ELECTRIC_BULLET	    (0xFFFFFF);

        public int tintColor;

        ElementalReload(int tintColor){
            this.tintColor = tintColor;
        }

        public String title(){
            return Messages.get(this, name() + ".name");
        }

        public String desc(){
            return Messages.get(this, name() + ".desc", hero.pointsInTalent(Talent.BA_ELEMENTAL_BULLET_1));
        }

    }

    public boolean canUseMove(ElementalReload move){
        return true;
    }

    public void useMove(ElementalReload move){
        switch (move) {
            case AP_BULLET:
                BAHero.yellP(hero, Messages.get(Hero.class, hero.heroClass.name().replace("BA_", "") + "_ap_bullet")); //"철갑탄 장전."
                Buff.affect(hero, BAAPBullet.class);
                break;
            case INCENDIARY_BULLET:
                BAHero.yellP(hero, Messages.get(Hero.class, hero.heroClass.name().replace("BA_", "") + "_incendiary_bullet")); //"소이탄 장전."
                Buff.affect(hero, BAIncendiaryBullet.class);
                break;
            case EXPLOSIVE_BULLET:
                BAHero.yellP(hero, Messages.get(Hero.class, hero.heroClass.name().replace("BA_", "") + "_explosive_bullet"));  //"폭발탄 장전."
                Buff.affect(hero, BAExplosiveBullet.class);
                break;
            case ICE_BULLET:
                BAHero.yellP(hero, Messages.get(Hero.class, hero.heroClass.name().replace("BA_", "") + "_ice_bullet"));    //"빙결탄 장전."
                Buff.affect(hero, BAIceBullet.class);
                break;
            case ELECTRIC_BULLET:
                BAHero.yellP(hero, Messages.get(Hero.class, hero.heroClass.name().replace("BA_", "") + "_electric_bullet"));   //"전격탄 장전."
                Buff.affect(hero, BAElectricBullet.class);
                break;
        }
        hero.sprite.operate(hero.pos);
        if (hero.pointsInTalent(Talent.BA_ELEMENTAL_BULLET_2) < 3) {
            hero.spendAndNext(Actor.TICK);
        }
        detach();
    }


    //액션인디케이터 내용
    @Override
    public String actionName() {
        return Messages.get(this, "action_name");
    }

    @Override
    public int actionIcon() {
        return HeroIcon.BA_ELEMENTAL_BULLET;
    }

    @Override
    public int indicatorColor() {
        return 0xC7C4C9;
    }

    @Override
    public void doAction() {
        GameScene.show(new BAWndElementalBullet(this));
    }
}