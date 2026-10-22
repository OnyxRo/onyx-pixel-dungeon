package com.shatteredpixel.shatteredpixeldungeon.items.herospells;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AbilityCooldown;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PriorityTarget;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class AbTeleport extends HeroSpellTargeted {

    private static final int DISTANCE_COOLDOWN = 6;

    {
        image = ItemSpriteSheet.HEROSPELL_TELEPORTATION;

        cellCaster = new CellSelector.Listener() {
            @Override
            public void onSelect(Integer cell) {
                if (cell != null) {
                    if (Char.findChar(cell) == null && Dungeon.level.passable[cell] && !Dungeon.level.pit[cell] && (Dungeon.level.visited[cell] || Dungeon.level.mapped[cell])) {
                        Buff.affect(Dungeon.hero, AbilityCooldown.class, castCooldown() + DISTANCE_COOLDOWN*Dungeon.level.distance(Dungeon.hero.pos, cell));//cooldown depends on the distance
                        Dungeon.hero.pos = cell;
                        ScrollOfTeleportation.appear(Dungeon.hero, cell);
                        Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
                        Dungeon.level.occupyCell(hero);
                        Dungeon.observe();
                        GameScene.updateFog();
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
    public String info() {
        return desc() + "\n\n" + Messages.get(this, "cost", castCooldown(), DISTANCE_COOLDOWN);
    }



    @Override
    protected int castCooldown() {
        return 6;
    }
}
