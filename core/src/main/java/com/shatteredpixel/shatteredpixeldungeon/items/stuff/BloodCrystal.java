package com.shatteredpixel.shatteredpixeldungeon.items.stuff;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SoulBleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ChiefRat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RipperDemon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Succubus;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CeremonialCandle;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class BloodCrystal extends Item  {
    {
        image = ItemSpriteSheet.BLOOD_CRYSTAL;
        cursed = true;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    protected void onThrow(int cell) {
        if (CeremonialCandle.checkCellForSurroundingLitCandles(cell)){
            ArrayList<Integer> candidates = new ArrayList<>();
            for (int i : PathFinder.NEIGHBOURS25){
                int cel = cell + i;
                if (Char.findChar(cel)==null && Dungeon.level.passable[cel]) candidates.add(cel);
            }
            int power = 40 + Dungeon.depth*10;
            while (power > 0 && !candidates.isEmpty()){
                power -= 40;
                RipperDemon demon = new RipperDemon();
                demon.pos = Random.element(candidates);
                candidates.remove((Integer)demon.pos);
                demon.alignment = Char.Alignment.ALLY;
                demon.state = demon.HUNTING;
                CellEmitter.get(demon.pos).burst(FlameParticle.FACTORY, 7);
                GameScene.add(demon);
            }
            Buff.affect(Dungeon.hero, SoulBleeding.class).prolong(Dungeon.hero.HT/3);
            Sample.INSTANCE.play(Assets.Sounds.DEATH);
            CellEmitter.get(cell).start(FlameParticle.FACTORY, 0.02f,50);
            return;
        }
        super.onThrow(cell);
    }

    @Override
    public void doDrop(Hero hero) {
        super.doDrop(hero);
    }

    @Override
    public int value() {
        return 200;
    }
}
