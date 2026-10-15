package com.shatteredpixel.shatteredpixeldungeon.items.allies;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.VampiricDragonSprite;
import com.watabou.utils.Random;

public class VampiricDragon extends DragonCrystal {
    public Dragon dragon = new Dragon();
    {
        image = ItemSpriteSheet.LIGHTGREENDRAGONCRYSTAL;
    }

    @Override
    public DragonCrystal.Dragon GetDragonTypeToSpawn() {
        return new VampiricDragon.Dragon();
    }

    public static class Dragon extends DragonCrystal.Dragon {
        {
            spriteClass = VampiricDragonSprite.class;
            immunities.add(Grim.class);//immune to Grim
            PassiveRegen = false;
        }

        @Override
        public Class CrystalType() {
            return VampiricDragon.class;
        }

        public int attackProc(Char enemy, int damage ) {
            //chance to heal scales from 25%-75% based on missing HP
            float missingPercent = (HT - HP) / (float) HT;
            float healChance = 0.25f + .50f * missingPercent;

            if (Random.Float() < healChance) {
                int healAmt = Math.round(damage * 0.70f);//Heals 70% of dmg on proc
                healAmt = Math.min(healAmt, HT - HP);

                if (healAmt > 0 && isAlive()) {

                    HP += healAmt;
                    sprite.emitter().start(Speck.factory(Speck.HEALING), 0.4f, 1);
                    sprite.showStatus(CharSprite.POSITIVE, Integer.toString(healAmt));

                }
            }
            return  super.attackProc(enemy, damage);

        }

        @Override
        public int damageRoll() {
            return (int)(super.damageRoll()*0.75);//25% reduced damage (35% was too much imo)
        }
    }

}

