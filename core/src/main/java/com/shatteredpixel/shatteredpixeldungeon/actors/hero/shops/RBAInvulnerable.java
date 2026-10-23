package com.shatteredpixel.shatteredpixeldungeon.actors.hero.shops;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.yuzu.RBAVIPMembership;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class RBAInvulnerable extends RBAYuzuShopContent {
    public static final RBAInvulnerable INSTANCE = new RBAInvulnerable();

    @Override
    public int icon() {
        return HeroIcon.RBA_SHOP_11;
    }

    @Override
    public void onSelect(Hero hero) {
        hero.busy();
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP, 1f, 1.2f);
        Buff.affect(hero, InvulnerableTracker.class);
        Buff.affect(hero, Barrier.class).incShield(30*hero.pointsInTalent(Talent.RBA_YUZU_ARMOR3_1));
        hero.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(30*hero.pointsInTalent(Talent.RBA_YUZU_ARMOR3_1)), FloatingText.SHIELDING);
        hero.sprite.operate(hero.pos, new Callback() {
            @Override
            public void call() {
                hero.sprite.idle();
                hero.next();
            }
        });
    }

    @Override
    public int creditUse(Hero hero) {
        return 300*inflationParameter();
    }

    @Override
    public boolean hideWindow() {
        return true;
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", 30 * Dungeon.hero.pointsInTalent(Talent.RBA_YUZU_ARMOR3_2)) + "\n\n" + Messages.get(this, "credit_cost", creditUse(Dungeon.hero));
    }

    @Override
    public boolean canSelect(Hero hero) {
        return super.canSelect(hero) && hero.buff(RBAVIPMembership.VIPBuff.class) != null && hero.buff(InvulnerableTracker.class) == null;
    }

    public static class InvulnerableTracker extends Buff {}
}
