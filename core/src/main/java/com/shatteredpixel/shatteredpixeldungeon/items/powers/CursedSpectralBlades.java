package com.shatteredpixel.shatteredpixeldungeon.items.powers;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Shuriken;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Callback;

import java.util.HashMap;

public class CursedSpectralBlades extends ActivatedPower {

    {
        image = ItemSpriteSheet.SPECTRALBLADES;
        mp_cost = 8;
    }

    @Override
    public boolean usesTargeting() {
        return false;
    }

    private HashMap<Callback, Mob> targets = new HashMap<Callback, Mob>();

    @Override
    public void affectCell(int pos) {
        Item proto = new Shuriken();

        for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
            if (Dungeon.level.distance(curUser.pos, mob.pos) <= 12) {

                Callback callback = new Callback() {
                    @Override
                    public void call() {
                        curUser.attack( targets.get( this ) );
                        targets.remove( this );
                    }
                };

                ((MissileSprite)curUser.sprite.parent.recycle( MissileSprite.class )).
                        reset( curUser.pos, mob.pos, proto, callback );

                targets.put( callback, mob );
            }
        }

        if (targets.size() == 0) {
            GLog.w( Messages.get(this, "no_enemies") );
            return;
        }

        curUser.sprite.zap( curUser.pos );
        curUser.busy();
    }
}
