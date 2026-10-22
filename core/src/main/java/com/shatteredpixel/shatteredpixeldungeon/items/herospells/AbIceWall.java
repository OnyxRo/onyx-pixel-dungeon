package com.shatteredpixel.shatteredpixeldungeon.items.herospells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.LicteriaLasher;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.IceWall;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class AbIceWall extends HeroSpellTargeted{
    {
        image = ItemSpriteSheet.HEROSPELL_ICEWALL;

        cellCaster = new CellSelector.Listener() {
            @Override
            public void onSelect(Integer cell) {
                if (cell != null) {
                    if (Char.findChar(cell) == null && Dungeon.level.passable[cell] && !Dungeon.level.pit[cell] && Dungeon.hero.fieldOfView[cell]) {
                        IceWall wall = new IceWall();
                        wall.HP = wall.HT = 20 + (int)(Dungeon.hero.lvl*(Math.sqrt(Math.sqrt(Dungeon.depth)))*8);
                        wall.alignment = Char.Alignment.ALLY;
                        wall.pos = cell;
                        GameScene.add(wall);
                        Sample.INSTANCE.play(Assets.Sounds.SHATTER);
                        CellEmitter.floor(cell).start(MagicMissile.MagicParticle.FACTORY, 0.05f,30);
                        CellEmitter.floor(cell).start(MagicMissile.MagicParticle.FACTORY, 0.1f,10);
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
        return 150;
    }
}
