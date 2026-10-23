package com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.fighter;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;

public class RBACharge extends RBAFighterConsoleContent {
    public static final RBACharge INSTANCE = new RBACharge();

    @Override
    public int icon() {
        return HeroIcon.RBA_FIGHTER_CHARGE;
    }

    @Override
    public boolean execute(Hero hero, int target) {
        if (!super.execute(hero, target)) return false;

        if (isEnhanced(hero)) Buff.affect(hero, FighterConsoleBuff.class).countUp(1);
        Buff.affect(hero, FighterConsoleBuff.class).enhance();
        hero.spendAndNext(1f);
        return !isEnhanced(hero);
    }

    @Override
    public boolean canSelect(Hero hero) {
        return super.canSelect(hero) && hero.buff(FighterConsoleBuff.class) != null && hero.buff(FighterConsoleBuff.class).count() > 1;
    }
}
