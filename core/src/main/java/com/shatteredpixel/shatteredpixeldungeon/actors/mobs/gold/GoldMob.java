package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold;

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

public abstract class GoldMob extends Mob {
    @Override
    public String info(){
        String desc = super.info();

        desc += "\n\n" + Messages.get(GoldMob.class,"infos");

        return desc;
    }

    protected static void GetSearch(){
        if(com.shatteredpixel.shatteredpixeldungeon.MagicLingStats.RandomQuest == 2){
            if(com.shatteredpixel.shatteredpixeldungeon.MagicLingStats.GoldMobDead>=15 && com.shatteredpixel.shatteredpixeldungeon.MagicLingStats.GoldMobLimit < 1){
                com.shatteredpixel.shatteredpixeldungeon.MagicLingStats.goldRefogreCount++;
                com.shatteredpixel.shatteredpixeldungeon.MagicLingStats.GoldMobDead = 0;
                com.shatteredpixel.shatteredpixeldungeon.MagicLingStats.GoldMobLimit++;
            } else if(com.shatteredpixel.shatteredpixeldungeon.MagicLingStats.GoldMobLimit == 0){
                com.shatteredpixel.shatteredpixeldungeon.MagicLingStats.GoldMobDead++;
            }
        }
    }

    @Override
    public void die( Object cause ) {
        super.die( cause );
        GetSearch();
    }
}
