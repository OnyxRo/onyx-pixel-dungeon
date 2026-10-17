package com.shatteredpixel.shatteredpixeldungeon.items.towerspawners;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerTotem;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class SpawnerTotemNecrotic extends TowerSpawner {

    {
        image = ItemSpriteSheet.TOWERSPAWNER_TOTEMNECROTIC;
        stackable = true;
        defaultAction = AC_THROW;
        instanceOfTower = new TowerTotem.TotemNecrotic();

    }

    @Override
    public int value() {
        return 100;
    }


}