package com.shatteredpixel.shatteredpixeldungeon.actors.hero.miracles;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mari.RBAMariTrinity;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RBACrossNecklace;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public abstract class RBATrinityMiracle extends RBAMariMiracle {
    @Override
    public float chargeUse(Hero hero) {
        return 6;
    }

    @Override
    public boolean canCast(Hero hero) {
        return super.canCast(hero) && hero.buff(RBAMariTrinity.TrinityBuff.class) != null && hero.armorAbility instanceof RBAMariTrinity;
    }

    protected RBAMariTrinity.TrinityBuff getBuff(Hero hero) {
        return hero.buff(RBAMariTrinity.TrinityBuff.class);
    }

    public abstract void castEffect(Hero hero);

    @Override
    public void onCast(RBACrossNecklace cross, Hero hero) {
        if (getBuff(hero) == null) return;

        hero.busy();
        Sample.INSTANCE.play(Assets.Sounds.READ);
        hero.sprite.operate(hero.pos, new Callback() {
            @Override
            public void call() {
                hero.sprite.idle();
                hero.next();
            }
        });
        castEffect(hero);
        onMiracleCast(cross, hero);
    }
}
