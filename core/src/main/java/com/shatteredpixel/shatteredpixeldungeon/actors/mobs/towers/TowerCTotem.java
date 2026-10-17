package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Animated;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.watabou.utils.Bundle;

public abstract class TowerCTotem extends TowerNotliving{

    public int abTime = 0;
    public int abTimeMax = 10;
    protected void useAbility(int cell){
        //
    }
    protected void searchAndUse(){
        //for (int i : PathFinder.NEIGHBOURS25) if (Char.findChar(pos+i)!=null){
        //    if ((effectPref == EffectPref.ALLIES&& Char.findChar(pos+i).alignment==this.alignment)||(effectPref == EffectPref.FOES&& Char.findChar(pos+i).alignment!=this.alignment)) useAbility(pos+i);
        //}
    }
    @Override
    protected boolean canAttack(Char enemy) {
        return false;
    }


    @Override
    protected boolean getCloser(int target) {
        if (buff(Animated.class) !=null) {
            if (Dungeon.level.distance(pos, hero.pos)>0) return super.getCloser(hero.pos);
            else {
                return super.getCloser( target );
            }
        } else return true;
    }

    @Override
    protected boolean getFurther(int target) {
        if (buff(Animated.class) !=null) return super.getFurther(target); else return true;
    }
    @Override
    protected boolean act() {
        if (buff(Animated.class) !=null) beckon(hero.pos);
        super.act();
        Bestiary.setSeen(getClass());
        abTime++;
        if(abTime>=abTimeMax){
            searchAndUse();
            abTime=0;
        }
        return true;
    }
    private static final String ABTIME = "abtime";
    private static final String ABTIMEMAX = "abtimemax";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(ABTIME, abTime);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        abTime = bundle.getInt(ABTIME);
    }
}
