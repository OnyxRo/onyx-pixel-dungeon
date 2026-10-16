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

import com.shatteredpixel.shatteredpixeldungeon.sprites.deep.*;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StenchGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.util.HashSet;

public class PurpleMushroom extends Mob {

    {
        spriteClass = PurpleMushroomSprite.class;

        HP = HT = 20;
        defenseSkill = 14;

        EXP = 5;
        maxLvl = 21;

        loot = Generator.Category.SEED;
        lootChance = 1f;

    }

    private int TurnCounter;

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(2, 3);
    }

    @Override
    public int attackSkill(Char target) {
        return 12;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 2);
    }


    @Override
    public int attackProc(Char enemy, int damage) {

        if (Random.Int(5)==1)
        Buff.affect(enemy, Poison.class);

        return damage;
    }

    @Override
    public int defenseProc(Char enemy, int damage) {

        if (TurnCounter > 2) {

            GameScene.add(Blob.seed(pos, 100, StenchGas.class));
            GameScene.add(Blob.seed(pos, 100, ParalyticGas.class));
            GameScene.add(Blob.seed(pos, 100, ToxicGas.class));
        }
        return super.defenseProc(enemy, damage);
    }
    @Override
    protected boolean act() {

        if (HP<HT){
            TurnCounter++;
        }

        if (TurnCounter>2){
            GLog.n("The Purple Mushroom withdraws in its cap!");
        }

        if (TurnCounter>4){
            TurnCounter = -1;
        }

        return super.act();
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();

    static {
        IMMUNITIES.add(Poison.class);
        IMMUNITIES.add(Paralysis.class);
    }
}