package com.shatteredpixel.shatteredpixeldungeon.items.herospells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Strength;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAggression;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class AbTaunt extends HeroSpell {
    {
        image = ItemSpriteSheet.HEROSPELL_TAUNT;
    }

    @Override
    public void cast() {
        super.cast();
        Buff.affect(Dungeon.hero, StoneOfAggression.Aggression.class, 12);
        curUser.sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );
        Sample.INSTANCE.play(Assets.Sounds.READ, 1f, 0.5f);
    }

    @Override
    protected int castCooldown() {
        if (Dungeon.level==null) return 200;
        return 75 + (Dungeon.level.width()+ Dungeon.level.height())/2;
    }
}
