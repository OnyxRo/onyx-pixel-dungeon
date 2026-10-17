package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.sprites.YetiSprite;

import java.util.HashSet;

public class Yeti extends UMob {

    {
        name = "yeti";
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
}

