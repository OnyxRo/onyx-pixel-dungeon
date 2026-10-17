package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class RunningToReport extends Dread {
    {
        announced = false;
    }

    @Override
    public boolean act() {

        if (!Dungeon.level.heroFOV[target.pos]
                && Dungeon.level.distance(target.pos, hero.pos) >= 6) {
            if (target instanceof Mob){
                ((Mob) target).EXP /= 2;
            }
            target.destroy();
            target.sprite.killAndErase();
            target.die(RunningToReport.class);
        }

        spend(TICK);
        return true;
    }

    @Override
    public void recover() {
    }

    @Override
    public int icon() {
        return BuffIndicator.RUNNINGTOREPORT;
    }
}
