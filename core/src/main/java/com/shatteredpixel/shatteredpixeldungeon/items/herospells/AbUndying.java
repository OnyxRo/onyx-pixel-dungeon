package com.shatteredpixel.shatteredpixeldungeon.items.herospells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Strength;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Undying;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class AbUndying extends HeroSpell {
    {
        image = ItemSpriteSheet.HEROSPELL_UNDYING;
    }
    @Override
    public void cast() {
        super.cast();
        Buff.affect(Dungeon.hero, Undying.class, 60 + Dungeon.hero.lvl*5);
        curUser.sprite.centerEmitter().start(ShadowParticle.CURSE, 0.3f, 3 );
        Sample.INSTANCE.play(Assets.Sounds.CURSED);
    }
    @Override
    protected int castCooldown() {
        return 80;
    }
}


