package com.shatteredpixel.shatteredpixeldungeon.actors.hero.shops;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.active.console.RBAConsole;
import com.shatteredpixel.shatteredpixeldungeon.items.active.console.RBAContinueConsole;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;

public class RBABuyContinueConsole extends RBAYuzuShopContent {
    public static final RBABuyContinueConsole INSTANCE = new RBABuyContinueConsole();

    @Override
    public int icon() {
        return HeroIcon.RBA_CONTINUE_CONSOLE;
    }

    @Override
    public void onSelect(Hero hero) {
        RBAConsole prize = new RBAContinueConsole();
        hero.spend(-prize.pickupDelay());
        if (!prize.doPickUp(hero)) {
            Dungeon.level.drop(prize, hero.pos);
        }
    }

    @Override
    public int creditUse(Hero hero) {
        return 50*inflationParameter();
    }
}
