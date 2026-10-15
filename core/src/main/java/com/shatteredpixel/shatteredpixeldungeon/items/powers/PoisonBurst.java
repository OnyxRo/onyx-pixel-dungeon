package com.shatteredpixel.shatteredpixeldungeon.items.powers;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class PoisonBurst extends ActivatedPower {

    {
        image = ItemSpriteSheet.POISONBURST;
        mp_cost = 3;
    }

    @Override
    public boolean usesTargeting() {
        return true;
    }

    @Override
    public void affectCell(int pos) {
        Char ch = Actor.findChar(pos);
        if (ch != null) {
            ch.damage(Random.NormalIntRange(1, curUser.MAX_MP), this);
            Buff.affect(ch, Poison.class).set( 3 + Dungeon.depth/2 );
        }
    }

    @Override
    public void fx(Ballistica shot, Callback callback) {
        MagicMissile.boltFromChar( curUser.sprite.parent,
                MagicMissile.POISON,
                curUser.sprite,
                shot.collisionPos,
                callback);
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
    }
}
