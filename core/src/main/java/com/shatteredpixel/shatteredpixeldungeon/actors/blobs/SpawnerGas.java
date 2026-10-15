package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Minion;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.utils.Random;

public class SpawnerGas extends SmokeScreen {
    @Override
    protected void evolve() {
        super.evolve();
        int cell;

        for (int i = area.left; i < area.right; i++){
            for (int j = area.top; j < area.bottom; j++){
                if (Random.Int(600) == 1) {
                    cell = i + j*Dungeon.level.width();
                    Minion mob = new Minion();
                    mob.state = mob.WANDERING;
                    mob.pos = cell;
                    GameScene.add(mob);
                }

            }
        }
    }
}



