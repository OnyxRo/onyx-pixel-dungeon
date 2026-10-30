
package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;


import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ConfusionGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;


public class ElementalistImmunity extends FlavourBuff {

    public static final float DURATION = Float.POSITIVE_INFINITY;

    {
        immunities.add(ConfusionGas.class);
        immunities.add(Paralysis.class);
        immunities.add(Vertigo.class);

    }

    public static class ChillImmunity extends FlavourBuff { { immunities.add(Chill.class); } }
    public static class ElectricityImmunity extends FlavourBuff { { immunities.add(Electricity.class); } }
    public static class CorrosionImmunity extends FlavourBuff { { immunities.add(Corrosion.class); } }
}
