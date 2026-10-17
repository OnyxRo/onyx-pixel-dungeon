package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Animated;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TowerCrossbow1Sprite;

public class TowerCrossbow1 extends TowerCShooting {
    {
        HP = HT = 40;
        spriteClass = TowerCrossbow1Sprite.class;

        attackRange = 8;//dpt/c=0.0097
        baseAttackDelay = 1.8f;

        cost = 200;
        upgrade1Cost = 250;
        damageMin = 2;
        damageMax = 5;
        upgradeLevel = 3;
    }

    @Override
    public int attackSkill(Char target) {
        return 12;
    }

    @Override
    protected boolean canAttack( Char enemy ) {//does not attack close foes in melee
        if (buff(Animated.class)!=null && (Dungeon.level.distance( pos, Dungeon.hero.pos )>2 || Dungeon.level.adjacent( pos, enemy.pos ))) return false;
        return !Dungeon.level.adjacent( pos, enemy.pos )
                && (new Ballistica( pos, enemy.pos, Ballistica.TARGETING_BOLT).collisionPos == enemy.pos && Dungeon.level.distance(enemy.pos, this.pos)<=attackRange);
    }
}
