package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.RBAAR;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.RBAGun;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class RBAGL extends RBAGun {

    {
        max_round = 2;
        round = max_round;
        explode = true;
    }

    @Override
    public int STRReq(int lvl) {
        int req = super.STRReq(lvl);
        if (Dungeon.hero != null && Dungeon.hero.heroClass == HeroClass.RBA_YUZU){
            req -= 1;
        }
        return req;
    }

    @Override
    public int baseBulletMax(int lvl) {
        return 6 * (tier() + 1) +
                lvl * (tier() + 1);
    }

    @Override
    public Bullet knockBullet(){
        return new GLBullet();
    }

    public class GLBullet extends Bullet {
        {
            image = ItemSpriteSheet.RBA_GRENADE_RED;
        }
    }

    @Override
    public PlaceHolder getPlaceHolder() {
        return new PlaceHolder();
    }

    public static class PlaceHolder extends RBAGun.PlaceHolder {
        {
            image = ItemSpriteSheet.RBA_GL_PLACEHOLDER;
        }

        @Override
        public boolean isSimilar(Item item) {
            return super.isSimilar(item) && item instanceof RBAGL;
        }
    }

}
