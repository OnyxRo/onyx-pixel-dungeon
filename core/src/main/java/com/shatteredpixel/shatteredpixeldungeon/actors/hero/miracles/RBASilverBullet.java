package com.shatteredpixel.shatteredpixeldungeon.actors.hero.miracles;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.RBAIceParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.RBASilverParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RBACrossNecklace;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.RBAGun;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Random;

public class RBASilverBullet extends RBATargetedMariMiracle {

    public static final RBASilverBullet INSTANCE = new RBASilverBullet();

    @Override
    public int icon() {
        return HeroIcon.RBA_SILVER_BULLET;
    }

    @Override
    public float chargeUse(Hero hero) {
        return 3;
    }

    public String desc(){
        int min = Dungeon.hero.pointsInTalent(Talent.SUNRAY) == 2 ? 6 : 4;
        int max = Dungeon.hero.pointsInTalent(Talent.SUNRAY) == 2 ? 12 : 8;
        int dur = Dungeon.hero.pointsInTalent(Talent.SUNRAY) == 2 ? 6 : 4;
        return Messages.get(this, "desc", min, max, dur ) + "\n\n" + Messages.get(this, "charge_cost", (int)chargeUse(Dungeon.hero));
    }

    @Override
    public boolean canCast(Hero hero) {
        return super.canCast(hero) && hero.hasTalent(Talent.RBA_MARI_T2_3);
    }

    @Override
    protected void onTargetSelected(RBACrossNecklace cross, Hero hero, Integer target) {
        if (target == null){
            return;
        }

        Ballistica aim = new Ballistica(hero.pos, target, targetingFlags());

        if (Actor.findChar( aim.collisionPos ) == hero){
            GLog.i( Messages.get(Wand.class, "self_target") );
            return;
        }

        if (Actor.findChar(aim.collisionPos) != null) {
            QuickSlotButton.target(Actor.findChar(aim.collisionPos));
        } else {
            QuickSlotButton.target(Actor.findChar(target));
        }

        hero.busy();
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
        hero.sprite.zap(target);
        SilverBulletItem silverBulletItem = new SilverBulletItem(cross, hero);
        silverBulletItem.cast(hero, target);
    }

    @Override
    public void onMiracleCast(RBACrossNecklace cross, Hero hero) {
        super.onMiracleCast(cross, hero);

        RBAGun gun = getEquippedGun(hero);
        if (gun != null) gun.manualReload();
    }

    public class SilverBulletItem extends Item {
        {
            image = ItemSpriteSheet.RBA_SILVER_BULLET;
        }

        RBACrossNecklace cross;
        Hero hero;

        public SilverBulletItem(RBACrossNecklace cross, Hero hero) {
            this.cross = cross;
            this.hero = hero;
        }

        @Override
        protected void onThrow(int cell) {
            Char ch = Actor.findChar( cell );
            if (ch != null) {
                int min = Dungeon.hero.pointsInTalent(Talent.SUNRAY) == 2 ? 6 : 4;
                int max = Dungeon.hero.pointsInTalent(Talent.SUNRAY) == 2 ? 12 : 8;
                int dur = Dungeon.hero.pointsInTalent(Talent.SUNRAY) == 2 ? 6 : 4;
                if (Char.hasProp(ch, Char.Property.DEMONIC) || Char.hasProp(ch, Char.Property.UNDEAD)) {
                    ch.damage(Hero.heroDamageIntRange(max, max), new RBASilverBullet());
                } else {
                    ch.damage(Hero.heroDamageIntRange(min, max), new RBASilverBullet());
                }

                Sample.INSTANCE.play(Assets.Sounds.HIT_MAGIC, 1, Random.Float(0.87f, 1.15f));
                if (ch.buff(SilverBulletBlindTracker.class) == null) {
                    Buff.affect(ch, SilverBulletBlindTracker.class);
                    Buff.affect(ch, Blindness.class, dur);
                }
                ch.sprite.burst(0xFFCCCCCC, 3);
            } else {
                Dungeon.level.pressCell(cell);
            }

            onMiracleCast(cross, hero);
            Splash.at(cell, 0xFFCCCCCC, 3);
        }

        @Override
        public void throwSound() {
            Sample.INSTANCE.play(Assets.Sounds.ZAP);
        }

        @Override
        public Emitter emitter() {
            Emitter emitter = new Emitter();
            emitter.pos( 5, 5, 0, 0);
            emitter.fillTarget = false;
            emitter.pour(RBASilverParticle.FACTORY, 0.001f);
            return emitter;
        }
    }

    public static class SilverBulletBlindTracker extends Buff {}
}
