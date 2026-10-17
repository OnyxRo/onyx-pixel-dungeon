package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class Prediction extends FlavourBuff{
    {
        type = buffType.POSITIVE;
    }

    //check amuletTower.startWave for usage

    @Override
    public int icon() {
        return BuffIndicator.PREDICTION;
    }

    @Override
    public float iconFadePercent() {
        return 1;
    }

}
