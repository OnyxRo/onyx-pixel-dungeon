package com.shatteredpixel.shatteredpixeldungeon.items.towerspawners;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerGuard1;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class SpawnerGuard extends TowerSpawner {

    {
        image = ItemSpriteSheet.TOWERSPAWNER_GUARD;
        stackable = true;
        defaultAction = AC_THROW;
        instanceOfTower = new TowerGuard1();
    }

    @Override
    public int value() {
        return 30;
    }


}