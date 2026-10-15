package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons.RelicEnchantments;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons.RelicMeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;

public class Bloodlust extends RelicEnchantment {

    private static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x000000 );

    @Override
    public boolean curse() {
        return true;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return BLACK;
    }

    @Override
    public int relicProc(RelicMeleeWeapon weapon, Char attacker, final Char defender, final int damage) {

        /*if (attacker instanceof Hero & defender.isAlive()) {
            final Hero hero = (Hero) attacker;
            if (!defender.isAlive()) {
                hero.spendAndNext(hero.attackDelay());
                return damage; //no point in proccing if they're already dead.
            }
            if (weapon.charge > 0 & ((ChainsawHand)weapon).isTurnedOn() & hero.canAttack(defender)) {
                weapon.use(1);
                hero.sprite.attack(defender.pos, new Callback() {
                    @Override
                    public void call() {
                        hero.actuallyAttack(defender);
                    }
                });
            } else {
                GLog.n(Messages.get(Bloodlust.class,"no_charge"));
            }
        } else {
            attacker.next();
            return damage;//TODO: allow proccing from Statues and Ghost
        }
        attacker.next();*/
        return damage;
    }
}
