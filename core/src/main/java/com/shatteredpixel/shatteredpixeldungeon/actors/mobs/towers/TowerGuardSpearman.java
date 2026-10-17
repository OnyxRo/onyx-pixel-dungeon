package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TowerGuardSpearmanSprite;

public class TowerGuardSpearman extends TowerGuard3{

    {
        HP = HT = 700;
        spriteClass = TowerGuardSpearmanSprite.class;

        cost = 1950;
        damageMin = 32;
        damageMax = 42;//dpt/c = 0.01897
        upgradeLevel = 8;
        defMin = 2;
        defMax = 6;
        upgCount = 0;
        regenNum = 12;
        viewDistance = 3;
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return super.canAttack(enemy) || (distance(enemy)<=2);
    }
}
