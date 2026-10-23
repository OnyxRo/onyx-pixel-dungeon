package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

public class RBAAlliedEnemyTracker extends Buff {
    @Override
    public boolean act() {
        detach();
        return super.act();
    }
}
