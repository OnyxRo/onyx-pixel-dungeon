package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrabSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TimelessSoulSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class TimelessSpirit extends Mob{
    {
        spriteClass = TimelessSoulSprite.class;

        HP = HT = 1;
        defenseSkill = 0;
        baseSpeed = 1f;

        EXP = 3;
        maxLvl = 25;

        viewDistance = 10;
    }
    private int dodges = 5;

    @Override
    public int damageRoll() {
        return 20;
    }

    @Override
    public int attackSkill( Char target ) {
        return 1000000;
    }

    @Override
    public int defenseProc(Char enemy, int damage) {
        return super.defenseProc(enemy, damage);
    }

    @Override
    public void damage(int dmg, Object src) {
        if (buff(PotionOfCleansing.Cleanse.class)==null) {
            if (dodges==0){
                die(enemy);
                Sample.INSTANCE.play(Assets.Sounds.GHOST);
                return;
            }
            ArrayList<Integer> candidates2 = new ArrayList<>();
            for (int n : PathFinder.NEIGHBOURS8) {
                if (Dungeon.level.passable[pos + n] && Actor.findChar(pos + n) == null) {
                    candidates2.add(pos + n);
                }
            }

            if (!candidates2.isEmpty()) {
                int posi;
                posi = Random.element(candidates2);
                ScrollOfTeleportation.appear(this, posi);
                dodges--;
                Sample.INSTANCE.play(Assets.Sounds.MISS);
                return;
            }

        }
        super.damage(1, src);
    }
    private static final String DODGES = "dodges";

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        bundle.put(DODGES, dodges);
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        dodges = bundle.getInt(DODGES);
    }
}
