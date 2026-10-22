package com.shatteredpixel.shatteredpixeldungeon.items.herospells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Rush;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class AbSwift extends HeroSpell {
    {
        image = ItemSpriteSheet.HEROSPELL_SWIFT;
    }

    @Override
    public void cast() {
        super.cast();
        Buff.affect(Dungeon.hero, Rush.class, 3);
        Sample.INSTANCE.play(Assets.Sounds.TRAMPLE);
        CellEmitter.get(Dungeon.hero.pos).burst(SparkParticle.FACTORY, 10);
    }

    @Override
    protected int castCooldown() {
        return 50;
    }
}
