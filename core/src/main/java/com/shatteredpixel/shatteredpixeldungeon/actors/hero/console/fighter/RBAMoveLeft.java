package com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.fighter;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;

public class RBAMoveLeft extends RBAMove {
    public static final RBAMoveLeft INSTANCE = new RBAMoveLeft();

    @Override
    public int icon() {
        return HeroIcon.RBA_FIGHTER_LEFT;
    }

    @Override
    public int targetPos(Hero hero) {
        return hero.pos - 1;
    }
}
