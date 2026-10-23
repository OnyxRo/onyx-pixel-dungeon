package com.shatteredpixel.shatteredpixeldungeon.items.remains;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class CrownShard extends RemainsItem {

    {
        image = ItemSpriteSheet.BROKEN_CROWN;
    }

    @Override
    protected void doEffect(Hero hero) {
        // super innovative
        for (RemainsItem item : new RemainsItem[]{new SealShard(), new BrokenStaff(), new BowFragment(), new BrokenHilt()}) {
            item.doEffect(hero);
        }
    }
}