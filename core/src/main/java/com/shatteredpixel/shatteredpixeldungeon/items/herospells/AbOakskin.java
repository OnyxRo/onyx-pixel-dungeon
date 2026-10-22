package com.shatteredpixel.shatteredpixeldungeon.items.herospells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Foliage;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class AbOakskin extends HeroSpellTargeted {
    {
        image = ItemSpriteSheet.HEROSPELL_OAKSKIN;

        cellCaster = new CellSelector.Listener() {
            @Override
            public void onSelect(Integer cell) {
                if (cell!=null) {
                    if (Char.findChar(cell) != null && Char.findChar(cell)!= Dungeon.hero) {
                        Char ch = Char.findChar(cell);
                        Buff.affect(ch, Barkskin.class).set((int)(Dungeon.hero.lvl/1.5) + 6, 25 - Math.min(Dungeon.hero.lvl, 16));
                        Sample.INSTANCE.play(Assets.Sounds.GRASS);
                        CellEmitter.get(cell).burst(LeafParticle.GENERAL, 10);
                        cooldown();
                        Dungeon.hero.spendAndNext(1f);
                    }
                }
            }
            @Override
            public String prompt() {
                return Messages.get(HeroSpellTargeted.class, "cell_choose");
            }
        };
    }


    @Override
    public String info() {
        return Messages.get(this, "desc", Dungeon.depth+4);
    }

    @Override
    protected int castCooldown() {
        if (Dungeon.hero==null) return 80;
        return 40 + Dungeon.hero.lvl*5;
    }
}
