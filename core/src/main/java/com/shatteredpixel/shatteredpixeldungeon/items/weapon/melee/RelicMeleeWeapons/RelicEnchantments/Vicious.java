package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons.RelicEnchantments;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons.RelicMeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;

public class Vicious extends RelicEnchantment {
    private static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x000000 );
    private final float DURATION = 50f;

    @Override
    public int relicProc(RelicMeleeWeapon weapon, Char attacker, Char defender, int damage) {
        Buff.affect( defender, Poison.class ).set( 3 + Dungeon.depth / 2 );
        Buff.affect( defender, Bleeding.class ).set( damage );
        Buff.affect( defender, Cripple.class, Cripple.DURATION );
        return damage;
    }

    @Override
    public void activate(RelicMeleeWeapon weapon, Char owner) {
        super.activate(weapon, owner);
        Buff.affect( owner, MagicImmune.class, DURATION );
        Buff.affect( owner, BlobImmunity.class, DURATION );
        Buff.affect( owner, Barrier.class ).setShield(owner.HT);
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return BLACK;
    }
}
