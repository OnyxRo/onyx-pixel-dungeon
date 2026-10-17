/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
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
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paranoid;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DroneSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Drone extends Mob {

    {
        spriteClass = DroneSprite.class;

        viewDistance = 4;

        EXP = 0;

        flying = true;
        state = WANDERING;
        alignment = Alignment.ALLY;
        immunities.add(Poison.class);
        immunities.add(Paranoid.class);
        properties.add(Property.INORGANIC);
    }

    private int level;

    //-1 refers to a pot that has gone missing.
    {
        HP = HT = 8;
    }

    private int potPos;
    //-1 for no owner
    private int potHolder;

    private static final String LEVEL = "level";
    private static final String POTPOS = "potpos";
    private static final String POTHOLDER = "potholder";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LEVEL, level);
        bundle.put(POTPOS, potPos);
        bundle.put(POTHOLDER, potHolder);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        spawn(bundle.getInt(LEVEL));
        potPos = bundle.getInt(POTPOS);
        potHolder = bundle.getInt(POTHOLDER);
    }

    public void spawn(int level) {
        this.level = level;

        HT = (2 + level) * 4;
        defenseSkill = 9 + level;
    }

    public void setPotInfo(int potPos, Char potHolder) {
        this.potPos = potPos;
        if (potHolder == null)
            this.potHolder = -1;
        else
            this.potHolder = potHolder.id();
    }

    /**
     * Used when a live controller is rendered for parts. The drone remains
     * intact, but falls back to the normal hostile-mob behaviour.
     */
    public void becomeHostile() {
        alignment = Alignment.ENEMY;
        setPotInfo(-1, null);
        if (Dungeon.hero != null) {
            aggro(Dungeon.hero);
        }
    }

    @Override
    public int attackSkill(Char target) {
        return defenseSkill;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(HT / 10, HT / 4);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if (enemy instanceof Mob) {
            ((Mob) enemy).aggro(this);
        }
        return damage;
    }

    @Override
    protected Char chooseEnemy() {
        if (alignment == Alignment.ENEMY) {
            return super.chooseEnemy();
        }

        //if the controller is no longer present, clear mines or idle.
        if (potHolder == -1 && potPos == -1) {
            return null;
        }

            //if something is holding the controller, follow and defend them.
        else if (Actor.findById(potHolder) != null) {
            Actor holder = Actor.findById(potHolder);
            potPos = ((Char) holder).pos;
            if (holder instanceof Mob && ((Mob) holder).alignment == Alignment.ENEMY) {
                return (Char) holder;
            }

            Char hostileTarget = findHostileTarget(potPos);
            if (hostileTarget != null) {
                return hostileTarget;
            }
            return null;
        }

            //if the pot is on the ground
        else {

            //if already targeting something, and that thing is still alive and near the pot, keeping targeting it.
            if (enemy != null && enemy.isAlive() && Dungeon.level.mobs.contains(enemy)
                    && Dungeon.level.heroFOV[enemy.pos] && enemy.invisible == 0
                    && Dungeon.level.distance(enemy.pos, potPos) <= 3)
                return enemy;

            //pick one, if there are none, clear nearby mines or idle.
            Char hostileTarget = findHostileTarget(potPos);
            if (hostileTarget != null) return hostileTarget;
            return null;
        }
    }

    protected boolean seesEnemy(Char enemy) {
        return enemy != null
                && enemy.isAlive()
                && enemy.invisible <= 0
                && Dungeon.level.distance(pos, enemy.pos) <= viewDistance;
    }

    @Override
    protected boolean getCloser(int target) {
        Actor holder = Actor.findById(potHolder);
        if (holder instanceof Char) {
            potPos = ((Char) holder).pos;
        }

        if (enemy != null && holder == enemy) {
            target = enemy.pos;
        } else if (potPos != -1 && state == WANDERING) {
            if (Dungeon.level.distance(pos, potPos) > 2) {
                this.target = target = potPos;
            } else if (target == -1 || target == pos || Dungeon.level.distance(target, potPos) > 4) {
                this.target = target = patrolDestination(potPos);
            }
        }
        return super.getCloser(target);
    }

    private Char findHostileTarget(int origin) {
        HashSet<Char> enemies = new HashSet<>();
        for (Mob mob : Dungeon.level.mobs) {
            if (!(mob instanceof Drone) && mob.alignment == Alignment.ENEMY && mob.invisible <= 0
                    && (Dungeon.level.distance(mob.pos, pos) <= viewDistance
                    || origin != -1 && Dungeon.level.distance(mob.pos, origin) <= viewDistance)) {
                enemies.add(mob);
            }
        }
        return enemies.size() > 0 ? Random.element(enemies) : null;
    }

    private int patrolDestination(int origin) {
        HashSet<Integer> candidates = new HashSet<>();
        for (int i = 0; i < Dungeon.level.passable.length; i++) {
            if (Dungeon.level.passable[i] && Actor.findChar(i) == null && Dungeon.level.distance(i, origin) <= 3) {
                candidates.add(i);
            }
        }
        return candidates.size() > 0 ? Random.element(candidates) : origin;
    }

}
