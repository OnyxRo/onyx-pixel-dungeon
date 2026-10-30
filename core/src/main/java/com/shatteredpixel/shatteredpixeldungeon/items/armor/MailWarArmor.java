package com.shatteredpixel.shatteredpixeldungeon.items.armor;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class MailWarArmor extends Armor {

    {
        image = ItemSpriteSheet.ARMOR_MAIL_WAR;
    }

    public MailWarArmor() {
        super(3);
    }

    @Override
    public int STRReq(int lvl) {
        return super.STRReq(lvl) + 1;
    }

    @Override
    public String info() {
        return super.info()+"\n\n_Special:_ Increases weapon damage by 15%.";
    }
}
