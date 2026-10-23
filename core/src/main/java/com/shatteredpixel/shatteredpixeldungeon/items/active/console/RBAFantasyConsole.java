package com.shatteredpixel.shatteredpixeldungeon.items.active.console;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.RBAYuzuConsoleContent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.fantasy.RBAFantasyConsoleContent;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class RBAFantasyConsole extends RBAConsole {
    {
        image = ItemSpriteSheet.RBA_FANTASY_CONSOLE;
    }

    @Override
    public void showWindow(Hero hero) {
        for (Buff b: hero.buffs()) {
            if (b instanceof RBAYuzuConsoleContent.ConsoleBuff && !(b instanceof RBAFantasyConsoleContent.FantasyConsoleBuff)) {
                b.detach();
            }
        }
        if (hero.buff(RBAFantasyConsoleContent.FantasyConsoleBuff.class) != null) {
            hero.buff(RBAFantasyConsoleContent.FantasyConsoleBuff.class).enhance();
        }
        Buff.affect(hero, RBAFantasyConsoleContent.FantasyConsoleBuff.class).set();
        BuffIndicator.refreshHero();
        super.showWindow(hero);
        detach(hero.belongings.backpack);
    }

    @Override
    public int value() {
        return 300/5;
    }

}
