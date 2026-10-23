package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR;



import com.shatteredpixel.shatteredpixeldungeon.RBAShims;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.RBAHero;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.miyu.RBAAntiMaterialRifle;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.shiroko.RBAPenetrationShot;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.RBAGun;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class RBASR_SP extends RBASR {
    public RBAGun originalGun;

    {
        image = ItemSpriteSheet.RBA_SR_SPECIAL;

        tier = 6;
        bones = false;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.remove(AC_THROW);
        return actions;
    }

    @Override
    public int STRReq(int lvl) {
        return super.STRReq(lvl)-2;
    }

    @Override
    public void reload() {
        doUnequip(Dungeon.hero, false, false);
        Dungeon.hero.belongings.weapon = originalGun;
    }

    @Override
    public void useRound() {
        super.useRound();
        if (round == 0) {
            changeWeapon(Dungeon.hero, true);
            Dungeon.hero.belongings.weapon = originalGun;
        }
    }

    @Override
    public void doDrop(Hero hero) {
        changeWeapon(hero, true);
        Dungeon.level.drop(originalGun, hero.pos).sprite.drop();
    }

    @Override
    public boolean doUnequip(Hero hero, boolean collect, boolean single) {
        changeWeapon(hero, true);
        if (collect) {
            if (!originalGun.collect(hero.belongings.backpack)) {
                Dungeon.level.drop(originalGun, hero.pos).sprite.drop();
            }
        };
        return true;
    }

    @Override
    protected void onThrow(int cell) {
        super.onThrow(cell);
    }

    public void changeWeapon(Hero hero, boolean changeQuickslot) {
        RBAHero.yellI(hero, "switching_original");
        originalGun.keptThoughLostInvent = this.keptThoughLostInvent;
        hero.belongings.weapon = null;
        if (Dungeon.hero.buff(RBAAntiMaterialRifle.GotRifleTracker.class) != null) Dungeon.hero.buff(RBAAntiMaterialRifle.GotRifleTracker.class).detach();
        int slot = Dungeon.quickslot.getSlot(this);
        if (changeQuickslot) {
            if (slot != -1
                    && originalGun.defaultAction() != null){
                Dungeon.quickslot.setSlot(slot, originalGun);
            }
        } else {
            if (slot != -1
                    && originalGun.defaultAction() != null){
                Dungeon.quickslot.clearSlot(slot);
            }
        }

        updateQuickslot();
    }

    public void set(RBAGun gun) {
        this.originalGun = gun;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public int value() {
        return -1;
    }

    private static final String ORIGINAL_GUN = "originalGun";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(ORIGINAL_GUN, originalGun);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        originalGun = (RBAGun) bundle.get(ORIGINAL_GUN);
    }

    @Override
    public Bullet knockBullet() {
        return new SR_SP_Bullet();
    }

    public class SR_SP_Bullet extends Bullet {
        {
            image = ItemSpriteSheet.RBA_SNIPER_BULLET;
        }

        boolean wallPenetration = false;

        @Override
        public int throwPos(Hero user, int dst) {
            return RBAPenetrationShot.finalPos(new Ballistica(user.pos, dst, RBAShims.WALL_PENETRATION), 1);
        }

        @Override
        public int proc(Char attacker, Char defender, int damage) {
            if (!wallPenetration) {
                Ballistica trajectory = new Ballistica(attacker.pos, defender.pos, Ballistica.STOP_TARGET);
                trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size() - 1), Ballistica.PROJECTILE);
                int power = 4 + 2 * Dungeon.hero.pointsInTalent(Talent.RBA_MIYU_ARMOR3_1);
                WandOfBlastWave.throwChar(defender, trajectory, power, false, true, this);
            }
            if (Dungeon.hero.hasTalent(Talent.RBA_MIYU_ARMOR3_1)) {
                Buff.affect(defender, Vulnerable.class, 5f*Dungeon.hero.pointsInTalent(Talent.RBA_MIYU_ARMOR3_1));
            }
            return super.proc(attacker, defender, damage);
        }

        @Override
        protected void onThrow(int cell) {
            Ballistica path = new Ballistica(curUser.pos, cell, RBAShims.WALL_PENETRATION);
            for (int c : path.path) {
                if (c == path.collisionPos) break;
                if (Dungeon.level.solid[c]) {
                    if (Dungeon.level.heroFOV[ c ]){
                        CellEmitter.get( c - Dungeon.level.width() ).start(Speck.factory(Speck.ROCK), 0.07f, 10);
                    }
                    Level.set(c, Terrain.EMPTY);
                    for (int i : PathFinder.NEIGHBOURS9) {
                        Dungeon.level.discoverable[c+i] = true;
                    }

                    GameScene.updateMap(c);

                    wallPenetration = true;
                }
            }
            if (wallPenetration) {
                Dungeon.observe();
                Sample.INSTANCE.play(Assets.Sounds.ROCKS);
            }
            super.onThrow(cell);
        }
    }
}
