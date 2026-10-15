package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TenguSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;

public class Tengu2 extends Mob {
    {
        spriteClass = TenguSprite.class;

        HP = HT = 240;
        EXP = 20;
        defenseSkill = 20;

        HUNTING = new Hunting();

        flying = true; //doesn't literally fly, but he is fleet-of-foot enough to avoid hazards

        properties.add(Property.BOSS);
    }

    @Override
    public void notice() {
        super.notice();
        BossHealthBar.assignBoss(this);
        if (HP <= HT/2) BossHealthBar.bleed(true);
        if (HP == HT) {
            yell(Messages.get(this, "notice_mine", Dungeon.hero.name()));
        } else {
            yell(Messages.get(this, "notice_face", Dungeon.hero.name()));
        }
    }

    //tengu is always hunting
    private class Hunting extends Mob.Hunting{

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            enemySeen = enemyInFOV;
            if (enemyInFOV && !isCharmedBy( enemy ) && canAttack( enemy )) {

                return doAttack( enemy );

            } else {

                if (enemyInFOV) {
                    target = enemy.pos;
                } else {
                    chooseEnemy();
                    if (enemy != null) {
                        target = enemy.pos;
                    }
                }

                spend( TICK );
                return true;

            }
        }
    }
}
