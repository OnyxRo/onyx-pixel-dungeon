package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
public class BAQuestEnemy extends Buff {

    {
        type = buffType.POSITIVE;
    }

    //how many monsters until the next quest enemy, Blue Archive kept this in Dungeon (not saved here, it only counts to 8)
    public static int mobsToQuest = -1;

    protected int color = 0xFFFFFF;

    @Override
    public int icon() {
        return BuffIndicator.CORRUPT;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(color);
    }

    @Override
    public void fx(boolean on) {
        if (on) target.sprite.aura( color, 4 );
        else target.sprite.clearAura();
    }

    {
        immunities.add(AllyBuff.class);
    }

    public static void rollForQuest(Mob m){
        if (BAQuestEnemy.mobsToQuest <= 0) BAQuestEnemy.mobsToQuest = 8;

        BAQuestEnemy.mobsToQuest--;

        if (BAQuestEnemy.mobsToQuest <= 0 && Dungeon.hero != null) {
            if (Dungeon.hero.heroClass == HeroClass.BA_YUZU) {
                Buff.affect(m, BAQuestEnemy.class);
                m.state = m.WANDERING;
            }
        }
    }

    @Override
    public void detach() {
        super.detach();
        Buff.affect(Dungeon.hero, BAQuestEnemy.QuestEnemyTracker.class);
    }

    public static class QuestEnemyTracker extends Buff {}

}
