package com.shatteredpixel.shatteredpixeldungeon.items.herospells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Skeleton;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class AbNecromancy extends HeroSpell {
    {
        image = ItemSpriteSheet.HEROSPELL_NECROMANCY;
    }

    @Override
    public void cast() {
        super.cast();
        Sample.INSTANCE.play(Assets.Sounds.CURSED);
        for ( int i : PathFinder.NEIGHBOURS4){
            int cell = Dungeon.hero.pos + i;
            if (Dungeon.level.passable[cell]){
                Char ch = Char.findChar(cell);
                Skeleton skeletonbud = new Skeleton();
                skeletonbud.pos = cell;
                skeletonbud.alignment = Char.Alignment.ALLY;
                GameScene.add(skeletonbud);
                Dungeon.level.occupyCell(skeletonbud);
                CellEmitter.get(cell).burst(ShadowParticle.UP, 5);
                Sample.INSTANCE.play(Assets.Sounds.CURSED);
                if (ch != null) {
                    skeletonbud.die(Dungeon.hero);
                    Buff.affect(ch, Vertigo.class, 2);
                }
            }

        }

        Dungeon.hero.spendAndNext(1f);
    }

    @Override
    protected int castCooldown() {
        if (Dungeon.hero == null) return 100;
        return Math.max(Math.max(55 - Dungeon.hero.lvl, 250 - Dungeon.hero.lvl*15), 30);
    }
}
