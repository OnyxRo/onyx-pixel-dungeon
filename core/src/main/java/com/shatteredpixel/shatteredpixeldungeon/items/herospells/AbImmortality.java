package com.shatteredpixel.shatteredpixeldungeon.items.herospells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AnkhInvulnerability;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SacrificialParticle;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class AbImmortality extends HeroSpell {
    {
        image = ItemSpriteSheet.HEROSPELL_IMMORTALITY;
    }

    @Override
    public void cast() {
        super.cast();
        Buff.affect(Dungeon.hero, AnkhInvulnerability.class, 10);
        CellEmitter.get(Dungeon.hero.pos).burst(SacrificialParticle.FACTORY, 3);
        Sample.INSTANCE.play(Assets.Sounds.EVOKE);
    }

    @Override
    protected int castCooldown() {
        return 200;
    }
}
