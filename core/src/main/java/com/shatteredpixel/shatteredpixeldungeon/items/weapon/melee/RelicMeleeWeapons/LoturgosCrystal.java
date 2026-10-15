package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons;

import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons.RelicEnchantments.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons.RelicEnchantments.RelicEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class LoturgosCrystal extends RelicMeleeWeapon {
    {
        image = ItemSpriteSheet.LOTURGOS_CRYSTAL;
        chargeToAdd = 1f;
        usesTargeting = true;
    }

    @Override
    public RelicEnchantment enchantment() {
        return new Barrier();
    }
}
