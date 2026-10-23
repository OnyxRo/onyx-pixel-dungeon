package com.shatteredpixel.shatteredpixeldungeon.actors.hero.miracles;


import com.shatteredpixel.shatteredpixeldungeon.actors.hero.RBAHero;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RBACrossNecklace;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class RBABlessOfCharity extends RBATargetedMariMiracle {

    public static final RBABlessOfCharity INSTANCE = new RBABlessOfCharity();

    @Override
    public int icon() {
        return HeroIcon.RBA_BLESS_OF_CHARITY;
    }

    @Override
    public float chargeUse(Hero hero) {
        return 3;
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", 2+3*Dungeon.hero.pointsInTalent(Talent.RBA_MARI_T2_5)) + "\n\n" + Messages.get(this, "charge_cost", (int)chargeUse(Dungeon.hero));
    }

    @Override
    public boolean canCast(Hero hero) {
        return super.canCast(hero) && hero.hasTalent(Talent.RBA_MARI_T2_5);
    }

    @Override
    protected void onTargetSelected(RBACrossNecklace cross, Hero hero, Integer target) {
        if (target == null) return;

        if (!Dungeon.level.heroFOV[target]) {
            RBAHero.yellW(hero, "fov");
            return;
        }

        Char ch = Actor.findChar(target);
        if (ch == null) {
            RBAHero.yellW(hero, "no_target");
            return;
        }

        hero.busy();

        Buff.affect(ch, BlessOfCharityBuff.class).set(BlessOfCharityBuff.MAX_DURATION+1);
        Buff.prolong(ch, Bless.class, 10f);

        hero.sprite.operate(target);
        Sample.INSTANCE.play(Assets.Sounds.READ);
        onMiracleCast(cross, hero);
        hero.spendAndNext(Actor.TICK);
    }

    public static class BlessOfCharityBuff extends Buff {

        public static final float MAX_DURATION = 10f;

        {
            type = buffType.POSITIVE;
        }

        float duration = 0;

        @Override
        public int icon() {
            return BuffIndicator.RBA_BLESS_OF_CHARITY;
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (MAX_DURATION - duration)/MAX_DURATION);
        }

        @Override
        public String iconTextDisplay() {
            return Messages.decimalFormat("#", duration);
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", Messages.decimalFormat("#.##", duration));
        }

        public void set(float duration) {
            this.duration = duration;
        }

        @Override
        public boolean act() {
            int shielding = 2+3*Dungeon.hero.pointsInTalent(Talent.RBA_MARI_T2_5);
            Buff.affect(target, Barrier.class).setShield(shielding);
            target.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(shielding), FloatingText.SHIELDING);
            spend(Actor.TICK);
            duration--;
            if (duration <= 0) {
                detach();
            }
            return true;
        }

        private static final String DURATION = "duration";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(DURATION, duration);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            duration = bundle.getFloat(DURATION);
        }
    }
}
