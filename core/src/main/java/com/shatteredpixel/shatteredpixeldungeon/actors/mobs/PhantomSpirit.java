package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MaxGuard;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrimTrap;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MirrorSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WraithSprite;


public class PhantomSpirit extends Mob {

    {
        spriteClass = WraithSprite.class;

        HP = HT = 1;
        defenseSkill = 1;

        EXP = 25;
        maxLvl = -2;

        immunities.add(Corrosion.class);
        properties.add(Property.DEMONIC);
        flying = true;
        WANDERING = new Wandering();
        HUNTING = new Hunting();
        Buff.affect(this, MaxGuard .class);
    }

    @Override
    protected boolean doAttack(Char enemy) {
        GrimTrap kill = new GrimTrap();
        kill.pos = enemy.pos;
        kill.activate();
        return super.doAttack(enemy);
    }

    @Override
    public int attackSkill(Char target) {
        return 999999;
    }

}
