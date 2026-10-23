package com.shatteredpixel.shatteredpixeldungeon.actors.hero.miracles;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;

public class RBAHolySoul extends RBATrinityMiracle {

    public static RBAHolySoul INSTANCE = new RBAHolySoul();

    @Override
    public int icon() {
        return HeroIcon.RBA_HOLY_SOUL;
    }

    @Override
    public boolean canCast(Hero hero) {
        return super.canCast(hero) && hero.hasTalent(Talent.RBA_MARI_ARMOR2_3);
    }

    @Override
    public String desc() {
        int amount = 15 * Dungeon.hero.pointsInTalent(Talent.RBA_MARI_ARMOR2_3);
        return Messages.get(this, "desc", amount) + "\n\n" + Messages.get(this, "charge_cost", (int)chargeUse(Dungeon.hero));
    }

    @Override
    public void castEffect(Hero hero) {
        getBuff(hero).shieldCast(hero);
    }
}
