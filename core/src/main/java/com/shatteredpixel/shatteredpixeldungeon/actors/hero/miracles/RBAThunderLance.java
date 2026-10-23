package com.shatteredpixel.shatteredpixeldungeon.actors.hero.miracles;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.fantasy.RBAThunder;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RBACrossNecklace;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;

public class RBAThunderLance extends RBATargetedMariMiracle {
    public static final RBAThunderLance INSTANCE = new RBAThunderLance();

    @Override
    public int icon() {
        return HeroIcon.RBA_THUNDER_LANCE;
    }

    @Override
    public float chargeUse(Hero hero) {
        return 8;
    }

    @Override
    public String desc() {
        int minBase = min(Dungeon.hero);
        int maxBase = max(Dungeon.hero);
        int paralysis = paralysisTurns();
        return Messages.get(this, "desc", minBase, maxBase, paralysis) + "\n\n" + Messages.get(this, "charge_cost", (int)chargeUse(Dungeon.hero));
    }

    @Override
    public boolean canCast(Hero hero) {
        return super.canCast(hero) && hero.hasTalent(Talent.RBA_MARI_EX1_2);
    }

    private int min(Hero hero) {
        return 15 + 15*hero.pointsInTalent(Talent.HOLY_LANCE);
    }

    private int max(Hero hero) {
        return Math.round(27.5f + 27.5f*hero.pointsInTalent(Talent.HOLY_LANCE));
    }

    private int paralysisTurns() {
        return 3;
    }

    @Override
    protected void onTargetSelected(RBACrossNecklace cross, Hero hero, Integer target) {
        if (target == null) return;

        ThunderItem thunder = new ThunderItem(cross, hero);
        thunder.cast(hero, target);
    }

    public class ThunderItem extends Item {
        {
            image = ItemSpriteSheet.RBA_THUNDER_LANCE;
        }

        RBACrossNecklace cross;
        Hero hero;

        public ThunderItem(RBACrossNecklace cross, Hero hero) {
            this.cross = cross;
            this.hero = hero;
        }

        @Override
        protected void onThrow(int cell) {
            Char ch = Actor.findChar( cell );
            if (ch != null) {
                int min = min(hero);
                int max = max(hero);
                int damage;
                if (Char.hasProp(ch, Char.Property.UNDEAD) || Char.hasProp(ch, Char.Property.DEMONIC)) {
                    damage = max(hero);
                } else {
                    damage = Hero.heroDamageIntRange(min, max);
                }
                ch.damage(damage, new RBAThunderLance());
                ch.sprite.burst(0xFFFFFFFF, paralysisTurns());
                RBAThunder.thunderEffect(ch.sprite);
                Buff.affect(ch, Paralysis.class, 3);
            } else {
                Dungeon.level.pressCell(cell);
            }

            onMiracleCast(cross, hero);
            Splash.at(cell, 0xFFFFFFFF, 3);
            hero.buff(RBALightBullet.LightMagazine.class).detach();
        }

        @Override
        public void throwSound() {
            Sample.INSTANCE.play(Assets.Sounds.LIGHTNING);
        }

        @Override
        public Emitter emitter() {
            Emitter emitter = new Emitter();
            emitter.pos( 5, 5, 0, 0);
            emitter.fillTarget = false;
            emitter.pour(SparkParticle.FACTORY, 0.04f);
            return emitter;
        }
    }
}
