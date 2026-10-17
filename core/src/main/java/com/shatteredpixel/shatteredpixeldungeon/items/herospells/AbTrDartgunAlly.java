package com.shatteredpixel.shatteredpixeldungeon.items.herospells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Animated;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerDartgun1;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerDisintegration1;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class AbTrDartgunAlly extends HeroSpell{

    {
        image = ItemSpriteSheet.HEROSPELL_TR_SUMMONDARTGUN;
    }

    @Override
    public void cast() {
        super.cast();
        Sample.INSTANCE.play(Assets.Sounds.RAY);
        ArrayList<Integer> veryclosecells = new ArrayList<>();
        ArrayList<Integer> abitclosecells = new ArrayList<>();
        for (int i : PathFinder.NEIGHBOURS8){
            int cell = Dungeon.hero.pos + i;
            if (Dungeon.level.passable[cell] && Char.findChar(cell)==null) veryclosecells.add(cell);
        }
        for (int i : PathFinder.NEIGHBOURS25){
            int cell = Dungeon.hero.pos + i;
            if (Dungeon.level.passable[cell] && Char.findChar(cell)==null) abitclosecells.add(cell);
        }
        if (!veryclosecells.isEmpty()){
            summonCutieBeautyPieDarty(Random.element(veryclosecells));
        } else if (!abitclosecells.isEmpty()){
            summonCutieBeautyPieDarty(Random.element(abitclosecells));
        } else {
            Sample.INSTANCE.play(Assets.Sounds.DEGRADE, 1f, 0.8f);
            Dungeon.hero.speak(Messages.get(AbTrDartgunAlly.class, "no_space"), CharSprite.MYSTERIOUS);
        }

    }
    private void summonCutieBeautyPieDarty(int cell){
        TowerDartgun1 darto = new TowerDartgun1();
        Buff.affect(darto, Animated.class);
        darto.sellable = false;
        darto.pos = cell;
        GameScene.add(darto);
        Dungeon.level.occupyCell(darto);
        CellEmitter.floor(cell).burst(Speck.factory(Speck.HEART), 5);
    }

    @Override
    protected int castCooldown() {
        return Math.max(50, 200 - (int)Math.pow(Dungeon.depth, 2));
    }
}
