package com.shatteredpixel.shatteredpixeldungeon.items.towerspawners;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerLightning1;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class SpawnerLightning extends TowerSpawner {

    {
        image = ItemSpriteSheet.TOWERSPAWNER_LIGHTNING;
        stackable = true;
        defaultAction = AC_THROW;
        instanceOfTower = new TowerLightning1();
    }

    @Override
    public int value() {
        return 40;
    }


}