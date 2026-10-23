package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.BAHero;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Daze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.BAGun;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class BASG extends BAGun {
    //근접 이외에 탄환이 맞지 않음. Hero.attackSkill()참고
    //탄환 기습 불가. Hero.canSurpriseAttack()참고
    private boolean throwAway = false;

    {
        max_round = 1;
        reload_time = 1f;
        round = max_round;
        shotPerShoot = 5;
        shootingAccuracy = 1.5f;
    }

    private static final String THROW_AWAY = "throwAway";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(THROW_AWAY, throwAway);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        throwAway = bundle.getBoolean(THROW_AWAY);
    }

    public static final String AC_THROWAWAY = "THROWAWAY";

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (isEquipped( hero )) {
            if (hero.hasTalent(Talent.BA_TACTICAL_PRESS_3)) {
                actions.add(AC_THROWAWAY);
            }
        }
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        if (action.equals(AC_RELOAD)) {
            if (hero.heroClass == HeroClass.BA_HOSHINO) {
                if (round == maxRound()+1) {
                    GLog.w(Messages.get(BAGun.class, "already_loaded"));
                    return;
                } else if (isAllLoaded()){
                    manualReload(1, true);
                    hero.busy();
                    hero.sprite.operate(hero.pos);
                    Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
                    hero.spendAndNext(reloadTime());
                    BAHero.yellP(hero, Messages.get(Hero.class, hero.heroClass.name().replace("BA_", "") + "_over_reload"));
                    return;
                } else {
                    reload();
                    return;
                }
            } else {
                reload();
                return;
            }
        }
        if (action.equals(AC_THROWAWAY)) {
            throwAway = !throwAway;
            hero.busy();
            hero.sprite.operate(hero.pos);
            Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
            if (throwAway) {
                BAHero.yellP(hero, Messages.get(Hero.class, hero.heroClass.name().replace("BA_", "") + "_throw_away_on"));
            } else {
                BAHero.yellP(hero, Messages.get(Hero.class, hero.heroClass.name().replace("BA_", "") + "_throw_away_off"));
            }
        }
        super.execute(hero, action);
    }

    @Override
    public int bulletMax(int lvl) {
        return  (tier+1) +
                Math.round(0.5f * lvl * (tier+1)) + //2강 당 2/3/4/5/6 증가
                RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
    }

    @Override
    public int STRReq(int lvl) {
        int req = super.STRReq(lvl);
        if (hero.hasTalent(Talent.BA_SG_MASTER) && this.tier <= 3) {
            req -= hero.pointsInTalent(Talent.BA_SG_MASTER);
        }
        return req;
    }

    @Override
    public int shotPerShoot() { //발사 당 탄환의 수
        int amount = super.shotPerShoot();
        if (hero.hasTalent(Talent.BA_ADDITIONAL_SHOT)) amount += hero.pointsInTalent(Talent.BA_ADDITIONAL_SHOT);
        return amount;
    }

    @Override
    public float reloadTime() { //재장전에 소모하는 턴
        float amount = super.reloadTime();

        amount *= 1 - (0.1f*hero.pointsInTalent(Talent.BA_SG_FAST_RELOAD));

        return amount;
    }

    @Override
    public Bullet knockBullet(){
        return new SGBullet();
    }

    public class SGBullet extends Bullet {
        {
            image = ItemSpriteSheet.BA_TRIPLE_BULLET;
        }

        @Override
        public void cast(final Hero user, final int dst) {
            if (hero.subClass == HeroSubClass.BA_HOSHINO_EX_TACTICAL_PRESS) {
                int cell = throwPos( user, dst );
                tacticalPress(cell);
            } else {
                super.cast(user, dst);
            }
        }

        @Override
        public int proc(Char attacker, Char defender, int damage) {
            if (hero.hasTalent(Talent.BA_TACTICAL_SHIELD_1)) {
                if (Random.Float() < 0.03f * hero.pointsInTalent(Talent.BA_TACTICAL_SHIELD_1)) {
                    Buff.prolong(defender, Paralysis.class, 2);
                }
            }

            return super.proc(attacker, defender, damage);
        }

        private void tacticalPress(int cell) {
            Ballistica aim = new Ballistica(hero.pos, cell, Ballistica.STOP_SOLID);

            int maxDist = 2;
            if (Random.Int(3) < hero.pointsInTalent(Talent.BA_TACTICAL_PRESS_1)) maxDist++;
            int dist = Math.min(aim.dist, maxDist);

            ArrayList<Char> affected = new ArrayList<>();
            if (cell != hero.pos) {
                for (int c : aim.subPath(1, dist)) {
                    CellEmitter.get(c).burst(SmokeParticle.FACTORY, 10);
                    CellEmitter.center(c).burst(BlastParticle.FACTORY, 3);
                    Char ch = Actor.findChar(c);
                    if (ch != null && ch.alignment != hero.alignment){
                        affected.add(ch);
                    }
                }
            } else {
                BAHero.yellW(hero, Messages.get(Hero.class, hero.heroClass.name().replace("BA_", "") + "_cannot_self"));
                return;
            }
            Invisibility.dispel();
            hero.sprite.zap(cell);
            float multi = 1f;

            for (Char ch : affected) {
                int hit = 0;
                for (int i=0; i<shotPerShoot(); i++) {
                    if (curUser.shoot(ch, SGBullet.this, multi, 0, multi)) {
                        if (hero.hasTalent(Talent.BA_TACTICAL_PRESS_2) && Random.Float() < 0.1f) {
                            Buff.prolong(ch, Daze.class, 2*hero.pointsInTalent(Talent.BA_TACTICAL_PRESS_2));
                        }
                        hit ++;
                    }
                }

                if (hero.hasTalent(Talent.BA_TACTICAL_PRESS_3) && throwAway ) {
                    Ballistica trajectory = new Ballistica(hero.pos, ch.pos, Ballistica.STOP_TARGET);
                    trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size()-1), Ballistica.PROJECTILE);
                    WandOfBlastWave.throwChar(ch,
                            trajectory,
                            hit*hero.pointsInTalent(Talent.BA_TACTICAL_PRESS_3),
                            false,
                            true,
                            SGBullet.this);
                }
            }

            hero.spendAndNext(delayFactor(hero));
            updateQuickslot();
            round --;
            Sample.INSTANCE.play( Assets.Sounds.HIT_CRUSH, 1, Random.Float(0.33f, 0.66f) );
        }
    }
}
