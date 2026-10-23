package com.shatteredpixel.shatteredpixeldungeon.items.food;


import com.shatteredpixel.shatteredpixeldungeon.actors.hero.RBATalents;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.RBAGun;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class RBAHardTack extends Food {

    {
        image = ItemSpriteSheet.RBA_HARD_BISCUIT;
        energy = Hunger.HUNGRY/3f; //100 food value

        bones = false;
    }

    @Override
    protected float eatingTime(){
        if (RBATalents.hasFoodTalent(Dungeon.hero)){
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    protected void satisfy(Hero hero) {
        super.satisfy(hero);
        hero.heal(5);
        if (hero.belongings.weapon() instanceof RBAGun) {
            ((RBAGun) hero.belongings.weapon()).quickReload();
            ((RBAGun) hero.belongings.weapon()).manualReload(4, true);
        }
    }

    @Override
    public int value() {
        return 5 * quantity;
    }

}
