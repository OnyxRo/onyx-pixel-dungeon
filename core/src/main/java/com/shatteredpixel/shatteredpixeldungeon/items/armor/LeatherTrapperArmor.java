package com.shatteredpixel.shatteredpixeldungeon.items.armor;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class LeatherTrapperArmor extends Armor {

    {
        image = ItemSpriteSheet.ARMOR_LEATHER_TRAP;
    }

    public LeatherTrapperArmor() {
        super(2);
    }

    @Override
    public int STRReq(int lvl) {
        return super.STRReq(lvl) + 1;
    }

    @Override
    public boolean doEquip(Hero hero) {

        if (super.doEquip(hero)) {
            com.shatteredpixel.shatteredpixeldungeon.TrapDungeon.increaseTrapSkill();
            updateQuickslot();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean doUnequip(Hero hero, boolean collect, boolean single) {
        if (super.doUnequip(hero, collect, single)) {
            com.shatteredpixel.shatteredpixeldungeon.TrapDungeon.decreaseTrapSkill();
            updateQuickslot();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String info() {
        return super.info()+"\n\n_Special:_ Increases trapskill by 1 when worn.";
    }
}
