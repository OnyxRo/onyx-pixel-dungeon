package com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.fighter;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;

public class RBAMoveUp extends RBAMove {
    public static final RBAMoveUp INSTANCE = new RBAMoveUp();

    @Override
    public int icon() {
        return HeroIcon.RBA_FIGHTER_UP;
    }

    @Override
    public int targetPos(Hero hero) {
        return hero.pos - Dungeon.level.width();
    }
}
