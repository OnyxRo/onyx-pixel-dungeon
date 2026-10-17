package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class Inspired extends Buff{

    public static final float DURATION = 15f;

    {
        type = Buff.buffType.POSITIVE;
        announced = false;
    }
    public int level = 1;
    public float time = DURATION;

    @Override
    public int icon() {
        return BuffIndicator.INSPIRED;
    }

    @Override
    public boolean act() {
        if (target.isAlive()) {
            spend(1f);
            time-=level;
            if (time <= 1){
                if (level<=1) detach();
                else {
                    do {
                        level--;
                        time = DURATION;
                    } while (level > 5); //a softening cap for absurd cases of more than 5 banners near a char, usually a guard, as this may boost the mob too much for a value of free banner
                    fx(true);
                }
            }
        } else {
            detach();
        }
        return true;
    }
    @Override
    public float iconFadePercent() {
        return 1 - time/DURATION;
    }
    public void add( int value ) {
        level += value;
    }

    @Override
    public void fx(boolean on) {
        if (on) target.sprite.aura( 0xFFFF00, 12 );
        else target.sprite.clearAura();
    }
    @Override
    public String iconTextDisplay() {
        return String.format("%.1f", (Math.pow(1.1, level))*100f) + "%";
    }
    @Override
    public String desc() {
        return Messages.get(this, "desc", String.format("%.1f", (Math.pow(1.1, level))*100f) + "%", String.format("%.1f", time/level));
    }

    private static final String LEVEL	    = "level";
    private static final String TIME	    = "time";
    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( LEVEL, level );
        bundle.put(TIME, time);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        level = bundle.getInt( LEVEL );
        time = bundle.getFloat(TIME);
    }
}
