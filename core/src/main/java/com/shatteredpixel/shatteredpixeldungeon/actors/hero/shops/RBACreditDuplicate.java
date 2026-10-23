package com.shatteredpixel.shatteredpixeldungeon.actors.hero.shops;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.yuzu.RBAVIPMembership;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;

public class RBACreditDuplicate extends RBAYuzuShopContent {
    public static final RBACreditDuplicate INSTANCE = new RBACreditDuplicate();

    @Override
    public int icon() {
        return HeroIcon.RBA_SHOP_13;
    }

    @Override
    public void onSelect(Hero hero) {
        if (hero.buff(RBAVIPMembership.VIPBuff.class) == null) return;
        int turnsRemain = (int)hero.buff(RBAVIPMembership.VIPBuff.class).visualcooldown();
        new Gold(turnsRemain*50*(1+hero.pointsInTalent(Talent.RBA_YUZU_ARMOR3_3))).doPickUp(hero, hero.pos);
        hero.buff(RBAVIPMembership.VIPBuff.class).detach();
    }

    @Override
    public int creditUse(Hero hero) {
        return 0;
    }

    @Override
    public boolean hideWindow() {
        return true;
    }

    @Override
    public String desc() {
        int turnsRemain = 1;
        if (Dungeon.hero.buff(RBAVIPMembership.VIPBuff.class) != null) turnsRemain = (int)Dungeon.hero.buff(RBAVIPMembership.VIPBuff.class).visualcooldown();
        return Messages.get(this, "desc", turnsRemain*50*(1+Dungeon.hero.pointsInTalent(Talent.RBA_YUZU_ARMOR3_3))) + "\n\n" + Messages.get(this, "credit_cost", creditUse(Dungeon.hero));
    }

    @Override
    public boolean canSelect(Hero hero) {
        return super.canSelect(hero) && hero.buff(RBAVIPMembership.VIPBuff.class) != null;
    }
}
