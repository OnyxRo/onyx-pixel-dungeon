package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MagicShardSprite;
import com.watabou.utils.Random;

public class MagicShard extends Mob {

    public int level = 1;
    {
        spriteClass = MagicShardSprite.class;
        flying = true;
        baseSpeed = 2;

        HP = HT = 1;
        defenseSkill = 15;
    }



    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 1, level);
    }

    @Override
    public int attackSkill( Char target ) {
        return 10;
    }

    @Override
    public int drRoll() {
        return 0;
    }
}
