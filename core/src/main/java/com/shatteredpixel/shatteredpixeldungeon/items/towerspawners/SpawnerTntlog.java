package com.shatteredpixel.shatteredpixeldungeon.items.towerspawners;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerTntLog;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class SpawnerTntlog extends TowerSpawner {

    {
        image = ItemSpriteSheet.TOWERSPAWNER_TNTLOG;
        stackable = true;
        defaultAction = AC_THROW;
        instanceOfTower = new TowerTntLog();
    }

    @Override
    public int value() {
        return 80;
    }

}