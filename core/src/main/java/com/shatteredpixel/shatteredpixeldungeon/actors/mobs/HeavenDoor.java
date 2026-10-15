package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;



import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.DreadPlague;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GolemSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class HeavenDoor extends Mob {

    {
        spriteClass = GolemSprite.class;

        HP = HT = 70;
        defenseSkill = 12;

        EXP = 22;
        maxLvl = -2;

        properties.add(Property.DEMONIC);
        properties.add(Property.LARGE);

        WANDERING = new Wandering();
        HUNTING = new Hunting();
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );

        if (Random.Int( 3 ) == 0) {
            Buff.affect( enemy, Vertigo.class,5f );
            Sample.INSTANCE.play( Assets.Sounds.SCAN );
        }
        if (Random.Int( 1 ) == 0) {
            Buff.affect( enemy, Paralysis.class,2f );
            Sample.INSTANCE.play( Assets.Sounds.SCAN );
        }

        return damage;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 15, 40 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 28;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 19);
    }

    {
        immunities.add( Burning.class );
        immunities.add( Terror.class );
    }



}