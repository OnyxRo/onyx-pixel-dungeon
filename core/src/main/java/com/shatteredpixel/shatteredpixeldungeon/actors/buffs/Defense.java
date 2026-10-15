package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Defense extends Buff {

    public int defensivePower = 0;

    private static String DEFENSIVE_POWER = "defensive_power";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(DEFENSIVE_POWER, defensivePower);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        defensivePower = bundle.getInt(DEFENSIVE_POWER);
    }

    public Defense setDefensivePower(Hero hero) {
        KindOfWeapon wep = hero.belongings.weapon;
        if (wep != null) {
            defensivePower = wep.defenseFactor(hero)*2;
        }
        //Cursed PD added a boost here from the weapon's attack type, this game has no attack types
        defensivePower += hero.damageRoll()/2;
        return this;
    }

    public boolean proc(Char defender, final Char attacker, int damage) {
        hit();
        if (Random.Int(defensivePower) >= Random.Int(damage)) {
            defender.attack(attacker, 0.5f, 0f, 1f);
            defender.sprite.attack(attacker.pos, new Callback() {
                @Override
                public void call() {
                    attacker.sprite.showStatus(CharSprite.POSITIVE, Messages.get(Char.class, "free_attack"));
                }
            });
            defender.sprite.showStatus(CharSprite.NEUTRAL, Messages.get(Char.class, "blocked"));
            return true;
        }
        return false;
    }

    public void hit() {
        defensivePower *= 0.67;
    }

    @Override
    public boolean act() {
        detach();
        return true;
    }

    @Override
    public int icon() {
        return BuffIndicator.ARMOR;
    }
}
