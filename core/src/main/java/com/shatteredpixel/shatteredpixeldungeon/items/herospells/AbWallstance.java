package com.shatteredpixel.shatteredpixeldungeon.items.herospells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WallStance;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class AbWallstance extends HeroSpell{
    {
        image = ItemSpriteSheet.HEROSPELL_WALLSTANCE;
    }

    @Override
    public void cast() {
        super.cast();
        Buff.affect(Dungeon.hero, WallStance.class, 20);
        Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY);
        Dungeon.hero.sprite.operate(Dungeon.hero.pos);
    }

    @Override
    protected int castCooldown() {
        if (Dungeon.level == null) return 100;
        return 50 + (Dungeon.level.width()+ Dungeon.level.height())/2;
    }
}
