package com.shatteredpixel.shatteredpixeldungeon.items.herospells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.custom.CPShield;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class AbShield extends HeroSpell {
    {
        image = ItemSpriteSheet.HEROSPELL_SHIELD;
    }

    @Override
    public void cast() {
        super.cast();
        for (int i : PathFinder.NEIGHBOURS9) {
            Char ch = Char.findChar(Dungeon.hero.pos + i);
            if (ch != null && ch.alignment== Char.Alignment.ALLY && !(ch instanceof Arena.AmuletTower)) {
                Buff.affect(ch, Barrier.class).setShield(10+Dungeon.hero.lvl*4);
                CellEmitter.floor(ch.pos).start(CPShield.TOCENTER, 0.01f, 10);
            }
        }
        Sample.INSTANCE.play(Assets.Sounds.MASTERY);
    }

    @Override
    protected int castCooldown() {
        return 80;
    }
}
