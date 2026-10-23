package com.shatteredpixel.shatteredpixeldungeon.actors.hero.miracles;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RBACrossNecklace;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Elastic;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

public class RBAHolyBlast extends RBAMariMiracle {
    public static final RBAHolyBlast INSTANCE = new RBAHolyBlast();

    @Override
    public float chargeUse(Hero hero) {
        return 6;
    }

    @Override
    public int icon() {
        return HeroIcon.RBA_HOLY_BLAST;
    }

    public String desc(){
        return Messages.get(this, "desc", Dungeon.hero.pointsInTalent(Talent.RBA_MARI_EX1_1)) + "\n\n" + Messages.get(this, "charge_cost", (int)chargeUse(Dungeon.hero));
    }

    @Override
    public boolean canCast(Hero hero) {
        return super.canCast(hero) && hero.hasTalent(Talent.RBA_MARI_EX1_1) && hero.buff(HolyBlastCooldown.class) == null;
    }

    @Override
    public void onCast(RBACrossNecklace cross, Hero hero) {
        hero.busy();
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        hero.sprite.operate(hero.pos, new Callback() {
            @Override
            public void call() {
                for (int i : PathFinder.NEIGHBOURS8) {
                    int c = hero.pos + i;
                    Char ch = Actor.findChar(c);
                    if (ch != null && !Char.hasProp(ch, Char.Property.IMMOVABLE)) {
                        Elastic.pushEnemy(null, hero, ch, hero.pointsInTalent(Talent.RBA_MARI_EX1_1));
                    }
                }
                Sample.INSTANCE.play(Assets.Sounds.BLAST, 0.6f+0.2f*hero.pointsInTalent(Talent.RBA_MARI_EX1_1));
                WandOfBlastWave.BlastWave.blast(hero.pos, hero.pointsInTalent(Talent.RBA_MARI_EX1_1)*2, 0xFFFF00);
                hero.spendAndNext(Actor.TICK);
                hero.sprite.idle();
                onMiracleCast(cross, hero);
            }
        });
    }

    @Override
    public void onMiracleCast(RBACrossNecklace cross, Hero hero) {
        super.onMiracleCast(cross, hero);
        Buff.affect(hero, HolyBlastCooldown.class);
    }

    public static class HolyBlastCooldown extends FlavourBuff {
        public static final float DURATION = 20f;

        @Override
        public int icon() {
            return BuffIndicator.TIME;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0x444444);
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, visualcooldown()/DURATION);
        }
    }

}
