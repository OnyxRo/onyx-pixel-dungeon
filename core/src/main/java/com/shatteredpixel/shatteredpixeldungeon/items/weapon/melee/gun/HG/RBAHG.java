package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.RBAGun;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class RBAHG extends RBAGun {

    {
        max_round = 4;
        round = max_round;
        shootingSpeed = 0.5f;
        reload_time = 1f;
    }

    @Override
    public int STRReq(int lvl) {
        int req = super.STRReq(lvl);
        if (Dungeon.hero != null && Dungeon.hero.heroClass == HeroClass.RBA_NOA){
            req -= 1;
        }
        return req;
    }

    @Override
    public int baseBulletMax(int lvl) {
        return 2 * (tier() + 1) +
                lvl * (tier() + 1);
    }

    @Override
    public Bullet knockBullet(){
        return new HGBullet();
    }

    public class HGBullet extends Bullet {
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
            image = ItemSpriteSheet.RBA_HG_PLACEHOLDER;
        }

        @Override
        public boolean isSimilar(Item item) {
            return super.isSimilar(item) && item instanceof RBAHG;
        }
    }
}
