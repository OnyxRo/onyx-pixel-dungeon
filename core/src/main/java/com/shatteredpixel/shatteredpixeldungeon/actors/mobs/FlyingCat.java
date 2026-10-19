/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FlyingCatSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.HashSet;

public class FlyingCat extends Mob implements Callback {

    private static final float TIME_TO_ZAP	= 1f;

    {
        spriteClass = FlyingCatSprite.class;

        HP = HT = Dungeon.depth*2 + 10;
        defenseSkill = 22;

        EXP = 13;
        maxLvl = 25;

        loot = Generator.Category.POTION;
        lootChance = 0.83f;

        properties.add(Property.DEMONIC);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 2, 3 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 3;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 3);
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
    }

    protected boolean doAttack( Char enemy ) {

        if (Dungeon.level.adjacent( pos, enemy.pos )) {

            return super.doAttack( enemy );

        } else {

            boolean visible = Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[enemy.pos];
            if (visible) {
                sprite.zap( enemy.pos );
            } else {
                zap();
            }

            return !visible;
        }
    }

    private void zap() {
        spend( TIME_TO_ZAP );

        if (hit( this, enemy, true )) {

            }

            int dmg = Random.Int( 1, 1 );
            enemy.damage( dmg, this );

            if (!enemy.isAlive() && enemy == Dungeon.hero) {
                Dungeon.fail( getClass() );
                GLog.n( Messages.get(this, "bolt_kill") );
            }
    }

    public void onZapComplete() {
        zap();
        next();
    }

    @Override
    public void call() {
        next();
    }

    @Override
    public Item createLoot(){
        Item loot = super.createLoot();

        if (loot instanceof PotionOfHealing){

            //count/10 chance of not dropping potion
            if (Random.Int(10)-Dungeon.LimitedDrops.WARLOCK_HP.count < 0){
                return null;
            } else
                Dungeon.LimitedDrops.WARLOCK_HP.count++;

        }

        return loot;
    }

    {
        resistances.add( Grim.class );
    }
}
