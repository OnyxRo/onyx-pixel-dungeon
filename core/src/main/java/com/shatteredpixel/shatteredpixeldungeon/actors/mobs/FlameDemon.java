package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;


import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;


import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAggression;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MonkSprite;
import com.watabou.noosa.audio.Sample;



import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.HashSet;

public class FlameDemon extends Mob {

    private static final float TIME_TO_BURN = 1f;

    public int gasTankPressure;
    public int CanFire = 1;
    //the actual affected cells
    private HashSet<Integer> affectedCells;
    //the cells to trace fire shots to, for visual effects.
    private HashSet<Integer> visualCells;
    private int direction = 0;


    {
        spriteClass = MonkSprite.class;
        properties.add(Property.DEMONIC);
        properties.add(Property.FIERY);
        HP = HT = 65;
        defenseSkill = 30;

        EXP = 20;
        maxLvl = -2;

        CanFire = 1;
    }


    @Override
    protected boolean canAttack(Char enemy) {
        Ballistica ballistica = new Ballistica(pos, enemy.pos, Ballistica.MAGIC_BOLT);
        if (CanFire == 1 && ballistica.collisionPos == enemy.pos && Dungeon.level.distance(pos,enemy.pos) < 3) {
            return true;
        } else {
            return super.canAttack(enemy);
        }
    }

    @Override
    protected boolean doAttack(Char enemy) {

        if (CanFire != 1  &&  Dungeon.level.distance(pos, enemy.pos) <= 1 ) {
            return super.doAttack(enemy);
        } else if (CanFire != 1 && Dungeon.level.distance(pos,enemy.pos) <= 3 ){
            return super.getCloser( target );
        } else if (CanFire == 1 && Dungeon.level.distance(pos,enemy.pos) >= 3 ){
            return super.getCloser( target );
        } else {

            boolean visible = (fieldOfView[pos] || fieldOfView[enemy.pos])
                    && CanFire == 1 && Dungeon.level.distance( pos, enemy.pos ) <= 3;
            if (visible) {
                sprite.attack(enemy.pos);
                spend(TIME_TO_BURN);
                CanFire--;
                shoot(this, enemy.pos);
            }

            return !visible;
        }
    }

    @Override
    protected Char chooseEnemy() {

        Terror terror = buff(Terror.class);
        if (terror != null) {
            Char source = (Char) Actor.findById(terror.object);
            if (source != null) {
                return source;
            }
        }

        StoneOfAggression.Aggression aggro = buff(StoneOfAggression.Aggression.class);
        if (aggro != null) {
            Char source = aggro.target;
            if (source != null) {
                return source;
            }
        }

        //find a new enemy if..
        boolean newEnemy = false;
        //we have no enemy, or the current one is dead
        if (enemy == null || !enemy.isAlive() || state == WANDERING)
            newEnemy = true;
            //We are an ally, and current enemy is another ally.
        else if (alignment == Alignment.ALLY && enemy.alignment == Alignment.ALLY)
            newEnemy = true;
            //We are amoked and current enemy is the hero
        else if (buff(Amok.class) != null && enemy == Dungeon.hero)
            newEnemy = true;
            //We are charmed and current enemy is what charmed us
        else if (buff(Charm.class) != null && buff(Charm.class).object == enemy.id())
            newEnemy = true;

        if (newEnemy) {

            HashSet<Char> enemies = new HashSet<>();

            //if the mob is amoked...
            if (buff(Amok.class) != null) {
                //try to find an enemy mob to attack first.
                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
                    if (mob.alignment == Alignment.ENEMY && mob != this && fieldOfView[mob.pos])
                        enemies.add(mob);

                if (enemies.isEmpty()) {
                    //try to find ally mobs to attack second.
                    for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
                        if (mob.alignment == Alignment.ALLY && mob != this && fieldOfView[mob.pos])
                            enemies.add(mob);

                    if (enemies.isEmpty()) {
                        //try to find the hero third
                        if (fieldOfView[Dungeon.hero.pos]) {
                            enemies.add(Dungeon.hero);
                        }
                    }
                }

                //if the mob is an ally...
            } else if (alignment == Alignment.ALLY) {
                //look for hostile mobs that are not passive to attack
                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
                    if (mob.alignment == Alignment.ENEMY
                            && fieldOfView[mob.pos]
                            && mob.state != mob.PASSIVE)
                        enemies.add(mob);

                //if the mob is an enemy...
            } else if (alignment == Alignment.ENEMY) {
                //and look for the hero
                if (fieldOfView[Dungeon.hero.pos]) {
                    enemies.add(Dungeon.hero);
                }

            }

            Charm charm = buff(Charm.class);
            if (charm != null) {
                Char source = (Char) Actor.findById(charm.object);
                if (source != null && enemies.contains(source) && enemies.size() > 1) {
                    enemies.remove(source);
                }
            }

            //neutral characters in particular do not choose enemies.
            if (enemies.isEmpty()) {
                return null;
            } else {
                //go after the closest potential enemy, preferring the hero if two are equidistant
                Char closest = null;
                for (Char curr : enemies) {
                    if (closest == null
                            || Dungeon.level.distance(pos, curr.pos) < Dungeon.level.distance(pos, closest.pos)
                            || Dungeon.level.distance(pos, curr.pos) == Dungeon.level.distance(pos, closest.pos) && curr == Dungeon.hero) {
                        closest = curr;
                    }
                }
                return closest;
            }

        } else
            return enemy;
    }

