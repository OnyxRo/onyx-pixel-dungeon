package com.shatteredpixel.shatteredpixeldungeon.items.towerspawners;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerWall1;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class SpawnerWall extends TowerSpawner {
    {
        image = ItemSpriteSheet.TOWERSPAWNER_WALL;
        stackable = true;
        defaultAction = AC_THROW;
        instanceOfTower = new TowerWall1();
    }

    @Override
    public int value() {
        return 20;
    }


}
