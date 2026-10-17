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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.sprites.UndeadSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GobUndeadSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Undead extends UMob {
//port-oldstats
public class Undead extends Mob {

    public static int count = 0;

    {
        name = "undead dwarf";
        spriteClass = UndeadSprite.class;

        HP = HT = 28;
        defenseSkill = 15;
        atkSkill = 16;
        dmgMin = 12;
        dmgMax = 16;
        dmgRed = 5;

        EXP = 0;
        TYPE_EVIL = true;
        TYPE_UNDEAD = true;
        TYPE_MINDLESS = true;

        state = WANDERING;
    }

    @Override
        spriteClass = GobUndeadSprite.class;

        HP = HT = 28;
        defenseSkill = 15;

        EXP = 0;

        state = WANDERING;
        properties.add( Property.UNDEAD );
        immunities.add( Grim.class );
        immunities.add( Paralysis.class );
    }

    @Override
    public int attackSkill( Char target ) {
        return 16;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 12, 16 );
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange( 0, 5 );
    }


    @Override
    protected void onAdd() {
        count++;
        super.onAdd();
    }

    @Override
    protected void onRemove() {
        count--;
        super.onRemove();
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        if (Random.Int(5) == 0) {
            Buff.prolong(enemy, Paralysis.class, 1);
        }

        return damage;
    }

    @Override
    public void damage( int dmg, Object src ) {
        super.damage( dmg, src );
        if (src instanceof ToxicGas) {
            ((ToxicGas)src).clear( pos );
        }
    }

    @Override
    public void die( Object cause ) {
        super.die( cause );

        if (Dungeon.level.heroFOV[pos]) {
            Sample.INSTANCE.play( Assets.Sounds.BONES );
        }
    }

    @Override
    public String defenseVerb() {
        return "blocked";
    }

    @Override
    public String description() {
        return
                "These undead dwarves, risen by the will of the King of Dwarves, were members of his court. " +
                        "They appear as skeletons with a stunning amount of facial hair.";
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
    static {
        IMMUNITIES.add( Grim.class );
        IMMUNITIES.add( Paralysis.class );
    }

    { immunities.addAll( IMMUNITIES ); }
}
