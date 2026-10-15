package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SwarmSprite;
import com.watabou.utils.Random;

public class WindSwarm extends Mob {

    {
        spriteClass = SwarmSprite.class;

        HP = HT = 55;
        defenseSkill = 24;
        baseSpeed = 2.5f;

        EXP = 25;
        maxLvl = -2;


        properties.add(Property.DEMONIC);
        flying = true;
        WANDERING = new Wandering();
        HUNTING = new Hunting();
    }


    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 12, 20 );
    }

    @Override
    public float attackDelay() {
        return super.attackDelay()*0.5f;
    }

    @Override
    public int attackSkill( Char target ) {
        return 28;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 3);
    }



}