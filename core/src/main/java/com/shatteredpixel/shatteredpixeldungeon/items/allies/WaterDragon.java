package com.shatteredpixel.shatteredpixeldungeon.items.allies;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WaterDragonSprite;

public class WaterDragon extends DragonCrystal {

    public Dragon dragon = new Dragon();
    {
        image = ItemSpriteSheet.LIGHTBLUEDRAGONCRYSTAL;
    }

    @Override
    public DragonCrystal.Dragon GetDragonTypeToSpawn() {
        return new WaterDragon.Dragon();
    }

    public static class Dragon extends DragonCrystal.Dragon {
        {
            spriteClass = WaterDragonSprite.class;
            immunities.add(Chill.class);//immune to chill
            baseSpeed = 1.5f;
            PassiveRegen = false;
        }

        @Override
        public Class CrystalType() {
            return WaterDragon.class;
        }

        @Override
        public boolean act() {

            if (Dungeon.level.water[pos] && HP < HT) {
                sprite.emitter().burst( Speck.factory( Speck.STEAM ), 1 );

                HP+= Math.min(HT/10,missingHP());
            }

            return super.act();
        }

        @Override
        public int HPCalc() {
            return 24 + 16 * Crystal.level();
        }

        @Override
        public int defenseSkill(Char enemy) {
            return super.defenseSkill*2;
        }
    }
}
