package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

//Abyssalnite's chaos, from the septium pillars and Cthulhu's mind blast. Each turn it lasts the hero may lose that turn.
//Abyssalnite's version was a flavour buff with a stray semicolon, so it only ever cost one turn, when it ran out
public class ChaosTime extends Buff {

    public static final float DURATION	= 10f;

    {
        type = buffType.NEGATIVE;
        announced = true;
    }

    private float left;
    private float total = DURATION;

    public void extend( float time ){
        left = Math.max( left, time );
        total = Math.max( left, 1 );
    }

    @Override
    public boolean act() {
        if (target instanceof Hero && Random.Int(3) == 0){
            ((Hero) target).spend( TICK );
            GLog.w(Messages.get(this, "message"));
        }
        left -= TICK;
        if (left <= 0) detach();
        spend( TICK );
        return true;
    }

    @Override
    public int icon() {
        return BuffIndicator.TIME;
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (total - left) / total);
    }

    @Override
    public String iconTextDisplay() {
        return Integer.toString((int)left);
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", (int)left);
    }

    private static final String LEFT = "left";
    private static final String TOTAL = "total";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( LEFT, left );
        bundle.put( TOTAL, total );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        left = bundle.getFloat( LEFT );
        total = bundle.contains( TOTAL ) ? bundle.getFloat( TOTAL ) : DURATION;
        if (left <= 0) left = 1; //saves from the old flavour buff
    }

}
