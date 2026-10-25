package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.FiveYearsNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears.PinkLingNormalPlot;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;

public class PinkLing extends FiveYearsNPC {

    {
        spriteClass = PinkLingSprite.class;
        plot1 = new PinkLingNormalPlot();
    }

    @Override
    public boolean interact(Char c) {
        sprite.turnTo( pos, c.pos );
        if(first){
            Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot1,false)));
            first = false;
        }
        return true;
    }

}
