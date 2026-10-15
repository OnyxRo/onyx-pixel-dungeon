package com.shatteredpixel.shatteredpixeldungeon.items.powers;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.Camera;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class WaterPump extends ActivatedPower {

    {
        image = ItemSpriteSheet.WATERPUMP;
        mp_cost = 7;
    }

    @Override
    public boolean usesTargeting() {
        return false;
    }

    @Override
    public void affectCell(int pos) {
        boolean[] passable = Dungeon.level.passable.clone();
        PathFinder.buildDistanceMap(pos, passable, 3);
        for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
            if (PathFinder.distance[mob.pos] < Integer.MAX_VALUE) {
                mob.damage(Random.IntRange(4 + Dungeon.depth*2, 10 + Dungeon.depth*6), this);
                mob.sprite.emitter().burst(Speck.factory( Speck.BUBBLE ), 30);
            }
        }
        Camera.main.shake(3, 1f);
    }
}
