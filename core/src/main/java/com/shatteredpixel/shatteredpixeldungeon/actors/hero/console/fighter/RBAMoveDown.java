package com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.fighter;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;

public class RBAMoveDown extends RBAMove {
    public static final RBAMoveDown INSTANCE = new RBAMoveDown();

    @Override
    public int icon() {
        return HeroIcon.RBA_FIGHTER_DOWN;
    }

    @Override
    public int targetPos(Hero hero) {
        return hero.pos + Dungeon.level.width();
    }
}
