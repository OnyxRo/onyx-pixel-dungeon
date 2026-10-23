package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mika;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.food.RBARollCake;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;

public class RBARollCakeThrow extends ArmorAbility {
    {
        baseChargeUse = 25f;
    }

    @Override
    public int icon() {
        return HeroIcon.RBA_MIKA_1;
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void activate(ClassArmor armor, Hero hero, Integer target) {
        if (target == null) return;

        RBARollCake rollCake = new RBARollCake();
        if (target == hero.pos) {
            rollCake.execute(hero);
        } else {
            rollCake.cast(hero, target);
        }

        armor.charge -= chargeUse(hero);
        Item.updateQuickslot();
        Invisibility.dispel();
    }

    @Override
    public Talent[] talents() {
        return new Talent[]{Talent.RBA_MIKA_ARMOR1_1, Talent.RBA_MIKA_ARMOR1_2, Talent.RBA_MIKA_ARMOR1_3, Talent.HEROIC_ENERGY};
    }
}
