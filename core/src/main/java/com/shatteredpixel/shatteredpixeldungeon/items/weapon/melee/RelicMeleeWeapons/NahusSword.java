package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons;

import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons.RelicEnchantments.Drawing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons.RelicEnchantments.RelicEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class NahusSword extends RelicMeleeWeapon {
    {
        image = ItemSpriteSheet.NAHUSSWORD;
        chargeToAdd = 1f;
    }

    @Override
    public RelicEnchantment enchantment() {
        return new Drawing();
    }
}
