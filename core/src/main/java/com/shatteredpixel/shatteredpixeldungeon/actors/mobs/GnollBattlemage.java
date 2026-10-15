package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollBattlemageSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class GnollBattlemage extends Mob implements Callback {
    {
        spriteClass = GnollBattlemageSprite.class;
        baseSpeed = 1.5f;
        HP = HT = 150;
        defenseSkill = 20;
        EXP = 20;
        maxLvl = 30;
    }

    private static final float TIME_TO_ZAP	= 2f;

    public static class LightningBolt{}

    private final int chargesCap = 3;
    private int charges = chargesCap;

    private static final String CHARGES = "charges";

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put(CHARGES, charges);
        super.storeInBundle(bundle);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        charges = bundle.getInt(CHARGES);
        super.restoreFromBundle(bundle);
    }

    @Override
    public int damageRoll() {
        return Random.IntRange(30,60);
    }

    @Override
    public int attackSkill( Char target ) {
        return 36;
    }

    @Override
    protected boolean doAttack( Char enemy ) {

        if (Dungeon.level.distance( pos, enemy.pos ) <= 1) {

            return super.doAttack( enemy );

        } else {

            boolean visible = fieldOfView[pos] || fieldOfView[enemy.pos];
            if (visible) {
                sprite.zap( enemy.pos );
            }

            charges--;

            spend( TIME_TO_ZAP );

            if (hit( this, enemy, true )) {
                int dmg = damageRoll();
                if (Dungeon.level.water[enemy.pos] && !enemy.flying) {
                    dmg *= 2f;
                }
                enemy.damage( dmg, new LightningBolt() );

                enemy.sprite.centerEmitter().burst( SparkParticle.FACTORY, 3 );
                enemy.sprite.flash();

                if (enemy == Dungeon.hero) {

                    Camera.main.shake( 2, 0.3f );

                    if (!enemy.isAlive()) {
                        Dungeon.fail( getClass() );
                        GLog.n( Messages.get(this, "zap_kill") );
                    }
                }
            } else {
                enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
            }

            return !visible;
        }
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if (charges < chargesCap) {
            charges++;
            ScrollOfRecharging.charge(this);
        }
        return super.attackProc(enemy, damage);
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        if (charges <= 0) {
            return super.canAttack(enemy);
        }
        Ballistica attack = new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE);
        return !Dungeon.level.adjacent( pos, enemy.pos ) && attack.collisionPos == enemy.pos;
    }

    @Override
    protected boolean getCloser( int target ) {
        if (state == HUNTING) {
            return enemySeen && getFurther( target );
        } else {
            return super.getCloser( target );
        }
    }

    @Override
    public void call() {
        next();
    }
}
