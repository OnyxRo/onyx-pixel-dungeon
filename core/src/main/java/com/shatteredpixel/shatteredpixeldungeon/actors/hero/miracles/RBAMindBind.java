package com.shatteredpixel.shatteredpixeldungeon.actors.hero.miracles;


import com.shatteredpixel.shatteredpixeldungeon.actors.hero.RBAHero;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RBACrossNecklace;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class RBAMindBind extends RBATargetedMariMiracle {
    public static final RBAMindBind INSTANCE = new RBAMindBind();

    @Override
    public int icon() {
        return HeroIcon.RBA_MIND_BIND;
    }

    @Override
    public float chargeUse(Hero hero) {
        return 4;
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", 5*Dungeon.hero.pointsInTalent(Talent.RBA_MARI_T2_4)) + "\n\n" + Messages.get(this, "charge_cost", (int)chargeUse(Dungeon.hero));
    }

    @Override
    public boolean canCast(Hero hero) {
        return super.canCast(hero) && hero.hasTalent(Talent.RBA_MARI_T2_4);
    }

    @Override
    protected void onTargetSelected(RBACrossNecklace cross, Hero hero, Integer target) {
        if (target == null) return;

        Char ch = Actor.findChar(target);

        if (ch == null) {
            if (!Dungeon.level.heroFOV[target]) {
                RBAHero.yellW(hero, "guess_no_target");
                onUse(cross, hero, target);
            } else {
                RBAHero.yellW(hero, "no_target");
            }
            return;
        }

        if (ch.alignment != Char.Alignment.ENEMY) {
            if (!Dungeon.level.heroFOV[target]) {
                RBAHero.yellW(hero, "guess_no_target");
                onUse(cross, hero, target);
            } else {
                RBAHero.yellW(hero, "not_enemy");
            }
            return;
        }

        Buff.append(hero, MindBindBuff.class, 5*hero.pointsInTalent(Talent.RBA_MARI_T2_4)).charID = ch.id();

        onUse(cross, hero, target);
    }

    public void onUse(RBACrossNecklace cross, Hero hero, Integer target) {
        hero.busy();
        hero.sprite.operate(target);
        Sample.INSTANCE.play(Assets.Sounds.READ);
        onMiracleCast(cross, hero);
        hero.spendAndNext(Actor.TICK);
    }

    public static class MindBindBuff extends FlavourBuff {
        public int charID;

        private static final String CHAR_ID = "char_id";

        @Override
        public void detach() {
            super.detach();
            Dungeon.observe();
            GameScene.updateFog();
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            charID = bundle.getInt(CHAR_ID);
        }

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(CHAR_ID, charID);
        }

        public boolean aimingTarget(int cell) {
            Char ch = (Char) Actor.findById(charID);
            if (ch == null) return false;
            return ch.pos == cell;
        }
    }
}
