package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GuardianSprite;
import com.watabou.utils.Random;

public class EarthenGuardian extends Mob {
    {
        spriteClass = GuardianSprite.class;

        HP = HT = 180;
        defenseSkill = 24;
        viewDistance = Light.DISTANCE;

        EXP = 14;
        maxLvl = 26;

        baseSpeed = 0.8f;

        loot = new PotionOfHealing();
        lootChance = 0.2f;

        properties.add(Property.DEMONIC);

        resistances.add(Wand.class);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 60, 120);
    }

    @Override
    public int attackSkill( Char target ) {
        return 36;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 16);
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        Ballistica attack = new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE);
        return !Dungeon.level.adjacent( pos, enemy.pos ) && attack.collisionPos == enemy.pos;
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
        return proc(enemy,damage);
    }

    @Override
    public int defenseProc(Char enemy, int damage) {
        damage = super.defenseProc(enemy,damage);
        return proc(enemy,damage);
    }

    private int proc(Char enemy, int damage) {
        if (Random.Int( 2 ) == 0) {
            Buff.prolong( enemy, Slow.class, Slow.DURATION );
        } else if (Random.Int(5) == 0) {
            Buff.affect( enemy, Bleeding.class ).set( damage / 3 );
        }

        return damage;
    }

    @Override
    protected boolean getCloser( int target ) {
        if (state == HUNTING) {
            return enemySeen && getFurther( target );
        } else {
            return super.getCloser( target );
        }
    }

    @Override
    public Item createLoot() {
        //50% chance of getting healing, otherwise mystery meat
        if (Random.Float() < 0.5f) {
            return (Item)loot;
        } else {
            return new MysteryMeat();
        }
    }

}

