package com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Swift extends Weapon.Enchantment {
    private static ItemSprite.Glowing YELLOW = new ItemSprite.Glowing( 0xFFEE00 );

    @Override
    public int proc( Weapon weapon, Char attacker, Char defender, int damage) {
        if (Random.Int(4) == 0) {
            new FlavourBuff(){
                {actPriority = VFX_PRIO;}
                public boolean act() {
                    Buff.affect(target, Swiftthistle.TimeBubble.class).reset(3);
                    return super.act();
                }
            }.attachTo(attacker);
        }
        return damage;
    }

    /*public static boolean activate(KindOfWeapon weapon) {
        return Random.Int(3) == 1;
    }*/

    @Override
    public ItemSprite.Glowing glowing() {
        return YELLOW;
    }
}
