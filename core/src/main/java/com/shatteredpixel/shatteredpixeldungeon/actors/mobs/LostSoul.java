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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFireblast;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blazing;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.sprites.LostSoulSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class LostSoul  extends UMob {

    {
        name = "lost soul";
        spriteClass = LostSoulSprite.class;

        HP = HT = 100;
        defenseSkill = 32;
        atkSkill = 46;
        dmgRed = 15;
        dmgMin = 22;
        dmgMax = 35;

        EXP = 12;
        maxLvl = 32;

        baseSpeed = 2f;
        flying = true;
        mobType = MOBTYPE_DEBUFF;
        TYPE_UNDEAD = true;
        TYPE_EVIL = true;
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        if (Random.Int( 3 ) == 0) {
            Buff.affect( enemy, Burning.class ).reignite( enemy );
        }

        return damage;
    }

    @Override
    public boolean add( Buff buff ) {
        if (buff instanceof Burning) {
            if (HP < HT) {
                HP++;
                sprite.emitter().burst( Speck.factory(Speck.HEALING), 1 );
            }
        } else if (buff instanceof Frost || buff instanceof Chill) {
            if (Dungeon.level.water[this.pos])
                damage( Random.NormalIntRange( HT / 2, HT ), buff );
            else
                damage( Random.NormalIntRange( 1, HT * 2 / 3 ), buff );
        } else {
            return super.add( buff );
        }
        return false;
    }

    @Override
    public String description() {
        return
                "A fallen hero forced to spend eternity wandering halls and protecting the Chaos Mage.";
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
    static {
        IMMUNITIES.add( Burning.class );
        IMMUNITIES.add( Blazing.class );
        IMMUNITIES.add( WandOfFireblast.class );
    }

    { immunities.addAll( IMMUNITIES ); }
}
