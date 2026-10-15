package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WraithSprite;
import com.watabou.utils.Random;

public class FakeWraith extends Mob {

    {
        spriteClass = WraithSprite.class;

        HP = HT = 1;
        defenseSkill = 100;

        maxLvl = -2;

        flying = true;

        properties.add(Property.UNDEAD);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 4, 8 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 20;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 1);
    }


}
