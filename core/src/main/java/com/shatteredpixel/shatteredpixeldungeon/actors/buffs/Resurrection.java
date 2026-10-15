package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

public class Resurrection extends Buff {
    {
        announced = true;
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }
}
