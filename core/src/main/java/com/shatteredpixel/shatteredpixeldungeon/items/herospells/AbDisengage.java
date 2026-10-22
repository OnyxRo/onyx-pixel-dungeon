package com.shatteredpixel.shatteredpixeldungeon.items.herospells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class AbDisengage extends HeroSpellTargeted {
    {
        image = ItemSpriteSheet.HEROSPELL_DISENGAGE;

        cellCaster = new CellSelector.Listener() {
            @Override
            public void onSelect(Integer cell) {
                if (cell!=null) {
                    boolean cheatingAttempt = false;
                    for (int i : PathFinder.NEIGHBOURS25) {
                        if (Char.findChar(cell + i) != null && Char.findChar(cell + i).buff(Paralysis.class) != null && Char.findChar(cell + i).buff(Invisibility.class) != null) {
                            cheatingAttempt = true;
                        }
                    }

                    Sample.INSTANCE.play(Assets.Sounds.PUFF);
                    if (Char.findChar(cell) != null && !(Char.findChar(cell)instanceof Arena.AmuletTower) && !(Char.findChar(cell).properties().contains(Char.Property.BOSS))) {
                        Char ch = Char.findChar(cell);
                        Buff.affect(ch, Invisibility.class, cheatingAttempt ? 2 : ch.alignment == Char.Alignment.ALLY ? 10 : 5);
                        Buff.affect(ch, Paralysis.class, cheatingAttempt ? 2 : ch.alignment == Char.Alignment.ALLY ? 10 : 5);
                    }
                    cooldown();
                }
            }
            @Override
            public String prompt() {
                return Messages.get(HeroSpellTargeted.class, "cell_choose");
            }
        };
    }



    @Override
    protected int castCooldown() {
        return 40;
    }
}
