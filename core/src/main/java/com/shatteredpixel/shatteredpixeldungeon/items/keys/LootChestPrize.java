package com.shatteredpixel.shatteredpixeldungeon.items.keys;

import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.watabou.utils.Random;

//Deep PD, prizes for its loot chests. Deep had its own weapon categories per chest tier, this game uses floor sets.
public class LootChestPrize {

    public static Item Tier1Prize() {
        return Random.Int(2) == 0 ? Generator.randomWeapon(1) : Generator.randomArmor(1);
    }

    public static Item Tier2Prize() {
        return Random.Int(2) == 0 ? Generator.randomWeapon(3) : Generator.randomArmor(3);
    }

    public static Item Tier3Prize() {
        return Generator.randomWeapon(4);
    }
}
