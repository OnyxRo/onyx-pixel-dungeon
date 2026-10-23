package com.shatteredpixel.shatteredpixeldungeon.actors.hero.shops;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBAYuzuStatus;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;

public class RBAPayToWin extends RBAYuzuShopContent {
    public static final RBAPayToWin INSTANCE = new RBAPayToWin();

    @Override
    public int icon() {
        return HeroIcon.RBA_SHOP_4;
    }

    @Override
    public void onSelect(Hero hero) {
        Buff.affect(hero, RBAYuzuStatus.CertainCritBuff.class).countUp(1);
        Buff.affect(hero, RBAYuzuStatus.PayToWinBuff.class);
    }

    @Override
    public int creditUse(Hero hero) {
        return 100*inflationParameter();
    }

    @Override
    public boolean canSelect(Hero hero) {
        return super.canSelect(hero) && hero.buff(RBAYuzuStatus.PayToWinBuff.class) == null;
    }

    @Override
    public boolean hideWindow() {
        return true;
    }
}
