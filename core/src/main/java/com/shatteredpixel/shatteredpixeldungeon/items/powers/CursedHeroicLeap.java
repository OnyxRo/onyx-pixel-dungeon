package com.shatteredpixel.shatteredpixeldungeon.items.powers;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.Camera;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

public class CursedHeroicLeap extends ActivatedPower {

    {
        image = ItemSpriteSheet.HEROICLEAP;
        mp_cost = 5;
    }

    private static int SHOCK_TIME	= 3;

    @Override
    public boolean usesTargeting() {
        return true;
    }

    @Override
    public void affectCell(int pos) {
        final int dest = pos;
        curUser.busy();
        curUser.sprite.jump(curUser.pos, pos, new Callback() {
            @Override
            public void call() {
                curUser.move(dest);
                Dungeon.level.occupyCell( curUser );
                Dungeon.observe();
                GameScene.updateFog();

                for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                    Char mob = Actor.findChar(curUser.pos + PathFinder.NEIGHBOURS8[i]);
                    if (mob != null && mob != curUser) {
                        Buff.prolong(mob, Paralysis.class, SHOCK_TIME);
                    }
                }

                CellEmitter.center(dest).burst(Speck.factory(Speck.DUST), 10);
                Camera.main.shake(2, 0.5f);
            }
        });
    }

    @Override
    public void fx(Ballistica shot, Callback callback) {
        callback.call();//No zapping animation, animation is handled in affectCell.
    }
}
