package com.shatteredpixel.shatteredpixeldungeon.items.herospells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.Banner;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.IceWall;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class AbBanner extends HeroSpellTargeted{
    {
        image = ItemSpriteSheet.HEROSPELL_BANNER;

        cellCaster = new CellSelector.Listener() {
            @Override
            public void onSelect(Integer cell) {
                if (cell != null) {
                    if (Char.findChar(cell) == null && Dungeon.level.passable[cell] && !Dungeon.level.pit[cell] && Dungeon.hero.fieldOfView[cell]) {
                        Banner banner = new Banner();
                        banner.HP = banner.HT = 10 + Dungeon.hero.lvl;
                        banner.alignment = Char.Alignment.ALLY;
                        banner.pos = cell;
                        GameScene.add(banner);
                        Sample.INSTANCE.play(Assets.Sounds.MASTERY);
                        CellEmitter.floor(cell).burst(MagicMissile.MagicParticle.ATTRACTING, 20);
                        cooldown();
                    }
                }
            }

            @Override
            public String prompt () {
                return Messages.get(HeroSpellTargeted.class, "cell_choose");
            }
        };
    }



    @Override
    protected int castCooldown() {
        return 100;
    }
}
