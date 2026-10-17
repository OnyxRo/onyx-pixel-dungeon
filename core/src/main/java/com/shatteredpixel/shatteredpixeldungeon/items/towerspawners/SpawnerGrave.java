package com.shatteredpixel.shatteredpixeldungeon.items.towerspawners;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerGrave1;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class SpawnerGrave extends TowerSpawner {

    {
        image = ItemSpriteSheet.TOWERSPAWNER_GRAVE;
        stackable = true;
        defaultAction = AC_THROW;
        instanceOfTower = new TowerGrave1();
    }

    @Override
    public int value() {
        return 50;
    }


}
