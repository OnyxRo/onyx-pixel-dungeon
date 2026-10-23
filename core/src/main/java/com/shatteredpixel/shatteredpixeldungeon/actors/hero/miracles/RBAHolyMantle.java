package com.shatteredpixel.shatteredpixeldungeon.actors.hero.miracles;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RBACrossNecklace;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class RBAHolyMantle extends RBAMariMiracle {
    public static final RBAHolyMantle INSTANCE = new RBAHolyMantle();

    @Override
    public int icon() {
        return HeroIcon.RBA_HOLY_MANTLE;
    }

    @Override
    public float chargeUse(Hero hero) {
        return 5-hero.pointsInTalent(Talent.RBA_MARI_EX1_3);
    }

    @Override
    public boolean canCast(Hero hero) {
        return super.canCast(hero) && hero.hasTalent(Talent.RBA_MARI_EX1_3) && hero.buff(HolyMantleBuff.class) == null && hero.buff(HolyMantleBuffCooldown.class) == null;
    }

    @Override
    public void onCast(RBACrossNecklace cross, Hero hero) {
        hero.busy();
        Sample.INSTANCE.play(Assets.Sounds.READ);
        Buff.affect(hero, HolyMantleBuff.class);
        onMiracleCast(cross, hero);
        hero.sprite.operate(hero.pos, new Callback() {
            @Override
            public void call() {
                hero.sprite.idle();
                hero.next();
            }
        });
    }

    public static class HolyMantleBuff extends Buff {

        {
            type = buffType.POSITIVE;
            announced = true;
        }

        @Override
        public int icon() {
            return BuffIndicator.RBA_HOLY_MANTLE;
        }

        @Override
        public void detach() {
            super.detach();
            Buff.affect(target, HolyMantleBuffCooldown.class, HolyMantleBuffCooldown.DURATION);
        }
    }

    public static class HolyMantleBuffCooldown extends FlavourBuff {
        public static final float DURATION = 50f;

        @Override
        public int icon() {
            return BuffIndicator.TIME;
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, visualcooldown()/DURATION);
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0xFFFF00);
        }
    }
}
