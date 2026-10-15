package com.shatteredpixel.shatteredpixeldungeon.items.powers;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

public class Greed extends ActivatedPower {
    {
        mp_cost = 10;
        image = ItemSpriteSheet.GREED;
    }

    @Override
    public boolean usesTargeting() {
        return true;
    }

    @Override
    public void affectCell(int pos) {
        Char ch = Actor.findChar(pos);
        if (ch == null) {
            return;
        }
        Buff.affect(ch, GreedBuff.class).set(10);
    }

    public void fx(Ballistica shot, Callback callback) {
        MagicMissile.boltFromChar( curUser.sprite.parent,
                MagicMissile.EARTH,
                curUser.sprite,
                shot.collisionPos,
                callback);
        Sample.INSTANCE.play( Assets.Sounds.GOLD );
    }


    public static class GreedBuff extends Buff {
        protected float left;

        private static final String LEFT	= "left";

        {
            type = buffType.NEGATIVE;
        }

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle( bundle );
            bundle.put( LEFT, left );

        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle( bundle );
            left = bundle.getFloat( LEFT );
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public int icon() {
            return BuffIndicator.RAGE;
        }

        public void set( float duration ) {
            this.left = Math.max(duration, left);
        }

        public void extend( float duration ) {
            this.left += duration;
        }

        @Override
        public boolean act() {
            Dungeon.level.drop( new Gold().random(), target.pos ).sprite.drop( target.pos );
            if ((left -= TICK) <= 0) {
                detach();
            }
            spend( TICK );
            return true;
        }
    }
}
