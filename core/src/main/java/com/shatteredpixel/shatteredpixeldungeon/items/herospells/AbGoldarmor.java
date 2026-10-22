package com.shatteredpixel.shatteredpixeldungeon.items.herospells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.GoldArmor;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class AbGoldarmor extends HeroSpell {
    {
        image = ItemSpriteSheet.HEROSPELL_GOLDARMOR;
    }

    @Override
    public void cast() {
        super.cast();
        Sample.INSTANCE.play(Assets.Sounds.GOLD, 1f, 0.5f);
        CellEmitter.get(Dungeon.hero.pos).burst(ElmoParticle.FACTORY, 10);
        Buff.affect(Dungeon.hero, GoldArmor.class).set(10);
    }

    @Override
    protected int castCooldown() {
        return 200;
    }
}
