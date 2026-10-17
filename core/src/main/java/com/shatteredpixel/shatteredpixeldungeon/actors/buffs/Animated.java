package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class Animated extends Buff {

    {
        type = buffType.NEUTRAL;
        announced = false;
    }

    @Override
    public int icon() {
        return BuffIndicator.ANIMATED;
    }

    @Override
    public void fx(boolean on) {
        if (on) target.sprite.add(CharSprite.State.HEARTS);
        else target.sprite.remove(CharSprite.State.HEARTS);
    }
}