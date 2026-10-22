package com.shatteredpixel.shatteredpixeldungeon.items.herospells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Battlecry;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.TowerWisp;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.Tower;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.custom.CPRed;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class AbWisp extends HeroSpell {
    {
        image = ItemSpriteSheet.HEROSPELL_SUN;
    }

    @Override
    public void cast() {
        super.cast();
        TowerWisp wisp = new TowerWisp();
        wisp.HP = wisp.HT = Dungeon.hero.lvl*2;
        wisp.alignment = Char.Alignment.ALLY;
        wisp.pos = Dungeon.hero.pos;
        wisp.state = wisp.HUNTING;
        GameScene.add(wisp);
        Sample.INSTANCE.play(Assets.Sounds.HIT_MAGIC);
    }

    @Override
    protected int castCooldown() {
        return 25;
    }
}
