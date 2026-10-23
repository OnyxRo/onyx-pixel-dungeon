package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.RBAGun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.RBASpecialGun;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class RBAWineRedAdmire extends RBASR implements RBASpecialGun {
    {
        tier = 5;
        image = ItemSpriteSheet.RBA_WINE_RED_ADMIRE;
    }

    public float explosionChance(int lvl) {
        return (2f+lvl)/(20f+lvl);
    }

    @Override
    protected int baseBulletMin(int lvl) {
        return (tier() - 1 + lvl)*2;
    }

    @Override
    public int baseBulletMax(int lvl) {
        return  5 * (tier()+1) +
                lvl * (tier()+1); //4티어 성능
    }

    @Override
    public Bullet knockBullet() {
        return new WineRedAdmireBullet();
    }

    public class WineRedAdmireBullet extends SRBullet {
        @Override
        protected void onThrow(int cell) {
            super.onThrow(cell);
            if (Dungeon.level.distance(Dungeon.hero.pos, cell) > 2 && Random.Float() < explosionChance(buffedLvl())) {
                for (int i : PathFinder.NEIGHBOURS4) {
                    int c = cell+i;
                    if (!Dungeon.level.solid[c] || Dungeon.level.passable[c]) new WineRedAdmireBomb().explode(c);
                }
            }
        }
    }

    public static class WineRedAdmireBomb extends Bomb.ConjuredBomb {

        {
            maxDamage = 8 + 2*Dungeon.scalingDepth();
        }

        @Override
        public boolean destroyItem() {
            return false;
        }
    }

    public static class Recipe extends RBASpecialGun.BaseRecipe {

        @Override
        public Class<? extends RBAGun> ingredients() {
            return RBASR.class;
        }

        @Override
        public Class<? extends RBAGun> result() {
            return RBAWineRedAdmire.class;
        }
    }
}
