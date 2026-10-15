package com.shatteredpixel.shatteredpixeldungeon.items.powers;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Energize extends ActivatedPower {

    {
        image = ItemSpriteSheet.ENERGIZE;
        mp_cost = 5;
    }

    @Override
    public boolean usesTargeting() {
        return false;
    }

    @Override
    public void affectCell(int pos) {
        Char ch = Actor.findChar(pos);
        if (ch != null) {
            Buff.affect(ch, Recharging.class, 30f/4f);
        }
    }
}
