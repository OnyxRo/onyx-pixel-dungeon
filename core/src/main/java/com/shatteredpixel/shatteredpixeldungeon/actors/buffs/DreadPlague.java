package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class DreadPlague extends Buff {

    public static final float DURATION = 20f;

    {
        type = buffType.NEGATIVE;
        announced = true;
    }

    private float left;
    private static final String LEFT	= "left";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( LEFT, left );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        left = bundle.getFloat(LEFT);
    }

    @Override
    public int icon() {
        return BuffIndicator.TERROR;
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - left) / DURATION);
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String heroMessage() {
        return Messages.get(this, "heromsg");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", dispTurns(left));
    }

    public void set(float left){
        this.left = left;
    }

    @Override
    public boolean act() {
        if (target.isAlive()) {
            if (Dungeon.depth > 20)
                target.damage( 3, this );
            else if (Dungeon.depth == 20)
                target.damage( 3, this );
            else if (Random.Int(2) == 0)
                target.damage( 2, this );
            if (!target.isAlive() && target == Dungeon.hero) {
                Dungeon.fail( getClass() );
                GLog.n( Messages.get(this, "ondeath") );
            }
            spend( TICK );
            left -= TICK;
            if (left <= 0){
                detach();
            }
        } else {
            detach();
        }
        return true;
    }
}
