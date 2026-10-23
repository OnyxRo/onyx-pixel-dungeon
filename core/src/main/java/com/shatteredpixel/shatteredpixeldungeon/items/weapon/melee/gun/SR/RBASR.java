package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.RBAGun;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class RBASR extends RBAGun {

    {
        max_round = 2;
        round = max_round;
        reload_time = 3f;
        shootingAccuracy = 1.5f;
    }

    @Override
    public int STRReq(int lvl) {
        int req = super.STRReq(lvl);
        if (Dungeon.hero != null && Dungeon.hero.heroClass == HeroClass.RBA_MIYU){
            req -= 1;
        }
        return req;
    }

    @Override
    protected int baseBulletMin(int lvl) {
        return super.baseBulletMin(lvl)*2;
    }

    @Override
    public int baseBulletMax(int lvl) {
        return 5 * (tier()+2) +
                lvl * (tier()+2);
    }

    @Override
    public Bullet knockBullet(){
        return new SRBullet();
    }

    public class SRBullet extends Bullet {
        {
            image = ItemSpriteSheet.RBA_SNIPER_BULLET;
        }
    }

    @Override
    public PlaceHolder getPlaceHolder() {
        return new PlaceHolder();
    }

    public static class PlaceHolder extends RBAGun.PlaceHolder {
        {
            image = ItemSpriteSheet.RBA_SR_PLACEHOLDER;
        }

        @Override
        public boolean isSimilar(Item item) {
            return super.isSimilar(item) && item instanceof RBASR;
        }
    }
}
