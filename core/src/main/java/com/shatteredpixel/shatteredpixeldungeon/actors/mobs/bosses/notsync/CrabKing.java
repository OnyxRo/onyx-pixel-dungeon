package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.notsync;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrabKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class CrabKing extends Boss {

    {
        initProperty();
        initBaseStatus(5, 15, 14, 0, 100, 0, 6);
        initStatus(30);
        spriteClass = CrabKingSprite.class;
    }

    //magicling, the mod gave the Crab King only its stats. Its Reloop Life buff text says it comes back twice,
    //so that is written here, along with the health bar, the sealed fight and the unlocked stairs on death
    private int revives = 0;

    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            Dungeon.level.seal();
            GameScene.bossReady();
        }
    }

    @Override
    public void damage( int dmg, Object src ) {
        if (dmg >= HP && revives < 2 && buff(ReloopLife.class) != null) {
            revives++;
            HP = HT;
            if (sprite != null) {
                sprite.emitter().burst( Speck.factory( Speck.HEALING ), 6 );
                sprite.showStatus( CharSprite.POSITIVE, Messages.get(ReloopLife.class, "name") );
            }
            if (revives >= 2) buff(ReloopLife.class).detach();
            return;
        }
        super.damage( dmg, src );
    }

    @Override
    public void die( Object cause ) {
        super.die( cause );
        Dungeon.level.unseal();
        GameScene.bossSlain();
        Badges.validateBossSlain();
    }

    private static final String REVIVES = "revives";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( REVIVES, revives );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        revives = bundle.getInt( REVIVES );
    }

    public static class ReloopLife extends FlavourBuff {

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0x00FFFF);
        }

        @Override
        public int icon() {
            return BuffIndicator.RECHARGING;
        }
    }

}
