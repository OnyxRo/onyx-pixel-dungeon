package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

public class AbyssalInfection extends Buff implements Hero.Doom {

    private static final float INFECTION	= 10f;

    public static final float LIGHTINFECTION	= 550f;
    public static final float MIDINFECTION	= 1000f;
    public static final float HARDINFECTION	= 1400f;

    private float level;
    private float partialDamage;

    private static final String LEVEL			= "level";
    private static final String PARTIALDAMAGE 	= "partialDamage";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( LEVEL, level );
        bundle.put( PARTIALDAMAGE, partialDamage );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        level = bundle.getFloat( LEVEL );
        partialDamage = bundle.getFloat(PARTIALDAMAGE);
    }

    @Override
    public boolean act() {

        if (Dungeon.level.locked ){
            spend(INFECTION);
            return true;
        }

        if (target.isAlive() && target instanceof Hero) {

            Hero hero = (Hero)target;

            if (isMIDINFECTION() && level < 1400f) {

                partialDamage += INFECTION * target.HT/1000f;

                if (partialDamage > 1){
                    target.damage( (int)partialDamage, this);
                    partialDamage -= (int)partialDamage;
                    Buff.affect( hero, Vulnerable.class, 15);
                    Buff.affect( hero, Weakness.class, 15);
                    level= level+20;
                }

            } else {


                float newLevel = level + INFECTION;
                if (newLevel >= HARDINFECTION) {

                    GLog.w(Messages.get(this, "onhardinfection"));
                    hero.resting = false;
                    hero.damage(10, this);
                    Buff.affect(hero, Vulnerable.class, 100);
                    Buff.affect(hero, Weakness.class, 100);
                    Buff.affect(hero, Doom.class);


                    hero.interrupt();
                } else if (newLevel >= MIDINFECTION && level < HARDINFECTION) {

                    GLog.n(Messages.get(this, "onmidinfection"));
                    hero.resting = false;
                    hero.damage(1, this);
                    Buff.affect(hero, Vulnerable.class, 20);
                    Buff.affect(hero, Weakness.class, 20);

                    hero.interrupt();


                } else if (newLevel >= LIGHTINFECTION && level < LIGHTINFECTION) {

                    GLog.w(Messages.get(this, "onlightinfection"));

                }
                level = newLevel;


            }spend( target.buff( Shadows.class ) == null ? INFECTION : INFECTION * 1.5f );

        } else {

            diactivate();

        }

        return true;
    }
    public void reduceInfection(float clean){

        level -= clean;
        if (level < 0) {
            level = 0;
        } else if (level > HARDINFECTION){
            float excess = level - HARDINFECTION;
            level = HARDINFECTION;
            partialDamage += excess * (target.HT/1000f);
        }

        BuffIndicator.refreshHero();
    }
    public void cureinfc( float clean) {

        reduceInfection( clean);
    }
    public boolean isMIDINFECTION() {
        return level >= MIDINFECTION;
    }

    public boolean isHARDINFECTION() {
        return level >= HARDINFECTION;
    }

    public int hunger() {
        return (int)Math.ceil(level);
    }

    @Override
    public int icon() {
        if (level < LIGHTINFECTION) {
            return BuffIndicator.NONE;
        } else {
            return BuffIndicator.TERROR;
        }
    }

    @Override
    public String toString() {
        if (level < MIDINFECTION) {
            return Messages.get(this, "lightinfection");
        } else if (level < HARDINFECTION) {
            return Messages.get(this, "midinfection");
        } else {
            return Messages.get(this, "hardinfection");
        }

    }

    @Override
    public String desc() {
        String result;
        if (level < MIDINFECTION) {
            result = Messages.get(this, "desc_intro_lightinfection");
        } else if (level < HARDINFECTION) {
            result = Messages.get(this, "desc_intro_midinfection");
        } else {
            result = Messages.get(this, "desc_intro_hardinfection");
        }

        result += Messages.get(this, "desc");

        return result;
    }

    @Override
    public void onDeath() {
        Dungeon.fail( getClass() );
        GLog.n( Messages.get(this, "ondeath") );
    }
}
