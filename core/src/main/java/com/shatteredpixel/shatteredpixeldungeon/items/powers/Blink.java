package com.shatteredpixel.shatteredpixeldungeon.items.powers;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;

public class Blink extends ActivatedPower {

    {
        image = ItemSpriteSheet.BLINK;
        mp_cost = 2;
    }

    @Override
    public boolean usesTargeting() {
        return true;
    }

    @Override
    public void affectCell(int pos) {

    }

    @Override
    public void onZap(Ballistica shot) {
        int level = curUser.lvl;

        int cell = shot.collisionPos;

        int dist = shot.dist;
        if (shot.dist > level/5 + 4) {
            dist = level + 4;
        } else if (Actor.findChar(cell) != null) {
            dist--;
        }
        shot = new Ballistica(shot.sourcePos, shot.path.get(dist), Ballistica.PROJECTILE);

        cell = shot.collisionPos;

        curUser.sprite.visible = true;
        appear(Dungeon.hero, cell);
        Dungeon.level.occupyCell(curUser);
        Dungeon.observe();
    }

    public static void appear(Char ch, int pos) {

        ch.sprite.interruptMotion();

        ch.move(pos);
        ch.sprite.place(pos);

        if (ch.invisible == 0) {
            ch.sprite.alpha(0);
            ch.sprite.parent.add(new AlphaTweener(ch.sprite, 1, 0.4f));
        }

        ch.sprite.emitter().start(Speck.factory(Speck.LIGHT), 0.2f, 3);
        Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
    }
}
