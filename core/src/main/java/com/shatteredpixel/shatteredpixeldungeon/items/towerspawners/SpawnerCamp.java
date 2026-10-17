package com.shatteredpixel.shatteredpixeldungeon.items.towerspawners;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerRatCamp;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class SpawnerCamp extends TowerSpawner {

    {
        image = ItemSpriteSheet.TOWERSPAWNER_CAMP;
        stackable = true;
        defaultAction = AC_THROW;
        instanceOfTower = new TowerRatCamp();
    }

    @Override
    public int value() {
        return 40;
    }



}