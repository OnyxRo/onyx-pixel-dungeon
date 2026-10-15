package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Inferno;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.AdultElementalSprite;
import com.watabou.utils.Random;

public class AdultFireElemental extends Elemental.FireElemental {
    {
        spriteClass = AdultElementalSprite.class;
        HP = HT = 250;
        defenseSkill = 30;

        EXP = 25;
        maxLvl = 30;
    }

    @Override
    public boolean act() {

        GameScene.add(Blob.seed(pos, 20, Inferno.class));

        return super.act();
    }
    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 70, 110 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 35;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 25);
    }
}
