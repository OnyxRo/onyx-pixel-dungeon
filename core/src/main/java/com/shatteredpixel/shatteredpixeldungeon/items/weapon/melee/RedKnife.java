package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class RedKnife extends MeleeWeapon {

    {
        image = ItemSpriteSheet.RED_KNIFE;
        hitSound = Assets.Sounds.HIT_STAB;
        hitSoundPitch = 1f;
        ACC= 1.2f;
        rarity = 3;
        critMult = 1.5f;
        critChance=0.03f;
        tier = 3;
    }

    public int proc(Char attacker, Char defender, int damage) {
        int i = Random.Int(5);
        if (i == 2 && level()>0) {Buff.affect( defender, Burning.class);}//rarely sets enemies on fire
        return super.proc(attacker, defender, damage);
    }
    @Override
    public int min(int lvl) {
        return Math.round(8 * damageModifier() +    //6 base
                3*lvl * damageModifier());   //scaling unchanged
    }
    @Override
    public int max(int lvl) {
        return Math.round(10 * damageModifier() +    //9 base
                4*lvl * damageModifier());   //scaling unchanged
    }

    @Override
    public int damageRoll(Char owner) {
        if (owner instanceof Hero) {
            Hero hero = (Hero) owner;
            Char enemy = hero.enemy();
            if (enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)) {
                //deals 67% toward max to max on surprise, instead of min to max.
                int diff = max() - min();
                int damage = augment.damageFactor(Random.NormalIntRange(
                        min() + Math.round(diff * 0.67f),
                        max()));
                int exStr = hero.STR() - STRReq();
                if (exStr > 0) {
                    damage += Random.IntRange(0, exStr);
                }
                return damage;
            }
        }
        return super.damageRoll(owner);
    }

    @Override
    protected int baseChargeUse(Hero hero, Char target) {
        return 2 * super.baseChargeUse(hero, target);
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        Dagger.sneakAbility(hero, target, 8, 2, this);
    }
}
