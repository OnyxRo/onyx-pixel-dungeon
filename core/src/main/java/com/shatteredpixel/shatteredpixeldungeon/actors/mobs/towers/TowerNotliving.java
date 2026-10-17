package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers;

import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;

public abstract class TowerNotliving extends Tower{
    {
        immunities.add(Charm.class);
        properties.add(Property.INORGANIC);
        immunities.add(ToxicGas.class);
        immunities.add(Amok.class);
    }
}
