package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class KeenEye extends FlavourBuff {

    public static final float DURATION = 10f;

    {
        type = Buff.buffType.POSITIVE;
        announced = true;
    }

    @Override
    public int icon() {
        return BuffIndicator.KEENEYE;
    }
    @Override
    public void fx(boolean on) {
        if (on) target.sprite.aura( 0x8888DD, 6 );
        else target.sprite.clearAura();
    }

    @Override
    public String desc() {
        return super.desc();
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - visualcooldown()) / DURATION);
    }
}
