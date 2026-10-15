package com.shatteredpixel.shatteredpixeldungeon.items.powers;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public abstract class Power extends Item {
    {
        cursed = false;
        cursedKnown = true;
        unique = true;
    }
    int charge = 0;
    float partialCharge = 0;
    int chargeCap = 100;

    PowerBuff passiveBuff = null;

    @Override
    public ArrayList<String> actions(Hero hero) {
        return new ArrayList<>();
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    private static final String CHARGE = "charge";
    private static final String PARTIALCHARGE = "partialcharge";
    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( CHARGE , charge );
        bundle.put( PARTIALCHARGE , partialCharge );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        if (chargeCap > 0)  charge = Math.min( chargeCap, bundle.getInt( CHARGE ));
        else                charge = bundle.getInt( CHARGE );
        partialCharge = bundle.getFloat( PARTIALCHARGE );
    }

    protected PowerBuff passiveBuff(){
        return null;
    }

    /*@Override
    public boolean collect(Bag container) {
        if (passiveBuff() != null) {
            passiveBuff = passiveBuff();
            passiveBuff.attachTo(container.owner);
        }
        return super.collect(container);
    }

    @Override
    protected void onDetach() {
        if (curUser.buff(passiveBuff().getClass()) != null) {
            curUser.buff(passiveBuff().getClass()).detach();
            passiveBuff = null;
        }
    }*/

    @Override
    public int value() {
        return 60 + 20 * Dungeon.hero.lvl;
    }

    abstract static class PowerBuff extends Buff {

    }

}
