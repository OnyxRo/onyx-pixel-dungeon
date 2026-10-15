package com.shatteredpixel.shatteredpixeldungeon.items.powers;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class BubbleShield extends ActivatedPower {
    {
         mp_cost = 5;
         image = ItemSpriteSheet.BUBBLESHIELD;
    }

    @Override
    public boolean usesTargeting() {
        return false;
    }

    @Override
    public void affectCell(int pos) {
        Char ch = Actor.findChar(pos);
        if (ch != null) {
            Buff.affect(ch, BubbleShieldBuff.class);
        }
    }

    public static class BubbleShieldBuff extends Buff {

        {
            type = buffType.POSITIVE;
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc");
        }

        @Override
        public void fx(boolean on) {
            if (on) target.sprite.add(CharSprite.State.SHIELDED);
            else target.sprite.remove(CharSprite.State.SHIELDED);
        }

        @Override
        public int icon() {
            return BuffIndicator.RAGE;
        }
    }
}
