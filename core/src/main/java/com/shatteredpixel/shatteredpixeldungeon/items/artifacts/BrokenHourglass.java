package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class BrokenHourglass extends Artifact {

    {
        image = ItemSpriteSheet.ARTIFACT_BROKENHOURGLASS;
    }

    public int cost = 100 + Dungeon.depth * 20;

    private final String AC_FIX = "FIX";

    @Override
    public String info() {
        return desc();
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", cost);
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = new ArrayList<>();
        actions.add( AC_DROP );
        actions.add( AC_THROW );
        actions.add(AC_FIX);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {


            super.execute( hero, action );

            if (action.equals( AC_FIX)) {

                if (Dungeon.gold>=cost){
                    Dungeon.gold-=cost;
                    this.detach(Dungeon.hero.belongings.backpack);
                    Sample.INSTANCE.play(Assets.Sounds.LEVELUP);
                    GLog.w(Messages.get(this, "upgraded"));
                    new TimekeepersHourglass().collect();
                    updateQuickslot();
                } else {
                    GLog.w(Messages.get(this, "no_gold"));
                }

            }

    }
}
