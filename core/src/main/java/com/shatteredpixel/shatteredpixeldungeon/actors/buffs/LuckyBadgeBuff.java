package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class LuckyBadgeBuff extends FlavourBuff {

    public int lootPerMob() {
        return Math.max(1, (int) cooldown()/10);
    }

    @Override
    public int icon() {
        return BuffIndicator.SACRIFICE;
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", dispTurns());
    }
}
