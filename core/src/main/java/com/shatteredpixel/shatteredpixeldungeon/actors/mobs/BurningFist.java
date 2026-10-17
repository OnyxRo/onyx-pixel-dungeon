/*
 * Unleashed Pixel Dungeon
 * Copyright (C) 2015  David Mitchell
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BurningFistSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.Utils;

import java.util.HashSet;

public class BurningFist extends UMob {

    {
        name = "burning fist";
        spriteClass = BurningFistSprite.class;

        HP = HT = 200;
        atkSkill = 36;
        defenseSkill = 25;
        dmgRed = 15;
        dmgMin = 20;
        dmgMax = 32;

        EXP = 0;

        TYPE_EVIL = true;
        state = WANDERING;
    }

    private String TXT_DESC = "The Burning Fist is an extension of an ancient god into our dimension.";
import com.shatteredpixel.shatteredpixeldungeon.sprites.GobBurningFistSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.HashSet;

//port-oldstats
public class BurningFist extends Mob {

    {
        spriteClass = GobBurningFistSprite.class;

        HP = HT = 200;
        defenseSkill = 25;

        EXP = 0;

        state = WANDERING;
        immunities.add( Amok.class );
        immunities.add( Sleep.class );
        immunities.add( Terror.class );
        immunities.add( Burning.class );
        immunities.add( Paralysis.class );
        immunities.add( Vertigo.class );
        resistances.add( ToxicGas.class );
        resistances.add( Grim.class );
    }

    @Override
    public int attackSkill( Char target ) {
        return 36;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 20, 32 );
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange( 0, 15 );
    }


    private String TXT_DESC = "This Burning Fist was once the left hand of the Sacred.";

    @Override
    protected boolean canAttack( Char enemy ) {
        return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
    }

    @Override
    public boolean attack( Char enemy, float dmgMulti, float dmgBonus, float accMulti ) {

        if (!Dungeon.level.adjacent(pos, enemy.pos)) {
            spend( attackDelay() );

            if (hit( this, enemy, true )) {

                int dmg =  damageRoll();
                enemy.damage( dmg, this );

                enemy.sprite.bloodBurstA( sprite.center(), dmg );
                enemy.sprite.flash();

                if (!enemy.isAlive() && enemy == Dungeon.hero) {
                    Dungeon.fail( getClass() );
                    GLog.n( "You were killed by " + name + "..." );
                    Dungeon.fail( this );
                    GLog.n("%s crushes you with a flaming fist.", name());
                }
                return true;

            } else {

                enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
                return false;
            }
        } else {
            return super.attack( enemy, dmgMulti, dmgBonus, accMulti );
        }
    }

    @Override
    public boolean act() {

        for (int i=0; i < PathFinder.NEIGHBOURS9.length; i++) {
            GameScene.add(Blob.seed(pos + PathFinder.NEIGHBOURS9[i], 2, Fire.class));
        for (int i=0; i < com.watabou.utils.PathFinder.NEIGHBOURS9.length; i++) {
            GameScene.add(Blob.seed(pos + com.watabou.utils.PathFinder.NEIGHBOURS9[i], 2, Fire.class));
        }

        return super.act();
    }

    @Override
    public String description() {
        return TXT_DESC;

    }

    private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
    static {
        RESISTANCES.add( ToxicGas.class );
        RESISTANCES.add( Grim.class );

    }

    { resistances.addAll( RESISTANCES ); }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
    static {
        IMMUNITIES.add( Amok.class );
        IMMUNITIES.add( Sleep.class );
        IMMUNITIES.add( Terror.class );
        IMMUNITIES.add( Burning.class );
        IMMUNITIES.add( ScrollOfPsionicBlast.class );
        IMMUNITIES.add( Vertigo.class );
    }

    { immunities.addAll( IMMUNITIES ); }
}
