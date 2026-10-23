package com.shatteredpixel.shatteredpixeldungeon.items.active.console;


import com.shatteredpixel.shatteredpixeldungeon.actors.hero.RBAHero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.fantasy.RBAFantasyConsoleContent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.fighter.RBAFighterConsoleContent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.sandbox.RBASandboxConsoleContent;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.windows.RBAWndYuzuConsole;
import com.shatteredpixel.shatteredpixeldungeon.windows.RBAWndYuzuFighterConsole;

public class RBAContinueConsole extends RBAConsole {
    {
        image = ItemSpriteSheet.RBA_CONTINUE_CONSOLE;
    }

    @Override
    public void showWindow(Hero hero) {
        if (hero.buff(RBAFighterConsoleContent.FighterConsoleBuff.class) != null) {
            GameScene.show(new RBAWndYuzuFighterConsole(new RBAFighterConsole(), hero, false));
            detach(hero.belongings.backpack);
            return;
        } else if (hero.buff(RBAFantasyConsoleContent.FantasyConsoleBuff.class) != null) {
            GameScene.show(new RBAWndYuzuConsole(new RBAFantasyConsole(), hero, false));
            detach(hero.belongings.backpack);
            return;
        } else if (hero.buff(RBASandboxConsoleContent.SandboxConsoleBuff.class) != null) {
            GameScene.show(new RBAWndYuzuConsole(new RBASandboxConsole(), hero, false));
            detach(hero.belongings.backpack);
            return;
        }

        RBAHero.yellW(hero, "no_active_console");
    }

    @Override
    public int value() {
        return 50/5;
    }
}
