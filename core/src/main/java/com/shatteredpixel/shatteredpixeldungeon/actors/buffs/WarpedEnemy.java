package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;
import com.watabou.noosa.particles.Emitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class WarpedEnemy extends Buff {

    {
        type = buffType.NEGATIVE;
        announced = true;
    }

    @Override
    public int icon() {
        return BuffIndicator.DR_WARP;
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    //a purple cloud around warped enemies, the mod drew it with its own sprite state
    private Emitter cloud;

    @Override
    public void fx(boolean on) {
        cloud = warpFx(this, cloud, on);
    }

    static Emitter warpFx(Buff b, Emitter cloud, boolean on) {
        if (on) {
            if (cloud == null && b.target.sprite != null) {
                cloud = b.target.sprite.emitter();
                cloud.pour(ShadowParticle.CURSE, 0.35f);
            }
        } else if (cloud != null) {
            cloud.on = false;
            cloud = null;
        }
        return cloud;
    }

    {
        immunities.add(Charm.class);
        immunities.add(Vertigo.class);
        immunities.add(Terror.class);
    }

    //special variant, used for boss buffs
    public static class BossEffect extends Buff {
        private Emitter cloud;

        @Override
        public void fx(boolean on) {
            cloud = warpFx(this, cloud, on);
        }

        {
            immunities.add(Charm.class);
            immunities.add(Vertigo.class);
            immunities.add(Terror.class);
        }
    }
}