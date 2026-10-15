package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class ChaosTime extends FlavourBuff {

    public static final float DURATION	= 10f;


    {
        type = buffType.NEGATIVE;

    }



    @Override
    public boolean act() {
        super.act();
        if (Random.Int(100) >= 14);
            ((Hero) target).spend( TICK );
        GLog.w(Messages.get(this, "message"));
        return true;
    }


    @Override
    public int icon() {
        return BuffIndicator.TIME;
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - visualcooldown()) / DURATION);
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
