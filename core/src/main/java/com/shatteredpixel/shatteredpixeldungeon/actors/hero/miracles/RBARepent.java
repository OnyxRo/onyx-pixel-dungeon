package com.shatteredpixel.shatteredpixeldungeon.actors.hero.miracles;


import com.shatteredpixel.shatteredpixeldungeon.actors.hero.RBAHero;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SoulMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RBACrossNecklace;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfSirensSong;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class RBARepent extends RBATargetedMariMiracle {
    public static RBARepent INSTANCE = new RBARepent();

    @Override
    public int icon() {
        return HeroIcon.RBA_REPENT;
    }

    @Override
    public float chargeUse(Hero hero) {
        return super.chargeUse(hero);
    }

    @Override
    public String desc() {
        String desc;
        int chance = 10 * (Dungeon.hero.pointsInTalent(Talent.RBA_MARI_EX2_3)-1);
        if (chance > 0) {
            desc = Messages.get(this, "alt_desc", chance);
        } else {
            desc = Messages.get(this, "desc");
        }
        return desc + "\n\n" + Messages.get(this, "charge_cost", (int)chargeUse(Dungeon.hero));
    }

    @Override
    public boolean canCast(Hero hero) {
        return super.canCast(hero) && hero.hasTalent(Talent.RBA_MARI_EX2_3);
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

        if (ch.alignment != Char.Alignment.ENEMY || !(ch instanceof Mob)) {
            RBAHero.yellW(hero, "not_enemy");
            return;
        }

        if (!Dungeon.level.adjacent(hero.pos, ch.pos)) {
            RBAHero.yellW(hero, "not_adjacent");
            return;
        }

        hero.busy();
        hero.sprite.zap(target, new Callback() {
            @Override
            public void call() {
                hero.spendAndNext(Actor.TICK);
                hero.sprite.idle();
            }
        });

        float chance = (ch.HT-ch.HP)/(float)ch.HT;
        chance += 0.1f * (hero.pointsInTalent(Talent.RBA_MARI_EX2_3)-1);

        Mob enemy = (Mob) ch;
        if (Random.Float() < chance && !enemy.isImmune(ScrollOfSirensSong.Enthralled.class)) {
            for (Buff buff : enemy.buffs()) {
                if (buff.type == Buff.buffType.NEGATIVE
                        && !(buff instanceof SoulMark)) {
                    buff.detach();
                }
            }
            AllyBuff.affectAndLoot(enemy, hero, ScrollOfSirensSong.Enthralled.class);
            int toHeal = enemy.HT - enemy.HP;
            enemy.heal(toHeal);
        } else {
            Buff.affect(enemy, Weakness.class, 10f);
        }
        if (Dungeon.level.heroFOV[target]) {
            CellEmitter.get( target ).burst( Speck.factory( Speck.LIGHT ), 4 );
        }
        Sample.INSTANCE.play(Assets.Sounds.LULLABY);

        onMiracleCast(cross, hero);
    }
}
