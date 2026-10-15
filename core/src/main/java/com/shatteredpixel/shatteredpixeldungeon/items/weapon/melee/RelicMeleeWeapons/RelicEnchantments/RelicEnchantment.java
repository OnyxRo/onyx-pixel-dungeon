package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons.RelicEnchantments;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons.RelicMeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;


public abstract class RelicEnchantment extends Weapon.Enchantment {
    @Override
    public int proc( Weapon weapon, Char attacker, Char defender, int damage) {
        if (weapon instanceof RelicMeleeWeapon) {
            return relicProc((RelicMeleeWeapon)weapon,attacker,defender,damage);
        } else {
            GLog.w("Inconceivable!");
            return damage;
        }
    }

    public abstract int relicProc(RelicMeleeWeapon weapon, Char attacker, Char defender, int damage);

    public void activate(RelicMeleeWeapon weapon, Char owner) {
        weapon.use();
        GLog.p(Messages.get(this,"activated"));
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return null;
    }
}
