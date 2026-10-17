package com.shatteredpixel.shatteredpixeldungeon.items.towerspawners;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerDartgun1;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class SpawnerDartgun extends TowerSpawner {

    {
        image = ItemSpriteSheet.TOWERSPAWNER_DARTGUN;
        stackable = true;
        defaultAction = AC_THROW;
        instanceOfTower = new TowerDartgun1();
    }

    @Override
    public int value() {
        return 36;
    }


}