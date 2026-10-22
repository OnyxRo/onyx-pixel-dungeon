package com.shatteredpixel.shatteredpixeldungeon.items.herospells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAggression;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class AbTreatwounds extends HeroSpell {
    {
        image = ItemSpriteSheet.HEROSPELL_TREATWOUNDS;
    }

    @Override
    public void cast() {
        super.cast();

        Dungeon.hero.heal(
                Dungeon.hero.HT/10 + // partial 10 percent heal
                        5 +                 // flat 5 HP
                        Dungeon.hero.lvl +  //1 HP per level of hero
                        (int)((Dungeon.hero.HT - Dungeon.hero.HP)*0.15f) // the higher is the HP lost, the better is the heal, up to 15 percent of your hp
        );

        Dungeon.hero.sprite.operate(Dungeon.hero.pos);
        Sample.INSTANCE.play(Assets.Sounds.GRASS);
    }

    @Override
    protected int castCooldown() {
        return 50;
    }
}