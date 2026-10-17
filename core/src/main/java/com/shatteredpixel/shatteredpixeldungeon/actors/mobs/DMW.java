package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave.throwChar;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Inferno;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.LiquidMetal;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DMWSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class DMW extends Mob {

    {
        spriteClass = DMWSprite.class;

        HP = HT = 2300;
        defenseSkill = 5;

        EXP = 50;

        viewDistance = 5;

        maxLvl = 25;

        loot = Random.oneOf(Generator.Category.WEAPON, Generator.Category.ARMOR);
        lootChance = 0; //initially, see lootChance()

        properties.add(Property.INORGANIC);
        properties.add(Property.LARGE);
        properties.add(Property.ELECTRIC);
        properties.add(Property.BOSS);

        HUNTING = new Hunting();
        immunities.add(ToxicGas.class);
        immunities.add(Paralysis.class);
        immunities.add(Corrosion.class);
        immunities.add(Amok.class);
        immunities.add(Hex.class);

        ranged = true;

        viewDistance = 1000;

        loot = new LiquidMetal();
        lootChance = 0.5f;

    }


    //DMW phases
    //1 - gas + melee
    //2 - paralytic gas + melee
    //3 - melee + speed + jumps
    //4 - melee + corrosive gas
    //5 - Intense jumping  + melee attacks pushing the hero away + speed
    //6 - melee + speed + inferno
    //7 - head + body + wheels + minions spam
    public void setDMWPhase (int phas){
        phase = phas;

        if (phase == 1) {
            baseSpeed = 1f;
        }
        if (phase == 2) {
            baseSpeed = 1f;
        }
        if (phase == 3) {
            baseSpeed = 1.5f;
            Sample.INSTANCE.play( Assets.Sounds.BLAST);
            CellEmitter.center(pos).burst(BlastParticle.FACTORY, 30);
        }if (phase == 4) {
            baseSpeed = 1.5f;
        }if (phase == 5) {
            baseSpeed = 1.3f;
            Sample.INSTANCE.play( Assets.Sounds.BLAST);
            CellEmitter.center(pos).burst(BlastParticle.FACTORY, 30);
            properties.add(Property.IMMOVABLE);
        }
        if (phase == 6) {
            properties.remove(Property.IMMOVABLE);
            baseSpeed = 1.1f;
            Buff.affect(this, Burning.class);
            Sample.INSTANCE.play( Assets.Sounds.BLAST);
            CellEmitter.center(pos).burst(BlastParticle.FACTORY, 30);
            GameScene.add(Blob.seed(pos, 7, Inferno.class));
        };
        CellEmitter.center(pos).burst(BlastParticle.FACTORY, phase);
    }
    public int nextPhase = 1;

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 30, 50 );
    }



    @Override
    public int attackSkill( Char target ) {
        return 11;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(4, 8);
    }

    private int ventCooldown = 0;
    private int chargeCooldown = 0;

    private static final String VENT_COOLDOWN = "vent_cooldown";
    private static final String CHARGE_COOLDOWN = "charge_cooldown";
    private static final String LAST_ENEMY_POS = "last_enemy_pos";
    private static final String CHARGE_POS = "charge_pos";
    private int chargePos = -1;

    public int phase = 1;

    private int lastEnemyPos = -1;

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(VENT_COOLDOWN, ventCooldown);
        bundle.put(CHARGE_COOLDOWN, chargeCooldown);
        bundle.put(LAST_ENEMY_POS, lastEnemyPos);
        bundle.put(CHARGE_POS, chargePos);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        ventCooldown = bundle.getInt( VENT_COOLDOWN );
        chargeCooldown = bundle.getInt( CHARGE_COOLDOWN );
        lastEnemyPos = bundle.getInt(LAST_ENEMY_POS);
        chargePos = bundle.getInt(CHARGE_POS);
    }

    @Override
    protected boolean act() {
        ventCooldown--;
        chargeCooldown--;
        return super.act();
    }

    public void onZapComplete() {
        zap();
        next();
    }

    @Override
    public void damage(int dmg, Object src) {
        int x = 1;
        if (HP - dmg <= 1500) x = 2;
        if (HP - dmg <= 1200) x = 3;
        if (HP - dmg <= 800) x = 4;
        if (HP - dmg <= 600) x = 5;
        if (HP - dmg <= 200) x = 6;
        if (phase != x) setDMWPhase(x);
        super.damage(dmg, src);
    }

    private void zap( ){
        spend( TICK );

        switch (phase) {
            case 1 : {Ballistica trajectory1 = new Ballistica(pos, enemy.pos, Ballistica.STOP_TARGET);

                for (int i : trajectory1.subPath(0, trajectory1.dist)){
                    GameScene.add(Blob.seed(i, 2, ToxicGas.class));
                }
                GameScene.add(Blob.seed(trajectory1.collisionPos, 13, ToxicGas.class));
                ventCooldown = 10;
                break;
            }
            case 2 : {Ballistica trajectory2 = new Ballistica(pos, enemy.pos, Ballistica.STOP_TARGET);

                for (int i : trajectory2.subPath(0, trajectory2.dist)){
                    GameScene.add(Blob.seed(i, 13, ParalyticGas.class));
                }
                GameScene.add(Blob.seed(trajectory2.collisionPos, 13, ParalyticGas.class));
                ventCooldown = 20;
                break;
            }
            case 4 : {Ballistica trajectory4 = new Ballistica(pos, enemy.pos, Ballistica.STOP_TARGET);

                for (int i : trajectory4.subPath(0, trajectory4.dist)){
                    GameScene.add(Blob.seed(i, 10, CorrosiveGas.class));
                }
                GameScene.add(Blob.seed(trajectory4.collisionPos, 100, CorrosiveGas.class));
                ventCooldown = Random.Int(10,15);
                break;
            }
            case 6 : {Ballistica trajectory6 = new Ballistica(pos, enemy.pos, Ballistica.STOP_TARGET);

                for (int i : trajectory6.subPath(0, trajectory6.dist)){
                    GameScene.add(Blob.seed(i, 3, Inferno.class));
                }
                GameScene.add(Blob.seed(trajectory6.collisionPos, 5, Inferno.class));
                ventCooldown = Random.Int(1,2);
                break;
            }
        }

    }

    protected boolean canVent(int target){
        if (ventCooldown > 0) return false;
        PathFinder.buildDistanceMap(target, BArray.not(Dungeon.level.solid, null), Dungeon.level.distance(pos, target)+1);
        return PathFinder.distance[pos] != Integer.MAX_VALUE;
    }

    private class Hunting extends Mob.Hunting{

        public boolean act( boolean enemyInFOV, boolean justAlerted ) {

            if(phase == 3||phase == 5) {

                if (chargePos != -1) {

                    if (phase == 3) chargeCooldown = Random.NormalIntRange(5, 10);
                    if (phase == 5) chargeCooldown = 1;

                    if (rooted) {
                            chargePos = -1;
                            return true;
                        }

                        Ballistica b = new Ballistica(pos, chargePos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
                        chargePos = b.collisionPos;

                        final Char chargeVictim = Actor.findChar(chargePos);
                        final int endPos;

                        if (chargeVictim != null) {
                            int bouncepos = -1;
                            for (int i : PathFinder.NEIGHBOURS8) {
                                if ((bouncepos == -1 || Dungeon.level.trueDistance(pos, chargePos + i) < Dungeon.level.trueDistance(pos, bouncepos))
                                        && Actor.findChar(chargePos + i) == null && Dungeon.level.passable[chargePos + i]) {
                                    bouncepos = chargePos + i;
                                }
                            }
                            if (bouncepos == -1) {
                                chargePos = -1;
                                return true;
                            } else {
                                endPos = bouncepos;
                            }
                        } else {
                            endPos = chargePos;
                        }

                        //The charge
                        sprite.visible = Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[chargePos] || Dungeon.level.heroFOV[endPos];

                    float height = (phase == 3) ?  0f : 200f;
                    float duration = (phase == 3) ? 0.1f : 0.9f;


                    sprite.jump(pos, chargePos,height,duration, new Callback() {
                        @Override
                        public void call() {

                            if (chargeVictim != null && alignment != chargeVictim.alignment) {//checks whether the victim is and ally, or if the DMW misses
                                if (hit(DMW.this, chargeVictim, Char.INFINITE_ACCURACY, false)) {
                                    switch (phase) {
                                        case 3: {
                                                chargeVictim.damage(Random.Int(30,50), this);
                                                Sample.INSTANCE.play(Assets.Sounds.HIT_CRUSH);
                                                chargeVictim.sprite.flash();
                                                Camera.main.shake( 10, 0.5f );
                                                break;
                                            }
                                            case 5: {
                                                Buff.affect(chargeVictim, Cripple.class, 10);
                                                Buff.affect(chargeVictim, Vertigo.class, 3);
                                                chargeVictim.damage(Random.Int(40,60), this);
                                                Sample.INSTANCE.play(Assets.Sounds.HIT_CRUSH);
                                                chargeVictim.sprite.flash();
                                                Camera.main.shake( 40, 0.5f );
                                                CellEmitter.center(pos).burst(BlastParticle.FACTORY, 5);
                                                break;
                                            }
                                        }

                                    } else {
                                        enemy.sprite.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
                                        Sample.INSTANCE.play(Assets.Sounds.BLAST);
                                        Camera.main.shake( 30, 0.5f );
                                        CellEmitter.center(pos).burst(BlastParticle.FACTORY, 5);
                                    }
                                }

                                if (endPos != chargePos) {
                                    Actor.addDelayed(new Pushing(DMW.this, chargePos, endPos), -1);//No delay required here, for insta attack
                                }

                                pos = endPos;
                                chargePos = -1;
                                sprite.idle();
                                Dungeon.level.occupyCell(DMW.this);
                                Sample.INSTANCE.play( Assets.Sounds.BLAST);
                                CellEmitter.center(pos).burst(BlastParticle.FACTORY, 5);
                                for (int i  : PathFinder.NEIGHBOURS8){
                                    Char ch = Actor.findChar(pos + i);
                                    if (ch != null){

                                        if (ch.alignment != Char.Alignment.ALLY) ch.damage(damageRoll(), this);

                                        if (ch.pos == pos + i) {
                                            Ballistica trajectory = new Ballistica(ch.pos, ch.pos + i, Ballistica.MAGIC_BOLT);
                                            int strength = 8;
                                            throwChar(ch, trajectory, strength, false, true, getClass());
                                        }

                                    }
                                }
                                spend(1f);
                                next();
                            }
                        });
                        return false;
                    }

                    enemySeen = enemyInFOV;
                    if (enemyInFOV && !isCharmedBy(enemy) && canAttack(enemy)&&!(phase==5)) {

                        return doAttack(enemy);

                    } else {

                        if (enemyInFOV) {
                            target = enemy.pos;
                        } else if (enemy == null) {
                            state = WANDERING;
                            target = Dungeon.level.randomDestination(DMW.this);
                            return true;
                        }

                        if (chargeCooldown <= 0 && enemyInFOV && !rooted
                                && ((Dungeon.level.distance(pos, enemy.pos) >= 1 && phase == 5) || (Dungeon.level.distance(pos, enemy.pos) >= 3 && phase == 3))) {

                            int targetPos = enemy.pos;
                            if (lastEnemyPos != enemy.pos) {
                                int closestIdx = 0;
                                for (int i = 1; i < PathFinder.CIRCLE8.length; i++) {
                                    if (Dungeon.level.trueDistance(lastEnemyPos, enemy.pos + PathFinder.CIRCLE8[i])
                                            < Dungeon.level.trueDistance(lastEnemyPos, enemy.pos + PathFinder.CIRCLE8[closestIdx])) {
                                        closestIdx = i;
                                    }
                                }
                                targetPos = enemy.pos + PathFinder.CIRCLE8[(closestIdx + 4) % 8];
                            }

                            Ballistica b = new Ballistica(pos, targetPos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
                            //try aiming directly at hero if aiming near them doesn't work
                            if (b.collisionPos != targetPos && targetPos != enemy.pos) {
                                targetPos = enemy.pos;
                                b = new Ballistica(pos, targetPos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
                            }
                            if (b.collisionPos == targetPos) {
                                //get ready to charge
                                chargePos = targetPos;
                                speak ("CHARGE READY", CharSprite.WARNING);
                                //don't want to overly punish players with slow move or attack speed
                                spend(3);
                                if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[chargePos]) {
                                    GLog.w(Messages.get(DMW.class, "leap"));
                                    sprite.parent.addToBack(TargetedCell.colored(chargePos, 0xFF0000));
                                    ((DMWSprite) sprite).chargePrep(chargePos);
                                    Dungeon.hero.interrupt();
                                }
                                return true;
                            }
                        }

                        int oldPos = pos;
                        if (target != -1 && getCloser(target)) {

                            spend(1 / speed());
                            return moveSprite(oldPos, pos);

                        } else {
                            spend(TICK);
                            if (!enemyInFOV) {
                                sprite.showLost();
                                state = WANDERING;
                                target = Dungeon.level.randomDestination(DMW.this);
                            }
                            return true;
                        }
                    }

                }

            if (phase == 1 || phase == 2 || phase == 4 || phase == 6){
                if (!enemyInFOV || canAttack(enemy)) {
                    return super.act(enemyInFOV, justAlerted);
                } else {
                    enemySeen = true;
                    target = enemy.pos;

                    int oldPos = pos;

                    if (distance(enemy) >= 1 && Random.Int(100 / distance(enemy)) == 0 && canVent(target)) {
                        if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
                            sprite.zap(enemy.pos);
                            return false;
                        } else {
                            zap();
                            return true;
                        }

                    } else if (getCloser(target)) {
                        spend(1 / speed());
                        return moveSprite(oldPos, pos);

                    } else if (canVent(target)) {
                        if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
                            sprite.zap(enemy.pos);
                            return false;
                        } else {
                            zap();
                            return true;
                        }

                    } else {
                        spend(TICK);
                        return true;
                    }

                    }
                }
            //phase choosing, no return to previous phases
            phase = nextPhase;

        return false;
        }
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        if (phase == 3) return false;
        else return (super.canAttack(enemy));
    }
    @Override
    protected boolean getCloser( int target ) {
        if (state == HUNTING &&(phase == 3 || phase == 4)) {
            return enemySeen && getFurther( target );
        } else {
            return super.getCloser( target );
        }
    }

    @Override
    public void die(Object cause) {


        ArrayList<Integer> candidates = new ArrayList<>();
        for (int n : PathFinder.NEIGHBOURS8) {
            if (Dungeon.level.passable[pos+n] && Actor.findChar( pos+n ) == null) {
                candidates.add( pos+n );
                CellEmitter.center(pos+n).burst(BlastParticle.FACTORY, 5);
            }
        }
        Sample.INSTANCE.play(Assets.Sounds.BLAST);
        CellEmitter.center(pos).burst(SmokeParticle.FACTORY, 5);

        if (!candidates.isEmpty()) {
            HashSet<Mob> toSpawn = new HashSet<>();
            toSpawn.add(new DMWHead());
            toSpawn.add(new DMWBody());
            toSpawn.add(new DMWWheels());
            for (Mob mob : toSpawn){
                mob.pos = Random.element(candidates);
                mob.state = mob.HUNTING;
                mob.alignment = alignment;
                GameScene.add(mob, 1);
                Dungeon.level.occupyCell(mob);
                if (sprite.isVisible()) Actor.addDelayed(new Pushing(mob, pos, mob.pos), -1);
                for (Buff b : buffs(ChampionEnemy.class)){
                    Buff.affect( mob, b.getClass());
                }
            }
        }

        super.die(cause);
    }
}