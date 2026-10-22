package com.shatteredpixel.shatteredpixeldungeon.items.herospells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Strength;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class AbBerserk extends HeroSpell {
    {
        image = ItemSpriteSheet.HEROSPELL_RAGE;
    }

    @Override
    public void cast() {
        super.cast();
        int time = 15;
        if (Dungeon.hero.HP < Dungeon.hero.HT/2) time = 20;
        if (Dungeon.hero.HP < Dungeon.hero.HT/4) time = 30;
        Buff.affect(Dungeon.hero, Adrenaline.class, time);

        curUser.sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );
        Sample.INSTANCE.play(Assets.Sounds.CHALLENGE, 1f, 0.9f);
    }

    @Override
    protected int castCooldown() {
        int cooldown = 100 - Dungeon.hero.lvl*2;
        if (Dungeon.hero == null) return 100;
        return cooldown;
    }
}
