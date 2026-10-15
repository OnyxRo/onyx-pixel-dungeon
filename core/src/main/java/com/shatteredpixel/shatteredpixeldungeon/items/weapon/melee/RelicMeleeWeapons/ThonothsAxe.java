package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons;

import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons.RelicEnchantments.RelicEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons.RelicEnchantments.Vicious;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class ThonothsAxe extends RelicMeleeWeapon {
    {
        image = ItemSpriteSheet.THONOTHS_AXE;
        ACC = 2f;
        damageMultiplier = 0.74f;
    }

    @Override
    public RelicEnchantment enchantment() {
        return new Vicious();
    }
}
