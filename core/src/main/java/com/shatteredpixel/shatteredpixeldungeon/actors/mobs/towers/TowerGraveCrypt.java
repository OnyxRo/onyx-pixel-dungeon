package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TowerGraveCryptSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WraithSprite;

public class TowerGraveCrypt extends TowerCSpawning{

    {
        HP = HT = 150;

        spriteClass = TowerGraveCryptSprite.class;

        maxMinions = 5;
        minionCooldown = 5;
        minionCooldownLeft = 1;

        minionHP = 1;
        minionDamageMax = 16;
        minionDamageMin = 13;
        minionAttackSkill = 15;
        minionDefenseSkill = 12;
        minionDR = 0;

        cost = 2000;

        upgrade1Cost = 1000;
        upgrade2Cost = 1500;

        upgradeLevel = 3;
        upgCount=0;

        spawnsExcessMinions = false;


    }
    @Override
    public void spawnMinion(int pos) {
        WraithMinion minion = new WraithMinion();
        minion.pos = pos;

        Dungeon.level.occupyCell(minion);
        minion.state = minion.WANDERING;

        minion.HT = minion.HP = minionHP;
        minion.minionDamageMax= minionDamageMax;
        minion.minionDamageMin= minionDamageMin;
        minion.minionAttackSkill= minionAttackSkill;
        minion.minionDefenseSkill= minionDefenseSkill;
        minion.minionDR= minionDR;

        minion.alignment = alignment;
        minion.momID = id();
        minion.momTower = this;

        addMinion(minion);
        GameScene.add(minion);

        CellEmitter.get(this.pos).burst(ShadowParticle.UP, 5);
        CellEmitter.get(minion.pos).burst(ShadowParticle.UP, 5);
    }

    public static class WraithMinion extends TowerCSpawningMinion{
        {
            spriteClass = WraithSprite.class;

            diesWithoutMomtower = false;
            staysNearMomTower = false;
            flying = true;
        }

        public WraithMinion() {
            super();
        }
    }

}