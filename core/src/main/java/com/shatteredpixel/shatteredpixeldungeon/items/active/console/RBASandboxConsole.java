package com.shatteredpixel.shatteredpixeldungeon.items.active.console;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.RBAYuzuConsoleContent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.fantasy.RBAFantasyConsoleContent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.sandbox.RBASandboxConsoleContent;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class RBASandboxConsole extends RBAConsole {
    {
        image = ItemSpriteSheet.RBA_SNADBOX_CONSOLE;
    }

    @Override
    public void showWindow(Hero hero) {
        for (Buff b: hero.buffs()) {
            if (b instanceof RBAYuzuConsoleContent.ConsoleBuff && !(b instanceof RBASandboxConsoleContent.SandboxConsoleBuff)) {
                b.detach();
            }
        }
        int tokenBonus = Math.max(0, -4+3*hero.pointsInTalent(Talent.RBA_YUZU_EX2_3)); //+0/2/5
        Buff.affect(hero, RBASandboxConsoleContent.SandboxConsoleBuff.class).set(RBAYuzuConsoleContent.ConsoleBuff.MAX_COUNT + tokenBonus);
        BuffIndicator.refreshHero();
        super.showWindow(hero);
        detach(hero.belongings.backpack);
    }

    @Override
    public int value() {
        return 300/5;
    }

}
