package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class ShinobiSprite extends MobSprite {
    private Animation cast;

    public ShinobiSprite() {
        super();

        texture(Assets.Sprites.SHINOBI);

        TextureFilm frames = new TextureFilm(texture, 14, 16);

        idle = new Animation(2, true);
        idle.frames(frames, 0, 0, 0, 1);

        run = new Animation(15, false);
        run.frames(frames, 2, 3, 4, 5, 0);

        attack = new Animation(15, false);
        attack.frames(frames, 6, 7, 7, 0);

        cast = attack.clone();

        die = new Animation(8, false);
        die.frames(frames, 8, 9, 10, 10);

        play(run.clone());
    }


    @Override
    public void attack(int cell) {
        if (!Dungeon.level.adjacent(cell, ch.pos)) {
            ((MissileSprite)parent.recycle( MissileSprite.class )).
                    reset( ch.pos, cell, new TenguSprite.TenguShuriken(), new Callback() {
                        @Override
                        public void call() {
                            ch.onAttackComplete();
                        }
                    } );

            play( cast );
            turnTo( ch.pos , cell );

        } else {

            super.attack(cell);

        }
    }

    @Override
    public void onComplete(Animation anim) {
        if (anim == run) {
            isMoving = false;
            idle();
        } else {
            super.onComplete(anim);
        }
    }
}

