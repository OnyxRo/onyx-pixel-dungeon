package com.shatteredpixel.shatteredpixeldungeon.items.herospells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Animated;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Overcharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerDartgun1;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerTntLog;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerWand1;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class AbTrPlanB extends HeroSpell{

    {
        image = ItemSpriteSheet.HEROSPELL_TR_PLANB;
    }

    @Override
    public void cast() {
        super.cast();
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        HashSet<Mob> mobsmoconcur = new HashSet<>(com.shatteredpixel.shatteredpixeldungeon.Dungeon.level.mobs);
        HashSet<Mob> mobsmoconcurfiltered = new HashSet<>();
        for (Mob mob : mobsmoconcur){
            if (mob instanceof TowerTntLog && mob.alignment == Char.Alignment.ALLY){
                mobsmoconcurfiltered.add(mob);
            }
        }
        for (Mob mob : mobsmoconcurfiltered){
            mob.die(this);
        }
    }

    @Override
    protected int castCooldown() {
        return 50;
    }
}
