/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.DeferredDeath;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Door;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WaterElementalSprite;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WaterElemental extends Mob {

    {
        spriteClass = WaterElementalSprite.class;

        HT = 400;
        HP = HT;

        defenseSkill = 25;

        EXP = 15;
        maxLvl = 30;
        resistances.add(Fire.class);

    }
    private static final float SPLIT_DELAY	= 1f;

    private int generation	= 0;

    private static final String GENERATION	= "generation";

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 46, 80 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 35;
    }

    @Override
    public boolean act() {

        if (Dungeon.level.water[pos] && HP < HT) {
            sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );

            HP+= 10;
        }

        return super.act();
    }

    @Override
    public int defenseProc(Char enemy, int damage ) {

        if (HP >= damage + 2) {
            ArrayList<Integer> candidates = new ArrayList<>();
            boolean[] solid = Dungeon.level.solid;

            int[] neighbours = {pos + 1, pos - 1, pos + Dungeon.level.width(), pos - Dungeon.level.width()};
            for (int n : neighbours) {
                if (!solid[n] && Actor.findChar( n ) == null) {
                    candidates.add( n );
                }
            }

            if (candidates.size() > 0) {

                WaterElemental clone = split();
                clone.HP = (HP - damage) / 2;
                clone.pos = Random.element( candidates );
                clone.state = clone.HUNTING;

                if (Dungeon.level.map[clone.pos] == Terrain.DOOR) {
                    Door.enter( clone.pos );
                }

                GameScene.add( clone, SPLIT_DELAY );
                Actor.addDelayed( new Pushing( clone, pos, clone.pos ), -1 );

                HP -= clone.HP;
            }
        }

        return super.defenseProc(enemy, damage);
    }

    private WaterElemental split() {
        WaterElemental clone = new WaterElemental();
        clone.generation = generation + 1;
        clone.EXP = 0;
        if (buff( Burning.class ) != null) {
            Buff.affect( clone, Burning.class ).reignite( clone );
        }
        if (buff( Poison.class ) != null) {
            Buff.affect( clone, Poison.class ).set(2);
        }
        if (buff(Corruption.class ) != null) {
            Buff.affect( clone, Corruption.class);
        }
        if (buff(DeferredDeath.class) != null) {
            clone.die(new Grim());
        }
        return clone;
    }
    public class ElementalIce {}//Used so Ring of Elements only affects extra damage.
    @Override
    public void die(Object cause) {
        super.die(cause);
    }
    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
        if (Random.Int( 2 ) == 0) {
            Buff.affect( enemy, Chill.class, 4f );
        } else {
            float duration = 0;
            Chill chill = Dungeon.hero.buff(Chill.class);
            if (chill != null) {
                duration = chill.speedFactor();
            }
            if (duration > 0) {
                enemy.damage((int)(damageRoll()*duration*0.5f),ElementalIce.class);
            }


        }

        return damage;
    }
}

