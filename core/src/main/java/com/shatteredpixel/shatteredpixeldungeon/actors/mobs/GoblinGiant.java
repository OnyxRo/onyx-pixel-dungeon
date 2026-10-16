package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GoblinGiantSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class GoblinGiant extends Goblin {

    {
        spriteClass = GoblinGiantSprite.class;

        HP = HT = 150;
        defenseSkill = 4;

        viewDistance = 10;
        baseSpeed = 1.05f;

        EXP = 25;
        maxLvl = 15;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(20, 30);
    }

    @Override
    public int attackSkill(Char target) {
        return 13;
    }

    @Override
    public float attackDelay() {
        return super.attackDelay() * 2f;
    }

    private int wasScared = 0;

    @Override
    protected boolean act() {
        if (buff(PotionOfCleansing.Cleanse.class) == null) {
            if (HP < HT) HP += 15;
            if (HP < HT * 3 / 5 && wasScared < 3 && buff(Terror.class) == null) {
                wasScared++;
                Buff.affect(this, Terror.class, 7);
                Sample.INSTANCE.play(Assets.Sounds.FALLING, 1, 0.9f);
            }
        }

        return super.act();
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 1);
    }

}