    public void shoot(Char ch, int pos) {
        final Ballistica shot = new Ballistica(ch.pos, pos, Ballistica.PROJECTILE);
        fx(shot, new Callback() {
            @Override
            public void call() {
                onZap(shot);
            }
        }, ch);
    }


    protected void fx(Ballistica bolt, Callback callback, Char ch) {
        //need to perform flame spread logic here so we can determine what cells to put flames in.
        affectedCells = new HashSet<>();
        visualCells = new HashSet<>();

        int maxDist = 4;
        int dist = Math.min(bolt.dist, maxDist);

        for (int i = 0; i < PathFinder.CIRCLE8.length; i++) {
            if (bolt.sourcePos + PathFinder.CIRCLE8[i] == bolt.path.get(1)) {
                direction = i;
                break;
            }
        }

        float strength = maxDist;
        for (int c : bolt.subPath(1, dist)) {
            strength--; //as we start at dist 1, not 0.
            affectedCells.add(c);
            if (strength > 1) {
            ;
                spreadFlames(c + PathFinder.CIRCLE8[direction], strength - 1);

            } else {
                visualCells.add(c);
            }
        }

        //going to call this one manually
        visualCells.remove(bolt.path.get(dist));

        for (int cell : visualCells) {
            //this way we only get the cells at the tip, much better performance.
            ((MagicMissile) ch.sprite.parent.recycle(MagicMissile.class)).reset(
                    MagicMissile.FIRE,
                    ch.sprite,
                    cell,
                    null
            );
        }
        MagicMissile.boltFromChar(ch.sprite.parent,
                MagicMissile.FIRE,
                ch.sprite,
                bolt.path.get(dist / 2),
                callback);
        if (Dungeon.level.heroFOV[bolt.sourcePos] || Dungeon.level.heroFOV[bolt.collisionPos]) {
            Sample.INSTANCE.play(Assets.Sounds.ZAP);
        }
    }

    //burn... BURNNNNN!.....
    private void spreadFlames(int cell, float strength) {
        if (strength >= 0 && (Dungeon.level.passable[cell] || Dungeon.level.flamable[cell])) {
            affectedCells.add(cell);
            if (strength >= 1.5f) {
                visualCells.remove(cell);

                spreadFlames(cell + PathFinder.CIRCLE8[direction], strength - 1.5f);

            } else {
                visualCells.add(cell);
            }
        } else if (!Dungeon.level.passable[cell])
            visualCells.add(cell);
    }

    private int left(int direction) {
        return direction == 0 ? 4 : direction - 1;
    }

    private int right(int direction) {
        return direction == 4 ? 0 : direction + 1;
    }

    protected void onZap(Ballistica bolt) {

        for (int cell : affectedCells) {

            //ignore caster cell
            if (cell == bolt.sourcePos) {
                continue;
            }

            //only ignite cells directly near caster if they are flammable
            if (!Dungeon.level.adjacent(bolt.sourcePos, cell)
                    || Dungeon.level.flamable[cell]) {
                GameScene.add(Blob.seed(cell, 1, Fire.class));
            }
        }
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 15, 35 );
    }

    @Override
    public int attackSkill(Char target) {
        return 40;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 10);
    }
}
