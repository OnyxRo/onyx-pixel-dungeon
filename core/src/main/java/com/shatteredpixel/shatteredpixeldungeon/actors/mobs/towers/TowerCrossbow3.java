package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TowerCrossbow3Sprite;

public class TowerCrossbow3 extends TowerCrossbow2{

    {
        HP = HT = 110;
        spriteClass = TowerCrossbow3Sprite.class;

        baseAttackDelay = 1.111f;//dpt=0.0072
        cost = 1000;
        upgCount=2;

        upgradeLevel = 11;

        upgrade1Cost = 1000;
        upgrade2Cost = 1000;
        damageMin = 4;
        damageMax = 12;
    }
    @Override
    public int attackSkill(Char target) {
        return 14;
    }
}
