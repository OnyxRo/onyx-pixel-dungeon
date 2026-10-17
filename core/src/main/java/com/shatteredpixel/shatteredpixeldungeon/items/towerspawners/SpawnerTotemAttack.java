package com.shatteredpixel.shatteredpixeldungeon.items.towerspawners;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.Tower;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerTotem;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class SpawnerTotemAttack extends TowerSpawner {

    {
        image = ItemSpriteSheet.TOWERSPAWNER_TOTEMATTACK;
        stackable = true;
        defaultAction = AC_THROW;
        instanceOfTower = new TowerTotem.TotemAttack();
    }

    @Override
    public int value() {
        return 200;
    }

}