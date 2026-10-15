package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;

public class Minion extends Wraith {
    {
        resistances.add(Wand.class);
        properties.add(Property.DEMONIC);
        alignment = Alignment.ENEMY;
    }

    public Minion() {
        super();
        adjustStats(30);
        defenseSkill = 0;
        HP = HT = 80;
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        Buff.prolong( enemy, Blindness.class, 5 );
        return super.attackProc(enemy, damage);
    }
}