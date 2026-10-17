package com.shatteredpixel.shatteredpixeldungeon.items.towerspawners;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerTotem;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class SpawnerTotemShield extends TowerSpawner {

    {
        image = ItemSpriteSheet.TOWERSPAWNER_TOTEMSHIELD;
        stackable = true;
        defaultAction = AC_THROW;
        instanceOfTower = new TowerTotem.TotemShield();

    }

    @Override
    public int value() {
        return 180;
    }


}