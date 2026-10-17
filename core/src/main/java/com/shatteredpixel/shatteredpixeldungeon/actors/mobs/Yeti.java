package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.watabou.utils.Random;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.sprites.YetiSprite;

import java.util.HashSet;

//port-oldstats
public class Yeti extends Mob {

    {
        spriteClass = YetiSprite.class;

        HP = HT = 120;
        defenseSkill = 30;

        EXP = 10;
        maxLvl = 32;
        immunities.add( Paralysis.class );
        immunities.add( Freezing.class );
    }

    @Override
    public int attackSkill( Char target ) {
        return 34;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 20, 45 );
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange( 0, 20 );
    }


    @Override
    public String defenseVerb() {
        return "blocked";
    }
}

