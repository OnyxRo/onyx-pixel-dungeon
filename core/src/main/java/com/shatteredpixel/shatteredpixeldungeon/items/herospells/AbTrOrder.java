package com.shatteredpixel.shatteredpixeldungeon.items.herospells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.Tower;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerGuard1;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerLightning1;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PointF;

public class AbTrOrder extends HeroSpellTargeted {

    {
        image = ItemSpriteSheet.HEROSPELL_TR_ORDER;

        cellCaster = new CellSelector.Listener() {
            @Override
            public void onSelect(Integer cell) {
                if (cell!=null){
                    for (Mob mob : com.shatteredpixel.shatteredpixeldungeon.Dungeon.level.mobs){
                        if (mob instanceof TowerGuard1 && mob.alignment == Char.Alignment.ALLY){
                            ((TowerGuard1)mob).defendPos(cell);
                        }
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
        return 0;
    }

}
