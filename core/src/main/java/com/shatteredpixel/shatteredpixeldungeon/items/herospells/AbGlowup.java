package com.shatteredpixel.shatteredpixeldungeon.items.herospells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Battlecry;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.Tower;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.custom.CPRed;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class AbGlowup extends HeroSpell {
    {
        image = ItemSpriteSheet.HEROSPELL_GLOWUP;
    }

    @Override
    public void cast() {
        super.cast();
        for (int i : PathFinder.NEIGHBOURS25) {
            Char ch = Char.findChar(Dungeon.hero.pos + i);
            if (ch != null &&ch != Dungeon.hero && ch.alignment== Char.Alignment.ALLY) {
                Buff.affect(ch, Battlecry.class, ch instanceof Tower ? Math.max(15, 70*(200/((Tower) ch).cost)) : 20);
                CellEmitter.floor(ch.pos).start(CPRed.UP, 0.05f, 10);
            }
        }
        Sample.INSTANCE.play(Assets.Sounds.CHALLENGE);
    }

    @Override
    protected int castCooldown() {
        return 100;
    }
}
