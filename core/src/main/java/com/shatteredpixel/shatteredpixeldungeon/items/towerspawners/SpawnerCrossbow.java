package com.shatteredpixel.shatteredpixeldungeon.items.towerspawners;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerCrossbow1;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class SpawnerCrossbow extends TowerSpawner {

    {
        image = ItemSpriteSheet.TOWERSPAWNER_CROSSBOW;
        stackable = true;
        defaultAction = AC_THROW;
        instanceOfTower = new TowerCrossbow1();
    }

    @Override
    public int value() {
        return 40;
    }

}
