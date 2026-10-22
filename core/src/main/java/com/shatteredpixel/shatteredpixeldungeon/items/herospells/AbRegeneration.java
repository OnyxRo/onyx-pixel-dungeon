package com.shatteredpixel.shatteredpixeldungeon.items.herospells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.custom.CPHeal;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class AbRegeneration extends HeroSpellTargeted {
    {
        image = ItemSpriteSheet.HEROSPELL_REGENERATION;

        cellCaster = new CellSelector.Listener() {
            @Override
            public void onSelect(Integer cell) {
                if (cell!=null){
                if (Char.findChar(cell)!=null){
                    Char ch = Char.findChar(cell);
                    Buff.affect(ch, Healing.class).setHeal(20,0,1);
                    CellEmitter.get(cell).burst(CPHeal.UP, 10);
                    Sample.INSTANCE.play(Assets.Sounds.READ);
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
    protected int castCooldown() {
        return 50;
    }
}
