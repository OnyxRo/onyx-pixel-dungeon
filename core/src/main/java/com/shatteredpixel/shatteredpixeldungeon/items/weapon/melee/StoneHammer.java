package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class StoneHammer extends MeleeWeapon {

    {
        image = ItemSpriteSheet.STONE_HAMMER;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.6f;
        rarity = 2;
        critMult = 1.5f;
        tier = 3;
        ACC = 0.7f; //-30% to accuracy
        DLY = 1.2f; //-20% to speed
    }

    @Override
    public int min(int lvl) {
        return  Math.round(3*damageModifier() +    //15 base, down from 30
                2*lvl*damageModifier());   //4/lvl
    }
    @Override
    public int max(int lvl) {
        return  Math.round(20*damageModifier() +    //24 base, down from 30
                6*lvl*damageModifier());   //8/lvl
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        Mace.heavyBlowAbility(hero, target, 1.50f, 0, this);
    }

}
