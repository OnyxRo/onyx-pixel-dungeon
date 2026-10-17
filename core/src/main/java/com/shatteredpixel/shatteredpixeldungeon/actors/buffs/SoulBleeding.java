package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

public class SoulBleeding extends Buff {

    {
        type = buffType.NEGATIVE;
    }

    protected int damage = 0;

    private static final String DAMAGE	= "damage";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( DAMAGE, damage );

    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        damage = bundle.getInt( DAMAGE );
    }

    @Override
    public boolean attachTo( Char target ) {
        if (super.attachTo( target )) {
            postpone( TICK );
            return true;
        } else {
            return false;
        }
    }

    public void prolong( int damage ) {
        this.damage += damage;
    }

    @Override
    public int icon() {
        return BuffIndicator.SOULBLEEDING;
    }

    @Override
    public String iconTextDisplay() {
        return Integer.toString(damage);
    }

    @Override
    public boolean act() {
        if (target.isAlive()) {

            int damageThisTick = Math.max(1, (int)(damage*0.05f));
            target.damage( damageThisTick, this );
            if (target == Dungeon.hero && !target.isAlive()) {

                Badges.validateDeathFromFriendlyMagic();

                Dungeon.fail( getClass() );
                GLog.n( Messages.get(this, "ondeath") );
            }
            spend( TICK );

            damage -= damageThisTick;
            if (damage <= 0) {
                detach();
            }

        } else {

            detach();

        }

        return true;
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", damage);
    }
}