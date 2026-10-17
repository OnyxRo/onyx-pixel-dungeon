package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers;


import com.shatteredpixel.shatteredpixeldungeon.sprites.TowerCannon2Sprite;


public class TowerCannon2 extends TowerCannon1{

    {
        HP = HT = 80;
        spriteClass = TowerCannon2Sprite.class;




        upgradeLevel = 8;

        cost = 600;
        upgrade1Cost = 700;
        damageMin = 4;
        damageMax = 11;
        damageExplosionMult = 0.5f;
    }
}
