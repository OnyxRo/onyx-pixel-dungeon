package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Battlecry;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TowerGraveEliteSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SkeletonWarriorSprite;

public class TowerGraveElite extends TowerGrave3{



    {
        HP = HT = 350;

        spriteClass = TowerGraveEliteSprite.class;

        maxMinions = 2;
        minionCooldown = 50;
        minionCooldownLeft = 1;

        minionHP = 400;
        minionDamageMax = 25;
        minionDamageMin = 20;
        minionAttackSkill = 10;
        minionDefenseSkill = 0;
        minionDR = 6;

        cost = 2500;
        upgradeLevel = 11;
        upgCount=0;
    }

    @Override
    public void spawnMinion(int pos) {
        SkeletonWarriorMinion minion = new SkeletonWarriorMinion();
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

    public class SkeletonWarriorMinion extends SkeletonMinion1 {

        {
            spriteClass = SkeletonWarriorSprite.class;
            viewDistance = 6;
        }

        public SkeletonWarriorMinion() {
            super();
            Buff.affect(this, Battlecry.class, 30);
        }
    }

}