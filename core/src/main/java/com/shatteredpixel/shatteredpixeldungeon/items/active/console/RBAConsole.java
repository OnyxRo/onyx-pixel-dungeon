package com.shatteredpixel.shatteredpixeldungeon.items.active.console;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.RBAWndYuzuConsole;

import java.util.ArrayList;

public class RBAConsole extends Item {

    public static final String AC_USE		= "USE";

    {
        stackable = true;
        levelKnown = true;
        bones = false;
        defaultAction = AC_USE;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add( AC_USE );
        return actions;
    }

    public void showWindow(Hero hero) {
        GameScene.show(new RBAWndYuzuConsole(this, hero, false));
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

        if (action.equals(AC_USE)) {
            showWindow(hero);
        }
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }
}
