package com.shatteredpixel.shatteredpixeldungeon.actors.hero.shops;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.active.console.RBAConsole;
import com.shatteredpixel.shatteredpixeldungeon.items.active.console.RBASandboxConsole;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;

public class RBABuySandboxConsole extends RBAYuzuShopContent {
    public static final RBABuySandboxConsole INSTANCE = new RBABuySandboxConsole();

    @Override
    public int icon() {
        return HeroIcon.RBA_SANDBOX_CONSOLE;
    }

    @Override
    public void onSelect(Hero hero) {
        RBAConsole prize = new RBASandboxConsole();
        hero.spend(-prize.pickupDelay());
        if (!prize.doPickUp(hero)) {
            Dungeon.level.drop(prize, hero.pos);
        }
    }

    @Override
    public int creditUse(Hero hero) {
        return 300*inflationParameter();
    }

    @Override
    public boolean canSelect(Hero hero) {
        return super.canSelect(hero) && hero.hasTalent(Talent.RBA_YUZU_EX2_3);
    }
}
