package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.RBAGun;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class RBAAR extends RBAGun {

    {
        max_round = 4;
        round = max_round;
        shootingAccuracy = 1.2f;
    }

    @Override
    public int STRReq(int lvl) {
        int req = super.STRReq(lvl);
        if (Dungeon.hero != null && Dungeon.hero.heroClass == HeroClass.RBA_SHIROKO){
            req -= 1;
        }
        return req;
    }

    @Override
    public int baseBulletMax(int lvl) {
        return 4 * (tier() + 1) +
                lvl * (tier() + 1);
    }

    @Override
    public Bullet knockBullet(){
        return new ARBullet();
    }

    public class ARBullet extends Bullet {
        {
            image = ItemSpriteSheet.RBA_SINGLE_BULLET;
        }
    }

    @Override
    public PlaceHolder getPlaceHolder() {
        return new PlaceHolder();
    }

    public static class PlaceHolder extends RBAGun.PlaceHolder {
        {
            image = ItemSpriteSheet.RBA_AR_PLACEHOLDER;
        }

        @Override
        public boolean isSimilar(Item item) {
            return super.isSimilar(item) && item instanceof RBAAR;
        }
    }
}
