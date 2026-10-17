package com.shatteredpixel.shatteredpixeldungeon.items.herospells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerCWall;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerCrossbow1;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerDisintegration1;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerGrave1;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerGraveCrypt;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Fireball;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfSkulls;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfDemonicSkull;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Predicate;

public class AbTrAngerTheDead extends HeroSpell{

    {
        image = ItemSpriteSheet.HEROSPELL_TR_ANGERTHEDEAD;
    }
    private static final int TURNS_ADDED_PER_GRAVE = 30;


    @Override
    public void cast() {
        super.cast();
        ArrayList<Integer> friconcurrent = new ArrayList<>();
        Sample.INSTANCE.play(Assets.Sounds.DEATH);
        Sample.INSTANCE.play(Assets.Sounds.BONES);
        for (Mob mob : com.shatteredpixel.shatteredpixeldungeon.Dungeon.level.mobs){
            if ((mob instanceof TowerGrave1 || mob instanceof TowerGraveCrypt) && mob.alignment == Char.Alignment.ALLY){
                ArrayList<Integer> emptycells = new ArrayList<>();
                for (int i : PathFinder.NEIGHBOURS8) {
                    int cell = i + mob.pos;
                    if (Dungeon.level.passable[cell] && Char.findChar(cell)==null){
                        emptycells.add(cell);
                    }
                }
                if (!emptycells.isEmpty()) friconcurrent.add(Random.element(emptycells));
            }
        }
        for (int cell : friconcurrent){
            ScrollOfSkulls.ExplosiveSkull skully = new ScrollOfSkulls.ExplosiveSkull();
            skully.pos = cell;
            skully.state = skully.HUNTING;
            CellEmitter.center(cell).burst(FlameParticle.FACTORY,10);
            GameScene.add(skully);
        }
    }

    @Override
    public String info() {
        return desc() + "\n\n" + Messages.get(this, "cost", castCooldown(), TURNS_ADDED_PER_GRAVE);
    }

    @Override
    protected int castCooldown() {
        int addturns = 0;
        HashSet<Mob> mobs = new HashSet<>(com.shatteredpixel.shatteredpixeldungeon.Dungeon.level.mobs);
        for (Mob mob : mobs) if (mob!= null && mob.isAlive() && mob.alignment != null) {
            if (mob.alignment == Char.Alignment.ALLY) {
                if (mob instanceof TowerGrave1 || mob instanceof TowerGraveCrypt)
                    addturns += TURNS_ADDED_PER_GRAVE;
            }
        }

        return 100 + addturns;
    }
}
