package com.shatteredpixel.shatteredpixeldungeon.items.allies;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PoisonDragonSprite;

public class PoisonDragon extends DragonCrystal {
    public Dragon dragon = new Dragon();
    {
        image  = ItemSpriteSheet.ADORNEDDRAGONCRYSTAL;
    }

    @Override
    public DragonCrystal.Dragon GetDragonTypeToSpawn() {
        return new Dragon();
    }

    public static class Dragon extends DragonCrystal.Dragon {
        {
            spriteClass = PoisonDragonSprite.class;
            properties.add(Property.ACIDIC);
            immunities.add(Poison.class);//immune to poison
        }

        public int attackProc(Char enemy, int damage ) {
            damage = (int)(super.attackProc( enemy, damage )*0.75f);
            Buff.affect( enemy, Poison.class ).set( 2 + Dungeon.depth / 6 );

            return damage;

        }

        @Override
        public Class CrystalType() {
            return PoisonDragon.class;
        }

        @Override
        public int defenseProc( Char enemy, int damage ) {
            enemy.damage(Math.round(damage/2),this);//damages enemies who attack
            return super.defenseProc(enemy, damage);
        }

        @Override
        public int drRoll() {
            return (int)(super.drRoll()*0.75);
        }
    }

}
