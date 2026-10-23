package com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.fighter;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;

public class RBAMoveRight extends RBAMove {
    public static final RBAMoveRight INSTANCE = new RBAMoveRight();

    @Override
    public int icon() {
        return HeroIcon.RBA_FIGHTER_RIGHT;
    }

    @Override
    public int targetPos(Hero hero) {
        return hero.pos + 1;
    }
}
