package com.shatteredpixel.shatteredpixeldungeon.items.herospells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.ShadowClone;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class AbShadowclone extends HeroSpell {
    {
        image = ItemSpriteSheet.HEROSPELL_SHADOWCLONE;
    }

    @Override
    public void cast() {
        super.cast();
        ShadowClone.ShadowAlly myShadow = new ShadowClone.ShadowAlly();
        myShadow.HP = myShadow.HT = Dungeon.hero.HT;
        myShadow.pos = Dungeon.hero.pos;
        Sample.INSTANCE.play(Assets.Sounds.PUFF);
        GameScene.add(myShadow);
    }

    @Override
    protected int castCooldown() {
        return Math.max(125, 250 - Dungeon.depth*10);
    }
}
