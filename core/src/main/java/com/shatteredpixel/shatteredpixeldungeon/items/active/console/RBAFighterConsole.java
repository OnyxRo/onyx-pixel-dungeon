package com.shatteredpixel.shatteredpixeldungeon.items.active.console;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.RBAYuzuConsoleContent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.fantasy.RBAFantasyConsoleContent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.fighter.RBAFighterConsoleContent;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.windows.RBAWndYuzuFighterConsole;

public class RBAFighterConsole extends RBAConsole {
    {
        image = ItemSpriteSheet.RBA_FIGHTER_CONSOLE;
    }

    @Override
    public void showWindow(Hero hero) {
        for (Buff b: hero.buffs()) {
            if (b instanceof RBAYuzuConsoleContent.ConsoleBuff && !(b instanceof RBAFighterConsoleContent.FighterConsoleBuff)) {
                b.detach();
            }
        }
        Buff.affect(hero, RBAFighterConsoleContent.FighterConsoleBuff.class).set();
        BuffIndicator.refreshHero();
        GameScene.show(new RBAWndYuzuFighterConsole(this, hero, false));
        detach(hero.belongings.backpack);
    }

    @Override
    public int value() {
        return 300/5;
    }
}
