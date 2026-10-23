package com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.sandbox;


import com.shatteredpixel.shatteredpixeldungeon.actors.hero.RBAHero;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.audio.Sample;

public class RBAFarming extends RBASandboxConsoleContent {
    public static final RBAFarming INSTANCE = new RBAFarming();

    @Override
    public int icon() {
        return HeroIcon.RBA_SANDBOX_FARMING;
    }

    @Override
    public boolean canBuild(int target) {
        return super.canBuild(target)
                && Dungeon.level.passable[target]
                && !Dungeon.level.avoid[target]
                && Dungeon.level.map[target] != Terrain.OPEN_DOOR;
    }

    @Override
    public boolean execute(Hero hero, int target) {
        if (Dungeon.level.distance(hero.pos, target) > 1) {
            RBAHero.yellW(hero, "need_to_be_adjacent");
            return false;
        }
        if (!canBuild(target)) {
            RBAHero.yellW(hero, "cannot_do");
            return false;
        }

        hero.sprite.zap(target);
        if (Dungeon.level.map[target] == Terrain.GRASS) {
            Dungeon.level.map[target] = Terrain.HIGH_GRASS;
        } else if (canBuild(target)) {
            Dungeon.level.map[target] = Terrain.GRASS;
        }
        hero.spendAndNext(1);

        GameScene.updateMap(target);

        Sample.INSTANCE.play(Assets.Sounds.TRAMPLE);
        CellEmitter.get(target).burst(LeafParticle.LEVEL_SPECIFIC, 4);
        if (Dungeon.level.heroFOV[target]) Dungeon.observe();
        return true;
    }
}
