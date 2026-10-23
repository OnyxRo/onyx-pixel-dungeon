package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.RBAGun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.RBASpecialGun;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class RBAMTET extends RBASG implements RBASpecialGun { //Multipurpose Tactical Enforcement Tool
    {
        tier = 4;
        image = ItemSpriteSheet.RBA_MTET;
    }

    @Override
    protected int baseBulletMin(int lvl) {
        return tier() - 1 + lvl;
    }

    @Override
    public int baseBulletMax(int lvl) {
        return  (tier()+2) +
                Math.round(0.5f * lvl * (tier()-1)); //3티어 성능
    }

    @Override
    public Bullet knockBullet() {
        return new MTETBullet();
    }

    public class MTETBullet extends SGBullet {
        @Override
        public int proc(Char attacker, Char defender, int damage) {
            float procChance = (buffedLvl()+1f)/(buffedLvl()+3f);
            if (Random.Float() < procChance) {

                float powerMulti = Math.max(1f, procChance);

                if (defender.buff(Burning.class) == null){
                    Buff.affect(defender, Burning.class).reignite(defender, 8f);
                    powerMulti -= 1;
                }

                if (powerMulti > 0){
                    int burnDamage = Random.NormalIntRange( 1, 3 + Dungeon.scalingDepth()/4 );
                    burnDamage = Math.round(burnDamage * 0.67f * powerMulti);
                    if (burnDamage > 0) {
                        defender.damage(burnDamage, new Burning());
                    }
                }

                defender.sprite.emitter().burst( FlameParticle.FACTORY, buffedLvl() + 1 );

            }

            return super.proc(attacker, defender, damage);
        }
    }

    public static class Recipe extends RBASpecialGun.BaseRecipe {

        @Override
        public Class<? extends RBAGun> ingredients() {
            return RBASG.class;
        }

        @Override
        public Class<? extends RBAGun> result() {
            return RBAMTET.class;
        }
    }
}
