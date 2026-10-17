package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class WaveCooldownBuff extends FlavourBuff {

    public static final float DURATION	= 30f;

    {
        type = buffType.NEUTRAL;
        announced = true;
    }

    @Override
    public String desc() {
        if (((Arena)Dungeon.level).maxWaves > 1000) return Messages.get(this, "descnomax", Dungeon.level.wave + 1, this.dispTurns());
        return Messages.get(this, "desc", Dungeon.level.wave + 1, ((Arena)Dungeon.level).maxWaves, this.dispTurns());
    }
    @Override
    public String heroMessage() {
        return "";
    }

    @Override
    public int icon() {
        return BuffIndicator.WAVECOOLDOWN;
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - visualcooldown()) / DURATION);
    }

    @Override
    public void detach() {
        super.detach();
    }
}