package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class GoldenSword extends MeleeWeapon {

    {
        image = ItemSpriteSheet.GOLDEN_SWORD;
        hitSound = Assets.Sounds.HIT_SLASH;
        hitSoundPitch = 1f;
        tier = 3;
        rarity = 5;
    }

    @Override
    public int min(int lvl) {
        return  Math.round(4*damageModifier() +
                lvl*damageModifier());
    }

    @Override
    public int max(int lvl) {
        return  Math.round(7*damageModifier() +
                3*lvl*damageModifier());
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        Dungeon.level.drop(new Gold(damage), attacker.pos);
        CellEmitter.center(defender.pos).burst(Speck.factory(Speck.COIN), 5);
        Sample.INSTANCE.play(Assets.Sounds.GOLD);
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