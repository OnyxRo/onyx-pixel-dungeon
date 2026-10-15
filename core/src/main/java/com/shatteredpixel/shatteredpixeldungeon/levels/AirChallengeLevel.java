package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;

public class AirChallengeLevel extends PrisonLevel {
    @Override
    public String tilesTex() {
        return Assets.Sprites.TILES_AIR;
    }
}
