package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class BoneMachete extends MeleeWeapon {

    {
        image = ItemSpriteSheet.BONE_MACHETE;
        hitSound = Assets.Sounds.BONES;
        hitSoundPitch = 1.2f;
        tier = 3;
        rarity = 2;

        critChance = 0.3f;
        critMult = 2f;

    }

    @Override
    public int max(int lvl) {
        return  Math.round(11*damageModifier() +    //12 base
                5*lvl*damageModifier());

    }
    @Override
    public int min(int lvl) {
        return  Math.round(3*damageModifier() +    //8 base
                3*lvl*damageModifier());
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        Buff.affect(defender, Bleeding.class).set(Dungeon.depth/2);
        return super.proc(attacker, defender, damage);
    }

    @Override
    protected int baseChargeUse(Hero hero, Char target) {
        if (hero.buff(Sword.CleaveTracker.class) != null){
            return 0;
        } else {
            return super.baseChargeUse(hero, target);
        }
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        Sword.cleaveAbility(hero, target, 1.27f, 0, this);
    }
}