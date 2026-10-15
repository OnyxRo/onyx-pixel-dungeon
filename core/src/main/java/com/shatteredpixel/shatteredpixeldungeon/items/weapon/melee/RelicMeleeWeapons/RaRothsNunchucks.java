package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons;

import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons.RelicEnchantments.Ethereal;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons.RelicEnchantments.RelicEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class RaRothsNunchucks extends RelicMeleeWeapon {
    {
        image = ItemSpriteSheet.RA_ROTHS_NUNCHUCKS;
        ACC = 0.7f;
        RCH = 3;
        DLY = 0.5f;
        damageMultiplier = 0.5f;
    }

    @Override
    public RelicEnchantment enchantment() {
        return new Ethereal();
    }
}
