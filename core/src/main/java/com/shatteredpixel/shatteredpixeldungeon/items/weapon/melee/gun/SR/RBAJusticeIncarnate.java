package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.RBAJusticeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.RBAGun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.RBASpecialGun;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class RBAJusticeIncarnate extends RBASR implements RBASpecialGun {
    {
        image = ItemSpriteSheet.RBA_JUSTICE_INCARNATE;
        tier = 5;
    }

    public float justiceChance(int lvl) {
        return (3f+lvl)/(30f+lvl);
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
        return new JusticeIncarnateBullet();
    }

    public class JusticeIncarnateBullet extends SRBullet {
        @Override
        public int proc(Char attacker, Char defender, int damage) {
            int finalDmg = super.proc(attacker, defender, damage);
            if (Random.Float() < justiceChance(buffedLvl())) {
                defender.damage(finalDmg, this);
                CellEmitter.heroCenter(defender.pos).burst(RBAJusticeParticle.factory(), 1);
            }
            return finalDmg;
        }
    }

    public static class Recipe extends BaseRecipe {

        @Override
        public Class<? extends RBAGun> ingredients() {
            return RBASR.class;
        }

        @Override
        public Class<? extends RBAGun> result() {
            return RBAJusticeIncarnate.class;
        }
    }
}
