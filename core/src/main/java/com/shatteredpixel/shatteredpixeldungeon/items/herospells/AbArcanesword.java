package com.shatteredpixel.shatteredpixeldungeon.items.herospells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Elemental;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.RainbowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Corrupting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Pure;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Vampiric;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class AbArcanesword extends HeroSpell {
    {
        image = ItemSpriteSheet.HEROSPELL_ARCANESWORD;
    }

    @Override
    public void cast() {
        super.cast();

        Weapon wep = (Weapon) Dungeon.hero.belongings.weapon;
        if(wep!=null) {
            wep.enchant(Weapon.Enchantment.random(Corrupting.class, Grim.class, Vampiric.class, Pure.class));
        } else{
           Weapon newwep = (Weapon)Generator.random(Generator.Category.WEP_T2);
           if (newwep.enchantment==null) newwep.enchant(Weapon.Enchantment.random(Corrupting.class, Grim.class, Vampiric.class, Pure.class));
           Dungeon.level.drop(newwep, Dungeon.hero.pos);
        }
        CellEmitter.get(Dungeon.hero.pos).start(RainbowParticle.BURST, 0.2f, 10);
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
    }

    @Override
    protected int castCooldown() {
        return 100;
    }
}
