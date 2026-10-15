package com.shatteredpixel.shatteredpixeldungeon.items.powers;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Sheep;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Callback;

public class Telekinesis extends ActivatedPower {
    {
        image = ItemSpriteSheet.TELEKINESIS;
        mp_cost = 4;
    }

    @Override
    public boolean usesTargeting() {
        return true;
    }

    @Override
    public void affectCell(int pos) {}

    @Override
    public void onZap(Ballistica shot) {
        int level = curUser.lvl;

        int cell = shot.collisionPos;

        int dist = shot.dist;
        if (shot.dist > level/5 + 4) {
            dist = level + 4;
        } else if (Actor.findChar(cell) != null) {
            dist--;
        }
        shot = new Ballistica(shot.sourcePos, shot.path.get(dist), Ballistica.PROJECTILE);

        for (Integer affectedCell : shot.path) {
            Char ch = Actor.findChar(affectedCell);
            if (ch != null && ch != curUser) {
                WandOfBlastWave.throwChar(ch, shot, 3, true, true, this);
            }
            Dungeon.level.pressCell(affectedCell);
            Heap heap = Dungeon.level.heaps.get(affectedCell);
            if (heap != null && heap.type == Heap.Type.HEAP) {
                for (Item item : heap.items) {
                    if (!item.collect()) {
                        Dungeon.level.drop(item, heap.pos).sprite.drop();
                    }
                }
                heap.pickUp();
            }
        }
    }

    @Override
    public void fx(Ballistica shot, Callback callback) {
        MagicMissile.boltFromChar(
                curUser.sprite.parent,
                MagicMissile.FORCE,
                curUser.sprite,
                shot.collisionPos,
                callback);
    }
}
