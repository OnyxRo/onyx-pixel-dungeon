package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MinerSprite;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class TowerMiner extends TowerNotliving {
    {
        spriteClass = MinerSprite.class;
        sellable = false;
        HT = HP = 200;
        state = PASSIVE;
        alignment = Alignment.ALLY;
        upgCount = 0;
        properties.add(Property.IMMOVABLE);
    }

    public void dropGold(int gold){
        int gpos = pos + PathFinder.NEIGHBOURS8[Random.NormalIntRange(0, PathFinder.NEIGHBOURS8.length-1)];
        Dungeon.level.drop(new Gold(gold), gpos);
    }

    @Override
    protected boolean getCloser(int target) {
        return true;
    }

    @Override
    protected boolean getFurther(int target) {
        return true;
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return false;
    }
}
