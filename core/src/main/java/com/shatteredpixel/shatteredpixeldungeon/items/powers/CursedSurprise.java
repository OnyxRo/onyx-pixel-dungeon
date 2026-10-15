package com.shatteredpixel.shatteredpixeldungeon.items.powers;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Wound;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class CursedSurprise extends ActivatedPower {

    {
        image = ItemSpriteSheet.SURPRISE;

        mp_cost = 5;
    }

    @Override
    public boolean usesTargeting() {
        return true;
    }

    @Override
    public void affectCell(int pos) {
        Char ch = Actor.findChar(pos);
        int damage;
        if (ch != null) {
            damage = Random.NormalIntRange(curUser.magicSkill, curUser.magicSkill*3);
            if (ch instanceof Mob && ((Mob)ch).surprisedBy(curUser)) {
                damage *= 3;
                Wound.hit(ch);
            }
            ch.damage(damage, this);
        }
    }

    @Override
    public void fx(Ballistica shot, Callback callback) {
        MagicMissile.boltFromChar( curUser.sprite.parent,
                MagicMissile.SHADOW,
                curUser.sprite,
                shot.collisionPos,
                callback);
    }
}
