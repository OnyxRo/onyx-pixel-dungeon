package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons.RelicEnchantments.Bloodlust;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons.RelicEnchantments.RelicEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class ChainsawHand extends RelicMeleeWeapon {
    {
        image = ItemSpriteSheet.CHAINSAW_HAND;
        ACC = 1.5f;
        damageMultiplier = 0.3f;
        cursed = true;
    }

    private final String AC_DEACTIVATE = "deactivate";

    private static final String ACTIVATED = "activated";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( ACTIVATED , turnedOn );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        turnedOn = bundle.getBoolean( ACTIVATED );
    }

    private boolean turnedOn = false;

    public boolean isTurnedOn() {
        return turnedOn;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (turnedOn) {
            actions.remove(AC_ACTIVATE);
            actions.add(AC_DEACTIVATE);
        }
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_DEACTIVATE)) {
            specialAction(hero);
        }
    }

    @Override
    public RelicEnchantment enchantment() {
        return new Bloodlust();
    }

    @Override
    public Weapon enchant(Enchantment ench) {
        cursed = true;//Since it has a curse enchantment, cleansing it will always remove it by enchant(null). This code, along with the regular RelicMeleeWeapon code, ensures that it stays cursed just like the Sprouted version.
        return super.enchant(ench);
    }

    @Override
    public void specialAction(Hero hero) {
        hero.spendAndNext(1f);
        turnedOn = !turnedOn;
        if (turnedOn) {
            GLog.p(Messages.get(Bloodlust.class,"activated"));
            defaultAction = AC_DEACTIVATE;
        } else {
            GLog.n(Messages.get(Bloodlust.class,"deactivated"));
            defaultAction = AC_ACTIVATE;
        }
    }

    @Override
    public void activate(Char ch) {
        super.activate(ch);
        if (turnedOn) {
            defaultAction = AC_DEACTIVATE;
        } else {
            defaultAction = AC_ACTIVATE;
        }
    }
}
