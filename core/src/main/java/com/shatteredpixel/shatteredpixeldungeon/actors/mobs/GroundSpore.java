package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ConfusionGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RotHeartSprite;

public class GroundSpore extends Mob {

    {
        spriteClass = RotHeartSprite.class;

        HP = HT = 50;
        defenseSkill = 0;

        EXP = 0;
        maxLvl = -2;

        state = PASSIVE;

        properties.add(Property.IMMOVABLE);
        properties.add(Property.MINIBOSS);
    }

    public float lifespan;

    private boolean initialized = false;

    @Override
    protected boolean act() {
        if (initialized) {
            HP = 0;
            die(null);
            sprite.die();

        } else {
            initialized = true;
            spend( lifespan + 8);
        }
        GameScene.add(Blob.seed(pos, 45, ToxicGas.class));
        GameScene.add(Blob.seed(pos, 45, ConfusionGas.class));
        return true;
    }

    @Override
    public void damage(int dmg, Object src) {
        if (dmg >= 20){
            dmg = 19 + (int)(Math.sqrt(8*(dmg - 19) + 1) - 1)/2;
        }
        super.damage(dmg, src);
    }


    {
        immunities.add( Paralysis.class );
        immunities.add( Amok.class );
        immunities.add( Sleep.class );
        immunities.add( ToxicGas.class );
        immunities.add( Terror.class );
        immunities.add( Vertigo.class );
    }





}
