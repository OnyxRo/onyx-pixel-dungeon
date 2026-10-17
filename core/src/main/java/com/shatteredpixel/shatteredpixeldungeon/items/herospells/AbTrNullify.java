package com.shatteredpixel.shatteredpixeldungeon.items.herospells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Overcharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerDisintegration1;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerWand1;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class AbTrNullify extends HeroSpell{

    {
        image = ItemSpriteSheet.HEROSPELL_TR_NULLIFY;
    }

    @Override
    public void cast() {
        super.cast();
        Sample.INSTANCE.play(Assets.Sounds.RAY);
        for (Mob mob : Dungeon.level.mobs){
            if (mob instanceof TowerDisintegration1){
                mob.sprite.emitter().burst(PurpleParticle.BURST, 10);
                ((TowerDisintegration1)mob).nullshots += 10;
            }
        }
    }

    @Override
    protected int castCooldown() {
        return 200;
    }
}
