package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.watabou.utils.Random;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.sprites.YetiSprite;

import java.util.HashSet;

public class Yeti extends UMob {

    {
        name = "yeti";
//port-oldstats
public class Yeti extends Mob {

    {
        spriteClass = YetiSprite.class;

        HP = HT = 120;
        defenseSkill = 30;
        atkSkill = 34;
        dmgRed = 20;
        dmgMin = 20;
        dmgMax = 45;

        EXP = 10;
        maxLvl = 32;
        mobType = MOBTYPE_TOUGH;
        TYPE_ANIMAL = true;
    }

    @Override
    public String defenseVerb() {
        return "blocked";
    }

    @Override
    public String description() {
        return
                "The Yeti is a strange and elusive creature that was probable drawn here by the intense cold.";
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
    static {
        IMMUNITIES.add( Paralysis.class );
        IMMUNITIES.add( Freezing.class );
    }

    { immunities.addAll( IMMUNITIES ); }

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

