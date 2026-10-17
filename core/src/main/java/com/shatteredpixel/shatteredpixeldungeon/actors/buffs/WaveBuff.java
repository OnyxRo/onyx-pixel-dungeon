package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class WaveBuff extends FlavourBuff {

    {
        type = buffType.NEUTRAL;
        announced = true;
    }



    @Override
    public String desc() {
        if (((Arena)Dungeon.level).maxWaves >= 1000) return Messages.get(this, "descnomax", Dungeon.level.wave, dispTurns());
        if (Dungeon.level.wave == ((Arena)Dungeon.level).maxWaves) return Messages.get(this, "descend", Dungeon.level.wave, ((Arena)Dungeon.level).maxWaves);
        return Messages.get(this, "desc", Dungeon.level.wave, ((Arena)Dungeon.level).maxWaves, dispTurns());
    }


    @Override
    public String heroMessage() {
        return "";
    }

    @Override
    public int icon() {
        return BuffIndicator.WAVE;
    }

    @Override
    public String iconTextDisplay() {
        return "";
    }
}