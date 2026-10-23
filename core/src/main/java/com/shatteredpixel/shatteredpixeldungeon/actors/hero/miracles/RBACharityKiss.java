package com.shatteredpixel.shatteredpixeldungeon.actors.hero.miracles;


import com.shatteredpixel.shatteredpixeldungeon.actors.hero.RBAHero;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RBACrossNecklace;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfSirensSong;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class RBACharityKiss extends RBATargetedMariMiracle {
    public static RBACharityKiss INSTANCE = new RBACharityKiss();

    @Override
    public int icon() {
        return HeroIcon.RBA_CHARITY_KISS;
    }

    @Override
    public float chargeUse(Hero hero) {
        return 10;
    }

    @Override
    public String desc() {
        int turns = 3*Dungeon.hero.pointsInTalent(Talent.RBA_MARI_ARMOR3_2);
        return Messages.get(this, "desc", turns) + "\n\n" + Messages.get(this, "charge_cost", (int)chargeUse(Dungeon.hero));
    }

    @Override
    public boolean canCast(Hero hero) {
        return super.canCast(hero) && hero.hasTalent(Talent.RBA_MARI_ARMOR3_2);
    }

    @Override
    protected void onTargetSelected(RBACrossNecklace cross, Hero hero, Integer target) {
        if (target == null || target == hero.pos) return;

        if (!Dungeon.level.heroFOV[target]) {
            RBAHero.yellW(hero, "fov");
        }

        Char ch = Actor.findChar(target);
        if (ch == null) {
            RBAHero.yellW(hero, "no_target");
            return;
        }

        if (ch.alignment == hero.alignment || !(ch instanceof Mob)) {
            RBAHero.yellW(hero, "not_enemy");
            return;
        }

        if (ch.isImmune(AllyBuff.class)) {
            RBAHero.yellW(hero, "immune");
            return;
        }

        hero.sprite.emitter().burst(Speck.factory( Speck.HEART ), 1);
        Sample.INSTANCE.play(Assets.Sounds.LULLABY);
        hero.sprite.zap(target, new Callback() {
            @Override
            public void call() {
                hero.spendAndNext(Actor.TICK);
                hero.sprite.idle();
            }
        });
        AllyBuff.affectAndLoot((Mob)ch, hero, ScrollOfSirensSong.Enthralled.class);
        ch.sprite.centerEmitter().burst( Speck.factory( Speck.HEART ), 10 );
        AllyBuff.affect(ch, Adrenaline.class, 3*hero.pointsInTalent(Talent.RBA_MARI_ARMOR3_2));

        onMiracleCast(cross, hero);
    }
}
