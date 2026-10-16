package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StenchGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.watabou.utils.Random;

public abstract class CampRat extends Rat {
    {
        alignment = Alignment.ALLY;


        immunities.add(Paralysis.class);
        immunities.add(StenchGas.class);

        state = HUNTING;

        viewDistance = 11;

    }

    public int damageMin;
    public int damageMax;
    public int defMin;
    public int defMax;

    public int cost;
    @Override
    protected boolean act() {
        int posofleader = -1;

       for (Mob mob : Dungeon.level.mobs){
            if (mob instanceof CampRatLeader &&
                    mob !=this &&
                    (Dungeon.level.trueDistance(mob.pos, this.pos) <= Dungeon.level.trueDistance(posofleader, this.pos) || posofleader==-1)){
                posofleader = mob.pos;
            }
        }

       if (posofleader!=-1 && !(this instanceof CampRatLeader)) beckon(posofleader);
        return super.act();
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(defMin, defMax);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(damageMin, damageMax);
    }
}
