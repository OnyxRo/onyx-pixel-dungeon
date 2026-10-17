package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class MinionBoss extends Minion{

    @Override
    public int icon() {
        return BuffIndicator.CORRUPT;
    }

    @Override
    public void fx(boolean on) {
        if (on) target.sprite.add( CharSprite.State.DARKENED );
        else if (target.invisible == 0) target.sprite.remove( CharSprite.State.DARKENED );
    }
}
