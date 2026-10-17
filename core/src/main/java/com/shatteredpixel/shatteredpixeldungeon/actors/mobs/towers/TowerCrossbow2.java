package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TowerCrossbow2Sprite;

public class TowerCrossbow2 extends TowerCrossbow1{
    {
        HP = HT = 75;
        spriteClass = TowerCrossbow2Sprite.class;


        baseAttackDelay = 1.5f;//dpt/c = 0.008

        upgradeLevel = 8;

        cost = 450;
        upgrade1Cost = 550;
        damageMin = 3;
        damageMax = 8;
    }
    @Override
    public int attackSkill(Char target) {
        return 13;
    }
}
