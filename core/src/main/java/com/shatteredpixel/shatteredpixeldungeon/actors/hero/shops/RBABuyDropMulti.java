package com.shatteredpixel.shatteredpixeldungeon.actors.hero.shops;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBAYuzuStatus;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.levels.VaultLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;

public class RBABuyDropMulti extends RBAYuzuShopContent {
    public static final RBABuyDropMulti INSTANCE = new RBABuyDropMulti();

    @Override
    public int icon() {
        return HeroIcon.RBA_SHOP_9;
    }

    @Override
    public void onSelect(Hero hero) {
        Buff.affect(hero, RBAYuzuStatus.class).buyStat(RBAYuzuStatus.DROP_MULTI);
        CellEmitter.center( hero.pos ).burst( Speck.factory( Speck.STAR ), 1 );
        CellEmitter.get( hero.pos ).burst( Speck.factory( Speck.FORGE ), 1 );
    }

    @Override
    public boolean canSelect(Hero hero) {
        if (Dungeon.level instanceof VaultLevel) return false;
        else return super.canSelect(hero) && RBAYuzuStatus.yuzuDropMulti(hero)-1 < RBAYuzuStatus.DROP_MULTI_INCREMENT*RBAYuzuStatus.MAX_LEVEL;
    }

    @Override
    public String shortDesc() {
        return Messages.get(this, "short_desc", Messages.decimalFormat("#", 100*RBAYuzuStatus.DROP_MULTI_INCREMENT))
                + ".\n" + Messages.get(this, "credit_cost", creditUse(Dungeon.hero));
    }

    public String desc(){
        return Messages.get(this, "desc",
                Messages.decimalFormat("#", 100*RBAYuzuStatus.DROP_MULTI_INCREMENT),
                Messages.decimalFormat("#", 100*RBAYuzuStatus.DROP_MULTI_INCREMENT*RBAYuzuStatus.MAX_LEVEL),
                Messages.decimalFormat("#", 100),
                Messages.decimalFormat("#", 100*(RBAYuzuStatus.yuzuDropMulti(Dungeon.hero)-1)))
                + "\n\n" + Messages.get(this, "credit_cost", creditUse(Dungeon.hero));
    }

    @Override
    public int creditUse(Hero hero) {
        return 1000*(1+RBAYuzuStatus.yuzuStatusCount(hero, RBAYuzuStatus.DROP_MULTI));
    }
}
